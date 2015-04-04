package howbuy.android.piggy.dialogfragment;

import howbuy.android.lib.MyAsyncTask;
import howbuy.android.piggy.R;
import howbuy.android.piggy.api.DispatchAccessData;
import howbuy.android.piggy.api.dto.SupportBankBranchDto;
import howbuy.android.piggy.api.dto.SupportBankBranchListDto;
import howbuy.android.piggy.widget.LoadingFooter;
import howbuy.android.piggy.widget.LoadingFooter.State;
import howbuy.android.util.Cons;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class DialogBankBanchFragment extends DialogFragment implements OnItemClickListener {
	public static final int typeLoadOut = 1;
	public static final int typeLoadIN = 2;
	public static final int Page = 50;
	private int mLoadType = 1;// 1为外部 2为内部
	private String mBankCode;
	private String mProvinceCode;
	howbuy.android.piggy.widget.ClearableEdittext mKeyword;
	private ProgressBar mPBar;
	private ListView mBankBranchListview;
	private TextView mEmptyText;
	private ProvinceAndBankTask mTask;
	private String mKeyWord;
	private int pageSize = Page, pageNo = 1;
	private LoadingFooter mLoadingFooter;
	private BranchAdapter mAdapter;
	private ArrayList<SupportBankBranchDto> mSupportBankBranchDtos, mSupportBankBranchDtos2;
	private static OnBankBanchLinster mOnBranchLinster;
	private boolean beforeLoaderError;

	public interface OnBankBanchLinster {
		/**
		 * 没有数据
		 */
		public void onNoData(boolean isNoData);

		/**
		 * 返回用户点击的值
		 * 
		 * @param id
		 */
		public void onBankBranchClick(String id, String bankName,String cityCode);
	}

	public static DialogBankBanchFragment newInstance(String bankCode, String provinceCode, OnBankBanchLinster onBranchLinster) {
		mOnBranchLinster = onBranchLinster;
		DialogBankBanchFragment fragment = new DialogBankBanchFragment();
		Bundle args = new Bundle();
		args.putString("mBankCode", bankCode);
		args.putString("mProvinceCode", provinceCode);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		View view = inflater.inflate(R.layout.frag_bankbranch, null);
		mKeyword = (howbuy.android.piggy.widget.ClearableEdittext) view.findViewById(R.id.keyword);
		mPBar = (ProgressBar) view.findViewById(R.id.progress);
		mBankBranchListview = (ListView) view.findViewById(R.id.banklist);
		mEmptyText = (TextView) view.findViewById(R.id.emptytext);
		builder.setView(view);
		builder.setTitle("请选择开户支行");
		mBankBranchListview.setCacheColorHint(getResources().getColor(android.R.color.transparent));
		mBankBranchListview.setOnItemClickListener(this);
		return builder.create();
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onActivityCreated(arg0);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		mBankCode = getArguments().getString("mBankCode");
		mProvinceCode = getArguments().getString("mProvinceCode");
		mKeyword.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
					mKeyWord = s.toString();
					loadSearch();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});

		mKeyword.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					hideSoftInput(v.getWindowToken());
				}
				return true;
			}
		});
		mLoadingFooter = new LoadingFooter(getActivity());
		mLoadType = typeLoadIN;
		mBankBranchListview.addFooterView(mLoadingFooter.getView());
		if (mSupportBankBranchDtos == null) {
			mSupportBankBranchDtos = new ArrayList<SupportBankBranchDto>();
		} else {
			mSupportBankBranchDtos.clear();
		}
		if (mSupportBankBranchDtos2 != null) {// 第一次外部加载完成
			int size = mSupportBankBranchDtos2.size();
			if (size == 0) {
				mLoadingFooter.setState(State.Nodata);
			} else {
				mSupportBankBranchDtos.addAll(mSupportBankBranchDtos2);
			}
		}else {
			loadData();
		}
		if (beforeLoaderError==true) {
			mLoadingFooter.setState(State.Error);
		}
		mAdapter = new BranchAdapter();
		mBankBranchListview.setAdapter(mAdapter);

		mBankBranchListview.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (mLoadingFooter.getState() == LoadingFooter.State.Loading || mLoadingFooter.getState() == LoadingFooter.State.TheEnd
						|| mLoadingFooter.getState() == LoadingFooter.State.Nodata) {
					return;
				}
				if (firstVisibleItem + visibleItemCount >= totalItemCount && totalItemCount != 0
						&& totalItemCount != mBankBranchListview.getHeaderViewsCount() + mBankBranchListview.getFooterViewsCount() && mAdapter.getCount() > 0) {
					loadNextPage();
				}
			}
		});

	}

	/**
	 * 多种隐藏软件盘方法的其中一种
	 * 
	 * @param token
	 */
	private void hideSoftInput(IBinder token) {
		if (token != null) {
			InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	private void loadData() {
		// canceTask();
		mTask = new ProvinceAndBankTask();
		mTask.execute(mBankCode, mProvinceCode, String.valueOf(pageSize), String.valueOf(pageNo), mKeyWord);
	}

	/**
	 * 开始任务
	 * 
	 * @param bankCode
	 * @param provCode
	 * @param pageSize
	 * @param pageNo
	 * @param mKeyWord
	 */
	public void outDoTask(String bankCode, String provCode, int loadType) {
		this.mLoadType = loadType;
		mBankCode = bankCode;
		mProvinceCode = provCode;
		if (mSupportBankBranchDtos2 != null) {
			mSupportBankBranchDtos2 = null;
		}
		if (mSupportBankBranchDtos != null) {
			mSupportBankBranchDtos.clear();
			mAdapter.notifyDataSetChanged();
		}
		loadData();
	}

	private void loadNextPage() {
		mLoadingFooter.setState(State.Loading);
		pageNo++;
		loadData();
	}

	private void loadSearch() {
		canceTask();
		mLoadingFooter.setState(State.Loading);
		pageNo = 1;
		mSupportBankBranchDtos.clear();
		mLoadingFooter.setState(State.init);
		mAdapter.notifyDataSetChanged();
		loadData();
	}

	public void canceTask() {
		if (mTask != null) {
			mTask.cancel(true);
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub
		canceTask();
		super.onCancel(dialog);
	}

	public class ProvinceAndBankTask extends MyAsyncTask<String, Void, SupportBankBranchListDto> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected SupportBankBranchListDto doInBackground(String... params) {
			// try {
			// Thread.sleep(5000);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			return DispatchAccessData.getInstance().getBankBranchList(params[0], params[1], params[2], params[3], params[4]);// bankCode,
																																// provCode,
																																// pageSize,
																																// pageNo,
																																// bankName
		}

		@Override
		protected void onPostExecute(SupportBankBranchListDto result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			System.out.println("onPostExecute");
			if (result.getContentCode() == Cons.SHOW_ERROR) {
				if (mLoadingFooter!=null) {
					mLoadingFooter.setState(State.Error);
					mLoadingFooter.setMessage(result.getContentMsg());
				}
				beforeLoaderError=true;
				return;
			}

			if (mLoadType == typeLoadOut) {
				if (mSupportBankBranchDtos2 == null) {
					mSupportBankBranchDtos2 = new ArrayList<SupportBankBranchDto>();
				}
				ArrayList<SupportBankBranchDto> xdto = result.getSupportBankBranchDtos();
				if (isNoData(result)) {
					mOnBranchLinster.onNoData(true);
				} else {
					mSupportBankBranchDtos2.addAll(xdto);
				}
			}

			if (mLoadType == typeLoadIN) {
				ArrayList<SupportBankBranchDto> xdto = result.getSupportBankBranchDtos();

				if (isPageEnd(result)) {
					mLoadingFooter.setState(State.TheEnd);
				}

				if (isNoData(result)) {
					mLoadingFooter.setState(State.Nodata);
					return;
				}

				if (xdto != null && xdto.size() > 0) {
					mSupportBankBranchDtos.addAll(xdto);
					mLoadingFooter.setState(State.init);
				}

				if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
				}
			}
		}
	}

	public boolean isNoData(SupportBankBranchListDto result) {
		if (result.getSupportBankBranchDtos() == null) {
			return true;
		}
		if (result.getSupportBankBranchDtos().size() == 0) {
			return true;
		}
		return false;
	}

	public boolean isPageEnd(SupportBankBranchListDto result) {
		if (isNoData(result)) {
			return true;
		}
		;

		if (result.getSupportBankBranchDtos().size() != Page) {
			return true;
		}
		return false;
	}

	public class BranchAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mSupportBankBranchDtos.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mSupportBankBranchDtos.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (getActivity()!=null) {
				TextView tView = (TextView) LayoutInflater.from(getActivity()).inflate(android.R.layout.simple_list_item_1, null);
				tView.setText(mSupportBankBranchDtos.get(position).getBankBranchName());
				return tView;
			}
			return null
					;
		}

	}

	@Override
	public String toString() {
		return "SpinnerBankBanchFragment [mLoadType=" + mLoadType + ", mBankCode=" + mBankCode + ", mProvinceCode=" + mProvinceCode + ", mKeyword=" + mKeyword + ", mPBar=" + mPBar
				+ ", mBankBranchListview=" + mBankBranchListview + ", mEmptyText=" + mEmptyText + ", mTask=" + mTask + ", mKeyWord=" + mKeyWord + ", pageSize=" + pageSize
				+ ", pageNo=" + pageNo + ", mLoadingFooter=" + mLoadingFooter + ", mAdapter=" + mAdapter + ", mSupportBankBranchDtos=" + mSupportBankBranchDtos
				+ ", mSupportBankBranchDtos2=" + mSupportBankBranchDtos2 + "]";
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// getDialog().dismiss();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		pageNo = 1;
		mKeyWord = null;
		canceTask();
		beforeLoaderError=false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		// System.out.println(mSupportBankBranchDtos);
		if (position > mSupportBankBranchDtos.size() - 1) {
			Toast.makeText(getActivity(), "未知错误", Toast.LENGTH_SHORT).show();
			return;
		}
		SupportBankBranchDto sdto = mSupportBankBranchDtos.get(position);
		System.out.println(position);
		String bbName = sdto.getBankBranchName();
		String bbCode = sdto.getCnapsNo();
		String bbCityCode= sdto.getCityCode();
		mOnBranchLinster.onBankBranchClick(bbCode, bbName,bbCityCode);
		dismiss();
	}
}