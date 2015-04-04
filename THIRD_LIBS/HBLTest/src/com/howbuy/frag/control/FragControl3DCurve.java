package com.howbuy.frag.control;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Adapter;

import com.howbuy.config.ValConfig.CharType;
import com.howbuy.entity.BarDataInf;
import com.howbuy.lib.control.curveview.Bar3D;
import com.howbuy.lib.control.curveview.Curve;
import com.howbuy.lib.control.curveview.Curve3D;
import com.howbuy.lib.control.curveview.CurveControl.CtrlOption;
import com.howbuy.lib.control.curveview.CurveFactory;
import com.howbuy.lib.control.curveview.CurveSetting;
import com.howbuy.lib.control.curveview.CurveSfView;
import com.howbuy.lib.entity.CharDataType;
import com.howbuy.lib.entity.ClickType;
import com.howbuy.lib.entity.CrossType;
import com.howbuy.lib.frag.AbsFrag;
import com.howbuy.lib.interfaces.IAnimChaged;
import com.howbuy.lib.interfaces.ICharData;
import com.howbuy.lib.interfaces.ICharType;
import com.howbuy.lib.interfaces.ICurveEvent;
import com.howbuy.libtest.R;

public class FragControl3DCurve extends AbsFrag implements ICurveEvent,
		IAnimChaged {
	private CurveSfView mCurveView;

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_control_3dcurve;
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		mCurveView = (CurveSfView) root.findViewById(R.id.curveview);
		CurveSetting set = mCurveView.getSetting();
		set.setEnableCoordX(true);
		set.setEnableCoordY(true);
		set.setEnableGrid(true);
		set.setEnableCoordTxtY(true);
		set.setEnableCoordTxtX(false);
		set.setEnableTxtYInSide(true);
		set.setEnable3D(true);
		set.setEnableGrid(false);
		set.setEnableBackgroud(true);
		set.setDeep(20, 20);
		set.setArrowSize(25);

		mCurveView.setCurveFactory(new CurveFactory() {
			public Curve createCurve(CtrlOption ctrlOption, RectF rect,
					ArrayList<? extends ICharData> datas, ICharType icharType) {
				sortData(datas);
				CharType charType = (CharType) icharType;
				switch (charType) {
				case TYPE_OTHER:
					return new Bar3D(ctrlOption, rect, datas, charType);
				default:
					return new Curve3D(ctrlOption, rect, datas, charType);
				}
			}
		});
		mCurveView.setCurveEvent(this);
		mCurveView.setAnimLinstener(this);
		mCurveView.getSetting().setCurveSize(1.5f);
		mCurveView.getSetting().setCoordUpBotWeight(0.1f, 0.2f);
		mCurveView.getSetting().setBezierSmoonth(0.4f);
		mCurveView.addCurve(CharType.TYPE_OTHER, generateDefBarData(8));
		mCurveView.setInterpolator(new AccelerateDecelerateInterpolator());
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
		case R.id.tbt_switch: {
			if (mCurveView.isRateIncrease()) {
				mCurveView.setRateIncrease(false);
				mCurveView.startAnimation(1200, 10, true);
			} else {
				mCurveView.setRateIncrease(true);
				mCurveView.startAnimation(1200, 10, false);
			}
			break;
		}
		case R.id.bt_reset_data: {
			ArrayList<BarDataInf> datas = generateDefBarData(16);
			mCurveView.cleanCurve(true, true);
			mCurveView.getSetting().setEnableCoordTxtX(false);
			mCurveView.addCurveData(CharType.TYPE_OTHER, datas, true, false);
			mCurveView.requestLayout();
			break;
		}
		case R.id.bt_add_data: {
			mCurveView.cleanCurve(true, true);
			mCurveView.addCurveData(CharType.TYPE_FINANCIAL,
					generateDefBarData(15), true, false);
			mCurveView.getSetting().setEnableCoordTxtX(true);
			mCurveView.requestLayout();
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
	public void onAnimChaged(View v, int type, int which, float val, float dval) {
		if (type != IAnimChaged.TYPE_CHANGED) {
			// popMsg("anim is ="+(type==CircleEvent.TYPE_START?"start":"end")+" percent="+percent);
		}
	}

	@Override
	public void onAttachChanged(boolean attached) {
		d("onAttachChanged", "attached="+attached);
	}

}
