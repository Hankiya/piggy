package com.howbuy.lib.adp;

import android.view.View;

public abstract class AbsViewHolder<T>{
 public T mItem=null;
 public int mIndex=-1,mType=0;
 
 protected final void initHolderData(int index, int type, T item,boolean isReuse){
	 mIndex=index;  mType=type;  mItem=item;
	 initData(index, type, item, isReuse);
 }
 
 protected abstract void initView(View root,int type);
 
 protected abstract void initData(int index, int type, T item,boolean isReuse);
 
 public int changeView(int arg){return arg;};
}
