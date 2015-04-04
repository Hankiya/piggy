package com.howbuy.lib.error;

import java.lang.reflect.Field;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Looper;

import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.lib.utils.SysUtils;

/**
 * @author rexy 840094530@qq.com
 * @date 2014-1-15 下午5:52:29
 */
public class CrashHandler {
	protected Context mUnSafeContext = null;
	protected String mProjName = null;

	public CrashHandler(Context cx, String projName) {
		mUnSafeContext = cx;
		mProjName = projName;
	}

	protected final void handCrash(Thread thrd, final WrapException err,
			final Activity last) {
		handOnCrashThrd(thrd, err);
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				if(LogUtils.mDebugCrashDialog&&last != null){
				   last.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
				   handOnUiThrd(err, last);	
				}else{
					if(LogUtils.mDebugCrashLaunch){
						launchSelf(last,err);
					}else{
						System.exit(1);
					}
				}
				Looper.loop();
			}
		}.start();
	}

	protected void handOnCrashThrd(Thread thrd, WrapException err) {
		StringBuilder sb = new StringBuilder();
		String l = "\r\n";
		sb.append("Crash log ,Date:")
				.append(Calendar.getInstance().getTime().toLocaleString())
				.append(l);
		sb.append("-------------------App Inf-------------------").append(l)
				.append(getAppInf()).append(l);
		sb.append("-------------------Device Inf-------------------").append(l)
				.append(getMobileInfo()).append(l);
		sb.append("-------------------Error Inf-------------------").append(l)
				.append(getErrorInfo(err)).append(l);
		String fileName="crash_"+GlobalApp.PROJ_NAME_DISPLAY;
		if(LogUtils.mDebugCrashMutiLogFile){
			fileName+="_"+StrUtils.timeFormat(System.currentTimeMillis(), "yyyyMMdd-HHmmss");
		}
		LogUtils.startLog(fileName+".txt");
		LogUtils.appendLog(sb.toString());
		LogUtils.endLog();
		LogUtils.d("handOnCrashThrd all inf :\r\n" + sb.toString());
		
	}

	protected void handOnUiThrd(final WrapException err, final Activity last) {
		if(last==null){
			return ;
		}
		String msg =  err.getError(null,true)+ "," + "程序崩溃,是否退出?";
		final AlertDialog.Builder build = new AlertDialog.Builder(last)
				.setTitle("提示").setCancelable(false).setMessage(msg);
		build.setNegativeButton("退出", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				System.exit(0);
			}
		}).setNeutralButton("详情", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				showExceptDlg(err, last);
			}
		});
		if(LogUtils.mDebugCrashLaunch){
			build.setPositiveButton("重启", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					launchSelf(last,err);
					System.exit(1);
				}
			});
		}
		try{
			build.show();
		}catch(Exception e){
			System.exit(0);
		}
		
	}

	protected void showExceptDlg(final WrapException err, final Activity last) {
		
		String msg = getErrorInfo(err);
		AlertDialog.Builder build = new AlertDialog.Builder(last)
				.setTitle("提示").setCancelable(false).setMessage(msg);
		build.setNeutralButton("退出", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(0);
			}
		}).setPositiveButton("重启", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				launchSelf(last,err);
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(1);
			}
		}).show();
	}

	protected void launchSelf(Activity last,WrapException err) {
		  Intent i = mUnSafeContext.getPackageManager()
				.getLaunchIntentForPackage(mUnSafeContext.getPackageName());
		  i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		  i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		  i.putExtra("crash_err", err);
		  if(last!=null){
			  last.overridePendingTransition(0, 0);
		  }
	      mUnSafeContext.startActivity(i);
	}

	protected String getAppInf() {
		String l = "\r\n";
		StringBuffer sb = new StringBuffer();
		sb.append("Project Name:").append(mProjName).append(l);
		sb.append("Version Name:")
				.append(SysUtils.getVersionName(mUnSafeContext)).append(l);
		sb.append("Version Code:").append(SysUtils.getVersion(mUnSafeContext))
				.append(l);
		return sb.toString();
	}

	protected String getErrorInfo(WrapException err) {
		return WrapException.getErrorDetail(err, true);
	}

	protected String getMobileInfo() {
		String l = "\r\n";
		StringBuffer sb = new StringBuffer();
		sb.append("IEME:").append(SysUtils.getImei(mUnSafeContext)).append(l);
		sb.append("MOBILE_MODEL:").append(SysUtils.getModel()).append(l);
		sb.append("MOBILE_OS:ANDROID_").append(SysUtils.getOsVersion()) .append(l);
		sb.append("SDK API:").append(SysUtils.getApiVersion()).append(l);
		try {
			Field[] fields = Build.class.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				String name = field.getName();
				String value = field.get(null).toString();
				sb.append(name + "=" + value);
				sb.append(l);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
