package org.jfan.an.utils.http;
//package org.an.utils.http;
//
//import java.io.IOException;
//import java.net.InetAddress;
//import java.net.InetSocketAddress;
//import java.net.Socket;
//import java.net.SocketAddress;
//import java.net.UnknownHostException;
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;
//
//import javax.net.SocketFactory;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;
//
//import org.apache.commons.httpclient.ConnectTimeoutException;
//import org.apache.commons.httpclient.params.HttpConnectionParams;
//import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
//
///**
// * 信任目标服务器中的所有证书
// * 
// * JFan 2011-11-11 下午04:34:41
// */
//public class HttpProtocolServerKeyStore implements ProtocolSocketFactory {
//
//	private SSLContext sslContext = null;
//
//	public HttpProtocolServerKeyStore() throws Exception {
//		sslContext = SSLContext.getInstance("TLS");
//		sslContext.init(null, new TrustManager[] { new TrustAnyTrustManager() }, null);
//	}
//
//	/*
//	 * （非 Javadoc）
//	 * 
//	 * @see
//	 * org.apache.commons.httpclient.protocol.ProtocolSocketFactory#createSocket
//	 * (java.lang.String, int)
//	 */
//	public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
//		return sslContext.getSocketFactory().createSocket(host, port);
//	}
//
//	/*
//	 * （非 Javadoc）
//	 * 
//	 * @see
//	 * org.apache.commons.httpclient.protocol.ProtocolSocketFactory#createSocket
//	 * (java.lang.String, int, java.net.InetAddress, int)
//	 */
//	public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException, UnknownHostException {
//		return sslContext.getSocketFactory().createSocket(host, port, clientHost, clientPort);
//	}
//
//	/*
//	 * （非 Javadoc）
//	 * 
//	 * @see
//	 * org.apache.commons.httpclient.protocol.ProtocolSocketFactory#createSocket
//	 * (java.lang.String, int, java.net.InetAddress, int,
//	 * org.apache.commons.httpclient.params.HttpConnectionParams)
//	 */
//	public Socket createSocket(String host, int port, InetAddress localAddress, int localPort, HttpConnectionParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
//		if (params == null)
//			throw new IllegalArgumentException("Parameters may not be null");
//		int timeout = params.getConnectionTimeout();
//		SocketFactory socketfactory = sslContext.getSocketFactory();
//		if (timeout == 0) {
//			return socketfactory.createSocket(host, port, localAddress, localPort);
//		} else {
//			Socket socket = socketfactory.createSocket();
//			SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
//			SocketAddress remoteaddr = new InetSocketAddress(host, port);
//			socket.bind(localaddr);
//			socket.connect(remoteaddr, timeout);
//			return socket;
//		}
//	}
//
//	/**
//	 * 关键: 信任所有证书
//	 */
//	private static class TrustAnyTrustManager implements X509TrustManager {
//
//		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//		}
//
//		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//		}
//
//		public X509Certificate[] getAcceptedIssuers() {
//			return new X509Certificate[] {};
//		}
//	}
//
//}
