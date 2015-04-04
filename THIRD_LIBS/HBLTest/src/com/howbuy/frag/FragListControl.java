package com.howbuy.frag;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.howbuy.adp.FuncAdp;
import com.howbuy.aty.AtyMain;
import com.howbuy.entity.Function;
import com.howbuy.lib.frag.AbsFrag;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.ViewUtils;
import com.howbuy.libtest.R;
import com.howbuy.xml.XmlParse;

public class FragListControl extends AbsFrag implements OnItemClickListener {
	private ListView mLv = null;
	ArrayList<Function> mList = null;
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadData(Bundle bundle, boolean forceLoaded) throws IOException {
		if (mList == null || forceLoaded) {
			String fileName = getArguments().getString("filename");
			mList = new ArrayList<Function>();
			XmlParse.parseExchange(mList, getSherlockActivity().getAssets().open(fileName),
					FuncAdp.FUNC_CONTROL);
		}
		mLv.setAdapter(new FuncAdp(getSherlockActivity(), mList, FuncAdp.FUNC_CONTROL));
		mAct.setAdapter(new FuncAdp(getSherlockActivity(), new ArrayList<Function>(mList),
				FuncAdp.FUNC_CONTROL));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ViewUtils.showKeybord(view, false);
		((AtyMain) getSherlockActivity()).onItemClick(parent, view, position, id);
		mAct.setText(null);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mLv != null) {
			mLv.setAdapter(null);
		}
	}

	// /////////////////////////////////////////////////////////////////

}
