package howbuy.android.util;

import java.math.BigDecimal;

import android.text.TextUtils;

public class AmountUtil {
	
	/**
	 * 比较两个金额 1为前者大 2为相等 3为两者一样大 4为参数不正确
	 * @param amount1
	 * @param amount2
	 * @return
	 */
	public static final int compareTwoCurrey(String amount1,String amount2){
		if (TextUtils.isEmpty(amount1)||TextUtils.isEmpty(amount2)) {
			return 4;
			
		}
		BigDecimal b1=new BigDecimal(amount1);
		BigDecimal b2=new BigDecimal(amount2);
		if (b1.compareTo(b2) == 1) {
			return 1;
		}else if (b1.compareTo(b2) == -1) {
			return 3;
		}else if (b1.compareTo(b2) == 0){
			return 2;
		}else {
			return 4;
		}
		
	}
}
