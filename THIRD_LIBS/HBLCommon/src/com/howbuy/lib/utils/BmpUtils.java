package com.howbuy.lib.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.util.Base64;
/**
 * @author rexy  840094530@qq.com 
 * @date 2013-12-9 下午11:23:33
 */
public class BmpUtils {
	/**
	 * get InputStream from a bitmap.
	 * 
	 * @param @param bitmap
	 */
	public static InputStream getBmpStream(Bitmap bitmap) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 85, os);
		return StreamUtils.toStream(os.toByteArray());
	}

	/**
	 * mCbCheck file whether or not it exists .
	 * 
	 * @param @param fulPath
	 * @param @param overide if file exist and overide is false then return
	 *        null;
	 * @return File
	 * @throws
	 */
	public static File checkFile(String fulPath, boolean overide) {
		File fi = new File(fulPath);
		fi.getParentFile().mkdirs();
		if (fi.exists()) {
			if (!overide) {
				return null;
			} else {
				return fi;
			}
		} else {
			try {
				fi.createNewFile();
				return fi;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	/**
	 * save a bitmap to a appointed full path name.
	 * 
	 * @param @param bmp
	 * @param @param fulPath
	 * @param @param overide whether or not to allow overide the old data.
	 * @return boolean true if success save it .
	 * @throws
	 */
	public static boolean saveBitmap(Bitmap bmp, String fulPath, boolean overide) {
		File fi = checkFile(fulPath, overide);
		if (fi != null) {
			try {
				return saveBitmap(bmp, new FileOutputStream(fi));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	/**
	 * save bitmap to a OutputStream by use Bitmap.compress.
	 * 
	 * @return true if success save else return false;
	 * @throws
	 */
	public static boolean saveBitmap(Bitmap bmp, OutputStream os) {
		BufferedOutputStream fout = new BufferedOutputStream(os);
		boolean result = bmp.compress(Bitmap.CompressFormat.JPEG, 85, fout);
		try {
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			StreamUtils.closeSilently(fout);
			StreamUtils.closeSilently(os);
		}
		return result;
	}

	/**
	 * encode a bitmamp to Base64 string . for small picture.
	 * 
	 * @param @param bmp
	 * @return String
	 * @throws
	 */
	public static String toBase64String(Bitmap bmp) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 60, stream);
		byte[] bytes = stream.toByteArray();
		return Base64.encodeToString(bytes, Base64.DEFAULT);
	}

	/**
	 * get a bitmap from a base64 string.
	 * 
	 * @param @param base64Str
	 * @return Bitmap
	 * @throws
	 */
	public Bitmap getBmpFromBase64s(String base64Str) {
		Bitmap bitmap = null;
		try {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(base64Str, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
					bitmapArray.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 
	 * @param fulPath
	 *            the full path of the picture with extras.
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Bitmap getBmpLocal(String fulPath) {
		File fi = new File(fulPath);
		if (fi.exists()) {
			try {
				return getBitmap(new BufferedInputStream(
						new FileInputStream(fi)), -1);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}

	public static Bitmap getBmpLocal(String fulPath, int minSideLenth,
			int maxNumOfPiexls) {
		File fi = new File(fulPath);
		if (fi.exists()) {
			try {
				BitmapFactory.Options opts = computeSampleOption(
						new FileInputStream(fi), minSideLenth, maxNumOfPiexls);
				return getBitmap(new FileInputStream(fi), opts);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param fulPath
	 *            theInputStream of the picture with extras.
	 * @param minSideLenth
	 *            the min side in piexls of the bitmap
	 * @param maxNumOfPiexls
	 *            the piexls of the result bitmap will less than maxNumOfPiexls.
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Bitmap getBitmap(byte[] bt, int minSideLenth,
			int maxNumOfPiexls) {
		Bitmap bmp = null;
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			bmp = BitmapFactory.decodeByteArray(bt, 0, bt.length, opts);
			opts.inJustDecodeBounds = false;
			opts.inDither = false;
			opts.inPurgeable = false;
			opts.inInputShareable = true;
			// opts.inTempStorage=new byte[1024*1024];
			opts.inSampleSize = BmpUtils.computeSampleSize(opts, minSideLenth,
					maxNumOfPiexls);

			bmp = BitmapFactory.decodeByteArray(bt, 0, bt.length, opts);
		} catch (OutOfMemoryError err) {
			return null;
		}
		return bmp;
	}

	/**
	 * * get bitmap from InputStream
	 * @throws IOException 
	 */
	public static Bitmap getBitmap(InputStream is,  
			int maxNumOfPiexls) throws IOException {
		BitmapFactory.Options opts = null;
		
			opts=new BitmapFactory.Options();
			opts.inJustDecodeBounds = false;
			opts.inDither = false;
			opts.inPurgeable = false;
			opts.inInputShareable = true;	
			if( maxNumOfPiexls<=0){
				opts.inSampleSize=(int) Math.sqrt( is.available()/maxNumOfPiexls);
		        opts.inSampleSize=Math.max(opts.inSampleSize, 1);
			}
	
		return getBitmap(is, opts);
	}

	/**
	 * get bitmap from InputStream with a Bitmap Opetions.
	 * 
	 * @param @param is
	 * @param @param opts
	 * @return Bitmap
	 * @throws
	 */
	public static Bitmap getBitmap(InputStream is, BitmapFactory.Options opts) {
		try {
			return BitmapFactory.decodeStream(is, null, opts);
		} catch (OutOfMemoryError err) {
			return null;
		} finally {
			StreamUtils.closeSilently(is);
		}
	}

	public static byte[] getBmpByte(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 85, output);
		if (needRecycle) {
			bmp.recycle();
		}
		byte[] result = output.toByteArray();
		StreamUtils.closeSilently(output);
		return result;
	}

	private static BitmapFactory.Options computeSampleOption(InputStream is,
			int minSideLenth, int maxNumOfPiexls) {
		BufferedInputStream bi = null;
		bi = new BufferedInputStream(is);
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(bi, null, opts);
		opts.inJustDecodeBounds = false;
		opts.inDither = false;
		opts.inPurgeable = false;
		opts.inInputShareable = true;
		// opts.inTempStorage=new byte[1024*1024];
		opts.inSampleSize = computeSampleSize(opts, minSideLenth,
				maxNumOfPiexls);
		StreamUtils.closeSilently(bi);
		StreamUtils.closeSilently(is);
		return opts;
	}

	/**
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return the scale values
	 */
	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels <= 0) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength <= 0) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 
	 * @param mapUrl
	 *            the local picture full path with a extras .
	 * @return
	 */
	public static String mapSize(String mapUrl) {
		String str = "";
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		try {
			BitmapFactory.decodeStream(new FileInputStream(new File(mapUrl)),
					null, o);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		str = o.outWidth + "*" + o.outHeight;
		return str;
	}

	public static Bitmap getBmpFromRes(Context cx, int resId) {
		return BitmapFactory.decodeResource(cx.getResources(), resId);
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
				.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_4444
				: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;
	}

	public static Bitmap setAlpha(Bitmap sourceImg, int number) {
		int bmpW = sourceImg.getWidth(), bmpH = sourceImg.getHeight();
		int[] argb = new int[bmpW * bmpH];
		sourceImg.getPixels(argb, 0, bmpW, 0, 0, bmpW, bmpH);// 閼惧嘲绶遍崶鍓у閻ㄥ嚈RGB閸婏拷
		number = number * 255 / 100;
		for (int i = 0; i < argb.length; i++) {

			argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);// 娣囶喗鏁奸張锟界彯2娴ｅ秶娈戦崐锟�
		}
		sourceImg.recycle();
		sourceImg = Bitmap.createBitmap(argb, bmpW, bmpH, Config.ARGB_8888);
		return sourceImg;
	}

	/**
	 * 
	 * @param originalImage
	 * @return a bitmap with a reflect effictive .
	 */
	public static Bitmap reflectBitmap(Bitmap originalImage, int reflectionGap,
			boolean recycle) {
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
				height / 2, width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height
				+ reflectionGap + height / 2), Config.ARGB_4444);

		Canvas canvas = new Canvas(bitmapWithReflection);

		canvas.drawBitmap(originalImage, 0, 0, null);
		Paint deafaultPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, deafaultPaint);
		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, width, 0, height
				+ reflectionGap, 0x20ffffff, 0xaaffffff, TileMode.CLAMP);
		paint.setShader(shader);
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);
		reflectionImage.recycle();
		if (recycle)
			originalImage.recycle();
		return bitmapWithReflection;
	}

	public static Bitmap zoomImage(Bitmap bgimage, int newWidth, int newHeight,
			boolean recycle) {
		int width = bgimage.getWidth();
		int height = bgimage.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, width, height,
				matrix, true);
		if (recycle) {
			bgimage.recycle();
		}
		return bitmap;
	}

	public static Bitmap zoomImage(Bitmap bgimage, int maxPiexl, boolean recycle) {
		int width = bgimage.getWidth();
		int height = bgimage.getHeight();
		Matrix matrix = new Matrix();
		float scale = (float) maxPiexl / (width * height);
		matrix.postScale(scale, scale);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, width, height,
				matrix, true);
		if (recycle) {
			bgimage.recycle();
		}
		return bitmap;
	}

	public static Bitmap cutBitmap(Bitmap bmp, int maxWith, int maxHeight,
			boolean recycle) {
		int srcW = bmp.getWidth(), srcH = bmp.getHeight();
		int w = 0, h = 0;
		float rate = srcH / (float) srcW;
		if (rate > 1.3) {
			rate = 1.3f;
		}
		w = srcW;
		h = (int) (srcW * rate);
		Rect res = new Rect(0, 0, srcW, h);

		float ratedes = maxHeight / (float) maxWith - rate;
		if (ratedes > 0) {
			ratedes = maxWith / (float) w;
			w = maxWith;
			h *= ratedes;
		} else if (ratedes < 0) {
			ratedes = maxHeight / (float) h;
			w *= ratedes;
			h = maxHeight;
		} else {
			if (w > maxWith) {
				w = maxWith;
			}
			if (h > maxHeight) {
				h = maxHeight;
			}
		}
		Bitmap newbmp = Bitmap.createBitmap(w, h, Config.ARGB_4444);
		Canvas cv = new Canvas(newbmp);
		cv.drawBitmap(bmp, res, new Rect(0, 0, w, h), null);
		if (recycle) {
			bmp.recycle();
		}
		return newbmp;
	}

	public static Bitmap roundedCorner(Bitmap bitmap, float round,
			boolean recycle) {
		int bmpW = bitmap.getWidth(), bmpH = bitmap.getHeight();
		Bitmap output = Bitmap.createBitmap(bmpW, bmpH, Config.ARGB_4444);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bmpW, bmpH);
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, round, round, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		if (recycle)
			bitmap.recycle();
		return output;
	}

	/**
	 * add a cenBmp to the center of srcBmp ,after this menthod,the cenBmp and
	 * srcBmp will be recycle.
	 * 
	 * @param src
	 *            the srcBmp.
	 * @param center
	 *            the cenBmp.
	 * @param recycleSrc
	 *            whether to recycle the bmp .
	 * @param recycleCenter
	 *            whether to recycle the bmp .
	 * @return a new Bmp.
	 * @return
	 */
	public static Bitmap addBmpToCenter(Bitmap src, Bitmap center,
			boolean recycleSrc, boolean recycleCenter) {
		int srcW = src.getWidth(), srcH = src.getHeight();
		int cenW = center.getWidth(), cenH = center.getHeight();
		int desW = (int) (srcW * 0.4f), desH = (int) (srcH * 0.3f), left = (srcW - desW) >> 1, top = (srcH - desH) >> 1;
		Bitmap newb = Bitmap.createBitmap(srcW, srcH, Config.ARGB_4444);
		Canvas cv = new Canvas(newb);
		cv.drawBitmap(src, 0, 0, null);
		cv.drawBitmap(center, new Rect(0, 0, cenW, cenH), new Rect(left, top,
				left + desW, top + desH), null);
		cv.save(Canvas.ALL_SAVE_FLAG);
		cv.restore();
		if (recycleSrc)
			src.recycle();
		if (recycleCenter)
			center.recycle();

		return newb;
	}

	public static Bitmap addToRightBottom(Bitmap src, Bitmap rbtom,
			boolean recycleSrc, boolean recycleBtom) {
		int srcW = src.getWidth(), srcH = src.getHeight();
		int cenW = rbtom.getWidth(), cenH = rbtom.getHeight();
		Bitmap newb = Bitmap.createBitmap(srcW, srcH, Config.ARGB_4444);
		Canvas cv = new Canvas(newb);
		cv.drawBitmap(src, 0, 0, null);
		cv.drawBitmap(rbtom, srcW - cenW, srcH - cenH, null);
		cv.save(Canvas.ALL_SAVE_FLAG);
		cv.restore();
		if (recycleSrc)
			src.recycle();
		if (recycleBtom)
			rbtom.recycle();
		return newb;
	}

}
