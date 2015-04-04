package howbuy.android.piggy.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * 
 * @author yesCpu 倒计时按钮
 * 
 */
public class CountDownButton extends Button {
	private static final long millisInFuture = 60 * 1000;
	private static final long countDownInterval = 1000;
	private String defaultText = "获取验证码";

	public CountDownButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CountDownButton(Context context, AttributeSet attrs) { 
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	CountDownTimer cTimer = new CountDownTimer(millisInFuture, countDownInterval) {

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			setText(String.valueOf(millisUntilFinished / 1000) + "秒后重发");
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			setEnabled(true);
			setText(defaultText);

		}
	};

	public void startCountDown() {
		setEnabled(false);
		System.out.println("start");
		cTimer.start();
	}

	public void stopCountDown() {
		cTimer.cancel();
	}
	
	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
		if (cTimer!=null) {
			cTimer.cancel();
		}
	}
	
}
