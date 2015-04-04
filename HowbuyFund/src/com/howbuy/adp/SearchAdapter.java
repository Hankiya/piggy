package com.howbuy.adp;

import howbuy.android.palmfund.R;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.howbuy.config.Analytics;
import com.howbuy.config.SelfConfig;
import com.howbuy.config.ValConfig;
import com.howbuy.control.CheckHeadText;
import com.howbuy.db.DbOperat;
import com.howbuy.entity.NetWorthBean;
import com.howbuy.entity.NetWorthListBean;
import com.howbuy.frag.FragSearchList;
import com.howbuy.lib.error.WrapException;
import com.howbuy.utils.FundUtils;

public class SearchAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer,
		Filterable, OnClickListener {
	public static final String SearchAdapter = "SearchAdapter";
	private NetWorthListBean mOriginListBean;
	private NetWorthListBean mBean;
	private Context mContext;
	private int[] mSectionIndex;
	private String[] mSectionLetters;
	private LayoutInflater mInflater;
	private SearchFillter mFillter;
	private String queryString;
	private boolean queringFlag;
	private boolean simuFlag;
	// start search key pos for hightlight.

	private int[] mSearchKeyPos = null;
	private int mKeyColor = 0xffff0000;
	private QueryCallBack mQueryCallBack;
	private FragSearchList mFrag;

	public boolean searchKeyIndex(String input, String tag, int[] r) {
		int from = 0, n = r.length;
		for (int i = 0; i < n; i++) {
			if (-1 != (r[i] = input.indexOf(tag.charAt(i), from))) {
				from = r[i] + 1;
			} else {
				return false;
			}
		}
		return true;
	}

	public SpannableString formatSearchSource(String input) {
		SpannableString r = new SpannableString(input);
		int index = input.indexOf(queryString);
		if (index != -1) {
			r.setSpan(new ForegroundColorSpan(mKeyColor), index, index + queryString.length(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		} else {
			if (searchKeyIndex(input, queryString, mSearchKeyPos)) {
				for (int i = 0; i < mSearchKeyPos.length; i++) {
					r.setSpan(new ForegroundColorSpan(mKeyColor), mSearchKeyPos[i],
							mSearchKeyPos[i] + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
		}

		return r;
	}

	// end search key pos for hightlight.
	public SearchAdapter(Context context, NetWorthListBean mListBean, FragSearchList flag,
			QueryCallBack callBack, boolean isSimu) {
		super();
		this.mOriginListBean = new NetWorthListBean();
		this.mOriginListBean.addItems(mListBean);
		this.mContext = context;
		this.mQueryCallBack = callBack;
		this.simuFlag = isSimu;
		this.mFrag = flag;
		mBean = new NetWorthListBean();
		mBean.addItems(new ArrayList<NetWorthBean>(mListBean.getItems()), true);
		mInflater = LayoutInflater.from(mContext);
		mSectionIndex = getSectionIndex();
		mSectionLetters = getSectionLetters();
	}

	private final class HeaderViewHolder {
		CheckHeadText text;
	}

	private final class ViewHolder {
		TextView name;
		TextView py;
		TextView code;
		TextView type;
		CheckBox checkbox;
	}

	@Override
	public int getCount() {
		return mBean.getItems().size();
	}

	@Override
	public Object getItem(int position) {
		return mBean.getItem(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.com_list_search_item, parent, false);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.py = (TextView) convertView.findViewById(R.id.py);
			holder.code = (TextView) convertView.findViewById(R.id.code);
			holder.type = (TextView) convertView.findViewById(R.id.type);
			holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		NetWorthBean bean = mBean.getItem(position);
		String jjpy = bean.getPinyin();

		if (TextUtils.isEmpty(jjpy)) {
			jjpy = "--";
		} else {
			jjpy = jjpy.toUpperCase();
		}
		if (mSearchKeyPos == null) {
			holder.name.setText(bean.getJjmc());
			holder.py.setText(jjpy);
			holder.code.setText(bean.getJjdm());
		} else {
			holder.name.setText(formatSearchSource(bean.getJjmc()));
			holder.py.setText(formatSearchSource(jjpy));
			holder.code.setText(formatSearchSource(bean.getJjdm()));
		}
		
		if (simuFlag) {
			holder.type.setVisibility(View.GONE);
		}else {
			holder.type.setVisibility(View.VISIBLE);
			holder.type.setText(FundUtils.getFundType(bean.getJjfl(), null));
		}
		
		
		holder.checkbox.setChecked(SelfConfig.isSelf(bean.getXunan()));
		holder.checkbox.setTag(position);
		holder.checkbox.setOnClickListener(this);
		return convertView;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder holder;
		if (convertView == null) {
			holder = new HeaderViewHolder();
			convertView = mInflater.inflate(R.layout.com_list_search_header, parent, false);
			holder.text = (CheckHeadText) convertView.findViewById(R.id.other_tips);
			holder.text.setHeadHeight(1f);
			holder.text.setHeadColor(0xffcccccc);
			convertView.setTag(holder); 
		} else {
			holder = (HeaderViewHolder) convertView.getTag();
		}

		// set header text as first char in name
		CharSequence headerChar = mBean.getItem(position).getPinyin().subSequence(0, 1);
		if (isQueryStatus()) {
			headerChar = "共" + mBean.size() + "条搜索结果";
		} else {
			boolean hotFlag = !TextUtils.isEmpty(mBean.getItem(position).getHbdr());
			if (!simuFlag && hotFlag) {
				headerChar = "热门搜索";
			}
		}
		holder.text.setText(headerChar.toString().toUpperCase());
		return convertView;
	}

	@Override
	public long getHeaderId(int position) {
		if (isQueryStatus()) {
			return -1;
		}
		if (mBean.getItem(position).getHbdr() != null) {
			return -1;
		} else {
			return mBean.getItem(position).getPinyin().charAt(0);
		}
	}

	// index
	@Override
	public Object[] getSections() {
		return mSectionLetters;
	}

	@Override
	public int getPositionForSection(int section) {
		if (isQueryStatus()) {
			return 0;
		}

		if (section >= mSectionIndex.length) {
			section = mSectionIndex.length - 1;
		} else if (section < 0) {
			section = 0;
		}
		return mSectionIndex[section];
	}

	@Override
	public int getSectionForPosition(int position) {
		if (isQueryStatus()) {
			return 0;
		}
		for (int i = 0; i < mSectionIndex.length; i++) {
			if (position < mSectionIndex[i]) {
				return i - 1;
			}
		}
		return mSectionIndex.length - 1;
	}

	@Override
	public Filter getFilter() {
		if (mFillter == null) {
			mFillter = new SearchFillter();
		}
		return mFillter;
	}

	/**
	 * index
	 * 
	 * @return
	 */
	private int[] getSectionIndex() {
		if (isQueryStatus()) {
			return new int[0];
		} else {
			if (mBean != null && mBean.size() != 0) {
				char lastFirstChar = mBean.getItem(0).getPinyin().charAt(0);
				ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
				sectionIndices.add(0);
				for (int i = 1; i < mBean.size(); i++) {
					String jjpy = mBean.getItem(i).getPinyin().toUpperCase();
					if (jjpy.equals("CHRONOS")) {
						System.out.println("CHRONOS");
					}
					if (simuFlag) {
						if (jjpy.charAt(0) != lastFirstChar) {
							lastFirstChar = jjpy.charAt(0);
							sectionIndices.add(i);
						}
					} else {
						String order = mBean.getItem(i).getHbdr();
						if (TextUtils.isEmpty(order)) {
							if (jjpy.charAt(0) != lastFirstChar) {
								lastFirstChar = jjpy.charAt(0);
								sectionIndices.add(i);
							}
						}
					}
				}

				int[] sections = new int[sectionIndices.size()];
				for (int i = 0; i < sectionIndices.size(); i++) {
					sections[i] = sectionIndices.get(i);
				}
				return sections;
			}
		}
		return new int[0];
	}

	/**
	 * indexValue
	 * 
	 * @return
	 */
	private String[] getSectionLetters() {
		if (mBean != null && mBean.size() != 0) {
			String[] letters = new String[mSectionIndex.length];
			String hotFlag = mBean.getItem(0).getHbdr();
			for (int i = 0; i < mSectionIndex.length; i++) {
				if (i == 0 && null != hotFlag) {
					letters[0] = "热";
				} else {
					letters[i] = Character
							.valueOf(mBean.getItem(mSectionIndex[i]).getPinyin().charAt(0))
							.toString().toUpperCase();
				}
			}
			return letters;
		} else {
			return new String[] { "-" };
		}
	}

	public class SearchFillter extends Filter {
		public static final String FilterSm = "-2";
		public static final String FilterGm = "-1";

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			// LogUtils.d("search", "publishResults 1" + "---" + constraint);
			queryString = constraint.toString().toUpperCase().replaceAll("\\s", "");
			if (queryString.length() > 0) {
				mSearchKeyPos = new int[queryString.length()];
			} else {
				mSearchKeyPos = null;
			}
			if (!TextUtils.isEmpty(queryString)) {
				// LogUtils.d("search", "publishResults 2-1");
				queringFlag = true;
				NetWorthListBean listBeans = new NetWorthListBean();
				try {
					listBeans = DbOperat.getInstance().searchAll(String.valueOf(constraint),
							simuFlag);
				} catch (WrapException e) {
					e.printStackTrace();
				}
				if (listBeans == null) {
					return results;
				}
				results.values = listBeans;
				results.count = listBeans.size();
			} else {
				// LogUtils.d("search", "publishResults 2-2");
				queringFlag = false;
			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			if (queringFlag) {
				boolean haveRes = !(results.values == null);
				if (haveRes) {
					mQueryCallBack.haveRes(true);
					mBean.clear();
					NetWorthListBean listBean = (NetWorthListBean) results.values;
					mBean.addItems(listBean);
					reloadIndex();
					notifyDataSetChanged();
					sendMsgToAnotherFrag(mBean);
				} else {
					sendMsgToAnotherFrag(null);
				}
				// 通知本身Fragment
				mQueryCallBack.haveRes(haveRes);
				mFrag.getmListView().setAreHeadersSticky(false);
				mFrag.getmListView().setFastScrollEnabled(false);
			} else {
				mQueryCallBack.haveRes(true);
				mFrag.getmListView().setFastScrollEnabled(true);
				mFrag.getmListView().setAreHeadersSticky(true);
				resetOriginData();
			}
		}

		/**
		 * 通知其他的frag，其他的flag可能没有搜索结果
		 * 
		 * @param listBean
		 */
		private void sendMsgToAnotherFrag(NetWorthListBean listBean) {
			// LogUtils.d("sendMsgToAnotherFrag");
			Intent iten = new Intent(FragSearchList.IntentAnotherFragBroadcast);
			iten.putExtra(ValConfig.IT_ENTITY, listBean);
			LocalBroadcastManager.getInstance(mContext).sendBroadcast(iten);
		}

	}

	public NetWorthListBean getBean() {
		return mBean;
	}

	/**
	 * hotlist
	 * 
	 * @param hot
	 */
	public void updateBean(NetWorthListBean hot) {
		mBean.addItems(hot.getItems(), false);
		reloadIndex();
		notifyDataSetChanged();
		if (mOriginListBean.getItem(0).getHbdr() == null) {
			mOriginListBean.clear();
			mOriginListBean.addItems(mBean);
		}
	}

	public void resetOriginData() {
		mBean.clear();
		mBean.addItems(mOriginListBean);
		reloadIndex();
		notifyDataSetChanged();
	}

	public void reloadIndex() {
		mSectionIndex = getSectionIndex();
		mSectionLetters = getSectionLetters();
	}

	public boolean isQueryStatus() {
		boolean b = true;
		if (TextUtils.isEmpty(queryString)) {
			b = false;
		}
		return b;
	}

	@Override
	public void onClick(View v) {
		CheckBox c = (CheckBox) v;
		Integer idx = (Integer) v.getTag();
		NetWorthBean temp = mBean.getItem(idx);
		int o;
		boolean os;
		if (!c.isChecked()) {
			o = SelfConfig.UNSynsDel;
			os = false;
		} else {
			o = SelfConfig.UNSynsAdd;
			os = true;
		}
		c.setChecked(os);
		temp.setXunan(o);
		FundUtils.updateOptional(mContext, temp.getJjdm(), o, false);

		String alys = "";
		if (isQueryStatus()) {
			alys = "搜索结果";
		} else if (temp.getHbdr() != null) {
			alys = "热搜";
		} else {
			alys = "字母表";
		}
		String evenId = o == SelfConfig.UNSynsDel ? Analytics.DELETE_CUSTOM_FUNDS
				: Analytics.ADD_CUSTOM_FUNDS;
		Analytics.onEvent(mContext, evenId, Analytics.KEY_FROM, alys);
	}

	/**
	 * 用户搜索结果回调
	 * 
	 * @author Administrator
	 * 
	 */
	public interface QueryCallBack {
		public void haveRes(boolean resFlag);
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

}
