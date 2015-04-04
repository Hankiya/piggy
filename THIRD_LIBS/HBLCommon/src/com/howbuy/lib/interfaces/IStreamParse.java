package com.howbuy.lib.interfaces;

import java.io.InputStream;

import com.howbuy.lib.net.ReqNetOpt;
/**
 * @author rexy  840094530@qq.com 
 * @date 2013-12-15 下午3:01:19
 */
public interface IStreamParse {
    /**
     * parse a InputeStream to Object such as String or Byte[] or Object that have implements Serializable or Parable.
     * @param @param is
     * @param @param opt 
     * @throws
     */
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception;
}
