package com.oneice.jdbc.utils;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface BeanHandler {
	/**
	 * 从rs中获取数据，放置入bean中
	 * @param clazz
	 * @param rs
	 * @return
	 */
	public Object handerResultSet(Class clazz,ResultSet rs) throws InstantiationException, IllegalAccessException, NoSuchMethodException, 
	SecurityException, SQLException, IllegalArgumentException, InvocationTargetException;

}
