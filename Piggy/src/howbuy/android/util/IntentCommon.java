package howbuy.android.util;

import howbuy.android.piggy.api.dto.UserCardDto;
import howbuy.android.piggy.api.dto.UserCardListDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.ui.AbsFragmentActivity;
import howbuy.android.piggy.ui.OutMoneyActivity;
import howbuy.android.piggy.ui.fragment.UserCardSelectFragment;
import android.content.Context;
import android.content.Intent;

public class IntentCommon {
	
	/**
	 * 跳转到取钱
	 * @param context
	 * @param intoType
	 * @param flags
	 */
	public static final void intentToOutMoney(Context context, int intoType, int flags) {
		UserCardListDto cards=ApplicationParams.getInstance().getPiggyParameter().getUserCardInfo();
		Intent intent;
		if (cards.getUserCardDtos().size()>1) {
			intent = new Intent(context, AbsFragmentActivity.class);
			intent.putExtra(Cons.Intent_name, UserCardSelectFragment.class.getName());
		}else {
			intent = new Intent(context, OutMoneyActivity.class);
			UserCardDto uCardDto=cards.getDefaultCard();
			intent.putExtra(Cons.Intent_bean, uCardDto);
		}
		if (intoType != 0) {
			intent.putExtra(Cons.Intent_type, intoType);
		}
		if (flags != 0) {
			intent.addFlags(flags);
		}
		context.startActivity(intent);
	}
}
