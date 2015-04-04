package com.howbuy.aty;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;

import com.google.protobuf.InvalidProtocolBufferException;
import com.howbuy.config.ValConfig;
import com.howbuy.lib.aty.AbsAty;
import com.howbuy.wireless.entity.protobuf.HostDistributionProtos.HostDistribution;

public class AtyUpdate extends Activity implements OnKeyListener, OnDismissListener {

	private HostDistribution mUpdateDto;
	private boolean need;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		byte[] arg = i.getByteArrayExtra(ValConfig.IT_ENTITY);
		try {
			mUpdateDto = arg == null ? null : HostDistribution.parseFrom(arg);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		if (mUpdateDto == null) {
			finish();
		}
		need = "1".equals(mUpdateDto.getVersionNeedUpdate());
		String desc = mUpdateDto.getUpdateDesc();
		final String url = mUpdateDto.getUpdateUrl();

		String exitBtnText = need ? "退出" : "取消";
		Builder builder = new Builder(this).setTitle("版本更新").setMessage(desc);
		builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent i = new Intent("android.intent.action.VIEW");
				i.setData(Uri.parse(url));
				startActivity(i);
				if (need) {
					AbsAty.exitApp(false);
				}
			}
		});

		builder.setNegativeButton(exitBtnText, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (need) {
					AbsAty.exitApp(false);
				}
			}
		});

		Dialog d = builder.create();
		d.setOnKeyListener(this);
		d.setOnDismissListener(this);
		if (need) {
			builder.setCancelable(false);
			d.setCancelable(false);
			d.setCanceledOnTouchOutside(false);
		}
		d.show();

	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (need) {
				AbsAty.exitApp(false);
				return true;
			}
		}
		return false;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		finish();
	}

}
