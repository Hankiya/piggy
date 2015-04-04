package com.howbuy.lib.net;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map.Entry;

import android.graphics.Bitmap;

import com.howbuy.lib.utils.BmpUtils;
/**
 * @author rexy  840094530@qq.com 
 * @date 2013-12-15 下午3:02:50
 */
public class LocalImgLoader {
	private static final HashMap<String, SoftReference<Bitmap>> mCache = new HashMap<String, SoftReference<Bitmap>>();

	public static void clearCache(boolean recycle, boolean calGc) {
		Bitmap bmp = null;
		if (recycle) {
			for (Entry<String, SoftReference<Bitmap>> entery : mCache
					.entrySet()) {
				bmp = entery.getValue().get();
				if (bmp != null && !bmp.isRecycled()) {
					bmp.recycle();
					bmp = null;
				}
			}
		}
		mCache.clear();
		if (calGc) {
			System.gc();
		}
	}

	public static boolean removeCache(String path, boolean recycle) {
		if (mCache.containsKey(path)) {
			SoftReference<Bitmap> ref = mCache.get(path);
			Bitmap bmp = ref.get();
			if (bmp != null && recycle && !bmp.isRecycled()) {
				bmp.recycle();
				bmp = null;
			}
			mCache.remove(path);
			return true;
		}
		return false;
	}

	public static boolean removeCache(String path, int minLength, int maxPiexl,
			boolean recycle) {
		path +=("_" + minLength + "_" + maxPiexl);
		if (mCache.containsKey(path)) {
			SoftReference<Bitmap> ref = mCache.get(path);
			Bitmap bmp = ref.get();
			if (bmp != null && recycle && !bmp.isRecycled()) {
				bmp.recycle();
				bmp = null;
			}
			mCache.remove(path);
			return true;
		}
		return false;
	}

	public static Bitmap checkCache(String path) {
		Bitmap bmp = null;
		if (mCache.containsKey(path)) {
			SoftReference<Bitmap> ref = mCache.get(path);
			bmp = ref.get();
			if (bmp == null) {
				mCache.remove(path);
			}
		}
		return bmp;
	}

	public static Bitmap checkLocal(String path) {
		return BmpUtils.getBmpLocal(path);
	}

	public static Bitmap checkLocal(String path, int minLength, int maxPiexl) {
		return BmpUtils.getBmpLocal(path, minLength, maxPiexl);
	}

	public static void addToCache(Bitmap bmp, String path) {
		mCache.put(path, new SoftReference<Bitmap>(bmp));
	}

	public static Bitmap checkCacheAdLocal(String path) {
		Bitmap bmp = null;
		bmp = checkCache(path);
		if (bmp != null) {
			return bmp;
		}
		bmp = checkLocal(path);
		if (bmp != null) {
			addToCache(bmp, path);
			return bmp;
		}
		return bmp;
	}

	public static Bitmap checkCacheAdLocal(String path, int minLength,
			int maxPiexl) {
		Bitmap bmp = null;
		bmp = checkCache(path + "_" + minLength + "_" + maxPiexl);
		if (bmp != null) {
			return bmp;
		}
		bmp = checkLocal(path, minLength, maxPiexl);
		if (bmp != null) {
			addToCache(bmp, path + "_" + minLength + "_" + maxPiexl);
			return bmp;
		}
		return bmp;
	}

}
