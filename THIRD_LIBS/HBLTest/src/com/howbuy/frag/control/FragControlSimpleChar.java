package com.howbuy.frag.control;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.howbuy.control.SimpleChartView;
import com.howbuy.entity.BarDataInf;
import com.howbuy.entity.CurveDataInfo;
import com.howbuy.lib.frag.AbsFrag;
import com.howbuy.libtest.R;

public class FragControlSimpleChar extends AbsFrag {
	private SimpleChartView mCurveView;

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_control_simplechar;
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		mCurveView = (SimpleChartView) root.findViewById(R.id.chartview);
		mCurveView.setData(generateDefData(120));
	}

	private ArrayList<CurveDataInfo> generateDefData(int n) {
		long cur = System.currentTimeMillis();
		long gap = 64 * 3600000;
		Random ran = new Random(cur);
		ArrayList<CurveDataInfo> list = new ArrayList<CurveDataInfo>();
		for (int i = 0; i < n; i++) {
			list.add(new CurveDataInfo(ran.nextFloat() * 10 + 5, cur - i * gap));
		}
		return list;
	}

	private ArrayList<CurveDataInfo> generateDefData(int n, long beforeTime) {
		long gap = 24 * 3600000;
		beforeTime -= gap;
		Random ran = new Random(beforeTime);
		ArrayList<CurveDataInfo> list = new ArrayList<CurveDataInfo>();
		for (int i = 0; i < n; i++) {
			list.add(new CurveDataInfo(ran.nextFloat() * 10 + 5, beforeTime - i
					* gap));
		}
		return list;
	}

	@Override
	public boolean onXmlBtClick(View v) {
		switch (v.getId()) {
		case R.id.bt_reset_data: {
			mCurveView.setData(generateDefData(120));
		}
			break;
		case R.id.bt_add_data: {
			mCurveView.addData(generateDefData(50, mCurveView.getData().get(mCurveView.getData().size()-1).getTime()), true);
		}
			break;
		case R.id.tbt_switch: {
		}
			break;
		}
		return false;
	}

}
