package com.howbuy.lib.entity;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcelable;
/**
 * @author rexy  840094530@qq.com 
 * @date 2013-12-9 下午11:22:16 
 * @param <T>
 * @param <K>
 */
public abstract class AbsLoadList<T, K> extends AbsLoadItem  implements Parcelable{
	protected int mTotal = 0;
	protected final List<T> mList = new ArrayList<T>();

	public AbsLoadList(){
		
	}
	public int getTotalNum() {
		return mTotal;
	}

	public void setTotalNum(int total_number) {
		this.mTotal = total_number;
	}

	public int size() {
		return mList.size();
	}

	public T getItem(int pos) {
		return mList.get(pos);
	}

	public List<T> getItems() {
		return mList;
	}

	public void addItem(T value) {
		mList.add(value);
	}

	public void addItem(T value, boolean isEnd) {
		if (isEnd) {
			mList.add(value);
		} else {
			mList.add(0, value);
		}
	}

	public boolean addItem(T value, int index) {
		if (index >= 0 && index < size()) {
			mList.add(index, value);
			return true;
		}
		return false;
	}
	public boolean insertItem(T item, int where) {

		if (where < 0 || where > size()) {
			return false;
		} else {
			mList.add(where, item);
			return true;
		}
	}

	public int indexOf(Object item) {
		return mList.indexOf(item);
	}
	

	public boolean setItem( int  index,T value) {
		if (index >= 0 && index < size()) {
			  mList.set(index, value);
			  return true;
		}
         return false;
	}
	public T remove(int which){
		if (which >= 0 && which < size()) {
			return  mList.remove(which);
		}
		return null;
	}
	
	public boolean remove(T item){
		return  mList.remove(item);
	}
	public void replaceAll(K valueList) {
		clear();
		addItems(valueList);
	}

	public void clear() {
		mList.clear();
		mTotal=0;
	}
    public void addItems(List<T> list,boolean isEnd){
    	if(isEnd){
    		mList.addAll(list);
    	}else{
    		mList.addAll(0,list);
    	}
    }
	@Override
	public int describeContents() {
		return 0;
	}
	
	public abstract void addItems(K valueList);
}
