package howbuy.android.piggy.ui.fragment;

import howbuy.android.lib.MyAsyncTask;
import howbuy.android.piggy.MyAlertDialog;
import howbuy.android.piggy.R;
import howbuy.android.piggy.api.DispatchAccessData;
import howbuy.android.piggy.api.dto.OneStringDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.dialog.PiggyProgressDialog;
import howbuy.android.piggy.parameter.GlobalParams;
import howbuy.android.piggy.ui.SettingAccountActivity;
import howbuy.android.piggy.ui.base.AbstractFragment;
import howbuy.android.piggy.widget.ClearableEdittext;
import howbuy.android.piggy.widget.CountDownButton;
import howbuy.android.piggy.widget.ImageTextBtn;
import howbuy.android.util.Cons;
import howbuy.android.util.FieldVerifyUtil;
import howbuy.android.util.FieldVerifyUtil.VerifyReslt;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.actionbarsherlock.view.MenuItem;
import com.umeng.analytics.MobclickAgent;

public class ResetPhoneFragment extends AbstractFragment implements OnClickListener {
	howbuy.android.piggy.widget.ClearableEdittext mPhone, mViry;
	howbuy.android.piggy.widget.ClearableEdittext mTrade;
	private ImageTextBtn mSubmitBtn;
	private PiggyProgressDialog mpDialog;
	private CountDownButton mDownButton;

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
		View view = inflater.inflate(R.layout.aty_resetphone, container, false);

		mDownButton = (CountDownButton) view.findViewById(R.id.accessmsver);
		mDownButton.setOnClickListener(this);
		mPhone = (howbuy.android.piggy.widget.ClearableEdittext) view.findViewById(R.id.phone);
		mViry = (howbuy.android.piggy.widget.ClearableEdittext) view.findViewById(R.id.verification);
		mTrade = (howbuy.android.piggy.widget.ClearableEdittext) view.findViewById(R.id.trade_pwd);
		mSubmitBtn = (ImageTextBtn) view.findViewById(R.id.submit_btn);
		mSubmitBtn.setOnClickListener(this);
		mPhone.setHint(R.string.phonenewhint);
		
		mDownButton.setEnabled(false);
		mSubmitBtn.setEnabled(false);
		mTrade.setClearType(ClearableEdittext.TypePas);
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
		
		mViry.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!(TextUtils.isEmpty(mPhone.getEditableText().toString())
					|| 	TextUtils.isEmpty(mTrade.getEditableText().toString()))){
					mSubmitBtn.setEnabled(true);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}
			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().equals("")){
					mSubmitBtn.setEnabled(false);
				}
			}
		});
		mTrade.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!(TextUtils.isEmpty(mPhone.getEditableText().toString())
					|| 	TextUtils.isEmpty(mViry.getEditableText().toString()))){
					mSubmitBtn.setEnabled(true);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}
			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().equals("")){
					mSubmitBtn.setEnabled(false);
				}
			}
		});
		
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.submit_btn:
			resetPhone();
			MobclickAgent.onEvent(getActivity(), Cons.EVENT_UI_MobilePhoneModify, "确认修改");
			break;
		case R.id.accessmsver:
			sendSms();
			MobclickAgent.onEvent(getActivity(), Cons.EVENT_UI_MobilePhoneModify, "获取验证码");
			break;
		default:
			break;
		}
	}

	private void resetPhone() {
		// TODO Auto-generated method stub
		String phoneValue = mPhone.getText().toString();
		String vire = mViry.getText().toString();
		String tradePwd = mTrade.getText().toString();
		String custNo = getSf().getString(Cons.SFUserCusNo, null);
		VerifyReslt msg = FieldVerifyUtil.verifyPhone(phoneValue);
		if (!msg.isSuccess()) {
			showToastShort(msg.getMsg());
			return;
		}
		msg = FieldVerifyUtil.verifyMsgVer(vire);
		if (!msg.isSuccess()) {
			showToastShort(msg.getMsg());
			return;
		}
		msg = FieldVerifyUtil.verifyTradePwd(tradePwd);
		if (!msg.isSuccess()) {
			showToastShort(msg.getMsg());
			return;
		}

		if (TextUtils.isEmpty(custNo)) {
			showToastShort("用户没有登录");
			return;
		}
		new ResetPhoneTask().execute(custNo, phoneValue, vire, tradePwd);
		mpDialog = new PiggyProgressDialog(getActivity());
		mpDialog.setMessage("正在提交新手机号...");
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
		mpDialog.setMessage("获取修改手机号...");
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
						mViry.setText(sms);
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

	/**
	 * 手机号重置
	 * 
	 * @ClassName: ResetPhoneFragment.java
	 * @Description:
	 * @author yescpu yes.cpu@gmail.com
	 * @date 2013-10-14上午10:32:16
	 */
	public class ResetPhoneTask extends MyAsyncTask<String, Void, OneStringDto> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected OneStringDto doInBackground(String... params) {
			// TODO Auto-generated method stub
			return DispatchAccessData.getInstance().updateMobile(params[0], params[1], params[2], params[3]);// custNo,
																												// mobile,
																												// dynaPwd,
																												// txPassword
		}

		@Override
		protected void onPostExecute(OneStringDto result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (mpDialog != null && mpDialog.isShowing()) {
				mpDialog.dismiss();
			}
			if (result.getContentCode() == Cons.SHOW_SUCCESS) {
				showToastTrueLong("修改手机号成功！");
				luncherMain();
			} else if (result.getContentCode() == Cons.SHOW_FORCELOGIN && getActivity() != null) {
				MyAlertDialog.newInstance(null).show(getFragmentManager(), "");
			}  else {
				showToastShort(result.getContentMsg());
			}
		}
	}

	private void luncherMain() {
		Intent intent = new Intent(ApplicationParams.getInstance().getApplicationContext(), SettingAccountActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
}
