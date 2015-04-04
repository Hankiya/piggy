package howbuy.android.piggy.ui.base;

import android.os.Bundle;
import android.view.View.OnClickListener;

import com.actionbarsherlock.view.MenuItem;

public abstract class AbstractNomalActivity extends AbstractBaseHaveLockActivity implements OnClickListener {

	/**
	 * 设置ActionBar
	 */
	protected abstract void onAbsBuildActionBar();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		onAbsBuildActionBar();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);

	}
}
