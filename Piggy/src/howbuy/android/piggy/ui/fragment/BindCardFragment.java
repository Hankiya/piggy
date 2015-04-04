package howbuy.android.piggy.ui.fragment;

import howbuy.android.piggy.R;
import howbuy.android.piggy.api.dto.SupportBankAndProvinceDto;
import howbuy.android.piggy.api.dto.UserCardDto;
import howbuy.android.piggy.api.dto.UserCardListDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.dialog.PiggyProgressDialog;
import howbuy.android.piggy.dialogfragment.DialogBankBanchFragment;
import howbuy.android.piggy.dialogfragment.DialogBankBanchFragment.OnBankBanchLinster;
import howbuy.android.piggy.dialogfragment.DialogBankProviceFragment;
import howbuy.android.piggy.dialogfragment.DialogBankProviceFragment.SpinnerSelect;
import howbuy.android.piggy.service.ServiceMger.TaskBean;
import howbuy.android.piggy.service.UpdateUserDataService;
import howbuy.android.piggy.ui.BindCardSucceedActivity;
import howbuy.android.piggy.ui.WebViewActivity;
import howbuy.android.piggy.ui.base.AbstractFragment;
import howbuy.android.piggy.ui.fragment.PureWebFragment.PureType;
import howbuy.android.piggy.widget.ClearableEdittext.MyTextChangeListen;
import howbuy.android.piggy.widget.ImageTextBtn;
import howbuy.android.util.Cons;
import howbuy.android.util.CpUtil;
import howbuy.android.util.SpannableUtil;
import howbuy.android.util.StringUtil;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.google.myjson.Gson;
import com.umeng.analytics.MobclickAgent;

public class BindCardFragment extends AbstractFragment implements OnClickListener, SpinnerSelect, OnBankBanchLinster, MyTextChangeListen {
	public static final int Into_SaveMoney = 1 << 5;
	public static final int Into_OutMoney = 1 << 6;
	public static final int Into_Register = 1 << 7;
	public static final int Into_Setting = 1 << 8;
	public static final int Into_SaveMoneyDialog = 1 << 9;
	public static final int Into_SettingCardList = 1 << 10;
	public static final int typeBank = 1;
	public static final int typeProvince = 2;
	public static final String BindCardRequsetNormal = "BindCardRequsetNormal";
	howbuy.android.piggy.widget.ClearableEdittext mEditBankNo;
	private Button provinceBtn, bankBtn, bankBranchBtn;
	private ImageTextBtn submitBtn;
	private PiggyProgressDialog mpDialog;
	private LinearLayout fastXieYi;
	private CheckBox checkBox;
	private TextView checkText;
	private SupportBankAndProvinceDto provinceAndBank;
	private DialogBankBanchFragment sBanchFragment;
	private String bankCode;
	private String provinceCode;
	private String bankBranchCode;
	private String bankBranchName;

	private int intoType = Into_Setting;
	private String mS = "";
	private CpUtil mCpUtil;
	private UserCardDto mCurrCardDto;
	

	@Override
	public boolean isShoudRegisterReciver() {
		return true;
	};

	@Override
	public void onServiceRqCallBack(Intent taskData, boolean isCurrPage) {
		// TODO Auto-generated method stub
		super.onServiceRqCallBack(taskData, isCurrPage);
		Log.d("service", "onServiceRqCallBack--BindCardFragment");
		mCpUtil.dissmisProgressDialog();
		String taskType = taskData.getStringExtra(Cons.Intent_type);
		mCpUtil.dissmisProgressDialog();
		if (UpdateUserDataService.TaskType_BankProvince.equals(taskType)) {
			provinceAndBank = ApplicationParams.getInstance().getPiggyParameter().getSupportBankInfo();
		} else if (UpdateUserDataService.TaskType_UserCard.equals(taskType)&&isCurrPage) {
			UserCardListDto userCards = taskData.getParcelableExtra(Cons.Intent_bean);
			String reqId = taskData.getStringExtra(Cons.Intent_id);
			if (BindCardRequsetNormal.equals(reqId)) {// 普通绑卡流程
				jumpIntent();
			} else {
				if (userCards != null && userCards.getContentCode() == Cons.SHOW_SUCCESS) {
					mCpUtil.handleStepRqRes(intoType, reqId, userCards, mCurrCardDto);
				} else {
					showToastTrue("获取用户信息失败");
				}
			}
		}

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
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onResume() {
		super.onResume();
		mCpUtil.onResumeCp();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.aty_bindcard, container, false);
		mEditBankNo = (howbuy.android.piggy.widget.ClearableEdittext) view.findViewById(R.id.bankno);
		bankBtn = (Button) view.findViewById(R.id.select_idno_type);
		provinceBtn = (Button) view.findViewById(R.id.provice_btn);
		bankBranchBtn = (Button) view.findViewById(R.id.bankbranch_btn);
		submitBtn = (ImageTextBtn) view.findViewById(R.id.submit_btn);
		fastXieYi = (LinearLayout) view.findViewById(R.id.fastxieyi);
		checkBox = (CheckBox) view.findViewById(R.id.checkf);
		checkText = (TextView) view.findViewById(R.id.clecktext);
		checkText.setText(Html.fromHtml("我已阅读并同意<font color=#2a5894>《银行自动转账授权书》</font>"));
		checkText.setOnClickListener(this);
		bankBtn.setOnClickListener(this);
		provinceBtn.setOnClickListener(this);
		bankBranchBtn.setOnClickListener(this);
		submitBtn.setOnClickListener(this);
		mEditBankNo.setmTextChangeListen(this);
		// meEditPwd = (howbuy.android.piggy.widget.ClearableEdittext)
		// view.findViewById(R.id.user_pwd);
		submitBtn.setEnabled(false);
		bankBranchBtn.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!TextUtils.isEmpty(mEditBankNo.getEditableText().toString())) {
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
//		String gson = getSf().getString(Cons.SFBindCardParams, null);
//		if (!TextUtils.isEmpty(gson)) {
//			UserCardDto b = new Gson().fromJson(gson, UserCardDto.class);
//			restoreBindCardParams(b);
//		}

		return view;
	}

	private void restoreBindCardParams(UserCardDto b) {
		this.bankCode = b.getBankCode();
		// b.getCustNo(), b.getBankNo(), b.getBankCode(), b.getBankBranchCode(),
		// b.getBankBranchName(), b.getProvinceCode(), b.getBankBranchCityCode()
		this.bankBranchCode = b.getBankRegionName();
		this.bankBranchName = b.getBankRegionCode();
		this.provinceCode = b.getProvCode();
		String bankNo = b.getBankCode();
		String bankName = b.getBankName();
		String provinceName = b.getProvCode();

		bankBtn.setText(bankName);
		mEditBankNo.setText("test");
		mEditBankNo.setText(bankNo);
		provinceBtn.setText(provinceName);
		onSelect(typeProvince, provinceName, provinceCode);

		bankBranchBtn.setText(bankBranchName);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		// new ProvinceAndBankTask().execute();
		provinceAndBank = ApplicationParams.getInstance().getPiggyParameter().getSupportBankInfo();
		intoType = getActivity().getIntent().getIntExtra(Cons.Intent_type, Into_Setting);
		mCpUtil=new CpUtil(intoType,this);
		if (null == provinceAndBank) {
			ApplicationParams.getInstance().getServiceMger().addTask(new TaskBean(UpdateUserDataService.TaskType_BankProvince, "0"));
			mCpUtil.showProgressDialog("正在获取数据");
		}
	}

	private void setXieyi() {
		SpannableString s1 = SpannableUtil.all("我已阅读并同意", 14, R.color.text_link, false);
		SpannableString s2 = SpannableUtil.all("《银行自动转账授权书》", 14, R.color.blue, true);
		SpannableStringBuilder s = new SpannableStringBuilder();
		s.append(s1).append(s2);
		checkText.setText(s);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		DialogBankProviceFragment dSpinnerDialogFragment;
		switch (v.getId()) {
		case R.id.clecktext:
			Intent in1 = new Intent(getActivity(), WebViewActivity.class);
			in1.putExtra(Cons.Intent_type, PureType.urlWearOperal.getType());
			in1.putExtra(Cons.Intent_id, "file:///android_asset/autotransferprococal.html");
			in1.putExtra(Cons.Intent_name, "银行自动转账授权书");
			startActivity(in1);
			break;
		case R.id.select_idno_type:
			dSpinnerDialogFragment = DialogBankProviceFragment.newInstance(typeBank, provinceAndBank, this);
			dSpinnerDialogFragment.show(getSherlockActivity().getSupportFragmentManager(), String.valueOf(typeBank));
			break;
		case R.id.provice_btn:
			dSpinnerDialogFragment = DialogBankProviceFragment.newInstance(typeProvince, provinceAndBank, this);
			dSpinnerDialogFragment.show(getSherlockActivity().getSupportFragmentManager(), String.valueOf(typeProvince));
			break;
		case R.id.bankbranch_btn:
			if (TextUtils.isEmpty(bankCode) || TextUtils.isEmpty(provinceCode)) {
				showToastShort("请选择银行和开户省份");
				return;
			}
			if (sBanchFragment == null) {
				// isFirstClick=false;
				sBanchFragment = DialogBankBanchFragment.newInstance(bankCode, provinceCode, this);
			}
			sBanchFragment.show(getSherlockActivity().getSupportFragmentManager(), String.valueOf(typeProvince + typeBank));
			break;
		// case R.id.register_btn:
		// Intent in = new Intent(getActivity(), RegisterActivity.class);
		// startActivity(in);
		// break;
		case R.id.submit_btn:
			MobclickAgent.onEvent(getActivity(), Cons.EVENT_UI_BindCard, "确认绑定");
			String bankNo = mEditBankNo.getEditableText().toString();
			bankNo = bankNo.replaceAll(" ", "");
			String custNo = getSf().getString(Cons.SFUserCusNo, null);

			if (TextUtils.isEmpty(custNo)) {
				showToastShort("您还未登录");
				return;
			}

			if (TextUtils.isEmpty(bankCode)) {
				showToastShort("请选择银行");
				return;
			}

			if (TextUtils.isEmpty(bankNo)) {
				showToastShort("请填写帐号");
				return;
			}

			if (bankNo.length() < 10) {
				showToastShort("账号填写错误");
				return;
			}

			if (TextUtils.isEmpty(provinceCode)) {
				showToastShort("请选择省份");
				return;
			}

			if (TextUtils.isEmpty(bankBranchCode)) {
				showToastShort("请选择支行");
				return;
			}

			if (checkBox.isChecked() == false) {
				showToastShort("请同意银行自动转账存款授权书");
				return;
			}
			// save params
			//String bankAcct, String bankCode, String bankName, String provCode, String provName, String bankRegionCode,String bankRegionName, String custBankId
			UserCardDto card=new UserCardDto(bankNo,bankCode,  bankBtn.getText().toString(), provinceCode, provinceBtn.getText().toString(), bankBranchCode, bankBranchName, null);
			mCurrCardDto=card;
			mCpUtil.startBindCard(card);
			Gson g = new Gson();
			String gson = g.toJson(card);
			getSf().edit().putString(Cons.SFBindCardParams, gson).commit();

			break;
		default:
			break;
		}
	}


	private void jumpIntent() {
		Intent ien = new Intent();
		ien.putExtra(Cons.Intent_type, intoType);
		ien.putExtra(Cons.Intent_id, BindCardSucceedFragment.Type_Res_BindCardSuccess);
		ien.setClass(getActivity(), BindCardSucceedActivity.class);
		startActivity(ien);
		getSherlockActivity().finish();
	}

	@Override
	public void onSelect(int mType, String bankAndProviceName, String bankAndProviceCode) {
		// TODO Auto-generated method stub
		System.out.println("onSelect");
		if (mType == BindCardFragment.typeBank) {
			if (!bankAndProviceName.equals(bankBtn.getText().toString())) {
				provinceBtn.setText("请选择");
				provinceCode = null;
				bankBranchBtn.setText("请选择");
				bankBranchCode = null;
			}
			bankBtn.setText(bankAndProviceName);
			bankCode = bankAndProviceCode;

		} else {
			if (!bankAndProviceName.equals(provinceBtn.getText().toString())) {
				bankBranchBtn.setText("请选择");
				bankBranchCode = null;
			}
			provinceBtn.setText(bankAndProviceName);
			provinceCode = bankAndProviceCode;
		}
		createData(mType, bankAndProviceCode);
	}

	@Override
	public void onBankBranchClick(String id, String bankBranchName, String bankBranchCityCode) {
		// TODO Auto-generated method stub
		this.bankBranchName = bankBranchName;
		bankBranchBtn.setText(bankBranchName);
		bankBranchCode = id;
	}

	public void createData(int mType, String bankAndProviceName) {
		if (TextUtils.isEmpty(bankCode) || TextUtils.isEmpty(provinceCode)) {
			return;
		}
		sBanchFragment = DialogBankBanchFragment.newInstance(bankCode, provinceCode, this);
		sBanchFragment.outDoTask(bankCode, provinceCode, DialogBankBanchFragment.typeLoadOut);
	}

	@Override
	public void onNoData(boolean isNodata) {
		// TODO Auto-generated method stub
		if (isNodata) {
			showToastShort("没有支行数据");
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub
		d("beforeTextChanged--" + s.toString());
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		d("onTextChanged--" + s.toString());
		if (TextUtils.isEmpty(mS)) {
			mS = s.toString();
		}

		if (!TextUtils.isEmpty(bankBranchCode)) {
			submitBtn.setEnabled(true);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		d("afterTextChanged--" + s.toString());
		d("afterTextChanged ms--" + mS.toString());
		String a = s.toString().replaceAll(" ", "");
		String b = mS.replaceAll(" ", "");

		if (!a.equals(b)) {
			String text = StringUtil.formatInputBankCard(a);
			mS = text;
			mEditBankNo.setText(text);
			mEditBankNo.setSelection(text.length());
		}

		if (s.toString().equals("")) {
			submitBtn.setEnabled(false);
		}
	}


}
