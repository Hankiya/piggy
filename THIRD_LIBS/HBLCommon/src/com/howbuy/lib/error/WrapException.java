package com.howbuy.lib.error;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import android.content.res.Resources;

import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.utils.StrUtils;

/**
 * WrapException类型的错误e. printStackTrace 不需要在程序中任何地方调用.
 * 
 * @author rexy 840094530@qq.com
 * @date 2014-1-15 下午5:53:39
 */
public class WrapException extends Exception {
	private static final long serialVersionUID = 500887776657990191L;
	private int mMsgCode = 0;// code from ErrType. second.
	private String mMsgDes = null; // msg from ErrType. //third.
	protected String mMsgDisplay = null; // msg to show user first
	private String mMsgDebug = null; // msg for debug last. the same with the
										// getMessage of throwable.
	private boolean mWraped = false;
	private Throwable mExpOrig = null;

	/**
	 * @param msg
	 *            显示给用户的msg.
	 */
	public void setMsgDisplay(String msg) {
		if (msg != null) {
			this.mMsgDisplay = msg;
		}
	}

	public String getMsgDebug() {
		return mMsgDebug + " " + getMessage();
	}

	/**
	 * @param extra
	 *            出错时的额外调式信息.
	 * @return void
	 */
	public void setMsgDebug(String extra) {
		if (extra != null) {
			this.mMsgDebug = extra;
		}
	}

	public int getMsgCode() {
		return mMsgCode;
	}

	public Throwable getOrigExcep() {
		return mExpOrig;
	}

	public static final Throwable getLastCause(Throwable e) {
		Throwable cause = e == null ? null : e.getCause();
		if (cause != null) {
			return getLastCause(cause);
		}
		return e;
	}

	public static String getErrorDetail(Throwable err,
			boolean onelyKeepLastCause) {
		Writer writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		err.printStackTrace(pw);
		pw.close();
		String error = writer.toString();
		if (onelyKeepLastCause) {
			StringBuffer sb = new StringBuffer(error);
			int first = error.indexOf("Caused by:");
			int last = error.lastIndexOf("Caused by:");
			if (first != last) {
				sb.delete(first, last);
			}
			error = sb.toString();
		}
		return error;
	}

	/**
	 * @param prefix
	 *            null for error_.
	 * @return String error message for user to scan.
	 * @throws
	 */
	public String getError(String prefix, boolean filterDebug) {
		if (!StrUtils.isEmpty(mMsgDisplay)) {
			return mMsgDisplay;
		}
		String name = (prefix == null ? "error_" : prefix) + mMsgCode;
		int i = GlobalApp
				.getApp()
				.getResources()
				.getIdentifier(name, "string",
						GlobalApp.getApp().getPackageName());
		try {
			return GlobalApp.getApp().getString(i);
		} catch (Resources.NotFoundException e) {
			if (!StrUtils.isEmpty(mMsgDes)) {
				return mMsgDes;
			}
			if (!filterDebug) {
				if (!StrUtils.isEmpty(mMsgDebug)) {
					return mMsgDebug + " : " + getMessage();
				}
				return getMessage();
			}
		}
		return "未知错误";
	}

	public WrapException(Throwable e, String msgDebug) {
		super(e.getMessage(), e);
		wrap(this, e, msgDebug);
	}

	public WrapException(String msg, String msgDebug) {
		super(msg);
		mMsgDebug = msgDebug==null?msg:msgDebug;
		mExpOrig = null; 
	}

	@Override
	public void printStackTrace() {
	}

	public static WrapException wrap(Throwable e, String msgDebug) {
		return wrap(null, e, msgDebug);
	}

	public static WrapException wrap(WrapException we, Throwable e,
			String msgDebug) {
		if (we == null) {
			if (e instanceof WrapException) {
				return (WrapException) e;
			}
			we = new WrapException(
					e == null ? new Throwable("Unknow exception")
							: getLastCause(e), msgDebug);
		} else if (e instanceof WrapException) {
			return copyWrapException(we, (WrapException) e);
		} else {
			e = getLastCause(e);
		}
		if (e != null) {
			we.mExpOrig = e;
			e.printStackTrace();
			ErrType ecode = GlobalApp.getApp().getErrType(e);
			if (ecode == null) {
				we.mMsgDes = null;
				we.mMsgCode = 0;
			} else {
				we.mMsgDes = ecode.mErrDes;
				we.mMsgCode = ecode.mErrCode;
			}
		}
		we.mMsgDebug = msgDebug;
		we.mWraped = true;
		return we;
	}

	private static WrapException copyWrapException(WrapException des,
			WrapException from) {
		if (des != null && from != null) {
			des.mMsgDisplay = from.mMsgDisplay;
			des.mMsgCode = from.mMsgCode;
			des.mMsgDes = from.mMsgDes;
			des.mMsgDebug = from.mMsgDebug;
			des.mWraped = from.mWraped;
			des.mExpOrig = from.mExpOrig;
		}
		return des;
	}

	@Override
	public String toString() {
		return "WrapException [mMsgCode=" + mMsgCode + ", mMsgDes=" + mMsgDes
				+ ", mMsgDisplay=" + mMsgDisplay + ", mMsgDebug=" + mMsgDebug
				+ ", mWraped=" + mWraped + ", mExpOrig=" + mExpOrig + "]";
	}

}
