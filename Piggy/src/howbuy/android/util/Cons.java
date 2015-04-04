package howbuy.android.util;

/**
 * 数据类
 * 
 * @author Administrator SFfirstVersion判断当前版本是否是第一次安装
 * 
 */
public class Cons {

	public static final String fundCode = "482002";// 070035//070029//070001/481001/482002/450009/100038/519069
	public static final boolean isDebug = false;
	public static final String PACKAGENAME = "howbuy.android.piggy";
	
	public static final String SUPPORT_CHANNELPAY="{\"channels\":[{\"name\":\"1013\",\"version\":1}]}";
//	public static final String SUPPORT_CHANNELPAY="";
	public static final String CP_Debug_Flag="TEST";//PRODUCT//TEST
     
	/** umeng anylisy **/
	public static final String EVENT_UI_UnLock = "UI_UnLock";
	public static final String EVENT_UI_Main = "UI_Main";
	public static final String EVENT_UI_Setting = "UI_Setting";
	public static final String EVENT_UI_Login = "UI_Login";
	public static final String EVENT_UI_AccountManage = "UI_AccountManage";
	public static final String EVENT_UI_GetCheckCode = "UI_GetCheckCode";
	public static final String EVENT_UI_Register = "UI_Register";
	public static final String EVENT_Event_CallSP = "Event_CallSP";
	public static final String EVENT_UI_SetLock = "UI_SetLock";
	public static final String EVENT_UI_BindCard = "UI_BindCard";
	public static final String EVENT_UI_TradeHistory = "UI_TradeHistory";
	public static final String EVENT_UI_Active = "UI_Active";
	public static final String EVENT_UI_SaveMoney = "UI_SaveMoney";
	public static final String EVENT_UI_GetMoney = "UI_GetMoney";
	public static final String EVENT_UI_LoginPWModify = "UI_LoginPWModify";
	public static final String EVENT_UI_TradePWModify = "UI_TradePWModify";
	public static final String EVENT_UI_MobilePhoneModify = "UI_MobilePhoneModify";

	/** Action **/
	public static final String actionServiceDownloadOpen = "howbuy.android.action.actiondownloadopen";// 后台下载
	public static final String actionServiceDownloadFirst = "howbuy.android.action.actiondownloadfirst";// 启动接口完成
	public static final String actionReciverSysAuto = "howbuy.android.action.actionreciversysauto";// 更新
	public static final String actionReciverSelfProgress = "howbuy.android.action.actionreciverselfprogress";// selfprogress刷新
	public static final String actionReciverNewsReciver = "howbuy.android.action.actionrecivernewsreciver";// News接收按钮事件
	public static final String actionReciverNewsProgress = "howbuy.android.action.actionrecivernewsprogress";//
	public static final String actionReciverOpenProgress = "howbuy.android.action.actionreciveropenprogress";//
	public static final String actionReciverDingTouProgress = "howbuy.android.action.actionreciverdingtouprogress";//
	public static final String actionReciverNewsListChange = "howbuy.android.action.actionrecivernewslistchange";//

	// 末尾
	public static final String TAG = "NetworthMainActivity";


	/** other Filename **/
	public static final String fileNameuploadedContactNumber = "uploadedContactNumber";

	/** sharedPreferences key **/
	public static final String SFbaseUser = "sfbaseuser";
	public static final String SFbaseSystem = "sfbaseSystem";
	public static final String SFbaseData = "SFbaseData";
	public static final String SFfirstUUid = "SFfirstUUid";
	public static final String SFfirstVersion = "SFfirstVersion-1";
	public static final String SFfirstChanneIdId = "SFfirstChanneIdId";
	public static final String SFfirstProductId = "SFfirstProductId";
	public static final String SFfirstParPhoneModel = "SFfirstParPhoneModel";
	public static final String SFfirstSubPhoneModel = "SFfirstSubPhoneModel";
	public static final String SFfirstCoopId = "SFfirstCoopId";
	public static final String SFfirstactionId= "SFfirstactionId";
	public static final String SFfirstcorpId= "SFfirstcorpId";
	public static final String SFfirstIVer = "SFfirstIVer";
	public static final String SFUserName = "SFUserName";
	public static final String SFUserPass = "SFUserPass";
	public static final String SFUserCusNo = "SFUserCusNo";
	public static final String SFSoundFlag="SFSoundFlag";
	public static final String SFBindCardParams="SFBindCardParams";

	/** 用户锁屏 **/
	public static final String SFPatternValue = "SFPatternValue";
	public static final String SFUserNOHistory = "SFUserNOHistory";//用户身份证
	public static final String SFPatternFlag = "SFPatternFlag";
	public static final String SFLoginOutFlag = "SFIsLoginOut";
	public static final String SFPatternForgetFlag = "SFPatternForget";
	public static final String SFLoginXXXXXX = "SFLoginXXXXXX";
	public static final String SFHomePressTime = "SFHomePressTime";
	/** 用户手机号 **/
	public static final String SFUserPhone = "SFUserPhone";

	/** push开关 **/
	public static String sFSettingPush = "sFSettingPush";

	/** 传值 **/
	public static final String SFRsa = "SFRsa";

	public static final String Intent_type = "Intent_type";
	public static final String Intent_type_sub = "Intent_type_sub";
	public static final String Intent_name = "Intent_name";
	public static final String Intent_bean = "Intent_bean";
	public static final String Intent_normal = "Intent_normal";
	public static final String Intent_id = "Intent_id";

	/** content values **/
	public static final String Content_Key = "Content_valuesKey";
	public static final String Content_value = "Content_value";
	public static final String Content_TAG = "Content_tag";

	/** hander **/
	public final static int SHOW_START = 1101;
	public final static int SHOW_SUCCESS = 1202;
	public final static int SHOW_NODATA = 1303;
	public final static int SHOW_ERROR = 1404;
	public final static int SHOW_RELOAD = 1505;
	public final static int SHOW_SEPT = 1606;
	public final static int SHOW_IMAGE = 1707;
	public final static int SHOW_ReFresh = 1808;
	public final static int SHOW_Order = 1909;
	public final static int SHOW_ASYN_ERROR = 4007;  //存钱时cp返回状态未知
	public final static int SHOW_FORCELOGIN = 9000;  //强制登陆
	public final static int SHOW_BindOtherChannl =9800;  //其他渠道

}
