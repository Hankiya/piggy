package com.howbuy.frag;

import howbuy.android.palmfund.R;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.howbuy.commonlib.ParFeedback;
import com.howbuy.component.AppFrame;
import com.howbuy.config.ValConfig;
import com.howbuy.control.CheckHeadText;
import com.howbuy.entity.UserInf;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.frag.AbsHbFrag;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.lib.utils.SysUtils;
import com.howbuy.lib.utils.ViewUtils;
import com.howbuy.utils.FundUtils;

public class FragSetFeedback extends AbsHbFrag implements IReqNetFinished {
	private static final int CHECK_INPUT_EMAIL = 1;
	private CheckHeadText[] mRate = new CheckHeadText[3];
	private View mTvSubmit;
	private EditText mEtFeed, mEtName, mEtEmail;
	private int mCurrentRate = 1;
	ProgressDialog mDialog;

	private void showCheckUpdateDlg(boolean show) {
		if (show) {
			if (mDialog == null) {
				mDialog = new ProgressDialog(getSherlockActivity());
				mDialog.setMessage("提交中...");
			}
			if (!mDialog.isShowing()) {
				mDialog.show();
			}
		} else {
			if (mDialog != null && mDialog.isShowing()) {
				mDialog.dismiss();
			}
		}
		mTvSubmit.setEnabled(!show);
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
		mEtFeed = (EditText) root.findViewById(R.id.et_content);
		mEtEmail = (EditText) root.findViewById(R.id.et_email);
		mEtName = (EditText) root.findViewById(R.id.et_name);
		for (int i = 0; i < 3; i++) {
			mRate[i] = (CheckHeadText) root.findViewWithTag("rate_" + i);
		}
		mRate[mCurrentRate].setChecked(true);
		mTvSubmit.setEnabled(false);
		mEtFeed.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				mTvSubmit.setEnabled(s.toString().trim().length() > 0);
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});
	}

	private int checkSubmit(String feed, String name, String email) {
		int checkResult = 0;
		if (!StrUtils.isEmpty(email)) {
			checkResult |= (!StrUtils.isEmail(email) ? CHECK_INPUT_EMAIL : 0);
		}
		return checkResult;
	}

	private String appendExtraInf(StringBuffer sb, String l) {
		sb.append("PROJECT_NAME:").append(getSherlockActivity().getPackageName()).append(l);
		sb.append("PROJECT_VERSION:").append(SysUtils.getVersionName(getSherlockActivity()))
				.append(l);
		String token = GlobalApp.getApp().getMapStr("token");
		if (token != null) {
			sb.append("PROJECT_TOKEN:").append(token).append(l);
		}
		sb.append("IEME:").append(SysUtils.getImei(GlobalApp.getApp())).append(l);
		sb.append("MOBILE_MODEL:").append(SysUtils.getModel()).append(l);
		sb.append("MOBILE_OS:ANDROID_").append(SysUtils.getOsVersion()).append(l);
		sb.append("SDK API:").append(SysUtils.getApiVersion()).append(l);
		if (UserInf.getUser().isLogined()) {
			sb.append("USER_NAME:").append(UserInf.getUser().getUserName()).append(l);
		}
		return sb.toString();
	}

	private void handSubmit(View v) {
		int err = 0;
		String email = mEtEmail.getText().toString().trim();
		String name = mEtName.getText().toString().trim();
		String feed = mEtFeed.getText().toString().trim();

		if (0 == (err = checkSubmit(feed, name, email))) {
			String l = "\r\n";
			StringBuffer sb = new StringBuffer();
			sb.append("USER_FEEDBACK:").append(feed).append(l);
			String formatContent = appendExtraInf(sb, l);
			showCheckUpdateDlg(true);
			new ParFeedback(0, this).setParams(FundUtils.readUserPhone(), 5 - mCurrentRate * 2,
					formatContent, name, email).execute();
		} else {
			boolean errEmail = ViewUtils.hasFlag(err, CHECK_INPUT_EMAIL);
			if (errEmail) {
				pop("请输入正确的邮箱地址", false);
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
		default:
			String tag = String.valueOf(v.getTag()).trim();
			if (tag.startsWith("rate_")) {
				int newIndex = tag.charAt(tag.length() - 1) - 48;
				if (newIndex != mCurrentRate) {
					mRate[mCurrentRate].setChecked(false);
					mCurrentRate = newIndex;
					mRate[mCurrentRate].setChecked(true);
				}
			} else {
				handed = false;
			}
			break;
		}
		return handed ? true : super.onXmlBtClick(v);
	}

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_set_feedback;
	}

	@Override
	public void onRepNetFinished(ReqResult<ReqNetOpt> r) {
		showCheckUpdateDlg(false);
		if (r.isSuccess()) {
			com.howbuy.wireless.entity.protobuf.trustdaquan.CommonProtos.Common com = (com.howbuy.wireless.entity.protobuf.trustdaquan.CommonProtos.Common) r.mData;
			if ("1".equals(com.getResponseCode())) {
				if (mCurrentRate == 0) {
					String pk = getSherlockActivity().getPackageName();
					String app_rate = AppFrame.getApp().getsF()
							.getString(ValConfig.SF_RATE_APP_LEVEL, null);
					if (app_rate != null && app_rate.contains(pk)) {
						pop("提交成功！感谢您的反馈", false);
						getSherlockActivity().onBackPressed();
					} else {
						pop("提交成功！感谢您的反馈", false);
						if (SysUtils.intentSafe(
								getSherlockActivity(),
								new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="
										+ getSherlockActivity().getPackageName())))) {
							mTvSubmit.postDelayed(new Runnable() {
								@Override
								public void run() {
									if (isVisible()) {
										showGotoMarketDlg();
									}
								}
							}, 1000);
						} else {
							pop("提交成功！感谢您的反馈", false);
							getSherlockActivity().onBackPressed();
						}
					}
				} else {
					pop("提交成功！感谢您的反馈", false);
					getSherlockActivity().onBackPressed();
				}
			} else {
				pop(com.getResponseContent(), false);
			}
		} else {
			pop(ValConfig.NET_ERR, false);
			pop("" + r.mErr, true);
		}
	}

	private void showGotoMarketDlg() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
		builder.setTitle("给我评分");
		builder.setMessage("喜欢掌上基金，请给我们评个分。");
		builder.setPositiveButton("去评分", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				gotoMarket();
				getSherlockActivity().onBackPressed();
			}
		});
		builder.setNegativeButton(android.R.string.cancel, null);
		builder.show();
	}

	private void gotoMarket() {
		String pk = getSherlockActivity().getPackageName();
		String app_rate = AppFrame.getApp().getsF().getString(ValConfig.SF_RATE_APP_LEVEL, null);
		Intent t = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pk));
		if (app_rate == null) {
			app_rate = pk;
			pk = null;
		} else {
			if (!app_rate.contains(pk)) {
				app_rate = app_rate + "#" + pk;
				pk = null;
			}
		}
		if (pk == null) {// 记录评分已经调用过.
			AppFrame.getApp().getsF().edit().putString(ValConfig.SF_RATE_APP_LEVEL, app_rate)
					.commit();
		}
		startActivity(t);

	}

}
