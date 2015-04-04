package howbuy.android.piggy.ui.fragment;

import howbuy.android.lib.MyAsyncTask;
import howbuy.android.piggy.R;
import howbuy.android.piggy.api.DispatchAccessData;
import howbuy.android.piggy.api.dto.LoginDto;
import howbuy.android.piggy.api.dto.UserTypeDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.dialog.PiggyProgressDialog;
import howbuy.android.piggy.dialogfragment.ActiveDialog;
import howbuy.android.piggy.help.NoticeHelp;
import howbuy.android.piggy.help.ParseWebReqUri;
import howbuy.android.piggy.help.ParseWebReqUri.Web_Flag;
import howbuy.android.piggy.loader.QueryActiveLoader;
import howbuy.android.piggy.service.ServiceMger.TaskBean;
import howbuy.android.piggy.service.UpdateUserDataService;
import howbuy.android.piggy.ui.LockSetActivity;
import howbuy.android.piggy.ui.ProPertyActivity;
import howbuy.android.piggy.ui.RegisterActivity;
import howbuy.android.piggy.ui.ResetPwdActivity;
import howbuy.android.piggy.ui.base.AbsNoticeFrag;
import howbuy.android.piggy.ui.base.OnKeyboardVisibilityListener;
import howbuy.android.piggy.widget.ClearableEdittext;
import howbuy.android.piggy.widget.ClearableEdittext.MyFocusChangeListen;
import howbuy.android.piggy.widget.ClearableEdittext.MyTextChangeListen;
import howbuy.android.util.Cons;
import howbuy.android.util.FieldVerifyUtil;
import howbuy.android.util.FieldVerifyUtil.VerifyReslt;
import howbuy.android.util.StringUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.umeng.analytics.MobclickAgent;

public class LoginFragment extends AbsNoticeFrag implements android.view.View.OnClickListener, OnKeyboardVisibilityListener, MyFocusChangeListen, MyTextChangeListen,
		LoaderCallbacks<UserTypeDto> {
	public static final String NEED_ACTIVE = "1";
	/**
	 * 从启动页面过来
	 */
	public static final int LoginType_Lanucher = 1;
	/**
	 * 从主页面过来
	 */
	public static final int LoginType_Main = 1 << 1;
	/**
	 * 从验证锁页面过来
	 */
	public static final int LoginType_VerfctPn = 1 << 2;
	/**
	 * 从设置页面过来
	 */
	public static final int LoginType_Setting = 1 << 3;
	/**
	 * 从激活过来
	 */
	public static final int LoginType_Active = 1 << 4;
	public String mHisNo;
	private boolean isSaveCurrIdNo;

	howbuy.android.piggy.widget.ClearableEdittext meEditPwd;
	private ClearableEdittext mEditId;
	private PiggyProgressDialog mpDialog;
	private LinearLayout mNoKeyBoard;
	private int mLoginType = LoginType_Main;
	private ActiveDialog mActiveDialog;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean isShoudRegisterReciver() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onServiceRqCallBack(Intent taskData, boolean isCurrPage) {
		// TODO Auto-generated method stub
		super.onServiceRqCallBack(taskData, isCurrPage);
		String taskType = taskData.getStringExtra(Cons.Intent_type);
		if (UpdateUserDataService.TaskType_UserInfo.equals(taskType) && isCurrPage && isAdded()) {
			if (mpDialog != null && mpDialog.isShowing()) {
				mpDialog.dismiss();
			}
			loginSuccess();
		}
	}

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		if ((mLoginType & Web_Flag._flagLogin) == Web_Flag._flagLogin) {
			getSherlockActivity().setResult(Activity.RESULT_CANCELED);
			ParseWebReqUri.jumpWebPage(getSherlockActivity(), Activity.RESULT_CANCELED, Web_Flag.Flag_SaveMoney);
			getActivity().finish();
		} else {
			Intent in = new Intent(getActivity(), ProPertyActivity.class);
			in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(in);
			getActivity().finish();
			return true;
		}
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}
		if (item.getTitle().equals("注册")) {
			Intent in = new Intent(getActivity(), RegisterActivity.class);
			in.putExtra(Cons.Intent_type, mLoginType);
			startActivity(in);
			MobclickAgent.onEvent(getActivity(), Cons.EVENT_UI_Login, "注册");
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
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		menu.add("注册").setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final View view = inflater.inflate(R.layout.aty_login, container, false);
		view.findViewById(R.id.submit_btn).setOnClickListener(this);
		view.findViewById(R.id.forgetpwd).setOnClickListener(this);
		mEditId = (ClearableEdittext) view.findViewById(R.id.idNo);
		meEditPwd = (howbuy.android.piggy.widget.ClearableEdittext) view.findViewById(R.id.user_pwd);
		mEditId.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!TextUtils.isEmpty(meEditPwd.getEditableText().toString())) {
					view.findViewById(R.id.submit_btn).setEnabled(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().equals("")) {
					view.findViewById(R.id.submit_btn).setEnabled(false);
				}
			}
		});
		meEditPwd.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!TextUtils.isEmpty(mEditId.getEditableText().toString())) {
					view.findViewById(R.id.submit_btn).setEnabled(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().equals("")) {
					view.findViewById(R.id.submit_btn).setEnabled(false);
				}
			}
		});
		mHisNo = getSf().getString(Cons.SFUserNOHistory, "");
		if (!TextUtils.isEmpty(mHisNo)) {
			mEditId.setText(StringUtil.formatExitIdCard(mHisNo));
			meEditPwd.requestFocus();
		}
		mEditId.setmListen(this);
		mEditId.setFilters(new InputFilter[] {// 大写
		new InputFilter.AllCaps() });
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setKeyboardListener(this);
		mLoginType = getActivity().getIntent().getIntExtra(Cons.Intent_type, 0);
		meEditPwd.setClearType(ClearableEdittext.TypePas);
		// if (mLoginType == LoginType_Lanucher||mLoginType==LoginType_Setting)
		// {
		// getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		// getSherlockActivity().getSupportActionBar().setHomeButtonEnabled(false);
		// }
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Intent intent = getActivity().getIntent();
		mLoginType = intent.getIntExtra(Cons.Intent_type, 0);
		String id = intent.getStringExtra(Cons.Intent_id);
		if ((mLoginType & LoginFragment.LoginType_Active) == LoginFragment.LoginType_Active) {
			mEditId.setText(id);
			meEditPwd.requestFocus();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent in;
		switch (v.getId()) {

		case R.id.submit_btn:
			MobclickAgent.onEvent(getActivity(), Cons.EVENT_UI_Login, "登录");
			String userId = mEditId.getEditableText().toString();
			String userPwd = meEditPwd.getEditableText().toString();

			if (userId.equals(StringUtil.formatExitIdCard(mHisNo))) {
				userId = mHisNo;
				isSaveCurrIdNo = true;
			}

			VerifyReslt msg = FieldVerifyUtil.verifyId(userId);
			if (!msg.isSuccess()) {
				showToastShort(msg.getMsg());
				return;
			}

			if (!TextUtils.isEmpty(userPwd)) {
				if (userPwd.length() < 6 || userPwd.length() > 16) {
					showToastShort("登录密码应为6-12位字母与数字的组合");
					return;
				}
			} else {
				showToastShort("用户密码不能为空");
				return;
			}

			new LoginTask().execute(userId, userPwd);
			mpDialog = new PiggyProgressDialog(getActivity(), PiggyProgressDialog.TypeLoginRegister);
			mpDialog.setMessage("登录中...");
			mpDialog.show();
			break;
		case R.id.forgetpwd:
			// 忘记密码
			in = new Intent(getActivity(), ResetPwdActivity.class);
			in.putExtra(Cons.Intent_type, ResetPwdFragment.typeForgetPwd);
			startActivity(in);
			break;
		default:
			break;
		}
	}

	public class LoginTask extends MyAsyncTask<String, Void, LoginDto> {

		@Override
		protected LoginDto doInBackground(String... params) {
			return DispatchAccessData.getInstance().commitLogin(params[0], params[1]);
		}

		@Override
		protected void onPostExecute(LoginDto result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result.getContentCode() == Cons.SHOW_SUCCESS) {
				String userNo = result.getCustNo();
				if (userNo == null) {
					showToastShort("未知错误！");
				} else {
					if (!isSaveCurrIdNo) {
						getSf().edit().putString(Cons.SFUserNOHistory, mEditId.getEditableText().toString()).commit();
					}
					showToastShort("登录成功！");
					ApplicationParams.getInstance().getsF().edit().putString(Cons.SFUserCusNo, result.getCustNo()).commit();
					ApplicationParams.getInstance().getServiceMger().addTask(new TaskBean(UpdateUserDataService.TaskType_UserInfo, ""));
					ApplicationParams.getInstance().getServiceMger().addTask(new TaskBean(UpdateUserDataService.TaskType_UserCard, ""));
				}
			} else {
				if (mpDialog != null && mpDialog.isShowing()) {
					mpDialog.dismiss();
				}
				showToastShort(result.getContentMsg());
				meEditPwd.setText("");
			}
		}
	}

	/**
	 * 登录成功 跳转
	 */
	private void loginSuccess() {
		switch (mLoginType) {
		case LoginType_Lanucher:
			boolean isFoget = getSf().getBoolean(Cons.SFPatternForgetFlag, false);// 跳过密码设置
			boolean isLoginOut = getSf().getBoolean(Cons.SFLoginOutFlag, false);// 退出登录
			if (isLoginOut || isFoget) {
				LuncherMain();
			} else {
				LuncherLockSet();
			}
			getActivity().finish();
			break;
		case LoginType_Main:
			LuncherLockSet();
			break;
		case LoginType_VerfctPn:
			LuncherLockSet();
			break;
		case LoginType_Setting:
			LuncherLockSet();
			break;
		default:
			LuncherLockSet();
			break;
		}
	}

	public void LuncherLockSet() {
		Intent startIntent = new Intent(getActivity(), LockSetActivity.class);
		startIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		int type = LockSetActivity.Type_Login;
		if (mLoginType != 0 && ((mLoginType & Web_Flag._flagLogin) == Web_Flag._flagLogin)) {
			type = type | LockSetActivity.Type_Login;
		}
		startIntent.putExtra(Cons.Intent_type, type);
		startActivity(startIntent);
		getActivity().finish();
	}

	public void LuncherMain() {
		Intent startIntent = new Intent(getActivity(), ProPertyActivity.class);
		startIntent.putExtra(Cons.Intent_type, ProPertyActivity.NeeduserDataReload);
		startIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(startIntent);
		getActivity().finish();
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

	@Override
	public Loader<UserTypeDto> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		String idNo = mEditId.getText().toString();
		String idType = "0";// 身份证
		Log.i("message", idNo);
		return new QueryActiveLoader(ApplicationParams.getInstance(), idNo, idType);
	}

	@Override
	public void onLoadFinished(Loader<UserTypeDto> loader, UserTypeDto data) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.obj = data;
		handler.sendMessage(msg);
		System.out.println("onLoadFinished");
	}

	@Override
	public void onLoaderReset(Loader<UserTypeDto> loader) {
		// TODO Auto-generated method stub

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			System.out.println("Hander");
			UserTypeDto data = (UserTypeDto) msg.obj;
			if (null != data && data.getContentCode() == Cons.SHOW_SUCCESS) {
				if (data.getNeedActive().equals(NEED_ACTIVE)) {
					if (data.getMobiles() != null && data.getMobiles().size() != 0) {
						showNeedActiveDialog(data);
					} else {
						showToastShort("检测到您是好卖渠道其他客户，但是无法得到手机信息，请联系好买柜台激活储蓄罐");
					}
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
		String idNo = mEditId.getText().toString();
		Bundle b = new Bundle();
		b.putString(Cons.Intent_id, idNo);
		b.putParcelable(Cons.Intent_bean, data);
		b.putInt(Cons.Intent_type, mLoginType);
		mActiveDialog = ActiveDialog.newInstance(b);
		mActiveDialog.setmBundle(b);
		mActiveDialog.show(getFragmentManager(), "dialog");
	}

	@Override
	public void onFocusChange(boolean hasFocus) {
		// TODO Auto-generated method stub
		if (false == hasFocus) {
			String idNo = mEditId.getText().toString();
			VerifyReslt msg = FieldVerifyUtil.verifyId(idNo);
			if (!msg.isSuccess()) {
				return;
			}
			// if (getLoaderManager().hasRunningLoaders()) {
			// System.out.println("hasRunningLoaders");
			getLoaderManager().restartLoader(0, null, this).forceLoad();
			// } else {
			// System.out.println("hasRunningLoaders1");
			// getLoaderManager().initLoader(0, null, this).forceLoad();
			// }
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getNoticeType() {
		// TODO Auto-generated method stub
		return NoticeHelp.Notice_ID_Login;
	}

}
