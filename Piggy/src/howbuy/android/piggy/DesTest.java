package howbuy.android.piggy;

import howbuy.android.piggy.api.dto.LoginDto;
import howbuy.android.piggy.api.dto.ResponseContentDto;
import howbuy.android.piggy.api.dto.ResponseDto;
import howbuy.android.piggy.jni.DesUtil;
import howbuy.android.util.MD5Utils;
import howbuy.android.util.StringUtil;
import howbuy.android.util.http.UrlConnectionUtil;
import howbuy.android.util.http.UrlMatchUtil;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Base64;
import android.util.Log;

import com.google.myjson.Gson;

public class DesTest extends InstrumentationTestCase {

	public static final char[] SrcRsaDataTemp = { 0x49, 0x2B, 0xF0, 0x48, 0x94, 0x01, 0x35, 0xA6, 0x3D, 0x25, 0xE2, 0x8C, 0x21, 0xED, 0xCC, 0x73, 0xCC, 0x08, 0x26, 0x97, 0x22,
			0x9B, 0xDD, 0x71, 0x1C, 0x40, 0x01, 0x99, 0x95, 0x6C, 0xB6, 0xE9, 0x3D, 0xB6, 0x73, 0x58, 0x0D, 0x71, 0x2E, 0xD2, 0xC6, 0x83, 0xCA, 0xB7, 0xC1, 0xE0, 0x25, 0xD5, 0x43,
			0x15, 0x6E, 0xAE, 0xCF, 0x2E, 0x77, 0x6A, 0xB6, 0x6F, 0xBF, 0x6C, 0xD6, 0xF4, 0xFD, 0xED, 0x92, 0x3F, 0x9B, 0xD4, 0x4B, 0x0C, 0x3F, 0x62, 0x25, 0x20, 0x4B, 0x33, 0x6C,
			0xAA, 0xAD, 0x9A, 0x77, 0xD8, 0xC6, 0xB3, 0xC0, 0xE6, 0xF2, 0x15, 0xF4, 0x09, 0x7D, 0xBC, 0x48, 0xE1, 0x82, 0xE6, 0xD9, 0x39, 0x8C, 0x3F, 0x68, 0xE9, 0xEE, 0xB9, 0xD1,
			0x87, 0x31, 0x5F, 0x9B, 0x66, 0xD6, 0xB3, 0xD2, 0x46, 0x8C, 0x93, 0x80, 0xCC, 0x67, 0x3E, 0x7D, 0xE8, 0x90, 0x15, 0xE9, 0x5A, 0x98, 0x16 };

	static {
		System.loadLibrary("desrsa");
	}

	public void testDes() throws Exception {
		Context context = getInstrumentation().getContext();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				byte[] byteKeyArray = new byte[128];
				for (int i = 0; i < SrcRsaDataTemp.length; i++) {
					byteKeyArray[i] = (byte) SrcRsaDataTemp[i];
				}

				DesUtil desTool = new DesUtil();

				byte[] srcByte = new String("您妹的").getBytes();
				Long a = System.currentTimeMillis();
				for (int i = 0; i < 10; i++) {
					// 先加密
					byte[] destByteArray = desTool.encryptDes(srcByte, byteKeyArray,true);

					// String base64String =
					// Base64.encodeToString(destByteArray, Base64.DEFAULT);
					// Log.d("pcyan", base64String);

					// 再解密
					byte[] decodeByteArray = desTool.dencryptDes(destByteArray, byteKeyArray,true);
					String destString = new String(decodeByteArray);
					Log.d("pcyan", destString);
				}
				Long b = System.currentTimeMillis();
				Log.d("pcyan", String.valueOf(b - a));
			}
		}).start();
		Log.d("pcyan", String.valueOf(System.currentTimeMillis()));

		String path = "";
		InputStream in = UrlConnectionUtil.getInstance().executeGet(path);
	}

	public void testEntry() throws Exception {
		Context context = getInstrumentation().getContext();
		String path = "login/login.htm";
		String pathKey = "common/getsercuritykey.htm";
		Gson gson = new Gson();
		DesUtil desUtil = new DesUtil();

		// tokenID=XXX&encMsg=XXX&signMsg=XXX
		String tokenID = "mToken01";

		Map<String, String> map = new HashMap<String, String>();
		map.put("tokenID", tokenID);
		String resUrl = UrlMatchUtil.urlUtilUnPublic(pathKey, map);
		Log.i("impl", resUrl);
		// 获取加密串
		InputStream in = UrlConnectionUtil.getInstance().executeGet(resUrl);
		String key = StringUtil.inputStream2String(in);
		ResponseDto res = gson.fromJson(key, ResponseDto.class);
		LinkedHashMap<String, String> sonHashMap = (LinkedHashMap<String, String>) res.getContent();
		ResponseContentDto rDto = gson.fromJson(gson.toJson(res.getContent()), ResponseContentDto.class);
		// key = rDto.getBody().toString();
		key = rDto.getBody().toString();
		Log.i("3ds", key);

		// ResponseContentDto rDto=
		// gson.fromJson(gsonSon,ResponseContentDto.class);
		// key = (String) rDto.getBody();
		// key =
		// "oQPWGak9gV9WWDtp2FMvJaFl8Eu837bJ5iQI/bfoFaHAkrmlcTepzwmg4/bO0CE0rxSi09/0p7rk\r\numXLyfgmo+KFvFq9Ff9LatOjcAm7CdzNrEM53t0oxGc4sepfHUyMFaM2/C9pmq+1qn6lI7FL49BN\r\nMkymuepq46t7XDckNGE=";

//		LoginDto lInfo = new LoginDto("1988", "thisispassword地主");
		LoginDto lInfo = new LoginDto();
		String source = gson.toJson(lInfo);

		byte[] keyByte = Base64.decode(key, Base64.DEFAULT);
		// String mRsaKey= new String(desUtil.dencryptRsa(null, keyByte));
		// Log.i("3ds", "mRsaKey---"+mRsaKey);
		byte[] encMsgB = desUtil.encryptDes(source.getBytes("utf-8"), keyByte,true);
		String encMsgBase64 = Base64.encodeToString(encMsgB, Base64.NO_WRAP);
		String signMsg = MD5Utils.toMD5(source);
		String signMsgBase64 = Base64.encodeToString(signMsg.getBytes(), Base64.NO_WRAP);
		// String jiemi=new String(desUtil.dencryptDes(encMsgB,
		// key.getBytes()),"utf-8");
		map.clear();
		map.put("tokenID", tokenID);
		map.put("encMsg", encMsgBase64);
		map.put("signMsg", signMsgBase64);
		// 执行业务
		resUrl = UrlMatchUtil.urlUtilUnPublic(path, map);
		Log.i("impl", resUrl);
		in = UrlConnectionUtil.getInstance().executeGet(resUrl);
		String resString = StringUtil.inputStream2String(in);
		// 解密
		deCodeEnc(resString, key);
	}

	public void deCodeEnc(String resString, String key) {
		Gson gson = new Gson();
		DesUtil desUtil = new DesUtil();
		ResponseDto lizhuan = gson.fromJson(resString, ResponseDto.class);
		String resCode = lizhuan.getContentType();
		if (resCode.equals("2")) {// 正确
			LinkedHashMap<String, String> sonHashMap = (LinkedHashMap<String, String>) lizhuan.getContent();
			String encMsg = sonHashMap.get("returnEncMsg");
			String signMd5Msg = sonHashMap.get("returnSignMsg");
			byte[] resNoBase64 = Base64.decode(encMsg, Base64.NO_WRAP);
			byte[] resKeyNoBase64 = Base64.decode(key.getBytes(), Base64.NO_WRAP);
			byte[] resByte = desUtil.dencryptDes(resNoBase64, resKeyNoBase64,true);
			String res = new String(resByte);
			String resMd5 = MD5Utils.toMD5(res);

			if (resMd5.equals(signMd5Msg)) {
				System.out.println(res);
			}

		} else if (resCode.equals("2")) {// 错误

		} else if (resCode.equals("2")) {// 过期

		}
	}

	class MObject {
		String phon;
		String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPhon() {
			return phon;
		}

		public void setPhon(String phon) {
			this.phon = phon;
		}
	}
	
	public void testMoreThread(){
		String signMsg = MD5Utils.toMD5("{\"requestId\":\"4\",\"custNo\":\"Y25SRy8wd0FxaUR4QlRRMjlVY0VWdz09\"}");
		try {
			String signMsgBase64 = Base64.encodeToString(signMsg.getBytes(), Base64.NO_WRAP);
			System.out.println(signMsgBase64);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		for (int i = 0; i < 100; i++) {
//			new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
////					String userNo=UserUtil.getUserNo();
//					DispatchAccessData.getInstance().commitLogin("43053198804204198", "qq1111");
//					
//					
//				}
//			}).start();
//			
//		}
	}
}
