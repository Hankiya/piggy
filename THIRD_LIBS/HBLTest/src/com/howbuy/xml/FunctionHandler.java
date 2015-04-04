/*
 *@author RenZheng Ray@NeoNan.com  ,2013-3-28
 *
 *QQ: 840094530[url]http://www.weibo.com/u/1630252487[/url]
 *
 */
package com.howbuy.xml;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.howbuy.entity.Function;

public class FunctionHandler extends DefaultHandler {
	private String mActivity=null;
	ArrayList<Function> mList=null;
	private Function mItem=null;
	private String mTag = null;
	StringBuffer sb=new StringBuffer(32);
	private int mType=1;
	public FunctionHandler(ArrayList<Function> list,int type) {
		mList=list;
		mType=type;
	}
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		  String temp = new String(ch, start, length);
		  if("function".equals(mTag)){
		   sb.append(temp);
		  } 
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if ("function".equals(localName)) {
			mList.add(mItem);
			mItem.setParams(sb.toString());
			sb.delete(0, sb.length());
		}
		mTag = "";
	}

	public void startElement(String uri, String localName, String qName,
			Attributes atr) throws SAXException {
		// 记录开始的标签名，以便文本匹配时用。
		mTag = localName;
		if("function".equals(mTag)){
			String name=atr.getValue("name");
			String ret=atr.getValue("return");
			String des=atr.getValue("funcdescribe");
			String suburl=atr.getValue("url");
			mItem=new Function(name, ret, des, suburl);
			mItem.mTarget=mType==1?mActivity:suburl;
		}else if("interfaces".equals(mTag)){
			mActivity=atr.getValue("activity");
		}
	}
}
