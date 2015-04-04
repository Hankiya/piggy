package howbuy.android.piggy.ui;

import howbuy.android.piggy.R;
import howbuy.android.piggy.ui.fragment.RegisterFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class RegisterActivity extends CallDialogActivity {

	@Override
	public void initUi(Bundle savedInstanceState) {
		setContentView(R.layout.content_frame);
		// TODO Auto-generated method stub
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.content_frame, Fragment.instantiate(this, RegisterFragment.class.getName())).commit();
		}
	}
	
	

}
