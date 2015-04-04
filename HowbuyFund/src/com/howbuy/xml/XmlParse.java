/*
 *@author RenZheng Ray@NeoNan.com  ,2013-3-28
 *
 *QQ: 840094530[url]http://www.weibo.com/u/1630252487[/url]
 *
 */
package com.howbuy.xml;

import java.io.InputStream;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.howbuy.config.FundConfig;

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

	public static boolean parseFundConfig(FundConfig fundConfig, InputStream is) {
		return parse(new FundConfigHandler(fundConfig), new InputSource(is));
	}

}
