package com.howbuy.adp;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.howbuy.lib.adp.AbsAdp;
import com.howbuy.lib.adp.AbsViewHolder;

public class SearchAdp extends AbsAdp<String>{

	public SearchAdp(Context cx, ArrayList<String> items) {
		super(cx, items);
	}

	@Override
	protected View getViewFromXml(int type,ViewGroup p) {
		    TextView tv = new TextView(mLf.getContext());
	        tv.setPadding(14, 8, 10, 8);
	        tv.setTextColor(Color.BLACK);
	        tv.setId(1111);
	        return tv;
	}
	@Override
	protected AbsViewHolder<String> getViewHolder() {
		// TODO Auto-generated method stub
		return new ViewHodlder();
	}
	
	public class ViewHodlder extends AbsViewHolder<String>{
        public TextView mTv=null;
		public ViewHodlder() {
		}

		@Override
		protected void initView(View root, int type) {
		      mTv=(TextView) root;	
		}

		@Override
		protected void initData(int index, int type, String item,
				boolean isReuse) {
			if(mTv!=null){
				mTv.setText(item);
			}
		}
	}
}
