package com.howbuy.lib.entity;

/**
 * @author rexy  840094530@qq.com 
 * @date 2013-12-9 下午11:22:01
 */
public abstract class AbsLoadItem{
	protected String mLoadKey=null;
	protected boolean isNeedReload;
    protected long mLoadTime=0l;
    
	public String getLoadKey() {
		return mLoadKey;
	}
	public void setLoadKey(String mLoadKey) {
		this.mLoadKey = mLoadKey;
	}
	public boolean isNeedReload() {
		return isNeedReload;
	}
	public void setNeedReload(boolean isNeedReload) {
		this.isNeedReload = isNeedReload;
	}
	public long getLoadTime() {
		return mLoadTime;
	}
	public void setLoadTime(long time){
		this.mLoadTime=time;
	}
}
