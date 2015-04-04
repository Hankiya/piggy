package com.howbuy.lib.interfaces;

import com.howbuy.lib.net.ReqOpt;
import com.howbuy.lib.net.ReqResult;

/**
 * @author rexy  840094530@qq.com 
 * @date 2013-12-15 下午3:01:30
 */
public interface IReqFinished {
    public static final int HAND_REQFINISHED=1024;
	public void onRepFinished(ReqResult<ReqOpt> result);
}