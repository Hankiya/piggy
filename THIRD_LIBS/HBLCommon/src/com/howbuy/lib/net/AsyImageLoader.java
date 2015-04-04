package com.howbuy.lib.net;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

/**
 * @author rexy  840094530@qq.com 
 * @date 2013-12-15 下午3:02:31
 */
public class AsyImageLoader {
    private static final String IMAGE_FILE_PATH =GetWorkDir() + "images";

    //URL <-> ImageView控件的Map，方便下载图片完成后调用设置图片(多个ImageView可能图片相同，所以用List关联)
    private static final Map<String, ArrayList<ImageView>> imageViews = new HashMap<String, ArrayList<ImageView>>();
    //URL <-> Bitmap 的Map，方便回收Bitmap
    private static final Map<String, SoftReference<Bitmap>>  bitmaps = new HashMap<String, SoftReference<Bitmap>>();

    /**
     * 加载图片。首先从本地查找，如果有则直接显示，如果没有则根据url下载后显示
     * 不需要图片是，请确保调用:{@link #recycleBitmap}或{@link #recycleAllBitmap}释放Bitmap资源。
     * @param iv 需要显示图片的ImageView控件
     * @param url 图片的url地址
     * @param defIconId 默认图片
     * @param fileNameFix 图片文件名称附加字符，为了区分不同尺寸图片
     */
    public static void loadImage( ImageView iv, String url, int defIconId, String fileNameFix) {
        if( iv == null || url == null || url.length() <= 0 || defIconId < 0 ) {
            return;
        }
        //从URL中截取图片文件名:以最后一个'/'之后到最后为文件名
        int lastIndexOfDot = url.lastIndexOf('.');
        String picName = url.substring( url.lastIndexOf('/'), lastIndexOfDot )
                + fileNameFix + url.substring( lastIndexOfDot );
        boolean hasLocalImage = true;

        //首先查找是否已经有被加载的图片
        SoftReference<Bitmap> target = bitmaps.get(url);
        if( target != null && target.get() != null ) {  // 这张图片已经被加载过可以直接使用
            iv.setImageBitmap( target.get() );
        } else {
            //先从缓存中清除
            bitmaps.remove(url);

            //本地查找是否已经有下载好的图片存在
            File imagePath = new File( IMAGE_FILE_PATH );
            File picFile = null;
            if( !imagePath.exists() ) {  // 图片目录不存在
                imagePath.mkdirs();
                hasLocalImage = false;
            } else {
                picFile = new File(imagePath, picName);
                if( !picFile.exists() ) { // 图片目录下该图片不存在
                    try {
                        picFile.createNewFile();
                    } catch ( IOException e ) {
                        return;
                    }
                    hasLocalImage = false;
                }
            }
            if( hasLocalImage == true ) {
                Bitmap bmp = BitmapFactory.decodeFile( picFile.getAbsolutePath() );
                iv.setImageBitmap( bmp );
                bitmaps.put( url, new SoftReference<Bitmap>(bmp) );
            } else { // 本地没有此图片存在，则需要下载
                iv.setImageResource( defIconId );
                asynLoadImage(iv, picFile, url);
            }
        }
    }

    /**
     * 释放指定Bitmap资源
     */
    synchronized public static void recycleBitmap(String url) {
        if( url != null && url.length() > 0 && bitmaps != null && !bitmaps.isEmpty() ) {
            SoftReference<Bitmap> ref = bitmaps.get(url);
            Bitmap bmp = ref.get();
            if( bmp != null && !bmp.isRecycled() ) {
                bmp.recycle();
            }
            bitmaps.remove(url);
        }
    }

    /**
     * 释放所有的Bitmap资源
     */
    synchronized public static void recycleAllBitmap() {
        if( bitmaps != null && !bitmaps.isEmpty() ) {
            Collection<SoftReference<Bitmap>> values = bitmaps.values();
            Iterator<SoftReference<Bitmap>> itr = values.iterator();
            while( itr.hasNext() ) {
                Bitmap bmp = itr.next().get();
                if( bmp != null && !bmp.isRecycled() ) {
                    bmp.recycle();
                }
            }
            bitmaps.clear();
        }
    }

    //开启线程下载图片
    synchronized private static void asynLoadImage(ImageView iv, final File picFile, final String url) {
        //检查是否已经在下载中
        ArrayList<ImageView> ivs = imageViews.get(url);
        if( ivs != null ) {  // 在下载中，将ImageView加入即可
            ivs.add(iv);
            return;
        } else { // 没有相同图片在下载
            ivs = new ArrayList<ImageView>();
            ivs.add(iv);
            imageViews.put(url, ivs);
        }

        new Thread() {
            @Override
            public void run() {
                Bitmap bmp = downloadImage(picFile, url);

                Message msg = new Message();
                msg.what = MSG_IMAGE_DOWNLOAD_FINISHED;
                msg.obj = bmp;
                Bundle data = new Bundle();
                data.putString("URL", url);
                msg.setData(data);
                innerHandler.sendMessage(msg);
            }
        }.start();
    }

    private static final int MSG_IMAGE_DOWNLOAD_FINISHED = 1;
    private static Handler innerHandler = new Handler() {
        @SuppressLint("NewApi")
		@Override
        public void handleMessage(Message msg) {
            if(MSG_IMAGE_DOWNLOAD_FINISHED == msg.what) {
                Bundle data = msg.getData();
                String url = data.getString("URL");
                imageDownloadFinished( url, (Bitmap)msg.obj );
            }
        }
    };

    //图片下载完成之后回调
    synchronized private static void imageDownloadFinished(String url, Bitmap bmp) {
        if( bmp == null ) {
            return;
        }
        bitmaps.put(url, new SoftReference<Bitmap>(bmp));
        ArrayList<ImageView> ivs = imageViews.get(url);
        if( ivs != null  ) {
            Iterator<ImageView> itr = ivs.iterator();
            while( itr.hasNext() ) {
                ImageView iv = itr.next();
                iv.setImageBitmap(bmp);
            }
        }
    }

    private static Bitmap downloadImage( File picFile, String url ) {
        Bitmap bitmap = null;
        InputStream is = null;
        BufferedOutputStream bos = null;
        try {
            // 显示网络上的图片
            URL myFileUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();

            is = conn.getInputStream();
            bos = new BufferedOutputStream(new FileOutputStream(picFile));

            byte[] buf = new byte[1024];
            int len = 0;
            // 将网络上的图片存储到本地
            while ((len = is.read(buf)) > 0) {
                bos.write(buf, 0, len);
            }
            bos.close();
            // 从本地加载图片
            bitmap = BitmapFactory.decodeFile( picFile.getAbsolutePath() );
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if( is != null ) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
    
    public static String GetWorkDir() { 
    	String operateDir=null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        File dirs;
        if (sdCardExist) {
            operateDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "HowBuy" + File.separator;
            dirs = new File(operateDir);
        } else {
            operateDir = Environment.getRootDirectory().getAbsolutePath() + File.separator + "HowBuy" + File.separator;
            dirs = new File(operateDir);
        }

        if (!dirs.exists()) {
            boolean suc = dirs.mkdirs();
            if (!suc) {
                return "";
            }
        }
        return operateDir;
    }
}
