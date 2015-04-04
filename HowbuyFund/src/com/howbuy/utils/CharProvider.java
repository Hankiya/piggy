package com.howbuy.utils;

import howbuy.android.palmfund.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.howbuy.adp.CharCycleAdp;
import com.howbuy.config.FundConfig;
import com.howbuy.config.FundConfig.FundType;
import com.howbuy.config.ValConfig;
import com.howbuy.curve.CharValue;
import com.howbuy.datalib.fund.AAParFundChart;
import com.howbuy.db.DbOperat;
import com.howbuy.entity.CharCycleInf;
import com.howbuy.entity.CharRequest;
import com.howbuy.entity.NetWorthBean;
import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.net.AsyPoolTask;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.wireless.entity.protobuf.HistoryFundNetValueChartProtos.HistoryFundNetValueChart;

/**
 * 可认为如果缓存里面有数据,那么数据一定是按最新到最旧排序的.
 */
public class CharProvider {
	public static String TAG = "CharProvider";
	private FundType mType = null;
	private NetWorthBean mBean = null;
	private long mNewTimeMils = 0;
	private String mNewDate = null;
	private final ArrayList<CharValue> mLJjjz = new ArrayList<CharValue>();
	private final ArrayList<CharValue> mLShare = new ArrayList<CharValue>();
	private final ArrayList<CharValue> mLPartition = new ArrayList<CharValue>();
	private String mDateShareFrom = null, mDateShareTo = null;
	private CharCycleAdp mCharAdp = null;
	private CharRequest mFirstNetReq = null;

	public String getShareDate(boolean from) {
		return from ? mDateShareFrom : mDateShareTo;
	}

	public int getBeanDanWei() {
		if (mType != null && mBean != null && mType.isSimu()) {
			String danwei = mBean.getDanWei();
			if (!StrUtils.isEmpty(danwei)) {
				return Integer.parseInt(danwei);
			}
			return 1;
		}
		return 0;
	}

	public void setShareDate(boolean from, String date) {
		if (from) {
			if (mDateShareFrom == null || mDateShareFrom.compareTo(date) > 0) {
				mDateShareFrom = date;
			}
		} else {
			if (mDateShareTo == null || mDateShareTo.compareTo(date) < 0) {
				mDateShareTo = date;
			}
		}
	}

	public CharCycleAdp getCharCycleAdp(boolean isSimu) {
		if (mCharAdp == null) {
			long foundDate = 0;
			String date = mBean.getFoundDate();
			if (!StrUtils.isEmpty(date)) {
				foundDate = StrUtils.getTimeFormatLong(date, ValConfig.DATEF_YMD);
			}
			mCharAdp = CharCycleAdp.getDefCharAdp(R.array.char_cycle, isSimu, foundDate);
		}
		return mCharAdp;
	}

	public String getNewDate() {
		return mNewDate == null ? mBean.getJzrq() : mNewDate;
	}

	public long getNewTimes() {
		if (mNewTimeMils == 0) {
			String newDate = getNewDate();
			if (!StrUtils.isEmpty(newDate)) {
				mNewTimeMils = StrUtils.getTimeFormatLong(newDate, ValConfig.DATEF_YMD);
			}
		}
		return mNewTimeMils;
	}

	public CharProvider(NetWorthBean b) {
		setBean(b);
	}

	public FundType getType() {
		return mType;
	}

	public NetWorthBean getBean() {
		return mBean;
	}

	public void setBean(NetWorthBean b) {
		if (b != null) {
			boolean sameCode = mBean != null && !mBean.getJjdm().equals(b.getJjdm());
			mBean = b;
			if (!sameCode) {
				mType = FundConfig.getConfig().getType(mBean.getJjfl());
				clearnAllCharCahce(false);
				firstQueryDatabase();
			}
		}
	}

	public boolean updateBean(NetWorthBean b, boolean isHuobi) {
		boolean changed = false;
		if (mBean == null) {
			setBean(b);
			changed = true;
		} else {
			b.setJjmc(mBean.getJjmc());
			b.setPinyin(mBean.getPinyin());
			b.setJjfl(mBean.getJjfl());
			b.setJjfl2(mBean.getJjfl2());
			b.setHbTradFlag(mBean.getHbTradFlag());
			b.setMbFlag(mBean.getMbFlag());
			b.setXuanTime(mBean.getXuanTime());
			b.setXunan(mBean.getXunan());
			b.setFoundDate(mBean.getFoundDate());
			b.setDanWei(mBean.getDanWei());
			b.setSortIndex(mBean.getSortIndex());
			if (isHuobi) {
				b.setHbdr(DbOperat.formatWfsyToHbdr(b.getWfsy()));
				b.setJjjz("1");
				b.setLjjz("1");
			} else {

			}
			changed = !b.getJzrq().equals(mBean.getJzrq());
			mBean = b;
		}
		return changed;
	}

	public ArrayList<CharValue> getOperatorList(int valueType) {
		switch (valueType) {
		case CharValue.TYPE_JJJZ:
			return mLJjjz;
		case CharValue.TYPE_DWFH:
			return mLShare;
		case CharValue.TYPE_FCBL:
			return mLPartition;
		default:
			return null;
		}
	}

	// END//////////////////网络请求数据//////////////////

	public void clearnAllCharCahce(boolean cleanListener) {
		mLJjjz.clear();
		mLShare.clear();
		mLPartition.clear();
		if (cleanListener) {
			mListener.clear();
		}
	}

	// START//////////////////通知缓存数据的变法//////////////////
	public void registerCharCacheChanged(ICharCacheChanged l) {
		if (!mListener.contains(l)) {
			mListener.add(l);
		}
	}

	public void unregisterCharCacheChanged(ICharCacheChanged l) {
		mListener.remove(l);
	}

	private void notifyTarget(int from, int cycleType) {
		int n = mListener.size();
		for (int i = 0; i < n; i++) {
			mListener.get(i).onCharCacheChanged(this, from, mLJjjz, cycleType);
		}
	}

	private void notifyTarget(int from, ReqResult<CharRequest> r, int cycleType) {
		int l = mLJjjz.size();
		String dataRange = "";
		if (l > 0) {
			dataRange = " range from " + mLJjjz.get(l - 1).date + " to " + mLJjjz.get(0).date;
		}
		d("notifyTarget", "err from=" + parseFrom(from) + " cycle=" + parseCycle(cycleType)
				+ " new datasize=" + l + dataRange);
		int n = mListener.size();
		for (int i = 0; i < n; i++) {
			mListener.get(i).onCharCacheErr(this, from, r, cycleType);
		}
	}

	private ArrayList<ICharCacheChanged> mListener = new ArrayList<CharProvider.ICharCacheChanged>();

	public interface ICharCacheChanged {
		public static final int FIRST_FROM_DB = 1;
		public static final int FIRST_FROM_NET = 2;
		public static final int FROM_DB = 4;
		public static final int FROM_NET = 8;

		public void onCharCacheChanged(CharProvider charProvider, int from,
				ArrayList<CharValue> val, int cycleType);

		public void onCharCacheErr(CharProvider charProvider, int from, ReqResult<CharRequest> r,
				int cycleType);
	}

	public static String parseFrom(int from) {
		switch (from) {
		case ICharCacheChanged.FIRST_FROM_DB:
			return "FIRST_FROM_DB";

		case ICharCacheChanged.FIRST_FROM_NET:
			return "FIRST_FROM_NET";

		case ICharCacheChanged.FROM_DB:
			return "FROM_DB";

		case ICharCacheChanged.FROM_NET:
			return "FROM_NET";
		default:
			return "CACHE_UNKNOW";
		}
	}

	public static String parseCycle(int cycle) {
		switch (cycle) {
		case CharCycleInf.CYCLE_DAY7:
			return "CYCLE_DAY7";

		case CharCycleInf.CYCLE_MONTH1:
			return "CYCLE_MONTH1";

		case CharCycleInf.CYCLE_MONTH3:
			return "CYCLE_MONTH3";

		case CharCycleInf.CYCLE_YEAR1:
			return "CYCLE_YEAR1";

		case CharCycleInf.CYCLE_YEAR:
			return "CYCLE_YEAR";

		case CharCycleInf.CYCLE_ALL:
			return "CYCLE_ALL";
		default:
			return "CYCLE_UNKNOW";
		}
	}

	// END//////////////////通知缓存数据的变法//////////////////
	final protected void d(String title, String msg) {
		if (title == null) {
			LogUtils.d(TAG, msg);
		} else {
			LogUtils.d(TAG, title + " -->" + msg);
		}
	}

	final protected void dd(String msg, Object... args) {
		d(TAG, String.format(msg, args));
	}

	protected List<CharValue> request(ArrayList<CharValue> res, String from) {
		int[] lows = new int[] { 0 };
		int n = res.size();
		if (searchChar(res, from, 0, n - 1, lows)) {
		}
		if (lows[0] >= n) {
			return res;
		} else {
			if (lows[0] > 0) {
				return res.subList(0, lows[0]);
			}
		}
		return null;
	}

	//from20111111 to20141414
	public static List<CharValue> request(ArrayList<CharValue> res, String from, String to) {
		int n = res == null ? 0 : res.size();
		if (n == 0)
			return null;
		int[] lows = new int[] { -1 };
		int[] higs = new int[] { -1 };
		searchChar(res, from, 0, n - 1, lows);
		searchChar(res, to, 0, Math.min(Math.max(lows[0], 0), n - 1), higs);
		if ((lows[0] == -1 && higs[0] == -1) || (lows[0] == n && higs[0] == n)) {
			return null;
		}
		int low = Math.max(0, Math.min(lows[0], n - 1));
		int hig = Math.max(0, Math.min(higs[0], n - 1));
		if (low < hig) {
			return null;
		}
		return res.subList(hig, low + 1);

	}

	public boolean mergeCache(ArrayList<CharValue> des, ArrayList<CharValue> res, int n) {
		boolean changed = n > 0;
		if (changed) {
			if (des.isEmpty()) {
				des.addAll(res);
			} else {
				changed = false;
				int[] r = new int[] { 0 };
				CharValue item = null;
				for (int i = 0; i < n; i++) {
					item = res.get(i);
					int len = des.size();
					if (!searchChar(des, item.date, 0, len - 1, r)) {
						changed = changed || true;
						if (r[0] == -1) {
							des.add(0, item);
						} else if (r[0] == len) {
							des.add(item);
						}
					}
				}
			}
		}
		return changed;
	}

	protected boolean mergeCache(ArrayList<CharValue> des, ArrayList<CharValue> res, int from,
			int cycleType) {
		StringBuffer sb = new StringBuffer();
		int n = res == null ? 0 : res.size();
		sb.append("from=").append(parseFrom(from)).append(" ,cycle=").append(parseCycle(cycleType))
				.append(",add size=").append(n);
		if (n > 0) {
			sb.append(" range from ").append(res.get(n - 1).date).append(" to ")
					.append(res.get(0).date);
		}
		boolean changed = n > 0;
		if (changed) {
			if (des.isEmpty()) {
				des.addAll(res);
			} else {
				int[] r = new int[] { 0 };
				CharValue item = null;
				for (int i = 0; i < n; i++) {
					item = res.get(i);
					int len = des.size();
					if (!searchChar(des, item.date, 0, len - 1, r)) {
						if (r[0] == -1) {
							des.add(0, item);
						} else if (r[0] == len) {
							des.add(item);
						}
					}
				}
			}
		}
		if (changed) {
			n = mLJjjz.size();
			if (mNewDate == null && from == ICharCacheChanged.FIRST_FROM_NET) {
				if (n > 0) {
					mNewDate = mLJjjz.get(0).date;
					mNewTimeMils = 0;
				}
			}
			sb.append(", total size=").append(n).append(",from ").append(mLJjjz.get(n - 1).date)
					.append(" to ").append(mNewDate);
			d("mergeCache", sb.toString());
			notifyTarget(from, cycleType);

		} else {
			d("mergeCache", sb.toString() + ", not merge changed.");
		}
		return changed;

	}

	public List<CharValue> getChaceList(String date) {
		int n = mLJjjz == null ? 0 : mLJjjz.size();
		int index = checkCacheOffset(mLJjjz, n, date);
		if (index > 0) {
			return mLJjjz.subList(0, Math.min(index, n));
		}
		return null;
	}

	private int checkCacheOffset(ArrayList<CharValue> list, int n, String date) {
		if (n > 0) {
			if (date != null) {
				int res[] = new int[] { 0 };
				if (searchChar(list, date, 0, n - 1, res)) {
					return res[0];
				} else {
					return res[0];
				}
			}
		}
		return n - 1;
	}

	public boolean searchChar(String date, int[] res) {
		if (res == null)
			res = new int[] { 0 };
		return searchChar(mLJjjz, date, 0, mLJjjz.size() - 1, res);
	}

	public CharValue getCharAt(int i) {
		if (i >= 0 && i < mLJjjz.size()) {
			return mLJjjz.get(i);
		}
		return null;
	}

	public static boolean searchChar(List<CharValue> list, String date, int low, int high,
			int res[]) {
		int r = 0, l = low;
		while (low <= high) {
			res[0] = (low + high) / 2;
			r = date.compareTo(list.get(res[0]).date);
			if (r == 0) {
				r = res[0];
				return true;
			}
			if (r > 0) {// 因为是日期比较且排序为逆序.否则应该反过来.
				high = res[0] - 1;
			} else {
				low = res[0] + 1;
			}
		}
		if (low + high < l + l) {
			res[0] = Math.min(low, high);
		} else {
			res[0] = Math.max(low, high);
		}
		return false;
	}

	public List<CharValue> request(CharRequest opt) {
		StringBuffer sb = new StringBuffer();
		sb.append("from=").append(parseCycle(opt.getCycleType())).append(",")
				.append(opt.toString());
		if (mLJjjz.isEmpty()) {
			sb.append(opt.toString());
			d("request", sb.toString());
			return null;
		}
		String last = mLJjjz.get(mLJjjz.size() - 1).date;
		String from = opt.StartTime;
		if (from == null) {
			sb.append(", return size=" + (Math.min(mLJjjz.size(), opt.getCharCount())));
			d("request", sb.toString());
			return mLJjjz.subList(0, Math.min(mLJjjz.size(), opt.getCharCount()));
		} else {
			List<CharValue> r = request(mLJjjz, from);
			int n = r == null ? 0 : r.size();
			if (last.compareTo(from) > 0) {// 缓存数据不够用,从数据库中loaded.
				opt = createCharRequest(opt.getCycleType(), from, last);
				sb.append(", return size=" + n).append(
						", data not enought load from db " + opt.toString());
				d("request", sb.toString());
				new CharDbTask(opt).execute(false);
			} else {
				sb.append(", return size=" + n).append(", data enought from cache ");
				d("request", sb.toString());
			}
			return r;
		}
	}

	public String getOldDate() {
		String date = mBean == null ? null : mBean.getFoundDate();
		if (StrUtils.isEmpty(date)) {
			return null;
		}
		return date;
	}

	private CharRequest createCharRequest(int cycle, String from, String last) {
		Calendar cf = Calendar.getInstance();
		Calendar ct = Calendar.getInstance();
		cf.setTimeInMillis(StrUtils.getTimeFormatLong(from, ValConfig.DATEF_YMD));
		ct.setTimeInMillis(StrUtils.getTimeFormatLong(last, ValConfig.DATEF_YMD));
		ct.add(Calendar.DAY_OF_MONTH, -1);
		int daySpace = (int) ((ct.getTimeInMillis() - cf.getTimeInMillis()) / 86400000l);
		if (daySpace < 90) {// 一次最少拿三个月的数据.
			cf.add(Calendar.DAY_OF_YEAR, daySpace - 90);
			Calendar cm = Calendar.getInstance();
			if (!StrUtils.isEmpty(getOldDate())) {
				cm.setTimeInMillis(StrUtils.getTimeFormatLong(getOldDate(), ValConfig.DATEF_YMD));
				if (cf.getTimeInMillis() > cm.getTimeInMillis()) {
					cf.setTimeInMillis(cm.getTimeInMillis());
				}
			}
		}
		return new CharRequest(cycle,
				StrUtils.timeFormat(cf.getTimeInMillis(), ValConfig.DATEF_YMD),
				StrUtils.timeFormat(ct.getTimeInMillis(), ValConfig.DATEF_YMD));
	}

	private void firstQueryDatabase() {
		int charCount = mType.isSimu() ? 3650 : 365;// 如果是私募希望取出
		new CharDbTask(new CharRequest(charCount)).execute(false);
	}

	public boolean firstQueryNet() {
		if (mNewDate == null && mFirstNetReq != null) {
			d("firstQueryNet", "from=" + parseCycle(mFirstNetReq.getCycleType()) + ","
					+ mFirstNetReq.toString());
			new QueryNetTask(mFirstNetReq).execute(false);
			return true;
		}
		return false;
	}

	public boolean hasFirstQureyDb() {
		return mFirstNetReq != null;
	}

	public boolean hasFirstQuryNet() {
		return mNewDate != null;
	}

	// START//////////////////本地查询请求数据//////////////////
	class CharDbTask extends AsyPoolTask<Void, Void, ReqResult<CharRequest>> {
		private ReqResult<CharRequest> mResult = null;

		public CharDbTask(CharRequest opt) {
			mResult = new ReqResult<CharRequest>(opt);
		}

		@Override
		protected ReqResult<CharRequest> doInBackground(Void... p) {
			try {
				ArrayList<ArrayList<CharValue>> data = CharValue.load(mResult.mReqOpt,
						mBean.getJjdm(), CharValue.TYPE_JJJZ);
				mResult.setData(data);
			} catch (WrapException e) {
				mResult.setErr(e);
			}
			return mResult;
		}

		@Override
		protected void onPostExecute(ReqResult<CharRequest> r) {
			int from = mLJjjz.size() > 0 ? ICharCacheChanged.FROM_DB
					: ICharCacheChanged.FIRST_FROM_DB;
			int cycle = r.mReqOpt.getCycleType();
			StringBuffer sb = new StringBuffer();
			sb.append("from=").append(parseFrom(from)).append(" ,cycle=").append(parseCycle(cycle));
			if (r.isSuccess()) {
				if (r.mData != null) {
					mergeCache(mLJjjz, ((ArrayList<ArrayList<CharValue>>) r.mData).get(0), from,
							r.mReqOpt.getCycleType());
					if (mFirstNetReq == null) {
						mFirstNetReq = new CharRequest(cycle, mLJjjz.get(0).date, null);
						firstQueryNet();
					} else {
						String last = mLJjjz.get(mLJjjz.size() - 1).date;
						if (last.compareTo(r.mReqOpt.StartTime) > 0) {// 缓存数据不够用,从数据库中loaded.
							r.mReqOpt = createCharRequest(r.mReqOpt.getCycleType(),
									r.mReqOpt.StartTime, last);
							sb.append(",load from db  not enought ,query net opt="
									+ r.mReqOpt.toString());
							new QueryNetTask(r.mReqOpt).execute(false);
						} else {
							sb.append(",load from db  enought ");
						}
					}

				} else {
					sb.append(",load from db empty query net.. " + r.mReqOpt.toString());
					if (from == ICharCacheChanged.FIRST_FROM_DB) {
						mFirstNetReq = r.mReqOpt;
						firstQueryNet();
					} else {
						new QueryNetTask(r.mReqOpt).execute(false);
					}

				}
				d("CharDbTask", sb.toString());
			} else {
				d("CharDbTask", "load from db failed ,err=" + r.mErr);
				notifyTarget(from, r, cycle);
			}
		}
	}

	// END//////////////////本地查询请求数据//////////////////

	// START//////////////////网络请求数据//////////////////
	private AAParFundChart mRequester = null;

	private AAParFundChart getRequester(CharRequest req) {
		if (mRequester == null) {
			mRequester = new AAParFundChart(0);
		}
		return mRequester.setParams(mBean.getJjdm(), mType.isSimu() ? "1" : "0", req.StartTime,
				req.EndTime, req.getCharCount());
	}

	class QueryNetTask extends AsyPoolTask<Void, Void, ReqResult<CharRequest>> {
		private ReqResult<CharRequest> mResult = null;
		private int mCycleType = 0;

		public QueryNetTask(CharRequest opt) {
			mCycleType = opt.getCycleType();
			mResult = new ReqResult<CharRequest>(opt);
		}

		@Override
		protected ReqResult<CharRequest> doInBackground(Void... params) {
			int n = mLJjjz.size();
			int offset = mCycleType == 0 ? -1 : checkCacheOffset(mLJjjz, n,
					mResult.mReqOpt.StartTime);
			if (offset != -1 && offset < n) {
				d("QueryNetTask", parseCycle(mCycleType) + ",have date in cache opt="
						+ mResult.mReqOpt + ",offset=" + offset + " mCycleType=" + mCycleType);
				mResult.setData(null);
				return mResult;
			}

			ReqResult<ReqNetOpt> r = getRequester(mResult.mReqOpt).execute(true);
			if (r.isSuccess()) {
				HistoryFundNetValueChart fps = (HistoryFundNetValueChart) r.mData;
				if (fps != null) {
					ArrayList<ArrayList<CharValue>> data = CharValue.save(fps, mBean.getJjdm());
					mResult.setData(data);
				} else {
					mResult.setErr(new WrapException("NullPointException",
							"request HistoryFundNetValueChart from net is null"));
				}
			} else {
				mResult.setErr(r.mErr);
			}
			return mResult;
		}

		@Override
		protected void onPostExecute(ReqResult<CharRequest> r) {
			int from = mNewDate == null ? ICharCacheChanged.FIRST_FROM_NET
					: ICharCacheChanged.FROM_NET;
			int cycle = r.mReqOpt.getCycleType();
			StringBuffer sb = new StringBuffer();
			sb.append("from=").append(parseFrom(from)).append(" ,cycle=").append(parseCycle(cycle));
			if (r.isSuccess()) {
				if (r.mData != null) {
					boolean merged = mergeCache(mLJjjz,
							((ArrayList<ArrayList<CharValue>>) r.mData).get(0), from, cycle);
				} else {
					sb.append(",query net no date");
					d("QueryNetTask", sb.toString());
					if (mNewDate == null) {
						if (mLJjjz.size() > 0) {
							mNewDate = mLJjjz.get(0).date;
							mNewTimeMils = 0;
						} else {
							mNewDate = StrUtils.timeFormat(System.currentTimeMillis(),
									ValConfig.DATEF_YMD);
						}
						notifyTarget(from, cycle);
					}
				}
			} else {
				sb.append(",query net err " + r.mErr);
				d("QueryNetTask", sb.toString());
				if (mNewDate == null) {
					notifyTarget(from, r, 0);
				} else {
					notifyTarget(from, r, cycle);
				}

			}
		}
	}

}
