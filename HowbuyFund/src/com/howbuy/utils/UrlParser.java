package com.howbuy.utils;

import java.util.HashMap;

import android.net.Uri;

import com.howbuy.lib.utils.StrUtils;

public class UrlParser {
	public static boolean handleUrl(IHbParser l, String url) {
		Uri uri = null;
		if (!StrUtils.isEmpty(url)) {
			uri = Uri.parse(url);
		}
		return uri == null ? false : handleUrl(l, uri);
	}

	private static boolean handleUrl(IHbParser l, Uri uri) {
		String scheme = uri.getScheme();
		if (!StrUtils.isEmpty(scheme)) {
			scheme = scheme.trim().toLowerCase();
			if (!(scheme.startsWith("http"))) {
				if ("hb".startsWith(scheme)) {
					String auth = uri.getAuthority();
					if (!StrUtils.isEmpty(auth)) {
						auth = auth.trim();
						HashMap<String, String> arg = getArgs(uri.getQuery());
						String calback = arg.get("cb");
						if (calback != null) {
							arg.remove("cb");
						}
						if (l != null) {
							l.handHbUri(auth, arg, calback);
						}
					}
					return true;
				} else {
					return (l != null && l.handSysUri(uri, scheme));
				}
			}
		}
		return false;
	}

	private static HashMap<String, String> getArgs(String qurey) {
		HashMap<String, String> map = new HashMap<String, String>();
		if (qurey != null && qurey.contains("=")) {
			String[] pairs = qurey.split("&");
			if (pairs != null && pairs.length > 0) {
				for (String pair : pairs) {
					String[] param = pair.split("=", 2);
					if (param != null && param.length == 2) {
						map.put(param[0], param[1]);
					}
				}
			}
		}
		return map;
	}

	public interface IHbParser {
		boolean handSysUri(Uri uri, String scheme);

		void handHbUri(String author, HashMap<String, String> args, String jsCalback);
	}
}
