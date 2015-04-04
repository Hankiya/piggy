package howbuy.android.piggy.api;

import howbuy.android.piggy.api.dto.ResponseContentDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.util.Cons;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * 获取rs
 * 
 * @ClassName: ResetRsaKey.java
 * @Description:
 * @author yescpu yes.cpu@gmail.com
 * @date 2013-10-11上午10:58:09
 */
public class ResetRsaKey {
	public static boolean rsaKeyRuningFlag;
	
	
	private static ResetRsaKey mRsaKey = new ResetRsaKey();

	private ResetRsaKey() {

	}

	public static ResetRsaKey getInstance() {
		return mRsaKey;
	}

	/**
	 * 重置rsa密码
	 * 
	 * @return
	 * true 重置成功
	 */
	public synchronized boolean  doResetRsaKay() {
		rsaKeyRuningFlag=true;
		SharedPreferences sf = ApplicationParams.getInstance().getsF();
		String tokenId = sf.getString(Cons.SFfirstUUid, "android");
		String rsaKey = null;
		try {
			ResponseContentDto res = AccessDataService.getRsaSecret(tokenId);
			rsaKey = res.getBody().toString();
			if (TextUtils.isEmpty(rsaKey)) {
				rsaKeyRuningFlag=false;
				return false;
			}
			sf.edit().putString(Cons.SFRsa, rsaKey).commit();
			rsaKeyRuningFlag=false;
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			rsaKeyRuningFlag=false;
		}
		return false;
	}

}
