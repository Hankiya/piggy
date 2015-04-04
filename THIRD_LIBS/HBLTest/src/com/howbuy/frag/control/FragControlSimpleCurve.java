package com.howbuy.frag.control;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;

import com.howbuy.config.ValConfig.CharType;
import com.howbuy.entity.BarDataInf;
import com.howbuy.entity.CurveDataInfo;
import com.howbuy.lib.control.curveview.Curve;
import com.howbuy.lib.control.curveview.CurveControl.CtrlOption;
import com.howbuy.lib.control.curveview.CurveFactory;
import com.howbuy.lib.control.curveview.CurveRect;
import com.howbuy.lib.control.curveview.CurveRectTxt;
import com.howbuy.lib.control.curveview.CurveSetting;
import com.howbuy.lib.control.curveview.CurveSfView;
import com.howbuy.lib.control.curveview.CurveShade;
import com.howbuy.lib.control.curveview.CurveView;
import com.howbuy.lib.entity.CharDataType;
import com.howbuy.lib.entity.ClickType;
import com.howbuy.lib.entity.CrossType;
import com.howbuy.lib.frag.AbsFrag;
import com.howbuy.lib.interfaces.ICharData;
import com.howbuy.lib.interfaces.ICharType;
import com.howbuy.lib.interfaces.ICurveEvent;
import com.howbuy.libtest.R;

public class FragControlSimpleCurve extends AbsFrag implements ICurveEvent {
	private CurveView mCurveView;
	private boolean hasCoord = true;

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_control_simple_curve;
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		mCurveView = (CurveView) root.findViewById(R.id.curveview);
		changeSeting(false);
		mCurveView.setCurveFactory(new CurveFactory() {
			public Curve createCurve(CtrlOption ctrlOption, RectF rect,
					ArrayList<? extends ICharData> datas, ICharType icharType) {
				sortData(datas);
				CharType charType = (CharType) icharType;
				switch (charType) {
				case TYPE_FINANCIAL:
					return new CurveRectTxt(ctrlOption, rect, datas, charType);
				case TYPE_OTHER:
					return new CurveRect(ctrlOption, rect, datas, charType);
				case TYPE_ALL:
					return new CurveShade(ctrlOption, rect, datas, charType);
				default:
					return new Curve(ctrlOption, rect, datas, charType);
				}
			}
		});
		mCurveView.getSetting().setCurveSize(1.5f);
		mCurveView.getSetting().setCoordUpBotWeight(0.1f, 0.2f);
		mCurveView.getSetting().setBezierSmoonth(0.4f);
		ArrayList<CurveDataInfo> datas = generateDefData(15);
		mCurveView.addCurve(CharType.TYPE_FINANCIAL, generateDefBarData(8));
	}

	private void changeSeting(boolean enableCoord) {
		if (enableCoord != hasCoord) {
			hasCoord = enableCoord;
			CurveSetting set = mCurveView.getSetting();
			set.setEnableInitCent(true);
			if (hasCoord) {
				set.setEnableCoordX(true);
				set.setEnableCoordY(true);
				set.setEnableGrid(true);
				set.setEnableCoordTxtY(true);
				set.setEnableTxtYInSide(true);
				set.setEnableCoordTxtX(true);
				set.setArrowSize(15);
			} else {
				set.setEnableCoordX(true);
				set.setEnableCoordY(false);
				set.setEnableGrid(false);
				set.setEnableCoordTxtY(false);
				set.setEnableTxtYInSide(true);
				set.setArrowSize(0);
			}
			mCurveView.requestLayout();
		}

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
		switch (v.getId()) {
		case R.id.bt_reset_data: {
			changeSeting(true);
			ArrayList<BarDataInf> datas = generateDefBarData(16);
			mCurveView.cleanCurve(true, true);
			mCurveView.addCurveData(CharType.TYPE_ALL, datas, true, false);
			// mCurveView.addCurveData(CharType.TYPE_MINING, datas,
			// true, false);
			// mCurveView.addCurve(CharType.TYPE_OTHER, datas);
			// mCurveView.addCurve(CharType.TYPE_OTHER, datas);
			mCurveView.requestLayout();
			break;
		}
		case R.id.bt_add_data: {
			ArrayList<CurveDataInfo> datas = generateDefData(
					20,
					mCurveView.getCurveData(null)
							.get((mCurveView.getCurveData(null).size() - 1))
							.getTime());
			changeSeting(true);
			mCurveView.removeCurve(CharType.TYPE_FINANCIAL, true);
			mCurveView.addCurveData(CharType.TYPE_OTHER, datas, true, false);
			mCurveView.addCurveData(CharType.TYPE_MINING, datas, true, false);
			break;
		}
		}
		return false;
	}

	@Override
	public void onPrepare(int start, int end) {
		d(null,"onPrepare> start=" + start + " end=" + end);
	}

	@Override
	public boolean onScaleChange(float scale) {
		d(null,"onScaleChange> count=" + scale);
		return false;
	}

	@Override
	public void onCrossEvent(CrossType crossType,
			ArrayList<CharDataType> datas, float x, float y, int index) {
		d(null,"onCrossEvent> crossType=" + crossType.name() + " index=" + index
				+ " datas size=" + (datas == null ? 0 : datas.size()));
	}

	@Override
	public void onClickEvent(ClickType clickType, float x, float y, int index) {
		d(null,"onClickEvent> clickType=" + clickType.name() + " index=" + index);
	}

	@Override
	public void onAttachChanged(boolean attached) {

	}

}
