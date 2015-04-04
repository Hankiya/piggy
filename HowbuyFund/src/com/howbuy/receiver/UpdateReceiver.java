package com.howbuy.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.howbuy.aty.AtyUpdate;
import com.howbuy.config.ValConfig;

public class UpdateReceiver extends BroadcastReceiver {
	public static String BROADCAST_UPDATE_APP = "howbuy.android.palmfund.BROADCAST_UPDATE_APP";
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (BROADCAST_UPDATE_APP.equals(action)) {
			byte[] arg = intent.getByteArrayExtra(ValConfig.IT_ENTITY);
			if (arg != null) {
				Intent t = new Intent(context, AtyUpdate.class);
				t.putExtra(ValConfig.IT_ENTITY, arg);
				t.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(t);
			}
		}
	}
}
