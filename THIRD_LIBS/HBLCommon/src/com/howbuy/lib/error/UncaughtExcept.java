package com.howbuy.lib.error;

import com.howbuy.lib.aty.AbsAty;
/**
 * @author rexy 840094530@qq.com
 * @date 2013-12-17 上午9:51:02
 */
public class UncaughtExcept implements Thread.UncaughtExceptionHandler {
 
    private CrashHandler mCrashhandler=null;
	public UncaughtExcept(CrashHandler crashHandler) {
		mCrashhandler=crashHandler; 
	}

	@Override
	public void uncaughtException(Thread thread, Throwable throwable) {
		Exception e=throwable instanceof Exception? (Exception)throwable:new Exception(throwable);
		WrapException wrapExp=new WrapException(e, "未捕获的异常");
		mCrashhandler.handCrash(thread, wrapExp, AbsAty.getAtys().size()>0?AbsAty.getAtys().get(0):null);
	}
	public void setCrashHnadler(CrashHandler handler){
		if(handler!=null){
			mCrashhandler=handler;
		}
	}
}
