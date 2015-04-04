package howbuy.android.piggy.loader;

import howbuy.android.piggy.api.DispatchAccessData;
import howbuy.android.piggy.api.dto.UpdateDto;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * 
 * @author yescpu
 * 
 */
public class CheckUpdateLoader extends AsyncTaskLoader<UpdateDto> {

	public CheckUpdateLoader(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public UpdateDto loadInBackground() {
		// TODO Auto-generated method stub
		return DispatchAccessData.getInstance().checkUpdate();
	}

}
