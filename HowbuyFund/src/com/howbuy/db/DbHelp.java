package com.howbuy.db;

import howbuy.android.palmfund.R;

import java.io.File;
import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.utils.FileUtils;
import com.howbuy.lib.utils.LogUtils;

public class DbHelp extends SQLiteOpenHelper {
	private final static String DB_NAME = "funds.db";
	private File mFile;
	public final static int VERSION = 10;
	private static DbHelp dbHelpInstance;

	private DbHelp(Context context, int version) {
		super(context, DB_NAME, null, VERSION);
		mFile = context.getDatabasePath(DB_NAME);
		if (!mFile.exists()) {
			LogUtils.d("DbHelp--copyLocalDataBase--" + VERSION);
			copyLocalDataBase(true);
		}
	}

	public static synchronized DbHelp getInstance() {
		if (dbHelpInstance == null) {
			dbHelpInstance = new DbHelp(GlobalApp.getApp(), VERSION);
		}
		return dbHelpInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		/*
		 * File file = GlobalApp.getApp().getDatabasePath(DB_NAME);
		 * LogUtils.d(String.valueOf(file.exists()) + "--onCreateversion--" +
		 * VERSION); ArrayList<String> sqlScript = getInitSqlScript(DB_Script);
		 */
		/*
		 * mDb.beginTransaction(); try { mDb.execSQL(commonTable); // for
		 * (String string : sqlScript) { // mDb.execSQL(string); // } } catch
		 * (Exception e) { // TODO: handle exception e.printStackTrace(); }
		 * finally { mDb.endTransaction(); }
		 */
	}

	private boolean updateCharValue8_9(SQLiteDatabase db) {
		try {
			 
			StringBuffer sb = new StringBuffer();
			sb.append("drop table tb_charvalue");
			db.execSQL(sb.toString());
			sb.delete(0, sb.length());
			sb.append("CREATE TABLE [tb_charvalue] (");
			sb.append("[code] TEXT DEFAULT NULL,");
			sb.append("[value] TEXT DEFAULT NULL, ");
			sb.append("[increase] TEXT DEFAULT NULL,");
			sb.append("[date] TEXT DEFAULT NULL,");
			sb.append("[type] INTEGER DEFAULT 0,");
			sb.append("CONSTRAINT [] PRIMARY KEY ([code], [type], [date]));");
			db.execSQL(sb.toString());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean updateNetValue8_10(SQLiteDatabase db) {
		try {
			db.execSQL("alter table NetValue add COLUMN  [hb3n] real DEFAULT NULL");
			LogUtils.d("DbHelp--updateNetValue8_10--alter table NetValue add COLUMN  [hb3n] real DEFAULT NULL"  );
			return true;
		} catch (Exception e) { 
			LogUtils.d("DbHelp--updateNetValue8_10--" + e);
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion >= 8) {// 3.0从8开始的。
			LogUtils.d("DbHelp--oldVersion=" + oldVersion+",newVersion="+newVersion);
			if (oldVersion < 9) {// 图表数据索引建错升级到9。
				updateCharValue8_9(db);
			} 
            if (oldVersion < 10) {// 私净值表里据需求要添加3年涨幅升级到10。
				updateNetValue8_10(db);
			}
		}
	}

	boolean checkColumnExit(String dbName, String column, SQLiteDatabase db) {
		String checksql = "SELECT * FROM sqlite_master WHERE name= '" + dbName + "'";
		Cursor cursor = db.rawQuery(checksql, null);
		cursor.moveToFirst();
		String tabsql = cursor.getString(cursor.getColumnIndex("sql"));
		if (tabsql != null && tabsql.equals("") == false) {
			if (tabsql.contains(column)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * Copy本地打包数据库 是否必须删除原来的数据库
	 */
	public void copyLocalDataBase(boolean isNeed) {
		if (isNeed == false) {
			return;
		}
		InputStream inM = null;
		inM = GlobalApp.getApp().getResources().openRawResource(R.raw.funds);
		try {
			FileUtils.saveFile(inM, mFile, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * private static ArrayList<String> getInitSqlScript(String assetFile) {
	 * String sqls = SysUtils.readFromAssets(GlobalApp.getApp(), assetFile); if
	 * (!StrUtils.isEmpty(sqls)) { ArrayList<String> objs = new
	 * ArrayList<String>(); String[] ss = sqls.split(";"); for (int i = 0; i <
	 * ss.length; i++) { if (!StrUtils.isEmpty(ss[i])) objs.add(ss[i]); } return
	 * objs; } return null; }
	 */

	@Override
	public synchronized void close() {
		// TODO Auto-generated method stub
		super.close();
		dbHelpInstance = null;
	}
}
