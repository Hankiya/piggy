package howbuy.android.piggy.ui;

import howbuy.android.piggy.R;
import howbuy.android.piggy.ui.base.AbstractBaseHaveLockActivity;
import howbuy.android.piggy.ui.base.AbstractFragment;
import howbuy.android.piggy.ui.fragment.BindCardSucceedFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BindCardSucceedActivity extends AbstractBaseHaveLockActivity {
	AbstractFragment absFrag;
	
	@Override
	public void initUi(Bundle savedInstanceState) {
		setContentView(R.layout.content_frame);
		// TODO Auto-generated method stub
		if (savedInstanceState == null) {
			absFrag= (AbstractFragment) Fragment.instantiate(this, BindCardSucceedFragment.class.getName());
			getSupportFragmentManager().beginTransaction().add(R.id.content_frame, absFrag).commit();
		}
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
	}
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();

		// TODO Auto-generated method stub
		if (absFrag!=null) {
			if (!absFrag.onBackPressed()) {
				super.onBackPressed();
			}
		}else {
			super.onBackPressed();
		}
		super.onBackPressed();
	
	}
}
