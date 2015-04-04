package com.howbuy.lib.net;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.content.Context;
import android.net.Proxy;

import com.howbuy.lib.error.WrapException;

/**
 * @author rexy 840094530@qq.com
 * @date 2014-2-20 下午5:07:16
 */
public class HttpClient {
	private static int TIMEOUT_CONNECT = 30000;
	private static int TIMEOUT_READ = 400000;
    private static int MAX_ROUTE_CONNECTIONS=10;
    private static int MAX_CONNECTIONS=60;
	private static HttpClient mClient;
	private DefaultHttpClient mDefClient = null;

	public DefaultHttpClient getClient() {
		if (mDefClient == null) {
			mDefClient = getHttpClient();
		}
		return mDefClient;
	}

	public static synchronized HttpClient getInstance() {
		if (mClient == null) {
			mClient = new HttpClient();
		}
		return mClient;
	}

	private DefaultHttpClient getHttpClient() {
		HttpParams params = new BasicHttpParams();
		// 设置一些基本参数
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, UrlUtils.CHARSET_UTF8);
		HttpProtocolParams.setUseExpectContinue(params, false);
		HttpProtocolParams.setUserAgent(params,
				"Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
						+ "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
		/* 从连接池中取连接的超时时间 */
		ConnManagerParams.setTimeout(params, 30 * 1000);
		ConnManagerParams.setMaxTotalConnections(params, MAX_CONNECTIONS);
		/* 连接超时 */
		HttpConnectionParams.setConnectionTimeout(params, TIMEOUT_CONNECT);
		/* 请求超时 */
		HttpConnectionParams.setSoTimeout(params, TIMEOUT_READ);
		ConnPerRouteBean connPerRoute = new ConnPerRouteBean(MAX_ROUTE_CONNECTIONS);  
        ConnManagerParams.setMaxConnectionsPerRoute(params,connPerRoute);  
		ConnManagerParams.getMaxConnectionsPerRoute(params);
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
		// 使用线程安全来创建HttpClient
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
		return new DefaultHttpClient(conMgr, params);
	}

	public static void setTimeOut(int connect, int read) {
		TIMEOUT_CONNECT = Math.min(connect, 60000);
		TIMEOUT_READ = Math.min(read, 100000);
	}

	public void setProxy(Context context) {
		String host = Proxy.getHost(context);
		int port = Proxy.getPort(context);
		HttpHost httpHost = new HttpHost(host, port);
		getClient().getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, httpHost);
	}

	public InputStream getRequest(String path) throws Exception {
		return exeHttpRequest(new HttpGet(path));
	}

	public InputStream getRequest(String path, HashMap<String, String> args) throws Exception {
		return getRequest(UrlUtils.buildUrlRaw(path, args));
	}

	/**
	 * post请求
	 * @param path
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public InputStream postRequest(String path, HashMap<String, String> params) throws Exception {
		HttpPost hpPost = new HttpPost(path);
		if (params != null) {
			List<NameValuePair> postParams = new ArrayList<NameValuePair>();
			for (HashMap.Entry<String, String> param : params.entrySet()) {
				postParams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
			}
			HttpEntity entity = new UrlEncodedFormEntity(postParams, "UTF-8");
			hpPost.setEntity(entity);
		}
		return exeHttpRequest(hpPost);
	}

	/**
	 * 把上传Input转换成Bytes再上传
	 */
	public InputStream postRequest(String path, HashMap<String, String> head, byte[] bytes)
			throws Exception {
		HttpPost hpPost = new HttpPost(path);
		if (bytes != null) {
			hpPost.setEntity(new ByteArrayEntity(bytes));
		}
		if (head != null) {
			for (HashMap.Entry<String, String> h : head.entrySet()) {
				hpPost.addHeader(h.getKey(), h.getValue());
			}
		}
		return exeHttpRequest(hpPost);
	}

	private InputStream exeHttpRequest(HttpUriRequest request) throws WrapException {
		try {
			HttpResponse hpRep = getClient().execute(request);
			int code = hpRep.getStatusLine().getStatusCode();
			if (code == HttpStatus.SC_OK) {
				return handleGzipStream(hpRep);
			} else {
				throw new Exception(String.format("ERROR CODE=%1$d,MSG=%2$s", code, hpRep
						.getStatusLine().getReasonPhrase()));
			}
		} catch (Exception e) {
			throw WrapException.wrap(null, e, request.getMethod() + ": uri=" + request.getURI());
		}
	}

	/**
	 * 判断是否为压缩流.
	 * 
	 * @param hpRep
	 * @throws IOException
	 */
	private static InputStream handleGzipStream(HttpResponse hpRep) throws IOException {
		BufferedHttpEntity entity = new BufferedHttpEntity(hpRep.getEntity());
		BufferedInputStream bips = new BufferedInputStream(entity.getContent());
		byte[] header = new byte[2];// 取前两个字节
		bips.mark(2);
		int result = bips.read(header);
		// reset输入流到开始位置
		bips.reset();
		// 判断是否是GZIP格式
		int ss = (header[0] & 0xff) | ((header[1] & 0xff) << 8);
		if (result != -1 && ss == GZIPInputStream.GZIP_MAGIC) {
			return new GZIPInputStream(bips);
		}
		return bips;
	}
}
