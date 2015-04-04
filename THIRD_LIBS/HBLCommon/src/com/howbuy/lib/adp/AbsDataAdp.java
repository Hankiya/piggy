package com.howbuy.lib.adp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.database.DataSetObservable;
import android.database.DataSetObserver;

/**
 * 这个数据适配器是不需要提供界面,只提供数据的.
 * @author rexy 840094530@qq.com
 * @date 2014-3-24 下午4:38:53
 * @param <T>
 */
public   class AbsDataAdp<T> {
	private final DataSetObservable mDataSetObservable = new DataSetObservable();
	protected final ArrayList<T> mItems = new ArrayList<T>(5);

	public AbsDataAdp( ArrayList<T> items) {
		addItems(items, true, false);
	}

	/**
	 * add a new item to the list.
	 * 
	 * @param @param item
	 * @param @param isEnd true to add to the last position. false to add at the
	 *        head of list.
	 * @param @param notify true to notify the adapter data set has changed.
	 * @return void
	 * @throws
	 */
	public void addItem(T item, boolean isEnd, boolean notify) {
		if (item != null) {
			beforeAdd(item, null, isEnd);
			if (isEnd) {
				mItems.add(item);
			} else {
				mItems.add(0, item);
			}
			if (notify) {
				notifyDataChanged(false);
			}
		}
	}

	/**
	 * add a new item list to this adapter list.
	 * 
	 * @param @param items
	 * @param @param isEnd true to add to the last position. false to add at the
	 *        head of list.
	 * @param @param notify true to notify the adapter data set has changed.
	 * @return void
	 * @throws
	 */
	public void addItems(ArrayList<T> items, boolean isEnd, boolean notify) {
		if (items != null && items.size() > 0) {
			beforeAdd(null, items, isEnd);
			if (isEnd) {
				mItems.addAll(items);
			} else {
				mItems.addAll(0, items);
			}
			if (notify) {
				notifyDataChanged(false);
			}
		}
	}
	public boolean hasFlag(int pos,int flag){
		return false;
	}
	public boolean insertItem(T item, int where, boolean notify) {

		if (where < 0 || where > getCount()) {
			return false;
		} else {
			mItems.add(where, item);
			if (notify) {
				notifyDataSetChanged();
			}
			return true;
		}
	}

	public int indexOf(Object item) {
		return mItems.indexOf(item);
	}

	public void setItems(ArrayList<T> items, boolean notify) {
		mItems.clear();
		if (items != null && items.size() > 0) {
			addItems(items, true, notify);
		} else if (notify) {
			notifyDataChanged(false);
		}

	}

	/**
	 * set a new item to the list at the appoint position.
	 * 
	 * @param @param item
	 * @param @param notify true to notify the adapter data set has changed.
	 * @return true if set the data success .
	 */
	public boolean setItem(int index, T item, boolean notify) {
		if (item != null && index >= 0 && index < getCount()) {
			beforeAdd(item, null, false);
			mItems.set(index, item);
			if (notify) {
				notifyDataChanged(false);
			}
			return true;
		}
		return false;
	}

	/**
	 * remove a item from the list at a appoint position.
	 * 
	 * @param @param which
	 * @param @param notify true to notify the adapter data set has changed.
	 * @return true to return the removed item. else return null.
	 */
	public T removeItem(int which, boolean notify) {
		if (which < 0 || which >= getCount()) {
			return null;
		} else {
			T item = mItems.remove(which);
			if (notify) {
				afterRemove(item, null);
				notifyDataChanged(false);
			}
			return item;
		}
	}

	/**
	 * remove a item from the
	 * 
	 * @param @param item
	 * @param @param notify true to notify the adapter data set has changed.
	 * @return
	 */
	public boolean removeItem(T item, boolean notify) {
		boolean flag = mItems.remove(item);
		if (flag) {
			afterRemove(item, null);
			if (notify) {
				notifyDataChanged(false);
			}
		}
		return flag;
	}

	/**
	 * swap the item at index of from and to .
	 * 
	 * @param @param from
	 * @param @param to
	 * @param @param notify true to notify the adapter data set has changed.
	 * @return true to success operated,else false.
	 */
	public boolean swapItem(int from, int to, boolean notify) {
		if (from == to || from < 0 || to < 0 || from >= getCount()
				|| to >= getCount()) {
			return false;
		} else {
			T tf = mItems.get(from);
			mItems.set(from, mItems.get(to));
			mItems.set(to, tf);
			if (notify) {
				notifyDataChanged(false);
			}
			return true;
		}
	}

	public void sort(Comparator<? super T> comparator, boolean notify) {
		Collections.sort(mItems, comparator);
		if (notify)
			notifyDataChanged(false);
	}

	/**
	 * clear all items from the list.
	 */
	public void clearItems() {
		afterRemove(null, mItems);
		mItems.clear();
		notifyDataChanged(true);
	}

	public T getItem(int pos) {
		return mItems.get(pos);
	}

	public ArrayList<T> getItems() {
		return mItems;
	}

	public int getCount() {
		return mItems.size();
	}

	/**
	 * call the observer that the data has changed or invalidated.
	 * 
	 * @param @param dataInvalided true to require the whole control to
	 *        repaint,otherwise to repaint the visible region.
	 */
	public void notifyDataChanged(boolean dataInvalidated) {
		// this will repaint the visible region.
		notifyDataSetChanged();
		if (dataInvalidated) {
			// this will repaint the whole control as a original state.
			notifyDataSetInvalidated();
		}
	}

	/**
	 * before add item to the list of this adapter ,to do something in this
	 * method
	 * 
	 * @param @param item
	 * @param @param items
	 * @param @param isEnd
	 */
	protected void beforeAdd(T item, ArrayList<T> items, boolean isEnd) {
	}

	protected void afterRemove(T Item, ArrayList<T> items) {
	}

	public long getItemId(int pos) {
		return pos;
	}

	public boolean enable(int pos) {
		return true;
	}

	public boolean isEmpty() {
		return getCount() == 0;
	}

	public boolean isAllSame(int type) {
		return true;
	}

	public boolean onMeasureData(int argi, float argf, Object argo) {
		return false;
	}

	public boolean onAttachChanged(Object owner, boolean isAttach) {
		if (!isAttach) {
			clearItems();
			return true;
		}
		return false;
	}

	public void registerDataSetObserver(DataSetObserver observer) {
		mDataSetObservable.registerObserver(observer);
	}

	public void unregisterDataSetObserver(DataSetObserver observer) {
		mDataSetObservable.unregisterObserver(observer);
	}

	public void notifyDataSetChanged() {
		mDataSetObservable.notifyChanged();
	}

	public void notifyDataSetInvalidated() {
		mDataSetObservable.notifyInvalidated();
	}
}