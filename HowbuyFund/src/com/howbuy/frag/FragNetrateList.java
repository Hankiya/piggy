package com.howbuy.frag;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.protobuf.InvalidProtocolBufferException;
import com.howbuy.adp.TradeRateAdp;
import com.howbuy.adp.TradeRateAdp.TradeRate;
import com.howbuy.config.ValConfig;
import howbuy.android.palmfund.R;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.utils.FundUtils;
import com.howbuy.wireless.entity.protobuf.FundTradeInfoProtos.FundTradeInfo;
import com.howbuy.wireless.entity.protobuf.FundTradeInfoProtos.PurchaseFeeRate;
import com.howbuy.wireless.entity.protobuf.FundTradeInfoProtos.RedeemFeeRate;
import com.tang.library.pulltorefresh.PullToRefreshBase;

public class FragNetrateList extends AbsFragList {
	private TradeRateAdp mAdapter = null;
	private FundTradeInfo mCompanyInf = null;
	private boolean mNeedParse = false;

	private String[] parseArea(String s) {
		String[] r = new String[] { null, null };
		int i = s.indexOf("-");
		if (i != -1) {
			r[0] = s.substring(0, i);
			r[1] = s.substring(i + 1);
		}
		return r;
	}

	private String formateAreaTime(String timeArea) {
		if (!mNeedParse) {
			return timeArea;
		}
		String[] s = parseArea(timeArea);
		int a = 0, a1 = 0, b = 0, b1 = 0;
		if (!StrUtils.isEmpty(s[0])) {
			a = Integer.parseInt(s[0]);
		}
		if (!StrUtils.isEmpty(s[1])) {
			b = Integer.parseInt(s[1]);
		}
		if (a == 0 && b == 0) {
			return "费率";
		} else {
			if (a == 0) {
				b1 = b % 12;
				b /= 12;
				if (b1 == 0) {
					return b + "年以下";
				} else {
					return b + "r年" + b1 + "个月以下";
				}
			} else {
				a1 = a % 12;
				a /= 12;
				if (b == 0) {
					if (a1 == 0) {
						return a + "年以上";
					} else {
						return a + "年" + a1 + "个月以上";
					}
				} else {
					b1 = b % 12;
					b /= 12;
					if (a1 == 0 && b1 == 0) {
						return a + "年-" + b + "年";
					} else {
						if (a1 != 0 && b1 != 0) {
							return a + "年" + a1 + "个月-" + b + "年" + b1 + "个月";
						} else {
							if (a1 == 0) {
								return a + "年-" + b + "年" + b1 + "个月";
							} else {
								return a + "年" + a1 + "个月-" + b + "年";
							}
						}
					}

				}
			}
		}
	}

	private String formateAreaMoney(String moneyArea) {
		if (!mNeedParse) {
			return moneyArea;
		}
		String[] s = parseArea(moneyArea);
		float a = 0, b = 0;
		if (!StrUtils.isEmpty(s[0])) {
			a = Float.parseFloat(s[0]);
		}
		if (!StrUtils.isEmpty(s[1])) {
			b = Float.parseFloat(s[1]);
		}
		if (a == 0 && b == 0) {
			return "费率";
		} else {
			if (a == 0) {
				return StrUtils.formatF(b, 2) + "万以下";
			} else {
				if (b == 0) {
					return StrUtils.formatF(a, 2) + "万以上";
				} else {
					return StrUtils.formatF(a, 2) + "万-" + StrUtils.formatF(b, 2) + "万";
				}
			}
		}
	}

	private void parsePurchase(ArrayList<TradeRate> r, List<PurchaseFeeRate> f, int n) {
		PurchaseFeeRate it = null;
		for (int i = 0; i < n; i++) {
			it = f.get(i);
			String title = formateAreaMoney(it.getAmountSection());// it.getAmountSection()
			r.add(new TradeRate(title, it.getRate()));
		}
	}

	private void parseRedeem(ArrayList<TradeRate> r, List<RedeemFeeRate> f, int n) {
		RedeemFeeRate it = null;
		for (int i = 0; i < n; i++) {
			it = f.get(i);
			String title = formateAreaTime(it.getDurationSection());// it.getDurationSection()
			r.add(new TradeRate(title, it.getRate()));
		}
	}

	private ArrayList<TradeRate> parseTrade(FundTradeInfo f) {
		ArrayList<TradeRate> r = new ArrayList<TradeRate>(8);
		r.add(new TradeRate("申购状态", FundUtils.formatFundState(f.getSgStatus(), null)));
		r.add(new TradeRate("购回状态", FundUtils.formatFundState(f.getShStatus(), null)));
		r.add(new TradeRate("定投状态", FundUtils.formatFundState(f.getDtStatus(), null)));
		String minAmount = FundUtils.formatProperty(f.getMinAmount(), 0);
		if (StrUtils.isEmpty(minAmount)) {
			minAmount = ValConfig.NULL_TXT0;
		}
		r.add(new TradeRate("起购金额", minAmount));//
		int a0 = f.getPrePurchaseFeeRatesCount();// 前申购.
		int a1 = f.getPreRedeemFeeRatesCount();// 前购回.
		int b0 = f.getSufPurchaseFeeRatesCount();// 后申购.
		int b1 = f.getSufRedeemFeeRatesCount();// 后购回.
		if (a0 > 0) {
			r.add(new TradeRate("申购费率"));
			parsePurchase(r, f.getPrePurchaseFeeRatesList(), a0);
		}
		if (b0 > 0) {
			r.add(new TradeRate("后端申购费率"));
			parsePurchase(r, f.getSufPurchaseFeeRatesList(), b0);
		}
		if (a1 > 0) {
			r.add(new TradeRate("赎回费率"));
			parseRedeem(r, f.getPreRedeemFeeRatesList(), a1);
		}
		if (b1 > 0) {
			r.add(new TradeRate("后端购回费率"));
			parseRedeem(r, f.getSufRedeemFeeRatesList(), b1);
		}
		return r;
	}

	private void intiAdapter() {
		if (mAdapter == null) {
			Bundle arg = getArguments();
			try {
				if (mTitleLable == null) {
					mTitleLable = arg.getString(ValConfig.IT_NAME);
				}
				mCompanyInf = FundTradeInfo.parseFrom(arg.getByteArray(ValConfig.IT_ENTITY));
				mAdapter = new TradeRateAdp(getSherlockActivity(), parseTrade(mCompanyInf));
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		super.initViewAdAction(root, bundle);
		setPullRefushMode(false, false);
		intiAdapter();
		mListView.setAdapter(mAdapter);
		mListView.setBackgroundColor(0xffffffff);
		mListView.setSelector(new ColorDrawable(0));
	}

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_content_list;
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub

	}

}
