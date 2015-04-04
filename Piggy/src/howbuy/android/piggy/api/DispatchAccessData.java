package howbuy.android.piggy.api;

import howbuy.android.piggy.api.dto.AccessInfo;
import howbuy.android.piggy.api.dto.BindCardDto;
import howbuy.android.piggy.api.dto.BindCardInf;
import howbuy.android.piggy.api.dto.EstimatesIncomeItem;
import howbuy.android.piggy.api.dto.EstimatesIncomeList;
import howbuy.android.piggy.api.dto.FundLimitDto;
import howbuy.android.piggy.api.dto.HeaderInfoDto;
import howbuy.android.piggy.api.dto.IncomeDto;
import howbuy.android.piggy.api.dto.LoginDto;
import howbuy.android.piggy.api.dto.NoticeDto;
import howbuy.android.piggy.api.dto.NoticeDtoList;
import howbuy.android.piggy.api.dto.OneStringDto;
import howbuy.android.piggy.api.dto.ProductInfo;
import howbuy.android.piggy.api.dto.ProvinceInfoDto;
import howbuy.android.piggy.api.dto.ResponseClient;
import howbuy.android.piggy.api.dto.ResponseContentDto;
import howbuy.android.piggy.api.dto.SupportBankAndProvinceDto;
import howbuy.android.piggy.api.dto.SupportBankBranchDto;
import howbuy.android.piggy.api.dto.SupportBankBranchListDto;
import howbuy.android.piggy.api.dto.SupportBankDto;
import howbuy.android.piggy.api.dto.TradeDate;
import howbuy.android.piggy.api.dto.TradeQueryDto;
import howbuy.android.piggy.api.dto.TradeQueryItemDto;
import howbuy.android.piggy.api.dto.UpdateDto;
import howbuy.android.piggy.api.dto.UserCardDto;
import howbuy.android.piggy.api.dto.UserCardListDto;
import howbuy.android.piggy.api.dto.UserCardUnBindDto;
import howbuy.android.piggy.api.dto.UserInfoDto;
import howbuy.android.piggy.api.dto.UserLimitDto;
import howbuy.android.piggy.api.dto.UserTypeDto;
import howbuy.android.piggy.api.dto.WebviewReqDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.error.HowbuyException;
import howbuy.android.piggy.help.CacheHelp;
import howbuy.android.piggy.parameter.GlobalParams;
import howbuy.android.util.Cons;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.text.TextUtils;
import android.util.Log;

import com.google.myjson.Gson;
import com.google.myjson.reflect.TypeToken;
import com.howbuy.wireless.entity.protobuf.AdvertListProtos.AdvertList;
import com.howbuy.wireless.entity.protobuf.CommonProtos.Common;
import com.howbuy.wireless.entity.protobuf.HostDistributionProtos.HostDistribution;

/**
 * 
 * @ClassName: DispatchAccessData.java
 * @Description:
 * @author yescpu yes.cpu@gmail.com
 * @date 2013-10-11上午11:46:25
 */
public class DispatchAccessData {
	public static final String SuccessValue = "成功";
	public static final int DefaulMaxNum = 2;
	public int selfNum = 0;
	public static final String ErrorNetWork = "网络不给力";
	public static final String ErrorExcption = "数据错误";
	public static final String ErrorNotData = "服务器数据为空";
	public static final String ErrorSelf = "安全传输错误";
	public static final String ErrorServiceNot = "数据返回异常";

	private static DispatchAccessData mDispatchAccessData = new DispatchAccessData();

	private DispatchAccessData() {

	}

	public static DispatchAccessData getInstance() {
		return mDispatchAccessData;
	}

	/**
	 * 获取tonken
	 * 
	 * @return
	 */
	public String getTokenId() {
		return null;
	}

	/**
	 * 获取rsaSecret
	 * 
	 * @param tokenId
	 * @return
	 */
	@Deprecated
	public OneStringDto getRsaSecret() {
		ResponseContentDto rnt = null;
		OneStringDto oneStringDto = new OneStringDto();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String rsaKey = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.getRsaSecret(tokenId);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}

		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					rsaKey = (String) rnt.getBody();
					if (TextUtils.isEmpty(rsaKey)) {
						msg = ErrorNotData;
					} else {
						code = Cons.SHOW_SUCCESS;
					}
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}
			}
		}

		oneStringDto.setContentCode(code);
		oneStringDto.setContentMsg(msg);
		oneStringDto.setOneString(rsaKey);
		return oneStringDto;
	}

	/**
	 * 发送短信验证码
	 * 
	 * @param phoneNum
	 * @return
	 */
	public OneStringDto smsSend(String phoneNum) {
		ResponseContentDto rnt = null;
		OneStringDto oneStringDto = new OneStringDto();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String smsResCode = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.smsSend(tokenId, phoneNum, "0");
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}

		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					smsResCode = rnt.getBody().toString();
					code = Cons.SHOW_SUCCESS;
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return smsSend(phoneNum);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf + "---para: phoneNum=" + phoneNum.toString();
						} else {
							msg = ErrorSelf;
						}
					}
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}
			}
		}
		oneStringDto.setContentCode(code);
		oneStringDto.setContentMsg(msg);
		oneStringDto.setOneString(smsResCode);
		return oneStringDto;
	}

	/**
	 * 验证短信验证码
	 * 
	 * @param dynaPwd
	 * @param mobile
	 * @return
	 */
	public OneStringDto smsVeri(String dynaPwd, String mobile) {
		ResponseContentDto rnt = null;
		OneStringDto oneStringDto = new OneStringDto();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String smsResCode = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.smsVeri(tokenId, dynaPwd, mobile);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}

		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					smsResCode = rnt.getBody().toString();
					code = Cons.SHOW_SUCCESS;
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return smsVeri(dynaPwd, mobile);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf + "---para: dynaPwd=" + dynaPwd.toString() + "; mobile=" + mobile.toString();
						} else {
							msg = ErrorSelf;
						}
					}
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}
			}
		}

		oneStringDto.setContentCode(code);
		oneStringDto.setContentMsg(msg);
		oneStringDto.setOneString(smsResCode);
		return oneStringDto;

	}

	/**
	 * 获取省份和分行
	 * 
	 * @param jsonList
	 *            支持的支付通道字符串采用Json格式组装
	 * @return
	 */
	// update by renzh add china pay jsonlist.
	public SupportBankAndProvinceDto getBankList(String jsonList) {
		ResponseContentDto rnt = null;
		SupportBankAndProvinceDto oneStringDto = new SupportBankAndProvinceDto();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.getBankList(tokenId, jsonList);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}

		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					Object o = rnt.getBody();
					Gson gson = new Gson();
					String gsonString = gson.toJson(o);
					oneStringDto=gson.fromJson(gsonString, SupportBankAndProvinceDto.class);
					code = Cons.SHOW_SUCCESS;
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return getBankList(jsonList);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf + "---para: jsonList=" + jsonList.toString();
						} else {
							msg = ErrorSelf;
						}
					}
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}
			}
		}
		oneStringDto.setContentCode(code);
		oneStringDto.setContentMsg(msg);
		return oneStringDto;
	}


	/**
	 * 支行列表
	 * 
	 * @param bankCode
	 * @param cityCode
	 * @param provCode
	 * @param pageSize
	 * @param pageNo
	 * @param bankName
	 * @return
	 */
	public SupportBankBranchListDto getBankBranchList(String bankCode, String provCode, String pageSize, String pageNo, String bankName) {
		ResponseContentDto rnt = null;
		SupportBankBranchListDto oneStringDto = new SupportBankBranchListDto();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.getBankBranchList(tokenId, bankCode, provCode, pageSize, pageNo, bankName);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}

		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					Object o = rnt.getBody();
					Gson gson = new Gson();
					String gsonString = gson.toJson(o);
					SupportBankBranchDto[] s = gson.fromJson(gsonString, new TypeToken<SupportBankBranchDto[]>() {
					}.getType());
					ArrayList<SupportBankBranchDto> q = new ArrayList<SupportBankBranchDto>();
					for (SupportBankBranchDto supportBankBranchDto : s) {
						q.add(supportBankBranchDto);
					}
					oneStringDto.setSupportBankBranchDtos(q);
					code = Cons.SHOW_SUCCESS;
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return getBankBranchList(bankCode, provCode, pageSize, pageNo, bankName);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf + "---para: bankCode=" + bankCode.toString() + "; provCode=" + provCode.toString() + "; pageSize=" + pageSize.toString() + "; pageNo="
									+ pageNo.toString() + "; bankName=" + bankName.toString();
						} else {
							msg = ErrorSelf;
						}
					}
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}
			}
		}

		oneStringDto.setContentCode(code);
		oneStringDto.setContentMsg(msg);
		return oneStringDto;
	}

	/**
	 * 注册提交
	 * 
	 * @param custName
	 * @param idNo
	 * @param mobile
	 * @param loginPwd
	 * @param tradePwd
	 * @return
	 */
	public OneStringDto commitRegistert(String custName, String idNo, String mobile, String loginPwd, String tradePwd, String verifyCode) {
		ResponseContentDto rnt = null;
		OneStringDto oneStringDto = new OneStringDto();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.commitRegistert(tokenId, custName, idNo, mobile, loginPwd, tradePwd, verifyCode);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}

		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					code = Cons.SHOW_SUCCESS;
					oneStringDto.setOneString(rnt.getBody().toString());
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return commitRegistert(custName, idNo, mobile, loginPwd, tradePwd, verifyCode);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf + "---para: custName=" + custName.toString() + "; idNo=" + idNo.toString() + "; mobile=" + mobile.toString() + "; loginPwd="
									+ loginPwd.toString() + "; tradePwd=" + tradePwd.toString();
						} else {
							msg = ErrorSelf;
						}
					}
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}
			}
		}

		oneStringDto.setContentCode(code);
		oneStringDto.setContentMsg(msg);
		return oneStringDto;

	}

	/**
	 * 登录
	 * 
	 * @param idNo
	 * @param password
	 * @return
	 */
	public LoginDto commitLogin(String idNo, String password) {
		ResponseContentDto rnt = null;
		LoginDto oneStringDto = new LoginDto();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String smsResCode = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.commitLogin(tokenId, idNo, password);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}

		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					smsResCode = rnt.getBody().toString();
					code = Cons.SHOW_SUCCESS;
					oneStringDto = new Gson().fromJson(rnt.getBody().toString(), LoginDto.class);
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return commitLogin(idNo, password);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf + "---para: idNo=" + idNo.toString() + "; password=" + password.toString();
						} else {
							msg = ErrorSelf;
						}
					}
				}  else if (handleForceLoginFlag(hDto)) {
					code = Cons.SHOW_FORCELOGIN;
				}else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}
			}
		}

		oneStringDto.setContentCode(code);
		oneStringDto.setContentMsg(msg);
		return oneStringDto;
	}

	/**
	 * 绑卡
	 * 
	 * @param custNo
	 * @param bankAcct
	 * @param bankCode
	 * @param cnapsNo
	 * @param provCode
	 * @param custBankId
	 * @return
	 */
	public BindCardDto bindCard(String custNo, String bankAcct, String bankCode, String cnapsNo, String provCode, String custBankId,String supportPayChannel) {
		ResponseContentDto rnt = null;
		BindCardDto bindCardDto = new BindCardDto();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.bindCard(tokenId, custNo, bankAcct, bankCode, cnapsNo, provCode, custBankId,supportPayChannel);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}

		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					Object o = rnt.getBody();
					if (o != null) {
						Gson gson = new Gson();
						String gsonString = gson.toJson(o);
						bindCardDto=gson.fromJson(gsonString, BindCardDto.class);
						//bindCardDto.setmBindCardInf(gson.fromJson(gsonString, BindCardInf.class));
						Log.i("bindCard", "body translate to obj " + bindCardDto);
					}
					code = Cons.SHOW_SUCCESS;
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return bindCard(custNo, bankAcct, bankCode, cnapsNo, provCode, custBankId,supportPayChannel);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf + "---para: custNo=" + custNo.toString() + "; accountNo=" + bankAcct.toString() + "; bankCode=" + bankCode.toString()
									+ "; subBankCode=" + cnapsNo.toString() + "; subBankName=" + "; provinceNo=" + provCode.toString() + "; custBankId=" + custBankId.toString();
						} else {
							msg = ErrorSelf;
						}
					}
				} else if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)
						&& hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_BindCard_Other)) {
					code = Cons.SHOW_BindOtherChannl;
					Object o = rnt.getBody();
					Log.i("bindCard", "body=" + o);
					if (o != null) {
						Gson gson = new Gson();
						String gsonString = gson.toJson(o);
						bindCardDto.setmBindCardInf(gson.fromJson(gsonString, BindCardInf.class));
						Log.i("bindCard", "body translate to obj " + bindCardDto);
					}
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}
			}
		}
		bindCardDto.setContentCode(code);
		bindCardDto.setContentMsg(msg);
		return bindCardDto;
	}

	public OneStringDto bindCardModify(String custNo, String cnapsNo, String custBankId) {
		ResponseContentDto rnt = null;
		OneStringDto bindCardDto = new OneStringDto();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.bindCardModify(tokenId, custNo, cnapsNo, custBankId);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}

		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					Object o = rnt.getBody();
					Log.i("bindCard", "body=" + o);
					if (o != null) {
						bindCardDto.setOneString(o.toString());
					}
					code = Cons.SHOW_SUCCESS;
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return bindCardModify(custNo, cnapsNo, custBankId);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf;
						} else {
							msg = ErrorSelf;
						}
					}
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}
			}
		}
		bindCardDto.setContentCode(code);
		bindCardDto.setContentMsg(msg);
		return bindCardDto;
	}

	/**
	 * 用户银行卡解绑
	 * 
	 * @param custNo
	 * @param custBankId
	 * @param txPasswd
	 * @return
	 */
	public UserCardUnBindDto unbindCard(String custNo, String custBankId, String txPasswd,String bankAcct) {
		ResponseContentDto rnt = null;
		UserCardUnBindDto bindCardDto = new UserCardUnBindDto();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.unbindCard(tokenId, custNo, custBankId, txPasswd,bankAcct);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}

		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					Object o = rnt.getBody();
					Log.i("bindCard", "body=" + o);
					if (o != null) {
						Gson gson = new Gson();
						String gsonString = gson.toJson(o);
						bindCardDto = gson.fromJson(gsonString, UserCardUnBindDto.class);
					}
					code = Cons.SHOW_SUCCESS;
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return unbindCard(custNo, custBankId, txPasswd,bankAcct);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf;
						} else {
							msg = ErrorSelf;
						}
					}
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}
			}
		}
		bindCardDto.setContentCode(code);
		bindCardDto.setContentMsg(msg);
		return bindCardDto;
	}

	/**
	 * 获取用户信息
	 * 
	 * @param custNo
	 * @return
	 */
	public UserInfoDto getUserInfo(String custNo) {
		ResponseContentDto rnt = null;
		UserInfoDto oneStringDto = new UserInfoDto();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.getUserInfo(tokenId, custNo);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}

		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					Object o = rnt.getBody();
					if (o != null) {
						code = Cons.SHOW_SUCCESS;
						Gson gson = new Gson();
						String gsonString = gson.toJson(o);
						oneStringDto = gson.fromJson(gsonString, UserInfoDto.class);
					} else {
						msg = ErrorNotData;
					}
				} else if (handleForceLoginFlag(hDto)) {
					code = Cons.SHOW_FORCELOGIN;
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return getUserInfo(custNo);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf + "---para: custNo=" + custNo.toString();
						} else {
							msg = ErrorSelf;
						}
					}
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}

			}
		}
		oneStringDto.setContentCode(code);
		oneStringDto.setContentMsg(msg);
		return oneStringDto;
	}

	/**
	 * 获取用户信息
	 * 
	 * @param custNo
	 * @return
	 */
	public UserCardListDto getUserCardList(String custNo, String cardNo) {
		ResponseContentDto rnt = null;
		UserCardListDto oneStringDto = new UserCardListDto();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.getUserCardList(tokenId, custNo, cardNo);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}

		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					Object o = rnt.getBody();
					if (o != null) {
						code = Cons.SHOW_SUCCESS;
						Gson gson = new Gson();
						String gsonString = gson.toJson(o);
						List<UserCardDto> list = gson.fromJson(gsonString, new TypeToken<List<UserCardDto>>() {
						}.getType());
						oneStringDto.setUserCardDtos(list);
					} else {
						msg = ErrorNotData;
					}
				} else if (handleForceLoginFlag(hDto)) {
					code = Cons.SHOW_FORCELOGIN;
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return getUserCardList(custNo, cardNo);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf + "---para: custNo=" + custNo.toString();
						} else {
							msg = ErrorSelf;
						}
					}
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}

			}
		}
		oneStringDto.setContentCode(code);
		oneStringDto.setContentMsg(msg);
		return oneStringDto;
	}

	// etUserCardIncomeList(String tokenId, String custNo)
	/**
	 * 获取用户信息
	 * 
	 * @param custNo
	 * @return
	 */
	public UserCardListDto getUserCardIncomeList(String custNo) {
		ResponseContentDto rnt = null;
		UserCardListDto oneStringDto = new UserCardListDto();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.getUserCardIncomeList(tokenId, custNo);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}

		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					Object o = rnt.getBody();
					if (o != null) {
						code = Cons.SHOW_SUCCESS;
						Gson gson = new Gson();
						String gsonString = gson.toJson(o);
						List<UserCardDto> list = gson.fromJson(gsonString, new TypeToken<List<UserCardDto>>() {
						}.getType());
						oneStringDto.setUserCardDtos(list);
					} else {
						msg = ErrorNotData;
					}
				} else if (handleForceLoginFlag(hDto)) {
					code = Cons.SHOW_FORCELOGIN;
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return getUserCardIncomeList(custNo);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf + "---para: custNo=" + custNo.toString();
						} else {
							msg = ErrorSelf;
						}
					}
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}

			}
		}
		oneStringDto.setContentCode(code);
		oneStringDto.setContentMsg(msg);
		return oneStringDto;
	}

	/**
	 * 获取产品信息
	 */
	public ProductInfo getProductInfo() {
		ResponseContentDto rnt = null;
		ProductInfo oneStringDto = new ProductInfo();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.getProductInfo(tokenId);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}

		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					code = Cons.SHOW_SUCCESS;
					Object o = rnt.getBody();
					Gson gson = new Gson();
					String gsonString = gson.toJson(o);
					oneStringDto = gson.fromJson(gsonString, ProductInfo.class);
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return getProductInfo();
					} else {
						msg = ErrorSelf;
					}
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}
			}
		}
		oneStringDto.setContentCode(code);
		oneStringDto.setContentMsg(msg);
		return oneStringDto;
	}

	/**
	 * 获取资产信息
	 */
	public AccessInfo getAssetsInfo(String custNo) {
		ResponseContentDto rnt = null;
		AccessInfo oneStringDto = new AccessInfo();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.getAssetsInfo(tokenId, custNo);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}

		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					code = Cons.SHOW_SUCCESS;
					Object o = rnt.getBody();
					Gson gson = new Gson();
					String gsonString = gson.toJson(o);
					oneStringDto = gson.fromJson(gsonString, AccessInfo.class);
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return getAssetsInfo(custNo);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf + "---para: custNo=" + custNo.toString();
						} else {
							msg = ErrorSelf;
						}
					}
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}
			}
		}
		oneStringDto.setContentCode(code);
		oneStringDto.setContentMsg(msg);
		return oneStringDto;
	}

	/**
	 * 用户密码重置前一步
	 * 
	 * @param custNo
	 * @param custName
	 * @param idNo
	 * @param dynaPwd
	 * @param pwdType
	 * @param mobile
	 * @return
	 */
	public OneStringDto passwordReset(String custNo, String custName, String idNo, String idType, String dynaPwd, String pwdType, String mobile) {
		ResponseContentDto rnt = null;
		OneStringDto oneStringDto = new OneStringDto();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.passwordReset(tokenId, custNo, custName, idNo, idType, dynaPwd, pwdType, mobile);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}
		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					code = Cons.SHOW_SUCCESS;
				} else if (handleForceLoginFlag(hDto)) {
					code = Cons.SHOW_FORCELOGIN;
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return passwordReset(custNo, custName, idNo, idType, dynaPwd, pwdType, mobile);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf + "---para: custNo=" + custNo.toString() + "; custName=" + custName.toString() + "; idNo=" + idNo.toString() + "; idType="
									+ idType.toString() + "; dynaPwd=" + dynaPwd.toString() + "; pwdType=" + pwdType.toString() + "; mobile=" + mobile.toString();
						} else {
							msg = ErrorSelf;
						}
					}
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}
			}
		}
		oneStringDto.setContentCode(code);
		oneStringDto.setContentMsg(msg);
		return oneStringDto;
	}

	/**
	 * 用户密码重置第二步
	 * 
	 * @param custNo
	 * @param password
	 * @param pwdType
	 * @return
	 */
	public OneStringDto passwordCommit(String custNo, String idNo, String idType, String password, String pwdType) {
		ResponseContentDto rnt = null;
		OneStringDto oneStringDto = new OneStringDto();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.passwordCommit(tokenId, custNo, idNo, idType, password, pwdType);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}
		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					code = Cons.SHOW_SUCCESS;
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return passwordCommit(custNo, idNo, idType, password, pwdType);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf + "---para: custNo=" + custNo.toString() + "; idNo=" + idNo.toString() + "; idType=" + idType.toString() + "; password="
									+ password.toString() + "; pwdType=" + pwdType.toString();
						} else {
							msg = ErrorSelf;
						}
					}
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}
			}
		}
		oneStringDto.setContentCode(code);
		oneStringDto.setContentMsg(msg);
		return oneStringDto;
	}

	/**
	 * 手机号修改
	 * 
	 * @param tokenId
	 * @param custNo
	 * @param mobile
	 * @param dynaPwd
	 * @param txPassword
	 * @return
	 */
	public OneStringDto updateMobile(String custNo, String mobile, String dynaPwd, String txPassword) {
		ResponseContentDto rnt = null;
		OneStringDto oneStringDto = new OneStringDto();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.updateMobile(tokenId, custNo, mobile, dynaPwd, txPassword);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}
		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					code = Cons.SHOW_SUCCESS;
				} else if (handleForceLoginFlag(hDto)) {
					code = Cons.SHOW_FORCELOGIN;
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return updateMobile(custNo, mobile, dynaPwd, txPassword);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf + "---para: custNo=" + custNo.toString() + "; mobile=" + mobile.toString() + "; dynaPwd=" + dynaPwd.toString() + "; txPassword="
									+ txPassword.toString();
						} else {
							msg = ErrorSelf;
						}
					}
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}
			}
		}
		oneStringDto.setContentCode(code);
		oneStringDto.setContentMsg(msg);
		return oneStringDto;
	}

	/**
	 * 存钱单笔
	 * 
	 * @param custNo
	 * @param fundCode
	 * @param purchaseMoney
	 * @param txpwd
	 * @return
	 */
	public OneStringDto saveMoney(String custNo, String purchaseMoney, String txpwd, String bankId, String bankFlag) {
		ResponseContentDto rnt = null;
		OneStringDto oneStringDto = new OneStringDto();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.saveMoney(tokenId, custNo, purchaseMoney, txpwd, bankId, bankFlag);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}
		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					code = Cons.SHOW_SUCCESS;
				} else if (handleForceLoginFlag(hDto)) {
					code = Cons.SHOW_FORCELOGIN;
				} else if (hDto != null && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_Beyound)) {
					code = Cons.SHOW_SEPT;
					msg = hDto.getResponseDesc();
				} else if (hDto != null && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_leater)) {
					code = Cons.SHOW_ASYN_ERROR;
					msg = hDto.getResponseDesc();
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return saveMoney(custNo, purchaseMoney, txpwd, bankId, bankFlag);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf + "---para: custNo=" + custNo.toString() + "; purchaseMoney=" + purchaseMoney.toString() + "; bankId=" + bankId.toString() + "; txpwd="
									+ txpwd.toString() + "; bankFlag=" + bankFlag.toString();
						} else {
							msg = ErrorSelf;
						}
					}
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}
			}
		}
		oneStringDto.setContentCode(code);
		oneStringDto.setContentMsg(msg);
		return oneStringDto;
	}

	/**
	 * 存钱多笔
	 * 
	 * @param custNo
	 * @param fundCode
	 * @param purchaseMoney
	 * @param txpwd
	 * @param delayFlag
	 * @param savingPlanSubsAppDt
	 * @return
	 */
	public OneStringDto saveMoneys(String custNo, String fundCode, String purchaseMoney, String txpwd, String delayFlag, String savingPlanSubsAppDt) {
		ResponseContentDto rnt = null;
		OneStringDto oneStringDto = new OneStringDto();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.saveMoneys(tokenId, custNo, fundCode, purchaseMoney, txpwd, delayFlag, savingPlanSubsAppDt);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}
		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					code = Cons.SHOW_SUCCESS;
				} else if (handleForceLoginFlag(hDto)) {
					code = Cons.SHOW_FORCELOGIN;
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return saveMoneys(custNo, fundCode, purchaseMoney, txpwd, delayFlag, savingPlanSubsAppDt);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf + "---para: custNo=" + custNo.toString() + "; fundCode=" + fundCode.toString() + "; purchaseMoney=" + purchaseMoney.toString()
									+ "; delayFlag=" + delayFlag.toString() + "; txpwd=" + txpwd.toString() + "; savingPlanSubsAppDt=" + savingPlanSubsAppDt.toString();
						} else {
							msg = ErrorSelf;
						}
					}
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}
			}
		}
		oneStringDto.setContentCode(code);
		oneStringDto.setContentMsg(msg);
		return oneStringDto;
	}

	/**
	 * 取钱
	 * 
	 * @param custNo
	 * @param fundCode
	 * @param redeemShare
	 * @param allRedeemFlag
	 * @param txpwd
	 * @return
	 */
	public OneStringDto quQian(String custNo, String redeemShare, String txpwd, String custBankId) {
		ResponseContentDto rnt = null;
		OneStringDto oneStringDto = new OneStringDto();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.quQian(tokenId, custNo, redeemShare, txpwd, custBankId);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}
		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {

				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					code = Cons.SHOW_SUCCESS;
				} else if (handleForceLoginFlag(hDto)) {
					code = Cons.SHOW_FORCELOGIN;
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return quQian(custNo, redeemShare, txpwd, custBankId);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf + "---para: custNo=" + custNo.toString() + "; custBankId=" + custBankId.toString() + "; redeemShare=" + redeemShare.toString();
						} else {
							msg = ErrorSelf;
						}
					}
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}
			}
		}
		oneStringDto.setContentCode(code);
		oneStringDto.setContentMsg(msg);
		return oneStringDto;
	}

	/**
	 * 查询交易
	 * 
	 * @param tokenId
	 * @param custNo
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public TradeQueryDto tradeHistory(String custNo, String pageNo, String pageSize, String custBankId) {
		ResponseContentDto rnt = null;
		TradeQueryDto oneStringDto = new TradeQueryDto();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.tradeHistory(tokenId, custNo, pageNo, pageSize, custBankId);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}
		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					Object o = rnt.getBody();
					Gson gson = new Gson();
					String gsonString = gson.toJson(o);
					oneStringDto=gson.fromJson(gsonString, TradeQueryDto.class);
					code = Cons.SHOW_SUCCESS;
				} else if (handleForceLoginFlag(hDto)) {
					code = Cons.SHOW_FORCELOGIN;
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return tradeHistory(custNo, pageNo, pageSize, custBankId);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf + "---para: custNo=" + custNo.toString() + "; pageNo=" + pageNo.toString() + "; pageSize=" + pageSize.toString();
						} else {
							msg = ErrorSelf;
						}
					}
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}
			}
		}
		oneStringDto.setContentCode(code);
		oneStringDto.setContentMsg(msg);
		return oneStringDto;
	}

	/**
	 * 收益列表
	 * 
	 * @param custNo
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	public IncomeDto tradeIncome(String custNo, String pageNo, String pageSize, String protocalNo, String fundCode) {
		ResponseContentDto rnt = null;
		IncomeDto oneStringDto = new IncomeDto();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			// String tokenId, String custNo, String pageSize, String
			// pageNo,String protocalNo,String fundCode
			rnt = AccessDataService.tradeShouyi(tokenId, custNo, String.valueOf(pageNo), String.valueOf(pageSize), protocalNo, fundCode);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}
		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					Object o = rnt.getBody();
					Gson gson = new Gson();
					String gsonString = gson.toJson(o);
					oneStringDto = gson.fromJson(gsonString, IncomeDto.class);
					code = Cons.SHOW_SUCCESS;
				} else if (handleForceLoginFlag(hDto)) {
					code = Cons.SHOW_FORCELOGIN;
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return tradeIncome(custNo, pageNo, pageSize, protocalNo, fundCode);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf + "---para: custNo=" + custNo.toString() + "; pageNo=" + pageNo.toString() + "; pageSize=" + pageSize.toString() + "; protocalNo="
									+ protocalNo.toString() + "; fundCode=" + fundCode.toString();
						} else {
							msg = ErrorSelf;
						}
					}
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}
			}
		}
		oneStringDto.setContentCode(code);
		oneStringDto.setContentMsg(msg);
		return oneStringDto;
	}

	/**
	 * 收益列表
	 * 
	 * @param custNo
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	public EstimatesIncomeList tradeDayEstimeatesIncome(String custNo, String dayCount, String custBankId, long cacheTime) {
		ResponseContentDto rnt = null;
		EstimatesIncomeList oneStringDto = new EstimatesIncomeList();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			// String tokenId, String custNo, String pageSize, String
			// pageNo,String protocalNo,String fundCode
			rnt = AccessDataService.tradeDayEstimeatesIncome(tokenId, custNo, dayCount, custBankId, cacheTime);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}
		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					Object o = rnt.getBody();
					Gson gson = new Gson();
					String gsonString = gson.toJson(o);
					
					if (gsonString != null && !gsonString.equals("{}")) {
						ArrayList<EstimatesIncomeItem> list = gson.fromJson(gsonString, new TypeToken<ArrayList<EstimatesIncomeItem>>() {
						}.getType());
						oneStringDto.setmList(list);
					}
					
					code = Cons.SHOW_SUCCESS;
				} else if (handleForceLoginFlag(hDto)) {
					code = Cons.SHOW_FORCELOGIN;
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return tradeDayEstimeatesIncome(custNo, dayCount, custBankId, cacheTime);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf + "---para: custNo=" + custNo.toString() + "; dayCount=" + dayCount.toString();
						} else {
							msg = ErrorSelf;
						}
					}
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}
			}
		}
		oneStringDto.setContentCode(code);
		oneStringDto.setContentMsg(msg);
		return oneStringDto;
	}

	/**
	 * 
	 * 交易到账日期
	 * 
	 * @param custNo
	 * @param fundCode
	 * @return
	 */
	public TradeDate tradeDate(String fundCode) {
		ResponseContentDto rnt = null;
		TradeDate oneStringDto = new TradeDate();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.tradeTiRi(tokenId, fundCode);
		} catch (HowbuyException e) {
			msg = e.getMessage();
			e.printStackTrace();
		}

		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					Object o = rnt.getBody();
					Gson gson = new Gson();
					String gsonString = gson.toJson(o);
					oneStringDto = gson.fromJson(gsonString, TradeDate.class);
					code = Cons.SHOW_SUCCESS;
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return tradeDate(fundCode);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf + "---para: fundCode=" + fundCode.toString();
						} else {
							msg = ErrorSelf;
						}
					}
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}
			}
		}
		oneStringDto.setContentCode(code);
		oneStringDto.setContentMsg(msg);
		return oneStringDto;
	}

	public UserTypeDto activeQuery(String idNo, String idType) {
		ResponseContentDto rnt = null;
		UserTypeDto oneStringDto = new UserTypeDto();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.activeQuery(tokenId, idNo, idType);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}
		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					Object o = rnt.getBody();
					Gson gson = new Gson();
					String gsonString = gson.toJson(o);
					oneStringDto = gson.fromJson(gsonString, UserTypeDto.class);
					code = Cons.SHOW_SUCCESS;
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						android.util.Log.d("activeQuery", "selfNum=" + selfNum);
						return activeQuery(idNo, idType);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf + "---para: idNo =" + idNo.toString() + "idType =" + idType.toString();
						} else {
							msg = ErrorSelf;
						}
					}
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}
			}
		}
		oneStringDto.setContentCode(code);
		oneStringDto.setContentMsg(msg);
		return oneStringDto;
	}

	public OneStringDto activeSubmit(String idNo, String idType, String mobile, String loginPasswd, String txPassword, String selfMsg) {
		ResponseContentDto rnt = null;
		OneStringDto oneStringDto = new OneStringDto();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.activeSubmit(tokenId, idNo, idType, mobile, loginPasswd, txPassword, selfMsg);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}
		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					code = Cons.SHOW_SUCCESS;
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return activeSubmit(idNo, idType, mobile, loginPasswd, txPassword, selfMsg);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf + "---para: idNo=" + idNo.toString() + "; idType=" + idType.toString() + "; mobile=" + mobile.toString() + "; loginPasswd="
									+ loginPasswd.toString() + "; txPassword=" + txPassword.toString() + "; selfMsg=" + selfMsg.toString();
						} else {
							msg = ErrorSelf;
						}
					}
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}
			}
		}
		oneStringDto.setContentCode(code);
		oneStringDto.setContentMsg(msg);
		return oneStringDto;
	}

	/**
	 * 查询用户快速赎回限额
	 * 
	 * @param custNo
	 * @return
	 */
	public UserLimitDto queryUserLimit(String custNo,String custBankId,long cacheTime) {
		ResponseContentDto rnt = null;
		UserLimitDto oneStringDto = new UserLimitDto();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.queryCustLimit(tokenId, custNo, custBankId,cacheTime);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}
		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					code = Cons.SHOW_SUCCESS;
					Gson gson = new Gson();
					String gsonString = gson.toJson(rnt.getBody());
					oneStringDto = gson.fromJson(gsonString, UserLimitDto.class);
				} else if (handleForceLoginFlag(hDto)) {
					code = Cons.SHOW_FORCELOGIN;
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return queryUserLimit(custNo,custBankId, CacheHelp.cachetime_one_day);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf + "---para: idNo=" + custNo.toString();
						} else {
							msg = ErrorSelf;
						}
					}
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}
			}
		}
		oneStringDto.setContentCode(code);
		oneStringDto.setContentMsg(msg);
		return oneStringDto;
	}

	/**
	 * 查询银行卡限额
	 * 
	 * @param idNo
	 * @return
	 */
	public FundLimitDto queryBankLimit(String idNo, String custBankId, String bankCode, long cacheTime) {
		ResponseContentDto rnt = null;
		FundLimitDto oneStringDto = new FundLimitDto();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.queryCustBankLimit(tokenId, idNo, custBankId, bankCode, cacheTime);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}
		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					code = Cons.SHOW_SUCCESS;
					Gson gson = new Gson();
					String gsonString = gson.toJson(rnt.getBody());
					oneStringDto = gson.fromJson(gsonString, FundLimitDto.class);
				} else if (handleForceLoginFlag(hDto)) {
					code = Cons.SHOW_FORCELOGIN;
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return queryBankLimit(idNo, custBankId, bankCode, CacheHelp.cachetime_nocache);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf + "---para: idNo=" + idNo.toString();
						} else {
							msg = ErrorSelf;
						}
					}
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}
			}
		}
		oneStringDto.setContentCode(code);
		oneStringDto.setContentMsg(msg);
		return oneStringDto;
	}

	/**
	 * 查询通知
	 * 
	 * @param custNo
	 * @param bankAcct
	 * @param bankCode
	 * @param tipCategory
	 * @return
	 */
	public NoticeDtoList queryNotice(String custNo, String bankAcct, String bankCode, String tipCategory) {
		ResponseContentDto rnt = null;
		NoticeDtoList oneStringDto = new NoticeDtoList();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.queryNotice(tokenId, custNo, bankAcct, bankCode, tipCategory);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}
		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					code = Cons.SHOW_SUCCESS;
					Gson gson = new Gson();
					String gsonString = gson.toJson(rnt.getBody());
					ArrayList<NoticeDto> list = gson.fromJson(gsonString, new TypeToken<ArrayList<NoticeDto>>() {
					}.getType());
					oneStringDto.setListNotice(list);
				} else if (handleForceLoginFlag(hDto)) {
					code = Cons.SHOW_FORCELOGIN;
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return queryNotice(custNo, bankAcct, bankCode, tipCategory);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf + "---para: idNo=" + custNo.toString();
						} else {
							msg = ErrorSelf;
						}
					}
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}
			}
		}
		oneStringDto.setContentCode(code);
		oneStringDto.setContentMsg(msg);
		return oneStringDto;
	}

	/**
	 * webview native 请求
	 * 
	 * @param custNo
	 * @param reqId
	 * @param reqParams
	 * @return
	 */
	public WebviewReqDto webviewReq(String custNo, String reqId, Map<String, String> reqParams) {
		ResponseContentDto rnt = null;
		WebviewReqDto oneStringDto = new WebviewReqDto();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.webviewReq(tokenId, custNo, reqId, reqParams);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}
		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				oneStringDto.setSpecialCode(hDto.getContentCode());
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					code = Cons.SHOW_SUCCESS;
					Gson gson = new Gson();
					String gsonString = gson.toJson(rnt.getBody());
					oneStringDto.setBody(gsonString);
				} else if (handleForceLoginFlag(hDto)) {
					code = Cons.SHOW_FORCELOGIN;
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return webviewReq(custNo, reqId, reqParams);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf + "---para: idNo=" + custNo.toString();
						} else {
							msg = ErrorSelf;
						}
					}
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}
			}
		}
		oneStringDto.setContentCode(code);
		oneStringDto.setContentMsg(msg);
		return oneStringDto;
	}

	/**
	 * 检查更新
	 * 
	 * @return
	 */
	public UpdateDto checkUpdate() {
		HostDistribution rnt = null;
		UpdateDto oneStringDto = new UpdateDto();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.checkUpdate(tokenId, CacheHelp.cachetime_60_mint);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}
		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				Common hDto = rnt.getCommon();
				if (hDto.getResponseCode().equals("1")) {
					code = Cons.SHOW_SUCCESS;
					oneStringDto.setUpdateDesc(rnt.getUpdateDesc());
					oneStringDto.setUpdateUrl(rnt.getUpdateUrl());
					oneStringDto.setVersionNeedUpdate(rnt.getVersionNeedUpdate());
				} else {
					String t = hDto.getResponseContent();
					msg = TextUtils.isEmpty(t) ? ErrorServiceNot : t;
				}
			}
		}
		oneStringDto.setContentCode(code);
		oneStringDto.setContentMsg(msg);
		return oneStringDto;
	}

	/**
	 * 获取广告
	 * 
	 * @param imgWidth
	 * @param imgHeight
	 * @param cacheTime
	 * @return
	 * @throws HowbuyException
	 */
	public AdvertList getAd(String imgWidth, String imgHeight, long cacheTime) throws HowbuyException {
		AdvertList rnt = null;
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.getAd(tokenId, imgWidth, imgHeight, cacheTime);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}
		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				Common hDto = rnt.getCommon();
				if (hDto.getResponseCode().equals("1")) {
					code = Cons.SHOW_SUCCESS;
					// oneStringDto.setUpdateDesc(rnt.getUpdateDesc());
					// oneStringDto.setUpdateUrl(rnt.getUpdateUrl());
					// oneStringDto.setVersionNeedUpdate(rnt.getVersionNeedUpdate());
				} else {
					String t = hDto.getResponseContent();
					msg = TextUtils.isEmpty(t) ? ErrorServiceNot : t;
				}
			}
		}
		return rnt;
	}

	/**
	 * 获取广告图片
	 * 
	 * @param imageUrl
	 * @param cacheTime
	 * @return
	 * @throws HowbuyException
	 */
	public InputStream getAdImg(String imageUrl, long cacheTime) throws HowbuyException {
		imageUrl = imageUrl.trim();
		InputStream in = AccessDataService.doGetHbFund(imageUrl, null, cacheTime);
		return in;
	}

	/**
	 * 
	 * @param imageUrl
	 * @param cacheTime
	 * @return
	 * @throws HowbuyException
	 */
	public InputStream getBankImg(String path, long cacheTime) throws HowbuyException {
		InputStream in = AccessDataService.doGetHbFund(path, null, cacheTime);
		return in;
	}

	public boolean checkMaxNum() {
		selfNum++;
		if (selfNum > DefaulMaxNum) {
			selfNum = 0;
			return false;
		}
		return true;
	}

	/**
	 * 根据content和response msg返回正确的msg
	 * 
	 * @param msg1
	 * @param msg2
	 * @return
	 */
	public String handleMsg(String msg1, String msg2) {
		String res = null;

		// 是否为空
		boolean bMsg1 = TextUtils.isEmpty(msg1);
		boolean bMsg2 = TextUtils.isEmpty(msg2);

		// 是否为"成功"
		boolean cMsg1 = false, cMsg2 = false;
		if (!bMsg1 && msg1.equals(SuccessValue)) {
			cMsg1 = true;
		}
		if (!bMsg2 && msg2.equals(SuccessValue)) {
			cMsg2 = true;
		}

		// 赋值
		if (cMsg1 && cMsg2) {
			return SuccessValue;
		} else {
			if (cMsg1 == false) {
				res = msg1;
			}

			if (cMsg2 == false) {
				res = msg2;
			}

			if (TextUtils.isEmpty(res)) {
				res = ErrorServiceNot;
			}

		}
		return res;

	}

	/**
	 * 是否重新请求
	 * 
	 * @param hDto
	 * @return
	 */
	public boolean handleSecurity(HeaderInfoDto hDto) {
		String serverString = ResponseClient.VERIFICATION_CODE_EXPIRED + ResponseClient.VERIFICATION_CODE_Error;
		if (hDto == null) {
			return false;
		}
		String code1 = hDto.getResponseCode();
		String code2 = hDto.getContentCode();
		if (TextUtils.isEmpty(code1) || TextUtils.isEmpty(code2)) {
			return false;
		}

		if (serverString.contains(code1) || serverString.contains(code2)) {
			return true;
		}
		return false;

	}

	/**
	 * 判断是否需要重新登陆
	 */
	public boolean handleForceLoginFlag(HeaderInfoDto hDto) {
		if (hDto != null && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_FORCELOGIN)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 鉴权
	 * 
	 * @param custNo
	 * @param bankAcct
	 * @param bankCode
	 * @param cnapsNo
	 * @param provCode
	 * @param custBankId
	 * String bankCode, String custNo, String custBankId, String supportPayChannel
	 * @return
	 */
	public BindCardDto bankAuth(String custNo, String bankCode, String custBankId,String supportPayChannel) {
		ResponseContentDto rnt = null;
		BindCardDto bindCardDto = new BindCardDto();
		int code = Cons.SHOW_ERROR;
		String msg = null;
		String tokenId = ApplicationParams.getInstance().getsF().getString(Cons.SFfirstUUid, "android");
		try {
			rnt = AccessDataService.bankAuth(tokenId, custNo,bankCode, custBankId, supportPayChannel);
		} catch (HowbuyException e) {
			msg = e.getMessage();
		}

		if (TextUtils.isEmpty(msg)) {
			if (rnt == null) {
				msg = ErrorNotData;
			} else {
				HeaderInfoDto hDto = rnt.getHeader();
				if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS) && hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)) {
					Object o = rnt.getBody();
					if (o != null) {
						Gson gson = new Gson();
						String gsonString = gson.toJson(o);
						bindCardDto=gson.fromJson(gsonString, BindCardDto.class);
						//bindCardDto.setmBindCardInf(gson.fromJson(gsonString, BindCardInf.class));
						Log.i("bankAuth", "body translate to obj " + bindCardDto);
					}
					code = Cons.SHOW_SUCCESS;
				} else if (handleSecurity(hDto)) {
					if (ResetRsaKey.getInstance().doResetRsaKay() && checkMaxNum()) {
						return bankAuth(custNo, bankCode, custBankId, supportPayChannel);
					} else {
						if (GlobalParams.getGlobalParams().isDebugFlag()) {
							msg = ErrorSelf + "---para: custNo=" + custNo.toString() + "; bankCode=" + bankCode.toString() + "; subBankName="+ "; custBankId=" + custBankId.toString();
						} else {
							msg = ErrorSelf;
						}
					}
				} else if (hDto != null && hDto.getResponseCode().equals(ResponseClient.RESPONSE_RES_SUCCESS)
						&& hDto.getContentCode().equals(ResponseClient.RESPONSE_RES_BindCard_Other)) {
					code = Cons.SHOW_BindOtherChannl;
					Object o = rnt.getBody();
					Log.i("bankAuth", "body=" + o);
					if (o != null) {
						Gson gson = new Gson();
						String gsonString = gson.toJson(o);
						bindCardDto.setmBindCardInf(gson.fromJson(gsonString, BindCardInf.class));
						Log.i("bankAuth", "body translate to obj " + bindCardDto);
					}
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				} else {
					String httpMsg = hDto.getResponseDesc();
					String contentMsg = hDto.getContentDesc();
					msg = handleMsg(httpMsg, contentMsg);
				}
			}
		}
		bindCardDto.setContentCode(code);
		bindCardDto.setContentMsg(msg);
		return bindCardDto;
	}
}
