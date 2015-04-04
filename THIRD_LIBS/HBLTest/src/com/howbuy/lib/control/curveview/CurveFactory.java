package com.howbuy.lib.control.curveview;

import java.util.ArrayList;
import java.util.Collections;

import android.graphics.RectF;

import com.howbuy.lib.interfaces.ICharData;
import com.howbuy.lib.interfaces.ICharType;

public abstract class CurveFactory {  

	public static void sortData(ArrayList<? extends ICharData> datas){
		if(datas!=null&&datas.size()>1){
			if(datas.get(0).getTime()<datas.get(1).getTime()){
			   Collections.reverse(datas); 
			}
		}
	}
	public static Curve getDefCurve(final  CurveControl.CtrlOption ctrlOption ,final RectF rect,ArrayList< ? extends ICharData> datas, final ICharType icharType) {
		sortData(datas); 
	    return new BezierLine(ctrlOption,rect, datas, icharType);
	}
	public abstract Curve createCurve(final  CurveControl.CtrlOption ctrlOption ,final RectF rect,ArrayList< ? extends ICharData> datas, final ICharType icharType);
    
}
