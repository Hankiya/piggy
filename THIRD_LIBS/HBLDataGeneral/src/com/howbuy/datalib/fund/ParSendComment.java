package com.howbuy.datalib.fund;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.CommonProtos.Common;
/**
 * // 发评论信息 Common
 * articleType：公募基金；2：私募基金；3：私募公司；4：TOT：私募经理
 * @author rexy  840094530@qq.com 
 * @date 2014-3-5 下午2:17:58
 */
public class ParSendComment extends AbsParam{
	public static final String URL="comments/commentadd.protobuf";
	
	//参数	值	是否能为空	描述
	private String code	=null;//	 	基金代码
	private int articleType	=0;//	N	基金类型
	private String userid	=null;//	 	 	用户id
	private String username	=null;//		Y	用户名称
	private String comments	=null;//	String	 	评论内容
	private String refCommentID	=null;//		Y	引用的评论的ID，为空则为发布评论
	
	
	public ParSendComment setParams(String code, int articleType,
			String userid, String username, String comments, String refCommentID) {
		this.code = code;
		this.articleType = articleType;
		this.userid = userid;
		this.username = username;
		this.comments = comments;
		this.refCommentID = refCommentID;
		return this;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setArticleType(int articleType) {
		this.articleType = articleType;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public void setRefCommentID(String refCommentID) {
		this.refCommentID = refCommentID;
	}

	public ParSendComment(int handType, Handler uiHand) {
		super(handType, uiHand, 0);
	}

	public ParSendComment(int handType, IReqNetFinished calbk) {
		super(handType, calbk, 0);
	}

	public ParSendComment(String key, int handType, IReqNetFinished calbk,
			Handler uiHand) {
		super(key, handType, calbk, uiHand, 0);
	}

	public ParSendComment(String key, IReqNetFinished calbk) {
		super(key, calbk, 0);
	}
	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
	   return  Common.parseFrom(is);
	}
	
	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}
	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("code", code);
		addArg("articleType",articleType );
		addArg("userid",userid );
		addArg("username",username );
		addArg("comments", comments);
		addArg("refCommentID",refCommentID );
	}
	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(true, true, false);
		return opt;
	}


     

}
