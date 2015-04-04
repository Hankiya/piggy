package com.howbuy.lib.compont;

import java.util.LinkedList;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.interfaces.IReqFinished;
import com.howbuy.lib.interfaces.ITimerListener;
import com.howbuy.lib.net.ReqOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.LogUtils;

public final class GlobalServiceMger implements Handler.Callback {
	private final LinkedList<Message> mQueue = new LinkedList<Message>();
	protected static final int SERVICE_BIND = 1;// bind service at first.
	protected static final int SERVICE_START_TASK = 2;// add service task.
	protected static final int SERVICE_STOP_TASK = 3;// remove service task.
	protected static final int SERVICE_RETURN_TASK = 4;// when service task
	protected static final int SERVICE_START_TIMER = 5;// launch service timer .
	protected static final int SERVICE_STOP_TIMER = 6; // stop service timer .
	protected static final int SERVICE_REGISTER_TIMER = 7; // register timer
															// listener.
	protected static final int SERVICE_UNREGISTER_TIMER = 8;// remove timer
															// listener .
	protected static final int SERVICE_RETURN_TIMER = 9;// when timer listener
														// call back.

	protected String TAG = null;
	/**
	 * a sender send service task to the service to run .
	 */
	private Messenger mServiceSender;
	/**
	 * service classs extends GlobalService.
	 */
	private Class<? extends GlobalServiceAbs> mServiceClass;
	/**
	 * ServiceConnection to control bind service as a bridge.
	 */
	private final ServiceConnection mConnection = new ServiceConnection() {
		/*
		 * service is stop abnormal.
		 */
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mServiceSender = null;
		}

		/**
		 * //service is connected.bind success.
		 */
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// rebuild a Messnger from the service binder.
			mServiceSender = new Messenger(service);
			Message msg = Message.obtain(null, SERVICE_BIND, 0, 0, new Handler(
					GlobalServiceMger.this));
			// send the main handler to the service .so both can communication
			// each other.
			sendServiceMessage(msg);
			while ((msg = mQueue.poll()) != null) {
				sendServiceMessage(msg);
			}
		}
	};

	public GlobalServiceMger(Class<? extends GlobalServiceAbs> cls) {
		mServiceClass = cls;
		TAG = getClass().getSimpleName();
	}

	private int sendServiceMessage(Message msg) {
		try {
			if (mServiceSender != null) {
				mServiceSender.send(msg);
				return 1;
			} else {
				mQueue.offer(msg);
				return 0;
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * e
	 * 
	 * @param opt
	 * @param l
	 * @return int 1 for execute right now ,0 for waiting in queue, -1 for err.
	 */
	public final int executeTask(ReqOpt opt, IReqFinished l) {
		Message msg = Message.obtain(null, SERVICE_START_TASK, new ServiceTask(opt, l));
		return sendServiceMessage(msg);
	}

	/**
	 * @param opt
	 * @return int 1 for execute right now ,0 for waiting in queue, -1 for err.
	 */
	public int cancleTask(ReqOpt opt) {
		Message msg = Message.obtain(null, SERVICE_STOP_TASK, opt);
		return sendServiceMessage(msg);
	}

	public final void addTimerListener(ScheduleTask task) {
		sendServiceMessage(Message.obtain(null, SERVICE_REGISTER_TIMER, task));
	}

	public final void removeTimerListener(int handType, ITimerListener l) {
		ScheduleTask task = new ScheduleTask(handType, l);
		sendServiceMessage(Message.obtain(null, SERVICE_UNREGISTER_TIMER, task));
	}

	/**
	 * start or stop the service.
	 * 
	 * @param launchService
	 *            true for start the service.
	 */
	public final boolean toggleService(boolean launchService) {// launched service
		if (launchService) {
			return GlobalApp.getApp().bindService(new Intent(GlobalApp.getApp(), mServiceClass),
					this.mConnection, Context.BIND_AUTO_CREATE);
		} else {
			if (mServiceSender != null) {
				GlobalApp.getApp().unbindService(mConnection);
				mServiceSender = null;
				return true;
			}
		}
		return false;
	}

	public void toggleTimer(boolean launchTimer, int delay, int period) {
		int what = launchTimer ? SERVICE_START_TIMER : SERVICE_STOP_TIMER;
		Message msg = Message.obtain(null, what, delay, period);
		sendServiceMessage(msg);
	}

	/**
	 * when service execute complete we accept message from the service .
	 */
	@Override
	final public boolean handleMessage(Message msg) {
		if (msg.what == SERVICE_RETURN_TASK) {
			final ServiceTask task = (ServiceTask) msg.obj;
			if (!task.notifyFinished()) {
				handResult(task.mResult);
			}
			task.destory();
		} else if (msg.what == SERVICE_RETURN_TIMER) {
			((ScheduleTask) msg.obj).notifyFinished(msg.arg1, msg.arg2);
			msg.obj = null;
		}
		msg.obj = null;
		return true;
	}

	/**
	 * if the service task callback don't called ,we handle the result here.
	 * 
	 * @param r
	 * @throws
	 */
	protected void handResult(ReqResult<ReqOpt> r) {
	}

	public static class ServiceTask {

		protected ReqResult<ReqOpt> mResult = null;
		protected IReqFinished mListener = null;
		protected boolean mAutoDestory = true;
		protected boolean mAutoCreated = true;

		public String getKey() {
			return String.valueOf(mResult.mReqOpt.getKey()) + "_" + mResult.mReqOpt.getHandleType();
		}

		public boolean isAutoCreated() {
			return mAutoCreated;
		}

		public ServiceTask(ReqOpt opt, IReqFinished l) {
			mResult = new ReqResult<ReqOpt>(opt);
			mListener = l;
		}

		public ReqOpt getReqOpt() {
			return mResult.mReqOpt;
		}

		/*
		 * this will set success to true whether the data is null or not.
		 */
		public void setData(Object data) {
			mResult.setData(data);
		}

		/*
		 * this will set success to false whether the data is null or not.
		 */
		public void setErr(WrapException err) {
			mResult.setErr(err);
		}

		protected boolean notifyFinished() {
			if (mListener != null) {
				mListener.onRepFinished(mResult);
				return true;
			}
			return false;
		}

		protected void destory() {
			if (mResult != null) {
				mResult.mReqOpt.setTimeStartExecute(0);
				mResult.mReqOpt.setTimeComplete(0);
				mResult.setData(null);
				mResult.setErr(null);
			}
			if (mAutoDestory) {
				mResult = null;
				mListener = null;
			}
		}
	}

	public static class ScheduleTask {
		public ITimerListener mTimerListener = null;
		protected boolean mExecuteRightNow = false;// excute right now .
		protected int mExecutePeriod = 1000;
		protected long mLastExecuteTime = 0;
		private int mHandType = 0;
		protected int mExecuteTimes = 0;
		protected int mMaxExcuteTimes = 0;
		protected ServiceTask mTask = null;

		// -1 cycle life have came. 1 in execute period,0 not in execute period.
		protected int checkSchedule(long cur) {
			if (mMaxExcuteTimes == 0 || mExecuteTimes <= mMaxExcuteTimes) {
				if (cur - mLastExecuteTime >= mExecutePeriod) {
					return 1;
				}
				return 0;
			}
			return -1;
		}

		public ScheduleTask(int handType, ITimerListener l) {
			setCallback(handType, l);
		}

		public ScheduleTask setCallback(int handType, ITimerListener l) {
			mTimerListener = l;
			mHandType = handType;
			return this;
		}

		public ScheduleTask setExecuteArg(int period, int maxExecuteTimes, boolean executeNow) {
			mExecuteRightNow = executeNow;
			mExecutePeriod = period;
			mMaxExcuteTimes = maxExecuteTimes;
			return this;
		}

		public void setExecuteTask(ServiceTask task) {
			mTask = task;
			if (mTask != null) {
				mTask.mAutoDestory = false;
				mTask.mAutoCreated = false;
			}
		}

		protected boolean notifyFinished(int timerState, int timesOrSize) {
			if (mTimerListener != null) {
				mTimerListener.onTimerRun(mHandType, timerState, mTask != null, timesOrSize);
				return true;
			}
			return false;
		}

		public void destory() {
			mExecuteTimes = 0;
			mLastExecuteTime = 0;
			mTimerListener = null;
			mMaxExcuteTimes = 0;
			if (mTask != null) {
				mTask.mAutoDestory = true;
				if (mTask.mResult.mReqOpt.getTimeStartExecute() == 0) {
					mTask.destory();
				}
			}
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			} else if (o instanceof ScheduleTask) {
				ScheduleTask t = (ScheduleTask) o;
				if (t.mHandType == mHandType && t.mTimerListener == mTimerListener) {
					return true;
				}
			}
			return false;
		}

	}

	final protected void d(String title, String msg) {
		if (title == null) {
			LogUtils.d(TAG, msg);
		} else {
			LogUtils.d(TAG, title + " -->" + msg);
		}

	}

	final protected void pop(String msg, boolean debug) {
		if (debug) {
			LogUtils.popDebug(msg);
		} else {
			LogUtils.pop(msg);
		}
	}
}
