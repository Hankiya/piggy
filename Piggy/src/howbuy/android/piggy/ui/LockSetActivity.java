package howbuy.android.piggy.ui;

import howbuy.android.piggy.R;
import howbuy.android.piggy.UserUtil;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.help.ParseWebReqUri;
import howbuy.android.piggy.help.ParseWebReqUri.Web_Flag;
import howbuy.android.piggy.ui.base.AbstractBaseActivity;
import howbuy.android.piggy.widget.LockPatternView;
import howbuy.android.piggy.widget.LockPatternView.Cell;
import howbuy.android.piggy.widget.LockPatternView.OnPatternListener;
import howbuy.android.util.Cons;
import howbuy.android.util.MD5Utils;
import howbuy.android.util.SpannableUtil;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.umeng.analytics.MobclickAgent;

/**
 * 设置锁屏 用户可以选择跳过
 * 
 * @author Administrator
 * 
 */
public class LockSetActivity extends AbstractBaseActivity implements OnClickListener {
	public static final String NAME = "yescpu";
	public static final int Type_Setting = 0;
	public static final int Type_Login = 1;
	private LockPatternView lockPatternBig;
	private LockPatternView lockPatternSmall;
	private TextView patternHint;
	private TextView patternReset;
	private TextView patternSkip;
	private int mForwordType;

	/**
	 * 0:初始化 1：已经输了第一遍 2：完成
	 */
	private int patternStatus = 0;
	private ArrayList<Cell> firstCells;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
//			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if ((mForwordType& Web_Flag._flagLogin) == Web_Flag._flagLogin) {
			return;
		}else if (mForwordType == Type_Login) {
//			luncherMain();
			MobclickAgent.onEvent(this, Cons.EVENT_UI_SetLock, "请设置手势密码");
			return;
		} else {
			setResult(RESULT_CANCELED);
			finish();
		}
		MobclickAgent.onEvent(this, Cons.EVENT_UI_SetLock, "跳过设置");
		super.onBackPressed();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setTitle(R.string.activity_set_lock_title);
		mForwordType = getIntent().getIntExtra(Cons.Intent_type, Type_Setting);

		patternHint = (TextView) findViewById(R.id.sethint);
		patternReset = (TextView) findViewById(R.id.sethint_reset);
		patternSkip = (TextView) findViewById(R.id.skipset);
		patternReset.setText(SpannableUtil.all(getString(R.string.setpatternreset), 16, R.color.windowscolor, false));
		patternSkip.setText(SpannableUtil.all(getString(R.string.setpatternskip), 16, R.color.windowscolor, false));
		patternReset.setOnClickListener(this);
		patternSkip.setOnClickListener(this);

		lockPatternBig = (LockPatternView) findViewById(R.id.lockPatternBig);
		lockPatternSmall = (LockPatternView) findViewById(R.id.lockPatternSmall);
		lockPatternSmall.setType_Patten(LockPatternView.type_Small);

		lockPatternBig.setmOnPatternListener(new OnPatternListener() {

			@Override
			public void onPatternDetected(List<Cell> pattern) {
				// TODO Auto-generated method stub

				Log.d(NAME, "onPatternDetected--" + pattern.toString());
				if (patternStatus == 0) {
					if (pattern != null && pattern.size() < 4) {
						patternHint.setText(R.string.setpatternlimiterror);
					} else {
						patternStatus = 1;
						firstCells = new ArrayList<LockPatternView.Cell>(pattern);
						patternHint.setText(R.string.setpatternagain);
						lockPatternSmall.setType_Patten(LockPatternView.type_Small);
						lockPatternSmall.setPattern(firstCells);
					}
				} else if (patternStatus == 1) {
					if (firstCells.toString().equals(pattern.toString())) {
						patternStatus = 2;
						savePattern();
						showToastShort("已经设置锁屏成功");
						luncherMain();
					} else {
						patternHint.setText(R.string.setpatternagainerror);
						patternReset.setVisibility(View.VISIBLE);
					}
				}
				// lockPatternView.clearPattern();
				// lockPatternView.clearPattern();

			}
		});

		if (mForwordType == Type_Setting) {
			patternSkip.setVisibility(View.GONE);
		}

	}

	private void luncherMain() {
		Intent startIntent = new Intent();
		if (Type_Setting == mForwordType) {
			setResult(Activity.RESULT_OK);
			ParseWebReqUri.jumpWebPage(this, Activity.RESULT_OK, Web_Flag.Flag_SaveMoney);
		} else if ((mForwordType & Web_Flag.Flag_login) == Web_Flag.Flag_login) {
			setResult(Activity.RESULT_OK);
			ParseWebReqUri.jumpWebPage(this, Activity.RESULT_OK, Web_Flag.Flag_SaveMoney);
		} else if ((mForwordType & Web_Flag.Flag_SaveMoney) == Web_Flag.Flag_SaveMoney) {
			startIntent.putExtra(Cons.Intent_type,Web_Flag.Flag_SaveMoney );
			if (UserUtil.isBindBank()) {
				startIntent.setClass(this, SaveMoneyActivity.class);
				startActivity(startIntent);
			}else {
				startIntent.setClass(this, BindCardActivity.class);
				startActivity(startIntent);
			}
		} else if ((mForwordType & Web_Flag.Flag_UserINfo) == Web_Flag.Flag_UserINfo) {
			setResult(Activity.RESULT_OK);
			ParseWebReqUri.jumpWebPage(this, Activity.RESULT_OK, Web_Flag.Flag_SaveMoney);
		} else {
			startIntent.setClass(LockSetActivity.this, ProPertyActivity.class);
			startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(startIntent);
			overridePendingTransition(R.anim.page_push_out, R.anim.page_push_in);
		}
		finish();
		// 是否设置键盘锁
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// d("userutil------"+ApplicationParams.getInstance().getmParameter().getUserInfo().toString());
	};

	/**
	 * 保存patten密码
	 */
	private void savePattern() {
		String gsonCells = firstCells.toString();
		gsonCells = MD5Utils.toMD5(gsonCells);
		ApplicationParams.getInstance().getsF().edit().putString(Cons.SFPatternValue, gsonCells).putBoolean(Cons.SFPatternFlag, true).commit();
	}

	@Override
	public void initUi(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.aty_lockset);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.skipset:
			ApplicationParams.getInstance().getsF().edit().putBoolean(Cons.SFPatternFlag, false).commit();
			luncherMain();
			MobclickAgent.onEvent(this, Cons.EVENT_UI_SetLock, "跳过设置");
			break;
		case R.id.sethint_reset:
			firstCells.clear();
			patternStatus = 0;
			patternReset.setVisibility(View.GONE);
			patternHint.setText(R.string.setpattern);
			lockPatternSmall.clearPattern();
			MobclickAgent.onEvent(this, Cons.EVENT_UI_SetLock, "重新设置");
			break;

		default:
			break;
		}
	}

}
