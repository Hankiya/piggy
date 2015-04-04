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

import android.content.SharedPreferences;

import com.howbuy.component.AppFrame;
import com.howbuy.config.FundConfig;
import com.howbuy.config.FundConfig.FundType;
import com.howbuy.config.FundConfig.SortType;

public class FundConfigHandler extends DefaultHandler {
	private String KEY_SORT_TYPES = "sort_types";
	private String KEY_SORT_TYPE = "sort_type";
	private String KEY_FUND_TYPES = "fund_types";
	private String KEY_FUND_TYPE = "fund_type";
	private String KEY_FUND_SORT = "fund_sort";
	private String KEY_ID = "id";
	private String KEY_TYPENUM = "typenum";
	private String KEY_SORTNUM = "sortnum";
	private String KEY_COLUMN = "column";
	private String KEY_NAME = "name";
	private String KEY_DATATYPE = "datatype";
	private String KEY_CLASSTYPE = "classtype";

	private int[] mSortIndex = null;
	private ArrayList<SortType> mSorts = null;
	private ArrayList<FundType> mTypes = null;
	private FundType mFundType = null;
	private FundConfig mFundConfig = null;
	private String mTag = null;
	StringBuffer sb = new StringBuffer(32);
	SharedPreferences sf = null;

	public FundConfigHandler(FundConfig fundConfig) {
		mFundConfig = fundConfig;
		sf = AppFrame.getApp().getsF();
		mFundConfig.readFlag(sf);
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		String temp = new String(ch, start, length);
		// LogUtils.d("parser","characters-->tag="+mTag+" temp="+temp);
		if (KEY_FUND_SORT.equals(mTag)) {
			sb.append(temp);
		}
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {
		// LogUtils.d("parser","endElement-->tag="+mTag+" localName="+localName);
		if (KEY_FUND_SORT.equals(localName)) {
			String[] nums = sb.toString().split("-");
			int n = nums == null ? 0 : nums.length;
			for (int i = 0; i < n; i++) {
				mSortIndex[i] = parseInt(nums[i], -1);
			}
			mFundType.setSortIndex(mSortIndex);
			mSortIndex = null;
			sb.delete(0, sb.length());
		} else if (KEY_SORT_TYPES.equals(localName)) {
			mFundConfig.setSortType(mSorts);
			mSorts = null;
		} else if (KEY_FUND_TYPES.equals(localName)) {
			mFundConfig.setFundTypes(mTypes);
			sf = null;
			sb = null;
			mTypes = null;
		} else if (KEY_FUND_TYPE.equals(localName)) {
			mTypes.add(mFundType);
			mFundType = null;
		}
		mTag = "";
	}

	public void startElement(String uri, String localName, String qName, Attributes atr)
			throws SAXException {
		mTag = localName;
		// LogUtils.d("parser","startElement-->tag="+mTag+" localName="+localName);
		if (KEY_SORT_TYPE.equals(mTag)) {
			int i = parseInt(atr.getValue(KEY_ID), -1);
			mSorts.add(mFundConfig.newSortType(atr.getValue(KEY_NAME), atr.getValue(KEY_COLUMN), i));
		} else if (KEY_FUND_TYPE.equals(mTag)) {
			String name = atr.getValue(KEY_NAME);
			int datatype = parseInt(atr.getValue(KEY_DATATYPE), 0);
			String classtype = atr.getValue(KEY_CLASSTYPE);
			mFundType = mFundConfig.newFundType(name, datatype, classtype);
		} else if (KEY_FUND_SORT.equals(mTag)) {
			int i = parseInt(atr.getValue(KEY_ID), -1);
			int n = parseInt(atr.getValue(KEY_SORTNUM), 0);
			if (n > 0) {
				mSortIndex = new int[n];
			} else {
				mSortIndex = null;
				i = -1;
			}
			mFundType.Selected = i;
			mFundType.readSelected(sf);
		} else if (KEY_FUND_TYPES.equals(mTag)) {
			int n = parseInt(atr.getValue(KEY_TYPENUM), 0);
			mTypes = new ArrayList<FundConfig.FundType>(n);
		} else if (KEY_SORT_TYPES.equals(mTag)) {
			int n = parseInt(atr.getValue(KEY_TYPENUM), 0);
			mSorts = new ArrayList<SortType>(n);
		}
	}

	private int parseInt(String str, int def) {
		return str == null ? def : Integer.parseInt(str);
	}
}
