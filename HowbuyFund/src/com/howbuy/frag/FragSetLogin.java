package com.howbuy.frag;

import howbuy.android.palmfund.R;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;

import com.howbuy.aty.AtyEmpty;
import com.howbuy.component.AppFrame;
import com.howbuy.component.AppService;
import com.howbuy.config.ValConfig;
import com.howbuy.datalib.fund.ParLogin;
import com.howbuy.entity.UserInf;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.frag.AbsFragMger.FragOpt;
import com.howbuy.lib.frag.AbsHbFrag;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AsyPoolTask;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.CoderUtils;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.wireless.entity.protobuf.LoginProto.Login;

@SuppressLint("SetJavaScriptEnabled")
public class FragSetLogin extends AbsHbFrag implements IReqNetFinished {
	private static String SF_LOCK_LOGIN_ACCOUNT = "LOCK_LOGIN_ACCOUNT";// 账号#时间/
	private static final int CHECK_INPUT_PASSWORD = 1;
	private static final int LOGIN_MAX_COUNT_TIME_GAP = 300000;// MS.
	private String mUserName, mPassword;
	private String mRegisterUser, mRegisterPassword;
	private View mTvSubmit;
	private EditText mEtUsername, mEtPassword;
	ProgressDialog mDialog;

	private void showCheckUpdateDlg(boolean show) {
		if (show) {
			if (mDialog == null) {
				mDialog = new ProgressDialog(getSherlockActivity());
				mDialog.setMessage("登录中...");
			}
			if (!mDialog.isShowing()) {
				mDialog.show();
			}
		} else {
			if (mDialog != null && mDialog.isShowing()) {
				mDialog.dismiss();
			}
		}
		mEtUsername.setEnabled(!show);
		mEtPassword.setEnabled(!show);
		mTvSubmit.setEnabled(!show);
	}

	private void parseArgments(Bundle arg) {
		if (arg != null) {
			if (mTitleLable == null) {
				mTitleLable = arg.getString(ValConfig.IT_NAME);
			}
		}
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		parseArgments(getArguments());
		mTvSubmit = root.findViewById(R.id.tv_submit);
		mEtUsername = (EditText) root.findViewById(R.id.et_username);
		mEtPassword = (EditText) root.findViewById(R.id.et_password);
		mTvSubmit.setEnabled(false);
		mEtUsername.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				mTvSubmit.setEnabled(s.toString().trim().length() > 0);
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});
		new QueryUser().execute(false);
	}

	private int checkSubmit(String username, String password) {
		int checkResult = 0;
		if (StrUtils.isEmpty(password)) {
			pop("请输入密码", false);
			checkResult = CHECK_INPUT_PASSWORD;
		}
		return checkResult;
	}

	private void handSubmit(View v) {
		int err = 0;
		boolean mLocked = false;
		String username = mEtUsername.getText().toString().trim();
		String password = mEtPassword.getText().toString().trim();
		String lockAccount = AppFrame.getApp().getsF().getString(SF_LOCK_LOGIN_ACCOUNT, null);
		if (lockAccount != null) {
			err = lockAccount.indexOf(username);
			if (err != -1) {
				int end = lockAccount.indexOf("##", err);
				if (end != -1) {
					lockAccount = lockAccount.substring(err, end);
					String[] ss = lockAccount.split("#");
					if (ss != null && ss.length == 2) {
						if (ss[0].equals(username)) {
							try {
								if (System.currentTimeMillis() - Long.parseLong(ss[1]) < LOGIN_MAX_COUNT_TIME_GAP) {
									mLocked = true;
								}
							} catch (Exception e) {
							}
						}
					}
				}

			}
			err = 0;
		}
		if (mLocked) {// has locked for 5 mins.
			pop("您的账号" + mUserName + "已被锁定，请稍后再试", false);
		} else {
			if (0 == (err = checkSubmit(username, password))) {
				mUserName = username;
				mPassword =new String(Base64.encode(password.getBytes(), Base64.NO_WRAP));
				showCheckUpdateDlg(true);
				new ParLogin(0, this).setPareams(mUserName, mPassword).execute();
			} else {
			}
		}
	}

	private void showLockAcountDlg() {
		// save lock account inf .
		String lockAccount = AppFrame.getApp().getsF().getString(SF_LOCK_LOGIN_ACCOUNT, null);
		StringBuilder sb = new StringBuilder();
		Editor e = AppFrame.getApp().getsF().edit();
		if (!StrUtils.isEmpty(lockAccount)) {
			sb.append(lockAccount);
			int err = lockAccount.indexOf(mUserName);
			if (err != -1) {
				int end = lockAccount.indexOf("##", err);
				if (end != -1) {
					lockAccount = lockAccount.substring(err, end);
					String[] ss = lockAccount.split("#");
					if (ss != null && ss.length == 2) {
						if (ss[0].equals(mUserName)) {
							sb.delete(err, end + 2);
						}
					}

				}
			}
		}
		sb.append(mUserName).append("#").append(System.currentTimeMillis()).append("##");
		e.putString(SF_LOCK_LOGIN_ACCOUNT, sb.toString()).commit();

		// first err password for 3 times show dlg.
		AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
		builder.setTitle("账号锁定");
		builder.setMessage("因为您已连续3次输错密码，您的账号" + mUserName + "将被锁定5分钟。");
		builder.setPositiveButton(android.R.string.ok, null);
		builder.show();
	}

	@Override
	public boolean onXmlBtClick(View v) {
		boolean handed = true;
		switch (v.getId()) {
		case R.id.tv_submit:
			handSubmit(v);
			break;
		case R.id.tv_submit_register:
			Bundle b = new Bundle();
			b.putString(ValConfig.IT_NAME, "注册");
			b.putString("mRegisterUser", mRegisterUser);
			b.putString("mRegisterPassword", mRegisterPassword);
			FragOpt opt = new FragOpt(FragSetRegister1.class.getName(), b, FragOpt.FRAG_POPBACK
					| FragOpt.FRAG_CACHE);
			opt.setTargetFrag(this, 0);
			((AtyEmpty) getSherlockActivity()).switchToFrag(opt);
			break;
		default:
			handed = false;
			break;
		}
		return handed ? true : super.onXmlBtClick(v);
	}

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_set_login;
	}

	@Override
	public void onRepNetFinished(ReqResult<ReqNetOpt> r) {
		showCheckUpdateDlg(false);
		if (r.isSuccess()) {
			Login l = (Login) r.mData;
			String code = l.getCommon().getResponseCode();
			String des = l.getCommon().getResponseContent();
			if ("1".equals(code)) {
				UserInf user = UserInf.getUser();
				if (user.isLogined()) {
					try {
						user.loginOut();
						user.save(); // 属于切换状态。
					} catch (Exception e) {
					}

				}
				try {
					user.loginIn(mUserName, mPassword, l.getCustNo());
					UserInf.getUser().save();
				} catch (Exception e) {
					e.printStackTrace();
				}
				successLogined("成功登录");
			} else {
				parseErrResult(code, des);
			}
		} else {
			String msg = r.mErr == null ? null : r.mErr.getError(null, true);
			if (msg != null) {
				pop(msg, false);
			} else {
				pop(r.mErr + "", true);
			}
		}
	}

	private void parseErrResult(String code, String content) {
		if ("-1".equals("code")) {
			showLockAcountDlg();
		} else {
			pop(content, false);
		}
	}

	public void setRegisterInf(String username, String password) {
		mRegisterUser = username;
		mRegisterPassword = password;
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

	class QueryUser extends AsyPoolTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			try {
				ArrayList<UserInf> list = UserInf.load(true);
				if (list.size() > 0) {
					return list.get(0).getUserName();
				}
			} catch (Exception e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result == null) {
			} else {
				mEtUsername.setText(result);
			}
		}
	}

}
