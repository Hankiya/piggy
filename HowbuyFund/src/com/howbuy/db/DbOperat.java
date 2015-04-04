package com.howbuy.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.howbuy.config.FundConfig;
import com.howbuy.config.SelfConfig;
import com.howbuy.db.DbUtils.SqlExeObj;
import com.howbuy.entity.NetWorthBean;
import com.howbuy.entity.NetWorthListBean;
import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.utils.FundUtils;
import com.howbuy.utils.SearchUtil;
import com.howbuy.wireless.entity.protobuf.ClosesNewProtos.ClosesNew;
import com.howbuy.wireless.entity.protobuf.FundBasicInfoProto.FundBasicInfoList;
import com.howbuy.wireless.entity.protobuf.FundBasicProtos.FundBasic;
import com.howbuy.wireless.entity.protobuf.FundInfoClosesNewProto.FundInfoClosesNew;
import com.howbuy.wireless.entity.protobuf.FundInfoMoneysProto.FundInfoMoneys;
import com.howbuy.wireless.entity.protobuf.FundInfoOpensProto.FundInfoOpens;
import com.howbuy.wireless.entity.protobuf.FundInfoSimusProto.FundInfoSimus;
import com.howbuy.wireless.entity.protobuf.FundInfosListProto.FundInfosList;
import com.howbuy.wireless.entity.protobuf.MoneysProtos.Moneys;
import com.howbuy.wireless.entity.protobuf.OpensProtos.Opens;
import com.howbuy.wireless.entity.protobuf.SimusProtos.Simus;

public class DbOperat {
	private static DbOperat dbOInstance;
	private static String TAG = "DbOperat";

	final protected void d(String title, String msg) {
		msg=msg+Thread.currentThread().getId();
		if (title == null) {
			LogUtils.d(TAG, msg);
		} else {
			LogUtils.d(TAG, title + " -->" + msg);
		}
	}

	private DbOperat() {
	}

	public static synchronized DbOperat getInstance() {
		if (dbOInstance == null) {
			dbOInstance = new DbOperat();
		}
		return dbOInstance;
	}

	public static String formatWfsyToHbdr(String wfsy) {
		try {
			if (StrUtils.isEmpty(wfsy)) {
				return wfsy;
			} else {
				float wf = Float.parseFloat(wfsy) / 100f;
				return wf + "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	/**
	 * 更新基金基本信息
	 */
	public boolean updateBasicFundInfo(FundBasicInfoList fBasicInfo, boolean isSimu) {
		String typeSimu = FundConfig.TYPE_SIMU;
		String updateSql = "insert or replace into FundsInfo(code,name,pinyin,type,state,unit_netvalue,status,jjfl2,tradeflag,mbflag,found_date,xuan) values(?,?,?,?,?,?,?,?,?,?,?,(select xuan from fundsinfo where code=?))";
		ArrayList<SqlExeObj> list = new ArrayList<DbUtils.SqlExeObj>();
		for (int i = 0; i < fBasicInfo.getFundBasicCount(); i++) {
			FundBasic fBasic = fBasicInfo.getFundBasic(i);
			if (fBasic.getJjdm().equals("160213")) {
				Log.d("database", "160213" + fBasic.getJjjc());
			}
			if (isSimu) {
				typeSimu = FundConfig.TYPE_SIMU;
			} else {
				typeSimu = fBasic.getJjfl();
			}
			list.add(new SqlExeObj(updateSql, new Object[] { fBasic.getJjdm(), fBasic.getJjjc(),
					fBasic.getJjpy(), typeSimu, fBasic.getJyzt(), fBasic.getJzdw(),
					fBasic.getStatus(), fBasic.getJjfl2(), fBasic.getHbTradeFlag(),
					fBasic.getMbFlag(), fBasic.getFoundDate(), fBasic.getJjdm() }));
		}
		WrapException w = DbUtils.exeSql(list, false);
		if (w == null) {
			return true;
		}
		return false;
	}

	/*
	 * public WrapException updateNetValue(NetWorthBean b) { SqlExeObj sql = new
	 * SqlExeObj(
	 * "insert or replace into NetValue(jjdm,jzrq,jjjz,ljjz,hbdr,hb3y,hb6y,hb1n,hb3n,hbjn,zfxz,hb1y) values(?,?,?,?,?,?,?,?,?,?,?)"
	 * , new Object[] { b.getJjdm(), b.getJzrq(), b.getJjjz(), b.getLjjz(),
	 * b.getHbdr(), b.getHb3y(), b.getHb6y(), b.getHb1n(), b.getHb3n(),
	 * b.getHbjn(), b.getZfxz(), b.getHb1y() }); return DbUtils.exeSql(sql,
	 * true); }
	 */

	/**
	 * 更新净值数据库
	 * 
	 * @param vObject
	 * @return
	 */
	public boolean updateNetValue(Object vObject, int dataType, boolean isLastSql) {// FundBasicInfoList
		ArrayList<SqlExeObj> r = new ArrayList<SqlExeObj>();
		if (vObject instanceof FundInfoOpens) {
			FundInfoOpens opens = (FundInfoOpens) vObject;
			buildOpensSql(r, opens.getOpensList());

		} else if (vObject instanceof FundInfoMoneys) {
			FundInfoMoneys money = (FundInfoMoneys) vObject;
			buildMoneySql(r, money.getMoneysList());
		} else if (vObject instanceof FundInfoSimus) {
			FundInfoSimus simus = (FundInfoSimus) vObject;
			buildSimuSql(r, simus.getSimusList());
		} else if (vObject instanceof FundInfoClosesNew) {
			FundInfoClosesNew close = (FundInfoClosesNew) vObject;
			buildCloseSql(r, close.getClosesNewList());
		}
		return r.isEmpty() ? true : DbUtils.exeSql(r, false) == null;
	}

	public WrapException updateNetValue(FundInfosList f) {
		ArrayList<SqlExeObj> r = new ArrayList<SqlExeObj>();
		if (f.getOpensCount() > 0) {
			buildOpensSql(r, f.getOpensList());
		}
		if (f.getMoneysCount() > 0) {
			buildMoneySql(r, f.getMoneysList());
		}
		if (f.getSimusCount() > 0) {
			buildSimuSql(r, f.getSimusList());
		}
		if (f.getClosesNewCount() > 0) {
			buildCloseSql(r, f.getClosesNewList());
		}
		return r.isEmpty() ? null : DbUtils.exeSql(r, false);
	}

	private void buildOpensSql(ArrayList<SqlExeObj> r, List<Opens> l) {
		int n = l.size();
		d("updateNetValue", "write Opens to db.NetValue size=" + n);
		String sql = "insert or replace into NetValue(jjdm,jzrq,jjjz,ljjz,hbdr,hb1y,hb3y,hb6y,hb1n,hbjn,zfxz) values(?,?,?,?,?,?,?,?,?,?,?)";
		for (int i = 0; i < n; i++) {
			Opens o = l.get(i);
			r.add(new SqlExeObj(sql, new Object[] { o.getJjdm(), o.getJzrq(), o.getJjjz(),
					o.getLjjz(), o.getHbdr(), o.getHb1Y(), o.getHb3Y(), o.getHb6Y(), o.getHb1N(),
					o.getHbjn(), o.getZfxz() }));
		}
	}

	private void buildMoneySql(ArrayList<SqlExeObj> r, List<Moneys> l) {
		int n = l.size();
		d("updateNetValue", "write Moneys to db.NetValue size=" + n);
		String sql = "insert or replace into NetValue(jjdm,jzrq,jjjz,ljjz,hbdr,hb1y,hb3y,hb6y,hb1n,hbjn,wfsy,qrsy,zfxz) values(?,?,1,1,?,?,?,?,?,?,?,?,?)";
		for (int i = 0; i < n; i++) {
			Moneys m = l.get(i);
			r.add(new SqlExeObj(sql, new Object[] { m.getJjdm(), m.getJzrq(),
					formatWfsyToHbdr(m.getWfsy()), m.getHb1Y(), m.getHb3Y(), m.getHb6Y(),
					m.getHb1N(), m.getHbjn(), m.getWfsy(), m.getQrsy(), m.getZfxz() }));
		}
	}

	private void buildCloseSql(ArrayList<SqlExeObj> r, List<ClosesNew> l) {
		int n = l.size();
		d("updateNetValue", "write ClosesNew to db.NetValue size=" + n);
		String sql = "insert or replace into NetValue(jjdm,jzrq,jjjz,ljjz,hbdr,hb1y,hb3y,hb6y,hb1n,hbjn,zfxz) values(?,?,?,?,?,?,?,?,?,?,?)";
		for (int i = 0; i < n; i++) {
			ClosesNew c = l.get(i);
			r.add(new SqlExeObj(sql, new Object[] { c.getJjdm(), c.getJzrq(), c.getJjjz(),
					c.getLjjz(), c.getHbdr(), c.getHb1Y(), c.getHb3Y(), c.getHb6Y(), c.getHb1N(),
					c.getHbjn(), c.getZfxz() }));
		}
	}

	private void buildSimuSql(ArrayList<SqlExeObj> r, List<Simus> l) {
		int n = l.size();
		d("updateNetValue", "write Simus to db.NetValue size=" + n);
		String sql = "insert or replace into NetValue(jjdm,jzrq,jjjz,ljjz,hb1y,hb6y,hb1n,hb3n,hbjn) values(?,?,?,?,?,?,?,?,?)";
		for (int i = 0; i < n; i++) {
			Simus s = l.get(i);
			r.add(new SqlExeObj(sql, new Object[] { s.getJjdm(), s.getJzrq(), s.getJjjz(),
					s.getLjjz(), s.getHb1Y(), s.getHb6Y(), s.getHb1N(), s.getHb3N(), s.getHbjn() }));
		}
	}

	/**
	 * 添加自选 operat
	 * 
	 * @param jjdm
	 * @param xuanStatus
	 * @throws Exception
	 */
	public boolean updateOptional(String jjdm, String xuanStatus) throws Exception {
		// 增加自选的时间
		String xuanTime = String.valueOf(System.currentTimeMillis() / 1000l);
		String sql = "update FundsInfo set xuan=? where code=?";
		String sql2 = "insert or replace into FundsInfoOpt(code,xuantime,postion) values(?,?,?)";
		WrapException w = DbUtils.exeSqlNoTrans(new SqlExeObj(sql,
				new Object[] { xuanStatus, jjdm }), false);
		DbUtils.exeSqlNoTrans(new SqlExeObj(sql2, new Object[] { jjdm, xuanTime, 0 }), false);
		return w == null;
	}

	/**
	 * 
	 * @param conditiion
	 *            自己加and开头.
	 * @param distinctDate
	 *            为0时按所有日期分组取数据。小于0时不按日期分组取数据。其它按最多前distinctDate个日期进行分组。
	 *            没有纳入分组的就在结果列表的最后一个。
	 * 
	 * @return
	 * @throws WrapException
	 */
	public ArrayList<ArrayList<NetWorthBean>> queryAll(String conditiion, int distinctDate,
			int VALUE_X, boolean mergeNullListEnd) throws WrapException {
		String recordTime = "distinct b.[jzrq]";
		String recordResult = "a.[code],a.[name],a.[pinyin],a.[type],a.[xuan],a.[tradeflag],a.[mbflag],a.[found_date],a.[unit_netvalue],b.[jzrq],b.[jjjz],b.[ljjz],b.[hbdr],b.[hb1y],b.[hb3y],b.[hb6y], b.[hb1n],b.[hb3n],b.[hbjn],b.[qrsy],b.[wfsy]";
		String baseSql = "select #### from fundsinfo a left join netvalue b on a.[code]=b.[jjdm] where a.[status]<>0 ";
		if (conditiion != null) {
			baseSql = baseSql + conditiion;
		}
		// //////////////////////////////////////////////////////////////////
		String sql = null;
		Cursor c = null;
		ArrayList<String> times = null;
		if (distinctDate >= 0) {
			sql = baseSql.replace("####", recordTime);
			int orderIndex = sql.indexOf("order by");
			if (orderIndex != -1) {
				sql = sql.substring(0, orderIndex);
			}
			sql = sql + "order by  b.[jzrq] desc";
			if (distinctDate > 0) {
				sql = sql + " limit 0," + distinctDate;
			}
			try {
				c = DbUtils.query(sql, null, false);
				if (c != null && c.moveToFirst()) {
					times = new ArrayList<String>();
					do {
						String date = c.getString(0);
						if (!StrUtils.isEmpty(date)) {
							times.add(date);
						}
					} while (c.moveToNext());
					d("queryAll", "query distinct date result=" + Arrays.toString(times.toArray()));
				}

			} catch (Exception e) {
				throw WrapException.wrap(e, sql);
			} finally {
				if (c != null && !c.isClosed()) {
					c.close();
				}
			}
		}

		// /////////////////////////////////////////////////////////////////////////////////////////////////
		sql = baseSql.replace("####", recordResult);
		d("queryAll", "sql=" + sql + "\n\t");
		try {
			c = DbUtils.query(sql, null, false);
			int size = times == null ? 0 : times.size();
			ArrayList<ArrayList<NetWorthBean>> lls = new ArrayList<ArrayList<NetWorthBean>>(
					size + 1);
			if (c != null && c.moveToFirst()) {
				ArrayList<NetWorthBean> nulist = new ArrayList<NetWorthBean>();
				int[] indexs = new int[size + 1];
				for (int i = 0; i <= size; i++) {
					lls.add(new ArrayList<NetWorthBean>());
					indexs[i] = -1;
				}
				do {
					String code = c.getString(0);
					String name = c.getString(1);
					String pinyin = c.getString(2);
					String type = c.getString(3);
					int xuan = c.getInt(4);
					int tradeflag = c.getInt(5);
					int mbflag = c.getInt(6);
					String date = c.getString(7);
					String danwei = c.getString(8);
					String jzrq = c.getString(9);
					String jjjz = c.getString(10);
					String ljjz = c.getString(11);
					String hbdr = c.getString(12);
					String hb1y = c.getString(13);
					String hb3y = c.getString(14);
					String hb6y = c.getString(15);
					String hb1n = c.getString(16);
					String hb3n = c.getString(17);
					String hbjn = c.getString(18);
					String qrsy = c.getString(19);
					String wfsy = c.getString(20);
					NetWorthBean b = new NetWorthBean();
					b.setJjdm(code);
					b.setJjmc(name);
					b.setJjfl(type);
					b.setPinyin(pinyin);
					b.setXunan(xuan);
					b.setHbTradFlag(tradeflag);
					b.setMbFlag(mbflag);
					b.setFoundDate(date);
					b.setDanWei(danwei);
					b.setJzrq(jzrq);
					b.setJjjz(jjjz);
					b.setLjjz(ljjz);
					b.setHbdr(hbdr);
					b.setHb1y(hb1y);
					b.setHb3y(hb3y);
					b.setHb6y(hb6y);
					b.setHb1n(hb1n);
					b.setHb3n(hb3n);
					b.setHbjn(hbjn);
					b.setQrsy(qrsy);
					b.setWfsy(wfsy);
					if (VALUE_X != 0) {
						if (StrUtils.isEmpty(FundUtils.pickFundValue(b, VALUE_X))) {
							nulist.add(b);
							continue;
						}
					}
					if (times == null) {
						b.setSortIndex(indexs[0] = indexs[0] + 1);
						lls.get(0).add(b);
					} else {
						int i = 0;
						for (i = 0; i < size; i++) {
							if (times.get(i).equals(jzrq)) {
								b.setSortIndex(indexs[i] = indexs[i] + 1);
								lls.get(i).add(b);
								break;
							}
						}
						if (i == size) {
							b.setSortIndex(indexs[size] = indexs[size] + 1);
							lls.get(size).add(b);
						}
					}
				} while (c.moveToNext());
				if (mergeNullListEnd) {
					if (nulist.size() > 0) {
						int n = nulist.size();
						for (int j = 0; j < n; j++) {
							nulist.get(j).setSortIndex(indexs[size] = indexs[size] + 1);
						}
						if (size == 0) {
							lls.get(0).addAll(nulist);
						} else {
							lls.get(size).addAll(nulist);
						}
					}
				} else {
                   lls.add(nulist);
				}
			}

			return lls;
		} catch (Exception e) {
			throw WrapException.wrap(e, sql);
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
			}
		}
	}

	public ArrayList<NetWorthBean> queryAll(String conditiion) throws WrapException {
		StringBuffer sb = new StringBuffer(512);
		sb.append("select a.[code],a.[name],a.[pinyin],a.[type],a.[xuan],a.[tradeflag],a.[mbflag],a.[found_date],a.[unit_netvalue],");
		sb.append("b.[jzrq],b.[jjjz],b.[ljjz],b.[hbdr],b.[hb1y],b.[hb3y],b.[hb6y], b.[hb1n], b.[hb3n],b.[hbjn],b.[qrsy],b.[wfsy] ");
		sb.append("from fundsinfo a left join netvalue b on a.[code]=b.[jjdm] where a.[status]<>0 ");
		if (conditiion != null) {
			sb.append(conditiion);
		}
		Cursor c = null;
		String sql = sb.toString();
		try {
			d("queryAll", "sql=" + sql);
			c = DbUtils.query(sql, null, false);
			ArrayList<NetWorthBean> lls = new ArrayList<NetWorthBean>();
			if (c != null && c.moveToFirst()) {
				do {
					String code = c.getString(0);
					String name = c.getString(1);
					String pinyin = c.getString(2);
					String type = c.getString(3);
					int xuan = c.getInt(4);
					int tradeflag = c.getInt(5);
					int mbflag = c.getInt(6);
					String date = c.getString(7);
					String danwei = c.getString(8);
					String jzrq = c.getString(9);
					String jjjz = c.getString(10);
					String ljjz = c.getString(11);
					String hbdr = c.getString(12);
					String hb1y = c.getString(13);
					String hb3y = c.getString(14);
					String hb6y = c.getString(15);
					String hb1n = c.getString(16);
					String hb3n = c.getString(17);
					String hbjn = c.getString(18);
					String qrsy = c.getString(19);
					String wfsy = c.getString(20);
					NetWorthBean b = new NetWorthBean();
					b.setJjdm(code);
					b.setJjmc(name);
					b.setJjfl(type);
					b.setPinyin(pinyin);
					b.setXunan(xuan);
					b.setHbTradFlag(tradeflag);
					b.setMbFlag(mbflag);
					b.setFoundDate(date);
					b.setDanWei(danwei);
					b.setJzrq(jzrq);
					b.setJjjz(jjjz);
					b.setLjjz(ljjz);
					b.setHbdr(hbdr);
					b.setHb1y(hb1y);
					b.setHb3y(hb3y);
					b.setHb6y(hb6y);
					b.setHb1n(hb1n);
					b.setHb3n(hb3n);
					b.setHbjn(hbjn);
					b.setQrsy(qrsy);
					b.setWfsy(wfsy);
					lls.add(b);
				} while (c.moveToNext());
			}
			return lls;
		} catch (Exception e) {
			throw WrapException.wrap(e, sql);
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
			}
		}
	}

	/**
	 * 查询自选列表
	 * 
	 * @return 为空代表没有值或者查询错误
	 */
	public NetWorthListBean queryOption(String sqlScript) throws WrapException {
		Cursor cursor = DbUtils.query(sqlScript, null, false);
		if (cursor != null && cursor.moveToFirst()) {
			ArrayList<NetWorthBean> list = new ArrayList<NetWorthBean>();
			NetWorthListBean listBean = new NetWorthListBean();
			do {
				// a.[code],a.[name],a.[type],a.[xuan],c.[xuantime],c.[postion],b.[jjjz],b.[jzrq],b.[hbdr]
				String jjdm = cursor.getString(0);
				String jjmc = cursor.getString(1);
				String jjfl = cursor.getString(2);
				int xuan = cursor.getInt(3);
				String xuanTime = cursor.getString(4);
				String jjjz = cursor.getString(5);
				String jzrq = cursor.getString(6);
				String hbdr = cursor.getString(7);
				String hb1y = cursor.getString(8);
				String wfsy = cursor.getString(9);
				String danwei = cursor.getString(10);
				String qrsy = cursor.getString(11);
				NetWorthBean b = new NetWorthBean();
				b.setJjdm(jjdm);
				b.setJjmc(jjmc);
				b.setJjjz(jjjz);
				b.setJzrq(jzrq);
				b.setHbdr(hbdr);
				b.setJjfl(jjfl);
				b.setXunan(xuan);
				b.setXuanTime(xuanTime);
				b.setHb1y(hb1y);
				b.setWfsy(wfsy);
				b.setDanWei(danwei);
				b.setQrsy(qrsy);
				;
				list.add(b);
			} while (cursor.moveToNext());
			listBean.addItems(list, false);
			cursor.close();
			return listBean;
		}
		return null;
	}

	/**
	 * 查询所有基金
	 * 
	 * @return
	 * @throws WrapException
	 */
	public NetWorthListBean searchAll(String keyWords, boolean isSimu) throws WrapException {
		StringBuilder sqlSb = new StringBuilder(
				"select code,name,type,xuan,pinyin from fundsinfo a where status=1");
		String orderEnd = " substr(pinyin, 0, 2),name,substr(pinyin, 2,length(pinyin))";

		if (isSimu) {
			sqlSb.append(" and type='sm' ");
		} else {
			sqlSb.append(" and type<>'sm' ");
		}
		if (!TextUtils.isEmpty(keyWords)) {
			keyWords = keyWords.toLowerCase();
			StringBuilder fuzzyKeyWords = SearchUtil.transfomKeyWords(keyWords);
			if (SearchUtil.isJustNumber(keyWords)) {// 纯数字 //代码-简称
				sqlSb.append(" and (code like '" + keyWords + "%'");
				sqlSb.append(" or code like '%" + keyWords + "%'");
				sqlSb.append(" or name like '" + keyWords + "%'");
				sqlSb.append(" or name like '%" + keyWords + "%'");
				sqlSb.append(" or name like " + fuzzyKeyWords.toString());
				sqlSb.append(" ) order by case ");
				sqlSb.append(" when code like '" + keyWords + "%' then 'a'");
				sqlSb.append(" when code like '%" + keyWords + "%' then 'b'");
				sqlSb.append(" when name like '" + keyWords + "%' then 'c'");
				sqlSb.append(" when name like '%" + keyWords + "%' then 'd'");
				sqlSb.append(" when name like " + fuzzyKeyWords + " then 'e'");
				sqlSb.append(" else 'f' end,");
			} else if (SearchUtil.isJustChar(keyWords)
					|| SearchUtil.isJustStartCharReverse(keyWords)) {// 纯字母////
																		// 数字和字母//
																		// 拼音&简称
				sqlSb.append(" and (pinyin like '" + keyWords + "%'");
				sqlSb.append(" or pinyin like '%" + keyWords + "%'");
				sqlSb.append(" or pinyin like " + fuzzyKeyWords);
				sqlSb.append(" or name like '" + keyWords + "%'");
				sqlSb.append(" or name like '%" + keyWords + "%'");
				sqlSb.append(" or name like " + fuzzyKeyWords);
				sqlSb.append(" ) order by case ");
				sqlSb.append(" when pinyin like '" + keyWords + "%' then 'a'");
				sqlSb.append(" when pinyin like '%" + keyWords + "%' then 'b'");
				sqlSb.append(" when pinyin like " + fuzzyKeyWords + " then 'c'");
				sqlSb.append(" when name like '" + keyWords + "%' then 'd'");
				sqlSb.append(" when name like '%" + keyWords + "%' then 'e'");
				sqlSb.append(" when name like " + fuzzyKeyWords + " then 'f'");
				sqlSb.append(" else 'g' end,");
			} else if (SearchUtil.isJustStartChar(keyWords)) {// 字母打头 //代码&拼音
				sqlSb.append(" and (code like '" + keyWords + "%'");
				sqlSb.append(" or code like '%" + keyWords + "%'");
				sqlSb.append(" or pinyin like '" + keyWords + "%'");
				sqlSb.append(" or pinyin like '%" + keyWords + "%'");
				sqlSb.append(" or pinyin like " + fuzzyKeyWords);
				sqlSb.append(" ) order by case ");
				sqlSb.append(" when code like '" + keyWords + "%' then 'a'");
				sqlSb.append(" when code like '%" + keyWords + "%' then 'b'");
				sqlSb.append(" when pinyin like '" + keyWords + "%' then 'c'");
				sqlSb.append(" when pinyin like '%" + keyWords + "%' then 'd'");
				sqlSb.append(" when pinyin like " + fuzzyKeyWords + " then 'e'");
				sqlSb.append(" else 'f' end,");
			} else {// 中文//简称SearchUtil.isJustChinese(keyWords)
				sqlSb.append(" and (name like '" + keyWords + "%'");
				sqlSb.append(" or name like '%" + keyWords + "%'");
				sqlSb.append(" or name like " + fuzzyKeyWords);
				sqlSb.append(" ) order by case ");
				sqlSb.append(" when name like '" + keyWords + "%' then 'a'");
				sqlSb.append(" when name like '%" + keyWords + "%' then 'b'");
				sqlSb.append(" when name like " + fuzzyKeyWords + " then 'c'");
				sqlSb.append(" else 'd' end,");
			}
			sqlSb.append(orderEnd);
		} else {
			sqlSb.append(" order by case when pinyin='' then '2' else '1' end,LOWER(pinyin)");
		}
		d("searchAll", sqlSb.toString());

		Cursor cursor = DbUtils.query(sqlSb.toString(), null, false);
		if (cursor != null && cursor.moveToFirst()) {
			ArrayList<NetWorthBean> list = new ArrayList<NetWorthBean>();
			NetWorthListBean listBean = new NetWorthListBean();
			do {
				String jjdm = cursor.getString(0);
				String jjmc = cursor.getString(1);
				String jjfl = cursor.getString(2);
				int xuan = cursor.getInt(3);
				String pinyin = cursor.getString(4);
				NetWorthBean b = new NetWorthBean();
				b.setJjdm(jjdm);
				b.setJjmc(jjmc);
				b.setJjfl(jjfl);
				b.setXunan(xuan);
				b.setPinyin(TextUtils.isEmpty(pinyin) ? "--" : pinyin.toUpperCase());
				list.add(b);

			} while (cursor.moveToNext());
			cursor.close();
			listBean.addItems(list, false);
			return listBean;
		}
		return null;
	}

	public Map<String, Integer> queryOptSum() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		String flagSm = " = 'sm'";
		String flagGm = " <> 'sm'";
		String sql = "select * from fundsinfo a where a.[xuan] in(" + SelfConfig.UNSynsAdd + ","
				+ SelfConfig.SynsShowAdd + ") and type";
		try {
			Cursor c = DbUtils.query(sql + flagGm, null, false);
			if (c != null) {
				map.put("公募", c.getCount());
			}
			Cursor c2 = DbUtils.query(sql + flagSm, null, false);
			if (c2 != null) {
				map.put("私募", c2.getCount());
			}
		} catch (WrapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;

	}

}
