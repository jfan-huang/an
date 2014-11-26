/**
 * 
 */
package org.jfan.an.langyan;

/**
 * <br>
 * <br>
 * 
 * @author JFan - 2014年11月5日 上午11:32:25
 */
public interface LangyanSignalService {

	/**
	 * 向channel通道发送一条消息，消息不持久化，只有一个在线的人可接收到
	 */
	public void send(String channel, String msg);

	/**
	 * 向channel通道发送一条消息，消息不持久化，所有时下在线的人可以接受到
	 */
	public void publish(String channel, String msg);

	/**
	 * 订阅一个channel的消息，可以是send也可以是publish发送过来的
	 */
	public void receive(String channel, LangyanSignalListener listener);

}
