package howbuy.android.piggy.ui;

import howbuy.android.piggy.R;
import howbuy.android.piggy.ui.base.AbstractBaseHaveLockActivity;
import howbuy.android.piggy.ui.fragment.BindCardFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BindCardActivity extends AbstractBaseHaveLockActivity {

	@Override
	public void initUi(Bundle savedInstanceState) {
		setContentView(R.layout.content_frame);
		// TODO Auto-generated method stub
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.content_frame, Fragment.instantiate(this, BindCardFragment.class.getName())).commit();
		}
	}

}
