package com.howbuy.adp;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.howbuy.config.ValConfig;
import howbuy.android.palmfund.R;
import com.howbuy.lib.adp.AbsAdp;
import com.howbuy.lib.adp.AbsViewHolder;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.utils.FundUtils;
import com.howbuy.wireless.entity.protobuf.FundProtos.Fund;

public class ManagerAchieveAdp extends AbsAdp<Fund> {
	public ManagerAchieveAdp(Context cx, ArrayList<Fund> list) {
		super(cx, list);
	}

	@Override
	protected View getViewFromXml(int type, ViewGroup p) {
		return mLf.inflate(R.layout.com_list_manager_archieve_item, null);
	}

	@Override
	protected AbsViewHolder<Fund> getViewHolder() {
		return new ManagerArchieveHolder();
	}

	public class ManagerArchieveHolder extends AbsViewHolder<Fund> {
		TextView TvName, TvDate, TvDateTo, TvIncrease;

		@Override
		protected void initView(View root, int type) {
			TvName = (TextView) root.findViewById(R.id.tv_name);
			TvDate = (TextView) root.findViewById(R.id.tv_date);
			TvDateTo = (TextView) root.findViewById(R.id.tv_dateto);
			TvIncrease = (TextView) root.findViewById(R.id.tv_increase);
		}

		@Override
		protected void initData(int index, int type, Fund item, boolean isReuse) {
			TvName.setText(item.getJjjc());
			FundUtils.formatFundValue(TvIncrease, item.getRqhb(), null,false, FundUtils.VALUE_HBDR);
			String range = item.getRzrq();
			int i = range.indexOf("至");
			if (i != -1 && i < range.length()) {
				String s1 = range.substring(0, i).trim();
				String s2 = range.substring(i + 1).trim();
				if (s1.length() > 0) {
					TvDate.setText(StrUtils.timeFormat(s1, ValConfig.DATEF_YMD,
							ValConfig.DATEF_YMD_));
				}
				if (s2.length() > 0) {
					if (StrUtils.isNumeric(s2)) {
						TvDateTo.setText(StrUtils.timeFormat(s2, ValConfig.DATEF_YMD,
								ValConfig.DATEF_YMD_));
					} else {
						TvDateTo.setText("至" + s2);
					}
				}
			} else {
				ArrayList<String> ls = StrUtils.findAllNumber(range);
				int n = ls.size();
				if (n == 0) {
					TvDate.setText(range);
					TvDateTo.setText(null);
				} else {
					if (n == 1) {
						TvDate.setText(StrUtils.timeFormat(ls.get(0), ValConfig.DATEF_YMD,
								ValConfig.DATEF_YMD_));
						TvDateTo.setText("至今");
					} else {
						TvDate.setText(StrUtils.timeFormat(ls.get(0), ValConfig.DATEF_YMD,
								ValConfig.DATEF_YMD_));
						TvDateTo.setText(StrUtils.timeFormat(ls.get(1), ValConfig.DATEF_YMD,
								ValConfig.DATEF_YMD_));
					}
				}
			}
		}

	}

}
