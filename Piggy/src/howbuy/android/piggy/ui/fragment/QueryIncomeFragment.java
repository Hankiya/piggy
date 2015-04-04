package howbuy.android.piggy.ui.fragment;

import howbuy.android.lib.MyAsyncTask;
import howbuy.android.piggy.MyAlertDialog;
import howbuy.android.piggy.R;
import howbuy.android.piggy.api.DispatchAccessData;
import howbuy.android.piggy.api.dto.EstimatesIncomeItem;
import howbuy.android.piggy.api.dto.EstimatesIncomeList;
import howbuy.android.piggy.api.dto.IncomeItemDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.help.CacheHelp;
import howbuy.android.piggy.ui.SaveMoneyActivity;
import howbuy.android.piggy.ui.base.AbstractFragment;
import howbuy.android.piggy.widget.LoadingFooter;
import howbuy.android.piggy.widget.LoadingFooter.State;
import howbuy.android.util.Cons;
import howbuy.android.util.StringUtil;
import howbuy.android.util.TimestampUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.umeng.analytics.MobclickAgent;

public class QueryIncomeFragment extends AbstractFragment {
	public static final String QueryDayNum = "30";
	public static final String STARTDATE = "20130125";
	public static final int typeLoadOut = 1;
	public static final int typeLoadIN = 2;
	public static final String typeTradeSG = "1";
	public static final String typeTradeSH = "2";
	public static final String typeTradeFH = "3";
	public static final int Page = 50;
	private int mLoadType = 1;// 1为外部 2为内部
	private ListView mListView;
	private TradeQueryTask mTask;
	private LoadingFooter mLoadingFooter;
	private TradeAdapter mAdapter;
	private EstimatesIncomeList mListAll;
	private TextView mEmptyText;
	private int pageNo = 1, pageSize = 20;
	private String startDate,endDate;
	private String dayCount="30";

	public static QueryIncomeFragment newInstance() {
		QueryIncomeFragment fragment = new QueryIncomeFragment();
		return fragment;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		// inflater.inflate(R.menu.tradequery, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		initDate();
		mListAll=new EstimatesIncomeList();
	}

	/**
	 * 初始化提交日期
	 */
	public void initDate() {
		SimpleDateFormat sDate = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		startDate = STARTDATE;
		endDate = sDate.format(date);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_filter:

			break;
		case android.R.id.home:
			getActivity().finish();
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.aty_tradequery, null);
		mListView = (ListView) view.findViewById(R.id.list);
		mEmptyText = (TextView) view.findViewById(R.id.emptytext);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onActivityCreated(arg0);

		mLoadingFooter = new LoadingFooter(getActivity());
		mLoadType = typeLoadIN;
		mListAll =new EstimatesIncomeList();
		mListView.addFooterView(mLoadingFooter.getView());
		mAdapter = new TradeAdapter();
		mListView.setAdapter(mAdapter);
		loadData();

//		mListView.setOnScrollListener(new OnScrollListener() {
//
//			@Override
//			public void onScrollStateChanged(AbsListView view, int scrollState) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//				// TODO Auto-generated method stub
//				State s = mLoadingFooter.getState();
//				if (s == LoadingFooter.State.Loading || s == LoadingFooter.State.TheEnd || s == LoadingFooter.State.Nodata|| s == LoadingFooter.State.Error) {
//					return;
//				}
//				if (firstVisibleItem + visibleItemCount >= totalItemCount && totalItemCount != 0
//						&& totalItemCount != mListView.getHeaderViewsCount() + mListView.getFooterViewsCount() && mAdapter.getCount() > 0) {
//					mLoadingFooter.setState(State.Loading);
//					pageNo++;
//					loadData();
//				}
//			}
//		});
	}

	private void loadData() {
		// canceTask();
		String cusNo = getSf().getString(Cons.SFUserCusNo, null);// 6227001215800062891
		mTask = new TradeQueryTask();
		//dayCount
		mTask.execute(cusNo, QueryDayNum);
	}

	public void canceTask() {
		if (mTask != null) {
			mTask.cancel(true);
		}
	}

	public class TradeQueryTask extends MyAsyncTask<String, Void, EstimatesIncomeList> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected EstimatesIncomeList doInBackground(String... params) {
			// String protocalNo,String fundCode
			return DispatchAccessData.getInstance().tradeDayEstimeatesIncome(params[0], params[1],null,CacheHelp.cachetime_60_mint);//null为custBankId
			// return DispatchAccessData.getInstance().tradeIncome(params{},
			// pageSize, pageNo);
		}

		@Override
		protected void onPostExecute(EstimatesIncomeList result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			System.out.println("onPostExecute");
			ArrayList<EstimatesIncomeItem> tempList = result.getmList();
			if (result.getContentCode() == Cons.SHOW_ERROR) {
				mLoadingFooter.setState(State.Error);
				mLoadingFooter.setMessage(result.getContentMsg());
				return;
			} else if (result.getContentCode() == Cons.SHOW_FORCELOGIN && getActivity() != null) {
				if (QueryFragment.income == 1) {
					MyAlertDialog.newInstance(null).show(getFragmentManager(), "");
					return;
				}
			} else if (result.getContentCode() == Cons.SHOW_SUCCESS) {
				if (tempList == null || (mListAll.getmList().size() == 0 && tempList.size() == 0)) {
					mLoadingFooter.setState(State.Nodata);
//					mLoadingFooter.setMessage(getClickableSpan("还没收益，赶快去"));
					mLoadingFooter.setMessage("近期暂无数据");
					return;
				}
				
				if (tempList != null && tempList.size() > 0) {
					mListAll.getmList().clear();
					mListAll.getmList().addAll(tempList);
					mLoadingFooter.setState(State.init);
				}

				if (tempList.size() != pageSize) {
					mLoadingFooter.setState(State.TheEnd);
				}

				if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
				}
			}
		}

		public boolean isNoData(ArrayList<IncomeItemDto> result) {
			if (result == null) {
				return true;
			}
			if (result.size() == 0) {
				return true;
			}
			return false;
		}

		public boolean isPageEnd(ArrayList<IncomeItemDto> result) {
			if (isNoData(result)) {
				return true;
			}
			if (result.size() != Page) {
				return true;
			}
			return false;
		}
	}

	public class TradeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mListAll.getmList().size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mListAll.getmList().get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			VieHoldw hView;
			if (convertView == null) {
				hView = new VieHoldw();
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_income, null);
				hView.date = (TextView) convertView.findViewById(R.id.date);
				hView.amounts = (TextView) convertView.findViewById(R.id.amount);
				convertView.setTag(hView);
			} else {
				hView = (VieHoldw) convertView.getTag();
			}

			EstimatesIncomeItem tDto = mListAll.getmList().get(position);

			String dateFm = "--";
			try {
				dateFm = TimestampUtil.getChangeHeng(tDto.getDate(), TimestampUtil.FORMATUPLOAD, TimestampUtil.FORMATNoS);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			hView.amounts.setText( StringUtil.formatCurrency(tDto.getIncome()));
			hView.date.setText(dateFm);
			return convertView;
		}
	}

	public static final class VieHoldw {
		TextView date;
		TextView amounts;
	}

	/**
	 * 超链接点击事件
	 * 
	 * @author Administrator
	 * 
	 */
	class Clickable extends ClickableSpan implements OnClickListener {
		private final View.OnClickListener mListener;

		public Clickable(View.OnClickListener l) {
			mListener = l;
		}

		@Override
		public void onClick(View v) {
			mListener.onClick(v);
		}
	}

	private SpannableString getClickableSpan(String spinnerHeader) {
		View.OnClickListener l = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Toast.makeText(ClickableText.this, "Click Success",
				// Toast.LENGTH_SHORT).show();
				Intent i = new Intent(ApplicationParams.getInstance().getApplicationContext(), SaveMoneyActivity.class);
				startActivity(i);
				MobclickAgent.onEvent(getActivity(), Cons.EVENT_UI_TradeHistory, "存钱");
			}
		};
		SpannableString spanableInfo = new SpannableString(spinnerHeader + "存钱吧！");
		int start = spinnerHeader.length();
		int end = spinnerHeader.length() + 2;
		spanableInfo.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spanableInfo.setSpan(new Clickable(l), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spanableInfo;
	}

}