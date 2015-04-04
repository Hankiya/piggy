package com.howbuy.lib.utils;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * class for parse JSONObject.
 * 
 * @author rexy 840094530@qq.com
 * @date 2013-11-14 上午8:59:34
 */
public class JsnUtils {
	/**
	 * return string value from JSONObject if key is exist,else return null;
	 * 
	 * @param obj
	 * @param key
	 * @return string if not exist return null;
	 */
	public static String getString(JSONObject obj, String key) {
		String str = null;
		if (obj.has(key)) {
			try {
				str = obj.getString(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return str;
	}

	/**
	 * return long value from JSONObject if key is exist,else return null;
	 * 
	 * @param obj
	 * @param key
	 * @return if not exist return 0;
	 */
	public static long getLong(JSONObject obj, String key) {
		long val = 0;
		if (obj.has(key)) {
			try {
				val = obj.getLong(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return val;
	}

	/**
	 * return double value from JSONObject if key is exist,else return null;
	 * 
	 * @param obj
	 * @param key
	 * @return if not exist return 0;
	 */
	public static double getDouble(JSONObject obj, String key) {
		double val = 0;
		if (obj.has(key)) {
			try {
				val = obj.getDouble(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return val;
	}

	/**
	 * return int value from JSONObject if key is exist,else return null;
	 * 
	 * @param obj
	 * @param key
	 * @return if not exist return 0;
	 */
	public static int getInt(JSONObject obj, String key) {
		int val = 0;
		if (obj.has(key)) {
			try {
				val = obj.getInt(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return val;
	}

	/**
	 * return boolean value from JSONObject if key is exist,else return null;
	 * 
	 * @param obj
	 * @param key
	 * @return if not exist return false;
	 */
	public static boolean getBoolean(JSONObject obj, String key) {
		boolean res = false;
		if(obj.has(key)){
			try {
				res = obj.getBoolean(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}	
		}
		return res;
	}

	/**
	 * return JSONObject value from JSONObject if key is exist,else return null;
	 * 
	 * @param obj
	 * @param key
	 * @return if not exist return 0;
	 */
	public static JSONObject getObject(JSONObject obj, String key) {
		JSONObject o = null;
		if (obj.has(key)) {
			try {
				o = obj.getJSONObject(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return o;
	}

	/**
	 * return JSONArray value from JSONObject if key is exist,else return null;
	 * 
	 * @param obj
	 * @param key
	 * @return if not exist return 0;
	 */
	public static JSONArray getArray(JSONObject obj, String key) {
		JSONArray ary = null;
		if (obj.has(key)) {
			try {
				ary = obj.getJSONArray(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return ary;
	}

	/**
	 * parse json string to JSONObject.
	 * 
	 * @param json
	 *            format string.
	 * @return if success return JSONObject else return null;
	 */
	public static JSONObject getObject(String json) {
		JSONObject o = null;
		try {
			o = new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return o;
	}

	/**
	 * parse json string to JSONArray.
	 * 
	 * @param json
	 *            format string.
	 * @return if success return JSONArray else return null;
	 */
	public static JSONArray getArray(String json) {
		JSONArray ary = null;
		try {
			ary = new JSONArray(json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ary;
	}
	
	/**
	 * 
	 * @param  map
	 * @throws if map is null empty throw NullPointerException.
	 */
	public static JSONObject getObject(HashMap<String, String> map) throws Exception{
		if(map==null||map.isEmpty()){
			throw new NullPointerException("getObject:map is null or empty");
		}else{
			JSONObject obj=new JSONObject();
			Iterator<HashMap.Entry<String, String>> iterator= map.entrySet().iterator();
			while (iterator.hasNext()) {
				HashMap.Entry<String, String> entry=iterator.next();
				String key=URLEncoder.encode(entry.getKey(),"UTF-8"); 
			    String value=URLEncoder.encode(entry.getValue(),"UTF-8"); 
				obj.put(key,value);
			}
			return obj;
		}
	}

}
