package com.howbuy.control;

import android.graphics.RectF;

import com.howbuy.lib.control.ElasticLayout;
import com.howbuy.lib.control.ElasticLayout.ElasticState;
import com.howbuy.lib.control.GroupSide;
import com.howbuy.lib.control.RefeshView;

public class RefeshSide extends MyGroupSide {
	RefeshView mRefeshHead;
	public RefeshSide(GroupSide wrap) {
		super(wrap);
	}

	protected int onViewAdded(int addType) {
		int addTop=0;
		if (hasFlag(addType,INIT_TOP)) {
			mRefeshHead=(RefeshView) mVTop;
			mRefeshHead.setRefeshThreshold(mElasticView.getThreshold()*getTopRefeshH(true));
			addTop|=INIT_TOP;
			addType&=~addTop;
		}
		return addTop|super.onViewAdded(addType);
	}

	protected int onViewRemoved(int removeType) {
		int removeTop=0;
		if (hasFlag(removeType,INIT_TOP)) {
			mRefeshHead=null;
			removeTop|=INIT_TOP;
			removeType&=~removeTop;
		}
		return removeTop|super.onViewAdded(removeType);
	}

	public void onTopBotStateChanged(ElasticState curState , boolean isHead) {
		if (isHead) {
			changeHeadView(curState);
		} else {
			super.onTopBotStateChanged(curState, isHead);
		}
	}

	public void changeHeadView(ElasticState state) {
		switch (state) {
		case HEAD_PULL_MID:
			d(state, "changeHeaderView HEAD_PULL_MID");
			break;
		case HEAD_PULL_START:
			if (state.getPreState() == ElasticState.HEAD_PULL_MID) {
				
			} 
			break;
		case HEAD_PULL_END:
			if(mRefeshHead!=null){
				 mRefeshHead.setText("正在加载...");	
			}
			break;
		case NORMAL:
			if(mRefeshHead!=null){
				mRefeshHead.setText("松开刷新");
				mRefeshHead.setRefeshThreshold(mElasticView.getThreshold()*getTopRefeshH(true));	
			}
			break; 
		}
	}
	@Override
	public int getTopRefeshH(boolean considerVisible) {
		if(mRefeshHead==null||(!hasFlag(REFESH_TOP) && considerVisible)){
			return 0;
		}else{
			return mRefeshHead.getBaseHeight();
		}
	}
 
	protected void onScrollChanged(ElasticState state ,int sideType, int disX, int disY, RectF rate) {
		int slidTop=0;
		if ( hasFlag(sideType, ElasticLayout.RECORD_TOP)) {
			slidTop|=ElasticLayout.RECORD_TOP;
			sideType&=~slidTop;
			if(mRefeshHead!=null){
				mRefeshHead.setVisibleHeight(disY, !mElasticView.isAnimRun()) ;	
			}
		}
		super.onScrollChanged(state,sideType, disX, disY, rate);
	}
	public void onAnimChanged(ElasticState state,int animType,boolean start){
		if(animType==ElasticLayout.ANIM_CENTER_TOP){
			if(mRefeshHead!=null){
			mRefeshHead.onParentAnimChanged(start,true);
			}
		
		}else if(animType==ElasticLayout.ANIM_CENTER&&state.getPreState()==ElasticState.HEAD_PULL_MID){
			if(mRefeshHead!=null){
				mRefeshHead.onParentAnimChanged(start,false);
			}
		}
	}

}
