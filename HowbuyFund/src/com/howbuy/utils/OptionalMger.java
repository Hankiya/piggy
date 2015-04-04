package com.howbuy.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.howbuy.component.AppFrame;
import com.howbuy.config.FundConfig;
import com.howbuy.config.SelfConfig;
import com.howbuy.config.ValConfig;
import com.howbuy.db.DbOperat;
import com.howbuy.db.DbUtils;
import com.howbuy.db.DbUtils.SqlExeObj;
import com.howbuy.entity.NetWorthBean;
import com.howbuy.entity.NetWorthListBean;
import com.howbuy.entity.UserInf;
import com.howbuy.frag.FragSearch;
import com.howbuy.frag.FragOptionalList;
import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.net.AsyPoolTask;
import com.howbuy.lib.utils.LogUtils;

public class OptionalMger {
	public static final String AppData_Type_OptionalReceiver = "AppDataType_OptionalReceiver";
	private ArrayList<OptOpt> mOpts;
	private static OptionalMger oM = new OptionalMger();

	public static OptionalMger getMger() {
		return oM;
	}

	private OptionalMger() {
	}

	public void handleOpt(Context context, Intent intent) {
		// TODO Auto-generated method stub
		mOpts = (ArrayList<OptOpt>) AppFrame.getApp().getMapObj(AppData_Type_OptionalReceiver);
		if (mOpts == null) {
			mOpts = new ArrayList<OptionalMger.OptOpt>();
			AppFrame.getApp().getMapObj().put(AppData_Type_OptionalReceiver, mOpts);
		}

		// 添加自选
		Bundle b = intent.getExtras();
		String type = b.getString(ValConfig.IT_TYPE);

		LogUtils.d("opt", "onReceive--type--" + type);
		if (type.equals(SelfConfig.IT_UPDATE)) {
			String jjdm = b.getString(ValConfig.IT_ID);
			String status = b.getString(ValConfig.IT_NAME);
			mOpts.add(new OptOpt(jjdm, status));
		} else if (type.equals(SelfConfig.IT_UPDATE_EXECUE)) {
			String jjdm = b.getString(ValConfig.IT_ID);
			String status = b.getString(ValConfig.IT_NAME);
			mOpts.add(new OptOpt(jjdm, status));
			exeOpt();
		} else {
			exeOpt();
		}

	}

	private void exeOpt() {
		if (mOpts.size() != 0) {
			new SaveOptTask().execute(false);
		}
	}

	public void exeSync(final String custNo) {
		new SyncTask().execute(true, custNo);
	}

	class SaveOptTask extends AsyPoolTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (mOpts.size() != 0) {
				clearSearchCache(true);
				clearSearchCache(false);
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < mOpts.size(); i++) {
					OptOpt opt = mOpts.get(i);
					if (opt.getStatus().equals(String.valueOf(SelfConfig.UNSynsAdd))) {
						// addOpts.add(0, opt);
						boolean isSimu = isSimu(opt.getJjdm());
						NetWorthListBean n = doQueryExistSort(isSimu);
						if (!(null == n)) {// has opt
							NetWorthBean nCurr = new NetWorthBean();
							nCurr.setJjdm(opt.getJjdm());
							n.addItem(nCurr, false);
							saveCurrSortItems(n.getItems());
							saveOptSortType(isSimu);
						}
					}
					DbOperat.getInstance().updateOptional(opt.getJjdm(), opt.getStatus());
					sb.append(opt.getJjdm()).append(":").append(opt.getStatus()).append("--");
				}
				if (sb.length() > 0) {
					LogUtils.d("opt", "send Broadcast--" + sb.toString());
					Bundle b = new Bundle();
					b.putString(ValConfig.IT_ID, sb.toString());
					AppFrame.getApp().getLocalBroadcast()
							.sendBroadcast(Receiver.FROM_OPTIONAL_CHNAGE, b);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				mOpts.clear();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			UserInf userInf = UserInf.getUser();
			boolean a = userInf.isLogined();
			if (a) {
				String custNo = userInf.getCustNo();
				exeSync(custNo);
			}
		}
	}

	class SyncTask extends AsyPoolTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			try {
				SyncOptUtil.syncOpt(params[0]);
			} catch (WrapException e) {
				e.printStackTrace();
			}
			return null;
		}

	}

	/**
	 * 清除search cache 不再去遍历search cache
	 * 
	 * @param isSimu
	 */
	public void clearSearchCache(boolean isSimu) {
		AppFrame.getApp().getMapObj()
				.remove(isSimu ? FragSearch.Map_Search_Sm : FragSearch.Map_Search_GM);
	}

	public static class OptOpt {
		private String jjdm;
		private String status;

		public OptOpt(String jjdm, String optStatus) {
			this.jjdm = jjdm;
			this.status = optStatus;
		}

		public String getJjdm() {
			return jjdm;
		}

		public void setJjdm(String jjdm) {
			this.jjdm = jjdm;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

	}

	/**
	 * 每次加入一条自选 所有的关于自选的排序多变成按自定义排序 当前添加的放在第一条
	 * 
	 * @param cusSortList
	 */
	private void saveCurrSortItems(List<NetWorthBean> cusSortList) {
		String sql = "update fundsinfoopt set postion=? where code=?";
		final ArrayList<SqlExeObj> sqlExeObjs = new ArrayList<SqlExeObj>();
		for (int i = 0; i < cusSortList.size(); i++) {
			String jjdm = cusSortList.get(i).getJjdm();
			LogUtils.d("savepostion--" + cusSortList.get(i).getJjmc() + jjdm);
			SqlExeObj sExeObj = new SqlExeObj(sql, new String[] { String.valueOf(i), jjdm });
			sqlExeObjs.add(sExeObj);
		}
		DbUtils.exeSql(sqlExeObjs, false);
	}

	@Deprecated
	private NetWorthListBean mergerList(NetWorthListBean listBean, ArrayList<OptOpt> addOpts) {
		for (int i = 0; i < addOpts.size(); i++) {
			NetWorthBean n = new NetWorthBean();
			n.setJjdm(addOpts.get(i).getJjdm());
			listBean.addItem(n, false);
		}
		return listBean;
	}

	private NetWorthListBean doQueryExistSort(boolean isSimu) {
		String sqlScript = buildExitQuerySql(isSimu);
		Cursor cursor;
		try {
			cursor = DbUtils.query(sqlScript, null, false);
			if (cursor != null && cursor.moveToFirst()) {
				ArrayList<NetWorthBean> list = new ArrayList<NetWorthBean>();
				NetWorthListBean listBean = new NetWorthListBean();
				do {
					// a.[code],a.[name],a.[type],a.[xuan],c.[xuantime],c.[postion],b.[jjjz],b.[jzrq],b.[hbdr]
					String code = cursor.getString(0);
					String name = cursor.getString(1);
					NetWorthBean b = new NetWorthBean();
					b.setJjdm(code);
					b.setJjmc(name);
					list.add(b);
				} while (cursor.moveToNext());
				cursor.close();
				listBean.addItems(list, false);

				// add first item
				return listBean;
			}
		} catch (WrapException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String buildExitQuerySql(boolean isSimu) {
		String sql = "select a.[code],a.[name],a.[type],a.[xuan],c.[xuantime],b.[jjjz],b.[jzrq],b.[hbdr],b.[hb1y],b.[wfsy],a.[unit_netvalue],b.[qrsy] from fundsinfo a left join netvalue b on a.[code]=b.[jjdm],fundsinfoopt c where c.[code]=a.[code] and a.[xuan] in('"
				+ SelfConfig.SynsShowAdd + "','" + SelfConfig.UNSynsAdd + "')";
		StringBuilder sb = new StringBuilder(sql);
		int tp = getSavedSortType(isSimu);
		if (!isSimu) {
			sb.append(" and a.[type] <> 'sm'");
			switch (tp) {
			case FragOptionalList.Type_Sort_default_name:
				sb.append(" order by lower(a.[pinyin]) asc");
				break;
			case FragOptionalList.Type_Sort_FundType:
				sb.append(" order by case a.[type] ");
				sb.append(" when ").append("'").append(FundConfig.TYPE_GUPIAO).append("'")
						.append(" then 0");
				sb.append(" when ").append("'").append(FundConfig.TYPE_HUOBI).append("'")
						.append(" then 1");
				sb.append(" when ").append("'").append(FundConfig.TYPE_ZHAIQUAN).append("'")
						.append(" then 2");
				sb.append(" when ").append("'").append(FundConfig.TYPE_HUNHE).append("'")
						.append(" then 3");
				sb.append(" when ").append("'").append(FundConfig.TYPE_ZHISHU).append("'")
						.append(" then 4");
				sb.append(" when ").append("'").append(FundConfig.TYPE_QDII).append("'")
						.append(" then 5");
				sb.append(" when ").append("'").append(FundConfig.TYPE_LICAI).append("'")
						.append(" then 6");
				sb.append(" when ").append("'").append(FundConfig.TYPE_BAOBEN).append("'")
						.append(" then 7");
				sb.append(" when ").append("'").append(FundConfig.TYPE_JIEGOU).append("'")
						.append(" then 8");
				sb.append(" when ").append("'").append(FundConfig.TYPE_FENGBI).append("'")
						.append(" then 9");
				sb.append(" end");
				sb.append(" , a.[name]");
				break;
			case FragOptionalList.Type_Sort_Custom:
				sb.append(" order by case when c.[postion] is null then 1 else 0 end,c.[postion]");
				break;
			default:
				break;
			}
		} else {
			sb.append(" and a.[type] = 'sm'");
			switch (tp) {
			case FragOptionalList.Type_Sort_default_name:
				sb.append(" order by lower(a.[pinyin]) asc");
				break;
			case FragOptionalList.Type_Sort_Custom:
				sb.append(" order by case when c.[postion] is null then 1 else 0 end,c.[postion]");
				break;
			default:
				break;
			}
		}
		return sb.toString();
	}

	private int getSavedSortType(boolean isSimu) {
		int tp;
		String ftp = !isSimu ? ValConfig.sFOptSortTypeGm : ValConfig.sFOptSortTypeSm;
		tp = AppFrame.getApp().getsF().getInt(ftp, FragOptionalList.Type_Sort_default_name);
		return tp;
	}

	private void saveOptSortType(boolean isSimu) {
		String ftp = !isSimu ? ValConfig.sFOptSortTypeGm : ValConfig.sFOptSortTypeSm;
		AppFrame.getApp().getsF().edit().putInt(ftp, FragOptionalList.Type_Sort_Custom).commit();
	}

	private boolean isSimu(String jjdm) {
		String sql = "select type from fundsinfo where code=?";
		Cursor c = null;
		try {
			c = DbUtils.query(sql, new String[] { jjdm }, false);
			if (c != null && c.moveToFirst()) {
				String tp = c.getString(0);
				if (tp.equals("sm")) {
					return true;
				} else {
					return false;
				}
			}

		} catch (WrapException e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return false;

	}

}
