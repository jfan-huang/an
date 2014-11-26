/**
 * 
 */
package org.jfan.an.utils.http;

import org.apache.http.HttpHost;
import org.apache.http.conn.routing.HttpRoute;
import org.jfan.an.utils.http.HttpClientUtils;
import org.junit.Test;

/**
 * <br>
 * <br>
 * 
 * @author JFan - 2014年11月7日 下午2:23:35
 */
public class HttpClientUtilsTest {

	@Test
	public void get() {
		try {
			for (int i = 1; i <= 60; i++)
				System.out.println(">>" + HttpClientUtils.doGet("http://localhost:8081/webapi/pp?qwe=123"));

			HttpRoute hr1 = new HttpRoute(new HttpHost("127.0.0.1", 8081));
			HttpRoute hr2 = new HttpRoute(new HttpHost("192.168.33.214", 9090));

			for (int i = 1; i <= 60; i++) {
				Thread.sleep(1000);
				HttpClientUtils.logConnectionStats(1, "");
				HttpClientUtils.logConnectionStats(hr1, "");
				HttpClientUtils.logConnectionStats(hr2, "");
				System.out.println("\r\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
