package howbuy.android.piggy.ui.fragment;
import howbuy.android.piggy.R;
import howbuy.android.piggy.help.ParseWebReqUri;
import howbuy.android.piggy.help.ParseWebReqUri.Web_Flag;
import howbuy.android.piggy.ui.OutMoneyActivity;
import howbuy.android.piggy.ui.ProPertyActivity;
import howbuy.android.piggy.ui.SaveMoneyActivity;
import howbuy.android.piggy.ui.SettingMainActivity;
import howbuy.android.piggy.ui.base.AbstractFragment;
import howbuy.android.util.Cons;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BindCardSucceedFragment extends AbstractFragment{
	/**
	 * 绑卡成功
	 */
	public static final String Type_Res_BindCardSuccess = "Type_Res_BindCardSuccess";
	/**
	 * 绑卡失败
	 */
	public static final String Type_Res_BindCardFailed = "Type_Res_BindCardFailed";
	
	private Button sureBtn;
	private ImageView resultImg;
	private TextView resultTv;
	private int intoType = BindCardFragment.Into_Setting;
	private String bindCardSuccessFlag = Type_Res_BindCardSuccess;   //绑卡成功 1； 失败 0

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		intoType = getActivity().getIntent().getIntExtra(Cons.Intent_type, 0);
		bindCardSuccessFlag = getActivity().getIntent().getStringExtra(Cons.Intent_id);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.aty_bindcard_succeed, container, false);
		sureBtn = (Button) view.findViewById(R.id.sure_btn);
		resultImg = (ImageView)view.findViewById(R.id.result_img);
		if (Type_Res_BindCardFailed.equals(bindCardSuccessFlag)){//失败
			resultTv = (TextView)view.findViewById(R.id.result_tv);
			resultImg.setImageResource(R.drawable.ic_cw);
			resultTv.setText("绑卡失败！");
			sureBtn.setText("我知道了");
			sureBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					jumpFialdIntent();
				}
			});
		}else {//成功
			resultImg.setBackgroundResource(R.drawable.ic_zq);
			
			if (intoType==BindCardFragment.Into_SettingCardList) {
				sureBtn.setText("完成");
			}
			sureBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					jumpSuccessIntent();
				}
			});
		}
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	private void jumpSuccessIntent() {
		Intent in=new Intent();
		if ((intoType & Web_Flag._flagLogin) == Web_Flag._flagLogin) {//web标致
			if ((intoType & Web_Flag.Flag_SaveMoney) == Web_Flag.Flag_SaveMoney) {
				in.setClass(getActivity(), SaveMoneyActivity.class);
				in.putExtra(Cons.Intent_type, intoType);
				startActivity(in);
			}else if ((intoType & Web_Flag.Flag_BindCard) == Web_Flag.Flag_BindCard) {
				getSherlockActivity().setResult(Activity.RESULT_OK);
				ParseWebReqUri.jumpWebPage(getActivity(), Activity.RESULT_OK, Web_Flag.Flag_SaveMoney);
			}else {
				startActProper();
			}
		}else {
			switch (intoType) {
			case BindCardFragment.Into_OutMoney:
				in.setClass(getActivity(), OutMoneyActivity.class);
				in.putExtra(Cons.Intent_type, intoType);
				startActivity(in);
			case BindCardFragment.Into_Register:
			case BindCardFragment.Into_SaveMoney:
				in.setClass(getActivity(), SaveMoneyActivity.class);
				in.putExtra(Cons.Intent_type, intoType);
				startActivity(in);
				break;
			case BindCardFragment.Into_Setting:
				in = new Intent(getActivity().getApplicationContext(), SettingMainActivity.class);
				in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(in);
				break;
			case BindCardFragment.Into_SettingCardList:
				getActivity().setResult(Activity.RESULT_OK);
				getActivity().finish();
				break;
			case BindCardFragment.Into_SaveMoneyDialog:
//				in = new Intent(getActivity().getApplicationContext(), SaveMoneyActivity.class);
//				in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(in);
				getActivity().setResult(Activity.RESULT_OK);
				getActivity().finish();
				break;
			default:
				startActProper();
				break;
			}
		}
		if (getActivity() != null) {
			getActivity().finish();
		}
	}
	
	
	private void jumpFialdIntent(){
		Intent in=new Intent();
		if ((intoType & Web_Flag._flagLogin) == Web_Flag._flagLogin) {//web标致
			if ((intoType & Web_Flag.Flag_SaveMoney) == Web_Flag.Flag_SaveMoney) {
				getActivity().setResult(Activity.RESULT_CANCELED);
			}else if ((intoType & Web_Flag.Flag_BindCard) == Web_Flag.Flag_BindCard) {
				getActivity().setResult(Activity.RESULT_CANCELED);
			}else {
				startActProper();
			}
		}else {
			switch (intoType) {
			case BindCardFragment.Into_OutMoney:
				startActProper();
			case BindCardFragment.Into_Register:
			case BindCardFragment.Into_SaveMoney:
				startActProper();
				break;
			case BindCardFragment.Into_SettingCardList:
				getActivity().setResult(Activity.RESULT_CANCELED);
				getActivity().finish();
				break;
			case BindCardFragment.Into_Setting:
				in = new Intent(getActivity().getApplicationContext(), SettingMainActivity.class);
				in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(in);
				break;
			default:
				startActProper();
				break;
			}
		}
		if (getActivity() != null) {
			getActivity().finish();
		}
	}
	
	
	/**
	 * 跳转到主页
	 */
	private void startActProper() {
		Intent startIntent;
		startIntent = new Intent(getSherlockActivity(), ProPertyActivity.class);
		startIntent.putExtra(Cons.Intent_type, ProPertyActivity.NeeduserDataReload);
		startActivity(startIntent);
	}
	
	
	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if (Type_Res_BindCardFailed.equals(bindCardSuccessFlag)){//失败
			jumpFialdIntent();
			return true;
		}else{
			jumpSuccessIntent();
			return true;
		}
	}
}
