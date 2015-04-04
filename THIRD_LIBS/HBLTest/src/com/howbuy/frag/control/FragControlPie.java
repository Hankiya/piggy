package com.howbuy.frag.control;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

import com.howbuy.config.ValConfig.CharType;
import com.howbuy.entity.BarDataInf;
import com.howbuy.lib.control.curveview.CircleView;
import com.howbuy.lib.control.curveview.CircleView.ICircleEvent;
import com.howbuy.lib.entity.CharDataType;
import com.howbuy.lib.frag.AbsFrag;
import com.howbuy.libtest.R;

public class FragControlPie extends AbsFrag implements ICircleEvent {
	private CircleView mCircleView;

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_pie;
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		mCircleView = (CircleView) root.findViewById(R.id.circleview);
		mCircleView.setCircleEvent(this);
		mCircleView.setOffset(90);
		resetCircleData();
	}

	private void resetCircleData() {
		CharType[] types = CharType.values();
		int n = Math.min(new Random().nextInt(types.length) + 1, types.length);
		ArrayList<BarDataInf> data = generateDefBarData(n);
		ArrayList<CharDataType> list = new ArrayList<CharDataType>(n);
		for (int i = 0; i < n; i++) {
			list.add(new CharDataType(data.get(i), types[i % types.length]));
		}
		mCircleView.setData(list);
	}

	private ArrayList<BarDataInf> generateDefBarData(int n) {
		long cur = System.currentTimeMillis();
		long gap = 64 * 3600000;
		Random ran = new Random(cur);
		ArrayList<BarDataInf> list = new ArrayList<BarDataInf>();
		for (int i = 0; i < n; i++) {
			list.add(new BarDataInf(ran.nextFloat() * 10 + 5, cur - i * gap,
					"基金" + i + "", i, Color.rgb(ran.nextInt(255),
							ran.nextInt(255), ran.nextInt(255))));
		}
		return list;
	}

	@Override
	public boolean onXmlBtClick(View v) {

		if (v instanceof ToggleButton) {
			ToggleButton tbt = (ToggleButton) v;
			switch (v.getId()) {
			case R.id.tbt_switch: {
				if (mCircleView.isRateIncrease()) {
					mCircleView.setRateIncrease(false);
					mCircleView.startAnimation(1100, 80, true);

				} else {
					mCircleView.setRateIncrease(true);
					mCircleView.startAnimation(1300, 80, false);
				}
			}
				break;
			case R.id.tbt_extend: {
				mCircleView.setEnableExtendSelected(tbt.isChecked());
			}
				break;
			case R.id.tbt_roate: {
				mCircleView.setEnableRoate(tbt.isChecked());
			}
				break;

			case R.id.tbt_sweep: {
				mCircleView.setEnableSweep(tbt.isChecked());
			}
				break;

			case R.id.tbt_scale: {
				mCircleView.setEnableScale(tbt.isChecked());
			}
				break;

			case R.id.tbt_alpha: {
				mCircleView.setEnableAlpah(tbt.isChecked());
			}
				break;
			}

		} else {
			switch (v.getId()) {
			case R.id.bt_reset_data: {
				resetCircleData();
				break;
			}
			case R.id.bt_stop_anim: {
				mCircleView.stopAnim(true);
			}
				break;
			}
		}
		return false;
	}

	@Override
	public void onSelectedChaged(int cur, int pre, boolean hasAnim) {
	}

	@Override
	public void onAnimChaged(View v, int type, int which, float val, float dval) {
		if (type != ICircleEvent.TYPE_CHANGED) {
		}
	}
}
