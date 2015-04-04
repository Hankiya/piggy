package howbuy.android.piggy.ui.base;

import howbuy.android.bean.ListBean;

/**
 * ListView加载
 * 
 * @author yescpu
 * 
 */
public interface IListLoadPatch<G extends ListBean<?, ?>> {

	/**
	 * 设置Adapter
	 */
	abstract void onAbsBuildAdapter();

	/**
	 * 加载完成
	 * 
	 * @param newValue
	 */
	abstract void onAbsPostExecute(ResultBean<G> newValue);

	/**
	 * 调用数据逻辑
	 * 
	 * @return
	 */
	abstract ResultBean<G> onAbsBackgroundData();

}
