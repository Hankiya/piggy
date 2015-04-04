package howbuy.android.piggy.ui;

import howbuy.android.piggy.R;
import howbuy.android.piggy.ui.base.AbstractBaseHaveLockActivity;
import howbuy.android.piggy.ui.fragment.QueryFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class QueryActivity extends AbstractBaseHaveLockActivity {

	@Override
	public void initUi(Bundle savedInstanceState) {
		setContentView(R.layout.content_frame);

		// TODO Auto-generated method stub
		if (savedInstanceState == null) {
			Fragment f = Fragment.instantiate(this, QueryFragment.class.getName());
			Bundle b = getIntent().getExtras();
			f.setArguments(b);
			getSupportFragmentManager().beginTransaction().add(R.id.content_frame, f).commit();
		}
	}

}
