package com.howbuy.interfaces;

import com.howbuy.lib.entity.AbsLoadList;
import com.howbuy.lib.entity.AbsResult;

/**
 * ListView加载
 * 
 * @author yescpu
 * 
 */
public interface IListLoadPatch<G extends AbsLoadList<?, ?>> {

	/**
	 * 加载完成
	 * 
	 * @param newValue
	 */
	abstract void onAbsPostExecute(AbsResult<G> newValue);

	/**
	 * 调用数据逻辑
	 * 
	 * @return
	 */
	abstract AbsResult<G> onAbsBackgroundData();

}
