/**
 * 
 */
package org.jfan.an.langyan.redis;

import java.util.ArrayList;
import java.util.List;

import org.jfan.an.langyan.LangyanSignalListener;
import org.jfan.an.langyan.redis.RedisLangyanSignalServiceImpl;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.ShardedJedisPool;

/**
 * <br>
 * <br>
 * 
 * @author JFan - 2014年11月5日 下午6:41:33
 */
public class RedisLangyanSignalServiceImplTest {

	private RedisLangyanSignalServiceImpl service1;
	private RedisLangyanSignalServiceImpl service2;
	private RedisLangyanSignalServiceImpl service3;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		service1 = new RedisLangyanSignalServiceImpl();
		service1.setJedis(jedis("127.0.0.1", 6379));

		service2 = new RedisLangyanSignalServiceImpl();
		service2.setJedis(jedis("127.0.0.1", 6379));

		service3 = new RedisLangyanSignalServiceImpl();
		service3.setJedis(jedis("127.0.0.1", 6379));
	}

	@Test
	public void test() {
		System.out.println(1);
		service1.receive("C1", new LL("C1-key"));
		System.out.println(1);
		service1.receive("C2", new LL("C1-key"));
		service1.receive("C3", new LL("C3-key"));

		service2.receive("C1", new LL("SSS1-key"));
		service2.receive("C3", new LL("C3-key"));

		// service3.receive("AAA", new LL("aaa"));

		service3.publish("C1", "CCCC-1111");

		for (int i = 1; i <= 3; i++)
			try {
				System.out.println(i);
				Thread.sleep(1000);

				service3.publish("C1", "CCCC-" + i);
			} catch (InterruptedException e) {
			}
	}

	private Jedis jedis(String host, int port) {
		ShardedJedis resource = initialShardedPool(host, port).getResource();
		ShardedJedisPipeline pipelined = resource.pipelined();
		pipelined.sync();
		return initialPool(host, port).getResource();
	}

	private JedisPool initialPool(String host, int port) {
		// 池基本配置
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(20);
		config.setMaxIdle(5);
		config.setMaxWaitMillis(1000);
		config.setTestOnBorrow(false);

		return new JedisPool(config, host, port);
	}

	private ShardedJedisPool initialShardedPool(String host, int port) {
		// 池基本配置
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(20);
		config.setMaxIdle(5);
		config.setMaxWaitMillis(1000);
		config.setTestOnBorrow(false);
		// slave链接
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo(host, port, "master"));

		return new ShardedJedisPool(config, shards);
	}

}

class LL implements LangyanSignalListener {

	private String key;

	public LL(String key) {
		this.key = key;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.langyan.LangyanSignalListener#onlyKey()
	 */
	@Override
	public String onlyKey() {
		return key;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.langyan.LangyanSignalListener#onMessage(java.lang.String)
	 */
	@Override
	public void onMessage(String msg) {
		System.out.println(key + "\t" + this + "\t收到消息：" + msg);
	}

}
