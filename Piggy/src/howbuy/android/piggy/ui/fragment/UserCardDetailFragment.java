package howbuy.android.piggy.ui.fragment;

import howbuy.android.bean.DialogBean;
import howbuy.android.lib.MyAsyncTask;
import howbuy.android.piggy.R;
import howbuy.android.piggy.UserUtil;
import howbuy.android.piggy.api.DispatchAccessData;
import howbuy.android.piggy.api.dto.UserCardDto;
import howbuy.android.piggy.api.dto.UserCardListDto;
import howbuy.android.piggy.api.dto.UserCardUnBindDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.dialogfragment.LoginVerifyDialog;
import howbuy.android.piggy.help.ImageLoaderHelp;
import howbuy.android.piggy.service.ServiceMger;
import howbuy.android.piggy.service.UpdateUserDataService;
import howbuy.android.piggy.ui.AbsFragmentActivity;
import howbuy.android.piggy.ui.base.AbstractFragment;
import howbuy.android.piggy.widget.ImageTextBtn;
import howbuy.android.util.AmountUtil;
import howbuy.android.util.AndroidUtil;
import howbuy.android.util.Cons;
import howbuy.android.util.CpUtil;
import howbuy.android.util.StringUtil;
import howbuy.android.util.http.UrlMatchUtil;

import java.math.BigDecimal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

/**
 * 用户银行卡详情
 * 
 * @author Administrator
 */
public class UserCardDetailFragment extends AbstractFragment implements View.OnClickListener, LoginVerifyDialog.InputCallBack {
	public static final String TAG = "UserCardDetailFragment";
	private TextView bankinfo_name;
	private TextView bankinfo_no;
	private TextView bankinfo_status;
	private TextView bankinfo_limit;
	private ImageView bankinfo_icon;
	private TextView mTvBankBranchName; 
	private RelativeLayout mModifyBankBranchLay;
	private TextView mModifyBankHint;
	private ImageTextBtn mSubmitBtn;
	private UserCardDto mCardBean;
	private ImageLoaderHelp mDisplayImageHelp;
	private ProgressDialog mPgbbar;
	private CpUtil mCpUtil;

	@Override
	public boolean isShoudRegisterReciver() {
		return true;
	}

	@Override
	public void onServiceRqCallBack(Intent taskData, boolean isCurrPage) {
		super.onServiceRqCallBack(taskData, isCurrPage);
		mCpUtil.dissmisProgressDialog();
		if (isCurrPage && isAdded()) {
			String taskType = taskData.getStringExtra(Cons.Intent_type);
			if (UpdateUserDataService.TaskType_UserCard.equals(taskType)&&isCurrPage) {
				String reqId = taskData.getStringExtra(Cons.Intent_id);
				UserCardListDto userCards = taskData.getParcelableExtra(Cons.Intent_bean);
				if (reqId.equals(CpUtil.Request_Type_Cp_Success) || reqId.equals(CpUtil.Request_Type_Cp_TimeOut)) {
					if (userCards != null && userCards.getContentCode() == Cons.SHOW_SUCCESS) {
						mCpUtil.handleStepRqRes(BindCardFragment.Into_SettingCardList, reqId, userCards, mCardBean);
					} else {
						showToastTrue("获取用户信息失败");
					}
				} else if(TAG.equals(reqId)) {
					showToastTrue("银行卡解除绑定成功");
					disMissDialog();
					getSherlockActivity().setResult(Activity.RESULT_OK);
					getSherlockActivity().finish();
				}
			}
		}

	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		d(TAG+"=="+"onActivityResult");
		if (resultCode==Activity.RESULT_OK) {
			mCardBean=ApplicationParams.getInstance().getPiggyParameter().getUserCardInfo().getCardByIdOrAcct(mCardBean.getCustBankId());
			setBankinfo();
		}
	}

	private void disMissDialog() {
		if (mPgbbar != null && mPgbbar.isShowing()) {
			mPgbbar.dismiss();
		}
	}

	public void buildActionBar() {
		ActionBar ab = getSherlockActivity().getSupportActionBar();
		ab.setTitle("银行卡详情");
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.usercard_detail, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_unbind:
			if (AmountUtil.compareTwoCurrey(mCardBean.getAvailAmt(), "0") == 1) {
				showHasVolDialog();
			} else {
				showUnbindCardDialog();
			}
			break;
		case android.R.id.home:
			onBackPressed();
			break;
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
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		getSherlockActivity().setResult(Activity.RESULT_OK);
		getSherlockActivity().finish();
		return true;
	};

	private void showHasVolDialog() {
		new AlertDialog.Builder(getSherlockActivity()).setMessage("您的储蓄罐账户存有余额，请取出后尝试注销").setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).show();

	}

	private void showUnbindCardDialog() {
		new AlertDialog.Builder(getSherlockActivity()).setMessage("确认注销?").setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Bundle b = new Bundle();
				b.putParcelable(Cons.Intent_bean, new DialogBean.DialogBeanBuilder().setTitle("提示").setPwdType("1").setInputHint("请输入交易密码").setSureBtnMsg("确认").createDialogBean());
				LoginVerifyDialog d = LoginVerifyDialog.newInstance(b);
				d.setmBack(UserCardDetailFragment.this);
				d.show(getFragmentManager(), "");
			}
		}).setNeutralButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		}).show();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View convertView = inflater.inflate(howbuy.android.piggy.R.layout.aty_card_detail, container, false);
		mModifyBankBranchLay = (RelativeLayout) convertView.findViewById(howbuy.android.piggy.R.id.modfy_branchbank_lay);
		bankinfo_name = (TextView) convertView.findViewById(howbuy.android.piggy.R.id.bankinfo_name);
		bankinfo_no = (TextView) convertView.findViewById(howbuy.android.piggy.R.id.bankinfo_no);
		bankinfo_status = (TextView) convertView.findViewById(howbuy.android.piggy.R.id.very_status);
		bankinfo_limit = (TextView) convertView.findViewById(howbuy.android.piggy.R.id.bankinfo_limit);
		bankinfo_icon = (ImageView) convertView.findViewById(howbuy.android.piggy.R.id.bankinfo_icon);
		mSubmitBtn = (ImageTextBtn) convertView.findViewById(howbuy.android.piggy.R.id.submit_btn);
		mTvBankBranchName = (TextView) convertView.findViewById(howbuy.android.piggy.R.id.hint);
		convertView.findViewById(howbuy.android.piggy.R.id.allow).setVisibility(View.GONE);
		buildActionBar();
		return convertView;
	}

	public void setBankinfo() {
		mTvBankBranchName.setText(mCardBean.getBankRegionName());
		mDisplayImageHelp = new ImageLoaderHelp();
		bankinfo_name.setText(mCardBean.getBankName());
		bankinfo_no.setText(StringUtil.formatViewBankCard(mCardBean.getBankAcct()));
		String danBi = mCardBean.getLimitPerTime();// 单笔
		String danRi = mCardBean.getLimitPerDay();// 日限额
		bankinfo_limit.setText("单笔限存入金额" + formatLimit(danBi) + ",日限额" + formatLimit(danRi));
		
		mDisplayImageHelp.disImage(iconUrl(mCardBean.getBankCode()), bankinfo_icon);
		UserUtil.UserSoundType sType = UserUtil.userSoundStatus(mCardBean.getCustBankId());
		if (sType == UserUtil.UserSoundType.Sounduccess) {
			bankinfo_status.setVisibility(View.GONE);
			mSubmitBtn.setVisibility(View.GONE);
		} else {
			mSubmitBtn.setVisibility(View.VISIBLE);
			bankinfo_status.setVisibility(View.VISIBLE);
		}
		mSubmitBtn.setOnClickListener(this);
		mModifyBankBranchLay.setOnClickListener(this);
	}

	private String iconUrl(String bankCode) {
		String basePath2 = UrlMatchUtil.getBasepath2() + AndroidUtil.getSourceFolderName() + "/" + bankCode + ".png";
		return basePath2;
	}

	public String formatLimit(String danbi) {
		if (TextUtils.isEmpty(danbi)) {
			return "--万";
		}
		try {
			BigDecimal b = new BigDecimal(danbi);
			int a = b.intValue();
			if (a < 1) {
				return String.valueOf(a);
			} else {
				int danbiInt = a / 10000;
				String wan = String.valueOf(danbiInt) + "万";
				return wan;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return "--万";
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		mCardBean = getSherlockActivity().getIntent().getParcelableExtra(Cons.Intent_bean);
		setBankinfo();
		mCpUtil = new CpUtil(BindCardFragment.Into_SettingCardList, this);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mCpUtil.onResumeCp();
	}

	/**
	 * Called when a view has been clicked.
	 * 
	 * @param v
	 *            The view that was clicked.
	 */
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case howbuy.android.piggy.R.id.modfy_branchbank_lay:
			intent = new Intent(getActivity(), AbsFragmentActivity.class);
			UserCardDto uCardDto = mCardBean;
			intent.putExtra(Cons.Intent_name, UserCardModifyFragment.class.getName());
			intent.putExtra(Cons.Intent_bean, uCardDto);
			startActivityForResult(intent, 0);
			break;
		case howbuy.android.piggy.R.id.submit_btn:
			mCpUtil.startBankAuth(mCardBean.getBankCode(), mCardBean.getCustBankId());
			break;
		}
	}

	@Override
	public void onCallBackPwd(String pwd, String type, boolean isCancel) {
		if (!isCancel) {
			if (!TextUtils.isEmpty(pwd)) {
				new UnBindCardTask().execute(UserUtil.getUserNo(), mCardBean.getCustBankId(), pwd,mCardBean.getBankAcct());
				mPgbbar = new ProgressDialog(getSherlockActivity());
				mPgbbar.setMessage("正在注销中...");
				mPgbbar.show();
			}
		}
	}

	private class UnBindCardTask extends MyAsyncTask<String, Void, UserCardUnBindDto> {

		@Override
		protected UserCardUnBindDto doInBackground(String... params) {
			return DispatchAccessData.getInstance().unbindCard(params[0], params[1], params[2],params[3]);
		}

		@Override
		protected void onPostExecute(UserCardUnBindDto userCardUnBindDto) {
			super.onPostExecute(userCardUnBindDto);
			if (userCardUnBindDto.getContentCode() == Cons.SHOW_SUCCESS) {
				ApplicationParams.getInstance().getServiceMger().addTask(new ServiceMger.TaskBean(UpdateUserDataService.TaskType_UserCard, TAG));
			} else {
				disMissDialog();
				showToastTrue(userCardUnBindDto.getContentMsg());
			}
		}
	}
}
