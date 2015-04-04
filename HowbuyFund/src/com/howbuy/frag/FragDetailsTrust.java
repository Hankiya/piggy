package com.howbuy.frag;

import howbuy.android.palmfund.R;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.howbuy.aty.AtyEmpty;
import com.howbuy.aty.AtyTbMain;
import com.howbuy.commonlib.ParTrustListByID;
import com.howbuy.component.CardDrawable;
import com.howbuy.config.Analytics;
import com.howbuy.config.ValConfig;
import com.howbuy.control.QuickReturnFooter;
import com.howbuy.entity.AtyInfs;
import com.howbuy.lib.frag.AbsFragMger.FragOpt;
import com.howbuy.lib.frag.AbsHbFrag;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.lib.utils.SysUtils;
import com.howbuy.lib.utils.ViewUtils;
import com.howbuy.utils.FundUtils;
import com.howbuy.wireless.entity.protobuf.trustdaquan.TrustInfoListProto.TrustInfoList;

public class FragDetailsTrust extends AbsHbFrag implements IReqNetFinished {
	private static final int HAND_TRUST_DETAILS = 1;// 信托列表详情.
	private QuickReturnFooter mQuickReturn = null;
	private LinearLayout mLayProgress = null;
	private com.howbuy.wireless.entity.protobuf.trustdaquan.TrustInfoProtos.TrustInfo mTrust;
	private String mProductId = null;
	private TextView mTvTradeState, mTvSubmit;
	private TextView mTvTitle, mTvIncrease;
	private TextView[] mTvValues;
	private int mLaunchResource = 0;

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_detail_trust;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mQuickReturn = new QuickReturnFooter(getSherlockActivity(),
				R.layout.frag_detail_trust, R.layout.com_details_footer);
		mRootView = mQuickReturn.createView();
		mLayProgress = new LinearLayout(getSherlockActivity());
		ProgressBar pb = new ProgressBar(getSherlockActivity());
		mLayProgress.addView(pb);
		mLayProgress.setBackgroundColor(0xfff5f5f5);
		mLayProgress.setClickable(true);
		mLayProgress.setGravity(Gravity.CENTER);
		((ViewGroup) mRootView).addView(mLayProgress, -1, -1);
		return mRootView;
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		Bundle argB = getArguments();
		if (argB != null) {
			if (mTitleLable == null) {
				mTitleLable = argB.getString(ValConfig.IT_NAME);
			}
			mLaunchResource = argB.getInt(ValConfig.IT_FROM);
			mProductId = argB.getString(ValConfig.IT_ID);
			launchRequest(HAND_TRUST_DETAILS, null);
		}
		mTvTitle = (TextView) root.findViewById(R.id.tv_title);
		mTvIncrease = (TextView) root.findViewById(R.id.tv_increase);
		mTvValues = new TextView[9];
		mTvValues[0] = (TextView) root.findViewById(R.id.tv_value);
		mTvValues[1] = (TextView) root.findViewById(R.id.tv_value1);
		mTvValues[2] = (TextView) root.findViewById(R.id.tv_value2);
		mTvValues[3] = (TextView) root.findViewById(R.id.tv_value3);
		mTvValues[4] = (TextView) root.findViewById(R.id.tv_value4);
		mTvValues[5] = (TextView) root.findViewById(R.id.tv_value5);
		mTvValues[6] = (TextView) root.findViewById(R.id.tv_value6);
		mTvValues[7] = (TextView) root.findViewById(R.id.tv_value7);
		mTvValues[8] = (TextView) root.findViewById(R.id.tv_value8);
		mTvTradeState = (TextView) root.findViewById(R.id.tv_fund_state);
		mTvSubmit = (TextView) root.findViewById(R.id.tv_fund_buy);
		mTvTradeState.setText("服务热线 400-700-9665");
		mTvSubmit.setText("预约");

		CardDrawable cd = new CardDrawable(0xffffffff);
		cd.setShadeWidth(0, 0, 0, 2 * SysUtils.getDensity(root.getContext()));
		ViewUtils.setBackground(root.findViewById(R.id.lay_trust_up), cd);
		cd = new CardDrawable(0xffffffff);
		cd.setShadeWidth(0, 0, 0, 2 * SysUtils.getDensity(root.getContext()));
		ViewUtils.setBackground(root.findViewById(R.id.lay_trust_mid), cd);

		cd = new CardDrawable(0xffffffff);
		cd.setShadeWidth(0, 0, 0, 2 * SysUtils.getDensity(root.getContext()));
		ViewUtils.setBackground(root.findViewById(R.id.lay_trust_bot), cd);

	}

	private void handConsuleCall() {
		Analytics.onEvent(getSherlockActivity(), Analytics.CALL_400, Analytics.KEY_FROM, "信托预约");
		Uri callUri = Uri.parse("tel:" + "4007009665");
		Intent it = new Intent(Intent.ACTION_DIAL, callUri);
		startActivity(it);
	}

	public boolean launchRequest(int handType, Object obj) {
		AbsParam par = null;
		switch (handType) {
		case HAND_TRUST_DETAILS:
			par = new ParTrustListByID(mProductId, CacheOpt.TIME_DAY);
			break;
		}
		if (par != null) {
			par.setCallback(handType, this).execute();
			return true;
		}
		return false;
	}

	private void initViewData(
			com.howbuy.wireless.entity.protobuf.trustdaquan.TrustInfoProtos.TrustInfo data) {
		mTvTitle.setText(data.getCpmc());
		SpannableString increase = new SpannableString(data.getYqsyCode() + "%");
		StyleSpan span = new StyleSpan(Typeface.BOLD);
		int n = increase.length();
		increase.setSpan(span, 0, n - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		mTvIncrease.setText(increase);

		String comment = data.getHmdp();
		mTvValues[0].setText("好买点评：");
		if (!StrUtils.isEmpty(comment)) {
			mTvValues[0].append(comment.replaceAll("\\s", " "));
		}
		String year = data.getCpqxCode();
		if (!StrUtils.isEmpty(year)) {
			mTvValues[1].setText(StrUtils.formatF(Float.parseFloat(year), 1) + "年期");
		}
		String money = data.getQszjCode();
		if (!StrUtils.isEmpty(money)) {
			mTvValues[2].setText(StrUtils.formatF(Float.parseFloat(money), 2) + "万");
		}
		String sysm = data.getSysm();
		if (!StrUtils.isEmpty(sysm)) {
			mTvValues[3].setText(sysm.replaceAll(";|；", "\n"));
		}
		mTvValues[4].setText(data.getGsmc());
		mTvValues[5].setText(FundUtils.formatProperty(data.getFxgm() + "", 2));
		mTvValues[6].setText(data.getTzfxCode());
		mTvValues[7].setText(data.getCpsm());
		mTvValues[8].setText(data.getFxkz());
	}

	@Override
	public boolean onXmlBtClick(View v) {
		switch (v.getId()) {
		case R.id.tv_fund_buy:
			handSubmit(FragSubscribeDetails.class.getName(), "在线预约");
			break;
		case R.id.tv_fund_state: {
			handConsuleCall();
			break;
		}
		default:
			pop("onXmlBtClick " + v, true);
		}
		return super.onXmlBtClick(v);
	}

	private void handSubmit(String fragName, String title) {
		if (mTrust != null) {
			Bundle b = new Bundle();
			b.putString(ValConfig.IT_NAME, title);
			fragName = FragSubscribeDetails.class.getName();
			b.putBoolean(ValConfig.IT_TYPE, false);
			b.putString(ValConfig.IT_URL, mTrust.getCpmc());
			FragOpt opt = new FragOpt(fragName, b, FragOpt.FRAG_POPBACK);
			((AtyEmpty) getSherlockActivity()).switchToFrag(opt);
		}
	}

	@Override
	public void onRepNetFinished(ReqResult<ReqNetOpt> result) {
		final int handType = result.mReqOpt.getHandleType();
		if (handType == HAND_TRUST_DETAILS) {
			mLayProgress.setVisibility(View.GONE);
			if (result.isSuccess()) {
				TrustInfoList list = (TrustInfoList) result.mData;
				if (list != null && list.getProductListCount() > 0) {
					mTrust = list.getProductList(0);
					initViewData(mTrust);
				}

			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return false;
		// return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyBack(boolean fromBar, boolean isFirstPress, boolean isTwiceInTime) {
		if (fromBar) {
			Intent upIntent = new Intent(getSherlockActivity(), AtyTbMain.class);//
			upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			if (AtyInfs.hasAty(AtyTbMain.class, null) == null) {
				TaskStackBuilder.create(getSherlockActivity()).addNextIntent(upIntent)
						.startActivities();
				getSherlockActivity().overridePendingTransition(0, 0);
			} else {
				if (mLaunchResource == ValConfig.SOURCE_PUSH) {
					NavUtils.navigateUpTo(getSherlockActivity(), upIntent);
				} else {
					return super.onKeyBack(fromBar, isFirstPress, isTwiceInTime);
				}
			}
			return true;
		}
		return super.onKeyBack(fromBar, isFirstPress, isTwiceInTime);
	}
}
