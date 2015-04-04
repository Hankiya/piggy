package howbuy.android.piggy.ui.base;

import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.parameter.GlobalParams;
import howbuy.android.piggy.service.ServiceBroadCastHelp;
import howbuy.android.piggy.service.ServiceBroadCastHelp.BroadCastRecHelpCallBack;
import howbuy.android.piggy.ui.VerfctPnActivity;
import howbuy.android.util.Cons;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * tlx
 */
public abstract class AbstractBaseActivity extends SherlockFragmentActivity implements BroadCastRecHelpCallBack{
	protected SharedPreferences mSf;
	private ServiceBroadCastHelp mCastHelp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (GlobalParams.getGlobalParams().isDebugFlag() == false) {
			MobclickAgent.setCatchUncaughtExceptions(false);
		}
		if (isShoudRegisterReciver()) {
			if (mCastHelp==null) {
				mCastHelp=new ServiceBroadCastHelp();
			}
			mCastHelp.registerReceiver(this, this);
		}
		mSf = getSharedPreferences(Cons.SFbaseUser, MODE_PRIVATE);
		initUi(savedInstanceState);
	}
	
	public boolean isShoudRegisterReciver(){
		return false;
	}

	@Override
	public void onServiceRqCallBack(Intent taskData, boolean isCurrPage) {
		// TODO Auto-generated method stub
	}

	public abstract void initUi(Bundle savedInstanceState);

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setIntent(intent);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		
		if (!(this instanceof VerfctPnActivity)) {
			ApplicationParams.getInstance().setActivity(this);
		}
			ApplicationParams.getInstance().getmActivities().add(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (isShoudRegisterReciver()&&mCastHelp!=null) {
			mCastHelp.unRegisterReceiver(this);
		}
	}

	/**
	 * Toast信息短
	 * 
	 * @param e
	 *            信息内容
	 */
	public void showToastShort(String e) {
		if (!TextUtils.isEmpty(e)) {
			Toast.makeText(this, e, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Toast信息长
	 * 
	 * @param e
	 *            信息内容
	 */
	public void showToastLong(String e) {
		if (!TextUtils.isEmpty(e)) {
		Toast.makeText(this, e, Toast.LENGTH_LONG).show();}
	}
	
	public void d(String e) {
		if (GlobalParams.getGlobalParams().isDebugFlag()) {
			Log.i("message", e);
		}
	}

}
