package howbuy.android.piggy.parameter;

import howbuy.android.piggy.api.dto.ProductInfo;
import howbuy.android.piggy.api.dto.SupportBankAndProvinceDto;
import howbuy.android.piggy.api.dto.TradeDate;
import howbuy.android.piggy.api.dto.UserCardListDto;
import howbuy.android.piggy.api.dto.UserInfoDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.util.Cons;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.myjson.Gson;

/**
 * 储蓄罐参数缓存 简单方法
 * 
 * @author Administrator
 * 
 */
public class PiggyParams {
	public static final String SfProductInfo = "SfProductInfo";
	public static final String SfSupportBankAndProvinceDto = "SfSupportBankAndProvinceDto";
	public static final String SfTDay = "TDay";

	public static final String SfProductInfoTime = "SfProductInfoTime";
	public static final String SfSupportBankAndProvinceDtoTime = "SfSupportBankAndProvinceDtoTime";
	public static final String SfTDayTime = "TDayTime";

	public static final String sfUserCardList="sfUserCardList";
	public static final String SfUserInfo = "SfUserInfo";
	public static final String SfIncome = "SfIncome";

	public static final String sfUserCardListTime="sfUserCardListTime";
	public static final String SfUserInfoTime = "SfUserInfoTime";
	public static final String SfIncomeTime = "SfIncomeTime";
	
	private UserCardListDto userCardListDto;
	private UserInfoDto userInfoDto;
	private ProductInfo productInfo;
	private SupportBankAndProvinceDto supportBankAndProvinceDto;
	private TradeDate tradeDate;
	private static PiggyParams mParameter;

	private PiggyParams() {

	}

	public static final PiggyParams getInstance() {
		if (mParameter == null) {
			mParameter = new PiggyParams();
		}
		return mParameter;
	}

	public static Gson getGson() {
		return new Gson();
	}

	public void saveProductInfo(ProductInfo mProductInfo) {

	}

	public synchronized SharedPreferences getSf() {
		return ApplicationParams.getInstance().getSharedPreferences(Cons.SFbaseData, ApplicationParams.MODE_PRIVATE);
	}

	public synchronized void saveObject(String sfName, Object object) {
		Gson gson = getGson();
		String objString = gson.toJson(object);
		getSf().edit().putString(sfName, objString).commit();
	}

	public synchronized ProductInfo getProductInfo() {
		if (productInfo != null) {
			return productInfo;
		}

		String objString = getSf().getString(SfProductInfo, null);
		if (TextUtils.isEmpty(objString)) {
			return null;
		} else {
			productInfo = getGson().fromJson(objString, ProductInfo.class);
			return productInfo;
		}
	}

	public SupportBankAndProvinceDto getSupportBankInfo() {
		if (supportBankAndProvinceDto != null) {
			return supportBankAndProvinceDto;
		}

		String objString = getSf().getString(SfSupportBankAndProvinceDto, null);
		if (TextUtils.isEmpty(objString)) {
			return null;
		} else {
			supportBankAndProvinceDto = getGson().fromJson(objString, SupportBankAndProvinceDto.class);
			return supportBankAndProvinceDto;
		}
	}

	public TradeDate getTwoDay() {
		if (tradeDate != null) {
			return tradeDate;
		}
		String objString = getSf().getString(SfTDay, null);
		if (TextUtils.isEmpty(objString)) {
			return null;
		} else {
			tradeDate = getGson().fromJson(objString, TradeDate.class);
			return tradeDate;
		}
	}

	public UserInfoDto getUserInfo() {
		if (userInfoDto != null) {
			return userInfoDto;
		}
		String objString = getSf().getString(SfUserInfo, null);
		if (TextUtils.isEmpty(objString)) {
			return null;
		} else {
			userInfoDto = getGson().fromJson(objString, UserInfoDto.class);
			return userInfoDto;
		}
	}
	
	
	public UserCardListDto getUserCardInfo() {
		if (userCardListDto != null) {
			return userCardListDto;
		}
		String objString = getSf().getString(sfUserCardList, null);
		if (TextUtils.isEmpty(objString)) {
			return null;
		} else {
			userCardListDto = getGson().fromJson(objString, UserCardListDto.class);
			return userCardListDto;
		}
	}

	/**
	 * 删除用户信息
	 */
	public void removeUserDataPrivate() {
		removeUserDataPrivateCache();
		getSf().edit().remove(SfUserInfo).remove(sfUserCardList).remove(SfIncome).remove(SfUserInfoTime).remove(SfIncomeTime).commit();
	}

	/**
	 * 删除用户信息持久化
	 */
	public void removeUserDataPrivateCache() {
		userInfoDto = null;
		userCardListDto=null;
	}

	/**
	 * 删除公共用户信息持久化
	 */
	public void removeUserDataPublic() {
		productInfo = null;
		supportBankAndProvinceDto = null;
		tradeDate = null;
		getSf().edit().remove(SfProductInfo).remove(SfProductInfoTime).remove(SfSupportBankAndProvinceDto).remove(SfSupportBankAndProvinceDtoTime).remove(SfTDay)
				.remove(SfTDayTime).commit();
	}

	/**
	 * 删除公共用户信息
	 */
	public void removeUserDataPublicCache() {
		productInfo = null;
		supportBankAndProvinceDto = null;
		tradeDate = null;
	}

	public void toCacheBasic() {
		removeUserDataPublicCache();
		
		getProductInfo();
		getSupportBankInfo();
		getTwoDay();
	}

	public void toCacheUser() {
		removeUserDataPrivateCache();
		
		getUserInfo();
		getUserCardInfo();
	}

}
