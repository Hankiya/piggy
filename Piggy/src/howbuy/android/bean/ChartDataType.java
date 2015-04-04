package howbuy.android.bean;

import java.util.HashMap;

public class ChartDataType {
	// // =00,工商企业=20,房地产=11,基础设施=13,金融=12,工矿企业=22,其他=99
	public static final String All = "全部";
	public static final String Bussiness = "工商企业";
	public static final String estate = "房地产";
	public static final String Basic = "基础设施";
	public static final String Financial = "金融";
	public static final String Mining = "工矿企业";
	public static final String other = "其他";

	public static String name(String desType) {
		if (desType.equals(All)) {
			return "00";
		} else if (desType.equals(Bussiness)) {
			return "20";
		} else if (desType.equals(estate)) {
			return "11";
		} else if (desType.equals(Basic)) {
			return "13";
		} else if (desType.equals(Financial)) {
			return "12";
		} else if (desType.equals(Mining)) {
			return "22";
		} else if (desType.equals(other)) {
			return "99";
		}
		return "00";
	}

	static HashMap<String, String> type = new HashMap<String, String>();

	// // 成员变量
	// private String typeName;
	// private String typeVlaue;
	//
	// // 构造方法
	// private ChartDataType(String name, String typeVlaue) {
	// this.typeName = name;
	// this.typeVlaue = typeVlaue;
	// }
	//
	// public String getTypeName() {
	// return typeName;
	// }
	//
	// public void setTypeName(String typeName) {
	// this.typeName = typeName;
	// }
	//
	// public String getTypeVlaue() {
	// return typeVlaue;
	// }
	//
	// public void setTypeVlaue(String typeVlaue) {
	// this.typeVlaue = typeVlaue;
	// }
	//
	// // 普通方法
	// // public static String getName(int index) {
	// // for (ChartDataType c : ChartDataType.values()) {
	// // if (c.getIndex() == index) {
	// // return c.typeName;
	// // }
	// // }
	// // return null;
	// // }
	//
	// // get set 方法

}