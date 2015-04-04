package howbuy.android.piggy.wxapi;

import android.os.Bundle;
import android.util.Log;

import com.umeng.socialize.weixin.view.WXCallbackActivity;


public class WXEntryActivity extends WXCallbackActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Log.d("weixin", "onCreate");
	}
}
