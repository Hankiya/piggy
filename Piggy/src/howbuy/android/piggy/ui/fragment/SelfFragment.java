package howbuy.android.piggy.ui.fragment;

import howbuy.android.piggy.R;
import howbuy.android.piggy.UserUtil;
import howbuy.android.piggy.ui.RegisterActivity;
import howbuy.android.piggy.ui.base.AbstractFragment;
import howbuy.android.piggy.widget.ImageTextBtn;
import howbuy.android.util.Cons;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.actionbarsherlock.view.MenuItem;

public class SelfFragment extends AbstractFragment implements OnClickListener {
	ImageTextBtn mButton;
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId()==android.R.id.home) {
			getActivity().finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.frag_self, container, false);
		mButton = (ImageTextBtn) v.findViewById(R.id.submit_btn);
		mButton.setOnClickListener(this);
		v.findViewById(R.id.buttom_lay).setVisibility(UserUtil.isLogin() ? View.GONE : View.VISIBLE);
		return v;
		// return null;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getSherlockActivity().getSupportActionBar().setTitle("安全保障");
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.submit_btn:
			Intent ien = new Intent(getActivity(), RegisterActivity.class);
			ien.putExtra(Cons.Intent_type, LoginFragment.LoginType_Main);
			startActivity(ien);
			break;
		default:
			break;
		}
	}
}
