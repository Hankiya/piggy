package com.howbuy.frag;

import howbuy.android.palmfund.R;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.howbuy.adp.CompanyFundAdp;
import com.howbuy.adp.CompanyFundAdp.CompanyFundHolder;
import com.howbuy.config.FundConfig.FundType;
import com.howbuy.config.ValConfig;
import com.howbuy.control.HolderFundView;
import com.howbuy.datalib.fund.ParFtenCompanyInfo;
import com.howbuy.db.DbOperat;
import com.howbuy.entity.NetWorthBean;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AsyPoolTask;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.lib.utils.ViewUtils;
import com.howbuy.utils.FundUtils;
import com.howbuy.wireless.entity.protobuf.CompanyInfoProto.CompanyInfo;
import com.howbuy.wireless.entity.protobuf.FundInfoProtos.FundInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.tang.library.pulltorefresh.PullToRefreshBase;

public class FragCompany extends AbsFragList implements OnItemClickListener,
		ImageLoadingListener, IReqNetFinished {
	private static DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.ic_menu_refresh)
			.showImageOnFail(R.drawable.ic_action_discard).resetViewBeforeLoading(true)
			.cacheOnDisc(true).cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY)
			.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
			.displayer(new FadeInBitmapDisplayer(0)).build();// ;

	private CompanyFundAdp mAdapter = null;
	private ImageView mIvCompanyLogo = null;
	private TextView mTvCompanyName, mTvCompanyFundCount;
	private HolderFundView mFundDate = null;
	private HolderFundView mFundScale = null;
	private HolderFundView mFundManager = null;
	private HolderFundView mFundRegister = null;
	private String mJjdm = null;
	private FundType mType = null;
	private String mCompanyLocal;

	private void intiAdapter() {
		if (mAdapter == null) {
			Bundle arg = getArguments();
			if (mTitleLable == null) {
				mTitleLable = arg.getString(ValConfig.IT_NAME);
			}
			if (mType == null) {
				mType = arg.getParcelable(ValConfig.IT_TYPE);
			}
			if (mCompanyLocal == null) {
				mCompanyLocal = arg.getString(ValConfig.IT_URL);
			}
			mJjdm = arg.getString(ValConfig.IT_ID);
			mAdapter = new CompanyFundAdp(getSherlockActivity(), null);
			if (StrUtils.isEmpty(mJjdm)) {
				mListView.setAdapter(mAdapter);
			} else {
				launchRequest(1);
			}
		}
	}

	private void launchRequest(int handType) {
		if (GlobalApp.getApp().getNetType() > 1) {
			if (mAdapter == null || mAdapter.getCount() == 0) {
				showProgress(true);
			}
			new ParFtenCompanyInfo(CacheOpt.TIME_DAY).setParams(mJjdm, "0")
					.setCallback(handType, this).execute();
		} else {
			pop("网络不可用，稍后重试", false);
		}

	}

	protected View initHeadView(CompanyInfo mCompanyInf) {
		View head = LayoutInflater.from(getSherlockActivity()).inflate(
				R.layout.com_list_company_head, null);
		mIvCompanyLogo = (ImageView) head.findViewById(R.id.iv_company_logo);
		mTvCompanyName = (TextView) head.findViewById(R.id.tv_company_name);
		mTvCompanyFundCount = (TextView) head.findViewById(R.id.tv_company_fund_count);
		mFundDate = new HolderFundView();
		mFundDate.initView(head.findViewById(R.id.com_fund_company_date));
		mFundScale = new HolderFundView();
		mFundScale.initView(head.findViewById(R.id.com_fund_company_scale));
		mFundManager = new HolderFundView();
		mFundManager.initView(head.findViewById(R.id.com_fund_company_manager));
		mFundRegister = new HolderFundView();
		mFundRegister.initView(head.findViewById(R.id.com_fund_company_register));
		mTvCompanyName.setText(mCompanyInf.getJgmc());
		mTvCompanyFundCount.setText(String.format("旗下基金%1$d只", mCompanyInf.getFundInfoCount()));
		String clrq = mCompanyInf.getClrq();
		if (!StrUtils.isEmpty(clrq)) {
			if (clrq.length() == 6) {
				clrq = StrUtils.timeFormat(clrq, "yyyyMM", "yyyy-MM");
			} else if (clrq.length() == 8) {
				clrq = StrUtils.timeFormat(clrq, ValConfig.DATEF_YMD, ValConfig.DATEF_YMD_);
			}
		}

		if (mType.isSimu()) {
			mFundDate.setVisible(View.GONE);
			head.findViewById(R.id.v_sep).setVisibility(View.GONE);
			mFundScale.setText("所在地", mCompanyLocal);
			mFundManager.setText("成立时间", clrq);
		} else {
			mFundDate.setText("成立时间", clrq);
			String scale = /* FundUtils.formatProperty(mCompanyInf.getZgm(), 2) */null;
			if (!StrUtils.isEmpty(mCompanyInf.getZgm())) {
				scale = mCompanyInf.getZgm() + "亿元";
			}
			if (!StrUtils.isEmpty(scale)) {
				mFundScale.setText(
						"总规模",
						String.format("%1$s(%2$d/%3$d)", scale, mCompanyInf.getJlsl(),
								mCompanyInf.getJjsl()));
			} else {
				mFundScale.setText("总规模", null);
			}
			mFundManager.setText("总经理", mCompanyInf.getZjl());
		}
		mFundRegister.setText("注册资本", FundUtils.formatProperty(mCompanyInf.getZczb() + "", 2));
		return head;
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		super.initViewAdAction(root, bundle);
		setPullRefushMode(false, false);
		intiAdapter();
	}

	private void loadCompanyIcon(String gsdm) {
		// 线上test会改为com、80000220是公司的code String String
		String url = null;
		String subUrl = "logo/companys/XXXX.jpg".replace("XXXX", gsdm);
		if (LogUtils.mDebugUrl) {
			url = "http://static.howbuy.test/images/hws/" + subUrl;
		} else {
			url = "http://static.howbuy.com/images/hws/" + subUrl;
		}
		ImageLoader.getInstance().displayImage(url, mIvCompanyLogo, options, this);
	}

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_content_list;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Object obj = view.getTag();
		if (obj instanceof CompanyFundHolder) {
			FundUtils.launchFundDetails(this, ((CompanyFundHolder) obj).mItem.getJjdm(),
					position - 2, 0);
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		launchRequest(1);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
	}

	class AsyFundTaks extends AsyPoolTask<Void, Void, ArrayList<NetWorthBean>> {
		List<FundInfo> mList = null;

		public AsyFundTaks(List<FundInfo> r) {
			this.mList = r;
		}

		@Override
		protected ArrayList<NetWorthBean> doInBackground(Void... p) {
			StringBuffer sb = new StringBuffer(256);
			try {
				int n = mList == null ? 0 : mList.size();
				ArrayList<NetWorthBean> r = DbOperat.getInstance().queryAll(buildQueryTop(sb, n));
				int size = r == null ? 0 : r.size();
				if (size != 0) {
					ArrayList<NetWorthBean> res = new ArrayList<NetWorthBean>(size);
					for (int i = 0; i < n; i++) {
						String code = mList.get(i).getJjdm();
						for (int j = size - 1; j >= 0; j--) {
							if (r.get(j).getJjdm().equals(code)) {
								res.add(r.remove(j));
								size--;
								break;
							}
						}
					}
					return res;
				}
			} catch (WrapException e) {
			}
			return null;
		}

		private String buildQueryTop(StringBuffer sb, int n) {
			sb.append("and code in(");
			for (int i = 0; i < n; i++) {
				sb.append("'").append(mList.get(i).getJjdm()).append("',");
			}
			sb.deleteCharAt(sb.length() - 1).append(")");
			return sb.toString();
		}

		@Override
		protected void onPostExecute(ArrayList<NetWorthBean> result) {
			if (result != null) {
				mAdapter.setItems(result, true);
				if (mAdapter.isEmpty()) {
					mListView.setEmptyView(null);
					mEmpty.setVisibility(View.GONE);
				}
				mListView.setAdapter(mAdapter);
			}
		}
	}

	@Override
	public void onLoadingCancelled(String arg0, View arg1) {

	}

	@Override
	public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
		ViewUtils.setVisibility(mIvCompanyLogo, View.VISIBLE);
	}

	@Override
	public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {

	}

	@Override
	public void onLoadingStarted(String arg0, View arg1) {

	}

	@Override
	public void onRepNetFinished(ReqResult<ReqNetOpt> result) {
		showProgress(false);
		if (result.isSuccess()) {
			CompanyInfo cif = (CompanyInfo) result.mData;
			if (cif != null) {
				mListView.addHeaderView(initHeadView(cif));
				if (cif.getFundInfoCount() > 0) {
					new AsyFundTaks(cif.getFundInfoList()).execute(false);
				} else {
					mListView.setAdapter(mAdapter);
					mListView.setVisibility(View.VISIBLE);
					mEmpty.setVisibility(View.GONE);
				}
				mListView.setHeaderDividersEnabled(false);
				loadCompanyIcon(cif.getGsdm());
				setPullRefushMode(false, false);
			} else {
				onLoadFailed(result.mErr);
			}
		} else {
			onLoadFailed(result.mErr);
		}
	}

	private void onLoadFailed(WrapException e) {
		setPullRefushMode(true, false);
		if (GlobalApp.getApp().getNetType() > 1) {
			pop("加载失败，下拉重式", false);
		} else {
			pop("加载失败", false);
		}

	}

}
