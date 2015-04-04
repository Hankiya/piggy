package howbuy.android.piggy.widget;

import howbuy.android.piggy.R;
import android.content.Context;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoadingFooter {
	protected View mLoadingFooter;

	protected TextView mLoadingText;

	protected State mState = State.init;

	private ProgressBar mProgress;

	private long mAnimationDuration;

	public static enum State {
		init, TheEnd, Loading, Nodata, Error
	}

	public LoadingFooter(Context context) {
		mLoadingFooter = LayoutInflater.from(context).inflate(R.layout.loading_footer, null);
		mLoadingFooter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 屏蔽点击
			}
		});
		mProgress = (ProgressBar) mLoadingFooter.findViewById(R.id.progressBar);
		mLoadingText = (TextView) mLoadingFooter.findViewById(R.id.textView);
		mAnimationDuration = context.getResources().getInteger(android.R.integer.config_shortAnimTime);
		mLoadingText.setText("正在加载...");
		setState(State.init);
	}

	public View getView() {
		return mLoadingFooter;
	}

	public State getState() {
		return mState;
	}

	public void setState(final State state, long delay) {
		mLoadingFooter.postDelayed(new Runnable() {

			@Override
			public void run() {
				setState(state);
			}
		}, delay);
	}

	public void setState(State status) {
		if (mState == status) {
			return;
		}
		mState = status;

		mLoadingFooter.setVisibility(View.VISIBLE);

		switch (status) {
		case Loading:
			mLoadingText.setText("正在加载...");
			mLoadingText.setVisibility(View.VISIBLE);
			mProgress.setVisibility(View.VISIBLE);
			break;
		case Nodata:
			mLoadingText.setText("没有更多结果");
			mLoadingText.setVisibility(View.VISIBLE);
			mProgress.setVisibility(View.GONE);
			break;
		case TheEnd:
			mLoadingText.setText("无更多数据");
			mLoadingText.setVisibility(View.GONE);
//			mLoadingText.animate().withLayer().alpha(1).setDuration(mAnimationDuration);
			mProgress.setVisibility(View.GONE);
			break;
		case Error:
			mLoadingText.setText("加载出错");
			mLoadingText.setVisibility(View.VISIBLE);
			break;
		default:
			// mLoadingText.setVisibility(View.GONE);
			// mProgress.setVisibility(View.VISIBLE);
			break;
		}
	}
	
	

	public TextView getmLoadingText() {
		return mLoadingText;
	}

	public void setmLoadingText(TextView mLoadingText) {
		this.mLoadingText = mLoadingText;
	}

	public void setMessage(String msg) {
		mLoadingText.setText(msg);
		mLoadingText.setMovementMethod(null);
		mProgress.setVisibility(View.GONE);
	}
	
	public void setMessage(SpannableString msg) {
		mLoadingText.setText(msg);
		mLoadingText.setMovementMethod(LinkMovementMethod.getInstance());
		mProgress.setVisibility(View.GONE);
	}
}
