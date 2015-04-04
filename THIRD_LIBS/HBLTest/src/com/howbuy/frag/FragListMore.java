package com.howbuy.frag;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.howbuy.adp.MenuAdp;
import com.howbuy.adp.MenuAdp.MenuViewHolder;
import com.howbuy.aty.AtyMain;
import com.howbuy.entity.ListMenuItem;
import com.howbuy.lib.frag.AbsFrag;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.SysUtils;
import com.howbuy.lib.utils.ViewUtils;
import com.howbuy.libtest.R;

@SuppressLint("NewApi")
public class FragListMore extends AbsFrag implements OnItemClickListener {
	ArrayList<ListMenuItem> mList = null;
	private ListView mLv = null;
	private AutoCompleteTextView mAct = null;

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_list;
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		try {
			mLv = (ListView) mRootView;
			mLv.setOnItemClickListener((OnItemClickListener) getSherlockActivity());
			if (mAct == null) {
				mAct = ((AtyMain) getSherlockActivity()).getAutoTextView();
				mAct.setOnItemClickListener(this);
			}
			loadData(bundle, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadData(Bundle bundle, boolean forceLoaded) throws Exception {
		if (mList == null || forceLoaded) {
			String fileName = getArguments().getString("filename");
			mList = ListMenuItem.parseMenus(getSherlockActivity(), fileName);
		}
		mLv.setAdapter(new MenuAdp(getSherlockActivity(), mList));
		mAct.setAdapter(new MenuAdp(getSherlockActivity(),
				new ArrayList<ListMenuItem>(mList)));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ViewUtils.showKeybord(view, false);
		MenuViewHolder hod = (MenuViewHolder) view.getTag();
		if (hod.mIndex != -1) {
			if (SysUtils.getApiVersion() > 10) {
				mLv.smoothScrollToPositionFromTop(hod.mIndex, 10);
			} else {
				mLv.smoothScrollToPosition(hod.mIndex);
			}

		}
		mAct.setText(null);
	}

	// ///////////////////////////////////////////////////////////////////////////

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if(mLv!=null){
			mLv.setAdapter(null);
		}
	}

	// /////////////////////////////////////////////////////////////////

}
