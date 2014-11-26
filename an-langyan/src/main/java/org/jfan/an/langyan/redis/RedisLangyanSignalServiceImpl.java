/**
 * 
 */
package org.jfan.an.langyan.redis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jfan.an.langyan.LangyanSignalListener;
import org.jfan.an.langyan.LangyanSignalService;
import org.jfan.an.log.Logger;
import org.jfan.an.log.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * <br>
 * <br>
 * 
 * @author JFan - 2014年11月5日 下午4:14:31
 */
public class RedisLangyanSignalServiceImpl implements LangyanSignalService {

	private static final Logger logger = LoggerFactory.getLogger(RedisLangyanSignalServiceImpl.class);
	private static final String MSETNX_SUFFIX = "_msetnx";
	private static final String P2P_PREFIX = "p2p_";

	// JedisMonitor 监控命令

	// 1. Sub(订阅端)的Jedis对象一旦开启subscribe（psubscribe）订阅则不能在进行其它操作。
	// 2. Pub广播的key值是不会写入Redis缓存中的，也就是通过get(key)是不会取得消息。
	private Jedis jedis;
	boolean receiveOK = false;
	private int maximumPoolSize = 5;
	private ExecutorService executorService;

	private String prefix = P2P_PREFIX;
	private String suffix = MSETNX_SUFFIX;
	private Lock lock = new ReentrantLock();
	private Map<String, LangyanSignalListener[]> listeners = new HashMap<String, LangyanSignalListener[]>();

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.langyan.LangyanSignalService#send(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void send(String channel, String msg) {
		long num = jedis.publish(prefix + channel, msg);
		logger.info("Langyan Send P2P '{}' receiveNum {}, msg: {}.", channel, num, msg);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.langyan.LangyanSignalService#publish(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void publish(String channel, String msg) {
		long num = jedis.publish(channel, msg);
		logger.info("Langyan Publish '{}' receiveNum {}, msg: {}.", channel, num, msg);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.langyan.LangyanSignalService#receive(java.lang.String,
	 * org.an.langyan.LangyanSignalListener)
	 */
	@Override
	public void receive(String channel, LangyanSignalListener listener) {
		if (null == listener) {
			logger.warn("Langyan Receive '{}' Listener is Null.", channel);
			return;
		}

		try {
			lock.lock();
			boolean add = true;

			LangyanSignalListener[] ls = listeners.get(channel);
			if (null == ls) {
				ls = new LangyanSignalListener[] { listener };
			} else {
				String key = listener.onlyKey();
				for (LangyanSignalListener l : ls)
					if (key.equals(l.onlyKey())) {
						add = false;
						break;
					}
				if (add) {
					LangyanSignalListener[] newLs = Arrays.copyOf(ls, ls.length + 1);
					newLs[newLs.length] = listener;
					ls = newLs;
				} else
					logger.info("Langyan Receive '{}' Repeat '{}' Listener {}.", channel, key, listener.getClass());
			}

			if (add) {
				logger.info("Langyan Receive '{}' Listener {}.", channel, listener.getClass());
				listeners.put(channel, ls);

				if (!receiveOK) {
					Future<?> submit = executorService().submit(new Runnable() {
						@Override
						public void run() {
							receive();
						}
					});
					logger.info("Future<?> submit = {}. ---->>>> Future ?????", submit);// TODO
					receiveOK = true;
				}
			}
		} finally {
			lock.unlock();
		}
	}

	// ####
	// ## private func

	private void receive() {
		jedis.psubscribe(new JedisPubSub() {

			@Override
			public void onMessage(String channel, String message) {
				logger.debug("Receive '{}' the message '{}'.", channel, message);
				LangyanSignalListener[] ls = listeners.get(channel);
				if (null == ls || 0 >= ls.length)
					logger.debug("Not subscribed to this channel '{}' news.", channel);
				else {
					logger.debug("Individual treatment '{}' to {}.", channel, ls.length);
					for (LangyanSignalListener l : ls)
						try {
							l.onMessage(message);
						} catch (Exception e) {
							logger.warn("The '{}' channel of news '{}', the error occurred in the {} treatment.", channel, message, l.getClass(), e);
						}
					logger.debug("The end of treatment '{}'.", message);
				}
			}

			@Override
			public void onPMessage(String pattern, String channel, String message) {
				// send
				if (channel.startsWith(prefix)) {
					// 只有一个人能处理
					boolean toMe = true;
					String msetnxKey = prefix + channel + suffix;
					long x = jedis.msetnx(msetnxKey);
					if (0 >= x) {
						toMe = false;
						logger.debug("The P2P '{}' Is not my.", channel);
					} else
						logger.debug("The P2P '{}' I Got.", channel);

					// i got, to me
					if (toMe) {
						try {
							onMessage(channel, message);
						} finally {
							logger.debug("The P2P '{}' Remove the occupying.", channel);
							jedis.del(msetnxKey);
						}
					}

					// subscribe
				} else
					onMessage(channel, message);
			}

			@Override
			public void onSubscribe(String channel, int subscribedChannels) {
				logger.debug(this + " onSubscribe('{}', {})", channel, subscribedChannels);
			}

			@Override
			public void onPSubscribe(String pattern, int subscribedChannels) {
				logger.debug(this + " onPSubscribe('{}', {})", pattern, subscribedChannels);
			}

			@Override
			public void onUnsubscribe(String channel, int subscribedChannels) {
				logger.debug(this + " onUnsubscribe('{}', {})", channel, subscribedChannels);
			}

			@Override
			public void onPUnsubscribe(String pattern, int subscribedChannels) {
				logger.debug(this + " onPUnsubscribe('{}', {})", pattern, subscribedChannels);
			}

		}, "*");
	}

	private ExecutorService executorService() {
		if (null == executorService) {
			logger.info("The ExecutorService Not Init, use default {}.", maximumPoolSize);
			executorService = Executors.newFixedThreadPool(maximumPoolSize);
		}
		return executorService;
	}

	// ####
	// ## set func

	/**
	 * @param jedis 要设置的 jedis
	 */
	public void setJedis(Jedis jedis) {
		this.jedis = jedis;
	}

	/**
	 * @param maximumPoolSize 要设置的 maximumPoolSize
	 */
	public void setMaximumPoolSize(int maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
	}

	/**
	 * @param executorService 要设置的 executorService
	 */
	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

}
