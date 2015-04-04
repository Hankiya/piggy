package howbuy.android.piggy.loader;

import howbuy.android.piggy.api.DispatchAccessData;
import howbuy.android.piggy.api.dto.UserTypeDto;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

/**
 * 
 * @author yescpu
 * 
 */
public class QueryActiveLoader extends AsyncTaskLoader<UserTypeDto> {
	private String idNo;
	private String idType;

	public QueryActiveLoader(Context context, String idNo, String idType) {
		super(context);
		// TODO Auto-generated constructor stub
		this.idNo = idNo;
		this.idType = idType;
	}

	
	@Override
	public UserTypeDto loadInBackground() {
		// TODO Auto-generated method stub
		Log.i("message", toString());
		return DispatchAccessData.getInstance().activeQuery(idNo,idType);
	}


	@Override
	public String toString() {
		return "QueryActiveLoader [idNo=" + idNo + ", idType=" + idType + "]";
	}
	
	

}
