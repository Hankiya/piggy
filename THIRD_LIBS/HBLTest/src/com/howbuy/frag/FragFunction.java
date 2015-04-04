package com.howbuy.frag;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.howbuy.aty.AtySecret;
import com.howbuy.entity.Function;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.frag.AbsFrag;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.lib.utils.SysUtils;
import com.howbuy.libtest.R;
import com.howbuy.test.CommonLibTest;

public class FragFunction extends AbsFrag implements IReqNetFinished, OnClickListener,
		OnCheckedChangeListener {
	private Function mFunction = null;
	private HashMap<String, String> mParams = new HashMap<String, String>();
	private HashMap<String, EditText> mEdits = new HashMap<String, EditText>();
	private TextView mTvDescribe, mTvReturn, mTvUrl, mTvParam, mTvResult, mTvExtras, mTvDate,
			mTvTime;
	private ViewGroup mLayEdits;

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_function;
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		mTvDescribe = (TextView) root.findViewById(R.id.tv_describe);
		mTvReturn = (TextView) root.findViewById(R.id.tv_return);
		mTvUrl = (TextView) root.findViewById(R.id.tv_url);
		mTvParam = (TextView) root.findViewById(R.id.tv_params);
		mTvResult = (TextView) root.findViewById(R.id.tv_result);
		mTvExtras = (TextView) root.findViewById(R.id.tv_extras);
		mLayEdits = (ViewGroup) root.findViewById(R.id.lay_params_edit);
		mLayEdits.setVisibility(View.INVISIBLE);

		try {
			Bundle arg = getArguments();
			mFunction = arg.getParcelable("arg_function");
			loadDefParams(getSherlockActivity().getAssets().open(arg.getString("arg_params")),
					mFunction.getName());
			initNecessaryViews();
			buildParamsEdits();
			if (mParams.size() == 0) {
				invokeFunction();
			} else {
				toggleParamsEdits();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initNecessaryViews() {
		if (mFunction != null) {
			getSherlockActivity().setTitle(mFunction.getName());
			mTvDescribe.setText("功能:" + mFunction.getDescription());
			mTvReturn.setText("返回:" + mFunction.getReturn());
			mTvUrl.setText("地址:" + mFunction.getUrl());
			mTvParam.setText("params:\n" + mFunction.getParams());
			if (mParams.size() > 0) {
				mTvParam.setText("当前参数:\r\n" + mParams.toString());
			} else {
				mTvParam.setText("");
			}
		}
	}

	private void initFunctionResult(ReqResult<ReqNetOpt> r) {
		if (r == null) {
			mTvResult.setText("执行" + mFunction.getName() + "方法失败.");
			mTvExtras.setText(null);
		} else {
			if (r.isSuccess()) {
				String result = encodeObj(r.mData);
				// LogUtils.printMsg("function", result);
				mTvResult.setText("\r\n执行成功,数据返回结果如下:\n" + result);
			} else {
				mTvResult.setText("\r\n执行失败,错误信息如下:\n" + r.mErr);
			}
			mTvExtras.setText("\n请求信息如下:\n" + r.mReqOpt);
			if (r.mReqOpt.hasFlag(ReqNetOpt.FLAG_PUBLIC_PARAMS)) {
				mTvExtras.append("\r\n公共参数:\r\n" + GlobalApp.getApp().getMapStr());
			}
			try {
				mTvExtras.append("\n\t完整地址\n\t");
				mTvExtras.append(r.mReqOpt.getUrlPath());
				d("initFunctionResult", "data:" + r.toShortString(true, false));
			} catch (Exception e) {

			}
		}
	}

	private String encodeObj(Object obj) {
		if (obj instanceof com.google.protobuf.GeneratedMessage) {
			StringBuffer sb = new StringBuffer(32);
			if (parseField(sb, obj, "")) {
				return sb.toString();
			}
		}
		return obj.toString();
	}

	private boolean parseField(StringBuffer sb, Object obj, String space) {
		boolean result = true;
		try {
			if (obj instanceof com.google.protobuf.GeneratedMessage) {// gm
				sb.append(space).append(obj.getClass().getSimpleName()).append("{\r\n");
				Field[] fields = obj.getClass().getDeclaredFields();
				for (Field field : fields) {
					if (!field.isAccessible()) {
						field.setAccessible(true);
					}
					String name = field.getName();
					if (name.endsWith("_")) {// user filed
						Object value = field.get(obj);
						if (value instanceof List) {// list
							sb.append(space).append("  List[\r\n");
							List lv = (List) value;
							int n = lv.size();
							for (int i = 0; i < n; i++) {
								Object item = lv.get(i);
								if (item instanceof com.google.protobuf.GeneratedMessage) {
									result = result && parseField(sb, item, space + "    ");
								} else {
									sb.append("  " + item);
								}
							}
							sb.append(space).append("  ]\r\n");
							continue;
						} else {
							if (value instanceof com.google.protobuf.GeneratedMessage) {// gm
								result = result && parseField(sb, value, space + "  ");
							} else {// obj
								sb.append(space).append("  " + name + "=" + value).append("\r\n");
							}
						}
					}
				}
				sb.append(space).append("}\r\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private ReqResult<ReqNetOpt> invokeFunction() {
		ReqResult<ReqNetOpt> r = null;
		try {
			CommonLibTest sdk = CommonLibTest.getInstance();
			r = (ReqResult<ReqNetOpt>) SysUtils.invokeMethod(sdk, mFunction.getName(), new Class[] {
					String.class, IReqNetFinished.class, HashMap.class },
					new Object[] { mFunction.getName(), this, mParams });
		} catch (Exception e) {
			e.printStackTrace();
			r = new ReqResult<ReqNetOpt>(null);
			r.mErr = WrapException.wrap(e, "invokeFunction failed");
		}
		if (r == null || !r.isSuccess()) {
			initFunctionResult(r);
		}
		return r;
	}

	private void buildParamsEdits() {
		LayoutInflater lf = getSherlockActivity().getLayoutInflater();
		Iterator<Entry<String, String>> it = mParams.entrySet().iterator();
		Entry<String, String> t = null;
		View v = null;
		TextView tv = null;
		EditText et = null;
		while (it.hasNext()) {
			t = it.next();
			v = lf.inflate(R.layout.com_param_edit_item, null);
			tv = (TextView) v.findViewById(R.id.tv_param_key);
			et = (EditText) v.findViewById(R.id.et_params_value);
			tv.setText(t.getKey() + ":");
			et.setText(t.getValue());
			mEdits.put(t.getKey(), et);
			mLayEdits.addView(v);
		}

		LinearLayout hlay = new LinearLayout(getActivity());
		hlay.setOrientation(LinearLayout.HORIZONTAL);

		mTvDate = new TextView(getActivity());
		mTvDate.setPadding(10, 10, 10, 10);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, -2, 1);
		lp.gravity = Gravity.CENTER;
		hlay.addView(mTvDate, lp);

		mTvTime = new TextView(getActivity());
		lp = new LinearLayout.LayoutParams(0, -2, 1);
		lp.gravity = Gravity.CENTER;
		mTvTime.setPadding(10, 10, 10, 10);
		hlay.addView(mTvTime, lp);
		mLayEdits.addView(hlay, -1, -2);

		CheckBox tb = new CheckBox(getActivity());
		tb.setText("测试地址");
		tb.setOnCheckedChangeListener(this);
		tb.setChecked(LogUtils.mDebugUrl);
		Button bt = new Button(getSherlockActivity());
		bt.setText("发起请求");
		bt.setGravity(Gravity.CENTER);
		bt.setOnClickListener(this);
		hlay = new LinearLayout(getActivity());
		hlay.setOrientation(LinearLayout.HORIZONTAL);
		lp = new LinearLayout.LayoutParams(0, -2, 1);
		lp.gravity = Gravity.CENTER_VERTICAL;
		hlay.addView(tb, lp);
		lp = new LinearLayout.LayoutParams(0, -2, 1);
		lp.gravity = Gravity.CENTER_VERTICAL;
		hlay.addView(bt, lp);
		mLayEdits.addView(hlay, -1, -2);
		mTvDate.setOnClickListener(this);
		mTvTime.setOnClickListener(this);
		mTvDate.setClickable(true);
		mTvTime.setClickable(true);

	}

	private void loadDefParams(InputStream is, String tag) throws Exception {
		mParams.clear();
		XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
		parser.setInput(is, null);
		int event = parser.getEventType();
		while (event != XmlResourceParser.END_DOCUMENT) {
			if (XmlResourceParser.START_TAG == event) {
				if (parser.getName().equals(tag)) {
					event = parser.getAttributeCount();
					for (int i = 0; i < event; i++) {
						mParams.put(parser.getAttributeName(i), parser.getAttributeValue(i));
					}
					break;
				}
			}
			event = parser.next();
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		menu.add("EditParams").setShowAsAction(
				MenuItem.SHOW_AS_ACTION_WITH_TEXT | MenuItem.SHOW_AS_ACTION_ALWAYS);
		Intent tent = new Intent(GlobalApp.getApp(), AtySecret.class);
		menu.add("SecretWind").setIcon(R.drawable.abs__ic_menu_moreoverflow_holo_dark)
				.setIntent(tent)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT | MenuItem.SHOW_AS_ACTION_NEVER);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if ("EditParams".equals(item.getTitle())) {
			toggleParamsEdits();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	public boolean toggleParamsEdits() {
		if (mLayEdits.getVisibility() == View.VISIBLE) {
			mLayEdits.setVisibility(View.INVISIBLE);
			return false;
		} else {
			long cur = System.currentTimeMillis();
			if (mTvDate != null && mTvTime != null) {
				mTvDate.setText(StrUtils.timeFormat(cur, "yyyyMMdd"));
				mTvTime.setText(String.valueOf(cur));
			}
			mLayEdits.setVisibility(View.VISIBLE);
			return true;
		}
	}

	@Override
	public void onRepNetFinished(ReqResult<ReqNetOpt> r) {
		initFunctionResult(r);
	}

	@Override
	public void onClick(View v) {
		if (v == mTvDate) {
			copyText(mTvDate.getText().toString());
		} else if (v == mTvTime) {
			copyText(mTvTime.getText().toString());
		} else {
			Iterator<Entry<String, EditText>> it = mEdits.entrySet().iterator();
			Entry<String, EditText> t = null;
			while (it.hasNext()) {
				t = it.next();
				mParams.put(t.getKey(), t.getValue().getText().toString().trim());
			}
			mLayEdits.setVisibility(View.INVISIBLE);
			mTvParam.setText("当前参数:\r\n" + mParams.toString());
			mTvResult.setText(null);
			mTvExtras.setText(null);
			invokeFunction();
		}
	}

	@SuppressLint("NewApi")
	private void copyText(String text) {
		SysUtils.copyText(getSherlockActivity(), text);
		pop(text + "已经复制", false);
	}

	@Override
	public boolean onKeyBack(boolean fromBar, boolean isFirstPress, boolean isTwiceInTime) {
		if (isFirstPress && !fromBar) {
			if (mLayEdits.getVisibility() == View.VISIBLE) {
				mLayEdits.setVisibility(View.INVISIBLE);
				return true;
			}
		}
		return super.onKeyBack(fromBar, isFirstPress, isTwiceInTime);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		LogUtils.mDebugUrl = isChecked;
	}

}
