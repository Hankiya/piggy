package howbuy.android.piggy.ui.fragment;

import howbuy.android.lib.MyAsyncTask;
import howbuy.android.piggy.R;
import howbuy.android.piggy.UserUtil;
import howbuy.android.piggy.api.DispatchAccessData;
import howbuy.android.piggy.api.dto.OneStringDto;
import howbuy.android.piggy.api.dto.ProvinceInfoDto;
import howbuy.android.piggy.api.dto.UserCardDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.dialogfragment.DialogBankBanchFragment;
import howbuy.android.piggy.dialogfragment.DialogBankProviceFragment;
import howbuy.android.piggy.dialogfragment.LoginVerifyDialog;
import howbuy.android.piggy.help.ImageLoaderHelp;
import howbuy.android.piggy.service.ServiceMger;
import howbuy.android.piggy.service.UpdateUserDataService;
import howbuy.android.piggy.ui.base.AbstractFragment;
import howbuy.android.piggy.widget.ImageTextBtn;
import howbuy.android.util.AndroidUtil;
import howbuy.android.util.Cons;
import howbuy.android.util.StringUtil;
import howbuy.android.util.http.UrlMatchUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

/**
 * 用户支行修改
 * 
 * @author Administrator
 * 
 */
public class UserCardModifyFragment extends AbstractFragment implements View.OnClickListener, LoginVerifyDialog.InputCallBack, DialogBankProviceFragment.SpinnerSelect,
		DialogBankBanchFragment.OnBankBanchLinster {
	public static final String TAG = "UserCardModifyFragment";
	private TextView bankinfo_name;
	private TextView bankinfo_no;
	private TextView bankinfo_status;
	private TextView bankinfo_limit;
	private ImageView bankinfo_icon;

	private Button mProvinceBtn;
	private Button mBankBranchBtn;

	private ImageTextBtn mSubmitBtn;
	private UserCardDto mCardBean;
	private ImageLoaderHelp mDisplayImageHelp;
	private ProgressDialog mPgbbar;

	private String mProvinceCode;
	private String mBankBranchCode;
	private DialogBankProviceFragment dSpinnerDialogFragment;
	private DialogBankBanchFragment sBanchFragment;

	@Override
	public boolean isShoudRegisterReciver() {
		return true;
	}

	@Override
	public void onServiceRqCallBack(Intent taskData, boolean isCurrPage) {
		super.onServiceRqCallBack(taskData, isCurrPage);
		if (isCurrPage && isAdded()) {
			String taskType = taskData.getStringExtra(Cons.Intent_type);
			String reqId = taskData.getStringExtra(Cons.Intent_id);
			if (UpdateUserDataService.TaskType_UserCard.equals(taskType) && TAG.equals(reqId)) {
				showToastTrue("银行卡修改成功");
				disMissDialog();
				getSherlockActivity().setResult(Activity.RESULT_OK);
				getSherlockActivity().finish();
			}
		}

	}

	private void disMissDialog() {
		if (mPgbbar != null && mPgbbar.isShowing()) {
			mPgbbar.dismiss();
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			getSherlockActivity().finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void buildActionBar() {
		ActionBar ab = getSherlockActivity().getSupportActionBar();
		ab.setTitle("支行号修改");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View convertView = inflater.inflate(R.layout.aty_card_modify, container, false);
		bankinfo_name = (TextView) convertView.findViewById(R.id.bankinfo_name);
		bankinfo_no = (TextView) convertView.findViewById(R.id.bankinfo_no);
		bankinfo_status = (TextView) convertView.findViewById(R.id.very_status);
		bankinfo_limit = (TextView) convertView.findViewById(R.id.bankinfo_limit);
		bankinfo_icon = (ImageView) convertView.findViewById(R.id.bankinfo_icon);
		mProvinceBtn = (Button) convertView.findViewById(R.id.provice_btn);
		mBankBranchBtn = (Button) convertView.findViewById(R.id.bankbranch_btn);
		mSubmitBtn = (ImageTextBtn) convertView.findViewById(R.id.submit_btn);
		bankinfo_status.setVisibility(View.GONE);
		bankinfo_limit.setVisibility(View.GONE);
		convertView.findViewById(R.id.allow).setVisibility(View.GONE);
		buildActionBar();
		return convertView;
	}

	public void setBankInfo() {
		mDisplayImageHelp = new ImageLoaderHelp();
		bankinfo_name.setText(mCardBean.getBankName());
		bankinfo_no.setText(StringUtil.formatViewBankCard(mCardBean.getBankAcct()));
		mDisplayImageHelp.disImage(iconUrl(mCardBean.getBankCode()), bankinfo_icon);

		// 设置省份 to dooo
		ProvinceInfoDto pi = ApplicationParams.getInstance().getPiggyParameter().getSupportBankInfo().getProvinceById(mCardBean.getProvCode());
		mProvinceBtn.setText(pi.getName());
		mProvinceCode = mCardBean.getProvCode();
		mBankBranchBtn.setText(mCardBean.getBankRegionName());
		mBankBranchCode = mCardBean.getBankRegionCode();

		mSubmitBtn.setOnClickListener(this);
		mProvinceBtn.setOnClickListener(this);
		mBankBranchBtn.setOnClickListener(this);
	}

	private String iconUrl(String bankCode) {
		String basePath2 = UrlMatchUtil.getBasepath2() + AndroidUtil.getSourceFolderName() + "/" + bankCode + ".png";
		return basePath2;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		mCardBean = getSherlockActivity().getIntent().getParcelableExtra(Cons.Intent_bean);
		setBankInfo();
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * Called when a view has been clicked.
	 * 
	 * @param v
	 *            The view that was clicked.
	 */
	@Override
	public void onClick(View v) {
		Intent it;
		switch (v.getId()) {
		case R.id.provice_btn:
			dSpinnerDialogFragment = DialogBankProviceFragment.newInstance(2, ApplicationParams.getInstance().getPiggyParameter().getSupportBankInfo(), this);
			dSpinnerDialogFragment.show(getSherlockActivity().getSupportFragmentManager(), String.valueOf(1));
			break;
		case R.id.bankbranch_btn:
			if (sBanchFragment == null) {
				// isFirstClick=false;
				sBanchFragment = DialogBankBanchFragment.newInstance(mCardBean.getBankCode(), mProvinceCode, this);
			}
			sBanchFragment.show(getSherlockActivity().getSupportFragmentManager(), String.valueOf("2"));
			break;
		case R.id.submit_btn:
			// String custNo, String cnapsNo, String custBankId
			if (mBankBranchCode.equals(mCardBean.getBankRegionCode())) {
				showToastTrue("您未变更支行信息");
			} else {
				new BindCardModifyTask().execute(UserUtil.getUserNo(), mBankBranchCode, mCardBean.getCustBankId());
				mPgbbar = new ProgressDialog(getSherlockActivity());
				mPgbbar.setMessage("提交数据中...");
				mPgbbar.show();
			}

			break;
		}
	}

	/**
	 * 外部调用获取支行列表 i
	 * 
	 * @param bankAndProviceName
	 */
	public void createData(String bankAndProviceName) {
		String bankCode = mCardBean.getBankCode();
		if (TextUtils.isEmpty(bankCode) || TextUtils.isEmpty(mProvinceCode)) {
			return;
		}
		sBanchFragment = DialogBankBanchFragment.newInstance(bankCode, mProvinceCode, this);
		sBanchFragment.outDoTask(bankCode, mProvinceCode, DialogBankBanchFragment.typeLoadOut);
	}

	@Override
	public void onCallBackPwd(String pwd, String type, boolean isCancel) {
		if (!isCancel) {
			if (!TextUtils.isEmpty(pwd)) {
				new BindCardModifyTask().execute(UserUtil.getUserNo(), mCardBean.getCustBankId(), pwd);
				mPgbbar = new ProgressDialog(getSherlockActivity());
				mPgbbar.setMessage("正在注销中...");
				mPgbbar.dismiss();
			}
		}
	}

	@Override
	public void onNoData(boolean isNoData) {
		// 没有相关支行号
		showToastShort("没有支行号信息");
	}

	@Override
	public void onBankBranchClick(String id, String bankName, String cityCode) {
		mBankBranchBtn.setText(bankName);
		mBankBranchCode = id;
	}

	@Override
	public void onSelect(int mType, String bankAndProvinceName, String bankAndProviceCode) {
		mProvinceBtn.setText(bankAndProvinceName);
		mProvinceCode = bankAndProviceCode;
		createData(bankAndProviceCode);
	}

	private class BindCardModifyTask extends MyAsyncTask<String, Void, OneStringDto> {

		@Override
		protected OneStringDto doInBackground(String... params) {
			return DispatchAccessData.getInstance().bindCardModify(params[0], params[1], params[2]);
		}

		@Override
		protected void onPostExecute(OneStringDto userCardUnBindDto) {
			super.onPostExecute(userCardUnBindDto);
			if (userCardUnBindDto.getContentCode() == Cons.SHOW_SUCCESS) {
				String oneString = userCardUnBindDto.getOneString();
				if ("1".equals(oneString) || "1.0".equals(oneString)) {
					ApplicationParams.getInstance().getServiceMger().addTask(new ServiceMger.TaskBean(UpdateUserDataService.TaskType_UserCard, TAG));
				} else {
					disMissDialog();
					showToastTrue("银行修改失败");
				}
			} else {
				disMissDialog();
				showToastTrue(userCardUnBindDto.getContentMsg());
			}
		}
	}
}
