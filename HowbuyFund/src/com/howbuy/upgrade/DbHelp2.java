package com.howbuy.upgrade;

import howbuy.android.palmfund.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelp2 extends SQLiteOpenHelper {
	private final static String DB_NAME = "funds.db";
	private final static String DB_PATH = "/data/data/howbuy.android.palmfund/databases/";
	// private final static int VERSION = 1;
	// 2.3.0基金基本信息表增加一个jjfl2
	// private final static int VERSION = 2;
	// 不能清除掉数据库，因为其中有自选
	// private final static int VERSION = 3;
	// jjfl2字段不能重复更新
	// private final static int VERSION = 4;
	// 再次jjfl2字段不能重复更新
	// private final static int VERSION = 5;
	//	private final static int VERSION = 6;
	// 基金数据库表中增加自选时间
	 public final static int VERSION = 7;

	private Context context;
	private static DbHelp2 dbHelpInstance;

	private DbHelp2(Context context, int version) {
		super(context, DB_NAME, null, VERSION);
		this.context = context;
		copyLocalDataBase(false);
	}

	public static synchronized DbHelp2 getInstance(Context context) {
		if (dbHelpInstance == null) {
			dbHelpInstance = new DbHelp2(context, VERSION);
		}
		return dbHelpInstance;

	}

	/**
	 * 分别创建开放、货币、私募、封闭式基金数据库
	 * 
	 **/
	@Override
	public void onCreate(SQLiteDatabase db) {
		String fundsInfo = "Create  TABLE FundsInfo([_id] integer PRIMARY KEY AUTOINCREMENT,[code] text,[name] text,[pinyin] text,[type] integer,[state] text,[unit_netvalue] text,[status] integer,[xuan] integer,[xuantime] text)";
		// jjdm,jzrq,jjjz,ljjz,hbdr,hb3y,hb6y,hb1,nhbj,nzfxz
		String fundOpens = "CREATE TABLE FundOpens([_id] integer PRIMARY KEY AUTOINCREMENT,[jjdm] text,[jzrq] text,[jjjz] real,[ljjz] real,[hbdr] real,[hb3y] real,[hb6y] real,[hb1n] real,[hbjn] real,[zfxz] real,[hb1y] real)";
		// jjdm,jzrq,wfsy,qrsy,zfxz
		String fundMoneys = "CREATE TABLE FundMoneys([_id] integer PRIMARY KEY AUTOINCREMENT,[jjdm] text,[jzrq] text,[wfsy] real,[qrsy] real,[zfxz] real,[hbjn] real)";
		// jjdm,jjjz,jzrq,ljjz,hb1y,hb6y,hb1n
		String fundSimus = "CREATE TABLE FundSimus([_id] integer PRIMARY KEY AUTOINCREMENT,[jjdm] text,[jjjz] text,[jzrq] text,[ljjz] real,[hb1y] real,[hb6y] real,[hb1n] real)";
		// jjdm,jjjz,Jzrq,ljjz,xzjl,zfxz,
		String fundCloses = "CREATE TABLE FundCloses([_id] integer PRIMARY KEY AUTOINCREMENT,[jjdm] text,[jzrq] text,[jjjz] real,[ljjz] real,[hbdr] real,[hb3y] real,[hb6y] real,[hb1n] real,[hbjn] real,[zfxz] real,[hb1y] real)";
		// 记录已读新闻内容表
		String readered = "CREATE TABLE NewsReaded([_id] integer PRIMARY KEY AUTOINCREMENT,[newsId] varchar(20))";
		// 用户搜索记录表
		String searchHistory = "Create TABLE SearchHistory([_id] integer PRIMARY KEY AUTOINCREMENT,[search_text] text,[search_date] text,[search_from] integer,[value_id] text,[value_type] text,[value_name] text,[value_pinyin] text)";

		db.execSQL(fundsInfo);
		db.execSQL(fundOpens);
		db.execSQL(fundMoneys);
		db.execSQL(fundSimus);
		db.execSQL(fundCloses);
		db.execSQL(readered);
		db.execSQL(searchHistory);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("oldversion:+" + oldVersion + "---newVersion:" + newVersion);
		// if (newVersion < 5) {
		if (checkColumnExit("FundsInfo", "jjfl2", db) == false) {
			String sql = "alter table FundsInfo add jjfl2 varchar(20)";
			db.execSQL(sql);
		}
		// }
		if (newVersion == 5) {
			if (checkColumnExit("NewsReaded", "read", db) == false) {
				String sql = "alter table NewsReaded add readed integer";
				String sql1 = "alter table NewsReaded add title varchar(20)";
				String sql2 = "alter table NewsReaded add lable1 varchar(20)";
				String sql3 = "alter table NewsReaded add publishtime varchar(20)";
				String sql4 = "alter table NewsReaded add tagname varchar(20)";
				String sql5 = "alter table NewsReaded add favorite integer";
				String sql6 = "alter table NewsReaded add newsoryanbao integer";
				db.execSQL(sql);
				db.execSQL(sql1);
				db.execSQL(sql2);
				db.execSQL(sql3);
				db.execSQL(sql4);
				db.execSQL(sql5);
				db.execSQL(sql6);
			}
		}
		
		
		if (checkColumnExit("FundsInfo", "xuantime", db) == false||oldVersion==6) {
			String sql = "alter table FundsInfo add xuantime varchar(20)";
			db.execSQL(sql);
		}
	}
	
	public void doUpdateFor7(){
		try {
			//version==6 1为加，2为减，0默认
			//version==7 1为加，0为减，2为已加，-1默认
			Log.d("impl", "update to new database version");
//			FundInfoDbService.getInstance(context).updateReset("1",String.valueOf(SelfOpateUtil.UNSynsAdd));
//			FundInfoDbService.getInstance(context).updateReset("2",String.valueOf(SelfOpateUtil.UNSynsDel));
//			FundInfoDbService.getInstance(context).updateReset("0",String.valueOf(SelfOpateUtil.defaultS));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
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
		File file = new File(DB_PATH);
		if (!file.exists()) {
			file.mkdir();
		}
		file = new File(DB_PATH + DB_NAME);

		if (file.exists()) {
			if (isNeed) {
				System.out.println("delete olad database");
				file.delete();
			} else {
				return;
			}
		}
		InputStream inM = null;
		OutputStream outM = null;
		inM = context.getResources().openRawResource(R.raw.funds);
		try {
			file.createNewFile();
			outM = new FileOutputStream(file);
			byte[] b = new byte[1024];
			int length;
			while ((length = inM.read(b)) > 0) {
				outM.write(b, 0, length);
			}
			outM.flush();
			outM.close();
			inM.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public synchronized void close() {
		// TODO Auto-generated method stub
		super.close();
		dbHelpInstance = null;
	}
}
