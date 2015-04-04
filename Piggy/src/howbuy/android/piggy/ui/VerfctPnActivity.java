package howbuy.android.piggy.ui;

import howbuy.android.piggy.R;
import howbuy.android.piggy.UserUtil;
import howbuy.android.piggy.api.dto.UserInfoDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.ui.base.AbstractBaseActivity;
import howbuy.android.piggy.ui.fragment.LoginFragment;
import howbuy.android.piggy.widget.LockPatternView;
import howbuy.android.piggy.widget.LockPatternView.Cell;
import howbuy.android.piggy.widget.LockPatternView.OnPatternListener;
import howbuy.android.util.Cons;
import howbuy.android.util.MD5Utils;
import howbuy.android.util.SpannableUtil;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.umeng.analytics.MobclickAgent;

/**
 * 用户验证
 * 
 * @author Administrator
 * 
 */
public class VerfctPnActivity extends AbstractBaseActivity implements OnClickListener {
	/**
	 * 从启动页面过来
	 */
	public static final int VerfctPnType_Lanucher = 0;
	/**
	 * 从主页面过来
	 */
	public static final int LoginType_Main = 1;

	/**
	 * 锁屏页面
	 */
	public static final int Type_VerfctPn = 2;

	public static final String NAME = "pcyan";
	public static final int maxErrorNumber = 5;
	private LockPatternView lockPatternView;
	private TextView patternHint;
	private TextView patternUserName;
	private TextView patternReset;
	private TextView patternSkip;
	/**
	 * 0:初始化 1：已经输了第一遍 2：完成
	 */
	private int remainingNumberOf = 0;
	private String originCells;
	private int mLoginType = LoginType_Main;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		verfctPnBack();
	}

	private void verfctPnBack() {
		Intent MyIntent = new Intent(Intent.ACTION_MAIN);
		MyIntent.addCategory(Intent.CATEGORY_HOME);
		startActivity(MyIntent);
		finish();
		boolean pattenFlag = ApplicationParams.getInstance().getsF().getBoolean(Cons.SFPatternFlag, false);
		if (mLoginType== VerfctPnActivity.Type_VerfctPn&&pattenFlag && UserUtil.isLogin()) {
			ApplicationParams.getInstance().setNeedPattern(true);
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		mLoginType= getIntent().getIntExtra(Cons.Intent_type, LoginType_Main);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getSupportActionBar().setTitle(R.string.ac_verfctpn);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		mLoginType = getIntent().getIntExtra(Cons.Intent_type, LoginType_Main);
		if (mLoginType == LockSetActivity.Type_Login) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		}

		patternHint = (TextView) findViewById(R.id.sethint);
		patternReset = (TextView) findViewById(R.id.sethint_reset);
		patternSkip = (TextView) findViewById(R.id.skipset);
		patternUserName = (TextView) findViewById(R.id.setuser);
		patternSkip.setText(SpannableUtil.all(getString(R.string.forgetpattern), 16, R.color.windowscolor, false));
		patternReset.setOnClickListener(this);
		patternSkip.setOnClickListener(this);

		lockPatternView = (LockPatternView) findViewById(R.id.lockPatternView1);

		lockPatternView.setmOnPatternListener(new OnPatternListener() {

			@Override
			public void onPatternDetected(List<Cell> pattern) {
				// TODO Auto-generated method stub
				Log.d(NAME, "onPatternDetected--" + pattern.toString());
				String currPatternToMd5 = MD5Utils.toMD5(pattern.toString());
				if (originCells != null && pattern != null && currPatternToMd5.toString().equals(originCells)) {
					MobclickAgent.onEvent(VerfctPnActivity.this, Cons.EVENT_UI_UnLock, "解锁成功");
					// 验证通过
					// lockPatternView.clearPattern();
					int type=getIntent().getIntExtra(Cons.Intent_type, LoginType_Main);
					if ( type== Type_VerfctPn) {
						if (ApplicationParams.getInstance().getActivity() instanceof ProPertyActivity) {
							ApplicationParams.getInstance().setNeedPattern(false);//确保不再二次启动这个页面
							lucncherMain();
						}else {
							lucncherP();
						}
						return;
					}

					if (type == VerfctPnType_Lanucher) {
						lucncherMain();
					}
				} else {
					// 验证不通过
					// lockPatternView.clearPattern();
					MobclickAgent.onEvent(VerfctPnActivity.this, Cons.EVENT_UI_UnLock, "解锁失败");
					remainingNumberOf++;
					if (remainingNumberOf < maxErrorNumber) {
						patternHint.setText("手势密码错误，您还可以输入" + (maxErrorNumber - remainingNumberOf) + "次");
					} else {
						new AlertDialog.Builder(VerfctPnActivity.this).setMessage("您已经连续5此输错手势，手势密码已经关闭，请重新登录。").setCancelable(false)
								.setPositiveButton("确认", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										lucncherLogin();
									}
								}).create().show();
					}
				}
			}
		});

		setUserName();
		originCells = getPattern();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	/**
	 * 设置用户名
	 */
	private void setUserName() {
		UserInfoDto uInfoDto = ApplicationParams.getInstance().getPiggyParameter().getUserInfo();
		if (uInfoDto == null) {
			return;
		}
		String uName = uInfoDto.getCustName();
		if (!TextUtils.isEmpty(uName)) {
			patternUserName.setText("您好, " + uName );
		}
	}

	private String getPattern() {
		String patter = ApplicationParams.getInstance().getsF().getString(Cons.SFPatternValue, null);
		return patter;
	}

	@Override
	public void initUi(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.aty_verfctpn);
	}
	
	private void lucncherMain(){
		Intent intent = new Intent(VerfctPnActivity.this, ProPertyActivity.class);
		intent.putExtra(Cons.Intent_type, ProPertyActivity.NeeduserDataReload);
		startActivity(intent);
		finish();
	}
	
	private void lucncherP(){
		finish();
	}
	
	private void lucncherLogin(){
		ApplicationParams.getInstance().getsF().edit().remove(Cons.SFUserCusNo).remove(Cons.SFPatternValue).remove(Cons.SFPatternFlag).remove(Cons.SFLoginOutFlag).remove(Cons.SFPatternForgetFlag).remove(Cons.SFBindCardParams).commit();
		ApplicationParams.getInstance().getPiggyParameter().removeUserDataPrivate();
		Intent intent = new Intent(VerfctPnActivity.this, LoginActivity.class);
		intent.putExtra(Cons.Intent_type, LoginFragment.LoginType_VerfctPn);
		startActivity(intent);
		finish();
	}  

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.skipset://忘记密码
			lucncherLogin();
			MobclickAgent.onEvent(this, Cons.EVENT_UI_UnLock, "忘记密码");
			break;
		default:
			break;
		}
	}

}
