package com.howbuy.frag.control;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.howbuy.adp.MenuAdp;
import com.howbuy.control.ExpandableLinearLayout;
import com.howbuy.entity.ListMenuItem;
import com.howbuy.lib.control.ExpandGroup;
import com.howbuy.lib.control.ExpandGroup.IExpandChanged;
import com.howbuy.lib.frag.AbsFrag;
import com.howbuy.libtest.R;

public class FragExpand extends AbsFrag implements IExpandChanged {
	private ExpandGroup mLayExpand = null;
	private View mExpnadIndicator = null;
	private Animation mRotateUpAnim, mRotateDownAnim;
	ExpandableLinearLayout mLayout = null;

	private Animation getExpnadAnim(boolean isExpand, int duration) {
		Animation anim = null;
		if (isExpand) {
			if (mRotateUpAnim == null) {
				mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF,
						0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
				mRotateUpAnim.setFillAfter(true);
			}
			anim = mRotateUpAnim;
		} else {
			if (mRotateDownAnim == null) {
				mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF,
						0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
				mRotateDownAnim.setFillAfter(true);
			}
			anim = mRotateDownAnim;
		}
		anim.setDuration(duration);
		return anim;

	}

	@Override
	protected int getFragLayoutId() {
		return R.layout.com_expand_layout;
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		mLayExpand = (ExpandGroup) root.findViewById(R.id.lay_expand_one);
		mLayExpand.setExpnadView(root.findViewById(R.id.expand_content), -1);
		mExpnadIndicator = root.findViewById(R.id.iv_expand);
		mLayout = (ExpandableLinearLayout) root.findViewById(R.id.lay_content);
		mLayExpand.setOnExpnadChangedListener(this);
		try {
			mLayout.setAdapter(
					new MenuAdp(getSherlockActivity(), ListMenuItem.parseMenus(
							getSherlockActivity(), "xml_menu_items.xml")), true);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onXmlBtClick(View v) {
		switch (v.getId()) {
		case R.id.bt_toggle:
		    mLayExpand.toggleExpand(300, 20);
			mLayout.toggleExpand(300, 10);
			break;
		case R.id.bt_expand:
			mLayExpand.toggleExpand(300, 20, true);
			break;
		case R.id.bt_unexpnad:
			mLayExpand.toggleExpand(300, 20, false);
			break;
		}
		return true;
	}

	@Override
	public void onExpnadChanged(int changeType, int duration) {
		if (changeType == IExpandChanged.CHANGE_START_EXPAND) {
			mExpnadIndicator.startAnimation(getExpnadAnim(true, duration));
		} else if (changeType == IExpandChanged.CHANGE_START_UNEXPAND) {
			mExpnadIndicator.startAnimation(getExpnadAnim(false, duration));
		}
	}

}
