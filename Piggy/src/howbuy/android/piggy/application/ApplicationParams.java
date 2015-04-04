package howbuy.android.piggy.application;

import howbuy.android.piggy.R;
import howbuy.android.piggy.parameter.GlobalParams;
import howbuy.android.piggy.parameter.PiggyParams;
import howbuy.android.piggy.service.ServiceMger;
import howbuy.android.util.AndroidUtil;
import howbuy.android.util.Cons;
import howbuy.android.util.InitParams;
import howbuy.android.util.ManifestMetaData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class ApplicationParams extends Application {
	private SharedPreferences sF;
	private Activity activity;
	private ArrayList<Activity> mActivities;
	private static ApplicationParams applictaionParams = null;
	private Map<String, String> pubNetParams;
	private boolean firstStart;
	private boolean needPattern;//need jump pattern  
	private HashMap<String, Object> intentData;
	private InitParams initParams;
	private PiggyParams mParameter;
	private AtomicInteger sumRequest=new AtomicInteger(0);
	private ServiceMger mServiceMger;
	private GlobalParams mGlobalParams;
	private String userNo;

	public static ApplicationParams getInstance() {
		return applictaionParams;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
        JPushInterface.setDebugMode(false); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
		
		applictaionParams = this;
		mActivities=new ArrayList<Activity>();
		mServiceMger=new ServiceMger(this);
		// CrashHandler.getInstance().init(this);
		initParams = new InitParams(this);
		sF = getSharedPreferences(Cons.SFbaseUser, MODE_PRIVATE);
		pubNetParams = new HashMap<String, String>();
		intentData = new HashMap<String, Object>();

		// service启动，非第一次执行
		if (initParams.isFirstStart() == true) {// 如果不是是第一次启动
			setFirstStart(true);
			initParams.initNetPublicParams();
		}else {
			initParams.doUpdate(AndroidUtil.getVersionName());
		}

		setPubNetParams(initParams.getPubParams());

		boolean is = ApplicationParams.getInstance().getsF().getBoolean(Cons.sFSettingPush, true);

		// 设置umeng
		// FeedbackAgent agent = new FeedbackAgent(this);
		// agent.startFeedbackActivity();
	
		
		if (is) {
			if (GlobalParams.getGlobalParams().isDebugFlag()) {
				JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
				JPushInterface.setAlias(this, "howbuytest", new TagAliasCallback() {
					
					@Override
					public void gotResult(int arg0, String arg1, Set<String> arg2) {
						// TODO Auto-generated method stub
						Log.d("push", "set alias success");
					}
				});
			}
		}

		if (GlobalParams.getGlobalParams().isDebugFlag()) {
			AndroidUtil.enableStrictMode();
		}

		initImageLoader(getApplicationContext());
		mServiceMger.bindService();
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		// //System.out.println("-------------------------------------onLowMemory");
		System.gc();
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		// TODO Auto-generated method stub
	}

	// 七个公共参数存入Application
	public Map<String, String> createPubParams() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("version", sF.getString(Cons.SFfirstVersion, ""));
		map.put("channelId", sF.getString(Cons.SFfirstChanneIdId, ""));
		map.put("productId", sF.getString(Cons.SFfirstProductId, ""));
		map.put("parPhoneModel", sF.getString(Cons.SFfirstParPhoneModel, ""));
		map.put("subPhoneModel", sF.getString(Cons.SFfirstSubPhoneModel, ""));
		map.put("token", sF.getString(Cons.SFfirstUUid, ""));
		map.put("iVer", sF.getString(Cons.SFfirstIVer, ""));
		map.put("corpId", ManifestMetaData.get(this, "TransactionCorpId").toString());
		map.put("coopId", ManifestMetaData.get(this, "TransactionCoopId").toString());
		return map;
	}

	public HashMap<String, Object> getIntentData() {
		return intentData;
	}

	public void setIntentData(HashMap<String, Object> intentData) {
		this.intentData = intentData;
	}

	public boolean isFirstStart() {
		return firstStart;
	}

	public void setFirstStart(boolean firstStart) {
		this.firstStart = firstStart;
	}

	public Map<String, String> getPubNetParams() {
		return pubNetParams;
	}

	public void setPubNetParams(Map<String, String> pubNetParams) {
		// for (Map.Entry<String, String> entry : pubNetParams.entrySet()) {
		// System.out.println(entry.getKey());
		// System.out.println(entry.getValue());
		// }
		this.pubNetParams = pubNetParams;
	}

	public SharedPreferences getsF() {
		return getSharedPreferences(Cons.SFbaseUser, MODE_PRIVATE);
	}

	public void setsF(SharedPreferences sF) {
		this.sF = sF;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public ArrayList<Activity> getmActivities() {
		return mActivities;
	}
	
	public void clearActivityTask(){
		ArrayList<Activity> activities=getmActivities();
		for (int i = 0; i < activities.size(); i++) {
			Activity a=activities.get(i);
			if (a!=null) {
				a.finish();
			}
		}
	}

	public void setmActivities(ArrayList<Activity> mActivities) {
		this.mActivities = mActivities;
	}

	public boolean isNeedPattern() {
		return needPattern;
	}

	public void setNeedPattern(boolean needPattern) {
		this.needPattern = needPattern;
	}

	public PiggyParams getPiggyParameter() {
		if (mParameter == null) {
			return PiggyParams.getInstance();
		}
		return mParameter;
	}

	public void setmParameter(PiggyParams mParameter) {
		this.mParameter = mParameter;
	}
	
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	public int getSumRequest() {
		int sum= sumRequest.getAndAdd(1);
		Log.d("ApplicationParams", sum+"");
		return sum;
	}

	public void setSumRequest(int sumRequest) {
		this.sumRequest.getAndSet(sumRequest);
	}
	

	public ServiceMger getServiceMger() {
		return mServiceMger;
	}

	public void setServiceMger(ServiceMger mServiceMger) {
		this.mServiceMger = mServiceMger;
	}

	public GlobalParams getGlobalParams() {
		if (mGlobalParams==null) {
			mGlobalParams=new GlobalParams();
			mGlobalParams.setDebugFlag(getResources().getBoolean(R.bool.SECRET_DEBUG_URL));
			mGlobalParams.setEncryptDebugFlag(getResources().getBoolean(R.bool.SECRET_DEBUG_ENCRYPT));
		}
		return mGlobalParams;
	}

	public void setmGlobalParams(GlobalParams mGlobalParams) {
		this.mGlobalParams = mGlobalParams;
	}


	public String getUserNo() {
		if (userNo==null) {
			userNo=getsF().getString(Cons.SFUserCusNo, null);
		}
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	
	
}
