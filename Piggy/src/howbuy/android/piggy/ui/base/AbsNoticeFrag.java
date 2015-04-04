package howbuy.android.piggy.ui.base;

import howbuy.android.piggy.UserUtil;
import howbuy.android.piggy.api.dto.NoticeDto;
import howbuy.android.piggy.api.dto.NoticeDtoList;
import howbuy.android.piggy.help.NoticeHelp;
import howbuy.android.piggy.service.UpdateUserDataService;
import howbuy.android.util.Cons;
import android.content.Intent;
import android.os.Bundle;

public abstract class AbsNoticeFrag extends AbstractFragment {
	private NoticeHelp mNoticeHelp;

	@Override
	public boolean isShoudRegisterReciver() {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	@Override
	public void onServiceRqCallBack(Intent taskData, boolean isCurrPage) {
		// TODO Auto-generated method stub
		super.onServiceRqCallBack(taskData, isCurrPage);
		String taskType=taskData.getStringExtra(Cons.Intent_type);
		if(UpdateUserDataService.TaskType_Notice.equals(taskType)&&isAdded()){
			mNoticeHelp.findView(getActivity(),null);
			NoticeDtoList nlistDtoList=taskData.getParcelableExtra(Cons.Intent_bean);
			if (nlistDtoList!=null) {
				NoticeDto nd= NoticeHelp.Util.getNotice(nlistDtoList, getNoticeType());
				mNoticeHelp.showOrHide(nd);
			}
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		if (mNoticeHelp==null) {
			mNoticeHelp=new NoticeHelp();
		}
		String reType;
		if (UserUtil.isLogin()) {
			reType=NoticeHelp.Req_Type_Login;
		}else {
			reType=NoticeHelp.Req_Type_UnLogin;
		}
		NoticeHelp.Util.launcherNoticeTask(reType, "notice");
	}
	
	
	public abstract String getNoticeType();
	
}
