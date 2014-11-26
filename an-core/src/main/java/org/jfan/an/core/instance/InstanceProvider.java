package org.jfan.an.core.instance;

/**
 * 实例提供者接口。该接口封装了IoC工厂的具体实现。<br>
 * 它抽象 出IoC工厂的基本能力：提供某种指定类型（接口/类）的一个 实例。 <br>
 * <br>
 * 
 * @author JFan - 2014年10月30日 上午10:16:30
 */
public interface InstanceProvider {

	/**
	 * 获取指定接口或基类的实现类或子类的实例
	 * 
	 * @param <T> 类型参数
	 * @param beanClass 实例的类型
	 * @return 指定类型的实例。
	 */
	public <T> T getInstance(Class<T> beanClass);

	/**
	 * 根据beanName指定的名字获取指定接口或基类的实现类或子类的实例
	 * 
	 * @param <T> 类型参数
	 * @param beanName 实现类在容器中配置的名字
	 * @param beanClass 实例的类型
	 * @return 指定类型的实例。
	 */
	public <T> T[] getInstances(Class<T> beanClass);

}
