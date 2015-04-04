package com.howbuy.utils;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import cn.jpush.android.api.JPushInterface;

import com.howbuy.component.AppFrame;
import com.howbuy.config.ValConfig;
import com.howbuy.lib.utils.LogUtils;

public class Receiver {
	public final static int FROM_UPGRADE_DB = 1;
	public final static int FROM_UPDATE_LAUNCH = 2;
	public final static int FROM_OPTIONAL_CHNAGE = 3;
	public final static int FROM_OPTIONAL_SYNC = 4;
	public final static int FROM_UPDATE_NETVAL_BETCH = 5;
	public final static int FROM_UPDATE_NETVAL_IDS = 6;
	public final static String RECEIVER_CHANGES = "LOCAL_BROADCAST_RECEIVER_CHANGES";
	private Context mContext = null;

	private IntentFilter getIntentFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(RECEIVER_CHANGES);
		return filter;
	}

	public void toggleLocalBroadcast(Context cx, boolean open) {
		if (open) {
			if (mContext == null) {
				mContext = cx;
				LocalBroadcastManager.getInstance(mContext).registerReceiver(mBroadCast,
						getIntentFilter());
				AppFrame.getApp().addFlag(AppFrame.GLOBAL_SERVICE_BROADCAST);
				LogUtils.d("AppService", "toggleLocalBroadcast on int trd "
						+ Thread.currentThread().getId());
			}
		} else {
			if (mContext != null) {
				mContext = null;
				LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mBroadCast);
				AppFrame.getApp().subFlag(AppFrame.GLOBAL_SERVICE_BROADCAST);
				LogUtils.d("AppService", "toggleLocalBroadcast off int trd "
						+ Thread.currentThread().getId());
			}
		}
	}

	private ArrayList<ILocalBroadcast> mListener = new ArrayList<ILocalBroadcast>();

	private ArrayList<ILocalBroadcast> getBroadCastListeners() {
		return mListener;
	}

	public void registerLocalBroadcast(ILocalBroadcast l) {
		ArrayList<ILocalBroadcast> listener = AppFrame.getApp().getLocalBroadcast()
				.getBroadCastListeners();
		if (!listener.contains(l)) {
			listener.add(l);
		}
	}

	public boolean sendBroadcast(int from, Bundle b) {
		if (mContext != null) {
			Intent t = new Intent(RECEIVER_CHANGES);
			t.putExtra(ValConfig.IT_FROM, from);
			if (b != null) {
				t.putExtras(b);
			}
			boolean result = LocalBroadcastManager.getInstance(mContext).sendBroadcast(t);
			StringBuilder sb = new StringBuilder();
			sb.append("send broadcast from ").append(from).append(",");
			dumpBundle(sb, b);
			sb.append(" send success is ").append(result);
			LogUtils.d("AppService", sb.toString());
		}
		return false;
	}

	public void unregisterLocalBroadcast(ILocalBroadcast l) {
		ArrayList<ILocalBroadcast> listener = AppFrame.getApp().getLocalBroadcast()
				.getBroadCastListeners();
		listener.remove(l);
	}

	private BroadcastReceiver mBroadCast = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int type = intent.getIntExtra(ValConfig.IT_FROM, 0);
			Bundle b = intent.getExtras();
			int n = mListener.size() - 1;
			StringBuilder sb = new StringBuilder();
			sb.append("onReceive from ").append(type).append(" for ").append(n + 1)
					.append(" listeners,");
			dumpBundle(sb, b);
			LogUtils.d("AppService", sb.toString());
			while (n >= 0) {
				mListener.get(n--).onReceiveBroadcast(type, b);
			}
		}
	};

	public interface ILocalBroadcast {
		void onReceiveBroadcast(int from, Bundle arg);
	}

	public static StringBuilder dumpBundle(StringBuilder sb, Bundle bundle) {
		if (bundle != null && bundle.size() > 0) {
			for (String key : bundle.keySet()) {
				sb.append(key).append("=").append(bundle.get(key)).append(",");
			}
		}
		return sb;
	}
}
