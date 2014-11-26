/**
 * 
 */
package org.jfan.an.langyan;

/**
 * <br>
 * <br>
 * 
 * @author JFan - 2014年11月5日 下午2:02:51
 */
public interface LangyanSignalListener {

	/**
	 * 返回一个key，这个key用于防止重复添加监听事件
	 * 
	 * 应该是按做不同的动作、事件处理类型 来区分
	 */
	public String onlyKey();

	public void onMessage(String msg);

}
