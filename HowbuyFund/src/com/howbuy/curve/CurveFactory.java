package com.howbuy.curve;

import java.util.ArrayList;
import java.util.Collections;

import android.graphics.RectF;

public abstract class CurveFactory {

	public static void sortData(ArrayList<? extends ICharData> datas) {
		if (datas != null && datas.size() > 1) {
			if (datas.get(0).getTime() < datas.get(1).getTime()) {
				Collections.reverse(datas);
			}
		}
	}

	public static Curve getDefCurve(final CurveControl.CtrlOption ctrlOption, final RectF rect,
			ArrayList<? extends ICharData> datas, final ICharType icharType) {
		sortData(datas);
		return new CurveShade(ctrlOption, rect, datas, icharType);
	}

	public abstract Curve createCurve(final CurveControl.CtrlOption ctrlOption, final RectF rect,
			ArrayList<? extends ICharData> datas, final ICharType icharType);

}
