package com.howbuy.aty;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.howbuy.component.AppFrame;
import com.howbuy.lib.aty.AbsAty;
import com.howbuy.lib.interfaces.IReqFinished;
import com.howbuy.lib.net.ReqOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.libtest.R;

public class AtyTest extends AbsAty implements IReqFinished {
    private TextView mTvResult=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_test);
		mTvResult=(TextView) findViewById(R.id.tv_result);
	}

	@Override
	public void onXmlBtClick(View v) {
		switch (v.getId()) {
		case R.id.bt_start: {
			AppFrame.getApp().getGlobalServiceMger().toggleService(true);
			break;
		}
		case R.id.bt_stop: {
			AppFrame.getApp().getGlobalServiceMger().toggleService(false);
			break;
		}
		case R.id.bt_send: {
			AppFrame.getApp().getGlobalServiceMger().executeTask(new ReqOpt(0,"test_key", 1), this);
			break;
		}
		}
	}

	@Override
	protected void onAbsBuildActionBar() {
		buildActionBarSimple();
	}

	@Override
	public void onRepFinished(ReqResult<ReqOpt> result) {
		mTvResult.setText(result+"");
	}

	@Override
	protected void onDestroy() {
		AppFrame.getApp().getGlobalServiceMger().toggleService(false);
		super.onDestroy();
	}

	@Override
	public boolean onNetChanged(int netType, int preType) {
		// TODO Auto-generated method stub
		return false;
	}

 

	
}