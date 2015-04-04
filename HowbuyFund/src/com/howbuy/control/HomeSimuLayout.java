package com.howbuy.control;

import howbuy.android.palmfund.R;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.howbuy.component.AppFrame;
import com.howbuy.component.CardDrawable;
import com.howbuy.config.SelfConfig;
import com.howbuy.config.ValConfig;
import com.howbuy.control.CheckImage.OnCheckImageChangeListener;
import com.howbuy.datalib.fund.AAParSimuRecommend;
import com.howbuy.db.DbOperat;
import com.howbuy.db.DbUtils;
import com.howbuy.entity.NetWorthBean;
import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.AsyPoolTask;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.lib.utils.SysUtils;
import com.howbuy.lib.utils.ViewUtils;
import com.howbuy.utils.FundUtils;
import com.howbuy.wireless.entity.protobuf.SimuRecommendProto;
import com.howbuy.wireless.entity.protobuf.SimuRecommendProto.SimuRecommend;

public class HomeSimuLayout extends RelativeLayout implements IReqNetFinished,
		OnCheckImageChangeListener {
	TextView mTvTitleOne, mTvCommentOne, mTvDesOne1, mTvDesOne2, mTvValOne1, mTvValOne2;
	TextView mTvTitleTwo, mTvCommentTwo, mTvDesTwo1, mTvDesTwo2, mTvValTwo1, mTvValTwo2;
	TextView mTvGroup;
	View mLayProgress, mLayOne, mLayTwo;
	CheckImage mCiOne, mCiTwo;

	ArrayList<NetWorthBean> mList = null;
	ArrayList<String> mCode = null;

	public HomeSimuLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ArrayList<NetWorthBean> getData() {
		return mList;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mTvGroup = (TextView) findViewById(R.id.tv_group);
		mTvTitleOne = (TextView) findViewById(R.id.tv_simu_title_one);
		mTvCommentOne = (TextView) findViewById(R.id.tv_simu_comment_one);
		mLayOne = findViewById(R.id.lay_simu_one);
		mTvDesOne1 = (TextView) mLayOne.findViewById(R.id.tv_simu_des_one1);
		mTvDesOne2 = (TextView) mLayOne.findViewById(R.id.tv_simu_des_one2);
		mTvValOne1 = (TextView) mLayOne.findViewById(R.id.tv_simu_val_one1);
		mTvValOne2 = (TextView) mLayOne.findViewById(R.id.tv_simu_val_one2);
		mCiOne = (CheckImage) mLayOne.findViewById(R.id.ci_home_simu_collect1);
		mTvTitleTwo = (TextView) findViewById(R.id.tv_simu_title_two);
		mTvCommentTwo = (TextView) findViewById(R.id.tv_simu_comment_two);
		mLayTwo = findViewById(R.id.lay_simu_two);
		mTvDesTwo1 = (TextView) mLayTwo.findViewById(R.id.tv_simu_des_two1);
		mTvDesTwo2 = (TextView) mLayTwo.findViewById(R.id.tv_simu_des_two2);
		mTvValTwo1 = (TextView) mLayTwo.findViewById(R.id.tv_simu_val_two1);
		mTvValTwo2 = (TextView) mLayTwo.findViewById(R.id.tv_simu_val_two2);
		mCiTwo = (CheckImage) mLayTwo.findViewById(R.id.ci_home_simu_collect2);
		mLayProgress = findViewById(R.id.pb_home_simu);
		showProgress(true);
		mCiTwo.setEnabled(false);
		mCiOne.setEnabled(false);
		mLayTwo.setEnabled(false);
		mLayOne.setEnabled(false);
		mCiOne.setOnCheckImageChangeListener(this);
		mCiTwo.setOnCheckImageChangeListener(this);
		postDelayed(new Runnable() {
			@Override
			public void run() {
				refush(false);
			}
		}, 1500);
		CardDrawable cd = null;
		float density = SysUtils.getDensity(getContext());
		cd = new CardDrawable(0xffffffff);
		cd.setShadeWidth(0, density, 0, 2 * density);
		ViewUtils.setBackground(this, cd);
	}

	public void showProgress(boolean show) {
		if (mLayProgress != null) {
			if (show) {
				if (mLayProgress.getVisibility() != View.VISIBLE) {
					mLayProgress.setVisibility(View.VISIBLE);
				}
			} else {
				if (mLayProgress.getVisibility() == View.VISIBLE) {
					mLayProgress.setVisibility(View.GONE);
				}
			}
		}
	}

	public void refush(boolean force) {
		AbsParam par = new AAParSimuRecommend(CacheOpt.TIME_WEEK);
		if (force && mCode != null) {
			par.addFlag(ReqNetOpt.FLAG_FORCE_UNCACHE);
		} else {
			par.subFlag(ReqNetOpt.FLAG_FORCE_UNCACHE);
			if (mCode == null) {
				par.addFlag(ReqNetOpt.FLAG_CACHE_THEN_REQ);
			}
		}
		par.setCallback(1, this).execute();
	}

	public void checkOptional() {
		if (mCode == null) {
			refush(true);
		} else {
			new AsyFundTaks(mList == null).execute(false);
		}
	}

	public void checkFund() {
		if (mCode != null) {
			new AsyFundTaks(true).execute(false);
		}
	}

	@Override
	public void onRepNetFinished(ReqResult<ReqNetOpt> r) {
		showProgress(false);
		if (r.isSuccess()) {
			SimuRecommend sr = (SimuRecommend) r.mData;
			if (sr != null) {
				initView(sr.getFundListList());
			}
		} else {
		}
	}

	private void initView(List<SimuRecommendProto.SimuRecommendInfo> srf) {
		int n = srf == null ? 0 : srf.size();
		if (n > 0) {
			if (mCode == null) {
				mCode = new ArrayList<String>(n + 1);
			} else {
				mCode.clear();
			}
			SimuRecommendProto.SimuRecommendInfo a = srf.get(0);
			SimuRecommendProto.SimuRecommendInfo b = n > 1 ? srf.get(1) : null;
			String fundCode = null;
			if (a != null) {
				mTvTitleOne.setText(a.getFundName());
				mTvCommentOne.setText(a.getAdvantage());
				mTvDesOne1.setText(FundUtils.formatStr(a.getData1Desc(), 0, ValConfig.NULL_TXT0));
				mTvValOne1.setText(FundUtils.formatStr(a.getData1(), 0, ValConfig.NULL_TXT1));
				mTvDesOne2.setText(FundUtils.formatStr(a.getData2Desc(), 0, ValConfig.NULL_TXT0));
				mTvValOne2.setText(FundUtils.formatStr(a.getData2(), 0, ValConfig.NULL_TXT1));
				fundCode = a.getFundCode();
				if (!StrUtils.isEmpty(fundCode)) {
					mCode.add(fundCode);
				}
			}
			if (b != null) {
				mTvTitleTwo.setText(b.getFundName());
				mTvCommentTwo.setText(b.getAdvantage());
				mTvDesTwo1.setText(FundUtils.formatStr(b.getData1Desc(), 0, ValConfig.NULL_TXT0));
				mTvValTwo1.setText(FundUtils.formatStr(b.getData1(), 0, ValConfig.NULL_TXT1));
				mTvDesTwo2.setText(FundUtils.formatStr(b.getData2Desc(), 0, ValConfig.NULL_TXT0));
				mTvValTwo2.setText(FundUtils.formatStr(b.getData2(), 0, ValConfig.NULL_TXT1));
				fundCode = b.getFundCode();
				if (!StrUtils.isEmpty(fundCode)) {
					mCode.add(fundCode);
				}
			}
			if (mCode.size() > 0) {
				new AsyFundTaks(true).execute(false);
			}
		}

	}

	class AsyFundTaks extends AsyPoolTask<Void, Void, ArrayList<NetWorthBean>> {
		boolean mQueryFund = false;

		public AsyFundTaks(boolean queryFund) {
			mQueryFund = queryFund;
		}

		@Override
		protected ArrayList<NetWorthBean> doInBackground(Void... p) {
			StringBuffer sb = new StringBuffer(256);
			if (mQueryFund) {
				return queryTopFund(sb);
			} else {
				return queryOptional(sb);
			}
		}

		private String buildQueryOptional(StringBuffer sb) {
			sb.append("select code,xuan from fundsinfo where code ");
			sb.append("in(");
			for (int i = 0; i < mList.size(); i++) {
				sb.append("'").append(mList.get(i).getJjdm()).append("',");
			}
			sb.deleteCharAt(sb.length() - 1).append(")");
			return sb.toString();
		}

		private String buildQueryTop(StringBuffer sb) {
			sb.append("and code in(");
			for (int i = 0; i < mCode.size(); i++) {
				sb.append("'").append(mCode.get(i)).append("',");
			}
			sb.deleteCharAt(sb.length() - 1).append(")");
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
						for (int i = 0; i < mList.size(); i++) {
							if (mList.get(i).getJjdm().equals(code)) {
								mList.get(i).setXunan(xuan);
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
			return mList;
		}

		protected ArrayList<NetWorthBean> queryTopFund(StringBuffer sb) {
			ArrayList<NetWorthBean> res = null;
			try {
				ArrayList<NetWorthBean> r = DbOperat.getInstance().queryAll(buildQueryTop(sb));
				int size = r == null ? 0 : r.size();
				if (size != 0) {
					res = new ArrayList<NetWorthBean>(size);
					for (int i = 0; i < mCode.size(); i++) {
						for (int j = size - 1; j >= 0; j--) {
							if (r.get(j).getJjdm().equals(mCode.get(i))) {
								res.add(r.remove(j));
								size--;
								break;
							}
						}
					}
				}
			} catch (WrapException e) {
			}
			return res;
		}

		@Override
		protected void onPostExecute(ArrayList<NetWorthBean> result) {
			if (mQueryFund) {
				mList = result;
			}
			if (mList != null) {
				int n = mList.size();
				if (n > 0) {
					mCiOne.setChecked(mList.get(0).getXunan() >= 1);
					if (!mLayOne.isEnabled()) {
						mLayOne.setEnabled(true);
						mCiOne.setEnabled(true);
					}
				}
				if (n > 1) {
					mCiTwo.setChecked(mList.get(1).getXunan() >= 1);
					if (!mLayTwo.isEnabled()) {
						mLayTwo.setEnabled(true);
						mCiTwo.setEnabled(true);
					}
				}
			}
		}
	}

	@Override
	public void onCheckImageChanged(View buttonView, boolean isChecked) {
		if (mList != null) {
			boolean handled = false;
			if (buttonView == mCiOne) {
				handled = checkOptional(mList.get(0), isChecked);
			} else if (buttonView == mCiTwo) {
				handled = checkOptional(mList.get(1), isChecked);
			}
			if (handled) {
				LogUtils.d("imgcheck", buttonView + ",checked=" + isChecked);
			}
		}
	}

	private boolean checkOptional(NetWorthBean b, boolean checked) {
		boolean handled = false;
		if (checked) {
			if (handled = b.getXunan() < 1) {
				b.setXunan(SelfConfig.UNSynsAdd);
				FundUtils
						.updateOptional(AppFrame.getApp(), b.getJjdm(), SelfConfig.UNSynsAdd, true);
			}
		} else {
			if (handled = b.getXunan() >= 1) {
				b.setXunan(SelfConfig.UNSynsDel);
				FundUtils
						.updateOptional(AppFrame.getApp(), b.getJjdm(), SelfConfig.UNSynsDel, true);
			}
		}
		return handled;
	}
}
