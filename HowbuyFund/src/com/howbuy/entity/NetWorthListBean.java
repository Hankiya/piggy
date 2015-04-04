package com.howbuy.entity;

import android.os.Parcel;

import com.howbuy.lib.entity.AbsLoadList;

public class NetWorthListBean extends AbsLoadList<NetWorthBean, NetWorthListBean> {
	@Override
	public void addItems(NetWorthListBean valueList) {
		addItems(valueList.getItems(), true);
		setTotalNum(valueList.getTotalNum());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}

	public static final Creator<NetWorthListBean> CREATOR = new Creator<NetWorthListBean>() {
		@Override
		public NetWorthListBean createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			NetWorthListBean _newsListBean = new NetWorthListBean();
			return _newsListBean;
		}

		@Override
		public NetWorthListBean[] newArray(int size) {
			return new NetWorthListBean[size];
		}
	};
}
