package org.jfan.an.utils.http;
//package org.guide.webapi.utils;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.UnsupportedEncodingException;
//import java.net.InetAddress;
//import java.net.MalformedURLException;
//import java.net.URI;
//import java.net.URL;
//import java.net.UnknownHostException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//import java.util.concurrent.TimeUnit;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpHost;
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.HttpVersion;
//import org.apache.http.NameValuePair;
//import org.apache.http.ParseException;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpDelete;
//import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.methods.HttpPut;
//import org.apache.http.client.methods.HttpRequestBase;
//import org.apache.http.client.params.ClientPNames;
//import org.apache.http.conn.DnsResolver;
//import org.apache.http.conn.routing.HttpRoute;
//import org.apache.http.conn.scheme.SchemeRegistry;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.impl.conn.PoolingClientConnectionManager;
//import org.apache.http.impl.conn.SchemeRegistryFactory;
//import org.apache.http.impl.conn.SystemDefaultDnsResolver;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.params.BasicHttpParams;
//import org.apache.http.params.CoreConnectionPNames;
//import org.apache.http.params.CoreProtocolPNames;
//import org.apache.http.params.HttpParams;
//import org.apache.http.pool.PoolStats;
//import org.apache.http.util.EntityUtils;
//
///**
// * @author Devin.Hu
// * @date 2011-6-24
// * @version V1.0
// * @description HttpClientUtils工具类，用于与服务端建立连接，传送或读取数据
// */
//public final class AHttpClientUtils {
//
//	private static final long warnTime = 800;
//	private static final Log logger = LogFactory.getLog(AHttpClientUtils.class);
//
//	private static Properties pro = new Properties();
//	private static PoolingClientConnectionManager cm;
//	private static Map<URL, Integer> routeMaxMap = new HashMap<URL, Integer>();
//	private static Map<String, String> localDnsMap = new HashMap<String, String>();
//	private static Map<String, String> userAgentMap = new HashMap<String, String>();
//
//	/** 默认请求超时秒数 */
//	public static int DEFAULT_TIMEOUT;
//	/** 默认USER_AGENT */
//	public static String USER_AGENT;
//	/** 通讯是否不做延迟 */
//	public static boolean TCP_NO_DELAY;
//	/** socket缓存大小（Byte） */
//	public static int SOCKET_BUFFER_SIZE;
//	/** 从连接池中取连接的超时时间 */
//	public static int DEFAULT_CONNECTION_TIMEOUT;
//	/** 默认字符编码 */
//	public static final String DEFAULT_CHARSET = "UTF-8";
//
//	static {
//		// init param
//		initPro("/hcConfig.properties");
//		init();
//
//		int mt = readInt("def.maxTotal", 6000);// 总线程池的大小
//		TimeUnit timeUnit = TimeUnit.MILLISECONDS;// http client中使用毫秒为单位
//		int dmpr = readInt("def.host.defaultMaxPerRoute", 300); // Host默认的最大线程池大小
//		long timeToLive = readLong("def.host.timeToLive", 3600000);// 每个连接最大空闲时间--1小时
//		SchemeRegistry schemeRegistry = SchemeRegistryFactory.createDefault();// 等同于
//																				// http->80、https->443
//
//		// dsResolver
//		DnsResolver dsResolver = null;
//		if ("config".equalsIgnoreCase(readString("dnsResolver", "system", false)))
//			dsResolver = new ConfigDnsResolver();
//		else
//			dsResolver = new SystemDefaultDnsResolver();
//
//		// init ConnectionManager
//		cm = new PoolingClientConnectionManager(schemeRegistry, timeToLive, timeUnit, dsResolver);
//		cm.setDefaultMaxPerRoute(dmpr);
//		cm.setMaxTotal(mt);
//		// 配置独立的池大小（host）
//		for (URL url : routeMaxMap.keySet()) {
//			HttpHost host = new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
//			cm.setMaxPerRoute(new HttpRoute(host), routeMaxMap.get(url));
//		}
//	}
//
//	/**
//	 * 得到HttpClient方法
//	 * 
//	 * @return
//	 */
//	private static HttpClient getHttpClient(int timeOut) {
//		HttpParams httpParams = new BasicHttpParams();
//
//		// set HttpConnectionParams
//		int to = (timeOut > 0 ? timeOut : DEFAULT_TIMEOUT) * 1000;
//		httpParams.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, to);
//		httpParams.setParameter(CoreProtocolPNames.USER_AGENT, USER_AGENT);
//		httpParams.setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, true);
//		httpParams.setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, TCP_NO_DELAY);
//		httpParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
//		httpParams.setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, SOCKET_BUFFER_SIZE);
//		httpParams.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, DEFAULT_CONNECTION_TIMEOUT);
//
//		// 通过连接池返回
//		return new DefaultHttpClient(cm, httpParams);
//	}
//
//	protected static void logConnectionStats(long offset, String url) {
//		PoolStats totalStats = cm.getTotalStats();
//		logger.warn("http request[" + url + "] take much time " + offset + " millis. ### http connection pool, max: " + totalStats.getMax() + ", acitve: " + totalStats.getAvailable() + ", leased: "
//				+ totalStats.getLeased() + ", pending:" + totalStats.getPending());
//	}
//
//	protected static void logConnectionStats(HttpRoute route, String url) {
//		if (logger.isDebugEnabled()) {
//			PoolStats stats = cm.getStats(route);
//			logger.debug("### http connection route pool for " + route.getProxyHost() + ", max: " + stats.getMax() + ", acitve: " + stats.getAvailable() + ", leased: " + stats.getLeased()
//					+ ", pending:" + stats.getPending());
//		}
//	}
//
//	/**
//	 * 执行doGet方法
//	 * 
//	 * @param url
//	 * @return
//	 * @throws Exception
//	 */
//	public static String doGet(String url) throws Exception {
//		// 使用工具默认编码
//		return doGet(url, DEFAULT_CHARSET);
//	}
//
//	public static String doGet(String url, String charset) throws Exception {
//		return doGet(url, charset, DEFAULT_TIMEOUT);
//	}
//
//	private static boolean isSucess(int respStatusCode) {
//		if (respStatusCode == HttpStatus.SC_OK || respStatusCode == HttpStatus.SC_CREATED) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	public static String doGet(String url, String charset, int timeout) throws Exception {
//		// 记录所有的请求到其他系统的url
//		// if (logger.isInfoEnabled())
//		// logger.info("url to b2c api: " + url);
//		/* 建立HTTPGet对象 */
//		String strResult = "";
//		HttpResponse httpResponse = null;
//		HttpClient httpClient = null;
//		HttpGet httpRequest = null;
//		/* 发送请求并等待响应 */
//		try {
//			long start = System.currentTimeMillis();
//			httpClient = getHttpClient(timeout);
//			httpRequest = new HttpGet(url);
//			httpRequest.addHeader("Referer", "http://mapi.vip.com/");
//
//			httpResponse = httpClient.execute(httpRequest);
//			// 查看响应状态 如果不是正常状态 则主动abort请求并返回
//			if (isSucess(httpResponse.getStatusLine().getStatusCode()) == false) {
//				httpRequest.abort();
//				logger.warn("target return error status, the url is :" + url);
//				return null;
//			}
//			if (httpResponse.getEntity() != null) {
//				byte[] bytes = EntityUtils.toByteArray(httpResponse.getEntity());
//				String responseMinStr = logHttpRespone(bytes, charset);
//				logger.info("HTTP GET : " + url + ",response is [" + responseMinStr + "],length is [" + (bytes != null ? bytes.length : 0) + "]");
//				strResult = new String(bytes, charset);
//				// strResult = EntityUtils.toString(httpResponse.getEntity(),
//				// charset);
//				strResult = filterHtml(strResult);
//			}
//			long offset = System.currentTimeMillis() - start;
//			if (offset > warnTime)
//				logConnectionStats(offset, url);
//		} catch (Exception e) {
//			logger.error("http get error with url " + url, e);
//			if (httpRequest != null) {
//				httpRequest.abort();
//			}
//			throw e;
//		}
//		return strResult;
//	}
//
//	/**
//	 * 执行doGet方法，失败返回null
//	 * 
//	 * @param url
//	 * @return InputStream
//	 */
//	public InputStream doGet4stream(String url) throws ClientProtocolException, IOException {
//		HttpGet httpRequest = new HttpGet(url);
//		HttpResponse httpResponse = getHttpClient(60).execute(httpRequest);
//
//		if (isSucess(httpResponse.getStatusLine().getStatusCode()) == false) {
//			return httpResponse.getEntity().getContent();
//		}
//		return null;
//	}
//
//	/**
//	 * 过滤特殊字符
//	 * 
//	 * @param result
//	 * @return
//	 */
//	private static String filterHtml(String result) {
//		if (null != result) {
//			result = result.replace("&#039;", "'");
//			result = result.replace("&amp;", "&");
//			result = result.replace("&nbsp;", " ");
//		}
//		return result;
//	}
//
//	/**
//	 * post request without parameters
//	 * 
//	 * @param url
//	 * @return
//	 */
//	public static String doPost(String url) {
//		return doPost(url, DEFAULT_CHARSET, null);
//	}
//
//	// // 判断网络类型
//	// public static boolean isWap() {
//	// String proxyHost = android.net.Proxy.getDefaultHost();
//	// if (proxyHost != null) {
//	// return true;
//	// } else {
//	// return false;
//	// }
//	// }
//
//	/**
//	 * post request by specified url & parameters
//	 * 
//	 * @param url
//	 * @param map
//	 * @return
//	 */
//	public static String doPost(String url, Map<String, Object> map) {
//		return doPost(url, DEFAULT_CHARSET, map);
//	}
//
//	public static String doPost(String url, String content) {
//		return doPost(url, content, DEFAULT_CHARSET, DEFAULT_TIMEOUT);
//	}
//
//	public static String doPostContent(String url, String content, String charset) {
//		return doPost(url, content, charset, DEFAULT_TIMEOUT);
//	}
//
//	/**
//	 * post request by specified url, charset & parameters
//	 * 
//	 * @param url
//	 * @param charset
//	 * @param map
//	 * @return
//	 */
//	public static String doPost(String url, String charset, Map<String, Object> map) {
//		return doPost(url, charset, map, DEFAULT_TIMEOUT);
//	}
//
//	public static String doPost(String url, String content, String charset, int timeout) {
//		if (logger.isInfoEnabled())
//			logger.info("post url: " + url + " with content " + content);// 这个日志重复，但是此方法没有地方调用，暂不理会
//		HttpClient httpClient = getHttpClient(timeout);
//		HttpPost httpRequest = new HttpPost(url);
//		httpRequest.addHeader("Referer", "http://mapi.vip.com/");
//		// HttpEntity repEntity = null;
//		try {
//			HttpEntity repEntity = new StringEntity(content, charset);
//			httpRequest.setEntity(repEntity);
//			return doPost(httpClient, httpRequest);
//		} catch (UnsupportedEncodingException e) {
//			logger.error("specified " + content + " convert to " + charset + " error", e);
//		} catch (Exception e) {
//			logger.error("post to " + url + " with content " + content, e);
//		} finally {
//			// request entity not contain input stream
//			/*
//			 * try { if (repEntity != null) { EntityUtils.consume(repEntity); }
//			 * } catch (IOException e) {
//			 * logger.error(" final consume the stream ", e); }
//			 */
//		}
//		return null;
//	}
//
//	public static String doPost(String url, String charset, Map<String, Object> parameters, int timeout) {
//		return doPost(url, charset, parameters, timeout, null);
//	}
//
//	public static String doPost(String url, String charset, Map<String, Object> parameters, int timeout, Map<String, String> requestHeaders) {
//		if (logger.isInfoEnabled())
//			logger.info("post url: " + url + " param:" + parameters + " header: " + requestHeaders);
//		HttpClient httpClient = getHttpClient(timeout);
//		HttpPost httpRequest = new HttpPost(url);
//		httpRequest.addHeader("Referer", "http://mapi.vip.com/");
//		// set request parameters
//		if (parameters != null && !parameters.isEmpty()) {
//			List<NameValuePair> params = new ArrayList<NameValuePair>(parameters.size());
//			for (String key : parameters.keySet()) {
//				params.add(new BasicNameValuePair(key, String.valueOf(parameters.get(key))));
//			}
//			try {
//				httpRequest.setEntity(new UrlEncodedFormEntity(params, DEFAULT_CHARSET));
//			} catch (UnsupportedEncodingException e) {
//				logger.warn("set parameter error ", e);
//			}
//		}
//
//		// set request headers
//		addHeaderForHttpRequestBase(httpRequest, requestHeaders);
//
//		try {
//			return doPost(httpClient, httpRequest);
//		} catch (Exception e) {
//			logger.warn("post to " + url + " param:" + parameters + " header: " + requestHeaders, e);
//		}
//		return null;
//	}
//
//	public static String doPost(HttpClient httpClient, HttpEntityEnclosingRequestBase httpRequest) throws Exception {
//		HttpEntity rspEntity = null;
//		try {
//			long start = System.currentTimeMillis();
//			HttpResponse httpResponse = httpClient.execute(httpRequest);
//			if (isSucess(httpResponse.getStatusLine().getStatusCode())) {
//				rspEntity = httpResponse.getEntity();
//				if (rspEntity != null) {
//					byte[] bytes = EntityUtils.toByteArray(rspEntity);
//					String responseMinStr = logHttpRespone(bytes, DEFAULT_CHARSET);
//					logger.info("HTTP POST : " + (httpRequest.getURI()) + ", Response is [" + responseMinStr + "], Length is [" + (bytes != null ? bytes.length : 0) + "]");
//					return new String(bytes, DEFAULT_CHARSET);
//				}
//				long offset = System.currentTimeMillis() - start;
//				if (offset > warnTime)
//					logConnectionStats(offset, httpRequest.getURI().toString());
//			} else {
//				// 查看响应状态 如果不是正常状态 则主动abort请求并返回{
//				logger.warn("target return error status, the url is :" + httpRequest.getURI());
//				rspEntity = httpResponse.getEntity();
//
//				if (rspEntity != null) {
//					logger.warn("target return error message, the data is :" + EntityUtils.toString(rspEntity, DEFAULT_CHARSET));
//				}
//				httpRequest.abort();
//				return null;
//			}
//		} catch (Exception e) {
//			httpRequest.abort();
//			throw e;
//		} finally {
//			try {
//				if (rspEntity != null) {
//					EntityUtils.consume(rspEntity);
//				}
//			} catch (IOException e) {
//				logger.error(" final consume the stream ", e);
//			}
//		}
//		return null;
//	}
//
//	public static String doDelete(String url) {
//		return doDelete(url, DEFAULT_CHARSET, null);
//	}
//
//	public static String doDelete(String url, Map<String, Object> map) {
//		return doDelete(url, DEFAULT_CHARSET, map);
//	}
//
//	public static String doDelete(String url, String charset, Map<String, Object> map) {
//		return doDelete(url, charset, map, DEFAULT_TIMEOUT, null);
//	}
//
//	public static String doDelete(String url, String charset, Map<String, Object> map, int timeout, Map<String, String> requestHeaders) {
//		HttpClient httpClient = getHttpClient(timeout);
//		HttpDelete httpRequest = new HttpDelete(url);
//		HttpResponse httpResponse = null;
//		try {
//			setParameterForHttpRequestBase(httpRequest, map);
//			addHeaderForHttpRequestBase(httpRequest, requestHeaders);
//
//			httpResponse = httpClient.execute(httpRequest);
//
//			return getResponseContent(httpRequest, httpResponse);
//		} catch (Exception ex) {
//			logger.error("http post error with url " + url, ex);
//			httpRequest.abort();
//		} finally {
//			try {
//				if (httpResponse != null) {
//					EntityUtils.consume(httpResponse.getEntity());
//				}
//			} catch (IOException e) {
//				logger.error(" final consume the stream ", e);
//			}
//		}
//		return null;
//	}
//
//	public static String doPut(String url, String charset, Map<String, Object> map, int timeout) {
//		HttpClient httpClient = getHttpClient(timeout);
//
//		HttpPut httpRequest = new HttpPut(url);
//		HttpResponse httpResponse = null;
//		try {
//			setParameterForHttpRequestBase(httpRequest, map);
//			httpResponse = httpClient.execute(httpRequest);
//
//			return getResponseContent(httpRequest, httpResponse);
//		} catch (Exception ex) {
//			logger.error("http post error with url " + url, ex);
//			httpRequest.abort();
//		} finally {
//			try {
//				if (httpResponse != null) {
//					EntityUtils.consume(httpResponse.getEntity());
//				}
//			} catch (IOException e) {
//				logger.error(" final consume the stream ", e);
//			}
//		}
//		return null;
//	}
//
//	private static String getResponseContent(HttpRequestBase httpRequest, HttpResponse httpResponse) throws ParseException, IOException {
//		HttpEntity entity = null;
//		if (isSucess(httpResponse.getStatusLine().getStatusCode())) {
//			entity = httpResponse.getEntity();
//			if (entity != null) {
//				return EntityUtils.toString(entity, DEFAULT_CHARSET);
//			}
//		} else {
//			// 查看响应状态 如果不是正常状态 则主动abort请求并返回{
//			httpRequest.abort();
//			logger.warn("target return error status, the url is :" + httpRequest.getURI());
//			return null;
//		}
//		return null;
//	}
//
//	/**
//	 * set parameter for HttpRequestBase
//	 * 
//	 * @param httpRequest
//	 * @param params
//	 */
//	private static void setParameterForHttpRequestBase(HttpRequestBase httpRequest, Map<String, Object> params) {
//		if (params != null && !params.isEmpty()) {
//			for (String key : params.keySet()) {
//				httpRequest.getParams().setParameter(key, params.get(key));
//			}
//		}
//	}
//
//	/**
//	 * add headers for HttpRequestBase
//	 * 
//	 * @param httpRequest
//	 * @param requestHeaders
//	 */
//	private static void addHeaderForHttpRequestBase(HttpRequestBase httpRequest, Map<String, String> requestHeaders) {
//		if (requestHeaders != null && !requestHeaders.isEmpty()) {
//			for (String key : requestHeaders.keySet()) {
//				httpRequest.addHeader(key, requestHeaders.get(key));
//			}
//		}
//	}
//
//	/**
//	 * 打印HTTP的响应
//	 * 
//	 * @author jame.xu
//	 * @date 2014-4-3
//	 * @description
//	 * @mail jame.xu@vipshop.com
//	 */
//	private static String logHttpRespone(byte[] data, String charset) {
//		if (data != null && data.length > 0) {
//			int length = data.length >= 16 ? 16 : data.length;
//			byte[] dest = new byte[length];
//			System.arraycopy(data, 0, dest, 0, length);
//			try {
//				String result = new String(dest, charset);
//				if (null != result) {
//					result = result.replace("\r\n", " ");
//					return result;
//				}
//			} catch (UnsupportedEncodingException e) {
//			}
//		}
//		return null;
//	}
//
//	// ########################################################################
//	// ######################### Private Method #############################
//	// ########################################################################
//
//	/**
//	 * 根据路径读取配置文件
//	 */
//	private static void initPro(String path) {
//		InputStream is = AHttpClientUtils.class.getResourceAsStream(path);
//		if (null == is)
//			logger.warn("HC Cannot find the configuration file 'hcConfig.properties' -- Please check!");
//		else
//			try {
//				pro.load(is);
//			} catch (IOException e) {
//				logger.warn("HC Init HttpClientUtils Config ERROR: Load Default Config -- Please check!", e);
//			}
//	}
//
//	/**
//	 * 读取Int
//	 */
//	private static int readInt(String key, int def) {
//		return (int) readLong(key, def);
//	}
//
//	/**
//	 * 读取Long
//	 */
//	private static long readLong(String key, long def) {
//		String str = readString(key, def + "", true);
//		try {
//			return Long.parseLong(str.trim());
//		} catch (NumberFormatException e) {
//			logger.warn("HC read Config '" + key + "' is not Num. " + str + ". def: " + def);
//			return def;
//		}
//	}
//
//	/**
//	 * 读取String
//	 */
//	private static String readString(String key, String def, boolean warn) {
//		String str = pro.getProperty(key);
//		String msg = "is Null.";
//		if (null != str)
//			return str;
//		if (warn)
//			logger.warn("HC read Config '" + key + "' setDefault: " + def + ". Because Value " + msg);
//		return def;
//	}
//
//	/**
//	 * 从pro中读取指定前缀的key内容
//	 */
//	private static Map<String, String> readByPprefix(String prefix) {
//		Map<String, String> map = new HashMap<String, String>();
//		for (Object ko : pro.keySet())
//			if (ko.toString().startsWith(prefix)) {
//				String key = ko.toString();
//				String value = pro.getProperty(key);
//				key = key.substring(prefix.length());
//				map.put(key, value);
//			}
//		return map;
//	}
//
//	/**
//	 * 初始化Host线程池配置列表 & 自定义HostDns配置列表
//	 */
//	private static void init() {
//		// static param
//		DEFAULT_TIMEOUT = readInt("def.readTimeout", 5);
//		USER_AGENT = readString("def.useragent", null, true);
//		SOCKET_BUFFER_SIZE = readInt("def.socketBufferSize", 8192);
//		DEFAULT_CONNECTION_TIMEOUT = readInt("def.getConnTimeout", 1000);
//		TCP_NO_DELAY = Boolean.parseBoolean(readString("def.tcpNoDelay", "false", true));
//
//		// init Host Config
//		Map<String, String> urimap = readByPprefix("http.uri.");
//		for (String key : urimap.keySet()) {
//			String uri = urimap.get(key);
//			try {
//				URL url = new URL(uri);
//				int pool = readInt("http.pool." + key, -1);
//				String useragent = readString("http.useragent." + key, null, false);
//				if (0 < pool)
//					routeMaxMap.put(url, pool);
//				if (null != useragent)
//					userAgentMap.put(url.getHost(), useragent);
//			} catch (MalformedURLException e) {
//				logger.warn("HC init pool config, '" + uri + "' not URL. continue. -- Please check!");
//			}
//		}
//
//		// init DnsMap
//		if ("config".equalsIgnoreCase(readString("dnsResolver", "system", false))) {
//			Map<String, String> dmap = readByPprefix("dns.");
//			for (String key : dmap.keySet()) {
//				String value = dmap.get(key);
//				localDnsMap.put(key, value);
//				logger.info("HC replace DNS '" + key + "' to '" + value + "'");
//			}
//		}
//	}
//
//	/**
//	 * 设置特定USER_AGENT TODO 暂时没有使用
//	 */
//	protected static void setUserAgent(HttpClient httpClient, HttpRequestBase requestBase) {
//		URI uri = requestBase.getURI();
//		String ua = null;
//		if (null != uri)
//			ua = userAgentMap.get(uri.getHost());
//		else
//			ua = USER_AGENT;
//		if (null != ua)
//			httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, ua);
//	}
//
//	/**
//	 * 主要实现从配置文件中读取hosts映射，指向预定的目标去；没有配置的使用系统dns
//	 * 
//	 * @author JFan 2013-12-24 下午4:49:28
//	 */
//	static final class ConfigDnsResolver implements DnsResolver {
//
//		/*
//		 * （非 Javadoc）
//		 * 
//		 * @see org.apache.http.conn.DnsResolver#resolve(java.lang.String)
//		 */
//		@Override
//		public InetAddress[] resolve(String host) throws UnknownHostException {
//			String ip;
//			if ((ip = localDnsMap.get(host)) != null)
//				return InetAddress.getAllByName(ip);
//			// System DNS
//			return InetAddress.getAllByName(host);
//		}
//
//	}
//
//}
