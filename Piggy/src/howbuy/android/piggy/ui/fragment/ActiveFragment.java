package howbuy.android.piggy.ui.fragment;

import howbuy.android.lib.MyAsyncTask;
import howbuy.android.piggy.R;
import howbuy.android.piggy.api.DispatchAccessData;
import howbuy.android.piggy.api.dto.OneStringDto;
import howbuy.android.piggy.api.dto.UserTypeDto;
import howbuy.android.piggy.dialog.PiggyProgressDialog;
import howbuy.android.piggy.dialogfragment.DialogSelectFragment;
import howbuy.android.piggy.dialogfragment.DialogSelectFragment.SpinnerSelect;
import howbuy.android.piggy.parameter.GlobalParams;
import howbuy.android.piggy.ui.base.AbstractFragment;
import howbuy.android.piggy.widget.ClearableEdittext;
import howbuy.android.piggy.widget.CountDownButton;
import howbuy.android.piggy.widget.ImageTextBtn;
import howbuy.android.util.Cons;
import howbuy.android.util.FieldVerifyUtil;
import howbuy.android.util.FieldVerifyUtil.VerifyReslt;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;

/**
 * 
 * @author yesCpu
 * 
 */
public class ActiveFragment extends AbstractFragment implements OnClickListener, SpinnerSelect {
	public static final int ActiveType_Login = 0;
	public static final int ActiveType_Register = 1;

	private PiggyProgressDialog mpDialog;
	private int mActiveType = ActiveType_Login;
	private TextView mIdNo;
	private Button mSelectMobile;
	private CountDownButton mGetScrict;
	private ClearableEdittext mVerCode;
	private ImageTextBtn mSubmitBtn;
	private UserTypeDto mUserTypeDto;
	private String mUserNo;
	private String mMobileValue;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
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
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putAll(getActivity().getIntent().getExtras());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.aty_active, container, false);
		mIdNo = (TextView) view.findViewById(R.id.idNo);
		mSelectMobile = (Button) view.findViewById(R.id.select_mobile);
		mGetScrict = (CountDownButton) view.findViewById(R.id.accessmsver);
		mVerCode = (ClearableEdittext) view.findViewById(R.id.verification);
		mSubmitBtn = (ImageTextBtn) view.findViewById(R.id.submit_btn);
		mSubmitBtn.setOnClickListener(this);
		mGetScrict.setOnClickListener(this);
		mSelectMobile.setOnClickListener(this);
		mSubmitBtn.setEnabled(false);
		mGetScrict.setEnabled(false);
		mSelectMobile.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				if (FieldVerifyUtil.verifyPhone(phoneValue).isSuccess()) {	
				if (s.length() > 10) {
					mGetScrict.setEnabled(true);
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
					mGetScrict.setEnabled(false);
				}
			}
		});
		mVerCode.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!TextUtils.isEmpty(mSelectMobile.getEditableText().toString())){
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
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Intent intent = getActivity().getIntent();
		if (savedInstanceState != null) {
			intent.putExtras(savedInstanceState);
		}
		mUserTypeDto = intent.getParcelableExtra(Cons.Intent_bean);
		mUserNo = intent.getStringExtra(Cons.Intent_id);
		mIdNo.setText(mUserNo);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent in;
		switch (v.getId()) {
		case R.id.submit_btn:
			verSms();
			break;
		case R.id.select_mobile:
			DialogSelectFragment.newInstance(0, mUserTypeDto.getMobiles(), this).show(getFragmentManager(), "");
			break;
		case R.id.accessmsver:
			sendSms();
			break;
		default:
			break;
		}
	}

	private void sendSms() {
		String phoneValue = mSelectMobile.getText().toString();
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

	private void verSms() {
		// TODO Auto-generated method stub
		String phoneValue = mMobileValue;
		String veri = mVerCode.getText().toString();
		VerifyReslt msg = FieldVerifyUtil.verifyPhone(phoneValue);
		if (!msg.isSuccess()) {
			showToastShort(msg.getMsg());
			return;
		}

		msg = FieldVerifyUtil.verifyMsgVer(veri);
		if (!msg.isSuccess()) {
			showToastShort(msg.getMsg());
			return;
		}
		new smsVeri().execute(veri, phoneValue);
		mpDialog = new PiggyProgressDialog(getActivity());
		mpDialog.setMessage("验证手机号...");
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
				mGetScrict.startCountDown();
				if (GlobalParams.getGlobalParams().isDebugFlag()) {
					String sms = result.getOneString();
					if (sms == null) {
						showToastShort("未知错误！");
					} else {
						mVerCode.setText(sms);
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
				showToastShort("手机验证成功！");
				loginSuccess();
			} else {
				showToastShort(result.getContentMsg());
			}
		}
	}

	/**
	 * 登录成功 跳转
	 */
	private void loginSuccess() {
		Bundle b = new Bundle();
		b.putString("mob", mSelectMobile.getText().toString());
		b.putString("no", mUserNo);
		doAddFragment(ActiveFragmentTwo.class.getName(), b);
	}

	@Override
	public void onSelect(int mType, String value, int index) {
		// TODO Auto-generated method stub
		mSelectMobile.setText(value);
		mMobileValue = value;
	}

}
