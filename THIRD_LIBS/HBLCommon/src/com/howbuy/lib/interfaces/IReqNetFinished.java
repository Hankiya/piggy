package com.howbuy.lib.interfaces;

import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqResult;

/**
 * @author rexy  840094530@qq.com 
 * @date 2013-12-15 下午3:01:30
 */
public interface IReqNetFinished {
    public static final int HAND_REQFINISHED=1024;
	public void onRepNetFinished(ReqResult<ReqNetOpt> result);
}