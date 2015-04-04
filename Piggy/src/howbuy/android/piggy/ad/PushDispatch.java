package howbuy.android.piggy.ad;


import howbuy.android.piggy.ad.JpushOpera.JpushType;
import howbuy.android.piggy.ui.WebViewActivity;
import howbuy.android.piggy.ui.fragment.PureWebFragment.PureType;
import howbuy.android.util.Cons;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;


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

	public void despatchFragmentClass(int resource, String code, String value, String extar,
			String message) {
		JpushType j = JpushOpera.getPushType(code);
		if (j == null) {
			return;
		}
		switch (j) {
//		case Recharge:  //充值界面放到了主界面中处理
//			lunchNews(resource, value, message);
//			break;
		case News:
			lunchNews(resource, value, message);
			break;
		case Opinion:
			lunchYanbao(resource, value, message);
			break;
		case Interview:
			lunchInterview(resource, value, message);
			break;
		case CommonWap:   //网址
			lunchWap(resource, value, message, extar);
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
//		case Update:    //更新
//			lunchUpdate(resource);
//			break;
		case Other:
//			lunchUpdate(resource);
			break;
		default:
//			lunchUpdate(resource);
			break;
		}
	}

	private void lunchNews(int source, String value, String message) {
//		Bundle b = obtainBundle("新闻资讯");
//		b.putString(ValConfig.IT_ID, value);
//		b.putInt(ValConfig.IT_TYPE, 0);
//		b.putInt(ValConfig.IT_FROM, source);
//		Intent t = new Intent(mContext, AtyEmpty.class);
//		t.putExtra(AtyEmpty.KEY_FRAG_NAME, FragArticalRead.class.getName());
//		t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
//		mContext.startActivity(t);
	}

	private void lunchYanbao(int source, String value, String message) {
//		Bundle b = obtainBundle("研究报告");
//		b.putString(ValConfig.IT_ID, value);
//		b.putInt(ValConfig.IT_TYPE, 1);
//		b.putInt(ValConfig.IT_FROM, source);
//		Intent t = new Intent(mContext, AtyEmpty.class);
//		t.putExtra(AtyEmpty.KEY_FRAG_NAME, FragArticalRead.class.getName());
//		t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
//		mContext.startActivity(t);
	}

	private void lunchInterview(int source, String value, String message) {
//		Bundle b = obtainBundle("走访报告");
//		b.putString(ValConfig.IT_ID, value);
//		b.putInt(ValConfig.IT_TYPE, 2);
//		b.putInt(ValConfig.IT_FROM, source);
//		Intent t = new Intent(mContext, AtyEmpty.class);
//		t.putExtra(AtyEmpty.KEY_FRAG_NAME, FragArticalRead.class.getName());
//		t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
//		mContext.startActivity(t);
	}

	private void lunchTrust(int source, String value, String message) {
//		Intent t = new Intent(mContext, AtyEmpty.class);
//		Bundle b = obtainBundle("信托详情");
//		b.putInt(ValConfig.IT_FROM, source);
//		b.putString(ValConfig.IT_ID, String.valueOf(value));
//		t.putExtra(AtyEmpty.KEY_FRAG_NAME, FragTrustDetails.class.getName());
//		t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
//		t.putExtra(AtyEmpty.KEY_EXIT_NOANIM, true);
//		mContext.startActivity(t);
	}

	private Bundle obtainBundle(String title) {
		Bundle b = new Bundle();
//		b.putString(ValConfig.IT_NAME, title);
		return b;
	}

	private void lunchTrade(int source, String value, String message, String extar) {
		// TODO Auto-generated method stub
//		Intent t = new Intent(mContext, AtyEmpty.class);
//		Bundle b = obtainBundle(mContext.getString(R.string.tb_title_trade));
//		t.putExtra(AtyEmpty.KEY_FRAG_NAME, FragTbTrade.class.getName());
//		t.putExtra(AtyEmpty.KEY_ANIM_ENTER, R.anim.push_up_none);
//		t.putExtra(AtyEmpty.KEY_ANIM_EXIT, R.anim.push_up_out);
//		t.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		b.putInt(ValConfig.IT_FROM, source);
//		if (TextUtils.isEmpty(value)) {
//			t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
//			mContext.startActivity(t);
//			return;
//		} else {
//			TradeType tp = getTrapType(value);
//			String url = null;
//			switch (tp) {
//			case C:
//				url = "cuxiao/" + extar + ".htm";
//				break;
//			case Q:
//				url = "";
//				break;
//			case R:
//				if (TextUtils.isEmpty(extar)) {
//					url = ValConfig.URL_Trade_RENGOU_End;
//				} else {
//					url = ValConfig.URL_Trade_RENGOU + extar;
//				}
//				break;
//			case SG:
//				if (TextUtils.isEmpty(extar)) {
//					url = ValConfig.URL_Trade_SHENGGOU_End;
//				} else {
//					url = ValConfig.URL_Trade_SHENGOU + extar;
//				}
//				break;
//			case SH:
//				if (TextUtils.isEmpty(extar)) {
//					url = ValConfig.URL_Trade_SHUHUI_End;
//				} else {
//					url = ValConfig.URL_Trade_SHUHUI + extar;
//				}
//				break;
//			case T:
//				url = "tongzhi/" + extar + ".htm";
//				break;
//			case ZY:
//				url = "";
//				break;
//			case Z:
//				if (TextUtils.isEmpty(extar)) {
//					url = ValConfig.URL_Trade_ZHUANHUAN_End;
//				} else {
//					url = ValConfig.URL_Trade_ZHUANHUAN + extar;
//				}
//				break;
//			default:
//				break;
//			}
//			b.putString(ValConfig.IT_URL, url);
//			b.putString(ValConfig.IT_NAME, message);
//			t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
//			mContext.startActivity(t);
//		}

	}

	private void lunchFund(int source, String value, String message) {
//		Intent t = new Intent(mContext, AtyEmpty.class);
//		Bundle b = obtainBundle("基金详情");
//		b.putString(ValConfig.IT_ID, value);
//		b.putInt(ValConfig.IT_URL, 0);
//		b.putInt(ValConfig.IT_FROM, source);
//		t.putExtra(AtyEmpty.KEY_FRAG_NAME, FragFundDetails.class.getName());
//		t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
//		mContext.startActivity(t);
//
//		String anly = null;
//		if (source == ValConfig.SOURCE_PUSH) {
//			anly = "通知";
//		} else if (source == ValConfig.SOURCE_ADV) {
//			anly = "广告图";
//		}
//
//		if (anly != null) {
//			Analytics.onEvent(mContext, Analytics.VIEW_FUND_DETAIL, Analytics.KEY_FROM, anly);
//		}

	}

	private void lunchUpdate(int source) {
		// TODO...
	}

	private void lunchWap(int source, String value, String message, String extar) {
		Intent intent = new Intent(mContext, WebViewActivity.class);
		intent.putExtra(Cons.Intent_type, PureType.url.getType());
		intent.putExtra(Cons.Intent_id, value);
		extar=message;
		if (TextUtils.isEmpty(extar)) {
			extar = "储蓄罐";
		}
		intent.putExtra(Cons.Intent_name, extar);
		mContext.startActivity(intent);
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
//			return dispatchEvent(VALCONFIG.SOURCE_PUSH, code, msg);
			return dispatchEvent(0, code, msg);
		}
		return false;
	}

//	public boolean doAdvEvent(String code, String title) {
//		return dispatchEvent(ValConfig.SOURCE_ADV, code, title);
//	}

	private boolean dispatchEvent(int source, String code, String msg) {
		if (!TextUtils.isEmpty(code)) {
			String k = JpushOpera.getPushCode(code);
			String V = JpushOpera.getPushValue(code);
			String Ext = JpushOpera.getPushExtra(code);
			despatchFragmentClass(source, k, V, Ext, msg);
			return true;
		}
		return false;
	}

}
