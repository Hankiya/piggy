package howbuy.android.util;

import howbuy.android.piggy.application.ApplicationParams;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

/**
 * 初始化数据结构
 * 
 * @author yescpu
 * 
 */
public class InitParams {
	public static final String SFfirstIVer = "1.0.0";
	public static final String SFfirstParPhoneModel = "android";
	public static final String SFfirstProductId = "105975974";
	private SharedPreferences mSf;
	private Context mContext;

	public InitParams(Context context) {
		this.mContext = context;
		mSf = mContext.getSharedPreferences(Cons.SFbaseUser, Context.MODE_PRIVATE);
	}

	public InitParams() {
		this.mContext = ApplicationParams.getInstance();
		mSf = mContext.getSharedPreferences(Cons.SFbaseUser, Context.MODE_PRIVATE);
	}

	/**
	 * 判断程序是第一次启动
	 * 
	 * @return
	 */
	public boolean isFirstStart() {
		String version = mSf.getString(Cons.SFfirstVersion, null);
		if (version == null) {
			return true;
		}
		return false;
	}

	/**
	 * 初始化七个网络参数
	 */
	public void initNetPublicParams() {
		String version = "";
		String chid = ManifestMetaData.getString(mContext, "channeId");

		try {
			version = mContext.getPackageManager().getPackageInfo(Cons.PACKAGENAME, -1).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		Editor editor = mSf.edit();

		String savedUUID = mSf.getString(Cons.SFfirstUUid, null);
		if (TextUtils.isEmpty(savedUUID)) {
			String url = Environment.getExternalStorageDirectory().getAbsolutePath();
			if (android.os.Build.DEVICE.contains("Samsung")
					|| android.os.Build.MANUFACTURER.contains("Samsung")) {
				url = url + "/external_sd/";
			}
			File file =null;
			if (url != null) {
				url += "/AndroidActivity.log";
				 file= new File(url);
			}
			
			String sdUuidString=null;
			try {
				sdUuidString = FileUtils.getFileString(file);
				sdUuidString=sdUuidString.trim();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if(!TextUtils.isEmpty(sdUuidString)){
				editor.putString(Cons.SFfirstUUid, sdUuidString);
			}else{
				String uuid = UUID.randomUUID().toString();
				editor.putString(Cons.SFfirstUUid, uuid);
				try {
					FileUtils.saveFile(uuid, file, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		editor.putString(Cons.SFfirstIVer, SFfirstIVer);
		editor.putString(Cons.SFfirstParPhoneModel, SFfirstParPhoneModel);
		editor.putString(Cons.SFfirstSubPhoneModel, Build.MODEL);
		editor.putString(Cons.SFfirstChanneIdId, String.valueOf(chid));
		editor.putString(Cons.SFfirstProductId, SFfirstProductId);
		editor.putString(Cons.SFfirstVersion, version);
		editor.commit();
	}

	/**
	 * 升级所要做的事情
	 * 
	 * @param currVersion
	 */
	public void doUpdate(String currVersion) {
		String oladVersion = mSf.getString(Cons.SFfirstVersion, null);
		if (currVersion != oladVersion) {
			initNetPublicParams();
		}
	}

	/**
	 * 获取当前的versionname
	 * 
	 * @return
	 */
	public String getCurrVersionName() {
		String versionName = null;
		try {
			versionName = ApplicationParams.getInstance().getPackageManager().getPackageInfo(Cons.PACKAGENAME, -1).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return versionName;
	}

	/**
	 * 取出公共参数
	 * 
	 * @return
	 */
	public Map<String, String> getPubParams() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("version", mSf.getString(Cons.SFfirstVersion, ""));
		map.put("channelId", mSf.getString(Cons.SFfirstChanneIdId, ""));
		map.put("productId", mSf.getString(Cons.SFfirstProductId, ""));
		map.put("parPhoneModel", mSf.getString(Cons.SFfirstParPhoneModel, ""));
		map.put("subPhoneModel", mSf.getString(Cons.SFfirstSubPhoneModel, ""));
		map.put("token", mSf.getString(Cons.SFfirstUUid, ""));
		map.put("iVer", mSf.getString(Cons.SFfirstIVer, ""));
		map.put("corpId", ManifestMetaData.get(mContext, "TransactionCorpId").toString());
		map.put("coopId", ManifestMetaData.get(mContext, "TransactionCoopId").toString());
		map.put("actionId", ManifestMetaData.get(mContext, "TransactionActionId").toString());
		return map;
	}

}
