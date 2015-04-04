package com.howbuy.control;

import howbuy.android.palmfund.R;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.howbuy.component.CardDrawable;
import com.howbuy.config.ValConfig;
import com.howbuy.curve.CharData;
import com.howbuy.curve.CharValue;
import com.howbuy.curve.SimpleChartView;
import com.howbuy.datalib.fund.AAParFundInfoByType;
import com.howbuy.db.DbOperat;
import com.howbuy.db.DbUtils;
import com.howbuy.entity.CharRequest;
import com.howbuy.entity.NetWorthBean;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.AsyPoolTask;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.SysUtils;
import com.howbuy.lib.utils.ViewUtils;
import com.howbuy.utils.CharProvider;
import com.howbuy.utils.CharProvider.ICharCacheChanged;
import com.howbuy.utils.FundUtils;
import com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundInfo;
import com.howbuy.wireless.entity.protobuf.FundInfoByTypeProtos.FundList;

public class HomeTopView implements IReqNetFinished, ICharCacheChanged {
	public static final int FIXED_TOP_NUM = 2;
	public static final int TOP_SCALE = 1;
	public static final int TOP_RECOMMAND = 2;
	private int mRealNum = -1;
	private int mType = 0;
	public View[] mRoot = null;
	public TextView mTvTitles[], mTvIncreases[], mTvGroup;
	public ImageView mIvGroup = null;
	public CheckBox mCbCollects[];
	public SimpleChartView mSChar1, mSChar2;
	public View mProgress[];
	ArrayList<NetWorthBean> mListFund = null;
	List<FundList> mList = null;
	ArrayList<CharData> mCharData1 = null;
	ArrayList<CharData> mCharData2 = null;
	private CharProvider mCharProvider1, mCharProvider2;
	private static CharRequest mCharRequest;
	private AbsParam mPar = null;

	public CharRequest getchRequest() {
		if (mCharRequest == null) {
			mCharRequest = new CharRequest(265);
		}
		return mCharRequest;
	}

	private CharProvider getCharProvider(boolean first, NetWorthBean b) {
		if (first) {
			if (mCharProvider1 == null) {
				mCharProvider1 = new CharProvider(b);
				mCharProvider1.registerCharCacheChanged(this);
			}
			return mCharProvider1;
		} else {
			if (mCharProvider2 == null) {
				mCharProvider2 = new CharProvider(b);
				mCharProvider2.registerCharCacheChanged(this);
			}
			return mCharProvider2;
		}
	}

	private int createTagInt(int i) {
		return mType * FIXED_TOP_NUM + i;
	}

	private int indexOfFund(String code) {
		if (mListFund != null) {
			for (int i = 0; i < mRealNum; i++) {
				if (mListFund.get(i).getJjdm().equals(code)) {
					return i;
				}
			}
		}
		return -1;
	}

	public void checkOptional() {
		if (mListFund != null && mRealNum > 0) {
			new AsyFundTaks(false).execute(false);
		}
	}

	public NetWorthBean getFund(int i) {
		if (i < 0 || i >= mListFund.size()) {
			return null;
		}
		return mListFund.get(i);
	}

	public int parsePosition(int i) {
		if (i / FIXED_TOP_NUM == mType) {
			return i % FIXED_TOP_NUM;
		}
		return -1;
	}

	public int setChecked(String code, int xun) {
		int i = indexOfFund(code);
		if (i != -1) {
			boolean checked = xun >= 1;
			mListFund.get(i).setXunan(xun);
			mCbCollects[i].setChecked(checked);
			return checked ? 1 : -1;
		} else {
			return 0;
		}
	}

	public HomeTopView(int type, View root) {
		mType = type;
		mProgress = new View[2];
		mRoot = new View[FIXED_TOP_NUM];
		mTvTitles = new TextView[FIXED_TOP_NUM];
		mTvIncreases = new TextView[FIXED_TOP_NUM];
		mCbCollects = new CheckBox[FIXED_TOP_NUM];
		resetupView(type, root);
		requestTopList(false);
	}

	public void refush(boolean force) {
		if (mListFund == null || force) {
			requestTopList(force);
		} else {
			if (mCharData1 == null) {
				getCharProvider(true, null).request(getchRequest());
			}
			if (mCharData2 == null) {
				getCharProvider(true, null).request(getchRequest());
			}
		}
	}

	private void requestTopList(boolean force) {
		if (mPar == null) {
			mPar = new AAParFundInfoByType(CacheOpt.TIME_WEEK).setParams(mType)
					.setCallback(1, this);
			mPar.getReqOpt(false).addFlag(ReqNetOpt.FLAG_CACHE_THEN_REQ);
		}
		if (force) {
			mPar.getReqOpt(false).addFlag(ReqNetOpt.FLAG_FORCE_UNCACHE);
		} else {
			mPar.getReqOpt(false).subFlag(ReqNetOpt.FLAG_FORCE_UNCACHE);
		}
		mPar.execute();
	}

	public void resetupView(int type, final View root) {
		CardDrawable cd = new CardDrawable(0xffffffff);
		cd.setShadeWidth(0, 0, 0, 2 * SysUtils.getDensity(root.getContext()));
		ViewUtils.setBackground(root, cd);
		mTvGroup = (TextView) root.findViewById(R.id.tv_group);
		mIvGroup = (ImageView) root.findViewById(R.id.iv_group);
		mSChar1 = findCharViews(0, root.findViewById(R.id.lay_top_char1));
		mSChar2 = findCharViews(1, root.findViewById(R.id.lay_top_char2));
		mSChar1.setCurveSize(1f);
		mSChar2.setCurveSize(1f);
		String title = GlobalApp.getApp().getString(
				mType == 1 ? R.string.home_title_topsales : R.string.home_title_recommend);
		mTvGroup.setText(title);
		if (mCharData1 == null) {
			ViewUtils.setVisibility(mProgress[0], View.VISIBLE);
		} else {
			mSChar1.setData(mCharData1);
		}
		if (mCharData2 == null) {
			ViewUtils.setVisibility(mProgress[1], View.VISIBLE);
		} else {
			mSChar2.setData(mCharData2);
		}
		int icon = (type == TOP_SCALE) ? R.drawable.ic_hot : R.drawable.ic_praise;
		mIvGroup.setImageResource(icon);
	}

	private SimpleChartView findCharViews(int i, View v) {
		mRoot[i] = v;
		mTvTitles[i] = (TextView) v.findViewById(R.id.tv_title);
		mTvIncreases[i] = (TextView) v.findViewById(R.id.tv_increase);
		mCbCollects[i] = (CheckBox) v.findViewById(R.id.cb_collect);
		mProgress[i] = v.findViewById(R.id.pb_home_char);
		mCbCollects[i].setEnabled(false);
		mRoot[i].setEnabled(false);
		return (SimpleChartView) v.findViewById(R.id.chartview);
	}

	private void initListData(boolean updateAll) {
		for (int i = 0; i < mRealNum; ++i) {
			NetWorthBean item = mListFund.get(i);
			if (updateAll) {
				mTvTitles[i].setText(item.getJjmc());
				FundUtils.formatFundValue(mTvIncreases[i], item, FundUtils.VALUE_HB1N);
				int tagId = createTagInt(i);
				mRoot[i].setTag(tagId);
				mCbCollects[i].setTag(tagId);
				mCbCollects[i].setEnabled(true);
				mRoot[i].setEnabled(true);
			}
			mCbCollects[i].setChecked(item.getXunan() >= 1);
		}
		if (updateAll) {
			getCharProvider(true, mListFund.get(0)).request(getchRequest());
			if (mRealNum > 1) {
				getCharProvider(false, mListFund.get(1)).request(getchRequest());
			}
		}
	}

	private boolean checkNeedReloadDb(List<FundList> list) {
		if (mList != null && mList.size() >= mRealNum) {
			for (int i = 0; i < mRealNum; i++) {
				if (!mList.get(i).getFundCode().equals(list.get(i).getFundCode())) {
					return true;
				}
			}
			return false;
		}
		return true;
	}

	@Override
	public void onRepNetFinished(ReqResult<ReqNetOpt> r) {
		if (r.isSuccess()) {
			FundInfo fund = (FundInfo) r.mData;
			mRealNum = Math.min(fund.getFundListCount(), FIXED_TOP_NUM);
			if (mRealNum > 0) {
				List<FundList> list = fund.getFundListList();
				boolean needReloadDb = checkNeedReloadDb(list);
				if (needReloadDb) {
					mList = list;
					new AsyFundTaks(true).execute(false);
				}
				if (!r.isResultFromCache()) {
					r.mReqOpt.subFlag(ReqNetOpt.FLAG_CACHE_THEN_REQ);
				}
			} else {
				ViewUtils.setVisibility(mProgress[0], View.GONE);
				ViewUtils.setVisibility(mProgress[1], View.GONE);
			}
		} else {
			if (mList == null) {
				mRealNum = -1;
			}
			ViewUtils.setVisibility(mProgress[0], View.GONE);
			ViewUtils.setVisibility(mProgress[1], View.GONE);
		}
	}

	class AsyFundTaks extends AsyPoolTask<Void, Void, ArrayList<NetWorthBean>> {
		boolean mQueryTop = true;

		public AsyFundTaks(boolean queryTop) {
			mQueryTop = queryTop;
		}

		@Override
		protected ArrayList<NetWorthBean> doInBackground(Void... p) {
			StringBuffer sb = new StringBuffer(256);
			if (mQueryTop) {
				return queryTopFund(sb);
			} else {
				return queryOptional(sb);
			}
		}

		private String buildQueryOptional(StringBuffer sb) {
			sb.append("select code,xuan from fundsinfo where code ");
			sb.append("in(");
			for (int i = 0; i < mRealNum; i++) {
				sb.append("'").append(mListFund.get(i).getJjdm()).append("',");
			}
			sb.deleteCharAt(sb.length() - 1).append(")");
			return sb.toString();
		}

		private String buildQueryTop(StringBuffer sb) {
			boolean saveTop = mType == TOP_RECOMMAND;
			String code = null;
			StringBuilder top = saveTop ? new StringBuilder() : null;
			sb.append("and code in(");
			for (int i = 0; i < mRealNum; i++) {
				code = mList.get(i).getFundCode();
				sb.append("'").append(code).append("',");
				if (saveTop) {
					top.append(code).append("-");
				}
			}
			sb.deleteCharAt(sb.length() - 1).append(")");
			if (saveTop) {
				int n = mList.size();
				for (int i = mRealNum; i < n; i++) {
					top.append(mList.get(i).getFundCode()).append("-");
				}
				GlobalApp.getApp().getMapObj().put(ValConfig.KEY_TOP_RECOMEND_LIST, top.toString());
			}
			return sb.toString();
		}

		protected ArrayList<NetWorthBean> queryOptional(StringBuffer sb) {
			Cursor c = null;
			String sql = buildQueryOptional(sb);
			try {
				c = DbUtils.query(sb.toString(), null, false);
				if (c != null && c.moveToFirst()) {
					do {
						String code = c.getString(0);
						int xuan = c.getInt(1);
						for (int i = 0; i < mRealNum; i++) {
							if (mListFund.get(i).getJjdm().equals(code)) {
								mListFund.get(i).setXunan(xuan);
								break;
							}
						}

					} while (c.moveToNext());
				} else {
					LogUtils.d("ranks", "query empty sql=" + sql);
				}
			} catch (Exception e) {
				e.printStackTrace();
				LogUtils.d("ranks", e + " sql=" + sql + "  " + mTvGroup.getText());
			} finally {
				if (c != null && !c.isClosed()) {
					c.close();
				}
			}
			return mListFund;
		}

		protected ArrayList<NetWorthBean> queryTopFund(StringBuffer sb) {
			ArrayList<NetWorthBean> res = null;
			try {
				ArrayList<NetWorthBean> r = DbOperat.getInstance().queryAll(buildQueryTop(sb));
				int size = r == null ? 0 : r.size();
				if (size != 0) {
					res = new ArrayList<NetWorthBean>(size);
					for (int i = 0; i < mRealNum; i++) {
						String code = mList.get(i).getFundCode();
						for (int j = size - 1; j >= 0; j--) {
							if (r.get(j).getJjdm().equals(code)) {
								res.add(r.remove(j));
								size--;
								break;
							}
						}
					}
				}
			} catch (WrapException e) {
				LogUtils.popDebug(e.toString());
			}
			return res;
		}

		@Override
		protected void onPostExecute(ArrayList<NetWorthBean> result) {
			if (mQueryTop) {
				if (result != null) {
					mListFund = result;
					initListData(true);
				} else {
					ViewUtils.setVisibility(mProgress[0], View.GONE);
					ViewUtils.setVisibility(mProgress[1], View.GONE);
				}
			} else {
				if (result != null) {
					initListData(false);
				}
			}
		}
	}

	@Override
	public void onCharCacheChanged(CharProvider charProvider, int from, ArrayList<CharValue> val,
			int cycleType) {
		if (charProvider == mCharProvider1) {
			onCharResult(true, charProvider, null);
		} else if (charProvider == mCharProvider2) {
			onCharResult(false, charProvider, null);
		}
	}

	@Override
	public void onCharCacheErr(CharProvider charProvider, int from, ReqResult<CharRequest> r,
			int cycleType) {
		if (charProvider == mCharProvider1) {
			onCharResult(true, charProvider, r.mErr);
		} else if (charProvider == mCharProvider2) {
			onCharResult(false, charProvider, r.mErr);
		}
	}

	private boolean onCharResult(boolean first, CharProvider charProvider, WrapException err) {
		if (err == null) {
			ArrayList<CharData> data = null;
			SimpleChartView chartview = null;
			if (first) {
				if (mCharData1 == null) {
					mCharData1 = new ArrayList<CharData>();
				}
				chartview = mSChar1;
				data = mCharData1;
				ViewUtils.setVisibility(mProgress[0], View.GONE);
			} else {
				if (mCharData2 == null) {
					mCharData2 = new ArrayList<CharData>();
				}
				chartview = mSChar2;
				data = mCharData2;
				ViewUtils.setVisibility(mProgress[1], View.GONE);
			}
			if (CharData.initCharData(data, charProvider.getOperatorList(CharValue.TYPE_JJJZ),
					charProvider.getType().isHuobi(), 0f) == 0) {
				chartview.setData(data);
				return true;
			}
		} else {
			ViewUtils.setVisibility(mProgress[first ? 0 : 1], View.GONE);
		}
		return false;
	}
}