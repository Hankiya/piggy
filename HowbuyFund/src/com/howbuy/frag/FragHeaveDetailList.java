package com.howbuy.frag;

import howbuy.android.palmfund.R;

import java.util.ArrayList;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.protobuf.InvalidProtocolBufferException;
import com.howbuy.adp.HeavyDetailHoldAdp;
import com.howbuy.aty.AtyEmpty;
import com.howbuy.config.FundConfig.FundType;
import com.howbuy.config.ValConfig;
import com.howbuy.control.CircleView;
import com.howbuy.curve.CharDataType;
import com.howbuy.entity.HeavyHoldItem;
import com.howbuy.lib.frag.AbsFragMger.FragOpt;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.lib.utils.SysUtils;
import com.howbuy.utils.FundUtils;
import com.howbuy.wireless.entity.protobuf.ZccgInfoListProtos.ZccgInfo;
import com.tang.library.pulltorefresh.PullToRefreshBase;

public class FragHeaveDetailList extends AbsFragList implements OnItemClickListener {
	private FundType mFundType = null;
	private CircleView mCircleView;
	private HeavyDetailHoldAdp mAdapter = null;
	private static int[] mColor = new int[] { 0xff60769e, 0xff9e9e60, 0xff609e60, 0xff609e9e,
			0xff9e609e, 0xffffb143, 0xff9a9ab6, 0xff466543, 0xffbaac8e, 0xff9e6073, 0xffe9e9e9 };
	private ZccgInfo mZccginf = null;
	private String mJjdm = null;

	private void intiAdapter() {
		Bundle arg = getArguments();
		if (mTitleLable == null) {
			mTitleLable = arg.getString(ValConfig.IT_NAME);
		}
		if (mAdapter == null) {
			try {
				mJjdm = arg.getString(ValConfig.IT_ID);
				mFundType = (FundType) arg.getParcelable(ValConfig.IT_TYPE);
				byte[] infb = arg.getByteArray(ValConfig.IT_ENTITY);
				if (infb != null) {
					mZccginf = ZccgInfo.parseFrom(infb);
					mAdapter = new HeavyDetailHoldAdp(getSherlockActivity(), HeavyHoldItem.parse(
							mZccginf.getStockListList(), mColor));
				}
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
			if (mAdapter == null) {
				mAdapter = new HeavyDetailHoldAdp(getSherlockActivity(), null);
			}
		}
	}

	private ArrayList<CharDataType> wrapAdapter(HeavyDetailHoldAdp adp) {
		ArrayList<HeavyHoldItem> ls = adp.getItems();
		int n = ls.size();
		ArrayList<CharDataType> r = new ArrayList<CharDataType>(n);
		CharDataType d = null;
		for (int i = 0; i < n; i++) {
			d = new CharDataType(ls.get(i), null);
			r.add(d);
		}
		return r;
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		super.initViewAdAction(root, bundle);
		setPullRefushMode(false, false);
		intiAdapter();
		mListView.setSelector(new ColorDrawable(0));
		int pad = (int) SysUtils.getDimension(getSherlockActivity(), TypedValue.COMPLEX_UNIT_DIP,
				12);
		LayoutInflater lf = LayoutInflater.from(getSherlockActivity());
		View head = lf.inflate(R.layout.com_list_heavy_header, null);
		head.setPadding(pad, 0, pad, 0);
		mCircleView = (CircleView) head.findViewById(R.id.circleview);
		mCircleView.setInnerDivOut(2 / 3f);
		mCircleView.setDivider(1);
		mCircleView.setDividerColor(0);
		mCircleView.setBackGroudColor(getResources().getColor(R.color.window_bg));
		mCircleView.setWdivH(1);
		mCircleView.setEnabled(false);
		mCircleView.setTxtColor(0xff999999);
		mCircleView.setOffset(270);
		mCircleView.setTexSize(SysUtils.getDimension(getSherlockActivity(),
				TypedValue.COMPLEX_UNIT_SP, 22));
		mCircleView.setData(wrapAdapter(mAdapter));
		String[] str = FundUtils.formatQuarter(mZccginf.getSeason());
		if (str != null) {
			mCircleView.setCenterText(str[0], str[1]);
		}
		mListView.addHeaderView(head);
		mListView.setAdapter(mAdapter);
		if (!StrUtils.isEmpty(mJjdm)) {
			mListView.addFooterView(lf.inflate(R.layout.com_list_heavy_footer, null));
			mListView.setOnItemClickListener(this);
			mListView.setFooterDividersEnabled(false);
		}
	}

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_content_list;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (position == mAdapter.getCount() + 3) {
		}
	}

	@Override
	public boolean onXmlBtClick(View v) {
		if (v.getId() == R.id.tv_name) {
			Bundle b = new Bundle();
			b.putParcelable(ValConfig.IT_TYPE, mFundType);
			b.putString(ValConfig.IT_NAME, "重仓持股列表");
			b.putString(ValConfig.IT_ID, mJjdm);
			b.putString(ValConfig.IT_ENTITY, mZccginf.getSeason());
			FragOpt f = new FragOpt(FragHeavyHoldeList.class.getName(), b, FragOpt.FRAG_POPBACK);
			((AtyEmpty) getActivity()).switchToFrag(f);
		}
		return true;
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
	}

}
