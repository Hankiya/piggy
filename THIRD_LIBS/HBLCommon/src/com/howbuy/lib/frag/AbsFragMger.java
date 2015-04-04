package com.howbuy.lib.frag;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.howbuy.lib.aty.AbsAty;
import com.howbuy.lib.utils.StrUtils;

/**
 * @author rexy 840094530@qq.com
 * @date 2014-3-21 上午11:06:34
 */
public abstract class AbsFragMger implements OnBackStackChangedListener {
	protected String TAG=null;
	protected FragmentManager mFragMger = null;
	protected FragmentActivity mAty = null;
	protected int mContentId = -1;
	protected FrameLayout mContent = null;
	protected AbsFrag mFragCur = null;
	private IFragChanged mListener=null;
	protected int mBackStackCount=0;
	public AbsFragMger(FragmentActivity aty, int contentId) {
		this(aty, (FrameLayout) aty.findViewById(contentId));
	}
	public AbsFragMger(FragmentActivity aty, FrameLayout content) {
		this.mAty = aty;
		this.mContent = content;
		this.mContentId = content.getId();
		this.mFragMger = aty.getSupportFragmentManager();
		mFragMger.addOnBackStackChangedListener(this);
		TAG=getClass().getSimpleName();
		if(aty instanceof IFragChanged){
			setOnFragChanged((IFragChanged) aty);
		}
	}
    
	public FrameLayout getContent(){
		return mContent;
	}
	public int getBackStackCount(){
		return mBackStackCount;
	}
	public AbsFrag getCurrentFrag(){
		return mFragCur;
	}
	public boolean onXmlBtClick(View v) {
		return mFragCur != null && mFragCur.onXmlBtClick(v);
	}

	public boolean onKeyBack(boolean fromBar,boolean isFirstPress, boolean isTwiceInTime,boolean isTwiceExitEnable) {
		boolean handled = mFragCur != null
				&& mFragCur.onKeyBack(fromBar,isFirstPress, isTwiceInTime);
		if (handled&&!fromBar) {
			AbsAty.resetTrace();
		} 
		return handled;
	}
	protected void notifyFragChanged(AbsFrag frag,String fragTag){
		if(mListener!=null&&frag!=null){
			mListener.onFragChanged(frag,fragTag);
		}
	}
	protected void notiffyFragStackChanged(int n){
		if(mListener!=null){
			mListener.onBackStackChanged(n);
		}
	}
	public boolean isContentVisible(){
		return mContent.getVisibility()==View.VISIBLE;
	}
	
	public void setContentVisible(int visibleType){
		if(visibleType==View.VISIBLE||visibleType==View.GONE||visibleType==View.INVISIBLE){
			if(mContent.getVisibility()!=visibleType){
				mContent.setVisibility(visibleType);
			 }
		}
	}
	
	public void setOnFragChanged(IFragChanged l){
		mListener=l;
	}
	
	public abstract void switchToFrag(FragOpt fo) ;
	
	public void removeFrag(FragOpt fo){
		if(fo==null){
			fo=new FragOpt(mFragCur, 0);
		}
		Fragment fg = fo==null?null:mFragMger.findFragmentByTag(fo.getTag());
		FragmentTransaction ft = mFragMger.beginTransaction();
		fo.addTransAnim(ft);
		if(fg!=null){
			if(fo.hasCache()){
				ft.hide(fg);
				ft.detach(fg);
			}else{
				ft.remove(fg);
			}
			fo.commit(ft);
		}
		
	}
	
	public interface IFragChanged{
		void onFragChanged(AbsFrag frag,String fragTag);
		void onBackStackChanged(int backCount);
	}
	
	
	
	public static class FragOpt {
		public static final int FRAG_POPBACK = 1;
		public static final int FRAG_CLEANTOP = 2;
		public static final int FRAG_CACHE = 4;
		public static final int FRAG_ALLOW_LOSS_STATE=8;
		private static final int FRAG_ANIM_CUSTOM = 16;//
		private static final int FRAG_ANIM_SYSTEM = 32;//

		private int mFlag = 0,mTargetFragRequesCode=0;
		private int mAnimCusIn = 0, mAnimCusOut = 0, mAnimSysType = 0;

		private String mFragName = null;
		private String mFragTag = null;
		private Fragment mFrag = null,mFragTarget;
		private Bundle mFragArg = null;

		public FragOpt(String fragName, int frag_flag) {
			this(null, fragName, null, null, frag_flag);
		}

		public FragOpt(String fragName, Bundle fragArg, int frag_flag) {
			this(null, fragName, null, fragArg, frag_flag);
		}

		public FragOpt(String fragName, String fragTag, Bundle fragArg,
				int frag_flag) {
			this(null, fragName, fragTag, fragArg, frag_flag);
		}

		public FragOpt(Fragment frag, int frag_flag) {
			this(frag, null, frag_flag);
		}

		public FragOpt(Fragment frag, String fragTag, int frag_flag) {
			this(frag, frag == null ? null : frag.getClass().getName(), fragTag,
					null, frag_flag);
		}

		private FragOpt(Fragment frag, String fragName, String fragTag,
				Bundle fragArg, int frag_flag) {
			this.mFrag = frag;
			this.mFragName = fragName;
			this.mFragTag = fragTag;
			this.mFragArg = fragArg;
			addAction(frag_flag);
		}

		public String getTag() {
			return StrUtils.isEmpty(mFragTag) ? mFragName : mFragTag;
		}

		public void setCustomAnim(int animIn, int animOut) {
			mAnimCusIn = animIn;
			mAnimCusOut = animOut;
			mFlag &= ~FRAG_ANIM_SYSTEM;
			mFlag |= FRAG_ANIM_CUSTOM;
		}

		public void setAnimSystem(int TRANSIT_FRAGMENT_X) {
			mAnimSysType = TRANSIT_FRAGMENT_X;
			mFlag &= ~FRAG_ANIM_CUSTOM;
			mFlag |= FRAG_ANIM_SYSTEM;
		}
		public void addAction(int frag_flag) {
			frag_flag &= (FRAG_CLEANTOP | FRAG_POPBACK|FRAG_CACHE | FRAG_ALLOW_LOSS_STATE);
			mFlag |= frag_flag;
		}

		public void subAction(int frag_flag) {
			frag_flag &= (FRAG_CLEANTOP | FRAG_POPBACK);
			mFlag &= ~frag_flag;
		}

		public void setFrag(String fragName, String fragTag) {
			if (mFragName == null || !mFragName.equals(fragName)) {
				mFragName = fragName;
				mFragTag = fragTag;
				mFrag = null;
			}
		}
		public void setTargetFrag(AbsFrag frag,int requestCode){
			this.mFragTarget=frag;
			this.mTargetFragRequesCode=requestCode;
		}
	    public boolean hasCache(){
	    	return FRAG_CACHE == (FRAG_CACHE & mFlag);
	    }
		public boolean hasBackStack() {
			return FRAG_POPBACK == (FRAG_POPBACK & mFlag);
		}

		public boolean hasCleanTop() {
			return FRAG_CLEANTOP == (FRAG_CLEANTOP & mFlag);
		}
		 
		public boolean addTransAnim(FragmentTransaction ft){
			if (FRAG_ANIM_SYSTEM == (FRAG_ANIM_SYSTEM & mFlag)) {
				ft.setTransition(mAnimSysType);
				return true;
			}
			if (FRAG_ANIM_CUSTOM == (FRAG_ANIM_CUSTOM & mFlag)) {
				ft.setCustomAnimations(mAnimCusIn, mAnimCusOut);
				return true;
			}
			return false;
		}
		public boolean addBackStack(FragmentTransaction ft) {
			if (hasBackStack()) {
				ft.addToBackStack(getTag());
				return true;
			}
			return false;
		}
	    public void commit(FragmentTransaction ft){
	    	if(FRAG_ALLOW_LOSS_STATE== (FRAG_ALLOW_LOSS_STATE & mFlag)){ 
	    		ft.commitAllowingStateLoss();
	    	}else{
	    		ft.commit();
	    	}
	    }
	    
		public Fragment getFrag(Context cx) {
			if (mFrag == null) {
				mFrag = Fragment.instantiate(cx, mFragName, mFragArg);
				mFrag.setTargetFragment(mFragTarget, mTargetFragRequesCode);
			}
			return mFrag;
		}
		
	}

}
