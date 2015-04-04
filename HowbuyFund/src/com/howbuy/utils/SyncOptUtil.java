package com.howbuy.utils;

import java.util.ArrayList;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.google.myjson.GsonBuilder;
import com.google.myjson.reflect.TypeToken;
import com.howbuy.component.AppFrame;
import com.howbuy.config.SelfConfig;
import com.howbuy.datalib.fund.ParSyncOptional;
import com.howbuy.db.DbUtils;
import com.howbuy.db.DbUtils.SqlExeObj;
import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.wireless.entity.protobuf.ICSynFavFundProtos.ICSynFavFund;
import com.howbuy.wireless.entity.protobuf.UserFavoriteProtos.UserFavorite;

public class SyncOptUtil {
	// public static final String Aciton_SyncOpt_resoult =
	// "Aciton_SyncOpt_resoult";

	/**
	 * 异步处理
	 * 
	 * @param custNo
	 */
	public static boolean syncOpt(String custNo) throws WrapException {
		// 获取数据库里面的自选基金
		UserOptList uFundList = new UserOptList();
		ArrayList<UserOpt> sysbeans = getOptRecord();
		uFundList.setCustNo(custNo);
		uFundList.setOpeInfos(sysbeans);

		String fundsString = null;
		fundsString = new GsonBuilder().create().toJson(uFundList, new TypeToken<UserOptList>() {
		}.getType());
		// 同步操作
		Log.i("sync", "upload--" + uFundList.getOpeInfos().size());
		ICSynFavFund uf = doSync(fundsString);
		Log.i("sync", "download--" + (uf == null ? 0 : uf.getUserFavoritesCount()));
		if (uf != null && uf.getCommon().getResponseCode().equals("1")
				&& uf.getUserFavoritesCount() > 0) {
			// 重置
			resetOptRecord();
			// 更新
			saveNewOpt(uf);
			AppFrame.getApp().getLocalBroadcast().sendBroadcast(Receiver.FROM_OPTIONAL_SYNC, null);
			return true;
		}
		return false;
	}

	/**
	 * 获取本地自选操作记录
	 * 
	 * @return
	 */
	private static final ArrayList<UserOpt> getOptRecord() throws WrapException {
		String sql = "select a.[code],a.[xuan],b.[xuantime] from fundsinfo a left join fundsinfoopt b on a.[code]=b.[code] where a.[xuan] in('1','0')";
		Cursor c = DbUtils.query(sql, null, false);
		ArrayList<UserOpt> list = new ArrayList<SyncOptUtil.UserOpt>();
		if (c != null && c.moveToFirst()) {
			do {
				String xuanTime = c.getString(2);
				if (TextUtils.isEmpty(xuanTime)) {
					xuanTime = String.valueOf(System.currentTimeMillis());
				}
				UserOpt u = new UserOpt(String.valueOf(c.getInt(1)), c.getString(0), xuanTime);
				list.add(u);
			} while (c.moveToNext());

		}
		return list;
	}

	/**
	 * merge server opt record
	 * 
	 * @param custNo
	 * @param fundList
	 * @return
	 */
	private static final ICSynFavFund doSync(String fundList) {
		ReqResult<ReqNetOpt> result = new ParSyncOptional(0).setParams(fundList).execute();
		if (result.isSuccess()) {
			return (ICSynFavFund) result.mData;
		}
		return null;
	}

	/**
	 * reset local opt record
	 */
	private static final void resetOptRecord() {
		String sql = "update fundsinfo set xuan=-1 where xuan in(" + SelfConfig.UNSynsAdd + ","
				+ SelfConfig.UNSynsDel + "," + SelfConfig.SynsShowAdd + ")";
		DbUtils.exeSql(new SqlExeObj(sql), false);
	}

	/**
	 * save new opt to db
	 * 
	 * @param uf
	 */
	private static final void saveNewOpt(ICSynFavFund uf) {
		String sql = "update fundsinfo set xuan=" + SelfConfig.SynsShowAdd + " where code = ?";
		ArrayList<SqlExeObj> sqlObjList = new ArrayList<DbUtils.SqlExeObj>();
		for (int i = 0; i < uf.getUserFavoritesCount(); i++) {
			UserFavorite u = uf.getUserFavorites(i);
			SqlExeObj s = new SqlExeObj(sql, new Object[] { u.getFavoriteObject() });
			sqlObjList.add(s);
		}
		DbUtils.exeSql(sqlObjList, false);
	}

	public static class UserOptList {
		private String custNo;
		private ArrayList<UserOpt> opeInfos;

		public ArrayList<UserOpt> getOpeInfos() {
			return opeInfos;
		}

		public void setOpeInfos(ArrayList<UserOpt> opeInfos) {
			this.opeInfos = opeInfos;
		}

		public String getCustNo() {
			return custNo;
		}

		public void setCustNo(String custNo) {
			this.custNo = custNo;
		}

	}

	public static class UserOpt {
		String op;
		String code;
		String operateTime;

		public UserOpt(String op, String code, String string) {
			this.op = op;
			this.code = code;
			this.operateTime = string;
		}

		public String getOp() {
			return op;
		}

		public void setOp(String op) {
			this.op = op;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getOperationTime() {
			return operateTime;
		}

		public void setOperationTime(String operationTime) {
			this.operateTime = operationTime;
		}

		@Override
		public String toString() {
			return "UserSelfFund [op=" + op + ", code=" + code + ", operationTime=" + operateTime
					+ "]";
		}

	}
}
