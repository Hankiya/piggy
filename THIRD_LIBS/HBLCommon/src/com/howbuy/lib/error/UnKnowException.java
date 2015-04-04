package com.howbuy.lib.error;
/**
 * @author rexy  840094530@qq.com 
 * @date 2014-2-28 下午1:38:24
 */
public class UnKnowException extends Exception{
	private static final long serialVersionUID = 8612068213996658602L;
	protected int mCode=0;
	protected String  mMsgDes=null;
	protected boolean mParsed=false;
	public UnKnowException(String message) {
		super(message);
		mParsed=parseException(message);
	}
	protected boolean parseException(String message){
		
		return true;
	}
	public boolean hasParseExcep(){
		return mParsed;
	}
	public void setMessage(int code ,String msgDes){
		mCode=code;
		mMsgDes=msgDes;
	}
	@Override
	public String toString() {
		return "UnKnowException [mCode=" + mCode + ", mMsgDes=" + mMsgDes
				+ ", mParsed=" + mParsed + ", toString()=" + super.toString()
				+ "]";
	}
	
}
