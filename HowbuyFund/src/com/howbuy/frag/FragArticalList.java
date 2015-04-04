package com.howbuy.frag;

import howbuy.android.palmfund.R;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.howbuy.adp.ArticalAdp;
import com.howbuy.aty.AtyEmpty;
import com.howbuy.commonlib.ParNewsByPage;
import com.howbuy.commonlib.ParResearchByPage;
import com.howbuy.config.ValConfig;
import com.howbuy.db.DbUtils;
import com.howbuy.db.DbUtils.SqlExeObj;
import com.howbuy.entity.NewsItem;
import com.howbuy.entity.NewsList;
import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.AsyPoolTask;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.wireless.entity.protobuf.NewsInfoProto.NewsInfo;
import com.howbuy.wireless.entity.protobuf.OpinionInfoProto.OpinionInfo;
import com.tang.library.pulltorefresh.PullToRefreshBase;

public class FragArticalList extends AbsFragPage<NewsList> implements IReqNetFinished {
	private static final int TASK_QUERY_READ_HISTORY = 1;
	private static final int TASK_ADD_READ_HISTORY = 2;
	private static final int TASK_PARSE_ARTICAL = 4;

	private static final int HAND_REQUEST_RESEARCH = 1;// 研报.
	private static final int HAND_REQUEST_NEWS = 2;// 新闻.

	// public static final String newsTypeNews = "129016";
	// public static final String newsbasicTypeNews = "0";
	// public static final String newsTypeYan = "10260049";
	// public static final String newsbasicTypeYan = "204";
	private ArticalAdp mAdapter;
	long mCacheTime = 0;
	String newsType = "129013";
	/**
	 * 0是news，1是研报
	 */
	int mNewsOrResearch = 0;
	private String mArticalClass = null;
	private StringBuilder mHistory = null;

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		super.initViewAdAction(root, bundle);
		Bundle argBund = getArguments();
		if (argBund != null) {
			mArticalClass = argBund.getString(ValConfig.IT_NAME);
			if (argBund.getInt(ValConfig.IT_ID) == 1) {
				newsType = "10260047";
				mNewsOrResearch = 1;
			}
		}
		if (mAdapter == null) {
			mAdapter = new ArticalAdp(getActivity().getLayoutInflater(), new NewsList());
		}
		mPullListView.setAdapter(mAdapter);
		mPageNum = 1;
		mPageCount = mPageSizeBasic;
		mCacheTime = CacheOpt.TIME_WEEK;
		launchRequest(mNewsOrResearch == 1 ? HAND_REQUEST_RESEARCH : HAND_REQUEST_NEWS,
				ValConfig.LOAD_LIST_FIRST);
		showProgress(true);
	}

	public NewsList getResearch(OpinionInfo infos) {
		if (infos != null) {
			if (infos.getOpinionCount() > 0) {
				NewsList list = new NewsList();
				for (int i = 0; i < infos.getOpinionCount(); i++) {
					NewsItem nBean = new NewsItem(infos.getOpinion(i));
					if (-1 != mHistory.indexOf(nBean.getId() + "")) {
						nBean.addFlag(NewsItem.ARTICAL_READED);
					}
					list.addItem(nBean);
				}
				list.setTotalNum(infos.getTotalNum());
				return list;
			}
		}
		return null;
	}

	public NewsList getNews(NewsInfo infos) {
		if (infos != null) {
			if (infos.getNewsCount() > 0) {
				NewsList list = new NewsList();
				for (int i = 0; i < infos.getNewsCount(); i++) {
					NewsItem nBean = new NewsItem(infos.getNews(i));
					if (-1 != mHistory.indexOf(nBean.getId() + "")) {
						nBean.addFlag(NewsItem.ARTICAL_READED);
					}
					list.addItem(nBean);
				}
				list.setTotalNum(infos.getTotalNum());
				return list;
			}
		}
		return null;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg2 != 0) {
			arg2 = arg2 - 1;
		}
		NewsItem mBean = (NewsItem) mAdapter.getItem(arg2);
		setReaded(mBean);
		Bundle b = new Bundle();
		b.putParcelable(ValConfig.IT_ENTITY, mBean);
		b.putString(ValConfig.IT_NAME, mArticalClass);
		b.putInt(ValConfig.IT_TYPE, mNewsOrResearch);
		Intent t = new Intent(getSherlockActivity(), AtyEmpty.class);
		t.putExtra(AtyEmpty.KEY_FRAG_NAME, FragArticalRead.class.getName());
		t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
		startActivity(t);
	}

	public void setReaded(NewsItem mBean) {
		if (!mBean.hasFlag(NewsItem.ARTICAL_READED)) {
			mBean.addFlag(NewsItem.ARTICAL_READED);
			mHistory.append(mBean.getId()).append('#');
			new ArticalHistory(TASK_ADD_READ_HISTORY).execute(false, mBean.getId() + "");
		}
	}

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_content_list;
	}

	@Override
	public void onRepNetFinished(ReqResult<ReqNetOpt> result) {
		int key = (Integer) result.mReqOpt.getArgInt();
		if (result.isSuccess()) {
			if (mHistory == null) {
				mHistory = new StringBuilder(128);
				new ArticalHistory(TASK_QUERY_READ_HISTORY).execute(false, result.mData, key);
			} else {
				new ArticalHistory(TASK_PARSE_ARTICAL).execute(false, result.mData, key);
			}
		} else {
			mEmpty.setText("请求失败了,下拉刷新试试");
			if (ValConfig.LOAD_LIST_FIRST == key) {
				showProgress(false);
			}
			mPullListView.onRefreshComplete();
		}
	}

	public boolean launchRequest(int handType, int loadType) {
		AbsParam par = null;
		switch (handType) {
		case HAND_REQUEST_NEWS:
			par = new ParNewsByPage(mCacheTime).setParams(newsType,
			/* newsBasicType */null, mPageNum, mPageCount, 0);
			break;
		case HAND_REQUEST_RESEARCH:
			par = new ParResearchByPage(mCacheTime).setParams(newsType,
			/* newsBasicType */null, mPageNum, mPageCount, 0);
			break;
		}
		if (par != null) {
			par.setArgInt(loadType);
			if (loadType == ValConfig.LOAD_LIST_FIRST) {
				par.addFlag(ReqNetOpt.FLAG_CACHE_THEN_REQ);
			}
			par.setCallback(handType, this).execute();
			return true;
		}
		return false;
	}

	public void checkNeedUpdate(int curNet) {
		if (mAdapter.isEmpty()) {
			onPullDownToRefresh(mPullListView);
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> arg0) {
		mPageNum = 1;
		mCacheTime = 0;
		launchRequest(mNewsOrResearch == 1 ? HAND_REQUEST_RESEARCH : HAND_REQUEST_NEWS,
				ValConfig.LOAD_LIST_REFUSH);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> arg0) {
		if (checkIsLastPage(mAdapter.getItems())) {
			pop("没有更多数据页了", false);
			mPullListView.onRefreshComplete();
		} else {
			mCacheTime = 0;
			launchRequest(mNewsOrResearch == 1 ? HAND_REQUEST_RESEARCH : HAND_REQUEST_NEWS,
					ValConfig.LOAD_LIST_PAGE);
		}
	}

	class ArticalHistory extends AsyPoolTask<Object, Void, ReqResult<ReqOpt>> {
		int mTaskType = 0;

		public ArticalHistory(int taskType) {
			this.mTaskType = taskType;
		}

		private String buildQuerySql(StringBuilder sb) {
			sb.append("select subkey from tb_common where state=");
			sb.append(mNewsOrResearch).append(" and key='artical_history'");
			return sb.toString();
		}

		private SqlExeObj buildAddSql(StringBuilder sb, String id) {
			sb.append("insert into tb_common (key,subkey,state,date) values('artical_history',?,?,?)");
			return new SqlExeObj(sb.toString(), new Object[] { id, mNewsOrResearch,
					System.currentTimeMillis() });
		}

		protected ReqResult<ReqOpt> parseArticals(ReqResult<ReqOpt> r, Object... p) {
			NewsList list = mNewsOrResearch == 1 ? getResearch((OpinionInfo) p[0])
					: getNews((NewsInfo) p[0]);
			r.mReqOpt.setArgObj(p[1]);
			r.setData(list);
			return r;
		}

		@Override
		protected ReqResult<ReqOpt> doInBackground(Object... p) {
			ReqResult<ReqOpt> r = new ReqResult<ReqOpt>(new ReqOpt(0,"artical", mTaskType));
			try {
				StringBuilder sb = new StringBuilder(64);
				WrapException err = null;
				if (mTaskType == TASK_QUERY_READ_HISTORY) {
					Cursor c = DbUtils.query(buildQuerySql(sb), null, false);
					while (c.moveToNext()) {
						mHistory.append(c.getString(0)).append('#');
					}
					if (c != null && !c.isClosed()) {
						c.close();
					}
					return parseArticals(r, p);
				} else if (mTaskType == TASK_PARSE_ARTICAL) {
					return parseArticals(r, p);
				} else if (mTaskType == TASK_ADD_READ_HISTORY) {
					err = DbUtils.exeSql(buildAddSql(sb, p[0].toString()), true);
					if (err != null) {
						throw err;
					} else {
						r.setData(null);
					}
				}
			} catch (Exception e) {
				r.setErr(WrapException.wrap(e, null));
			}
			return r;
		}

		@Override
		protected void onPostExecute(ReqResult<ReqOpt> result) {
			if (result.isSuccess()) {
				if (mTaskType == TASK_ADD_READ_HISTORY) {
					mAdapter.notifyDataSetChanged();
				} else {
					mPageNum = mPageNum + 1;
					NewsList list = (NewsList) result.mData;
					if (list != null) {
						int key = (Integer) result.mReqOpt.getArgObj();
						if (ValConfig.LOAD_LIST_PAGE == key) {
							mAdapter.addItems(list, true, true);
						} else {
							mAdapter.setItems(list, true);
							if (ValConfig.LOAD_LIST_FIRST == key) {
								if (list.size() < mPageCount) {
									setPullRefushMode(true, false);
									if (list.size() == 0) {
										mEmpty.setText("没有数据了");
									}
								}
								showProgress(false);
							}
						}
					} else {
						mEmpty.setText("没有数据了");
						mEmpty.setVisibility(View.VISIBLE);
						showProgress(false);
					}
					mPullListView.onRefreshComplete();
				}
			} else {
				if (mTaskType != TASK_ADD_READ_HISTORY) {
					mEmpty.setText("数据解析错误");
					mPullListView.onRefreshComplete();
					showProgress(false);
				}
			}
		}

	}

}
