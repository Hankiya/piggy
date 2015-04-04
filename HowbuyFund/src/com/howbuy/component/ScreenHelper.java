package com.howbuy.component;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.howbuy.lib.utils.LogUtils;

public class ScreenHelper implements SensorEventListener {
	private static int TRIG_SCREEN_CHANGED = 6;
	private static int TRIG_LOCKCHANGED = 4;
	private static int TRIG_LOCKZ=5;
	private SensorManager mSensorMgr = null;
	private int mSensorPre = IScreenChanged.SENSOR_SCREEN_UNKNOW,
			mSensorCur = IScreenChanged.SENSOR_SCREEN_UNKNOW;

	final protected void d(String title, String msg) {
		if (title == null) {
			LogUtils.d("ScreenHelper", msg);
		} else {
			LogUtils.d("ScreenHelper", title + " -->" + msg);
		}
	}

	public int getCurScreen() {
		return mSensorCur;
	}

	public int getPreScreen() {
		return mSensorPre;
	}

	public void registerSensor(Activity aty, IScreenChanged l) {
		if (mSensorMgr == null) {
			mSensorMgr = (SensorManager) aty.getSystemService(Context.SENSOR_SERVICE);
			Sensor sensor = mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			mSensorMgr.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
			mSensorPre = mSensorCur = aty.getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ? IScreenChanged.SENSOR_SCREEN_PORT
					: IScreenChanged.SENSOR_SCREEN_LAND;
		}

		if (l != null && !mListener.contains(l)) {
			mListener.add(l);
		}

	}

	public void unregisterSensor(IScreenChanged l) {
		if (mSensorMgr != null) {
			mSensorMgr.unregisterListener(this);
			mSensorMgr = null;
		}
		if (l != null) {
			mListener.remove(l);
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// 重力传感器
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			float x = event.values[SensorManager.DATA_X];
			float y = event.values[SensorManager.DATA_Y];
			float z = event.values[SensorManager.DATA_Z];
			if (Math.abs(z) < TRIG_LOCKZ) {
				if (Math.abs(x) > TRIG_SCREEN_CHANGED && Math.abs(y) < TRIG_LOCKCHANGED) {
					onSensorChanged(IScreenChanged.SENSOR_SCREEN_LAND);
				} else if (Math.abs(y) > TRIG_SCREEN_CHANGED && Math.abs(x) < TRIG_LOCKCHANGED) {
					onSensorChanged(IScreenChanged.SENSOR_SCREEN_PORT);
				} else {
					onSensorChanged(IScreenChanged.SENSOR_SCREEN_UNKNOW);
				}
			}

		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	private void onSensorChanged(int sensor_screen) {
		if (mSensorCur != sensor_screen) {
			mSensorPre = mSensorCur;
			mSensorCur = sensor_screen;
			notifyScreenChanged(mSensorPre, mSensorCur);
		}
	}

	private void notifyScreenChanged(int pre, int cur) {
		for (int i = 0; i < mListener.size(); i++) {
			mListener.get(i).onScreenChanged(pre, cur);
		}
	}

	private ArrayList<IScreenChanged> mListener = new ArrayList<IScreenChanged>();

	public interface IScreenChanged {
		public static final int SENSOR_SCREEN_PORT = 1;
		public static final int SENSOR_SCREEN_LAND = 2;
		public static final int SENSOR_SCREEN_UNKNOW = 3;

		void onScreenChanged(int preScreen, int curScreen);
	}
}
