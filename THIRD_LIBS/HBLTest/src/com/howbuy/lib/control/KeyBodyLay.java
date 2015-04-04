package com.howbuy.lib.control;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout; 

 public class KeyBodyLay extends LinearLayout {
	private IKeyBordLayoutChanged mLayChangeListener = null;

	public void setOnLayChangeListener(IKeyBordLayoutChanged layChangeListener) {
		mLayChangeListener = layChangeListener;
	}

	public KeyBodyLay(Context context) {
		this(context, null);
	}

	public KeyBodyLay(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			if (mLayChangeListener != null) {
				mLayChangeListener.onLayChange(l, t, r, b);
			}
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (mLayChangeListener != null) {
			mLayChangeListener.onSizeChange(w, h, oldw, oldh);
		}
	}

	public interface IKeyBordLayoutChanged {
		void onLayChange(int l, int t, int r, int b);
		void onSizeChange(int w, int h, int oldw, int oldh);
	}
}