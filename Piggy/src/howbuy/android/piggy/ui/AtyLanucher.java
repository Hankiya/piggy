package howbuy.android.piggy.ui;

import howbuy.android.piggy.R;
import howbuy.android.piggy.UserUtil;
import howbuy.android.piggy.ad.PushDispatch;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.service.ServiceMger.TaskBean;
import howbuy.android.piggy.service.UpdateUserDataService;
import howbuy.android.piggy.sound.SoundUtil;
import howbuy.android.piggy.ui.base.AbstractBaseActivity;
import howbuy.android.piggy.ui.fragment.LoginFragment;
import howbuy.android.piggy.widget.ImageTextBtn;
import howbuy.android.util.Cons;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.IconPagerAdapter;

public class AtyLanucher extends AbstractBaseActivity implements OnClickListener {
	public static final String TAG = "lucher";
	public static final long TimeLauncherMax = 3000;
	public static final long TimeLauncherMini = 3000;
	public long progressTime = 0;
	private ViewPager mViewPager;
	private CirclePageIndicator mIndicator;
	private RelativeLayout mLuncherLay, mGuadeLay;
	private TextView mVersion;
	private ImageTextBtn subImgBtn;
	private ViewSwitcher mvSwitcher;
	private boolean isHand;
	private Handler tHandler;
	private ContentValues mBasicResValue;
	private ProgressDialog mpDialog;
	private boolean isClick = false;
	private boolean isFirst = false;
	private SoundUtil mSoundUtil;
	private static AtyLanucher thisAc;
	private  Bundle pushBundle = null;
	private String JPUSHBUNDLE = "jpushBundle";


	private void luncherLogin() {
		ApplicationParams.getInstance().setFirstStart(false);
		// 是否设置键盘锁
		boolean pattenFlag = ApplicationParams.getInstance().getsF().getBoolean(Cons.SFPatternFlag, false);
		boolean loginOutFlag = ApplicationParams.getInstance().getsF().getBoolean(Cons.SFLoginOutFlag, false);
		boolean loginXXXXX = ApplicationParams.getInstance().getsF().getBoolean(Cons.SFLoginXXXXXX, false);

		if (mFisrtStart) {
			startActProper();
			return;
		}

		if (loginXXXXX) {
			startActLogin();
			return;
		}
		
		if (UserUtil.isLogin()) {
			if (pattenFlag) {
				startActVerfctPn();
			} else {
				startActLogin();
			}
		} else {
			startActProper();
		}

		// if (pattenFlag && UserUtil.isLogin()) {
		// startActVerfctPn();
		// } else {
		// if (loginOutFlag || pattenFlag == false) {
		// startActLogin();
		// } else {
		// startActProper();
		// }
		// }
		
	}

	/**
	 * 跳转到主页
	 */
	private void startActProper() {
		Intent startIntent;
		startIntent = new Intent(AtyLanucher.this, ProPertyActivity.class);
		startIntent.putExtra(Cons.Intent_type, ProPertyActivity.NeeduserDataReload);
		startIntent.putExtra(JPUSHBUNDLE, pushBundle);
		startActivity(startIntent);
		overridePendingTransition(R.anim.page_push_out, R.anim.page_push_in);
	}
	/**
	 * jpush点击跳转到主页
	 */
	private void jumpActProperFromJpush(Bundle pushBundle) {
		Intent startIntent;
		startIntent = new Intent(AtyLanucher.this, ProPertyActivity.class);
		startIntent.putExtra(JPUSHBUNDLE, pushBundle);
		startActivity(startIntent);
		overridePendingTransition(R.anim.page_push_out, R.anim.page_push_in);
	}

	/**
	 * 跳转到登录
	 */
	private void startActLogin() {
		Intent startIntent;
		startIntent = new Intent(AtyLanucher.this, LoginActivity.class);
		startIntent.putExtra(Cons.Intent_type, LoginFragment.LoginType_Lanucher);
		startActivity(startIntent);
		overridePendingTransition(R.anim.page_push_out, R.anim.page_push_in);
	}

	/**
	 * 跳转到验证
	 */
	private void startActVerfctPn() {
		Intent startIntent;
		startIntent = new Intent(AtyLanucher.this, VerfctPnActivity.class);
		startIntent.putExtra(Cons.Intent_type, VerfctPnActivity.VerfctPnType_Lanucher);
		startActivity(startIntent);
		overridePendingTransition(R.anim.page_push_out, R.anim.page_push_in);
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == Cons.SHOW_SEPT) {// 产品基本信息更新完成
				luncherLogin();
				finish();
				this.removeCallbacksAndMessages(null);
			} else if (msg.what == Cons.SHOW_START) {// 第一次启动
				isFirst = true;
				mvSwitcher.showNext();
			} else if (msg.what == Cons.SHOW_SUCCESS) {// 未用
				luncherLogin();
				finish();
				this.removeCallbacksAndMessages(null);
			}
		};
	};

	private boolean mFisrtStart;

	@Override
	protected void onResume() {
		super.onResume();
		subImgBtn.setTextColor(getResources().getColor(android.R.color.black));
	};

	@Override
	public void onServiceRqCallBack(Intent taskData, boolean isCurrPage) {
		// TODO Auto-generated method stub
		super.onServiceRqCallBack(taskData, isCurrPage);
		Log.d("service", "onServiceRqCallBack--LanucherActivity");
		if (taskData.getStringExtra(Cons.Intent_type)==UpdateUserDataService.TaskType_AllNormal) {
			mBasicResValue=taskData.getParcelableExtra(Cons.Intent_bean);
			long b = System.currentTimeMillis();
			long jian = b - progressTime;
			if (jian > TimeLauncherMini) {
				mHandler.sendEmptyMessageDelayed(Cons.SHOW_SEPT, jian + 100);
			}
		}
	}
	
	@Override
	public boolean isShoudRegisterReciver() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		// 保证不再出现二次锁屏
		ApplicationParams.getInstance().setNeedPattern(false);
		progressTime = System.currentTimeMillis();
		HandlerThread thread = new HandlerThread(TAG);
		thread.start();
		tHandler = new Handler(thread.getLooper());
		mFisrtStart = ApplicationParams.getInstance().isFirstStart();
		// 如果用户登录已经登录则迅速去更新用户数据
		ApplicationParams.getInstance().getServiceMger().addTask(new TaskBean(UpdateUserDataService.TaskType_AllNormal, "0"));
		if (getIntent().getExtras() != null) {
			if (getIntent().getExtras().getString(PushDispatch.INTENT_ID) != null) {
				pushBundle = getIntent().getExtras();
				jumpActProperFromJpush(pushBundle);
				ProPertyActivity.jPushBundle = pushBundle;
				return;
			}			
		}else {
			pushBundle = null;
		}
		// 登录
		if (UserUtil.isLogin()) {
			ApplicationParams.getInstance().getServiceMger().addTask(new TaskBean(UpdateUserDataService.TaskType_UserInfo, "0"));
			ApplicationParams.getInstance().getServiceMger().addTask(new TaskBean(UpdateUserDataService.TaskType_UserCard, "0"));
			System.out.println("firsttimestart");
		}

		if (mFisrtStart) {
			mHandler.sendEmptyMessageDelayed(Cons.SHOW_START, TimeLauncherMini);
		} else {
			mHandler.sendEmptyMessageDelayed(Cons.SHOW_SEPT, TimeLauncherMini);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (tHandler != null) {
			tHandler.removeCallbacksAndMessages(null);
		}
		if (mHandler!=null) {
			mHandler.removeCallbacksAndMessages(null);
		}
	}

	@Override
	public void initUi(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.aty_lanucher);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mVersion = (TextView) findViewById(R.id.version);
		mGuadeLay = (RelativeLayout) findViewById(R.id.guide_lay);
		mLuncherLay = (RelativeLayout) findViewById(R.id.luncher_lay);
		mvSwitcher = (ViewSwitcher) findViewById(R.id.viewswidcher);
		subImgBtn = (ImageTextBtn) findViewById(R.id.submit_btn);
		subImgBtn.setOnClickListener(this);

		LanucherPager lanucherPager = new LanucherPager(getSupportFragmentManager());
		mViewPager.setAdapter(lanucherPager);
		mIndicator.setViewPager(mViewPager);

		AlphaAnimation alphaAnimation1 = new AlphaAnimation(1, 0);
		alphaAnimation1.setDuration(500);
		alphaAnimation1.setFillAfter(true);
		AlphaAnimation alphaAnimation2 = new AlphaAnimation(0, 1);
		alphaAnimation2.setDuration(1000);
		alphaAnimation2.setFillAfter(true);
		mvSwitcher.setInAnimation(alphaAnimation2);
		mvSwitcher.setOutAnimation(alphaAnimation1);

		thisAc = this;
	}

	public class LanucherPager extends FragmentPagerAdapter implements IconPagerAdapter {

		public LanucherPager(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public int getIconResId(int index) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return ImageFargment.newInstance(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 3;
		}

	}

	public static class ImageFargment extends Fragment {
		int mNum;

		/**
		 * Create a new instance of CountingFragment, providing "num" as an
		 * argument.
		 */
		static ImageFargment newInstance(int num) {
			ImageFargment f = new ImageFargment();
			// Supply num input as an argument.
			Bundle args = new Bundle();
			args.putInt("num", num);
			f.setArguments(args);
			return f;
		}

		/**
		 * When creating, retrieve this instance's number from its arguments.
		 */
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mNum = getArguments() != null ? getArguments().getInt("num") : 0;
		}

		/**
		 * The Fragment's UI is just a simple text view showing its instance
		 * number.
		 */
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.frag_guide, container, false);
			ImageView imageView = (ImageView) v.findViewById(R.id.icon);
			Button mButton = (Button) v.findViewById(R.id.button);
			switch (mNum) {
			case 0:
				mButton.setVisibility(View.GONE);
				v.setBackgroundResource(R.drawable.guide1_bg);
				imageView.setImageResource(R.drawable.start_one);
				break;
			case 1:
				mButton.setVisibility(View.GONE);
				v.setBackgroundResource(R.drawable.guide2_bg);
				imageView.setImageResource(R.drawable.start_two);
				break;
			case 2:
				mButton.setOnClickListener(thisAc);
				mButton.setVisibility(View.VISIBLE);
				v.setBackgroundResource(R.drawable.guide3_bg);
				imageView.setImageResource(R.drawable.start_three);
				break;

			default:
				break;
			}
			return v;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent it;
		switch (v.getId()) {
		case R.id.submit_btn:
			isClick = true;
			if (mBasicResValue == null && mFisrtStart) {
				// boolean
				// isBank=mBasicResValue.getAsBoolean(UpdateBasicDataService.ResBank);
				// boolean
				// isProduct=mBasicResValue.getAsBoolean(UpdateBasicDataService.ResProduct);
				// boolean
				// isRsaKey=mBasicResValue.getAsBoolean(UpdateBasicDataService.ResRsaKey);
				// boolean
				// isTday=mBasicResValue.getAsBoolean(UpdateBasicDataService.ResTDay);
				// if (isBank&&isProduct&&isRsaKey&&isTday) {
				showProgress("初始化加载中...");
				// }
			} else {
				luncherLogin();
			}
			break;
		case R.id.button:
			isClick = true;
			if (mBasicResValue == null && mFisrtStart) {
				// boolean
				// isBank=mBasicResValue.getAsBoolean(UpdateBasicDataService.ResBank);
				// boolean
				// isProduct=mBasicResValue.getAsBoolean(UpdateBasicDataService.ResProduct);
				// boolean
				// isRsaKey=mBasicResValue.getAsBoolean(UpdateBasicDataService.ResRsaKey);
				// boolean
				// isTday=mBasicResValue.getAsBoolean(UpdateBasicDataService.ResTDay);
				// if (isBank&&isProduct&&isRsaKey&&isTday) {
				showProgress("初始化加载中...");
				// }
			} else {
				luncherLogin();
			}
			break;
		default:
			break;
		}
	}

	public void showProgress(String msg) {
		mpDialog = new ProgressDialog(this);
		mpDialog.setMessage(msg);
		mpDialog.setCancelable(false);
		mpDialog.setCanceledOnTouchOutside(false);
		mpDialog.show();
	}

}
