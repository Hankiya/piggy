package com.howbuy.control;

import howbuy.android.palmfund.R;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.howbuy.config.FundConfig;
import com.howbuy.datalib.fund.ParFundsNetValueByIDs;
import com.howbuy.db.DbOperat;
import com.howbuy.entity.NetWorthBean;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AsyPoolTask;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.utils.FundUtils;
import com.howbuy.wireless.entity.protobuf.FundInfosListProto.FundInfosList;

public class HomePiggyLayout extends com.actionbarsherlock.internal.widget.IcsLinearLayout
		implements IReqNetFinished {
	TextView mTvFundValue, mTvSubmit;
	NetWorthBean mBean;

	public HomePiggyLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mTvFundValue = (TextView) findViewById(R.id.tv_value);
		mTvSubmit = (TextView) findViewById(R.id.tv_submit);
		FundUtils.formatFundValue(mTvFundValue, null, null, false, FundUtils.VALUE_QRSY);
		new AsyFundTaks().execute(false);
	}

	public void refush(boolean force) {
		if (mBean == null) {
			new AsyFundTaks().execute(false);
		} else {
			ParFundsNetValueByIDs par = new ParFundsNetValueByIDs(CacheOpt.TIME_HOUR)
					.setParams("482002");
			if (force) {
				par.addFlag(ReqNetOpt.FLAG_FORCE_UNCACHE);
			}
			par.setCallback(1, this).execute();
		}
	}

	class AsyFundTaks extends AsyPoolTask<Void, Void, ArrayList<NetWorthBean>> {
		@Override
		protected ArrayList<NetWorthBean> doInBackground(Void... p) {
			ArrayList<NetWorthBean> res = null;
			try {
				res = DbOperat.getInstance().queryAll("and code='482002'");
			} catch (WrapException e) {
				LogUtils.popDebug(e.toString());
			}
			return res;
		}

		@Override
		protected void onPostExecute(ArrayList<NetWorthBean> result) {
			if (result != null&&result.size()>0) {
				mBean = result.get(0);
				if (mBean != null) {
					FundUtils.formatFundValue(mTvFundValue, mBean, FundUtils.VALUE_QRSY);
					if (GlobalApp.getApp().getNetType() > 1) {
						postDelayed(new Runnable() {
							@Override
							public void run() {
								ParFundsNetValueByIDs par = new ParFundsNetValueByIDs(
										CacheOpt.TIME_HOUR).setParams("482002");
								par.setCallback(1, HomePiggyLayout.this).execute();
							}
						}, 2000);
					}

				}
			}
		}
	}

	@Override
	public void onRepNetFinished(ReqResult<ReqNetOpt> result) {
		if (result.isSuccess()) {
			FundInfosList fl = (FundInfosList) result.mData;
			ArrayList<NetWorthBean> r = FundUtils.getNetWorthBean(fl, FundConfig.DATA_MONEY);
			if (r.size() > 0) {
				NetWorthBean b = r.get(0);
				if (b != null) {
					b.setHbdr(DbOperat.formatWfsyToHbdr(b.getWfsy()));
					b.setJjjz("1");
					b.setLjjz("1");
					if (mBean != null) {
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
					}
					mBean = b;
					FundUtils.formatFundValue(mTvFundValue, mBean, FundUtils.VALUE_QRSY);
				}

			}

		}

	}
}
