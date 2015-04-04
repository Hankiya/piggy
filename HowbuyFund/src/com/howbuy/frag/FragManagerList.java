package com.howbuy.frag;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.protobuf.InvalidProtocolBufferException;
import com.howbuy.adp.ManagerAdp;
import com.howbuy.adp.ManagerAdp.ManagerHolder;
import com.howbuy.aty.AtyEmpty;
import com.howbuy.config.FundConfig.FundType;
import com.howbuy.config.ValConfig;
import howbuy.android.palmfund.R;
import com.howbuy.lib.frag.AbsFragMger.FragOpt;
import com.howbuy.wireless.entity.protobuf.ManagerInfoListProto.ManagerInfoList;
import com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo;
import com.tang.library.pulltorefresh.PullToRefreshBase;

public class FragManagerList extends AbsFragList implements OnItemClickListener {
	private ManagerAdp mAdapter = null;
	private FundType mFundType = null;

	private void intiAdapter() {
		Bundle arg = getArguments();
		if (mTitleLable == null) {
			mTitleLable = arg.getString(ValConfig.IT_NAME);
		}
		if (mAdapter == null) {
			try {
				mFundType = (FundType) arg.getParcelable(ValConfig.IT_TYPE);
				ManagerInfoList mis = ManagerInfoList.parseFrom(arg
						.getByteArray(ValConfig.IT_ENTITY));
				ArrayList<ManagerInfo> r = new ArrayList<ManagerInfo>();
				r.addAll(mis.getManagerInfoList());
				mAdapter = new ManagerAdp(getSherlockActivity(), r);

			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		super.initViewAdAction(root, bundle);
		setPullRefushMode(false, false);
		intiAdapter();
		mListView.setAdapter(mAdapter);
	}

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_content_list;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Object obj = view.getTag();
		if (obj instanceof ManagerHolder) {
			Bundle b = new Bundle();
			String fragName = FragManagerDetail.class.getName();
			b.putString(ValConfig.IT_NAME, "基金经理");
			b.putParcelable(ValConfig.IT_TYPE, mFundType);
			b.putString(ValConfig.IT_ID, ((ManagerHolder) obj).mItem.getRydm());
			FragOpt f = new FragOpt(fragName, b, FragOpt.FRAG_POPBACK);
			((AtyEmpty) getSherlockActivity()).switchToFrag(f);
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
	}

}
