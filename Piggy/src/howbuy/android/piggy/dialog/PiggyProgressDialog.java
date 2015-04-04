package howbuy.android.piggy.dialog;

import howbuy.android.piggy.R;
import howbuy.android.util.AndroidUtil;
import howbuy.android.util.Screen;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class PiggyProgressDialog extends AlertDialog {
	public static final int TypeNormal = 1;
	public static final int TypeLoginRegister = 2;
	public static final int TypeMoney = 3;
	private ImageView mProgressImg;
	private ImageView mProgressMoneyImg;
	private AnimationDrawable animationDrawable;
	private Context mContext;
	private int progressType = TypeNormal;

	public PiggyProgressDialog(Context context) {
		super(context);
		this.mContext = context;
		// TODO Auto-generated constructor stub
	}

	public PiggyProgressDialog(Activity context) {
		super(context);
		this.mContext = context;
		// TODO Auto-generated constructor stub
	}

	public PiggyProgressDialog(Activity context, int type) {
		super(context);
		this.mContext = context;
		// TODO Auto-generated constructor stub
		this.progressType = type;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_progress);
		mProgressImg = (ImageView) findViewById(R.id.progressimg);
		mProgressMoneyImg = (ImageView) findViewById(R.id.progressimg_money);
		AndroidUtil.hideIme((Activity) mContext);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		Screen s = new Screen(getContext());
		if (progressType == TypeMoney) {
			mProgressImg.setVisibility(View.INVISIBLE);
			mProgressMoneyImg.setVisibility(View.VISIBLE);
			startAnimation1(R.drawable.progress_piggy_down);
		} else if (progressType == TypeLoginRegister) {
			mProgressImg.setVisibility(View.VISIBLE);
			mProgressMoneyImg.setVisibility(View.INVISIBLE);
			mProgressImg.getLayoutParams().width = s.dip2px(s.getWidth());
			mProgressImg.getLayoutParams().height = s.dip2px(s.getWidth() / 7);
			startAnimation(R.drawable.progress_piggy_wait);
		} else {
			mProgressImg.setVisibility(View.VISIBLE);
			mProgressMoneyImg.setVisibility(View.INVISIBLE);
			mProgressImg.getLayoutParams().width = s.dip2px(80);
			mProgressImg.getLayoutParams().height = s.dip2px(80);
			startAnimation(R.drawable.progress_piggy_up);
		}

	}

	private void startAnimation(int id) {
		mProgressImg.setImageResource(id);
		animationDrawable = (AnimationDrawable) mProgressImg.getDrawable();
		animationDrawable.start();
	}

	public void startAnimation1(int id) {
		mProgressImg.setVisibility(View.INVISIBLE);
		mProgressMoneyImg.setVisibility(View.VISIBLE);
//		startAnimation1(R.drawable.progress_piggy_down);
		mProgressMoneyImg.setImageResource(id);
		animationDrawable = (AnimationDrawable) mProgressMoneyImg.getDrawable();
		animationDrawable.start();
	}

	public int getProgressType() {
		return progressType;
	}

	public void setProgressType(int progressType) {
		this.progressType = progressType;
	}

}
