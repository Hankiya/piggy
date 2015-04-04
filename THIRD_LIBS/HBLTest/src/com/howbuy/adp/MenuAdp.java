package com.howbuy.adp;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.howbuy.entity.ListMenuItem;
import com.howbuy.entity.ListMenuItem.IMenuViewAdjust;
import com.howbuy.lib.adp.AbsAdp;
import com.howbuy.lib.adp.AbsViewHolder;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.libtest.R;

public class MenuAdp extends AbsAdp<ListMenuItem> implements IMenuViewAdjust , Filterable{
	private ArrayList<ListMenuItem>mOriginalValues=null;
	private ArrayFilter mFilter=null;
	private Object mLock=new Object();
	
	public MenuAdp(Context cx, ArrayList<ListMenuItem> items) {
		super(cx, items);
	}
	public MenuAdp(LayoutInflater lf, ArrayList<ListMenuItem> items) {
		super(lf, items);
	}

	@Override
	public boolean isEnabled(int position) {
		return mItems.get(position).isClickAble();
	}

	@Override
	protected View getViewFromXml(int type,ViewGroup p) {
	 return mLf.inflate(R.layout.com_list_menu, null); 
	}
	@Override
	public void adjustMenuView(int type, AbsViewHolder<ListMenuItem> holder,boolean isReuse){
		 
	}

	@Override
	protected AbsViewHolder<ListMenuItem> getViewHolder() {
		return new MenuViewHolder(this);
	}
	@Override
	public boolean hasFlag(int pos, int flag) {
		if(pos%2==1)return true;
		return super.hasFlag(pos, flag);
	}
	
	public static class MenuViewHolder extends AbsViewHolder<ListMenuItem>{
		public TextView mTvTitle, mTvSummary;
		public TextView mTvAlerm, mTvState;
		public ImageView mIvIcon, mIvGo;
		public CheckBox mCbCheck;
		public View mVSep; 
		private IMenuViewAdjust mListener;

		public MenuViewHolder(IMenuViewAdjust l) {
			mListener = l;
		}
		@Override
		public void initData(int index, int type, ListMenuItem item,boolean isReuse) {
			mIndex = index;
			mItem = item;
			initViewHoder(type);
			if (mListener != null) {
				mListener.adjustMenuView(type, this,isReuse);
			}
		}
		
        @Override
		protected void initView(View v, int type) {
         mTvTitle=(TextView) v.findViewById(R.id.menuitem_tv_title);
       	 mTvSummary=(TextView) v.findViewById(R.id.menuitem_tv_summary);
       	 mTvAlerm=(TextView) v.findViewById(R.id.menuitem_tv_alerm);
       	 mTvState=(TextView) v.findViewById(R.id.menuitem_tv_state);
       	 mIvIcon=(ImageView) v.findViewById(R.id.menuitem_iv_icon);
       	 mIvGo=(ImageView) v.findViewById(R.id.menuitem_iv_goto);
       	 mCbCheck=(CheckBox) v.findViewById(R.id.menuitem_cb_check);
       	 mVSep=v.findViewById(R.id.menuitem_v_sep); 
			
		}

		@Override
		public int changeView(int arg) {
			if(mItem.isCheckAble()){
        		mItem.setChecked(! mItem.isChecked());
       			mCbCheck.setChecked(mItem.isChecked());
        	}
			return arg;
		}
 
		private void initViewHoder(int type) {
			mTvTitle.setText(mItem.getTitle());
			if (StrUtils.isEmpty(mItem.getSummary())) {
				mTvSummary.setVisibility(View.GONE);
			} else {
				mTvSummary.setText(mItem.getSummary());
				mTvSummary.setVisibility(View.VISIBLE);
			}
			if (StrUtils.isEmpty(mItem.getAlerm())) {
				mTvAlerm.setVisibility(View.GONE);
			} else {
				mTvAlerm.setText(mItem.getAlerm());
				mTvAlerm.setVisibility(View.VISIBLE);
			}
			if (StrUtils.isEmpty(mItem.getState())) {
				mTvState.setVisibility(View.GONE);
			} else {
				mTvState.setText(mItem.getState());
				mTvState.setVisibility(View.VISIBLE);
			}
			if (mItem.isCheckAble()) {
				mCbCheck.setVisibility(View.VISIBLE);
				mCbCheck.setChecked(mItem.isChecked());
			} else {
				mCbCheck.setVisibility(View.GONE);
			}
			mVSep.setVisibility(mItem.isSepAble() ? View.VISIBLE : View.GONE);
			mIvGo.setVisibility(mItem.isRArrAble() ? View.VISIBLE : View.GONE);
			if(mItem.getIconResid()>0){
				mIvIcon.setVisibility(View.VISIBLE);
				mIvIcon.setImageResource(mItem.getIconResid());
			}else{
				mIvIcon.setVisibility(View.GONE);
			}
		}
	}

	
	@Override
	public Filter getFilter() {
		 if(mFilter==null){
			 mFilter=new ArrayFilter();
		 }
		return mFilter;
	}
 

	private class ArrayFilter extends Filter {
	        @Override
	        protected FilterResults performFiltering(CharSequence prefix) {
	            FilterResults results = new FilterResults();
	            if (mOriginalValues == null) {
	                synchronized (mLock) {
	                    mOriginalValues = new ArrayList<ListMenuItem>(mItems);
	                }
	            }

	            if (prefix == null || prefix.length() == 0) {
	                synchronized (mLock) {
	                    ArrayList<ListMenuItem> list = new ArrayList<ListMenuItem>(mOriginalValues);
	                    results.values = list;
	                    results.count = list.size();
	                }
	            } else {
	                String prefixString = prefix.toString().toLowerCase();
	                final ArrayList<ListMenuItem> values = mOriginalValues;
	                final int count = values.size();
	                final ArrayList<ListMenuItem> newValues = new ArrayList<ListMenuItem>(count);
	                for (int i = 0; i < count; i++) {
	                    final ListMenuItem value = values.get(i);
	                    final String valueText = value.toShortString().toLowerCase();

	                    // First match against the whole, non-splitted value
	                    if (valueText.startsWith(prefixString)) {
	                        newValues.add(value);
	                    } else {
	                        final String[] words = valueText.split(" ");
	                        final int wordCount = words.length;
	                        for (int k = 0; k < wordCount; k++) {
	                            if (words[k].startsWith(prefixString)) {
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
	        	setItems((ArrayList<ListMenuItem>)results.values, false);
	            if (results.count > 0) {
	                notifyDataSetChanged();
	            } else {
	                notifyDataSetInvalidated();
	            }
	        }
	    }
}
