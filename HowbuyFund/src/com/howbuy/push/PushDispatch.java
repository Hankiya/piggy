package com.howbuy.push;

import howbuy.android.palmfund.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.howbuy.aty.AtyEmpty;
import com.howbuy.aty.AtyTbMain;
import com.howbuy.component.AppService;
import com.howbuy.config.Analytics;
import com.howbuy.config.ValConfig;
import com.howbuy.entity.AtyInfs;
import com.howbuy.frag.FragArticalRead;
import com.howbuy.frag.FragDetailsFund;
import com.howbuy.frag.FragTbTrade;
import com.howbuy.frag.FragDetailsTrust;
import com.howbuy.frag.FragWebView;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.net.ReqOpt;
import com.howbuy.push.PushParse.JpushType;

/**
 * 推送以及广告的点击事件
 * 
 * @author YesCPU
 * 
 */
public class PushDispatch {
	public static final String INTENT_ID = "INTENT_ID";
	public static final String INTENT_MSG = "INTENT_MSG";
	public static final String INTENT_TYPE = "INTENT_TYPE";
	private static Context mContext;

	/**
	 * 促销活动：C；通知：T；认购：R；申购：SG；赎回：SH；转换：Z；交易主页：ZY；其他：Q
	 */
	public enum TradeType {
		/**
		 * 促销活动
		 */
		C,
		/**
		 * 通知
		 */
		T,
		/**
		 * 认购
		 */
		R,
		/**
		 * 申购
		 */
		SG,
		/**
		 * 赎回
		 */
		SH,
		/**
		 * 转换
		 */
		Z,
		/**
		 * 交易主页
		 */
		ZY,
		/**
		 * 其他
		 */
		Q
	}

	public PushDispatch(Context context) {
		mContext = context;
	}

	private void despatchFragmentClass(int resource, String code, String value, String extar,
			String message) {
		JpushType j = PushParse.getPushType(code);
		if (j == null) {
			return;
		}
		switch (j) {
		case News:
			lunchNews(resource, value, message);
			break;
		case Opinion:
			lunchYanbao(resource, value, message);
			break;
		case Interview:
			lunchInterview(resource, value, message);
			break;
		case CommonWap:
			lunchWap(resource, value, message);
			break;
		case Trust:
			lunchTrust(resource, value, message);
			break;
		case Trade:// 交易
			lunchTrade(resource, value, message, extar);
			break;
		case Fund:
			lunchFund(resource, value, message);
			break;
		case Update:
			lunchUpdate(resource);
			break;
		case Other:
			lunchUpdate(resource);
			break;
		default:
			lunchUpdate(resource);
			break;
		}
	}

	private void lunchNews(int source, String value, String message) {
		Bundle b = obtainBundle("新闻资讯");
		b.putString(ValConfig.IT_ID, value);
		b.putString(ValConfig.IT_URL, message);
		b.putInt(ValConfig.IT_TYPE, 0);
		b.putInt(ValConfig.IT_FROM, source);
		Intent t = new Intent(mContext, AtyEmpty.class);
		t.putExtra(AtyEmpty.KEY_FRAG_NAME, FragArticalRead.class.getName());
		t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
		mContext.startActivity(t);
	}

	private void lunchYanbao(int source, String value, String message) {
		Bundle b = obtainBundle("研究报告");
		b.putString(ValConfig.IT_ID, value);
		b.putInt(ValConfig.IT_TYPE, 1);
		b.putString(ValConfig.IT_URL, message);
		b.putInt(ValConfig.IT_FROM, source);
		Intent t = new Intent(mContext, AtyEmpty.class);
		t.putExtra(AtyEmpty.KEY_FRAG_NAME, FragArticalRead.class.getName());
		t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
		mContext.startActivity(t);
	}

	private void lunchInterview(int source, String value, String message) {
		Bundle b = obtainBundle("走访报告");
		b.putString(ValConfig.IT_ID, value);
		b.putInt(ValConfig.IT_TYPE, 2);
		b.putInt(ValConfig.IT_FROM, source);
		b.putString(ValConfig.IT_URL, message);
		Intent t = new Intent(mContext, AtyEmpty.class);
		t.putExtra(AtyEmpty.KEY_FRAG_NAME, FragArticalRead.class.getName());
		t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
		mContext.startActivity(t);
	}

	private void lunchTrust(int source, String value, String message) {
		Intent t = new Intent(mContext, AtyEmpty.class);
		Bundle b = obtainBundle("信托详情");
		b.putInt(ValConfig.IT_FROM, source);
		b.putString(ValConfig.IT_ID, String.valueOf(value));
		t.putExtra(AtyEmpty.KEY_FRAG_NAME, FragDetailsTrust.class.getName());
		t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
		t.putExtra(AtyEmpty.KEY_EXIT_NOANIM, true);
		mContext.startActivity(t);
	}

	private Bundle obtainBundle(String title) {
		Bundle b = new Bundle();
		b.putString(ValConfig.IT_NAME, title);
		return b;
	}

	private void lunchTrade(int source, String value, String message, String extar) {
		Intent t = new Intent(mContext, AtyEmpty.class);
		Bundle b = obtainBundle(mContext.getString(R.string.tb_title_trade));
		t.putExtra(AtyEmpty.KEY_FRAG_NAME, FragTbTrade.class.getName());
		t.putExtra(AtyEmpty.KEY_ANIM_ENTER, R.anim.push_up_none);
		t.putExtra(AtyEmpty.KEY_ANIM_EXIT, R.anim.push_up_out);
		t.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		b.putInt(ValConfig.IT_FROM, source);
		if (TextUtils.isEmpty(value)) {
			t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
			mContext.startActivity(t);
			return;
		} else {
			TradeType tp = getTrapType(value);
			String url = null;
			switch (tp) {
			case C:
				url = "cuxiao/" + extar + ".htm";
				break;
			case Q:
				url = "";
				break;
			case R:
				if (TextUtils.isEmpty(extar)) {
					url = ValConfig.URL_Trade_RENGOU_End;
				} else {
					url = ValConfig.URL_Trade_RENGOU + extar;
				}
				break;
			case SG:
				if (TextUtils.isEmpty(extar)) {
					url = ValConfig.URL_Trade_SHENGGOU_End;
				} else {
					url = ValConfig.URL_Trade_SHENGOU + extar;
				}
				break;
			case SH:
				if (TextUtils.isEmpty(extar)) {
					url = ValConfig.URL_Trade_SHUHUI_End;
				} else {
					url = ValConfig.URL_Trade_SHUHUI + extar;
				}
				break;
			case T:
				url = "tongzhi/" + extar + ".htm";
				break;
			case ZY:
				url = "";
				break;
			case Z:
				if (TextUtils.isEmpty(extar)) {
					url = ValConfig.URL_Trade_ZHUANHUAN_End;
				} else {
					url = ValConfig.URL_Trade_ZHUANHUAN + extar;
				}
				break;
			default:
				break;
			}
			b.putString(ValConfig.IT_URL, ValConfig.URL_TRADE_BASEURL_RELEASE + url);
			b.putString(ValConfig.IT_NAME, message);
			t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
			mContext.startActivity(t);
		}

	}

	private void lunchFund(int source, String value, String message) {
		Intent t = new Intent(mContext, AtyEmpty.class);
		Bundle b = obtainBundle("基金详情");
		b.putString(ValConfig.IT_ID, value);
		b.putInt(ValConfig.IT_URL, 0);
		b.putInt(ValConfig.IT_FROM, source);
		t.putExtra(AtyEmpty.KEY_FRAG_NAME, FragDetailsFund.class.getName());
		t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
		mContext.startActivity(t);

		String anly = null;
		if (source == ValConfig.SOURCE_PUSH) {
			anly = "通知";
		} else if (source == ValConfig.SOURCE_ADV) {
			anly = "广告图";
		}

		if (anly != null) {
			Analytics.onEvent(mContext, Analytics.VIEW_FUND_DETAIL, Analytics.KEY_FROM, anly);
		}

	}

	private void lunchUpdate(int source) {
		if (AtyInfs.hasAty(AtyTbMain.class, null) == null) {
			Intent i = new Intent(mContext, AtyTbMain.class);//
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			mContext.startActivity(i);
		} else {
			GlobalApp
					.getApp()
					.getGlobalServiceMger()
					.executeTask(new ReqOpt(0,"your key arg", AppService.HAND_UPDATE_APP),
							null);
		}
	}

	private void lunchWap(int source, String value, String message) {
		Bundle b = obtainBundle("掌上基金");
		b.putString(ValConfig.IT_URL, value);
		b.putInt(ValConfig.IT_FROM, source);
		Intent t = new Intent(mContext, AtyEmpty.class);
		t.putExtra(AtyEmpty.KEY_FRAG_NAME, FragWebView.class.getName());
		t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
		//t.putExtra(AtyEmpty.KEY_ANIM_ENTER, R.anim.push_up_none);
		//t.putExtra(AtyEmpty.KEY_ANIM_EXIT, R.anim.push_up_out);
		mContext.startActivity(t);

	}

	private TradeType getTrapType(String operaType) {
		if (operaType.equals("C")) {
			return TradeType.C;
		} else if (operaType.equals("Q")) {
			return TradeType.Q;
		} else if (operaType.equals("R")) {
			return TradeType.R;
		} else if (operaType.equals("SG")) {
			return TradeType.SG;
		} else if (operaType.equals("SH")) {
			return TradeType.SH;
		} else if (operaType.equals("T")) {
			return TradeType.T;
		} else if (operaType.equals("Z")) {
			return TradeType.Z;
		} else {
			return TradeType.ZY;
		}
	}

	public boolean doPushBund(Bundle b) {
		if (b != null && b.size() > 0 && !TextUtils.isEmpty(INTENT_TYPE)) {
			String code = b.getString(INTENT_ID);
			String msg = b.getString(INTENT_MSG);
			return dispatchEvent(ValConfig.SOURCE_PUSH, code, msg);
		}
		return false;
	}

	public boolean doAdvEvent(String code, String title) {
		return dispatchEvent(ValConfig.SOURCE_ADV, code, title);
	}

	private boolean dispatchEvent(int source, String code, String msg) {
		if (!TextUtils.isEmpty(code)) {
			String k = PushParse.getPushCode(code);
			String V = PushParse.getPushValue(code);
			String Ext = PushParse.getPushExtra(code);
			despatchFragmentClass(source, k, V, Ext, msg);
			return true;
		}
		return false;
	}

}
