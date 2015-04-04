package com.howbuy.lib.frag;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.howbuy.lib.utils.LogUtils;
//d\(\s*"[\s\S]*?\);
/**
 * @author rexy 840094530@qq.com
 * @date 2014-1-22 下午5:02:55
 */
public abstract class AbsFrag extends SherlockFragment {
	/**
	 * when activity is recycled by system, isFirstTimeStartFlag will be reset
	 * to default true, when activity is recreated because a configuration
	 * change for example screen rotate, isFirstTimeStartFlag will stay false
	 */
	public static final int CREATE_FIRST_TIME = 0; // when activity is first
													// time
	// start
	public static final int CREATE_FOR_SCREENROATE = 1; // when activity is
														// destroyed and
	// recreated because a configuration
	// change, see
	// setRetainInstance(boolean retain)
	public static final int CREATE_AFTER_DESTROY = 2; // when activity is
	// destroyed because
	// memory is too
	// low, recycled by
	// android system

	public String TAG = "AbsFrag";
	private boolean isFirstTimeStart = true;
	protected int mFragCreateType = 0;
	private final SparseIntArray mRequestCodes = new SparseIntArray();
	protected View mRootView = null;
	protected boolean mFragCached = false;
	protected String mTitleLable = null;

	public AbsFrag() {
	}

	// Checks to see whether there is any children fragments which has been
	// registered with {@code requestCode} before. If so, let it handle the
	private boolean checkNestedFragmentsForResult(int requestCode, int resultCode, Intent data) {
		final int id = mRequestCodes.get(requestCode);
		if (id == 0)
			return false;

		mRequestCodes.delete(requestCode);

		List<Fragment> fragments = getChildFragmentManager().getFragments();
		if (fragments == null)
			return false;

		for (Fragment fragment : fragments) {
			if (fragment.hashCode() == id) {
				fragment.onActivityResult(requestCode, resultCode, data);
				return true;
			}
		}

		return false;
	}

	private int getCurrentState(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			isFirstTimeStart = false;
			return CREATE_AFTER_DESTROY;
		}
		if (!isFirstTimeStart) {
			return CREATE_FOR_SCREENROATE;
		}
		isFirstTimeStart = false;
		return CREATE_FIRST_TIME;
	}

	public boolean onNetChanged(int netType, int preNet) {
		return false;
	}

	public boolean onXmlBtClick(View v) {
		return false;
	};

	public boolean onKeyBack(boolean fromBar, boolean isFirstPress, boolean isTwiceInTime) {
		return false;
	}

	abstract protected int getFragLayoutId();

	abstract protected void initViewAdAction(View root, Bundle bundle);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (null != container && -1 != getFragLayoutId()) {
			mRootView = inflater.inflate(getFragLayoutId(), container, false);
		}
		return mRootView;
	}

	@Override
	public final void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			onAttachChanged(activity, true);
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement FragmentWeatherListener");
		} finally {
		}
	}

	@Override
	public final void onDetach() {
		super.onDetach();
		onAttachChanged(getSherlockActivity(), false);
	}

	@Override
	public void onActivityCreated(Bundle bundle) {
		super.onActivityCreated(bundle);
		mFragCreateType = getCurrentState(bundle);
		initViewAdAction(mRootView, bundle);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Bundle b = getArguments();
		if (b != null) {
			outState.putAll(b);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (!checkNestedFragmentsForResult(requestCode, resultCode, data))
			super.onActivityResult(requestCode, resultCode, data);
	}

	protected void onAttachChanged(Activity aty, boolean isAttach) {
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		TAG = getClass().getSimpleName();
	}

	final protected void d(String title, String msg) {
		if (title == null) {
			LogUtils.d(TAG, msg);
		} else {
			LogUtils.d(TAG, title + " -->" + msg);
		}
	}

	final protected void dd(String msg, Object... args) {
		d(TAG, String.format(msg, args));
	}

	final protected void pop(String msg, boolean debug) {
		if (debug) {
			LogUtils.popDebug(msg);
		} else {
			LogUtils.pop(msg);
		}
	}

	public void registerRequestCode(int requestCode, int id) {
		mRequestCodes.put(requestCode, id);
	}// registerRequestCode()

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		if (getParentFragment() instanceof AbsFrag) {
			((AbsFrag) getParentFragment()).registerRequestCode(requestCode, hashCode());
			getParentFragment().startActivityForResult(intent, requestCode);
		} else
			super.startActivityForResult(intent, requestCode);
	}

}
