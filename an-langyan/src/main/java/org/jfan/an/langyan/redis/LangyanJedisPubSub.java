/**
 * 
 */
package org.jfan.an.langyan.redis;

import redis.clients.jedis.JedisPubSub;

/**
 * 实现一个JedisPubSub类，其中定义了在Sub订阅端的事件方法 JedisPubSub类中有两类方法：<br>
 * <br>
 * 常规模式:当订阅常规key时使用的方法<br>
 * onUnsubscribe(),onSubscribe(),onMessage(); <br>
 * <br>
 * 正则模式:当订阅正则模式key时使用的方法<br>
 * onPUnsubscribe(),onPSubscribe(),onPMessage(); <br>
 * 
 * @author JFan - 2014年11月5日 下午4:16:24
 */
public class LangyanJedisPubSub extends JedisPubSub {

	/*
	 * （非 Javadoc）
	 * 
	 * @see redis.clients.jedis.JedisPubSub#onMessage(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void onMessage(String channel, String message) {
		// 常规模式：收到匹配key值的消息时触发 arg0 key值 arg1 收到的消息值
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see redis.clients.jedis.JedisPubSub#onPMessage(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void onPMessage(String pattern, String channel, String message) {
		// 正则模式：收到匹配key值的消息时触发 arg0订阅的key正则表达式 arg1匹配上该正则key值 arg2收到的消息值
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see redis.clients.jedis.JedisPubSub#onSubscribe(java.lang.String, int)
	 */
	@Override
	public void onSubscribe(String channel, int subscribedChannels) {
		// 常规模式：启动订阅时触发 arg0 key值 arg1 订阅数量
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see redis.clients.jedis.JedisPubSub#onUnsubscribe(java.lang.String, int)
	 */
	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {
		// 常规模式：关闭订阅时触发 arg0 key值 arg1 订阅数量
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see redis.clients.jedis.JedisPubSub#onPUnsubscribe(java.lang.String,
	 * int)
	 */
	@Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {
		// 正则模式：关闭正则类型订阅时触发 arg0 key的正则表达式 arg1 订阅数量
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see redis.clients.jedis.JedisPubSub#onPSubscribe(java.lang.String, int)
	 */
	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {
		// 正则模式：启动正则类型订阅时触发 arg0 key的正则表达式 arg1 订阅数量
	}

}
