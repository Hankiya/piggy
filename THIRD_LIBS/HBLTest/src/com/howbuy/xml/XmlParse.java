/*
 *@author RenZheng Ray@NeoNan.com  ,2013-3-28
 *
 *QQ: 840094530[url]http://www.weibo.com/u/1630252487[/url]
 *
 */
package com.howbuy.xml;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.howbuy.entity.Function;

public class XmlParse {

	private static boolean parse(ContentHandler handler, InputSource is) {
		SAXParserFactory saxParseFactory = SAXParserFactory.newInstance();
		try {
			XMLReader xmlReader = saxParseFactory.newSAXParser().getXMLReader();
			xmlReader.setContentHandler(handler);
			xmlReader.parse(is);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static  boolean parseExchange( ArrayList<Function> list,String input,int type) {
		InputSource is = new InputSource();
		is.setEncoding("UTF-8");
		is.setCharacterStream(new StringReader(input));
		return parse(new FunctionHandler(list,type), is);
	}
	public static boolean parseExchange( ArrayList<Function> list,  InputStream is,int type) {
		return parse(new FunctionHandler(list,type), new InputSource(is));
	}
	
}
