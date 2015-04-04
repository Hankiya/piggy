package howbuy.android.piggy.ui.fragment;

import howbuy.android.lib.MyAsyncTask;
import howbuy.android.piggy.R;
import howbuy.android.piggy.api.DispatchAccessData;
import howbuy.android.piggy.api.dto.OneStringDto;
import howbuy.android.piggy.dialog.PiggyProgressDialog;
import howbuy.android.piggy.help.NoticeHelp;
import howbuy.android.piggy.parameter.GlobalParams;
import howbuy.android.piggy.ui.base.AbsNoticeFrag;
import howbuy.android.piggy.widget.CountDownButton;
import howbuy.android.util.Cons;
import howbuy.android.util.FieldVerifyUtil;
import howbuy.android.util.FieldVerifyUtil.VerifyReslt;
import android.content.res.Configuration;
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

public class RegisterFragment extends AbsNoticeFrag implements OnClickListener {
	howbuy.android.piggy.widget.ClearableEdittext mPhoneEditText;
	howbuy.android.piggy.widget.ClearableEdittext mVerEditText;
	private PiggyProgressDialog mpDialog;
	private String mPhoneValue;
	private CountDownButton mDownButton;
	private String mVeriValue;

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
		final View view = inflater.inflate(R.layout.aty_register, container, false);
		view.findViewById(R.id.register_next_btn).setOnClickListener(this);
		view.findViewById(R.id.register_next_btn).setEnabled(false);
		mDownButton = (CountDownButton) view.findViewById(R.id.accessmsver);
		mDownButton.setOnClickListener(this);
		mDownButton.setEnabled(false);
		mPhoneEditText = (howbuy.android.piggy.widget.ClearableEdittext) view.findViewById(R.id.phone);
		mVerEditText = (howbuy.android.piggy.widget.ClearableEdittext) view.findViewById(R.id.verification);
		mPhoneEditText.addTextChangedListener(new TextWatcher() {
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
		mVerEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!TextUtils.isEmpty(mPhoneEditText.getEditableText().toString())){
					view.findViewById(R.id.register_next_btn).setEnabled(true);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}
			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().equals("")){
					view.findViewById(R.id.register_next_btn).setEnabled(false);
				}
			}
		});
		
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getSherlockActivity().setTitle("注册");
		mVerEditText.setText("");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		switch (getCurrentState(savedInstanceState)) {
		case FIRST_TIME_START:

			break;

		default:
			break;
		}
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
		switch (v.getId()) {
		case R.id.register_next_btn:
			verSms();
			// addFragment();
			MobclickAgent.onEvent(getActivity(), Cons.EVENT_UI_GetCheckCode, "获取验证码");
			break;
		case R.id.accessmsver:
			MobclickAgent.onEvent(getActivity(), Cons.EVENT_UI_GetCheckCode, "下一步");
			sendSms();
			break;
		default:
			break;
		}
	}

	private void verSms() {
		// TODO Auto-generated method stub
		String phoneValue = mPhoneEditText.getText().toString();
		mVeriValue = mVerEditText.getText().toString();
		VerifyReslt msg = FieldVerifyUtil.verifyPhone(phoneValue);
		if (!msg.isSuccess()) {
			showToastShort(msg.getMsg());
			return;
		}

		msg = FieldVerifyUtil.verifyMsgVer(mVeriValue);
		if (!msg.isSuccess()) {
			showToastShort(msg.getMsg());
			return;
		}
		mPhoneValue = phoneValue;
		new smsVeri().execute(mVeriValue, phoneValue);
		mpDialog = new PiggyProgressDialog(getActivity(),PiggyProgressDialog.TypeLoginRegister);
		mpDialog.setMessage("验证手机号...");
		mpDialog.show();
	}

	private void sendSms() {
		String phoneValue = mPhoneEditText.getText().toString();
		VerifyReslt msg = FieldVerifyUtil.verifyPhone(phoneValue);
		if (!msg.isSuccess()) {
			showToastShort(msg.getMsg());
			return;
		}
		new SmsSendTask().execute(phoneValue);
		mpDialog = new PiggyProgressDialog(getActivity(),PiggyProgressDialog.TypeLoginRegister);
		mpDialog.setMessage("获取验证码...");
		mpDialog.show();
	}

	public class SmsSendTask extends MyAsyncTask<String, Void, OneStringDto> {

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
						mVerEditText.setText(sms);
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

	public class smsVeri extends MyAsyncTask<String, Void, OneStringDto> {
		@Override
		protected OneStringDto doInBackground(String... params) {
			// TODO Auto-generated method stub
			return DispatchAccessData.getInstance().smsVeri(params[0], params[1]);
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
				// Intent in = new Intent(getActivity(),
				// RegisterTwoActivity.class);
				// in.putExtra(Cons.Intent_normal, mPhoneValue);
				// getActivity().startActivity(in);
				addFragment();
			} else {
				showToastShort(result.getContentMsg());
			}
		}
	}

	public void addFragment() {
		String fgName = RegisterTwoFragment.class.getName();
		Bundle b = new Bundle();
		b.putString(Cons.Intent_normal, mPhoneValue);
		b.putString(Cons.Intent_id,mVeriValue);
		doAddFragment(fgName, b);
	}

	@Override
	public String getNoticeType() {
		// TODO Auto-generated method stub
		return NoticeHelp.Notice_ID_Register;
	}
}
