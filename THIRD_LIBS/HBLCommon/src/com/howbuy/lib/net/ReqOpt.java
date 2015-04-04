package com.howbuy.lib.net;

import com.howbuy.lib.utils.StrUtils;

/**
 * @author rexy 840094530@qq.com
 * @date 2013-12-15 下午3:03:01
 */
public class ReqOpt {
	public static final int FLAG_ENABLE_CACHE = 1;
	public static final int FLAG_FROM_CACHE = 2;// set to be protected
	public static final int FLAG_FORCE_UNCACHE = 4;
	public static final int FLAG_CACHE_THEN_REQ = 8;
	protected int mOFlg = 0;
	protected long mTimeStartedExecute;
	protected long mTimeCompleted;
	/**
	 * every request have a unique key so that when the it come back caller
	 * would know which it returned.
	 */
	protected String mKey = null;
	protected int mHandleType = 0;
	protected int mArgi = 0;
	protected int mReqId = 0;
	protected Object mArgo = null;

	public ReqOpt(ReqOpt opt) {
		mReqId=opt.mReqId;
		mKey=opt.mKey;
		mHandleType=opt.mHandleType;
		mOFlg = opt.mOFlg;
		mArgi=opt.mArgi;
		mArgo=opt.mArgo; 
		mTimeStartedExecute=opt.mTimeStartedExecute;
		mTimeCompleted=opt.mTimeCompleted;
	}

	public ReqOpt(int id, String key, int handType) {
		mReqId=id;
		mKey=key;
		mHandleType=handType;
	}
 
	final public int getFlag() {
		return mOFlg;
	}

	 protected void setFlag(int flag) {
		mOFlg = flag;
	}

	final public boolean hasFlag(int value, int flag) {
		return flag == 0 ? false : flag == (value & flag);
	}

	final public boolean hasFlag(int flag) {
		return flag == 0 ? false : flag == (mOFlg & flag);
	}

	public void addFlag(int flag) {
		mOFlg |= flag;
	}

	 public void subFlag(int flag) {
		if (flag != 0) {
			mOFlg &= ~flag;
		}
	}

	public String getKey() {
		return mKey;
	}

	public void setKeyType(String key, int handType) {
		this.mKey = key;
		this.mHandleType = handType;
	}

	public void setArgInt(int argi) {
		mArgi = argi;
	}

	public void setArgObj(Object argo) {
		mArgo = argo;
	}

	public int getArgInt() {
		return mArgi;
	}

	public Object getArgObj() {
		return mArgo;
	}

	public void setReqId(int id) {
		mReqId = id;
	}

	public int getReqId() {
		return mReqId;
	}

	public void setTimeComplete(long timeComplete) {
		mTimeCompleted = timeComplete;
	}

	public void setTimeStartExecute(long timeStartExecute) {
		mTimeStartedExecute = timeStartExecute;
	}

	public int getHandleType() {
		return mHandleType;
	}

	public long getTimeStartExecute() {
		return mTimeStartedExecute;
	}

	public long getTimeComplete() {
		return mTimeCompleted;
	}

	public long getTimeExecute() {
		return mTimeCompleted - mTimeStartedExecute;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		return dumpState(sb, true).toString();
	}

	public String toShortString() {
		StringBuffer sb = new StringBuffer();
		return dumpState(sb, false).toString();
	}

	protected StringBuffer dumpState(StringBuffer sb, boolean allInf) {
		sb.append("ReqOpt[");
		sb.append("mKey=").append(mKey);
		sb.append(",mHandleType=").append(mHandleType);
		sb.append(",mOFlg={");
		if (mOFlg != 0) {
			dumpFlag(sb);
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("}");
		sb.append(",mArgi=").append(mArgi);
		if (allInf) {
			sb.append(",mArgo=").append(mArgo);
		} else {
			if (mArgo != null) {
				sb.append(",mArgo=").append(mArgo);
			}
		}
		dumpTime(sb, allInf);
		return sb.append("]");
	}

	private StringBuffer dumpTime(StringBuffer sb, boolean allInf) {
		if (allInf) {
			if (mTimeStartedExecute != 0) {
				sb.append(",mTimeStartedExecute=").append(
						StrUtils.timeFormat(mTimeStartedExecute, "mm:ss.SSS"));
			} else {
				sb.append(",mTimeStartedExecute=").append(mTimeStartedExecute);
			}
			if (mTimeCompleted != 0) {
				sb.append(",mTimeCompleted=").append(
						StrUtils.timeFormat(mTimeCompleted, "mm:ss.SSS"));
			} else {
				sb.append(",mTimeCompleted=").append(mTimeCompleted);
			}
			if (mTimeStartedExecute != 0 && mTimeCompleted != 0) {
				sb.append(",execute cost:").append(mTimeCompleted - mTimeStartedExecute)
						.append("ms");
			}
		} else {
			if (mTimeStartedExecute != 0) {
				sb.append(",mTimeStartedExecute=").append(
						StrUtils.timeFormat(mTimeStartedExecute, "mm:ss.SSS"));
				if (mTimeCompleted != 0) {
					sb.append(",execute cost:").append(mTimeCompleted - mTimeStartedExecute)
							.append("ms");
				}
			}
		}
		return sb;
	}

	protected StringBuffer dumpFlag(StringBuffer sb) {
		if (hasFlag(FLAG_ENABLE_CACHE)) {
			sb.append("FLAG_ENABLE_CACHE,");
		}
		if (hasFlag(FLAG_FROM_CACHE)) {
			sb.append("FLAG_FROM_CACHE,");
		}
		if (hasFlag(FLAG_FORCE_UNCACHE)) {
			sb.append("FLAG_FORCE_UNCACHE,");
		}
		if (hasFlag(FLAG_CACHE_THEN_REQ)) {
			sb.append("FLAG_CACHE_THEN_REQ,");
		}
		return sb;
	}
}
