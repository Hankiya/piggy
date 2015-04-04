package howbuy.android.bean;

public abstract class ItemBean {
	protected boolean isNeedReload;

	public abstract String getmClassName();

	public boolean isNeedReload() {
		return isNeedReload;
	}

	public void setNeedReload(boolean isNeedReload) {
		this.isNeedReload = isNeedReload;
	}

}
