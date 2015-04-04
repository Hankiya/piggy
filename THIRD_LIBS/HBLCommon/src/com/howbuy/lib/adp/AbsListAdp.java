package com.howbuy.lib.adp;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.howbuy.lib.entity.AbsLoadList;

public abstract class AbsListAdp<T extends AbsLoadList<K, T>, K> extends
		BaseAdapter {
	protected T mItems;
	protected LayoutInflater mLf = null;

	public AbsListAdp(Context cx) {
		this(LayoutInflater.from(cx), null);
	}

	public AbsListAdp(LayoutInflater lf) {
		this(lf, null);
	}

	public AbsListAdp(Context cx, T items) {
		this(LayoutInflater.from(cx), items);
	}

	public AbsListAdp(LayoutInflater lf, T items) {
		this.mItems = items;
		this.mLf = lf;
	}

	/**
	 * remove a item from the list at a appoint position.
	 * 
	 * @param @param which
	 * @param @param notify true to notify the adapter data set has changed.
	 * @return true to return the removed item. else return null.
	 */
	public Object removeItem(int which, boolean notify) {
		if (which < 0 || which >= getCount()) {
			return null;
		} else {
			Object item = mItems.remove(which);
			afterRemove(item, null);
			if (notify) {
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
	public boolean removeItem(K item, boolean notify) {
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
	 * add a new item to the list.
	 * 
	 * @param @param item
	 * @param @param isEnd true to add to the last position. false to add at the
	 *        head of list.
	 * @param @param notify true to notify the adapter data set has changed.
	 * @return void
	 * @throws
	 */
	public void addItem(K item, boolean isEnd, boolean notify) {
		if (item != null) {
			beforeAdd(item, null, isEnd);
			mItems.addItem(item, isEnd);
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
	public void addItems(T items, boolean isEnd, boolean notify) {
		if (items != null && items.size() > 0) {
			beforeAdd(null, items, isEnd);
			mItems.addItems(items.getItems(), isEnd);
			mItems.setTotalNum(items.getTotalNum());
			if (notify) {
				notifyDataChanged(false);
			}
		}
	}
	
	public void addItems(List<K> items, boolean isEnd, boolean notify) {
		if (items != null && items.size() > 0) {
			beforeAdd(items, null, isEnd);
			mItems.addItems(items, isEnd);
			mItems.setTotalNum(mItems.getTotalNum()+items.size());
			if (notify) {
				notifyDataChanged(false);
			}
		}
	}

	public boolean insertItem(K item, int where, boolean notify) {

		if (where < 0 || where > getCount()) {
			return false;
		} else {
			mItems.insertItem(item, where);
			if (notify) {
				notifyDataSetChanged();
			}
			return true;
		}
	}

	public int indexOf(Object item) {
		return mItems.indexOf(item);
	}

	/**
	 * set a new item to the list at the appoint position.
	 * 
	 * @param @param item
	 * @param @param notify true to notify the adapter data set has changed.
	 * @return true if set the data success .
	 */
	public boolean setItem(int index, K item, boolean notify) {
		if (item != null && index >= 0 && index < getCount()) {
			beforeAdd(item, null, false);
			mItems.setItem(index, item);
			if (notify) {
				notifyDataChanged(false);
			}
			return true;
		}
		return false;
	}

	public void setItems(T items, boolean notify) {
		mItems.clear();
		if (items != null && items.size() > 0) {
			addItems(items, true, notify);
		} else if (notify) {
			notifyDataChanged(false);
		}
	}


	public void setItems(List<K> items, boolean notify) {
		mItems.clear();
		if (items != null && items.size() > 0) {
			addItems(items, true, notify);
		} else if (notify) {
			notifyDataChanged(false);
		}
	}
	
	/**
	 * clear all items from the list.
	 */
	public void clearItems() {
		afterRemove(null, mItems);
		mItems.clear();
		notifyDataChanged(true);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public Object getItem(int position) {
		return mItems.getItem(position);
	}

	public T getItems() {
		return mItems;
	}

	@Override
	public int getCount() {
		return mItems == null ? 0 : mItems.size();
	}

	public boolean onAttachChanged(Object owner, boolean isAttach) {
		if (!isAttach) {
			clearItems();
			return true;
		}
		return false;
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

	@SuppressWarnings("unchecked")
	@Override
	public final View getView(int pos, View view, ViewGroup p) {
		int type = getItemViewType(pos);
		AbsViewHolder<K> holder=null;
		if (view == null) {
			view = getViewFromXml(type, p);
		    holder = getViewHolder();
			holder.initView(view, type);
			view.setTag(holder);
			holder.initHolderData(pos, type, mItems.getItem(pos), false);
		} else {
			holder=(AbsViewHolder<K>) view.getTag();
			holder.initHolderData(pos, type, mItems.getItem(pos), true);
		}
		return view;
	}

	/**
	 * before add item to the list of this adapter ,to do something in this
	 * method
	 * 
	 * @param @param item
	 * @param @param items
	 * @param @param isEnd
	 */
	protected void beforeAdd(Object item, T items, boolean isEnd) {
	}

	protected void afterRemove(Object Item, T items) {
	}

	/**
	 * @param p
	 *            TODO
	 * @param @param type a item view type.
	 * @return View should not be null.
	 */
	protected abstract View getViewFromXml(int type, ViewGroup p);

	protected abstract AbsViewHolder<K> getViewHolder();
}
