package com.howbuy.frag.control;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.howbuy.control.SlidBitView;
import com.howbuy.lib.frag.AbsFrag;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.libtest.R;

public class FragSlidNums extends AbsFrag {
	SlidBitView mBit = null;
	EditText mEtValue = null;

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_slid_nums;
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		mBit = (SlidBitView) root.findViewById(R.id.tv_bit);
		mBit.setMaxBitAndDecimal(4, 2);
		mEtValue = (EditText) root.findViewById(R.id.et_value);
		mBit.setInterpolator(new LinearInterpolator());
		mBit.setTxtSize(70);
		mBit.setTxtStroke(15);
		mBit.setBitArg(20, 15, 10, 8);
		mBit.setBitBackRoundRadius(15);
		mBit.setColor(0xff00cc00, 0xffcc0000);
		mBit.setBitAnimDuration(200);
		mBit.setInterpolator(new LinearInterpolator());
		mBit.setCurrentNum(12.34f, false);
	}

	public boolean onXmlBtClick(View v) {
		boolean handled = true;
		switch (v.getId()) {
		case R.id.bt_test:
			break;
		case R.id.bt_change:
			startChange(5);
			break;
		default:
			handled = false;
			break;
		}
		return handled;
	}

	private void startChange(int to) {
		float value = 12.34f;
		try {
			String str = mEtValue.getText().toString();
			if (!StrUtils.isEmpty(str)) {
				value = Float.parseFloat(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mBit.setCurrentNum(value, true);
	}

	@Override
	protected void onAttachChanged(Activity aty, boolean isAttach) {
		super.onAttachChanged(aty, isAttach);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean handled = false;
		return handled;
	}
}
