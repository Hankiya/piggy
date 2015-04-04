package howbuy.android.piggy.receiver;

import howbuy.android.piggy.R;
import howbuy.android.piggy.ad.PushDispatch;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.ui.AtyLanucher;

import java.util.List;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

public class JpushReceiver extends BroadcastReceiver {
	public static final String Type = "JpushType";
	public static final String TAG = "JpushReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		Bundle bundle = intent.getExtras();
		Log.d(TAG, "onReceive11 - " + intent.getAction() + ", extras: " + printBundle(bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "接收Registration Id : " + regId);
			// send the Registration Id to your server...
		} else if (JPushInterface.ACTION_UNREGISTER.equals(intent.getAction())) {
			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "接收UnRegistration Id : " + regId);
			// send the UnRegistration Id to your server...
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
			Log.d(TAG, "接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String code = bundle.getString(JPushInterface.EXTRA_TITLE);
			// String code = bundle.getString(JPushInterface.EXTRA_EXTRA);
			createNotifiation(context, message, code);
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			Log.d(TAG, "接收到推送下来的通知");
			int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "接收到推送下来的通知的ID: " + notifactionId);
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			Log.d(TAG, "用户点击打开了通知");
			// Intent i = new Intent(context, TrustJpushActivity.class);
			// i.putExtras(bundle);
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// context.startActivity(i);
		} else {
			Log.d(TAG, "Unhandled intent - " + intent.getAction());
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	public void createNotifiation(Context context, String message, String code) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ApplicationParams.getInstance())
				.setSmallIcon(R.drawable.ic_launcher).setContentTitle("储蓄罐")
				.setContentText(message);
		// Creates an explicit intent for an Activity in your app
		
		Intent resultIntent;
//		if (isForeground(context,"howbuy.android.piggy")) {
//			resultIntent = new Intent(context, ProPertyActivity.class);
//		}else {
			resultIntent = new Intent(context, AtyLanucher.class);
//		}
		resultIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		resultIntent.putExtra(PushDispatch.INTENT_ID, code);
		resultIntent.putExtra(PushDispatch.INTENT_MSG, message);
		resultIntent.putExtra(PushDispatch.INTENT_TYPE, Type);
		// The stack builder object will contain an artificial back stack for
		// the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack for the Intent (but not the Intent itself)
		
//		if (isForeground(context,"howbuy.android.piggy")) {
//			LanucherActivity.pushBundle = resultIntent.getExtras();
//			ProPertyActivity.pushBundle2 = resultIntent.getExtras();
//			stackBuilder.addParentStack(ProPertyActivity.class);
//		}else {
			stackBuilder.addParentStack(AtyLanucher.class);
//		}
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		Notification n = mBuilder.build();
		n.flags = Notification.FLAG_AUTO_CANCEL;
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(101, n);
	}
	
	public boolean isForeground(Context context,String PackageName){
		  // Get the Activity Manager
//		  ActivityManager manager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
		  ActivityManager manager = (ActivityManager)context.getSystemService(context.ACTIVITY_SERVICE);
		 
		  // Get a list of running tasks, we are only interested in the last one,
		  // the top most so we give a 1 as parameter so we only get the topmost.
		  List< ActivityManager.RunningTaskInfo > task = manager.getRunningTasks(1);
		 
		  // Get the info we need for comparison.
		  ComponentName componentInfo = task.get(0).topActivity;
		 
		  // Check if it matches our package name.
		  if(componentInfo.getPackageName().equals(PackageName)) return true;
		     
		  // If not then our app is not on the foreground.
		  return false;
		}

}
