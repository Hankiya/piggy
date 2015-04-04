package com.howbuy.entity;

import com.howbuy.lib.interfaces.ICharData;
import com.howbuy.lib.utils.StrUtils;

public final class BarDataInf implements ICharData{
	
	public float mVal=0;
	public long mTime=0;
	public int  mType=0;
	public int mColor=0xff00aaaa;
	public String mName="";
	public BarDataInf(float val, int color) {
		super();
		this.mVal = val; 
		this.mColor=color;
	}
	public BarDataInf(float val, String  name, int color) {
		this(val, color);
		this.mName = name; 
	}
	public BarDataInf(float val, String  name,int type,int color) {
		this(val, name,color);
		this.mType=type; 
	}
	public BarDataInf(float val,long time, String  name,int type,int color) {
		this(val, name,type,color); 
		this.mTime=time;
	}
	
	@Override
	public float getValue(int type) {
		return mVal;
	}
	@Override
	public long getTime() { 
		return mTime;
	}
	@Override
	public String getValueStr(int type) {
		// TODO Auto-generated method stub
		return "Y"+StrUtils.formatF(mVal, 2)+"W";
	}
	@Override
	public String getTime(String format) {
		return mName;
	}
	@Override
	public Object getExtras(int type) {
		// TODO Auto-generated method stub
		return null;
	}
    
	private int getColor(int a,int r,int g,int b,float scale){
		int na=Math.min((int) (a*scale), 255);
		int nr=Math.min((int) (r*scale), 255);
		int ng=Math.min((int) (g*scale), 255);
		int nb=Math.min((int) (b*scale), 255);
		int ncolor=(na<<24)|(nr<<16)|(ng<<8)|nb;
		return ncolor;
	}
	
	@Override
	public int getColor(boolean selected) {
		if(selected){
			int a=mColor>>>24;
			int r=(mColor&0x00FF0000)>>>16;
			int g=(mColor&0x0000FF00)>>>8;
			int b=mColor&0x000000FF;
			int ncolor=getColor(a, r, g, b, 1.2f);
			if(ncolor!=mColor){
				return ncolor;
			}else{
				return getColor(a, r, g, b,0.8f);
			} 
		}
		return mColor;
	}
	@Override
	public String toString() {
		return " [mVal=" + mVal + ", mName=" + mName + "]";
	}
	 
	 
}