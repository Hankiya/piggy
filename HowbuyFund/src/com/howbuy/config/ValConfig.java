package com.howbuy.config;

public class ValConfig {
	private static final String DEBUG_URL_QA = "http://192.168.220.108:6080/howbuy-wireless/";
	private static final String DEBUG_URL_UAT = "http://10.70.70.21:7080/hws/";
	private static final String DEBUG_URL_TEST = "http://192.168.220.105:8070/hws_newest/";
	// ////////////////////////////////////////////////////////////////////////
	public static final String URL_BASE_DEBUG = DEBUG_URL_UAT;
	public static final String URL_BASE_RELEASE = "http://data.howbuy.com/hws/";
	public static final String URL_BASE_DEBUG_NEWS = "http://data.howbuy.com/hws/";
	public static final String ASSETS_SCRIPT = "config/howbuyfund.sql";
	public static final String ASSETS_FUNDTYPE = "config/fund_type_config.xml";
	public static final String URL_TRADE_BASEURL_RELEASE = "https://trade.ehowbuy.com/wap2/";
	public static final String URL_TRADE_BASEURL_RELEASE_DoMain = "trade.ehowbuy.com";
	public static final String URL_Trade_RENGOU = "trade/ipo.htm?method=apply&fundCode=";
	public static final String URL_Trade_SHENGOU = "trade/subs.htm?method=apply&fundCode=";
	public static final String URL_Trade_SHUHUI = "trade/redeemfund.htm?method=confirm&fundCode=";
	public static final String URL_Trade_ZHUANHUAN = "trade/exablefund.htm?method=apply&fundCode=";
	public static final String URL_Trade_RENGOU_End = "trade/ipo.htm";
	public static final String URL_Trade_SHENGGOU_End = "trade/subs.htm";
	public static final String URL_Trade_SHUHUI_End = "trade/redeemfund.htm";
	public static final String URL_Trade_ZHUANHUAN_End = "trade/exablefund.htm";
	public static final String URL_APP_MARKET = "http://a.wap.myapp.com/and2/s?aid=detail&appid=50801";
	public static final String URL_APP_XINTUO = "http://www.howbuy.com/mobile/mini/trust/";
	public static final String URL_APP_CHUXUGUAN = "http://www.howbuy.com/mobile/mini/cxg/";
	public static final String URL_APP_FUND = "http://www.howbuy.com/mobile/zsjj/";
	// START:TIME FORMAT
	public static final String DATEF_YMDHMS = "yyyyMMdd HH:mm:ss";
	public static final String DATEF_YMDHMS_ = "yyyy-MM-dd HH:mm:ss";
	public static final String DATEF_YMD_ = "yyyy-MM-dd";
	public static final String DATEF_YMD = "yyyyMMdd";
	public static final String DATEF_YMD_S_ = "yyyy-M-d";
	// END:TIME FORMAT

	// start activity for result request code
	public static final int ATY_REQUEST_FUND_DETAIL = 1;
	// end activity for result request code

	public static final String JPushAlias = "howbuytest3";
	// start secret settings key
	public static final String SECRET_SF_NAME = "setting_secret";
	public static final String SECRET_DEBUG_URL = "SECRET_DEBUG_URL";
	public static final String SECRET_DEBUG_LOG = "SECRET_DEBUG_LOG";
	public static final String SECRET_DEBUG_LOG_FILE = "SECRET_DEBUG_LOG_FILE";
	public static final String SECRET_DEBUG_POP = "SECRET_DEBUG_POP";
	public static final String SECRET_DEBUG_CRASH_MUTIFILE = "SECRET_DEBUG_CRASH_MUTIFILE";
	public static final String SECRET_DEBUG_CRASH_LAUNCH = "SECRET_DEBUG_CRASH_LAUNCH";
	public static final String SECRET_DEBUG_CRASH_DIALOG = "SECRET_DEBUG_CRASH_DIALOG";
	public static final String SECRET_DEBUG_LIST = "SECRET_DEBUG_LIST";
	public static final String SECRET_DEBUG_MORE = "SECRET_DEBUG_MORE";
	// end secret settings key

	// start settings key
	public static final String SET_SF_NAME = "setting_app";
	public static final String SET_ABOUT = "SET_ABOUT";
	public static final String SET_ACCOUNT = "SET_ACCOUNT";
	public static final String SET_SUBSCRIBE = "SET_SUBSCRIBE";
	public static final String SET_FEEDBACK = "SET_FEEDBACK";
	public static final String SET_RECOMMEND = "SET_RECOMMEND";
	public static final String SET_CHECKUPDATE = "SET_CHECKUPDATE";
	public static final String SET_APP_MARKET = "SET_APP_MARKET";
	public static final String SET_APP_XINTUO = "SET_APP_XINTUO";
	public static final String SET_APP_CHUXUGUAN = "SET_APP_CHUXUGUAN";
	public static final String SET_ATTENTION_WX = "SET_ATTENTION_WX";
	public static final String SET_BAND_SINA = "SET_BAND_SINA";
	public static final String SET_PUSH = "SET_PUSH";

	// end settings key

	public static final int LOAD_LIST_FIRST = 1, LOAD_LIST_REFUSH = 2, LOAD_LIST_PAGE = 4;
	public static final int SOURCE_NORMAL = 0, SOURCE_PUSH = 1, SOURCE_ADV = 2, SOURCE_OTHER = 3;
	public static final String KEY_TOP_RECOMEND_LIST = "KEY_TOP_RECOMEND_LIST";
	public static final String NULL_TXT = "", NULL_TXT0 = "暂无数据", NULL_TXT1 = "--",
			NET_ERR = "请求失败";
	// START INTENT KEY FOR TRANSLATE VALUES.
	public static final String IT_NAME = "IT_NAME";
	public static final String IT_ID = "IT_ID";
	public static final String IT_URL = "IT_URL";
	public static final String IT_ENTITY = "IT_ENTITY";
	public static final String IT_TYPE = "IT_TYPE";
	public static final String IT_FROM = "IT_FROM";
	// END INTENT KEY FOR TRANSLATE VALUES.

	/** sharedPreferences key **/
	public static final String SF_RATE_APP_LEVEL = "rate_app_level";

	public static final String SFbaseUser = "sfbaseuser";
	public static final String SFbaseSystem = "sfbaseSystem";
	public static final String SFfirstUUid = "SFfirstUUid";
	public static final String SFfirstVersion = "SFfirstVersion-1";
	public static final String SFfirstChanneIdId = "SFfirstChanneIdId";
	public static final String SFfirstProductId = "SFfirstProductId";
	public static final String SFfirstParPhoneModel = "SFfirstParPhoneModel";
	public static final String SFfirstSubPhoneModel = "SFfirstSubPhoneModel";
	public static final String SFfirstIVer = "SFfirstIVer";
	public static final String SFUserName = "SFUserName";
	public static final String SFUserPass = "SFUserPass";
	public static final String SFUserCusNo = "SFUserCusNo";
	public static final String SFUserSysAuto = "SFUserSys";
	public static final String SFCommentPostTime = "SFCommentPostTime";
	public static final String SFContentBigImgClick = "SFContentBigImgClick";

	/** 用户手机号 **/
	public static final String SFUserPhone = "SFUserPhone";
	/** 用户手机号的上传状态 **/
	public static final String SFUserPhoneState = "SFUserPhoneState";
	public static final String SFVerNewsContentVer = "SFVerNewsVer";
	/** 大盘更新JS **/
	public static final String SFVerJsVer = "SFVerJsVer";
	/** 经理版本 **/
	public static final String SFVerManagerVer = "SFVerManagerVer";
	/** 公司版本 **/
	public static final String SFVerCompanyVer = "SFVerCompanyVer";
	/** 新闻头部版本 **/
	public static final String SFVerNewsTypeVer = "SFVerNewsTypeVer";
	/** 研报头部版本 */
	public static final String SFVerOpinionTypeVer = "SFVerOpinionTypeVer";
	/** 基本信息版本 公募 **/
	public static final String SFVerBasicFundInfoVer = "SFVerBasicFundInfoVer";
	/** 基本信息版本 私募 **/
	public static final String SFVerBasicSimuInfoVer = "SFVerBasicSimuInfoVer";
	/** 开放式版本日期 **/
	public static final String SFVerKfsVer = "SFVerKfsVer";
	/** 开放式数据日期 **/
	public static final String SFVerKfsDATAVer = "SFVerKfsDATAVer";
	/** 货币式版本日期 **/
	public static final String SFVerHbsVer = "SFVerHbsVer";
	/** 货币式数据日期 **/
	public static final String SFVerHbsDATAVer = "SFVerHbsDATAVer";
	/** 私募版本日期 **/
	public static final String SFVerSimVer = "SFVerSimVer";
	/** 私募数据日期 **/
	public static final String SFVerSimDATAVer = "SFVerSimDATAVer";
	/** 封闭式版本日期 **/
	public static final String SFVerFbsVer = "SFVerFbsVer";
	/** 封闭式数据日期 **/
	public static final String SFVerFbsDATAVer = "SFVerFbsDATAVer";
	/** 定投刷新数据用 **/
	public static final String SFVerDingTou = "SFVerDingTou";
	/** 净值部分数据 **/
	public static final String SFPostionNetworthMap = "SFPostionNetworthMap";
	public static final String SFPostionFixMap = "SFPostionFixMap";
	/** 交易url end **/
	public static final String SFTransactionLastURL = "SFTransactionLastURL";
	/** 是否传完 **/
	public static final String SFContactStatus = "SFContactStatus";
	/** 是否再次出现提醒版本更新 **/
	public static final String SFSystemNeedUpadate = "SFSystemNeedUpadate";
	/** 防止快速再次进入 **/
	public static final String SFContentNoRepeatTime = "SFContentNoRepeatTime";
	/** 保存新闻详情页面字体大小 **/
	public static final String SF_ARTICAL_FONTSIZE_TYPE = "artical_fontsize_type";
	/** 内容页提示 **/
	public static final String SFContentNewhint = "SFContentNewhint";
	/** 程序启动过多少次 **/
	public static final String SFMainAppLunchSum = "SFAppLunchSum";
	/** 推荐提醒点击次数 **/
	public static final String SFMainTuiJianShowSum = "SFMainTuiJianClickSum";
	/** push开关 **/
	public static String sFSettingPush = "sFSettingPush";

	/** 自选 **/
	/** 自选排序类型 **/
	public static final String sFOptSortTypeGm = "sFOptSortType_GM";
	public static final String sFOptSortTypeSm = "sFOptSortType_SM";
	public static final String SFOPTUserClose = "SFOPTUNLoginVisable";

}
