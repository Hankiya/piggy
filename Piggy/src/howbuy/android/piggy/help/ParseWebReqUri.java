package howbuy.android.piggy.help;

import howbuy.android.piggy.UserUtil;
import howbuy.android.piggy.ui.BindCardActivity;
import howbuy.android.piggy.ui.LoginActivity;
import howbuy.android.piggy.ui.SaveMoneyActivity;
import howbuy.android.util.Cons;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

public class ParseWebReqUri {
	public static int mCurrFlag=-1;
	
	public static final class Web_Flag {
	
		public static final int _flagActive =1<<10;
		public static final int _flagRegister =1<<11;
		public static final int _flagBindCard =1<<12;
		public static final int _flagSaveMoney =1<<13;
		public static final int _flagGetUserInfo =1<<14;
		public static final int _flagLogin =1<<15;
		
		public static final int Flag_login=_flagLogin|_flagActive|_flagRegister;//登录
		public static final int Flag_BindCard=_flagBindCard|Flag_login;//绑卡
		public static final int Flag_SaveMoney=_flagSaveMoney|Flag_BindCard;//存钱
		public static final int Flag_UserINfo =_flagGetUserInfo|Flag_login;//获取用户信息
		
	}

	public static final String NAME = "ParseWebReqUri";
	public static final String URI_MAIN = "URI_MAIN";
	public static final String URI_BODY = "URI_BODY";
	public static final String Type_Login = "login";
	public static final String Type_BdCard = "bindCard";
	public static final String Type_QUserIfo = "queryUserinfo";
	public static final String Type_Trade = "trade";
	public static final String Type_Share = "share";
	public static final String Type_Test = "waptest";

	public static Map<String, String> parseHBUrl(String parseUrl) {
		Map<String, String> map = new HashMap<String, String>();
		if (!TextUtils.isEmpty(parseUrl)) {
			try {
				String targetUrl = URLDecoder.decode(parseUrl, "UTF-8");
				handleUriAddress(map, targetUrl);
				String keyValue = map.get(URI_BODY);
				if (!TextUtils.isEmpty(keyValue)) {
					parseKeyValue(map, keyValue);
				}
				map.remove(URI_BODY);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Log.d(NAME, map.toString());
		return map;
	}

	public static Map<String, String> parseKeyValue(Map<String, String> resMap, String keyValues) {
		String[] keyvalueArry = keyValues.split("&");
		for (String kv : keyvalueArry) {
			int index = kv.indexOf("=");
			String key = kv.substring(0, index);
			String value = kv.substring(index + 1, kv.length());
			resMap.put(key, value);
		}
		return resMap;
	}

	public static Map<String, String> handleUriAddress(Map<String, String> resMap, String uri) {
		int indexStart = uri.indexOf("://");
		int indexEnd = uri.indexOf("?");
		if (indexEnd == -1) {
			indexEnd = uri.length();
		}
		String uriMain = uri.substring(indexStart + 3, indexEnd);
		resMap.put(URI_MAIN, uriMain);

		if (!(indexEnd == uri.length())) {
			String uriBody = uri.substring(indexEnd + 1, uri.length());
			resMap.put(URI_BODY, uriBody);
		}
		return resMap;
	}

	public void dispatchWebEvent(Activity context,String key, Map<String, String> params, String cb) {
		if (TextUtils.isEmpty(key)) {
			return;
		}
		if (Type_Login.equals(key)) {
			mCurrFlag=Web_Flag.Flag_login;
			handLogin(context,key, params, cb);
		} else if (Type_BdCard.equals(key)) {
			mCurrFlag=Web_Flag.Flag_BindCard;
			handBindCard(context,key, params, cb);
		} else if (Type_Trade.equals(key)) {
			mCurrFlag=Web_Flag.Flag_SaveMoney;
			handTrade(context,key, params, cb);
		} else if (Type_Share.equals(key)) {
			mCurrFlag=Web_Flag.Flag_SaveMoney;
			handShare(context,key, params, cb);
		} else if (Type_QUserIfo.equals(key)) {
			mCurrFlag=Web_Flag.Flag_UserINfo;
			handQuserInfo(context,key, params, cb);
		}
	}

	public static void handLogin(Activity context,String key, Map<String, String> params, String cb) {
		Intent i=new Intent(context,LoginActivity.class);
		i.putExtra(Cons.Intent_type, Web_Flag.Flag_login);
		context.startActivityForResult(i, 0);
	}

	public static void handBindCard(Activity context,String key, Map<String, String> params, String cb) {
		if (UserUtil.isLogin()) {
			Intent i=new Intent(context,BindCardActivity.class);
			i.putExtra(Cons.Intent_type, Web_Flag.Flag_BindCard);
			context.startActivityForResult(i, 0);
		}
	}

	public static void handTrade(Activity context,String key, Map<String, String> params, String cb) {
		Intent i=new Intent();
		i.putExtra(Cons.Intent_type, Web_Flag.Flag_SaveMoney);
		String action = params.get("action");
		if (!TextUtils.isEmpty(action)) {
			if (action.equals("sg")) {
				if (UserUtil.isLogin()) {
					if (UserUtil.isBindBank()) {
						i.setClass(context, SaveMoneyActivity.class);
					}else {
						i.setClass(context, BindCardActivity.class);
					}
				}else {
					i.setClass(context, LoginActivity.class);
				}
				context.startActivityForResult(i, 0);
			}
		}
	}

	public static void handShare(Context context,String key, Map<String, String> params, String cb) {
		
	}

	public static void handQuserInfo(Context context,String key, Map<String, String> params, String cb) {
		Intent i=new Intent();
		i.putExtra(Cons.Intent_type, Web_Flag.Flag_UserINfo);
		if (UserUtil.isLogin()) {
			
		}else {
			i.setClass(context, LoginActivity.class);
		}
		
	}
	
	public static void jumpWebPage(Context mContext,int res,int type){
		if (mContext instanceof Activity) {
			((Activity)mContext).finish();
		}
	}

}
