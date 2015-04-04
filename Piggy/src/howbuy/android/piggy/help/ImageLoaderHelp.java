package howbuy.android.piggy.help;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Administrator on 2014/8/23.
 */
public class ImageLoaderHelp {
    private ImageLoader mImageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options= new DisplayImageOptions.Builder().showImageOnLoading(howbuy.android.piggy.R.drawable.abs__ic_clear).showImageForEmptyUri(howbuy.android.piggy.R.drawable.abs__ic_clear).showImageOnFail(howbuy.android.piggy.R.drawable.abs__ic_clear).cacheInMemory(true)
            .cacheOnDisc(true).considerExifParams(false).build();

    public DisplayImageOptions getOptions() {
        return options;
    }

    public void setOptions(DisplayImageOptions options) {
        this.options = options;
    }

    public ImageLoader getmImageLoader() {
        return mImageLoader;
    }

    public void setmImageLoader(ImageLoader mImageLoader) {
        this.mImageLoader = mImageLoader;
    }

    public void  disImage(String url,ImageView imageView){
        getmImageLoader().displayImage(url,imageView,getOptions());
    }

}
