package com.howbuy.datalib.trade;

import howbuy.android.piggy.api.dto.RespondCipher;
import howbuy.android.piggy.jni.DesUtil;

import java.io.UnsupportedEncodingException;

import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.CoderUtils;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.StrUtils;

/**
 * 加密解密上行下行参数
 * 
 * @author Administrator
 * 
 */
public class DesUtilForNetParam {
	public static final String SF_TOKEN_ID = "SF_TOKEN_ID";
	public static final String SF_RSA_ID = "SF_RSA_ID";

	static DesUtil desUtil;
	static {
		try{
			System.loadLibrary("desrsa");
			desUtil = new DesUtil();	
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}

	public static boolean isDebug() {
		return LogUtils.mDebugUrl;
	}

	/**
	 * 加密上传参数
	 * 
	 * @param paramExpressly
	 * @return
	 */
	public static String encryptParam(String source) {
		String rsa = getRsaKey();
		byte[] keyByte = Base64.decode(rsa, Base64.DEFAULT);
		byte[] encMsgB = null;
		try {
			encMsgB = desUtil.encryptDes(source.getBytes("utf-8"), keyByte, isDebug());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String key = new String(encMsgB);
		Log.i("piggy", key);
		String encMsgBase64 = Base64.encodeToString(encMsgB, Base64.NO_WRAP);
		return encMsgBase64;
	}

	/**
	 * 把上传参数明文进行md5签名
	 * 
	 * @param paramExpressly
	 * @return
	 */
	public static String encryptMd5(String source) {
		String signMsg = null;
		signMsg = CoderUtils.toMD5(source);
		String signMsgBase64 = Base64.encodeToString(signMsg.getBytes(), Base64.NO_WRAP);
		return signMsgBase64;
	}

	/**
	 * 解密下行参数，并且与签名串进行对比
	 * 
	 * @param paramCiphertext
	 * @param paramMd5Sigin
	 * @return
	 */
	public static String dencryptParam(String paramCiphertext, String paramMd5Sigin)
			throws Exception {
		byte[] keyb = getRsaKey().getBytes();
		byte[] resNoBase64 = Base64.decode(paramCiphertext, Base64.NO_WRAP);
		byte[] resKeyNoBase64 = Base64.decode(keyb, Base64.NO_WRAP);
		byte[] resByte = desUtil.dencryptDes(resNoBase64, resKeyNoBase64, isDebug());
		String res = new String(resByte);
		String resMd5 = CoderUtils.toMD5(res);
		String resMd5Base64 = Base64.encodeToString(resMd5.getBytes("utf-8"), Base64.NO_WRAP);
		if (!resMd5Base64.equals(paramMd5Sigin)) {
			return null;
		}
		return res;
	}

	/**
	 * 解密下行参数，并且与签名串进行对比
	 * 
	 * @param paramCiphertext
	 * @param paramMd5Sigin
	 * @return
	 */
	public static String dencryptParam(RespondCipher sDto) throws WrapException {
		if (sDto == null) {
			return null;
		}
		String encMsg = sDto.getReturnEncMsg();
		String encMsgMd5 = sDto.getReturnSignMsg();
		byte[] keyb = getRsaKey().getBytes();
		byte[] resNoBase64 = Base64.decode(encMsg, Base64.NO_WRAP);
		byte[] resKeyNoBase64 = Base64.decode(keyb, Base64.NO_WRAP);
		byte[] resByte = desUtil.dencryptDes(resNoBase64, resKeyNoBase64, isDebug());
		String res = new String(resByte);
		String resMd5 = CoderUtils.toMD5(res);
		String resMd5Base64;
		try {
			resMd5Base64 = Base64.encodeToString(resMd5.getBytes("utf-8"), Base64.NO_WRAP);
			if (!resMd5Base64.equals(encMsgMd5)) {
				return null;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw WrapException.wrap(e, "dencryptParam");
		}
		return res;
	}

	public static String getRsaKey() {
		SharedPreferences sf = GlobalApp.getApp().getsF();
		return sf.getString(SF_RSA_ID, "android"); 
	}

	/**
	 * 重置rsa密码
	 * 
	 * @return true 重置成功
	 */
	public static boolean doResetRsaKay() {
		SharedPreferences sf = GlobalApp.getApp().getsF();
		String tokenId = sf.getString(SF_TOKEN_ID, "android");
		String rsaKey = null;
		ReqResult<ReqNetOpt> r = TradeBuilder.newRsa(tokenId).execute();
		if (r.isSuccess()) {
			rsaKey = (String) r.mData;
			if (!StrUtils.isEmpty(rsaKey)) {
				sf.edit().putString(SF_RSA_ID, rsaKey).commit();
				return true;
			}
		}
		return false;
	}
}
