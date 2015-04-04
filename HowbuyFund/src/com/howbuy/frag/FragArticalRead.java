package com.howbuy.frag;

import howbuy.android.palmfund.R;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.VideoView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.howbuy.aty.AtyEmpty;
import com.howbuy.aty.AtyEmptyTab;
import com.howbuy.aty.AtyTbMain;
import com.howbuy.component.AppFrame;
import com.howbuy.component.HbUrlHandler;
import com.howbuy.config.Analytics;
import com.howbuy.config.ValConfig;
import com.howbuy.db.DbUtils;
import com.howbuy.db.DbUtils.SqlExeObj;
import com.howbuy.entity.AtyInfs;
import com.howbuy.entity.NewsItem;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.frag.AbsHbFrag;
import com.howbuy.lib.interfaces.INetChanged;
import com.howbuy.lib.net.AsyPoolTask;
import com.howbuy.lib.net.ParDefault;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.net.UrlUtils;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.lib.utils.SysUtils;
import com.howbuy.utils.NetToastUtils;
import com.howbuy.utils.ShareHelper;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;

@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
public class FragArticalRead extends AbsHbFrag {
	public static final int ARTICLA_NEWS = 0;
	public static final int ARTICLA_RESEARCH = 1;
	public static final int ARTICLA_ISSUE = 2;
	public static final int ARTICLA_INTERVIEW = 3;
	private static final int TASK_QUERY_COLLECT = 1;
	private static final int TASK_ADD_COLLECT = 2;
	private static final int TASK_DEL_COLLECT = 3;
	private boolean mHasInit = false, mJsFont = false;
	private WebView webView = null;
	private View layProgress = null;
	private NewsItem mBean = null;
	private String mArticalId = null;
	private int mArticalType = 0;
	private int mFontSizeType = 0;
	private int mLaunchResource = 0;
	private int[] mFontlist = new int[] { 100, 140 };
	private boolean mHasAlermView = false;
	private String mHomeUrl = null;
	private HbUrlHandler mHbUrlhandler = null;

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		findAllViews(root);
		mHbUrlhandler = new HbUrlHandler(this, webView, null);
		if (!mHasInit) {
			mHasInit = true;
			mFontSizeType = AppFrame.getApp().getsF().getInt(ValConfig.SF_ARTICAL_FONTSIZE_TYPE, 0);
		}
		showProgress(true);
		try {
			loadWebViewData(bundle);
			String souce = mTitleLable;
			if (mLaunchResource == ValConfig.SOURCE_OTHER) {
				souce = "文章收藏";
			} else if (mLaunchResource == ValConfig.SOURCE_ADV) {
				souce = "广告图";
			} else if (mLaunchResource == ValConfig.SOURCE_PUSH) {
				souce = "推送";
			}
			Analytics.onEvent(getSherlockActivity(), Analytics.VIEW_NEWS_INFO, Analytics.KEY_FROM,
					souce);
		} catch (Exception e) {
			e.printStackTrace();
			if (getSherlockActivity() != null) {
				getSherlockActivity().finish();
			}
		}
	}

	private String getArticalUrl(String fileId) {
		String urltitle, end = ".html";
		if (mArticalType == ARTICLA_NEWS) {
			urltitle = "news/";
		} else if (mArticalType == ARTICLA_RESEARCH) {
			urltitle = "opinion/";
		} else if (mArticalType == ARTICLA_ISSUE) {
			urltitle = "ften/issue/detail/";
			end = ".htm";
		} else {
			urltitle = "ften/interview/detail/";
			end = ".htm";
		}
		if (LogUtils.mDebugUrl) {
			urltitle = ValConfig.URL_BASE_DEBUG_NEWS + urltitle + fileId + end;
		} else {
			urltitle = UrlUtils.buildUrl(urltitle + fileId + end);
		}
		d("getArticalUrl", urltitle);
		return urltitle;
	}

	@SuppressLint("NewApi")
	private void loadWebViewData(Bundle bundle) throws NumberFormatException {
		if (mTitleLable == null) {
			mTitleLable = getArguments().getString(ValConfig.IT_NAME);
		}
		Bundle arg = getArguments();
		mBean = arg.getParcelable(ValConfig.IT_ENTITY);
		mArticalType = arg.getInt(ValConfig.IT_TYPE);
		mLaunchResource = arg.getInt(ValConfig.IT_FROM);
		if (mBean == null) {
			mArticalId = arg.getString(ValConfig.IT_ID);
			mBean = new NewsItem();
			mBean.setId(Long.parseLong(mArticalId));
			mBean.setTitle(arg.getString(ValConfig.IT_URL));
			mBean.setNewsType(mArticalType);
			mBean.setPublishTime(System.currentTimeMillis());
			mBean.setTagName("");
			mBean.setLabel("");
		}
		if (mBean != null) {
			if (mLaunchResource == 0) {
				new ArticalTask(TASK_QUERY_COLLECT).execute(false);
			} else {
				getSherlockActivity().invalidateOptionsMenu();
			}
			if (!mJsFont) {
				setArticalTextSize(mFontSizeType, false);
			}
			mHomeUrl = getArticalUrl(mBean.getId() + "");
			webView.loadUrl(mHomeUrl);
			int netType = GlobalApp.getApp().getNetType();
			if (netType > 1 && netType < INetChanged.NET_TYPE_WIFI && LogUtils.mDebugLog) {
				loadHtml();
			}

		}
	}

	private void loadHtml() {
		new Thread() {
			public void run() {
				ParDefault p = new ParDefault(0);
				p.setUrl(mHomeUrl, null);
				p.setEnablePrefix(true, true);
				p.addFlag(ReqNetOpt.FLAG_OUT_STRING);
				ReqResult<ReqNetOpt> r = p.execute();
				if (r.isSuccess()) {
					LogUtils.startLog("artical"
							+ StrUtils.timeFormat(System.currentTimeMillis(), null));
					LogUtils.appendLog(r.mData + "");
					LogUtils.endLog();
				}
			};
		}.start();
	}

	private void findAllViews(View root) {
		webView = (WebView) root.findViewById(R.id.webview);
		layProgress = root.findViewById(R.id.lay_progress);
		webView.setBackgroundColor(0);
		webView.getSettings().setJavaScriptEnabled(true);
		// webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onShowCustomView(View view, CustomViewCallback callback) {
				super.onShowCustomView(view, callback);
				if (view instanceof FrameLayout) {
					FrameLayout frame = (FrameLayout) view;
					if (frame.getFocusedChild() instanceof VideoView) {
						VideoView video = (VideoView) frame.getFocusedChild();
						frame.removeView(video);
						View lay = getView();
						if (lay instanceof ViewGroup) {
							ViewGroup group = (ViewGroup) lay;
							group.removeAllViews();
							group.addView(video, -1, -1);
							video.start();
						}
					}
				}
			}
		});
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				if (mJsFont) {
					setArticalTextSize(mFontSizeType, false);
				}
				if (!mHasAlermView) {
					showProgress(false);
					getSherlockActivity().invalidateOptionsMenu();
				}
				d("onPageFinished", url);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (isImageUrl(url)) {
					showTouchImg(url);
					return true;
				} else {
					if (mHbUrlhandler.handWebUrl(url)) {
						return true;
					}
				}
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description,
					String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				if (GlobalApp.getApp().getNetType() <= 1 && failingUrl.equals(mHomeUrl)) {
					mHasAlermView = addRootView(FragArticalRead.this, R.layout.com_pop_netalerm,
							R.id.root_id);
					if (mHasAlermView) {
						webView.setVisibility(View.INVISIBLE);
						layProgress.setVisibility(View.GONE);
					}
				}
			}

		});
		webView.addJavascriptInterface(new JsMethod(), "JsMethod");
	}

	private boolean isImageUrl(String url) {
		int n = url.lastIndexOf(".");
		if (n != -1) {
			String type = url.substring(n + 1);
			if (!StrUtils.isEmpty(type)) {
				if (ShareHelper.isImage(type.trim().toLowerCase())) {
					return true;
				}
			}
		} else {
			if (url.startsWith("data:image/")) {
				return true;
			}
		}
		return false;
	}

	private void showTouchImg(String url) {
		Bundle b = new Bundle();
		b.putParcelable(ValConfig.IT_ENTITY, mBean);
		b.putString(ValConfig.IT_URL, url);
		Intent t = new Intent(getSherlockActivity(), AtyEmpty.class);
		t.putExtra(AtyEmpty.KEY_FRAG_NAME, FragTouchImage.class.getName());
		t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
		t.putExtra(AtyEmpty.KEY_ANIM_ENTER, 0);
		t.putExtra(AtyEmpty.KEY_ANIM_EXIT, R.anim.popup_scale_exit);
		startActivity(t);
		getSherlockActivity().overridePendingTransition(R.anim.popup_scale_enter, 0);
	}

	@Override
	public boolean onXmlBtClick(View v) {
		if (v.getId() == R.id.tv_net_setting) {
			NetToastUtils.launchNetSetting(getSherlockActivity());
			return true;
		}
		return super.onXmlBtClick(v);
	}

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_content_web;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_artical, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		MenuItem it = menu.findItem(R.id.menu_font);
		if (it != null) {
			int resid = mFontSizeType == 0 ? R.drawable.ic_font : R.drawable.ic_font_1;
			it.setIcon(resid);
		}
		if (mArticalType > 1 || mLaunchResource != 0) {
			menu.removeItem(R.id.menu_more);
		} else {
			it = menu.findItem(R.id.menu_collect);
			if (it != null && mBean != null) {
				int resid = R.string.menu_text_collect;
				if (mBean.hasFlag(NewsItem.ARTICAL_COLLECTED)) {
					resid = R.string.menu_text_uncollect;
				}
				it.setTitle(getString(resid));
			}
			setMeunEnable(
					menu,
					!(mHasAlermView || (layProgress != null && layProgress.getVisibility() == View.VISIBLE)));
		}
		super.onPrepareOptionsMenu(menu);
	}

	private void setMeunEnable(Menu menu, boolean enable) {
		MenuItem it = menu.findItem(R.id.menu_more);
		if (it != null) {
			if (it.isEnabled() != enable) {
				it.setEnabled(enable);
			}
		}

		it = menu.findItem(R.id.menu_share);
		if (it != null) {
			if (it.isEnabled() != enable) {
				it.setEnabled(enable);
			}
		}
		it = menu.findItem(R.id.menu_font);
		if (it != null) {
			if (it.isEnabled() != enable) {
				it.setEnabled(enable);
			}
		}
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean handled = true;
		switch (item.getItemId()) {
		case R.id.menu_share:
			webView.loadUrl("javascript:window.JsMethod.shareArtical(document.documentElement.innerText)");
			break;
		case R.id.menu_font:
			setArticalTextSize(1 - mFontSizeType, true);
			getSherlockActivity().invalidateOptionsMenu();
			break;
		case R.id.menu_collect:
			new ArticalTask(mBean.hasFlag(NewsItem.ARTICAL_COLLECTED) ? TASK_DEL_COLLECT
					: TASK_ADD_COLLECT).execute(false, item.getTitle() + "");
			break;
		default:
			handled = false;
		}
		return handled;
	}

	@SuppressLint("NewApi")
	private void setArticalTextSize(int fontSizeType, boolean saveFontSize) {
		mFontSizeType = fontSizeType;
		if (!mJsFont) {
			if (VERSION.SDK_INT >= 14) {
				webView.getSettings().setTextZoom(mFontlist[fontSizeType]);
			} else {
				if (fontSizeType == 0) {
					webView.getSettings().setTextSize(TextSize.NORMAL);
				} else {
					webView.getSettings().setTextSize(TextSize.LARGER);
				}
			}
		} else {
			int size = fontSizeType == 0 ? 17 : 22;
			webView.loadUrl("javascript:document.getElementsByTagName('div')[4].style.fontSize='"
					+ size + "px'");
		}
		if (saveFontSize) {
			Analytics.onEvent(getSherlockActivity(), Analytics.SWITCH_FONT_SIZE);
			AppFrame.getApp().getsF().edit()
					.putInt(ValConfig.SF_ARTICAL_FONTSIZE_TYPE, mFontSizeType).commit();
		}
	}

	class JsMethod {
		@JavascriptInterface
		public void shareArtical(final String text) {
			GlobalApp.getApp().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (!StrUtils.isEmpty(text)) {
						String[] reStrings = text.split("\n");
						mShareTitle = mBean.getTitle();
						if (StrUtils.isEmpty(mShareTitle)) {
							mShareTitle = reStrings[0];
						}
						mShareContent = (reStrings[2].length() > 70 ? reStrings[2].substring(0, 70)
								+ "..." : reStrings[2]);

						mShareUrl = getArticalUrl(mBean != null ? String.valueOf(mBean.getId())
								: mArticalId);
						ShareHelper.showSharePicker(getSherlockActivity(), mDlgClickListener);
					}
				}
			}, 0);
		}
	}

	class ArticalTask extends AsyPoolTask<String, Void, ReqResult<ReqOpt>> {
		private int mTaskType = 0;

		public ArticalTask(int taskType) {
			mTaskType = taskType;
		}

		private String buildQuerySql(StringBuilder sb) {
			sb.append("select state from tb_common where key='");
			sb.append("artical_collect").append("' and subkey='").append(mBean.getId())
					.append("' and state=").append(mArticalType);
			return sb.toString();
		}

		private SqlExeObj buildAddSql(StringBuilder sb) {
			sb.append("insert into tb_common values(?,'artical_collect',?,?,?,?)");
			return new SqlExeObj(sb.toString(), new Object[] { null, mBean.getId(),
					mBean.toNews(mArticalType).toByteArray(), mArticalType,
					System.currentTimeMillis() });
		}

		private String buildDelSql(StringBuilder sb) {
			sb.append("delete from tb_common where key='");
			sb.append("artical_collect").append("' and subkey='").append(mBean.getId())
					.append("' and state=").append(mArticalType);
			return sb.toString();
		}

		@Override
		protected ReqResult<ReqOpt> doInBackground(String... p) {
			ReqResult<ReqOpt> r = new ReqResult<ReqOpt>(new ReqOpt(0, null, mTaskType));
			try {
				StringBuilder sb = new StringBuilder(64);
				WrapException err = null;
				if (mTaskType == TASK_QUERY_COLLECT) {
					Cursor c = DbUtils.query(buildQuerySql(sb), null, false);
					if (null != c && c.moveToFirst()) {
						mBean.addFlag(NewsItem.ARTICAL_COLLECTED);
					} else {
						mBean.subFlag(NewsItem.ARTICAL_COLLECTED);
					}
					if (c != null && !c.isClosed()) {
						c.close();
					}
				} else if (mTaskType == TASK_ADD_COLLECT) {
					err = DbUtils.exeSql(buildAddSql(sb), true);
					if (err != null) {
						throw err;
					}
					mBean.addFlag(NewsItem.ARTICAL_COLLECTED);
				} else {
					err = DbUtils.exeSql(new SqlExeObj(buildDelSql(sb)), true);
					if (err != null) {
						throw err;
					}
					mBean.subFlag(NewsItem.ARTICAL_COLLECTED);
				}
				r.setData(p == null || p.length == 0 ? null : p[0]);
			} catch (Exception e) {
				r.setErr(WrapException.wrap(e, null));
			}
			return r;
		}

		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(ReqResult<ReqOpt> r) {
			if (r.isSuccess()) {
				getSherlockActivity().invalidateOptionsMenu();
				if (mTaskType != TASK_QUERY_COLLECT) {
					if (mBean.hasFlag(NewsItem.ARTICAL_COLLECTED)) {
						Analytics.onEvent(getSherlockActivity(), Analytics.ADD_FAVORITE_NEWS);
					} else {
						Analytics.onEvent(getSherlockActivity(), Analytics.DELETE_FAVORITE_NEWS);
					}
					pop("成功" + r.mData, false);
				}
			}
		}

	}

	private String mShareTitle = null;
	private String mShareContent = null;
	private String mShareUrl = null;
	private int mShareType = -1;
	DialogInterface.OnClickListener mDlgClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			mShareType = which;
			String title = mShareTitle;
			String content = mShareContent;
			String contentUrl = mShareUrl;
			Object bmp = R.drawable.my_start;
			switch (which) {
			case 0:
				break;
			case 1:
				title = content;
				break;
			case 2:
				bmp = null;
				content = "【" + title + "】" + content + " " + contentUrl;
				break;
			default:
				content = "【" + title + "】" + content + " " + contentUrl;
			}
			ShareHelper.share(getSherlockActivity(), which, title, content, contentUrl, bmp,
					mListener, "新闻");
		}
	};
	private SnsPostListener mListener = new SnsPostListener() {
		@Override
		public void onStart() {
		}

		@Override
		public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
			if (mShareType == 0) {
				if (eCode == StatusCode.ST_CODE_SUCCESSED) {
					pop("分享到 " + ShareHelper.getSharePlateform().get(mShareType) + "成功", false);
				} else {
					if (eCode != StatusCode.ST_CODE_ERROR_CANCEL) {
						pop("分享到 " + ShareHelper.getSharePlateform().get(mShareType) + "失败", false);
					}
				}
			}
		}
	};

	@Override
	public boolean onKeyBack(boolean fromBar, boolean isFirstPress, boolean isTwiceInTime) {
		if (webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		if (fromBar) {
			Bundle b = new Bundle();
			b.putString(ValConfig.IT_NAME, getString(R.string.tb_title_infos));
			Intent two = new Intent(getSherlockActivity(), AtyEmptyTab.class);
			two.putExtra(AtyEmpty.KEY_FRAG_NAME, FragTbInfos.class.getName());
			two.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
			Intent one = new Intent(getSherlockActivity(), AtyTbMain.class);
			one.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			two.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			if (AtyInfs.hasAty(AtyTbMain.class, null) == null) {
				TaskStackBuilder.create(getSherlockActivity()).addNextIntent(one)
						.addNextIntent(two).startActivities();
				getSherlockActivity().overridePendingTransition(0, 0);
			} else {
				if (mLaunchResource == ValConfig.SOURCE_PUSH) {
					NavUtils.navigateUpTo(getSherlockActivity(), two);
				} else {
					return super.onKeyBack(fromBar, isFirstPress, isTwiceInTime);
				}
			}
			return true;
		} else {
			// AtyInfs atyInf = AtyInfs.hasAty(AtyTbMain.class, null);
		}
		return super.onKeyBack(fromBar, isFirstPress, isTwiceInTime);
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
							showProgress(true);
						}
					}
					webView.reload();
				}
			}
			return true;
		}
		return super.onNetChanged(netType, preNet);
	}

	private void showProgress(boolean show) {
		if (show) {
			if (layProgress.getVisibility() != View.VISIBLE) {
				layProgress.setVisibility(View.VISIBLE);
			}
			if (webView.getVisibility() == View.VISIBLE) {
				webView.setVisibility(View.INVISIBLE);
			}
		} else {
			if (layProgress.getVisibility() == View.VISIBLE) {
				layProgress.setVisibility(View.GONE);
			}
			if (webView.getVisibility() != View.VISIBLE) {
				webView.setVisibility(View.VISIBLE);
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
		webView.destroy();
		mHbUrlhandler.destory();
	}
}
