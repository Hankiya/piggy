package com.howbuy.datalib.trade;

import howbuy.android.piggy.api.dto.AccessInfo;
import howbuy.android.piggy.api.dto.BindCardDto;
import howbuy.android.piggy.api.dto.BindCardInf;
import howbuy.android.piggy.api.dto.EstimatesIncomeItem;
import howbuy.android.piggy.api.dto.EstimatesIncomeList;
import howbuy.android.piggy.api.dto.FundLimitDto;
import howbuy.android.piggy.api.dto.HeaderInfoDto;
import howbuy.android.piggy.api.dto.IncomeDto;
import howbuy.android.piggy.api.dto.NoticeDto;
import howbuy.android.piggy.api.dto.NoticeDtoList;
import howbuy.android.piggy.api.dto.ProductInfo;
import howbuy.android.piggy.api.dto.ProvinceInfoDto;
import howbuy.android.piggy.api.dto.SupportBankAndProvinceDto;
import howbuy.android.piggy.api.dto.SupportBankBranchDto;
import howbuy.android.piggy.api.dto.SupportBankBranchListDto;
import howbuy.android.piggy.api.dto.SupportBankDto;
import howbuy.android.piggy.api.dto.TradeDate;
import howbuy.android.piggy.api.dto.TradeQueryDto;
import howbuy.android.piggy.api.dto.TradeQueryItemDto;
import howbuy.android.piggy.api.dto.UserInfoDto;
import howbuy.android.piggy.api.dto.UserLimitDto;
import howbuy.android.piggy.api.dto.UserTypeDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.myjson.reflect.TypeToken;
import com.howbuy.datalib.trade.ParTrade.ITradeHandler;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.StrUtils;

public class TradeBuilder {

	public static final String urlGetRsaSecret = "common/getsercuritykey.htm"; // 获取rsa私钥
	public static final String urlGetSms = "misce/retriveDynaPwd.htm"; // 获取验证码
	public static final String urlSmsVerCommit = "misce/mobileValidate.htm"; // 验证码提交
	public static final String urlGetBankList = "query/supportBankList.htm"; // 获取支行列表
	public static final String urlGetBankBranchList = "query/branchBankList.htm"; // 获取分行列表
	public static final String urlRegister = "cust/register.htm"; // 注册
	public static final String urlLogin = "login/login.htm"; // 登录
	public static final String urlBindCard = "cust/bindCard.htm"; // 绑卡
	public static final String urlGetUserInfo = "query/userInfo.htm"; // 用户信息(合并用户信息和资产信息)
	public static final String urlGetProductInfo = "query/fundProdList.htm"; // 产品信息
	public static final String urlGetAssetsInfo = "cust/assetsInfo.htm"; // 资产信息
	public static final String urlPwdResetVal = "cust/custInfoValidate.htm"; // 用户修改密码验证
	public static final String urlPwdResetCommit = "cust/pwdReset.htm"; // 用户修改密码提交
	public static final String urlUpdateMobile = "cust/custInfoModify.htm"; // 用户手机号修改
	public static final String urlDeposit = "trade/singleDeposit.htm"; // 单笔存款
	public static final String urlntervalDeposit = "trade/intervalDeposit.htm"; // 定存
	public static final String urlQuQian = "trade/withdraw.htm";// 取钱
	public static final String urlHistory = "cust/tradeHistory.htm";// 交易查询
	public static final String urlShouyi = "cust/funddividend.htm";// 收益查询
	public static final String urlTRi = "query/acckdate.htm";// t日查询
	public static final String urlActiviteAccountQuery = "account/activeFind.htm";// 激活查询
	public static final String urlActiveSubmit = "account/activeSubmit.htm";// 激活提交
	public static final String urlUpdate = "start/checkupdate.protobuf";// 检查更新
	public static final String urlCusLimit = "query/custLimit.htm";// 查询用户限额
	public static final String urlQueryQrnh = "query/fundNav.htm";// 查询七日年化
	public static final String urlQueryEstimatesIncome = "query/pgyBnkIncome.htm";// 查询用户每日估算收益
	public static final String urlAd = "advert/advertlist.protobuf";// 广告
	public static final String urlcustFundLimit = "query/queryCustFundLimit.htm";// 查询银行卡限额
	public static final String urlNotice = "query/notice.htm";// 查询通知类型
	public static final String urlWebViewRequest = "activity/waprequest.htm";// 查询通知类型

	public static void setBaseUrl(String tradeUrl, String tradeDebugUrl) {
		ParTrade.setBaseUrl(tradeUrl, tradeDebugUrl);
	}

	private ParTrade mParTrade = null;

	private TradeBuilder(String urlSuffix, boolean hasSecret, String... args) {
		mParTrade = new ParTrade(0);
		mParTrade.setUrl(urlSuffix, false);
		mParTrade.setHasSecret(hasSecret);
		mParTrade.addArg(args);
	}

	private TradeBuilder(String urlSuffix, boolean hasSecret, Map<String, String> arg,
			String... args) {
		this(urlSuffix, hasSecret, args);
		if (arg != null) {
			Iterator<Entry<String, String>> its = arg.entrySet().iterator();
			while (its.hasNext()) {
				Entry<String, String> et = its.next();
				mParTrade.addArg(et.getKey(), et.getValue());
			}
		}
	}

	public TradeBuilder setCacheTime(long time) {
		mParTrade.setCacheTime(time);
		return this;
	}

	public ReqResult<ReqNetOpt> execute(int handType, IReqNetFinished l) {
		return mParTrade.setCallback(handType, l).execute();
	}

	public ReqResult<ReqNetOpt> execute() {
		return mParTrade.execute();
	}

	public static TradeBuilder newRsa(String tokenId) {
		TradeBuilder tb = new TradeBuilder(urlGetRsaSecret, false, "tokenId", tokenId);
		tb.mParTrade.setTradeHandler(new ITradeHandler() {

			@Override
			public Object handSuccess(Object body, HeaderInfoDto rh) throws WrapException {
				String rsaKey = body == null ? null : body.toString();
				if (!StrUtils.isEmpty(rsaKey)) {
					GlobalApp.getApp().getsF().edit()
							.putString(DesUtilForNetParam.SF_RSA_ID, rsaKey).commit();
				}
				return rsaKey;
			}

			@Override
			public void HandFailed(HeaderInfoDto rh, ReqNetOpt opt) throws WrapException {
				if (GlobalApp.getApp().getNetType() > 1 && ParTrade.checkMaxNum()) {
					DesUtilForNetParam.doResetRsaKay();
				}
			}

		});
		return tb;
	}

	public static TradeBuilder newSmsSend(String tokenId, String phone) {
		TradeBuilder tb = new TradeBuilder(urlGetSms, true, "tokenId", tokenId, "mobile", phone);
		return tb;
	}

	public static TradeBuilder newSmsVeri(String tokenId, String dynaPwd, String phone) {
		TradeBuilder tb = new TradeBuilder(urlSmsVerCommit, true, "tokenId", tokenId, "mobile",
				phone, "dynaPwd", dynaPwd);
		return tb;
	}

	public static TradeBuilder newBankList(String tokenId, String jsonList) {
		TradeBuilder tb = new TradeBuilder(urlGetBankList, false, "tokenId", tokenId,
				"supportPayChannel", jsonList);
		tb.mParTrade.setTradeHandler(new ITradeHandler() {
			@Override
			public Object handSuccess(Object body, HeaderInfoDto rh) throws WrapException {
				SupportBankAndProvinceDto bankList = new SupportBankAndProvinceDto();

				String gsonString = GsonUtils.toJson(body);
				Object[] objsObjects = GsonUtils.toObj(gsonString, Object[].class);
				Map<String, SupportBankDto> bank = GsonUtils.toObj(
						GsonUtils.toJson(objsObjects[0]),
						new TypeToken<Map<String, SupportBankDto>>() {
						}.getType());
				Map<String, ProvinceInfoDto> provice = GsonUtils.toObj(
						GsonUtils.toJson(objsObjects[1]),
						new TypeToken<Map<String, ProvinceInfoDto>>() {
						}.getType());
				bankList.setsBankDto(bank);
				bankList.setsProvince(provice);
				return bankList;
			}

			@Override
			public void HandFailed(HeaderInfoDto rh, ReqNetOpt opt) throws WrapException {

			}
		});
		return tb;
	}

	public static TradeBuilder newBankBranchList(String tokenId, String bankCode, String provCode,
			String pageSize, String pageNo, String bankName) {
		TradeBuilder tb = new TradeBuilder(urlGetBankBranchList, false, "tokenId", tokenId,
				"bankCode", bankCode, "provCode", provCode, "pageSize", pageSize, "pageNo", pageNo,
				"bankName", bankName);
		tb.mParTrade.setTradeHandler(new ITradeHandler() {
			@Override
			public Object handSuccess(Object body, HeaderInfoDto rh) throws WrapException {
				SupportBankBranchListDto sbbl = new SupportBankBranchListDto();
				String gsonString = GsonUtils.toJson(body);
				SupportBankBranchDto[] s = GsonUtils.toObj(gsonString,
						new TypeToken<SupportBankBranchDto[]>() {
						}.getType());
				ArrayList<SupportBankBranchDto> q = new ArrayList<SupportBankBranchDto>();
				for (SupportBankBranchDto supportBankBranchDto : s) {
					q.add(supportBankBranchDto);
				}
				sbbl.setSupportBankBranchDtos(q);
				return sbbl;
			}

			@Override
			public void HandFailed(HeaderInfoDto rh, ReqNetOpt opt) throws WrapException {

			}
		});
		return tb;
	}

	public static TradeBuilder newRegister(String tokenId, String custName, String idNo,
			String mobile, String loginPwd, String tradePwd) {
		TradeBuilder tb = new TradeBuilder(urlRegister, true, "tokenId", tokenId, "custName",
				custName, "idNo", idNo, "mobile", mobile, "loginPwd", loginPwd, "tradePwd",
				tradePwd);
		return tb;
	}

	public static TradeBuilder newLoginer(String tokenId, String idNo, String password) {
		TradeBuilder tb = new TradeBuilder(urlLogin, true, "tokenId", tokenId, "idNo", idNo,
				"password", password);
		return tb;
	}

	public static TradeBuilder newBindCard(String tokenId, String custNo, String accountNo,
			String bankCode, String subBankCode, String subBankName, String provinceNo) {
		TradeBuilder tb = new TradeBuilder(urlBindCard, true, "tokenId", tokenId, "custNo", custNo,
				"accountNo", accountNo, "bankCode", bankCode, "subBankCode", subBankCode,
				"subBankName", subBankName, "provinceNo", provinceNo);
		tb.mParTrade.setTradeHandler(new ITradeHandler() {
			@Override
			public Object handSuccess(Object body, HeaderInfoDto rh) throws WrapException {
				BindCardDto bindCardDto = new BindCardDto();
				String gsonString = GsonUtils.toJson(body);
				bindCardDto.setmBindCardInf(GsonUtils.toObj(gsonString, BindCardInf.class));
				return bindCardDto;
			}

			@Override
			public void HandFailed(HeaderInfoDto rh, ReqNetOpt opt) throws WrapException {
			}
		});
		return tb;
	}

	public static TradeBuilder newUserInf(String tokenId, String custNo) {
		TradeBuilder tb = new TradeBuilder(urlGetUserInfo, true, "tokenId", tokenId, "custNo",
				custNo);
		tb.mParTrade.setTradeHandler(new ITradeHandler() {
			@Override
			public Object handSuccess(Object body, HeaderInfoDto rh) throws WrapException {
				UserInfoDto oneStringDto = new UserInfoDto();
				String gsonString = GsonUtils.toJson(body);
				oneStringDto = GsonUtils.toObj(gsonString, UserInfoDto.class);
				return oneStringDto;
			}

			@Override
			public void HandFailed(HeaderInfoDto rh, ReqNetOpt opt) throws WrapException {

			}
		});
		return tb;
	}

	public static TradeBuilder newProductInf(String tokenId) {
		TradeBuilder tb = new TradeBuilder(urlGetProductInfo, false, "tokenId", tokenId);
		tb.mParTrade.setTradeHandler(new ITradeHandler() {
			@Override
			public Object handSuccess(Object body, HeaderInfoDto rh) throws WrapException {
				ProductInfo oneStringDto = new ProductInfo();
				String gsonString = GsonUtils.toJson(body);
				oneStringDto = GsonUtils.toObj(gsonString, ProductInfo.class);
				return oneStringDto;
			}

			@Override
			public void HandFailed(HeaderInfoDto rh, ReqNetOpt opt) throws WrapException {

			}
		});
		return tb;
	}

	public static TradeBuilder newAssetsInfo(String tokenId, String custNo) {
		TradeBuilder tb = new TradeBuilder(urlGetAssetsInfo, true, "tokenId", tokenId, "custNo",
				custNo);
		tb.mParTrade.setTradeHandler(new ITradeHandler() {
			@Override
			public Object handSuccess(Object body, HeaderInfoDto rh) throws WrapException {
				AccessInfo oneStringDto = new AccessInfo();
				String gsonString = GsonUtils.toJson(body);
				oneStringDto = GsonUtils.toObj(gsonString, AccessInfo.class);
				return oneStringDto;
			}

			@Override
			public void HandFailed(HeaderInfoDto rh, ReqNetOpt opt) throws WrapException {

			}
		});
		return tb;
	}

	public static TradeBuilder newResetPasswordVeri(String tokenId, String custNo, String custName,
			String idNo, String idType, String dynaPwd, String pwdType, String mobile) {
		TradeBuilder tb = new TradeBuilder(urlPwdResetVal, true, "tokenId", tokenId, "custNo",
				custNo, "pwdType", pwdType, "dynaPwd", dynaPwd, "idNo", idNo, "idType", idType,
				"mobile", mobile, "custName", custName);
		return tb;
	}

	public static TradeBuilder newResetPasswordCommit(String tokenId, String custNo, String idNo,
			String idType, String password, String pwdType) {
		TradeBuilder tb = new TradeBuilder(urlPwdResetCommit, true, "tokenId", tokenId, "custNo",
				custNo, "password", password, "pwdType", pwdType, "idNo", idNo, "idType", idType);
		return tb;
	}

	public static TradeBuilder newModifyMobile(String tokenId, String custNo, String mobile,
			String dynaPwd, String txPassword) {
		TradeBuilder tb = new TradeBuilder(urlUpdateMobile, true, "tokenId", tokenId, "custNo",
				custNo, "mobile", mobile, "dynaPwd", dynaPwd, "txPassword", txPassword);
		return tb;
	}

	public static TradeBuilder newDeposit(String tokenId, String custNo, String fundCode,
			String purchaseMoney, String txpwd, String bankId, String bankFlag) {
		TradeBuilder tb = new TradeBuilder(urlDeposit, true, "tokenId", tokenId, "custNo", custNo,
				"fundCode", fundCode, "purchaseMoney", purchaseMoney, "txpwd", txpwd, "isHbsign",
				bankFlag, "bankAcct", bankId);
		return tb;
	}

	public static TradeBuilder newDepositlnterval(String tokenId, String custNo, String fundCode,
			String purchaseMoney, String txpwd, String delayFlag, String savingPlanSubsAppDt) {
		TradeBuilder tb = new TradeBuilder(urlntervalDeposit, true, "tokenId", tokenId, "custNo",
				custNo, "fundCode", fundCode, "purchaseMoney", purchaseMoney, "txpwd", txpwd,
				"delayFlag", delayFlag, "savingPlanSubsAppDt", savingPlanSubsAppDt);
		return tb;
	}

	public static TradeBuilder newDraw(String tokenId, String custNo, String fundCode,
			String redeemShare, String allRedeemFlag, String txpwd, String protocalNo) {
		TradeBuilder tb = new TradeBuilder(urlQuQian, true, "tokenId", tokenId, "custNo", custNo,
				"fundCode", fundCode, "redeemShare", redeemShare, "allRedeemFlag", allRedeemFlag,
				"protocalNo", protocalNo, "txpwd", txpwd);
		return tb;
	}

	public static TradeBuilder newTradeHistory(String tokenId, String custNo, String pageNo,
			String pageSize) {
		TradeBuilder tb = new TradeBuilder(urlHistory, true, "tokenId", tokenId, "custNo", custNo,
				"pageNo", pageNo, "pageSize", pageSize);
		tb.mParTrade.setTradeHandler(new ITradeHandler() {
			@Override
			public Object handSuccess(Object body, HeaderInfoDto rh) throws WrapException {
				TradeQueryDto oneStringDto = new TradeQueryDto();
				String gsonString = GsonUtils.toJson(body);
				Object[] objsObjects = GsonUtils.toObj(gsonString, Object[].class);
				int count = GsonUtils.toObj(GsonUtils.toJson(objsObjects[0]), Integer.class);
				TradeQueryItemDto[] tradeQuerys = GsonUtils.toObj(GsonUtils.toJson(objsObjects[1]),
						TradeQueryItemDto[].class);
				ArrayList<TradeQueryItemDto> list = new ArrayList<TradeQueryItemDto>();
				for (TradeQueryItemDto tradeQueryItemDto : tradeQuerys) {
					list.add(tradeQueryItemDto);
				}
				oneStringDto.setCountItem(count);
				oneStringDto.setShuz(list);
				return oneStringDto;
			}

			@Override
			public void HandFailed(HeaderInfoDto rh, ReqNetOpt opt) throws WrapException {
			}
		});
		return tb;
	}

	public static TradeBuilder newIncome(String tokenId, String custNo, String pageNo,
			String pageSize, String protocalNo, String fundCode) {
		TradeBuilder tb = new TradeBuilder(urlShouyi, true, "tokenId", tokenId, "custNo", custNo,
				"pageSize", pageSize, "pageNo", pageNo, "fundCode", fundCode, "protocalNo",
				protocalNo);
		tb.mParTrade.setTradeHandler(new ITradeHandler() {
			@Override
			public Object handSuccess(Object body, HeaderInfoDto rh) throws WrapException {
				IncomeDto oneStringDto = new IncomeDto();
				String gsonString = GsonUtils.toJson(body);
				oneStringDto = GsonUtils.toObj(gsonString, IncomeDto.class);
				return oneStringDto;
			}

			@Override
			public void HandFailed(HeaderInfoDto rh, ReqNetOpt opt) throws WrapException {

			}
		});
		return tb;
	}

	public static TradeBuilder newTiRi(String tokenId, String fundCode) {
		TradeBuilder tb = new TradeBuilder(urlTRi, true, "tokenId", tokenId, "fundCode", fundCode);
		tb.mParTrade.setTradeHandler(new ITradeHandler() {
			@Override
			public Object handSuccess(Object body, HeaderInfoDto rh) throws WrapException {
				TradeDate oneStringDto = new TradeDate();
				String gsonString = GsonUtils.toJson(body);
				oneStringDto = GsonUtils.toObj(gsonString, TradeDate.class);
				return oneStringDto;
			}

			@Override
			public void HandFailed(HeaderInfoDto rh, ReqNetOpt opt) throws WrapException {

			}
		});
		return tb;
	}

	public static TradeBuilder newActiveQuery(String tokenId, String idNo, String idType) {
		TradeBuilder tb = new TradeBuilder(urlActiviteAccountQuery, true, "tokenId", tokenId,
				"idNo", idNo, "idType", idType);
		tb.mParTrade.setTradeHandler(new ITradeHandler() {
			@Override
			public Object handSuccess(Object body, HeaderInfoDto rh) throws WrapException {
				UserTypeDto oneStringDto = new UserTypeDto();
				String gsonString = GsonUtils.toJson(body);
				oneStringDto = GsonUtils.toObj(gsonString, UserTypeDto.class);
				return oneStringDto;
			}

			@Override
			public void HandFailed(HeaderInfoDto rh, ReqNetOpt opt) throws WrapException {

			}
		});
		return tb;
	}

	public static TradeBuilder newActiveSubmit(String tokenId, String idNo, String idType,
			String mobile, String loginPasswd, String txPassword, String selfMsg) {
		TradeBuilder tb = new TradeBuilder(urlActiveSubmit, true, "tokenId", tokenId, "idNo", idNo,
				"idType", idType, "mobile", mobile, "loginPasswd", loginPasswd, "txPassword",
				txPassword, "selfMsg", selfMsg);
		return tb;
	}

	public static TradeBuilder newTradLimit(String tokenId, String custNo) {
		TradeBuilder tb = new TradeBuilder(urlCusLimit, true, "tokenId", tokenId, "custNo", custNo);
		tb.mParTrade.setTradeHandler(new ITradeHandler() {
			@Override
			public Object handSuccess(Object body, HeaderInfoDto rh) throws WrapException {
				UserLimitDto oneStringDto = new UserLimitDto();
				String gsonString = GsonUtils.toJson(body);
				oneStringDto = GsonUtils.toObj(gsonString, UserLimitDto.class);
				return oneStringDto;
			}

			@Override
			public void HandFailed(HeaderInfoDto rh, ReqNetOpt opt) throws WrapException {
			}
		});
		return tb;
	}

	public static TradeBuilder newEstimateIncome(String tokenId, String custNo, String dayCount) {
		TradeBuilder tb = new TradeBuilder(urlGetSms, true, "tokenId", tokenId, "custNo", custNo,
				"dayCount", dayCount);
		tb.mParTrade.setTradeHandler(new ITradeHandler() {
			@Override
			public Object handSuccess(Object body, HeaderInfoDto rh) throws WrapException {
				EstimatesIncomeList oneStringDto = new EstimatesIncomeList();
				String gsonString = GsonUtils.toJson(body);
				ArrayList<EstimatesIncomeItem> list = GsonUtils.toObj(gsonString,
						new TypeToken<ArrayList<EstimatesIncomeItem>>() {
						}.getType());
				oneStringDto.setmList(list);
				return oneStringDto;
			}

			@Override
			public void HandFailed(HeaderInfoDto rh, ReqNetOpt opt) throws WrapException {

			}
		});
		return tb;
	}

	public static TradeBuilder newCardLimit(String tokenId, String custNo, String bankAcct,
			String bankCode) {
		TradeBuilder tb = new TradeBuilder(urlCusLimit, true, "tokenId", tokenId, "custNo", custNo,
				"bankAcct", bankAcct, "bankCode", bankCode);
		tb.mParTrade.setTradeHandler(new ITradeHandler() {
			@Override
			public Object handSuccess(Object body, HeaderInfoDto rh) throws WrapException {
				FundLimitDto oneStringDto = new FundLimitDto();
				String gsonString = GsonUtils.toJson(body);
				oneStringDto = GsonUtils.toObj(gsonString, FundLimitDto.class);
				return oneStringDto;
			}

			@Override
			public void HandFailed(HeaderInfoDto rh, ReqNetOpt opt) throws WrapException {
			}
		});
		return tb;
	}

	public static TradeBuilder newNotifyQuery(String tokenId, String custNo, String bankAcct,
			String bankCode, String tipCategory) {
		TradeBuilder tb = new TradeBuilder(urlGetSms, true, "tokenId", tokenId, "custNo", custNo,
				"bankAcct", bankAcct, "bankCode", bankCode, "tipCategory", tipCategory);
		tb.mParTrade.setTradeHandler(new ITradeHandler() {
			@Override
			public Object handSuccess(Object body, HeaderInfoDto rh) throws WrapException {
				NoticeDtoList oneStringDto = new NoticeDtoList();
				String gsonString = GsonUtils.toJson(body);
				ArrayList<NoticeDto> list = GsonUtils.toObj(gsonString,
						new TypeToken<ArrayList<NoticeDto>>() {
						}.getType());
				oneStringDto.setListNotice(list);
				return oneStringDto;
			}

			@Override
			public void HandFailed(HeaderInfoDto rh, ReqNetOpt opt) throws WrapException {

			}
		});
		return tb;
	}

	public static TradeBuilder newWebRequest(String tokenId, String custNo, String reqId,
			Map<String, String> reqParams) {
		TradeBuilder tb = new TradeBuilder(urlGetSms, true, reqParams, "tokenId", tokenId,
				"custNo", custNo, "id", reqId);
		tb.mParTrade.setTradeHandler(new ITradeHandler() {
			@Override
			public Object handSuccess(Object body, HeaderInfoDto rh) throws WrapException {
				return null;
			}

			@Override
			public void HandFailed(HeaderInfoDto rh, ReqNetOpt opt) throws WrapException {

			}
		});
		return tb;
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	public static TradeBuilder newXX(String tokenId) {
		TradeBuilder tb = new TradeBuilder(urlGetSms, true, "tokenId", tokenId);
		tb.mParTrade.setTradeHandler(new ITradeHandler() {
			@Override
			public Object handSuccess(Object body, HeaderInfoDto rh) throws WrapException {
				return null;
			}

			@Override
			public void HandFailed(HeaderInfoDto rh, ReqNetOpt opt) throws WrapException {

			}
		});
		return tb;
	}
}
