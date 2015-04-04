package com.howbuy.lib.compont;

import android.os.Handler;
import android.os.Message;
/**
 * @author rexy  840094530@qq.com 
 * @date 2013-12-15 下午10:07:46
 */
public class SmartExitTwince {
	private OnExitTwiceListener mListener=null;
	private byte mState = 0;
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				if (mState == 1) {
					mState = 2;
					if(mListener!=null){
						if(mListener.onSecondPressDelayed()){
							mState=0;
						}
					}
				}
			}  
		}
	};

	public void resetTrace() {
		mState = 0;
		mHandler.removeMessages(0);
	}

	public boolean startTraceExit(OnExitTwiceListener listener) {
		this.mListener=listener;
		boolean result = (mState != 0);
		if (mState == 0) {
			mState = 1;
			mHandler.sendEmptyMessageDelayed(0, 250);
		}
		return result;
	}

	public boolean isPressTwinceExit() {
		if (mState == 1) {
			mState = 0;
			return true;
		} else{
			mState=0;
			return false;
		}
	}

  public interface OnExitTwiceListener{
	 boolean onSecondPressDelayed();
  }
}
