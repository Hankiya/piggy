package com.howbuy.frag;

import howbuy.android.palmfund.R;

import java.util.ArrayList;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.howbuy.adp.ArticalCollectAdp;
import com.howbuy.adp.ArticalCollectAdp.ArticalHolder;
import com.howbuy.aty.AtyEmpty;
import com.howbuy.component.AppFrame;
import com.howbuy.config.ValConfig;
import com.howbuy.db.DbUtils;
import com.howbuy.db.DbUtils.SqlExeObj;
import com.howbuy.entity.NewsItem;
import com.howbuy.lib.compont.GlobalServiceMger.ScheduleTask;
import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.frag.AbsHbFrag;
import com.howbuy.lib.interfaces.ITimerListener;
import com.howbuy.lib.net.AsyPoolTask;
import com.howbuy.lib.net.ReqOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.wireless.entity.protobuf.NewsProtos.News;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;
import com.mobeta.android.dslv.DragSortListView.RemoveListener;
import com.mobeta.android.dslv.DragSortListView.TouchListener;

@SuppressLint("NewApi")
public class FragArticalCollectList extends AbsHbFrag implements OnItemClickListener,
		OnItemLongClickListener, RemoveListener, ITimerListener {
	public static final int TASK_QUERY = 1;
	public static final int TASK_DELETE = 2;
	private LinkedList<OperateRecord> mOperates = new LinkedList<FragArticalCollectList.OperateRecord>();
	private PopupWindow mPop = null;
	private TextView mStatBar = null;
	private DragSortListView mListView;
	private DragSortController mDragSortController;
	private ActionMode mActionMode;
	private ArticalCollectAdp mAdapter = null;
	private boolean mRequireGoBack = false;
	private boolean mEnableEditModeRemove = true;

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_artical_collect;
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		if (mTitleLable == null) {
			mTitleLable = getArguments().getString(ValConfig.IT_NAME);
		}
		mListView = (DragSortListView) root.findViewById(R.id.listview);
		mListView.setOnItemLongClickListener(this);
		mListView.setOnItemClickListener(this);
		mListView.setRemoveListener(this);
		TextView tv = (TextView) root.findViewById(R.id.empty);
		tv.setText("无收藏文章");
		mListView.setEmptyView(tv);
		mDragSortController = new DragSortController(mListView);
		mDragSortController.setRemoveEnabled(true);
		mListView.setPadding(0, 0, 0, 0);
		if (mAdapter == null) {
			mAdapter = new ArticalCollectAdp(getSherlockActivity(), null);
			mAdapter.setSelectedDrawable(new ColorDrawable(0xffdde1e7));
			new ArticalTask(TASK_QUERY).execute(false);
		}

		mListView.setAdapter(mAdapter);
		mListView.setListTouchListener(new TouchListener() {
			@Override
			public void onListTouch(MotionEvent e) {
				if (mPop != null && mPop.isShowing()) {
					hidePopWind();
				}
			}
		});
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		if (mActionMode == null) {
			mActionMode = getSherlockActivity().startActionMode(new ActionModeEdit());
			if (!mEnableEditModeRemove) {
				mDragSortController.setRemoveEnabled(false);
				mListView.setDragEnabled(false);
			}
		}
		return false;// return true 将不会调用onItemClick事件.
	}

	private void onSelectedChanged() {
		int selectedCount = mAdapter.getSelectedCount();
		if (selectedCount == 0) {
			// mActionMode.setTitle("点击选中");
			mActionMode.finish();
		} else {
			mActionMode.setTitle("已选中" + selectedCount + "篇文章");
		}
	}

	@Override
	public void remove(int which) {
		NewsItem removeItem = mAdapter.removeItem(which, true);
		if (mActionMode != null && removeItem.hasFlag(NewsItem.ARTICAL_SELECTED)) {
			if (mEnableEditModeRemove) {
				mAdapter.setSelectedCount(mAdapter.getSelectedCount() - 1);
				onSelectedChanged();
			}
		}
		pushRecord(which, removeItem, false);
		showPopWind("文章已删除");
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (mActionMode != null) {
			if (parent instanceof DragSortListView) {
				view = ((ViewGroup) view).getChildAt(0);
				((ArticalHolder) view.getTag()).changeView(0);
				onSelectedChanged();
			}
		} else {
			NewsItem item = (NewsItem) mAdapter.getItem(position);
			item.addFlag(NewsItem.ARTICAL_READED | NewsItem.ARTICAL_COLLECTED);
			int articalType = item.getNewsType();
			Bundle b = new Bundle();
			b.putParcelable(ValConfig.IT_ENTITY, item);
			b.putString(ValConfig.IT_NAME, articalType == 0 ? "新闻资讯" : "研究报告");
			b.putInt(ValConfig.IT_TYPE, articalType);
			b.putInt(ValConfig.IT_FROM, ValConfig.SOURCE_OTHER);
			Intent t = new Intent(getSherlockActivity(), AtyEmpty.class);
			t.putExtra(AtyEmpty.KEY_FRAG_NAME, FragArticalRead.class.getName());
			t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
			startActivityForResult(t, 1);
		}
	}

	public void removeSelectedItem() {
		ArrayList<NewsItem> its = mAdapter.getItems();
		int n = its.size();
		NewsItem it = null;
		for (int i = n - 1; i >= 0; i--) {
			it = its.get(i);
			if (it.hasFlag(NewsItem.ARTICAL_SELECTED)) {
				pushRecord(i, it, true);
			}
		}
		int s = mOperates.size();
		for (int i = 0; i < s; i++) {
			mAdapter.removeItem(mOperates.get(i).Item, false);
		}
		mAdapter.notifyDataSetChanged();
		showPopWind(mAdapter.getSelectedCount() + "篇文章已删除");
		if (mActionMode != null) {
			mActionMode.finish();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			new ArticalTask(TASK_QUERY).execute(false);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private class ArticalTask extends AsyPoolTask<String, Void, ReqResult<ReqOpt>> {
		private int mTaskType = 0;

		public ArticalTask(int taskType) {
			mTaskType = taskType;
		}

		private String buildQuerySql(StringBuilder sb) {
			sb.append("select value from tb_common where key='artical_collect' order by date desc");
			return sb.toString();
		}

		private String buildDelSql(StringBuilder sb) {
			sb.append("delete from tb_common where key='artical_collect' and subkey in(");
			OperateRecord op = null;
			while ((op = popRecord()) != null) {
				sb.append("'").append(op.Item.getId()).append("',");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(')');
			return sb.toString();
		}

		@Override
		protected ReqResult<ReqOpt> doInBackground(String... params) {
			ReqResult<ReqOpt> res = new ReqResult<ReqOpt>(new ReqOpt(0, null, mTaskType));
			try {
				StringBuilder sb = new StringBuilder();
				if (mTaskType == TASK_QUERY) {
					Cursor c = DbUtils.query(buildQuerySql(sb), null, false);
					ArrayList<NewsItem> r = new ArrayList<NewsItem>();
					while (c.moveToNext()) {
						r.add(new NewsItem(News.parseFrom(c.getBlob(0))));
					}
					res.setData(r);
				} else {
					WrapException err = DbUtils.exeSql(new SqlExeObj(buildDelSql(sb)), true);
					if (err == null) {
						res.setData(null);
					} else {
						res.setErr(err);
					}
				}
			} catch (Exception e) {
				res.setErr(WrapException.wrap(e, null));
			}
			return res;
		}

		@Override
		protected void onPostExecute(ReqResult<ReqOpt> r) {
			if (r.isSuccess()) {
				if (mTaskType == TASK_QUERY) {
					mAdapter.setItems((ArrayList<NewsItem>) r.mData, true);
				}
			} else {
			}
		}

	}

	private final class ActionModeEdit implements ActionMode.Callback {
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			menu.add("delete").setIcon(R.drawable.ic_action_discard)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			mode.setTitle("已选中1篇文章");
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			if (item.getTitle().equals("delete")) {
				if (mAdapter.getSelectedCount() > 0) {
					removeSelectedItem();
				}
			}
			return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			if (!mEnableEditModeRemove) {
				mListView.setDragEnabled(true);
				mDragSortController.setRemoveEnabled(true);
			}

			mAdapter.cleanSelectedFlags(true);
			mActionMode = null;
		}
	}

	private void pushRecord(int index, NewsItem item, boolean mutiOpt) {
		mOperates.push(new OperateRecord(index, item));
	}

	private OperateRecord popRecord() {
		return mOperates.poll();
	}

	private class OperateRecord {
		public OperateRecord(int index, NewsItem item) {
			Index = index;
			Item = item;
		}

		public int Index = -1;
		public NewsItem Item = null;
	}

	@Override
	public boolean onXmlBtClick(View v) {
		if (v.getId() == R.id.tv_artical_del_cancle) {
			mRequireGoBack = true;
			hidePopWind();
		}
		return super.onXmlBtClick(v);
	}

	private void buildPopWind() {
		final View lay = LayoutInflater.from(getSherlockActivity()).inflate(
				R.layout.com_pop_restore, null);
		mStatBar = (TextView) lay.findViewById(R.id.tv_artical_del_state);
		mPop = new PopupWindow(lay, -1, -2);
		mPop.setAnimationStyle(R.style.pop_anim_bottom_appear);
	}

	private void showPopWind(String msg) {
		if (mPop == null) {
			buildPopWind();
		}
		mStatBar.setText(msg);
		if (!mPop.isShowing()) {
			mPop.showAtLocation(mListView, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
			ScheduleTask task = new ScheduleTask(1, this);
			task.setExecuteArg(3000, 0, false);
			AppFrame.getApp().getGlobalServiceMger().addTimerListener(task);
		}
	}

	private void hidePopWind() {
		if (mPop != null && mPop.isShowing()) {
			mPop.dismiss();
			onRequireHideUndoBar();
			AppFrame.getApp().getGlobalServiceMger().removeTimerListener(1, this);
		}
	}

	@Override
	public void onTimerRun(int which, int timerState, boolean hasTask, int timesOrSize) {
		if (timerState == ITimerListener.TIMER_SCHEDULE) {
			hidePopWind();
		}
	}

	public void onRequireHideUndoBar() {
		if (mRequireGoBack) {// 按了撤回.
			OperateRecord op = null;
			while ((op = popRecord()) != null) {
				op.Item.subFlag(NewsItem.ARTICAL_SELECTED);
				mAdapter.insertItem(op.Item, op.Index, false);
			}
			mRequireGoBack = false;
		} else {
			new ArticalTask(TASK_DELETE).execute(false);
		}
		mAdapter.notifyDataSetInvalidated();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mPop != null && mPop.isShowing()) {
			hidePopWind();
		}
	}
}
