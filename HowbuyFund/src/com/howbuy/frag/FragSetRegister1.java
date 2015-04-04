package com.howbuy.frag;

import howbuy.android.palmfund.R;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.howbuy.aty.AtyEmpty;
import com.howbuy.component.AppFrame;
import com.howbuy.config.ValConfig;
import com.howbuy.datalib.fund.ParCheckCode;
import com.howbuy.entity.UserInf;
import com.howbuy.lib.frag.AbsFragMger.FragOpt;
import com.howbuy.lib.frag.AbsHbFrag;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.lib.utils.ViewUtils;
import com.howbuy.wireless.entity.protobuf.RegisterProtos.Register;

@SuppressLint("SetJavaScriptEnabled")
public class FragSetRegister1 extends AbsHbFrag implements IReqNetFinished {
	private static final int CHECK_INPUT_USERNAME = 1;
	private static final int CHECK_INPUT_PASSWORD = 2;
	private View vRight = null;
	private EditText mEtUsername, mEtPassword;
	private String mReturnBackPhone = null;
	private int mReturnCountTime = 60;
	private long mReturnTimes = 0;
	ProgressDialog mDialog;

	private void showCheckUpdateDlg(boolean show) {
		if (show) {
			if (mDialog == null) {
				mDialog = new ProgressDialog(getSherlockActivity());
				mDialog.setMessage("提交中...");
			}
			if (!mDialog.isShowing()) {
				mDialog.show();
			}
		} else {
			if (mDialog != null && mDialog.isShowing()) {
				mDialog.dismiss();
			}
		}
		mEtPassword.setEnabled(!show);
		mEtUsername.setEnabled(!show);
		vRight.setEnabled(!show);
		vRight.setEnabled(!show);
	}

	private void parseArgments(Bundle arg) {
		if (arg != null) {
			if (mTitleLable == null) {
				mTitleLable = arg.getString(ValConfig.IT_NAME);
			}
			String username = arg.getString("mRegisterUser");
			String password = arg.getString("mRegisterPassword");
			if (username != null) {
				mEtUsername.setText(username);
				arg.putString("mRegisterUser", null);
			}
			if (password != null) {
				mEtPassword.setText(password);
				arg.putString("mRegisterPassword", null);
			}
		}
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		mEtUsername = (EditText) root.findViewById(R.id.et_username);
		mEtPassword = (EditText) root.findViewById(R.id.et_password);

		vRight = root.findViewById(R.id.tv_right);
		parseArgments(getArguments());
		mEtPassword.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				if (s.length() > 30) {
					pop("对不起，暂不支持30位以上的密码", false);
					mEtPassword.setText(s.subSequence(0, s.length() - 1));
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}
		});
	}

	private int checkSubmit(String username, String password) {
		int checkResult = 0;
		if (StrUtils.isEmpty(username)) {
			pop("请输入手机号码", false);
			checkResult = CHECK_INPUT_USERNAME;
		} else if (StrUtils.isEmpty(password)) {
			pop("请输入密码", false);
			checkResult = CHECK_INPUT_PASSWORD;
		} else if (password.length() < 6) {
			pop("为了您的账号安全，请设置6位以上密码", false);
			checkResult = CHECK_INPUT_PASSWORD;
		}
		if (checkResult == 0) {
			if (!StrUtils.isMobileTel(username)) {
				pop("请输入正确的手机号码", false);
				checkResult = CHECK_INPUT_USERNAME;
			}
		}
		return checkResult;
	}

	private void savePhoneNum(String phone) {
		UserInf user = UserInf.getUser();
		if (!user.isLogined()) {
			user.setUserPhone(phone);
		}
		AppFrame.getApp().getsF().edit().putString(ValConfig.SFUserPhone, phone).commit();
	}

	private void handSubmit(View v) {

		String username = mEtUsername.getText().toString().trim();
		String password = mEtPassword.getText().toString().trim();
		if (0 == checkSubmit(username, password)) {
			if (mReturnBackPhone != null && mReturnBackPhone.equals(username)) {
				gotoNextStep(false);
			} else {
				showCheckUpdateDlg(true);
				new ParCheckCode(1, this).setParams("2", username).execute();
			}
		}
	}

	private void gotoNextStep(boolean hasSendAuthCode) {
		String username = mEtUsername.getText().toString().trim();
		String password = mEtPassword.getText().toString().trim();
		Bundle b = new Bundle();
		b.putString("username", username);
		b.putString("password", password);
		savePhoneNum(username);
		if (mReturnBackPhone != null && mReturnBackPhone.equals(username)) {
			mReturnCountTime = (int) (mReturnCountTime - (System.currentTimeMillis() - mReturnTimes) / 1000);
			if (mReturnCountTime <= 1) {
				mReturnCountTime = 60;
			}
			b.putInt("mReturnCountTime", mReturnCountTime);
			b.putBoolean(ValConfig.IT_TYPE, false);
		} else {
			b.putInt("mReturnCountTime", 60);
			b.putBoolean(ValConfig.IT_TYPE, true);
		}
		b.putBoolean(ValConfig.IT_ID, hasSendAuthCode);
		FragOpt opt = new FragOpt(FragSetRegister2.class.getName(), b, FragOpt.FRAG_POPBACK
				| FragOpt.FRAG_CACHE);
		opt.setTargetFrag(this, 1);
		View focuseV = mRootView.findFocus();
		ViewUtils.showKeybord(focuseV == null ? mEtPassword : focuseV, false);
		((AtyEmpty) getSherlockActivity()).switchToFrag(opt);
	}

	@Override
	public boolean onXmlBtClick(View v) {
		boolean handed = true;
		switch (v.getId()) {
		case R.id.tv_left:
			mEtUsername.setText(null);
			mEtPassword.setText(null);
			getSherlockActivity().onBackPressed();
			break;
		case R.id.tv_right:
			handSubmit(v);
			break;
		default:
			handed = false;
			break;
		}
		return handed ? true : super.onXmlBtClick(v);
	}

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_set_register_one;
	}

	public void onStepBack(String arg, int timercounter) {
		mReturnBackPhone = arg;
		mReturnCountTime = timercounter;
		mReturnTimes = System.currentTimeMillis();
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}

	@Override
	public boolean onKeyBack(boolean fromBar, boolean isFirstPress, boolean isTwiceInTime) {
		if (getTargetFragment() instanceof FragSetLogin) {
			String username = mEtUsername.getText().toString().trim();
			String password = mEtPassword.getText().toString().trim();
			((FragSetLogin) getTargetFragment()).setRegisterInf(username, password);
		}
		return super.onKeyBack(fromBar, isFirstPress, isTwiceInTime);
	}

	@Override
	public void onRepNetFinished(ReqResult<ReqNetOpt> r) {
		showCheckUpdateDlg(false);
		if (r.isSuccess()) {
			Register cm = (Register) r.mData;
			String code = cm.getCommon().getResponseCode();
			if (code.equals("1")) {
				pop("验证码已发出", false);
				gotoNextStep(true);
			} else {
				if ("-1".equals(code)) {
					pop("您当前请求了过多的短信验证，请稍后再试", false);
				} else {
					pop(cm.getCommon().getResponseContent(), false);
				}
			}
		} else {
			pop("err=" + r.mErr, true);
		}

	}
}
