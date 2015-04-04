package com.howbuy.entity;

import java.util.ArrayList;

import android.database.Cursor;

public class NetValue {
	public static boolean FUNDINFO_INITED = false;
	public static int FUNDINFO_JJDM, FUNDINFO_JZRQ, FUNDINFO_JJJZ, FUNDINFO_LJJZ,
			FUNDINFO_ZFXZ, FUNDINFO_HBDR, FUNDINFO_HB1Y, FUNDINFO_HB3Y, FUNDINFO_HB6Y,
			FUNDINFO_HB1N, FUNDINFO_HBJN, FUNDINFO_QRSY, FUNDINFO_WFSY;
	public String jjdm, jzrq;
	public double jjjz, ljjz, zfxz, hbdr, hb1Y, hb3Y, hb6Y, hb1N, hbjn, qrsy,
			wfsy;
	public static NetValue getNetValue(Cursor c) {
		NetValue r = new NetValue();
		r.jjdm=c.getString(FUNDINFO_JJDM);
		r.jzrq=c.getString(FUNDINFO_JZRQ);
		r.jjjz=c.getDouble(FUNDINFO_JJJZ);
		r.ljjz=c.getDouble(FUNDINFO_LJJZ);
		r.zfxz=c.getDouble(FUNDINFO_ZFXZ);
		r.hbdr=c.getDouble(FUNDINFO_HBDR);
		r.hb1Y=c.getDouble(FUNDINFO_HB1Y);
		r.hb3Y=c.getDouble(FUNDINFO_HB3Y);
		r.hb6Y=c.getDouble(FUNDINFO_HB6Y);
		r.hb1N=c.getDouble(FUNDINFO_HB1N);
		r.qrsy=c.getDouble(FUNDINFO_QRSY);
		r.wfsy=c.getDouble(FUNDINFO_WFSY);
		return r;
	}
	public static void initIndex(Cursor c) {
		FUNDINFO_JJDM = c.getColumnIndex("jjdm");
		FUNDINFO_JZRQ = c.getColumnIndex("jzrq");
		FUNDINFO_JJJZ = c.getColumnIndex("jjjz");
		FUNDINFO_LJJZ = c.getColumnIndex("ljjz");
		FUNDINFO_ZFXZ = c.getColumnIndex("zfxz");
		FUNDINFO_HBDR = c.getColumnIndex("hbdr");
		FUNDINFO_HB1Y = c.getColumnIndex("hb1Y");
		FUNDINFO_HB3Y = c.getColumnIndex("hb3Y");
		FUNDINFO_HB6Y = c.getColumnIndex("hb6Y");
		FUNDINFO_HB1N = c.getColumnIndex("hb1N");
		FUNDINFO_HBJN = c.getColumnIndex("hbjn");
		FUNDINFO_QRSY = c.getColumnIndex("qrsy");
		FUNDINFO_WFSY = c.getColumnIndex("wfsy");
		FUNDINFO_INITED = true;
	}

	public static ArrayList<NetValue> getNetValues(Cursor c) {
		int n = c.getCount();
		ArrayList<NetValue> r = new ArrayList<NetValue>(Math.max(8, n));
		if (n > 0) {
			if (!NetValue.FUNDINFO_INITED) {
				NetValue.initIndex(c);
			}
			while (c.moveToNext()) {
				r.add(getNetValue(c));
			}
		}
		return r;
	}

}
