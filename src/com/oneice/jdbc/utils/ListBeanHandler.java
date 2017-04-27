package com.oneice.jdbc.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


public class ListBeanHandler implements BeanHandler{

	/**
	 * 从结果集中取出放入到list
	 */
	@Override
	public Object handerResultSet(Class clazz, ResultSet rs)
			throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException,
			SQLException, IllegalArgumentException, InvocationTargetException {
		List<Object> list = new LinkedList<>();
		Field fields[] = clazz.getDeclaredFields();
		while(rs.next()){
			Object object = clazz.newInstance();
			for(Field field:fields){
				String label = field.getName();
				String value = rs.getString(label);
				field.setAccessible(true);
				field.set(object, value);
			}
			list.add(object);
			
		}
		return list;
	}
	
}
