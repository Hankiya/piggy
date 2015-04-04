package howbuy.android.piggy.ui.fragment;

import howbuy.android.piggy.R;
import howbuy.android.piggy.api.dto.UserCardDto;
import howbuy.android.piggy.api.dto.UserCardListDto;
import howbuy.android.piggy.help.ParseWebReqUri.Web_Flag;
import howbuy.android.piggy.ui.ProPertyActivity;
import howbuy.android.piggy.ui.SettingMainActivity;
import howbuy.android.piggy.ui.base.AbstractFragment;
import howbuy.android.util.Cons;
import howbuy.android.util.CpUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class CpNetConnTimeoutFragment extends AbstractFragment {
	private Button sureBtn;
	private Button cancleBtn;
	private int intoType = BindCardFragment.Into_Setting;
	private UserCardDto mCurrCardDto;
	private CpUtil mCpUtil;

	@Override
	public boolean isShoudRegisterReciver() {
		return true;
	};

	@Override
	public void onServiceRqCallBack(Intent taskData, boolean isCurrPage) {
		super.onServiceRqCallBack(taskData, isCurrPage);
		Log.d("service", "onServiceRqCallBack--CpNetConnTimeoutFragment.java (2 matches)");
		mCpUtil.dissmisProgressDialog();
		if (!CpUtil.Request_Type_Nomal.equals(taskData.getStringExtra(Cons.Intent_id))&&isCurrPage) {
			UserCardListDto uInfoDto = taskData.getParcelableExtra(Cons.Intent_bean);
			String reqId = taskData.getStringExtra(Cons.Intent_id);
			if (uInfoDto != null && uInfoDto.getContentCode() == Cons.SHOW_SUCCESS) {
				if (reqId != CpUtil.Request_Type_Nomal ) {
					mCpUtil.handleStepRqRes(intoType, reqId, uInfoDto, mCurrCardDto);
				}
			} else {
				showToastTrue("获取用户信息失败");
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		getSherlockActivity().getSupportActionBar().setTitle("绑卡结果");
	}

	@Override
	public void onResume() {
		super.onResume();
		mCpUtil.onResumeCp();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.cp_net_conn_timeout, container, false);
		sureBtn = (Button) view.findViewById(R.id.sure_btn);
		cancleBtn = (Button) view.findViewById(R.id.cancle_btn);
		return view;
	}

	private void jumpFialdIntent() {
		Intent in = new Intent();
		if ((intoType & Web_Flag._flagLogin) == Web_Flag._flagLogin) {// web标致
			if ((intoType & Web_Flag.Flag_SaveMoney) == Web_Flag.Flag_SaveMoney) {
				getActivity().setResult(Activity.RESULT_CANCELED);
			} else if ((intoType & Web_Flag.Flag_BindCard) == Web_Flag.Flag_BindCard) {
				getActivity().setResult(Activity.RESULT_CANCELED);
			} else {
				startActProper();
			}
		} else {
			switch (intoType) {
			case BindCardFragment.Into_OutMoney:
				startActProper();
			case BindCardFragment.Into_Register:
			case BindCardFragment.Into_SaveMoney:
				startActProper();
				break;
			case BindCardFragment.Into_Setting:
				in = new Intent(getActivity().getApplicationContext(), SettingMainActivity.class);
				in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(in);
				break;
			default:
				startActProper();
				break;
			}
		}
		if (getActivity() != null) {
			getActivity().finish();
		}
	}

	/**
	 * 跳转到主页
	 */
	private void startActProper() {
		Intent startIntent;
		startIntent = new Intent(getSherlockActivity(), ProPertyActivity.class);
		startIntent.putExtra(Cons.Intent_type, ProPertyActivity.NeeduserDataReload);
		startActivity(startIntent);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		intoType = getActivity().getIntent().getIntExtra(Cons.Intent_type, BindCardFragment.Into_Setting);
		mCurrCardDto = getActivity().getIntent().getParcelableExtra(Cons.Intent_bean);
		mCpUtil = new CpUtil(intoType, this);
		sureBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 直接去cp验证银行卡
				mCpUtil.startBindCard(mCurrCardDto);
			}
		});
		cancleBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				jumpFialdIntent();
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onBackPressed() {
		jumpFialdIntent();
		return true;
	}

}
