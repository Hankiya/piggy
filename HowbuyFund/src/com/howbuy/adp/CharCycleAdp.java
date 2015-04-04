package com.howbuy.adp;

import java.util.ArrayList;
import java.util.Calendar;

import com.howbuy.entity.CharCycleInf;
import com.howbuy.lib.adp.AbsDataAdp;
import com.howbuy.lib.compont.GlobalApp;

public class CharCycleAdp extends AbsDataAdp<CharCycleInf> {
	private int mEnableCount = 0;
	private int mSelect = -1;

	public CharCycleAdp(ArrayList<CharCycleInf> items) {
		super(items);
	}

	@Override
	public void notifyDataSetChanged() {
		int n = getCount(), enableCount = 0;
		for (int i = 0; i < n; i++) {
			CharCycleInf inf = mItems.get(i);
			if (inf.mEnabled) {
				enableCount++;
				if (inf.mChecked) {
					mSelect = i;
				}
			}
		}
		mEnableCount = enableCount;
		super.notifyDataSetChanged();
	}

	public int getEnableCount() {
		return mEnableCount;
	}

	public int getSelect() {
		return mSelect;
	}

	public void setSelect(int select) {
		mSelect = select;
	}

	/**
	 * @param enablePos
	 *            是在可有的Item集合中的有序索引.
	 * @return 返回对应在所有集合中的索引.
	 */
	public int getEnableIndex(int enablePos) {
		int n = getCount(), k = -1;
		for (int i = 0; i < n; i++) {
			if (mItems.get(i).mEnabled) {
				if (enablePos == (++k)) {
					return i;
				} else {
					if (k >= mEnableCount) {
						break;
					}
				}
			}
		}
		return -1;
	}

	/**
	 * @param index
	 *            是所有集合中的有序索引.
	 * @return 返回对应在可用有序集合中的索引.
	 */
	public int getEnablePos(int index) {
		int n = getCount(), k = -1;
		if (!(index < 0 || index >= n || !mItems.get(index).mEnabled)) {
			for (int i = 0; i < index; i++) {
				if (mItems.get(i).mEnabled) {
					++k;
				}
			}
			++k;
		}
		return k;
	}

	public CharCycleInf getItem(int i, boolean ignoreDisable) {
		if (ignoreDisable) {
			i = getEnableIndex(i);
		}
		if (i >= 0 && i <= getCount()) {
			return getItem(i);
		}
		return null;
	}

	public String getPageTitle(int i) {
		return getItem(i).mCycleName;
	}

	public static CharCycleAdp getDefCharAdp(int aryId, boolean isSimu, long foundDate) {
		CharCycleAdp adp = null;
		if (!isSimu) {
			Calendar ca = Calendar.getInstance();
			String[] s = GlobalApp.getApp().getResources().getStringArray(aryId);
			int n = s == null ? 0 : s.length;
			if (n > 0) {
				adp = new CharCycleAdp(null);
				int cycle = 0;
				for (int i = 0; i < n; ++i) {
					cycle = 1 << i;
					adp.addItem(new CharCycleInf(cycle, s[i], checkEnable(cycle, ca, foundDate)),
							true, false);
				}
			}
		} else {
			adp = new CharCycleAdp(null);
			adp.addItem(new CharCycleInf(CharCycleInf.CYCLE_ALL, null, true), true, false);
		}

		return adp;
	}

	public static boolean checkEnable(int cycle, Calendar ca, long foundDate) {
		ca.setTimeInMillis(System.currentTimeMillis());
		if (cycle == CharCycleInf.CYCLE_ALL || cycle == CharCycleInf.CYCLE_DAY7) {
			return true;// 全部的和7天的都可用.
		} else if (cycle == CharCycleInf.CYCLE_YEAR) {
			ca.set(Calendar.MONTH, 0);
			ca.set(Calendar.DAY_OF_MONTH, 1);// 如果是今年年初前成立的可用.
			return ca.getTimeInMillis() > foundDate;
		} else {
			switch (cycle) {
			case CharCycleInf.CYCLE_MONTH1:
				ca.add(Calendar.MONTH, -1);
				break;
			case CharCycleInf.CYCLE_MONTH3:
				ca.add(Calendar.MONTH, -3);
				break;
			case CharCycleInf.CYCLE_YEAR1:
				ca.add(Calendar.YEAR, -1);
				break;
			}
			// 大于成立时间的区间可用.
			return ca.getTimeInMillis() > foundDate;
		}
	}
}
