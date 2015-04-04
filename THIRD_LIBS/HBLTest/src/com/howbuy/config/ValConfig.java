package com.howbuy.config;

import com.howbuy.lib.interfaces.ICharType;

public class ValConfig {

	private static final String DEBUG_URL_QA = "http://192.168.220.108:6080/howbuy-wireless/";
	private static final String DEBUG_URL_UAT = "http://10.70.70.21:7080/hws/";
	private static final String DEBUG_URL_TEST = "http://192.168.220.105:8070/hws_newest/";
	public static final String URL_BASE_DEBUG = DEBUG_URL_QA;
	public static final String URL_BASE_RELEASE = "http://data.howbuy.com/hws/";
	// START:GPS ACCESS STATE.
	public static final int GPS_NONE = 0;
	public static final int GPS_SATELLITE = 1;
	public static final int GPS_INTERNET = 2;
	// END: GPS ACCESS STATE;

	// START:TIME FORMAT
	public static final String DATE_FORMAT_YMDHMS = "yyyyMMdd HH:mm:ss";
	public static final String DATE_FORMAT_YMDHMS_ = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT_YMD_ = "yyyy-MM-dd";
	public static final String DATE_FORMAT_YMD = "yyyyMMdd";
	// END:TIME FORMAT

	// start secret settings key
	public static final String SECRET_SF_NAME = "setting_secret";
	public static final String SECRET_DEBUG_URL = "SECRET_DEBUG_URL";
	public static final String SECRET_DEBUG_LOG = "SECRET_DEBUG_LOG";
	public static final String SECRET_DEBUG_LOG_FILE = "SECRET_DEBUG_LOG_FILE";
	public static final String SECRET_DEBUG_POP = "SECRET_DEBUG_POP";
	public static final String SECRET_DEBUG_CRASH_MUTIFILE = "SECRET_DEBUG_CRASH_MUTIFILE";
	public static final String SECRET_DEBUG_CRASH_LAUNCH = "c";
	public static final String SECRET_DEBUG_CRASH_DIALOG = "SECRET_DEBUG_CRASH_DIALOG";
	public static final String SECRET_DEBUG_LIST = "SECRET_DEBUG_LIST";
	// end secret settings key

	// START:CACHE DATA TIME
	/* 60秒 */
	public static final Long CACHE_TIME_1MINUTE = 60 * 1000l;
	/* 12小时 */
	public static final Long CACHE_TIME_12HOURS = 12 * 60 * CACHE_TIME_1MINUTE;
	/* 24小时 */
	public static final Long CACHE_TIME_1DAY = 2 * CACHE_TIME_12HOURS;
	/* cache缓存最大60天 */
	public static final Long CACHE_TIME_2MONTH = 60 * CACHE_TIME_1DAY;
	/* 0 */
	public static final Long CACHE_TIME_NONE = 0l;
	// END:CACHE DATA TIME

	public static final String PACKAGENAME = "howbuy.android.trustcollection";
	public static final String actionServiceDownloadFirst = "actionServiceDownloadFirst";

	/** umeng anylisy **/
	public static final String UM_EVENT_PART = "Event_Part";// 栏目浏览
	public static final String UM_EVENT_NEWSTYPE = "Event_News_Type";// 新闻类型
	public static final String UM_EVENT_ORDER = "Event_DingYue";// 订阅
	public static final String UM_EVENT_SUBSCRIPTION = "Event_YuYue";// 预约

	/** sharedPreferences key **/
	public static final String SFbaseUser = "sfbaseuser";
	public static final String SFbaseSystem = "sfbaseSystem";
	public static final String SFfirstUUid = "SFfirstUUid";
	public static final String SFfirstVersion = "SFfirstVersion";
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
	public static final String sFSettingPush = "sFSettingPush";

	/** 用户手机号 **/
	public static final String SFUserPhone = "SFUserPhone";

	/** 保存各个的开关，前面加分类的名字以区分 **/
	public static final String SFNetWorthToggleSelf = "SFNetWorthToggleSelf";
	public static final String SFNetWorthOrder = "SFNetWorthOrder";
	public static final String SFNetWorthJiangXu = "SFNetWorthJiangXu";
	public static final String SFNetWorthGroupDate = "SFNetWorthGroupDate";
	public static final String SFNetWorthFixTopTag = "SFNetWorthFixTopTag";

	public static final String SFNetWorthOptionMiddle = "SFNetWorthOptionMiddle";
	public static final String SFNetWorthOptionEnd = "SFNetWorthOptionMiddle";

	/** 是否再次出现提醒版本更新 **/
	public static final String SFSystemNeedUpadate = "SFSystemNeedUpadate";
	/** 防止快速再次进入 **/
	public static final String SFContentNoRepeatTime = "SFContentNoRepeatTime";
	/** 保存新闻详情页面字体大小 **/
	public static final String SFNewsFontSize = "SFNewsFontSize";
	/** 内容页提示 **/
	public static final String SFContentNewhint = "SFContentNewhint";
	/** 程序启动过多少次 **/
	public static final String SFMainAppLunchSum = "SFAppLunchSum";
	/** 推荐提醒点击次数 **/
	public static final String SFMainTuiJianShowSum = "SFMainTuiJianClickSum";

	/** 传值 **/
	public static final String SFTabFundInfo = "SFTabFundInfo";
	public static final String Intent_type = "Intent_type";
	public static final String Intent_intotype = "Intent_intotype";
	public static final String Intent_name = "Intent_name";
	public static final String Intent_id = "Intent_id";
	public static final String Intent_bean = "Intent_bean";
	public static final String Intent_yuyuetype = "Intent_yuyuetype";
	public static final String Intent_normal = "Intent_normal";
	public static final String Intent_url = "Intent_url";

	/** content values **/
	public static final String Content_Key = "Content_valuesKey";
	public static final String Content_value = "Content_value";
	public static final String Content_TAG = "Content_tag";

	/** hander **/
	public static final int SHOW_START = 1101;
	public static final int SHOW_SUCCESS = 1202;
	public static final int SHOW_NODATA = 1303;
	public static final int SHOW_ERROR = 1404;
	public static final int SHOW_RELOAD = 1505;
	public static final int SHOW_SEPT = 1606;
	public static final int SHOW_IMAGE = 1707;
	public static final int SHOW_ReFresh = 1808;
	public static final int SHOW_Order = 1909;

	public enum FilterType {
		/**
		 * 公司背景 起始资金 信托分类 投资方向 预期收益 产品期限
		 */
		gsbj, qszj, xtfl, tzfx, yqsy, cpqx;
	}

	public enum CharType implements ICharType {
		TYPE_ALL("全部", "00") {
			@Override
			public int getColor(int color) {
				if (color != 0) {
					return color;
				}
				return 0xff0099cc;
			}
		},
		TYPE_BUSSINESS("工商企业", "20") {
			@Override
			public int getColor(int color) {
				if (color != 0) {
					return color;
				}
				return 0xff0819f2;
			}
		},
		TYPE_ESTATE("房地产", "11") {
			@Override
			public int getColor(int color) {
				if (color != 0) {
					return color;
				}
				return 0xffcc0000;
			}
		},
		TYPE_BASIC("基础设施", "13") {
			@Override
			public int getColor(int color) {
				if (color != 0) {
					return color;
				}
				return 0xffff8800;
			}
		},
		TYPE_FINANCIAL("金融", "12") {
			@Override
			public int getColor(int color) {
				if (color != 0) {
					return color;
				}
				return 0xff9933cc;
			}
		},
		TYPE_MINING("工矿企业", "22") {
			@Override
			public int getColor(int color) {
				if (color != 0) {
					return color;
				}
				return 0xff9933cc;
			}
		},
		TYPE_OTHER("其他", "99") {
			@Override
			public int getColor(int color) {
				if (color != 0) {
					return color;
				}
				return 0xff669900;
			}
		};
		private String mName = "";
		private String mCode = "";
		private int mColor = -1;

		private CharType(String name, String code) {
			mName = name;
			mCode = code;
		}

		public String getName() {
			return mName;
		}

		public String getCode() {
			return mCode;
		}

		public int getType() {
			return ordinal();
		}

		public String getTag() {
			return name();
		}
	}
}
