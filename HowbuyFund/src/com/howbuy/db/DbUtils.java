package com.howbuy.db;

import java.util.ArrayList;
import java.util.Arrays;

import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.interfaces.ICursorCalbak;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbUtils {
	private static DbHelp mDbHelper;
	private static SQLiteDatabase mDb;
	static {
		mDbHelper = DbHelp.getInstance();
	}

	public static synchronized void close() {
		if (mDb != null && mDb.isOpen()) {
			try {
				mDb.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		mDb = null;
	}

	public static synchronized void open() {
		if (mDb == null) {
			mDb = mDbHelper.getWritableDatabase();
		}
	}

	public static void query(String key, String sql, ICursorCalbak calBk, boolean closedb) {
		Cursor c = null;
		try {
			open();
			c = mDb.rawQuery(sql, null);
			calBk.getCursor(key, c, null);
		} catch (Exception ex) {
			calBk.getCursor(key, null, WrapException.wrap(ex, key + "spl=" + sql));
		} finally {
			if (c != null && !c.isClosed()) {
				c.close();
			}
			if (closedb) {
				close();
			}
		}
	}

	public static Cursor query(String sql, String[] args, boolean closedb) throws WrapException {
		Cursor c = null;
		try {
			open();
			c = mDb.rawQuery(sql, args);
			return c;
		} catch (Exception ex) {
			throw WrapException.wrap(ex, "spl=" + sql);
		} finally {
			if (closedb) {
				close();
			}
		}
	}

	public static synchronized WrapException exeSql(SqlExeObj sqlObj, boolean closedb) {
		if (sqlObj == null) {
			return new WrapException("SqlExeObj is null", "sqlObj  is null");
		}
		try {
			open();
			mDb.beginTransaction();
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
			if (closedb) {
				close();
			}
		}
	}

	public static synchronized WrapException exeSqlNoTrans(SqlExeObj sqlObj, boolean closedb) {
		if (sqlObj == null) {
			return new WrapException("SqlExeObj is null", "sqlObj  is null");
		}
		try {
			open();
			if (sqlObj.mObjs == null) {
				mDb.execSQL(sqlObj.mSqlStr);
			} else {
				mDb.execSQL(sqlObj.mSqlStr, sqlObj.mObjs);
			}
			return null;
		} catch (Exception ex) {
			return WrapException.wrap(ex, "sqlObj=" + sqlObj);
		} finally {
			if (closedb) {
				close();
			}
		}
	}

	public static synchronized WrapException exeSql(ArrayList<SqlExeObj> sqlObj, boolean closedb) {
		int len = sqlObj == null ? 0 : sqlObj.size();
		if (len == 0) {
			return new WrapException("empty sqlobj list", "exeSql<params:null or empty>");
		}
		try {
			open();
			mDb.beginTransaction();
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
			if (closedb) {
				close();
			}
		}
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
			return "SqlExeObj [mSqlStr=" + mSqlStr + ", mObjs=" + Arrays.toString(mObjs) + "]";
		}
	}
}
