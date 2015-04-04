package howbuy.android.piggy.api;

import howbuy.android.piggy.api.dto.ResponseContentDto;
import howbuy.android.piggy.api.dto.ResponseDto;
import howbuy.android.piggy.api.dto.SercurityInfoDto;

import java.util.Map;

import com.google.myjson.Gson;
import com.google.myjson.GsonBuilder;

/**
 * 
 * @ClassName: PeripheryJson
 * @Description:
 * @author yescpu yes.cpu@gmail.com
 * @date 2013-9-12上午9:29:46
 */
public class PeripheryJson {
	private static Gson mGson;

	static {
		mGson = new GsonBuilder().serializeNulls().create();
	}

	/**
	 * 解析外围数据
	 * 
	 * @param json
	 * @return
	 */
	public static ResponseDto getResponseDto(String json) {
		ResponseDto responseDto = mGson.fromJson(json, ResponseDto.class);
		if (responseDto != null) {
			return responseDto;
		} else {
			return null;
		}
	}

	/**
	 * 封装map数据
	 * 
	 * @param paramMap
	 * @return
	 */
	public synchronized static String paramMapToString(Map<String, String> paramMap) {
		return mGson.toJson(paramMap);

	}

	/**
	 * 解析明文
	 */
	public static ResponseContentDto resolveExpressly(Object expressly) {
		if (expressly != null) {
			String jsonString = null;
			if (expressly instanceof String) {
				jsonString = expressly.toString();
			} else {
				jsonString = mGson.toJson(expressly);
			}
			ResponseContentDto res = mGson.fromJson(jsonString, ResponseContentDto.class);
			return res;
		} else {
			return null;
		}
	}

	/**
	 * 解析密文
	 */
	public static SercurityInfoDto resolveCiphertext(Object ciphertext) {
		if (ciphertext != null) {
			String jsonString = null;
			if (ciphertext instanceof String) {
				jsonString = ciphertext.toString();
			} else {
				jsonString = mGson.toJson(ciphertext);
			}
			SercurityInfoDto res = mGson.fromJson(jsonString, SercurityInfoDto.class);
			return res;
		} else {
			return null;
		}
	}

}
