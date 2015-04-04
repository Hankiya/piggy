package com.howbuy.entity;

import java.util.HashMap;
import java.util.Map;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.howbuy.component.AppFrame;
import com.howbuy.component.AppService;
import com.howbuy.config.Analytics;
import com.howbuy.config.FundConfig;
import com.howbuy.config.InitConfig;
import com.howbuy.config.ValConfig;
import com.howbuy.control.HomeStockLayout;
import com.howbuy.datalib.fund.ParFundsNetValueBatch;
import com.howbuy.datalib.fund.ParStart;
import com.howbuy.datalib.fund.ParUpdateOpenFundBasicInfo;
import com.howbuy.datalib.fund.ParUpdateSimuFundBasicInfo;
import com.howbuy.db.DbOperat;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.utils.Receiver;
import com.howbuy.wireless.entity.protobuf.FundBasicInfoProto.FundBasicInfoList;
import com.howbuy.wireless.entity.protobuf.HostDistributionProtos.HostDistribution;
import com.umeng.analytics.MobclickAgent;

public class InitUpdateInfs {
	public static final int UPDATE_BASIC_FUND = 128;
	public static final int UPDATE_BASIC_SIMU = 256;
	public static final int UPDATE_SIM = 512;
	public static final int UPDATE_KFS = 1024;
	public static final int UPDATE_FBS = 2048;
	public static final int UPDATE_HBS = 4096;
	private int mUpdatedSuccess = 0;
	private int mUpdatedFailed = 0;
	private SharedPreferences mSf = null;
	private ReqOpt mOpt = null;
	private String mLogTitle = null;
	private String curDate = null;
	private long mCurLong = 0l;
	private String oldDate = InitConfig.TIME_YYMMDD;
	private String basicInfoVer = null;// 20110522 N 基金基本信息版本号
	private String kfsVer = null;// N 开放式基金净值版本号
	private String fbsVer = null;// N 封闭式基金净值版本号
	private String hbsVer = null;// N 货币式基金净值版本号
	private String smVer = null;// N 私募基金净值版本号
	private String managerVer = null;// 20110522 N 基金经理版本号
	private String companyVer = null;// 20110522 N 基金公司版本号
	private String newsTypeVer = null;// 1L N 资讯分类版本号
	private String opinionTypeVer = null;// 1L N 研报分类版本号
	private String jsVer = "1";
	private int mTaskRequest = 0;
	public static int mTaskFlag = 0;
	public static int mDoneFlag = 0;

	public static boolean hasTask() {
		return mTaskFlag != 0;
	}

	public int getDownTask() {
		return mDoneFlag;
	}

	public InitUpdateInfs(SharedPreferences sf, int taskFlag) {
		mSf = sf;
		mCurLong = System.currentTimeMillis();
		curDate = StrUtils.timeFormat(mCurLong, ValConfig.DATEF_YMD);
		this.basicInfoVer = sf.getString(ValConfig.SFVerBasicFundInfoVer, oldDate);
		this.kfsVer = sf.getString(ValConfig.SFVerKfsVer, String.valueOf(1));
		this.fbsVer = sf.getString(ValConfig.SFVerFbsVer, String.valueOf(1));
		this.hbsVer = sf.getString(ValConfig.SFVerHbsVer, String.valueOf(1));
		this.smVer = sf.getString(ValConfig.SFVerSimVer, String.valueOf(1));
		this.jsVer = sf.getString(ValConfig.SFVerJsVer, String.valueOf(1));
		this.managerVer = /* sf.getString(ValConfig.SFVerManagerVer, oldDate) */curDate;
		this.companyVer = /* sf.getString(ValConfig.SFVerCompanyVer, oldDate) */curDate;
		this.newsTypeVer = /* sf.getString(ValConfig.SFVerNewsTypeVer, oldDate) */String
				.valueOf(mCurLong);
		this.opinionTypeVer = /*
							 * * sf.getString(ValConfig.SFVerOpinionTypeVer,
							 * oldDate)
							 */String.valueOf(mCurLong);
		if (taskFlag == 0) {
			mTaskRequest = getAllFlag();
		} else {
			mTaskRequest = taskFlag & getAllFlag();
		}
	}

	final protected void d(String title, String msg) {
		if (title == null) {
			LogUtils.d("AppService", msg);
		} else {
			LogUtils.d("AppService", title + " -->" + msg);
		}
	}

	private int getAllFlag() {
		return UPDATE_BASIC_FUND | UPDATE_BASIC_SIMU | UPDATE_FBS | UPDATE_HBS | UPDATE_KFS
				| UPDATE_SIM;
	}

	private synchronized void updateState(int updateType, boolean success) {
		if (success) {
			mUpdatedSuccess |= updateType;
		} else {
			mUpdatedFailed |= updateType;
		}
		mDoneFlag |= updateType;
		mTaskFlag &= ~updateType;
		StringBuffer sb = new StringBuffer();
		sb.append("mUpdatedSuccess=").append(mUpdatedSuccess);
		sb.append(",mUpdatedFailed=").append(mUpdatedFailed);
		sb.append(",mDoneFlag=").append(mDoneFlag);
		sb.append(",mTaskFlag=").append(mTaskFlag);
		if ((mUpdatedFailed | mUpdatedSuccess) == mTaskRequest) {
			mOpt.setTimeComplete(System.currentTimeMillis());
			AppService.updateExecuteState(AppService.HAND_APP_START,
					AppService.EXECUTE_STATE_SUCCESS | mUpdatedSuccess);
			// 如果要通知可以发广播通知基金基本信息和净值信息更新完成。

			Bundle t = new Bundle();
			t.putInt(ValConfig.IT_TYPE, mUpdatedFailed);
			t.putInt(ValConfig.IT_ID, mUpdatedSuccess);
			AppFrame.getApp()
					.getLocalBroadcast()
					.sendBroadcast(
							mTaskRequest == getAllFlag() ? Receiver.FROM_UPDATE_LAUNCH
									: Receiver.FROM_UPDATE_NETVAL_BETCH, t);
			sb.append(",all is done send broadcast.");
			d("updateState", sb.toString());

			if (getAllFlag() == mTaskRequest) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						String[] types = new String[] { "公募", "私募" };
						Map<String, Integer> mapValue = DbOperat.getInstance().queryOptSum();
						Map<String, String> gm = new HashMap<String, String>();
						gm.put(Analytics.KEY_TYPE, types[0]);
						Map<String, String> sm = new HashMap<String, String>();
						sm.put(Analytics.KEY_TYPE, types[1]);
						MobclickAgent.onEventValue(AppFrame.getApp(), Analytics.CUSTOM_COUNT, gm,
								mapValue.get(types[0]));
						MobclickAgent.onEventValue(AppFrame.getApp(), Analytics.CUSTOM_COUNT, sm,
								mapValue.get(types[1]));
					}
				}).start();
			}

		}
	}

	public ReqResult<ReqNetOpt> getStartRequesterResult(String key, int handType) {
		ParStart par = new ParStart(0).setParams(basicInfoVer, kfsVer, fbsVer, hbsVer, smVer,
				managerVer, companyVer, newsTypeVer, opinionTypeVer, jsVer);
		d("ParStart", "basicInfoVer=" + basicInfoVer + ", kfsVer=" + kfsVer + ",fbsVer=" + fbsVer
				+ ",hbsVer=" + hbsVer + ",smVer=" + smVer + ",jsVer=" + jsVer);
		return par.setKey("ParStart", handType).execute();
	}

	public void updateItself(final ReqOpt opt, String title) {
		mDoneFlag = mTaskFlag = 0;
		mOpt = opt;
		mLogTitle = title;
		if (UPDATE_BASIC_FUND == (mTaskRequest & UPDATE_BASIC_FUND)) {
			new UpdateTrd(UPDATE_BASIC_FUND).start();
		}
		if (UPDATE_BASIC_SIMU == (mTaskRequest & UPDATE_BASIC_SIMU)) {
			new UpdateTrd(UPDATE_BASIC_SIMU).start();
		}
		if (UPDATE_SIM == (mTaskRequest & UPDATE_SIM)) {
			new UpdateTrd(UPDATE_SIM).start();
		}
		if (UPDATE_KFS == (mTaskRequest & UPDATE_KFS)) {
			new UpdateTrd(UPDATE_KFS).start();
		}
		if (UPDATE_FBS == (mTaskRequest & UPDATE_FBS)) {
			new UpdateTrd(UPDATE_FBS).start();
		}
		if (UPDATE_HBS == (mTaskRequest & UPDATE_HBS)) {
			new UpdateTrd(UPDATE_HBS).start();
		}
	}

	public void updateItself(final ReqOpt opt, HostDistribution r, String title) {
		mDoneFlag = mTaskFlag = 0;
		mOpt = opt;
		mLogTitle = title;
		if ("1".equals(r.getBasicInfoNeedUpdate())) {
			new UpdateTrd(UPDATE_BASIC_FUND).start();
			new UpdateTrd(UPDATE_BASIC_SIMU).start();
		} else {
			updateState(UPDATE_BASIC_FUND, true);
			updateState(UPDATE_BASIC_SIMU, true);
		}
		if ("1".equals(r.getKfsNeedUpdate())) {
			new UpdateTrd(UPDATE_KFS).start();
		} else {
			updateState(UPDATE_KFS, true);
		}
		if ("1".equals(r.getHbsNeedUpdate())) {
			new UpdateTrd(UPDATE_HBS).start();
		} else {
			updateState(UPDATE_HBS, true);
		}
		if ("1".equals(r.getSmNeedUpdate())) {
			new UpdateTrd(UPDATE_SIM).start();
		} else {
			updateState(UPDATE_SIM, true);
		}
		if ("1".equals(r.getFbsNeedUpdate())) {
			new UpdateTrd(UPDATE_FBS).start();
		} else {
			updateState(UPDATE_FBS, true);
		}
		String jsVersion = r.getNewJsVer();
		if (!StrUtils.isEmpty(jsVersion)) {
			if (StrUtils.isNumeric(jsVersion) && StrUtils.isNumeric(jsVer)) {
				if (Integer.parseInt(jsVersion) > Integer.parseInt(jsVer)) {
					HomeStockLayout.JS_LOAD_URL = jsVersion + "##" + r.getJsUrl();
				}
			} else {
				if (jsVersion.compareTo(jsVer) > 0) {
					HomeStockLayout.JS_LOAD_URL = jsVersion + "##" + r.getJsUrl();
				}
			}
		}
	}

	private boolean updateBasic(int updateType, boolean isSimu, String verKey) {
		AbsParam par = null;
		if (isSimu) {
			par = new ParUpdateSimuFundBasicInfo(0).setParams(basicInfoVer);
		} else {
			par = new ParUpdateOpenFundBasicInfo(0).setParams(basicInfoVer);
		}

		ReqResult<ReqNetOpt> rr = par.execute();
		if (rr.isSuccess()) {
			d(mLogTitle, "success load " + (verKey + " isSimu=" + isSimu) + " from server cost : "
					+ (System.currentTimeMillis() - mOpt.getTimeComplete()) + " ms");
			new UpdateTask(verKey, 0, updateType, rr.mData).start();
			return true;
		} else {
			d(mLogTitle, "failed load " + (verKey + " isSimu=" + isSimu) + " from server cost : "
					+ (System.currentTimeMillis() - mOpt.getTimeComplete()) + " ms" + " err="
					+ rr.mErr);
			return false;
		}
	}

	private boolean updateNetValue(int updateType, String netType, int dbType, String verKey,
			String ver) {
		ReqResult<ReqNetOpt> rr = new ParFundsNetValueBatch(0).setParams(netType, ver).execute();
		if (rr.isSuccess()) {
			d(mLogTitle,
					"success load " + verKey + " from server cost : "
							+ (System.currentTimeMillis() - mOpt.getTimeComplete()) + " ms");
			new UpdateTask(verKey, dbType, updateType, rr.mData).start();
			return true;
		} else {
			d(mLogTitle,
					"failed load " + verKey + " from server cost : "
							+ (System.currentTimeMillis() - mOpt.getTimeComplete()) + " ms"
							+ " err=" + rr.mErr);
			return false;
		}
	}

	private boolean updateBasicDb(Object obj, boolean isSimu, String verKey) {
		boolean result = DbOperat.getInstance()
				.updateBasicFundInfo((FundBasicInfoList) obj, isSimu);
		mSf.edit().putString(verKey, curDate).commit();
		return result;
	}

	private boolean updateNetValueDb(Object obj, int dbType, String verKey) {
		boolean result = DbOperat.getInstance().updateNetValue(obj, dbType, false);
		mSf.edit().putString(verKey, String.valueOf(mCurLong)).commit();
		return result;
	}

	class UpdateTrd extends Thread {
		int mType = 0;

		public UpdateTrd(int updateType) {
			this.mType = updateType;
			mTaskFlag |= updateType;
		}

		@Override
		public void run() {
			boolean handled = false;
			switch (mType) {
			case InitUpdateInfs.UPDATE_BASIC_FUND:
				handled = updateBasic(mType, false, ValConfig.SFVerBasicFundInfoVer);
				break;
			case InitUpdateInfs.UPDATE_BASIC_SIMU:
				handled = updateBasic(mType, true, ValConfig.SFVerBasicSimuInfoVer);
				break;
			case InitUpdateInfs.UPDATE_SIM:
				handled = updateNetValue(mType, ParFundsNetValueBatch.Type_Simu,
						FundConfig.DATA_SIMU, ValConfig.SFVerSimVer, smVer);
				break;
			case InitUpdateInfs.UPDATE_FBS:
				handled = updateNetValue(mType, ParFundsNetValueBatch.Type_Close,
						FundConfig.DATA_CLOSE, ValConfig.SFVerFbsVer, fbsVer);
				break;
			case InitUpdateInfs.UPDATE_HBS:
				handled = updateNetValue(mType, ParFundsNetValueBatch.Type_Moneys,
						FundConfig.DATA_MONEY, ValConfig.SFVerHbsVer, hbsVer);
				break;
			case InitUpdateInfs.UPDATE_KFS:
				handled = updateNetValue(mType, ParFundsNetValueBatch.Type_Opens,
						FundConfig.DATA_OPEN, ValConfig.SFVerKfsVer, kfsVer);
				break;
			}
			if (!handled) {
				updateState(mType, false);
			}
		}
	}

	class UpdateTask extends Thread {
		String mKeyVer = null;
		int mDbType = 0;
		int mUpDateType;
		long mStartTime = 0;
		Object mObj = null;

		public UpdateTask(String mKeyVer, int mDbType, int mUpDateType, Object obj) {
			this.mKeyVer = mKeyVer;
			this.mDbType = mDbType;
			this.mUpDateType = mUpDateType;
			this.mObj = obj;
			mStartTime = System.currentTimeMillis();
		}

		@Override
		public void run() {
			if (mUpDateType == UPDATE_BASIC_FUND) {
				updateBasicDb(mObj, false, mKeyVer);
				d(mLogTitle,
						"write " + (mKeyVer + " isSimu=" + false) + " to db cost : "
								+ (System.currentTimeMillis() - mStartTime) + " ms");
			} else if (mUpDateType == UPDATE_BASIC_SIMU) {
				updateBasicDb(mObj, true, mKeyVer);
				d(mLogTitle,
						"write " + (mKeyVer + " isSimu=" + true) + " to db cost : "
								+ (System.currentTimeMillis() - mStartTime) + " ms");
			} else {
				updateNetValueDb(mObj, mDbType, mKeyVer);
				d(mLogTitle, "write " + mKeyVer + " to db cost : "
						+ (System.currentTimeMillis() - mStartTime) + " ms" + " mUpDateType="
						+ mUpDateType);
			}
			updateState(mUpDateType, true);
		}
	}

}
