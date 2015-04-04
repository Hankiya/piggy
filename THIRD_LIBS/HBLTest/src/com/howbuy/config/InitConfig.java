package com.howbuy.config;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;

import com.howbuy.component.AppFrame;
import com.howbuy.lib.utils.SysUtils;
/**
 * 初始化数据结构
 * @author yescpu
 */
public class InitConfig {
	// 净值类型版本
	public static final String TIME_STRING = "2013-03-21 00:00:00";
	// 基金基本数据版本
	public static final String TIME_YYMMDD = "20070707";
	public static final String SFfirstIVer = "3.0.0";
	public static final String SFfirstParPhoneModel = "android";
	public static final String SFfirstProductId = "28294488";// "85348808";

	private SharedPreferences mSf;
	private Context mContext;

	public InitConfig(Context context) {
		this.mContext = context;
		mSf = mContext.getSharedPreferences(ValConfig.SFbaseUser, Context.MODE_PRIVATE);
	}

	public InitConfig() {
		this.mContext = AppFrame.getApp();
		mSf = mContext.getSharedPreferences(ValConfig.SFbaseUser, Context.MODE_PRIVATE);
	}

	/**
	 * 判断程序是第一次启动
	 * 
	 * @return
	 */
	public boolean isFirstStart() {
		String version = mSf.getString(ValConfig.SFfirstVersion, null);
		if (version == null) {
			return true;
		}
		return false;
	}

	/**
	 * 初始化七个网络参数
	 */
	public void initNetPublicParams() {
		Editor editor = mSf.edit();
		if (mSf.getString(ValConfig.SFfirstVersion, null)==null) {
			String uuid = SysUtils.getWifiMac(mContext) == null ? UUID.randomUUID().toString()
					: SysUtils.getWifiMac(mContext);
			editor.putString(ValConfig.SFfirstUUid, uuid);
		}
		editor.putString(ValConfig.SFfirstIVer, SFfirstIVer);
		editor.putString(ValConfig.SFfirstParPhoneModel, SFfirstParPhoneModel);
		editor.putString(ValConfig.SFfirstSubPhoneModel, Build.MODEL);
		editor.putString(ValConfig.SFfirstChanneIdId, SysUtils.getMetaData(mContext, "channeId")
				.toString());
		editor.putString(ValConfig.SFfirstProductId, SFfirstProductId);
		editor.putString(ValConfig.SFfirstVersion, SysUtils.getVersionName(mContext));
		editor.commit();
	}

	/**
	 * 升级所要做的事情
	 * 
	 * @param currVersion
	 */
	public void doUpdate(String currVersion) {
		String oladVersion = mSf.getString(ValConfig.SFfirstVersion, null);
		if (currVersion != oladVersion) {
			initNetPublicParams();
		}
	}

	/**
	 * 取出公共参数
	 * 
	 * @return
	 */
	public Map<String, String> getPubParams() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("version", mSf.getString(ValConfig.SFfirstVersion, ""));
		map.put("channelId", mSf.getString(ValConfig.SFfirstChanneIdId, ""));
		map.put("productId", mSf.getString(ValConfig.SFfirstProductId, ""));
		map.put("parPhoneModel", mSf.getString(ValConfig.SFfirstParPhoneModel, ""));
		map.put("subPhoneModel", mSf.getString(ValConfig.SFfirstSubPhoneModel, ""));
		map.put("token", mSf.getString(ValConfig.SFfirstUUid, ""));
		map.put("iVer", mSf.getString(ValConfig.SFfirstIVer, ""));
		map.put("corpId", SysUtils.getMetaData(mContext, "TransactionCorpId").toString());
		return map;
	}

}
