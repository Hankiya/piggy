package howbuy.android.piggy.service;

import java.util.LinkedList;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class ServiceMger {
	private Context mContext;

	public ServiceMger(Context context) {
		this.mContext = context;
		mTask = new LinkedList<TaskBean>();
	}

	private LinkedList<TaskBean> mTask;
	private UpdateUserDataService mService;
	private ServiceConnection sConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			mService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			mService = ((UpdateUserDataService.UpdateUserDataBind) service).getService();
			TaskBean task;
			while ((task = mTask.poll()) != null) {
				mService.luncherTaskN(mergeParams(task));
			}
		}
	};

	public void bindService() {
		Intent i = new Intent(mContext, UpdateUserDataService.class);
		mContext.bindService(i, sConnection, Context.BIND_AUTO_CREATE);
	}

	public void unBindService() {
		if (mService != null) {
			mContext.unbindService(sConnection);
		}
	}

	public void addTask(TaskBean task) {
		Log.d("service", "addTask++++taskName-=" + task.getTaskName() + "--reqId-=" + task.getReqId());
		if (mService != null) {
			mService.luncherTaskN(mergeParams(task));
		} else {
			mTask.add(task);
		}
	}

	public static class TaskBean {
		String taskName;
		String reqId;
		String reqParams[];

		public TaskBean(String taskName, String reqId) {
			super();
			this.reqId = reqId;
			this.taskName = taskName;
		}
		
		public TaskBean(String taskName, String reqId, String[] params) {
			super();
			this.reqId = reqId;
			this.taskName = taskName;
			this.reqParams = params;
		}

		public String getReqId() {
			return reqId;
		}

		public void setReqId(String reqId) {
			this.reqId = reqId;
		}

		public String getTaskName() {
			return taskName;
		}

		public void setTaskName(String taskName) {
			this.taskName = taskName;
		}

		public String[] getReqParams() {
			return reqParams;
		}

		public void setReqParams(String[] params) {
			this.reqParams = params;
		}
	}

	public static String[] mergeParams(TaskBean t) {
		String[] a = new String[] { t.getTaskName(), t.getReqId() };
		String[] b = t.getReqParams();
		if (b == null) {
			return a;
		} else {
			String[] c = new String[a.length + b.length];
			System.arraycopy(a, 0, c, 0, a.length);
			System.arraycopy(b, 0, c, a.length, b.length);
			return c;
		}
	}
}
