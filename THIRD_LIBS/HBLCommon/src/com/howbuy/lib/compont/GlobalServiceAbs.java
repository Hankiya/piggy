package com.howbuy.lib.compont;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import com.howbuy.lib.compont.GlobalServiceMger.ScheduleTask;
import com.howbuy.lib.compont.GlobalServiceMger.ServiceTask;
import com.howbuy.lib.interfaces.ITimerListener;
import com.howbuy.lib.net.AsyPoolTask;
import com.howbuy.lib.net.ReqOpt;
import com.howbuy.lib.utils.LogUtils;

public abstract class GlobalServiceAbs extends Service implements Handler.Callback {
	// start-------------------------------------- timer task.
	public static String TAG = null;
	private ArrayList<ScheduleTask> mTask = new ArrayList<ScheduleTask>();
	private Timer mTime = null;

	private void toggleTimer(boolean launchTimer, int delay, int period) {
		if (launchTimer) {
			if (mTime == null) {
				GlobalApp.getApp().addFlag(GlobalApp.GLOBAL_SERVICE_TIMER);
				d("toggleTimer", "timer is starting running");
				mTime = new Timer();
				mTime.scheduleAtFixedRate(new TimerTask() {
					private long mLaunchTime = System.currentTimeMillis();

					@Override
					public void run() {
						long cur = System.currentTimeMillis();
						onTimerSchedule(cur - mLaunchTime, cur);
						queryTimeTask(cur);
					}
				}, delay, period);
			}
		} else {
			if (mTime != null) {
				mTime.cancel();
				mTime.purge();
				mTime = null;
				GlobalApp.getApp().subFlag(GlobalApp.GLOBAL_SERVICE_TIMER);
				d("toggleTimer", "timer is starting stoping");
			}
		}
	}

	protected void onTimerSchedule(long past, long cur) {
	}

	final private int findSchedule(ScheduleTask task) {
		int r = -1, n = mTask.size();
		for (int i = 0; i < n; i++) {
			if (mTask.get(i).equals(task)) {
				r = i;
				break;
			}
		}
		return r;
	}

	final private void addSchedule(ScheduleTask task) {
		synchronized (mTask) {
			int exist = findSchedule(task);
			if (exist != -1) {
				ScheduleTask t = mTask.get(exist);
				t.mExecutePeriod = task.mExecutePeriod;
				t.mTask = task.mTask;
				return;
			}
			long cur = System.currentTimeMillis() + 10;
			if (task.mExecuteRightNow) {
				executeSchedule(task, cur);
			} else {
				task.mLastExecuteTime = cur;
			}
			mTask.add(task);
			mActivitySender.sendMessage(Message.obtain(null,
					GlobalServiceMger.SERVICE_RETURN_TIMER, ITimerListener.TIMER_START,
					mTask.size(), task));
		}
	}

	final private void removeSchedule(ScheduleTask task) {
		synchronized (mTask) {
			int i = findSchedule(task);
			if (i != -1) {
				task.destory();
				task = mTask.remove(i);
				task.destory();
				mActivitySender.sendMessage(Message.obtain(null,
						GlobalServiceMger.SERVICE_RETURN_TIMER, ITimerListener.TIMER_END,
						mTask.size(), task));
			}
		}
	}

	final private void queryTimeTask(long cur) {
		synchronized (mTask) {
			int n = mTask.size() - 1;
			for (int i = n; i >= 0; i--) {
				ScheduleTask task = mTask.get(i);
				int check = task.checkSchedule(cur);
				if (check == 1) {
					executeSchedule(task, cur);
				} else {
					if (check == -1) {
						mTask.remove(i);
					}
				}
			}
		}
	}

	final private void executeSchedule(ScheduleTask task, long cur) {
		mActivitySender.sendMessage(Message.obtain(null, GlobalServiceMger.SERVICE_RETURN_TIMER,
				ITimerListener.TIMER_SCHEDULE, ++task.mExecuteTimes, task));
		task.mLastExecuteTime = cur;
		if (task.mTask != null) {
			AsyPoolTask.execute(true, new TaskRunner(task.mTask, true));
		}
	}

	// end-------------------------------------- timer task.
	/**
	 * a sender to send message to main ui.
	 */
	private Handler mActivitySender = null;

	/**
	 * when binded return a IBinder associated with the service.
	 */
	@Override
	final public IBinder onBind(Intent intent) {
		GlobalApp.getApp().addFlag(GlobalApp.GLOBAL_SERVICE_SERVICE);
		d("onBind", "service is binded");
		onServiceBind(intent, true);
		return new Messenger(new Handler(this)).getBinder();
	}

	@Override
	final public boolean onUnbind(Intent intent) {
		toggleTimer(false, 0, 0);
		mActivitySender = null;
		GlobalApp.getApp().subFlag(GlobalApp.GLOBAL_SERVICE_SERVICE);
		d("onBind", "service is unbinded");
		onServiceBind(intent, false);
		return super.onUnbind(intent);
	}

	protected void onServiceBind(Intent t, boolean bind) {

	}

	/**
	 * handle message from main ui .
	 */
	@Override
	final public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case GlobalServiceMger.SERVICE_BIND:
			if (msg.obj instanceof Handler) {
				mActivitySender = (Handler) msg.obj;
				d("handleMessage", "service is ready to running");
			}
			break;
		case GlobalServiceMger.SERVICE_START_TASK:
			AsyPoolTask.execute(true, new TaskRunner((ServiceTask) msg.obj, false));
			break;
		case GlobalServiceMger.SERVICE_STOP_TASK:
			cancleTask((ReqOpt) msg.obj);
			break;

		case GlobalServiceMger.SERVICE_START_TIMER:
			toggleTimer(true, msg.arg1, msg.arg2);
			break;
		case GlobalServiceMger.SERVICE_STOP_TIMER:
			toggleTimer(false, msg.arg1, msg.arg2);
			break;
		case GlobalServiceMger.SERVICE_REGISTER_TIMER:
			addSchedule((ScheduleTask) msg.obj);
			break;
		case GlobalServiceMger.SERVICE_UNREGISTER_TIMER:
			removeSchedule((ScheduleTask) msg.obj);
			break;
		}
		msg.obj = null;
		return true;
	}

	/**
	 * when service execute a task completed call the main ui..
	 */
	final protected boolean notifyFinished(final ServiceTask task) {
		if (mActivitySender != null) {
			Message msg = Message.obtain(null, GlobalServiceMger.SERVICE_RETURN_TASK, task);
			mActivitySender.sendMessage(msg);
			return true;
		}
		return false;
	}

	/**
	 * to handle any service and execute it .after completed execute should call
	 * notifyFinished(task) immediatelly.
	 * 
	 * @param task
	 *            should not change all call IReqFinished in task.
	 * @throws
	 */
	protected abstract void executeTask(final ServiceTask task, boolean fromTimer);

	/**
	 * if we execute with a thread ,should keep a reference with the thread and
	 * ServiceTask . cancel any task whose ServiceTask has a ReqOpt with opt.
	 * 
	 * @param opt
	 */
	protected void cancleTask(ReqOpt opt) {
	};

	final protected void d(String title, String msg) {
		if (TAG == null) {
			TAG = getClass().getSimpleName();
		}
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

	class TaskRunner implements Runnable {
		ServiceTask task = null;
		boolean fromTimer = false;
		public TaskRunner(ServiceTask t, boolean fromTimer) {
			this.task = t;
			this.fromTimer = fromTimer;
		}
		@Override
		public void run() {
			executeTask(task, fromTimer);
		}
	}
}
