package com.howbuy.test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.howbuy.commonlib.ParCheckAppUpdate;
import com.howbuy.commonlib.ParDingYue;
import com.howbuy.commonlib.ParDingYueCheckCode;
import com.howbuy.commonlib.ParFeedback;
import com.howbuy.commonlib.ParFilterCondition;
import com.howbuy.commonlib.ParMakeAppointment;
import com.howbuy.commonlib.ParNewsByPage;
import com.howbuy.commonlib.ParResearchByPage;
import com.howbuy.commonlib.ParRunChartData;
import com.howbuy.commonlib.ParShare2Friends;
import com.howbuy.commonlib.ParTrustListByID;
import com.howbuy.commonlib.ParTrustListByPage;
import com.howbuy.datalib.fund.AAParFtenHeavyHoldingInfo;
import com.howbuy.datalib.fund.AAParFtenSimuBasicInf;
import com.howbuy.datalib.fund.AAParFundChart;
import com.howbuy.datalib.fund.AAParFundHistoryValue;
import com.howbuy.datalib.fund.AAParFundInfoByType;
import com.howbuy.datalib.fund.AAParFundTradeInfo;
import com.howbuy.datalib.fund.AAParSimuRecommend;
import com.howbuy.datalib.fund.AAParUserStatusCheck;
import com.howbuy.datalib.fund.ParAdvertisementList;
import com.howbuy.datalib.fund.ParBook;
import com.howbuy.datalib.fund.ParCheckCode;
import com.howbuy.datalib.fund.ParCommentByPage;
import com.howbuy.datalib.fund.ParCommentByTimestamp;
import com.howbuy.datalib.fund.ParFinanceDebtList;
import com.howbuy.datalib.fund.ParFinanceIndexList;
import com.howbuy.datalib.fund.ParFixedInvestOrderByPage;
import com.howbuy.datalib.fund.ParFtenAchieveProformance;
import com.howbuy.datalib.fund.ParFtenCompanyInfo;
import com.howbuy.datalib.fund.ParFtenDebtList;
import com.howbuy.datalib.fund.ParFtenHeavyHolding;
import com.howbuy.datalib.fund.ParFtenInvestList;
import com.howbuy.datalib.fund.ParFtenManagerInf;
import com.howbuy.datalib.fund.ParFtenManagerList;
import com.howbuy.datalib.fund.ParFtenProfitDistributeList;
import com.howbuy.datalib.fund.ParFtenPropertyConfigList;
import com.howbuy.datalib.fund.ParFtenQuarterReportList;
import com.howbuy.datalib.fund.ParFtenRecessiveHeavyHolding;
import com.howbuy.datalib.fund.ParFtenRecruitList;
import com.howbuy.datalib.fund.ParFtenTicketCombinedList;
import com.howbuy.datalib.fund.ParFtenYearReportList;
import com.howbuy.datalib.fund.ParFtenfundAnnoList;
import com.howbuy.datalib.fund.ParFundPerformance;
import com.howbuy.datalib.fund.ParFundRunChartData;
import com.howbuy.datalib.fund.ParFundsNetValue;
import com.howbuy.datalib.fund.ParFundsNetValueBatch;
import com.howbuy.datalib.fund.ParFundsNetValueByIDs;
import com.howbuy.datalib.fund.ParFundsValueByPage;
import com.howbuy.datalib.fund.ParLogin;
import com.howbuy.datalib.fund.ParNewsAndResearchByKeyword;
import com.howbuy.datalib.fund.ParNewsByKeyword;
import com.howbuy.datalib.fund.ParRegister;
import com.howbuy.datalib.fund.ParResearchByKeyword;
import com.howbuy.datalib.fund.ParSendComment;
import com.howbuy.datalib.fund.ParSendUserActions;
import com.howbuy.datalib.fund.ParSimuFundsValueByPage;
import com.howbuy.datalib.fund.ParStart;
import com.howbuy.datalib.fund.ParSyncOptional;
import com.howbuy.datalib.fund.ParTrustContent;
import com.howbuy.datalib.fund.ParTrustList;
import com.howbuy.datalib.fund.ParUpAndDownCount;
import com.howbuy.datalib.fund.ParUpOrDownFund;
import com.howbuy.datalib.fund.ParUpdateCompany;
import com.howbuy.datalib.fund.ParUpdateManager;
import com.howbuy.datalib.fund.ParUpdateOpenFundBasicInfo;
import com.howbuy.datalib.fund.ParUpdateSimuFundBasicInfo;
import com.howbuy.datalib.fund.ParUploadContact;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqResult;

public class CommonLibTest {
	private static Object mLock = new Object();
	private AbsParam mParam = null;
	private static CommonLibTest mInstance = null;

	private CommonLibTest() {

	};

	public static CommonLibTest getInstance() {
		if (mInstance == null) {
			synchronized (mLock) {
				if (mInstance == null) {
					mInstance = new CommonLibTest();
				}
			}
		}
		return mInstance;
	}

	public ReqResult<ReqNetOpt> ParCheckAppUpdate(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParCheckAppUpdate(key, calback);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParDingYue(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParDingYue(key, calback);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParDingYueCheckCode(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParDingYueCheckCode(key, calback);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParFeedback(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParFeedback(key, calback);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParFilterCondition(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParFilterCondition(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParMakeAppointment(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParMakeAppointment(key, calback);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParNewsByPage(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParNewsByPage(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParResearchByPage(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParResearchByPage(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParRunChartData(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParRunChartData(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParShare2Friends(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParShare2Friends(key, calback);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParTrustListByID(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParTrustListByID(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParTrustListByPage(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParTrustListByPage(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParAdvertisementList(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParAdvertisementList(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParBook(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParBook(key, calback);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParCheckCode(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParCheckCode(key, calback);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParCommentByPage(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParCommentByPage(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParCommentByTimestamp(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParCommentByTimestamp(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParFinanceDebtList(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParFinanceDebtList(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParFinanceIndexList(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParFinanceIndexList(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParFixedInvestOrderByPage(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParFixedInvestOrderByPage(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParFtenAchieveProformance(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParFtenAchieveProformance(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParFtenCompanyInfo(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParFtenCompanyInfo(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParFtenDebtList(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParFtenDebtList(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParFtenfundAnnoList(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParFtenfundAnnoList(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParFtenHeavyHolding(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParFtenHeavyHolding(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParFtenInvestList(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParFtenInvestList(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParFtenManagerInf(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParFtenManagerInf(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParFtenManagerList(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParFtenManagerList(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParFtenProfitDistributeList(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParFtenProfitDistributeList(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParFtenPropertyConfigList(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParFtenPropertyConfigList(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParFtenQuarterReportList(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParFtenQuarterReportList(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParFtenRecessiveHeavyHolding(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParFtenRecessiveHeavyHolding(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParFtenRecruitList(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParFtenRecruitList(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParFtenTicketCombinedList(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParFtenTicketCombinedList(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParFtenYearReportList(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParFtenYearReportList(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParFundRunChartData(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParFundRunChartData(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParFundsNetValue(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParFundsNetValue(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParFundsNetValueByIDs(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParFundsNetValueByIDs(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParFundsNetValueBatch(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParFundsNetValueBatch(key, calback, 0l);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParFundsValueByPage(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParFundsValueByPage(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParLogin(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParLogin(key, calback);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParNewsAndResearchByKeyword(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParNewsAndResearchByKeyword(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParNewsByKeyword(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParNewsByKeyword(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParFundPerformance(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParFundPerformance(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParRegister(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParRegister(key, calback);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParResearchByKeyword(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParResearchByKeyword(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParSendComment(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParSendComment(key, calback);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParSendUserActions(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParSendUserActions(key, calback);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParSimuFundsValueByPage(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParSimuFundsValueByPage(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParStart(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParStart(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParSyncOptional(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParSyncOptional(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParTrustContent(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParTrustContent(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParTrustList(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParTrustList(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParUpAndDownCount(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParUpAndDownCount(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParUpdateCompany(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParUpdateCompany(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParUpdateManager(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParUpdateManager(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParUpdateOpenFundBasicInfo(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParUpdateOpenFundBasicInfo(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParUpdateSimuFundBasicInfo(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParUpdateSimuFundBasicInfo(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParUploadContact(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParUploadContact(key, calback);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> ParUpOrDownFund(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new ParUpOrDownFund(key, calback);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> AAParFtenHeavyHoldingInfo(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new AAParFtenHeavyHoldingInfo(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> AAParFundHistoryValue(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new AAParFundHistoryValue(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> AAParFundInfoByType(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new AAParFundInfoByType(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> AAParFundTradeInfo(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new AAParFundTradeInfo(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> AAParUserStatusCheck(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new AAParUserStatusCheck(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> AAParFundChart(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new AAParFundChart(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> AAParFtenSimuBasicInf(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new AAParFtenSimuBasicInf(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	public ReqResult<ReqNetOpt> AAParSimuRecommend(String key, IReqNetFinished calback,
			HashMap<String, String> args) {
		mParam = new AAParSimuRecommend(key, calback, 0);
		intitParams(mParam, args);
		return mParam.execute();
	}

	private void intitParams(AbsParam ap, HashMap<String, String> args) {
		if (args != null && args.size() > 0) {
			Iterator<Entry<String, String>> it = args.entrySet().iterator();
			Entry<String, String> t = null;
			while (it.hasNext()) {
				t = it.next();
				ap.addArg(t.getKey(), t.getValue(), true);
			}
		}
	}
}
