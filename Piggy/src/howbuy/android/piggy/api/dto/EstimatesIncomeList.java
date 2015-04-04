package howbuy.android.piggy.api.dto;

import java.util.ArrayList;

public class EstimatesIncomeList extends TopHeaderDto{

	private ArrayList<EstimatesIncomeItem> mList;

	public ArrayList<EstimatesIncomeItem> getmList() {
		if (mList==null) {
			mList=new ArrayList<EstimatesIncomeItem>();
		}
		return mList;
	}

	public void setmList(ArrayList<EstimatesIncomeItem> mList) {
		this.mList = mList;
	}
	
}
