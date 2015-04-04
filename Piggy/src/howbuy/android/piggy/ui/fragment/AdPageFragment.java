package howbuy.android.piggy.ui.fragment;

import howbuy.android.piggy.R;
import howbuy.android.piggy.ad.JpushOpera;
import howbuy.android.piggy.ad.PushDispatch;
import howbuy.android.piggy.ui.base.AbstractFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AdPageFragment extends AbstractFragment implements OnClickListener {
	ImageView mImageView;
//	ProgressBar mProgressBar;
	TextView madText;
	String mImageUrl;
	String mActionString;
	String mIdString;
	String mTitle;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	private static final String NAME="AdPageFragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.load).showImageForEmptyUri(R.drawable.load).showImageOnFail(R.drawable.load).cacheInMemory(true)
				.cacheOnDisc(false).considerExifParams(false).build();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.frag_adpage, null);
		view.setOnClickListener(this);
		mImageView = (ImageView) view.findViewById(R.id.adimage);
		mImageView.setOnClickListener(this);
//		mProgressBar = (ProgressBar) view.findViewById(R.id.adprogerss);
		madText = (TextView) view.findViewById(R.id.adtext);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null) {
			mImageUrl = savedInstanceState.getString("advImageUrl");
			mIdString = savedInstanceState.getString("advID");
			mActionString = savedInstanceState.getString("advEventCode");
			mTitle = savedInstanceState.getString("advTitle");
		} else {
			mImageUrl = getArguments().getString("advImageUrl");
			mIdString = getArguments().getString("advID");
			mActionString = getArguments().getString("advEventCode");
			mTitle = getArguments().getString("advTitle");
		}

		switch (getCurrentState(savedInstanceState)) {
		case ACTIVITY_DESTROY_AND_CREATE:
			System.out.println("ACTIVITY_DESTROY_AND_CREATE");
//			HowBuyAndroidUtil.setVisibilityNoRepate(mProgressBar, View.GONE);
			madText.setText(mImageUrl);
			imageLoader.displayImage(mImageUrl.trim(), mImageView,options);
			break;
		case FIRST_TIME_START:
			System.out.println("FIRST_TIME_START");
//			HowBuyAndroidUtil.setVisibilityNoRepate(mProgressBar, View.VISIBLE);
			Log.d(NAME, "imageid="+mImageView.getId());
			imageLoader.displayImage(mImageUrl.trim(), mImageView,options);
			break;
		case SCREEN_ROTATE:
			System.out.println("SCREEN_ROTATE");
			imageLoader.displayImage(mImageUrl.trim(), mImageView,options);
//			HowBuyAndroidUtil.setVisibilityNoRepate(mProgressBar, View.GONE);
			break;
		default:
			break;
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putString("advID", mIdString);
		outState.putString("advTitle", mTitle);
		outState.putString("advImageUrl", mImageUrl);
		outState.putString("advEventCode", mActionString);
	}


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		if (!TextUtils.isEmpty(mActionString)) {
			String k = JpushOpera.getPushCode(mActionString);
			String V = JpushOpera.getPushValue(mActionString);
			String Ext = JpushOpera.getPushExtra(mActionString);
			if (TextUtils.isEmpty(mTitle)) {
				mTitle = "推送消息";
			}
			new PushDispatch(getActivity()).despatchFragmentClass(1, k, V, Ext, mTitle);
		} else {
			Toast.makeText(getActivity(), "无效广告ID", Toast.LENGTH_SHORT).show();
		}
		
		
	}

}
