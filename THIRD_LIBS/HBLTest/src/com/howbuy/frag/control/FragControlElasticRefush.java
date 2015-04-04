package com.howbuy.frag.control;

import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.howbuy.control.RefeshSide;
import com.howbuy.datalib.fund.AAParFundChart;
import com.howbuy.lib.control.ElasticLayout;
import com.howbuy.lib.control.ElasticLayout.ElasticState;
import com.howbuy.lib.control.ElasticLayout.IElasticEvent;
import com.howbuy.lib.control.RefeshView;
import com.howbuy.lib.frag.AbsFrag;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.libtest.R;

public class FragControlElasticRefush extends AbsFrag implements IElasticEvent, IReqNetFinished {
	ElasticLayout mElasticLayout;
	TextView mTvResult = null;

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_control_elastic_refush;
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		mElasticLayout = (ElasticLayout) root.findViewById(R.id.elastic_root);
		mTvResult = (TextView) mElasticLayout.findViewById(R.id.tv_result);
		mElasticLayout.setElasticEvent(this);
		mElasticLayout.setHeadFootView(-1, R.layout.com_foot_view);
		mElasticLayout.setView(ElasticLayout.RECORD_TOP, new RefeshView(getSherlockActivity()));
		mElasticLayout.setLeftRightView(R.layout.com_left_view, R.layout.com_right_view);
		mElasticLayout.setHeadFootHandle(new RefeshSide(null));
		mElasticLayout.setElasticRange(0.7f, true);
		mElasticLayout.setElasticRange(1f, false);
		mElasticLayout.setThreshold(2);
		mElasticLayout.setWrapContent(false);
		mElasticLayout.setElasticBoth(false);
		// mElasticLayout.setRefeshEnable(ElasticLayout.RECORD_BOT, false);
		// mElasticLayout.setSlidFromEdgeEnable(ElasticLayout.SLID_FROM_VERTICAL|ElasticLayout.SLID_FROM_HORIZONAL,
		// true);
		mElasticLayout.setShadeEnable(ElasticLayout.SHADE_TOP, false);
		mElasticLayout.setScaleEnable(ElasticLayout.SCALE_TOP | ElasticLayout.SCALE_LEFT, false);
		// mElasticLayout.setScrollEnable(IScrollable.SCROLL_LEFT|IScrollable.SCROLL_RIGHT,
		// false);
		mElasticLayout.setShadeVerticalBarEnable(true);
	}

	@Override
	public boolean onElasticRefresh(final boolean isHead, float val) {
		AAParFundChart par = null;
		if (isHead) {
			par = new AAParFundChart("boolean:" + isHead, this, CacheOpt.TIME_MIN);
			par.setParams("" + 519670, 0 + "", null, null, 30);
			ReqNetOpt opt = par.getReqOpt(false);
			opt.addFlag(ReqNetOpt.FLAG_CACHE_THEN_REQ);
		} else {
			par = new AAParFundChart("boolean:" + isHead, this, CacheOpt.TIME_MIN);
			par.setParams("" + 519670, 0 + "", null, null, 30);
		}
		par.execute();
		return true;
	}

	@Override
	public void onAnimChaged(View v, int type, int which, float val, float dval) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onElasticStateChanged(ElasticState state, boolean newState, RectF rate) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onElasticViewChanged(int pos, View curV, int animFlag) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onRepNetFinished(final ReqResult<ReqNetOpt> result) {
		mTvResult.setText(result.toString());
		mElasticLayout.onRefreshComplete(result.mReqOpt.getHandleType() == 1000);

	}

}
