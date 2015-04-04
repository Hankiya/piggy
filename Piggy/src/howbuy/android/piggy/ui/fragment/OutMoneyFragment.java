package howbuy.android.piggy.ui.fragment;

import howbuy.android.lib.MyAsyncTask;
import howbuy.android.piggy.MyAlertDialog;
import howbuy.android.piggy.R;
import howbuy.android.piggy.UserUtil;
import howbuy.android.piggy.api.DispatchAccessData;
import howbuy.android.piggy.api.dto.OneStringDto;
import howbuy.android.piggy.api.dto.ProductInfo;
import howbuy.android.piggy.api.dto.TradeDate;
import howbuy.android.piggy.api.dto.UserCardDto;
import howbuy.android.piggy.api.dto.UserInfoDto;
import howbuy.android.piggy.api.dto.UserLimitDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.dialog.PiggyProgressDialog;
import howbuy.android.piggy.error.HowbuyException;
import howbuy.android.piggy.help.CacheHelp;
import howbuy.android.piggy.help.NoticeHelp;
import howbuy.android.piggy.sound.SoundUtil;
import howbuy.android.piggy.ui.AbsFragmentActivity;
import howbuy.android.piggy.ui.WebViewActivity;
import howbuy.android.piggy.ui.base.AbsNoticeFrag;
import howbuy.android.piggy.ui.base.OnKeyboardVisibilityListener;
import howbuy.android.piggy.ui.fragment.PureWebFragment.PureType;
import howbuy.android.piggy.widget.ClearableEdittext;
import howbuy.android.piggy.widget.ClearableEdittext.MySelelctChanged;
import howbuy.android.piggy.widget.ImageTextBtn;
import howbuy.android.util.AndroidUtil;
import howbuy.android.util.Cons;
import howbuy.android.util.FieldVerifyUtil;
import howbuy.android.util.FieldVerifyUtil.VerifyReslt;
import howbuy.android.util.SpannableUtil;
import howbuy.android.util.StringUtil;
import howbuy.android.util.http.UrlConnectionUtil;
import howbuy.android.util.http.UrlMatchUtil;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
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
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.umeng.analytics.MobclickAgent;

public class OutMoneyFragment extends AbsNoticeFrag implements OnClickListener, OnKeyboardVisibilityListener,MySelelctChanged {
	private static final String NAME = "OutMoneyFragment";
	private PiggyProgressDialog mpDialog;
	private TextView bankinfo_name;
	private TextView bankinfo_no;
	private TextView outMoneyHint;
	private LinearLayout fastXieYi;
	private CheckBox checkBox;
	private TextView checkText;
	private ImageView bankinfo_icon;
	howbuy.android.piggy.widget.ClearableEdittext outmoneynumber;
	howbuy.android.piggy.widget.ClearableEdittext tridePwd;
	private TextView startshouyidate;
	private ImageTextBtn submitBtn;
	private UserInfoDto mUserInfoDto;
	private ProductInfo mProductInfo;
	private TradeDate mTradeDate;
	private BigDecimal maxAmount = new BigDecimal("0");
	private BigDecimal miniAmount = new BigDecimal("0");
	private UserLimitDto mLimitDto;
	// private ActionViewProgressUtil mActionViewUtil;
	private boolean isShowRefreshMenu;
	public SoundUtil mSoundUtil;
	private String outMount;
	private boolean isFast;
	private String mS;
	private UserCardDto mCurrCardDto;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		System.out.println(newConfig);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.findItem(R.id.action_refresh).setVisible(isShowRefreshMenu);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		// mActionViewUtil = new ActionViewProgressUtil(getActivity());
		getSherlockActivity().getSupportMenuInflater().inflate(R.menu.outmoney, menu);
		// mActionViewUtil.setRefreshItem(menu.findItem(R.id.action_refresh));
	}

	private void showActionMenu(boolean isShow) {
		isShowRefreshMenu = isShow;
		getSherlockActivity().invalidateOptionsMenu();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			getActivity().finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return super.onBackPressed();
	}

	private void setClickText() {
		SpannableString s1 = SpannableUtil.all("我已阅读并同意", 14, R.color.text_page_gray, false);
		SpannableString s2 = SpannableUtil.all("《好买快速取钱协议》", 14, R.color.text_page_gray, true);
		SpannableStringBuilder s = new SpannableStringBuilder();
		s.append(s1).append(s2);
		checkText.setText(s);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		// mActionViewUtil = new ActionViewProgressUtil(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.aty_outmoney, container, false);
		bankinfo_name = (TextView) view.findViewById(R.id.bankinfo_name);
		bankinfo_no = (TextView) view.findViewById(R.id.bankinfo_no);
		bankinfo_icon = (ImageView) view.findViewById(R.id.bankinfo_icon);
		outmoneynumber = (howbuy.android.piggy.widget.ClearableEdittext) view.findViewById(R.id.outmoneynumber);
		outMoneyHint = (TextView) view.findViewById(R.id.outmoneyhint);
		tridePwd = (howbuy.android.piggy.widget.ClearableEdittext) view.findViewById(R.id.trade_pwd);

		startshouyidate = (TextView) view.findViewById(R.id.belikedate);
		submitBtn = (ImageTextBtn) view.findViewById(R.id.submit_btn);
		fastXieYi = (LinearLayout) view.findViewById(R.id.fastxieyi);
		checkBox = (CheckBox) view.findViewById(R.id.checkf);
		checkText = (TextView) view.findViewById(R.id.clecktext);
		checkText.setText(Html.fromHtml("我已阅读并同意<font color=#2a5894>《好买快速取钱协议》</font>"));
		checkText.setOnClickListener(this);
		submitBtn.setOnClickListener(this);
		submitBtn.setEnabled(false);
		outmoneynumber.setMySelelctChanged(this);
		outmoneynumber.addTextChangedListener(new TextWatcher() {
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
				setDaoDate(mTradeDate);

				String a = s.toString().replaceAll(",", "");
				String b = mS.replaceAll(",", "");
				if (!a.equals(b) && !a.endsWith(".")&&!(a.contains(".")&&a.subSequence(a.length()-2, a.length()-1).equals("."))) {
					String text = StringUtil.formatAll(a, StringUtil.floatFormatSaveOrOut);
					mS = text;
					outmoneynumber.setText(text);
					outmoneynumber.setSelection(text.length());
				}
			}
		});
		tridePwd.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!(TextUtils.isEmpty(outmoneynumber.getEditableText().toString()))) {
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
//		bankinfo_icon.setImageResource(UserUtil.userBankIcon());
		mProductInfo = ApplicationParams.getInstance().getPiggyParameter().getProductInfo();
		mUserInfoDto = ApplicationParams.getInstance().getPiggyParameter().getUserInfo();
		mTradeDate = ApplicationParams.getInstance().getPiggyParameter().getTwoDay();
		mCurrCardDto=getSherlockActivity().getIntent().getParcelableExtra(Cons.Intent_bean);
		tridePwd.setClearType(ClearableEdittext.TypePas);
		initParam();
		setCunZuiDi(mProductInfo);
		setBankInfo(mCurrCardDto);
		setAvailableQu(mUserInfoDto);
		// setClickText();
		setDaoDate(mTradeDate);
		setTask(mUserInfoDto);
		new QueryLimit().execute();
	}

	@Override
	public void onResume() {
		super.onResume();
		if ((mUserInfoDto != null) && (mUserInfoDto.contentCode == Cons.SHOW_FORCELOGIN)) {
			MyAlertDialog.newInstance(null).show(getFragmentManager(), "");
		}
	}

	private void initParam() {
		try {

			if (mProductInfo != null) {
				miniAmount = new BigDecimal(mProductInfo.getLowestRedemption());
			}
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			miniAmount=new BigDecimal("1");
		}
		
		
		try {
			if (mCurrCardDto != null) {
				maxAmount = new BigDecimal(mCurrCardDto.getAvailAmt());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
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
			in.putExtra(Cons.Intent_id, "file:///android_asset/fastout.html");
			in.putExtra(Cons.Intent_name, "好买快速取现协议");
			startActivity(in);
			break;
		case R.id.submit_btn:
			outMount = outmoneynumber.getEditableText().toString().replace(",", "");
			String tradePwdValue = tridePwd.getEditableText().toString();

			if (mProductInfo == null || mUserInfoDto == null) {
				return;
			}

			if (checkIsFast().isSuccess() && checkBox.isChecked() == false) {
				showToastShort("请同意好买快速取现协议");
				return;
			}

			if (outMount.startsWith(".")) {
				outmoneynumber.setText("");
				showToastShort("金额非法！");
				return;
			}

			if (outMount.length() == 1 && outMount.endsWith(".")) {
				outmoneynumber.setText("");
				showToastShort("金额非法！");
				return;
			}

			VerifyReslt vReslt = FieldVerifyUtil.verifyAmount(outMount, 2);
			if (!vReslt.isSuccess()) {
				showToastShort(vReslt.getMsg());
				return;
			}

			BigDecimal amount = new BigDecimal(outMount);
			if (amount.compareTo(maxAmount) == 1) {
				showToastShort("金额超限");
				return;
			}

			BigDecimal amountMini = new BigDecimal(outMount);
			if (amount.compareTo(amountMini) == -1) {
				showToastShort("取款金额低于规定下限");
				return;
			}

			vReslt = FieldVerifyUtil.verifyTradePwd(tradePwdValue);
			if (!vReslt.isSuccess()) {
				showToastShort(vReslt.getMsg());
				return;
			}
			d("res2--" + outMount);
			//custNo, redeemShare, txpwd, custBankId
			new OutMoneyTask().executeOnExecutor(Executors.newCachedThreadPool(),outMount, tradePwdValue,mCurrCardDto.getCustBankId());
			mpDialog = new PiggyProgressDialog(getActivity());
			mpDialog.setCancelable(false);
			mpDialog.setCanceledOnTouchOutside(false);
			mpDialog.setMessage("取钱中...");
			mpDialog.show();
			MobclickAgent.onEvent(getActivity(), Cons.EVENT_UI_GetMoney, "确认取出");
			break;
		default:
			break;
		}
	}

	public class QueryLimit extends MyAsyncTask<Void, Void, UserLimitDto> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showActionMenu(true);
		}

		@Override
		protected UserLimitDto doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String custNo = getSf().getString(Cons.SFUserCusNo, null);// 6227001215800062891
			UserLimitDto uLimitDto = null;
			try {
				uLimitDto = DispatchAccessData.getInstance().queryUserLimit(custNo,mCurrCardDto.getCustBankId(),CacheHelp.cachetime_one_day);
			} catch (Exception e) {
				// TODO: handle exception
				uLimitDto = new UserLimitDto();
				uLimitDto.setContentCode(Cons.SHOW_ERROR);
				uLimitDto.setContentMsg("数据解析错误");
				e.printStackTrace();
			}
			return uLimitDto;
		}

		@Override
		protected void onPostExecute(UserLimitDto result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (mpDialog != null && mpDialog.isShowing()) {
				mpDialog.dismiss();
			}
			if (result != null && result.getContentCode() == Cons.SHOW_SUCCESS && getActivity() != null) {
				if (result.getContentCode() == Cons.SHOW_SUCCESS && getActivity() != null) {
					mLimitDto = result;
					showActionMenu(false);
					setDaoDate(mTradeDate);
				}
			} else if (result.getContentCode() == Cons.SHOW_FORCELOGIN && getActivity() != null) {
				MyAlertDialog.newInstance(null).show(getFragmentManager(), "");
			} else if (getActivity() != null) {
				String res = result.getContentMsg();
				showToastShort(res);
			}
		}

	}

	public class OutMoneyTask extends MyAsyncTask<String, Void, OneStringDto> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showActionMenu(true);
		}

		@Override
		protected OneStringDto doInBackground(String... params) {
			// TODO Auto-generated method stub
			String cusNo = getSf().getString(Cons.SFUserCusNo, null);// 6227001215800062891
			//custNo, redeemShare, txpwd, custBankId
			return DispatchAccessData.getInstance().quQian(cusNo,params[0],params[1],params[2]);
		}

		@Override
		protected void onPostExecute(OneStringDto result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			showActionMenu(false);
			if (result.getContentCode() == Cons.SHOW_SUCCESS && getActivity() != null) {
				Handler handler = new Handler() {
					public void handleMessage(android.os.Message msg) {
						if (msg.what == Cons.SHOW_SUCCESS) {
							if (mpDialog != null && mpDialog.isShowing()) {
								mpDialog.dismiss();
							}
							// 播放声音
							mSoundUtil = new SoundUtil(ApplicationParams.getInstance());
							mSoundUtil.playSound(R.raw.save_money);
							Intent intent = new Intent(getActivity().getApplicationContext(), AbsFragmentActivity.class);
							intent.putExtra(Cons.Intent_name, "howbuy.android.piggy.ui.fragment.OpeatSuccessFragment");
							intent.putExtra(Cons.Intent_id, OpeatSuccessFragment.TypeOutMoneySuccess);
							intent.putExtra(Cons.Intent_bean, outMount);
							intent.putExtra(Cons.Intent_normal, isFast);
							getActivity().startActivity(intent);
							getActivity().finish();
						}
					};
				};
				// 进行第二个动画
				mpDialog.startAnimation1(R.drawable.progress_piggy_down);
				// 进行第二个动画完成时的动作
				handler.sendEmptyMessageDelayed(Cons.SHOW_SUCCESS, getResources().getInteger(R.integer.progresstime) * 13);

			} else if (result.getContentCode() == Cons.SHOW_FORCELOGIN && getActivity() != null) {
				if (mpDialog != null && mpDialog.isShowing()) {
					mpDialog.dismiss();
				}
				MyAlertDialog.newInstance(null).show(getFragmentManager(), "");
			} else if (getActivity() != null) {
				if (mpDialog != null && mpDialog.isShowing()) {
					mpDialog.dismiss();
				}
				String res = result.getContentMsg();
				if (res.equals(UrlConnectionUtil.ErrorNetWorkTimeOut)) {
					res = "请求已发起，等待系统确认，请稍后在交易查询-交易历史里查询订单状态";
				}
				showToastShort(res);
				tridePwd.setText("");
			}
		}
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
	 * 设置可用取得
	 * 
	 * @param productInfo
	 */
	public void setAvailableQu(UserInfoDto accessInfo) {
		if (accessInfo == null) {
			return;
		}

		String temp = StringUtil.formatCurrency(String.valueOf(maxAmount));
		SpannableStringBuilder sBuilder = new SpannableStringBuilder();
		sBuilder.append(SpannableUtil.all("当前", 14, R.color.text_page, false)).append(SpannableUtil.all(String.valueOf(temp), 14, R.color.actioncolor, false))
				.append(SpannableUtil.all("元可取", 14, R.color.text_page, false));
		outMoneyHint.setText(sBuilder);
	}

	/**
	 * 设置用户最低存款
	 * 
	 * @param productInfo
	 */
	public void setCunZuiDi(ProductInfo productInfo) {
		try {
			if (productInfo == null) {
				return;
			}
			String lowest = String.valueOf(StringUtil.formatCurrency(miniAmount.toString()));
			outmoneynumber.setHint("≥" + lowest + "元" + getResources().getString(R.string.freefee));
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * 检查是否可以快速取现
	 */
	private VerifyReslt checkIsFast() {
		VerifyReslt vReslt = new VerifyReslt("", false);
		if (mLimitDto == null) {
			return vReslt;
		}

		if (Float.parseFloat(mLimitDto.getLastAmt()) == 0.0f) {
			vReslt.setMsg("剩余额度不足");
			return vReslt;
		}

		if (TextUtils.isEmpty(mLimitDto.getOpenFlag()) || mLimitDto.getOpenFlag().equals(UserLimitDto.FlagClose)) {
			vReslt.setMsg("快速取现未开通");
			return vReslt;
		}

		String amt = outmoneynumber.getText().toString().replace(",", "");
		if (!TextUtils.isEmpty(amt)) {
			BigDecimal b1 = new BigDecimal(amt);
			BigDecimal b2 = new BigDecimal(mLimitDto.getLastAmt());
			int res = b1.compareTo(b2);// 判断剩余额度
			if (res == 1) {// 大于
				vReslt.setMsg("金额超过最大剩余额度");
				System.out.println(vReslt.toString());
				return vReslt;
			} else if (res == -1) {// 小于

			} else {// 等于

			}

			BigDecimal once = new BigDecimal(mLimitDto.getMaxAmtEach());
			int res2 = b1.compareTo(once);// 比较单笔额度
			if (res2 == 1) {// 大于
				vReslt.setMsg("金额超过最大单笔额度");
				System.out.println(vReslt.toString());
				return vReslt;
			}

		}
		vReslt.setSuccess(true);
		return vReslt;
	}

	/**
	 * 设置预期到期日期
	 */
	public void setDaoDate(TradeDate tDate) {
		if (tDate == null) {
			return;
		}
		String t = outmoneynumber.getText().toString();
		if (TextUtils.isEmpty(t)) {
			startshouyidate.setVisibility(View.GONE);
			fastXieYi.setVisibility(View.GONE);
			return;
		} else {
			fastXieYi.setVisibility(View.VISIBLE);
			startshouyidate.setVisibility(View.VISIBLE);
		}

		String date = tDate.getPaymentReceiptDt();
		VerifyReslt v = checkIsFast();
		if (v.isSuccess()) {
			isFast = true;
			SpannableStringBuilder sBuilder = new SpannableStringBuilder();
			sBuilder.append(SpannableUtil.all("到款时间：", 14, R.color.text_page, false)).append(SpannableUtil.all("5分钟内", 14, R.color.actioncolor, false));
			startshouyidate.setText(sBuilder);

			fastXieYi.setVisibility(View.VISIBLE);
		} else {
			isFast = false;
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
			sBuilder.append(SpannableUtil.all("到款时间：", 14, R.color.text_page, false)).append(SpannableUtil.all(res, 14, R.color.actioncolor, false));
			startshouyidate.setText(sBuilder);
			fastXieYi.setVisibility(View.GONE);
		}

	}

	@Override
	public String getNoticeType() {
		// TODO Auto-generated method stub
		return NoticeHelp.Notice_ID_Out;
	}

	public void setTask(UserInfoDto infoDto) {
		if (infoDto != null) {
			new BankIconTask().executeOnExecutor(Executors.newCachedThreadPool(),mCurrCardDto.getBankCode());
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
			}else if (isAdded()) {
				bankinfo_icon.setImageResource(UserUtil.userBankIcon(mCurrCardDto.getBankCode()));
			}
		}

		private String iconUrl(String bankCode) {
			String basePath2 = UrlMatchUtil.getBasepath2() + AndroidUtil.getSourceFolderName() + "/" + bankCode + ".png";
			return basePath2;
		}
	}
	

	@Override
	public void onSelectionChanged(int selStart, int selEnd) {
		// TODO Auto-generated method stub
		Log.d(NAME, "onSelectionChanged--"+"selStart="+selStart+"--selEnd"+selEnd);
		int offsetPos=outmoneynumber.getEditableText().length();
		if (selStart==selEnd&&selEnd!=offsetPos) {
			outmoneynumber.setSelection(offsetPos);
		}
		
		
	}

}
