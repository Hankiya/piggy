package com.howbuy.frag;

import howbuy.android.palmfund.R;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.howbuy.component.AppService;
import com.howbuy.config.SelfConfig;
import com.howbuy.config.ValConfig;
import com.howbuy.datalib.fund.ParCheckCode;
import com.howbuy.datalib.fund.ParRegister;
import com.howbuy.db.DbUtils;
import com.howbuy.db.DbUtils.SqlExeObj;
import com.howbuy.entity.UserInf;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.compont.GlobalServiceMger.ScheduleTask;
import com.howbuy.lib.frag.AbsHbFrag;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.interfaces.ITimerListener;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.CoderUtils;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.lib.utils.ViewUtils;
import com.howbuy.wireless.entity.protobuf.RegisterProtos.Register;

@SuppressLint("SetJavaScriptEnabled")
public class FragSetRegister2 extends AbsHbFrag implements ITimerListener, IReqNetFinished {
	private static final int CHECK_AUTHCODE = 1;
	private static final int HAND_REQUEST_AUTHCODE = 1;
	private static final int HAND_REQUEST_REGISTER = 2;
	private TextView mTvSubmit;
	private EditText mEtAuthcode;
	private View vLeft, vRight;
	private int mCountTimer = 60;
	private int mRequestFlag = 0;
	private String mUsername, mPassword;
	private boolean mPhoneChanged = false;
	private boolean mHasSendAuth = false;
	ProgressDialog mDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().getWindow()
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

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
		mEtAuthcode.setEnabled(!show);
		vLeft.setEnabled(!show);
		vRight.setEnabled(!show);

	}

	private void parseArgments(Bundle arg) {
		if (arg != null) {
			if (mTitleLable == null) {
				mTitleLable = arg.getString(ValConfig.IT_NAME);
			}
			mUsername = arg.getString("username");
			mPassword = arg.getString("password");
			mPhoneChanged = arg.getBoolean(ValConfig.IT_TYPE);
			mHasSendAuth = arg.getBoolean(ValConfig.IT_ID);
			mCountTimer = arg.getInt("mReturnCountTime");
			if (mCountTimer == 0) {
				mCountTimer = 60;
			}
			if (mHasSendAuth) {
				mTvSubmit.setEnabled(false);
			}
			dd("username=%1$s, password=%2$s,mPhoneChanged=%3$s,mCountTimer=%4$d", mUsername,
					mPassword, mPhoneChanged, mCountTimer);
			ScheduleTask task = new ScheduleTask(1, this);
			task.setExecuteArg(1000, 0, true);
			GlobalApp.getApp().getGlobalServiceMger().addTimerListener(task);
			updateSendText();
		}
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		mTvSubmit = (TextView) root.findViewById(R.id.tv_submit);
		mEtAuthcode = (EditText) root.findViewById(R.id.et_authcode);
		vLeft = root.findViewById(R.id.tv_left);
		vRight = root.findViewById(R.id.tv_right);
		parseArgments(getArguments());
	}

	private int checkSubmit(String authcode) {
		int checkResult = 0;
		if (StrUtils.isEmpty(authcode)) {
			pop("请输入短信验证码", false);
			checkResult = CHECK_AUTHCODE;
		}
		return checkResult;
	}

	private void handSubmit(View v) {
		String authcode = mEtAuthcode.getText().toString().trim();
		if (0 == checkSubmit(authcode)) {
			new ParRegister(HAND_REQUEST_REGISTER, this).setParams(mUsername, mPassword, authcode,
					2).execute();
			mRequestFlag |= HAND_REQUEST_REGISTER;
			showCheckUpdateDlg(true);
		} else {
		}
	}

	private void sendAuthRequest(boolean sendAuthRequest) {
		if (sendAuthRequest) {
			new ParCheckCode(HAND_REQUEST_AUTHCODE, this).setParams("2", mUsername).execute();
			mRequestFlag |= HAND_REQUEST_AUTHCODE;
		}
		mTvSubmit.setEnabled(false);
		ScheduleTask task = new ScheduleTask(1, this);
		task.setExecuteArg(1000, 0, true);
		GlobalApp.getApp().getGlobalServiceMger().addTimerListener(task);
	}

	@Override
	public boolean onXmlBtClick(View v) {
		boolean handed = true;
		switch (v.getId()) {
		case R.id.tv_submit:
			sendAuthRequest(true);
			break;
		case R.id.tv_left:
			if (!onKeyBack(true, true, false)) {
				getSherlockActivity().onBackPressed();
			}
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
		return R.layout.frag_set_register_two;
	}

	@Override
	public boolean onKeyBack(boolean fromBar, boolean isFirstPress, boolean isTwiceInTime) {
		if (getTargetFragment() instanceof FragSetRegister1) {
			((FragSetRegister1) getTargetFragment()).onStepBack(mUsername, mCountTimer);
		}
		GlobalApp.getApp().getGlobalServiceMger().removeTimerListener(1, this);
		return super.onKeyBack(fromBar, isFirstPress, isTwiceInTime);
	}

	@Override
	public void onTimerRun(int which, int timerState, boolean hasTask, int timesOrSize) {
		if (timerState == ITimerListener.TIMER_SCHEDULE) {
			if (mCountTimer == 0) {
				mCountTimer = 60;
				mTvSubmit.setText("重发");
				mTvSubmit.setEnabled(true);
				GlobalApp.getApp().getGlobalServiceMger().removeTimerListener(1, this);
			} else {
				updateSendText();
			}
		}
	}

	private void updateSendText() {
		String text = String.format(GlobalApp.getApp().getString(R.string.register_auth_resend),
				mCountTimer--);
		mTvSubmit.setText(text);
	}

	@Override
	public void onRepNetFinished(ReqResult<ReqNetOpt> r) {
		int handType = r.mReqOpt.getHandleType();
		if (handType == HAND_REQUEST_AUTHCODE) {
			handResultAuth(r);
		} else if (handType == HAND_REQUEST_REGISTER) {
			handResultRegister(r);
		}
	}

	private void handResultAuth(ReqResult<ReqNetOpt> r) {
		if (r.isSuccess()) {
			Register cm = (Register) r.mData;
			String code = cm.getCommon().getResponseCode();
			if (code.equals("1")) {
				pop("验证码已发出", false);
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
		mRequestFlag = ViewUtils.subFlag(mRequestFlag, HAND_REQUEST_AUTHCODE);
	}

	private void handResultRegister(ReqResult<ReqNetOpt> r) {
		showCheckUpdateDlg(false);
		if (r.isSuccess()) {
			Register cm = (Register) r.mData;
			if ("1".equals(cm.getCommon().getResponseCode())) {
				UserInf user = UserInf.getUser();
				/*
				 * boolean logined = user.isLogined(); if (logined) { try {
				 * user.loginOut(); user.save(); // 属于切换状态。 resetOptRecord();
				 * AppFrame
				 * .getApp().getsF().edit().remove(ValConfig.SFOPTUserClose
				 * ).commit(); } catch (Exception e) { e.printStackTrace(); }
				 * 
				 * }
				 */
				try {
					/*
					 * if (logined) { user.setUserPhone(mUsername); }
					 */
					String encPass=new String(Base64.encode(mPassword.getBytes(), Base64.NO_WRAP));
					user.loginIn(mUsername,encPass, cm.getCustNo());
					UserInf.getUser().save();
				} catch (Exception e) {
					e.printStackTrace();
				}
				successLogined("注册成功");
			} else {
				pop("" + cm.getCommon().getResponseContent(), false);
			}
		} else {
			pop("" + r.mErr, true);
		}
		mRequestFlag = ViewUtils.subFlag(mRequestFlag, HAND_REQUEST_REGISTER);
	}

	/**
	 * reset local opt record
	 */
	private static final void resetOptRecord() {
		String sql = "update fundsinfo set xuan=-1 where xuan in(" + SelfConfig.UNSynsAdd + ","
				+ SelfConfig.UNSynsDel + "," + SelfConfig.SynsShowAdd + ")";
		DbUtils.exeSql(new SqlExeObj(sql), false);
	}

	private void successLogined(String msg) {
		pop(msg, false);
		GlobalApp.getApp().getGlobalServiceMger()
				.executeTask(new ReqOpt(0, "your key arg", AppService.HAND_SYNC_OPTIONAL), null);
		GlobalApp.getApp().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				getSherlockActivity().finish();
			}
		}, 100);
	}
}
