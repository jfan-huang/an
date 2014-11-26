/**
 * 
 */
package org.jfan.an.langyan.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.jfan.an.langyan.LangyanSignalListener;
import org.jfan.an.langyan.LangyanSignalService;

/**
 * <br>
 * <br>
 * 
 * @author JFan - 2014年11月10日 上午11:09:26
 */
public class ZookeeperLangyanSignalServiceImpl implements LangyanSignalService {

	private static final String PATH_PREFIX = "/zkly/";

	private String pathPrefix = PATH_PREFIX;
	private ZooKeeper zookeeper;

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.langyan.LangyanSignalService#send(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void send(String channel, String msg) {
		String path = pathPrefix + "s/" + channel;
		try {
			zookeeper.setData(path, msg.getBytes(), -1);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.langyan.LangyanSignalService#publish(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void publish(String channel, String msg) {
		// TODO 自动生成的方法存根

	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.langyan.LangyanSignalService#receive(java.lang.String,
	 * org.an.langyan.LangyanSignalListener)
	 */
	@Override
	public void receive(String channel, LangyanSignalListener listener) {

	}

	// ####
	// ## private func

	private void init() {

	}

	// ####
	// ## set func

	/**
	 * @param pathPrefix 要设置的 pathPrefix
	 */
	public void setPathPrefix(String pathPrefix) {
		this.pathPrefix = pathPrefix;
	}

	/**
	 * @param zookeeper 要设置的 zookeeper
	 */
	public void setZookeeper(ZooKeeper zookeeper) {
		this.zookeeper = zookeeper;
	}

}
