package howbuy.android.piggy.service;

import howbuy.android.piggy.api.DispatchAccessData;
import howbuy.android.piggy.api.ResetRsaKey;
import howbuy.android.piggy.api.dto.ProductInfo;
import howbuy.android.piggy.api.dto.SupportBankAndProvinceDto;
import howbuy.android.piggy.api.dto.TradeDate;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.parameter.PiggyParams;
import howbuy.android.util.Cons;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class UpdateBasicDataService extends IntentService {
	public static final String UpdateBasicDataServiceAction = "howbuy.android.piggy.service.UpdateBasicDataService";
	public static final String NAME = "UpdateUserDataService";
	public static final String ResRsaKey = "ResRsaKey";
	public static final String ResBank = "ResBank";
	public static final String ResProduct = "ResProduct";
	public static final String ResTDay = "ResTDay";

	public UpdateBasicDataService() {
		// TODO Auto-generated constructor stub
		super("HelloIntentService");
	}

	public UpdateBasicDataService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		long a=System.currentTimeMillis();
		ContentValues cValues = new ContentValues();
		DispatchAccessData accessData = DispatchAccessData.getInstance();
		// rsa信息
		boolean rsaKey= ResetRsaKey.getInstance().doResetRsaKay();
		cValues.put(ResRsaKey, rsaKey);
		
		// 银行信息
		SupportBankAndProvinceDto bankAndProvinceDto = accessData.getBankList(Cons.SUPPORT_CHANNELPAY);
		// 产品信息
//		ProductInfo productInfo = accessData.getProductInfo();
		ProductInfo productInfo = accessData.getProductInfo();
		// T日信息
		TradeDate tDate;
		
//		try {
//			Thread.sleep(3000);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		PiggyParams parameter=ApplicationParams.getInstance().getPiggyParameter();
		
		
		if (bankAndProvinceDto.getContentCode() == Cons.SHOW_SUCCESS) {
			parameter.saveObject(PiggyParams.SfSupportBankAndProvinceDto, bankAndProvinceDto);
			parameter.saveObject(PiggyParams.SfSupportBankAndProvinceDtoTime, String.valueOf(System.currentTimeMillis()));
			cValues.put(ResBank, true);
		}
		
		if (productInfo.getContentCode() == Cons.SHOW_SUCCESS) {
			parameter.saveObject(PiggyParams.SfProductInfo, productInfo);
			parameter.saveObject(PiggyParams.SfProductInfoTime, String.valueOf(System.currentTimeMillis()));
			cValues.put(ResProduct, true);
			
			tDate=accessData.tradeDate(productInfo.getFundCode());
			if (tDate.getContentCode() == Cons.SHOW_SUCCESS) {
				parameter.saveObject(PiggyParams.SfTDay, tDate);
				parameter.saveObject(PiggyParams.SfTDayTime, String.valueOf(System.currentTimeMillis()));
				cValues.put(ResTDay, true);
			}
		}
		
		
		parameter.toCacheBasic();
		long b=System.currentTimeMillis();
		Log.i("msg--basictime--", String.valueOf(b-a));
		Intent intent2 = new Intent(UpdateBasicDataServiceAction);
		intent2.putExtra(Cons.Intent_bean, cValues);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent2);
		stopSelf();
	}
	

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(NAME, "onDestroy");
	}
}
