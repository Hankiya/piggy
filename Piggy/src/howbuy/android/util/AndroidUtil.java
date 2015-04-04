package howbuy.android.util;

import howbuy.android.piggy.application.ApplicationParams;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.StrictMode;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

public class AndroidUtil {
	public static void stopListViewScrollingAndScrollToTop(ListView listView) {
		if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			listView.setSelection(0);
			listView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0, 0, 0));

		} else {
			listView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
			listView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
			listView.setSelection(0);
		}
	}

	
	/**
	 * 复制
	 * @param source
	 */
	public static final void copyText(String source){
		if(VERSION.SDK_INT < VERSION_CODES.HONEYCOMB) {
		    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) ApplicationParams.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
		    clipboard.setText(source);
		} else {
		    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) ApplicationParams.getInstance().getSystemService(Context.CLIPBOARD_SERVICE); 
		    android.content.ClipData clip = android.content.ClipData.newPlainText(source,source);
		    clipboard.setPrimaryClip(clip);
		}
	}
	
	public static String getPackageName() {
		return ApplicationParams.getInstance().getPackageName();
	}
	
	/**
	 * 隐藏键盘
	 * 
	 * @param context
	 */
	public static void hideIme(Activity context) {
		if (context == null)
			return;
		final View v = context.getCurrentFocus();
		System.out.println(v);
		if (v != null && v.getWindowToken() != null) {
			InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}

	/**
	 * 键盘是否打开
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isImeShow(Context context) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
		return imm.isActive();
	}

	/**
	 * 获取屏幕宽度
	 * 
	 * @return
	 */
	public static int getWidth() {
		return ApplicationParams.getInstance().getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * 获取屏幕高度
	 * 
	 * @return
	 */
	public static int getHeight() {
		return ApplicationParams.getInstance().getResources().getDisplayMetrics().heightPixels;
	}

	/**
	 * dip转px
	 * 
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(float dipValue) {
		final float scale = ApplicationParams.getInstance().getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * px转dip
	 * 
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(float pxValue) {
		final float scale = ApplicationParams.getInstance().getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 隐藏
	 * 
	 * @param view
	 * @param operate
	 */
	public static void setVisibilityNoRepate(View view, int operate) {
		if (view.getVisibility() != operate) {
			view.setVisibility(operate);
		}

	}

	/**
	 * 获取mac
	 * 
	 * @param context
	 * @return
	 */
	public static String getWifiMac(Context context) {
		String macAddress = null, ip = null;
		WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
		if (null != info) {
			macAddress = info.getMacAddress();
			return macAddress;
		}
		return null;
	}
	
	
	public static String getImei(Context cx) {
		String Imei = "";
		try {
			Imei = ((TelephonyManager) cx.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		} catch (Exception e) {
		}
		return Imei;
	}
	
	public static String getPhoneNumber(Context cx) {
		String phoneNumber = "";
		try {
			phoneNumber = ((TelephonyManager) cx
					.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
		} catch (Exception e) {
		}
		return phoneNumber;
	}

	@TargetApi(11)
	public static void enableStrictMode() {
		if (hasGingerbread()) {
			StrictMode.ThreadPolicy.Builder threadPolicyBuilder = new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog();
			StrictMode.VmPolicy.Builder vmPolicyBuilder = new StrictMode.VmPolicy.Builder().detectAll().penaltyLog();

			if (hasHoneycomb()) {
				threadPolicyBuilder.penaltyFlashScreen();
				// vmPolicyBuilder.setClassInstanceLimit(MainActivity.class,
				// 1).setClassInstanceLimit(SelfMainNewActivity.class, 1);
			}
			StrictMode.setThreadPolicy(threadPolicyBuilder.build());
			StrictMode.setVmPolicy(vmPolicyBuilder.build());
		}
	}

	public static String getVersionName() {
		try {
			return ApplicationParams.getInstance().getPackageManager().getPackageInfo(Cons.PACKAGENAME, -1).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "1.0.0";
	}
	
	public static int getVersionCode() {
		try {
			return ApplicationParams.getInstance().getPackageManager().getPackageInfo(Cons.PACKAGENAME, -1).versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public static String getSourceFolderName(){
		float density= ApplicationParams.getInstance().getResources().getDisplayMetrics().density;
		String folderName;
		if (density==1.0f) {
			folderName="mdpi";
		}else if (density==1.5f) {
			folderName="hdpi";
		}else if (density==2.0f) {
			folderName="xhdpi";
		}else if (density==3.0f) {
			folderName="xxhdpi";
		}else if (density==4.0f) {
			folderName="xxxhdpi";
		}else {
			folderName="xhdpi";
		}
		return folderName;
	}

	public static boolean hasFroyo() {
		// Can use static final constants like FROYO, declared in later versions
		// of the OS since they are inlined at compile time. This is guaranteed
		// behavior.
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}

	public static void setFixedDrableBg(View v, int drawAbRes) {
		int bottom = v.getPaddingBottom();
		int top = v.getPaddingTop();
		int right = v.getPaddingRight();
		int left = v.getPaddingLeft();
		v.setBackgroundResource(drawAbRes);
		v.setPadding(left, top, right, bottom);
	}
	
	public static Bitmap loadBitmapFromView(View v, boolean isParemt) {  
        if (v == null||v.getWidth()==0) {  
            return null;  
        }  
        Bitmap screenshot;  
        screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Config.ARGB_8888);  
        Canvas c = new Canvas(screenshot);  
        c.translate(-v.getScrollX(), -v.getScrollY());  
        v.draw(c);  
        return screenshot;  
    }  
}
