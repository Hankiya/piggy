package howbuy.android.piggy.service;

import howbuy.android.lib.MyAsyncTask;
import howbuy.android.piggy.api.DispatchAccessData;
import howbuy.android.piggy.api.dto.BindCardDto;
import howbuy.android.piggy.api.dto.BindCardUploadParams;
import howbuy.android.piggy.api.dto.BindCardUploadParams.Channels;
import howbuy.android.piggy.api.dto.UpdateDto;
import howbuy.android.piggy.api.dto.UserInfoDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.dialog.PiggyProgressDialog;
import howbuy.android.piggy.parameter.PiggyParams;
import howbuy.android.util.Cons;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.google.myjson.Gson;
import com.hxcr.chinapay.activity.Initialize;
import com.hxcr.chinapay.util.CPGlobaInfo;
import com.hxcr.chinapay.util.Utils;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * 
 * @author zhaoxing.zhang 直接去cp验证银行卡,直接跳转 验卡界面
 */

public class BindCardAndVerify extends MyAsyncTask<Void, Void, UserInfoDto> {

	private PiggyProgressDialog mpDialog;
	private Context context;
	private BindTask mBindTask;
	private int intoType=0;

	public BindCardAndVerify(Context context, int intoType) {
		this.context = context;
		this.intoType=intoType;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		mpDialog = new PiggyProgressDialog(context);
		// mpDialog.setMessage(channelPay != null ? "绑定中..." : "绑定中...");
		mpDialog.setMessage("绑定中...");
		mpDialog.show();
	}

	@Override
	protected UserInfoDto doInBackground(Void... params) {
		String custNo;
		// TODO Auto-generated method stub
		custNo = getSf().getString(Cons.SFUserCusNo, null);
		// 用户信息
		 UserInfoDto u = DispatchAccessData.getInstance().getUserInfo(custNo);
		 if (u.getContentCode() == Cons.SHOW_SUCCESS) {
			ApplicationParams.getInstance().getPiggyParameter().saveObject(PiggyParams.SfUserInfo, u);
			ApplicationParams.getInstance().getPiggyParameter().saveObject(PiggyParams.SfUserInfoTime, String.valueOf(System.currentTimeMillis()));
			ApplicationParams.getInstance().getPiggyParameter().toCacheUser();
		}
		return u;
	}

	/**
	 * 获取sf内容
	 * 
	 * @return
	 */
	public SharedPreferences getSf() {
		return ApplicationParams.getInstance().getsF();
	}

	@Override
	protected void onPostExecute(UserInfoDto result) {
		super.onPostExecute(result);
		if (result.contentCode==Cons.SHOW_SUCCESS) {
			String custNo = getSf().getString(Cons.SFUserCusNo, null);
//			String bankNo = result.getBankAcct();
//			String bankCode = result.getBankCode();
//			String bankBranchCode = result.getSubBankCode();
//			String bankBranchName = result.getSubBankName();
//			String provinceCode = result.getProvinceNo();
//			String bankBranchCityCode = "";
			//to doo
			if (mBindTask!=null) {
				mBindTask.cancel(true);
			}
			mBindTask= new BindTask();
//			mBindTask.execute(custNo, bankNo, bankCode, bankBranchCode, provinceCode, bankBranchCityCode);
		}
	}

	/**
	 * 绑定银行task
	 * 
	 * @ClassName: BindCardFragment.java
	 * @Description:
	 * @author yescpu yes.cpu@gmail.com
	 * @date 2013-10-17下午4:55:59
	 */
	class BindTask extends MyAsyncTask<String, Void, BindCardDto> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected BindCardDto doInBackground(String... params) {
			return DispatchAccessData.getInstance().bindCard(params[0], params[1], params[2], params[3], params[4], params[5],getSupportChalln());// custNo,
		}

		@Override
		protected void onPostExecute(BindCardDto result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (mpDialog != null && mpDialog.isShowing()) {
				mpDialog.dismiss();
			}
			if (result.getContentCode() == Cons.SHOW_SUCCESS) {
				if (result.hasOrderInf()) {
					Utils.setPackageName(context.getPackageName());
					Intent t = new Intent(context, Initialize.class);
					t.putExtra(CPGlobaInfo.XML_TAG, result.buildOrderInf(Cons.CP_Debug_Flag));// PRODUCT
					context.startActivity(t);
				}

			} else if (result.getContentCode() == Cons.SHOW_BindOtherChannl) {
				String channelId = result.getChannelId();
				if (channelId != null && channelId.equals("1013")) {//1013才抵用cp
					Utils.setPackageName(context.getPackageName());
					Intent t = new Intent(context, Initialize.class);
					String info = result.buildOrderInf(Cons.CP_Debug_Flag);
					Log.d("cp", info);
					t.putExtra(CPGlobaInfo.XML_TAG, info);
					context.startActivity(t);
				} else {
					mpDialog = new PiggyProgressDialog(context);
					mpDialog.show();
					new UpdateTask().execute();
					showToastShort(result.getContentMsg());
				}
			} else {
				showToastShort(result.getContentMsg());
			}
			
		}
		private String getSupportChalln(){
			BindCardUploadParams channles = new BindCardUploadParams();
			  ArrayList<Channels> list = new ArrayList<Channels>();
			  list.add(new Channels("1013", "1"));
			  channles.setChannels(list);
			  String supportPayChannel = new Gson().toJson(channles);
			  return supportPayChannel;
		}		

		/**
		 * Toast信息短
		 * 
		 * @param e
		 *            信息内容
		 */
		public void showToastShort(String e) {
			// Toast.makeText(getActivity(), e, Toast.LENGTH_SHORT).show();
			if (!TextUtils.isEmpty(e) && context != null) {
				Crouton.showText((Activity) context, e, Style.ALERT);
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

			if (result.contentCode == Cons.SHOW_SUCCESS) {
				UpdateDto arg1 = result;
				// arg1.setVersionNeedUpdate("1");
				String needUpdate = arg1.getVersionNeedUpdate();
				if (!needUpdate.equals("2")) {
					String url = arg1.getUpdateUrl();
					showUpdateDialog(url);
				}
			}
			Activity a = (Activity) context;
			a.finish();
		}

		private void showUpdateDialog(final String url) {
			new AlertDialog.Builder(context).setMessage("当前版本不支持新增的开户渠道，更新到新版本后即可开户。").setNegativeButton("取消", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			}).setPositiveButton("升级", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					String murl = url;
					Intent i = new Intent("android.intent.action.VIEW");
					i.setData(Uri.parse(murl));
					context.startActivity(i);
				}
			});
		}

	}

}
