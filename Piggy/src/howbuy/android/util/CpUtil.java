package howbuy.android.util;

import howbuy.android.lib.MyAsyncTask;
import howbuy.android.piggy.UserUtil;
import howbuy.android.piggy.UserUtil.UserBankVeryType;
import howbuy.android.piggy.api.DispatchAccessData;
import howbuy.android.piggy.api.dto.BindCardAuthModeDto;
import howbuy.android.piggy.api.dto.BindCardDto;
import howbuy.android.piggy.api.dto.BindCardUploadParams;
import howbuy.android.piggy.api.dto.BindCardUploadParams.Channels;
import howbuy.android.piggy.api.dto.UpdateDto;
import howbuy.android.piggy.api.dto.UserCardDto;
import howbuy.android.piggy.api.dto.UserCardListDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.dialog.PiggyProgressDialog;
import howbuy.android.piggy.service.ServiceMger.TaskBean;
import howbuy.android.piggy.service.UpdateUserDataService;
import howbuy.android.piggy.ui.BindCardSucceedActivity;
import howbuy.android.piggy.ui.CpNetConnTimeoutActivity;
import howbuy.android.piggy.ui.PrefectActivity;
import howbuy.android.piggy.ui.base.AbstractFragment;
import howbuy.android.piggy.ui.fragment.BindCardFragment;
import howbuy.android.piggy.ui.fragment.BindCardSucceedFragment;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.google.myjson.Gson;
import com.hxcr.chinapay.activity.Initialize;
import com.hxcr.chinapay.util.CPGlobaInfo;
import com.hxcr.chinapay.util.Utils;

public class CpUtil {
	public static final String Request_Type_Nomal = "11";
	public static final String Request_Type_Cp_Success = "12";
	public static final String Request_Type_Cp_TimeOut = "13";
	private PiggyProgressDialog mpDialog;
	private AbstractFragment mContext;
	private int mIntoType;
	private BindTask mBindTask;
	private AuthTask mAuthTask;
	private String mBankAcct = null;

	public CpUtil(int intoType, AbstractFragment mContext) {
		this.mIntoType = intoType;
		this.mContext = mContext;
	}

	/**
	 * 处理onResume
	 */
	public void onResumeCp() {
		final String cpResCode = Utils.getPayResult();
		CPGlobaInfo.init(); // 清空返回结果
		if (!TextUtils.isEmpty(cpResCode)) {
			String res = handleCpRes(mIntoType, cpResCode);
			if (res == null) {
				// 超时|成功这时已handleCpRes去获取用户信息
				showProgressDialog("正在获取数据");
			}
		}
	}

	public void startBindCard(UserCardDto card) {
		if (mBindTask != null) {
			mBindTask.cancel(true);
		}
		mBindTask = new BindTask();
		// String custNo, String bankAcct, String bankCode, String cnapsNo,
		// String provCode, String custBankId
		String custNo = UserUtil.getUserNo();
		mBankAcct = card.getBankAcct();
		String bankCode = card.getBankCode();
		String cnapsNo = card.getBankRegionCode();
		String provCode = card.getProvCode();
		String custBankId = card.getCustBankId();
		mBindTask.execute(custNo, mBankAcct, bankCode, cnapsNo, provCode, custBankId);
		showProgressDialog("正在绑定银行卡");
	}

	public void startBankAuth( String bankCode, String custBankId) {
		if (mAuthTask != null) {
			mAuthTask.cancel(true);
		}
		mAuthTask = new AuthTask();
		String custNo = UserUtil.getUserNo();
//String custNo, String bankCode, String custBankId,
		mAuthTask.execute(custNo,bankCode,  custBankId);
		showProgressDialog("正在验证银行卡");
	}

	public static String parseCpResult(String cpCode) {
		if (!TextUtils.isEmpty(cpCode) && cpCode.length() >= 14) {
			return cpCode.trim().substring(10, 14);
		}
		return null;
	}

	public static boolean isSuccess(String cpRes) {
		if (!TextUtils.isEmpty(cpRes) && cpRes.equals("0000")) {
			return true;
		}
		return false;
	}

	public static boolean isTimeOut(String cpRes) {
		if (!TextUtils.isEmpty(cpRes) && cpRes.equals("9901")) {
			return true;
		}
		return false;
	}

	public static String otherRes(String cpResult) {
		if (!TextUtils.isEmpty(cpResult)) {
			// if ("9903".equals(cpResult)) {
			// return "报文解析错误";
			// } else if ("9902".equals(cpResult)) {
			// return "您已取消绑卡";
			// } else if ("9904".equals(cpResult)) {
			// return "配置文件验证失败";
			// } else if ("9905".equals(cpResult)) {
			// return "调用参数不正确";
			// } else if ("2007".equals(cpResult)) {
			// return "通讯超时";
			// } else if (cpResult.startsWith("200")) {
			// return "通讯异常";
			// } else {
			// // return "系统异常");
			// }
			return "绑卡失败！";
		}
		return "未知异常";
	}

	public void dissmisProgressDialog() {
		if (mpDialog != null && mpDialog.isShowing()) {
			mpDialog.dismiss();
		}
	}

	public void showProgressDialog(String text) {
		mpDialog = new PiggyProgressDialog(mContext.getActivity());
		mpDialog.show();
		mpDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				if (mBindTask != null) {
					mBindTask.cancel(true);
				}
			}
		});
	}

	public String handleCpRes(int pageType, String cpCode) {
		String cpRes = parseCpResult(cpCode);
		if (isSuccess(cpRes)) {
			launchTask(Request_Type_Cp_Success, null);
			return null;
		} else if (isTimeOut(cpRes)) {
			launchTask(Request_Type_Cp_TimeOut, new String[] { mBankAcct });
			return null;
		}
		// 失败
		String otherRes = otherRes(cpRes);
		Intent ien = new Intent();
		ien.putExtra(Cons.Intent_type, pageType);
		ien.putExtra(Cons.Intent_id, BindCardSucceedFragment.Type_Res_BindCardFailed);
		ien.setClass(mContext.getSherlockActivity(), BindCardSucceedActivity.class);
		mContext.startActivity(ien);
		return otherRes;
	}

	public void handleStepRqRes(int intoType, String reqId, UserCardListDto cardListDto, UserCardDto mCardDto) {
		try {

			UserCardListDto u = cardListDto;
			if (u != null && u.getContentCode() == Cons.SHOW_SUCCESS) {
				UserUtil.UserBankVeryType bankType = UserUtil.VrfyCardStatus(mCardDto.getBankAcct());
				Intent ien = new Intent();
				if ((bankType == UserBankVeryType.VrfySuccessBindCard)) {
					ien.putExtra(Cons.Intent_type, intoType);
					ien.putExtra(Cons.Intent_id, BindCardSucceedFragment.Type_Res_BindCardSuccess);
					ien.setClass(mContext.getSherlockActivity(), BindCardSucceedActivity.class);
					try {
						Activity a = (Activity) mContext.getActivity();
						if (a.getComponentName().getClassName().contains("BindCardActivity")) {
							a.finish();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					if (Request_Type_Cp_TimeOut.equals(reqId)) {
						ien.putExtra(Cons.Intent_type, intoType);
						ien.putExtra(Cons.Intent_bean, mCardDto);
						ien.setClass(mContext.getActivity(), CpNetConnTimeoutActivity.class);
					} else {//
						ien.putExtra(Cons.Intent_type, intoType);
						ien.putExtra(Cons.Intent_id, BindCardSucceedFragment.Type_Res_BindCardFailed);
						ien.setClass(mContext.getActivity(), BindCardSucceedActivity.class);
					}
				}
				mContext.startActivity(ien);
			} else {
				// 获取用户银行卡信息失败
				
			}
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

	private void launchTask(String type, String[] args) {
		ApplicationParams.getInstance().getServiceMger().addTask(new TaskBean(UpdateUserDataService.TaskType_UserCard, type, args));
	}
	
	
	private void startCpPluis(BindCardDto result){
		Utils.setPackageName(mContext.getSherlockActivity().getPackageName());
		Intent t = new Intent(mContext.getSherlockActivity(), Initialize.class);
		String info = result.buildOrderInf(Cons.CP_Debug_Flag);
		t.putExtra(CPGlobaInfo.XML_TAG, info);
		mContext.startActivity(t);
	}

	/**
	 * 绑定银行task
	 * 
	 * @ClassName: BindCardFragment.java
	 * @Description:
	 * @author yescpu yes.cpu@gmail.com
	 * @date 2013-10-17下午4:55:59
	 */
	public class BindTask extends MyAsyncTask<String, Void, BindCardDto> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected BindCardDto doInBackground(String... params) {
			// TODO Auto-generated method stub
			return DispatchAccessData.getInstance().bindCard(params[0], params[1], params[2], params[3], params[4], params[5], getSupportChalln());// custNo,
		}

		@Override
		protected void onPostExecute(BindCardDto result) {
			super.onPostExecute(result);
			if (result.getContentCode() == Cons.SHOW_SUCCESS) {// 普通渠道绑卡成功
				String channelId = result.getChannelId();
				if (channelId!=null &&channelId.equals("1013")) {
					startCpPluis(result);
				}else {
					BindCardAuthModeDto defaultAuth= result.getDefaultAuth();
					if (defaultAuth!=null) {
						if (defaultAuth.getAuthMode().equals(BindCardAuthModeDto.Auth_WeChat)) {
							//返回成功
							mContext.showToastShort("绑定成功");
							ApplicationParams.getInstance().getServiceMger().addTask(new TaskBean(UpdateUserDataService.TaskType_UserCard, BindCardFragment.BindCardRequsetNormal));
						}else if (defaultAuth.getAuthMode().equals(BindCardAuthModeDto.Auth_DaKuang)) {
							//返回成功
							mContext.showToastShort("绑定成功");
							ApplicationParams.getInstance().getServiceMger().addTask(new TaskBean(UpdateUserDataService.TaskType_UserCard, BindCardFragment.BindCardRequsetNormal));
						}else if(defaultAuth.getAuthMode().equals(BindCardAuthModeDto.Auth_B2c)){
							//调用cp
							startCpPluis(result);
						}else {
							// 调用升级接口
							showProgressDialog("请稍后...");
							new UpdateTask().execute();
						}
					}else {
						mContext.showToastShort("绑定成功");
						ApplicationParams.getInstance().getServiceMger().addTask(new TaskBean(UpdateUserDataService.TaskType_UserCard, BindCardFragment.BindCardRequsetNormal));
					}
				}
			} else if (result.getContentCode() == Cons.SHOW_BindOtherChannl) {
				dissmisProgressDialog();
				mContext.showToastShort(result.getContentMsg());
			} else {
				dissmisProgressDialog();
				mContext.showToastShort(result.getContentMsg());
			}
		}
	}
	

	public class UpdateTask extends MyAsyncTask<Void, Void, UpdateDto> {

		@Override
		protected UpdateDto doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return DispatchAccessData.getInstance().checkUpdate();
		}

		@Override
		protected void onPostExecute(UpdateDto result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (mpDialog != null && mpDialog.isShowing()) {
				mpDialog.dismiss();
			}
			if (mContext.isAdded()) {
				if (result.contentCode == Cons.SHOW_SUCCESS) {
					UpdateDto arg1 = result;
					// arg1.setVersionNeedUpdate("1");
					String needUpdate = arg1.getVersionNeedUpdate();
					if (!needUpdate.equals("2")) {
						String url = arg1.getUpdateUrl();
						showUpdateDialog(url);
					}
				}
				// getActivity().finish();
			}
		}

		private void showUpdateDialog(final String url) {

			new AlertDialog.Builder(mContext.getActivity()).setMessage("当前版本不支持新增的开户渠道，更新到新版本后即可开户。").setNegativeButton("取消", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			}).setPositiveButton("升级", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					String murl = url;
					Intent i = new Intent("android.intent.action.VIEW");
					i.setData(Uri.parse(murl));
					mContext.startActivity(i);
				}
			});
		}

	}

	/**
	 * 银行鉴权task
	 * 
	 * @ClassName: BindCardFragment.java
	 * @Description:
	 * @author yescpu yes.cpu@gmail.com
	 * @date 2013-10-17下午4:55:59
	 */
	public class AuthTask extends MyAsyncTask<String, Void, BindCardDto> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected BindCardDto doInBackground(String... params) {
			return DispatchAccessData.getInstance().bankAuth(params[0], params[1], params[2], getSupportChalln());// custNo,
		}

		@Override
		protected void onPostExecute(BindCardDto result) {
			super.onPostExecute(result);
			if (result.getContentCode() == Cons.SHOW_SUCCESS) {// 普通渠道绑卡成功
				String channelId = result.getChannelId();
				if (channelId!=null &&channelId.equals("1013")) {
					startCpPluis(result);
				}else {
					BindCardAuthModeDto defaultAuth= result.getDefaultAuth();
					if (defaultAuth!=null) {
						if (defaultAuth.getAuthMode().equals(BindCardAuthModeDto.Auth_WeChat)) {
							//跳转到微信
							Intent it=new Intent(mContext.getActivity(),PrefectActivity.class);
							mContext.startActivity(it);
							dissmisProgressDialog();
						}else if (defaultAuth.getAuthMode().equals(BindCardAuthModeDto.Auth_DaKuang)) {
							//返回成功
							mContext.showToastShort("绑定成功");
							ApplicationParams.getInstance().getServiceMger().addTask(new TaskBean(UpdateUserDataService.TaskType_UserCard, BindCardFragment.BindCardRequsetNormal));
							dissmisProgressDialog();
						}else if(defaultAuth.getAuthMode().equals(BindCardAuthModeDto.Auth_B2c)){
							//调用cp
							startCpPluis(result);
						}else {
							// 调用升级接口
							showProgressDialog("请稍后...");
							new UpdateTask().execute();
						}
					}else {
						//未知|跳转到微信
						Intent it=new Intent(mContext.getActivity(),PrefectActivity.class);
						mContext.startActivity(it);
						dissmisProgressDialog();
					}
				}
			} else if (result.getContentCode() == Cons.SHOW_BindOtherChannl) {
				dissmisProgressDialog();
				mContext.showToastShort(result.getContentMsg());
			} else {
				dissmisProgressDialog();
				mContext.showToastShort(result.getContentMsg());
			}
		}
	}

	private String getSupportChalln() {
		BindCardUploadParams channles = new BindCardUploadParams();
		ArrayList<Channels> list = new ArrayList<Channels>();
		list.add(new Channels("1013", "1"));
		list.add(new Channels("02", "1"));
		channles.setChannels(list);
		String supportPayChannel = new Gson().toJson(channles);
		return supportPayChannel;
	}
}
