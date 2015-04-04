package com.howbuy.frag.control;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.howbuy.control.VipHeadView;
import com.howbuy.control.VipHeadView.IVipHeadEvent;
import com.howbuy.entity.VipInf;
import com.howbuy.lib.compont.Rotate3DAnimation;
import com.howbuy.lib.control.AnimAlphaFrame;
import com.howbuy.lib.entity.AbsResult;
import com.howbuy.lib.entity.ClickType;
import com.howbuy.lib.frag.AbsFrag;
import com.howbuy.lib.utils.ViewUtils;
import com.howbuy.libtest.R;

public class FragControlViphead extends AbsFrag implements IVipHeadEvent{
	
	private VipHeadView mVipHead = null;
	AnimAlphaFrame mLayTransn;
	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_control_viphead;
	}
	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		mLayTransn = (AnimAlphaFrame) root.findViewById(R.id.lay_tran);
		mVipHead = (VipHeadView) root.findViewById(R.id.vip_head_view);
		mVipHead.setVipHeadEvent(this);
	}

	private void applyRoateAnim(View v, int type, boolean ifCancleCleanAmin) {
		if (!ViewUtils.invokeAnim(v, type, ifCancleCleanAmin)) {
			Rotate3DAnimation rotation = new Rotate3DAnimation(90, 0, 90, 0,
					90, 0);
			rotation.setDuration(2000);
			rotation.setFillAfter(false);
			rotation.setInterpolator(new AccelerateInterpolator());
			v.startAnimation(rotation);
		}
	}
	
	@Override
	public boolean onXmlBtClick(View v) {
		switch(v.getId()){
		case R.id.bt_anim3d: {
			View animV = mVipHead.findViewById(R.id.vip_head_iv_photo);
			applyRoateAnim(animV, 0, false);
			break;
		}
		case R.id.bt_hide: {
			mLayTransn.startMaskTransition(true, 1000);
		}
			break;
		case R.id.bt_show: {
			mLayTransn.startMaskTransition(false, 1000);
		}
		}
		return false;
	}


	@Override
	protected void onAttachChanged(Activity aty, boolean isAttach) {
		super.onAttachChanged(aty, isAttach);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean handled = false;
		return handled;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		mVipHead.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public Activity onStartAtyForResult(Intent tent, int requestCode) {
		startActivityForResult(tent, requestCode);
		return getSherlockActivity();
	}
	@Override
	public void onPreRequest(int requestType, Object obj) {
		pop("onPreRequest requestType" + requestType,false);
	}

	@Override
	public void onVipHeadLoaded(AbsResult<VipInf> result, boolean fromNet) {
		pop("onVipHeadLoaded fromNet=" + fromNet + " result=" + result,false);
	}

	@Override
	public void onVipHeadUploaded(int uploadedType, Object obj, Exception e) {
		pop("onVipHeadUploaded uploadedType=" + uploadedType + " obj=" + obj
				+ " e=" + e,false);
	}

	@Override
	public void onVipHeadPhotoClick(ClickType clickType) {
		pop("onVipHeadPhotoClick clickType=" + clickType.name(),false);
	}

}
