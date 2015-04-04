 
package com.howbuy.lib.control;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;

import com.howbuy.lib.control.ElasticLayout.IScrollable;

/**
 * A container that places a masking view on top of all other views.  The masking view can be
 * faded in and out.  Currently, the masking view is solid color white.
 */
/**
 * @author rexy  840094530@qq.com 
 * @date 2014-1-3 下午2:04:59
 */
@SuppressLint("NewApi")
public class AnimAlphaFrame extends FrameLayout implements AnimationListener,IScrollable{
    private View mMaskingView;
    private ObjectAnimator mAnimator;

    public AnimAlphaFrame(Context context) {
        this(context, null, 0);
    }

    public AnimAlphaFrame(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimAlphaFrame(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMaskingView = new View(getContext());
        mMaskingView.setVisibility(View.INVISIBLE);
        mMaskingView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        mMaskingView.setBackgroundColor(Color.WHITE);
        addView(mMaskingView);
        mMaskingView.bringToFront();
    }

    public void setMaskBackgroud(int color){
    	  mMaskingView.setBackgroundColor(color);
    }
    public void setMaskBackgroudResource(int resid){
  	  mMaskingView.setBackgroundResource(resid);
  }
    public void setMaskVisibility(boolean flag) {
        if (flag) {
        	if(android.os.Build.VERSION.SDK_INT>11){
        		  mMaskingView.setAlpha(1.0f);	
        	}
            mMaskingView.setVisibility(View.VISIBLE);
        } else {
            mMaskingView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Starts the transition of showing or hiding the mask. To the user, the view will appear to
     * either fade in or out of view.
     *
     * @param showMask If true, the mask the mask will be set to be invisible then fade into hide
     * the other views in this container. If false, the the mask will be set to be hide other
     * views initially.  Then, the other views in this container will be revealed.
     * @param duration The duration the animation should last for. If -1, the system default(300)
     * is used.
     */
    public void startMaskTransition(boolean showMask, int duration) {
    	if(android.os.Build.VERSION.SDK_INT>=14){
            // Stop any animation that may still be running.
            if (mAnimator != null && mAnimator.isRunning()) {
                mAnimator.end();
            }
            mMaskingView.setVisibility(View.VISIBLE);
            if (showMask) {
                mAnimator = ObjectAnimator.ofFloat(mMaskingView, View.ALPHA, 0.0f, 1.0f);
            } else {
                // asked to hide the view
                mAnimator = ObjectAnimator.ofFloat(mMaskingView, View.ALPHA, 1.0f, 0.0f);
            }
            if (duration != -1) {
                mAnimator.setDuration(duration);
            }
            mAnimator.start();	
    	}else{
    		AlphaAnimation anim=null;
    		if(showMask){
    			anim=new AlphaAnimation(0, 1);
    		}else{
    			anim=new AlphaAnimation(1, 0);
    		}
    		anim.setDuration(duration);
    		mMaskingView.startAnimation(anim);
    	}
    }
    
    public void startMaskTransition(int duration) {
      	if(android.os.Build.VERSION.SDK_INT>11){
  		  mMaskingView.setAlpha(1f);	
  	    }
    	AnimationSet a=new AnimationSet(true);
    	AlphaAnimation anim=new AlphaAnimation(0, 1);
    	anim.setDuration(duration>>1);
    	anim.setStartOffset(0);
    	anim.setFillAfter(false);
    	a.addAnimation(anim);
    	anim=new AlphaAnimation(1, 0);
    	anim.setDuration(duration);
    	anim.setStartOffset(duration>>1);
    	anim.setFillAfter(true);
    	a.addAnimation(anim);
    	a.setAnimationListener(this);
        mMaskingView.startAnimation(a);
    }

	@Override
	public void onAnimationStart(Animation animation) {
	}

	@Override
	public void onAnimationEnd(Animation animation) {
	   	if(android.os.Build.VERSION.SDK_INT>11){
	   		  mMaskingView.setVisibility(View.INVISIBLE);
	  		  mMaskingView.setAlpha(1f);	
	    }
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}

	@Override
	public boolean isScrollable(int scrollType) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean hasScroll(int val, int Type) {
		// TODO Auto-generated method stub
		return true;
	}
}
