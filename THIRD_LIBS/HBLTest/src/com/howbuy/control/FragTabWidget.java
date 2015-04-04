package com.howbuy.control;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.howbuy.control.FragTabHost.IInnerFragTabChanged;
 
/**
 *每一个WIDGET ITEM必须设置tag="tab"
 * @author rexy  840094530@qq.com 
 * @date 2014-3-7 下午5:21:03
 */
public class FragTabWidget extends LinearLayout implements IInnerFragTabChanged,View.OnClickListener{
   
	private FragTabHost mHost=null;
	private ArrayList<View>mIndicators=new ArrayList<View>();
	private int mSelected=-1;
	
	public FragTabWidget(Context context) {
		this(context,null);
	}
	public FragTabWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(LinearLayout.HORIZONTAL);
		setDescendantFocusability(LinearLayout.FOCUS_AFTER_DESCENDANTS);
	}
	
	private FragTabHost getHost(){
		if(mHost==null){
			mHost=(FragTabHost) getParent();
		}
		return mHost;
	}
	
	public View getCurrentWidgetItem(){
		if(mSelected!=-1){
			return mIndicators.get(mSelected);
		}
		return null;
	}
	
	@Override
	public void addView(View v, int index, ViewGroup.LayoutParams params) {
	   	if(v.getVisibility()!=View.GONE&&"tab".equals( v.getTag())){
    	  v.setSelected(false);
    	  v.setOnClickListener(this);
    	  mIndicators.add(v);
    	}
	   super.addView(v, index, params);
	}
 
	@Override
	public void onTabChanged(int cur) {
		if(mSelected!=cur){
			setSelected(mSelected,false);
			mSelected=cur;
			setSelected(mSelected,true);	
		}
	}
	
	private void setSelected(int i,boolean selected){
		if(i!=-1){
			View v=mIndicators.get(i);
			v.setSelected(selected);
		}
	}
	
	@Override
	public void onClick(View v) {
		int clickIndex=-1;
		for(int i=0;i<mIndicators.size();i++) {
			if(v==mIndicators.get(i)){
				clickIndex=i;
				break;
			}
		}
		if(clickIndex!=-1){
			getHost().setCurrentTab(clickIndex);
		}
	}
}
