package howbuy.android.piggy.ui.fragment;

import howbuy.android.bean.DialogBean;
import howbuy.android.lib.MyAsyncTask;
import howbuy.android.piggy.R;
import howbuy.android.piggy.api.DispatchAccessData;
import howbuy.android.piggy.api.dto.LoginDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.dialog.PiggyProgressDialog;
import howbuy.android.piggy.dialogfragment.LoginVerifyDialog;
import howbuy.android.piggy.dialogfragment.LoginVerifyDialog.InputCallBack;
import howbuy.android.piggy.parameter.PiggyParams;
import howbuy.android.piggy.ui.LockSetActivity;
import howbuy.android.piggy.ui.ResetPhoneActivity;
import howbuy.android.piggy.ui.ResetPwdActivity;
import howbuy.android.piggy.ui.base.AbstractFragment;
import howbuy.android.util.AndroidUtil;
import howbuy.android.util.Cons;
import howbuy.android.util.FieldVerifyUtil;
import howbuy.android.util.FieldVerifyUtil.VerifyReslt;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.actionbarsherlock.view.MenuItem;
import com.umeng.analytics.MobclickAgent;

public class SettingAccountFragment extends AbstractFragment implements OnClickListener, InputCallBack {
	/**
	 * 修改手势
	 */
	public static final String INPUT_TYPE_PATTERNMODIFY =" 1";
	/**
	 * 开关手势
	 */
	public static final String INPUT_TYPE_PATTERNFLAG = "2";
	
	private PiggyProgressDialog mpDialog;
	private int typeResetPwd;// 1为Login，2为trade
	private RelativeLayout gestureOpenLay;
	private RelativeLayout modGustureLay;
	private RelativeLayout modLoginPwdLay;
	private RelativeLayout modTradePwdLay;
	private RelativeLayout modPhoneLay;
	private CheckBox mflagPattern;
	private String mInputType = INPUT_TYPE_PATTERNFLAG;
	private boolean mIsCancel;

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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode==Activity.RESULT_OK) {
			if (mInputType==INPUT_TYPE_PATTERNFLAG) {
				mflagPattern.setChecked(true);
				setChecked(true);
			}
			initCheck();
			showToastShort("手势密码修改成功");
			MobclickAgent.onEvent(getActivity(), Cons.EVENT_UI_AccountManage, "打开手势密码");
		}else {
			showToastShort("手势密码未改动");
			MobclickAgent.onEvent(getActivity(), Cons.EVENT_UI_AccountManage, "手势密码没有设置成功");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.aty_setting_account, container, false);
		gestureOpenLay = (RelativeLayout) view.findViewById(R.id.gesture_open_lay);
		modGustureLay = (RelativeLayout) view.findViewById(R.id.mod_gesture_lay);
		modLoginPwdLay = (RelativeLayout) view.findViewById(R.id.mod_loginpwd_lay);
		modTradePwdLay = (RelativeLayout) view.findViewById(R.id.mod_tradepwd_lay);
		modPhoneLay = (RelativeLayout) view.findViewById(R.id.mod_phone_lay);
		mflagPattern = (CheckBox) view.findViewById(R.id.flagpattern);
		gestureOpenLay.setOnClickListener(this);
		modGustureLay.setOnClickListener(this);
		modLoginPwdLay.setOnClickListener(this);
		modTradePwdLay.setOnClickListener(this);
		modPhoneLay.setOnClickListener(this);

		initCheck();

		mflagPattern.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction()==MotionEvent.ACTION_UP) {
					checkBoxListener();
				}
				return true;
			}
		});
		return view;
	}
	
	public void checkBoxListener(){
		//boolean isCheck=!mflagPattern.isChecked();
		Bundle b = new Bundle();
        b.putParcelable(Cons.Intent_bean, new DialogBean.DialogBeanBuilder().setDialogId(INPUT_TYPE_PATTERNFLAG).setInputHint("请输入交易密码").setSureBtnMsg("下一步").createDialogBean());
		LoginVerifyDialog d = LoginVerifyDialog.newInstance(b);
		d.setmBack(SettingAccountFragment.this);
		d.show(getFragmentManager(), "");
	}

	/**
	 * 初始化check
	 */
	private void initCheck() {
		boolean saveedChecked = ApplicationParams.getInstance().getsF().getBoolean(Cons.SFPatternFlag, false);
		mflagPattern.setChecked(saveedChecked);
		setChecked(saveedChecked);
	}

	/**
	 * 设置check
	 */
	private void setChecked(boolean isChecked) {

		// TODO Auto-generated method stub
		if (isChecked) {
			AndroidUtil.setFixedDrableBg(modGustureLay, R.drawable.setting_top_bg);
		} else {
			AndroidUtil.setFixedDrableBg(modGustureLay, R.drawable.setting_top_press);
		}
		ApplicationParams.getInstance().getsF().edit().putBoolean(Cons.SFPatternFlag, isChecked).commit();

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initCheck();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		case R.id.gesture_open_lay:
			//mflagPattern.setChecked(!mflagPattern.isChecked());
			checkBoxListener();
			break;
		case R.id.mod_gesture_lay:
			PiggyParams p = ApplicationParams.getInstance().getPiggyParameter();
			if (!mflagPattern.isChecked() || p.getUserInfo() == null) {
				return;
			}
			Bundle b = new Bundle();
			b.putParcelable(Cons.Intent_bean, new DialogBean.DialogBeanBuilder().setDialogId(INPUT_TYPE_PATTERNMODIFY).setInputHint("请输入交易密码").setSureBtnMsg("下一步").createDialogBean());
			LoginVerifyDialog d = LoginVerifyDialog.newInstance(b);
			d.setmBack(this);
			d.show(getFragmentManager(), "");
			MobclickAgent.onEvent(getActivity(), Cons.EVENT_UI_AccountManage, "手势密码修改");
			break;
		case R.id.mod_loginpwd_lay:
			intent = new Intent(getActivity(), ResetPwdActivity.class);
			intent.putExtra(Cons.Intent_type, ResetPwdFragment.typeUserPwd);
			startActivity(intent);
			MobclickAgent.onEvent(getActivity(), Cons.EVENT_UI_AccountManage, "修改登录密码");
			break;
		case R.id.mod_tradepwd_lay:
			intent = new Intent(getActivity(), ResetPwdActivity.class);
			intent.putExtra(Cons.Intent_type, ResetPwdFragment.typeTradePwd);
			startActivity(intent);
			MobclickAgent.onEvent(getActivity(), Cons.EVENT_UI_AccountManage, "修改交易密码");
			break;
		case R.id.mod_phone_lay:
			intent = new Intent(getActivity(), ResetPhoneActivity.class);
			intent.putExtra(Cons.Intent_type, 2);
			startActivity(intent);
			MobclickAgent.onEvent(getActivity(), Cons.EVENT_UI_AccountManage, "手机号修改");
			break;
		default:
			break;
		}
	}

	public class LoginTask extends MyAsyncTask<String, Void, LoginDto> {
		private String inputType;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			// Cons.SFUserNOHistory

		}

		@Override
		protected LoginDto doInBackground(String... params) {
			// TODO Auto-generated method stub
			inputType = params[2];
			return DispatchAccessData.getInstance().commitLogin(params[0], params[1]);
			// return
			// DispatchAccessData.getInstance().commitLogin("430523198804204190","123456");
		}

		@Override
		protected void onPostExecute(LoginDto result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (mpDialog != null && mpDialog.isShowing()) {
				mpDialog.dismiss();
			}
			if (result.getContentCode() == Cons.SHOW_SUCCESS) {
				String userNo = result.getCustNo();
				if (userNo == null) {
					setError();
				} else {
					setSuccess();
				}
			} else {
				showToastShort(result.getContentMsg());
				//setError();
			}
		}

		/**
		 * 登录成功
		 */
		public void setSuccess() {
			if (inputType.equals(String.valueOf(INPUT_TYPE_PATTERNFLAG))) {
				boolean isCurrCheck=!mflagPattern.isChecked();
				if (isCurrCheck) {//需要有手势密码
					Intent intent = new Intent(ApplicationParams.getInstance().getApplicationContext(), LockSetActivity.class);
					startActivityForResult(intent, 1);
				}else {//关闭手势密码
					//ApplicationParams.getInstance().getsF().edit().remove(Cons.SFPatternFlag).remove(Cons.SFPatternValue).commit();
					mflagPattern.setChecked(false);
					setChecked(false);
					showToastShort("关闭手势密码成功");
					MobclickAgent.onEvent(getActivity(), Cons.EVENT_UI_AccountManage, "关闭手势密码");
				}
			} else if (inputType.equals(String.valueOf(INPUT_TYPE_PATTERNMODIFY))) {
				Intent intent = new Intent(ApplicationParams.getInstance().getApplicationContext(), LockSetActivity.class);
				startActivityForResult(intent, 1);
			}
			
		}

		public void setError() {
			showToastShort("登录失败！");
//			if (inputType.equals(String.valueOf(InputCallBack.type_PatternFlag))) {
//				setChecked(!mflagPattern.isChecked());
//			} else if (inputType.equals(String.valueOf(InputCallBack.type_PatternModify))) {
//				
//			}
		}

	}

	@Override
	public void onCallBackPwd(String pwd, String type, boolean isCancel) {
		// TODO Auto-generated method stub
		String userId = getSf().getString(Cons.SFUserNOHistory, "");
		String userPwd = pwd;
		mInputType = type;
		mIsCancel = isCancel;
		if (isCancel) {
//			if (mInputType == InputCallBack.type_PatternFlag) {
//				boolean currCheck = !mflagPattern.isChecked();
//				mflagPattern.setChecked(currCheck);
//				setChecked(currCheck);
//			}
			return;
		}

		VerifyReslt msg = FieldVerifyUtil.verifyId(userId);
		if (!msg.isSuccess()) {
			showToastShort(msg.getMsg());
			return;
		}

		msg = FieldVerifyUtil.verifyLoginPwd(userPwd);
		if (!msg.isSuccess()) {
			showToastShort(msg.getMsg());
			return;
		}

		new LoginTask().execute(userId, userPwd, String.valueOf(mInputType));
		mpDialog = new PiggyProgressDialog(getActivity());
		mpDialog.setMessage("验证中...");
		mpDialog.show();
	}

}
