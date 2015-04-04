package com.howbuy.lib.interfaces;

import java.util.ArrayList;

import com.howbuy.lib.entity.CharDataType;
import com.howbuy.lib.entity.ClickType;
import com.howbuy.lib.entity.CrossType;

public interface ICurveEvent {

	public void onAttachChanged(boolean attached);

	public void onPrepare(int start, int end);

	public boolean onScaleChange(float scale);

	public void onCrossEvent(CrossType crossType,
			ArrayList<CharDataType> datas, float x, float y, int index);

	public void onClickEvent(ClickType clickType, float x, float y,
			int index);
}