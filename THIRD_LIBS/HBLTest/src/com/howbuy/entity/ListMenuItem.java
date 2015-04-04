package com.howbuy.entity;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.Intent; 
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Xml;

import com.howbuy.lib.adp.AbsViewHolder;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.StrUtils;

public class ListMenuItem implements Parcelable {
	private String mTitle, mSummary, mAlerm, mState, mExtras;
	private int mIconResid = 0, mType = 0;
	private boolean mClickAble = true, mCheckAble = false, mRArrAble = false,
			mSepAble = false, mChecked = true;
	private Intent mIntent = null;

	public ListMenuItem(String title, String summary, String state) {
		this.mTitle = title;
		this.mSummary = summary;
		this.mState = state;
	}

	public ListMenuItem(String title, String summary, boolean checkAble) {
		this.mTitle = title;
		this.mSummary = summary;
		this.mCheckAble = checkAble;
	}

	public ListMenuItem(String title, int iconResid,boolean rightArr) {
		this.mTitle = title;
		this.mIconResid=iconResid;
		this.mRArrAble = rightArr;
	}
	public ListMenuItem(int type,boolean clickable,boolean checkable,boolean rarrable,boolean sepable) {
	 this.mType=type;
	 this.mClickAble=clickable;
	 this.mCheckAble=checkable;
	 this.mRArrAble=rarrable;
	 this.mSepAble=sepable;
	}
	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public String getSummary() {
		return mSummary;
	}

	public void setSummary(String mSummary) {
		this.mSummary = mSummary;
	}

	public String getAlerm() {
		return mAlerm;
	}

	public void setAlerm(String mAlerm) {
		this.mAlerm = mAlerm;
	}

	public String getState() {
		return mState;
	}

	public void setState(String mState) {
		this.mState = mState;
	}

	public String getExtras() {
		return mExtras;
	}

	public void setExtras(String mExtras) {
		this.mExtras = mExtras;
	}

	public int getIconResid() {
		return mIconResid;
	}

	public void setIconResid(int mIconResid) {
		this.mIconResid = mIconResid;
	}

	public int getType() {
		return mType;
	}

	public void setType(int mType) {
		this.mType = mType;
	}

	public boolean isSepAble() {
		return mSepAble;
	}

	public void setSepAble(boolean mEnableSep) {
		this.mSepAble = mEnableSep;
	}

	public boolean isChecked() {
		return mChecked;
	}

	public void setChecked(boolean mChecked) {
		this.mChecked = mChecked;
	}

	public boolean isClickAble() {
		return mClickAble;
	}

	public void setClickAble(boolean clickAble) {
		this.mClickAble = clickAble;
	}

	public boolean isCheckAble() {
		return mCheckAble;
	}

	public void setCheckAble(boolean checkAble) {
		this.mCheckAble = checkAble;
	}

	public boolean isRArrAble() {
		return mRArrAble;
	}

	public void setRArrAble(boolean rightArrAble) {
		this.mRArrAble = rightArrAble;
	}

	public Intent getIntent() {
		return mIntent;
	}

	public void setIntent(Intent mIntent) {
		this.mIntent = mIntent;
	}

	public static Parcelable.Creator<ListMenuItem> getCreator() {
		return CREATOR;
	}

	public void readFromParcel(Parcel in) {
		mTitle = in.readString();
		mSummary = in.readString();
		mAlerm = in.readString();
		mState = in.readString();
		mExtras = in.readString();
		mIconResid = in.readInt();
		mType = in.readInt();
		mClickAble = in.readInt() == 1;
		mCheckAble = in.readInt() == 1;
		mRArrAble = in.readInt() == 1;
		mSepAble = in.readInt() == 1;
		mChecked = in.readInt() == 1;
		String s = in.readString();
		if (s != null) {
			try {
				mIntent = Intent.parseUri(s, 0);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mTitle);
		dest.writeString(mSummary);
		dest.writeString(mAlerm);
		dest.writeString(mState);
		dest.writeString(mExtras);
		dest.writeInt(mIconResid);
		dest.writeInt(mType);
		dest.writeInt(mClickAble ? 1 : 0);
		dest.writeInt(mCheckAble ? 1 : 0);
		dest.writeInt(mRArrAble ? 1 : 0);
		dest.writeInt(mSepAble ? 1 : 0);
		dest.writeInt(mChecked ? 1 : 0);
		dest.writeString(mIntent == null ? null : mIntent.toURI());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<ListMenuItem> CREATOR = new Parcelable.Creator<ListMenuItem>() {
		public ListMenuItem createFromParcel(Parcel in) {
			ListMenuItem r = new ListMenuItem(null, null, null);
			r.readFromParcel(in);
			return r;
		}

		public ListMenuItem[] newArray(int size) {
			return new ListMenuItem[size];
		}
	};

	public static interface IMenuViewAdjust {
		void adjustMenuView(int type, AbsViewHolder<ListMenuItem> holder,boolean isReuse);
	}
	
	private static ListMenuItem parseMenuItem(XmlPullParser p){
		int type=Integer.parseInt(p.getAttributeValue(null, "type"));
		boolean clickable=Boolean.parseBoolean(p.getAttributeValue(null, "clickable"));
		boolean checkable=Boolean.parseBoolean(p.getAttributeValue(null, "checkable"));
		boolean rarrable=Boolean.parseBoolean(p.getAttributeValue(null, "rarrable"));
		boolean sepable=Boolean.parseBoolean(p.getAttributeValue(null, "sepable"));
		ListMenuItem item=new ListMenuItem(type, clickable, checkable, rarrable, sepable);
		return item;
	}
	private static void parseMenuItemInf(ListMenuItem item,String tag,XmlPullParser p){
		String text=p.getText();
		text=text==null?"":text.trim();
		if("title".equals(tag)){
		  item.setTitle(text);
		}else if("summary".equals(tag)){
			 item.setSummary(text);
		}else if("state".equals(tag)){
			 item.setState(text);
		}else if("checked".equals(tag)){
			item.setChecked(Boolean.parseBoolean(text));
		}else if("intent".equals(tag)){
			if(!StrUtils.isEmpty(text)){
				try {
					item.setIntent(Intent.parseUri(text, 0));
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
		} 
	}
	public static ArrayList<ListMenuItem> parseMenus(Context cx,String assetsFile) throws XmlPullParserException, IOException {
        InputStream is=cx.getAssets().open(assetsFile);
        XmlPullParser parser=Xml.newPullParser();
        parser.setInput(is, "UTF-8");
        return parseMenus(cx, parser);

	}
	
	public static ArrayList<ListMenuItem> parseMenus(Context cx,int xmlId)
			throws XmlPullParserException, IOException {
		return parseMenus(cx, cx.getResources().getXml(xmlId));
	}
	
	public static ArrayList<ListMenuItem> parseMenus(Context cx,XmlPullParser parser)
			throws XmlPullParserException, IOException {
		ArrayList<ListMenuItem> list=null;
		ListMenuItem  item=null;
		String tag=null;
		int event = parser.getEventType();
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				if(list==null){
					list=new ArrayList<ListMenuItem>();
				}
				break;
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("menu_item")) { 
					item=parseMenuItem(parser);
				}else{
				    tag=parser.getName();
				}
				break;
			case XmlPullParser.TEXT: 
				if(tag!=null){
					if(tag.equals("icon")){
						int resid=cx.getResources().getIdentifier(parser.getText(), "drawable", cx.getPackageName());
						item.setIconResid(resid);
					}else{
						parseMenuItemInf(item,tag,parser); 	
					}
				}
			 break;
			case XmlPullParser.END_TAG:
				if (parser.getName().equals("menu_item")) {
					list.add(item); 
				}else{
					tag=null;
				}
				 
				break;
			}
			event = parser.next();
		}
		return list;
	}

	public String toShortString() {
		return mTitle + " " + mSummary
				+ " " + mState;
	}
	
	
}
