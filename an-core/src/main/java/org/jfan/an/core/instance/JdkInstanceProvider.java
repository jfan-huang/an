package org.jfan.an.core.instance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * 用JDK6 ServiceLoader实现的实例提供者。使用它的话，根本不需要任何IoC框架，也不需要配置文件
 * 指明接口的实现类是哪个，系统会自行查找接口的实现类。
 * 举例：假如你有一个接口（或抽象类）a.b.c.Xxx和它的一个实现类x.y.z.XxxImpl，那么你只需要在/META-INF/services
 * 目录下创建一个文本文件a.b.c.Xxx，其内容中只包含一行文字：x.y.z.XxxImpl。那么，Jdk6InstanceProvider类的
 * getInstance(a.b.c.Xxx.class)方法就会实例化一个x.y.z.XxxImpl类的实例并返回给调用者。
 * 可以为接口提供多个实现类，只需要在文件a.b.c.Xxx中添加代表其他实现类名的行。这样，getInstance(Class<T> beanClass)
 * 方法就会返回列表中的第一个实现类的实例。也可以用getInstance(a.b.c.Xxx.class, "xyz")方法，获得标记为
 * Named("xyz")的实现类的实例。 该方法的灵活性在于：首先你不需要通过配置告诉系统哪个接口由哪个实现类来实现，系统会自行查找接口的实现类；其次，
 * 你可以把不同的实现类放在不同的jar里，通过在相应的jar的META-INF/services中定义接口的实现类，可以通过替换实现
 * jar的方式直接替换底层实现。 该方法的缺点包括：（1）JDK6以上才支持；（2）要求每个实现类都要有一个无参构造函数；（3）没有其他IoC框架那样的
 * Singleton等范围定义； <br>
 * <br>
 * 
 * @author JFan - 2014年10月30日 上午10:17:26
 */
public class JdkInstanceProvider implements InstanceProvider {

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.core.instance.InstanceProvider#getInstance(java.lang.Class)
	 */
	@Override
	public <T> T getInstance(Class<T> beanClass) {
		T[] is = getInstances(beanClass);
		if (null == is || 0 >= is.length)
			return null;
		if (1 != is.length)
			throw new IndexOutOfBoundsException("");// TODO
		return is[0];
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.an.core.instance.InstanceProvider#getInstances(java.lang.Class)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T[] getInstances(Class<T> beanClass) {
		ServiceLoader<T> load = ServiceLoader.load(beanClass);
		if (null == load)
			return null;
		Iterator<T> it = load.iterator();
		List<T> list = new ArrayList<T>();
		while (it.hasNext())
			list.add(it.next());
		return (T[]) list.toArray();
	}

}
