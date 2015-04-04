package howbuy.android.piggy.ui.fragment;

import howbuy.android.lib.MyAsyncTask;
import howbuy.android.piggy.R;
import howbuy.android.piggy.UserUtil;
import howbuy.android.piggy.api.DispatchAccessData;
import howbuy.android.piggy.api.dto.WebviewReqDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.help.ParseWebReqUri;
import howbuy.android.piggy.ui.base.AbstractFragment;
import howbuy.android.util.AndroidUtil;
import howbuy.android.util.Cons;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.actionbarsherlock.view.MenuItem;
import com.google.myjson.Gson;
import com.google.myjson.reflect.TypeToken;

public class PureWebFragment extends AbstractFragment {
	public static final String NAME = "PureWebFragment";
	private WebView mWebView;
	private ProgressBar mProgressBar;
	private PureType mPureType;
	private String mUrl;
	private String mExtUrl;
	private String mExtName;

	public enum PureType {
		about(0), help(1), url(2),urlWearOperal(3);

		PureType(int a) {
			this.type=a;
		}

		private int type;

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return String.valueOf(type) ;
		}

		public static PureType getValueType(int inType) {
			for (PureType mType : PureType.values()) {
				if (mType.type == inType) {
					return mType;
				}
			}
			throw new IllegalArgumentException("PureType name: " + inType);
		}
		
		public int getType(){
			return type;
		}
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		int urlType = PureType.url.type;
		final String versionName = AndroidUtil.getVersionName();
		if (savedInstanceState == null) {
			urlType = getArguments().getInt(Cons.Intent_type);
			mExtUrl = getArguments().getString(Cons.Intent_id);
			mExtName = getArguments().getString(Cons.Intent_name);
		} else {
			urlType = savedInstanceState.getInt(Cons.Intent_type);
			mExtUrl = savedInstanceState.getString(Cons.Intent_id);
			mExtName = savedInstanceState.getString(Cons.Intent_name);
		}

		mPureType = PureType.getValueType(urlType);

		if (mPureType != PureType.url) {
			mProgressBar.setVisibility(View.GONE);
		}

		switch (mPureType) {
		case about:
			mUrl = "file:///android_asset/about.html";
			getSherlockActivity().getSupportActionBar().setTitle(R.string.about);
			break;
		case help:
			// mUrl = "file:///android_asset/help.html";
			mUrl = "http://www.ehowbuy.com/help/piggy/index.html";
			getSherlockActivity().getSupportActionBar().setTitle(R.string.help);
			break;
		case url:
			mUrl = mExtUrl;
			getSherlockActivity().getSupportActionBar().setTitle(mExtName);
			break;
		case urlWearOperal:
			mUrl = mExtUrl;
			getSherlockActivity().getSupportActionBar().setTitle(mExtName);
			break;
		default:
			break;
		}

		mWebView.getSettings().setJavaScriptEnabled(true);

		WebViewClient vClient = new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				String title=view.getTitle();
				if (!TextUtils.isEmpty(title)&&TextUtils.isEmpty(mExtName)) {
					getSherlockActivity().setTitle(String.valueOf(title+" "));
				}
				if (mPureType == PureType.about) {
					mWebView.loadUrl("javascript:document.getElementById(\"mVersion\").innerHTML=\"" + "版本："+versionName + "\"");
				}
			}
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				Log.d(NAME, "ovldurl--"+url);
				if (url.startsWith("http")) {
					if (url.toLowerCase().endsWith("apk")) {
						Intent i = new Intent("android.intent.action.VIEW");
						i.setData(Uri.parse(url));
						startActivity(i);
						return true;
					}else {
						return super.shouldOverrideUrlLoading(view, url);
					}
				}else {
					if (url.startsWith("hb")) {
						Map<String, String> map=ParseWebReqUri.parseHBUrl(url);
						String urlMain=map.get(ParseWebReqUri.URI_MAIN);
						if (urlMain.equals(ParseWebReqUri.Type_QUserIfo)) {
							try {
								handWebReq(map);
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						return true;
					}else {
						try {
							Intent i = new Intent("android.intent.action.VIEW");
							i.setData(Uri.parse(url));
							startActivity(i);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						return true;
					}
				}
				
			}
		};

		WebChromeClient chromeClient = new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				super.onProgressChanged(view, newProgress);
				if (newProgress < 100) {
					mProgressBar.setVisibility(View.VISIBLE);
				}else{
					mProgressBar.setVisibility(View.GONE);
				}
				mProgressBar.setProgress(newProgress);
			}
		};
		mWebView.setWebViewClient(vClient);
		mWebView.setWebChromeClient(chromeClient);
		mWebView.loadUrl(mUrl);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.aty_webview, null);
		mWebView = (WebView) view.findViewById(R.id.webview);
		mProgressBar = (ProgressBar) view.findViewById(R.id.webview_progress);
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putAll(getArguments());
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			if (mWebView.canGoBack()) {
				mWebView.goBack();
			}else {
				getSherlockActivity().onBackPressed();
			}
			
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
	public void handWebReq(Map<String, String> webParams) throws UnsupportedEncodingException {
		String id=webParams.get("id");
		String param=webParams.get("params");
		param = URLDecoder.decode(param, "utf-8");
		String cb=webParams.get("cb");
		String custNo=UserUtil.getUserNo();
		String macAdr =AndroidUtil.getWifiMac(ApplicationParams.getInstance());
		String imei = AndroidUtil.getImei(ApplicationParams.getInstance());
		String phoneNumber = AndroidUtil.getPhoneNumber(getSherlockActivity());
		
		
		Map<String, String> reqParams=new Gson().fromJson(param,new TypeToken<Map<String, String>>() {  
        }.getType());
		reqParams.put("imei", imei);
		reqParams.put("phoneNumber", phoneNumber);
		reqParams.put("mac", macAdr);
		new EncryTask(custNo, id, reqParams,cb).execute();
	}
	
	public void handReqRes(String reqId,String cb,WebviewReqDto result){
		Log.d(NAME, cb+result);
		Map<String, String> params = new HashMap<String, String>();
		if (result.getSpecialCode() != null) {
			params.put("contentCode", result.getSpecialCode());
		}
		if (result.getContentMsg() != null) {
			params.put("contentMsg", result.getContentMsg());
		}
		if (result.getBody() != null) {
			params.put("body", result.getBody().toString());
		}
		String jsonString = new Gson().toJson(params);
		String js="javascript:"+cb+"("+reqId+",\'"+ jsonString+"\')";
		Log.d(NAME, "js="+js);
		mWebView.loadUrl(js);
	}
	
	class EncryTask extends MyAsyncTask<String, Void, WebviewReqDto> {
		private String cb;
		private String custNo;
		private String reqId;
		private Map<String, String> reqParams;
		
		public EncryTask(String custNo, String reqId, Map<String, String> reqParams,String cb) {
			super();
			this.custNo = custNo;
			this.reqId = reqId;
			this.reqParams = reqParams;
			this.cb=cb;
		}

		@Override
		protected WebviewReqDto doInBackground(String... params) {
			// TODO Auto-generated method stub
			return DispatchAccessData.getInstance().webviewReq(custNo,reqId,reqParams);
		}

		@Override
		protected void onPostExecute(WebviewReqDto result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.d(NAME, "res="+result);
				handReqRes(reqId,cb,result);
		}
	}
	
	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		if (mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return false;
		
		
		
	}
	
	
	
}
