package com.howbuy.control;

import howbuy.android.palmfund.R;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.howbuy.component.AppFrame;
import com.howbuy.component.CardDrawable;
import com.howbuy.config.ValConfig;
import com.howbuy.entity.StockInf;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.compont.GlobalServiceMger;
import com.howbuy.lib.compont.GlobalServiceMger.ScheduleTask;
import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.interfaces.IStreamParse;
import com.howbuy.lib.interfaces.ITimerListener;
import com.howbuy.lib.net.AsyPoolTask;
import com.howbuy.lib.net.ParDefault;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.FileUtils;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.PathUtils;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.lib.utils.SysUtils;
import com.howbuy.lib.utils.ViewUtils;

public class HomeStockLayout extends com.actionbarsherlock.internal.widget.IcsLinearLayout
		implements ITimerListener, IStreamParse, IReqNetFinished {
	private static String DEF_STOCK_URL = "file:///android_asset/html/stock.html";
	private static String STOCK_FILENAME = "stock.html";
	private static String SF_KEY_STOCK = "stock_file_path";
	private static String STOCK_CACHE_FILENAME = "stock_index_inf_.data";
	public static String JS_LOAD_URL = null;
	private TextView mTvName1, mTvName2, mTvName3;
	private TextView mTvIncrease1, mTvIncrease2, mTvIncrease3;
	private ImageView mIvState1, mIvState2, mIvState3;
	private NumBitView mBitView1, mBitView2, mBitView3;
	private WebView mWebView = null;
	private View mLayProgress = null;
	private StockRun mRunStock = null;
	private String mStockTxt = null;

	// 根据版本检测是否需要更新JS,如果更新下载到sd目录后保存文件路径,下次加载这个保存的文件路径,如文件不存在用默认的.
	public HomeStockLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		CardDrawable cd = new CardDrawable(0xffffffff);
		cd.setShadeWidth(0, 0, 0, 2 * SysUtils.getDensity(getContext()));
		ViewUtils.setBackground(this, cd);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mTvName1 = (TextView) findViewById(R.id.tv_stock_name1);
		mTvName2 = (TextView) findViewById(R.id.tv_stock_name2);
		mTvName3 = (TextView) findViewById(R.id.tv_stock_name3);
		mTvIncrease1 = (TextView) findViewById(R.id.tv_stock_increase1);
		mTvIncrease2 = (TextView) findViewById(R.id.tv_stock_increase2);
		mTvIncrease3 = (TextView) findViewById(R.id.tv_stock_increase3);
		mIvState1 = (ImageView) findViewById(R.id.iv_stock_state1);
		mIvState2 = (ImageView) findViewById(R.id.iv_stock_state2);
		mIvState3 = (ImageView) findViewById(R.id.iv_stock_state3);
		mIvState1.setImageLevel(3);
		mIvState2.setImageLevel(3);
		mIvState3.setImageLevel(3);
		mBitView1 = (NumBitView) findViewById(R.id.tv_stock_value1);
		mBitView2 = (NumBitView) findViewById(R.id.tv_stock_value2);
		mBitView3 = (NumBitView) findViewById(R.id.tv_stock_value3);
		mBitView1.setTextGravity(Gravity.LEFT);
		mBitView2.setTextGravity(Gravity.LEFT);
		mBitView3.setTextGravity(Gravity.LEFT);
		mBitView1.setSlidMode(NumBitView.SLID_DIF_BIT_ONLY | NumBitView.SLID_ALL_IF_BITNUM_DIF);
		mBitView2.setSlidMode(NumBitView.SLID_DIF_BIT_ONLY | NumBitView.SLID_ALL_IF_BITNUM_DIF);
		mBitView3.setSlidMode(NumBitView.SLID_DIF_BIT_ONLY | NumBitView.SLID_ALL_IF_BITNUM_DIF);
		mBitView1.setAnimMask(false);
		mBitView2.setAnimMask(false);
		mBitView3.setAnimMask(false);
		initWebView();
		loadWebView();
		saveOrLoadStockinf(false);
	}

	private void initStockData(NumBitView numView, TextView tvName, TextView tvIncrease,
			StockInf inf, ImageView a) {
		float fi = Float.parseFloat(inf.StockIncrease);
		int level = 3;
		if (fi > 0) {
			numView.setTextColor(0xffcc0000);
			tvIncrease.setTextColor(0xffcc0000);
			level = 2;
		} else if (fi < 0) {
			numView.setTextColor(0xff009900);
			tvIncrease.setTextColor(0xff009900);
			level = 1;
		} else {
			numView.setTextColor(0xff000000);
			tvIncrease.setTextColor(0xff000000);
		}
		a.setImageLevel(level);
		if (level < 3) {
			ViewUtils.setVisibility(a, View.VISIBLE);
		} else {
			ViewUtils.setVisibility(a, View.GONE);
		}
		tvName.setText(inf.StockName);
		float val = Float.parseFloat(inf.StockValue);
		if (!numView.setCurrentNum(val, 600)) {
			numView.invalidate();
		}
		String value = inf.StockIncrease, percent = inf.StockPercent;
		if (value.startsWith("-")) {
			value = value.substring(1);
		}
		if (percent.startsWith("-")) {
			percent = percent.substring(1);
		}
		tvIncrease.setText(String.format("%1$s  %2$s", value, percent));

	}

	private void initWebView() {
		mWebView = new WebView(getContext());
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setAllowFileAccess(true);
		mWebView.addJavascriptInterface(new JsMethod(), "JsMethod");
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress >= 100) {
					showProgress(false);
				}
			}

			/*
			 * public void onConsoleMessage(String message, int lineNumber,
			 * String sourceID) { Log.d("stocks", message + " -- From line " +
			 * lineNumber + " of " + sourceID); }
			 */

		});
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("hb://")) {
					return true;
				} else {
					return false;
				}
			}
		});
		// addView(mWebView);//测试webview内部定时刷新.
	}

	private void loadWebView() {
		showProgress(true);
		String url = AppFrame.getApp().getsF().getString(SF_KEY_STOCK, null);
		if (url == null) {
			url = PathUtils.buildPath(PathUtils.PATH_ANDR_FILE, STOCK_FILENAME, true);
			if (new File(url).exists()) {
				url = "file://" + url;
			} else {
				url = DEF_STOCK_URL;
			}
		}
		mWebView.loadUrl(url);
		postDelayed(new Runnable() {
			@Override
			public void run() {
				loadJsIfNeeded();
			}
		}, 5000);
	}

	public void refush(boolean force) {
		if (force) {
			showProgress(true);
		}
		mWebView.reload();

	}

	private void loadJsIfNeeded() {
		if (JS_LOAD_URL != null) {
			String url = null;
			int indexUrl = JS_LOAD_URL.indexOf("##");
			if (indexUrl != -1) {
				url = JS_LOAD_URL.substring(indexUrl + 2);
			}
			if (!StrUtils.isEmpty(url)) {
				ParDefault par = new ParDefault(0).setUrl(url, null);
				par.setEnablePrefix(true, false);
				par.getReqOpt(false).setPareser(this);
				par.setCallback(1, this).execute();
			}
		}
	}

	public void setProgress(View v) {
		mLayProgress = v;
	}

	public void showProgress(boolean show) {
		if (mLayProgress != null) {
			if (show) {
				if (mLayProgress.getVisibility() != View.VISIBLE) {
					mLayProgress.setVisibility(View.VISIBLE);
				}
			} else {
				if (mLayProgress.getVisibility() == View.VISIBLE) {
					mLayProgress.setVisibility(View.GONE);
				}
			}
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		saveOrLoadStockinf(true);
	}

	private void onRefushCompleted(List<StockInf> r, WrapException err, boolean fromCache) {
		if (err == null && r != null) {
			initStockData(mBitView1, mTvName1, mTvIncrease1, r.get(0), mIvState1);
			initStockData(mBitView2, mTvName2, mTvIncrease2, r.get(1), mIvState2);
			initStockData(mBitView3, mTvName3, mTvIncrease3, r.get(2), mIvState3);
		} else {
			LogUtils.d("stocks", "err > r=" + r + " err=" + err);
		}
		showProgress(false);
	}

	public void toggleTimer(int duration) {
		GlobalServiceMger mger = GlobalApp.getApp().getGlobalServiceMger();
		if (duration > 0) {
			ScheduleTask task = new ScheduleTask(1, this);
			task.setExecuteArg(duration, 0, false);
			mger.addTimerListener(task);
		} else {
			mger.removeTimerListener(1, this);
		}

	}

	@Override
	public void onTimerRun(int which, int timerState, boolean hasTask, int timesOrSize) {
		if (timerState == ITimerListener.TIMER_SCHEDULE) {
			refush(false);
		}
	}

	class JsMethod {
		@JavascriptInterface
		public void pickStockInf(final String text) {
			ArrayList<StockInf> r = null;
			WrapException err = null;
			if (!StrUtils.isEmpty(text)) {
				try {
					r = StockInf.parseStock(text);
					mStockTxt = text;
				} catch (JSONException e) {
					e.printStackTrace();
					err = WrapException.wrap(e, "parse stock json err.");
				}
			} else {
				err = new WrapException("stock inner text is empty", null);
			}
			if (mRunStock == null) {
				mRunStock = new StockRun();
			}
			GlobalApp.getApp().runOnUiThread(mRunStock.setData(r, err), 0);
		}
	}

	public class StockRun implements Runnable {
		ArrayList<StockInf> mResult = null;
		WrapException mErr = null;

		public StockRun setData(ArrayList<StockInf> r, WrapException err) {
			mResult = r;
			mErr = err;
			return this;
		}

		public void run() {
			onRefushCompleted(mResult, mErr, false);
		}
	}

	private void saveOrLoadStockinf(boolean save) {
		new StcokInfTask().execute(true, save ? StcokInfTask.SAVE : StcokInfTask.LOAD);
	}

	class StcokInfTask extends AsyPoolTask<Integer, Void, ReqResult<ReqOpt>> {
		public static final int SAVE = 1, LOAD = 2;

		public StcokInfTask() {
		}

		@Override
		protected ReqResult<ReqOpt> doInBackground(Integer... p) {
			ReqResult<ReqOpt> r = new ReqResult<ReqOpt>(new ReqOpt(0, STOCK_CACHE_FILENAME, p[0]));
			try {
				File f = new File(PathUtils.buildPath(PathUtils.PATH_PK_FILE, STOCK_CACHE_FILENAME,
						true));
				if (p[0] == SAVE) {
					if (!StrUtils.isEmpty(mStockTxt)) {
						FileUtils.saveFile(mStockTxt, f, false);
						r.setData(null);
					} else {
						r.setErr(null);
					}

				} else {
					if (f.exists()) {
						r.setData(StockInf.parseStock(FileUtils.getFileString(f)));
					} else {
						r.setErr(new WrapException("file " + f + " is not exist",
								"task load cache stockinf failed"));
					}
				}
			} catch (Exception e) {
				r.setErr(WrapException.wrap(e, "task type=" + p[0] + " execute err ."));
			}
			return r;
		}

		@Override
		protected void onPostExecute(ReqResult<ReqOpt> result) {
			if (LOAD == result.mReqOpt.getHandleType()) {
				if (result.isSuccess()) {
					onRefushCompleted((List<StockInf>) result.mData, null, true);
				} else {
					onRefushCompleted(null, result.mErr, true);
				}
			} else {

			}
		}
	}

	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
		try {
			File f = new File(PathUtils.buildPath(PathUtils.PATH_ANDR_FILE, STOCK_FILENAME, true));
			FileUtils.saveFile(is, f, false);
			if (f.exists()) {
				return f.getAbsolutePath();
			} else {
				throw new NotFoundException(f.getAbsolutePath());
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void onRepNetFinished(ReqResult<ReqNetOpt> r) {
		if (r.isSuccess()) {
			String url = (String) r.mData;
			if (url != null && JS_LOAD_URL != null) {
				String version = null;
				int urlIndex = JS_LOAD_URL.indexOf("##");
				if (urlIndex > 0) {
					version = JS_LOAD_URL.substring(0, urlIndex);
				}
				mWebView.loadUrl("file://" + url);
				AppFrame.getApp().getsF().edit().putString(SF_KEY_STOCK, url)
						.putString(ValConfig.SFVerJsVer, version).commit();
				JS_LOAD_URL = null;
			}
		} else {
		}

	}

}
