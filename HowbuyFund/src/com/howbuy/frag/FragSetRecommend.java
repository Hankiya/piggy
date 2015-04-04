package com.howbuy.frag;

import howbuy.android.palmfund.R;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.howbuy.config.ValConfig;
import com.howbuy.lib.frag.AbsHbFrag;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.utils.ShareHelper;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;

@SuppressLint("SetJavaScriptEnabled")
public class FragSetRecommend extends AbsHbFrag {
	EditText mEtShare;

	private void parseArgments(Bundle arg) {
		if (arg != null) {
			if (mTitleLable == null) {
				mTitleLable = arg.getString(ValConfig.IT_NAME);
			}
		}
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		parseArgments(getArguments());
		mEtShare = (EditText) root.findViewById(R.id.et_share);
		mEtShare.append(ValConfig.URL_APP_FUND);
		mEtShare.setSelection(mEtShare.getText().toString().length());
	}

	@Override
	public boolean onXmlBtClick(View v) {
		boolean handed = true;
		switch (v.getId()) {
		case R.id.tv_submit:
			String content = mEtShare.getText().toString();
			if (!StrUtils.isEmpty(content)) {
				ShareHelper.showSharePicker(getSherlockActivity(), mDlgClickListener);
			} else {
				pop("分享内容不能为空", false);
			}
			break;

		default:
			handed = false;
			break;
		}
		return handed ? true : super.onXmlBtClick(v);
	}

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_set_recomend;
	}

	private int mShareType = -1;
	DialogInterface.OnClickListener mDlgClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			mShareType = which;
			String title = "掌上基金分享";
			String content = mEtShare.getText().toString().trim();
			String contentUrl = ValConfig.URL_APP_FUND;
			Object bmp = R.drawable.qr_invite_friends;
			switch (which) {
			case 0:
				bmp = R.drawable.my_start;
				break;
			case 1:
				bmp = R.drawable.my_start;
				break;
			case 2:
				break;
			}
			ShareHelper.share(getSherlockActivity(), which, title, content, contentUrl, bmp,
					mListener, "推荐好友");
		}
	};
	private SnsPostListener mListener = new SnsPostListener() {
		@Override
		public void onStart() {
		}

		@Override
		public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
			if (mShareType == 0) {
				if (eCode == StatusCode.ST_CODE_SUCCESSED) {
					pop("分享到 " + ShareHelper.getSharePlateform().get(mShareType) + "成功", false);
				} else {
					if (eCode != StatusCode.ST_CODE_ERROR_CANCEL) {
						pop("分享到 " + ShareHelper.getSharePlateform().get(mShareType) + "失败", false);
					}
				}
			}
		}
	};
}
