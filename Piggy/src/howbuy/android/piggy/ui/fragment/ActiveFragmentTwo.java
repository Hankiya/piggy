package howbuy.android.piggy.ui.fragment;

import howbuy.android.lib.MyAsyncTask;
import howbuy.android.piggy.R;
import howbuy.android.piggy.api.DispatchAccessData;
import howbuy.android.piggy.api.dto.OneStringDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.dialog.PiggyProgressDialog;
import howbuy.android.piggy.ui.LoginActivity;
import howbuy.android.piggy.ui.base.AbstractFragment;
import howbuy.android.piggy.widget.ClearableEdittext;
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

import com.actionbarsherlock.view.MenuItem;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author yesCpu
 * 
 */
public class ActiveFragmentTwo extends AbstractFragment implements OnClickListener {

	private PiggyProgressDialog mpDialog;
	private ClearableEdittext mUserPwd;
	private ClearableEdittext mTradePwd;
	private ClearableEdittext mSelfHint;
	private ImageTextBtn mSubmitBtn;
	private String mUserNo;
	private String mMobile;
	private int mForwordType;

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
		mUserNo= getArguments().getString("no");
		mMobile= getArguments().getString("mob");
		mForwordType = getSherlockActivity().getIntent().getIntExtra(Cons.Intent_type, 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.aty_active_two, container, false);
		mUserPwd = (ClearableEdittext) view.findViewById(R.id.user_pwd);
		mTradePwd = (ClearableEdittext) view.findViewById(R.id.trade_pwd);
		mSelfHint = (ClearableEdittext) view.findViewById(R.id.selfhint);
		mSubmitBtn = (ImageTextBtn) view.findViewById(R.id.submit_btn);
		mSubmitBtn.setOnClickListener(this);
		mSubmitBtn.setEnabled(false);
		mUserPwd.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!(TextUtils.isEmpty(mTradePwd.getEditableText().toString()) || TextUtils.isEmpty(mSelfHint.getEditableText().toString()))){
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
		mTradePwd.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!(TextUtils.isEmpty(mUserPwd.getEditableText().toString()) || TextUtils.isEmpty(mSelfHint.getEditableText().toString()))){
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
		mSelfHint.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!(TextUtils.isEmpty(mUserPwd.getEditableText().toString()) || TextUtils.isEmpty(mTradePwd.getEditableText().toString()))){
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
		mUserPwd.setClearType(ClearableEdittext.TypePas);
		mTradePwd.setClearType(ClearableEdittext.TypePas);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent in;
		switch (v.getId()) {
		case R.id.submit_btn:
			String uPwd = mUserPwd.getText().toString();
			String tPwd = mTradePwd.getText().toString();
			String msg = mSelfHint.getText().toString();
			
			VerifyReslt vmsg=FieldVerifyUtil.verifyLoginPwd(uPwd);
			if (!vmsg.isSuccess()) {
				showToastShort(vmsg.getMsg());
				return;
			}
			
			vmsg=FieldVerifyUtil.verifyTradePwd(tPwd);
			if (!vmsg.isSuccess()) {
				showToastShort(vmsg.getMsg());
				return;
			}
			
			if (TextUtils.isEmpty(msg)) {
				showToastShort("防钓鱼信息不能为空");
			}

			mpDialog = new PiggyProgressDialog(getActivity());
			mpDialog.setMessage("激活中...");
			mpDialog.show();
			new ActivieCommit().execute(mUserNo, "0", mMobile, uPwd, tPwd, msg);
			MobclickAgent.onEvent(getActivity(), Cons.EVENT_UI_Active, "确认激活");
			break;
		default:
			break;
		}
	}

	public class ActivieCommit extends MyAsyncTask<String, Void, OneStringDto> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected OneStringDto doInBackground(String... params) {
			// TODO Auto-generated method stub

//			 return DispatchAccessData.getInstance().activeSubmit(custNo, idNo, idType, mobile, loginPasswd, txPassword, selfMsg)
			return DispatchAccessData.getInstance().activeSubmit(params[0], params[1], params[2], params[3], params[4], params[5]);
		}

		@Override
		protected void onPostExecute(OneStringDto result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (mpDialog != null && mpDialog.isShowing()) {
				mpDialog.dismiss();
			}
			if (result.getContentCode() == Cons.SHOW_SUCCESS) {
//				String userNo = result.getOneString();
//				if (userNo == null) {
//					showToastShort("未知错误！");
//				} else {
					//getSf().edit().putString(Cons.SFUserNOHistory,mUserNo).commit();
					showToastTrue("激活成功！");
					activitySuccess();
//				}
			} else {
				showToastShort(result.getContentMsg());
			}
		}

	}

	private void activitySuccess() {
		// TODO Auto-generated method stub
		Intent startIntent=new Intent(ApplicationParams.getInstance().getApplicationContext(), LoginActivity.class);
		startIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		int defaultType = LoginFragment.LoginType_Active;
		if (mForwordType != 0) {
			defaultType = defaultType | mForwordType;
		}
		startIntent.putExtra(Cons.Intent_type, defaultType);
		startIntent.putExtra(Cons.Intent_id, mUserNo);
		startActivity(startIntent);
		getActivity().finish();
	}

}
