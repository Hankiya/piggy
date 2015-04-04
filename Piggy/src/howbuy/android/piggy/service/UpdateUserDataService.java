package howbuy.android.piggy.service;

import howbuy.android.lib.MyAsyncTask;
import howbuy.android.piggy.api.DispatchAccessData;
import howbuy.android.piggy.api.ResetRsaKey;
import howbuy.android.piggy.api.dto.NoticeDtoList;
import howbuy.android.piggy.api.dto.ProductInfo;
import howbuy.android.piggy.api.dto.SupportBankAndProvinceDto;
import howbuy.android.piggy.api.dto.TradeDate;
import howbuy.android.piggy.api.dto.UserCardListDto;
import howbuy.android.piggy.api.dto.UserInfoDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.parameter.PiggyParams;
import howbuy.android.util.Cons;

import java.util.concurrent.Executors;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

public class UpdateUserDataService extends Service {
	public static final String UpdateUserDataServiceAction = "howbuy.android.piggy.service.UpdateUserDataService";
	public static final String NAME = "UpdateUserDataService";
	public static final String TaskType_AllNormal = "TaskType_AllNormal";
	public static final String TaskType_UserInfo = "TaskType_AllUser";
	public static final String TaskType_UserCard = "TaskType_UserCard";
	public static final String TaskType_Product = "TaskType_Product";
	public static final String TaskType_BankProvince = "TaskType_BankProvince";
	public static final String TaskType_TDay = "TaskType_TDay";
	public static final String TaskType_Notice = "TaskType_Notice";

	private boolean isInfoCompate = false;

	// 客户信息查询
	private final UpdateUserDataBind mBind = new UpdateUserDataBind();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.i(NAME, "onBind");
		return mBind;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	public class UpdateUserDataBind extends Binder {
		public UpdateUserDataService getService() {
			return UpdateUserDataService.this;
		}
	}

	public void luncherTask(String task, String requestId) {
		Log.i(NAME, "luncherTask");
		isInfoCompate = false;
		if (task != null) {
			new UpdateUserDataTask().executeOnExecutor(Executors.newCachedThreadPool(), task, requestId);
		}
	}

	public void luncherTaskN(String... params) {
		Log.i(NAME, "luncherTask");
		isInfoCompate = false;
		if (params != null) {
//			if (TaskType_AllNormal.equals(params[0])||TaskType_Product.equals(params[0])||TaskType_BankProvince.equals(params[0])||TaskType_TDay.equals(params[0])) {
				new UpdateUserDataTask().executeOnExecutor(Executors.newCachedThreadPool(), params);
//			}else {
//				new UpdateUserDataTask().execute(params);
//			}
		}
	}

	private class UpdateUserDataTask extends MyAsyncTask<String, Void, Parcelable> {
		/**
		 * 此taskType有双重含义，代表task的类型，并且代表广播的Action类型
		 * 
		 */
		private String taskType;
		private String requestId;

		@Override
		protected Parcelable doInBackground(String... params) {
			taskType = params[0];
			requestId = params[1];
			String cardId = null;
			if (taskType == null) {
				taskType = "null";
			}
			// TODO Auto-generated method stub
			if (params[0].equals(TaskType_UserInfo)) {
				return handTypeUserData();
			} else if (params[0].equals(TaskType_UserCard)) {
				String cardNo=params.length>2?params[2]:null;
				return handTypeUserCard(cardNo);
			} else if (params[0].equals(TaskType_Product)) {
				return handTypeProduct();
			} else if (params[0].equals(TaskType_BankProvince)) {
				return handTypeProvince();
			} else if (params[0].equals(TaskType_TDay)) {
				return handTypeTday();
			} else if (params[0].equals(TaskType_Notice)) {
				return handTypeNotice(params[2], params[3], params[4], params[5]);
			} else {
				return handTypeNormalData();
			}
		}

		@Override
		protected void onPostExecute(Parcelable result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Intent intent2 = new Intent(taskType);
			intent2.putExtra(Cons.Intent_type, taskType);
			intent2.putExtra(Cons.Intent_bean, result);
			intent2.putExtra(Cons.Intent_id, requestId);
			Log.d("service", "taskFinish++++" + taskType + "--requestId--" + requestId);
			LocalBroadcastManager.getInstance(UpdateUserDataService.this).sendBroadcast(intent2);
		}
	}

	private UserInfoDto handTypeUserData() {
		String custNo = ApplicationParams.getInstance().getsF().getString(Cons.SFUserCusNo, null);
		UserInfoDto userInfoDto = DispatchAccessData.getInstance().getUserInfo(custNo);
		PiggyParams parameter = ApplicationParams.getInstance().getPiggyParameter();
		if (userInfoDto.getContentCode() == Cons.SHOW_SUCCESS) {
			parameter.saveObject(PiggyParams.SfUserInfo, userInfoDto);
			parameter.saveObject(PiggyParams.SfUserInfoTime, String.valueOf(System.currentTimeMillis()));
		}
		parameter.toCacheUser();
		return userInfoDto;
	}

	private UserCardListDto handTypeUserCard(String cardNo) {
		UserCardListDto userInfoDto = null;
		String custNo = ApplicationParams.getInstance().getsF().getString(Cons.SFUserCusNo, null);
		userInfoDto = DispatchAccessData.getInstance().getUserCardList(custNo, cardNo);
		PiggyParams parameter = ApplicationParams.getInstance().getPiggyParameter();
		if ((userInfoDto.getContentCode() == Cons.SHOW_SUCCESS)||TextUtils.isEmpty(cardNo)) {
			parameter.saveObject(PiggyParams.sfUserCardList, userInfoDto);
			parameter.saveObject(PiggyParams.sfUserCardListTime, String.valueOf(System.currentTimeMillis()));
		}
		parameter.toCacheUser();
		return userInfoDto;
	}

	private ContentValues handTypeNormalData() {
		ResetRsaKey.getInstance().doResetRsaKay();
		ContentValues cv = new ContentValues();
		ProductInfo pInfo = handTypeProduct();
		cv.put(TaskType_Product, pInfo.getContentCode());
		SupportBankAndProvinceDto sBankPInfo = handTypeProvince();
		cv.put(TaskType_BankProvince, sBankPInfo.getContentCode());
		TradeDate dDay = handTypeTday();
		if (dDay != null) {
			cv.put(TaskType_TDay, dDay.getContentCode());
		}
		return cv;
	}

	private ProductInfo handTypeProduct() {
		DispatchAccessData accessData = DispatchAccessData.getInstance();
		ProductInfo productInfo = accessData.getProductInfo();
		PiggyParams parameter = ApplicationParams.getInstance().getPiggyParameter();
		if (productInfo.getContentCode() == Cons.SHOW_SUCCESS) {
			parameter.saveObject(PiggyParams.SfProductInfo, productInfo);
			parameter.saveObject(PiggyParams.SfProductInfoTime, String.valueOf(System.currentTimeMillis()));
		}
		parameter.toCacheBasic();
		return productInfo;
	}

	private TradeDate handTypeTday() {
		DispatchAccessData accessData = DispatchAccessData.getInstance();
		PiggyParams parameter = ApplicationParams.getInstance().getPiggyParameter();
		ProductInfo productInfo = parameter.getProductInfo();
		if (productInfo != null) {
			TradeDate tDate = accessData.tradeDate(productInfo.getFundCode());
			if (tDate.getContentCode() == Cons.SHOW_SUCCESS) {
				parameter.saveObject(PiggyParams.SfTDay, tDate);
				parameter.saveObject(PiggyParams.SfTDayTime, String.valueOf(System.currentTimeMillis()));
			}
			return tDate;
		}
		parameter.toCacheBasic();
		return null;
	}

	private SupportBankAndProvinceDto handTypeProvince() {
		DispatchAccessData accessData = DispatchAccessData.getInstance();
		PiggyParams parameter = ApplicationParams.getInstance().getPiggyParameter();
		SupportBankAndProvinceDto bankAndProvinceDto = accessData.getBankList(Cons.SUPPORT_CHANNELPAY);
		if (bankAndProvinceDto.getContentCode() == Cons.SHOW_SUCCESS) {
			parameter.saveObject(PiggyParams.SfSupportBankAndProvinceDto, bankAndProvinceDto);
			parameter.saveObject(PiggyParams.SfSupportBankAndProvinceDtoTime, String.valueOf(System.currentTimeMillis()));
		}
		parameter.toCacheBasic();
		return bankAndProvinceDto;
	}

	private NoticeDtoList handTypeNotice(String custNo, String bankAcct, String bankCode, String tipCategory) {
		DispatchAccessData accessData = DispatchAccessData.getInstance();
//		PiggyParams parameter = ApplicationParams.getInstance().getPiggyParameter();
		NoticeDtoList noticeDto = accessData.queryNotice(custNo, bankAcct, bankCode, tipCategory);
		return noticeDto;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i(NAME, "onCreate");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(NAME, "onDestroy");
	}

	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		super.onRebind(intent);
		Log.i(NAME, "onDestroy");
	}

	public boolean loadComplate() {
		Log.i(NAME, "isInfoCompate--" + isInfoCompate);
		return isInfoCompate;
	}

	public boolean isInfoCompate() {
		return isInfoCompate;
	}

	public void setInfoCompate(boolean isInfoCompate) {
		this.isInfoCompate = isInfoCompate;
	}

}
