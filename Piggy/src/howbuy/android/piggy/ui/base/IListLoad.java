package howbuy.android.piggy.ui.base;

/**
 * ListView加载
 * 
 * @author yescpu
 * 
 */
public interface IListLoad {
	/**
	 * 加载缓存
	 */
	public abstract void onAbsPreCacheLoad();

	/**
	 * 加载缓存后再加载网络
	 */
	public abstract void onAbsPreFristLoad();

	/**
	 * 刷新
	 */
	public abstract void onAbsPreReLoad();

}
