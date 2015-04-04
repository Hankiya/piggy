package com.howbuy.upgrade;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.TextUtils;

import com.howbuy.config.SelfConfig;
import com.howbuy.db.DbOperat;
import com.howbuy.db.DbUtils;
import com.howbuy.db.DbUtils.SqlExeObj;
import com.howbuy.entity.NetWorthBean;
import com.howbuy.entity.NewsItem;
import com.howbuy.entity.UserInf;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.StreamUtils;

public class UpgradeUtil2 {
	public static final String SFfirstVersionOlad = "SFfirstVersion";
	public static final String SFfirstVersion = "SFfirstVersion-1";// SFfirstVersion-1
	public static final String SFbaseUser = "sfbaseuser";
	public static final String SFUserCusNo = "SFUserCusNo";
	public static final String SFUserPhone = "SFUserPhone";
	public static final String SFUserName = "SFUserName";

	private SharedPreferences getSf(Context context) {
		return context.getSharedPreferences(SFbaseUser, Activity.MODE_PRIVATE);
	}

	private SQLiteDatabase getDataBase(Context context) {
		return DbHelp2.getInstance(context).getReadableDatabase();
	}

	public boolean needUpdate(Context context) {
		SharedPreferences sf = getSf(context);
		String firstVer = sf.getString(SFfirstVersion, null);
		if (TextUtils.isEmpty(firstVer) || firstVer.startsWith("3")) {
			return false;
		}
		return true;
	}

	public void doUpdate(Context context) throws Exception {
		ArrayList<NetWorthBean> listOpt = queryOladOptional(context);
		ArrayList<NewsItem> listNews = queryOladCollection(context);
		UserInf userInf = queryOladUser(context);
		cleanApplicationData(context, null);
		backUpOptional(listOpt, context);
		LogUtils.d("backup", "backUpOptional success");
		backUpNewCollection(listNews, context);
		LogUtils.d("backup", "backUpNewCollection success");
		backUser(userInf, context);
		LogUtils.d("backup", "backUser success");
	}

	private ArrayList<NetWorthBean> queryOladOptional(Context context) throws Exception {
		ArrayList<NetWorthBean> listOpt = new ArrayList<NetWorthBean>();
		// 20 2.6.3 since recent
		// 25 2.7.0 db update to new opt rule
		// 6363
		String firstVer = getSf(context).getString(SFfirstVersion, null);
		String[] opVerArry = firstVer.split("\\.");
		int[] opVerArryInt = new int[opVerArry.length];
		for (int i = 0; i < opVerArry.length; i++) {
			opVerArryInt[i] = Integer.parseInt(opVerArry[i]);
		}

		if (opVerArryInt[0] < 1) {
			return null;
		} else {
			if (opVerArryInt[0] == 2) {
				// version==6 1为加，2为减，0默认
				// version==7 1为加，0为减，2为已加，-1默认
				Cursor c;
				if (opVerArryInt[1] == 7 && opVerArryInt[2] > 0) {// new opt
																	// rule
					c = getDataBase(context).rawQuery("select code from fundsinfo where xuan=1",
							null);
				} else if (opVerArryInt[1] <= 7) {// 2.6.3之前到2.0.0
					c = getDataBase(context).rawQuery(
							"select code from fundsinfo where xuan in(1,2)", null);
				} else {
					c = null;
				}

				if (c != null && c.moveToFirst()) {
					do {
						NetWorthBean n = new NetWorthBean();
						n.setJjdm(c.getString(0));
						listOpt.add(n);
					} while (c.moveToNext());

					return listOpt;
				}
				c.close();

			} else {
				return null;
			}
		}
		return null;
	}

	private void backUpOptional(ArrayList<NetWorthBean> backData, Context context) throws Exception {
		if (backData != null) {
			for (int i = 0; i < backData.size(); i++) {
				String jjdm = backData.get(i).getJjdm();
				DbOperat.getInstance().updateOptional(jjdm, SelfConfig.UNSynsAdd + "");
			}
		}
	}

	private ArrayList<NewsItem> queryOladCollection(Context context) throws Exception {
		String sql = "select * from NewsReaded where favorite=1";
		Cursor cursor = getDataBase(context).rawQuery(sql, null);
		int i = 0;
		ArrayList<NewsItem> list = new ArrayList<NewsItem>();
		if (cursor.moveToFirst()) {
			do {// newsId=? readed=? title=? lable1=? publishtime=? tagname=?
				// favorite=?
				NewsItem newsBean = new NewsItem();
				newsBean.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex("newsId"))));
				newsBean.setNewsType(cursor.getInt(cursor.getColumnIndex("newsoryanbao")));
				newsBean.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				newsBean.setLabel(cursor.getString(cursor.getColumnIndex("lable1")));
				newsBean.setPublishTime(Long.parseLong(cursor.getString(cursor
						.getColumnIndex("publishtime"))));
				newsBean.setTagName(cursor.getString(cursor.getColumnIndex("tagname")));
				newsBean.addFlag(NewsItem.ARTICAL_COLLECTED);
				list.add(newsBean);
			} while (cursor.moveToNext());
			cursor.close();
			return list;
		} else {
			cursor.close();
			return null;
		}
	}

	private void backUpNewCollection(ArrayList<NewsItem> collectNews, Context context)
			throws Exception {
		if (collectNews != null) {
			String sql = "insert into tb_common values(?,'artical_collect',?,?,?,?)";
			ArrayList<SqlExeObj> list = new ArrayList<DbUtils.SqlExeObj>();
			for (int i = 0; i < collectNews.size(); i++) {
				NewsItem n = collectNews.get(i);
				int newsType = n.getNewsType() - 1;
				list.add(new SqlExeObj(sql, new Object[] { null, n.getId(),
						n.toNews(newsType).toByteArray(), newsType, System.currentTimeMillis() }));

			}
			DbUtils.exeSql(list, false);
		}

	}

	private UserInf queryOladUser(Context context) throws Exception {
		String cusNo = getSf(context).getString(SFUserCusNo, "");
		if (!TextUtils.isEmpty(cusNo)) {
			String name = getSf(context).getString(SFUserName, "");
			String phone = getSf(context).getString(SFUserPhone, "");
			UserInf u = new UserInf(name, "update-noPwd-default", cusNo,
					System.currentTimeMillis(), 1, phone);
			return u;
		}
		return null;
	}

	private void backUser(UserInf userInf, Context context) throws Exception {
		if (userInf != null) {
			String slq = "insert or replace into tb_common(key,subkey,value,state,date) values('user_login',?,?,?,?)";
			DbUtils.exeSql(
					new SqlExeObj(slq, new Object[] { userInf.getUserName(),
							StreamUtils.toBytes(userInf), "1", userInf.getLoginTime() }), false);
		}
	}

	/** * 清除本应用所有的数据 * * @param context * @param filepath */
	public void cleanApplicationData(Context context, String... filepath) {
		cleanInternalCache(context);
		cleanExternalCache(context);
		cleanDatabases(context);
		cleanSharedPreference(context);
		cleanFiles(context);
		if (filepath != null) {
			for (String filePath : filepath) {
				cleanCustomCache(filePath);
			}
		}
	}

	private void cleanInternalCache(Context context) {
		deleteFilesByDirectory(context.getCacheDir());
	}

	private void cleanDatabases(Context context) {
		deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/databases"));
	}

	private void cleanSharedPreference(Context context) {
		deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
	}

	private void cleanDatabaseByName(Context context, String dbName) {
		context.deleteDatabase(dbName);
	}

	private void cleanFiles(Context context) {
		deleteFilesByDirectory(context.getFilesDir());
	}

	private void cleanExternalCache(Context context) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			deleteFilesByDirectory(context.getExternalCacheDir());
		}
	}

	private void cleanCustomCache(String filePath) {
		deleteFilesByDirectory(new File(filePath));
	}

	private void deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				LogUtils.d("upgrade", item.getAbsolutePath());
				item.delete();
			}
		}
	}
}
