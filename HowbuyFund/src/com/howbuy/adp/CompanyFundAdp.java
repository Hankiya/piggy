package com.howbuy.adp;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.howbuy.config.FundConfig;
import com.howbuy.config.FundConfig.FundType;
import com.howbuy.entity.NetWorthBean;
import howbuy.android.palmfund.R;
import com.howbuy.lib.adp.AbsAdp;
import com.howbuy.lib.adp.AbsViewHolder;

public class CompanyFundAdp extends AbsAdp</* FundInfo */NetWorthBean> {
	public CompanyFundAdp(Context cx, ArrayList</* FundInfo */NetWorthBean> list) {
		super(cx, list);
	}

	@Override
	protected View getViewFromXml(int type, ViewGroup p) {
		return mLf.inflate(R.layout.com_list_company_item, null);
	}

	@Override
	protected AbsViewHolder</* FundInfo */NetWorthBean> getViewHolder() {
		return new CompanyFundHolder();
	}

	public class CompanyFundHolder extends AbsViewHolder</* FundInfo */NetWorthBean> {
		TextView TvName, TvCode, TvType;

		@Override
		protected void initView(View root, int type) {
			TvName = (TextView) root.findViewById(R.id.tv_name);
			TvCode = (TextView) root.findViewById(R.id.tv_code);
			TvType = (TextView) root.findViewById(R.id.tv_type);
		}

		@Override
		protected void initData(int index, int type, /* FundInfo */NetWorthBean item, boolean isReuse) {
			TvName.setText(/* item.getJjjc() */item.getJjmc());
			TvCode.setText(item.getJjdm());
			FundType f = FundConfig.getConfig().getType(item.getJjfl());
			if (f != null) {
				TvType.setText(/* item.getRyxm() */f.FundName);
			} else {
				TvType.setText(/* item.getRyxm() */item.getJjfl() + "私募");
			}

		}

	}

}
