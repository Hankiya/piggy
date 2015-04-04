package howbuy.android.piggy.ad;

import howbuy.android.util.AndroidUtil;

public class AdTools {
	public static final String AdActionId = "4";
	public static int[] s1 = new int[] { 480, 150 };
	public static int[] s2 = new int[] { 720, 225 };
	public static int[] s3 = new int[] { 1080, 338 };
//	public static int[] s1 = new int[] { 480, 240 };
//	public static int[] s2 = new int[] { 720, 360 };
//	public static int[] s3 = new int[] { 1080, 540 };
	public static AdTools adTools;

	public static AdTools getInstance() {
		if (adTools == null) {
			adTools = new AdTools();
		}
		return adTools;
	}

	public int adWidth;
	public int adHeight;

	private AdTools() {
		int screenWidth = AndroidUtil.getWidth();
		if (screenWidth < 480 || screenWidth == 480) {
			setAdHeight(s1[1]);
			setAdWidth(s1[0]);
		} else if (screenWidth > 480 && screenWidth <= 800) {
			setAdHeight(s2[1]);
			setAdWidth(s2[0]);
		} else if (screenWidth > 800) {
			setAdHeight(s3[1]);
			setAdWidth(s3[0]);
		} else {
			setAdHeight(s1[1]);
			setAdWidth(s1[0]);
		}
	}

	public int getAdWidth() {
		return adWidth;
	}

	public void setAdWidth(int adWidth) {
		this.adWidth = adWidth;
	}

	public int getAdHeight() {
		return adHeight;
	}

	public void setAdHeight(int adHeight) {
		this.adHeight = adHeight;
	}

}
