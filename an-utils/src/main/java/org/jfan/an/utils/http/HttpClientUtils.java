package org.jfan.an.utils.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.pool.PoolStats;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.jfan.an.log.Logger;
import org.jfan.an.log.LoggerFactory;

public final class HttpClientUtils {

	private static final long warnTime = 800;
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

	private static Properties pro = new Properties();
	private static Map<URL, Integer> routeMaxMap = new HashMap<URL, Integer>();
	private static Map<String, String> localDnsMap = new HashMap<String, String>();
	private static Map<String, String> userAgentMap = new HashMap<String, String>();

	private static HttpClientBuilder build;
	private static PoolingHttpClientConnectionManager cm;

	/** 默认请求超时秒数 */
	public static int DEFAULT_TIMEOUT;
	/** 默认USER_AGENT */
	public static String USER_AGENT;
	/** 通讯是否不做延迟 */
	public static boolean TCP_NO_DELAY;
	/** socket缓存大小（Byte） */
	public static int SOCKET_BUFFER_SIZE;
	/** 从连接池中取连接的超时时间 */
	public static int DEFAULT_CONNECTION_TIMEOUT;
	/** 默认字符编码 */
	public static final String DEFAULT_CHARSET = "UTF-8";

	static {
		// init param
		initPro("./hcConfig.properties");
		init();

		int mt = readInt("def.maxTotal", 6000);// 总线程池的大小
		TimeUnit timeUnit = TimeUnit.MILLISECONDS;// http client中使用毫秒为单位
		int dmpr = readInt("def.host.defaultMaxPerRoute", 300); // Host默认的最大线程池大小
		long timeToLive = readLong("def.host.timeToLive", 3600000);// 每个连接最大空闲时间--1小时

		// dsResolver
		DnsResolver dsResolver = null;
		if ("config".equalsIgnoreCase(readString("dnsResolver", "system", false)))
			dsResolver = new ConfigDnsResolver();
		else
			dsResolver = new SystemDefaultDnsResolver();

		Registry<ConnectionSocketFactory> sockFactory = RegistryBuilder.<ConnectionSocketFactory> create()//
				.register("http", PlainConnectionSocketFactory.getSocketFactory())//
				.register("https", SSLConnectionSocketFactory.getSocketFactory())//
				.build();

		// init ConnectionManager
		cm = new PoolingHttpClientConnectionManager(sockFactory, null, null, dsResolver, timeToLive, timeUnit);
		cm.setDefaultMaxPerRoute(dmpr);
		cm.setMaxTotal(mt);
		// 配置独立的池大小（host）
		for (URL url : routeMaxMap.keySet()) {
			HttpHost host = new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
			cm.setMaxPerRoute(new HttpRoute(host), routeMaxMap.get(url));
		}

		ConnectionConfig cc = ConnectionConfig.custom().setBufferSize(SOCKET_BUFFER_SIZE).build();
		cm.setDefaultConnectionConfig(cc);

		SocketConfig sc = SocketConfig.custom().setSoTimeout(DEFAULT_CONNECTION_TIMEOUT).setTcpNoDelay(TCP_NO_DELAY).build();
		cm.setDefaultSocketConfig(sc);

		build = HttpClients.custom().setConnectionManager(cm);

		build.setRoutePlanner(new HttpRoutePlanner() {
			@Override
			public HttpRoute determineRoute(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
				boolean r = (1 == System.currentTimeMillis() % 2);
				String host = "127.0.0.1";
				int port = 8081;
				if (r) {
					host = "192.168.33.214";
					port = 9090;
				}

				System.out.println("TO host: " + host + ", port: " + port);
				HttpHost hh = new HttpHost(host, port);
				return new HttpRoute(hh);
			}
		});
		// CloseableHttpClient clean = build.build();
	}

	private static void setConParams(HttpRequestBase base, int timeout) {
		int out = (timeout > 0 ? timeout : DEFAULT_TIMEOUT) * 1000;

		RequestConfig config = RequestConfig.custom().setSocketTimeout(out).setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT).setRedirectsEnabled(true).build();

		base.setConfig(config);
		base.setProtocolVersion(HttpVersion.HTTP_1_1);
		if (null != USER_AGENT)
			base.addHeader(HttpHeaders.USER_AGENT, USER_AGENT);
		base.addHeader(HttpHeaders.REFERER, "http://mapi.vip.com/");
		// base.setHeader(HttpHeaders.REFERER, "http://mapi.vip.com/");
	}

	protected static void logConnectionStats(long offset, String url) {
		PoolStats totalStats = cm.getTotalStats();
		logger.warn("http request[" + url + "] take much time " + offset + " millis. ### http connection pool, max: " + totalStats.getMax() + ", acitve: " + totalStats.getAvailable() + ", leased: "
				+ totalStats.getLeased() + ", pending:" + totalStats.getPending());
	}

	protected static void logConnectionStats(HttpRoute route, String url) {
		if (logger.isDebugEnabled()) {
			PoolStats stats = cm.getStats(route);
			logger.debug("### http connection route pool for " + route.getTargetHost() + ", max: " + stats.getMax() + ", acitve: " + stats.getAvailable() + ", leased: " + stats.getLeased()
					+ ", pending:" + stats.getPending());
		}
	}

	/**
	 * 执行doGet方法
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String doGet(String url) throws Exception {
		// 使用工具默认编码
		return doGet(url, DEFAULT_CHARSET);
	}

	public static String doGet(String url, String charset) throws Exception {
		return doGet(url, charset, DEFAULT_TIMEOUT);
	}

	private static boolean isSucess(int respStatusCode) {
		if (respStatusCode == HttpStatus.SC_OK || respStatusCode == HttpStatus.SC_CREATED) {
			return true;
		} else {
			return false;
		}
	}

	public static String doGet(String url, String charset, int timeout) throws Exception {
		// 记录所有的请求到其他系统的url
		// if (logger.isInfoEnabled())
		// logger.info("url to b2c api: " + url);
		/* 建立HTTPGet对象 */
		String strResult = "";
		HttpResponse httpResponse = null;
		HttpClient httpClient = null;
		HttpGet httpRequest = null;
		/* 发送请求并等待响应 */
		try {
			long start = System.currentTimeMillis();
			httpClient = build.build();
			httpRequest = new HttpGet(url);
			setConParams(httpRequest, timeout);

			httpResponse = httpClient.execute(httpRequest);
			// 查看响应状态 如果不是正常状态 则主动abort请求并返回
			int sc = httpResponse.getStatusLine().getStatusCode();
			if (isSucess(sc) == false) {
				httpRequest.abort();
				logger.warn("target return error status " + sc + ", the url is :" + url);
				return null;
			}
			if (httpResponse.getEntity() != null) {
				byte[] bytes = EntityUtils.toByteArray(httpResponse.getEntity());
				String responseMinStr = logHttpRespone(bytes, charset);
				logger.info("HTTP GET : " + url + ",response is [" + responseMinStr + "],length is [" + (bytes != null ? bytes.length : 0) + "]");
				strResult = new String(bytes, charset);
				// strResult = EntityUtils.toString(httpResponse.getEntity(),
				// charset);
			}
			long offset = System.currentTimeMillis() - start;
			if (offset > warnTime) {
				logConnectionStats(offset, url);
				URI uri = httpRequest.getURI();
				// HttpHost host = URIUtils.extractHost(uri);
				HttpRoute route = new HttpRoute(new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme()));
				logConnectionStats(route, url);
			}
		} catch (Exception e) {
			logger.error("http get error with url " + url, e);
			if (httpRequest != null) {
				httpRequest.abort();
			}
			throw e;
		}
		return strResult;
	}

	/**
	 * post request without parameters
	 */
	public static String doPost(String url) {
		return doPost(url, DEFAULT_CHARSET, null);
	}

	public static String doPost(String url, Map<String, Object> map) {
		return doPost(url, DEFAULT_CHARSET, map);
	}

	public static String doPost(String url, String content) {
		return doPost(url, content, DEFAULT_CHARSET, DEFAULT_TIMEOUT);
	}

	public static String doPost(String url, String charset, Map<String, Object> map) {
		return doPost(url, charset, map, DEFAULT_TIMEOUT);
	}

	public static String doPost(String url, String content, String charset, int timeout) {
		if (logger.isInfoEnabled())
			logger.info("post url: " + url + " with content " + content);// 这个日志重复，但是此方法没有地方调用，暂不理会
		HttpClient httpClient = build.build();
		HttpPost httpRequest = new HttpPost(url);
		setConParams(httpRequest, timeout);
		// HttpEntity repEntity = null;
		try {
			HttpEntity repEntity = new StringEntity(content, charset);
			httpRequest.setEntity(repEntity);
			return doPost(httpClient, httpRequest, url);
		} catch (UnsupportedEncodingException e) {
			logger.error("specified " + content + " convert to " + charset + " error", e);
		} catch (Exception e) {
			logger.error("post to " + url + " with content " + content, e);
		} finally {
			// request entity not contain input stream
			/*
			 * try { if (repEntity != null) { EntityUtils.consume(repEntity); }
			 * } catch (IOException e) {
			 * logger.error(" final consume the stream ", e); }
			 */
		}
		return null;
	}

	public static String doPost(String url, String charset, Map<String, Object> parameters, int timeout) {
		return doPost(url, charset, parameters, timeout, null);
	}

	public static String doPost(String url, String charset, Map<String, Object> parameters, int timeout, Map<String, String> requestHeaders) {
		if (logger.isInfoEnabled())
			logger.info("post url: " + url + " param:" + parameters + " header: " + requestHeaders);
		HttpClient httpClient = build.build();
		HttpPost httpRequest = new HttpPost(url);
		setConParams(httpRequest, timeout);
		// set request parameters
		if (parameters != null && !parameters.isEmpty()) {
			List<NameValuePair> params = new ArrayList<NameValuePair>(parameters.size());
			for (String key : parameters.keySet()) {
				params.add(new BasicNameValuePair(key, String.valueOf(parameters.get(key))));
			}
			try {
				httpRequest.setEntity(new UrlEncodedFormEntity(params, DEFAULT_CHARSET));
			} catch (UnsupportedEncodingException e) {
				logger.warn("set parameter error ", e);
			}
		}

		// set request headers
		addHeaderForHttpRequestBase(httpRequest, requestHeaders);

		try {
			return doPost(httpClient, httpRequest, url);
		} catch (Exception e) {
			logger.warn("post to " + url + " param:" + parameters + " header: " + requestHeaders, e);
		}
		return null;
	}

	public static String doPost(HttpClient httpClient, HttpEntityEnclosingRequestBase httpRequest, String url) throws Exception {
		HttpEntity rspEntity = null;
		try {
			long start = System.currentTimeMillis();
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			if (isSucess(httpResponse.getStatusLine().getStatusCode())) {
				rspEntity = httpResponse.getEntity();
				if (rspEntity != null) {
					byte[] bytes = EntityUtils.toByteArray(rspEntity);
					String responseMinStr = logHttpRespone(bytes, DEFAULT_CHARSET);
					logger.info("HTTP POST : " + (httpRequest.getURI()) + ", Response is [" + responseMinStr + "], Length is [" + (bytes != null ? bytes.length : 0) + "]");
					return new String(bytes, DEFAULT_CHARSET);
				}
				long offset = System.currentTimeMillis() - start;

				if (offset > warnTime) {
					logConnectionStats(offset, httpRequest.getURI().toString());
					URI uri = httpRequest.getURI();
					// HttpHost host = URIUtils.extractHost(uri);
					HttpRoute route = new HttpRoute(new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme()));
					logConnectionStats(route, url);
				}
			} else {
				// 查看响应状态 如果不是正常状态 则主动abort请求并返回{
				logger.warn("target return error status, the url is :" + httpRequest.getURI());
				rspEntity = httpResponse.getEntity();

				if (rspEntity != null) {
					logger.warn("target return error message, the data is :" + EntityUtils.toString(rspEntity, DEFAULT_CHARSET));
				}
				httpRequest.abort();
				return null;
			}
		} catch (Exception e) {
			httpRequest.abort();
			throw e;
		} finally {
			try {
				if (rspEntity != null) {
					EntityUtils.consume(rspEntity);
				}
			} catch (IOException e) {
				logger.error(" final consume the stream ", e);
			}
		}
		return null;
	}

	/**
	 * add headers for HttpRequestBase
	 * 
	 * @param httpRequest
	 * @param requestHeaders
	 */
	private static void addHeaderForHttpRequestBase(HttpRequestBase httpRequest, Map<String, String> requestHeaders) {
		if (requestHeaders != null && !requestHeaders.isEmpty()) {
			for (String key : requestHeaders.keySet()) {
				httpRequest.addHeader(key, requestHeaders.get(key));
			}
		}
	}

	/**
	 * 打印HTTP的响应
	 * 
	 * @author jame.xu
	 * @date 2014-4-3
	 * @description
	 * @mail jame.xu@vipshop.com
	 */
	private static String logHttpRespone(byte[] data, String charset) {
		if (data != null && data.length > 0) {
			int length = data.length >= 16 ? 16 : data.length;
			byte[] dest = new byte[length];
			System.arraycopy(data, 0, dest, 0, length);
			try {
				String result = new String(dest, charset);
				if (null != result) {
					result = result.replace("\r\n", " ");
					return result;
				}
			} catch (UnsupportedEncodingException e) {
			}
		}
		return null;
	}

	// ########################################################################
	// ######################### Private Method #############################
	// ########################################################################

	/**
	 * 根据路径读取配置文件
	 */
	private static void initPro(String path) {
		InputStream is = HttpClientUtils.class.getResourceAsStream(path);
		if (null == is)
			logger.warn("HC Cannot find the configuration file 'hcConfig.properties' -- Please check!");
		else
			try {
				pro.load(is);
			} catch (IOException e) {
				logger.warn("HC Init HttpClientUtils Config ERROR: Load Default Config -- Please check!", e);
			}
	}

	/**
	 * 读取Int
	 */
	private static int readInt(String key, int def) {
		return (int) readLong(key, def);
	}

	/**
	 * 读取Long
	 */
	private static long readLong(String key, long def) {
		String str = readString(key, def + "", true);
		try {
			return Long.parseLong(str.trim());
		} catch (NumberFormatException e) {
			logger.warn("HC read Config '" + key + "' is not Num. " + str + ". def: " + def);
			return def;
		}
	}

	/**
	 * 读取String
	 */
	private static String readString(String key, String def, boolean warn) {
		String str = pro.getProperty(key);
		String msg = "is Null.";
		if (null != str)
			return str;
		if (warn)
			logger.warn("HC read Config '" + key + "' setDefault: " + def + ". Because Value " + msg);
		return def;
	}

	/**
	 * 从pro中读取指定前缀的key内容
	 */
	private static Map<String, String> readByPprefix(String prefix) {
		Map<String, String> map = new HashMap<String, String>();
		for (Object ko : pro.keySet())
			if (ko.toString().startsWith(prefix)) {
				String key = ko.toString();
				String value = pro.getProperty(key);
				key = key.substring(prefix.length());
				map.put(key, value);
			}
		return map;
	}

	/**
	 * 初始化Host线程池配置列表 & 自定义HostDns配置列表
	 */
	private static void init() {
		// static param
		DEFAULT_TIMEOUT = readInt("def.readTimeout", 5);
		USER_AGENT = readString("def.useragent", null, true);
		SOCKET_BUFFER_SIZE = readInt("def.socketBufferSize", 8192);
		DEFAULT_CONNECTION_TIMEOUT = readInt("def.getConnTimeout", 1000);
		TCP_NO_DELAY = Boolean.parseBoolean(readString("def.tcpNoDelay", "false", true));

		// init Host Config
		Map<String, String> urimap = readByPprefix("http.uri.");
		for (String key : urimap.keySet()) {
			String uri = urimap.get(key);
			try {
				URL url = new URL(uri);
				int pool = readInt("http.pool." + key, -1);
				String useragent = readString("http.useragent." + key, null, false);
				if (0 < pool)
					routeMaxMap.put(url, pool);
				if (null != useragent)
					userAgentMap.put(url.getHost(), useragent);
			} catch (MalformedURLException e) {
				logger.warn("HC init pool config, '" + uri + "' not URL. continue. -- Please check!");
			}
		}

		// init DnsMap
		if ("config".equalsIgnoreCase(readString("dnsResolver", "system", false))) {
			Map<String, String> dmap = readByPprefix("dns.");
			for (String key : dmap.keySet()) {
				String value = dmap.get(key);
				localDnsMap.put(key, value);
				logger.info("HC replace DNS '" + key + "' to '" + value + "'");
			}
		}
	}

	/**
	 * 主要实现从配置文件中读取hosts映射，指向预定的目标去；没有配置的使用系统dns
	 * 
	 * @author JFan 2013-12-24 下午4:49:28
	 */
	static final class ConfigDnsResolver implements DnsResolver {

		/*
		 * （非 Javadoc）
		 * 
		 * @see org.apache.http.conn.DnsResolver#resolve(java.lang.String)
		 */
		@Override
		public InetAddress[] resolve(String host) throws UnknownHostException {
			String ip;
			if ((ip = localDnsMap.get(host)) != null)
				return InetAddress.getAllByName(ip);
			// System DNS
			return InetAddress.getAllByName(host);
		}

	}

}
