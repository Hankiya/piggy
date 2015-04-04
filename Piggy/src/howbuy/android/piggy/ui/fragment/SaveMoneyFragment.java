package howbuy.android.piggy.ui.fragment;

import howbuy.android.lib.MyAsyncTask;
import howbuy.android.piggy.MyAlertDialog;
import howbuy.android.piggy.R;
import howbuy.android.piggy.UserUtil;
import howbuy.android.piggy.UserUtil.UserSoundType;
import howbuy.android.piggy.api.DispatchAccessData;
import howbuy.android.piggy.api.dto.OneStringDto;
import howbuy.android.piggy.api.dto.ProductInfo;
import howbuy.android.piggy.api.dto.TradeDate;
import howbuy.android.piggy.api.dto.UserCardDto;
import howbuy.android.piggy.api.dto.UserInfoDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.dialog.PiggyProgressDialog;
import howbuy.android.piggy.dialogfragment.PrefectDialog;
import howbuy.android.piggy.error.HowbuyException;
import howbuy.android.piggy.help.CacheHelp;
import howbuy.android.piggy.help.NoticeHelp;
import howbuy.android.piggy.service.ServiceMger.TaskBean;
import howbuy.android.piggy.service.UpdateUserDataService;
import howbuy.android.piggy.sound.SoundUtil;
import howbuy.android.piggy.ui.AbsFragmentActivity;
import howbuy.android.piggy.ui.BindCardActivity;
import howbuy.android.piggy.ui.ProPertyActivity;
import howbuy.android.piggy.ui.WebViewActivity;
import howbuy.android.piggy.ui.base.AbsNoticeFrag;
import howbuy.android.piggy.ui.base.OnKeyboardVisibilityListener;
import howbuy.android.piggy.ui.fragment.PureWebFragment.PureType;
import howbuy.android.piggy.widget.ClearableEdittext;
import howbuy.android.piggy.widget.ClearableEdittext.MySelelctChanged;
import howbuy.android.piggy.widget.ImageTextBtn;
import howbuy.android.util.AndroidUtil;
import howbuy.android.util.Arith;
import howbuy.android.util.Cons;
import howbuy.android.util.FieldVerifyUtil;
import howbuy.android.util.SpannableUtil;
import howbuy.android.util.StringUtil;
import howbuy.android.util.http.UrlConnectionUtil;
import howbuy.android.util.http.UrlMatchUtil;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.umeng.analytics.MobclickAgent;

public class SaveMoneyFragment extends AbsNoticeFrag implements OnClickListener, OnKeyboardVisibilityListener, MySelelctChanged {
	public static final String NAME = "SaveMoneyFragment";
	public static final float maxUnPrefectAmount = 5000f;
	private PiggyProgressDialog mpDialog;
	private TextView bankinfo_name;
	private TextView bankinfo_no;
	private TextView bankinfo_limit;
	howbuy.android.piggy.widget.ClearableEdittext savamoneynumber;
	howbuy.android.piggy.widget.ClearableEdittext tridePwd;
	private ImageView bankinfo_icon;
	private TextView startshouyidate;
	private TextView oneyearshouyi;
	private TextView comparebankshouyi;
	private ImageTextBtn submitBtn;
	private LinearLayout fastXieYi;
	private CheckBox checkBox;
	private TextView checkText;
	private RelativeLayout mBankLay;

	private UserInfoDto mUserInfo;
	private ProductInfo mProductInfo;
	private TradeDate mTradeDate;
	private BigDecimal maxAmount = new BigDecimal("0");
	private BigDecimal miniAmount = new BigDecimal("0");//银行卡用户最低存款
	private String bankId;
	private String bankFlag;
	public SoundUtil mSoundUtil;
	private String numberValue;
	protected String mS;
	private int mIntoType;
	private UserCardDto mCurrCardDto;
	private int mCurrCheckedItem;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			startActProper();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean isShoudRegisterReciver() {
		return true;
	};

	@Override
	public void onServiceRqCallBack(Intent taskData, boolean isCurrPage) {
		// TODO Auto-generated method stub
		super.onServiceRqCallBack(taskData, isCurrPage);
		Log.d("service", "onServiceRqCallBack--SaveMoneyFragment");
		String taskType = taskData.getStringExtra(Cons.Intent_type);
		if (UpdateUserDataService.TaskType_TDay.equals(taskType)) {
			TradeDate mTradeDate2 = ApplicationParams.getInstance().getPiggyParameter().getTwoDay();
			if (mTradeDate2 != mTradeDate) {
				setDaoDate(mTradeDate2);
			}
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		mIntoType = getSherlockActivity().getIntent().getIntExtra(Cons.Intent_type, 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.aty_savemoney, container, false);
		bankinfo_name = (TextView) view.findViewById(R.id.bankinfo_name);
		bankinfo_no = (TextView) view.findViewById(R.id.bankinfo_no);
		bankinfo_limit = (TextView) view.findViewById(R.id.bankinfo_limit);
		bankinfo_icon = (ImageView) view.findViewById(R.id.bankinfo_icon);
		savamoneynumber = (howbuy.android.piggy.widget.ClearableEdittext) view.findViewById(R.id.savamoneynumber);
		tridePwd = (howbuy.android.piggy.widget.ClearableEdittext) view.findViewById(R.id.trade_pwd);
		mBankLay = (RelativeLayout) view.findViewById(R.id.bank_lay);

		startshouyidate = (TextView) view.findViewById(R.id.startshouyidate);
		oneyearshouyi = (TextView) view.findViewById(R.id.oneyearshouyi);
		comparebankshouyi = (TextView) view.findViewById(R.id.comparebankshouyi);
		submitBtn = (ImageTextBtn) view.findViewById(R.id.submit_btn);
		fastXieYi = (LinearLayout) view.findViewById(R.id.fastxieyi);
		checkBox = (CheckBox) view.findViewById(R.id.checkf);
		checkText = (TextView) view.findViewById(R.id.clecktext);
		checkText.setOnClickListener(this);
		submitBtn.setOnClickListener(this);
		mBankLay.setOnClickListener(this);
		savamoneynumber.setMySelelctChanged(this);

		submitBtn.setEnabled(false);
		savamoneynumber.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!(TextUtils.isEmpty(tridePwd.getEditableText().toString()))) {
					submitBtn.setEnabled(true);
				}

				if (TextUtils.isEmpty(mS)) {
					mS = s.toString();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().equals("")) {
					submitBtn.setEnabled(false);
				}
				setBeyound(mProductInfo, s.toString());

				String a = s.toString().replaceAll(",", "");
				String b = mS.replaceAll(",", "");
				if (!a.equals(b) && !a.endsWith(".") && !(a.contains(".") && a.subSequence(a.length() - 2, a.length() - 1).equals("."))) {
					String text = StringUtil.formatAll(a, StringUtil.floatFormatSaveOrOut);
					mS = text;
					savamoneynumber.setText(text);
					savamoneynumber.setSelection(text.length());
				}
			}
		});
		tridePwd.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!(TextUtils.isEmpty(savamoneynumber.getEditableText().toString()))) {
					submitBtn.setEnabled(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().equals("")) {
					submitBtn.setEnabled(false);
				}
			}
		});

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		// setKeyboardListener(this);
		mProductInfo = ApplicationParams.getInstance().getPiggyParameter().getProductInfo();
		mUserInfo = ApplicationParams.getInstance().getPiggyParameter().getUserInfo();
		mTradeDate = ApplicationParams.getInstance().getPiggyParameter().getTwoDay();
		// mCurrCardDto=getSherlockActivity().getIntent().getParcelableExtra(Cons.Intent_bean);
		mCurrCardDto = ApplicationParams.getInstance().getPiggyParameter().getUserCardInfo().getDefaultCard();
		setBankInfo(mCurrCardDto);
		setXieYiFlag(mCurrCardDto);
		setLimit(mCurrCardDto);
		setBeyound(mProductInfo, "0");
		setCunZuiDi(mProductInfo, null);
		setDaoDate(mTradeDate);
		tridePwd.setClearType(ClearableEdittext.TypePas);

	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode==Activity.RESULT_OK) {
			//添加银行卡成功
		}
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ApplicationParams.getInstance().getServiceMger().addTask(new TaskBean(UpdateUserDataService.TaskType_TDay, "0"));
		if ((mUserInfo != null) && (mUserInfo.contentCode == Cons.SHOW_FORCELOGIN)) {
			MyAlertDialog.newInstance(null).show(getFragmentManager(), "");
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent in;
		switch (v.getId()) {
		case R.id.clecktext:
			in = new Intent(getActivity(), WebViewActivity.class);
			in.putExtra(Cons.Intent_type, PureType.urlWearOperal.getType());
			in.putExtra(Cons.Intent_id, "file:///android_asset/autotransferprococal.html");
			in.putExtra(Cons.Intent_name, "银行自动转账授权书");
			startActivity(in);
			break;
		case R.id.submit_btn:
			String cusNo = getSf().getString(Cons.SFUserCusNo, null);// 6227001215800062891
			numberValue = savamoneynumber.getEditableText().toString().replaceAll(",", "");
			String tridePwdValue = tridePwd.getEditableText().toString();
			String bkFlag = "0";
			bankId = mCurrCardDto.getCustBankId();

			if (mProductInfo == null || mUserInfo == null) {
				return;
			}

			if (fastXieYi.getVisibility() == View.VISIBLE) {
				if (checkBox.isChecked() == false) {
					showToastShort("请同意银行自动转账授权书");
					return;
				} else {
					bkFlag = "1";
				}
			}

			FieldVerifyUtil.VerifyReslt vReslt = FieldVerifyUtil.verifyAmount(numberValue, 2);
			if (!vReslt.isSuccess()) {
				showToastShort(vReslt.getMsg());
				return;
			}

			vReslt = FieldVerifyUtil.verifyTradePwd(tridePwdValue);
			if (!vReslt.isSuccess()) {
				showToastShort(vReslt.getMsg());
				return;
			}

			if (numberValue.length() == 1 && numberValue.endsWith(".")) {
				savamoneynumber.setText("");
				showToastShort("金额非法！");
				return;
			}

			BigDecimal amount = new BigDecimal(numberValue);
			if (!(UserUtil.userSoundStatus(mCurrCardDto.getBankAcct()) == UserSoundType.Sounduccess) && amount.floatValue() > maxUnPrefectAmount) {
				showPrefectDialog();
				return;
			}

			if (amount.compareTo(miniAmount) == -1) {
				showToastShort("金额低于最低限额");
				return;
			}
			
			if (amount.compareTo(maxAmount)==1) {
				showToastShort("金额大于单日限额");
				return;
			}

			new SaveMoneyTask().executeOnExecutor(Executors.newCachedThreadPool(), cusNo, numberValue, tridePwdValue, bankId, bkFlag);
			mpDialog = new PiggyProgressDialog(getActivity());
			mpDialog.setCancelable(false);
			mpDialog.setCanceledOnTouchOutside(false);
			mpDialog.setMessage("操作中...");
			mpDialog.show();
			MobclickAgent.onEvent(getActivity(), Cons.EVENT_UI_SaveMoney, "确认存入");
			break;
		case R.id.bank_lay:
			showBankDialog();
			break;
		default:
			break;
		}
	}

	public class SaveMoneyTask extends MyAsyncTask<String, Void, OneStringDto> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected OneStringDto doInBackground(String... params) {
			// TODO Auto-generated method stub
			return DispatchAccessData.getInstance().saveMoney(params[0], params[1], params[2], params[3], params[4]);
		}

		@Override
		protected void onPostExecute(OneStringDto result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result.getContentCode() == Cons.SHOW_SUCCESS && getActivity() != null) {

				Handler handler = new Handler() {
					public void handleMessage(android.os.Message msg) {
						if (msg.what == Cons.SHOW_SUCCESS) {
							if (mpDialog != null && mpDialog.isShowing()) {
								mpDialog.dismiss();
							}
							// showToastTrueLong("存钱成功！");
							// 播放声音
							mSoundUtil = new SoundUtil(ApplicationParams.getInstance());
							mSoundUtil.playSound(R.raw.save_money);
							Intent intent = new Intent(getActivity().getApplicationContext(), AbsFragmentActivity.class);
							intent.putExtra(Cons.Intent_name, "howbuy.android.piggy.ui.fragment.OpeatSuccessFragment");
							intent.putExtra(Cons.Intent_id, OpeatSuccessFragment.TypeSaveMoneySuccess);
							intent.putExtra(Cons.Intent_bean, numberValue);
							intent.putExtra(Cons.Intent_type, mIntoType);
							getActivity().startActivity(intent);
							getActivity().finish();

						}
					};
				};
				// 进行第二个动画
				mpDialog.startAnimation1(R.drawable.progress_piggy_down);
				// 进行第二个动画完成时的动作
				handler.sendEmptyMessageDelayed(Cons.SHOW_SUCCESS, getResources().getInteger(R.integer.progresstime) * 12);

			} else if (result.getContentCode() == Cons.SHOW_FORCELOGIN && getActivity() != null) {
				if (mpDialog != null && mpDialog.isShowing()) {
					mpDialog.dismiss();
				}
				MyAlertDialog.newInstance(null).show(getFragmentManager(), "");
			} else if (result.getContentCode() == Cons.SHOW_SEPT && getActivity() != null) {
				if (mpDialog != null && mpDialog.isShowing()) {
					mpDialog.dismiss();
				}
				showPrefectDialog();
			} else if (result.getContentCode() == Cons.SHOW_ERROR && getActivity() != null) {
				if (mpDialog != null && mpDialog.isShowing()) {
					mpDialog.dismiss();
				}
				String res = result.getContentMsg();
				if (res.equals(UrlConnectionUtil.ErrorNetWorkTimeOut)) {
					res = "请求已发起，等待系统确认，请稍后在交易查询-交易历史里查询订单状态";
				}
				showToastShort(res);
				tridePwd.setText("");
			} else if (result.getContentCode() == Cons.SHOW_ASYN_ERROR && getActivity() != null) { // cp
																									// 不能及时返回存钱成功与否的状态，就返回状态未知，异步错误
				Handler handler = new Handler() {
					public void handleMessage(android.os.Message msg) {
						if (msg.what == Cons.SHOW_SUCCESS) {
							if (mpDialog != null && mpDialog.isShowing()) {
								mpDialog.dismiss();
							}
							// showToastTrueLong("存钱成功！");
							// 播放声音
							mSoundUtil = new SoundUtil(ApplicationParams.getInstance());
							mSoundUtil.playSound(R.raw.save_money);
							Intent intent = new Intent(getActivity().getApplicationContext(), AbsFragmentActivity.class);
							intent.putExtra(Cons.Intent_name, "howbuy.android.piggy.ui.fragment.OpeatSuccessFragment");
							intent.putExtra(Cons.Intent_id, OpeatSuccessFragment.TypeSaveMoneySuccessCP);
							intent.putExtra(Cons.Intent_type, mIntoType);
							intent.putExtra(Cons.Intent_bean, numberValue);
							getActivity().startActivity(intent);
							getActivity().finish();

						}
					};
				};
				// 进行第二个动画
				mpDialog.startAnimation1(R.drawable.progress_piggy_down);
				// 进行第二个动画完成时的动作
				handler.sendEmptyMessageDelayed(Cons.SHOW_SUCCESS, getResources().getInteger(R.integer.progresstime) * 12);
			}
		}
	}

	public void showPrefectDialog() {
		PrefectDialog.newInstance(null).show(getFragmentManager(), "");
	}

	/**
	 * 判断软键盘是否被弹出
	 * 
	 * @param listener
	 */
	public final void setKeyboardListener(final OnKeyboardVisibilityListener listener) {
		final View activityRootView = ((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0);
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			private boolean wasOpened;

			private final Rect r = new Rect();

			@Override
			public void onGlobalLayout() {
				activityRootView.getWindowVisibleDisplayFrame(r);

				int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
				if (heightDiff == 0 && !wasOpened) {
					return;
				}
				if (heightDiff > 100) {
					wasOpened = true;
					listener.onVisibilityChanged(wasOpened);
				} else {
					wasOpened = false;
					listener.onVisibilityChanged(wasOpened);
				}
			}
		});
	}

	@Override
	public void onVisibilityChanged(boolean visible) {
		// TODO Auto-generated method stub
		// mNoKeyBoard.setVisibility(visible ? View.GONE : View.VISIBLE);
	}

	/**
	 * 设置银行信息
	 * 
	 * @param uInfoDto
	 */
	public void setBankInfo(UserCardDto uInfoDto) {
		if (uInfoDto == null) {
			return;
		}
		bankinfo_name.setText(uInfoDto.getBankName());
		bankinfo_no.setText(StringUtil.formatViewBankCard(uInfoDto.getBankAcct()));
	}

	/**
	 * 设置用户最低存款
	 * 
	 * @param productInfo
	 * @param mini2
	 */
	public void setCunZuiDi(ProductInfo productInfo, BigDecimal mini2) {
		try {
			if (productInfo == null) {
				return;
			}
			float uu = Float.parseFloat(productInfo.getMinAcctVol());
			if (uu < 1) {
				uu = 1.00f;
			}
			miniAmount = new BigDecimal(uu);
			if (mini2 != null) {
				miniAmount = new BigDecimal(Math.max(miniAmount.floatValue(), mini2.floatValue()));
			}
			String v = StringUtil.formatCurrency(miniAmount.toString());
			savamoneynumber.setHint("≥" + v + "元" + getResources().getString(R.string.freefee));
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	/**
	 * 设置预期到期日期
	 */
	public void setDaoDate(TradeDate tDate) {
		if (tDate == null) {
			return;
		}
		String date = tDate.getConfirmDt();
		String res = "--";
		if (!TextUtils.isEmpty(date)) {
			SimpleDateFormat sfFormat = new SimpleDateFormat("yyyy年MM月dd日");
			SimpleDateFormat sfPose = new SimpleDateFormat("yyyyMMdd");
			Date date2 = null;
			try {
				date2 = sfPose.parse(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			res = sfFormat.format(date2);
		}
		SpannableStringBuilder sBuilder = new SpannableStringBuilder();
		sBuilder.append(SpannableUtil.all("起息日：", 14, R.color.text_page, false)).append(SpannableUtil.all(res, 14, R.color.actioncolor, false));
		startshouyidate.setText(sBuilder);
	}

	/**
	 * 设置绑定银行卡协议
	 * 
	 * @param uInfoDto
	 */
	public void setXieYiFlag(UserCardDto uInfoDto) {
		if (uInfoDto == null) {
			return;
		}

		bankFlag = uInfoDto.getPaySign();
		// bankId=map.get(UserInfoDto.Flag_BANKACCT);
		// bankFlag=map.get(UserInfoDto.Flag_ISHBSIGN);
		if (TextUtils.isEmpty(bankFlag) == false && !UserInfoDto.Flag_BANKACCTSuccess.equals(bankFlag)) {
			fastXieYi.setVisibility(View.VISIBLE);
			SpannableString s1 = SpannableUtil.all("我已阅读并同意", 14, R.color.text_page_gray, false);
			SpannableString s2 = SpannableUtil.all("《银行自动转账授权书》", 14, R.color.text_page_gray, true);
			SpannableStringBuilder s = new SpannableStringBuilder();
			s.append(s1).append(s2);
			checkText.setText(s);
		}
	}

	/**
	 * 用户存钱提示 加上.的判断
	 * 
	 * @param productInfo
	 * @param amount
	 */
	public void setBeyound(ProductInfo productInfo, String amount) {
		if (productInfo == null) {
			return;
		}
		float oneYearBenefits = Float.parseFloat(productInfo.getBankInterestRates());
		float qrnhua = Float.parseFloat(productInfo.getQrsy());
		// boolean isNum = amount.matches("[0-9.]+");

		if (amount.startsWith(".")) {
			savamoneynumber.setText("");
			showToastShort("金额非法！");
			return;
		}

		boolean isNull = TextUtils.isEmpty(amount);
		if (isNull || amount.equals("0")) {
			amount = "0";
			oneyearshouyi.setVisibility(View.GONE);
			comparebankshouyi.setVisibility(View.GONE);
		} else {
			oneyearshouyi.setVisibility(View.VISIBLE);
			comparebankshouyi.setVisibility(View.VISIBLE);
		}
		amount = amount.replaceAll(",", "");

		float amountIntger = Float.parseFloat(amount);
		if (amountIntger < 100f) {
			oneyearshouyi.setVisibility(View.GONE);
			comparebankshouyi.setVisibility(View.GONE);
			return;
		}

		float resHowbuyShouyi = Arith.divFloat(Arith.mulFloat((float) amountIntger, qrnhua), 100f);
		float resBankShouyi = Arith.divFloat(Arith.mulFloat((float) amountIntger, oneYearBenefits), 100f);
		String resHowbuyShouyiString = StringUtil.formatBaiFen(String.valueOf(resHowbuyShouyi));
		String resBankShouyiString = StringUtil.formatBaiFen(String.valueOf(resBankShouyi)) + "元";
		String resBei = StringUtil.formatFloat(Arith.divFloat(qrnhua, oneYearBenefits), StringUtil.floatFormatOneStr);

		SpannableStringBuilder sBuilder = new SpannableStringBuilder();
		sBuilder.append(SpannableUtil.all("未来一年预计收益", 14, R.color.text_page, false)).append(SpannableUtil.all(resHowbuyShouyiString, 14, R.color.actioncolor, false))
				.append(SpannableUtil.all("元，是银行一年活期存款收益", 14, R.color.text_page, false)).append(SpannableUtil.all(resBankShouyiString + "的", 14, R.color.text_page, false))
				.append(SpannableUtil.all(String.valueOf(resBei), 14, R.color.actioncolor, false)).append(SpannableUtil.all("倍", 14, R.color.text_page, false));
		oneyearshouyi.setText(sBuilder);
		sBuilder.clear();

		// sBuilder.append(SpannableUtil.exceptBeyoundHint(getActivity().getApplicationContext(),
		// "同期银行活期收益：")).append(
		// SpannableUtil.exceptBeyoundNumber(getActivity().getApplicationContext(),
		// String.valueOf(resBankShouyiString)));
		comparebankshouyi.setVisibility(View.GONE);
	}

	/**
	 * 跳转到主页
	 */
	private void startActProper() {
		Intent startIntent;
		startIntent = new Intent(getSherlockActivity(), ProPertyActivity.class);
		startIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// startIntent.putExtra(Cons.Intent_type,
		// ProPertyActivity.NeeduserDataReload);
		startActivity(startIntent);
		getActivity().finish();
	}

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		startActProper();
		return true;
	}

	@Override
	public String getNoticeType() {
		// TODO Auto-generated method stub
		return NoticeHelp.Notice_ID_Save;
	}

	public void setLimit(UserCardDto infoDto) {
		if (infoDto != null) {
			new BankIconTask().executeOnExecutor(Executors.newCachedThreadPool(), infoDto.getBankCode());
			String danBi = infoDto.getLimitPerTime();// 单笔
			String danRi = infoDto.getLimitPerDay();// 日限额
			bankinfo_limit.setText("单笔限存入金额" + formatLimit(danBi) + ",日限额" + formatLimit(danRi));
			maxAmount=new BigDecimal(danBi);
		
			//设置用户最低存款
			String mini = infoDto.getMinPayLimit();
			if (TextUtils.isEmpty(mini)) {
				setCunZuiDi(mProductInfo, miniAmount);
			}else {
				BigDecimal mini2 = new BigDecimal(mini);
				if (miniAmount.floatValue() < mini2.floatValue()) {
					miniAmount = mini2;
					setCunZuiDi(mProductInfo, mini2);
				}
			}
		}
	}

	/**
	 * 银行icon
	 * 
	 * @author Administrator
	 * 
	 */
	class BankIconTask extends MyAsyncTask<String, Void, InputStream> {

		@Override
		protected InputStream doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				InputStream in = DispatchAccessData.getInstance().getBankImg(iconUrl(params[0]), CacheHelp.cachetime_one_month);
				return in;
			} catch (HowbuyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(InputStream result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null && isAdded()) {
				Drawable b = Drawable.createFromStream(result, null);
				bankinfo_icon.setImageDrawable(b);
			} else if (isAdded()) {
				bankinfo_icon.setImageResource(UserUtil.userBankIcon(mCurrCardDto.getBankCode()));
			}
		}

		private String iconUrl(String bankCode) {
			String basePath2 = UrlMatchUtil.getBasepath2() + AndroidUtil.getSourceFolderName() + "/" + bankCode + ".png";
			return basePath2;
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
	
	
	@Override
	public void onSelectionChanged(int selStart, int selEnd) {
		// TODO Auto-generated method stub
		int offsetPos = savamoneynumber.getEditableText().length();
		if (selStart == selEnd && selEnd != offsetPos) {
			savamoneynumber.setSelection(offsetPos);
		}

	}

	private void showBankDialog() {
		final List<UserCardDto> list = ApplicationParams.getInstance().getPiggyParameter().getUserCardInfo().getUserCardDtos();
		final String[] dataArray = new String[list.size() + 1];
		for (int i = 0; i < list.size(); i++) {
			UserCardDto u = list.get(i);
			dataArray[i] = u.getBankName() + "|" + u.getBankAcct().substring(u.getBankAcct().length() - 4, u.getBankAcct().length());
		}
		dataArray[dataArray.length - 1] = "添加银行卡";

		new AlertDialog.Builder(getSherlockActivity()).setTitle("请选择存入银行卡").setSingleChoiceItems(dataArray, mCurrCheckedItem, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (which == dataArray.length - 1) {
					// to do
					// 添加银行卡
					Intent intent = new Intent(getActivity(), BindCardActivity.class);
					intent.putExtra(Cons.Intent_type, BindCardFragment.Into_SaveMoneyDialog);
					startActivity(intent);
				} else {
					mCurrCheckedItem = which;
					mCurrCardDto = list.get(which);
					setBankInfo(mCurrCardDto);
					setXieYiFlag(mCurrCardDto);
					setLimit(mCurrCardDto);
				}
			}
		}).show();

	}
}
