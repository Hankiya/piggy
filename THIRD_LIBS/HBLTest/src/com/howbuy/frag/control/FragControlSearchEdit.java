package com.howbuy.frag.control;

import java.util.ArrayList;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.howbuy.adp.SearchAdp;
import com.howbuy.component.DbUtils;
import com.howbuy.control.EditBar;
import com.howbuy.control.EditBar.IEditListener;
import com.howbuy.control.SearchBar;
import com.howbuy.control.SearchBar.ISearchListener;
import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.frag.AbsFrag;
import com.howbuy.lib.interfaces.ICursorCalbak;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.libtest.R;

public class FragControlSearchEdit extends AbsFrag implements ISearchListener , IEditListener {
	public SearchBar mSearchBar = null;
	public SearchAdp mSearchAdp = null;
	public EditBar mEditBar = null;
	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_control_searchedit;
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		 
		mSearchBar = (SearchBar) root.findViewById(R.id.lay_search);
		mEditBar = (EditBar) root.findViewById(R.id.lay_edit);
		mSearchBar.setSearchBarListener(this);
		mEditBar.setEditBarListener(this);
		mSearchAdp = new SearchAdp(getSherlockActivity(), null);
		mSearchBar.setAdapter(mSearchAdp);
		mSearchBar.setSearchLayout(false);
	}
 

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals("mode")) {
			mSearchBar.setSearchMode(!mSearchBar.isSearchMode(), true);
			return true;
		} else if (item.getTitle().equals("enable")) {
			mSearchBar.setSearchEnable(!mSearchBar.isSearchEnable(), false);
			return true;
		} else if (item.getTitle().equals("test")) {
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    //menu.clear();
		SubMenu sub=menu.addSubMenu("searchmode");
		sub.add("mode");
		sub.add("enable");
		sub.add("test");
		sub.getItem().setShowAsAction(
				MenuItem.SHOW_AS_ACTION_WITH_TEXT
				| MenuItem.SHOW_AS_ACTION_ALWAYS);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	public void queryAllSearchHistory() {
		DbUtils.query("", "select * from tb_common", new ICursorCalbak() {
			@Override
			public void getCursor(String key, Cursor c, WrapException e) {
				if (e == null) {
					if (c != null) {
						ArrayList<String> r = new ArrayList<String>();
						while (c.moveToNext()) {
							byte[] byts = c.getBlob(2);
							r.add(new String(byts == null ? "null".getBytes()
									: byts));
						}
						mSearchAdp.setItems(r, true);
					}
				} else {
					pop("queryAllSearchHistory search key empty  querry all error: "
							+ e,true);
				}
			}
		});
	}

	public String getEditMode(int editState) {
		String mode = "unknown";
		switch (editState) {
		case IEditListener.EDIT_ACCESS: {
			mode = "EDIT_ACCESS";
		}
			break;
		case IEditListener.EDIT_DISABLE: {
			mode = "EDIT_DISABLE";
		}
			break;
		case IEditListener.EDIT_EMPTY: {
			mode = "EDIT_EMPTY";
		} break;
		}
		return mode;
	}

	public String getSearchMode(int searchState) {
		String mode = "unknown";
		switch (searchState) {
		case ISearchListener.SEARCH_EMPTY: {
			mode = "SEARCH_EMPTY";
		}
			break;
		case ISearchListener.SEARCH_FOCUSED: {
			mode = "SEARCH_FOCUSED";
		}
			break;
		case ISearchListener.SEARCH_UNFOCUSED: {
			mode = "SEARCH_UNFOCUSED";
		}
			break;
		case ISearchListener.SEARCH_DISABLE: {
			mode = "SEARCH_DISABLE";
		}
			break;
		}
		return mode;
	}

	@Override
	public boolean onSearchKeyClear() {
		d("search", "onSearchKeyClear");
		WrapException result = DbUtils
				.exeSql("delete from tb_common where key='search_history'");
		if (result == null) {
			return true;
		} else {
			pop("onSearchKeyClear  delete error: " + result,true);
			return false;
		}
	}

	@Override
	public void onSearchKeyChanged(String curTag, String preTag, int curLen) {
		d("search", "onSearchKeyChanged curTag=" + curTag + " preTag=" + preTag
				+ " curLen=" + curLen);
		StringBuilder sb = new StringBuilder();
		sb.append("select *  from ").append("tb_common");
		sb.append(" where subkey like '").append(curTag).append("%'");
		sb.append(" and key='").append("search_history").append("'");
		DbUtils.query("", sb.toString(), new ICursorCalbak() {
			@Override
			public void getCursor(String key, Cursor c, WrapException e) {
				if (e == null) {
					if (c != null) {
						ArrayList<String> r = new ArrayList<String>();
						while (c.moveToNext()) {
							byte[] byts = c.getBlob(2);
							r.add(new String(byts == null ? "null".getBytes()
									: byts));
						}
						mSearchAdp.setItems(r, true);
					}
				} else {
					pop("onSearchKeyChanged  querry all error: " + e,true);
				}
			}
		});

	}

	@Override
	public boolean onSearchSubmit(String tag, int searchState) {
		String searchMode = getSearchMode(searchState);
		d("search", "onSearchSubmit searchMode=" + searchMode + " tag=" + tag
				+ " searchState=" + searchState);

		if (searchState == ISearchListener.SEARCH_FOCUSED) {
			if (StrUtils.isEmpty(tag)) {
				pop("onSearchSubmit  tag is empty ,searchMode="
						+ searchMode,true);
				return false;
			} else {
				DbUtils.SqlExeObj obj = new DbUtils.SqlExeObj(
						"insert into tb_common values(?,?,?,?,?)");
				obj.mObjs = new Object[] { "search_history", tag,
						tag.getBytes(), 1, System.currentTimeMillis() };
				WrapException result = DbUtils.exeSql(obj);
				if (result != null && result.getMsgCode() != 22) {
					pop("onSearchSubmit  add to db error : " + result
							+ " searchMode=" + searchMode,true);
				}
				return true;
			}
		} else {
			return false;
		}

	}

	@Override
	public void onSearchStateChanged(int searchState) {
		String searchMode = getSearchMode(searchState);
		d("search", "onSearchStateChanged searchMode=" + searchMode
				+ " searchState=" + searchState);

		if (searchState == ISearchListener.SEARCH_EMPTY) {
			queryAllSearchHistory();
		} else {
			pop("onSearchStateChanged  searchState=" + searchState,true);
		}

	}

//////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onEditTextChanged(String curTag, String preTag, int curLen) {
		d("edit", "onEditTextChanged curTag=" + curTag + " preTag=" + preTag
				+ " curLen=" + curLen);
	}

	@Override
	public boolean onEditSubmit(String tag,int editState) {
		String mode=getEditMode(editState);
		d("edit", "onEditSubmit tag=" + tag + " mode=" + mode );
		if(editState==IEditListener.EDIT_ACCESS){
			if (StrUtils.isEmpty(tag)) {
				pop("onEditSubmit tag=" + tag,true);
			} else {
				pop("onEditSubmit tag=" + tag,true);
				return true;
			}
		}
        return false;
	}
}	 
 