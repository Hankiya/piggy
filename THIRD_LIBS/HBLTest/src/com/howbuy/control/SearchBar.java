package com.howbuy.control;

import android.content.Context;
import android.database.DataSetObserver;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.howbuy.lib.utils.StrUtils;
import com.howbuy.lib.utils.ViewUtils;
import com.howbuy.libtest.R;

public class SearchBar extends LinearLayout implements View.OnClickListener,
		EditText.OnEditorActionListener, OnFocusChangeListener, TextWatcher,
		OnItemClickListener {
	private ListView mAdpter = null;
	private TextView mTvSearch = null;
	private EditText mEtSearch;
	private View mLaySearch, mLayEdit, mLayAdapter, mIvClear, mTvHistory,
			mIvSearchIcon, mPbProgress;
	private Button mBtSearch;
	private boolean isSearchMode = true, isAutoHistorySearch = false,
			mWrapContent = true, mEnableCancle = true;

	private int mSearchState = -1;
	private String mSearchKey = "";
	private String mSearchHintNormal = "hit normal",
			mSearchHintFocused = "hit focused";
	private String mSubmitTextEmpty = "empty", mSubmitTextAccess = "access",
			mSubmitTextDisable = "disable";

	private DataSetObserver mObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			adjustAdapterView(false);
		}

		@Override
		public void onInvalidated() {
			adjustAdapterView(false);
		}
	};

 
	public void setEnableCancle(boolean enableCancle) {
		if(mEnableCancle!=enableCancle){
			mEnableCancle = enableCancle;
			if (mBtSearch != null) {
				if (mSearchState == ISearchListener.SEARCH_DISABLE) {
					mBtSearch.setEnabled(enableCancle);
				}
			}
		}
	}

	private ISearchListener mListener = null;


	public void setSubmitText(String whenEmpty, String whenAccess,
			String whenDisable) {
		this.mSubmitTextEmpty = whenEmpty;
		this.mSubmitTextAccess = whenAccess;
		this.mSubmitTextDisable = whenDisable;
		if (mBtSearch != null) {
			if (mSearchState == ISearchListener.SEARCH_DISABLE) {
				mBtSearch.setText(mSubmitTextDisable);
			} else if (mSearchState == ISearchListener.SEARCH_EMPTY) {
				mBtSearch.setText(mSubmitTextEmpty);
			} else {
				mBtSearch.setText(mSubmitTextAccess);
			}
		}
	}

	public void setSearchHintText(String hitNormal, String hitFocuse) {
		mSearchHintNormal = hitNormal;
		mSearchHintFocused = hitFocuse;
		if (mTvSearch != null) {
			mTvSearch.setText(mSearchHintNormal);
		}
		if (mEtSearch != null) {
			mEtSearch.setHint(mSearchHintFocused);
		}
	}

	public Button getSearchButton() {
		return mBtSearch;
	}

	public ListView getSearchList() {
		return mAdpter;
	}

	public EditText getSearchEdit() {
		return mEtSearch;
	}

	public TextView getSearchTextView() {
		return mTvSearch;
	}

	public void setSearchBarListener(ISearchListener l) {
		mListener = l;
	}

	public SearchBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public boolean isSearchMode() {
		return isSearchMode;
	}

	public boolean isSearchEnable() {
		return isSearchMode && mEtSearch.isEnabled();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		stepViewAndAction();
	}

	private void stepViewAndAction() {
		mLaySearch = this.findViewById(R.id.search_bar_lay_search);
		mLayEdit = mLaySearch.findViewById(R.id.search_bar_lay_edit);
		mLayAdapter = this.findViewById(R.id.search_bar_lay_adapter);
		mEtSearch = (EditText) mLayEdit.findViewById(R.id.search_bar_et_edit);
		mIvClear = mLayEdit.findViewById(R.id.search_bar_iv_clear);
		mIvSearchIcon = mLayEdit.findViewById(R.id.search_bar_iv_searchicon);
		mPbProgress = mLayEdit.findViewById(R.id.search_bar_pb_progress);
		mTvHistory = mLayAdapter.findViewById(R.id.search_bar_tv_history);
		mAdpter = (ListView) mLayAdapter
				.findViewById(R.id.search_bar_lv_adapter);
		mBtSearch = (Button) mLaySearch.findViewById(R.id.search_bar_bt_search);
		mTvSearch = (TextView) mLaySearch
				.findViewById(R.id.search_bar_tv_search);
		mIvClear.setOnClickListener(this);
		mBtSearch.setOnClickListener(this);
		mTvSearch.setOnClickListener(this);
		mTvHistory.setOnClickListener(this);
		mEtSearch.setOnEditorActionListener(this);
		mEtSearch.setOnFocusChangeListener(this);
		mEtSearch.addTextChangedListener(this);
		mAdpter.setOnItemClickListener(this);
		mWrapContent = getLayoutParams().height == -2;
		mTvSearch.setText(mSearchHintNormal);
		mEtSearch.setHint(mSearchHintFocused);
		boolean searchMode = !(isSearchMode = true);
		setSearchMode(searchMode, searchMode);
		if(searchMode){
		    mSearchState=ISearchListener.SEARCH_FOCUSED;
		    if(StrUtils.isEmpty(mEtSearch.getText().toString())){
		    	mBtSearch.setText(mSubmitTextEmpty);
		    }
		}else{
			mSearchState=ISearchListener.SEARCH_UNFOCUSED;
			mBtSearch.setText(mSubmitTextAccess);
		}
	}

	public void setSearchLayout(boolean wrapContent) {
		ViewGroup.LayoutParams lp = getLayoutParams();
		if (wrapContent) {
			lp.height = -2;
		} else {
			if (isSearchMode) {
				lp.height = -1;
			}
		}
		if (getHandler() != null) {
			requestLayout();
		}
		mWrapContent = wrapContent;
	}

	public void setSearchKey(String tag, boolean notify) {
		String oldTxt = mSearchKey;
		mSearchKey = tag == null ? "" : tag.trim();
		mEtSearch.setText(mSearchKey);
		if (notify && mListener != null) {
			if (StrUtils.isEmpty(mSearchKey)) {
				setSearchState(ISearchListener.SEARCH_EMPTY, true);
			} else {

				setSearchState(
						mEtSearch.hasFocus() ? ISearchListener.SEARCH_FOCUSED
								: (mEtSearch.getVisibility() == View.VISIBLE ? ISearchListener.SEARCH_DISABLE
										: ISearchListener.SEARCH_UNFOCUSED),
						true);
				mListener.onSearchKeyChanged(mSearchKey, oldTxt,
						mSearchKey.length());
			}

		}
	}

	private void setSearchState(int state, boolean notify) {
		int oldState = mSearchState;
		mSearchState = state;
		if (notify && oldState != state && mListener != null) {
			mListener.onSearchStateChanged(mSearchState);
		}
	}

	public String getSearchKey() {
		if (mEtSearch != null) {
			Editable able = mEtSearch.getText();
			if (able != null) {
				mSearchKey = able.toString();
			} else {
				mSearchKey = "";
			}
		}
		return mSearchKey;
	}

	public void setAdapter(BaseAdapter adapter) {
		if (mAdpter.getAdapter() != null) {
			mAdpter.getAdapter().unregisterDataSetObserver(mObserver);
		}
		if (adapter != null) {
			adapter.registerDataSetObserver(mObserver);
		}
		mAdpter.setAdapter(adapter);
		adjustAdapterView(!isSearchMode);
	}

	private void adjustAdapterView(boolean forceHide) {
		if (forceHide || mAdpter.getAdapter() == null
				|| mAdpter.getCount() == 0) {
			mLayAdapter.setVisibility(View.GONE);
		} else {
			mLayAdapter.setVisibility(View.VISIBLE);
		}
	}

	public void setSearchEnable(boolean enabled, boolean invokeKeybod) {
		if (isSearchMode) {
			mEtSearch.setEnabled(enabled);
			mIvClear.setEnabled(enabled);
			mBtSearch.setEnabled(enabled);
			if (mEnableCancle) {
				mBtSearch.setEnabled(true);
			}
			if (enabled) {
				if (StrUtils.isEmpty(mEtSearch.getText().toString())) {
					mBtSearch.setText(mSubmitTextEmpty);
				} else {
					mBtSearch.setText(mSubmitTextAccess);
				}
				mPbProgress.setVisibility(View.INVISIBLE);
				mIvSearchIcon.setVisibility(View.VISIBLE);
				if (invokeKeybod) {
					ViewUtils.showKeybord(mEtSearch, enabled);
				}
				setSearchState(
						mEtSearch.hasFocus() ? ISearchListener.SEARCH_FOCUSED
								: ISearchListener.SEARCH_UNFOCUSED, true);
			} else {
				adjustAdapterView(true);
				mBtSearch.setText(mSubmitTextDisable);
				mPbProgress.setVisibility(View.VISIBLE);
				mIvSearchIcon.setVisibility(View.INVISIBLE);
				setSearchState(ISearchListener.SEARCH_DISABLE, true);
			}
		}
	}
/**
 *  android:windowSoftInputMode="stateHidden"
 * @param searchMode
 * @param forceKeybord
 */
	public void setSearchMode(boolean searchMode, boolean forceKeybord) {
		if (isSearchMode != searchMode) {
			isSearchMode = searchMode;
			setSearchKey("", isSearchMode);
			if (isSearchMode) {
				setSearchModeOpen(forceKeybord);
			} else {
				if(!forceKeybord){
					ViewUtils.showKeybord(mEtSearch, false);
				}
				setSearchModeClose(forceKeybord);
			}
			if(mEtSearch.hasFocus()){
				mBtSearch.setText(mSubmitTextEmpty);
			}else{
				mBtSearch.setText(mSubmitTextAccess);
			}
		} else {
			if (isSearchMode && !mEtSearch.isEnabled()) {
				setSearchEnable(true, forceKeybord);
			}
			if (!isSearchMode && mEtSearch.isEnabled()) {
				setSearchEnable(false, forceKeybord);
			}
		}
	}

	private void setSearchModeOpen(boolean forceKeybord) {
		if (!mWrapContent) {
			setSearchLayout(false);
		}
		mTvSearch.setVisibility(View.GONE);
		mLayEdit.setVisibility(View.VISIBLE);
		mEtSearch.setText(mSearchKey);
		mEtSearch.requestFocus();
		if (!isSearchEnable()) {
			setSearchEnable(true, forceKeybord);
		}
	}

	private void setSearchModeClose(boolean forceKeybord) {
		if (!mWrapContent) {
			setSearchLayout(true);
			mWrapContent = false;
		}
		if (forceKeybord) {
			ViewUtils.showKeybord(mEtSearch, false);
		}
		mLayEdit.setVisibility(View.GONE);
		mTvSearch.setVisibility(View.VISIBLE);
		adjustAdapterView(true);
	}

	public void setSelectText(int start, int end) {
		int len = mSearchKey.length();
		if (end > len)
			end = len;
		if (start < 0)
			start = 0;
		if (end < 0)
			end = len;
		mEtSearch.setSelection(start, end);
	}

	@Override
	public void onClick(View v) {
		if (v.equals(mTvSearch)) {
			setSearchMode(true, true);
		} else if (v.equals(mIvClear)) {
			mBtSearch.setText(mSubmitTextEmpty);
			setSearchKey("", true);
		} else if (v.equals(mBtSearch)) {
			getSearchKey();
			if (mSearchState == ISearchListener.SEARCH_DISABLE) {
				if (mListener == null
						|| !mListener.onSearchSubmit(mSearchKey, mSearchState  )) {
					setSearchEnable(true, false);
				}
			} else {
				submit();
			}

		} else if (v.equals(mTvHistory)) {
			if (mListener != null) {
				if (mListener.onSearchKeyClear()) {
					if (mLayAdapter.getVisibility() == View.VISIBLE) {
						adjustAdapterView(true);
					}
				}
			}
		}
	}

	private void submit() {
		boolean result = mListener == null ? false : mListener.onSearchSubmit(
				mSearchKey, mSearchState);
		if (StrUtils.isEmpty(mSearchKey)) {
			if (mListener == null || !result) {
				setSearchMode(false, false);
			}
		} else {
			if (result) {
				if (mSearchState == ISearchListener.SEARCH_FOCUSED) {
					setSearchEnable(false, false);
				}
			} else {
			}
		}
	}

	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE || actionId == 0) {
			if (mListener != null) {
				getSearchKey();
				if (!StrUtils.isEmpty(mSearchKey)) {
					submit();
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public void showKeybord(boolean show){
		ViewUtils.showKeybord(mEtSearch, show);
	}
	
	public void onFocusChange(View v, boolean hasFocus) {
		if (v.equals(mEtSearch)) {
			if (!hasFocus) {
				ViewUtils.showKeybord(mEtSearch, false);
			} else {
				mBtSearch.setText(mSubmitTextAccess);
			}
			int oldState = mSearchState;
			setSearchState(hasFocus ? ISearchListener.SEARCH_FOCUSED
					: ISearchListener.SEARCH_UNFOCUSED, true);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		String cur = s.toString();
		if (!mSearchKey.equals(cur)) {
			int len = s.length();
			if (len == 0) {
				mBtSearch.setText(mSubmitTextEmpty);
				setSearchState(ISearchListener.SEARCH_EMPTY, true);
			} else {
				int newState = mEtSearch.hasFocus() ? ISearchListener.SEARCH_FOCUSED
						: (mEtSearch.getVisibility() == View.VISIBLE ? ISearchListener.SEARCH_DISABLE
								: ISearchListener.SEARCH_UNFOCUSED);
				setSearchState(
						newState,
						!(mSearchState == ISearchListener.SEARCH_EMPTY && newState == ISearchListener.SEARCH_FOCUSED));
				mBtSearch .setText(mSearchState == ISearchListener.SEARCH_FOCUSED ? mSubmitTextAccess
								: mSubmitTextDisable);
				if (mListener != null) {
					mListener.onSearchKeyChanged(cur, mSearchKey, len);
				}
			}
		}
		mSearchKey = cur;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void onItemClick(AdapterView<?> p, View v, int i, long l) {
		String preTxt = mSearchKey;
		boolean preEmpty = StrUtils.isEmpty(preTxt);
		setSearchKey(p.getItemAtPosition(i).toString(), !isAutoHistorySearch
				&& !preEmpty);
		if (preEmpty && mListener != null) {
			mSearchState = ISearchListener.SEARCH_FOCUSED;
			mListener.onSearchKeyChanged(mSearchKey, preTxt,
					mSearchKey.length());
		}
		mEtSearch.setSelection(mSearchKey.length());
		mBtSearch.setText(mSubmitTextAccess);
		if (isAutoHistorySearch) {
			mSearchKey = mEtSearch.getText().toString().trim();
			submit();
			ViewUtils.showKeybord(mEtSearch, false);
		}
	}

	public interface ISearchListener {
		public static final int SEARCH_UNFOCUSED = 0;
		public static final int SEARCH_FOCUSED = 1;
		public static final int SEARCH_EMPTY = 2;
		public static final int SEARCH_DISABLE = 3;

		/**
		 * @return boolean true for clean history from local .
		 */
		public boolean onSearchKeyClear();

		public void onSearchKeyChanged(String curTag, String preTag, int curLen);

		/**
		 * @param tag
		 *            search key maybe empty.
		 * @return boolean true for query accepted.
		 */
		public boolean onSearchSubmit(String tag, int searchState);

		public void onSearchStateChanged(int searchState);
	}
}
