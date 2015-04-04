package howbuy.android.util;

public class VersionDo {
	/**
	 * 
	 * @param version1
	 * @param version2
	 * @return 0 错误 1:version1大 2:version2大 3:一样大
	 */
	public static int ComparableVerison(String version1, String version2) {
		if (version1 == null || version2 == null) {
			return 0;
		} else {
			String[] version1Array = version1.split("\\.");
			String[] version2Array = version2.split("\\.");
			System.out.println(version1Array.length + "--" + version2Array.length);
			if (Integer.parseInt(version1Array[0]) > Integer.parseInt(version2Array[0])) {
				return 1;
			} else if (Integer.parseInt(version1Array[0]) == Integer.parseInt(version2Array[0])) {
				if (Integer.parseInt(version1Array[1]) > Integer.parseInt(version2Array[1])) {
					return 1;
				} else if (Integer.parseInt(version1Array[1]) == Integer.parseInt(version2Array[1])) {
					if (Integer.parseInt(version1Array[2]) > Integer.parseInt(version2Array[2])) {
						return 1;
					} else if (Integer.parseInt(version1Array[2]) == Integer.parseInt(version2Array[2])) {
						return 3;
					} else {
						return 2;
					}
				} else {
					return 2;
				}
			} else {
				return 2;
			}
		}
	}
}
