package com.howbuy.config;

public class SelfConfig {
	public static final String IT_UPDATE = "Intent_Update";
	public static final String IT_EXECUE = "Intent_Execue";
	public static final String IT_UPDATE_EXECUE = "Intent_Update_EXECUE";
	public static final int UNSynsAdd = 1;
	public static final int UNSynsDel = 0;
	public static final int defaultS = -1;
	public static final int SynsShowAdd = 2;

	public static final boolean isSelf(int a) {
		if (a == UNSynsAdd || a == SynsShowAdd) {
			return true;
		} else {
			return false;
		}
	}

	public static final boolean isSelf(String a) {
		if (a.equals(String.valueOf(UNSynsAdd)) || a.equals(String.valueOf(SynsShowAdd))) {
			return true;
		} else {
			return false;
		}
	}

}
