package howbuy.android.piggy.ui.fragment;

import howbuy.android.piggy.R;
import howbuy.android.piggy.api.dto.UserCardDto;
import howbuy.android.piggy.api.dto.UserCardListDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.ui.OutMoneyActivity;
import howbuy.android.piggy.ui.base.AbstractFragment;
import howbuy.android.util.AndroidUtil;
import howbuy.android.util.Cons;
import howbuy.android.util.SpannableUtil;
import howbuy.android.util.StringUtil;
import howbuy.android.util.http.UrlMatchUtil;

import java.math.BigDecimal;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
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
import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 用户银行列表
 * @author Administrator
 *
 */
public class UserCardSelectFragment extends AbstractFragment implements OnItemClickListener{
	private UserCardListDto mCardListDto=ApplicationParams.getInstance().getPiggyParameter().getUserCardInfo();
	private ListView mListView;
	private ImageLoader mImageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options= new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.abs__ic_clear).showImageForEmptyUri(R.drawable.abs__ic_clear).showImageOnFail(R.drawable.abs__ic_clear).cacheInMemory(true)
			.cacheOnDisc(true).considerExifParams(false).build();;
	private BankAdapter mAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId()==android.R.id.home) {
			onBackPressed();
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
		View v=inflater.inflate(R.layout.aty_card_list, container,false);
		mListView=(ListView) v.findViewById(R.id.list);
		if (mAdapter==null) {
			mAdapter=new BankAdapter();
		}
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		buildActionBar();
		return v;
	}
	
	
	public void buildActionBar() {
		ActionBar ab= getSherlockActivity().getSupportActionBar();
		ab.setTitle("选择取钱银行卡");
	}
	

	public class BankAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mCardListDto.getUserCardDtos().size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mCardListDto.getUserCardDtos().get(position);
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
				hView.bankinfo_allow = (ImageView) convertView.findViewById(R.id.allow);
				convertView.setTag(hView);
			} else {
				hView = (VieHoldw) convertView.getTag();
			}

			hView.bankinfo_allow.setVisibility(View.GONE);
			hView.bankinfo_status.setVisibility(View.GONE);
			UserCardDto u=mCardListDto.getUserCardDtos().get(position);
			hView.bankinfo_name.setText(u.getBankName());
			hView.bankinfo_no.setText(StringUtil.formatViewBankCard(u.getBankAcct()));
			String userCardIncome=u.getAvailAmt();
			
			
			userCardIncome=StringUtil.formatCurrency(userCardIncome);
			userCardIncome+="元";
			SpannableStringBuilder sb=new SpannableStringBuilder().append(SpannableUtil.all(userCardIncome, -1, R.color.actioncolor, false)).append("可取");
			hView.bankinfo_limit.setText(sb) ;
			mImageLoader.displayImage(iconUrl(u.getBankCode()), hView.bankinfo_icon,options);
			return convertView;
		}
		
		private String iconUrl(String bankCode){
			String basePath2=UrlMatchUtil.getBasepath2()+AndroidUtil.getSourceFolderName()+"/"+bankCode+".png";
			return basePath2;
		}

		public String formatLimit(String danbi){
			if (TextUtils.isEmpty(danbi)) {
				return "--万";
			}
			try {
				BigDecimal b = new BigDecimal(danbi);   
				int a=b.intValue();
				if (a<1) {
					return String.valueOf(a);	
				}else {
					int danbiInt=a/10000;
					String wan=String.valueOf(danbiInt)+"万";
					return wan;
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			return "--万";
		}
	}
	public static final class VieHoldw{
		TextView bankinfo_name;
		TextView bankinfo_no;
		TextView bankinfo_limit;
		TextView bankinfo_status;
		ImageView bankinfo_icon;
		ImageView bankinfo_allow;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), OutMoneyActivity.class);
		UserCardDto uCardDto=(UserCardDto) parent.getItemAtPosition(position);
		intent.putExtra(Cons.Intent_bean, uCardDto);
		intent.putExtras(getActivity().getIntent().getExtras());
		startActivity(intent);
	}
}
