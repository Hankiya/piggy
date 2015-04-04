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
	private ArrayList<TradeQueryItemDto> tradeList;
	private String pageNo;
	private String totalPage;
	private String pageSize;
	private int listNumber;
	public ArrayList<TradeQueryItemDto> getList() {
		return tradeList;
	}
	public String getPageNo() {
		return pageNo;
	}
	public String getTotalPage() {
		return totalPage;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setList(ArrayList<TradeQueryItemDto> list) {
		this.tradeList = list;
	}
	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
	public void setTotalPage(String totalPage) {
		this.totalPage = totalPage;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public int getCountItem() {
		return listNumber;
	}
	public void setCountItem(int countItem) {
		this.listNumber = countItem;
	}
	
	

}
