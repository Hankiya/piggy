/* Copyright (c) 1995-2000, The Hypersonic SQL Group.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the Hypersonic SQL Group nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE HYPERSONIC SQL GROUP,
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * This software consists of voluntary contributions made by many individuals
 * on behalf of the Hypersonic SQL Group.
 *
 *
 * For work added by the HSQL Development Group:
 *
 * Copyright (c) 2001-2005, The HSQL Development Group
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the HSQL Development Group nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL HSQL DEVELOPMENT GROUP, HSQLDB.ORG,
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.howbuy.lib.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * class for operator of String .time format , regular expression for filter.
 * @author rexy  840094530@qq.com 
 * @date 2013-11-14 下午3:09:00
 */
public class StrUtils {
  

	/**
	 * use a charset format the bytes to string.
	 * @param @param b
	 * @param @param charset  format for encoding the bytes to String
	 */
	public static String byteToString(byte[] b, String charset) {
		try {
			return (charset == null) ? new String(b) : new String(b, charset);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * returns a String from an InputStream.
	 */
	public static String streamToString(InputStream x, int length)
			throws IOException {
		InputStreamReader in = new InputStreamReader(x);
		StringWriter writer = new StringWriter();
		int blocksize = 8 * 1024;
		char[] buffer = new char[blocksize];

		for (int left = length; left > 0;) {
			int read = in.read(buffer, 0, left > blocksize ? blocksize : left);

			if (read == -1) {
				break;
			}
			writer.write(buffer, 0, read);
			left -= read;
		}
		writer.close();

		return writer.toString();
	}

    public static int length(String paramString) {
        int i = 0;
        for (int j = 0; j < paramString.length(); j++) {
            if (paramString.substring(j, j + 1).matches("[Α-￥]"))
                i += 2;
            else
                i++;
        }

        if (i % 2 > 0) {
            i = 1 + i / 2;
        } else {
            i = i / 2;
        }

        return i;
    }
    
	/**
	 * Counts Character c in String s
	 * @param String s
	 * @return int count
	 */
	public static int count(final String s, final char c) {
		int pos = 0;
		int count = 0;
		if (s != null) {
			while ((pos = s.indexOf(c, pos)) > -1) {
				count++;
				pos++;
			}
		}
		return count;
	}

	/**
	 *judge whether or not the given string is null or all character is whitespace.
	 */
	public static boolean isEmpty(String value) {
		//TextUtils.isEmpty(value)
		int strLen;
		if (value == null || (strLen = value.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(value.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 
	 * @param values
	 * @return true if all value is empty,
	 */
	public static boolean areEmpty(String... values) {
		boolean result = true;
		if (values == null || values.length == 0) {
			result = true;
		} else {
			for (String value : values) {
				result = isEmpty(value);
				if (result == false) {
					return false;
				}
			}
		}
		return result;
	}
	
	/**
	 * get time long from a given format time string .
	 * @param @param timeOrDateStr 
	 * @return long  0 if failed
	 */
	public static long getTimeLong(String timeOrDateStr){
		long result=0;
		try{
		if(!isEmpty(timeOrDateStr)){
			if(isNumeric(timeOrDateStr)){
				result=Long.parseLong(timeOrDateStr);
			}else if(isDate(timeOrDateStr)){
				result=java.util.Date.parse(timeOrDateStr);
			}
		}}catch(Exception e){
		e.printStackTrace();
		}
		return result;
	} 
	
	/**
	 * get time long from a given format time and format type.
	 * @param @param formatTime time string formated by SimpleDateFormat type.
	 * @param @param format SimpleDateFormat type
	 * @return long 0 if failed
	 */
	public static long getTimeFormatLong(String formatTime, String format) {
		long result=0;
		try { 
		  SimpleDateFormat sdf  = new SimpleDateFormat(format);
		  result = sdf.parse(formatTime).getTime(); 
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
 
	/**
	 * translate a original format time to a new format time.
	 * @param @param formatTime string value of time that was formated by SimpleDateFormat.
	 * @param @param formatIn original format type that generate the formatTime.
	 * @param @param formatOut output format type . 
	 * @return String  "" if failed.
	 * @throws
	 */
	public static String timeFormat(String formatTime,String formatIn,String formatOut){
		if(!isEmpty(formatTime)){
			 try {
				return new SimpleDateFormat(isEmpty(formatOut)?"yyyy-MM-dd  HH:mm":formatOut)
					.format( new SimpleDateFormat(isEmpty(formatIn)?"yyyy-MM-dd  HH:mm":formatIn).parse(formatTime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	/**
	 * translate a time string to a format time.
	 * @param @param timeOrDateStr a long value string or a Date String.
	 * @param @param formatOut 
	 * @return String "" if failed.
	 * @throws
	 */
	public static String timeFormat(String timeOrDateStr,String formatOut){
		if(!isEmpty(timeOrDateStr)){
			if(isNumeric(timeOrDateStr)){
				return timeFormat(Long.parseLong(timeOrDateStr), formatOut);
			}else if(isDate(timeOrDateStr)){ 
				return new SimpleDateFormat(isEmpty(formatOut)?"yyyy-MM-dd  HH:mm":formatOut)
				.format(new java.util.Date(java.util.Date.parse(timeOrDateStr)));
			}
		}
		return "";
	}
	
	
	/**
	 * format the time with a special format type.
	 * @param  format  format type to format the time.null for default as yyyy-MM-dd  HH:mm ;
	 * @param timeflag a long time to format.
	 */
	public static String timeFormat(Long time,String format) {
		String date = new SimpleDateFormat(isEmpty(format)?"yyyy-MM-dd  HH:mm":format)
				.format(new java.util.Date(time)); 
		return date;
	}
	/**
	 * format the time to yyyy-MM-dd 2013-12-25;
	 * @param @param timeflag a long time to format.
	 */
	public static String timeformat(Long timeflag) {
		return timeFormat(timeflag,"yyyy-MM-dd");
	}
    /**
     * format the time as time state of day.
     * @throws
     */
	public static String timeState(long _timestamp) {
		long millsGap=System.currentTimeMillis()- _timestamp;
		String []msgs=null;
		if(millsGap<0){
			millsGap=0-millsGap;
			msgs=new String[]{"即将","分钟后","今天 HH:mm","明天 HH:mm","d天后 HH:mm"};
		} else{
			msgs=new String[]{"刚刚","分钟前","今天 HH:mm","昨天 HH:mm","d天前 HH:mm"};
		}
		
		if (millsGap < 1 * 60 * 1000) {
			return msgs[0];
		} else if (millsGap < 30 * 60 * 1000) {
			return (millsGap/ 1000 / 60) +msgs[1];
		} else {
			Calendar now = Calendar.getInstance();
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(_timestamp);
			if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)
					&& c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
					&& c.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH)) {
				SimpleDateFormat sdf = new SimpleDateFormat(msgs[2]);
				return sdf.format(c.getTime());
			}
			if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)
					&& c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
					&& Math.abs(c.get(Calendar.DAY_OF_MONTH)- now.get(Calendar.DAY_OF_MONTH))==1) {
				SimpleDateFormat sdf = new SimpleDateFormat(msgs[3]);
				return sdf.format(c.getTime());
			} else if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
				SimpleDateFormat sdf = null;
				int datagap=Math.abs(now.get(Calendar.DATE) - c.get(Calendar.DATE));
				if ( datagap< 8) {
					c.set(Calendar.DATE, datagap);
					sdf = new SimpleDateFormat(msgs[4]);
				} else {
					sdf = new SimpleDateFormat("M月d日 HH:mm");
				}
				return sdf.format(c.getTime());
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yy年M月d日 HH");
				return sdf.format(c.getTime());
			}
		}
	  
	}
	/**
	 *format a float value with format type such as "0.0000"
	 * @param f value to format
	 * @param format if null def is "0.00";
	 * @return
	 */
	public static String formatF(float f, String format) {
		DecimalFormat df = new DecimalFormat(isEmpty(format)?"0.00":format);
		return df.format(f);
	} 

	public static String formatF(float val, int decimal) {
		try {
			if (decimal > 0) {
				String format = "%1$.#f".replace("#", decimal + "");
				String r = String.format(format, val);
				int len = r.length();
				while (r.charAt(--len) == '0')
					;
				if (r.charAt(len) == '.') {
					return r.substring(0, len);
				} else {
					return r.substring(0, ++len);
				}
			}
		} catch (Exception e) {
		}
		return val + "";
	}

	/**
	 * judge whether the string contain chinese
	 * @param str
	 * @return
	 */
	public static boolean isGB2312(String str) {
		String march = "^[\u4e00-\u9fa5]+$";
		return str.matches(march);
	}
	/**
	 */
	public static boolean isNumeric(Object obj) {
		if (obj == null) {
			return false;
		}
		String str = obj.toString();
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNumeric(String str) {
		if (str != null) {
			Pattern pattern = Pattern.compile("[0-9]*");
			return pattern.matcher(str).matches();
		} else {
			return false;
		}
	}

	public static boolean isDate(String str) { 
		Pattern pattern = Pattern
				.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
		return pattern.matcher(str).matches();
	}
	public static boolean isEmail(String str) {
		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		return pattern.matcher(str).matches();
	}

	/*
	 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188 　　
	 * 联通：130、131、132、152、155、156、185、186 　　 电信：133、153、180、189、（1349卫通）
	 * 虚拟运营号段170
	 */
	public static boolean isMobileTel(String mobiles) {
		Pattern p = Pattern
				.compile("^((\\+86)|(86))?(13[0-9]|14[0-9]|15[0-9]|18[0-9]|16[0-9]|19[0-9]|170)\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static ArrayList<String> findAllNumber(String input) {
		ArrayList<String> list = new ArrayList<String>();
		Pattern pattern = Pattern.compile("[0-9]+");
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			list.add(matcher.group());
		}
		return list;
	}

	/**
	 */
	public static String unicodeToChinese(String unicode) {
		StringBuilder out = new StringBuilder();
		if (!isEmpty(unicode)) {
			for (int i = 0; i < unicode.length(); i++) {
				out.append(unicode.charAt(i));
			}
		}
		return out.toString();
	}

	/**
	 */
	public static String stripNonValidXMLCharacters(String input) {
		if (input == null || ("".equals(input)))
			return "";
		StringBuilder out = new StringBuilder();
		char current;
		for (int i = 0; i < input.length(); i++) {
			current = input.charAt(i);
			if ((current == 0x9) || (current == 0xA) || (current == 0xD)
					|| ((current >= 0x20) && (current <= 0xD7FF))
					|| ((current >= 0xE000) && (current <= 0xFFFD))
					|| ((current >= 0x10000) && (current <= 0x10FFFF)))
				out.append(current);
		}
		return out.toString();
	}

 
  
 

	/**
	 *filter all html elements from the str.
	 * @param str
	 * @return String a new string with no html element.
	 */
	public static String filterHtml(String str) {
		return str.replaceAll("<([^>]*)>", "");
	}

	/**
	 * filter part html element with appoint tag.
	 * fiterHtmlTag(str,"a"); filter all html element of a tag. 
	 * @return String
	 */
	public static String fiterHtmlTag(String str, String tag) {
		String regxp = "<\\s*/{0,1}\\s*" + tag + "\\s*([^>]*)\\s*>";
		return str.replaceAll(regxp, "");
	}
 

	/**
	 * remove attribution tagAttrib in Tag beforeTag. input is the whole html
	 * text.
	 * 
	 * @param input
	 * @param tag
	 * @param tagAttrib
	 * @return
	 */
	public static String filterTagAttri(String input, String tag,
			String tagAttrib) {
		String regxpTag = "<\\s*" + tag + "\\s+([^>]*)\\s*>";
		String regxpTagAttrib = tagAttrib + "\\s*=\\s*\"[^\"]*\"";
		Pattern patternTag = Pattern.compile(regxpTag);
		Matcher matcherTag = patternTag.matcher(input);
		StringBuffer sb = new StringBuffer();
		boolean result = matcherTag.find();
		while (result) {
			String ss = "<" + tag + " " + matcherTag.group(1) + ">";
			matcherTag.appendReplacement(sb, ss.replaceAll(regxpTagAttrib, ""));
			result = matcherTag.find();
		}
		matcherTag.appendTail(sb);
		return sb.toString();
	}
}