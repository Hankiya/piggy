package com.howbuy.adp;

import howbuy.android.palmfund.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.support.v4.util.SparseArrayCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.howbuy.config.FundConfig;
import com.howbuy.control.CheckableLinerlayout;
import com.howbuy.entity.NetWorthBean;
import com.howbuy.entity.NetWorthListBean;
import com.howbuy.lib.adp.AbsListAdp;
import com.howbuy.lib.adp.AbsViewHolder;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.utils.FundUtils;

public class FundOptionalAdp extends AbsListAdp<NetWorthListBean, NetWorthBean> {
	private static final String DateInputStr = "yyyyMMdd";
	private static final String DateOutStr = "yyyy-M-d";
	private boolean actionModleFlag = false;
	private SparseArrayCompat<Boolean> mCheckList;
 

	public boolean getActionModleFlag() {
		return actionModleFlag;
	}

	public void setActionModleFlag(boolean isShowDragView) {
		if (isShowDragView != this.actionModleFlag) {
			this.actionModleFlag = isShowDragView;
		} else {
			this.actionModleFlag = false;
		}
		this.notifyDataSetChanged();
	}

	public FundOptionalAdp(LayoutInflater lf, NetWorthListBean items) {
		super(lf, items);
		if (mCheckList == null) {
			mCheckList = new SparseArrayCompat<Boolean>();
		}
	}

	public SparseArrayCompat<Boolean> getCheckList() {
		return mCheckList;
	}

	public void setCheckList(SparseArrayCompat<Boolean> mCheck) {
		if (mCheck != null) {
			mCheckList = (SparseArrayCompat<Boolean>) mCheck.clone();
		}
	}

	public void clearCheckList() {
		mCheckList.clear();
		this.notifyDataSetInvalidated();
	}

	public void setItemChecked(int postion, boolean isCheck) {
		mCheckList.put(postion, isCheck);
		this.notifyDataSetChanged();
		printlnCheck();
	}

	public boolean getItemsChecked(int postion) {
		Boolean curr = mCheckList.get(postion);
		return curr == null ? false : curr;
	}

	// *888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888
	public void moveCheckState(int from, int to) {
		SparseArrayCompat<Boolean> cip = mCheckList;
		int rangeStart = from;
		int rangeEnd = to;
		if (to < from) {
			rangeStart = to;
			rangeEnd = from;
		}
		rangeEnd += 1;

		int[] runStart = new int[cip.size()];
		int[] runEnd = new int[cip.size()];
		int runCount = buildRunList(cip, rangeStart, rangeEnd, runStart, runEnd);
		if (runCount == 1 && (runStart[0] == runEnd[0])) {
			// Special case where all items are checked, we can never set any
			// item to false like we do below.
			return;
		}

		if (from < to) {
			for (int i = 0; i != runCount; i++) {
				setItemChecked(rotate(runStart[i], -1, rangeStart, rangeEnd), true);
				setItemChecked(rotate(runEnd[i], -1, rangeStart, rangeEnd), false);
			}

		} else {
			for (int i = 0; i != runCount; i++) {
				setItemChecked(runStart[i], false);
				setItemChecked(runEnd[i], true);
			}
		}
	}

	public void removeCheckState(int position) {
		SparseArrayCompat<Boolean> cip = mCheckList;

		if (cip.size() == 0)
			return;
		int[] runStart = new int[cip.size()];
		int[] runEnd = new int[cip.size()];
		int rangeStart = position;
		int rangeEnd = cip.keyAt(cip.size() - 1) + 1;
		int runCount = buildRunList(cip, rangeStart, rangeEnd, runStart, runEnd);
		for (int i = 0; i != runCount; i++) {
			if (!(runStart[i] == position || (runEnd[i] < runStart[i] && runEnd[i] > position))) {
				// Only set a new check mark in front of this run if it does
				// not contain the deleted position. If it does, we only need
				// to make it one check mark shorter at the end.
				setItemChecked(rotate(runStart[i], -1, rangeStart, rangeEnd), true);
			}
			setItemChecked(rotate(runEnd[i], -1, rangeStart, rangeEnd), false);
		}
	}

	private static int buildRunList(SparseArrayCompat<Boolean> cip, int rangeStart, int rangeEnd,
			int[] runStart, int[] runEnd) {
		int runCount = 0;

		int i = findFirstSetIndex(cip, rangeStart, rangeEnd);
		if (i == -1)
			return 0;

		int position = cip.keyAt(i);
		int currentRunStart = position;
		int currentRunEnd = currentRunStart + 1;
		for (i++; i < cip.size() && (position = cip.keyAt(i)) < rangeEnd; i++) {
			if (!cip.valueAt(i)) // not checked => not interesting
				continue;
			if (position == currentRunEnd) {
				currentRunEnd++;
			} else {
				runStart[runCount] = currentRunStart;
				runEnd[runCount] = currentRunEnd;
				runCount++;
				currentRunStart = position;
				currentRunEnd = position + 1;
			}
		}

		if (currentRunEnd == rangeEnd) {
			// rangeStart and rangeEnd are equivalent positions so to be
			// consistent we translate them to the same integer value. That way
			// we can check whether a run covers the entire range by just
			// checking if the start equals the end position.
			currentRunEnd = rangeStart;
		}
		runStart[runCount] = currentRunStart;
		runEnd[runCount] = currentRunEnd;
		runCount++;

		if (runCount > 1) {
			if (runStart[0] == rangeStart && runEnd[runCount - 1] == rangeStart) {
				// The last run ends at the end of the range, and the first run
				// starts at the beginning of the range. So they are actually
				// part of the same run, except they wrap around the end of the
				// range. To avoid adjacent runs, we need to merge them.
				runStart[0] = runStart[runCount - 1];
				runCount--;
			}
		}
		return runCount;
	}

	private static int rotate(int value, int offset, int lowerBound, int upperBound) {
		int windowSize = upperBound - lowerBound;

		value += offset;
		if (value < lowerBound) {
			value += windowSize;
		} else if (value >= upperBound) {
			value -= windowSize;
		}
		return value;
	}

	private static int findFirstSetIndex(SparseArrayCompat<Boolean> cip, int rangeStart,
			int rangeEnd) {
		int size = cip.size();
		int i = insertionIndexForKey(cip, rangeStart);
		while (i < size && cip.keyAt(i) < rangeEnd && !cip.valueAt(i))
			i++;
		if (i == size || cip.keyAt(i) >= rangeEnd)
			return -1;
		return i;
	}

	private static int insertionIndexForKey(SparseArrayCompat<Boolean> cip, int key) {
		int low = 0;
		int high = cip.size();
		while (high - low > 0) {
			int middle = (low + high) >> 1;
			if (cip.keyAt(middle) < key)
				low = middle + 1;
			else
				high = middle;
		}
		return low;
	}

	// 000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000

	// public void removeCheckState(int postion) {
	// mCheckList.delete(postion);
	// printlnCheck();
	// // 先删除，之后这个postion的item多得加-1
	// for (int i = 0; i < mCheckList.size(); i++) {
	// int temp_postion = mCheckList.keyAt(i);
	// boolean temp_check = mCheckList.valueAt(i);
	// if (temp_postion > postion) {
	// mCheckList.delete(temp_postion);
	// mCheckList.put(temp_postion - 1, temp_check);
	// }
	// }
	// printlnCheck();
	// }

	class Holder extends AbsViewHolder<NetWorthBean> {
		ImageView dragView;
		TextView title, date, netvalue, hbdr;
		CheckableLinerlayout root;

		@Override
		protected void initView(View v, int type) {
			title = (TextView) v.findViewById(R.id.name);
			date = (TextView) v.findViewById(R.id.date);
			dragView = (ImageView) v.findViewById(R.id.drag_handle);
			netvalue = (TextView) v.findViewById(R.id.networth);
			hbdr = (TextView) v.findViewById(R.id.other_tips);
			root = (CheckableLinerlayout) v;
			root.setChecked(false);
		}

		@Override
		protected void initData(int index, int type, NetWorthBean item, boolean isReuse) {
			if (actionModleFlag) {
				boolean isCheck = mCheckList.get(index, false);
				root.setChecked(isCheck);
			} else {
				root.setChecked(false);
			}

			if (!TextUtils.isEmpty(item.getJjmc())) {
				title.setText(item.getJjmc());
			}

			String jjfl = item.getJjfl();
			if (jjfl.equals(FundConfig.TYPE_SIMU)) {
				FundUtils.formatFundValue(netvalue, item, FundUtils.VALUE_JJJZ);
				// FundUtils.formatFundValue(hbdr, item, FundUtils.VALUE_HB1Y);
				netvalue.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				hbdr.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				FundUtils.formatFundValue(hbdr, item.getHb1y(), null, true, FundUtils.VALUE_HB1Y);
			} else if (jjfl.equals(FundConfig.TYPE_HUOBI) || jjfl.equals(FundConfig.TYPE_LICAI)) {
				FundUtils.formatFundValue(netvalue, item, FundUtils.VALUE_WFSY);
				FundUtils.formatFundValue(hbdr, item, FundUtils.VALUE_QRSY);
				netvalue.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_wan, 0, 0, 0);
				hbdr.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_7, 0, 0, 0);
			} else {
				FundUtils.formatFundValue(netvalue, item, FundUtils.VALUE_JJJZ);
				FundUtils.formatFundValue(hbdr, item, FundUtils.VALUE_HBDR);
				netvalue.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				hbdr.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			}

			if (!TextUtils.isEmpty(item.getJzrq())) {
				date.setText(StrUtils.timeFormat(item.getJzrq(), DateInputStr, DateOutStr));
			} else {
				date.setText("--");
			}

			dragView.setVisibility(actionModleFlag ? View.VISIBLE : View.GONE);
		}

		public String formatBaiFen(String _xiaoShu) {
			if (TextUtils.isEmpty(_xiaoShu)) {
				return "";
			}
			NumberFormat formater = null;
			double num = Double.parseDouble(_xiaoShu);
			StringBuffer buff = new StringBuffer();
			buff.append("###,##0.00");
			formater = new DecimalFormat(buff.toString());
			return formater.format(num);
		}
	}

	@Override
	protected View getViewFromXml(int type, ViewGroup p) {
		// TODO Auto-generated method stub
		View v = mLf.inflate(R.layout.com_list_opt_item, p, false);
		return v;
	}

	@Override
	protected AbsViewHolder<NetWorthBean> getViewHolder() {
		return new Holder();
	}

	public void printlnCheck() {
		LogUtils.d("checklist", "--");
		for (int i = 0; i < mCheckList.size(); i++) {
			LogUtils.d("checklist", mCheckList.keyAt(i) + "--" + mCheckList.valueAt(i));
		}
	}

}
