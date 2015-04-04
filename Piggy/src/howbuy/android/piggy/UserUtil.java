package howbuy.android.piggy;

import howbuy.android.piggy.api.dto.UserCardDto;
import howbuy.android.piggy.api.dto.UserCardListDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.util.Cons;

import java.util.List;

import android.text.TextUtils;

public class UserUtil {
	public static final int BindEdBand = 1;
	
	/**
	 * 银行账户验证状态（1-未验证，2-验证通过，3-验证失败）
	 * @author Administrator
	 * 
	 */
	public enum UserBankVeryType {
		notVrfy, VrfyError, VrfySuccessBindCard
	}

	/**
	 * 健全状态 1-未鉴权；2-鉴权中；3-鉴权失败；4-鉴权通过
	 * 
	 * @author Administrator
	 * 
	 */
	public enum UserSoundType {
		notSound, SoundFail, Sounduccess, Sounding, SoundUnknown
	}

	/**
	 * 判断是否登录
	 * 
	 * @return
	 */
	public static final boolean isLogin() {
		String userNo = ApplicationParams.getInstance().getsF().getString(Cons.SFUserCusNo, null);
		if (TextUtils.isEmpty(userNo)) {
			return false;
		}
		return true;
	}

	/**
	 * 验证 状态
	 * 1-未验证，2-验证通过，3-验证失败
	 * @return
	 */
	public static final UserBankVeryType VrfyCardStatus(String cardId) {
		UserCardListDto u = ApplicationParams.getInstance().getPiggyParameter().getUserCardInfo();
		UserBankVeryType res = null;
		String bankVrfy;
		if (u == null) {
			bankVrfy = null;
		} else {
			bankVrfy = u.getCardByIdOrAcct(cardId).getBankAcctVrfyStat();
		}
		if (TextUtils.isEmpty(bankVrfy)) {
			res = UserBankVeryType.notVrfy;
		} else if (bankVrfy.equals("1")) {
			res = UserBankVeryType.notVrfy;
		} else if (bankVrfy.equals("2")) {
			res = UserBankVeryType.VrfySuccessBindCard;
		} else if (bankVrfy.equals("3")) {
			res = UserBankVeryType.VrfyError;
		}
		return res;
	}

	/**
	 * 判断是否有银行卡绑定成功
	 * @return
	 */
	public static final boolean isBindBank() {
		UserCardListDto u = ApplicationParams.getInstance().getPiggyParameter().getUserCardInfo();
		if (u != null) {
			List<UserCardDto> list = u.getUserCardDtos();
			if (list!=null&&list.size()>0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 鉴权 状态
	 * 1-未鉴权；2-鉴权中；3-鉴权失败；4-鉴权通过
	 * @return
	 */
	public static final UserSoundType userSoundStatus(String cardId) {
		UserCardListDto u = ApplicationParams.getInstance().getPiggyParameter().getUserCardInfo();
		if (u == null) {
			return UserSoundType.SoundUnknown;
		}
		
		String bankVrfy;
		try {
			bankVrfy = u.getCardByIdOrAcct(cardId).getAcctIdentifyStat();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return UserSoundType.SoundUnknown;
		}
		
		if (TextUtils.isEmpty(bankVrfy)) {
			return UserSoundType.SoundUnknown;
		} else if (bankVrfy.equals("1")) {
			return UserSoundType.notSound;
		} else if (bankVrfy.equals("2")) {
			return UserSoundType.Sounding;
		} else if (bankVrfy.equals("3")) {
			return UserSoundType.SoundFail;
		} else if (bankVrfy.equals("4")) {
			return UserSoundType.Sounduccess;
		}
		return UserSoundType.SoundUnknown;
	}

	/**
	 * 310 浦发银行 ×102 中国工商银行 ×103 中国农业银行 104 中国银行 ×105 中国建设银行 ×305 中国民生银行 ×307
	 * 平安银行 ×308 招商银行 ×403 中国邮政储蓄银行 306 广东发展银行 314 上海银行 317 温州银行 106 交通银行
	 * 
	 * @return
	 */
	public static final int userBankIcon(String cardId) {
		UserCardListDto u = ApplicationParams.getInstance().getPiggyParameter().getUserCardInfo();

		if (u == null) {
			return R.drawable.abs__ic_clear;
		}
		String bankcode = u.getCardByIdOrAcct(cardId).getBankCode();
		if (TextUtils.isEmpty(bankcode)) {
			return R.drawable.abs__ic_clear;
		}
		int bank[][] = new int[][] { { 310, R.drawable.pufa }, { 102, R.drawable.gonghang }, { 103, R.drawable.nonghang }, { 104, R.drawable.zhongguo },
				{ 105, R.drawable.jianhang }, { 305, R.drawable.minsheng }, { 307, R.drawable.pingan }, { 308, R.drawable.zhaohang }, { 403, R.drawable.youzheng },
				{ 306, R.drawable.guandongfazhang }, { 314, R.drawable.shanghai }, { 317, R.drawable.wenzhou }, { 106, R.drawable.jiaohang }, };

		for (int i = 0; i < bank.length; i++) {
			String _bankCode = String.valueOf(bank[i][0]);
			if (_bankCode.equals(bankcode)) {
				return bank[i][1];
			}
		}
		return R.drawable.abs__ic_clear;
	}

	public static String getUserNo() {
		// TODO Auto-generated method stub
		return ApplicationParams.getInstance().getUserNo();
	}

}
