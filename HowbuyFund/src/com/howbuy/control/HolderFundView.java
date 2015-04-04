package com.howbuy.control;

import howbuy.android.palmfund.R;
import android.view.View;
import android.widget.TextView;

public class HolderFundView {
	private View mRoot = null;
	public TextView mTvTitle, mTvState;
	public Object obj;

	public HolderFundView() {
	}

	public HolderFundView initView(View root) {
		mRoot = root;
		mTvTitle = (TextView) root.findViewById(R.id.tv_title);
		mTvState = (TextView) root.findViewById(R.id.tv_state);
		mRoot.setEnabled(false);
		return HolderFundView.this;
	}

	public void setText(String title, String state) {
		mTvTitle.setText(title);
		mTvState.setText(state);
	}

	public void setText(String state) {
		mTvState.setText(state);
	}

	public void setObject(Object obj) {
		this.obj=obj;
		if (obj!=null) {
			mRoot.setEnabled(true);
		}
	}

	public void setVisible(int visibility) {
		mRoot.setVisibility(visibility);
	}

	public void setEnable(boolean enable) {
		mRoot.setClickable(enable);
		mRoot.setEnabled(enable);
	}
}
