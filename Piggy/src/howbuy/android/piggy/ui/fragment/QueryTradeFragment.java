package howbuy.android.piggy.ui.fragment;

import howbuy.android.lib.MyAsyncTask;
import howbuy.android.piggy.MyAlertDialog;
import howbuy.android.piggy.R;
import howbuy.android.piggy.api.DispatchAccessData;
import howbuy.android.piggy.api.dto.TradeQueryDto;
import howbuy.android.piggy.api.dto.TradeQueryItemDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.ui.SaveMoneyActivity;
import howbuy.android.piggy.ui.base.AbstractFragment;
import howbuy.android.piggy.widget.LoadingFooter;
import howbuy.android.piggy.widget.LoadingFooter.State;
import howbuy.android.util.AndroidUtil;
import howbuy.android.util.CodeDes.Parser;
import howbuy.android.util.CodeDes.PiggyTradeState;
import howbuy.android.util.Cons;
import howbuy.android.util.StringUtil;
import howbuy.android.util.TimestampUtil;
import howbuy.android.util.http.UrlMatchUtil;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tjerkw.slideexpandable.library.ActionSlideExpandableListView;
import com.umeng.analytics.MobclickAgent;

public class QueryTradeFragment extends AbstractFragment {
	private ImageLoader mImageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options= new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.abs__ic_clear).showImageForEmptyUri(R.drawable.abs__ic_clear).showImageOnFail(R.drawable.abs__ic_clear).cacheInMemory(true)
			.cacheOnDisc(true).considerExifParams(false).build();;
	public static final String STARTDATE = "20130125";
	public static final int typeLoadOut = 1;
	public static final int typeLoadIN = 2;
	public static final String typeTradeSG = "1";
	public static final String typeTradeSH = "2";
	public static final String typeTradeFH = "3";
	public static final int Page = 20;
	private int mLoadType = 1;// 1为外部 2为内部
	private ActionSlideExpandableListView mListView;
	private TradeQueryTask mTask;
	private LoadingFooter mLoadingFooter;
	private TradeAdapter mAdapter;
	private ArrayList<TradeQueryItemDto> mListAll, mListSave, mListOut, mListZhuan, mListForView;
	private TextView mEmptyText;
	private int pageNo = 1, pageSize = Page;
	private int pageNum=0;
	

	public static QueryTradeFragment newInstance() {
		QueryTradeFragment fragment = new QueryTradeFragment();
		return fragment;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		// inflater.inflate(R.menu.tradequery, menu);
		// super.onCreateOptionsMenu(menu, inflater);
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
		View view = inflater.inflate(R.layout.aty_tradequeryhis, null);
		mListView = (ActionSlideExpandableListView) view.findViewById(R.id.list);
		mEmptyText = (TextView) view.findViewById(R.id.emptytext);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onActivityCreated(arg0);
		setHasOptionsMenu(true);

		mLoadingFooter = new LoadingFooter(getActivity());
		mLoadType = typeLoadIN;
		mListForView = new ArrayList<TradeQueryItemDto>();
		mListAll = new ArrayList<TradeQueryItemDto>();
		mListView.addFooterView(mLoadingFooter.getView());
		mAdapter = new TradeAdapter();
		mListView.setAdapter(mAdapter);
		mListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				State s = mLoadingFooter.getState();
				if (s == LoadingFooter.State.Loading || s == LoadingFooter.State.TheEnd || s == LoadingFooter.State.Nodata|| s == LoadingFooter.State.Error) {
					return;
				}
				if (firstVisibleItem + visibleItemCount >= totalItemCount && totalItemCount != 0
						&& totalItemCount != mListView.getHeaderViewsCount() + mListView.getFooterViewsCount() && mAdapter.getCount() > 0) {
					mLoadingFooter.setState(State.Loading);
					pageNo++;
					loadData();
				}
			}
		});
		loadData();
	}

	private void loadData() {
		// canceTask();
		String cusNo = getSf().getString(Cons.SFUserCusNo, null);// 6227001215800062891
		mTask = new TradeQueryTask();
		mTask.execute(cusNo, String.valueOf(pageNo), String.valueOf(pageSize));
	}

	public void canceTask() {
		if (mTask != null) {
			mTask.cancel(true);
		}
	}

	public class TradeQueryTask extends MyAsyncTask<String, Void, TradeQueryDto> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected TradeQueryDto doInBackground(String... params) {
			return DispatchAccessData.getInstance().tradeHistory(params[0], params[1], params[2],null);//null custBankId
		}

		@Override
		protected void onPostExecute(TradeQueryDto result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result.getContentCode() == Cons.SHOW_ERROR) {
				mLoadingFooter.setState(State.Error);
				mLoadingFooter.setMessage(result.getContentMsg());
				return;
			}  else if (result.getContentCode() == Cons.SHOW_FORCELOGIN && getActivity() != null) {
				if (QueryFragment.income == 0) {
					MyAlertDialog.newInstance(null).show(getFragmentManager(), "");
					return;
				}
			} else if (result.getContentCode() == Cons.SHOW_SUCCESS) {
				ArrayList<TradeQueryItemDto> tempList = result.getList();
				pageNum=result.getCountItem();
				if (tempList == null || (mListAll.size() == 0 && tempList.size() == 0)) {
					mLoadingFooter.setState(State.Nodata);
					mLoadingFooter.setMessage(getClickableSpan("还没存钱历史，赶快去"));
					return;
				}
				
				if (tempList != null && tempList.size() > 0) {
					mListForView.addAll(tempList);
					mLoadingFooter.setState(State.init);
				}

				if (isPageEnd(tempList)) {
					mLoadingFooter.setState(State.TheEnd);
				}

				if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
				}
			}
		}
		
		public boolean isNoData(ArrayList<TradeQueryItemDto> result) {
			if (result == null) {
				return true;
			}
			if (result.size() == 0) {
				return true;
			}
			return false;
		}

		public boolean isPageEnd(ArrayList<TradeQueryItemDto> result) {
		/**
		 * 这个判断有问题
		 * @param result
		 * @return
		 */
			if (isNoData(result)) {
				return true;
			}

			if (pageNum==0) {
				if (result.size() != Page) {
					return true;
				}
			}else {
				if (mListForView.size()==pageNum) {
					return true;
				}
			}
			return false;
		}
	}

	public class TradeAdapter extends BaseAdapter {
		public static final String TypeOrder = "下单";
		public static final String TypePay = "付款";
		public static final String TypeComp = "完成";
//		1.下单
//
//		2.付款
//		确认中
//		等待付款
//		付款中
//		已撤单
//
//		3.完成
//		部分成功
//		交易成功
//		交易失败
		
		public static final String Status1 = "1";
		public static final String Status2 = "623";
		public static final String Status3 = "571";
		
		public String getStatus3(String status) {
			if (Status3.contains(status)) {
				return TypeComp;
			}
			return null;
		}
		
		public String getStatus2(String status) {
			if (Status2.contains(status)) {
				return TypePay;
			}
			return null;
		}
		
		public String getStatus1(String status) {
			if (Status1.contains(status)) {
				return TypeOrder;
			}
			return null;
		}
		
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mListForView.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mListForView.get(position);
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
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_trade, null);
				hView.type = (TextView) convertView.findViewById(R.id.saveorout);
				hView.amounts = (TextView) convertView.findViewById(R.id.amount);
				hView.status1 = (TextView) convertView.findViewById(R.id.status);
				hView.status2 = (TextView) convertView.findViewById(R.id.status2);
				hView.optionDate = (TextView) convertView.findViewById(R.id.date1);
				hView.successDate = (TextView) convertView.findViewById(R.id.date2);
				hView.bankIcon = (ImageView) convertView.findViewById(R.id.bank_icon);
				hView.bankName = (TextView) convertView.findViewById(R.id.bank_title);
				hView.bankHint = (TextView) convertView.findViewById(R.id.bank_hint);
				hView.incomeLay = (RelativeLayout) convertView.findViewById(R.id.income_lay);
				convertView.setTag(hView);
			} else {
				hView = (VieHoldw) convertView.getTag();
			}

			TradeQueryItemDto tDto = mListForView.get(position);
			String amount = String.valueOf(tDto.getTradeBal());
			hView.optionDate.setText(formatTradeDate(tDto.getOptDt()));
			
			String type=tDto.getTradeType();
			
			String trueStatus=tDto.getTxStatus();
			String status= Parser.parse(PiggyTradeState.values(), trueStatus).getDescribe();
			String typeStatus = null;
			if (type.equals(typeTradeSG)) {
				hView.type.setBackgroundColor(getResources().getColor(R.color.btn_sure_normal));
				hView.amounts.setText("+"+StringUtil.formatCurrency(amount));
				hView.optionDate.setText(formatOptionDate(tDto.getTradeDt()+tDto.getTradeTm()));
				hView.successDate.setText(formatTradeDate(tDto.getIncomeDate()));
				hView.incomeLay.setVisibility(View.VISIBLE);
				typeStatus="存钱";
				
				
				if (getStatus3(trueStatus)!=null) {
					hView.optionDate.setText("(成功)下单"+"\r\n"+formatOptionDate(tDto.getTradeDt()+tDto.getTradeTm()));
					hView.status2.setText("(成功)付款");
					
					if (PiggyTradeState.TS_SUCCESS_FULL.getCode().equals(trueStatus)) {
						hView.successDate.setText("(成功)完成"+formatTradeDate(tDto.getIncomeDate()));
					}else {
						hView.successDate.setText("(未成功)完成"+formatTradeDate(tDto.getIncomeDate()));
					}
				}else if (getStatus2(trueStatus)!=null) {
					hView.optionDate.setText("(成功)下单"+"\r\n"+formatOptionDate(tDto.getTradeDt()+tDto.getTradeTm()));
					hView.status2.setText("(成功)付款");
					hView.successDate.setText("(未成功)完成"+formatTradeDate(tDto.getIncomeDate()));
				}else if (getStatus1(trueStatus)!=null) {
					hView.optionDate.setText("(成功)下单"+"\r\n"+formatOptionDate(tDto.getTradeDt()+tDto.getTradeTm()));
					hView.status2.setText("(未成功)付款");
					hView.successDate.setText("(未成功)完成"+formatTradeDate(tDto.getIncomeDate()));
				}
				
			}else if (type.equals(typeTradeSH)) {
				hView.type.setBackgroundColor(getResources().getColor(R.color.btn_register_normal));
				hView.amounts.setText("-"+StringUtil.formatCurrency(amount));
				hView.optionDate.setText(formatOptionDate(tDto.getTradeDt()+tDto.getTradeTm()));
				hView.successDate.setText(formatTradeDate(tDto.getToAccountDate()));
				hView.incomeLay.setVisibility(View.VISIBLE);
				typeStatus="取钱";
				
				
				if (getStatus3(trueStatus)!=null) {
					hView.optionDate.setText("(成功)下单"+"\r\n"+formatOptionDate(tDto.getTradeDt()+tDto.getTradeTm()));
					hView.status2.setText("(成功)付款");
					
					if (PiggyTradeState.TS_SUCCESS_FULL.getCode().equals(trueStatus)) {
						hView.successDate.setText("(成功)完成"+formatTradeDate(tDto.getIncomeDate()));
					}else {
						hView.successDate.setText("(未成功)完成"+formatTradeDate(tDto.getIncomeDate()));
					}
				}else if (getStatus2(trueStatus)!=null) {
					hView.optionDate.setText("(成功)下单"+"\r\n"+formatOptionDate(tDto.getTradeDt()+tDto.getTradeTm()));
					hView.status2.setText("(成功)付款");
					hView.successDate.setText("(未成功)完成"+formatTradeDate(tDto.getIncomeDate()));
				}else if (getStatus1(trueStatus)!=null) {
					hView.optionDate.setText("(成功)下单"+"\r\n"+formatOptionDate(tDto.getTradeDt()+tDto.getTradeTm()));
					hView.status2.setText("(未成功)付款");
					hView.successDate.setText("(未成功)完成"+formatTradeDate(tDto.getIncomeDate()));
				}
			}else if (type.equals(typeTradeFH)) {
				hView.type.setBackgroundColor(getResources().getColor( R.color.text_list_fenghong));
				hView.amounts.setText("+"+StringUtil.formatCurrency(amount));
				hView.incomeLay.setVisibility(View.GONE);
				typeStatus="收益";
			}
			hView.bankName.setText(tDto.getBankName());
			hView.bankHint.setText(StringUtil.formatViewBankCard(tDto.getBankAcct()));
			mImageLoader.displayImage(iconUrl(tDto.getBankCode()), hView.bankIcon,options);
			
			if (!TextUtils.isEmpty(status)&&!type.equals(typeTradeFH)) {
				hView.status1.setText(String.valueOf(status));
				hView.status1.setVisibility(View.VISIBLE);
			}else {
				hView.status1.setVisibility(View.GONE);
			}
			hView.type.setText(typeStatus);
			return convertView;
		}

		public String formatTradeDate(String yyyyMMdd) {
			if (TextUtils.isEmpty(yyyyMMdd)) {
				return "--";

			}
			String text = "--";
			try {
				text = TimestampUtil.getChangeHeng(yyyyMMdd, TimestampUtil.FORMATUPLOAD, TimestampUtil.FORMATNoS);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return text;
		}

		public String formatOptionDate(String yyyyMMddHHmmss) {
			if (TextUtils.isEmpty(yyyyMMddHHmmss)) {
				return "--";

			}
			String text = "--";
			try {
				text = TimestampUtil.getChangeHeng(yyyyMMddHHmmss, "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return text;
		}
	}
	
	private String iconUrl(String bankCode){
		String basePath2=UrlMatchUtil.getBasepath2()+AndroidUtil.getSourceFolderName()+"/"+bankCode+".png";
		return basePath2;
	}

	public static final class VieHoldw {
		TextView type;
		TextView amounts;
		TextView status1;
		TextView status2;
		TextView optionDate;
		TextView successDate;
		ImageView bankIcon;
		TextView bankName;
		TextView bankHint;
		RelativeLayout incomeLay;
	}
	
	class Clickable extends ClickableSpan implements OnClickListener{
	    private final View.OnClickListener mListener;

	    public Clickable(View.OnClickListener l){
	      mListener = l;
	    }

	    @Override
	    public void onClick(View v){
	      mListener.onClick(v);
	    }
	}

	private SpannableString getClickableSpan(String spinnerHeader) {
		View.OnClickListener l = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(ClickableText.this, "Click Success", Toast.LENGTH_SHORT).show();
				Intent i=new Intent(ApplicationParams.getInstance().getApplicationContext(),SaveMoneyActivity.class);
				startActivity(i);
				MobclickAgent.onEvent(getActivity(), Cons.EVENT_UI_TradeHistory, "存钱");
			}
		};
		SpannableString spanableInfo = new SpannableString(spinnerHeader+"存钱吧！");
		int start = spinnerHeader.length();
		int end = spinnerHeader.length()+2;
		spanableInfo.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spanableInfo.setSpan(new Clickable(l), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spanableInfo;
	}

}