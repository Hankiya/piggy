package com.howbuy.lib.net;

import java.io.Serializable;

import com.howbuy.lib.error.WrapException;

/**
 * @author rexy 840094530@qq.com
 * @date 2013-12-15 下午3:03:09
 */
public final class ReqResult<T extends ReqOpt> implements Serializable {
	private static final long serialVersionUID = 1L;
	protected boolean mStop = false;
	protected boolean mSuccess = false;
	public Object mData = null;
	public T mReqOpt = null;
	public WrapException mErr = null;

	protected void cancle() {
		mStop = true;
		setErr(new WrapException("stop request", null));
	}

	public boolean isStoped() {
		return mStop;
	}

	public boolean isSuccess() {
		return mSuccess;
	}

	public boolean isResultFromCache() {
		if (mReqOpt != null) {
			return mReqOpt.hasFlag(ReqOpt.FLAG_FROM_CACHE);
		}
		return false;
	}

	public ReqResult(T reqOpt) {
		this.mReqOpt = reqOpt;
	}

	/*
	 * this will set success to true whether the data is null or not.
	 */
	public void setData(Object data) {
		mData = data;
		mSuccess = true;
	}

	/*
	 * this will set success to false whether the data is null or not.
	 */
	public void setErr(WrapException err) {
		mErr = err;
		mSuccess = false;
	}

	public void destory() {
		mReqOpt = null;
		mData = null;
		mErr = null;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		return dumpState(sb, true, false).toString();
	}

	public String toShortString(boolean onlyResult, boolean exceptResultData) {
		StringBuffer sb = new StringBuffer();
		if (onlyResult) {
			if (mSuccess) {
				return String.valueOf(mData);
			} else {
				String.valueOf(mErr);
			}
		}
		return dumpState(sb, false, exceptResultData).toString();
	}

	protected StringBuffer dumpState(StringBuffer sb, boolean allInf, boolean exceptResultData) {
		sb.append("ReqResult[");
		if (allInf) {
			sb.append("mSuccess=").append(mSuccess);
			if (!exceptResultData) {
				sb.append(",mData=").append(mData);
			} else {
				sb.append(",mData=...");
			}
			sb.append(",mErr=").append(mErr);
			sb.append(",mReqOpt=").append(mReqOpt.toString());
		} else {
			if (mSuccess) {
				if (!exceptResultData) {
					sb.append("mData=").append(mData);
				} else {
					sb.append("mData=...");
				}
			} else {
				sb.append("mErr=").append(mErr);
			}
			if (mReqOpt != null) {
				sb.append(",mReqOpt=").append(mReqOpt.toShortString());
			}
		}
		return sb.append("]");
	}
}
