package howbuy.android.util;


import howbuy.android.piggy.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.actionbarsherlock.view.MenuItem;

public class ActionViewProgressUtil {
	private FrameLayout layout;
	private MenuItem refreshItem;

	public ActionViewProgressUtil(Context mContext) {
		layout = (FrameLayout) LayoutInflater.from(mContext).inflate(R.layout.refresh_action_view_progress, null);
	}

	public FrameLayout getLayout() {
		return layout;
	}

	public MenuItem getRefreshItem() {
		return refreshItem;
	}

	/**
	 * 第一步
	 * 
	 * @param refreshItem
	 */
	public void setRefreshItem(MenuItem refreshItem) {
		this.refreshItem = refreshItem;
	}

	/**
	 * 第二部
	 */
	public void showActionView() {
		if (refreshItem != null) {
			refreshItem.setActionView(layout);
		}
	}

	/**
	 * 第三部
	 */
	public void reMoveActionView() {
		if (refreshItem != null) {
			refreshItem.setActionView(null);
		}
	}

}
