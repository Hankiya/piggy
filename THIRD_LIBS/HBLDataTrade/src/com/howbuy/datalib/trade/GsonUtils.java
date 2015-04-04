package com.howbuy.datalib.trade;

import howbuy.android.piggy.api.dto.RespondCipher;
import howbuy.android.piggy.api.dto.RespondExpress;
import howbuy.android.piggy.api.dto.RespondResult;

import com.google.myjson.Gson;

/**
 * @ClassName: PeripheryJson
 * @Description:
 * @author yescpu yes.cpu@gmail.com
 * @date 2013-9-12上午9:29:46
 */
public class GsonUtils {
	private static Gson mGson;
	static {
		mGson = new Gson();
	}

	/**
	 * 封装map数据
	 * 
	 * @param paramMap
	 * @return
	 */
	public synchronized static String toJson(Object obj) {
		return mGson.toJson(obj);
	}

	public synchronized static <T> T toObj(String json, Class<T> cls) {
		return mGson.fromJson(json, cls);
	}

	public synchronized static <T> T toObj(String json, java.lang.reflect.Type t) {
		return mGson.fromJson(json, t);
	}

	/**
	 * 解析外围数据
	 * 
	 * @param json
	 * @return
	 */
	public static RespondResult getRespond(String json) {
		RespondResult responseDto = mGson.fromJson(json, RespondResult.class);
		if (responseDto != null) {
			return responseDto;
		} else {
			return null;
		}
	}

	/**
	 * 解析明文
	 */
	public static RespondExpress getExpressly(Object expressly) {
		if (expressly != null) {
			String jsonString = null;
			if (expressly instanceof String) {
				jsonString = expressly.toString();
			} else {
				jsonString = mGson.toJson(expressly);
			}
			return mGson.fromJson(jsonString, RespondExpress.class);
		}
		return null;
	}

	/**
	 * 解析密文
	 */
	public static RespondCipher getCiphertext(Object ciphertext) {
		if (ciphertext != null) {
			String jsonString = null;
			if (ciphertext instanceof String) {
				jsonString = ciphertext.toString();
			} else {
				jsonString = mGson.toJson(ciphertext);
			}
			RespondCipher res = mGson.fromJson(jsonString, RespondCipher.class);
			return res;
		} else {
			return null;
		}
	}

}
