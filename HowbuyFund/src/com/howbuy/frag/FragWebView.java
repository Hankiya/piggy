package com.howbuy.frag;

import howbuy.android.palmfund.R;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.howbuy.aty.AtyTbMain;
import com.howbuy.component.HbUrlHandler;
import com.howbuy.config.ValConfig;
import com.howbuy.entity.AtyInfs;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.frag.AbsHbFrag;
import com.howbuy.lib.utils.ViewUtils;
import com.howbuy.utils.NetToastUtils;

public class FragWebView extends AbsHbFrag {
	private WebView mWebView;
	private View mLayProgress;
	private ProgressBar mLayProgressHorizonal;
	private String mHomeUrl = null;
	private String mTempUrl = null;
	private int mLaunchResource = 0;
	private boolean mHasAlermView = false;
	private HbUrlHandler mHbUrlhandler = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().getWindow()
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	@Override
	public void onSaveInstanceState(Bundle o) {
		super.onSaveInstanceState(o);
		o.putString(ValConfig.IT_URL, mHomeUrl);
		o.putString(ValConfig.IT_ID, mTempUrl);
		o.putInt(ValConfig.IT_FROM, mLaunchResource);
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		findAllViews(root);
		mHbUrlhandler = new HbUrlHandler(this, mWebView, null);
		Bundle arg = bundle != null ? bundle : getArguments();
		if (arg != null) {
			parseArg(arg, bundle == null);
		}
	}

	private void parseArg(Bundle arg, boolean fromInit) {
		if (mTitleLable == null) {
			mTitleLable = arg.getString(ValConfig.IT_NAME);
		}
		mLaunchResource = arg.getInt(ValConfig.IT_FROM);
		mHomeUrl = arg.getString(ValConfig.IT_URL);
		mTempUrl = arg.getString(ValConfig.IT_ID);
		if (mHomeUrl != null && mHomeUrl.startsWith("http")) {
			mWebView.loadUrl(mHomeUrl);
		} else {
			if (getSherlockActivity() != null) {
				getSherlockActivity().finish();
			}
		}
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void findAllViews(final View root) {
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
				if (!mHasAlermView) {
					mLayProgressHorizonal.setProgress(newProgress);
					if (newProgress == 100) {
						ViewUtils.setVisibility(mLayProgressHorizonal, View.GONE);
					}
				}
				super.onProgressChanged(view, newProgress);
			}
		});

		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				if (!mHasAlermView) {
					ViewUtils.setVisibility(mLayProgressHorizonal, View.VISIBLE);
				}
				if (!mHomeUrl.equals(url)) {
					mTempUrl = url;
				}
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				if (!mHasAlermView) {
					showProgress(false);
				}
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
					mHasAlermView = addRootView(FragWebView.this, R.layout.com_pop_netalerm,
							R.id.root_id);
					if (mHasAlermView) {
						ViewUtils.setVisibility(mWebView, View.INVISIBLE);
						ViewUtils.setVisibility(mLayProgress, View.GONE);
						ViewUtils.setVisibility(mLayProgressHorizonal, View.GONE);
					}
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
						v.requestFocus();// for keybord.
					}
					break;
				}
				return false;
			}
		});
	}

	private boolean goBackPage() {
		if (mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return false;
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
		Intent up = new Intent(getSherlockActivity(), AtyTbMain.class);//
		up.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		if (AtyInfs.hasAty(AtyTbMain.class, null) == null) {
			TaskStackBuilder.create(getSherlockActivity()).addNextIntent(up).startActivities();
		} else {
			NavUtils.navigateUpTo(getSherlockActivity(), up);
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
	protected int getFragLayoutId() {
		return R.layout.frag_content_web;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mWebView.destroy();
		mHbUrlhandler.destory();
	}

	@Override
	public boolean onXmlBtClick(View v) {
		boolean handed = true;
		switch (v.getId()) {
		case R.id.tv_net_setting:
			NetToastUtils.launchNetSetting(getSherlockActivity());
			break;
		default:
			handed = false;
		}
		return handed ? true : super.onXmlBtClick(v);
	}
}
