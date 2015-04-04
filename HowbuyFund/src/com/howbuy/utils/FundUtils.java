package com.howbuy.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.howbuy.aty.AtyEmpty;
import com.howbuy.component.AppFrame;
import com.howbuy.config.FundConfig;
import com.howbuy.config.SelfConfig;
import com.howbuy.config.ValConfig;
import com.howbuy.entity.NetWorthBean;
import com.howbuy.entity.UserInf;
import com.howbuy.frag.FragDetailsFund;
import com.howbuy.frag.FragSearch;
import com.howbuy.lib.frag.AbsFrag;
import com.howbuy.lib.frag.AbsHbFrag;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.wireless.entity.protobuf.ClosesNewProtos.ClosesNew;
import com.howbuy.wireless.entity.protobuf.FundInfosListProto.FundInfosList;
import com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys;
import com.howbuy.wireless.entity.protobuf.OpensProtos.Opens;
import com.howbuy.wireless.entity.protobuf.SimusProtos.Simus;

public class FundUtils {
	public static final String FDECIMAL2 = "0.00";
	public static final String FDECIMAL4 = "0.0000";

	public static final int VALUE_JJJZ = 1;// 净值
	public static final int VALUE_LJJZ = 2;// 累计净值
	public static final int VALUE_WFSY = 3;// 万份收益
	public static final int VALUE_QRSY = 4;// 七日年华收益
	public static final int VALUE_HBDR = 5;// 日增幅
	public static final int VALUE_HB1Y = 6;// 月增幅
	public static final int VALUE_HB1N = 7;// 年涨幅
	public static final int VALUE_HB3N = 8;// 3年涨幅
	public static final int VALUE_HBJN = 9; // 今年以来涨幅

	public static final String pickFundValue(NetWorthBean b, int VALUE_X) {
		String valStr = null;
		switch (VALUE_X) {
		case VALUE_JJJZ:
			valStr = b.getJjjz();
			break;
		case VALUE_LJJZ:
			valStr = b.getLjjz();
			break;
		case VALUE_WFSY:
			valStr = b.getWfsy();
			break;
		case VALUE_QRSY:
			valStr = b.getQrsy();
			break;
		case VALUE_HBDR:
			valStr = b.getHbdr();
			break;
		case VALUE_HB1Y:
			valStr = b.getHb1y();
			break;
		case VALUE_HB1N:
			valStr = b.getHb1n();
			break;
		case VALUE_HB3N:
			valStr = b.getHb3n();
			break;
		case VALUE_HBJN:
			valStr = b.getHbjn();
			break;
		default:
			valStr = null;
		}
		return valStr;
	}

	public static final String formatFundValue(TextView tv, NetWorthBean b, int VALUE_X) {
		return formatFundValue(tv, pickFundValue(b, VALUE_X), b.getDanWei(),
				FundConfig.TYPE_SIMU.equals(b.getJjfl()), VALUE_X);
	}

	public static String formatFundValue(TextView tv, String valStr, String danwei, boolean simu,
			int VALUE_X) {
		String result = null;
		float val = 0;
		if (!StrUtils.isEmpty(valStr)) {// empty value.
			val = Float.parseFloat(valStr);
			boolean sm100 = false;
			if (simu) {
				sm100 = simu && (VALUE_X == VALUE_JJJZ || VALUE_X == VALUE_LJJZ)
						&& !StrUtils.isEmpty(danwei) && danwei.length() > 1;
				if (sm100) {
					val /= Integer.parseInt(danwei.trim());
				} else if (VALUE_X > VALUE_QRSY) {
					val *= 100;
				}
			}
			if (VALUE_X < 4) {
				result = StrUtils.formatF(val, FDECIMAL4);
			} else {
				result = StrUtils.formatF(val, FDECIMAL2) + "%";
			}
			if (sm100) {
				result = result + "*";
			}
		}
		if (tv != null) {
			// 9999，sort null to end
			if (result == null || result.startsWith("9999") || result.startsWith("-9999")) {
				tv.setText("--");
				tv.setTextColor(0xff333333);
			} else {
				tv.setText(result);
				if (VALUE_X >= VALUE_QRSY) {
					if (val > 0) {
						tv.setTextColor(0xffcc0000);
					} else if (val < 0) {
						tv.setTextColor(0xff009900);
					} else {
						tv.setTextColor(0xff000000);
					}
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * @param state
	 */
	public static String formatFundState(String state, String which) {
		StringBuffer sb = new StringBuffer(16);
		boolean ok = "1".equals(state);
		if (ok) {
			sb.append("开放");
		} else {
			if ("2".equals(state)) {
				sb.append("限额");
			} else {
				sb.append("暂停");
			}
		}
		if (StrUtils.isEmpty(which)) {
			return sb.toString();
		} else {
			if (ok) {
				sb.insert(0, which);
			} else {
				sb.append(which);
			}
		}

		return sb.toString();
	}

	/*
	 * danwei 0 为元,1为分,2为万
	 */
	public static String formatProperty(String property, int danwei) {
		try {
			float val = Float.parseFloat(property);
			if (danwei == 2) {
				if (val >= 10000) {
					val = val / 10000;
					return StrUtils.formatF(val, 2) + "亿元";
				} else {
					return StrUtils.formatF(val, 2) + "万元";
				}
			} else {
				if (1 == danwei) {
					val /= 100;
				}
				if (val >= 10000) {
					val = val / 10000;
					return StrUtils.formatF(val, 2) + "万元";
				} else {
					return StrUtils.formatF(val, 2) + "元";
				}
			}

		} catch (Exception e) {
			return "";
		}
	}

	public static String formatStr(String str, int maxLen, String emptyDef) {
		if (StrUtils.isEmpty(str)) {
			return emptyDef == null ? str : emptyDef;
		} else {
			str=str.trim();
			if (maxLen > 3) {
				int n = str.length();
				if (n > maxLen) {
					return str.substring(0, maxLen - 3) + "...";
				}
			}
		}
		return str;
	}

	public static String[] formatQuarter(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat(ValConfig.DATEF_YMD);
		Date d = null;
		String year = null;
		String jiDu = null;
		try {
			d = sdf.parse(date);
			year = String.valueOf(d.getYear() + 1900);
			int a = d.getMonth();
			if (a > 0 && a < 4) {
				jiDu = "一";
			} else if (a > 3 && a < 7) {
				jiDu = "二";
			} else if (a > 6 && a < 10) {
				jiDu = "三";
			} else {
				jiDu = "四";
			}
			return new String[] { year + "年", jiDu + "季度" };
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param jjfl
	 * @param jjdm
	 *            非私募可以为空
	 * @return
	 */
	public static String getFundType(String jjfl, String jjdm) {
		String type = null;
		if (!TextUtils.isEmpty(jjdm) && jjdm.matches("[A-Za-z]{1}.*")) {
			type = "私募";
			return type;
		}
		if (jjfl.equals(FundConfig.TYPE_SIMU)) {
			type = "私募";
		} else if (jjfl.equals(FundConfig.TYPE_OTHER)) {
			type = "开放型";
		} else if (jjfl.equals(FundConfig.TYPE_GUPIAO)) {// 股票
			type = "股票型";
		} else if (jjfl.equals(FundConfig.TYPE_HUNHE)) {// 混合
			type = "混合型";
		} else if (jjfl.equals(FundConfig.TYPE_ZHAIQUAN)) {// 债券
			type = "债券型";
		} else if (jjfl.equals(FundConfig.TYPE_HUOBI)) {// 货币
			type = "货币型";
		} else if (jjfl.equals(FundConfig.TYPE_QDII)) {// qdii
			type = "QDII型";
		} else if (jjfl.equals(FundConfig.TYPE_FENGBI)) {// 封闭
			type = "封闭式";
		} else if (jjfl.equals(FundConfig.TYPE_JIEGOU)) {// 结构
			type = "结构型";
		} else if (jjfl.equals(FundConfig.TYPE_ZHISHU)) {// 指数
			type = "指数型";
		} else if (jjfl.equals(FundConfig.TYPE_BAOBEN)) {// 保本
			type = "保本型";
		} else if (jjfl.equals(FundConfig.TYPE_LICAI)) {// 理财
			type = "理财型";
		} else if (jjfl.equals(FundConfig.TYPE_HUNHE)) {
			type = "混合型";
		} else {
			type = "开放型";
		}
		return type;
	}

	public static ArrayList<NetWorthBean> getNetWorthBean(FundInfosList fl, int dataType) {
		ArrayList<NetWorthBean> list = new ArrayList<NetWorthBean>();
		NetWorthBean bean = null;
		switch (dataType) {
		case FundConfig.DATA_CLOSE:
			List<ClosesNew> closesNews = fl.getClosesNewList();
			ClosesNew close;
			for (int i = 0; i < closesNews.size(); i++) {
				close = closesNews.get(i);
				bean = new NetWorthBean();
				bean.setJjdm(close.getJjdm());
				bean.setJzrq(close.getJzrq());
				bean.setJjjz(close.getJjjz());
				bean.setLjjz(close.getLjjz());
				bean.setHbdr(close.getHbdr());
				bean.setHb3y(close.getHb3Y());
				bean.setHb6y(close.getHb6Y());
				bean.setHb1n(close.getHb1N());
				bean.setHbjn(close.getHbjn());
				bean.setZfxz(close.getZfxz());
				bean.setHb1y(close.getHb1Y());
				list.add(bean);
			}
			break;
		case FundConfig.DATA_SIMU:
			List<Simus> simus = fl.getSimusList();
			Simus simu;
			for (int i = 0; i < simus.size(); i++) {
				simu = simus.get(i);
				bean = new NetWorthBean();
				bean.setJjdm(simu.getJjdm());
				bean.setJzrq(simu.getJzrq());
				bean.setJjjz(simu.getJjjz());
				bean.setLjjz(simu.getLjjz());
				bean.setHb1y(simu.getHb1Y());
				bean.setHb6y(simu.getHb6Y());
				bean.setHb1n(simu.getHb1N());
				bean.setHb3n(simu.getHb3N());
				list.add(bean);
			}
			break;
		case FundConfig.DATA_MONEY:
			List<Moneys> moneys = fl.getMoneysList();
			Moneys money;
			for (int i = 0; i < moneys.size(); i++) {
				money = moneys.get(i);
				bean = new NetWorthBean();
				bean.setJjdm(money.getJjdm());
				bean.setJzrq(money.getJzrq());
				bean.setWfsy(money.getWfsy());
				bean.setQrsy(money.getQrsy());
				bean.setZfxz(money.getZfxz());
				bean.setHb1y(money.getHb1Y());
				bean.setHb3y(money.getHb3Y());
				bean.setHb6y(money.getHb6Y());
				bean.setHb1n(money.getHb1N());
				bean.setHbjn(money.getHbjn());
				list.add(bean);
			}
			break;
		case FundConfig.DATA_OPEN:
			List<Opens> openss = fl.getOpensList();
			Opens opens;
			for (int i = 0; i < openss.size(); i++) {// 转换成fundvalue
				opens = openss.get(i);
				bean = new NetWorthBean();
				bean.setJjdm(opens.getJjdm());
				bean.setJzrq(opens.getJzrq());
				bean.setJjjz(opens.getJjjz());
				bean.setLjjz(opens.getLjjz());
				bean.setHbdr(opens.getHbdr());
				bean.setHb3y(opens.getHb3Y());
				bean.setHb6y(opens.getHb6Y());
				bean.setHb1n(opens.getHb1N());
				bean.setHbjn(opens.getHbjn());
				bean.setZfxz(opens.getZfxz());
				bean.setHb1y(opens.getHb1Y());
				list.add(bean);
			}
			break;
		}
		return list;
	}

	/**
	 * 加入自选
	 * 
	 * @param context
	 * @param jjdm
	 * @param status
	 * @param nowExe
	 */
	public static void updateOptional(Context context, String jjdm, int status, boolean nowExe) {
		// LocalBroadcastManager.getInstance(context).sendBroadcastSync(intent);
		if (status == SelfConfig.UNSynsAdd) {
			Toast.makeText(context, "添加自选成功", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(context, "删除自选成功", Toast.LENGTH_SHORT).show();
		}
		updateOptionalNoToast(context, jjdm, status, nowExe);
	}

	/**
	 * 加入自选
	 * 
	 * @param context
	 * @param jjdm
	 * @param status
	 * @param nowExe
	 */
	public static void updateOptionalNoToast(Context context, String jjdm, int status,
			boolean nowExe) {
		Intent intent = new Intent();
		intent.putExtra(ValConfig.IT_TYPE, nowExe ? SelfConfig.IT_UPDATE_EXECUE
				: SelfConfig.IT_UPDATE);
		intent.putExtra(ValConfig.IT_ID, jjdm);
		intent.putExtra(ValConfig.IT_NAME, String.valueOf(status));
		OptionalMger.getMger().handleOpt(context, intent);
	}

	/**
	 * 自选入库
	 * 
	 * @param context
	 * @param jjdm
	 * @param status
	 */
	public static void exeOptional(Context context) {
		Intent intent = new Intent();
		intent.putExtra(ValConfig.IT_TYPE, SelfConfig.IT_EXECUE);
		OptionalMger.getMger().handleOpt(context, intent);
	}

	private static Intent buildFundDetail(Context cx, Object obj, int arg) {
		Intent t = new Intent(cx, AtyEmpty.class);
		Bundle b = new Bundle();
		if (obj instanceof NetWorthBean) {
			b.putSerializable(ValConfig.IT_ENTITY, (NetWorthBean) obj);
		} else {
			b.putString(ValConfig.IT_ID, String.valueOf(obj));
		}
		b.putInt(ValConfig.IT_URL, arg);
		b.putString(ValConfig.IT_NAME, "基金详情");
		t.putExtra(AtyEmpty.KEY_FRAG_NAME, FragDetailsFund.class.getName());
		t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
		return t;
	}

	/**
	 * @param frag
	 * @param obj
	 *            基金code或者NetWorthBean
	 * @param arg
	 * @param requestCode
	 *            大于0时,会调用startActivityForResult
	 */
	public static void launchFundDetails(AbsHbFrag frag, Object obj, int arg, int requestCode) {
		Intent t = buildFundDetail(frag.getSherlockActivity(), obj, arg);
		if (requestCode > 0) {
			frag.startActivityForResult(t, requestCode);
		} else {
			frag.startActivity(t);
		}
	}

	/**
	 * @param frag
	 * @param obj
	 *            基金code或者NetWorthBean
	 * @param arg
	 * @param requestCode
	 *            大于0时,会调用startActivityForResult
	 */
	public static void launchFundDetails(FragmentActivity aty, Object obj, int arg, int requestCode) {
		Intent t = buildFundDetail(aty, obj, arg);
		if (requestCode > 0) {
			aty.startActivityForResult(t, requestCode);
		} else {
			aty.startActivity(t);
		}
	}

	private static Intent buildFundSearch(Context cx, int arg) {
		Intent t = new Intent(cx, AtyEmpty.class);
		Bundle b = new Bundle();
		b.putInt(ValConfig.IT_TYPE, arg);
		b.putString(ValConfig.IT_NAME, "搜索");
		t.putExtra(AtyEmpty.KEY_FRAG_NAME, FragSearch.class.getName());
		t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
		return t;
	}

	/**
	 * @param frag
	 * @param arg
	 * @param requestCode
	 *            大于0时,会调用startActivityForResult
	 */
	public static void launchFundSearch(AbsFrag aty, int arg, int requestCode) {
		Intent t = buildFundSearch(aty.getSherlockActivity(), arg);
		if (requestCode > 0) {
			aty.startActivityForResult(t, requestCode);
		} else {
			aty.startActivity(t);
		}
	}

	/**
	 * @param frag
	 * @param arg
	 * @param requestCode
	 *            大于0时,会调用startActivityForResult
	 */
	public static void launchFundSearch(FragmentActivity aty, int arg, int requestCode) {
		Intent t = buildFundSearch(aty, arg);
		if (requestCode > 0) {
			aty.startActivityForResult(t, requestCode);
		} else {
			aty.startActivity(t);
		}
	}

	public static String readUserPhone() {
		String phone = UserInf.getUser().getUserPhone();
		if (StrUtils.isEmpty(phone)) {
			phone = AppFrame.getApp().getsF().getString(ValConfig.SFUserPhone, null);
		}
		return phone;
	}
}
