package howbuy.android.piggy.api.dto;

import java.util.ArrayList;

/**
 * 交易查询
 * 
 * @ClassName: TradeQueryDto.java
 * @Description:
 * @author yescpu yes.cpu@gmail.com
 * @date 2013-10-25上午11:14:24
 */
public class TradeQueryDto extends TopHeaderDto {
	private ArrayList<TradeQueryItemDto> shuz;
	private int itemCount;

	public ArrayList<TradeQueryItemDto> getShuz() {
		return shuz;
	}

	public void setShuz(ArrayList<TradeQueryItemDto> shuz) {
		this.shuz = shuz;
	}

	public int getCountItem() {
		return itemCount;
	}

	public void setCountItem(int countItem) {
		itemCount = countItem;
	}

}
