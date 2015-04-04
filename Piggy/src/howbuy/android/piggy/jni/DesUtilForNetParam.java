package howbuy.android.piggy.jni;

import howbuy.android.piggy.api.PeripheryJson;
import howbuy.android.piggy.api.dto.SercurityInfoDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.error.HowbuyException;
import howbuy.android.piggy.parameter.GlobalParams;
import howbuy.android.util.Cons;
import howbuy.android.util.MD5Utils;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import android.util.Base64;
import android.util.Log;

/**
 * 加密解密上行下行参数
 * 
 * @author Administrator
 * 
 */
public class DesUtilForNetParam {
	public static final String TAG = "DesUtilForNetParam"; 
	static DesUtil desUtil;
	static Object oLock = new Object();

	static {
		System.loadLibrary("desrsa");
		desUtil = new DesUtil();
	}

	public static boolean isDebug() {
		return GlobalParams.getGlobalParams().isEncryptDebugFlag();
	}

	/**
	 * 加密上传参数
	 * 
	 * @param paramExpressly
	 * @return
	 */
	public static String encryptParam(Map<String, String> paramExpressly) {
		synchronized (oLock) {

			String source = PeripheryJson.paramMapToString(paramExpressly);
			String rsa = getRsaKey();
			byte[] keyByte = Base64.decode(rsa, Base64.DEFAULT);
			byte[] encMsgB = null;
			try {
				Log.d(TAG,"encrypt"+"start=="+String.valueOf(System.currentTimeMillis()));
				encMsgB = desUtil.encryptDes(source.getBytes("utf-8"), keyByte, isDebug());
				Log.d(TAG,"encrypt"+"end=="+String.valueOf(System.currentTimeMillis()));
				byte[] b= desUtil.dencryptDes(encMsgB, keyByte, isDebug());
				Log.d(TAG, "userParam=="+new String(b));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String encMsgBase64 = Base64.encodeToString(encMsgB, Base64.NO_WRAP);
			return encMsgBase64;

		}
	}

	/**
	 * 把上传参数明文进行md5签名
	 * 
	 * @param paramExpressly
	 * @return
	 */
	public static synchronized String encryptMd5(Map<String, String> paramExpressly) {
		String source = PeripheryJson.paramMapToString(paramExpressly);
		String signMsg = null;
		signMsg = MD5Utils.toMD5(source);
		String signMsgBase64 = null;
		try {
			signMsgBase64 = Base64.encodeToString(signMsg.getBytes("utf-8"), Base64.NO_WRAP);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return signMsgBase64;
	}

	/**
	 * 解密下行参数，并且与签名串进行对比
	 * 
	 * @param paramCiphertext
	 * @param paramMd5Sigin
	 * @return
	 */
	public static String dencryptParam(String paramCiphertext, String paramMd5Sigin) throws Exception {
		synchronized (oLock) {
			byte[] keyb = getRsaKey().getBytes();
			byte[] resNoBase64 = Base64.decode(paramCiphertext, Base64.NO_WRAP);
			byte[] resKeyNoBase64 = Base64.decode(keyb, Base64.NO_WRAP);
			byte[] resByte = desUtil.dencryptDes(resNoBase64, resKeyNoBase64, isDebug());
			String res = new String(resByte);
			String resMd5 = MD5Utils.toMD5(res);
			String resMd5Base64 = Base64.encodeToString(resMd5.getBytes("utf-8"), Base64.NO_WRAP);
			if (!resMd5Base64.equals(paramMd5Sigin)) {
				return null;
			}
			return res;
		}
	}

	/**
	 * 解密下行参数，并且与签名串进行对比
	 * 
	 * @param paramCiphertext
	 * @param paramMd5Sigin
	 * @return
	 */
	public static String dencryptParam(SercurityInfoDto sDto) throws HowbuyException {
		synchronized (oLock) {
			if (sDto == null) {
				return null;
			}
			String encMsg = sDto.getReturnEncMsg();
			String encMsgMd5 = sDto.getReturnSignMsg();
			byte[] keyb = getRsaKey().getBytes();
			byte[] resNoBase64 = Base64.decode(encMsg, Base64.NO_WRAP);
			byte[] resKeyNoBase64 = Base64.decode(keyb, Base64.NO_WRAP);
			Log.d(TAG,"dencrypt"+"start=="+ String.valueOf(System.currentTimeMillis()));
			byte[] resByte = desUtil.dencryptDes(resNoBase64, resKeyNoBase64, isDebug());
			Log.d(TAG,"dencrypt"+"end=="+ String.valueOf(System.currentTimeMillis()));
			String res = new String(resByte);
			String resMd5 = MD5Utils.toMD5(res);
			String resMd5Base64;
			try {
				resMd5Base64 = Base64.encodeToString(resMd5.getBytes("utf-8"), Base64.NO_WRAP);
				if (!resMd5Base64.equals(encMsgMd5)) {
					return null;
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new HowbuyException(e.getMessage(), e);
			}
			return res;

		}
	}

	public static String getRsaKey() {
		return ApplicationParams.getInstance().getsF().getString(Cons.SFRsa, "android");
	}

}
