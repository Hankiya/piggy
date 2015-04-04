package com.howbuy.frag;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import com.howbuy.component.DbUtils;
import com.howbuy.component.DbUtils.SqlExeObj;
import com.howbuy.entity.NetValue;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.frag.AbsFrag;
import com.howbuy.lib.interfaces.ICursorCalbak;
import com.howbuy.lib.utils.FileUtils;
import com.howbuy.lib.utils.StreamUtils;
import com.howbuy.libtest.R;

public class FragDbTest extends AbsFrag implements ICursorCalbak {
	private String mDbPath = "renzh_test.db";
	private String mTabCopy = "tb_copy";
	private long mQueryStartTime = 0;
	private static final int QUERY_FAST = 1;
	private static final int QUERY_ORDER = 2;
	private static final int DELETE_ALL = 3;
	private static final int INSERT_ALL = 4;
	private static final int UPDATE_ALL = 5;
	private HashMap<Integer, ArrayList<Integer>> mMapParseTimer = new HashMap<Integer, ArrayList<Integer>>();
	private HashMap<Integer, ArrayList<Integer>> mMapExecuteTime = new HashMap<Integer, ArrayList<Integer>>();
	private HashMap<Integer, TextView> mMapTvs = new HashMap<Integer, TextView>();
	ArrayList<NetValue> mlista = new ArrayList<NetValue>();
	private StringBuffer mSb = new StringBuffer(32);
	private TextView mTvRecord;

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_dbtest;
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		mTvRecord = (TextView) root.findViewById(R.id.tv_record);
		mMapTvs.put(QUERY_FAST, (TextView) root.findViewById(R.id.tv_avg1));
		mMapTvs.put(QUERY_ORDER, (TextView) root.findViewById(R.id.tv_avg2));
		mMapTvs.put(DELETE_ALL, (TextView) root.findViewById(R.id.tv_avg3));
		mMapTvs.put(INSERT_ALL, (TextView) root.findViewById(R.id.tv_avg4));
		mMapTvs.put(UPDATE_ALL, (TextView) root.findViewById(R.id.tv_avg5));
		mDbPath = GlobalApp.getApp().getDatabasePath(mDbPath).getAbsolutePath();
		checkDbAndCopyToLocal("HBFunds2.db");
		DbUtils.setCurrentDb("renzh_test.db");
		resetCopyDb();
	}

	private void resetCopyDb() {
		String sqlDel = "drop table " + mTabCopy;
		String sql = "create table " + mTabCopy + " as select * from NetValue";
		WrapException e = DbUtils.exeSql(sqlDel);
		if (e != null) {
		}
		e = DbUtils.exeSql(sql);
		if (e != null) {
		}
	}

	private String getType(int type) {
		switch (type) {
		case QUERY_FAST:
			return "QUERY_FAST";
		case QUERY_ORDER:
			return "QUERY_ORDER";
		case DELETE_ALL:
			return "DELETE_ALL";
		case INSERT_ALL:
			return "INSERT_ALL";
		case UPDATE_ALL:
			return "UPDATE_ALL";
		}
		return null;
	}

	private void addDbOpTime(int type, int parseTime, int opTime) {
		String typeStr = getType(type);
		mSb.append(typeStr).append(" parse:").append(parseTime)
				.append(" execute:").append(opTime).append(" >")
				.append(parseTime + opTime).append("\r\n");
		if (!mMapParseTimer.containsKey(type)) {
			mMapParseTimer.put(type, new ArrayList<Integer>());
			mMapExecuteTime.put(type, new ArrayList<Integer>());
		}
		mMapParseTimer.get(type).add(parseTime);
		mMapExecuteTime.get(type).add(opTime);
		int[] res = new int[3];
		getAvgTime(type, res);
		mTvRecord.setText(mSb.toString());
		mMapTvs.get(type).setText(
				typeStr + " parse=" + res[0] + " execute=" + res[1] + " >"
						+ (res[0] + res[1]) + " in " + res[2] + " times");
	}

	private void getAvgTime(int type, int[] result) {
		ArrayList<Integer> a = mMapParseTimer.get(type);
		ArrayList<Integer> b = mMapExecuteTime.get(type);
		int n = a == null ? 0 : a.size();
		result[0] = result[1] = 0;
		for (int i = 0; i < n; i++) {
			result[0] += a.get(i);
			result[1] += b.get(i);
		}
		if (n != 0) {
			result[0] /= n;
			result[1] /= n;
		}
		result[2] = n;
	}

	private void cleanRecord() {
		mQueryStartTime = SystemClock.currentThreadTimeMillis();
		mSb.delete(0, mSb.length());
		mMapExecuteTime.clear();
		mMapParseTimer.clear();
		mQueryStartTime = 0;
		mTvRecord.setText(null);
		mMapTvs.get(QUERY_FAST).setText(null);
		mMapTvs.get(QUERY_ORDER).setText(null);
		mMapTvs.get(DELETE_ALL).setText(null);
		mMapTvs.get(INSERT_ALL).setText(null);
		mMapTvs.get(UPDATE_ALL).setText(null);
	}

	@Override
	public boolean onXmlBtClick(View v) {
		int type = -1;
		switch (v.getId()) {
		case R.id.bt_query_fast:
			type = QUERY_FAST;
			break;
		case R.id.bt_query_order:
			type = QUERY_ORDER;
			break;
		case R.id.bt_delall:
			type = DELETE_ALL;
			break;
		case R.id.bt_insert:
			type = INSERT_ALL;
			break;
		case R.id.bt_update:
			type = UPDATE_ALL;
			break;
		case R.id.bt_clean:
			cleanRecord();
			break;
		case R.id.bt_resetdb:
			resetCopyDb();
			break;
		}
		if (type != -1) {
			startQuery(type);
			return true;
		}
		return false;
	}

	private void startQuery(int type) {
		if (mQueryStartTime > 0) {
			pop("等待前一个查询返回", true);
			return;
		}
		WrapException e = null;
		mQueryStartTime = SystemClock.currentThreadTimeMillis();
		switch (type) {
		case QUERY_FAST: {
			DbUtils.query("" + type, "select * from " + mTabCopy, this);
			break;
		}

		case QUERY_ORDER: {
			DbUtils.query("" + type, "select * from " + mTabCopy
					+ " group by jzrq order by ljjz, ljjz DESC;", this);
			break;
		}
		case DELETE_ALL: {
			long queryTime = SystemClock.currentThreadTimeMillis()
					- mQueryStartTime;
			e = DbUtils.exeSql("delete from " + mTabCopy);
			addDbOpTime(type, 0, (int) queryTime);
			onDbOperatored(type, e);
			break;
		}
		case INSERT_ALL: {
			e = DbUtils.exeSql("delete from " + mTabCopy);
			mQueryStartTime = SystemClock.currentThreadTimeMillis();

			long parseTime = 0, queryTime = 0;
			ArrayList<SqlExeObj> sqls = new ArrayList<SqlExeObj>();
			for (int i = 0; i < mlista.size(); i++) {
				NetValue r = mlista.get(i);
				SqlExeObj obj = new SqlExeObj(
						"insert into "
								+ mTabCopy
								+ " ( jjdm,jzrq,jjjz, ljjz,zfxz,hbdr,hb1Y,hb3Y,hb6Y,hb1N,qrsy,wfsy ) values(?,?,?,?,?,?,?,?,?,?,?,?)",
						new Object[] { r.jjdm, r.jzrq, r.jjjz, r.ljjz, r.zfxz,
								r.hbdr, r.hb1Y, r.hb3Y, r.hb6Y, r.hb1N, r.qrsy,
								r.wfsy });
				sqls.add(obj);
			}
			parseTime = SystemClock.currentThreadTimeMillis() - mQueryStartTime;
			e = DbUtils.exeSql(sqls);
			queryTime = SystemClock.currentThreadTimeMillis() - mQueryStartTime
					- parseTime;
			dd("查询时间：%1$d ms 解析时间：%2$d ms 总时间：%3$d ms", queryTime, parseTime,
					queryTime + parseTime);
			addDbOpTime(type, (int) parseTime, (int) queryTime);
			onDbOperatored(type, e);
			break;
		}
		case UPDATE_ALL: {
			long parseTime = 0, queryTime = 0;
			ArrayList<SqlExeObj> sqls = new ArrayList<SqlExeObj>();
			for (int i = 0; i < mlista.size(); i++) {
				NetValue r = mlista.get(i);
				SqlExeObj obj = new SqlExeObj("update " + mTabCopy
						+ " set jjjz=?, ljjz=?,zfxz=? where jjdm=?  ",
						new Object[] { r.jjjz * 2, r.ljjz * 2, r.zfxz * 2,
								r.jjdm });
				sqls.add(obj);
			}
			parseTime = SystemClock.currentThreadTimeMillis() - mQueryStartTime;
			e = DbUtils.exeSql(sqls);
			queryTime = SystemClock.currentThreadTimeMillis() - mQueryStartTime
					- parseTime;
			dd("查询时间：%1$d ms 解析时间：%2$d ms 总时间：%3$d ms", queryTime, parseTime,
					queryTime + parseTime);
			addDbOpTime(type, (int) parseTime, (int) queryTime);
			onDbOperatored(type, e);
			break;
		}
		}

	}

	private void checkDbAndCopyToLocal(String fileName) {
		File f = new File(mDbPath);
		if (!f.exists()) {
			try {
				StreamUtils.copyStream(
						getSherlockActivity().getAssets().open(fileName),
						FileUtils.getOutStream(f, false));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void onDbOperatored(int type, WrapException e) {
		if (e != null) {
		}
		resetCopyDb();
		mQueryStartTime = 0;
	}

	@Override
	public void getCursor(String key, Cursor c, WrapException e) {
		if (e == null) {
			long queryTime = SystemClock.currentThreadTimeMillis()
					- mQueryStartTime;
			ArrayList<NetValue> mlist = NetValue.getNetValues(c);
			long parseTime = SystemClock.currentThreadTimeMillis()
					- mQueryStartTime - queryTime;
			addDbOpTime(Integer.parseInt(key), (int) parseTime, (int) queryTime);
			d(null,"列表长度：="
					+ mlist.size()
					+ String.format("查询时间：%1$d ms 解析时间：%2$d ms 总时间：%3$d ms",
							queryTime, parseTime, queryTime + parseTime));
			if ((QUERY_FAST + "").equals(key)) {
				if (mlist == null || mlista.size() == 0) {
					mlista = mlist;
				}
			}
		}
		onDbOperatored(Integer.parseInt(key), e);
	}

	@Override
	public void onPause() {
		super.onPause();
		DbUtils.setCurrentDb(null);
	}

	@Override
	public void onResume() {
		super.onResume();
		DbUtils.setCurrentDb("renzh_test.db");
	}

}
