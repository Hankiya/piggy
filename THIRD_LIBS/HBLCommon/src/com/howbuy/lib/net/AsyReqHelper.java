package com.howbuy.lib.net;

import java.io.InputStream;
import java.util.ArrayList;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.utils.LogUtils;

/**
 * @author rexy 840094530@qq.com
 * @date 2013-12-15 下午3:02:41
 */
public class AsyReqHelper implements Handler.Callback {
	protected ArrayList<LoadThread> mReque = null;
	protected Handler mHandler = null;
	private static AsyReqHelper mReqHelper = null;

	protected AsyReqHelper() {
		mReque = new ArrayList<LoadThread>();
		mHandler = new Handler(Looper.getMainLooper(), this);
	}

	public static AsyReqHelper getInstance() {
		if (mReqHelper == null) {
			synchronized (AsyReqHelper.class) {
				if (mReqHelper == null) {
					mReqHelper = new AsyReqHelper();
				}
			}
		}
		return mReqHelper;
	}

	public static void setTimeOut(int connect, int read) {
		HttpClient.setTimeOut(connect, read);
	}

	public boolean cancleRequest(ReqNetOpt opt) {
		synchronized (mReque) {
			int n = mReque.size();
			for (int i = 0; i < n; i++) {
				if (mReque.get(0).mResult.mReqOpt.equals(opt)) {
					mReque.remove(0).interrupt();
					return true;
				}

			}
			return false;
		}
	}

	public int cancleRequest() {
		synchronized (mReque) {
			int n = mReque.size();
			for (int i = 0; i < n; i++) {
				mReque.remove(0).interrupt();
			}
			return n;
		}

	}

	public LoadThread doRequest(ReqNetOpt opt, IReqNetFinished calbk, Handler uiHandler) {
		LoadThread thread = new LoadThread(opt, calbk, uiHandler);
		addTask(thread, true);
		return thread;
	}

	private boolean addTask(LoadThread thrd, boolean run) {
		boolean success = false;
		synchronized (mReque) {
			success = mReque.add(thrd);
		}
		if (run && success) {
			thrd.start();
		}
		return success;
	}

	public static InputStream getInputStream(ReqNetOpt opt) throws Exception {
		InputStream is = null, newIs = null;
		String urlPath = opt.getUrlPath();
		boolean enableCache = opt.hasFlag(ReqNetOpt.FLAG_ENABLE_CACHE);
		if (enableCache) {
			is = opt.getCacheStream(urlPath, null);
			if (is != null) {
				opt.addFlag(ReqNetOpt.FLAG_FROM_CACHE);
				return is;
			} else {
				enableCache = false;
			}
		}

		if (!enableCache) {
			if (opt.hasFlag(ReqNetOpt.FLAG_REQ_GET)) {
				newIs = HttpClient.getInstance().getRequest(urlPath);
			} else {
				if (opt.mArgByte != null) {
					newIs = HttpClient.getInstance().postRequest(opt.mUrl, opt.getPostArg(),
							opt.mArgByte);
				} else {
					newIs = HttpClient.getInstance().postRequest(opt.mUrl, opt.getPostArg());
				}
			}
			if (newIs != null && opt.hasFlag(ReqNetOpt.FLAG_ENABLE_CACHE)) {
				return opt.getCacheStream(urlPath, newIs);
			}
			return newIs;
		}
		return null;
	}

	protected class LoadThread extends Thread {
		// protected ReqNetOpt mOpt = null;
		protected ReqResult<ReqNetOpt> mResult = null;
		protected IReqNetFinished mCalBk = null;
		protected Handler mUiHander = null;

		public LoadThread(ReqNetOpt opt, IReqNetFinished calbk, Handler uiHandler) {
			this.mCalBk = calbk;
			this.mUiHander = uiHandler;
			this.mResult = new ReqResult<ReqNetOpt>(opt);
		}

		@Override
		public void interrupt() {
			mResult.cancle();
			super.interrupt();
		}

		protected void callBack() {
			if (LogUtils.mDebugLog) {
				LogUtils.d("AsyReq", "mResult=" + mResult.toShortString(false, true) + "\n\t");
			}
			if (mCalBk != null) {
				mCalBk.onRepNetFinished(mResult); // notify for observer
			}
			if (mUiHander != null) { // notify for ui thread.
				Message msg = new Message();
				msg.what = IReqNetFinished.HAND_REQFINISHED; // for result from
																// webload.
				msg.arg1 = mResult.mReqOpt.mHandleType; // request web type ,so
														// we can know
				// from
				// handler which request returned.
				msg.arg2 = (mResult.mSuccess ? 1 : 0); // 0 for failed,1 for
														// success.
				msg.obj = mResult; // result from web respond.
				mUiHander.sendMessage(msg);
			}
			synchronized (mReque) {
				mReque.remove(this);
			}
		}

		protected Object doInbackgroud(ReqNetOpt opt) throws Exception, InterruptedException {
			opt.mTimeStartedExecute = System.currentTimeMillis();
			Object obj = opt.parse(getInputStream(opt));
			if (opt.hasFlag(ReqNetOpt.FLAG_CACHE_THEN_REQ)
					&& opt.hasFlag(ReqNetOpt.FLAG_FROM_CACHE)) {
				if (obj != null && !mResult.mStop) {
					ReqNetOpt req = new ReqNetOpt(opt);
					req.subFlag(ReqNetOpt.FLAG_CACHE_THEN_REQ);
					req.subFlag(ReqNetOpt.FLAG_FROM_CACHE);
					req.addFlag(ReqNetOpt.FLAG_FORCE_UNCACHE);
					asyReq(req, mCalBk, mUiHander);
				}
			}
			return obj;
		}

		@Override
		public void run() {
			ReqNetOpt opt = mResult.mReqOpt;
			try {
				mResult.mData = doInbackgroud(opt);
				opt.mTimeCompleted = System.currentTimeMillis();
				if (!mResult.mStop) {
					mResult.mSuccess = true;
				}  
			} catch (Exception e) {
				mResult.mSuccess = false;
				mResult.mErr = WrapException.wrap(null, e, "" + opt);
			} finally {
				Message msg = mHandler.obtainMessage(0, this);
				mHandler.sendMessage(msg);
			}
		}

		public void destory() {
			mResult.mReqOpt = null;
			mResult = null;
			mCalBk = null;
			mUiHander = null;
		}
	}

	public static Object request(ReqNetOpt opt) throws Exception {
		return opt.parse(getInputStream(opt));
	}

	public static boolean asyReq(ReqNetOpt opt, IReqNetFinished calbk, Handler uiHandler) {
		if (calbk != null || uiHandler != null) {
			AsyReqHelper.getInstance().doRequest(opt, calbk, uiHandler);
			return true;
		}
		return false;
	}

	@Override
	public boolean handleMessage(Message msg) {
		LoadThread trd = (LoadThread) msg.obj;
		trd.callBack();
		trd.destory();
		return true;
	}

	// ///////////////////////////////////////////////////////////////////////////////
}
