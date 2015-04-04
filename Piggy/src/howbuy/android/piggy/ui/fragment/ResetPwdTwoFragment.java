package howbuy.android.piggy.ui.fragment;

import howbuy.android.lib.MyAsyncTask;
import howbuy.android.piggy.R;
import howbuy.android.piggy.api.DispatchAccessData;
import howbuy.android.piggy.api.dto.OneStringDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.dialog.PiggyProgressDialog;
import howbuy.android.piggy.ui.LoginActivity;
import howbuy.android.piggy.ui.SettingAccountActivity;
import howbuy.android.piggy.ui.base.AbstractFragment;
import howbuy.android.piggy.widget.ClearableEdittext;
import howbuy.android.piggy.widget.ImageTextBtn;
import howbuy.android.util.Cons;
import howbuy.android.util.FieldVerifyUtil;
import howbuy.android.util.FieldVerifyUtil.VerifyReslt;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.actionbarsherlock.view.MenuItem;

public class ResetPwdTwoFragment extends AbstractFragment implements OnClickListener {
	private int typeResetPwd;
	private String idNo;
	howbuy.android.piggy.widget.ClearableEdittext mPwd;// mPwdRe;
	private ImageTextBtn mSubmitBtn;
	private PiggyProgressDialog mpDialog;

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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.aty_resetpwd_two, container, false);
		mPwd = (howbuy.android.piggy.widget.ClearableEdittext) view.findViewById(R.id.pwd);
//		mPwdRe = (howbuy.android.piggy.widget.ClearableEdittext) view.findViewById(R.id.pwdre);
		mSubmitBtn = (ImageTextBtn) view.findViewById(R.id.submit_btn);
		mSubmitBtn.setOnClickListener(this);
		mSubmitBtn.setEnabled(false);
		mPwd.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mSubmitBtn.setEnabled(true);
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
		if (getArguments() == null) {
			return;
		}
		
		
		typeResetPwd = getArguments().getInt(Cons.Intent_type, 0);
		idNo = getArguments().getString(Cons.Intent_id);
		String title;
		if (typeResetPwd == ResetPwdFragment.typeUserPwd || typeResetPwd == ResetPwdFragment.typeForgetPwd) {
			title = "修改登录密码";
			mPwd.setHint(R.string.hint_userpwd);
		} else {
			title = "修改交易密码";
			mPwd.setHint(R.string.hint_tradepwd);
			mPwd.setInputType(InputType.TYPE_CLASS_NUMBER);
		}
		mPwd.setClearType(ClearableEdittext.TypePas);
		getSherlockActivity().getSupportActionBar().setTitle(title);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.submit_btn:
			resetPwdCommit();
			break;

		default:
			break;
		}
	}

	public void resetPwdCommit() {
		String userPwdValue = mPwd.getText().toString();
//		String userPwdReValue = mPwdRe.getText().toString();
		String custNo = getSf().getString(Cons.SFUserCusNo, null);
		int type=ResetPwdFragment.typeUserPwd;
		
		VerifyReslt msg;
		
		if (typeResetPwd == ResetPwdFragment.typeUserPwd|| typeResetPwd == ResetPwdFragment.typeForgetPwd) {
			msg=FieldVerifyUtil.verifyLoginPwd(userPwdValue);
			type=ResetPwdFragment.typeUserPwd;
		}else {
			msg=FieldVerifyUtil.verifyTradePwd(userPwdValue);
			type=ResetPwdFragment.typeTradePwd;
		}
		if (!msg.isSuccess()) {
			showToastShort(msg.getMsg());
			return;
		}
		
		
		
//		if (TextUtils.isEmpty(userPwdReValue)) {
//			showToastShort("重复密码不能为空");
//			return;
//		}
//
//		if (!userPwdValue.equals(userPwdReValue)) {
//			showToastShort("密码不相同");
//			return;
//		}

		// //custNo, password, pwdType
		new ResetPwdCommit().execute(custNo, idNo, ResetPwdFragment.UserNoTypeDefault, userPwdValue, String.valueOf(type));// custNo,
																																	// idNo,
																																	// idType,
																																	// password,
																																	// pwdType
		mpDialog = new PiggyProgressDialog(getActivity());
		mpDialog.setMessage("正在修改密码...");
		mpDialog.show();
	}

	public class ResetPwdCommit extends MyAsyncTask<String, Void, OneStringDto> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected OneStringDto doInBackground(String... params) {
			// TODO Auto-generated method stub
			return DispatchAccessData.getInstance().passwordCommit(params[0], params[1], params[2], params[3], params[4]);// custNo,
			// password,
			// pwdType
		}

		@Override
		protected void onPostExecute(OneStringDto result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (mpDialog != null && mpDialog.isShowing()) {
				mpDialog.dismiss();
			}
			if (result.getContentCode() == Cons.SHOW_SUCCESS) {
				showToastTrueLong("修改密码成功！");
				if ( typeResetPwd == ResetPwdFragment.typeForgetPwd) {
					luncherLogin();
				}else {
					luncherMain();
				}
			} else {
				showToastShort(result.getContentMsg());
			}
		}
	}
	
	private void luncherMain(){
		Intent intent=new Intent(ApplicationParams.getInstance().getApplicationContext(),SettingAccountActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	private void luncherLogin(){
		Intent intent=new Intent(ApplicationParams.getInstance().getApplicationContext(),LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

}
