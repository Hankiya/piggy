package howbuy.android.piggy.ui.fragment;

import howbuy.android.piggy.R;
import howbuy.android.piggy.UserUtil;
import howbuy.android.piggy.UserUtil.UserSoundType;
import howbuy.android.piggy.api.dto.UserCardDto;
import howbuy.android.piggy.api.dto.UserCardListDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.service.ServiceMger.TaskBean;
import howbuy.android.piggy.service.UpdateUserDataService;
import howbuy.android.piggy.ui.AbsFragmentActivity;
import howbuy.android.piggy.ui.BindCardActivity;
import howbuy.android.piggy.ui.base.AbstractFragment;
import howbuy.android.util.AndroidUtil;
import howbuy.android.util.Cons;
import howbuy.android.util.StringUtil;
import howbuy.android.util.http.UrlMatchUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 用户银行列表
 * 
 * @author Administrator
 * 
 */
public class UserCardListFragment extends AbstractFragment implements OnItemClickListener {
	private static final int ATY_RE_QCODE1 = 10;;
	private static final int ATY_RE_QCODE2 = 11;;
	public static final String TAG = "UserCardListFragment";
	private UserCardListDto mCardListDto = ApplicationParams.getInstance().getPiggyParameter().getUserCardInfo();
	private ListView mListView;
	private ImageLoader mImageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.abs__ic_clear).showImageForEmptyUri(R.drawable.abs__ic_clear)
			.showImageOnFail(R.drawable.abs__ic_clear).cacheInMemory(true).cacheOnDisc(true).considerExifParams(false).build();;
	private BankAdapter mAdapter;
	private View mFooterView;
	private List<UserCardDto> mListDto;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.usercard_list, menu);
	}

	@Override
	public boolean isShoudRegisterReciver() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onServiceRqCallBack(Intent taskData, boolean isCurrPage) {
		// TODO Auto-generated method stub
		super.onServiceRqCallBack(taskData, isCurrPage);
		String taskType = taskData.getStringExtra(Cons.Intent_type);
		if (UpdateUserDataService.TaskType_UserCard.equals(taskType)) {
			dissmissIndeterProgress();
			mListDto.clear();
			mListDto.addAll(ApplicationParams.getInstance().getPiggyParameter().getUserCardInfo().getUserCardDtos());
			mAdapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		d("UserCardListFragment==onActivityResult||resultCode=="+resultCode+"||requestCode=="+requestCode);
		
		if (resultCode == Activity.RESULT_OK||requestCode==ATY_RE_QCODE1||requestCode==ATY_RE_QCODE2) {
			showIndeterProgress();
			ApplicationParams.getInstance().getServiceMger().addTask(new TaskBean(UpdateUserDataService.TaskType_UserCard, "1"));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		} else if (item.getItemId() == R.id.action_addcard) {
			Intent intent = new Intent(getActivity(), BindCardActivity.class);
			intent.putExtra(Cons.Intent_type, BindCardFragment.Into_SettingCardList);
			startActivityForResult(intent, ATY_RE_QCODE1);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		getSherlockActivity().finish();

		return true;
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.aty_card_list, container, false);
		mListView = (ListView) v.findViewById(R.id.list);
		setListAdapter();
		buildActionBar();
		return v;
	}

	private void setListAdapter() {
		mListDto = new ArrayList<UserCardDto>(mCardListDto.getUserCardDtos());
		if (mAdapter == null) {
			mAdapter = new BankAdapter();
		}
		if (mFooterView == null) {
			mFooterView = LayoutInflater.from(getSherlockActivity()).inflate(R.layout.lay_banklist_btm, null);
		}
		if (mListView.getFooterViewsCount() == 0) {
			mListView.addFooterView(mFooterView, null, false);
		}
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}

	public void buildActionBar() {
		ActionBar ab = getSherlockActivity().getSupportActionBar();
		ab.setTitle("我的银行卡");
	}

	public class BankAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mListDto.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mListDto.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			VieHoldw hView;
			if (convertView == null) {
				hView = new VieHoldw();
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_bank, null);
				hView.bankinfo_name = (TextView) convertView.findViewById(R.id.bankinfo_name);
				hView.bankinfo_no = (TextView) convertView.findViewById(R.id.bankinfo_no);
				hView.bankinfo_status = (TextView) convertView.findViewById(R.id.very_status);
				hView.bankinfo_limit = (TextView) convertView.findViewById(R.id.bankinfo_limit);
				hView.bankinfo_icon = (ImageView) convertView.findViewById(R.id.bankinfo_icon);
				convertView.setTag(hView);
			} else {
				hView = (VieHoldw) convertView.getTag();
			}

			UserCardDto u = mListDto.get(position);
			if (u != null) {
				hView.bankinfo_name.setText(u.getBankName());
				hView.bankinfo_no.setText(StringUtil.formatViewBankCard(u.getBankAcct()));
				String danBi = u.getLimitPerTime();// 单笔
				String danRi = u.getLimitPerDay();// 日限额
				hView.bankinfo_limit.setText("单笔限存入金额" + formatLimit(danBi) + ",日限额" + formatLimit(danRi));
				
				mImageLoader.displayImage(iconUrl(u.getBankCode()), hView.bankinfo_icon, options);
				UserSoundType sType = UserUtil.userSoundStatus(u.getCustBankId());
				if (sType == UserSoundType.Sounduccess) {
					hView.bankinfo_status.setVisibility(View.GONE);
				} else {
					hView.bankinfo_status.setVisibility(View.VISIBLE);
				}
				d(TAG + "==" + u.getBankRegionName());

			}
			return convertView;
		}

		private String iconUrl(String bankCode) {
			String basePath2 = UrlMatchUtil.getBasepath2() + AndroidUtil.getSourceFolderName() + "/" + bankCode + ".png";
			return basePath2;
		}

		public String formatLimit(String danbi) {
			if (TextUtils.isEmpty(danbi)) {
				return "--万";
			}
			try {
				BigDecimal b = new BigDecimal(danbi);
				int a = b.intValue();
				if (a < 1) {
					return String.valueOf(a);
				} else {
					int danbiInt = a / 10000;
					String wan = String.valueOf(danbiInt) + "万";
					return wan;
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			return "--万";
		}
	}

	public static final class VieHoldw {
		TextView bankinfo_name;
		TextView bankinfo_no;
		TextView bankinfo_limit;
		TextView bankinfo_status;
		ImageView bankinfo_icon;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), AbsFragmentActivity.class);
		UserCardDto uCardDto = (UserCardDto) parent.getItemAtPosition(position);
		intent.putExtra(Cons.Intent_name, UserCardDetailFragment.class.getName());
		intent.putExtra(Cons.Intent_bean, uCardDto);
		startActivityForResult(intent, ATY_RE_QCODE2);
	}
}
