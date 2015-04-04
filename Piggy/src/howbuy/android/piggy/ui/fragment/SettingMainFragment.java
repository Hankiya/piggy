package howbuy.android.piggy.ui.fragment;

import howbuy.android.piggy.R;
import howbuy.android.piggy.UserUtil;
import howbuy.android.piggy.api.DispatchAccessData;
import howbuy.android.piggy.api.dto.UserCardListDto;
import howbuy.android.piggy.api.dto.UserInfoDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.dialog.PiggyProgressDialog;
import howbuy.android.piggy.parameter.PiggyParams;
import howbuy.android.piggy.service.ServiceMger.TaskBean;
import howbuy.android.piggy.service.UpdateUserDataService;
import howbuy.android.piggy.ui.AbsFragmentActivity;
import howbuy.android.piggy.ui.BindCardActivity;
import howbuy.android.piggy.ui.LoginActivity;
import howbuy.android.piggy.ui.SettingAccountActivity;
import howbuy.android.piggy.ui.WebViewActivity;
import howbuy.android.piggy.ui.base.AbstractFragment;
import howbuy.android.piggy.ui.fragment.PureWebFragment.PureType;
import howbuy.android.util.AndroidUtil;
import howbuy.android.util.Cons;
import howbuy.android.util.CpUtil;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.umeng.analytics.MobclickAgent;

/**
 * tradeManageLay del
 * 
 * @author Administrator
 * 
 */
public class SettingMainFragment extends AbstractFragment implements OnClickListener {
	private RelativeLayout accountInfoLay;
	private RelativeLayout accountManageLay;
	private LinearLayout bankAllLay;
	private RelativeLayout bankLay;
	private RelativeLayout helpLay;
	private RelativeLayout aouboutLay;
	private TextView accountInfo;
	private TextView accountInfoHint;
	private TextView tradeInfoHint;
	private TextView bankContent;
	private TextView bankHint;

	private UserInfoDto userInfo;
	private UserCardListDto userCards;
	private PiggyProgressDialog mpDialog;
	private int a;

	private void launchTask(String type) {
		ApplicationParams.getInstance().getServiceMger().addTask(new TaskBean(UpdateUserDataService.TaskType_UserInfo, type));
		ApplicationParams.getInstance().getServiceMger().addTask(new TaskBean(UpdateUserDataService.TaskType_UserCard, type));
	}

	@Override
	public boolean isShoudRegisterReciver() {
		return true;
	};

	@Override
	public void onServiceRqCallBack(Intent taskData, boolean isCurrPage) {
		// TODO Auto-generated method stub
		super.onServiceRqCallBack(taskData, isCurrPage);
		Log.d("service", "onServiceRqCallBack--SettingMainFragment.java (2 matches)");
		if (mpDialog != null && mpDialog.isShowing()) {
			mpDialog.dismiss();
		}
		String taskType = taskData.getStringExtra(Cons.Intent_type);
		if (UpdateUserDataService.TaskType_UserInfo.equals(taskType)||UpdateUserDataService.TaskType_UserCard.equals(taskType)) {
			initSetting();
		}
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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.aty_setting, container, false);
		accountInfoLay = (RelativeLayout) view.findViewById(R.id.account_info_lay);
		accountManageLay = (RelativeLayout) view.findViewById(R.id.account_manage_lay);
		
		bankAllLay = (LinearLayout) view.findViewById(R.id.bankall_lay);
		bankLay = (RelativeLayout) view.findViewById(R.id.bank_lay);
		helpLay = (RelativeLayout) view.findViewById(R.id.help_lay);
		aouboutLay = (RelativeLayout) view.findViewById(R.id.about_lay);

		accountInfo = (TextView) view.findViewById(R.id.account_info_title);
		accountInfoHint = (TextView) view.findViewById(R.id.account_info_hint);
		tradeInfoHint = (TextView) view.findViewById(R.id.trade_manage_hint);
		bankContent = (TextView) view.findViewById(R.id.bank_title);
		bankHint = (TextView) view.findViewById(R.id.bank_hint);

		accountInfoLay.setOnClickListener(this);
		accountManageLay.setOnClickListener(this);
		aouboutLay.setOnClickListener(this);
		helpLay.setOnClickListener(this);
		bankLay.setOnClickListener(this);

		return view;
	}

	public String getUserName() {
		String userName = "";
		UserInfoDto userInfoDto = ApplicationParams.getInstance().getPiggyParameter().getUserInfo();
		if (userInfoDto == null) {
			return "--";
		}
		userName = userInfoDto.getCustName();
		if (TextUtils.isEmpty(userName)) {
			return "--";
		}
		return userName;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initSetting();
	}

	private void showProgressDialog() {
		mpDialog = new PiggyProgressDialog(getActivity());
		mpDialog.show();
	}

	private void dissmisProgressDialog() {
		if (mpDialog != null && mpDialog.isShowing()) {
			mpDialog.dismiss();
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		// initSetting();
	}

	private void initSetting() {
		if (UserUtil.isLogin()) {
			userInfo = ApplicationParams.getInstance().getPiggyParameter().getUserInfo();
			userCards = ApplicationParams.getInstance().getPiggyParameter().getUserCardInfo();
			if (userInfo == null) {
				mpDialog = new PiggyProgressDialog(getActivity());
				mpDialog.setMessage("加载中...");
				mpDialog.show();
				launchTask(CpUtil.Request_Type_Nomal);
				return;
			}
			if (userCards == null) {
				mpDialog = new PiggyProgressDialog(getActivity());
				mpDialog.setMessage("加载中...");
				mpDialog.show();
				ApplicationParams.getInstance().getServiceMger().addTask(new TaskBean(UpdateUserDataService.TaskType_UserCard, "0"));
				return;
			}

			setAccount();
			setTradeUnCount();
		} else {
			setNoAccount();
			return;
		}

		// UserUtil.UserBankType bankType = UserUtil.BindCardStatus();

		if (userCards!=null&&userCards.getUserCardDtos()!=null&&userCards.getUserCardDtos().size() > 0) {
			setBank();
		} else {
			setNoBank();
		}

		setNotUserData();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/**
	 * 特殊情况
	 */
	private void setNotUserData() {
		if (UserUtil.isLogin() && (PiggyParams.getInstance().getUserInfo() == null || PiggyParams.getInstance().getProductInfo() == null)) {
		}
		if (UserUtil.isLogin()) {
			if (PiggyParams.getInstance().getUserInfo() == null || PiggyParams.getInstance().getProductInfo() == null) {
				bankAllLay.setVisibility(View.GONE);
			} else {
				bankAllLay.setVisibility(View.VISIBLE);
			}
		}

	}

	private void setTradeUnCount() {
		UserInfoDto accessInfo = ApplicationParams.getInstance().getPiggyParameter().getUserInfo();
		if (accessInfo == null || accessInfo.contentCode != Cons.SHOW_SUCCESS) {
			return;
		}
		int unCuncount = accessInfo.getUnconfirmVolCount() == null ? 0 : Integer.parseInt(accessInfo.getUnconfirmVolCount());
		int unQucount = accessInfo.getUnconfirmAmtCount() == null ? 0 : Integer.parseInt(accessInfo.getUnconfirmAmtCount());
		if (tradeInfoHint == null) {
			return;
		}
		int sum = unCuncount + unQucount;
		if (sum == 0) {
			tradeInfoHint.setVisibility(View.INVISIBLE);
		} else {
			tradeInfoHint.setVisibility(View.VISIBLE);
			if (unCuncount == 0 && unQucount != 0) {
				tradeInfoHint.setText("您有" + String.valueOf(sum) + "笔取钱中");
			} else if (unCuncount != 0 && unQucount == 0) {
				tradeInfoHint.setText("您有" + String.valueOf(sum) + "笔存钱中");
			} else if (unCuncount != 0 && unQucount != 0) {
				tradeInfoHint.setText("您有" + String.valueOf(sum) + "笔存/取钱中");
			} else {
				tradeInfoHint.setVisibility(View.GONE);
			}
		}
	}

	private void setNoAccount() {
		setAccountInfo();
		AndroidUtil.setFixedDrableBg(accountInfoLay, R.drawable.setting_one_bg);
		accountManageLay.setVisibility(View.GONE);
		bankAllLay.setVisibility(View.GONE);
		setNoAccountInfo();
	}

	private void setAccount() {
		AndroidUtil.setFixedDrableBg(accountInfoLay, R.drawable.setting_top_bg);
		accountManageLay.setVisibility(View.VISIBLE);
		bankAllLay.setVisibility(View.VISIBLE);
		setAccountInfo();
	}

	private void setNoBank() {
		AndroidUtil.setFixedDrableBg(bankLay, R.drawable.setting_one_bg);
		AndroidUtil.setFixedDrableBg(accountManageLay, R.drawable.setting_buttom_bg);
		setNoBankInfo();
	}

	private void setBank() {
		AndroidUtil.setFixedDrableBg(bankLay, R.drawable.setting_buttom_bg);
		AndroidUtil.setFixedDrableBg(accountManageLay, R.drawable.setting_item_bg);
		setBankInfo();
	}

	/**
	 * 没有登录时设置信息
	 */
	private void setNoAccountInfo() {
		accountInfo.setText(getResources().getString(R.string.notlogin));
		accountInfoHint.setText(getResources().getString(R.string.login));
	}

	/**
	 * 登录时显示用户名
	 */
	private void setAccountInfo() {
		accountInfo.setText("您好，" + getUserName());
		accountInfoHint.setText("注销");
	}

	/**
	 * 已经绑定银行卡 设置银行卡名称 银行卡账号 解除限额
	 */
	private void setBankInfo() {
		bankContent.setText("我的银行卡");
		int cardsSize=userCards.getUserCardDtos().size();
		bankHint.setText(cardsSize + "张");
	}

	/**
	 * 没有绑定银行卡
	 */
	private void setNoBankInfo() {
		bankContent.setText(getResources().getString(R.string.nothaveBank));
		bankHint.setText(getResources().getString(R.string.band));
	}

	private void clearActivityTask() {
		ApplicationParams.getInstance().clearActivityTask();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		case R.id.account_info_lay:
			MobclickAgent.onEvent(getActivity(), Cons.EVENT_UI_Setting, "注销");
			if (UserUtil.isLogin()) {
				new AlertDialog.Builder(getActivity()).setMessage("您确认要注销？").setPositiveButton("确认", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						ApplicationParams.getInstance().getsF().edit().remove(Cons.SFUserCusNo).remove(Cons.SFPatternValue).remove(Cons.SFPatternFlag)
								.remove(Cons.SFBindCardParams).commit();
						ApplicationParams.getInstance().getPiggyParameter().removeUserDataPrivate();
						ApplicationParams.getInstance().getsF().edit().putBoolean(Cons.SFLoginOutFlag, true).commit();
						Intent intent2 = new Intent(getActivity(), LoginActivity.class);
						intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
						intent2.putExtra(Cons.Intent_type, LoginFragment.LoginType_Setting);
						startActivity(intent2);
						if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
							clearActivityTask();
						}
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).create().show();
			} else {
				intent = new Intent(getActivity(), LoginActivity.class);
				intent.putExtra(Cons.Intent_type, LoginFragment.LoginType_Main);
				startActivity(intent);
			}
			break;
		case R.id.account_manage_lay:
			MobclickAgent.onEvent(getActivity(), Cons.EVENT_UI_Setting, "账户管理");
			intent = new Intent(getActivity(), SettingAccountActivity.class);
			startActivity(intent);
			break;
		case R.id.help_lay:
			MobclickAgent.onEvent(getActivity(), Cons.EVENT_UI_Setting, "帮助");
			intent = new Intent(getActivity(), WebViewActivity.class);
			intent.putExtra(Cons.Intent_type, PureType.help.getType());
			startActivity(intent);
			testMoreThread();
			
			
			break;
		case R.id.about_lay:
			MobclickAgent.onEvent(getActivity(), Cons.EVENT_UI_Setting, "关于");
			intent = new Intent(getActivity(), AbsFragmentActivity.class);
			intent.putExtra(Cons.Intent_name, "howbuy.android.piggy.ui.fragment.AboutFragment");
			startActivity(intent);
			break;
		case R.id.bank_lay:
			MobclickAgent.onEvent(getActivity(), Cons.EVENT_UI_Setting, "鉴权说明");
			if (userCards.getUserCardDtos().size()>0) {// 已经绑卡并验证通过
				intent = new Intent(getActivity(), AbsFragmentActivity.class);
				intent.putExtra(Cons.Intent_name, UserCardListFragment.class.getName());
				startActivity(intent);
			} else {
				intent = new Intent(getActivity(), BindCardActivity.class);
				intent.putExtra(Cons.Intent_type, BindCardFragment.Into_Setting);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
	}
	
	public void testMoreThread(){
		a = 0;
		for (int i = 0; i < 100; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					a++;
					// TODO Auto-generated method stub
					String userNo=UserUtil.getUserNo();
					if (a%2==0) {
						Log.d(getTag(), DispatchAccessData.getInstance().commitLogin("43053198804204198", "qq1111").toString());
					}else {
						Log.d(getTag(), DispatchAccessData.getInstance().getUserCardList(userNo, null).toString());
					}
				}
			}).start();
			
		}
	}

}
