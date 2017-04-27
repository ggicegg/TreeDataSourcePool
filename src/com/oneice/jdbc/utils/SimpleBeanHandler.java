package com.oneice.jdbc.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 实现BeanHandler接口的类，用来处理单一数据的结果集
 * @author ice
 *
 */
public class SimpleBeanHandler implements BeanHandler{

	
	
	/**
	 * 从结果集中取出数据（单条）
	 */
	@Override
	public Object handerResultSet(Class clazz, ResultSet rs)
			throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException,
			SQLException, IllegalArgumentException, InvocationTargetException {
		// TODO Auto-generated method stub
		Object object = clazz.newInstance();
		Field[] fields = clazz.getDeclaredFields();
		int i = 0;
		if(rs.next()){
			for (Field field : fields) {
				String label = field.getName();
				//有BigDecimal 不能转换成String异常
				//按照标签名拿数据，field中有什么字段，就在rs中找到这个字段5
				String value = rs.getString(label);
				field.setAccessible(true);
				field.set(object, value);
			}
		}
		
		return object;
	}
}