package howbuy.android.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ImageCompress {

	/**
	 * @param bmpOrg
	 * @param requestWidth
	 * @param requestHeight
	 * @return
	 */
	public static Bitmap getCompressBmp(Bitmap bmpOrg, int requestWidth, int requestHeight) {
		if (bmpOrg != null) {
			int width = bmpOrg.getWidth();
			int height = bmpOrg.getHeight();
			float wh = ((float) width) / height;// 图片
			float sWh = ((float) requestWidth) / requestHeight;// 手机
			float w;// 横向/竖向缩放的倍数

			if (wh > sWh) {
				w = ((float) requestWidth) / width;
			} else {
				w = ((float) requestHeight) / height;
			}

			// System.out.println("wh:"+wh+"--w:"+w+"--h:"+h+"--width:"+width+"--height:"+height);

			Matrix matrix = new Matrix();
			matrix.postScale(w, w);
			Bitmap resizeBitmap = Bitmap.createBitmap(bmpOrg, 0, 0, width, height, matrix, true);
			return resizeBitmap;
		}
		return null;
	}
}
