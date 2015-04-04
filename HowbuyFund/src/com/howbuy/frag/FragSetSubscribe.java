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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.howbuy.config.Analytics;
import com.howbuy.config.ValConfig;
import com.howbuy.datalib.fund.ParBook;
import com.howbuy.lib.frag.AbsHbFrag;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.lib.utils.ViewUtils;
import com.howbuy.utils.FundUtils;
import com.howbuy.wireless.entity.protobuf.CommonProtos.Common;

@SuppressLint("SetJavaScriptEnabled")
public class FragSetSubscribe extends AbsHbFrag implements OnCheckedChangeListener, IReqNetFinished {
	private static final int CHECK_INPUT_PHONE = 1;
	private static final int CHECK_SUBSCRIBE = 2;
	private View mTvSubmit;
	private EditText mEtPhone;
	private CheckBox mCbWeek, mCbMonth;

	ProgressDialog mDialog;

	private void showCheckUpdateDlg(boolean show) {
		if (show) {
			if (mDialog == null) {
				mDialog = new ProgressDialog(getSherlockActivity());
				mDialog.setMessage("订阅中...");
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
		}
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		parseArgments(getArguments());
		mTvSubmit = root.findViewById(R.id.tv_submit);
		mEtPhone = (EditText) root.findViewById(R.id.et_phone);
		mTvSubmit.setEnabled(false);
		mEtPhone.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				checkSubmitEnable();
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});
		mCbWeek = (CheckBox) root.findViewById(R.id.cb_week);
		mCbMonth = (CheckBox) root.findViewById(R.id.cb_month);
		mCbWeek.setOnCheckedChangeListener(this);
		mCbMonth.setOnCheckedChangeListener(this);
		String phone = FundUtils.readUserPhone();
		if (!StrUtils.isEmpty(phone)) {
			mEtPhone.setText(phone);
			mEtPhone.setSelection(phone.length());
		}
	}

	private int checkSubmit(String phone) {
		int checkResult = 0;
		checkResult |= (!StrUtils.isMobileTel(phone) ? CHECK_INPUT_PHONE : 0);
		checkResult |= (!mCbWeek.isChecked() && !mCbMonth.isChecked() ? CHECK_SUBSCRIBE : 0);
		return checkResult;
	}

	private void handSubmit(View v) {
		int err = 0;
		String phone = mEtPhone.getText().toString().trim();
		if (0 == (err = checkSubmit(phone))) {
			StringBuilder sb = new StringBuilder();
			if (mCbWeek.isChecked()) {
				sb.append("1000;");
			}
			if (mCbMonth.isChecked()) {
				sb.append("1001;");
			}
			sb.deleteCharAt(sb.length() - 1);
			showCheckUpdateDlg(true);
			new ParBook(1, this).setParams(0, sb.toString(), phone, null, null, null, null)
					.execute();
		} else {
			if (ViewUtils.hasFlag(err, CHECK_INPUT_PHONE)) {
				pop("请输入正确的手机号码", false);
			} else {
				pop("请至少选择一项订阅", true);
			}

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
			Analytics.onEvent(getSherlockActivity(), Analytics.CALL_400, Analytics.KEY_FROM,
					mTitleLable);
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
		return R.layout.frag_set_subscribe;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		checkSubmitEnable();
	}

	private void checkSubmitEnable() {
		String phone = mEtPhone.getText().toString().trim();
		boolean week = mCbWeek.isChecked();
		boolean month = mCbMonth.isChecked();
		mTvSubmit.setEnabled(!(StrUtils.isEmpty(phone) || (!week && !month)));
	}

	@Override
	public void onRepNetFinished(ReqResult<ReqNetOpt> r) {
		showCheckUpdateDlg(false);
		if (r.isSuccess()) {
			com.howbuy.wireless.entity.protobuf.CommonProtos.Common cm = (Common) r.mData;
			if ("1".equals(cm.getResponseCode())) {
				Analytics.onEvent(getSherlockActivity(), Analytics.SMS_SUBSCRIPTION);
				pop("订阅成功", false);
			} else {
				pop(cm.getResponseContent(), false);
			}
		} else {
			pop(ValConfig.NET_ERR, false);
			pop("" + r.mErr, true);
		}
	}

}
