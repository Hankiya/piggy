package howbuy.android.piggy.ui.base;


/**
 * 双击退出
 */
public abstract class AbstractDoubleBackActivity extends AbstractBaseHaveLockActivity {
	public static final String NAME = "储蓄罐";
	private long mAgoPress;
	private long mCurrPress;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		mCurrPress = System.currentTimeMillis();
		boolean c = mCurrPress - mAgoPress < 3000;
		if (c) {
			super.onBackPressed();
		} else {
			showToastShort("再按一次退出"+NAME+"！");
		}
		mAgoPress = mCurrPress;
		//super.onBackPressed();
	}
	
	
}
