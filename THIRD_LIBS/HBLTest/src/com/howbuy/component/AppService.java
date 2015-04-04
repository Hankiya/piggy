package com.howbuy.component;

import com.howbuy.lib.compont.GlobalServiceAbs;
import com.howbuy.lib.compont.GlobalServiceMger.ServiceTask;
import com.howbuy.lib.net.ReqOpt;

public class AppService extends GlobalServiceAbs {
	@Override
	protected void executeTask(ServiceTask task, boolean fromTimer) {
		try {
			Thread.sleep(2000);
			notifyFinished(task);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void cancleTask(ReqOpt opt) {
	}
}
