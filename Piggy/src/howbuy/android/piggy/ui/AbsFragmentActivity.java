package howbuy.android.piggy.ui;

import howbuy.android.piggy.R;
import howbuy.android.piggy.ui.base.AbstractBaseHaveLockActivity;
import howbuy.android.piggy.ui.base.AbstractFragment;
import howbuy.android.util.Cons;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * 所有fragment共用
 * 
 * @author TANG
 * 
 */
public class AbsFragmentActivity extends AbstractBaseHaveLockActivity {
	public static final String ActionAbsFragmentOnBack = "ActionAbsFragmentOnBack";
	private String frgName;
	AbstractFragment absFrag;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		Bundle b = getIntent().getExtras();
		if (b != null) {
			outState.putAll(b);
		}
	}

	@Override
	public void initUi(Bundle savedInstanceState) {
		setContentView(R.layout.content_frame);
		Bundle b;
		if (savedInstanceState != null) {
			b = savedInstanceState;
		} else {
			b = getIntent().getExtras();
		}
		frgName = b.getString(Cons.Intent_name);
		// TODO Auto-generated method stub
		if (savedInstanceState == null) {
			absFrag= (AbstractFragment) Fragment.instantiate(this, frgName);
			absFrag.setArguments(b);
			getSupportFragmentManager().beginTransaction().add(R.id.content_frame, absFrag, frgName).commit();
		}
	}

	@Override
	public void onBackPressed() {
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
