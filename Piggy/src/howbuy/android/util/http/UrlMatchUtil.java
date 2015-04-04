package howbuy.android.util.http;

import howbuy.android.bean.UpdateJsonBase;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.error.HowbuyException;
import howbuy.android.piggy.parameter.GlobalParams;
import howbuy.android.util.Cons;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * 组合连接字符串
 * 
 * @author yescpu
 * 
 */
public class UrlMatchUtil {
	//uat
	//qa
	//dev
	
	public static final String HTM = ".htm";
	public static final String HTML = ".html"; 
	
	
	public static final String basePath2 = "http://trade.ehowbuy.com/static/web/images/piggy/banks/";//银行图标
	public static final String basePath1 = "http://data.howbuy.com/hws/";// 掌上基金
	public static final String basePath =   "https://trade.ehowbuy.com/tws/";	// 生产环境
	
	public static final String basePath2_test = "http://trade.ehowbuy.test/static/web/images/piggy/banks/";
	public static final String basePath1_test = "http://192.168.121.108:8080/wap/";
//	public static final String basePath1_test = "http://192.168.220.108:6080/howbuy-wireless/";
//	public static final String basePath_test = "http://192.168.220.105:6080/howbuy-trade-customer/";
	public static final String basePath_test = "http://192.168.121.108:8080/wap/";
	
	/**
	 * 测试
	 */
//  http://192.168.220.105:8070/hws_newest/ //测试检查更新
//  http://192.168.220.108:6080/howbuy-wireless/ //Qa
//	http://10.70.70.21:7080/hws//仿真
	
//  http://192.168.220.105:6080/Howbuy-Trade-Wireless/ //测试环境1
//  http://192.168.220.105:6080/howbuy-trade-customer/ //测试环境2
//	http://10.168.109.22:8080/twsuat/ //仿真环境
//	http://192.168.220.105:6080/tws/ //集成测试环境
//	http://192.168.121.21:8080/howbuy-trade-piggy/ //振岭
// 	http://192.168.121.21:8088/howbuy-trade-customer//振林交易sdk 3.0

	

	/**
	 * 返回一个头
	 * 
	 * @param url
	 * @return
	 */
	public static String urlSimple(String url) {
		if (url != null) {
			return getBasepath() + url;
		} else {
			return getBasepath();
		}
	}
	
	public static String getBasepath2() {
		if (GlobalParams.getGlobalParams().isDebugFlag()) {
			return basePath2_test;
		}else {
			return basePath2;
		}
	}
	
	
	public static String getBasepath1() {
		if (GlobalParams.getGlobalParams().isDebugFlag()) {
			return basePath1_test;
		}else {
			return basePath1;
		}
	}

	public static String getBasepath() {
		if (GlobalParams.getGlobalParams().isDebugFlag()) {
			return basePath_test;
		}else {
			return basePath;
		}
	}

	/**
	 * 加个前缀
	 * @param head
	 * @param path
	 * @param params
	 * @param context
	 * @return
	 * @throws HowbuyException
	 */
	private static String doUrlUtilOnPublic(String head,String path, Map<String, String> params, Context context) throws HowbuyException {
		StringBuilder builder = new StringBuilder(head + path + "?");
		builder.append(getParams(ApplicationParams.getInstance().getPubNetParams()));
		if (params != null) {
			builder.append("&").append(getParams(params));
		}
		return builder.toString();
	}
	
	/**
	 * （交易）需要公共参数（交易）
	 * 
	 * @param path
	 * @param params
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String urlUtilOnPublic(String path, Map<String, String> params, Context context) throws HowbuyException {
		return doUrlUtilOnPublic(getBasepath(), path, params, context);
	}
	
	/**
	 * （交易）需要公共参数（交易）basepath需传入
	 * @param path
	 * @param basePath
	 * @param params
	 * @param context
	 * @return
	 * @throws HowbuyException
	 */
	public static String urlUtilOnPublicP(String basePath,String path, Map<String, String> params, Context context) throws HowbuyException {
		return doUrlUtilOnPublic(basePath, path, params, context);
	}
	
	
	/**
	 * （普通）需要公共参数
	 * 
	 * @param path
	 * @param params
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String urlUtilOnPublic1(String path, Map<String, String> params, Context context) throws HowbuyException {
		return doUrlUtilOnPublic(getBasepath1(), path, params, context);
	}

	/**
	 * 不需要公共参数
	 * 
	 * @param path
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String urlUtilUnPublic(String path, Map<String, String> params) throws HowbuyException {
		StringBuilder builder = new StringBuilder(getBasepath() + path + "?");
		return builder.append(getParams(params)).toString();
	}
	
	/**
	 * 不需要公共参数
	 * @param basePath
	 * @param path
	 * @param params
	 * @return
	 * @throws HowbuyException
	 */
	public static String urlUtilUnPublicP(String basePath,String path, Map<String, String> params) throws HowbuyException {
		StringBuilder builder = new StringBuilder(basePath + path + "?");
		return builder.append(getParams(params)).toString();
	}

	
	/**
	 * Post数据
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String urlUtilPost(String path) throws HowbuyException {
		StringBuilder builder = new StringBuilder(getBasepath() + path);
		return builder.toString();
	}
	
	/**
	 * Post数据1
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String urlUtilPost1(String path) throws HowbuyException {
		StringBuilder builder = new StringBuilder(getBasepath1() + path);
		return builder.toString();
	}

	/**
	 * 工具类
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private static StringBuilder getParams(Map<String, String> map) throws HowbuyException {
		StringBuilder pathBuilder = new StringBuilder();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			try {
				pathBuilder.append(URLEncoder.encode(entry.getKey(), "UTF-8")).append('=').append((URLEncoder.encode(entry.getValue(), "UTF-8"))).append("&");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new HowbuyException("请求参数编码错误");
			}
		}
		pathBuilder.deleteCharAt(pathBuilder.length() - 1);
		return pathBuilder;
	}

	public static UpdateJsonBase getPubUpdataBean(SharedPreferences sf) {
		UpdateJsonBase upd = new UpdateJsonBase();
		upd.setChannelId(sf.getString(Cons.SFfirstChanneIdId, ""));
		upd.setiVer(sf.getString(Cons.SFfirstIVer, ""));
		upd.setParPhoneModel(sf.getString(Cons.SFfirstParPhoneModel, ""));
		upd.setProductId(sf.getString(Cons.SFfirstProductId, ""));
		upd.setSubPhoneModel(sf.getString(Cons.SFfirstSubPhoneModel, ""));
		upd.setToken(sf.getString(Cons.SFfirstUUid, ""));
		upd.setVersion(sf.getString(Cons.SFfirstVersion, ""));
		return upd;
	}

	/**
	 * 塞入可选参数
	 * 
	 * @param map
	 * @param pramName
	 * @param pram
	 */
	public static void addPram(Map<String, String> map, String pramName, String pram) {
		if (!TextUtils.isEmpty(pram)) {
			map.put(pramName, pram);
		}
	}
}
