package com.howbuy.adp;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import howbuy.android.palmfund.R;

import com.howbuy.config.ValConfig;
import com.howbuy.lib.adp.AbsAdp;
import com.howbuy.lib.adp.AbsViewHolder;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.utils.FundUtils;
import com.howbuy.wireless.entity.protobuf.ManagerInfoProtos.ManagerInfo;

public class ManagerAdp extends AbsAdp<ManagerInfo> {
	public ManagerAdp(Context cx, ArrayList<ManagerInfo> list) {
		super(cx, list);
	}

	@Override
	protected View getViewFromXml(int type, ViewGroup p) {
		return mLf.inflate(R.layout.com_list_manager_item, null);
	}

	@Override
	protected AbsViewHolder<ManagerInfo> getViewHolder() {
		return new ManagerHolder();
	}

	public class ManagerHolder extends AbsViewHolder<ManagerInfo> {
		TextView TvName, TvDate, TvReturn, TvAvg;

		@Override
		protected void initView(View root, int type) {
			TvName = (TextView) root.findViewById(R.id.tv_name);
			TvDate = (TextView) root.findViewById(R.id.tv_date);
			TvReturn = (TextView) root.findViewById(R.id.tv_return);
			TvAvg = (TextView) root.findViewById(R.id.tv_avg);
		}

		@Override
		protected void initData(int index, int type, ManagerInfo item, boolean isReuse) {
			TvName.setText(item.getRyxm());
			TvDate.setText(formatDate(item.getRzqj()));
			FundUtils.formatFundValue(TvReturn, item.getRqhb(), null,false, FundUtils.VALUE_HBDR);
			FundUtils.formatFundValue(TvAvg, item.getTlpj(), null,false, FundUtils.VALUE_HBDR);
		}

		String formatDate(String ds) {
			if (!StrUtils.isEmpty(ds)) {
				String[] ss = ds.split("-");
				StringBuffer sb = new StringBuffer();
				if (ss.length > 0) {
					if (ss.length == 1) {
						sb.append(StrUtils.timeFormat(ss[0], ValConfig.DATEF_YMD,
								ValConfig.DATEF_YMD_));
						sb.append(" 至今");
					} else {
						sb.append(StrUtils.timeFormat(ss[0], ValConfig.DATEF_YMD,
								ValConfig.DATEF_YMD_));
						sb.append(" 至 ");
						sb.append(StrUtils.timeFormat(ss[1], ValConfig.DATEF_YMD,
								ValConfig.DATEF_YMD_));
					}
				}
				return sb.toString();
			}
			return ValConfig.NULL_TXT0;
		}
	}

}
