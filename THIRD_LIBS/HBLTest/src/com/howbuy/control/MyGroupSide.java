package com.howbuy.control;

import java.util.Calendar;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.howbuy.lib.control.ElasticLayout.ElasticState;
import com.howbuy.lib.control.GroupSide;
import com.howbuy.libtest.R;

public class MyGroupSide extends GroupSide {
	protected RotateAnimation mAnimRoat, mAnimRoatReverse;
	protected ProgressBar mProgHead, mProFoot;
	protected ImageView mIvArrowHead, mIvArrowFoot;
	protected TextView mTvTipHead, mTvTipFoot, mTvTimeHead, mTvTimeFoot;

	public MyGroupSide(GroupSide wrap) {
		super(wrap);
	}

	protected int onViewAdded(int addType) {
		if (hasFlag(addType,INIT_TOP)) {
			mIvArrowHead = (ImageView) mVTop
					.findViewById(R.id.elastic_head_iv_arrow);
			mTvTipHead = (TextView) mVTop
					.findViewById(R.id.elastic_head_tv_tips);
			mTvTimeHead = (TextView) mVTop
					.findViewById(R.id.elastic_head_tv_time);
			mProgHead = (ProgressBar) mVTop
					.findViewById(R.id.elastic_head_prog);
		}
		if (hasFlag(addType,INIT_BOT)) {
			mIvArrowFoot = (ImageView) mVBot
					.findViewById(R.id.elastic_foot_iv_arrow);
			mTvTipFoot = (TextView) mVBot
					.findViewById(R.id.elastic_foot_tv_tips);
			mTvTimeFoot = (TextView) mVBot
					.findViewById(R.id.elastic_foot_tv_time);
			mProFoot = (ProgressBar) mVBot.findViewById(R.id.elastic_foot_prog);
		}
		if (mAnimRoat == null) {
			mAnimRoat = new RotateAnimation(0, -180,
					RotateAnimation.RELATIVE_TO_SELF, 0.5f,
					RotateAnimation.RELATIVE_TO_SELF, 0.5f);
			mAnimRoat.setDuration(500);
			mAnimRoat.setFillAfter(true);
			mAnimRoatReverse = new RotateAnimation(-180, 0,
					RotateAnimation.RELATIVE_TO_SELF, 0.5f,
					RotateAnimation.RELATIVE_TO_SELF, 0.5f);
			mAnimRoatReverse.setDuration(500);
			mAnimRoatReverse.setFillAfter(true);
		}
		return addType;
	}

	protected int onViewRemoved(int removeType) {
		if (hasFlag(removeType,INIT_TOP)) {
			mIvArrowHead.clearAnimation();
			mIvArrowHead = null;
			mTvTipHead = null;
			mTvTimeHead = null;
		}
		if (hasFlag(removeType,INIT_BOT)) {
			mIvArrowFoot.clearAnimation();
			mIvArrowFoot = null;
			mTvTipFoot = null;
			mTvTimeFoot = null;
		}
		return removeType;
	}

	public void onTopBotStateChanged(ElasticState curState , boolean isHead) {
		if(getAdpter()==null){
			if (isHead) {
				changeHeadView(curState);
			} else {
				changeFootView(curState);
			}
		}
	}

	public void changeHeadView(ElasticState state) {
		switch (state) {
		case HEAD_PULL_MID:
			mIvArrowHead.setVisibility(View.VISIBLE);
			mProgHead.setVisibility(View.GONE);
			mTvTipHead.setVisibility(View.VISIBLE);
			mTvTimeHead.setVisibility(View.VISIBLE);
			mIvArrowHead.clearAnimation();
			mIvArrowHead.startAnimation(mAnimRoat);
			mTvTipHead.setText("松开刷新");
			d(state, "changeHeaderView HEAD_PULL_MID");
			break;
		case HEAD_PULL_START:
			mProgHead.setVisibility(View.GONE);
			mTvTipHead.setVisibility(View.VISIBLE);
			mTvTimeHead.setVisibility(View.VISIBLE);
			mIvArrowHead.clearAnimation();
			mIvArrowHead.setVisibility(View.VISIBLE);
			if (state.getPreState() == ElasticState.HEAD_PULL_MID) {
				mIvArrowHead.clearAnimation();
				mIvArrowHead.startAnimation(mAnimRoatReverse);
			}
			mTvTipHead.setText("下拉刷新");
			break;
		case HEAD_PULL_END:
			mProgHead.setVisibility(View.VISIBLE);
			mIvArrowHead.clearAnimation();
			mIvArrowHead.setVisibility(View.GONE);
			mTvTipHead.setText("正在刷新...");
			mTvTimeHead.setVisibility(View.INVISIBLE);
			mTvTimeHead.setText("上次更新："
					+ Calendar.getInstance().getTime().toLocaleString());
			break;
		case NORMAL:
			mProgHead.setVisibility(View.GONE);
			mIvArrowHead.clearAnimation();
			mIvArrowHead.setImageResource(R.drawable.arrow_down);
			mTvTipHead.setText("下拉刷新");
			mTvTimeHead.setVisibility(View.VISIBLE);
			break; 
		}
	}

	public void changeFootView(ElasticState state) {
		switch (state) {
		case FOOT_PUSH_MID:
			mIvArrowFoot.setVisibility(View.VISIBLE);
			mProFoot.setVisibility(View.GONE);
			mTvTipFoot.setVisibility(View.VISIBLE);
			mTvTimeFoot.setVisibility(View.VISIBLE);
			mIvArrowFoot.clearAnimation();
			mIvArrowFoot.startAnimation(mAnimRoat);
			mTvTipFoot.setText("松开刷新");
			break;
		case FOOT_PUSH_START:
			mProFoot.setVisibility(View.GONE);
			mTvTipFoot.setVisibility(View.VISIBLE);
			mTvTimeFoot.setVisibility(View.VISIBLE);
			mIvArrowFoot.clearAnimation();
			mIvArrowFoot.setVisibility(View.VISIBLE);
			if (state.getPreState() == ElasticState.FOOT_PUSH_MID) {
				mIvArrowFoot.clearAnimation();
				mIvArrowFoot.startAnimation(mAnimRoatReverse);
			}
			mTvTipFoot.setText("上推刷新");
			break;
		case FOOT_PUSH_END:
			mProFoot.setVisibility(View.VISIBLE);
			mIvArrowFoot.clearAnimation();
			mIvArrowFoot.setVisibility(View.GONE);
			mTvTipFoot.setText("正在刷新...");
			mTvTimeFoot.setVisibility(View.INVISIBLE);
			mTvTimeFoot.setText("上次更新："
					+ Calendar.getInstance().getTime().toLocaleString());
			break;
		case NORMAL:
			mProFoot.setVisibility(View.GONE);
			mIvArrowFoot.clearAnimation();
			mIvArrowFoot.setImageResource(R.drawable.arrow_up);
			mTvTipFoot.setText("上推刷新");
			mTvTimeFoot.setVisibility(View.VISIBLE);
			break;
		}
	}
 
}
