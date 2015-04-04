package com.howbuy.lib.entity;
/**
 * @author rexy  840094530@qq.com 
 * @date 2013-12-9 下午11:22:43 
 * @param <T>
 */
public class AbsResult<T extends AbsLoadItem> {
	public T mData;
	public boolean mSuccess=false;
	public String mMsg;
	public void setMessage(String msg,boolean success){
		this.mMsg=msg;
		this.mSuccess=success;
	}
	public void setData(T data){
		this.mData=data;
	     mSuccess=mData!=null;
	}
}
