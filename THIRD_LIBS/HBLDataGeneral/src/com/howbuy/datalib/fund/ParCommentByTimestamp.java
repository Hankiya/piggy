package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.CommentInfoListProto.CommentInfoList;
/**
 * 根据时间戳获取评论信息  CommentInfoList
 *  articleType：1：公募基金；2：私募基金；3：私募公司；4：TOT；5：私募经理
 * @author rexy  840094530@qq.com 
 * @date 2014-3-5 下午2:17:58
 */
public class ParCommentByTimestamp extends AbsParam{
	public static final String URL="comments/fundcommentlist.protobuf";
	
	//参数	类型 是否能为空描述
	private String code	=null;//	 	基金代码
	private int articleType	=0;//	N	基金类型
	private String posttime=null;//当前时间戳
	
 
	public ParCommentByTimestamp setParams( String code, int articleType,
			String posttime) {
		this.code = code;
		this.articleType = articleType;
		this.posttime = posttime;
		return this;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setArticleType(int articleType) {
		this.articleType = articleType;
	}
	public void setPosttime(String posttime) {
		this.posttime = posttime;
	} 
	public ParCommentByTimestamp(long cacheTime) {
		super(cacheTime);
	}
	public ParCommentByTimestamp(int handType, Handler uiHand, long cacheTime) {
		super(handType, uiHand, cacheTime);
	}

	public ParCommentByTimestamp(int handType, IReqNetFinished calbk, long cacheTime) {
		super(handType, calbk, cacheTime);
	}

	public ParCommentByTimestamp(String key, int handType, IReqNetFinished calbk,
			Handler uiHand, long cacheTime) {
		super(key, handType, calbk, uiHand, cacheTime);
	}

	public ParCommentByTimestamp(String key, IReqNetFinished calbk, long cacheTime) {
		super(key, calbk, cacheTime);
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	   return  CommentInfoList.parseFrom(is);
	}
	
	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("code", code);
		addArg("articleType",articleType );
		addArg("posttime",posttime );
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(false, true, false);
		return opt;
	}


     

}
