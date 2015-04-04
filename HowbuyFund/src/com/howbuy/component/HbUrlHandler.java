package com.howbuy.component;

import howbuy.android.palmfund.R;

import java.util.HashMap;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;

import com.howbuy.aty.AtyEmpty;
import com.howbuy.config.ValConfig;
import com.howbuy.frag.FragArticalRead;
import com.howbuy.frag.FragDetailsFund;
import com.howbuy.frag.FragDetailsTrust;
import com.howbuy.frag.FragSearch;
import com.howbuy.frag.FragTbTrade;
import com.howbuy.lib.frag.AbsFrag;
import com.howbuy.lib.utils.SysUtils;
import com.howbuy.utils.FundUtils;
import com.howbuy.utils.ShareHelper;
import com.howbuy.utils.UrlParser;
import com.howbuy.utils.UrlParser.IHbParser;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;

public class HbUrlHandler implements IHbParser, SnsPostListener {
	WebView mWebView;
	AbsFrag mFragment;
	IHbParser mUrlPaser;

	public HbUrlHandler(AbsFrag frag, WebView webView, IHbParser paser) {
		this.mWebView = webView;
		this.mFragment = frag;
		this.mUrlPaser = paser;
	}

	public void destory() {
		mFragment = null;
		mWebView = null;
		mUrlPaser = null;
	}

	public boolean handWebUrl(String url) {
		boolean handled = false;
		if (url.endsWith(".Apk") || url.endsWith("apk") || url.contains("action=openOrDownloadApp")) {
			Uri u = Uri.parse(url);
			String scheme = u.getScheme();
			handled = mUrlPaser != null && mUrlPaser.handSysUri(u, scheme);
			return handled ? true : handSysUri(u, scheme);
		} else {
			handled = mUrlPaser != null && UrlParser.handleUrl(mUrlPaser, url);
			return handled ? true : UrlParser.handleUrl(this, url);
		}
	}

	@Override
	public boolean handSysUri(Uri uri, String scheme) {
		Intent it = new Intent();
		it.setAction("android.intent.action.VIEW");
		it.setData(uri);
		if (SysUtils.intentSafe(mFragment.getSherlockActivity(), it)) {
			mFragment.startActivity(it);
			return true;
		}
		return false;
	}

	@Override
	public void handHbUri(String author, HashMap<String, String> args, String jsCalback) {
		if ("share".equals(author)) {
			handShare(args, jsCalback);
		} else if ("fundDetail".equals(author)) {
			handFundView(false, args.get("id"));
		} else if ("trustDetail".equals(author)) {
			handFundView(true, args.get("id"));
		} else if ("trade".equals(author)) {
			handTrade(args.get("action"), args.get("id"));
		} else if ("news".equals(author) || "newsList".equals(author)) {
			handInfoView(args.get("type"), args.get("id"));
		} else if ("login".equals(author)) {
			// hb://login?actionId=actionId&cb=callbackMethodName
		} else if ("bindCard".equals(author)) {
			// hb://bindCard?actionId=actionId&cb=callbackMethodName
		} else if ("queryUserinfo".equals(author)) {
			// hb://queryUserinfo?id=5768&param=jsonString&needLogin=1&cb=callbackMethodName&
			// actionId=actionId
		} else if ("searchFund".equals(author)) {
			handSearch(args.get("type"));
		}
	}

	private void handSearch(String type) {
		int arg = 0;
		if ("private".equals(type)) {
			arg = FragSearch.Type_Intent_Simu;
		} else if ("open".equals(type)) {
			arg = FragSearch.Type_Intent_Fund;
		}
		FundUtils.launchFundSearch(mFragment, arg, 0);
	}

	private void handTrade(String action, String id) {
		String url = ValConfig.URL_TRADE_BASEURL_RELEASE;
		if ("rg".equals(action)) {
			url += ValConfig.URL_Trade_RENGOU + id;
		} else if ("sg".equals(action)) {
			url += ValConfig.URL_Trade_SHENGOU + id;
		} else if ("sh".equals(action)) {
			url += ValConfig.URL_Trade_SHUHUI + id;
		}
		Bundle b = new Bundle();
		b.putString(ValConfig.IT_URL, url);
		Intent t = new Intent(mFragment.getSherlockActivity(), AtyEmpty.class);
		t.putExtra(AtyEmpty.KEY_FRAG_NAME, FragTbTrade.class.getName());
		t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
		t.putExtra(AtyEmpty.KEY_ANIM_ENTER, R.anim.push_up_none);
		t.putExtra(AtyEmpty.KEY_ANIM_EXIT, R.anim.push_up_out);
		mFragment.startActivity(t);
		mFragment.getSherlockActivity().overridePendingTransition(R.anim.push_up_in,
				R.anim.push_up_none);

	}

	private void handInfoView(String type, String id) {
		Bundle b = new Bundle();
		b.putString(ValConfig.IT_ID, id);
		if ("opinion".equals(type)) {
			b.putString(ValConfig.IT_NAME, "研究报告");
			b.putInt(ValConfig.IT_TYPE, 1);
		} else {
			b.putString(ValConfig.IT_NAME, "新闻资讯");
			b.putInt(ValConfig.IT_TYPE, 0);
		}
		b.putInt(ValConfig.IT_FROM, ValConfig.SOURCE_OTHER);
		Intent t = new Intent(mFragment.getSherlockActivity(), AtyEmpty.class);
		t.putExtra(AtyEmpty.KEY_FRAG_NAME, FragArticalRead.class.getName());
		t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
		mFragment.startActivity(t);
	}

	private void handFundView(boolean isXintuo, String id) {
		Intent t = new Intent(mFragment.getSherlockActivity(), AtyEmpty.class);
		Bundle b = new Bundle();
		b.putString(ValConfig.IT_ID, id);
		b.putString(ValConfig.IT_NAME, isXintuo ? "信托详情" : "基金详情");
		b.putInt(ValConfig.IT_FROM, ValConfig.SOURCE_OTHER);
		t.putExtra(AtyEmpty.KEY_FRAG_NAME, isXintuo ? FragDetailsTrust.class.getName()
				: FragDetailsFund.class.getName());
		t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
		mFragment.startActivity(t);
	}

	private void handShare(final HashMap<String, String> args, final String jsCalback) {
		ShareHelper.showSharePicker(mFragment.getSherlockActivity(),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String title = args.get("title");
						String content = args.get("detail");
						String contentUrl = args.get("url");
						String img = args.get("img");
						String actionId = args.get("actionId");
						Object bmp = R.drawable.my_start;
						switch (which) {
						case 0:// weixin
							break;
						case 1:// circle
							title = content;
							break;
						case 2:// weibo.
							bmp = img;
							content = "【" + title + "】" + content + " " + contentUrl;
							break;
						default:
							content = "【" + title + "】" + content + " " + contentUrl;
						}
						ShareHelper.share(mFragment.getSherlockActivity(), which, title, content,
								contentUrl, bmp, HbUrlHandler.this, "通用WEB");
					}
				});
	}

	@Override
	public void onComplete(SHARE_MEDIA arg0, int arg1, SocializeEntity arg2) {

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub

	}
}
