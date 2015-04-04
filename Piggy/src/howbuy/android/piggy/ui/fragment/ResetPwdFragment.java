package howbuy.android.piggy.ui.fragment;

import howbuy.android.lib.MyAsyncTask;
import howbuy.android.piggy.MyAlertDialog;
import howbuy.android.piggy.R;
import howbuy.android.piggy.api.DispatchAccessData;
import howbuy.android.piggy.api.dto.OneStringDto;
import howbuy.android.piggy.dialog.PiggyProgressDialog;
import howbuy.android.piggy.parameter.GlobalParams;
import howbuy.android.piggy.ui.base.AbstractFragment;
import howbuy.android.piggy.widget.CountDownButton;
import howbuy.android.util.Cons;
import howbuy.android.util.FieldVerifyUtil;
import howbuy.android.util.FieldVerifyUtil.VerifyReslt;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.actionbarsherlock.view.MenuItem;
import com.umeng.analytics.MobclickAgent;

public class ResetPwdFragment extends AbstractFragment implements OnClickListener {
	public static final String UserNoTypeDefault = "0";
	public static final int typeUserPwd = 0;
	public static final int typeTradePwd = 1;
	public static final int typeForgetPwd = 2;
	private int typeResetPwd;// 0为Login，1为trade
	howbuy.android.piggy.widget.ClearableEdittext mUserName, mUserNo;
	howbuy.android.piggy.widget.ClearableEdittext mVer;
	howbuy.android.piggy.widget.ClearableEdittext mPhone;
	private PiggyProgressDialog mpDialog;
	private CountDownButton mDownButton;
	private String mUserNoV;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final View view = inflater.inflate(R.layout.aty_resetpwd, container, false);
		view.findViewById(R.id.submit_btn).setOnClickListener(this);
		mDownButton = (CountDownButton) view.findViewById(R.id.accessmsver);
		mDownButton.setOnClickListener(this);
		mUserName = (howbuy.android.piggy.widget.ClearableEdittext) view.findViewById(R.id.user_name);
		mUserNo = (howbuy.android.piggy.widget.ClearableEdittext) view.findViewById(R.id.user_no);
		mVer = (howbuy.android.piggy.widget.ClearableEdittext) view.findViewById(R.id.verification);
		mPhone = (howbuy.android.piggy.widget.ClearableEdittext) view.findViewById(R.id.phone);
		// mUserNo.setText(getSf().getString(Cons.SFUserNOHistory, ""));
		mUserNo.setFilters(new InputFilter[] {// 大写
		new InputFilter.AllCaps() });
		
		mDownButton.setEnabled(false);
		view.findViewById(R.id.submit_btn).setEnabled(false);
		mPhone.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				if (FieldVerifyUtil.verifyPhone(phoneValue).isSuccess()) {	
				if (s.length() > 10) {
					mDownButton.setEnabled(true);
				}
//				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}
			@Override
			public void afterTextChanged(Editable s) {
				if (s.length()<11){
					mDownButton.setEnabled(false);
				}
			}
		});
		mUserName.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!(TextUtils.isEmpty(mUserNo.getEditableText().toString())
					|| 	TextUtils.isEmpty(mPhone.getEditableText().toString())
					|| 	TextUtils.isEmpty(mVer.getEditableText().toString()))){
					
					view.findViewById(R.id.submit_btn).setEnabled(true);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}
			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().equals("")){
					view.findViewById(R.id.submit_btn).setEnabled(false);
				}
			}
		});
		mUserNo.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!(TextUtils.isEmpty(mUserName.getEditableText().toString())
					|| 	TextUtils.isEmpty(mPhone.getEditableText().toString())
					|| 	TextUtils.isEmpty(mVer.getEditableText().toString()))){
					
					view.findViewById(R.id.submit_btn).setEnabled(true);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}
			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().equals("")){
					view.findViewById(R.id.submit_btn).setEnabled(false);
				}
			}
		});
		mVer.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!(TextUtils.isEmpty(mUserName.getEditableText().toString())
					|| 	TextUtils.isEmpty(mUserNo.getEditableText().toString())
					|| 	TextUtils.isEmpty(mPhone.getEditableText().toString()))){
					
					view.findViewById(R.id.submit_btn).setEnabled(true);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}
			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().equals("")){
					view.findViewById(R.id.submit_btn).setEnabled(false);
				}
			}
		});
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		typeResetPwd = getActivity().getIntent().getIntExtra(Cons.Intent_type, 0);
		String title;
		if (typeResetPwd == typeUserPwd || typeResetPwd == typeForgetPwd) {
			title = getResources().getString(R.string.modifyloginpwd);;
		} else {
			title = getResources().getString(R.string.modifytradepwd);;
		}
		getSherlockActivity().getSupportActionBar().setTitle(title);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		System.out.println(newConfig);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String title = "";
		switch (v.getId()) {
		case R.id.submit_btn:
			title = "下一步";
			resetPwd();
			break;
		case R.id.accessmsver:
			title = "获取验证码";
			sendSms();
			break;
		default:
			break;
		}
		MobclickAgent.onEvent(getActivity(), typeResetPwd == typeTradePwd ? Cons.EVENT_UI_TradePWModify : Cons.EVENT_UI_LoginPWModify, title);
	}

	private void resetPwd() {
		// TODO Auto-generated method stub
		String phoneValue = mPhone.getText().toString();
		String veri = mVer.getText().toString();
		mUserNoV = mUserNo.getText().toString();
		String userName = mUserName.getText().toString();
		String custNo = getSf().getString(Cons.SFUserCusNo, null);

		VerifyReslt msg = FieldVerifyUtil.verifyUserName(userName);
		if (!msg.isSuccess()) {
			showToastShort(msg.getMsg());
			return;
		}

		msg = FieldVerifyUtil.verifyId(mUserNoV);
		if (!msg.isSuccess()) {
			showToastShort(msg.getMsg());
			return;
		}

		msg = FieldVerifyUtil.verifyPhone(phoneValue);
		if (!msg.isSuccess()) {
			showToastShort(msg.getMsg());
			return;
		}

		msg = FieldVerifyUtil.verifyMsgVer(veri);
		if (!msg.isSuccess()) {
			showToastShort(msg.getMsg());
			return;
		}

		// if (TextUtils.isEmpty(custNo)) {
		// showToastShort("没有登录");
		// return;
		// }

		new ReretPwdTask().execute(custNo, userName, mUserNoV, UserNoTypeDefault, veri, String.valueOf(typeResetPwd), phoneValue);// custNo,
		// custName,
		// idNo,
		// dynaPwd,
		// pwdType,
		// mobile
		mpDialog = new PiggyProgressDialog(getActivity());
		mpDialog.setMessage("验证手机号...");
		mpDialog.show();
	}

	private void sendSms() {
		String phoneValue = mPhone.getText().toString();

		VerifyReslt msg = FieldVerifyUtil.verifyPhone(phoneValue);
		if (!msg.isSuccess()) {
			showToastShort(msg.getMsg());
			return;
		}

		new smsSendTask().execute(phoneValue);
		mpDialog = new PiggyProgressDialog(getActivity());
		mpDialog.setMessage("获取验证码...");
		mpDialog.show();
	}

	public class smsSendTask extends MyAsyncTask<String, Void, OneStringDto> {

		@Override
		protected OneStringDto doInBackground(String... params) {
			// TODO Auto-generated method stub
			return DispatchAccessData.getInstance().smsSend(params[0]);
		}

		@Override
		protected void onPostExecute(OneStringDto result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (mpDialog != null && mpDialog.isShowing()) {
				mpDialog.dismiss();
			}
			if (result.getContentCode() == Cons.SHOW_SUCCESS) {
				mDownButton.startCountDown();
				if (GlobalParams.getGlobalParams().isDebugFlag()) {
					String sms = result.getOneString();
					if (sms == null) {
						showToastShort("未知错误！");
					} else {
						mVer.setText(sms);
						showToastShort("验证码获取成功！");
					}
				} else {
					showToastShort("验证码获取成功！");
				}

			} else {
				showToastShort(result.getContentMsg());
			}
		}
	}

	public class ReretPwdTask extends MyAsyncTask<String, Void, OneStringDto> {
		@Override
		protected OneStringDto doInBackground(String... params) {
			// TODO Auto-generated method stub
			return DispatchAccessData.getInstance().passwordReset(params[0], params[1], params[2], params[3], params[4], params[5], params[6]);// custNo,
			// custName,
			// idNo,
			// dynaPwd,
			// pwdType,
			// mobile
			
		}

		@Override
		protected void onPostExecute(OneStringDto result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (mpDialog != null && mpDialog.isShowing()) {
				mpDialog.dismiss();
			}
			if (result.getContentCode() == Cons.SHOW_SUCCESS) {
				// showToastShort("手机验证成功！");
				addFragment();
			} else if (result.getContentCode() == Cons.SHOW_FORCELOGIN && getActivity() != null) {
				MyAlertDialog.newInstance(null).show(getFragmentManager(), "");
			}  else {
				showToastShort(result.getContentMsg());
			}
		}
	}

	public void addFragment() {
		String fgName = ResetPwdTwoFragment.class.getName();
		Bundle b = new Bundle();
		b.putInt(Cons.Intent_type, typeResetPwd);
		b.putString(Cons.Intent_id, mUserNoV);
		doAddFragment(fgName, b);
	}
}
