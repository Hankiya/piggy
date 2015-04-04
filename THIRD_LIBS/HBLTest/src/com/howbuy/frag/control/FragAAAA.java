package com.howbuy.frag.control;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.howbuy.lib.frag.AbsFrag;
import com.howbuy.libtest.R;

public class FragAAAA extends AbsFrag {
	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_xxxx;
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {

	}

	@Override
	public boolean onXmlBtClick(View v) {
		return false;
	}

	@Override
	protected void onAttachChanged(Activity aty, boolean isAttach) {
		super.onAttachChanged(aty, isAttach);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean handled = false;
		return handled;
	}
}
