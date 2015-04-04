package howbuy.android.piggy.ui.fragment;

import howbuy.android.piggy.R;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.parameter.GlobalParams;
import howbuy.android.piggy.ui.AbsFragmentActivity;
import howbuy.android.piggy.ui.base.AbstractFragment;
import howbuy.android.util.AndroidUtil;
import howbuy.android.util.Cons;
import howbuy.android.util.ManifestMetaData;
import howbuy.android.util.SpannableUtil;
import howbuy.android.util.http.UrlMatchUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.MenuItem;

public class AboutFragment extends AbstractFragment implements OnClickListener {
	public static final String QQGroupPiggIdKey = "307286028cd37ead01c8d4eb589fd99d443505c5b13ffe6d0f63a697eb4099ad";
	View mLongClick;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			getSherlockActivity().finish();
			return true;
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
		View view = inflater.inflate(R.layout.frag_about, null);
		view.findViewById(R.id.call_lay).setOnClickListener(this);
		view.findViewById(R.id.qq_lay).setOnClickListener(this);
		mLongClick=view.findViewById(R.id.weixin_lay);
		mLongClick.setOnClickListener(this);
		TextView t = (TextView) view.findViewById(R.id.about_top);
		TextView version = (TextView) view.findViewById(R.id.version);
		version.setText("当前版本V" + AndroidUtil.getVersionName());
		t.setText(getClickableSpan());
		t.setOnClickListener(this);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getSherlockActivity().getSupportActionBar().setTitle("关于储蓄罐");
		mLongClick.setLongClickable(true);
		mLongClick.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				showAppInfo();
				return false;
			}
		});
	};
	
	private  void showAppInfo() {
		LinkedHashMap<String, LinkedHashMap<String, String>> info=new LinkedHashMap<String, LinkedHashMap<String,String>>();
		LinkedHashMap<String, String> sysnfo=new LinkedHashMap<String, String>();
		sysnfo.put("App_package", AndroidUtil.getPackageName());
		sysnfo.put("App_VCode", String.valueOf(AndroidUtil.getVersionCode()));
		sysnfo.put("App_VName", AndroidUtil.getVersionName());
		sysnfo.put("App_InsTime", AndroidUtil.getPackageName());
		sysnfo.put("App_updTime", AndroidUtil.getPackageName());
		try {
			List<PackageInfo> pkgInfos = ApplicationParams.getInstance().getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES);
			for(PackageInfo pi : pkgInfos) {
				if (pi.packageName.equals(AndroidUtil.getPackageName())) {
					sysnfo.put("App_InsTime", String.valueOf(pi.firstInstallTime));
					sysnfo.put("App_updTime",  String.valueOf(pi.lastUpdateTime));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	    info.put("应用信息", sysnfo);
		
		//设备信息忽略
	    LinkedHashMap<String, String> debug=new LinkedHashMap<String, String>();
	    debug.put("Debug_Entaty", String.valueOf(GlobalParams.getGlobalParams().isEncryptDebugFlag()));
	    debug.put("MainUrl", UrlMatchUtil.getBasepath());
	    debug.put("HbUrl", UrlMatchUtil.getBasepath1());
	    debug.put("IconUrl", UrlMatchUtil.getBasepath2());
	    info.put("调试信息", debug);
	    LinkedHashMap<String, String> publicNet=new LinkedHashMap<String, String>();
	    publicNet.putAll(ApplicationParams.getInstance().getPubNetParams());
	    info.put("网络参数", publicNet);
	    
	    LinkedHashMap<String, String> meta=new LinkedHashMap<String, String>();
	    meta.put("channeId", String.valueOf(ManifestMetaData.get(ApplicationParams.getInstance(), "channeId")));
	    meta.put("TransactionCoopId", String.valueOf(ManifestMetaData.get(ApplicationParams.getInstance(), "TransactionCoopId")));
	    meta.put("TransactionActionId", String.valueOf(ManifestMetaData.get(ApplicationParams.getInstance(), "TransactionActionId")));
	    meta.put("TransactionCorpId", String.valueOf(ManifestMetaData.get(ApplicationParams.getInstance(), "TransactionCorpId")));
	    meta.put("JPUSH_APPKEY",hide(String.valueOf(ManifestMetaData.get(ApplicationParams.getInstance(), "JPUSH_APPKEY"))) );
	    meta.put("JPUSH_CHANNEL", String.valueOf(ManifestMetaData.get(ApplicationParams.getInstance(), "JPUSH_CHANNEL")));
	    meta.put("UMENG_APPKEY",hide( String.valueOf(ManifestMetaData.get(ApplicationParams.getInstance(), "UMENG_APPKEY"))));
	    meta.put("UMENG_CHANNEL", String.valueOf(ManifestMetaData.get(ApplicationParams.getInstance(), "UMENG_CHANNEL")));
	    info.put("Meta", meta);
	    
	    ScrollView sv=new ScrollView(getActivity());
	    LinearLayout layout=new LinearLayout(getActivity());
	    layout.setBackgroundColor(0xffffffff);
	    layout.setPadding(20, 20, 20, 20);
	    sv.addView(layout);
	    Set<String> infoKey=info.keySet();
	    for (String key : infoKey) {
			layout.setOrientation(LinearLayout.VERTICAL);
			TextView v=new TextView(getActivity());
			v.setPadding(0, 20, 0, 5);
			v.setText(key);
			layout.addView(v);
			Map<String, String> detail=info.get(key);
			Set<String> detailKey=detail.keySet();
			for (String dKey : detailKey) {
				TextView item=new TextView(getActivity());
				item.setText(dKey.toUpperCase()+":"+detail.get(dKey));
				layout.addView(item);
			}
		}
	    
	   new AlertDialog.Builder(getActivity()).setView(sv).setTitle("更多").setPositiveButton("确定", new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			dialog.dismiss();
		}
		
	}).show();
	}

	private String hide(String source){
		String res;
		int sLength=source.length();
		int maxLength=4;
		if (sLength>8) {
			String start=source.substring(0, maxLength);
			String end=source.substring(sLength-maxLength, sLength);
			res=start+"****"+end;
		}else {
			res=source;
		}
		return res;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent in;
		switch (v.getId()) {
		case R.id.call_lay:
			Uri callUri = Uri.parse("tel:" + "4007009665");
			in = new Intent(Intent.ACTION_DIAL, callUri);
			getSherlockActivity().startActivity(in);
			break;
		case R.id.about_top:
			Intent i;
			i = new Intent(getActivity(), AbsFragmentActivity.class);
			i.putExtra(Cons.Intent_name, "howbuy.android.piggy.ui.fragment.AboutHowbuyFragment");
			getSherlockActivity().startActivity(i);
			break;
		case R.id.qq_lay:
			// joinQQGroup(QQGroupPiggIdKey);
			AndroidUtil.copyText("338949757");
			showToastShort("QQ群号码已经复制到粘贴板！");
			break;
		case R.id.weixin_lay:
			// joinQQGroup(QQGroupPiggIdKey);
			AndroidUtil.copyText("howbuy");
			showToastShort("微信公众号已经复制到粘贴板！");
			break;
		default:
			break;
		}
	}

	/****************
	 * 
	 * 发起添加群流程。群号：{QQ_GROUP_NAME}({QQ_GROUP_UIN}) 的 key 为， {id_key} 调用
	 * joinQQGroup({id_key}) 即可发起手Q客户端申请加群 {QQ_GROUP_NAME}({QQ_GROUP_UIN})
	 * 
	 * @param key
	 *            由官网生成的key
	 * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
	 ******************/
	public boolean joinQQGroup(String key) {
		Intent intent = new Intent();
		intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
		// 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
		// //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
		try {
			getSherlockActivity().startActivity(intent);
			return true;
		} catch (Exception e) {
			// 未安装手Q或安装的版本不支持
			showToastShort("未安装手机移动QQ");
			return false;
		}
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
				Toast.makeText(getActivity(), "Click Success", Toast.LENGTH_SHORT).show();
				Intent i;
				i = new Intent(getActivity(), AbsFragmentActivity.class);
				i.putExtra(Cons.Intent_name, "howbuy.android.piggy.ui.fragment.AboutHowbuyFragment");
				getSherlockActivity().startActivity(i);
			}
		};

		SpannableString s1 = SpannableUtil.all("储蓄罐是好买财富（", 14, -1, false);
		SpannableString s2 = SpannableUtil.all("了解好买", 14, -1, false);// R.color.textcolor
		s2.setSpan(new Clickable(l), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		SpannableString s3 = SpannableUtil.all("）为用户打造的一款移动理财软件，产品有如下特色：", 14, -1, false);

		SpannableStringBuilder sBuilder = new SpannableStringBuilder();
		return sBuilder.append(s1).append(s2).append(s3);
	}


}
