package com.howbuy.frag;

import howbuy.android.palmfund.R;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.howbuy.commonlib.ParMakeAppointment;
import com.howbuy.config.Analytics;
import com.howbuy.config.ValConfig;
import com.howbuy.lib.frag.AbsHbFrag;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.utils.FundUtils;
import com.howbuy.wireless.entity.protobuf.trustdaquan.CommonProtos.Common;

@SuppressLint("SetJavaScriptEnabled")
public class FragSubscribeDetails extends AbsHbFrag implements IReqNetFinished {
	private static final int CHECK_INPUT_PHONE = 1;
	private View mTvSubmit;
	private TextView mTvTitle;
	private EditText mEtName, mEtPhone;
	private String mTitle;
	private boolean isSimu;

	ProgressDialog mDialog;

	private void showCheckUpdateDlg(boolean show) {
		if (show) {
			if (mDialog == null) {
				mDialog = new ProgressDialog(getSherlockActivity());
				mDialog.setMessage("预约中...");
			}
			if (!mDialog.isShowing()) {
				mDialog.show();
			}
		} else {
			if (mDialog != null && mDialog.isShowing()) {
				mDialog.dismiss();
			}
		}
	}

	private void parseArgments(Bundle arg) {
		if (arg != null) {
			if (mTitleLable == null) {
				mTitleLable = arg.getString(ValConfig.IT_NAME);
			}
			mTitle = arg.getString(ValConfig.IT_URL);
			isSimu = arg.getBoolean(ValConfig.IT_TYPE);
		}
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		parseArgments(getArguments());
		mTvSubmit = root.findViewById(R.id.tv_submit);
		mTvTitle = (TextView) root.findViewById(R.id.tv_title);
		mEtName = (EditText) root.findViewById(R.id.et_name);
		mEtPhone = (EditText) root.findViewById(R.id.et_phone);
		mTvTitle.setText(mTitle);
		mTvSubmit.setEnabled(false);
		mEtPhone.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				mTvSubmit.setEnabled(s.toString().trim().length() > 0);
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

		});
		mEtPhone.setText(FundUtils.readUserPhone());
	}

	private int checkSubmit(String name, String phone) {
		int checkResult = 0;
		checkResult |= (!StrUtils.isMobileTel(phone) ? CHECK_INPUT_PHONE : 0);
		return checkResult;
	}

	private void handSubmit(View v) {
		String name = mEtName.getText().toString().trim();
		if (StrUtils.isEmpty(name))
			name = "掌上基金用户";
		String phone = mEtPhone.getText().toString().trim();
		if (0 == checkSubmit(name, phone)) {
			showCheckUpdateDlg(true);
			new ParMakeAppointment(1, this).setParams(name, phone, null, isSimu ? 1 : 0).execute();
		} else {
			pop("请输入正确的手机号码", true);
		}
	}

	@Override
	public boolean onXmlBtClick(View v) {
		boolean handed = true;
		switch (v.getId()) {
		case R.id.tv_submit:
			handSubmit(v);
			break;
		case R.id.lay_server: {
			String title = isSimu ? "私募预约" : "信托预约";
			Analytics.onEvent(getSherlockActivity(), Analytics.CALL_400, Analytics.KEY_FROM, title);
			startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "4007009665")));
			break;
		}
		default:
			handed = false;
			break;
		}
		return handed ? true : super.onXmlBtClick(v);
	}

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_subscribe;
	}

	@Override
	public void onRepNetFinished(ReqResult<ReqNetOpt> r) {
		showCheckUpdateDlg(false);
		if (r.isSuccess()) {
			com.howbuy.wireless.entity.protobuf.trustdaquan.CommonProtos.Common cm = (Common) r.mData;
			if ("1".equals(cm.getResponseCode())) {
				pop("预约成功", false);
				Analytics.onEvent(getSherlockActivity(), Analytics.MAKE_APPOINTMENT);
				getSherlockActivity().onBackPressed();
			} else {
				pop(cm.getResponseContent(), false);
			}
		} else {
			pop(ValConfig.NET_ERR, false);
			pop(""+r.mErr, true);
		}
	}

}
