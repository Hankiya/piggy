package com.howbuy.component;

import java.util.ArrayList;
import java.util.Arrays;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.interfaces.ICursorCalbak;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.lib.utils.SysUtils;


/**
 * @author rexy 840094530@qq.com
 * @date 2013-12-13 下午3:13:53
 */
@Deprecated public class DbUtils { 
	private static String DB_NAME = "HOWBUY_PROJ.db",DB_TEMP=null;;
	private static SQLiteDatabase mDb = null;
	private static boolean isTempDb=false;
	private static int mVersion = 0;
	private static int mDbVersion = -1;
	private static IDbUpdate mUpdateListener = null;
	
	public static void initDbUtils(IDbUpdate dbUpdateListener,int version, String fileName) {
		mUpdateListener = dbUpdateListener;
		DB_NAME = GlobalApp.PROJ_NAME + ".db";
		mVersion=version;
		createCommonTable();
		checkAdExeInitSql(fileName);
	}
    public static void setCurrentDb(String dbName){
    	DB_TEMP=dbName;
        close();
    }
	private static void createCommonTable() {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE IF NOT EXISTS tb_common(");
		sb.append("key TEXT NOT NULL,");
		sb.append("subkey TEXT NOT NULL,");
		sb.append("value BLOB NULL,");
		sb.append("state INTEGER NULL,");
		sb.append("date LONG NULL,");
		sb.append("PRIMARY  KEY(key,subkey) )");
		exeSql(sb.toString());
	}

	private static synchronized void open() {
		if (mDb == null || mDb.isOpen() == false) {
			isTempDb=DB_TEMP!=null;
			mDb = GlobalApp.getApp().openOrCreateDatabase(isTempDb?DB_TEMP:DB_NAME, 0, null);
			if(!isTempDb){
				checkIfNeedUpdate();
			}
		}
	}
	
	private static void checkIfNeedUpdate(){
		if (mDbVersion == -1) {
			mDbVersion = mDb.getVersion();
		}
		if (mDbVersion != mVersion) {
			mDb.beginTransaction();
			try {
				if (mUpdateListener != null) {
					mUpdateListener.onDbUpdate(mDb, mDbVersion, mVersion);
				}
				mDb.setVersion(mVersion);
				mDbVersion = mVersion;
				mDb.setTransactionSuccessful();
			} finally {
				mDb.endTransaction();
			}
		}
	}
	
	private static synchronized void close() {
		if (mDb != null) {
			mDb.close();
			mDb = null;
		}
	}

	@SuppressWarnings("null")
	public static void query(String key, String sqlStr, ICursorCalbak calBk) {
		if (calBk == null) {
			calBk.getCursor(key, null, new WrapException(
					"CursorCalbak should be null.", key + ":spl=" + sqlStr));
			return;
		}
		Cursor curson = null;
		try {
			open();
			curson = mDb.rawQuery(sqlStr, null);
			calBk.getCursor(key, curson, null);
		} catch (Exception ex) {
			calBk.getCursor(key, null,
					WrapException.wrap(ex, key + "spl=" + sqlStr));
		} finally {
			if (curson != null && !curson.isClosed()) {
				curson.close();
			}
			close();
		}
	}

	public static WrapException exeSql(String sqlStr) {
		return exeSql(new SqlExeObj(sqlStr));
	}

	public static WrapException exeSql(SqlExeObj sqlObj) {
		if (sqlObj == null) {
			return  new WrapException( "SqlExeObj is null", "sqlObj  is null");
		}
		open();
		mDb.beginTransaction();
		try {
			if (sqlObj.mObjs == null) {
				mDb.execSQL(sqlObj.mSqlStr);
			} else {
				mDb.execSQL(sqlObj.mSqlStr, sqlObj.mObjs);
			}
			mDb.setTransactionSuccessful();
			return null;
		} catch (Exception ex) {
			return WrapException.wrap(ex, "sqlObj=" + sqlObj);
		} finally {
			mDb.endTransaction();
			close();
		}
	}

	public static WrapException exeSql(ArrayList<SqlExeObj> sqlObj) {
		int len = sqlObj == null ? 0 : sqlObj.size();
		if (len == 0) {
			return new WrapException("empty sqlobj list",
					"exeSql<params:null or empty>");
		}
		open();
		mDb.beginTransaction();
		try {
			for (int i = 0; i < len; i++) {
				SqlExeObj obj = sqlObj.get(i);
				if (obj.mObjs == null) {
					mDb.execSQL(obj.mSqlStr);
				} else {
					mDb.execSQL(obj.mSqlStr, obj.mObjs);
				}
			}
			mDb.setTransactionSuccessful();
			return null;
		} catch (Exception ex) {
			return WrapException.wrap(ex, "sqlObj=" + sqlObj);
		} finally {
			mDb.endTransaction();
			close();
		}
	}

	public static WrapException exeSql(SqlExeObj[] sqlObj) {
		int len = sqlObj == null ? 0 : sqlObj.length;
		if (len == 0) {
			return new WrapException("empty sqlobj list",
					"exeSql<params:null or empty>");
		}
		open();
		mDb.beginTransaction();
		try {
			for (int i = 0; i < len; i++) {
				SqlExeObj obj = sqlObj[i];
				if (obj.mObjs == null) {
					mDb.execSQL(obj.mSqlStr);
				} else {
					mDb.execSQL(obj.mSqlStr, obj.mObjs);
				}
			}
			mDb.setTransactionSuccessful();
			return null;
		} catch (Exception ex) {
			return WrapException.wrap(ex, "sqlObj=" + sqlObj);
		} finally {
			mDb.endTransaction();
			close();
		}

	}

	private static void checkAdExeInitSql(String fileName) {
		if (fileName != null) {
			ArrayList<SqlExeObj> sqlist = getInitSqlScript(fileName);
			if (sqlist != null) {
				exeSql(sqlist);
			}
		}
	}

	private static ArrayList<SqlExeObj> getInitSqlScript(String filename) {
		String sqls = SysUtils.readFromAssets(GlobalApp.getApp(), filename);
		if (!StrUtils.isEmpty(sqls)) {
			ArrayList<SqlExeObj> objs = new ArrayList<SqlExeObj>();
			String[] ss = sqls.split(";");
			for (int i = 0; i < ss.length; i++) {
				if (!StrUtils.isEmpty(ss[i]))
					objs.add(new SqlExeObj(ss[i]));
			}
			return objs;
		}
		return null;
	}

	public static class SqlExeObj {
		public String mSqlStr = null;
		public Object[] mObjs = null;

		public SqlExeObj(String sqlStr) {
			this(sqlStr, null);
		}

		public SqlExeObj(String sqlStr, Object[] objs) {
			this.mSqlStr = sqlStr;
			this.mObjs = objs;
		}

		@Override
		public String toString() {
			return "SqlExeObj [mSqlStr=" + mSqlStr + ", mObjs="
					+ Arrays.toString(mObjs) + "]";
		}
	}
	public static interface IDbUpdate {
		public void onDbUpdate(SQLiteDatabase db, int curVersion, int newVersion);
	}
	 
}

 
