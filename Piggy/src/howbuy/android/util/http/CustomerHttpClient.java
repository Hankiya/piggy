package howbuy.android.util.http;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

/**
 * 单例 只能有一个连接
 * 
 * @author yescpu
 * 
 */
public class CustomerHttpClient {
	public static final int ConnectionPoolTimeout = 40000;
	public static final int ConnectionTimeout = 40000;
	public static final int SocketTimeout = 40000;
	private static final String CHARSET = HTTP.UTF_8;
	private static CustomerHttpClient client;
	private DefaultHttpClient defaultHttpClient;

	private CustomerHttpClient() {
	}

	public DefaultHttpClient getDefaultHttpClient() {
		if (defaultHttpClient == null) {
			defaultHttpClient = getHttpClient();
		}
		return defaultHttpClient;
	}

	public static synchronized CustomerHttpClient getInstance() {
		if (client == null) {
			client = new CustomerHttpClient();
		}
		return client;
	}

	/**
	 * 第一行设置ConnectionPoolTimeout： ConnectionPoolTimeoutException,
	 * 这定义了从ConnectionManager管理的连接池中取出连接的超时时间，此处设置为1秒
	 * 
	 * 第二行设置ConnectionTimeout： ConnectionTimeoutException,
	 * 这定义了通过网络与服务器建立连接的超时时间。
	 * Httpclient包中通过一个异步线程去创建与服务器的socket连接，这就是该socket连接的超时时间，此处设置为2秒。
	 * 
	 * 第三行设置SocketTimeout： SocketTimeoutException。　
	 * 这定义了Socket读数据的超时时间，即从服务器获取响应数据需要等待的时间，此处设置为4秒。 　　
	 * 
	 * @return
	 */
	private DefaultHttpClient getHttpClient() {
		HttpParams params = new BasicHttpParams();
		// 设置一些基本参数
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, false);

		HttpProtocolParams.setUserAgent(params, "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus 4 Build.FRG83) "
				+ "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
		// 超时设置
		/* 从连接池中取连接的超时时间 */
		ConnManagerParams.setTimeout(params, ConnectionPoolTimeout);

		/* 连接超时 */
		HttpConnectionParams.setConnectionTimeout(params, ConnectionTimeout);
		/* 请求超时 */
		HttpConnectionParams.setSoTimeout(params, SocketTimeout);

		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		// 信任所有的证书
		try {
			KeyStore trustStore;
			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);
			SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			schReg.register(new Scheme("https", sf, 443));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 使用线程安全来创建HttpClient
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);

		DefaultHttpClient dhClient = new DefaultHttpClient(conMgr, params);

		// 加入公司证书失败
		// try {
		// CertificateFactory cerFactory =
		// CertificateFactory.getInstance("X.509");
		// InputStream ins =
		// ApplicationParams.getInstance().getAssets().open("tradehb.cer");
		// Certificate cer = cerFactory.generateCertificate(ins);
		// KeyStore keyStore = KeyStore.getInstance("X.509");
		// keyStore.load(null, null);
		// keyStore.setCertificateEntry("trust", cer);
		// SSLSocketFactory socketFactory = new SSLSocketFactory(keyStore);
		// Scheme sch = new Scheme("https", socketFactory, 443);
		// dhClient.getConnectionManager().getSchemeRegistry().register(sch);
		// } catch (Exception e) {
		// // TODO: handle exception
		// }

		return dhClient;
	}

	/**
	 * 绕过所有证书
	 * 
	 * @author Administrator
	 * 
	 */
	public class SSLSocketFactoryEx extends SSLSocketFactory {

		SSLContext sslContext = SSLContext.getInstance("TLS");

		public SSLSocketFactoryEx(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
				}

				@Override
				public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
				}
			};
			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}
}
