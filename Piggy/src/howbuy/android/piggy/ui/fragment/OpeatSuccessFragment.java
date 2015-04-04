package howbuy.android.piggy.ui.fragment;

import howbuy.android.piggy.R;
import howbuy.android.piggy.help.ParseWebReqUri;
import howbuy.android.piggy.help.ParseWebReqUri.Web_Flag;
import howbuy.android.piggy.parameter.PiggyParams;
import howbuy.android.piggy.ui.ProPertyActivity;
import howbuy.android.piggy.ui.QueryActivity;
import howbuy.android.piggy.ui.base.AbstractFragment;
import howbuy.android.util.Cons;
import howbuy.android.util.SpannableUtil;
import howbuy.android.util.StringUtil;
import howbuy.android.util.TimestampUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class OpeatSuccessFragment extends AbstractFragment implements OnClickListener {
	public static final int TypeSaveMoneySuccess = 1;
	public static final int TypeOutMoneySuccess = 2;
	public static final int TypeSaveMoneySuccessCP = 3;
	private int typeSuccess = TypeSaveMoneySuccess;
	private String resValue;
	private Bundle mSaveBund;
	private TextView txt11;
	private TextView txt12;
	private TextView txt21;
	private TextView txt22;
	private ImageView imgType;
	private Button submitBtn;
	private boolean isfast;
	private int mIntoType; 

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putAll(getArguments());
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		// inflater.inflate(R.menu.moneysuccess, menu);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSherlockActivity().getSupportActionBar().setHomeButtonEnabled(false);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void jumpToHome() {
		Intent intent = new Intent(getActivity().getApplicationContext(), ProPertyActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(Cons.Intent_type, ProPertyActivity.NeeduserDataReload);
		getActivity().startActivity(intent);
		getActivity().finish();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.frag_success, container, false);
		txt11 = (TextView) v.findViewById(R.id.txt1_1);
		txt12 = (TextView) v.findViewById(R.id.txt1_2);
		txt21 = (TextView) v.findViewById(R.id.txt2_1);
		txt22 = (TextView) v.findViewById(R.id.txt2_2);
		submitBtn = (Button) v.findViewById(R.id.submit_btn);
		imgType = (ImageView) v.findViewById(R.id.img_type);
		submitBtn.setOnClickListener(this);
		if (savedInstanceState == null) {
			mSaveBund = getArguments();
		} else {
			mSaveBund = savedInstanceState;
		}
		return v;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		typeSuccess = mSaveBund.getInt(Cons.Intent_id);
		mIntoType = mSaveBund.getInt(Cons.Intent_type);
		resValue = mSaveBund.getString(Cons.Intent_bean);
		isfast = mSaveBund.getBoolean(Cons.Intent_normal);
		String currStatus = "";
		String confmeDate = "";
		String confmeTitle = "";
		int imgResource = 0;
		String title = "";
		if (typeSuccess == TypeOutMoneySuccess) {
			imgResource = R.drawable.out_success_bg;
			title = "取钱结果";
			currStatus = "取钱成功";
			confmeTitle = "到帐时间";
			if (isfast) {
				confmeDate = "5分钟内";
			} else {
				confmeDate = PiggyParams.getInstance().getTwoDay().getPaymentReceiptDt();
				if (!TextUtils.isEmpty(confmeDate)) {
					try {
						confmeDate=TimestampUtil.getChangeHeng(confmeDate, TimestampUtil.FORMATUPLOAD, TimestampUtil.FORMATUPLOADHeng);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} else if (typeSuccess == TypeSaveMoneySuccess) {
			title = "存钱结果";
			imgResource = R.drawable.save_success_bg;
			currStatus = "成功存入" + StringUtil.formatCurrency(resValue) + "元";
			confmeTitle = "开始计算收益";
			confmeDate = PiggyParams.getInstance().getTwoDay().getConfirmDt();
			if (!TextUtils.isEmpty(confmeDate)) {
				try {
					confmeDate=TimestampUtil.getChangeHeng(confmeDate, TimestampUtil.FORMATUPLOAD, TimestampUtil.FORMATUPLOADHeng);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else if (typeSuccess == TypeSaveMoneySuccessCP) {
			title = "存钱结果";
			imgResource = R.drawable.save_success_bg;
			currStatus = "订单已提交！";
			confmeTitle="开始计算收益";
			confmeDate = PiggyParams.getInstance().getTwoDay().getConfirmDt();
			if (!TextUtils.isEmpty(confmeDate)) {
				try {
					confmeDate=TimestampUtil.getChangeHeng(confmeDate, TimestampUtil.FORMATUPLOAD, TimestampUtil.FORMATUPLOADHeng);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			txt12.setText(getClickableSpan());
		}
		txt11.setText(currStatus);
		txt21.setText(confmeTitle);
		txt22.setText(confmeDate);
		getSherlockActivity().getSupportActionBar().setTitle(title);
		imgType.setBackgroundResource(imgResource);
	}

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		if ((mIntoType & Web_Flag.Flag_SaveMoney) == Web_Flag.Flag_SaveMoney) {
			getSherlockActivity().setResult(Activity.RESULT_OK);
			ParseWebReqUri.jumpWebPage(getActivity(), Activity.RESULT_OK, Web_Flag.Flag_SaveMoney);
			getSherlockActivity().finish();
			return true;
		} else {
			jumpToHome();
			return true;
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.submit_btn:
			onBackPressed();
			break;

		default:
			break;
		}
	}
	
	public void jumpWebPage(){
		
	}
	
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
	private SpannableStringBuilder getClickableSpan() {
		View.OnClickListener l = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent ien = new Intent(getActivity(), QueryActivity.class);
				startActivity(ien);
				if (getActivity() != null) {
					getActivity().finish();
				}
			}
		};

		SpannableString s1 = SpannableUtil.all("稍等10秒，您可以在", 14, -1, false);
		SpannableString s2 = SpannableUtil.all("交易历史", 14, R.color.blue, false);// R.color.textcolor
		s2.setSpan(new Clickable(l), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		SpannableString s3 = SpannableUtil.all("中查看订单状态", 14, -1, false);

		SpannableStringBuilder sBuilder = new SpannableStringBuilder();
		return sBuilder.append(s1).append(s2).append(s3);
	}
}
