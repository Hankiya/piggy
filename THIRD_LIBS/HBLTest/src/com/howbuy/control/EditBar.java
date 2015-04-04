package com.howbuy.control;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.howbuy.control.SearchBar.ISearchListener;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.lib.utils.ViewUtils;
import com.howbuy.libtest.R;
 

public class EditBar extends LinearLayout implements View.OnClickListener,
		EditText.OnEditorActionListener, OnFocusChangeListener, TextWatcher {
	private EditText mEtSearch;
	private View mLayEdit, mIvClear, mIvSearchIcon, mPbProgress;
	private Button mBtSearch;
	private boolean mEnableCancle = true;
	private int mSearchState = -1;
	private String mSearchKey = "";
	private String mSearchHintNormal = "hit normal",
			mSearchHintFocused = "hit focused";
	private String mSubmitTextEmpty = "empty", mSubmitTextAccess = "access",
			mSubmitTextDisable = "disable";

	public void setEnableCancle(boolean enableCancle) {
		if (mEnableCancle != enableCancle) {
			mEnableCancle = enableCancle;
			if (mBtSearch != null) {
				if (mSearchState == ISearchListener.SEARCH_DISABLE) {
					mBtSearch.setEnabled(enableCancle);
				}
			}
		}
	}

	private IEditListener mListener = null;

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
		if (mEtSearch != null) {
			if (mEtSearch.hasFocus() && mEtSearch.isEnabled()) {
				mEtSearch.setHint(mSearchHintFocused);
			} else {
				mEtSearch.setHint(mSearchHintNormal);
			}
		}
	}

	public Button getSearchButton() {
		return mBtSearch;
	}

	public EditText getSearchEdit() {
		return mEtSearch;
	}

	public void setEditBarListener(IEditListener l) {
		mListener = l;
	}

	public EditBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public boolean isSearchEnable() {
		return mEtSearch.isEnabled();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		stepViewAndAction();
	}

	private void stepViewAndAction() {
		mLayEdit = findViewById(R.id.edit_bar_lay_edit);
		mEtSearch = (EditText) mLayEdit.findViewById(R.id.edit_bar_et_edit);
		mIvClear = mLayEdit.findViewById(R.id.edit_bar_iv_clear);
		mIvSearchIcon = mLayEdit.findViewById(R.id.edit_bar_iv_searchicon);
		mPbProgress = mLayEdit.findViewById(R.id.edit_bar_pb_progress);
		mBtSearch = (Button) findViewById(R.id.edit_bar_bt_search);
		mIvClear.setOnClickListener(this);
		mBtSearch.setOnClickListener(this);
		mEtSearch.setOnEditorActionListener(this);
		mEtSearch.setOnFocusChangeListener(this);
		mEtSearch.addTextChangedListener(this);
		mEtSearch.setHint(mSearchHintFocused);
		mSearchState = ISearchListener.SEARCH_UNFOCUSED;
		mBtSearch.setText(mSubmitTextAccess);
	}

	public void setSearchKey(String tag, boolean notify) {
		String oldTxt = mSearchKey;
		mSearchKey = tag == null ? "" : tag.trim();
		mEtSearch.setText(mSearchKey);
		if (notify && mListener != null) {
			if (StrUtils.isEmpty(mSearchKey)) {
				mSearchState = ISearchListener.SEARCH_EMPTY;
			} else {

				mSearchState = mEtSearch.hasFocus() ? ISearchListener.SEARCH_FOCUSED
						: (mEtSearch.getVisibility() == View.VISIBLE ? ISearchListener.SEARCH_DISABLE
								: ISearchListener.SEARCH_UNFOCUSED);
				mListener.onEditTextChanged(mSearchKey, oldTxt,
						mSearchKey.length());
			}

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

	public void setSearchEnable(boolean enabled, boolean invokeKeybod) {
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
			mSearchState = mEtSearch.hasFocus() ? ISearchListener.SEARCH_FOCUSED
					: ISearchListener.SEARCH_UNFOCUSED;
		} else {
			mBtSearch.setText(mSubmitTextDisable);
			mPbProgress.setVisibility(View.VISIBLE);
			mIvSearchIcon.setVisibility(View.INVISIBLE);
			mSearchState = ISearchListener.SEARCH_DISABLE;
		}
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
		if (v.equals(mIvClear)) {
			mBtSearch.setText(mSubmitTextEmpty);
			setSearchKey("", true);
		} else if (v.equals(mBtSearch)) {
			getSearchKey();
			if (mSearchState == ISearchListener.SEARCH_DISABLE) {
				if (mListener == null
						|| !mListener.onEditSubmit(mSearchKey, mSearchState)) {
					setSearchEnable(true, false);
				}
			} else {
				submit();
			}

		}
	}

	private void submit() {
		boolean result = mListener == null ? false : mListener.onEditSubmit(
				mSearchKey, mSearchState);
		if (StrUtils.isEmpty(mSearchKey)) {
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

	public void showKeybord(boolean show) {
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
			mSearchState = hasFocus ? ISearchListener.SEARCH_FOCUSED
					: ISearchListener.SEARCH_UNFOCUSED;
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		String cur = s.toString();
		if (!mSearchKey.equals(cur)) {
			int len = s.length();
			if (len == 0) {
				mBtSearch.setText(mSubmitTextEmpty);
				mSearchState = ISearchListener.SEARCH_EMPTY;
			} else {
				mSearchState = mEtSearch.hasFocus() ? ISearchListener.SEARCH_FOCUSED
						: (mEtSearch.getVisibility() == View.VISIBLE ? ISearchListener.SEARCH_DISABLE
								: ISearchListener.SEARCH_UNFOCUSED);
				mBtSearch
						.setText(mSearchState == ISearchListener.SEARCH_FOCUSED ? mSubmitTextAccess
								: mSubmitTextDisable);
				if (mListener != null) {
					mListener.onEditTextChanged(cur, mSearchKey, len);
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

	public interface IEditListener {
		public static final int EDIT_ACCESS = 1;
		public static final int EDIT_EMPTY = 2;
		public static final int EDIT_DISABLE = 3;

		public void onEditTextChanged(String curTag, String preTag, int curLen);

		public boolean onEditSubmit(String tag, int editState);
	}
}
