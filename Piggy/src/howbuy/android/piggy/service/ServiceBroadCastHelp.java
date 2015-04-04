package howbuy.android.piggy.service;

import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.util.Cons;
import howbuy.android.util.CpUtil;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class ServiceBroadCastHelp {
	private BroadCastRecHelpCallBack mCallBack;
	private Context mContext;

	private BroadcastReceiver br = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (mCallBack != null) {
				boolean bgFlag=isAppOnForeground(mContext);
				String taskType=intent.getStringExtra(Cons.Intent_type);
				String taskId=intent.getStringExtra(Cons.Intent_id);
				if (UpdateUserDataService.TaskType_UserInfo.equals(taskType)&&taskId!=null&&(taskId.equals(CpUtil.Request_Type_Cp_TimeOut)||taskId.equals(CpUtil.Request_Type_Cp_Success))) {
					if (bgFlag) { 
						mCallBack.onServiceRqCallBack(intent, bgFlag);
					}
				}else {
					mCallBack.onServiceRqCallBack(intent, bgFlag);
				}
			}
		}
	};

	public boolean isAppOnForeground(Context _context) {
		// _context是一个保存的上下文
		ComponentName cn = ApplicationParams.getInstance().getActivity().getComponentName();  
		Activity a=null;
		try {
			a=(Activity) _context;
			String iClass=a.getComponentName().getClassName();
			String topClass=cn.getClassName();
			Log.d("service","当前页面--"+a.getComponentName().getClassName()+"_____topCalss--"+topClass);
			if (iClass.equals(topClass)) {
				return true;
			}
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
		}
		return true;//非Activity，默认返回true
		
	}

	public void registerReceiver(Context context, BroadCastRecHelpCallBack callBack) {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(UpdateUserDataService.TaskType_BankProvince);
		intentFilter.addAction(UpdateUserDataService.TaskType_AllNormal);
		intentFilter.addAction(UpdateUserDataService.TaskType_Product);
		intentFilter.addAction(UpdateUserDataService.TaskType_TDay);
		intentFilter.addAction(UpdateUserDataService.TaskType_UserInfo);
		intentFilter.addAction(UpdateUserDataService.TaskType_Notice);
		intentFilter.addAction(UpdateUserDataService.TaskType_UserCard);
		LocalBroadcastManager.getInstance(context).registerReceiver(br, intentFilter);
		this.mContext=context;
		mCallBack = callBack;
	}

	public void unRegisterReceiver(Context context) {
		if (br != null) {
			LocalBroadcastManager.getInstance(context).unregisterReceiver(br);
		}
	}

	public interface BroadCastRecHelpCallBack {
		public void onServiceRqCallBack(Intent taskData, boolean isCurrPage);
	}
}
