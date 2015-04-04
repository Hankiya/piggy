package howbuy.android.piggy.ui.fragment;

import howbuy.android.piggy.R;
import howbuy.android.piggy.ad.JpushOpera;
import howbuy.android.piggy.ad.JpushOpera.JpushType;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.ui.ProPertyActivity;
import howbuy.android.piggy.ui.WebViewActivity;
import howbuy.android.piggy.ui.base.AbstractFragment;
import howbuy.android.piggy.ui.fragment.PureWebFragment.PureType;
import howbuy.android.util.Cons;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.howbuy.wireless.entity.protobuf.AdvertListProtos.ICAdvert;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class AdFragment extends AbstractFragment implements OnItemClickListener {
	private DisplayImageOptions options;
	private ListView mListView;
	private List<ICAdvert> mListValue;
	private ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.logo).showImageForEmptyUri(R.drawable.logo).showImageOnFail(R.drawable.logo).cacheInMemory(true)
				.cacheOnDisc(false).considerExifParams(true).displayer(new RoundedBitmapDisplayer(10)).build();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.frag_ad, container, false);
		mListView = (ListView) view.findViewById(R.id.list);
		mListView.setOnItemClickListener(this);
		mListValue = (List<ICAdvert>) ApplicationParams.getInstance().getIntentData().get(ProPertyActivity.MapIntentAD);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		if (mListValue!=null&&mListValue.size()!=0) {
			mListView.setAdapter(new AdPage());
		}
	}

	public class AdPage extends BaseAdapter {
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (mListView==null) {
				return 0;
			}
				
			return mListValue.size();
			
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (mListView==null) {
				return 0;
			}
			return mListValue.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			if (mListView==null) {
				return 0;
			}
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.item_ad, parent, false);
				holder = new ViewHolder();
				holder.text = (TextView) convertView.findViewById(R.id.adtext);
				holder.image = (ImageView) convertView.findViewById(R.id.adimg);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			if (mListView!=null) {
				holder.text.setText(mListValue.get(position).getAdvTitle());
				// holder.image.setText(mListValue.get(position).getAdvTitle());
				// imageLoader.displayImage(mListValue.get(position).getAdvImageUrl(),
				// holder.image, options, animateFirstListener);
				imageLoader.displayImage(mListValue.get(position).getAdvImageUrl(), holder.image, animateFirstListener);
			}

			return convertView;
		}

		class ViewHolder {
			ImageView image;
			TextView text;
		}
	}

	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		ICAdvert i = (ICAdvert) parent.getItemAtPosition(position);
		String eventCode = i.getAdvEventCode();
		if (!TextUtils.isEmpty(eventCode)) {
			String k = JpushOpera.getPushCode(eventCode);
			String V = JpushOpera.getPushValue(eventCode);
			String Ext = JpushOpera.getPushExtra(eventCode);
			if (JpushOpera.getPushType(k)==JpushType.CommonWap) {
				Intent in1=new Intent(getActivity(),WebViewActivity.class);
				in1.putExtra(Cons.Intent_type, PureType.urlWearOperal.getType());
				in1.putExtra(Cons.Intent_id, V);
				in1.putExtra(Cons.Intent_name, i.getAdvTitle());
				startActivity(in1);
			}else {
				Toast.makeText(getActivity(), "储蓄罐仅支持wap页的广告", Toast.LENGTH_SHORT).show();
			}
		}else {
			Toast.makeText(getActivity(), "无效广告ID", Toast.LENGTH_SHORT).show();
		}
	}

}
