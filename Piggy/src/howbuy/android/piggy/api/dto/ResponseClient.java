package howbuy.android.piggy.api.dto;

import howbuy.android.piggy.api.PeripheryJson;
import howbuy.android.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;

import android.text.TextUtils;

public class ResponseClient {
	/**
	 * 签名串过期
	 */
	public static final String VERIFICATION_CODE_EXPIRED = "B003";
	/**
	 * 签名串错误
	 */
	public static final String VERIFICATION_CODE_Error = "B004";
	/**
	 * 明文
	 */
	public static final String RESPONSE_CONTENT_TYPE_PLATEXT = "1";
	/**
	 * 密文
	 */
	public static final String RESPONSE_CONTENT_TYPE_CIPHERTEXT = "0";

	/**
	 * 响应成功
	 */
	public static final String RESPONSE_RES_SUCCESS = "0000";
	
	/**
	 * 强制登陆
	 */
	public static final String RESPONSE_RES_FORCELOGIN = "B006";//B9000
	
	/**
	 * 非银联渠道B9800
	 */
	public static final String RESPONSE_RES_BindCard_Other = "B9800";
	
	/**
	 * 未鉴权限额
	 */
	public static final String RESPONSE_RES_Beyound = "1222";
	/**
	 * 未鉴权限额
	 */
	public static final String RESPONSE_RES_leater = "4007";

	/**
	 * 密文
	 */
	SercurityInfoDto sercurityInfoDto;

	/**
	 * 明文
	 */
	ResponseContentDto responseContentDto;

	/** 是否过期 **/
	public boolean isSercurity;

	public ResponseClient(InputStream netStream) {
		String jsonString = null;
		try {
			jsonString = StringUtil.inputStream2String(netStream);
			netStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (TextUtils.isEmpty(jsonString)) {
			return;
		}

		ResponseDto res = PeripheryJson.getResponseDto(jsonString);

		if (res.getContentType().equals(RESPONSE_CONTENT_TYPE_PLATEXT)) {
			responseContentDto = PeripheryJson.resolveExpressly(res.getContent());
			isSercurity = false;
		} else {
			sercurityInfoDto = PeripheryJson.resolveCiphertext(res.getContent());
			isSercurity = true;
		}
	}

	public SercurityInfoDto getSercurityInfoDto() {
		return sercurityInfoDto;
	}

	public void setSercurityInfoDto(SercurityInfoDto sercurityInfoDto) {
		this.sercurityInfoDto = sercurityInfoDto;
	}

	public ResponseContentDto getResponseContentDto() {
		return responseContentDto;
	}

	public void setResponseContentDto(ResponseContentDto responseContentDto) {
		this.responseContentDto = responseContentDto;
	}

	public boolean isSercurity() {
		return isSercurity;
	}

	public void setSercurity(boolean isSercurity) {
		this.isSercurity = isSercurity;
	}

	@Override
	public String toString() {
		return "ResponseClient [sercurityInfoDto=" + sercurityInfoDto + ", responseContentDto=" + responseContentDto + ", isSercurity=" + isSercurity + "]";
	}

	

}
