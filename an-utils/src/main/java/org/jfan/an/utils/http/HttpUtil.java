package org.jfan.an.utils.http;
//package org.an.utils.http;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.UnsupportedEncodingException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.httpclient.ConnectTimeoutException;
//import org.apache.commons.httpclient.Header;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.HttpMethod;
//import org.apache.commons.httpclient.HttpMethodBase;
//import org.apache.commons.httpclient.HttpMethodRetryHandler;
//import org.apache.commons.httpclient.NameValuePair;
//import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
//import org.apache.commons.httpclient.methods.GetMethod;
//import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
//import org.apache.commons.httpclient.methods.PostMethod;
//import org.apache.commons.httpclient.methods.RequestEntity;
//import org.apache.commons.httpclient.methods.StringRequestEntity;
//import org.apache.commons.httpclient.params.HttpMethodParams;
//import org.apache.commons.httpclient.protocol.Protocol;
//import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
//
//import com.sf.esb.platform.utils.protocol.HttpProtocolServerKeyStore;
//
//public final class HttpUtil {
//
//	/**
//	 * 通过POST取得HTML资源
//	 * 
//	 * @param url 目标URL
//	 */
//	public static String post(String url) throws Exception {
//		return call(true, url, null, null, null, null, null);
//	}
//
//	/**
//	 * 通过POST取得HTML资源
//	 * 
//	 * @param url 目标URL
//	 * @param params POST参数列表 String : String|String[]
//	 */
//	public static String post(String url, Map<String, ?> params) throws Exception {
//		return call(true, url, null, params, null, null, null);
//	}
//
//	/**
//	 * 通过POST取得HTML资源
//	 * 
//	 * @param url 目标URL
//	 * @param params POST参数列表 String : String|String[]
//	 * @param requestEncode 发送请求时的编码
//	 * @param responseEncode 返回内容的编码
//	 */
//	public static String post(String url, Map<String, ?> params, String requestEncode, String responseEncode) throws Exception {
//		return call(true, url, null, params, requestEncode, responseEncode, null);
//	}
//
//	/**
//	 * 通过POST取得HTML资源
//	 * 
//	 * @param url 目标URL
//	 * @param headerParam HEADER参数列表
//	 * @param params POST参数列表 String : String|String[]
//	 */
//	public static String post(String url, Map<String, Object> headerParam, Map<String, ?> params) throws Exception {
//		return call(true, url, headerParam, params, null, null, null);
//	}
//
//	/**
//	 * 通过POST取得HTML资源
//	 * 
//	 * @param url 目标URL
//	 * @param headerParam HEADER参数列表
//	 * @param params POST参数列表 String : String|String[]
//	 * @param requestEncode 发送请求时的编码
//	 * @param responseEncode 返回内容的编码
//	 */
//	public static String post(String url, Map<String, Object> headerParam, Map<String, ?> params, String requestEncode, String responseEncode) throws Exception {
//		return call(true, url, headerParam, params, requestEncode, responseEncode, null);
//	}
//
//	/**
//	 * 通过POST取得HTML资源
//	 * 
//	 * @param url 目标URL
//	 * @param headerParam HEADER参数列表
//	 * @param params POST参数列表 String : String|String[]
//	 * @param requestEncode 发送请求时的编码
//	 * @param responseEncode 返回内容的编码
//	 * @param psf 协议实现--信任指定证书库中的证书
//	 */
//	public static String post(String url, Map<String, Object> headerParam, Map<String, ?> params, String requestEncode, String responseEncode, ProtocolSocketFactory psf) throws Exception {
//		return call(true, url, headerParam, params, requestEncode, responseEncode, psf);
//	}
//
//	// ########################################################################
//
//	/**
//	 * 通过POST(设置BODY内容)获取HTML资源
//	 * 
//	 * @param url 目标URL
//	 */
//	public static String postByBody(String url) throws Exception {
//		return call(true, url, null, null, null, null, null);
//	}
//
//	/**
//	 * 通过POST(设置BODY内容)获取HTML资源
//	 * 
//	 * @param url 目标URL
//	 * @param params BODY内容 String | byte[] | InputStream
//	 */
//	public static String postByBody(String url, Object params) throws Exception {
//		return call(true, url, null, params, null, null, null);
//	}
//
//	/**
//	 * 通过POST(设置BODY内容)获取HTML资源
//	 * 
//	 * @param url 目标URL
//	 * @param params BODY内容 String | byte[] | InputStream
//	 * @param requestEncode 发送请求时的编码
//	 * @param responseEncode 返回内容的编码
//	 */
//	public static String postByBody(String url, Object params, String requestEncode, String responseEncode) throws Exception {
//		return call(true, url, null, params, requestEncode, responseEncode, null);
//	}
//
//	/**
//	 * 通过POST(设置BODY内容)获取HTML资源
//	 * 
//	 * @param url 目标URL
//	 * @param headerParam HEADER参数列表
//	 * @param params BODY内容 String | byte[] | InputStream
//	 */
//	public static String postByBody(String url, Map<String, Object> headerParam, Object params) throws Exception {
//		return call(true, url, headerParam, params, null, null, null);
//	}
//
//	/**
//	 * 通过POST(设置BODY内容)获取HTML资源
//	 * 
//	 * @param url 目标URL
//	 * @param headerParam HEADER参数列表
//	 * @param params BODY内容 String | byte[] | InputStream
//	 * @param requestEncode 发送请求时的编码
//	 * @param responseEncode 返回内容的编码
//	 */
//	public static String postByBody(String url, Map<String, Object> headerParam, Object params, String requestEncode, String responseEncode) throws Exception {
//		return call(true, url, headerParam, params, requestEncode, responseEncode, null);
//	}
//
//	/**
//	 * 通过POST(设置BODY内容)获取HTML资源
//	 * 
//	 * @param url 目标URL
//	 * @param headerParam HEADER参数列表
//	 * @param params BODY内容 String | byte[] | InputStream
//	 * @param requestEncode 发送请求时的编码
//	 * @param responseEncode 返回内容的编码
//	 * @param psf 协议实现--默认信任所有证书
//	 */
//	public static String postByBody(String url, Map<String, Object> headerParam, Object params, String requestEncode, String responseEncode, ProtocolSocketFactory psf) throws Exception {
//		return call(true, url, headerParam, params, requestEncode, responseEncode, psf);
//	}
//
//	// ########################################################################
//
//	/**
//	 * 通过GET获取HTML资源
//	 * 
//	 * @param url 目标URL
//	 */
//	public static String get(String url) throws Exception {
//		return call(false, url, null, null, null, null, null);
//	}
//
//	/**
//	 * 通过GET获取HTML资源
//	 * 
//	 * @param url 目标URL
//	 * @param requestEncode 发送请求时的编码
//	 * @param responseEncode 返回内容的编码
//	 */
//	public static String get(String url, String requestEncode, String responseEncode) throws Exception {
//		return call(false, url, null, null, requestEncode, responseEncode, null);
//	}
//
//	/**
//	 * 通过GET获取HTML资源
//	 * 
//	 * @param url 目标URL
//	 * @param headerParam HEADER参数列表
//	 * @param requestEncode 发送请求时的编码
//	 * @param responseEncode 返回内容的编码
//	 */
//	public static String get(String url, Map<String, Object> headerParam, String requestEncode, String responseEncode) throws Exception {
//		return call(false, url, headerParam, null, requestEncode, responseEncode, null);
//	}
//
//	/**
//	 * 通过GET获取HTML资源
//	 * 
//	 * @param url 目标URL
//	 * @param headerParam HEADER参数列表
//	 * @param requestEncode 发送请求时的编码
//	 * @param responseEncode 返回内容的编码
//	 * @param psf 协议实现--默认信任所有证书
//	 */
//	public static String get(String url, Map<String, Object> headerParam, String requestEncode, String responseEncode, ProtocolSocketFactory psf) throws Exception {
//		return call(false, url, headerParam, null, requestEncode, responseEncode, psf);
//	}
//
//	// ########################################################################
//	// ####################### Private
//	// ########################################################################
//
//	/**
//	 * 请求HTTP资源并返回
//	 * 
//	 * @param post 使用POST还是GET方式
//	 * @param url 目标URL
//	 * @param headerParam HEADER参数列表
//	 * @param params POST参数列表(Map,String,byte[],InputStream)
//	 * @param requestEncode 发送请求时的编码
//	 * @param responseEncode 返回内容的编码
//	 * @param psf 协议实现--默认信任所有证书(如果安装出错,就当做没有安装)
//	 * @return 响应内容
//	 * @throws Exception URL不正确|通讯异常|读取流失败|设置POST参数失败
//	 */
//	private static String call(boolean post, String url, Map<String, Object> headerParam, Object params, String requestEncode, String responseEncode, ProtocolSocketFactory psf) throws Exception {
//		String urlStr = url;
//		URL urlObj = new URL(urlStr);
//		ProtocolSocketFactory psf_ = psf;
//		HttpClient client = new HttpClient();
//
//		// 如果是SSL连接，同时没有设定协议实现，则默认信任服务器所有的证书
//		if (null == psf_ && "https".equals(urlObj.getProtocol()))
//			try {
//				psf_ = new HttpProtocolServerKeyStore();
//			} catch (Exception e) {
//				e.printStackTrace();// 不应该影响程序继续执行
//			}
//
//		// 设定协议实现
//		if (null != psf_) {
//			String protocol = urlObj.getProtocol();
//			int port = urlObj.getPort();
//			if (-1 == port && "https".equals(protocol))
//				port = 443;
//			if (-1 == port && "http".equals(protocol))
//				port = 80;
//			urlStr = urlObj.getFile();
//			client.getHostConfiguration().setHost(urlObj.getHost(), port, new Protocol(protocol, psf_, port));
//		}
//
//		// 设置超时时间(毫秒)
//		client.getHttpConnectionManager().getParams().setConnectionTimeout(20000);
//
//		// 设置超时次数(策略)
//		client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new TimeOutNum(1));
//
//		// 设置HEADER
//		List<Header> headers = new ArrayList<Header>();
//		if (null != headerParam)
//			// for (String key : headerParam.keySet())
//			// client.getParams().setParameter(key, headerParam.get(key));
//			for (String key : headerParam.keySet())
//				headers.add(new Header(key, headerParam.get(key).toString()));
//		client.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
//
//		// 构造HTTP Method
//		HttpMethodBase method = null;
//		if (post)
//			method = new PostMethod(urlStr);
//		else
//			method = new GetMethod(urlStr);
//
//		// 默认 US-ASCII ??
//		String encode = requestEncode;
//		if (null != encode && !"".equals(encode.trim()))
//			method.getParams().setContentCharset(encode);
//
//		if (null != params && post) {
//			RequestEntity re = null;
//			NameValuePair[] nvp = null;
//
//			// 设置POST请求参数
//			if (params instanceof Map) {
//				Map<?, ?> map = (Map<?, ?>) params;
//				List<NameValuePair> nvpList = new ArrayList<NameValuePair>();
//				for (Object key : map.keySet()) {
//					Object value_ = map.get(key);
//					if (value_.getClass().isArray())
//						for (Object value : (Object[]) value_)
//							nvpList.add(new NameValuePair(key.toString(), value.toString()));
//					else if (value_ instanceof Collection)
//						for (Object value : (Collection<?>) value_)
//							nvpList.add(new NameValuePair(key.toString(), value.toString()));
//					else
//						nvpList.add(new NameValuePair(key.toString(), value_.toString()));
//				}
//				// List To []
//				int index = 0;
//				nvp = new NameValuePair[nvpList.size()];
//				for (NameValuePair nvp_ : nvpList)
//					nvp[index++] = nvp_;
//			}
//			// 设置POST BODY内容 [MultipartRequestEntity 未启用]
//			else if (params instanceof String)
//				re = new StringRequestEntity((String) params);
//			else if (params instanceof byte[])
//				re = new ByteArrayRequestEntity((byte[]) params);
//			else if (params instanceof InputStream)
//				re = new InputStreamRequestEntity((InputStream) params);
//			else
//				System.out.println("HttpUtil 未知的Body内容:" + params);
//
//			// set
//			if (null != nvp)
//				((PostMethod) method).setRequestBody(nvp);
//			if (null != re)
//				((PostMethod) method).setRequestEntity(re);
//		}
//
//		// execute
//		client.executeMethod(method);
//		// HttpStatus.SC_OK
//
//		// 响应编码
//		encode = responseEncode;
//		if (null == encode || "".equals(encode.trim()))
//			encode = method.getResponseCharSet();
//
//		// 取结果
//		byte[] bytes = new byte[0];
//		InputStream stream = method.getResponseBodyAsStream();
//		if (null != stream) {
//			ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
//			byte[] buffer = new byte[256];
//			int readCount = 0;
//			while ((readCount = stream.read(buffer)) != -1)
//				outBuffer.write(buffer, 0, readCount);
//			bytes = outBuffer.toByteArray();
//		}
//
//		// return
//		try {
//			return new String(bytes, encode);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//			return new String(bytes);
//		}
//	}
//
//}
//
///**
// * 超时策略实现(次数)
// * 
// * JFan 2011-11-24 上午11:39:24
// */
//class TimeOutNum implements HttpMethodRetryHandler {
//
//	private int timeOutNum;
//
//	public TimeOutNum(int timeOutNum) {
//		this.timeOutNum = timeOutNum;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see
//	 * org.apache.commons.httpclient.HttpMethodRetryHandler#retryMethod(org.
//	 * apache.commons.httpclient.HttpMethod, java.io.IOException, int)
//	 */
//	public boolean retryMethod(HttpMethod method, IOException e, int num) {
//		// 如果是超时异常,并且次数小于指定次数
//		if (e instanceof ConnectTimeoutException && timeOutNum > num)
//			return true;
//		return false;
//
//	}
//
//}
