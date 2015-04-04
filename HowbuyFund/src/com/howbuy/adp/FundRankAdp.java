package com.howbuy.adp;

import howbuy.android.palmfund.R;

import java.util.Arrays;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.howbuy.component.AppFrame;
import com.howbuy.config.FundConfig;
import com.howbuy.config.FundConfig.FundType;
import com.howbuy.config.SelfConfig;
import com.howbuy.config.ValConfig;
import com.howbuy.control.CheckHeadText;
import com.howbuy.entity.NetWorthBean;
import com.howbuy.entity.NetWorthListBean;
import com.howbuy.lib.adp.AbsListAdp;
import com.howbuy.lib.adp.AbsViewHolder;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.utils.FundUtils;

public class FundRankAdp extends AbsListAdp<NetWorthListBean, NetWorthBean> {
	private static String[] HEADTITLE = new String[] { "昨天", "更早" };
	private int[] HEADINDEX = null;
	// private int[] HEADINDEXCOUNT = null;
	private int mSortIndex = 0;
	private int mExtraIndex = 0;
	// private FundType mType = null;
	private static String mTopList = null;
	private static Drawable mTopDrawable = null;

	public FundRankAdp(Context cx, NetWorthListBean items) {
		super(cx, items);
		if (mTopList == null) {
			mTopList = (String) GlobalApp.getApp().getMapObj(ValConfig.KEY_TOP_RECOMEND_LIST);
		}
	}

	private Drawable getRecomendDrawable() {
		if (mTopDrawable == null) {
			mTopDrawable = GlobalApp.getApp().getResources().getDrawable(R.drawable.ic_grade1);
		}
		return mTopDrawable;
	}

	private boolean checkRecommend(String code) {
		if (mTopList != null) {
			return mTopList.contains(code);
		}
		return false;
	}

	@Override
	protected View getViewFromXml(int type, ViewGroup p) {
		return mLf.inflate(type == 0 ? R.layout.com_list_rank_item
				: R.layout.com_list_rank_head, p, false);
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	protected AbsViewHolder<NetWorthBean> getViewHolder() {
		return new RankHolder();
	}

	@Override
	public int getItemViewType(int position) {
		if (HEADINDEX != null && position != 0) {
			for (int i = 0; i < HEADINDEX.length; i++) {
				if (HEADINDEX[i] == position) {
					return 1;
				}
			}
		}
		return 0;
	}

	public void setHeadIndex(int[] indexs, int indexsCount[]) {
		HEADINDEX = indexs;
		// HEADINDEXCOUNT = indexsCount;
		if (indexs != null && HEADINDEX[0] != -1) {
			HEADTITLE[0] = StrUtils.timeFormat(mItems.getItem(HEADINDEX[0]).getJzrq(),
					ValConfig.DATEF_YMD, ValConfig.DATEF_YMD_S_);
		}
	}

	public void setSortIndex(int sortIndex, int extraIndex, FundType type) {
		mSortIndex = sortIndex;
		mExtraIndex = extraIndex;
		// mType = type;
	}

	public int indexOfHead(int position) {
		int n = HEADINDEX == null ? 0 : HEADINDEX.length;
		for (int i = 0; i < n; i++) {
			if (HEADINDEX[i] == position) {
				return i;
			}
		}
		return -1;
	}

	public class RankHolder extends AbsViewHolder<NetWorthBean> {
		ImageView IvGrade;
		TextView TvTitle, TvCode, TvValue, TvIncrease, TvRank;
		CheckHeadText TvHead;
		CheckBox CbCollect;

		// TextView TvDate = null;

		@Override
		protected void initView(View root, int type) {
			// TvDate = (TextView) root.findViewById(R.id.tv_date);
			IvGrade = (ImageView) root.findViewById(R.id.iv_grade);
			TvTitle = (TextView) root.findViewById(R.id.tv_title);
			TvCode = (TextView) root.findViewById(R.id.tv_code);
			TvValue = (TextView) root.findViewById(R.id.tv_value);
			TvIncrease = (TextView) root.findViewById(R.id.tv_increase);
			TvRank = (TextView) root.findViewById(R.id.tv_rank);
			CbCollect = (CheckBox) root.findViewById(R.id.cb_collect);
			CbCollect.setTag(RankHolder.this);
			if (type == 1) {
				TvHead = (CheckHeadText) root.findViewById(R.id.tv_head);
				TvHead.setHeadColor(0xffff9500);
			}
		}

		@Override
		public int changeView(int arg) {
			if (CbCollect.isChecked()) {
				if (mItem.getXunan() < 1) {
					mItem.setXunan(SelfConfig.UNSynsAdd);
					FundUtils.updateOptional(AppFrame.getApp(), mItem.getJjdm(),
							SelfConfig.UNSynsAdd, true);
					return 1;
				}
			} else {
				if (mItem.getXunan() >= 1) {
					mItem.setXunan(SelfConfig.UNSynsDel);
					FundUtils.updateOptional(AppFrame.getApp(), mItem.getJjdm(),
							SelfConfig.UNSynsDel, true);
					if (FundConfig.getConfig().hasFlag(FundConfig.RANK_FLAG_OPTIONAL)) {
						removeItem(mIndex, true);
					}
					return -1;
				}
			}
			return 0;
		}

		@Override
		protected void initData(int index, int type, NetWorthBean item, boolean isReuse) {
			// TvDate.setText(item.getJzrq());
			if (type == 1) {
				int headIndex = indexOfHead(index);
				if (headIndex != -1 && HEADTITLE != null) {
					LogUtils.d("ranks", " TvHead=" + (TvHead != null) + " date=" + item.getJzrq()
							+ " state=" + HEADTITLE[headIndex] + " type=" + getItemViewType(index)
							+ "headdex=" + Arrays.toString(HEADINDEX) + " curIndex=" + headIndex);
					TvHead.setText(HEADTITLE[headIndex]);
				}
			}
			TvRank.setText("" + (item.getSortIndex() + 1));
			if (TextUtils.isEmpty(item.getJjmc())) {
				TvTitle.setText("--");
			} else {
				TvTitle.setText(item.getJjmc());
			}
			if (TextUtils.isEmpty(item.getJjdm())) {
				TvCode.setText("--");
			} else {
				TvCode.setText(item.getJjdm());
			}
			//TvCode.setText(item.getJzrq());

			FundUtils.formatFundValue(TvIncrease, item, mSortIndex);
			if (mSortIndex == FundUtils.VALUE_WFSY) {
				TvIncrease.setTextColor(0xff000000);
			}
			if (mExtraIndex != 0) {
				FundUtils.formatFundValue(TvValue, item, mExtraIndex);
			} else {
				TvValue.setText(null);
			}
			CbCollect.setChecked(item.getXunan() >= 1 ? true : false);
			if (checkRecommend(item.getJjdm())) {
				IvGrade.setImageDrawable(getRecomendDrawable());
			} else {
				IvGrade.setImageDrawable(null);
			}
		}
	}

}
