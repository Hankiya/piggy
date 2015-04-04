package howbuy.android.piggy.ui.base;

import howbuy.android.piggy.R;
import howbuy.android.piggy.UserUtil;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.ui.LoginActivity;
import howbuy.android.piggy.ui.VerfctPnActivity;
import howbuy.android.piggy.ui.fragment.LoginFragment;
import howbuy.android.util.Cons;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

/**
 * tlx
 */
public abstract class AbstractBaseHaveLockActivity extends AbstractBaseActivity {
	private static final Long abs = 1 * 60 * 1000l;
	static final String SYSTEM_REASON = "reason";
	static final String SYSTEM_HOME_KEY = "homekey";// home key
	static final String SYSTEM_RECENT_APPS = "recentapps";// long home key

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mBatInfoReceiver, filter);
		filter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
		registerReceiver(mHomeKeyEventReceiver, filter);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		long homePressTime = ApplicationParams.getInstance().getsF().getLong(Cons.SFHomePressTime, System.currentTimeMillis());
		if (System.currentTimeMillis() - homePressTime > abs) {
			ApplicationParams.getInstance().setNeedPattern(true);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 锁屏
		if (ApplicationParams.getInstance().isNeedPattern()) {
			ApplicationParams.getInstance().getsF().edit().remove(Cons.SFHomePressTime).commit();
			ApplicationParams.getInstance().setNeedPattern(false);
			luncherVerPattern();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ApplicationParams.getInstance().getsF().edit().remove(Cons.SFHomePressTime).commit();
		if (mBatInfoReceiver != null) {
			try {
				unregisterReceiver(mBatInfoReceiver);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		if (mHomeKeyEventReceiver != null) {
			try {
				unregisterReceiver(mHomeKeyEventReceiver);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * 是否是当前activity
	 * 
	 * @return
	 */
	private boolean checkCurrActivity() {
		Activity a = ApplicationParams.getInstance().getActivity();
		if (a == this) {
			return true;
		} else {
			return false;
		}
	}
	
	private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(final Context context, final Intent intent) {
			final String action = intent.getAction();
			Log.i("home", "bat action--" + action);
			if (Intent.ACTION_SCREEN_OFF.equals(action) && checkCurrActivity() && (UserUtil.isLogin())) {
				Log.i("home", "bat currAc--" + getClass().getName());
				ApplicationParams.getInstance().setNeedPattern(true);
				luncherVerPattern();
				ApplicationParams.getInstance().setNeedPattern(false);
			}
		}
	};

	private final BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(final Context context, final Intent intent) {

			final String action = intent.getAction();
			Log.i("home", "HomeKey action--" + action);

			if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
				String reason = intent.getStringExtra(SYSTEM_REASON);
				if (reason != null) {
					if (reason.equals(SYSTEM_HOME_KEY) && checkCurrActivity()) {
						//用时间来记录是否已经超时
						ApplicationParams.getInstance().getsF().edit().putLong(Cons.SFHomePressTime, System.currentTimeMillis()).commit();
					} else if (reason.equals(SYSTEM_RECENT_APPS)) {
						// long home key处理点
					}
				}

			}
		}
	};

	/**
	 * 跳转到锁屏
	 */
	private void luncherVerPattern() {
		Log.i("home", "luncher currAc--" + getClass().getName());
		boolean pattenFlag = ApplicationParams.getInstance().getsF().getBoolean(Cons.SFPatternFlag, false);
		if (pattenFlag && UserUtil.isLogin()) {
			startActVerfctPn();
		}else {
			//如果超时需要设置手势密码 而用户没有设置手势密码  则删除配置跳转到登陆
			lucncherLogin();
		}
	}

	private void startActVerfctPn() {
		Intent startIntent;
		startIntent = new Intent(this, VerfctPnActivity.class);
		startIntent.putExtra(Cons.Intent_type, VerfctPnActivity.Type_VerfctPn);
		startIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startIntent);
		overridePendingTransition(R.anim.page_push_out, R.anim.page_push_in);
	}
	
	private void lucncherLogin(){
		ApplicationParams.getInstance().getsF().edit().remove(Cons.SFUserCusNo).remove(Cons.SFPatternValue).remove(Cons.SFPatternFlag).remove(Cons.SFLoginOutFlag).remove(Cons.SFPatternForgetFlag).remove(Cons.SFBindCardParams).commit();
		ApplicationParams.getInstance().getPiggyParameter().removeUserDataPrivate();
		Intent intent = new Intent(this, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(Cons.Intent_type, LoginFragment.LoginType_Lanucher);
		startActivity(intent);
		finish();
	}


}
