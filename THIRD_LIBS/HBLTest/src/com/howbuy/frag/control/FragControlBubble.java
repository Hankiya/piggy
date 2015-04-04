package com.howbuy.frag.control;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.howbuy.control.RandomBubbleView;
import com.howbuy.control.RandomBubbleView.BubbleClickListener;
import com.howbuy.lib.adp.AbsSimpleAdp;
import com.howbuy.lib.adp.AbsViewHolder;
import com.howbuy.lib.frag.AbsFrag;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.libtest.R;

public class FragControlBubble extends AbsFrag implements BubbleClickListener {
	private RandomBubbleView mGroup = null;
	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_control_bubble;
	}
	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		mGroup = (RandomBubbleView)root.findViewById(R.id.lay_content);
		mGroup.setAdapter(new MyAdp(getSherlockActivity(),createItems(150)));
		mGroup.setBubbleClickListener(this);
	}
 
	public ArrayList<String> createItems(int n){
		 ArrayList<String> list=new ArrayList<String>();
		 for(int i=0;i<n;i++){
			 list.add(""+i);
		 }
		 return list;
	}
	class MyAdp extends AbsSimpleAdp<String> {

		public MyAdp(LayoutInflater lf, ArrayList<String> items) {
			super(lf, items);
		}
		public MyAdp(Context cx, ArrayList<String> items) {
			super(cx, items);
		}
		@Override
		protected View getViewFromXml(int type,ViewGroup p) {
			TextView mTv=new TextView(getSherlockActivity());
			mTv.setTextSize(40);
			mTv.setBackgroundResource(R.drawable.xml_bt_blue);
			return   mTv;
		}

		@Override
		public void recycleView(int index, View v) {
			TextView tv = (TextView) v;
			tv.setText("-1");
		}
		@Override
		protected AbsViewHolder<String> getViewHolder() {
			return new BubbleViewHolder();
		}
		
		class BubbleViewHolder extends AbsViewHolder<String>{
            public TextView mTv=null;
			@Override
			protected void initView(View root, int type) {
				mTv=(TextView) root;
			}

			@Override
			protected void initData(int index, int type, String item,
					boolean isReuse) {
				mTv.setText(""+index);
			}
			
		}
	}

	@Override
	public void onBubbleClickListener(RandomBubbleView p, View v, int index) {
		 pop("click "+index,false);
	}
}
