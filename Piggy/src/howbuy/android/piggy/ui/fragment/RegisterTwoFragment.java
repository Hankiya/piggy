package howbuy.android.piggy.ui.fragment;

import howbuy.android.lib.MyAsyncTask;
import howbuy.android.piggy.R;
import howbuy.android.piggy.api.DispatchAccessData;
import howbuy.android.piggy.api.dto.OneStringDto;
import howbuy.android.piggy.api.dto.UserTypeDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.dialog.PiggyProgressDialog;
import howbuy.android.piggy.dialogfragment.ActiveDialog;
import howbuy.android.piggy.help.NoticeHelp;
import howbuy.android.piggy.loader.QueryActiveLoader;
import howbuy.android.piggy.service.ServiceMger.TaskBean;
import howbuy.android.piggy.service.UpdateUserDataService;
import howbuy.android.piggy.ui.LockSetActivity;
import howbuy.android.piggy.ui.RegisterActivity;
import howbuy.android.piggy.ui.WebViewActivity;
import howbuy.android.piggy.ui.base.AbsNoticeFrag;
import howbuy.android.piggy.ui.fragment.PureWebFragment.PureType;
import howbuy.android.piggy.widget.ClearableEdittext;
import howbuy.android.piggy.widget.ClearableEdittext.MyFocusChangeListen;
import howbuy.android.piggy.widget.ImageTextBtn;
import howbuy.android.util.Cons;
import howbuy.android.util.FieldVerifyUtil;
import howbuy.android.util.FieldVerifyUtil.VerifyReslt;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.umeng.analytics.MobclickAgent;

public class RegisterTwoFragment extends AbsNoticeFrag implements OnClickListener, LoaderCallbacks<UserTypeDto>, MyFocusChangeListen {
	howbuy.android.piggy.widget.ClearableEdittext mUserName, mUserNo;
	howbuy.android.piggy.widget.ClearableEdittext mUserPwd;
	howbuy.android.piggy.widget.ClearableEdittext mTradePwd;
	private TextView mClickText;
	private CheckBox mCheckBox;
	private ImageTextBtn mSubmitBtn;
	private PiggyProgressDialog mpDialog;
	private int mForwordType;

	private void clearActivityTask() {
		ApplicationParams.getInstance().clearActivityTask();
	}

	@Override
	public void onServiceRqCallBack(Intent taskData, boolean isCurrPage) {
		// TODO Auto-generated method stub
		super.onServiceRqCallBack(taskData, isCurrPage);
		String taskType = taskData.getStringExtra(Cons.Intent_type);
		if (UpdateUserDataService.TaskType_UserInfo.equals(taskType) && isAdded()) {
			if (!(ApplicationParams.getInstance().getActivity() instanceof RegisterActivity)) {
				return;
			}
			showToastShort("注册成功");
			if (mpDialog != null && mpDialog.isShowing()) {
				mpDialog.dismiss();
			}
			Intent intent2 = new Intent(getActivity(), LockSetActivity.class);
			// intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
			int defaultType = LockSetActivity.Type_Login;
			if (mForwordType != 0) {
				defaultType = defaultType | mForwordType;
			}
			intent2.putExtra(Cons.Intent_type, defaultType);
			startActivity(intent2);
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
				clearActivityTask();
			}
			getSherlockActivity().finish();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			getSherlockActivity().onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		getSherlockActivity().setTitle("注册");
		// getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
		mForwordType = getSherlockActivity().getIntent().getIntExtra(Cons.Intent_type, 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.aty_register_two, container, false);
		mUserName = (howbuy.android.piggy.widget.ClearableEdittext) view.findViewById(R.id.user_name);
		mUserNo = (howbuy.android.piggy.widget.ClearableEdittext) view.findViewById(R.id.user_no);
		mUserPwd = (howbuy.android.piggy.widget.ClearableEdittext) view.findViewById(R.id.user_pwd);
		mTradePwd = (howbuy.android.piggy.widget.ClearableEdittext) view.findViewById(R.id.trade_pwd);
		mSubmitBtn = (ImageTextBtn) view.findViewById(R.id.submit_btn);
		mCheckBox = (CheckBox) view.findViewById(R.id.checkf);
		mClickText = (TextView) view.findViewById(R.id.clecktext);
		mClickText.setText(Html.fromHtml("我已阅读并同意<font color=#2a5894>《好买电子交易服务协议》</font>"));
		mSubmitBtn.setOnClickListener(this);
		mClickText.setOnClickListener(this);
		mUserNo.setmListen(this);
		view.findViewById(R.id.callphone).setOnClickListener(this);
		mUserNo.setFilters(new InputFilter[] {// 大写
		new InputFilter.AllCaps() });
		mSubmitBtn.setEnabled(false);
		mUserName.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!(TextUtils.isEmpty(mUserNo.getEditableText().toString()) || TextUtils.isEmpty(mUserPwd.getEditableText().toString()) || TextUtils.isEmpty(mTradePwd
						.getEditableText().toString()))) {

					mSubmitBtn.setEnabled(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().equals("")) {
					mSubmitBtn.setEnabled(false);
				}
			}
		});
		mUserNo.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!(TextUtils.isEmpty(mUserName.getEditableText().toString()) || TextUtils.isEmpty(mUserPwd.getEditableText().toString()) || TextUtils.isEmpty(mTradePwd
						.getEditableText().toString()))) {

					mSubmitBtn.setEnabled(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().equals("")) {
					mSubmitBtn.setEnabled(false);
				}
			}
		});
		mUserPwd.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!(TextUtils.isEmpty(mUserName.getEditableText().toString()) || TextUtils.isEmpty(mUserNo.getEditableText().toString()) || TextUtils.isEmpty(mTradePwd
						.getEditableText().toString()))) {

					mSubmitBtn.setEnabled(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().equals("")) {
					mSubmitBtn.setEnabled(false);
				}
			}
		});
		mTradePwd.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!(TextUtils.isEmpty(mUserName.getEditableText().toString()) || TextUtils.isEmpty(mUserNo.getEditableText().toString()) || TextUtils.isEmpty(mUserPwd
						.getEditableText().toString()))) {

					mSubmitBtn.setEnabled(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().equals("")) {
					mSubmitBtn.setEnabled(false);
				}
			}
		});

		mTradePwd.setClearType(ClearableEdittext.TypePas);
		mUserPwd.setClearType(ClearableEdittext.TypePas);
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.submit_btn:
			register();
			MobclickAgent.onEvent(getActivity(), Cons.EVENT_UI_Register, "确认注册");
			break;
		case R.id.clecktext:
			Intent intent = new Intent(getActivity(), WebViewActivity.class);
			intent.putExtra(Cons.Intent_type, PureType.urlWearOperal.getType());
			intent.putExtra(Cons.Intent_id, "file:///android_asset/tradeprotocal.html");
			intent.putExtra(Cons.Intent_name, "好买电子交易服务协议");
			startActivity(intent);
			break;
		case R.id.callphone:
			Uri callUri = Uri.parse("tel:" + "4007009665");
			Intent returnIt = new Intent(Intent.ACTION_DIAL, callUri);
			startActivity(returnIt);
			MobclickAgent.onEvent(getActivity(), Cons.EVENT_Event_CallSP, "注册2");
			break;
		default:
			break;
		}
	}

	public void register() {
		String phoneValue = getArguments().getString(Cons.Intent_normal);// name
		String phoneVery = getArguments().getString(Cons.Intent_id);// name
		String userNameValue = mUserName.getText().toString();
		String userNoValue = mUserNo.getText().toString();
		String userPwdValue = mUserPwd.getText().toString();
		String tradeValue = mTradePwd.getText().toString();

		VerifyReslt msg = FieldVerifyUtil.verifyUserName(userNameValue);
		if (!msg.isSuccess()) {
			showToastShort(msg.getMsg());
			return;
		}

		msg = FieldVerifyUtil.verifyPhone(phoneValue);
		if (!msg.isSuccess()) {
			showToastShort(msg.getMsg());
			return;
		}

		msg = FieldVerifyUtil.verifyId(userNoValue);
		if (!msg.isSuccess()) {
			showToastShort(msg.getMsg());
			return;
		}

		msg = FieldVerifyUtil.verifyLoginPwd(userPwdValue);
		if (!msg.isSuccess()) {
			showToastShort(msg.getMsg());
			return;
		}

		msg = FieldVerifyUtil.verifyTradePwd(tradeValue);
		if (!msg.isSuccess()) {
			showToastShort(msg.getMsg());
			return;
		}

		if (!mCheckBox.isChecked()) {
			showToastShort("请同意好买电子交易服务协议");
			return;
		}

		new RegisterTask().execute(userNameValue, userNoValue, phoneValue, userPwdValue, tradeValue,phoneVery);
		mpDialog = new PiggyProgressDialog(getActivity(), PiggyProgressDialog.TypeLoginRegister);
		mpDialog.setMessage("正在提交注册信息...");
		mpDialog.show();
	}

	public class RegisterTask extends MyAsyncTask<String, Void, OneStringDto> {
		private String userNo;
		private String userPwd;

		@Override
		protected OneStringDto doInBackground(String... params) {
			// TODO Auto-generated method stub
			userNo = params[1];
			userPwd = params[3];
			return DispatchAccessData.getInstance().commitRegistert(params[0], params[1], params[2], params[3], params[4], params[5]);// custName,
			// idNo,
			// mobile,
			// loginPwd,
			// tradePwd
		}

		@Override
		protected void onPostExecute(OneStringDto result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// if (mpDialog != null && mpDialog.isShowing()) {
			// mpDialog.dismiss();
			// }
			String userId = result.getOneString();
			if (result.getContentCode() == Cons.SHOW_SUCCESS && !TextUtils.isEmpty(userId)) {
				mpDialog.setMessage("注册成功，正在登录账户...");
				ApplicationParams.getInstance().getsF().edit().putString(Cons.SFUserCusNo, userId).putString(Cons.SFUserNOHistory, mUserNo.getText().toString()).commit();
				// mUpdateUserService.luncherTask();
				ApplicationParams.getInstance().getServiceMger().addTask(new TaskBean(UpdateUserDataService.TaskType_UserInfo, "0"));
			} else {
				if (mpDialog != null && mpDialog.isShowing()) {
					mpDialog.dismiss();
				}
				showToastShort(result.getContentMsg());
			}
		}
	}

	@Override
	public Loader<UserTypeDto> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		String idNo = mUserNo.getText().toString();
		String idType = "0";// 身份证
		return new QueryActiveLoader(ApplicationParams.getInstance(), idNo, idType);
	}

	@Override
	public void onLoadFinished(Loader<UserTypeDto> arg0, UserTypeDto arg1) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.obj = arg1;
		handler.sendMessage(msg);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			UserTypeDto data = (UserTypeDto) msg.obj;
			if (null != data && data.getContentCode() == Cons.SHOW_SUCCESS) {
				if (data.getNeedActive().equals(LoginFragment.NEED_ACTIVE)) {
					showNeedActiveDialog(data);
				}
			}
		};
	};

	public void showNeedActiveDialog(UserTypeDto data) {
		// FragmentTransaction ft = getFragmentManager().beginTransaction();
		// Fragment prev = getFragmentManager().findFragmentByTag("dialog");
		// if (prev != null) {
		// ft.remove(prev);
		// }
		// ft.addToBackStack(null);
		String idNo = mUserNo.getText().toString();
		Bundle b = new Bundle();
		b.putString(Cons.Intent_id, idNo);
		b.putParcelable(Cons.Intent_bean, data);
		ActiveDialog newFragment = ActiveDialog.newInstance(b);
		newFragment.setmBundle(b);
		newFragment.show(getFragmentManager(), "dialog");
	}

	@Override
	public void onLoaderReset(Loader<UserTypeDto> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFocusChange(boolean hasFocus) {
		// TODO Auto-generated method stub
		if (false == hasFocus) {
			String idNo = mUserNo.getText().toString();
			if (TextUtils.isEmpty(idNo)) {
				return;
			}
			if (getLoaderManager().hasRunningLoaders()) {
				getLoaderManager().restartLoader(0, null, this).forceLoad();
			} else {
				getLoaderManager().initLoader(0, null, this).forceLoad();
			}
		}
	}

	@Override
	public String getNoticeType() {
		// TODO Auto-generated method stub
		return NoticeHelp.Notice_ID_Register;
	}
}
