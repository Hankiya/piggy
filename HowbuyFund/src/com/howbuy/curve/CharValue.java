package com.howbuy.curve;

import java.util.ArrayList;

import android.database.Cursor;

import com.howbuy.db.DbUtils;
import com.howbuy.db.DbUtils.SqlExeObj;
import com.howbuy.entity.CharRequest;
import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.wireless.entity.protobuf.HistoryFundNetValueChartProtos.HistoryFundNetValueChart;
import com.howbuy.wireless.entity.protobuf.HistoryFundNetValueChartProtos.NetValueInfo;
import com.howbuy.wireless.entity.protobuf.HistoryFundNetValueChartProtos.PartitionInfo;
import com.howbuy.wireless.entity.protobuf.HistoryFundNetValueChartProtos.ShareInfo;

public class CharValue {
	// 画图贷币时用七日年化,其它为基金净值.
	public static final int TYPE_JJJZ = 1;// 基金净值.代币型时为万份收益.
	public static final int TYPE_DWFH = 2;// 单位分红
	public static final int TYPE_FCBL = 4;// 分拆比例（如：1：2）
	public String value;
	public String date;
	public String increase;// 单位涨幅（货币或理财型基金时下发七日年化这个字段，封闭式为周增幅，私募为月增幅，其他为日增幅）
	public int type;

	public CharValue(String value, String increase, String date, int type) {
		this.value = value;
		this.increase = increase;
		this.date = date;
		this.type = type;
	}

	/**
	 * 无数据时返回NULL; valType==0 时为全部类型,否则为指定类型.
	 * 
	 * @throws WrapException
	 */
	public static ArrayList<ArrayList<CharValue>> load(CharRequest opt, String code, int... valType)
			throws WrapException {
		int valLen = valType == null ? 0 : valType.length;
		StringBuilder sb = new StringBuilder(128);
		sb.append("select value,increase,date,type from tb_charvalue where code='");
		sb.append(code).append("'");
		if (valLen > 0) {
			sb.append("  and type in(");
			for (int i = 0; i < valLen; i++) {
				sb.append("'").append(valType[i]).append("',");
			}
			sb.deleteCharAt(sb.length() - 1).append(")");
		}
		if (opt.getCharCount() > 0) {
			sb.append(" order by date desc limit ").append(opt.getCharCount());
		} else {
			sb.append(" and date between '").append(opt.StartTime).append("' and '")
					.append(opt.EndTime).append("'");
			sb.append(" order by date desc");
		}
		Cursor c = DbUtils.query(sb.toString(), null, false);
		sb.append(",load code ").append(code);
		if (c != null && c.moveToFirst()) {// have data.
			CharValue t = null;
			ArrayList<ArrayList<CharValue>> r = new ArrayList<ArrayList<CharValue>>(3);
			for (int i = 0; i < 3; i++) {
				r.add(new ArrayList<CharValue>());
			}
			do {
				t = new CharValue(c.getString(0), c.getString(1), c.getString(2), c.getInt(3));
				switch (t.type) {
				case TYPE_JJJZ:
					r.get(0).add(t);
					break;
				case TYPE_DWFH:
					r.get(1).add(t);
					break;
				case TYPE_FCBL:
					r.get(2).add(t);
					break;
				}
			} while (c.moveToNext());
			ArrayList<CharValue> rj = r.get(0);
			sb.append(",JJJZ SIZE=" + rj.size());
			if (rj.size() > 0) {
				sb.append(" from ").append(rj.get(rj.size() - 1).date);
				sb.append(" to ").append(rj.get(0).date);
			}
			LogUtils.d("loader", sb.toString());
			return r;
		}
		sb.append("empty");
		LogUtils.d("loader", sb.toString());
		return null;
	}

	public static ArrayList<ArrayList<CharValue>> save(HistoryFundNetValueChart r, String code) {
		int a = r.getNetValueInfoCount();
		int b = r.getShareInfoCount();
		int c = r.getPartitionInfoCount();
		ArrayList<ArrayList<CharValue>> list = null;
		if (a + b + c > 0) {
			list = new ArrayList<ArrayList<CharValue>>(4);
			for (int i = 0; i < 3; i++) {
				list.add(new ArrayList<CharValue>());
			}
		}
		String sql = "insert or replace into  tb_charvalue(code,value,increase,date,type) values(?,?,?,?,?)";
		StringBuffer sb = new StringBuffer();
		if (a > 0) {
			sb.append("save code " + code).append(", from ")
					.append(r.getNetValueInfo(a - 1).getJzrq());
			sb.append(" to ").append(r.getNetValueInfo(0).getJzrq());
		}
		ArrayList<SqlExeObj> ls = new ArrayList<DbUtils.SqlExeObj>(a + 1);
		for (int i = 0; i < a; i++) {
			NetValueInfo v = r.getNetValueInfo(i);
			String date = v.getJzrq();
			String increase = v.getHbxx();
			ls.add(new SqlExeObj(sql, new Object[] { code, v.getJjjz(), increase, date,
					CharValue.TYPE_JJJZ }));
			list.get(0).add(new CharValue(v.getJjjz(), increase, date, CharValue.TYPE_JJJZ));
		}
		for (int i = 0; i < b; i++) {
			ShareInfo v = r.getShareInfo(i);
			ls.add(new SqlExeObj(sql, new Object[] { code, v.getDwfh(), null, v.getFhrq(),
					CharValue.TYPE_DWFH }));
			list.get(1).add(new CharValue(v.getDwfh(), null, v.getFhrq(), CharValue.TYPE_DWFH));
		}

		for (int i = 0; i < c; i++) {
			PartitionInfo v = r.getPartitionInfo(i);
			ls.add(new SqlExeObj(sql, new Object[] { code, v.getFcbl(), null, v.getFcrq(),
					CharValue.TYPE_FCBL }));
			list.get(2).add(new CharValue(v.getFcbl(), null, v.getFcrq(), CharValue.TYPE_FCBL));
		}
		WrapException err = DbUtils.exeSql(ls, true);
		sb.append(",save result err=" + err);
		LogUtils.d("loader", sb.toString());
		return list;
	}

	@Override
	public String toString() {
		return "CharValue [value=" + value + ", date=" + date + ", increase=" + increase
				+ ", type=" + type + "]";
	}

}
