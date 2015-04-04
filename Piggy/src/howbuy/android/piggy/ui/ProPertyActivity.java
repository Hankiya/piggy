package howbuy.android.piggy.ui;

import howbuy.android.lib.MyAsyncTask;
import howbuy.android.piggy.R;
import howbuy.android.piggy.UserUtil;
import howbuy.android.piggy.UserUtil.UserBankVeryType;
import howbuy.android.piggy.ad.AdTools;
import howbuy.android.piggy.ad.JpushOpera;
import howbuy.android.piggy.ad.PushDispatch;
import howbuy.android.piggy.api.DispatchAccessData;
import howbuy.android.piggy.api.dto.NoticeDto;
import howbuy.android.piggy.api.dto.NoticeDtoList;
import howbuy.android.piggy.api.dto.ProductInfo;
import howbuy.android.piggy.api.dto.UpdateDto;
import howbuy.android.piggy.api.dto.UserInfoDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.dialog.PiggyProgressDialog;
import howbuy.android.piggy.dialogfragment.UpdateDialog;
import howbuy.android.piggy.error.HowbuyException;
import howbuy.android.piggy.font.FontLoader;
import howbuy.android.piggy.help.AdHelp;
import howbuy.android.piggy.help.CacheHelp;
import howbuy.android.piggy.help.NoticeHelp;
import howbuy.android.piggy.help.ShareHelper;
import howbuy.android.piggy.loader.CheckUpdateLoader;
import howbuy.android.piggy.parameter.GlobalParams;
import howbuy.android.piggy.service.ServiceMger.TaskBean;
import howbuy.android.piggy.service.UpdateUserDataService;
import howbuy.android.piggy.ui.base.AbstractDoubleBackActivity;
import howbuy.android.piggy.ui.fragment.BindCardFragment;
import howbuy.android.piggy.ui.fragment.LoginFragment;
import howbuy.android.piggy.ui.fragment.PureWebFragment.PureType;
import howbuy.android.piggy.ui.fragment.QueryFragment;
import howbuy.android.piggy.widget.ImageTextBtn;
import howbuy.android.util.ActionViewProgressUtil;
import howbuy.android.util.AndroidUtil;
import howbuy.android.util.Arith;
import howbuy.android.util.Cons;
import howbuy.android.util.CpUtil;
import howbuy.android.util.IntentCommon;
import howbuy.android.util.NetTools;
import howbuy.android.util.SpannableUtil;
import howbuy.android.util.StringUtil;
import howbuy.android.util.http.UrlMatchUtil;

import java.math.BigDecimal;
import java.util.concurrent.Executors;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.howbuy.wireless.entity.protobuf.AdvertListProtos.AdvertList;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;

public class ProPertyActivity extends AbstractDoubleBackActivity implements OnClickListener, LoaderCallbacks<UpdateDto> {

	public static final String NAME = "ProPertyActivity";

	/** Request userInfo type, 1 with normal; 2 is cp **/
	public static final String MapIntentAD = "MapIntentAD";
	public static int NeeduserDataReload = 9999;
	private TextView cusmTitle;
	private LinearLayout cusmlay;
	private LinearLayout mNumAcMenuLay;
	private ImageButton mSafeBtn;
	private ActionViewProgressUtil actionViewUtil;
	private boolean animIsRuning;
	private GestureDetector mGestureDetector;
	private UserInfoDto userInfo;
	private ProductInfo productInfo;
	private String code;
	private String msg;
	private boolean isShowRefreshMenu;

	private ViewStub mStubLoginEd;
	private ViewStub mStubUnLogin;
	private RelativeLayout mLoginEdLay;
	private RelativeLayout mUnLoginLay;
	/** notify lay **/
	private RelativeLayout mNotifyLay;
	private TextView mNotifyMsg;
	private ImageView mNotifyCancleBtn;
	/** logined **/
	private TextView mIncomeNewTitle;
	private TextView mIncomeNew;
	private ImageView mIncomeNewShare;
	private ImageView mIncomeNewClick;
	private TextView mProperty;
	private TextView mIncomeAll;
	private TextView mQrnh;
	private TextView mQrnhTitle;
	private TextView mWsfy;
	private TextView mWsfyTitle;
	private RelativeLayout mQueryLay;
	private RelativeLayout mEventsLay;
	private TextView mQueryHint;
	private TextView mEventHint;
	private ImageTextBtn mSavaMoneyBtn, mOutMoneyBtn;
	private LinearLayout mButtomLay;
	/** unlogin **/
	private TextView mDoubleViewUnlgn1;
	private LinearLayout mBeiLayout;
	private TextView mQrnhUnLgn;
	private TextView mUesrNum;
	private ImageTextBtn mStartBtnUnlgn;
	private ImageView mUnLoginAdImg;

	private boolean mCurrLoginFlag;
	static boolean fromOutMoneyBtn = false;
	static boolean fromSaveMoneyBtn = false;
	private int intoType;
	private String JPUSHBUNDLE = "jpushBundle";
	public static Bundle jPushBundle = null;
	private PiggyProgressDialog mpDialog;
	private boolean isFirstResume = true;
	private int mShareType;

	private NoticeHelp mNoticeHelp;
	private AdHelp mAdHelp;

	DialogInterface.OnClickListener mDlgClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			mShareType = which;
			String title = "朋友，推荐你一款会赚钱的储蓄罐";
			String content = getShareContentString();
			String contentUrl = UrlMatchUtil.urlSimple("activity/homeshare.htm?corpId=000004");
			Object bmp = R.drawable.ic_launcher_share;
			switch (which) {
			case 0:
				// content = "【" + title + "】" + content + " " + contentUrl;
				break;
			case 1:
				// 调换
				title = content;
				content = "储蓄罐去年同类产品收益冠军，上半年年化收益超5%。会赚钱的储蓄罐，活期存款增值快。";
				break;
			case 2:
				content = content + " " + contentUrl;
				break;
			case 3:
				content = "朋友，推荐你一款会赚钱的储蓄罐。储蓄罐去年同类产品收益冠军，上半年年化收益超5%，储蓄罐下载地址:" + contentUrl;
				break;
			default:
				content = "【" + title + "】" + content + " " + contentUrl;
			}
			ShareHelper.share(ProPertyActivity.this, which, title, content, contentUrl, bmp, snsPostListener, "新闻");
		}
	};

	SnsPostListener snsPostListener = new SnsPostListener() {

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			Log.d(NAME, "start share");
		}

		@Override
		public void onComplete(SHARE_MEDIA arg0, int arg1, SocializeEntity arg2) {
			// TODO Auto-generated method stub
			Log.d(NAME, "SHARE_MEDIA=" + arg0);
			Log.d(NAME, "arg1=" + arg1);
			Log.d(NAME, "SocializeEntity=" + arg2);
		}
	};

	private void initStubView() {
		mStubLoginEd = (ViewStub) findViewById(R.id.activity_property_logined_sub);
		mStubUnLogin = (ViewStub) findViewById(R.id.activity_property_unlogin_sub);
	}

	private void srueThisIsInit(boolean isLogin) {
		if (isLogin) {
			if (mLoginEdLay == null) {
				findAllView();
			}
		} else {
			if (mUnLoginLay == null) {
				findAllView();
			}
		}
	}

	private void findAllView() {
		Log.d(NAME, "findAllView");
		if (UserUtil.isLogin()) {
			mLoginEdLay = (RelativeLayout) mStubLoginEd.inflate();
			mIncomeNewTitle = (TextView) findViewById(R.id.income_new_title);
			mIncomeNew = (TextView) findViewById(R.id.income_new);
			mIncomeNewClick = (ImageView) findViewById(R.id.income_new_click);
			mIncomeNewShare = (ImageView) findViewById(R.id.income_new_share);
			mProperty = (TextView) findViewById(R.id.property_all);
			mIncomeAll = (TextView) findViewById(R.id.income_all);
			mQrnhTitle = (TextView) findViewById(R.id.qrnh_title);
			mQrnh = (TextView) findViewById(R.id.qrnh);
			mWsfyTitle = (TextView) findViewById(R.id.wfsy_title);
			mWsfy = (TextView) findViewById(R.id.wfsy);
			mQueryLay = (RelativeLayout) findViewById(R.id.query_lay);
			mQueryHint = (TextView) findViewById(R.id.query_hint);
			mEventsLay = (RelativeLayout) findViewById(R.id.events_lay);
			mEventHint = (TextView) findViewById(R.id.events_hint);
			mSavaMoneyBtn = (ImageTextBtn) findViewById(R.id.savemoneybtn);
			mOutMoneyBtn = (ImageTextBtn) findViewById(R.id.outmoneybtn);
			mButtomLay = (LinearLayout) findViewById(R.id.btm_lay);

			mIncomeNew.setIncludeFontPadding(false);
			mIncomeNew.setOnClickListener(this);
			mSavaMoneyBtn.setOnClickListener(this);
			mOutMoneyBtn.setOnClickListener(this);
			mIncomeNewShare.setOnClickListener(this);
			mEventsLay.setOnClickListener(this);
			mQueryLay.setOnClickListener(this);
			mIncomeNewClick.setOnClickListener(this);
		} else {
			mUnLoginLay = (RelativeLayout) mStubUnLogin.inflate();
			mStartBtnUnlgn = (ImageTextBtn) findViewById(R.id.start_btn_unlgn);
			mBeiLayout = (LinearLayout) findViewById(R.id.bei_lay);
			mQrnhUnLgn = (TextView) findViewById(R.id.qrnh_unlgn);
			mUesrNum = (TextView) findViewById(R.id.user_num_unlgn);
			mUnLoginAdImg = (ImageView) findViewById(R.id.unlogn_ad);
			mStartBtnUnlgn.setOnClickListener(this);
		}
	}

	public void jumpQuery(View v) {
		if (UserUtil.isBindBank()) {
			Intent ien = new Intent(this, QueryActivity.class);
			startActivity(ien);
		}
	}

	public void jumpToIncome(View v) {
		if (UserUtil.isBindBank()) {
			Intent ien = new Intent(this, QueryActivity.class);
			ien.putExtra(Cons.Intent_type, QueryFragment.TypeToIncome);
			startActivity(ien);
		}
	}

	public void jumpToModifyToken(View v) {
		if (GlobalParams.getGlobalParams().isDebugFlag()) {
			Intent ien = new Intent(this, ModifyTokenId.class);
			startActivity(ien);
		}
	}

	/**
	 * launcher task
	 * 
	 * @param type
	 */
	private void launchUserTask(String type) {
		ApplicationParams.getInstance().getServiceMger().addTask(new TaskBean(UpdateUserDataService.TaskType_UserInfo, type));
		ApplicationParams.getInstance().getServiceMger().addTask(new TaskBean(UpdateUserDataService.TaskType_UserCard, type));
	}

	@Override
	public boolean isShoudRegisterReciver() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onServiceRqCallBack(Intent taskData, boolean isCurrPage) {
		// TODO Auto-generated method stub
		super.onServiceRqCallBack(taskData, isCurrPage);
		Log.d("service", "onServiceRqCallBack--ProPertyActivity");
		String taskType = taskData.getStringExtra(Cons.Intent_type);
		if (UpdateUserDataService.TaskType_UserInfo.equals(taskType)) {
			// TODO Auto-generated method stub
			userInfo = taskData.getParcelableExtra(Cons.Intent_bean);
			String reqId = taskData.getStringExtra(Cons.Intent_id);
			if (userInfo.getContentCode() == Cons.SHOW_SUCCESS) {
				setTextValueAll();
				hideActionBtnView();
				invalidateOptionsMenu();
			} else {
				hideActionBtnView();
				showActionBtnRefeshView();
				hideActionBtnView();
				showToastShort(userInfo.getContentMsg());
			}
		} else if (UpdateUserDataService.TaskType_Notice.equals(taskType) && isCurrPage) {
			if (mNoticeHelp == null) {
				mNoticeHelp = new NoticeHelp();
			}
			mNoticeHelp.findView(this, UserUtil.isLogin() ? mLoginEdLay : mUnLoginLay);
			NoticeDtoList nlistDtoList = taskData.getParcelableExtra(Cons.Intent_bean);
			Log.d(NAME, "Notice is Curr");
			if (nlistDtoList != null) {
				NoticeDto nd;
				NoticeDto nd2;
				if (UserUtil.isLogin()) {// 登录状态的通知
					nd = NoticeHelp.Util.getNotice(nlistDtoList, NoticeHelp.Notice_ID_Main);
					if (nd != null) {
						mNoticeHelp.showOrHide(nd);
					}
					nd2 = NoticeHelp.Util.getNotice(nlistDtoList, NoticeHelp.Notice_ID_Activity);
					if (nd2 != null) {
						mEventHint.setText(nd2.getTipMsg());
						mEventHint.setVisibility(View.VISIBLE);
					} else {
						mEventHint.setVisibility(View.GONE);
					}
				} else {// 非登录状态
					nd = NoticeHelp.Util.getNotice(nlistDtoList, NoticeHelp.Notice_ID_UserCount);
					if (nd != null) {
						String userCount = nd.getTipMsg();
						setSpecialNotice(userCount);
					}
				}
			}
		}
	}

	private void doPushBund(Bundle b) {
		if (b != null && b.size() > 0 && !TextUtils.isEmpty(PushDispatch.INTENT_TYPE)) {
			code = b.getString(PushDispatch.INTENT_ID);
			msg = b.getString(PushDispatch.INTENT_MSG);
		}
		if (!TextUtils.isEmpty(code)) {
			String k = JpushOpera.getPushCode(code);
			String V = JpushOpera.getPushValue(code);
			String Ext = JpushOpera.getPushExtra(code);
			if (k.equals("CZ")) {
				mSavaMoneyBtn.performClick();
			} else if (k.equals("U")) {
				// 不做处理
			} else {
				new PushDispatch(this).despatchFragmentClass(0, k, V, Ext, msg);
			}
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		actionViewUtil = new ActionViewProgressUtil(this);
		if (getSupportLoaderManager().hasRunningLoaders()) {
			getSupportLoaderManager().restartLoader(0, null, this).forceLoad();
		} else {
			getSupportLoaderManager().initLoader(0, null, this).forceLoad();
		}

		if (!NetTools.getNetWorkVisable(this)) {
			showToastShort("网络不给力");
		}

		showActionBtnRefeshView();

		mGestureDetector = new GestureDetector(this, new MGestureListener());
		if (getIntent() != null && getIntent().getExtras() != null) {
			Bundle pushBundle = (Bundle) getIntent().getExtras().get(JPUSHBUNDLE);
			if (pushBundle != null) {
				doPushBund(pushBundle);
			}
		}

		if (UserUtil.isLogin()) {
			launchUserTask(CpUtil.Request_Type_Nomal);
		}
		// 启动通知更新接口
		NoticeHelp.Util.launcherNoticeTask(NoticeHelp.Req_Type_All, "notice");

		mCurrLoginFlag = UserUtil.isLogin();
		mAdHelp = new AdHelp(this, UserUtil.isLogin() ? mLoginEdLay : mUnLoginLay);
		new AdTask().executeOnExecutor(Executors.newCachedThreadPool());
	};

	@Override
	public void initUi(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.aty_property_new);
		buildActionbar();
		userInfo = ApplicationParams.getInstance().getPiggyParameter().getUserInfo();
		productInfo = ApplicationParams.getInstance().getPiggyParameter().getProductInfo();

		initStubView();
		setTextValueAll();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		boolean handled = super.dispatchTouchEvent(ev);
		handled = mGestureDetector.onTouchEvent(ev);
		return handled;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		userInfo = ApplicationParams.getInstance().getPiggyParameter().getUserInfo();
		productInfo = ApplicationParams.getInstance().getPiggyParameter().getProductInfo();

		if (isFirstResume == false) {
			boolean currLoginFlag = UserUtil.isLogin();
			if (currLoginFlag == true && mCurrLoginFlag == false) {
				((ViewGroup) findViewById(R.id.rootview)).removeView(mUnLoginLay);
			}

			if (!(currLoginFlag == mCurrLoginFlag)) {
				Log.i(NAME, "loginstatus is change");
				setTextValueAll();
				NoticeHelp.Util.launcherNoticeTask(NoticeHelp.Req_Type_All, "notice");
				if (mAdHelp != null) {
					mAdHelp.distroyAd();
				}
				mAdHelp = new AdHelp(this, UserUtil.isLogin() ? mLoginEdLay : mUnLoginLay);
				new AdTask().execute();
				mCurrLoginFlag = currLoginFlag;
			}

		} else {
			isFirstResume = false;
		}

		// if (mAdHelp.hasAd()) {
		// if (hasWindowFocus()) {
		// mAdHelp.startAd();
		// } else {
		// mAdHelp.distroyAd();
		// }
		// }

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		int intentType = intent.getIntExtra(Cons.Intent_type, 0);
		if (intentType == NeeduserDataReload && UserUtil.isLogin()) {
			showActionBtnRefeshView();
			launchUserTask(CpUtil.Request_Type_Nomal);
		}

		if (getIntent() != null) {
			if (jPushBundle != null) {
				doPushBund(jPushBundle);
				jPushBundle = null;
			}
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		boolean isLogin = UserUtil.isLogin();
		menu.findItem(R.id.action_refresh).setVisible(isShowRefreshMenu && isLogin);
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		actionViewUtil = new ActionViewProgressUtil(this);
		getSupportMenuInflater().inflate(R.menu.main, menu);
		actionViewUtil.setRefreshItem(menu.findItem(R.id.action_refresh));
		mNumAcMenuLay = (LinearLayout) menu.findItem(R.id.action_settings).getActionView();
		mNumAcMenuLay.setOnClickListener(this);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(this, SettingMainActivity.class);
			startActivity(intent);
			// if (untreatedLay.getVisibility() == View.GONE) {
			// showUntradeLay();
			// } else {
			// hideUntradeLay();
			// }
			break;
		case R.id.action_refresh:
			// mUpdateUserService.luncherTask();
			launchUserTask(CpUtil.Request_Type_Nomal);
			showActionBtnRefeshView();
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (UserUtil.isLogin() && ApplicationParams.getInstance().getsF().getBoolean(Cons.SFPatternFlag, false) == false) {
			ApplicationParams.getInstance().getsF().edit().putBoolean(Cons.SFLoginXXXXXX, true).remove(Cons.SFUserCusNo).remove(Cons.SFBindCardParams).commit();
			ApplicationParams.getInstance().getPiggyParameter().removeUserDataPrivate();
		} else {
			ApplicationParams.getInstance().getsF().edit().putBoolean(Cons.SFLoginXXXXXX, false).commit();
		}
		if (mAdHelp != null) {
			mAdHelp.distroyAd();
		}

		finish();
	}

	private void buildActionbar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setHomeButtonEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		RelativeLayout cusmlayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.property_cusm_title, null);
		cusmTitle = (TextView) cusmlayout.findViewById(R.id.cusm_title);
		cusmlay = (LinearLayout) cusmlayout.findViewById(R.id.content_lay);
		mSafeBtn = (ImageButton) cusmlayout.findViewById(R.id.safe_btn);
		cusmlay.setOnClickListener(this);
		mSafeBtn.setOnClickListener(this);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
		getSupportActionBar().setCustomView(cusmlayout, params);
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.windowscolor));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		Intent ien;

		UserBankVeryType uBankType;
		UserBankVeryType uVrfyType;
		switch (v.getId()) {
		case R.id.ac_menu_lay:
			Intent intent = new Intent(this, SettingMainActivity.class);
			startActivity(intent);

			// String testString2 =
			// "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"+
			// "<CpPay application=\"LunchPay.Req\">" + "<env>" + "TEST" +
			// "</env>" + "<merchantId>" + "201405229900001"+ "</merchantId>" +
			// "<merchantOrderId>" + "12345678901234567823" +
			// "</merchantOrderId>" + "<merchantOrderTime>" + "20140530173642"+
			// "</merchantOrderTime>" + "<orderKey>" + "2014053000014363"+
			// "</orderKey>" + "<sign>" +
			// "44CE90E47FCC3235ADDBB6F061EF8F7A055ECF0AEB64C2514650E21FD13554840AF4F4FC9FCF2FD7D21553EA6357202CB7E4A68CAF56190C424FE2E8B3E78E97DD95440AAF52179E6A7A457CEAFE90DE556BD77EBFAFD1F5E1689C802D8ECADF1C9BE067EC3E6BB715B86FD7788C11AB1DBB58013EF52B1F1DE67346958A4DBD"
			// + "</sign>" + "</CpPay>";
			//
			// String packageName=getApplicationContext().getPackageName();
			// Utils.setPackageName(packageName);
			// Intent i = new Intent(this, Initialize.class);
			// i.putExtra(CPGlobaInfo.XML_TAG, testString2);
			// startActivity(i);
			break;
		case R.id.savemoneybtn:
			if (!UserUtil.isLogin()) {
				ien = new Intent(this, LoginActivity.class);
				ien.putExtra(Cons.Intent_type, LoginFragment.LoginType_Main);
				startActivity(ien);
				return;
			}

			if (!isEnbleSaveMoney()) {
				showToastShort("当前存钱状态不可用");
				return;
			}

			MobclickAgent.onEvent(ProPertyActivity.this, Cons.EVENT_UI_Main, "存钱");

			if (userInfo == null) {
				showToastShort("数据加载中");
				launchUserTask(CpUtil.Request_Type_Nomal);
				return;
			}

			if (!UserUtil.isBindBank()) {
				ien = new Intent(this, BindCardActivity.class);
				ien.putExtra(Cons.Intent_type, BindCardFragment.Into_SaveMoney);
				startActivity(ien);
				return;
			}
			// else if (!(UserBankType.VrfySuccess == uVrfyType)) {
			// // 直接去cp验证银行卡
			// // 获取用户信息，直接跳转 验卡界面
			// fromSaveMoneyBtn = true;
			// new BandCardAndVerify(this).execute();
			// return;
			// }
			ien = new Intent(this, SaveMoneyActivity.class);
			ien.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(ien);
			break;
		case R.id.income_new:
			mIncomeNewClick.performClick();
			break;
		case R.id.outmoneybtn:
			MobclickAgent.onEvent(ProPertyActivity.this, Cons.EVENT_UI_Main, "取钱");
			if (!UserUtil.isLogin()) {
				ien = new Intent(this, LoginActivity.class);
				ien.putExtra(Cons.Intent_type, LoginFragment.LoginType_Main);
				startActivity(ien);
				return;
			}

			if (!isEnbleOutMoney()) {
				showToastShort("当前取钱状态不可用");
				return;
			}
			if (userInfo == null) {
				showToastShort("数据加载中");
				launchUserTask(CpUtil.Request_Type_Nomal);
				return;
			}
			if (!UserUtil.isBindBank()) {
				ien = new Intent(this, BindCardActivity.class);
				ien.putExtra(Cons.Intent_type, BindCardFragment.Into_OutMoney);
				startActivity(ien);
				return;
			}
			IntentCommon.intentToOutMoney(this, 0, 0);
			// mOutMoneyBtn.setEnabled(false);
			break;
		case R.id.content_lay:
			MobclickAgent.onEvent(this, Cons.EVENT_UI_Setting, "关于");
			ien = new Intent(this, AbsFragmentActivity.class);
			ien.putExtra(Cons.Intent_name, "howbuy.android.piggy.ui.fragment.AboutFragment");
			startActivity(ien);
			break;
		case R.id.start_btn_unlgn:
			if (!UserUtil.isLogin()) {
				ien = new Intent(this, LoginActivity.class);
				ien.putExtra(Cons.Intent_type, LoginFragment.LoginType_Main);
				startActivity(ien);
				return;
			}
		case R.id.income_new_share:
			if (productInfo != null) {
				ShareHelper.showSharePicker(this, mDlgClickListener);
			}
			break;
		case R.id.safe_btn:
			// self
//			MobclickAgent.onEvent(ProPertyActivity.this, Cons.EVENT_UI_Main, "安全");
//			ien = new Intent(this, AbsFragmentActivity.class);
//			ien.putExtra(Cons.Intent_name, "howbuy.android.piggy.ui.fragment.SelfFragment");
//			startActivity(ien);
//			break;
			
			MobclickAgent.onEvent(ProPertyActivity.this, Cons.EVENT_UI_Main, "安全");
			ien = new Intent(this, WebViewActivity.class);
			ien.putExtra(Cons.Intent_type, PureType.url.getType());
			ien.putExtra(Cons.Intent_id, "http://192.168.121.108:8080/wap/activity/1121/index.html");// activity/index.htm//activity/waptest.htm
			ien.putExtra(Cons.Intent_name, "活动专区");
			startActivity(ien);
			break;
		case R.id.query_lay:
			MobclickAgent.onEvent(ProPertyActivity.this, Cons.EVENT_UI_Main, "查历史");
			jumpQuery(null);
			break;
		case R.id.events_lay:
			MobclickAgent.onEvent(ProPertyActivity.this, Cons.EVENT_UI_Main, "公告");
			ien = new Intent(this, WebViewActivity.class);
			ien.putExtra(Cons.Intent_type, PureType.url.getType());
			ien.putExtra(Cons.Intent_id, UrlMatchUtil.urlSimple("activity/index.htm"));// activity/index.htm//activity/waptest.htm
			ien.putExtra(Cons.Intent_name, "活动专区");
			startActivity(ien);
			break;
		case R.id.income_new_click:
			jumpToIncome(null);
			break;
		default:
			break;
		}
	}

	private void showActionBtnRefeshView() {
		// !UserUtil.isLogin()
		if (actionViewUtil == null) {
			return;
		}
		actionViewUtil.showActionView();
	}

	private void hideActionBtnView() {
		if (actionViewUtil == null) {
			return;
		}
		actionViewUtil.reMoveActionView();
	}

	/**
	 * 获取untreatedLay高度 绑定service要放在获取unTreatedLayHeight之后进行
	 */
	private void obtainUntradeLayHeight() {
		mNotifyLay.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				isFirstResume = true;
				// unTreatedLayHeight = untreatedLay.getHeight(); // Ahaha!
				// Gotcha

				// untreatedLay.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				// untreatedLay.setVisibility(View.GONE);

			}

		});
	}

	private boolean isEnbleSaveMoney() {
		return (productInfo != null && productInfo.getSgbz() == ProductInfo.flagSGDisable);
	}

	private boolean isEnbleOutMoney() {
		return (productInfo != null && productInfo.getShbz() == ProductInfo.flagSHDisable);
	}

	/**
	 * 设置储蓄罐两个按钮是否可以点击
	 */
	private void setBtnEnable() {
		mSavaMoneyBtn.setEnabled(isEnbleSaveMoney());
		mOutMoneyBtn.setEnabled(isEnbleOutMoney());
	}

	private void setLoginFlagVisable() {
		boolean isLogin = UserUtil.isLogin();
		srueThisIsInit(isLogin);
		if (isLogin) {
			mLoginEdLay.setVisibility(View.VISIBLE);
			if (mUnLoginLay != null) {
				mUnLoginLay.setVisibility(View.GONE);
			}
		} else {
			mUnLoginLay.setVisibility(View.VISIBLE);
			if (mLoginEdLay != null) {
				mLoginEdLay.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 设置所有的text值
	 */
	private void setTextValueAll() {
		Log.d(NAME, "setTextValueAll");
		setLoginFlagVisable();
		if (UserUtil.isLogin()) {
			setTextLgn();
		} else {
			setTextUnlgn();
		}
	}

	private void setTextUnlgn() {
		if (productInfo == null) {
			setBei("0.00");
			return;
		}
		String bei;

		try {
			float qrnhF;
			float bankF;
			String qrnh = productInfo.getQrsy();
			String bank = productInfo.getBankInterestRates();

			if (!TextUtils.isEmpty(qrnh)) {
				qrnhF = Float.parseFloat(qrnh);
				qrnh = StringUtil.formatNum(String.valueOf(qrnhF), "###,##0.000") + "%";
			} else {
				qrnhF = 0f;
				qrnh = "0.00%";
			}

			if (!TextUtils.isEmpty(bank)) {
				bankF = Float.parseFloat(bank);
			} else {
				bankF = 0f;
			}

			if (qrnhF != 0f && bankF != 0f) {
				float beiF = Arith.divFloat(qrnhF, bankF);
				bei = StringUtil.formatFloat(beiF, StringUtil.floatFormatTwoStr);
			} else {
				bei = "0.00";
			}

			SpannableStringBuilder spsb = new SpannableStringBuilder();
			spsb.append("七日年化收益为");
			spsb.append(SpannableUtil.all(qrnh, -1, R.color.highlight_unlogin, false));
			spsb.append("是银行活期利率的");
			mQrnhUnLgn.setText(spsb);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			bei = "0.00";
		}
		setBei(bei);
	}

	private void setBei(String bei) {
		mBeiLayout.removeAllViews();
		int netIIndex = 0;
		int padding = getResources().getDimensionPixelSize(R.dimen.text_bei_padding);
		for (int i = 0; i < bei.length(); i++) {
			String number = String.valueOf(bei.charAt(i));
			TextView tv = new TextView(this);
			FontLoader.apply(tv, FontLoader.HoloFont.ROBOTO);
			tv.setIncludeFontPadding(false);
			tv.setBackgroundResource(R.drawable.bei_bg);
			tv.setText(number);
			tv.setTextColor(Color.WHITE);
			tv.setTextSize(50);
			tv.setGravity(Gravity.CENTER);
			int mgleft;
			if (number.equals(".") || (netIIndex != 0 && netIIndex + 1 == i)) {
				mgleft = AndroidUtil.dip2px(5);
				if (netIIndex == 0) {
					netIIndex = i;
					tv.setTextColor(0XffF96C57);
					tv.setBackgroundDrawable(null);
				} else {
					tv.setPadding(padding, 0, padding, 0);
				}
			} else {
				tv.setPadding(padding, 0, padding, 0);
				mgleft = AndroidUtil.dip2px(10);
			}
			LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.leftMargin = mgleft;
			mBeiLayout.addView(tv, lp);
		}
	}

	private void setSpecialNotice(String text) {
		int danbiInt = 0;
		try {
			if (!TextUtils.isEmpty(text)) {
				BigDecimal b = new BigDecimal(text);
				if (b.intValue() == 0) {
					danbiInt = 0;
				} else {
					danbiInt = Integer.parseInt(b.toPlainString());
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		if (danbiInt == 0) {
			mUesrNum.setVisibility(View.GONE);
		} else {
			mUesrNum.setVisibility(View.VISIBLE);
			SpannableStringBuilder spsb = new SpannableStringBuilder();
			spsb.append("已有");
			spsb.append(SpannableUtil.all(formatLimit(String.valueOf(danbiInt)), -1, R.color.highlight_unlogin, false));
			spsb.append("人使用储蓄罐");
			mUesrNum.setText(spsb);
		}
	}

	public String formatLimit(String danbi) {
		if (TextUtils.isEmpty(danbi)) {
			return "--万";
		}
		try {
			BigDecimal b = new BigDecimal(danbi);
			int a = b.intValue();
			if (a < 1) {
				return String.valueOf(a);
			} else {
				int danbiInt = a / 10000;
				String wan = String.valueOf(danbiInt) + "万";
				return wan;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return "--万";
	}

	/**
	 * 设置用户等信息
	 */
	private void setTextLgn() {
		userInfo = ApplicationParams.getInstance().getPiggyParameter().getUserInfo();
		productInfo = ApplicationParams.getInstance().getPiggyParameter().getProductInfo();
		setBtnEnable();
		if (userInfo != null) {
			if (userInfo.getContentCode() == Cons.SHOW_SUCCESS) {
				setTextAccess(userInfo);
				cusmTitle.setText("储蓄罐");
			} else {
				showToastShort(userInfo.getContentMsg());
			}
		} else {
			if (UserUtil.isLogin()) {
				cusmTitle.setText("数据加载中...");
			} else {
				// cusmTitle.setText("点击登录");
				cusmTitle.setText("储蓄罐");
			}
		}

		if (productInfo != null) {
			if (productInfo.getContentCode() == Cons.SHOW_SUCCESS) {
				setWfsyQrnh(productInfo);
			} else {
				showToastShort(productInfo.getContentMsg());
			}
		}
	}

	private void setTextAccess(UserInfoDto accessInfo) {
		try {

			if (accessInfo == null || accessInfo.contentCode != Cons.SHOW_SUCCESS) {
				return;
			}
			String res = StringUtil.formatCurrency(accessInfo.getBalanceAmt());
			String ye = TextUtils.isEmpty(accessInfo.getYesterdayIncome()) ? "0" : accessInfo.getYesterdayIncome();
			String yesT = StringUtil.formatCurrency(ye);
			BigDecimal allIn = new BigDecimal(accessInfo.getSettledAmt()).add(new BigDecimal(accessInfo.getUnSettleAmt()));// 已结转+未结转
			String allInStr = StringUtil.formatCurrency(allIn.toString());
			String incomeNewTitle = String.format(getString(R.string.income_title), accessInfo.getNavDt());
			mIncomeNew.setText(yesT);
			mProperty.setText(res);
			mIncomeAll.setText(allInStr);
			mIncomeNewTitle.setText(incomeNewTitle);
			setTextQuerySum(accessInfo);

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void setTextQuerySum(UserInfoDto accessInfo) {
		try {
			int unCuncount = accessInfo.getUnconfirmVolCount() == null ? 0 : Integer.parseInt(accessInfo.getUnconfirmVolCount());
			int unQucount = accessInfo.getUnconfirmAmtCount() == null ? 0 : Integer.parseInt(accessInfo.getUnconfirmAmtCount());
			int sum = unCuncount + unQucount;

			if (sum == 0) {
				mQueryHint.setVisibility(View.GONE);
			} else {
				mQueryHint.setVisibility(View.VISIBLE);
				SpannableStringBuilder sb = new SpannableStringBuilder();
				sb.append("(");
				sb.append(SpannableUtil.all(String.valueOf(sum), -1, R.color.highlight_unlogin, false));
				sb.append(getResources().getString(R.string.sum_un_op));
				sb.append(")");
				mQueryHint.setText(sb);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void setWfsyQrnh(ProductInfo productInfo) {
		try {
			String nvDate = productInfo.getNavDate() == null ? "" : productInfo.getNavDate();
			String qrnh = productInfo.getQrsy();
			String wfsy = productInfo.getWfsy();
			nvDate = TextUtils.isEmpty(nvDate) ? "" : "(" + nvDate + ")";

			String qrnhDate;
			String wfsyDate;
			qrnhDate = String.format(getResources().getString(R.string.qrnhsy), nvDate);
			wfsyDate = String.format(getResources().getString(R.string.wfsy), nvDate);
			mQrnhTitle.setText(qrnhDate);
			mWsfyTitle.setText(wfsyDate);

			if (!TextUtils.isEmpty(qrnh)) {
				float qrsy = Float.parseFloat(qrnh);
				qrnh = StringUtil.formatNum(String.valueOf(qrsy), "###,##0.000");
			} else {
				qrnh = "0.000";
			}
			mQrnh.setText(qrnh + "%");

			if (!TextUtils.isEmpty(wfsy)) {
				wfsy = StringUtil.formatNum(wfsy, "###,##0.0000");
			} else {
				wfsy = "0.0000";
			}
			mWsfy.setText(wfsy);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private String getShareContentString() {
		if (productInfo == null) {
			return "";
		}
		String bei;
		String res;
		try {
			float qrnhF;
			float bankF;
			String qrnh = productInfo.getQrsy();
			String bank = productInfo.getBankInterestRates();

			if (!TextUtils.isEmpty(qrnh)) {
				qrnhF = Float.parseFloat(qrnh);
				qrnh = StringUtil.formatNum(String.valueOf(qrnhF), "###,##0.000") + "%";
			} else {
				qrnhF = 0f;
				qrnh = "0.000%";
			}

			if (!TextUtils.isEmpty(bank)) {
				bankF = Float.parseFloat(bank);
			} else {
				bankF = 0f;
			}

			if (qrnhF != 0f && bankF != 0f) {
				float beiF = Arith.divFloat(qrnhF, bankF);
				bei = StringUtil.formatFloat(beiF, StringUtil.floatFormatTwoStr);
			} else {
				bei = "0.00";
			}
			// 7月9日我的储蓄罐七日年化收益为4.948%，是活期存款利率的12.43倍。会赚钱的储蓄罐，活期存款增值快！
			String date = productInfo.getNavDate();
			res = date + "我的储蓄罐七日年化收益为" + qrnh + ",是活期存款的" + bei + "倍。会赚钱的储蓄罐，活期存款增值快!";
		} catch (Exception e) {
			// TODO: handle exception
			res = "";
		}
		return res;
	}

	@Override
	public Loader<UpdateDto> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return new CheckUpdateLoader(this);
	}

	@Override
	public void onLoadFinished(Loader<UpdateDto> arg0, UpdateDto arg1) {
		// TODO Auto-generated method stub
		if (arg1.getContentCode() == Cons.SHOW_SUCCESS) {
			Message m = new Message();
			m.obj = arg1;
			m.what = Cons.SHOW_SUCCESS;
			handler.sendMessage(m);
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == Cons.SHOW_SUCCESS) {
				UpdateDto arg1 = (UpdateDto) msg.obj;
				// arg1.setVersionNeedUpdate("1");
				String needUpdate = arg1.getVersionNeedUpdate();
				if (!needUpdate.equals("2")) {
					Bundle b = new Bundle();
					b.putParcelable(Cons.Intent_bean, arg1);
					UpdateDialog.newInstance(b).show(getSupportFragmentManager(), "");
				}
			}
		};
	};

	@Override
	public void onLoaderReset(Loader<UpdateDto> arg0) {
		// TODO Auto-generated method stub

	}

	class MGestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			// TODO Auto-generated method stub
			// getRowX：触摸点相对于屏幕的坐标
			// getX： 触摸点相对于按钮的坐标
			// getTop： 按钮左上角相对于父view（LinerLayout）的y坐标
			// getLeft： 按钮左上角相对于父view（LinerLayout）的x坐标

			int dx = (int) (e2.getX() - e1.getX()); // 计算滑动的距离
			Log.d(NAME, "velocityx=" + velocityX);
			if (Math.abs(dx) > 200 && Math.abs(velocityX) > Math.abs(velocityY)) { // 降噪处理，必须有较大的动作才识别
				Log.d(NAME, "velocityxx=" + velocityX);
				if (velocityX > 0) {
					// 向右边
				} else {
					// 向左边
					Log.d(NAME, "RawY=" + e2.getRawY());
					if (e2.getRawY() > AndroidUtil.dip2px(25 + 48) + getResources().getDimensionPixelSize(R.dimen.cicle_height)) {
						Intent i = new Intent(ProPertyActivity.this, SettingMainActivity.class);
						startActivity(i);
					} else {
						return false;
					}
				}
				return true;
			} else {
				return false; // 当然可以处理velocityY处理向上和向下的动作
			}
		}
	}

	class AdTask extends MyAsyncTask<Void, Void, AdvertList> {

		@Override
		protected AdvertList doInBackground(Void... params) {
			// TODO Auto-generated method stub
			AdTools ad = AdTools.getInstance();
			try {
				AdvertList adList = DispatchAccessData.getInstance().getAd(String.valueOf(ad.getAdWidth()), String.valueOf(ad.getAdHeight()), CacheHelp.cachetime_60_mint);
				return adList;
			} catch (HowbuyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(AdvertList result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null && result.getCommon().getResponseCode().equals("1") && result.getIcAdvertsCount() > 0) {

				mAdHelp.setAdView(result);
				if (!UserUtil.isLogin()) {
					// 默认一张图片清除
					if (mUnLoginAdImg != null) {
						mUnLoginAdImg.setVisibility(View.GONE);
					}
				}
			} else {
				mAdHelp.noneAd();
			}
		}
	}

}
