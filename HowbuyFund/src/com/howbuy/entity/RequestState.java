package com.howbuy.entity;


public class RequestState {
	public static final int FLAG_FAILD = 0;
	public static final int FLAG_WAIT = 1;
	public static final int FLAG_SUCCESS = 2;
	public static final int FLAG_RESULT_CACHE = 3;
	//以上改为标志位。//TODO
	public int mRequestFlag = FLAG_FAILD;
	public long mReturnTime = 0;//最近一次请求时间。
	public long mRequestTime = 0;//最近一次请求返回时间。
	public long mLastNetReturnTime=0;//最近一次网络请求返回时间。
	public int mMinRequestDuration = 0;//最小请求的时间间隔。
    public int mTimeOutDuration=60000;//超时时间，超过这个时间没有返回认为失败。
    
    
	public RequestState(int duration) {
		mMinRequestDuration = duration;
	}

	public void updateRequest(int flag) {
		mRequestFlag = flag;
		if (mRequestFlag == FLAG_WAIT) {
			mRequestTime = System.currentTimeMillis();
		}
	}

	public void setResult(boolean success) {
		mRequestFlag = success ? FLAG_SUCCESS : FLAG_FAILD;
		mReturnTime = System.currentTimeMillis();
	}

	public void setMinRequestDuration(int time) {
		mMinRequestDuration = time;
	}
	
	public void setTimeOutDuration(int time) {
		mTimeOutDuration = time;
	}
	
	public boolean needRequest(boolean update) {
		boolean need = mRequestFlag == FLAG_FAILD;
		if (!need) {
			long cur=System.currentTimeMillis() ;
			if (mRequestFlag == FLAG_SUCCESS) {
				need = mMinRequestDuration <= 0;
				if (!need) {
					need = cur- mReturnTime > mMinRequestDuration;
				}
			}else if(mRequestFlag==FLAG_WAIT){
				need=cur-mRequestTime>mTimeOutDuration;
			}
		}
		if (need) {
			updateRequest(FLAG_WAIT);
		}
		return need;
	}

	public boolean isRequestWaiting() {
		return mRequestFlag == FLAG_WAIT;
	}

	@Override
	public String toString() {
		return "[mRequestFlag=" + mRequestFlag + ", mReturnTime=" + mReturnTime
				+ ", mMinRequestDuration=" + mMinRequestDuration + "]";
	}

}
