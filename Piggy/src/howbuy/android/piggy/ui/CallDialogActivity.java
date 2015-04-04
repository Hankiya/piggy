package howbuy.android.piggy.ui;

import howbuy.android.piggy.ui.base.AbstractBaseHaveLockActivity;
import howbuy.android.util.Cons;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

public abstract class CallDialogActivity extends AbstractBaseHaveLockActivity {

	public void onClickCallDialog(View view) {
		Uri callUri = Uri.parse("tel:" + "4007009665");
		Intent returnIt = new Intent(Intent.ACTION_DIAL, callUri);
		startActivity(returnIt);
		String res = "";
		String className = getClass().getSimpleName();
		if (className.equals(RegisterActivity.class.getSimpleName())) {
			res = "注册1";
		} else if (className.equals(SaveMoneyActivity.class.getSimpleName())) {
			res = "存钱";
		} else if (className.equals(OutMoneyActivity.class.getSimpleName())) {
			res = "取钱";
		}
		MobclickAgent.onEvent(this, Cons.EVENT_Event_CallSP, res);
	}

}
