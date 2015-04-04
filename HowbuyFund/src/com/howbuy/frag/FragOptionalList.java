package com.howbuy.frag;

import howbuy.android.palmfund.R;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.SparseArrayCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.internal.widget.IcsLinearLayout;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.howbuy.adp.FundOptionalAdp;
import com.howbuy.aty.AtyEmpty;
import com.howbuy.component.AppFrame;
import com.howbuy.config.Analytics;
import com.howbuy.config.FundConfig;
import com.howbuy.config.SelfConfig;
import com.howbuy.config.ValConfig;
import com.howbuy.db.DbOperat;
import com.howbuy.db.DbUtils;
import com.howbuy.db.DbUtils.SqlExeObj;
import com.howbuy.entity.NetWorthBean;
import com.howbuy.entity.NetWorthListBean;
import com.howbuy.entity.UserInf;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.compont.GlobalServiceMger.ScheduleTask;
import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.frag.AbsHbFrag;
import com.howbuy.lib.interfaces.ITimerListener;
import com.howbuy.lib.net.AsyPoolTask;
import com.howbuy.utils.FundUtils;
import com.howbuy.utils.Receiver;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;
import com.mobeta.android.dslv.DragSortListView.RemoveListener;
import com.mobeta.android.dslv.DragSortListView.TouchListener;

@SuppressLint("NewApi")
public class FragOptionalList extends AbsHbFrag implements OnClickListener, OnItemClickListener,
		OnItemLongClickListener, ITimerListener {
	public static final int Type_Sort_jjjz = 1;
	public static final int Type_Sort_hbdr = 2;
	public static final int Type_Sort_FundType = 3;
	public static final int Type_Sort_default_name = 4;
	public static final int Type_Sort_Custom = 5;
	public static final int Type_Sort_hb1y = 6;
	public static final int Type_Desc = 1;
	public static final int Type_Asc = 2;
	private DragSortListView mListView;
	private DragSortController mDragSortController;
	private FundOptionalAdp mAdp;
	private ActionMode mActionMode;
	private IcsLinearLayout mOrderLay;
	private Button mOrderByName;
	private Button mOrderByAddFundType;
	private ProgressBar mProgressBar;
	private RelativeLayout mContentLay;
	private LinearLayout mNoDataLay;
	private LinearLayout mAddLay;
	private RelativeLayout mSyncLay;
	private FrameLayout mButtomLay;

	private LinearLayout mOoption_title_networth_lay;
	private LinearLayout mOption_title_change_lay;
	private TextView mOption_title_networth;
	private TextView mOption_title_name;
	private ImageView mOption_title_networth_image;
	private TextView mOption_title_change;
	private ImageView mOption_title_change_image;

	private NetWorthListBean mWorthListBean;
	private NetWorthListBean mWorthListBeanView;
	// 撤销删除
	private ArrayList<NetWorthBean> mBackDelBean;
	private SparseArrayCompat<Integer> mBackDelIndex;
	private SparseArrayCompat<Boolean> mBackDelCheck;

	private PopupWindow mPop = null;
	private TextView mStatBar = null;
	private boolean mRequireGoBack = false;

	private String mFundType;// is Simu
	private View viewRoot;
	private boolean mActionModleFlag;
	private int mSortTypeTrue;// 排序条件
	private int mSortTypeLastSerializable;// 已存排序条件
	private boolean mSortTypeTempFlagNetWorth;// 是否临时排序middle
	private boolean mSortTypeTempFlagChange;// 是否临时排序end
	private int mSortTypeTempIndex;// 临时排序升还是降序或者普通
	private QueryOptionalTask mTask;
	private Animation mRotateUpAnim, mRotateDownAnim;

	// private mCurrtemp

	@Override
	protected int getFragLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.frag_opt_list;
	}

	private void luncherAdd() {
		int type = isSimu() ? FragSearch.Type_Intent_Simu : FragSearch.Type_Intent_Fund;
		FundUtils.launchFundSearch(this, type, 0);
	}

	public void menuSearchClick() {
		luncherAdd();
		resetTempSortStatus();
		// 重置下排序条件
		mSortTypeTrue = 0;
	}

	public void menuEditClick() {
		if (mWorthListBeanView != null && mWorthListBeanView.size() != 0) {
			startActionMode();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mWorthListBean = new NetWorthListBean();
		mWorthListBeanView = new NetWorthListBean();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		AppFrame.getApp()
				.getsF()
				.edit()
				.putInt(isSimu() ? ValConfig.sFOptSortTypeSm : ValConfig.sFOptSortTypeGm,
						mSortTypeLastSerializable).commit();
		printlType(mSortTypeLastSerializable);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (mTask == null) {
			// exeTask(0);
		} else {
			// 延迟加载 避免search添加多个自选 正在入库而导致自选显示不完全
			// mHandler.sendEmptyMessageDelayed(0, 400);
		}
	}

	private void updateNetValues() {
		int n = mAdp == null ? 0 : mAdp.getCount();
		if (n > 0) {
			List<NetWorthBean> r = mAdp.getItems().getItems();
			StringBuilder sb = new StringBuilder(64);
			for (int i = 0; i < n; i++) {
				sb.append(r.get(i).getJjdm()).append("-");
			}
			if (getParentFragment() instanceof FragTbOptional) {
				((FragTbOptional) getParentFragment())
						.updateNetValueByIDs(mFundType, sb.toString());
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.option_title_networth_lay:
			if (true) {
				return;
			}

			if (mActionModleFlag) {
				return;
			}
			setSortType(Type_Sort_jjjz);
			if (mSortTypeTempFlagChange) {
				mOption_title_change.setTextColor(getResources().getColor(R.color.text_title));
				mOption_title_change_image.clearAnimation();
				mOption_title_change_image.setImageResource(R.drawable.ic_down_21);
				mSortTypeTempIndex = 0;
				mSortTypeTempFlagChange = false;
			}

			if (mSortTypeTempIndex == 0) {
				mSortTypeTempIndex = Type_Desc;
				mOption_title_networth.setTextColor(getResources().getColor(R.color.text_increase));
				if (mSortTypeTempFlagNetWorth) {
					mOption_title_networth_image.startAnimation(mRotateDownAnim);
				}
				mOption_title_networth_image.setImageResource(R.drawable.ic_down_20);
				mSortTypeTempFlagNetWorth = true;
			} else if (mSortTypeTempIndex == Type_Desc) {
				mSortTypeTempIndex = Type_Asc;
				mOption_title_networth.setTextColor(getResources().getColor(R.color.text_increase));
				mOption_title_networth_image.setImageResource(R.drawable.ic_down_20);
				mOption_title_networth_image.startAnimation(mRotateUpAnim);
				mSortTypeTempFlagNetWorth = true;
			} else {
				mSortTypeTrue = mSortTypeLastSerializable;
				mSortTypeTempIndex = 0;
				mOption_title_networth.setTextColor(getResources().getColor(R.color.text_title));
				mOption_title_networth_image.setImageResource(R.drawable.ic_down_21);
				mOption_title_networth_image.clearAnimation();
				mSortTypeTempFlagNetWorth = false;
			}
			exeTask(mSortTypeTempIndex, false);
			break;
		case R.id.option_title_change_lay:
			if (true) {
				return;
			}
			if (mActionModleFlag) {
				return;
			}
			if (isSimu()) {
				setSortType(Type_Sort_hb1y);
			} else {
				setSortType(Type_Sort_hbdr);
			}

			if (mSortTypeTempFlagNetWorth) {
				mOption_title_networth.setTextColor(getResources().getColor(R.color.text_title));
				mOption_title_networth_image.clearAnimation();
				mOption_title_networth_image.setImageResource(R.drawable.ic_down_21);
				mSortTypeTempIndex = 0;
				mSortTypeTempFlagNetWorth = false;
			}

			if (mSortTypeTempIndex == 0) {
				mSortTypeTempIndex = Type_Desc;
				mOption_title_change.setTextColor(getResources().getColor(R.color.text_increase));
				mOption_title_change_image.setImageResource(R.drawable.ic_down_20);
				if (mSortTypeTempFlagChange) {
					mOption_title_change_image.startAnimation(mRotateDownAnim);
				}
				mSortTypeTempFlagChange = true;
			} else if (mSortTypeTempIndex == Type_Desc) {
				mSortTypeTempIndex = Type_Asc;
				mOption_title_change.setTextColor(getResources().getColor(R.color.text_increase));
				mOption_title_change_image.setImageResource(R.drawable.ic_down_20);
				mOption_title_change_image.startAnimation(mRotateUpAnim);
				mSortTypeTempFlagChange = true;
			} else {
				mSortTypeTrue = mSortTypeLastSerializable;
				mSortTypeTempIndex = 0;
				mOption_title_change.setTextColor(getResources().getColor(R.color.text_title));
				mOption_title_change_image.setImageResource(R.drawable.ic_down_21);
				mOption_title_change_image.clearAnimation();
				mSortTypeTempFlagChange = false;
			}
			exeTask(mSortTypeTempIndex, false);
			break;
		case R.id.order_name_btn:
			clearSelects();
			// 按民稱
			Analytics.onEvent(getSherlockActivity(), Analytics.EDIT_CUSTOM_FUNDS,
					Analytics.KEY_FROM, "按名称整理");
			exeTask(0, false);
			if (mSortTypeTrue != Type_Sort_default_name) {
				mSortTypeTrue = Type_Sort_default_name;
				exeTask(0, false);
			}
			setActionModleTitle();
			break;
		case R.id.order_type_btn:
			clearSelects();
			Analytics.onEvent(getSherlockActivity(), Analytics.EDIT_CUSTOM_FUNDS,
					Analytics.KEY_FROM, "按类型整理");
			// 按基金类型排序
			if (mSortTypeTrue != Type_Sort_FundType) {
				mSortTypeTrue = Type_Sort_FundType;
				exeTask(0, false);
			}
			setActionModleTitle();
			break;
		case R.id.nodata_addclick:
			// 跳转到搜索
			luncherAdd();
			break;
		case R.id.nodata_other_click:
			// 跳转到同步
			launchItem("登录", FragSetLogin.class.getName());
			break;
		case R.id.close:
			AppFrame.getApp().getsF().edit().putBoolean(ValConfig.SFOPTUserClose, true).commit();
			setButtomView(false);
			break;
		case R.id.tv_artical_del_cancle:
			mRequireGoBack = true;
			hidePopWind();
			Analytics.onEvent(getSherlockActivity(), Analytics.EDIT_CUSTOM_FUNDS,
					Analytics.KEY_FROM, "撤销删除");
			break;
		default:
			break;
		}
	}

	private void launchItem(String title, String fragName) {
		Bundle b = new Bundle();
		b.putString(ValConfig.IT_NAME, title);
		Intent t = new Intent(getSherlockActivity(), AtyEmpty.class);
		t.putExtra(AtyEmpty.KEY_FRAG_NAME, fragName);
		t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
		startActivity(t);
	}

	@Override
	public boolean onKeyBack(boolean fromBar, boolean isFirstPress, boolean isTwiceInTime) {
		// TODO Auto-generated method stub
		hidePopWind();
		return super.onKeyBack(fromBar, isFirstPress, isTwiceInTime);
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		// TODO Auto-generated method stub
		mFundType = getArguments().getString(ValConfig.IT_TYPE);
		viewRoot = root;
		mListView = (DragSortListView) root.findViewById(R.id.listview);
		mOrderLay = (IcsLinearLayout) root.findViewById(R.id.buttom_tbn);
		mOrderByName = (Button) root.findViewById(R.id.order_name_btn);
		mOrderByAddFundType = (Button) root.findViewById(R.id.order_type_btn);
		mProgressBar = (ProgressBar) root.findViewById(R.id.progressbar);
		mNoDataLay = (LinearLayout) root.findViewById(R.id.nodata_lay);
		mAddLay = (LinearLayout) root.findViewById(R.id.nodata_addclick);
		mSyncLay = (RelativeLayout) root.findViewById(R.id.nodata_other_click);
		mContentLay = (RelativeLayout) root.findViewById(R.id.content_lay);
		mButtomLay = (FrameLayout) root.findViewById(R.id.buttom_lay);

		mOoption_title_networth_lay = (LinearLayout) root
				.findViewById(R.id.option_title_networth_lay);
		mOption_title_change_lay = (LinearLayout) root.findViewById(R.id.option_title_change_lay);
		mOption_title_networth = (TextView) root.findViewById(R.id.option_title_networth);
		mOption_title_name = (TextView) root.findViewById(R.id.option_title_name);
		mOption_title_networth_image = (ImageView) root
				.findViewById(R.id.option_title_networth_image);
		mOption_title_change = (TextView) root.findViewById(R.id.option_title_change);
		mOption_title_change_image = (ImageView) root.findViewById(R.id.option_title_change_image);

		TextView login = (TextView) root.findViewById(R.id.tv_login);
		login.setText(all(getResources().getString(R.string.sync_tips), 0, R.color.actionbar_bg,
				false));
		if (mRotateUpAnim == null) {
			mRotateDownAnim = getExpnadAnim(false, 200);
			mRotateUpAnim = getExpnadAnim(true, 200);
		}

		mOrderByName.setOnClickListener(this);
		mOrderByAddFundType.setOnClickListener(this);
		mAddLay.setOnClickListener(this);
		root.findViewById(R.id.close).setOnClickListener(this);
		mSyncLay.setOnClickListener(this);
		mListView.setOnItemLongClickListener(this);
		mListView.setOnItemClickListener(this);
		mListView.setDropListener(onDrop);
		mListView.setRemoveListener(onRemove);
		mListView.setChoiceMode(AbsListView.CHOICE_MODE_NONE);
		mOoption_title_networth_lay.setOnClickListener(this);
		mOption_title_change_lay.setOnClickListener(this);

		mDragSortController = new DragSortController(mListView);
		mDragSortController.setRemoveEnabled(false);
		// mDragSortController.setBackgroundColor(getResources().getColor(R.color.text_title));
		if (mAdp == null) {
			mAdp = new FundOptionalAdp(getActivity().getLayoutInflater(), mWorthListBeanView);
		}
		mListView.setAdapter(mAdp);
		if (isSimu()) {
			mOption_title_change.setText(R.string.hb1y);
			mOption_title_name.setText(R.string.fundname_simu);
		}
		mListView.setListTouchListener(new TouchListener() {
			@Override
			public void onListTouch(MotionEvent e) {
				if (mPop != null && mPop.isShowing()) {
					hidePopWind();
				}
			}
		});

		exeTask(0, false);

	}

	public static SpannableString all(String content, int size, int color, boolean isXia) {

		SpannableString sp = new SpannableString(content);
		if (color != -1) {
			sp.setSpan(new ForegroundColorSpan(AppFrame.getApp().getResources().getColor(color)),
					0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		if (isXia) {
			sp.setSpan(new UnderlineSpan(), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return sp;
	}

	@Override
	public void onStart() {
		// getParent().requestDisallowInterceptTouchEvent(true)
		super.onStart();
	};

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (isTempSortType(mSortTypeTrue)) {
					resetTempSortStatus();
					mSortTypeTrue = 0;
				}
				exeTask(0, false);
				break;
			case 1:
				break;
			default:
				break;
			}
		};
	};

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub'
		if (mActionModleFlag == false) {
			mAdp.setItemChecked(position, true);
			startActionMode();
			setActionModleTitle();
			return true;
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		if (mActionModleFlag) {
			mAdp.setItemChecked(position, !mAdp.getItemsChecked(position));
			setActionModleTitle();
		} else {
			Object obj = mAdp.getItem(position);
			if (obj instanceof NetWorthBean) {
				FundUtils.launchFundDetails(this, ((NetWorthBean) obj).getJjdm(), 0, 0);
			}
			Analytics.onEvent(getActivity(), Analytics.VIEW_FUND_DETAIL, Analytics.KEY_FROM, "自选");
		}

	}

	private void startActionMode() {
		// LogUtils.d(viewRoot.getParent().toString());
		if (mSortTypeTempFlagChange || mSortTypeTempFlagNetWorth) {
			resetTempSortStatus();
		}

		mActionMode = getSherlockActivity().startActionMode(new OptActionMode());
		mAdp.setActionModleFlag(true);
		setButtomView(true);
		if (isSimu()) {
			mOrderByAddFundType.setVisibility(View.GONE);
		}
		mListView.setDragEnabled(true);
		mDragSortController.setRemoveEnabled(true);
		((FragTbOptional) getParentFragment()).setViewPagerCanHScroll(false);
		mActionModleFlag = true;
	}

	private void endActionModile() {
		mAdp.setActionModleFlag(false);
		setButtomView(false);
		mListView.setDragEnabled(false);
		mDragSortController.setRemoveEnabled(false);
		((FragTbOptional) getParentFragment()).setViewPagerCanHScroll(true);
		clearSelects();
		mActionModleFlag = false;
	}

	private void setActionModleTitle() {
		if (mActionModleFlag) {
			SparseArrayCompat<Boolean> sChecked = mAdp.getCheckList();
			SparseArrayCompat<Boolean> sCheckedTrue = getCheckedItemTrue(sChecked);
			if (sCheckedTrue == null || sCheckedTrue.size() == 0) {
				mActionMode.setTitle(R.string.dragchangesort);
			} else {
				mActionMode.setTitle(sCheckedTrue.size() + "只基金已选中");
			}
		}
	}

	private void clearSelects() {
		mAdp.clearCheckList();
	}

	private void setSortType(int type) {
		mSortTypeTrue = type;
		if (!isTempSortType(type)) {
			mSortTypeLastSerializable = mSortTypeTrue;
		}
	}

	private boolean isTempSortType(int sortType) {
		if (sortType == Type_Sort_hb1y || sortType == Type_Sort_hbdr || sortType == Type_Sort_jjjz) {
			return true;
		}
		return false;
	}

	private int getSavedSortType() {
		int tp;
		if (mSortTypeTrue == 0) {
			String ftp = !isSimu() ? ValConfig.sFOptSortTypeGm : ValConfig.sFOptSortTypeSm;
			tp = AppFrame.getApp().getsF().getInt(ftp, Type_Sort_default_name);
		} else {
			tp = mSortTypeTrue;
		}

		if (!isTempSortType(tp)) {
			mSortTypeLastSerializable = tp;
		}

		return tp;
	}

	private String buildQuerySql(int desAsc) {
		String sql = "select a.[code],a.[name],a.[type],a.[xuan],c.[xuantime],b.[jjjz],b.[jzrq],b.[hbdr],b.[hb1y],b.[wfsy],a.[unit_netvalue],b.[qrsy] from fundsinfo a left join netvalue b on a.[code]=b.[jjdm] left join fundsinfoopt c on a.[code]=c.[code] where a.[xuan] in('"
				+ SelfConfig.SynsShowAdd + "','" + SelfConfig.UNSynsAdd + "')";
		StringBuilder sb = new StringBuilder(sql);

		int tp = getSavedSortType();
		printlType(tp);
		if (!isSimu()) {
			sb.append(" and a.[type] <> 'sm'");
			switch (tp) {
			case Type_Sort_default_name:
				sb.append(" order by lower(a.[pinyin]) asc");
				break;
			case Type_Sort_jjjz:
				sb = changeSql(sb, "b.[jjjz]", desAsc == Type_Asc ? " asc" : " desc");
				break;
			case Type_Sort_hbdr:
				sb = changeSql(sb, "b.[hbdr]", desAsc == Type_Asc ? " asc" : " desc");
				break;
			case Type_Sort_FundType:
				sb.append(" order by case a.[type] ");
				sb.append(" when ").append("'").append(FundConfig.TYPE_GUPIAO).append("'")
						.append(" then 0");
				sb.append(" when ").append("'").append(FundConfig.TYPE_HUOBI).append("'")
						.append(" then 1");
				sb.append(" when ").append("'").append(FundConfig.TYPE_ZHAIQUAN).append("'")
						.append(" then 2");
				sb.append(" when ").append("'").append(FundConfig.TYPE_HUNHE).append("'")
						.append(" then 3");
				sb.append(" when ").append("'").append(FundConfig.TYPE_ZHISHU).append("'")
						.append(" then 4");
				sb.append(" when ").append("'").append(FundConfig.TYPE_QDII).append("'")
						.append(" then 5");
				sb.append(" when ").append("'").append(FundConfig.TYPE_LICAI).append("'")
						.append(" then 6");
				sb.append(" when ").append("'").append(FundConfig.TYPE_BAOBEN).append("'")
						.append(" then 7");
				sb.append(" when ").append("'").append(FundConfig.TYPE_JIEGOU).append("'")
						.append(" then 8");
				sb.append(" when ").append("'").append(FundConfig.TYPE_FENGBI).append("'")
						.append(" then 9");
				sb.append(" end");
				sb.append(" , a.[name]");
				break;
			case Type_Sort_Custom:
				sb.append(" order by case when c.[postion] is null then 1 else 0 end,c.[postion]");
				break;
			default:
				break;
			}
		} else {
			sb.append(" and a.[type] = 'sm'");
			switch (tp) {
			case Type_Sort_default_name:
				sb.append(" order by lower(a.[pinyin]) asc");
				break;
			case Type_Sort_jjjz:
				sb = changeSql(sb, "b.[jjjz]/a.[unit_netvalue]", desAsc == Type_Asc ? " asc"
						: " desc");
				break;
			case Type_Sort_hb1y:
				sb = changeSql(sb, "b.[hb1y]", desAsc == Type_Asc ? " asc" : " desc");
				break;
			case Type_Sort_Custom:
				sb.append(" order by case when c.[postion] is null then 1 else 0 end,c.[postion]");
				break;
			default:
				break;
			}
		}
		return sb.toString();
	}

	public boolean isSimu() {
		if (TextUtils.isEmpty(mFundType)) {
			return false;
		} else {
			if (mFundType.equals(FragTbOptional.FUND)) {
				return false;
			}
		}
		return true;
	}

	private boolean isAccountLogin() {
		return UserInf.getUser().isLogined();
	}

	private SparseArrayCompat<Boolean> getCheckedItemTrue(SparseArrayCompat<Boolean> oragin) {
		if (oragin != null) {
			SparseArrayCompat<Boolean> s = new SparseArrayCompat<Boolean>();
			for (int i = 0; i < oragin.size(); i++) {
				boolean flag = oragin.valueAt(i);
				if (flag) {
					s.put(oragin.keyAt(i), flag);
				}
			}
			return s;
		}
		return null;
	}

	private void resetTempSortStatus() {
		if (mSortTypeTempFlagNetWorth) {
			mSortTypeTempFlagNetWorth = false;
			mOption_title_networth.setTextColor(getResources().getColor(R.color.text_title));
			mOption_title_networth_image.setImageResource(R.drawable.ic_down_21);
			mOption_title_networth_image.clearAnimation();
			mSortTypeTempIndex = 0;
			setSortType(mSortTypeLastSerializable);
			exeTask(0, false);
		}
		if (mSortTypeTempFlagChange) {
			mSortTypeTempFlagChange = false;
			mOption_title_change.setTextColor(getResources().getColor(R.color.text_title));
			mOption_title_change_image.setImageResource(R.drawable.ic_down_21);
			mOption_title_change_image.clearAnimation();
			mSortTypeTempIndex = 0;
			setSortType(mSortTypeLastSerializable);
			exeTask(0, false);
		}

	}

	private void setNoDataView() {
		if (mAdp == null || mAdp.getCount() == 0) {
			mListView.setVisibility(View.GONE);
			mNoDataLay.setVisibility(View.VISIBLE);
			if (mActionMode != null) {
				mActionMode.finish();
				if (mBackDelCheck != null) {
					mBackDelCheck.clear();
				}
			}
		} else {
			mListView.setVisibility(View.VISIBLE);
			mNoDataLay.setVisibility(View.GONE);
		}
		getSherlockActivity().invalidateOptionsMenu();
	}

	private void setButtomView(boolean isActionMode) {
		boolean isLogin = isAccountLogin();
		boolean isUserClose = AppFrame.getApp().getsF().getBoolean(ValConfig.SFOPTUserClose, false);
		if (isLogin || isUserClose) {
			if (isActionMode) {
				mButtomLay.setVisibility(View.VISIBLE);
				mOrderLay.setVisibility(View.VISIBLE);
			} else {
				mOrderLay.setVisibility(View.GONE);
				mButtomLay.setVisibility(View.GONE);
			}
			mSyncLay.setVisibility(View.GONE);
		} else {
			if (isActionMode) {
				mButtomLay.setVisibility(View.VISIBLE);
				mOrderLay.setVisibility(View.VISIBLE);
				mSyncLay.setVisibility(View.GONE);
			} else {
				mButtomLay.setVisibility(View.VISIBLE);
				mOrderLay.setVisibility(View.GONE);
				mSyncLay.setVisibility(View.VISIBLE);
			}
		}
	}

	private void backUpDelBean() {
		mBackDelBean = new ArrayList<NetWorthBean>(mAdp.getItems().getItems());
		mBackDelCheck = mAdp.getCheckList().clone();
		mAdp.getCheckList();
	}

	private void backUpDelIndex(int delIndex) {
		if (mBackDelIndex == null) {
			mBackDelIndex = new SparseArrayCompat<Integer>();
		}
		mBackDelIndex.put(delIndex, delIndex);
	}

	private void exeTask(int ascDesc, boolean forNetValueUpdated) {
		String sqlScript = buildQuerySql(ascDesc);
		mTask = new QueryOptionalTask(sqlScript, forNetValueUpdated);
		mTask.execute(false);
	}

	private class QueryOptionalTask extends AsyPoolTask<Void, Void, NetWorthListBean> {
		private String sqlScript;
		private boolean forNetValueUpdated = false;

		public QueryOptionalTask(String sql, boolean forUpdated) {
			super();
			// TODO Auto-generated constructor stub
			this.sqlScript = sql;
			forNetValueUpdated = forUpdated;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if (mWorthListBeanView == null || mWorthListBeanView.size() == 0) {
				mProgressBar.setVisibility(View.VISIBLE);
				mContentLay.setVisibility(View.VISIBLE);
				mNoDataLay.setVisibility(View.GONE);
			}
		}

		@Override
		protected NetWorthListBean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				mWorthListBean = DbOperat.getInstance().queryOption(sqlScript);
				return mWorthListBean;
			} catch (WrapException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(NetWorthListBean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mProgressBar.setVisibility(View.GONE);
			mWorthListBeanView.clear();
			if (result != null) {
				mWorthListBeanView.addItems(result.getItems(), false);
				mAdp.notifyDataSetChanged();
				if (!forNetValueUpdated) {
					updateNetValues();
				}
			}
			setNoDataView();
			setButtomView(mActionModleFlag);
			if (isAdded()) {
				getSherlockActivity().supportInvalidateOptionsMenu();
			}
		}

		/**
		 * 找出simu
		 * 
		 * @deprecated
		 * @param result
		 * @return
		 */
		public NetWorthListBean getSimuList(NetWorthListBean result) {
			NetWorthListBean resBean = new NetWorthListBean();
			if (result == null) {
				return resBean;
			}
			List<NetWorthBean> orginList = result.getItems();
			List<NetWorthBean> resList = new ArrayList<NetWorthBean>();
			for (int i = 0; i < orginList.size(); i++) {
				NetWorthBean b = orginList.get(i);
				if (b.getJjfl().equals(FundConfig.TYPE_SIMU)) {
					resList.add(b);
				}
			}
			resBean.addItems(resList, false);
			return resBean;
		}

	}

	private Animation getExpnadAnim(boolean isExpand, int duration) {
		Animation anim = null;
		if (isExpand) {
			if (mRotateUpAnim == null) {
				mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF,
						0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
				mRotateUpAnim.setFillAfter(true);
			}
			anim = mRotateUpAnim;
		} else {
			if (mRotateDownAnim == null) {
				mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF,
						0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
				mRotateDownAnim.setFillAfter(true);
			}
			anim = mRotateDownAnim;
		}
		anim.setDuration(duration);
		return anim;
	}

	private final class OptActionMode implements ActionMode.Callback {
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// Used to put dark icons on light action bar
			menu.add(R.string.top).setIcon(R.drawable.ic_rank)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
			menu.add(R.string.delete).setIcon(R.drawable.ic_action_discard)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			mode.setTitle(R.string.dragchangesort);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			SparseArrayCompat<Boolean> sTrue = mAdp.getCheckList();
			if (sTrue.size() == 0) {
				pop("请选择条目", false);
				return true;
			}

			if (item.getTitle().equals(getResources().getString(R.string.delete))) {
				if (mPop == null || !mPop.isShowing()) {
					Analytics.onEvent(getSherlockActivity(), Analytics.EDIT_CUSTOM_FUNDS,
							Analytics.KEY_FROM, "点击删除");
					delSelectedItems(sTrue);
				}
			} else {
				if (mSortTypeTrue != Type_Sort_Custom) {
					setSortType(Type_Sort_Custom);
					Analytics.onEvent(getSherlockActivity(), Analytics.EDIT_CUSTOM_FUNDS,
							Analytics.KEY_FROM, "置顶");
				}
				topItems(sTrue);
			}
			return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			endActionModile();
		}
	}

	private void topItems(SparseArrayCompat<Boolean> sTrue) {
		ArrayList<NetWorthBean> checkedList = new ArrayList<NetWorthBean>();
		if (null != sTrue) {
			for (int i = sTrue.size() - 1; i >= 0; i--) {
				int index = sTrue.keyAt(i);
				boolean check = sTrue.valueAt(i);
				if (check) {
					NetWorthBean n = (NetWorthBean) mAdp.getItem(index);
					checkedList.add(0, n);
					mAdp.removeItem(index, false);
				}
			}
			clearSelects();
			mWorthListBeanView.addItems(checkedList, false);
			mAdp.notifyDataSetChanged();
			saveTopItems(mAdp.getItems().getItems());
			setActionModleTitle();
		}
	}

	private void saveTopItems(List<NetWorthBean> cusSortList) {
		String sql = "insert or replace into fundsinfoopt(postion,code) values(?,?)";
		final ArrayList<SqlExeObj> sqlExeObjs = new ArrayList<SqlExeObj>();
		for (int i = 0; i < cusSortList.size(); i++) {
			String jjdm = cusSortList.get(i).getJjdm();
			SqlExeObj sExeObj = new SqlExeObj(sql, new String[] { String.valueOf(i), jjdm });
			sqlExeObjs.add(sExeObj);
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				DbUtils.exeSql(sqlExeObjs, false);
			}
		}).start();
	}

	private void delSelectedItems(SparseArrayCompat<Boolean> sTrue) {
		if (null != sTrue) {
			backUpDelBean();
			int sum = 0;
			for (int i = sTrue.size() - 1; i >= 0; i--) {
				int index = sTrue.keyAt(i);
				boolean check = sTrue.valueAt(i);
				if (check) {
					sum++;
					backUpDelIndex(index);
					mAdp.removeCheckState(index);
					mAdp.removeItem(index, false);
				}
			}
			showPopWind(sum + "条自选已删除");
			Analytics.onEvent(getSherlockActivity(), Analytics.DELETE_CUSTOM_FUNDS,
					Analytics.KEY_FROM, "自选编辑");
			mAdp.notifyDataSetChanged();
			setActionModleTitle();
			setNoDataView();
			setButtomView(mActionModleFlag);
		}
	}

	private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
		@Override
		public void drop(int from, int to) {
			if (from != to) {
				NetWorthBean item = (NetWorthBean) mAdp.getItem(from);
				mAdp.removeItem(from, false);
				mAdp.insertItem(item, to, true);
				mAdp.moveCheckState(from, to);
				// 保存用戶自定义状态
				setSortType(Type_Sort_Custom);
				saveTopItems(mAdp.getItems().getItems());
				Analytics.onEvent(getSherlockActivity(), Analytics.EDIT_CUSTOM_FUNDS,
						Analytics.KEY_FROM, "排序");
			}
			Log.i("num--drop--", String.valueOf(mAdp.getCheckList()));
		}
	};

	private RemoveListener onRemove = new DragSortListView.RemoveListener() {
		@Override
		public void remove(int which) {
			backUpDelBean();
			backUpDelIndex(which);

			mAdp.removeItem(which, true);
			mAdp.removeCheckState(which);
			setActionModleTitle();
			// delete after update title
			setNoDataView();
			setButtomView(mActionModleFlag);
			showPopWind("1条自选已删除");
			Analytics.onEvent(getSherlockActivity(), Analytics.DELETE_CUSTOM_FUNDS,
					Analytics.KEY_FROM, "自选编辑");
			Analytics.onEvent(getSherlockActivity(), Analytics.EDIT_CUSTOM_FUNDS,
					Analytics.KEY_FROM, "滑动删除");
		}
	};

	private void buildPopWind() {
		final View lay = LayoutInflater.from(getSherlockActivity()).inflate(
				R.layout.com_pop_restore, null);
		mStatBar = (TextView) lay.findViewById(R.id.tv_artical_del_state);
		lay.findViewById(R.id.tv_artical_del_cancle).setOnClickListener(this);
		mPop = new PopupWindow(lay, -1, -2);
		mPop.setOutsideTouchable(true);
		mPop.setAnimationStyle(R.style.pop_anim_bottom_appear);
	}

	private synchronized void showPopWind(String msg) {
		if (mPop == null) {
			buildPopWind();
		}
		mStatBar.setText(msg);
		if (!mPop.isShowing()) {
			mPop.showAtLocation(mListView, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
			ScheduleTask task = new ScheduleTask(1, this);
			task.setExecuteArg(3000, 0, false);
			GlobalApp.getApp().getGlobalServiceMger().addTimerListener(task);
		}
	}

	private void hidePopWind() {
		if (mPop != null && mPop.isShowing()) {
			onUndoBarDismiss();
			mPop.dismiss();
			AppFrame.getApp().getGlobalServiceMger().removeTimerListener(1, this);
		}
	}

	@Override
	public void onTimerRun(int which, int timerState, boolean hasTask, int timesOrSize) {
		d("onTimerRun", "which=" + which + ",timerState=" + timerState + ",hasTask=" + hasTask
				+ ",timesOrSize");
		if (timerState == ITimerListener.TIMER_SCHEDULE) {
			hidePopWind();
		}
	}

	private void onUndoBarDismiss() {
		if (mRequireGoBack) {// 按了撤回.
			mAdp.setItems(mBackDelBean, true);
			mAdp.setCheckList(mBackDelCheck);
			mRequireGoBack = false;
			setNoDataView();
			setButtomView(mActionModleFlag);
			for (int i = 0; i < mBackDelIndex.size(); i++) {
				mAdp.setItemChecked(mBackDelIndex.keyAt(i), false);
			}
		} else {
			for (int i = 0; i < mBackDelIndex.size(); i++) {
				NetWorthBean n = mBackDelBean.get(mBackDelIndex.keyAt(i));
				FundUtils.updateOptionalNoToast(AppFrame.getApp(), n.getJjdm(),
						SelfConfig.UNSynsDel, false);
			}
			FundUtils.exeOptional(getActivity());
		}
		mBackDelBean.clear();
		mBackDelIndex.clear();
		mBackDelCheck.clear();
		mAdp.notifyDataSetInvalidated();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mPop != null && mPop.isShowing()) {
			hidePopWind();
		}
	}

	/**
	 * 净值排行中拼接SQL 可以实现空值排到最后 且空值为“-9999”/“9999”
	 * 
	 * @param sql
	 * @param orderClumns
	 * @param sorderAsc
	 * @return
	 */
	public static StringBuilder changeSql(StringBuilder sql, String orderClumns, String sorderAsc) {
		if (orderClumns != null && sorderAsc != null) {
			String mSorderAsc = sorderAsc.equals(" desc") ? "-99999" : "99999";
			String mSql = " CASE WHEN " + orderClumns + " is null THEN " + mSorderAsc + " WHEN "
					+ orderClumns + "='' THEN " + mSorderAsc + " ELSE " + orderClumns + " END QQ";
			if (orderClumns.contains("a.[unit_netvalue]")) {
				orderClumns = "b.[jjjz]";
			}
			mSql = sql.toString().replace(orderClumns, mSql);
			sql = new StringBuilder(mSql);
			sql.append(" order by QQ " + sorderAsc);
			return sql;
		} else {
			return sql;
		}
	}

	private void printlType(int type) {
		String text = null;
		switch (type) {
		case Type_Sort_Custom:
			text = "Custom";
			break;
		case Type_Sort_default_name:
			text = "name";
			break;
		case Type_Sort_FundType:
			text = "FundType";
			break;
		case Type_Sort_hb1y:
			text = "hb1y";
			break;
		case Type_Sort_hbdr:
			text = "hbdr";
			break;
		case Type_Sort_jjjz:
			text = "jjjz";
			break;

		default:
			break;
		}
	}

	@Override
	public void onReceiveBroadcast(int from, Bundle b) {
		if (from == Receiver.FROM_UPDATE_LAUNCH || from == Receiver.FROM_OPTIONAL_CHNAGE
				|| from == Receiver.FROM_OPTIONAL_SYNC || from == Receiver.FROM_UPGRADE_DB) {
			exeTask(0, false);
		} else if (from == Receiver.FROM_UPDATE_NETVAL_IDS) {
			if (mFundType.equals(b.get(ValConfig.IT_TYPE))) {
				if (b.getInt(ValConfig.IT_ID) > 0) {
					exeTask(0, true);
				}
			}
		}
	}

	@Override
	public boolean shouldEnableLocalBroadcast() {
		return true;
	}

	public FundOptionalAdp getmAdp() {
		return mAdp;
	}

	public void setmAdp(FundOptionalAdp mAdp) {
		this.mAdp = mAdp;
	}

}
