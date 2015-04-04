package com.howbuy.adp;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.howbuy.entity.Function;
import com.howbuy.lib.adp.AbsAdp;
import com.howbuy.lib.adp.AbsViewHolder;
import com.howbuy.libtest.R;

public class FuncAdp extends AbsAdp<Function> implements Filterable {
	public static final int FUNC_LIB = 1;
	public static final int FUNC_CONTROL = 2;
	public static final int FUNC_MORE = 3;

	private ArrayList<Function> mOriginalValues = null;
	private ArrayFilter mFilter = null;
	private Object mLock = new Object();
	private int mFuncAdpType = 0;

	public FuncAdp(Context cx, ArrayList<Function> items, int funcAdpType) {
		super(cx, items);
		mFuncAdpType = funcAdpType;
	}

	public FuncAdp(Context cx, int funcAdpType) {
		super(cx);
		mFuncAdpType = funcAdpType;
	}

	@Override
	protected View getViewFromXml(int type, ViewGroup p) {
		int res = mFuncAdpType == FUNC_LIB ? R.layout.com_list_item_lib
				: R.layout.com_list_item_control;
		return mLf.inflate(res, null);
	}

	@Override
	protected AbsViewHolder<Function> getViewHolder() {
		return new FuncHolder(mFuncAdpType);
	}

	public static class FuncHolder extends AbsViewHolder<Function> {
		public TextView mTvName, mTvDescribe;
		public TextView mTvReturn, mTvUrl, mTvParams;
		public View mLayMore;
		public int mFuncType = -1;

		public FuncHolder(int funcType) {
			this.mFuncType = funcType;
		}

		protected void initView(View v, int type) {
			mTvName = (TextView) v.findViewById(R.id.tv_name);
			mTvDescribe = (TextView) v.findViewById(R.id.tv_describe);
			mTvReturn = (TextView) v.findViewById(R.id.tv_return);
			mTvUrl = (TextView) v.findViewById(R.id.tv_url);
			mTvParams = (TextView) v.findViewById(R.id.tv_params);
			mLayMore = v.findViewById(R.id.lay_params);
			v.findViewById(R.id.bt_params).setTag(this);
		}

		protected void initData(int index, int type, Function item, boolean isReuse) {
			mIndex = index;
			mItem = item;
			mTvName.setText((index + 1) + "." + mItem.getName());
			if (mFuncType == FUNC_LIB) {
				mTvReturn.setText("返回:" + mItem.getReturn());
			}
			mTvDescribe.setText(mItem.getDescription());
			if (mFuncType == FUNC_LIB) {
				mTvUrl.setText("后缀URL:" + mItem.getSubUrl());
			} else {
				mTvUrl.setText("根路径" + mItem.getReturn());
			}
			mTvParams.setText(mItem.getParams());
			if (mItem.isExpanded()) {
				mLayMore.setVisibility(View.VISIBLE);
			} else {
				mLayMore.setVisibility(View.GONE);
			}
		}

		@Override
		public int changeView(int arg) {
			if (mItem.isExpanded()) {
				mLayMore.setVisibility(View.GONE);
				mItem.setExpanded(false);
			} else {
				mLayMore.setVisibility(View.VISIBLE);
				mItem.setExpanded(true);
			}
			return arg;
		}
	}

	@Override
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new ArrayFilter();
		}
		return mFilter;
	}

	private class ArrayFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();
			if (mOriginalValues == null) {
				synchronized (mLock) {
					mOriginalValues = new ArrayList<Function>(mItems);
				}
			}

			if (prefix == null || prefix.length() == 0) {
				synchronized (mLock) {
					ArrayList<Function> list = new ArrayList<Function>(mOriginalValues);
					results.values = list;
					results.count = list.size();
				}
			} else {
				String prefixString = prefix.toString().toLowerCase();
				final ArrayList<Function> values = mOriginalValues;
				final int count = values.size();
				final ArrayList<Function> newValues = new ArrayList<Function>(count);
				for (int i = 0; i < count; i++) {
					final Function value = values.get(i);
					final String valueText = value.toShortString().toLowerCase();

					// First match against the whole, non-splitted value
					if (valueText.startsWith(prefixString)) {
						newValues.add(value);
					} else {
						final String[] words = valueText.split(" ");
						final int wordCount = words.length;
						for (int k = 0; k < wordCount; k++) {
							boolean contain = false;
							if (k <= 1) {
								contain = words[k].contains(prefixString);
							} else {
								contain = words[k].startsWith(prefixString);
							}
							if (contain) {
								newValues.add(value);
								break;
							}
						}
					}
				}

				results.values = newValues;
				results.count = newValues.size();
			}

			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			setItems((ArrayList<Function>) results.values, false);
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
	}
}
