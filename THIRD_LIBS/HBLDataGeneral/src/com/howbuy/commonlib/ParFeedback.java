package com.howbuy.commonlib;

import java.io.InputStream;
import java.util.HashMap;

import android.os.Handler;

import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.AbsParam;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.wireless.entity.protobuf.trustdaquan.CommonProtos.Common;

/**
 * 用户反馈 返回参数：（仅公共参数）Common
 * 
 * @author rexy 840094530@qq.com
 * @date 2014-2-28 下午5:26:41
 */
public class ParFeedback extends AbsParam {
	private static final String URL = "feedback/addfeedback.protobuf"; // 反馈
	// 参数 值 能否能为空 描述
	private String phone = null;// Y 用户手机号码
	private int evaluation = 5; // N 评分
	private String content = null; // N 评价内容(osVersion、root标志，解锁，越狱)
	private String name;// string N 称呼
	private String email; // string Y email

	public ParFeedback(int handType, Handler uiHand) {
		super(handType, uiHand, 0);
	}

	public ParFeedback(int handType, IReqNetFinished calbk) {
		super(handType, calbk, 0);
	}

	public ParFeedback(String key, IReqNetFinished calbk) {
		super(key, calbk, 0);
	}

	public ParFeedback(String key, int handType, IReqNetFinished calbk, Handler uiHand) {
		super(key, handType, calbk, uiHand, 0);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setEvaluation(int evaluation) {
		this.evaluation = evaluation;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public AbsParam setParams(String phone, int evaluation, String content, String name,
			String email) {
		this.phone = phone;
		this.evaluation = evaluation;
		this.content = content;
		this.name = name;
		this.email = email;
		return this;
	}

	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {
		return Common.parseFrom(is);
	}

	@Override
	protected String getUrl() {
		return buildUrl(URL);
	}

	@Override
	protected void buildArgs(HashMap<String, String> args) {
		addArg("phone", phone);
		addArg("evaluation", evaluation);
		addArg("content", content);
		addArg("name", name);
		addArg("email", email);
	}

	@Override
	protected CacheOpt initOptions(CacheOpt opt) {
		setParamType(true, true, false);
		return opt;
	}

}
