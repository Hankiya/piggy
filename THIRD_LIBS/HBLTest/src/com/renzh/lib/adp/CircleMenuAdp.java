package com.renzh.lib.adp;

import java.util.ArrayList;

import android.content.Context;

import com.howbuy.lib.adp.AbsDataAdp;
import com.howbuy.lib.control.Sector;

public class CircleMenuAdp extends AbsDataAdp<Sector> {

	public CircleMenuAdp(ArrayList<Sector> items) {
		super(items);
	}

	@Override
	protected void afterRemove(Sector item, ArrayList<Sector> items) {
		if (item != null) {
			item.onAttachChanged(false);
		}
		int n = items == null ? 0 : items.size();
		for (int i = 0; i < n; i++) {
			item = items.get(i);
			item.onAttachChanged(false);
		}
	}

	@Override
	public boolean onAttachChanged(Object owner, boolean isAttach) {
		if (isAttach) {
			int n = getCount();
			for (int i = 0; i < n; i++) {
				getItem(i).onAttachChanged(isAttach);
			}
			return true;
		} else {
			return super.onAttachChanged(owner, isAttach);
		}
	}

	@Override
	public boolean onMeasureData(int argi, float argf, Object argo) {
		int n = getCount();
		if (n > 0) {
			argf = (n == 1 ? 0 : argf);
			float wSum = 0, avarageAngles = 0, angles = 360 - argf * n;
			int wCount = 0;
			if (!isAllSame(0)) {
				for (int i = 0; i < n; i++) {
					Sector it = getItem(i);
					if (it.getWeight() > 0) {
						wSum += it.getWeight();
						wCount++;
					} else {
						avarageAngles += it.getMinSweepAngle();
					}
				}
			}
			avarageAngles = (wSum == 0 ? angles : avarageAngles);
			float averageAngle = (avarageAngles == 0 ? 0 : avarageAngles / n
					- wCount);
			measureData(angles - avarageAngles, averageAngle, argf, wSum,
					wCount);
		}
		return n > 0;
	}

	private void measureData(float weightAngles, float averageAngle,
			float divider, float wSum, int wN) {
		boolean adjust = (wSum == 0);
		int n = getCount();
		float start = 0, dAngle;
		for (int i = 0; i < n; i++) {
			Sector it = getItem(i);
			it.setStartAngle(start);
			if (it.getWeight() > 0) {
				dAngle = it.setSweepAngle(weightAngles * it.getWeight() / wSum,
						adjust);
			} else {
				dAngle = it.setSweepAngle(averageAngle, adjust);
			}
			start += (dAngle + divider);
		}
	}
}
