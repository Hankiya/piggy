package com.howbuy.frag;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.howbuy.datalib.trade.TradeBuilder;
import com.howbuy.lib.frag.AbsFrag;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.libtest.R;

public class FragNetTest extends AbsFrag implements IReqNetFinished {

	SparseArray<String> mRequests = new SparseArray<String>();
	TextView mTvRecord;

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		TradeBuilder.setBaseUrl("https://trade.ehowbuy.com/tws/", "http://10.168.109.22:8080/twsuat/");
		LogUtils.mDebugUrl=false;
		mTvRecord = (TextView) root.findViewById(R.id.tv_record);
	}

	@Override
	public boolean onXmlBtClick(View v) {
		launchTest();
		return super.onXmlBtClick(v);
	}

	private void launchTest() {
		//TradeBuilder.newRsa("android").execute(1, this);
		TradeBuilder.newSmsSend("android", "18672324218").execute(1, this);
	}

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_net_test;
	}

	@Override
	public void onRepNetFinished(ReqResult<ReqNetOpt> r) {
		mTvRecord.setText(r.toString());
		LogUtils.d("trade", r.toString());
	}

}
