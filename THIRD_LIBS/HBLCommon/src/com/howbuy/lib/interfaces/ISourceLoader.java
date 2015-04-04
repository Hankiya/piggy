package com.howbuy.lib.interfaces;

import java.io.InputStream;

import com.howbuy.lib.net.CacheOpt;

public interface ISourceLoader extends IReqNetFinished{
   InputStream loadResource(CacheOpt opt,String fileName) throws Exception;
   InputStream saveResource(CacheOpt opt,InputStream is,String fileName) throws Exception;
}
