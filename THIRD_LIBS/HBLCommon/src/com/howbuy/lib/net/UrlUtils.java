package com.howbuy.lib.net;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;

import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.StrUtils;
/**
 * @author rexy  840094530@qq.com 
 * @date 2014-2-28 下午1:36:33
 */
public class UrlUtils {
	public static final String CHARSET_UTF8="UTF-8";
	public static String URL_BASE_RELEASE=null;
	public static String URL_BASE_DEBUG=null;
	private static HashMap<String, String> MAP_NET_PARAM=null;
	public static void initUrlBase(String urlRelease,String urlDebug){
		URL_BASE_RELEASE=urlRelease;
		URL_BASE_DEBUG=urlDebug;
	}
	
	public static String buildUrl(String rootUrl,String subUrl){
		if(!StrUtils.isEmpty(subUrl)){
			boolean a=rootUrl.endsWith("/"),b=subUrl.startsWith("/");
			boolean or=a||b,and=a&&b;
		    if(or&&(!and)){
		    	return rootUrl+subUrl;
		    }else{
		    	if(and){
		    		return rootUrl+subUrl.substring(1);
		    	}
		    	if(!or){
		    		return rootUrl+"/"+subUrl;
		    	}
		    }
		} 
		return rootUrl;
	}
	public static String buildUrl(String subUrl){
		String root=LogUtils.mDebugUrl?URL_BASE_DEBUG:URL_BASE_RELEASE;
		return buildUrl(root,subUrl);
	}

	public static String buildUrl(String subUrl,HashMap<String, String> map) throws Exception{
		  return buildUrlRaw(buildUrl(subUrl), getUrlParams(map));
	}
	public static String buildUrlPublic(String subUrl,HashMap<String, String> map) throws Exception{
	    return buildUrlRaw(buildUrl(subUrl, map), getUrlParams(getPublicParams()));
	}
	
	/**
	 * @param  urlRoot this must not be empty or null.
	 * @throws
	 */
	public static String buildUrlRaw(String urlRoot,String urlParams){
		if(!StrUtils.isEmpty(urlParams)){
			if(urlRoot.contains("?")){
				if(urlRoot.endsWith("?")){
					return urlRoot+urlParams;
				}else{
					return urlRoot+"&"+urlParams;
				}
			}else{
				return urlRoot+"?"+urlParams;
			}
		}
	    return urlRoot;
	}
	
	public static String buildUrlRaw(String urlRoot,HashMap<String, String> map) throws Exception{
		 return buildUrlRaw(urlRoot, getUrlParams(map));
	}
	
	public static String buildUrlRawPublic(String urlRoot,HashMap<String, String> map) throws Exception{
	    return buildUrlRaw(buildUrlRaw(urlRoot, map), getUrlParams(getPublicParams()));
	}
	
	public static HashMap<String, String> getPublicParams(){
		if(MAP_NET_PARAM==null){
			MAP_NET_PARAM=GlobalApp.getApp().getMapStr();
		}
		return MAP_NET_PARAM;
	}
	public static String getUrlParams(HashMap<String, String> map ,String charset)throws Exception{
		if(map==null||map.isEmpty()){
			return null;
		}
		String set=StrUtils.isEmpty(charset)?CHARSET_UTF8:charset;
		HashMap.Entry<String, String> entry=null;
		Iterator<HashMap.Entry<String, String>> iterator= map.entrySet().iterator();
		StringBuilder sb = new StringBuilder();
		while (iterator.hasNext()) {
			entry=iterator.next();
			sb.append(URLEncoder.encode(entry.getKey(), set));
			sb.append('=');
			sb.append((URLEncoder.encode(entry.getValue(), set)));
			sb.append('&');
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	public static String getUrlParams(HashMap<String, String> map) throws Exception {
		 return getUrlParams(map, null);
	}
	
	public static String getUrlRoot(){
		return LogUtils.mDebugUrl?URL_BASE_DEBUG:URL_BASE_RELEASE;
	}
	
 
	public static HashMap<String, String> getUrlParams(String url) {
		if(StrUtils.isEmpty(url)){
			return null;
		}
		int sep=url.indexOf("?");
		if(sep!=-1){
			url=url.substring(sep+1);
		}
		HashMap<String, String> map =null;
		if(url.contains("=")){
			map= new HashMap<String, String>();
			String[] pairs = url.split("&");
			if (pairs != null && pairs.length > 0) {
				for (String pair : pairs) {
					String[] param = pair.split("=", 2);
					if (param != null && param.length == 2) {
						map.put(param[0], param[1]);
					}
				}
			}	
		}
		return map;
	}
	 
    public static String getUrlHead(String url){
    	if (StrUtils.isEmpty(url)) {
    		return null;
    	}
    	int sep=url.indexOf("?");
    	if(sep!=-1){
    		return url.substring(0, sep);
    	}
    	return url;
    }
 
	
	
	
	
	
	
	
	
	
	
	
	
	
}
