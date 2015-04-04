package com.howbuy.component;

import howbuy.android.palmfund.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

import com.howbuy.commonlib.ParCheckAppUpdate;
import com.howbuy.config.Analytics;
import com.howbuy.config.FundConfig;
import com.howbuy.config.ValConfig;
import com.howbuy.datalib.fund.ParFundsNetValueByIDs;
import com.howbuy.db.DbOperat;
import com.howbuy.entity.InitUpdateInfs;
import com.howbuy.entity.UserInf;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.compont.GlobalServiceAbs;
import com.howbuy.lib.compont.GlobalServiceMger.ServiceTask;
import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.receiver.UpdateReceiver;
import com.howbuy.utils.Receiver;
import com.howbuy.utils.SyncOptUtil;
import com.howbuy.wireless.entity.protobuf.FundInfosListProto.FundInfosList;
import com.howbuy.wireless.entity.protobuf.HostDistributionProtos.HostDistribution;

public class AppService extends GlobalServiceAbs {
	// 执行记录
	public static String KEY_SERVICE_EXECUTE_TASK = "com.howbuy.fund.service_task";
	public static int EXECUTE_STATE_NULL = -1;// 没有加入执行队列。
	public static int EXECUTE_STATE_QUEUE = 0;// 加入到执行队列，等待Service启动执行。
	public static int EXECUTE_STATE_RUNNING = 1;// 正在执行。
	public static int EXECUTE_STATE_SUCCESS = 2;// 执行结束成功了。
	public static int EXECUTE_STATE_FAILED = 4;// 执行结束失败了。

	private final static HashMap<String, Integer> obtainTaskStates() {
		Object obj = GlobalApp.getApp().getMapObj(KEY_SERVICE_EXECUTE_TASK);
		if (obj == null) {
			HashMap<String, Integer> r = new HashMap<String, Integer>();
			GlobalApp.getApp().getMapObj().put(KEY_SERVICE_EXECUTE_TASK, r);
			return r;
		}
		return (HashMap<String, Integer>) obj;
	}

	/**
	 * 当调用执行时，返回一个int结果，从这里传入。
	 * 
	 * @param handType
	 * @param result
	 */
	public final static void updateExecuteState(int handType, int result) {
		String stateKey = KEY_SERVICE_EXECUTE_TASK + "_" + handType;
		HashMap<String, Integer> r = obtainTaskStates();
		r.put(stateKey, result);
		LogUtils.d(TAG == null ? "AppService" : TAG, "updateExecuteState -->" + "handType="
				+ handType + " , result=" + result);
	}

	public final static int queryExecuteState(int handType) {
		String stateKey = KEY_SERVICE_EXECUTE_TASK + "_" + handType;
		int state = EXECUTE_STATE_NULL;
		Integer val = obtainTaskStates().get(stateKey);
		if (val != null) {
			state = val;
		}
		return state;
	}

	public static final int HAND_APP_START = 1;
	public static final int HAND_SYNC_OPTIONAL = 2;
	public static final int HAND_UPDATE_APP = 3;
	public static final int HAND_UPDATE_NETVALUE_BATCH = 4;
	public static final int HAND_UPDATE_NETVALUE_IDS = 5;

	@Override
	protected void onServiceBind(Intent t, boolean bind) {
		if (bind) {
			new Thread() {
				public void run() {
					ReqOpt opt = new ReqOpt(0, "init launch", 0);
					long s = System.currentTimeMillis(), e = 0;
					opt.setTimeStartExecute(s);
					// 加载排行配置.
					new FundConfig(ValConfig.ASSETS_FUNDTYPE);
					e = System.currentTimeMillis();
					d("handTaskLaunchInit", "load fund and sort type configs from xml cost : "
							+ (e - s) + " ms");
					s = e;
					// 读取调试配置.
					SharedPreferences sf = GlobalApp.getApp().getSharedPreferences(
							ValConfig.SECRET_SF_NAME, 0);
					Resources r = getResources();
					LogUtils.mDebugUrl = sf.getBoolean(ValConfig.SECRET_DEBUG_URL,
							r.getBoolean(R.bool.SECRET_DEBUG_URL));
					LogUtils.mDebugPop = sf.getBoolean(ValConfig.SECRET_DEBUG_POP,
							r.getBoolean(R.bool.SECRET_DEBUG_POP));
					LogUtils.mDebugLog = sf.getBoolean(ValConfig.SECRET_DEBUG_LOG,
							r.getBoolean(R.bool.SECRET_DEBUG_LOG));
					LogUtils.mDebugLogFile = sf.getBoolean(ValConfig.SECRET_DEBUG_LOG_FILE,
							r.getBoolean(R.bool.SECRET_DEBUG_LOG_FILE));
					LogUtils.mDebugCrashMutiLogFile = sf.getBoolean(
							ValConfig.SECRET_DEBUG_CRASH_MUTIFILE,
							r.getBoolean(R.bool.SECRET_DEBUG_CRASH_MUTIFILE));
					LogUtils.mDebugCrashDialog = sf.getBoolean(ValConfig.SECRET_DEBUG_CRASH_DIALOG,
							r.getBoolean(R.bool.SECRET_DEBUG_CRASH_DIALOG));
					LogUtils.mDebugCrashLaunch = sf.getBoolean(ValConfig.SECRET_DEBUG_CRASH_LAUNCH,
							r.getBoolean(R.bool.SECRET_DEBUG_CRASH_LAUNCH));
					d("handTaskLaunchInit", LogUtils.getDebugState());
					e = System.currentTimeMillis();
					d("handTaskLaunchInit", "read debug config from sf cost : " + (e - s) + " ms");
					s = e;
					try {
						ArrayList<UserInf> users = UserInf.load(false);
						if (users != null && users.size() > 0) {
							UserInf.getUser().loginIn(users.get(0));
							Analytics.onEvent(getApplicationContext(),
									Analytics.ACTIVE_APP_BY_LOGINED_USER);
						}
					} catch (WrapException e1) {
						e1.printStackTrace();
					}
					d("handTaskLaunchInit", "load login user infs cost : " + (e - s) + " ms"
							+ " user=" + UserInf.getUser());
					s = e;
					// ......
					opt.setTimeComplete(e);
					d("handTaskLaunchInit", "total execute cost : " + opt.getTimeExecute() + " ms");

					if (LogUtils.mDebugUrl) {
						JPushInterface.setAlias(AppService.this, ValConfig.JPushAlias,
								new cn.jpush.android.api.TagAliasCallback() {

									@Override
									public void gotResult(int arg0, String arg1, Set<String> arg2) {
										// TODO Auto-generated method stub
										System.out.println("jpush alias......");
									}
								});
						JPushInterface.setDebugMode(true);
					}
				}
			}.start();
		}
	}

	@Override
	protected void executeTask(ServiceTask task, boolean fromTimer) {
		if (task != null) {
			final ReqOpt opt = task.getReqOpt();
			final int handType = opt.getHandleType();
			if (task.isAutoCreated()) {// 用户直接调用 ，非timer里调用的。
				switch (handType) {
				case HAND_APP_START:
					handAppStart(opt, task);
					break;
				case HAND_SYNC_OPTIONAL:
					handSyncOptional(opt, task);
					break;
				case HAND_UPDATE_APP:
					handUpdateApp(opt, task);
					break;
				case HAND_UPDATE_NETVALUE_BATCH:
					handUpdateNetValueBatch(opt, task);
					break;
				case HAND_UPDATE_NETVALUE_IDS:
					handUpdateNetValueIDs(opt, task);
					break;
				}
			} else {// timer里带有的task里调用的。
			}
		}
	}

	// 获取启动的版本更新信息，并更新数据库基金信息。
	private void handAppStart(final ReqOpt opt, final ServiceTask task) {
		final InitUpdateInfs updateInf = new InitUpdateInfs(AppFrame.getApp().getsF(), 0);
		long s = System.currentTimeMillis();
		opt.setTimeStartExecute(s);
		ReqResult<ReqNetOpt> res = updateInf.getStartRequesterResult(TAG, 0);
		if(res.isSuccess()) {
			HostDistribution r = (HostDistribution) res.mData;
			opt.setTimeComplete(System.currentTimeMillis());
			d("handAppStart", "load HostDistribution from server cost : " + opt.getTimeExecute()
					+ " ms" + r.getCommon().getResponseContent());
			updateExecuteState(HAND_APP_START, EXECUTE_STATE_RUNNING);
			updateInf.updateItself(opt, r, "handAppStart");
			String updateVer = r == null ? null : r.getVersionNeedUpdate();
			if (!StrUtils.isEmpty(updateVer) && !"2".equals(updateVer)) {
				byte[] updates = HostDistribution.newBuilder()
						.setVersionNeedUpdate(r.getVersionNeedUpdate())
						.setUpdateUrl(r.getUpdateUrl()).setUpdateDesc(r.getUpdateDesc()).build()
						.toByteArray();
				final Intent t = new Intent(UpdateReceiver.BROADCAST_UPDATE_APP);
				t.putExtra(ValConfig.IT_ENTITY, updates);
				AppFrame.getApp().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						AppFrame.getApp().sendBroadcast(t);
						d("handAppStart", "need update app send broadcast.");
					}
				}, 3000);
			}
		} else {
			d("handAppStart", "load HostDistribution from server err : " + res.mErr);
			updateExecuteState(HAND_APP_START, EXECUTE_STATE_FAILED);
		}
	}

	private void handUpdateApp(final ReqOpt opt, final ServiceTask task) {
		ReqResult<ReqNetOpt> r = new ParCheckAppUpdate(null, 0, null, null).execute();
		if (r.isSuccess()) {
			HostDistribution startData = (HostDistribution) r.mData;
			if (!"2".equals(startData.getVersionNeedUpdate())) {
				Intent t = new Intent(UpdateReceiver.BROADCAST_UPDATE_APP);
				t.putExtra(ValConfig.IT_ENTITY, startData.toByteArray());
				AppFrame.getApp().sendBroadcast(t);
				d("handUpdateApp", "need update send broadcast." + task);
			}
		}
	}

	// 外面调用 GlobalApp.getApp().getGlobalServiceMger() .executeTask(new
	// ReqOpt("your key arg", AppService.HAND_SERVICE_SYNOPTIONAL), null);
	private void handSyncOptional(final ReqOpt opt, final ServiceTask task) {
		opt.setTimeStartExecute(System.currentTimeMillis());
		if (UserInf.getUser().isLogined()) {
			try {
				SyncOptUtil.syncOpt(UserInf.getUser().getCustNo());
			} catch (WrapException e) {
				e.printStackTrace();
			}
		}
		opt.setTimeComplete(System.currentTimeMillis());
		d("handSyncOptional", "sysn total execute cost : " + opt.getTimeExecute() + " ms");
	}

	private void handUpdateNetValueBatch(final ReqOpt opt, final ServiceTask task) {
		int allUpdate = InitUpdateInfs.UPDATE_KFS | InitUpdateInfs.UPDATE_SIM
				| InitUpdateInfs.UPDATE_HBS | InitUpdateInfs.UPDATE_FBS;
		int updateFlag = opt.getArgInt();
		if ((updateFlag & updateFlag) == 0) {
			updateFlag = allUpdate;
		}
		new InitUpdateInfs(AppFrame.getApp().getsF(), updateFlag).updateItself(opt,
				"UPDATE_NETVALUE");
	}

	private void handUpdateNetValueIDs(final ReqOpt opt, final ServiceTask task) {
		Object o = opt.getArgObj();
		if (o != null) {
			ReqResult<ReqNetOpt> r = new ParFundsNetValueByIDs(0).setParams(o.toString()).execute();
			if (r.isSuccess()) {
				FundInfosList fl = (FundInfosList) r.mData;
				if (null == DbOperat.getInstance().updateNetValue(fl)) {
					Bundle b = new Bundle();
					b.putInt(
							ValConfig.IT_ID,
							fl.getOpensCount() + fl.getClosesNewCount() + fl.getMoneysCount()
									+ fl.getSimusCount());
					b.putString(ValConfig.IT_TYPE, opt.getKey());
					AppFrame.getApp().getLocalBroadcast()
							.sendBroadcast(Receiver.FROM_UPDATE_NETVAL_IDS, b);
				}
			}
		}
	}

}
