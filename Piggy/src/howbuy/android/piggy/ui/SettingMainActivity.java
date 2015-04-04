package howbuy.android.piggy.ui;

import howbuy.android.piggy.R;
import howbuy.android.piggy.ui.base.AbstractBaseHaveLockActivity;
import howbuy.android.piggy.ui.fragment.SettingMainFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class SettingMainActivity extends AbstractBaseHaveLockActivity {
	private GestureDetector mGestureDetector;

	@Override
	public void initUi(Bundle savedInstanceState) {
		setContentView(R.layout.content_frame);
		// TODO Auto-generated method stub
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.content_frame, Fragment.instantiate(this, SettingMainFragment.class.getName())).commit();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mGestureDetector = new GestureDetector(this, new MGestureListener());
	}

	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		boolean handled = super.dispatchTouchEvent(ev);
		handled = mGestureDetector.onTouchEvent(ev);
		return handled;
	}
	
	class MGestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			// TODO Auto-generated method stub
			int dx = (int) (e2.getX() - e1.getX()); // 计算滑动的距离
			String NAME = "test";
			Log.d(NAME, "velocityx=" + velocityX);
			if (Math.abs(dx) > 200 && Math.abs(velocityX) > Math.abs(velocityY)) { // 降噪处理，必须有较大的动作才识别
				Log.d(NAME, "velocityxx=" + velocityX);
				if (velocityX > 0) {
					// 向右边
					finish();
				} else {
					// 向左边
				}
				return true;
			} else {
				return false; // 当然可以处理velocityY处理向上和向下的动作
			}

		}
	}
	
	
}
