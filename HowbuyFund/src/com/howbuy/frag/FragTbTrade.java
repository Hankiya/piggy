package com.howbuy.frag;

import howbuy.android.palmfund.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.howbuy.aty.AtyTbMain;
import com.howbuy.component.HbUrlHandler;
import com.howbuy.config.Analytics;
import com.howbuy.config.ValConfig;
import com.howbuy.entity.AtyInfs;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.frag.AbsHbFrag;
import com.howbuy.lib.utils.SysUtils;
import com.howbuy.lib.utils.ViewUtils;
import com.howbuy.utils.NetToastUtils;

public class FragTbTrade extends AbsHbFrag {
	public static final String INTENT_TRANSACTIONACTIONID = "INTENT_TRANSACTIONACTIONID";
	public static final String INTENT_TRANSACTIONCOOPID = "INTENT_TRANSACTIONCOOPID";
	private String index = ValConfig.URL_TRADE_BASEURL_RELEASE + "user/index.htm";
	private final static String JS = "javascript:var firstDiv=document.getElementsByTagName(\"div\")[0];var nodeSize=firstDiv.childNodes.length;var res=0;if(nodeSize==1){res=firstDiv.childNodes.item(0).textContent}else if(nodeSize==2){res=firstDiv.childNodes.item(0).getAttribute(\"href\");res=res+\"|\"+firstDiv.childNodes.item(0).innerHTML;res=res+\"|\"+firstDiv.childNodes.item(1).textContent}else{var firstValue=firstDiv.childNodes.item(0).textContent;if(firstValue==\" \"){res=firstDiv.childNodes.item(1).getAttribute(\"href\");res=res+\"|\"+firstDiv.childNodes.item(1).innerHTML;res=res+\"|\"+firstDiv.childNodes.item(2).textContent}else{res=firstDiv.childNodes.item(0).getAttribute(\"href\");res=res+\"|\"+firstDiv.childNodes.item(0).innerHTML;res=res+\"|\"+firstDiv.childNodes.item(2).getAttribute(\"href\");res=res+\"|\"+firstDiv.childNodes.item(2).innerHTML}};firstDiv.parentNode.removeChild(firstDiv);window.Methods.javaMethod(res);";
	private WebView mWebView;
	private View mLayProgress, mIBGoback;
	private ProgressBar mLayProgressHorizonal;
	private RelativeLayout mBtmLay;
	private String mHomeUrl = null;
	private String mTempUrl = null;
	private boolean mHomePage = true;
	private int mLaunchResource = 0;
	private String coopId;
	private String actionId;
	private boolean mHasAlermView = false;
	private HbUrlHandler mHbUrlhandler = null;

	@SuppressLint("NewApi")
	@Override
	protected void onAttachChanged(Activity aty, boolean isAttach) {
		super.onAttachChanged(aty, isAttach);
		if (isAttach) {
			if (getSherlockActivity().getSupportActionBar().isShowing()) {
				getSherlockActivity().getSupportActionBar().hide();
			}
		}
	}

	private boolean goBackPage() {
		if (!mHomePage && mWebView.canGoBack()) {
			if (!mWebView.canGoBackOrForward(-2)) {
				mIBGoback.setEnabled(false);
			}
			mWebView.goBack();
			return true;
		}
		return false;
	}

	@Override
	public boolean onXmlBtClick(View v) {
		boolean handed = true;
		switch (v.getId()) {
		case R.id.ib_refush:
			mWebView.reload();
			break;
		case R.id.ib_close:
			navToUp();
			break;
		case R.id.ib_back:
			goBackPage();
			break;
		case R.id.tv_net_setting:
			NetToastUtils.launchNetSetting(getSherlockActivity());
			break;
		default:
			handed = false;
		}
		if (handed) {
			return true;
		}
		return super.onXmlBtClick(v);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putString(ValConfig.IT_URL, mHomeUrl);
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		findAllViews(root);
		mWebView.requestFocus();
		mHbUrlhandler = new HbUrlHandler(this, mWebView, null);
		coopId = SysUtils.getMetaData(getActivity()).getString("TransactionCoopId");
		actionId = SysUtils.getMetaData(getActivity()).getString("TransactionActionId");
		Bundle arg;
		if (bundle != null) {
			arg = bundle;
		} else {
			arg = getArguments();
		}

		if (arg != null) {
			mLaunchResource = arg.getInt(ValConfig.IT_FROM);
			loadWebViewData(arg);
		} else {
			mHomeUrl = ValConfig.URL_TRADE_BASEURL_RELEASE;
			mWebView.loadUrl(mHomeUrl);
		}
	}

	private void loadWebViewData(Bundle bundle) {
		if (mTitleLable == null) {
			mTitleLable = getArguments().getString(ValConfig.IT_NAME);
		}
		mHomeUrl = getArguments().getString(ValConfig.IT_URL);
		if (mHomeUrl == null) {
			mHomeUrl = ValConfig.URL_TRADE_BASEURL_RELEASE;
		}
		if (mHomeUrl.startsWith("http")) {
			mWebView.loadUrl(mHomeUrl);
		}
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void findAllViews(final View root) {
		mIBGoback = root.findViewById(R.id.ib_back);
		mBtmLay = (RelativeLayout) root.findViewById(R.id.btm_lay);
		mIBGoback.setEnabled(false);
		mWebView = (WebView) root.findViewById(R.id.webview);
		mLayProgress = root.findViewById(R.id.lay_progress);
		mLayProgressHorizonal = (ProgressBar) root.findViewById(R.id.pb_horizonal);
		mWebView.setBackgroundColor(0);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setAllowFileAccess(true);
		mWebView.getSettings().setSavePassword(false);
		showProgress(true);
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				d("onProgressChanged", "newProgress=" + newProgress);
				mLayProgressHorizonal.setProgress(newProgress);
				if (newProgress == 100) {
					ViewUtils.setVisibility(mLayProgressHorizonal, View.GONE);
				}
				super.onProgressChanged(view, newProgress);
			}
		});

		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				ViewUtils.setVisibility(mLayProgressHorizonal, View.VISIBLE);
				if (url.contains("login.htm")
						&& (url.contains("coopId") == false || url.contains("actId") == false)) {
					mWebView.stopLoading();
					url = createLoginLink(url);
					mWebView.loadUrl(url);
				}
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				analyticsLoginSuccess(ValConfig.URL_TRADE_BASEURL_RELEASE_DoMain);
				if (mHomeUrl == null) {
					mHomeUrl = url.substring(url.lastIndexOf("/"));
				} else {
					if (url.contains("login/login.htm")) {
						mHomePage = true;
						mIBGoback.setEnabled(false);
					} else {
						mHomePage = false;
						mIBGoback.setEnabled(true);
					}
				}
				mTempUrl = url;
				if (!mHasAlermView) {
					if (!url.equals("about:blank:")) {
						showProgress(false);
					}
				}
				d("onPageFinished", url);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (mHbUrlhandler.handWebUrl(url)) {
					return true;
				}
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description,
					String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				if (GlobalApp.getApp().getNetType() <= 1 && failingUrl.equals(mHomeUrl)) {
					mHasAlermView = addRootView(FragTbTrade.this, R.layout.com_pop_netalerm,
							R.id.root_id);
					if (mHasAlermView) {
						ViewUtils.setVisibility(mWebView, View.INVISIBLE);
						ViewUtils.setVisibility(mLayProgress, View.GONE);
						ViewUtils.setVisibility(mLayProgressHorizonal, View.GONE);
						view.loadUrl("about:blank:");
					}
				}
				d("onReceivedError", "errorCode=" + errorCode + ",des=" + description + ",url="
						+ failingUrl);
			}

		});

		root.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				Rect r = new Rect();
				// r will be populated with the coordinates of your view that
				// area still visible.
				root.getWindowVisibleDisplayFrame(r);
				int heightDiff = root.getRootView().getHeight() - (r.bottom - r.top);
				if (heightDiff > 100) { // if more than 100 pixels, its probably
										// a keyboard...
					mBtmLay.setVisibility(View.GONE);
				} else {
					mBtmLay.setVisibility(View.VISIBLE);
				}
				if (!TextUtils.isEmpty(mTempUrl) && !mTempUrl.equals(mWebView.getUrl())
						&& isAdded()) {
					Analytics.onEvent(GlobalApp.getApp(), Analytics.LOAD_NEW_TRADE_PAGE);
				}
			}
		});

		mWebView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (!v.hasFocus()) {
						v.requestFocus();//for keybord.
					}
					break;
				}
				return false;
			}
		}); 
	}

	private String getCookies(String url) {
		CookieManager cm = CookieManager.getInstance();
		String cookiestr = cm.getCookie(url);
		return cookiestr;
	}

	private void setCookieLoginedFlag(String url) {
		String cookiesValue = "logined=false; domain=" + ValConfig.URL_TRADE_BASEURL_RELEASE_DoMain;
		CookieSyncManager.createInstance(GlobalApp.getApp());
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		// cookieManager.removeSessionCookie();// 移除
		cookieManager.setCookie(url, cookiesValue);// cookies是在HttpClient中获得的cookie
		CookieSyncManager.getInstance().sync();
	}

	/**
	 * 统计登录成功
	 * 
	 * @param url
	 */
	private void analyticsLoginSuccess(String url) {
		String cookies = getCookies(url);
		if (!TextUtils.isEmpty(cookies)) {
			String[] ckArry = cookies.split("; ");
			for (String string : ckArry) {
				if (string.contains("logined") && string.contains("true")) {
					Analytics.onEvent(getActivity(), Analytics.TRADE_LOGIN_SUCCEED);
					setCookieLoginedFlag(url);
				}
			}
		}
	}

	/**
	 * 判断登录url是否有coopID 如果没有 则加上
	 * 
	 * @param mLoginUrl
	 * @return
	 */
	private String createLoginLink(String mLoginUrl) {
		StringBuilder sBuilder = new StringBuilder(mLoginUrl);
		if (mLoginUrl.contains("login.htm") && mLoginUrl.contains("coopId") == false) {
			if (mLoginUrl.contains("login.htm?")) {
				sBuilder.append("&").append("coopId=").append(coopId).append("&").append("actId=")
						.append(actionId);
			} else {
				sBuilder.append("?").append("coopId=").append(coopId).append("&").append("actId=")
						.append(actionId);
			}
		}
		return sBuilder.toString();
	}

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_tb_trade;
	}

	@Override
	public boolean onKeyBack(boolean fromBar, boolean isFirstPress, boolean isTwiceInTime) {
		if (goBackPage() && !mHasAlermView) {
			return true;
		} else {
			return navToUp();
		}
	}

	private boolean navToUp() {
		Intent upIntent = new Intent(getSherlockActivity(), AtyTbMain.class);//
		upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		if (AtyInfs.hasAty(AtyTbMain.class, null) == null) {
			TaskStackBuilder.create(getSherlockActivity()).addNextIntent(upIntent)
					.startActivities();
			getSherlockActivity()
					.overridePendingTransition(R.anim.push_up_none, R.anim.push_up_out);
		} else {
			NavUtils.navigateUpTo(getSherlockActivity(), upIntent);
			getSherlockActivity()
					.overridePendingTransition(R.anim.push_up_none, R.anim.push_up_out);
		}
		return true;
	}

	@Override
	public boolean onNetChanged(int netType, int preNet) {
		if (isVisible()) {
			if (netType <= 1 && preNet > 1) {
				if (!mHasAlermView) {
					// pop("网络不可用", false);
				}
			} else {
				if (netType > 1 && preNet <= 1) {
					if (mHasAlermView) {
						if (removeRootView(this, R.id.root_id)) {
							mHasAlermView = false;
							mWebView.loadUrl(mHomeUrl);
							showProgress(true);
						}
					} else {
						mWebView.reload();
					}
				}
			}
			return true;
		}
		return super.onNetChanged(netType, preNet);
	}

	private void showProgress(boolean show) {
		if (show) {
			if (mLayProgress.getVisibility() != View.VISIBLE) {
				mLayProgress.setVisibility(View.VISIBLE);
			}
			if (mWebView.getVisibility() == View.VISIBLE) {
				mWebView.setVisibility(View.INVISIBLE);
			}
		} else {
			if (mLayProgress.getVisibility() == View.VISIBLE) {
				mLayProgress.setVisibility(View.GONE);
			}
			if (mWebView.getVisibility() != View.VISIBLE) {
				mWebView.setVisibility(View.VISIBLE);
			}
		}
	}

	private boolean addRootView(Fragment frag, int layoutId, int viewId) {
		FrameLayout root = (FrameLayout) frag.getView();
		if (root != null) {
			viewId = viewId <= 0 ? R.id.root_id : viewId;
			View content = getLayoutInflater(null).inflate(layoutId, null);
			content.setId(viewId);
			root.addView(content);
			return true;
		}
		return false;
	}

	private boolean removeRootView(Fragment frag, int viewId) {
		FrameLayout root = (FrameLayout) frag.getView();
		if (root != null) {
			viewId = viewId <= 0 ? R.id.root_id : viewId;
			View removeView = null;
			int n = root.getChildCount();
			for (int i = 0; i < n; i++) {
				removeView = root.getChildAt(i);
				if (removeView.getId() == viewId) {
					break;
				} else {
					removeView = null;
				}
			}
			if (removeView != null) {
				root.removeView(removeView);
				return true;
			}

		}
		return false;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mWebView.destroy();
		mHbUrlhandler.destory();
	}
}
