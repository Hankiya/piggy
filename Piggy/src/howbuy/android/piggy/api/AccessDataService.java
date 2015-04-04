package howbuy.android.piggy.api;

import howbuy.android.piggy.api.dto.ResponseClient;
import howbuy.android.piggy.api.dto.ResponseContentDto;
import howbuy.android.piggy.api.dto.SercurityInfoDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.error.HowbuyException;
import howbuy.android.piggy.help.CacheHelp;
import howbuy.android.piggy.jni.DesUtilForNetParam;
import howbuy.android.util.http.UrlConnectionUtil;
import howbuy.android.util.http.UrlMatchUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import android.text.TextUtils;
import android.util.Log;

import com.howbuy.wireless.entity.protobuf.AdvertListProtos.AdvertList;
import com.howbuy.wireless.entity.protobuf.HostDistributionProtos.HostDistribution;

/**
 * 获取网络数据
 * 
 * @author yescpu
 * 
 */
public class AccessDataService {
	public static final String TAG = "impl";


	static {
		System.loadLibrary("desrsa");
	}

	static Map<String, String> getScrateParm() {
		Map<String, String> scrateParm=new HashMap<String, String>();
		scrateParm.put("requestId", String.valueOf(ApplicationParams.getInstance().getSumRequest()));
		return scrateParm;
	}

	/**
	 * 
	 * @author yescpu
	 * 
	 */
	public final static class AccessUrl {
		public static final String urlGetCusInfo = "piggy/custInfo.htm"; // 用户信息
		public static final String urlGetProductInfo = "piggy/productInfo.htm"; // 产品信息
		public static final String urlTRi = "piggy/acckdate.htm";// t日查询
		public static final String urlHistory = "piggy/tradehistory.htm";// 交易查询
		public static final String urlCusLimit = "piggy/quickRedeemLimit.htm";// 快速赎回限额
		public static final String urlQueryEstimatesIncome = "piggy/pgyBnkIncome.htm";// 查询用户每日估算收益(正在使用)
		public static final String urlDeposit = "piggy/purchase.htm"; // 单笔存款
		public static final String urlGetUserCardVol = "piggy/custVol.htm";//用户份额信息
		public static final String urlGetBindBankCards = "piggy/queryBindBankCards.htm"; // 获取绑卡信息
		public static final String urlQuQian = "piggy/redeem.htm";// 取钱
		
		
		public static final String urlShouyi = "query/pgyBnkIncome.htm";// 收益查询
		public static final String urlGetRsaSecret = "key/getsercuritykey.htm"; // 获取rsa私钥
		public static final String urlGetSms = "common/getVerifyCode.htm"; // 获取验证码
		public static final String urlSmsVerCommit = "common/checkVerifyCode.htm"; // 验证码提交
		public static final String urlGetBankList = "query/supportBank.htm"; // 获取支行列表
		public static final String urlGetBankBranchList = "query/branchbanks.htm"; // 获取分行列表
		public static final String urlRegister = "account/register.htm"; // 注册
		public static final String urlLogin = "account/login.htm"; // 登录
		public static final String urlBindCard = "account/bindcard.htm"; // 绑卡
		public static final String urlGetAssetsInfo = "cust/assetsInfo.htm"; // 资产信息
		public static final String urlPwdResetVal = "cust/custInfoValidate.htm"; // 用户修改密码验证
		public static final String urlPwdResetCommit = "cust/pwdReset.htm"; // 用户修改密码提交
		public static final String urlUpdateMobile = "cust/custInfoModify.htm"; // 用户手机号修改
		public static final String urlntervalDeposit = "trade/intervalDeposit.htm"; // 定存
		public static final String urlActiviteAccountQuery = "account/activeFind.htm";// 激活查询
		public static final String urlActiveSubmit = "account/activeSubmit.htm";// 激活提交
		public static final String urlUpdate = "start/checkupdate.protobuf";// 检查更新
		public static final String urlQueryQrnh = "query/fundNav.htm";// 查询七日年化
		public static final String urlAd = "advert/advertlist.protobuf";// 广告
		public static final String urlCustBankLimit= "query/queryCustFundLimit.htm";// 查询银行卡限额
		public static final String urlNotice = "query/notice.htm";// 查询通知类型
		public static final String urlWebViewRequest = "activity/waprequest.htm";// 查询通知类型
		public static final String urlUnBindCard = "account/unbindcard.htm";// 8.	银行卡解绑
		public static final String urlModifyBank  = "account/chgSubBank.htm";// 6.	联行号设置

//		public static final String urlQueryBankAuth = "account/queryBankAuth.htm";		// 获取银行卡鉴权方式
		public static final String urlBankAuth = "account/bankAuth.htm";		// 银行卡鉴权
	}

	/**
	 * 塞入可选参数
	 * 
	 * @param map
	 * @param pramName
	 * @param pram
	 */
	public static void addPram(Map<String, String> map, String pramName, String pram) {
		if (!TextUtils.isEmpty(pram)) {
			map.put(pramName, pram);
		}
	}

	/**
	 * 获取Get请求
	 * @param path
	 * @param cacheTime
	 * @return
	 * @throws HowbuyException
	 */
	public static InputStream doGetHbFund(String basePath,String doUrl, long cacheTime) throws HowbuyException {
		String path=basePath;
		if (doUrl!=null) {
			path+=doUrl;
		}
		Log.i(TAG, path);
		InputStream inputStream = CacheHelp.hasCacheInTime(path, cacheTime);
		if (inputStream == null) {
			inputStream = UrlConnectionUtil.getInstance().executeGet(path);
			if (inputStream == null) {
				return null;
			} else {
				inputStream= CacheHelp.saveCacheFile(path, inputStream);
			}
		}
		return inputStream;
	}
	
	
	/**
	 * 非加密处理
	 * 
	 * @param doUrl
	 * @param paraMap
	 * @param baseUrl
	 *            TODO
	 * @param cacheTime
	 *            TODO
	 * @return
	 * @throws HowbuyException
	 */
	private static ResponseContentDto doGetNormal(String doUrl, Map<String, String> paraMap, String baseUrl, long cacheTime) throws HowbuyException {
		String path = UrlMatchUtil.urlUtilOnPublicP(baseUrl, doUrl, paraMap, ApplicationParams.getInstance());
		Log.i(TAG, path);
		InputStream inputStream = CacheHelp.hasCacheInTime(path, cacheTime);
		if (inputStream == null) {
			inputStream = UrlConnectionUtil.getInstance().executeGet(path);
			if (inputStream == null) {
				return null;
			} else {
				inputStream= CacheHelp.saveCacheFile(path, inputStream);
			}
		}
		ResponseContentDto resCnt = null;
		ResponseClient responseClient = new ResponseClient(inputStream);
		resCnt = responseClient.getResponseContentDto();
		Log.i("serverdata", "url--" + doUrl + "--value--" + responseClient);
		return resCnt;
	}

	/**
	 * 加密Get请求网络
	 * 
	 * @param doUrl
	 * @param paraMap
	 * @param baseUrl
	 *            TODO
	 * @param cacheTime
	 *            TODO
	 * @return
	 * @throws HowbuyException
	 */
	private static synchronized ResponseContentDto doGetSecurity(String doUrl, Map<String, String> paraMap, String baseUrl, long cacheTime) throws HowbuyException {
		ResponseContentDto responseContentDto;
		
		while (ResetRsaKey.rsaKeyRuningFlag) {
//			Log.d(TAG, "doGetSecurity...");
		}
		
		String path = UrlMatchUtil.urlUtilUnPublicP(baseUrl, doUrl, paraMap);
		Log.i(TAG, path);
		InputStream inputStream = CacheHelp.hasCacheInTime(path, cacheTime);
		if (inputStream == null) {
			inputStream = UrlConnectionUtil.getInstance().executeGet(path);
			if (inputStream == null) {
				return null;
			} else {
				inputStream= CacheHelp.saveCacheFile(path, inputStream);
			}
		}
		ResponseClient reClient = new ResponseClient(inputStream);
		// 明文 直接返回
		if (reClient.isSercurity() == false) {
			responseContentDto = reClient.getResponseContentDto();
			Log.i("serverdata", "url--" + doUrl + "--value--" + responseContentDto+"\r\n"+paraMap);
			return responseContentDto;
		}

		// 密文未解
		SercurityInfoDto sDto = reClient.getSercurityInfoDto();
		if (sDto == null) {
			return null;
		}

		// 解密文
		String resJson = DesUtilForNetParam.dencryptParam(sDto);
		if (TextUtils.isEmpty(resJson)) {
			return null;
		}
		Log.i("serverdata", "url--" + doUrl + "--value--" + resJson+"\r\n"+paraMap);
		responseContentDto = PeripheryJson.resolveExpressly(resJson);

		return responseContentDto;
	}

	/**
	 * 获取rsa私钥
	 * 
	 * @return
	 * @throws HowbuyException
	 */
	public static synchronized ResponseContentDto getRsaSecret(String tokenId) throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		// reset requestId
		ApplicationParams.getInstance().setSumRequest(0);
		ResponseContentDto resDto=doGetNormal(AccessUrl.urlGetRsaSecret, paraMap, UrlMatchUtil.getBasepath(), 10*1000);
		return resDto;
	}

	/**
	 * 发送短信
	 * 
	 * @param tokenId
	 * @param phoneNum
	 * @return
	 * @throws HowbuyException
	 */
	public static ResponseContentDto smsSend(String tokenId, String phoneNum,String type) throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		Map<String, String> sparaMap = getScrateParm();
		addPram(sparaMap, "mobile", phoneNum);
		addPram(sparaMap, "type", type);
		String encMsgBase64 = DesUtilForNetParam.encryptParam(sparaMap);
		String signMsgBase64 = DesUtilForNetParam.encryptMd5(sparaMap);
		addPram(paraMap, "encMsg", encMsgBase64);
		addPram(paraMap, "signMsg", signMsgBase64);
		return doGetSecurity(AccessUrl.urlGetSms, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);
	}

	/**
	 * 验证短信
	 * 
	 * @param tokenId
	 * @param phoneNum
	 * @return
	 * @throws HowbuyException
	 */
	public static ResponseContentDto smsVeri(String tokenId, String dynaPwd, String mobile) throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		Map<String, String> sparaMap = getScrateParm();
		addPram(sparaMap, "mobile", mobile);
		addPram(sparaMap, "verifyCode", dynaPwd);
		String encMsgBase64 = DesUtilForNetParam.encryptParam(sparaMap);
		String signMsgBase64 = DesUtilForNetParam.encryptMd5(sparaMap);
		addPram(paraMap, "encMsg", encMsgBase64);
		addPram(paraMap, "signMsg", signMsgBase64);
		return doGetSecurity(AccessUrl.urlSmsVerCommit, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);
	}

	/**
	 * 获取开户行列表
	 * 
	 * @param jsonList
	 *            支持的支付通道字符串采用Json格式组装
	 * @return
	 * @throws HowbuyException
	 */
	// update by renzh add jsonList for china pay.
	public static ResponseContentDto getBankList(String tokenId, String jsonList) throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		addPram(paraMap, "supportPayChannel", jsonList);

		return doGetNormal(AccessUrl.urlGetBankList, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);
	}

	/**
	 * 获取支行列表
	 * 
	 * @param tokenId
	 * @param bankCode
	 * @param provCode
	 * @param pageSize
	 * @param pageNo
	 * @param bankName
	 * @return
	 * @throws HowbuyException
	 */
	public static ResponseContentDto getBankBranchList(String tokenId, String bankCode, String provCode, String pageSize, String pageNo, String bankName) throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		addPram(paraMap, "bankCode", bankCode);
		// addPram(paraMap, "cityCode", cityCode);
		addPram(paraMap, "provCode", provCode);
		addPram(paraMap, "pageSize", pageSize);
		addPram(paraMap, "pageNo", pageNo);
		addPram(paraMap, "bankName", bankName);
		return doGetNormal(AccessUrl.urlGetBankBranchList, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);

	}

	/**
	 * 注册参数提交
	 * 唐立兴
	 * @param tokenId
	 * @param custName
	 * @param idNo
	 * @param mobile
	 * @param loginPwd
	 * @param tradePwd
	 * @return
	 * @throws HowbuyException
	 */
	public static ResponseContentDto commitRegistert(String tokenId, String custName, String idNo, String mobile, String loginPwd, String tradePwd,String verifyCode) throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		Map<String, String> sparaMap = getScrateParm();
		addPram(sparaMap, "custName", custName);
		addPram(sparaMap, "idNo", idNo);
		addPram(sparaMap, "mobile", mobile);
		addPram(sparaMap, "loginPwd", loginPwd);
		addPram(sparaMap, "txPwd", tradePwd);
		addPram(sparaMap, "verifyCode", verifyCode);
		String encMsgBase64 = DesUtilForNetParam.encryptParam(sparaMap);
		String signMsgBase64 = DesUtilForNetParam.encryptMd5(sparaMap);
		addPram(paraMap, "encMsg", encMsgBase64);
		addPram(paraMap, "signMsg", signMsgBase64);
		return doGetSecurity(AccessUrl.urlRegister, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);
	}

	/**
	 * 登录提交
	 * 
	 * @param tokenId
	 * @param idNo
	 * @param password
	 * @return
	 * @throws HowbuyException
	 */
	public static ResponseContentDto commitLogin(String tokenId, String idNo, String password) throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		Map<String, String> sparaMap = getScrateParm();
		addPram(sparaMap, "loginPwd", password);
		addPram(sparaMap, "idNo", idNo);
		addPram(sparaMap, "idType", "0");
		String encMsgBase64 = DesUtilForNetParam.encryptParam(sparaMap);
		String signMsgBase64 = DesUtilForNetParam.encryptMd5(sparaMap);
		addPram(paraMap, "encMsg", encMsgBase64);
		addPram(paraMap, "signMsg", signMsgBase64);
		return doGetSecurity(AccessUrl.urlLogin, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);
	}

	/**
	 * 绑卡
	 * @param tokenId
	 * @param custNo
	 * @param bankAcct
	 * @param bankCode
	 * @param cnapsNo
	 * @param provCode
	 * @param custBankId
	 * @return
	 * @throws HowbuyException
	 */
    public static ResponseContentDto bindCard(String tokenId, String custNo, String bankAcct, String bankCode, String cnapsNo,String provCode, String custBankId,String supportPayChannel)
            throws HowbuyException {
        Map<String, String> paraMap = new HashMap<String, String>();
        addPram(paraMap, "tokenId", tokenId);
        Map<String, String> sparaMap = getScrateParm();
        addPram(sparaMap, "custNo", custNo);
        addPram(sparaMap, "bankAcct", bankAcct);
        addPram(sparaMap, "bankCode", bankCode);
        addPram(sparaMap, "cnapsNo", cnapsNo);
        addPram(sparaMap, "provCode", provCode);
        addPram(sparaMap, "custBankId", custBankId);
        addPram(sparaMap, "supportPayChannel", supportPayChannel);
        String encMsgBase64 = DesUtilForNetParam.encryptParam(sparaMap);
        String signMsgBase64 = DesUtilForNetParam.encryptMd5(sparaMap);
        addPram(paraMap, "encMsg", encMsgBase64);
        addPram(paraMap, "signMsg", signMsgBase64);
        return doGetSecurity(AccessUrl.urlBindCard, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);

    }
    
    
    /**
     * 鉴权
     * @param tokenId
     * @param bankCode
     * @param custNo
     * @param custBankId
     * @param supportPayChannel
     * @return
     * @throws HowbuyException
     */
	public static ResponseContentDto bankAuth(String tokenId, String custNo, String bankCode, String custBankId, String supportPayChannel) throws HowbuyException{
		Map<String, String> paraMap = new HashMap<String, String>();
        addPram(paraMap, "tokenId", tokenId);
        Map<String, String> sparaMap = getScrateParm();
        addPram(sparaMap, "custNo", custNo);
        addPram(sparaMap, "bankCode", bankCode);
        addPram(sparaMap, "custBankId", custBankId);
        addPram(sparaMap, "supportPayChannel", supportPayChannel);
        String encMsgBase64 = DesUtilForNetParam.encryptParam(sparaMap);
        String signMsgBase64 = DesUtilForNetParam.encryptMd5(sparaMap);
        addPram(paraMap, "encMsg", encMsgBase64);
        addPram(paraMap, "signMsg", signMsgBase64);
        return doGetSecurity(AccessUrl.urlBankAuth, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);
	}


    public static ResponseContentDto bindCardModify(String tokenId, String custNo, String cnapsNo, String custBankId)
            throws HowbuyException {
        Map<String, String> paraMap = new HashMap<String, String>();
        addPram(paraMap, "tokenId", tokenId);
        Map<String, String> sparaMap = getScrateParm();
        addPram(sparaMap, "custNo", custNo);
        addPram(sparaMap, "cnapsNo", cnapsNo);
        addPram(sparaMap, "custBankId", custBankId);
        String encMsgBase64 = DesUtilForNetParam.encryptParam(sparaMap);
        String signMsgBase64 = DesUtilForNetParam.encryptMd5(sparaMap);
        addPram(paraMap, "encMsg", encMsgBase64);
        addPram(paraMap, "signMsg", signMsgBase64);
        return doGetSecurity(AccessUrl.urlModifyBank, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);

    }


    public static ResponseContentDto unbindCard(String tokenId, String custNo, String custBankId, String txPasswd,String bankAcct) throws HowbuyException {
        Map<String, String> paraMap = new HashMap<String, String>();
        addPram(paraMap, "tokenId", tokenId);
        Map<String, String> sparaMap = getScrateParm();
        addPram(sparaMap, "custNo", custNo);
        addPram(sparaMap, "custBankId", custBankId);
        addPram(sparaMap, "txPasswd", txPasswd);
        addPram(sparaMap, "bankAcct", bankAcct);
        String encMsgBase64 = DesUtilForNetParam.encryptParam(sparaMap);
        String signMsgBase64 = DesUtilForNetParam.encryptMd5(sparaMap);
        addPram(paraMap, "encMsg", encMsgBase64);
        addPram(paraMap, "signMsg", signMsgBase64);
        return doGetSecurity(AccessUrl.urlUnBindCard, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);
    }


	/**
	 * 获取用户信息
	 * 
	 * @param tokenId
	 * @param custNo
	 * @return
	 * @throws HowbuyException
	 */
	// {"body":{"acctIdentifyStat":"1","bankAcct":"6227001215800062891","bankAcctVrfyStat":"2","bankName":"中国建设银行","custName":"name"},"header":{"backCallFlag":"0","backUrl":"","bizCode":"","responseCode":"0000","responseDesc":"成功","responseTime":"2013-09-27 15:41:24","source":"howbuy","version":"1.0"}}
	public static ResponseContentDto getUserInfo(String tokenId, String custNo) throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		Map<String, String> sparaMap = getScrateParm();
		addPram(sparaMap, "custNo", custNo);
		
		Log.d(TAG, "Base--"+sparaMap);
		String encMsgBase64 = DesUtilForNetParam.encryptParam(sparaMap);
		String signMsgBase64 = DesUtilForNetParam.encryptMd5(sparaMap);
		addPram(paraMap, "encMsg", encMsgBase64);
		addPram(paraMap, "signMsg", signMsgBase64);
		return doGetSecurity(AccessUrl.urlGetCusInfo, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);
	}
	
	/**
	 * 获取用户银行卡信息
	 * 
	 * @param tokenId
	 * @param custNo
	 * @return
	 * @throws HowbuyException
	 */
	public static ResponseContentDto getUserCardList(String tokenId, String custNo, String cardNo) throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		Map<String, String> sparaMap = getScrateParm();
		addPram(sparaMap, "custNo", custNo);
		addPram(sparaMap, "bankAcct", cardNo);
		Log.d(TAG, "Base--"+sparaMap);
		String encMsgBase64 = DesUtilForNetParam.encryptParam(sparaMap);
		String signMsgBase64 = DesUtilForNetParam.encryptMd5(sparaMap);
		addPram(paraMap, "encMsg", encMsgBase64);
		addPram(paraMap, "signMsg", signMsgBase64);
		return doGetSecurity(AccessUrl.urlGetBindBankCards, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);
	}
	
	
	/**
	 * 获取用户银行卡以及份额
	 * 
	 * @param tokenId
	 * @param custNo
	 * @return
	 * @throws HowbuyException
	 */
	public static ResponseContentDto getUserCardIncomeList(String tokenId, String custNo) throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		Map<String, String> sparaMap = getScrateParm();
		addPram(sparaMap, "custNo", custNo);
		String encMsgBase64 = DesUtilForNetParam.encryptParam(sparaMap);
		String signMsgBase64 = DesUtilForNetParam.encryptMd5(sparaMap);
		addPram(paraMap, "encMsg", encMsgBase64);
		addPram(paraMap, "signMsg", signMsgBase64);
		return doGetSecurity(AccessUrl.urlGetUserCardVol, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);
	}
	
	
	

	/**
	 * 获取产品信息
	 * 
	 * @param tokenId
	 * @return
	 * @throws HowbuyException
	 */
	// {"fundCode":"240001","lowestRedemption":1000.0,"minAcctVol":0.0,"oneYearBenefits":3.77,"sgbz":"1","shbz":"1","wfsy":0.0}
	public static ResponseContentDto getProductInfo(String tokenId) throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		return doGetNormal(AccessUrl.urlGetProductInfo, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);
	}

	/**
	 * 获取资产信息
	 * 
	 * @param tokenId
	 * @param pwdType
	 * @return
	 * @throws HowbuyException
	 */
	public static ResponseContentDto getAssetsInfo(String tokenId, String custNo) throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		Map<String, String> sparaMap = getScrateParm();
		addPram(sparaMap, "custNo", custNo);
		String encMsgBase64 = DesUtilForNetParam.encryptParam(sparaMap);
		String signMsgBase64 = DesUtilForNetParam.encryptMd5(sparaMap);
		addPram(paraMap, "encMsg", encMsgBase64);
		addPram(paraMap, "signMsg", signMsgBase64);
		return doGetSecurity(AccessUrl.urlGetAssetsInfo, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);
	}

	/**
	 * 用户密码重置验证
	 * 
	 * @param tokenId
	 * @param pwdType
	 *            0登录密码 1交易密码重置
	 * @return
	 * @throws HowbuyException
	 */
	public static ResponseContentDto passwordReset(String tokenId, String custNo, String custName, String idNo, String idType, String dynaPwd, String pwdType, String mobile)
			throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		Map<String, String> sparaMap = getScrateParm();
		addPram(sparaMap, "custNo", custNo);
		addPram(sparaMap, "pwdType", pwdType);
		addPram(sparaMap, "dynaPwd", dynaPwd);
		addPram(sparaMap, "idNo", idNo);
		addPram(sparaMap, "idType", idType);
		addPram(sparaMap, "mobile", mobile);
		addPram(sparaMap, "custName", custName);
		String encMsgBase64 = DesUtilForNetParam.encryptParam(sparaMap);
		String signMsgBase64 = DesUtilForNetParam.encryptMd5(sparaMap);
		addPram(paraMap, "encMsg", encMsgBase64);
		addPram(paraMap, "signMsg", signMsgBase64);
		return doGetSecurity(AccessUrl.urlPwdResetVal, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);
	}

	/**
	 * 用户密码重置提交
	 * 
	 * @param tokenId
	 * @param custNo
	 * @param password
	 * @return
	 * @throws HowbuyException
	 */
	public static ResponseContentDto passwordCommit(String tokenId, String custNo, String idNo, String idType, String password, String pwdType) throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		Map<String, String> sparaMap = getScrateParm();
		addPram(sparaMap, "custNo", custNo);
		addPram(sparaMap, "password", password);
		addPram(sparaMap, "pwdType", pwdType);
		addPram(sparaMap, "idNo", idNo);
		addPram(sparaMap, "idType", idType);
		String encMsgBase64 = DesUtilForNetParam.encryptParam(sparaMap);
		String signMsgBase64 = DesUtilForNetParam.encryptMd5(sparaMap);
		addPram(paraMap, "encMsg", encMsgBase64);
		addPram(paraMap, "signMsg", signMsgBase64);
		return doGetSecurity(AccessUrl.urlPwdResetCommit, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);
	}

	/**
	 * 用户手机号修改
	 * 
	 * @param tokenId
	 * @param custNo
	 * @param mobile
	 * @param dynaPwd
	 * @return
	 * @throws HowbuyException
	 */
	public static ResponseContentDto updateMobile(String tokenId, String custNo, String mobile, String dynaPwd, String txPassword) throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		Map<String, String> sparaMap = getScrateParm();
		addPram(sparaMap, "custNo", custNo);
		addPram(sparaMap, "mobile", mobile);
		addPram(sparaMap, "dynaPwd", dynaPwd);
		addPram(sparaMap, "txPassword", txPassword);
		String encMsgBase64 = DesUtilForNetParam.encryptParam(sparaMap);
		String signMsgBase64 = DesUtilForNetParam.encryptMd5(sparaMap);
		addPram(paraMap, "encMsg", encMsgBase64);
		addPram(paraMap, "signMsg", signMsgBase64);
		return doGetSecurity(AccessUrl.urlUpdateMobile, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);
	}

	/**
	 * 单笔
	 * 
	 * @param tokenId
	 * @param custNo
	 * @param fundCode
	 * @param purchaseMoney
	 * @param txpwd
	 * @return
	 * @throws HowbuyException
	 */
	public static ResponseContentDto saveMoney(String tokenId, String custNo,  String purchaseMoney, String txpwd, String bankId, String bankFlag)
			throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		Map<String, String> sparaMap = getScrateParm();
		addPram(sparaMap, "custNo", custNo);
		addPram(sparaMap, "purchaseMoney", purchaseMoney);
		addPram(sparaMap, "txPwd", txpwd);
		addPram(sparaMap, "isHbsign", bankFlag);
		addPram(sparaMap, "custBankId", bankId);
		String encMsgBase64 = DesUtilForNetParam.encryptParam(sparaMap);
		String signMsgBase64 = DesUtilForNetParam.encryptMd5(sparaMap);
		addPram(paraMap, "encMsg", encMsgBase64);
		addPram(paraMap, "signMsg", signMsgBase64);
		return doGetSecurity(AccessUrl.urlDeposit, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);
	}

	/**
	 * 定存
	 * 
	 * @param tokenId
	 * @param custNo
	 * @param fundCode
	 * @param purchaseMoney
	 * @param txpwd
	 * @param deadline
	 * @param delayFlag
	 * @param savingPlanSubsAppDt
	 * @return
	 * @throws HowbuyException
	 */
	public static ResponseContentDto saveMoneys(String tokenId, String custNo, String fundCode, String purchaseMoney, String txpwd, String delayFlag, String savingPlanSubsAppDt)
			throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		Map<String, String> sparaMap = getScrateParm();
		addPram(sparaMap, "custNo", custNo);
		addPram(sparaMap, "fundCode", fundCode);
		addPram(sparaMap, "purchaseMoney", purchaseMoney);
		addPram(sparaMap, "txpwd", txpwd);
		// addPram(sparaMap, "deadline", deadline);//
		addPram(sparaMap, "delayFlag", delayFlag);// 0/1扣款失败/顺延/不顺延
		addPram(sparaMap, "savingPlanSubsAppDt", savingPlanSubsAppDt);// 扣款日期0~28
		String encMsgBase64 = DesUtilForNetParam.encryptParam(sparaMap);
		String signMsgBase64 = DesUtilForNetParam.encryptMd5(sparaMap);
		addPram(paraMap, "encMsg", encMsgBase64);
		addPram(paraMap, "signMsg", signMsgBase64);
		return doGetSecurity(AccessUrl.urlntervalDeposit, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);
	}

	/**
	 * 取钱
	 * 
	 * @param tokenId
	 * @param custNo
	 * @param redeemShare
	 * @param txpwd
	 * @param custBankId
	 * @return
	 * @throws HowbuyException
	 */
	public static ResponseContentDto quQian(String tokenId, String custNo,String redeemShare, String txpwd,String custBankId)
			throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		Map<String, String> sparaMap = getScrateParm();
		addPram(sparaMap, "custNo", custNo);
		addPram(sparaMap, "redeemShare", redeemShare);// 
		addPram(sparaMap, "txPwd", txpwd);
		addPram(sparaMap, "custBankId", custBankId);
		String encMsgBase64 = DesUtilForNetParam.encryptParam(sparaMap);
		String signMsgBase64 = DesUtilForNetParam.encryptMd5(sparaMap);
		addPram(paraMap, "encMsg", encMsgBase64);
		addPram(paraMap, "signMsg", signMsgBase64);
		return doGetSecurity(AccessUrl.urlQuQian, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);
	}

	/**
	 * 交易查询
	 * 
	 * @param tokenId
	 * @param custNo
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws HowbuyException
	 */
	public static ResponseContentDto tradeHistory(String tokenId, String custNo, String pageNo, String pageSize,String custBankId) throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		Map<String, String> sparaMap = getScrateParm();
		addPram(sparaMap, "custNo", custNo);
		addPram(sparaMap, "pageNo", pageNo);
		addPram(sparaMap, "pageSize", pageSize);
		addPram(sparaMap, "custBankId", custBankId);
		String encMsgBase64 = DesUtilForNetParam.encryptParam(sparaMap);
		String signMsgBase64 = DesUtilForNetParam.encryptMd5(sparaMap);
		addPram(paraMap, "encMsg", encMsgBase64);
		addPram(paraMap, "signMsg", signMsgBase64);
		return doGetSecurity(AccessUrl.urlHistory, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);
	}

	/**
	 * 收益查询
	 * 
	 * @param tokenId
	 * @param custNo
	 * @param pageSize
	 * @param pageNo
	 * @return
	 * @throws HowbuyException
	 */
	public static ResponseContentDto tradeShouyi(String tokenId, String custNo, String pageNo, String pageSize, String protocalNo, String fundCode) throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		Map<String, String> sparaMap = getScrateParm();
		addPram(sparaMap, "custNo", custNo);
		addPram(sparaMap, "pageSize", pageSize);
		addPram(sparaMap, "pageNo", pageNo);
		addPram(sparaMap, "fundCode", fundCode);
		// 投资协议号这个比较特殊，空也要传
		// addPram(sparaMap, "protocalNo", protocalNo);
		sparaMap.put("protocalNo", protocalNo);
		String encMsgBase64 = DesUtilForNetParam.encryptParam(sparaMap);
		String signMsgBase64 = DesUtilForNetParam.encryptMd5(sparaMap);
		addPram(paraMap, "encMsg", encMsgBase64);
		addPram(paraMap, "signMsg", signMsgBase64);
		return doGetSecurity(AccessUrl.urlShouyi, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);
	}

	/**
	 * 每日估算收益
	 * 
	 * @param tokenId
	 * @param custNo
	 * @param startTradeDt
	 * @param endTradeDt
	 * @return
	 * @throws HowbuyException
	 */
	public static ResponseContentDto tradeDayEstimeatesIncome(String tokenId, String custNo, String dayCount,String custBankId,long cacheTime) throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		Map<String, String> sparaMap = getScrateParm();
		addPram(sparaMap, "custNo", custNo);
		addPram(sparaMap, "dayCount", dayCount);
		addPram(sparaMap, "custBankId", custBankId);
		String encMsgBase64 = DesUtilForNetParam.encryptParam(sparaMap);
		String signMsgBase64 = DesUtilForNetParam.encryptMd5(sparaMap);
		addPram(paraMap, "encMsg", encMsgBase64);
		addPram(paraMap, "signMsg", signMsgBase64);
		return doGetSecurity(AccessUrl.urlQueryEstimatesIncome, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);
	}

	/**
	 * 交易到账日期
	 * 
	 * @param tokenId
	 * @param fundCode
	 * @return
	 * @throws HowbuyException
	 */
	public static ResponseContentDto tradeTiRi(String tokenId, String fundCode) throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		addPram(paraMap, "fundCode", fundCode);
		// String path = UrlMatchUtil.urlUtilOnPublic(AccessUrl.urlTRi, paraMap,
		// ApplicationParams.getInstance());
		// Log.i("impl", path);
		// InputStream inputStream =
		// UrlConnectionUtil.getInstance().executeGet(path);
		// ResponseClient reClient = new ResponseClient(inputStream);
		// ResponseContentDto responseContentDto =
		// reClient.getResponseContentDto();
		return doGetNormal(AccessUrl.urlTRi, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);

	}

	public static ResponseContentDto activeQuery(String tokenId, String idNo, String idType) throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		Map<String, String> sparaMap = getScrateParm();
		addPram(sparaMap, "idNo", idNo);
		addPram(sparaMap, "idType", idType);
		String encMsgBase64 = DesUtilForNetParam.encryptParam(sparaMap);
		String signMsgBase64 = DesUtilForNetParam.encryptMd5(sparaMap);
		addPram(paraMap, "encMsg", encMsgBase64);
		addPram(paraMap, "signMsg", signMsgBase64);
		return doGetSecurity(AccessUrl.urlActiviteAccountQuery, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);
	}

	public static ResponseContentDto activeSubmit(String tokenId, String idNo, String idType, String mobile, String loginPasswd, String txPassword, String selfMsg)
			throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		Map<String, String> sparaMap = getScrateParm();
		addPram(sparaMap, "idNo", idNo);
		addPram(sparaMap, "idType", idType);
		addPram(sparaMap, "mobile", mobile);
		addPram(sparaMap, "loginPasswd", loginPasswd);
		addPram(sparaMap, "txPassword", txPassword);
		addPram(sparaMap, "selfMsg", selfMsg);
		String encMsgBase64 = DesUtilForNetParam.encryptParam(sparaMap);
		String signMsgBase64 = DesUtilForNetParam.encryptMd5(sparaMap);
		addPram(paraMap, "encMsg", encMsgBase64);
		addPram(paraMap, "signMsg", signMsgBase64);
		return doGetSecurity(AccessUrl.urlActiveSubmit, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);
	}

	//快速赎回
	public static ResponseContentDto queryCustLimit(String tokenId, String custNo,String custBankId,long cacheTime) throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		Map<String, String> sparaMap = getScrateParm();
		addPram(sparaMap, "custNo", custNo);
		addPram(sparaMap, "custBankId", custBankId);
		String encMsgBase64 = DesUtilForNetParam.encryptParam(sparaMap);
		String signMsgBase64 = DesUtilForNetParam.encryptMd5(sparaMap);
		addPram(paraMap, "encMsg", encMsgBase64);
		addPram(paraMap, "signMsg", signMsgBase64);
		return doGetSecurity(AccessUrl.urlCusLimit, paraMap, UrlMatchUtil.getBasepath(), cacheTime);
	}

	//银行卡限额
	public static ResponseContentDto queryCustBankLimit(String tokenId, String custNo, String custBankId, String bankCode,long cacheTime) throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		Map<String, String> sparaMap = getScrateParm();
		addPram(sparaMap, "custNo", custNo);
		addPram(sparaMap, "custBankId", custBankId);
		addPram(sparaMap, "bankCode", bankCode);
		String encMsgBase64 = DesUtilForNetParam.encryptParam(sparaMap);
		String signMsgBase64 = DesUtilForNetParam.encryptMd5(sparaMap);
		addPram(paraMap, "encMsg", encMsgBase64);
		addPram(paraMap, "signMsg", signMsgBase64);
		return doGetSecurity(AccessUrl.urlCustBankLimit, paraMap, UrlMatchUtil.getBasepath(), cacheTime);
	}

	public static ResponseContentDto queryNotice(String tokenId, String custNo, String bankAcct, String bankCode, String tipCategory) throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		Map<String, String> sparaMap = getScrateParm();
		addPram(sparaMap, "custNo", custNo);
		addPram(sparaMap, "bankAcct", bankAcct);
		addPram(sparaMap, "bankCode", bankCode);
		addPram(sparaMap, "tipCategory", tipCategory);
		String encMsgBase64 = DesUtilForNetParam.encryptParam(sparaMap);
		String signMsgBase64 = DesUtilForNetParam.encryptMd5(sparaMap);
		addPram(paraMap, "encMsg", encMsgBase64);
		addPram(paraMap, "signMsg", signMsgBase64);
		return doGetSecurity(AccessUrl.urlNotice, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);
	}

	public static ResponseContentDto webviewReq(String tokenId, String custNo, String reqId, Map<String, String> reqParams) throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		Map<String, String> sparaMap = getScrateParm();
		sparaMap.putAll(reqParams);
		addPram(sparaMap, "custNo", custNo);
		addPram(sparaMap, "id", reqId);
		Log.d("PureWebFragment", sparaMap.toString() + "--" + tokenId);
		String encMsgBase64 = DesUtilForNetParam.encryptParam(sparaMap);
		String signMsgBase64 = DesUtilForNetParam.encryptMd5(sparaMap);
		addPram(paraMap, "encMsg", encMsgBase64);
		addPram(paraMap, "signMsg", signMsgBase64);
		return doGetSecurity(AccessUrl.urlWebViewRequest, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);
	}

	public static ResponseContentDto queryQrnh(String tokenId, int dayCount) throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		addPram(paraMap, "dayCount", String.valueOf(dayCount));
		return doGetNormal(AccessUrl.urlQueryQrnh, paraMap, UrlMatchUtil.getBasepath(), CacheHelp.cachetime_nocache);
	}

	public static HostDistribution checkUpdate(String tokenId, long cacheTime) throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		String path = UrlMatchUtil.urlUtilOnPublic1(AccessUrl.urlUpdate, paraMap, ApplicationParams.getInstance());
		Log.i("impl", path);
		InputStream inputStream = CacheHelp.hasCacheInTime(path, cacheTime);
		if (inputStream == null) {
			inputStream = UrlConnectionUtil.getInstance().executeGet(path);
			if (inputStream == null) {
				return null;
			} else {
				inputStream= CacheHelp.saveCacheFile(path, inputStream);
			}
		}

		HostDistribution common;
		try {
			common = HostDistribution.parseFrom(inputStream);
			inputStream.close();
			return common;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new HowbuyException(DispatchAccessData.ErrorExcption, e);
		}
	}

	public static AdvertList getAd(String tokenId, String imgWidth, String imgHeight, long cacheTime) throws HowbuyException {
		Map<String, String> paraMap = new HashMap<String, String>();
		addPram(paraMap, "tokenId", tokenId);
		addPram(paraMap, "imageWidth", imgWidth);
		addPram(paraMap, "imageHeight", imgHeight);
		String path = UrlMatchUtil.urlUtilPost1(AccessUrl.urlAd);
		Log.i("impl", path);

		InputStream inputStream = CacheHelp.hasCacheInTime(path, cacheTime);
		if (inputStream == null) {
			inputStream = UrlConnectionUtil.getInstance().executePost(path, paraMap, ApplicationParams.getInstance().getPubNetParams());
			if (inputStream == null) {
				return null;
			} else {
				inputStream= CacheHelp.saveCacheFile(path, inputStream);
			}
		}

		AdvertList common;
		try {
			common = AdvertList.parseFrom(inputStream);
			inputStream.close();
			return common;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new HowbuyException(DispatchAccessData.ErrorExcption, e);
		}
	}
	

}
