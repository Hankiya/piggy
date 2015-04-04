package com.howbuy.frag;

import howbuy.android.palmfund.R;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.TextView;

import com.howbuy.config.Analytics;
import com.howbuy.config.ValConfig;
import com.howbuy.curve.CharData;
import com.howbuy.curve.CharValue;
import com.howbuy.curve.CurveSetting;
import com.howbuy.curve.FunCharType;
import com.howbuy.curve.ICharType;
import com.howbuy.curve.TouchCurveView;
import com.howbuy.curve.TouchCurveView.ITouchObserver;
import com.howbuy.entity.CharCycleInf;
import com.howbuy.entity.CharRequest;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.frag.AbsHbFrag;
import com.howbuy.lib.net.AsyPoolTask;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.lib.utils.ViewUtils;
import com.howbuy.utils.CharProvider;
import com.howbuy.utils.CharProvider.ICharCacheChanged;

public class FragCharLand extends AbsHbFrag implements ICharCacheChanged, ITouchObserver,
		OnPageChangeListener {
	public static int TOUCH_lINE_ONE = 1;
	public static int TOUCH_lINE_TWO = 2;
	private int mTouchType = 0;
	private int mCycle = 0;
	private int mDanWei=1;
	private FragDetailsFundLand mTarget = null;
	private View mProgress;
	private TouchCurveView mCurveSfView;// 图表
	private ArrayList<CharData> mCharData = new ArrayList<CharData>();
	private CharRequest mCharOpt = null;
	private CharProvider mCharProvider = null;
	private TextView mTvDate[];
	private static String TIMEFORMAT = "yyyy年M月d日";
	private ICharType mCharType = new FunCharType();

	@Override
	public void onResume() {
		super.onResume();
		mCharProvider.registerCharCacheChanged(this);
		if (mTarget != null) {
			mTarget.addPageListener(this);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		mCharProvider.unregisterCharCacheChanged(this);
		if (mTarget != null) {
			mTarget.removePageListener(this);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mCurveSfView.destory(false);
	}

	private void adjustBeizer(int n) {
		float bezier = 0.2f;
		if (n < 10) {
			bezier = 0.4f;
		} else if (n < 30) {
			bezier = 0.5f;
		} else if (n < 100) {
			bezier = 0.6f;
		} else if (n < 150) {
			bezier = 0.7f;
		} else if (n < 200) {
			bezier = 0.8f;
		} else {
			bezier = 0.9f;
		}
		mCurveSfView.getSetting().setBezierSmoonth(bezier);
	}

	private void applyCharData() {
		showProgress(false);
		int oldSize = 0;
		try {
			oldSize = mCurveSfView.getCurveData(mCharType).size();
		} catch (Exception e) {
		}
		mCurveSfView.cleanCurve(true, true);
		if (oldSize != 0) {
			mCurveSfView.getSetting().setEnableAutoCalcuMinMax(true);
		}
		int n = mCharData.size();
		adjustBeizer(n);
		mCurveSfView.addCurve(mCharType, mCharData);
		if (n >= 7) {
			updateCoordX(mTvDate, n, 6);
		} else {
			if (n > 1) {
				TextView[] tvs = new TextView[n];
				tvs[n - 1] = mTvDate[6];
				int j = 0;
				for (int i = 0; i < 6; i++) {
					if (j < n - 1) {
						tvs[j] = mTvDate[i];
						j++;
					} else {
						ViewUtils.setVisibility(mTvDate[i], View.GONE);
					}
				}
				updateCoordX(tvs, n, n - 1);
			} else {
				for (int i = 0; i < 7; i++) {
					ViewUtils.setVisibility(mTvDate[i], View.GONE);
				}
				if (n == 1) {
					ViewUtils.setVisibility(mTvDate[1], View.VISIBLE);
					CharData cd = mCharData.get(0);
					mTvDate[1].setText(StrUtils.timeFormat(cd.getTime(),
							adjustFormat(null, cd.getTime(null))));
				}
			}
		}
	}

	private void updateCoordX(TextView[] tvs, int n, int l) {
		float dn = (n - 1) / (float) l;
		CharData cd = null;
		String preDate = null;
		String curDate = null;
		int k = n - 1;
		// d("applyCharData", "n=" + n + " , dn=" + dn + " cur_cycle=" +
		// CharProvider.parseCycle(mCycle));f
		for (int i = 0; i < l; i++) {
			k = Math.round(n - 1 - dn * i);
			cd = mCharData.get(k);
			curDate = cd.getTime(null);
			tvs[i].setText(StrUtils.timeFormat(cd.getTime(), adjustFormat(preDate, curDate)));
			ViewUtils.setVisibility(tvs[i], View.VISIBLE);
			preDate = curDate;
		}
		cd = mCharData.get(0);
		curDate = cd.getTime(null);
		tvs[l].setText(StrUtils.timeFormat(cd.getTime(), adjustFormat(preDate, curDate)));
		ViewUtils.setVisibility(tvs[l], View.VISIBLE);
		// d("applyCharData", "k=" + 0 + " cur_cycle=" +
		// CharProvider.parseCycle(mCycle));
	}

	private String adjustFormat(String preDate, String curDate) {
		if (preDate == null) {
			return "M月d日";
		} else {
			String y = preDate.substring(0, 4);
			if (!curDate.startsWith(y)) {// year difs.
				return "yyyy年M月";
			} else {
				String preM = preDate.substring(4, 6);
				String curM = curDate.substring(4, 6);
				if (curM.equals(preM)) {// month same.
					return "d";
				} else {
					return "M月";
				}
			}
		}
	}

	private void parseBundle(Bundle bundle) {
		if (bundle != null) {
			mCycle = bundle.getInt(ValConfig.IT_TYPE);
			android.support.v4.app.Fragment fgg = getTargetFragment();
			if (!(fgg instanceof FragDetailsFundLand)) {
				fgg = getParentFragment();
			}
			if (fgg instanceof FragDetailsFundLand) {
				mTarget = (FragDetailsFundLand) getTargetFragment();
			} else {
				if (getSherlockActivity() != null) {
					getSherlockActivity().finish();
				} else {
					return;
				}
			}
			mCharProvider = mTarget.getPageChar().getCharProvider();
			mCharProvider.registerCharCacheChanged(this);
			refush(false);
		}
	}

	private void refush(boolean fromErrRefush) {

		if (mCharData.size() == 0) {
			showProgress(true);
		}
		if (mCharProvider.hasFirstQureyDb()) {
			List<CharValue> cache = mCharProvider.request(getRequest());
			/*
			 * d("refush", "cycle=" + CharProvider.parseCycle(mCycle) +
			 * " , mCharOpt=" + mCharOpt + " cache size=" + (cache == null ? 0 :
			 * cache.size()));
			 */
			if (cache != null && cache.size() > 0) {
				if (isAdded()) {
					mDanWei=mCharProvider.getBeanDanWei();
					CharData.initCharData(mCharData, cache, mCharProvider.getType().isHuobi(),
							mDanWei);
					applyCharData();
				}
			}
		}

	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		if (bundle != null) {
			mCycle = bundle.getInt(ValConfig.IT_TYPE);
		}
		mCurveSfView = (TouchCurveView) root.findViewById(R.id.chartview);
		mProgress = root.findViewById(R.id.pb_char);
		mTvDate = new TextView[7];
		mTvDate[0] = (TextView) root.findViewById(R.id.tv_date1);
		mTvDate[1] = (TextView) root.findViewById(R.id.tv_date2);
		mTvDate[2] = (TextView) root.findViewById(R.id.tv_date3);
		mTvDate[3] = (TextView) root.findViewById(R.id.tv_date4);
		mTvDate[4] = (TextView) root.findViewById(R.id.tv_date5);
		mTvDate[5] = (TextView) root.findViewById(R.id.tv_date6);
		mTvDate[6] = (TextView) root.findViewById(R.id.tv_date7);
		// mCurveSfView.setBackGroudColor(0);
		// mCurveSfView.setCurveEvent(this);
		CurveSetting set = mCurveSfView.getSetting();
		set.setCurveSize(1.5f);
		set.setCoordUpBotWeight(0.05f, 0.1f);
		set.setBezierSmoonth(0.4f);
		set.setEnableInitShowAll(true);
		set.setMinMaxSpace(0.01f, 10000);
		set.setEnableTxtYInSide(false);
		set.setEnableCoordX(false);
		set.setEnableCoordY(false);
		set.setEnableGridDash(false);
		set.setCoordTxtYColor(0xff999999);
		set.setGridColor(0xffebebeb);
		set.setCrossColor(0xffff0000);
		set.setEnableInitOffset(false);
		set.setCurveSize(1.5f);
		set.setGridSize(0.5f);
		set.setCoordTxtYSize(12);
		set.setCoordTxtWidth(0.3f);
		set.setEnableAutoCalcuMinMax(false);
		parseBundle(getArguments());
		showProgress(false);
		mCurveSfView.setTouchObverser(this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(ValConfig.IT_TYPE, mCycle);
	}

	void showProgress(boolean show) {
		if (show) {
			if (mProgress.getVisibility() != View.VISIBLE) {
				mProgress.setVisibility(View.VISIBLE);
			}
		} else {
			if (mProgress.getVisibility() == View.VISIBLE) {
				mProgress.setVisibility(View.GONE);
			}
		}
	}

	@Override
	protected int getFragLayoutId() {
		return R.layout.com_page_details_char_land;
	}

	private CharRequest getRequest() {
		if (mCharOpt == null) {
			mCharOpt = CharRequest.getRequest(mCycle, mCharProvider);
		}
		return mCharOpt;
	}

	@Override
	public void onCharCacheChanged(CharProvider charProvider, int from, ArrayList<CharValue> r,
			int cycleType) {
		if (isAdded()) {
			if (from == FIRST_FROM_NET) {
				mCharOpt = null;
			}
			List<CharValue> cache = mCharProvider.request(getRequest());
			/*
			 * d("onCharCacheChanged", " from=" + CharProvider.parseFrom(from) +
			 * ",cycleType=" + CharProvider.parseCycle(cycleType) + ",r.size=" +
			 * r.size() + " mDataEnough=" + mDataEnough + " cur_cycle=" +
			 * CharProvider.parseCycle(mCycle) + " cache size=" + (cache == null
			 * ? 0 : cache.size()) + " new Date=" + mCharProvider.getNewDate());
			 */
			if (cache != null && cache.size() > 0) {
				mDanWei=mCharProvider.getBeanDanWei();
				CharData.initCharData(mCharData, cache, mCharProvider.getType().isHuobi(),
						mDanWei);
				applyCharData();
				showProgress(false);
			}
		}
	}

	@Override
	public void onCharCacheErr(CharProvider charProvider, int from, ReqResult<CharRequest> r,
			int cycleType) {
		if (isAdded()) {
			showProgress(false);
			/*
			 * d("onCharCacheErr", "from=" + CharProvider.parseFrom(from) +
			 * ",cycleType=" + CharProvider.parseCycle(cycleType) + ",r=" + r +
			 * " cur_cycle=" + CharProvider.parseCycle(mCycle));
			 */
			if (GlobalApp.getApp().getNetType() > 1 && isVisible()) {
				if (from == FROM_NET) {
					// refush(true);
				} else if (from == FIRST_FROM_NET) {
					// mCharProvider.firstQueryNet();
				}
			}
		}

	}

	private void setViewPageEnable(boolean enable) {
		mTarget.getPageChar().getViewPage().setCanHScroll(enable);
	}

	public void setCharInf(String date, String value, String increase) {
		mTarget.setBarText(date, value, increase);
	}

	@Override
	public void onTouchObserver(boolean focused, int touchCount) {
		// d("onTouchObserver", "focused=" + focused + " , touchCount=" +
		// touchCount);
		if (focused && mCharProvider.getShareDate(true) == null) {
			checkShareValue(true, "onTouchObserver focused");
		}
		setViewPageEnable(!focused);
		if (!focused && touchCount == 1) {
			mCurveSfView.cleanTouchSign();
			setCharInf(null, null, null);
		} else if (focused) {
			int a = mCurveSfView.getSelect1();
			int b = mCurveSfView.getSelect2();
			if (touchCount == 1 || a == b) {
				if (a >= 0 && a < mCharData.size()) {
					onTouchOne(a);
				}
			} else {
				if (a >= 0 && b >= 0 && a < mCharData.size() && b < mCharData.size()) {
					onTouchTwo(Math.max(a, b), Math.min(a, b));
				}
			}
		}
	}

	private void onTouchOne(int s) {
		if (mTouchType != TOUCH_lINE_ONE) {
			mTouchType = TOUCH_lINE_ONE;
			Analytics.onEvent(getSherlockActivity(), Analytics.ONE_FINGER_TOUCH_EVENT);
		}
		CharData d = mCharData.get(s);
		CharValue v = mCharData.get(s).getCharValue();
		setCharInf(StrUtils.timeFormat(d.getTime(), TIMEFORMAT), v.value, v.increase);
	}

	private void onTouchTwo(int a, int b) {
		if (mTouchType != TOUCH_lINE_TWO) {
			mTouchType = TOUCH_lINE_TWO;
			Analytics.onEvent(getSherlockActivity(), Analytics.TWO_FINGER_TOUCH_EVENT);
		}
		CharData ac = mCharData.get(a);
		CharData bc = mCharData.get(b);
		String increase = null;
		if (mCharProvider.getType().isHuobi()) {
			increase = calculateIncome(a, b);
		} else {
			increase = calculateIncome(ac, bc);
		}
		String date = StrUtils.timeFormat(ac.getTime(), TIMEFORMAT) + " 至 "
				+ StrUtils.timeFormat(bc.getTime(), TIMEFORMAT);
		setCharInf(date, null, increase);
	}

	private String calculateIncome(int a, int b) {
		if (a == b) {
			return mCharData.get(a).getCharValue().increase;
		} else {
			float sum = 1, temp = 1;
			for (int i = b; i <= a; i++) {
				try {
					temp = (1 + Float.parseFloat(mCharData.get(i).getCharValue().value) / 10000f);
				} catch (Exception e) {
					e.printStackTrace();
					temp = 1;
				}
				sum *= temp;
			}
			return (sum - 1) * 100 + "";
		}
	}

	private String calculateIncome(CharData ac, CharData bc) {
		String from = ac.getTime(null);
		String to = bc.getTime(null);
		float va = ac.getValue(0);
		float vb = bc.getValue(0);
		List<CharValue> dwfh = CharProvider.request(
				mCharProvider.getOperatorList(CharValue.TYPE_DWFH), from, to);
		List<CharValue> fcbl = CharProvider.request(
				mCharProvider.getOperatorList(CharValue.TYPE_FCBL), from, to);
		int dwfhN = dwfh == null ? 0 : dwfh.size();
		int fcblN = fcbl == null ? 0 : fcbl.size();
		if (dwfhN > 0 || fcblN > 0) {
			vb = calculateIncome(dwfh, 0, dwfhN, fcbl, 0, fcblN, vb);
		}
		return "" + (vb - va) * 100 / va;
	}

	private float calculateIncome(List<CharValue> dwfh, int dwfhOffset, int dwfhN,
			List<CharValue> fcbl, int fcblOffset, int fcblN, float vb) {
		boolean hasFcbl = fcbl == null ? false : fcbl.size() > fcblOffset;
		if (hasFcbl) {
			float fb = 1;
			String fbstr = fcbl.get(fcblOffset).value;
			if (!StrUtils.isEmpty(fbstr)) {
				try {
					fb = Float.parseFloat(fbstr);
				} catch (Exception e) {
					e.printStackTrace();
					fb = 1;
				}
			}
			int fcblIndwfh[] = new int[] { 0 };
			CharProvider.searchChar(dwfh, fcbl.get(fcblOffset).date, dwfhOffset, dwfhN - 1,
					fcblIndwfh);
			if (fcblIndwfh[0] < dwfhOffset) {
				fcblIndwfh[0] = dwfhOffset;
			}
			vb = calculatePartion(dwfh, dwfhOffset, fcblIndwfh[0], vb) * fb;
			return calculateIncome(dwfh, fcblIndwfh[0], dwfhN, fcbl, fcblOffset + 1, fcblN, vb);
		} else {
			return calculatePartion(dwfh, dwfhOffset, dwfhN, vb);
		}
	}

	private float calculatePartion(List<CharValue> dwfh, int start, int end, float vb) {
		float r = vb;
		while (start < end) {
			try {
				r += Float.parseFloat(dwfh.get(start++).value)/mDanWei;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return r;
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		boolean stopScroll = state == 0;
		mCurveSfView.setEnabled(stopScroll);
		if (stopScroll) {
			// d("onPageScrollStateChanged", "state=" + state +
			// " , mCurveSfView.enable=" + stopScroll);
			setViewPageEnable(true);
		}
	}

	@Override
	public void onPageScrolled(int arg0, float rate, int arg2) {

	}

	@Override
	public void onPageSelected(int cur) {
		CharCycleInf inf = mTarget.getPageChar().getCycleInf(cur);
		if (inf != null) {
			if (inf.mCycleType != mCycle) {
				mCurveSfView.cleanTouchSign();
				mTouchType = 0;
			} else {
				checkShareValue(true, "onPageSelected cur=" + cur);
			}
		}
	}

	private boolean checkShareValue(boolean forceLoadIfNeed, String tag) {
		boolean shouldLoadedShare = false;
		int n = mCharData == null ? 0 : mCharData.size();
		if (n > 0) {
			String shareFrom = mCharProvider.getShareDate(true);
			String shareTo = mCharProvider.getShareDate(false);
			String dataFrom = mCharData.get(n - 1).getTime(null);
			String dataTo = mCharData.get(0).getTime(null);
			try {
				shouldLoadedShare = (shareFrom == null || shareTo == null
						|| shareFrom.compareTo(dataFrom) > 0 || shareTo.compareTo(dataTo) < 0);
			} catch (Exception e) {
				shouldLoadedShare = false;
			}
			d("checkShareValue", "forceLoadIfNeed=" + forceLoadIfNeed + ",shouldLoadedShare="
					+ shouldLoadedShare + " tag=" + tag);
			if (shouldLoadedShare && forceLoadIfNeed) {
				new ShareTask(new CharRequest(mCycle, dataFrom, dataTo)).execute(false);
			}
		}
		return shouldLoadedShare;
	}

	// START//////////////////本地查询请求数据//////////////////
	class ShareTask extends AsyPoolTask<Void, Void, ReqResult<CharRequest>> {
		private ReqResult<CharRequest> mResult = null;

		public ShareTask(CharRequest opt) {
			mResult = new ReqResult<CharRequest>(opt);
		}

		@Override
		protected ReqResult<CharRequest> doInBackground(Void... p) {
			try {
				if (checkShareValue(false, "ShareTask")) {
					ArrayList<ArrayList<CharValue>> data = CharValue.load(mResult.mReqOpt,
							mCharProvider.getBean().getJjdm(), CharValue.TYPE_DWFH,
							CharValue.TYPE_FCBL);
					boolean changed = false;
					if (data != null) {
						ArrayList<CharValue> dwfh = data.get(1);
						ArrayList<CharValue> fcbl = data.get(2);
						int n = dwfh.size();
						if (n > 0) {
							changed = changed
									|| mCharProvider.mergeCache(
											mCharProvider.getOperatorList(CharValue.TYPE_DWFH),
											dwfh, n);
						}
						n = fcbl.size();
						if (n > 0) {
							changed = changed
									|| mCharProvider.mergeCache(
											mCharProvider.getOperatorList(CharValue.TYPE_FCBL),
											fcbl, n);
						}
					}
					CharRequest opt = mResult.mReqOpt;
					mCharProvider.setShareDate(true, opt.StartTime);
					mCharProvider.setShareDate(false, opt.EndTime);
					mResult.setData(changed);
				} else {
					mResult.setData(null);
				}
			} catch (WrapException e) {
				mResult.setErr(e);
			}
			return mResult;
		}

		@Override
		protected void onPostExecute(ReqResult<CharRequest> r) {
			if (r.isSuccess() && r.mData != null) {
				boolean changed = (Boolean) r.mData;
				StringBuffer sb = new StringBuffer();
				sb.append("loaded dwfh and fcbl from ").append(mCharProvider.getShareDate(true));
				sb.append(" to ").append(mCharProvider.getShareDate(false))
						.append(",dataset changed=").append(changed);
				if (changed) {
					ArrayList<CharValue> dwfh = mCharProvider.getOperatorList(CharValue.TYPE_DWFH);
					ArrayList<CharValue> fcbl = mCharProvider.getOperatorList(CharValue.TYPE_FCBL);
					if (dwfh.size() > 0) {
						sb.append("\n\tTYPE_DWFH:").append(dwfh);
					}
					if (fcbl.size() > 0) {
						sb.append("\n\tTYPE_FCBL:").append(fcbl);
					}
				}
				d("ShareTask", sb.toString());
			}
		}

	}

}