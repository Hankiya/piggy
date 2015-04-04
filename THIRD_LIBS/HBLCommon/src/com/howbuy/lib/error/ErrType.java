package com.howbuy.lib.error;

/**
 * @author rexy  840094530@qq.com 
 * @date 2014-1-15 下午5:52:37
 */
public final class ErrType {
	public int mErrCode = 0;
	public String mErrDes = null;

	public ErrType(int mErrCode, String mErrDes) {
		this.mErrCode = mErrCode;
		this.mErrDes = mErrDes;
	}
	@Override
	public String toString() {
		return "ErrChild [mErrCode=" + mErrCode + ", mErrDes=" + mErrDes + "]";
	}
}
