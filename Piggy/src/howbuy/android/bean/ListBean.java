package howbuy.android.bean;

import java.util.List;

public abstract class ListBean<T extends ItemBean, K> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int total_number = 0;
	protected boolean isNeedReload;

	public abstract int getSize();

	public int getTotal_number() {
		return total_number;
	}

	public void setTotal_number(int total_number) {
		this.total_number = total_number;
	}

	public boolean isNeedReload() {
		return isNeedReload;
	}

	public void setNeedReload(boolean isNeedReload) {
		this.isNeedReload = isNeedReload;
	}

	public abstract T getItem(int position);

	public abstract List<T> getItemList();

	public abstract void clearData();

	public abstract void addData(K newValue);

	public abstract void replaceAll(K replaceValue);

}
