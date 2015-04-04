package howbuy.android.piggy.ui;

import howbuy.android.piggy.R;
import howbuy.android.piggy.ui.base.AbstractBaseActivity;
import howbuy.android.piggy.ui.fragment.LoginFragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class LoginActivity extends AbstractBaseActivity {

	@Override
	public void initUi(Bundle savedInstanceState) {
		setContentView(R.layout.content_frame);
		// TODO Auto-generated method stub
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.content_frame, Fragment.instantiate(this, LoginFragment.class.getName())).commit();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		System.out.println(newConfig);
	}
}
