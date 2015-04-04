package howbuy.android.util.http;

import howbuy.android.piggy.error.HowbuyException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.net.Proxy;

/**
 * http连接类
 * 
 * @author yescpu
 * 
 */
public class UrlConnectionUtil {
	public static final String ErrorNetWork = "网络不给力";
	public static final String ErrorUNlinkServer = "无法连接到服务器";
	public static final String ErrorNetWorkTimeOut = "请求超时";
	public static final String ErrorExcption = "数据错误";
	public static final String ErrorNotData = "服务器数据为空";
	public static final String ErrorSelf = "安全传输错误";
	public static final String ErrorServiceNot = "数据返回异常";
	public static final String ErrorService500 = "数据返回异常";

	private static UrlConnectionUtil urlConnectionUtil;

	private UrlConnectionUtil() {
	}

	private DefaultHttpClient getClient() {
		return CustomerHttpClient.getInstance().getDefaultHttpClient();
	}

	public static UrlConnectionUtil getInstance() {
		if (urlConnectionUtil == null) {
			urlConnectionUtil = new UrlConnectionUtil();
		}
		return urlConnectionUtil;
	}

	@Deprecated
	public void setProxy(Context context) {
		String host = Proxy.getHost(context);
		int port = Proxy.getPort(context);
		HttpHost httpHost = new HttpHost(host, port);
		getClient().getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, httpHost);
	}

	/**
	 * Get请求无压缩
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public InputStream executeGet(String path) throws HowbuyException {
		HttpGet httpGet = new HttpGet(path);
		HttpResponse httpResponse;
		try {
			httpResponse = getClient().execute(httpGet);
			int httpCode=httpResponse.getStatusLine().getStatusCode();
			if (httpCode == HttpStatus.SC_OK) {
				InputStream is = handleGzipStream(httpResponse);
				if (is == null) {
					throw new HowbuyException(ErrorNotData);
				}
				return is;
			}else if (httpCode==HttpStatus.SC_NOT_FOUND) {
				throw new HowbuyException(ErrorUNlinkServer);
			}else if (httpCode==HttpStatus.SC_INTERNAL_SERVER_ERROR ) {
				throw new HowbuyException(ErrorService500);
			}
			throw new HowbuyException(ErrorServiceNot);
		} catch (HttpHostConnectException e) {
			throw new HowbuyException(ErrorNetWork, e);
		} catch (UnknownHostException e) {
			throw new HowbuyException(ErrorUNlinkServer, e);
		} catch (NoHttpResponseException e) {
			throw new HowbuyException(ErrorNetWork, e);
		} catch (SocketTimeoutException e) {
			throw new HowbuyException(ErrorNetWorkTimeOut, e);
		} catch (ConnectTimeoutException e) {
			throw new HowbuyException(ErrorNetWorkTimeOut, e);
		}catch (ConnectException e) {
			throw new HowbuyException(ErrorNetWork, e);
		} catch (SocketException e) {
			throw new HowbuyException(ErrorUNlinkServer, e);
		} catch (Exception e) {
			throw new HowbuyException(ErrorServiceNot, e);
		}
		
		
	}

	/**
	 * post无压缩请求
	 * 
	 * @param path
	 * @param params
	 * @return
	 * @throws HowbuyException
	 */
	public InputStream executePost(String path, Map<String, String> params,Map<String,String> pubparams) throws HowbuyException {
		HttpPost httpPost = new HttpPost(path);
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> param : params.entrySet()) {
			postParams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
		}
		
		if (pubparams!=null) {
			for (Map.Entry<String, String> param : pubparams.entrySet()) {
				postParams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
			}
		}
		HttpEntity entity;
		try {
			entity = new UrlEncodedFormEntity(postParams, "UTF-8");
			httpPost.setEntity(entity);
			HttpResponse httpResponse;
			httpResponse = getClient().execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream in = handleGzipStream(httpResponse);
				return in;
			}
			throw new HowbuyException(ErrorServiceNot);
		} catch (ClientProtocolException e) {
			throw new HowbuyException(ErrorNetWork, e);
		} catch (UnknownHostException e) {
			throw new HowbuyException(ErrorNetWork, e);
		} catch (NoHttpResponseException e) {
			throw new HowbuyException(ErrorNetWork, e);
		} catch (SocketTimeoutException e) {
			throw new HowbuyException(ErrorNetWorkTimeOut, e);
		} catch (Exception e) {
			throw new HowbuyException(ErrorServiceNot, e);
		}
	}

	/**
	 * 把上传Input转换成Bytes再上传
	 * 
	 * @param path
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public InputStream excutePostStream(String path, byte[] bytes) throws Exception {
		HttpPost httpPost = new HttpPost(path);
		httpPost.setEntity(new ByteArrayEntity(bytes));
		HttpResponse httpResponse = getClient().execute(httpPost);
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			InputStream in = handleGzipStream(httpResponse);
			return in;
		} else {
			return null;
		}
	}

	/**
	 * Gzip
	 * 
	 * @param httpResponse
	 * @return
	 * @throws HowbuyException
	 */
	public InputStream handleGzipStream(HttpResponse httpResponse) throws HowbuyException {
		InputStream ips = null;
		// 取前两个字节
		byte[] header = new byte[2];
		BufferedInputStream bips = null;
		try {
			bips = new BufferedInputStream(httpResponse.getEntity().getContent());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new HowbuyException(ErrorNetWork, e1);
		}
		// if (isIncludeGzipHeader(httpResponse)) {
		try {
			bips.mark(2);
			int result = bips.read(header);
			// reset输入流到开始位置
			bips.reset();
			// 判断是否是GZIP格式
			int ss = (header[0] & 0xff) | ((header[1] & 0xff) << 8);
			if (result != -1 && ss == GZIPInputStream.GZIP_MAGIC) {
				// System.out.println("为数据压缩格式...");
				ips = new GZIPInputStream(bips);
			} else {
				// 取前两个字节
				ips = bips;
			}
		} catch (java.io.IOException e) {
			throw new HowbuyException(ErrorExcption, e);
		}
		// }else {
		// ips = bips;
		// }
		return ips;
	}

	/**
	 * 是否包含Gzip的Header
	 * 
	 * @param httpResponse
	 * @return
	 * @deprecated
	 */
	public boolean isIncludeGzipHeader(HttpResponse httpResponse) {
		Header[] headers = httpResponse.getAllHeaders();
		for (Header header : headers) {
			if (header.getName().equalsIgnoreCase("content-encoding") && header.getValue().equals("gzip")) {
				return true;
			}
		}
		return false;
	}

}
