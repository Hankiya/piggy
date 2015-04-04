package howbuy.android.piggy.ui.base;

import howbuy.android.piggy.R;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.parameter.GlobalParams;
import howbuy.android.piggy.service.ServiceBroadCastHelp;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * User: qii Date: 12-12-30
 */
public class AbstractFragment extends SherlockFragment implements ServiceBroadCastHelp.BroadCastRecHelpCallBack {
	/**
	 * when activity is recycled by system, isFirstTimeStartFlag will be reset
	 * to default true, when activity is recreated because a configuration
	 * change for example screen rotate, isFirstTimeStartFlag will stay false
	 */
	private boolean isFirstTimeStartFlag = true;

	public static final int FIRST_TIME_START = 0; // when activity is first time
													// start
	public static final int SCREEN_ROTATE = 1; // when activity is destroyed and
												// recreated because a
												// configuration
												// change, see
												// setRetainInstance(boolean
												// retain)
	public static final int ACTIVITY_DESTROY_AND_CREATE = 2; // when activity is
																// destroyed
																// because
																// memory is too
																// low, recycled
																// by
																// android
																// system
	public boolean isCleanCrouton = true;
	private ServiceBroadCastHelp mCastHelp;

	public boolean isCleanCrouton() {
		return isCleanCrouton;
	}

	public void setCleanCrouton(boolean isCleanCrouton) {
		this.isCleanCrouton = isCleanCrouton;
	}

	public AbstractFragment() {
	}

	protected int getCurrentState(Bundle savedInstanceState) {

		if (savedInstanceState != null) {
			isFirstTimeStartFlag = false;
			return ACTIVITY_DESTROY_AND_CREATE;
		}

		if (!isFirstTimeStartFlag) {
			return SCREEN_ROTATE;
		}

		isFirstTimeStartFlag = false;
		return FIRST_TIME_START;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isShoudRegisterReciver()) {
			if (mCastHelp == null) {
				mCastHelp = new ServiceBroadCastHelp();
			}
			mCastHelp.registerReceiver(getActivity(), this);
		}
	}

	/**
	 * 是否注册datareceiver
	 * 
	 * @return
	 */
	public boolean isShoudRegisterReciver() {
		return false;
	}

	/**
	 * Toast信息短
	 * 
	 * @param e
	 *            信息内容
	 */
	public void showToastShort(String e) {
		// Toast.makeText(getActivity(), e, Toast.LENGTH_SHORT).show();
		if (!TextUtils.isEmpty(e) && getActivity() != null) {
			// if (GlobalParams.getGlobalParams().isDebugFlag()) {
			// showCroutonLong(e);
			// }else {
			Crouton.showText(getActivity(), e, Style.ALERT);
			// }
		}
	}

	private void showCroutonLong(String msg) {
		final Style INFINITEFinal = new Style.Builder().setBackgroundColorValue(Style.holoRedLight).build();
		final Configuration CONFIGURATION_INFINITE = new Configuration.Builder().setDuration(Configuration.DURATION_INFINITE).build();
		Crouton.makeText(getActivity(), msg, INFINITEFinal).setConfiguration(CONFIGURATION_INFINITE).show();
	}

	public void showToastTrue(String e) {
		if (!TextUtils.isEmpty(e) && getActivity() != null) {
			Toast.makeText(getActivity(), e, Toast.LENGTH_SHORT).show();
		}
	}

	public void showToastTrueLong(String e) {
		if (!TextUtils.isEmpty(e) && getActivity() != null) {
			Toast.makeText(getActivity(), e, Toast.LENGTH_LONG).show();
		}
	}

	public void d(String e) {
		if (GlobalParams.getGlobalParams().isDebugFlag()) {
			Log.i("message", e);
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		clearCrouton();
		if (isShoudRegisterReciver() && mCastHelp != null && isAdded()) {
			mCastHelp.unRegisterReceiver(getActivity());
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		clearCrouton();
	}

	public void clearCrouton() {
		if (isCleanCrouton) {
			Crouton.clearCroutonsForActivity(getActivity());
		}
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
	public void onServiceRqCallBack(Intent taskData, boolean isCurrPage) {
		// TODO Auto-generated method stub

	}

	public boolean onBackPressed() {
		return false;
	}

	/**
	 * addfragment to stack
	 * 
	 * @param fgName
	 * @param b
	 */
	public void doAddFragment(String fgName, Bundle b) {
		SherlockFragmentActivity sa = getSherlockActivity();
		if (sa == null)
			return;
		FragmentTransaction f = sa.getSupportFragmentManager().beginTransaction();
		f.setCustomAnimations(R.anim.intro_left_in, R.anim.intro_left_out, R.anim.intro_right_in, R.anim.intro_right_out);
		Fragment fragment = Fragment.instantiate(sa, fgName, b);
		f.addToBackStack(null);
		f.replace(R.id.content_frame, fragment);
		f.commitAllowingStateLoss();
	}

	/**
	 * 显示进度条
	 */
	public void showIndeterProgress() {
		getSherlockActivity().setSupportProgress(Window.PROGRESS_END);
		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
	}
	
	/**
	 * 隐藏进度条
	 */
	public void dissmissIndeterProgress() {
		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
	}
}
