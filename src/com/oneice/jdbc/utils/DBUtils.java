package com.oneice.jdbc.utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import com.oneice.tree.pool.TreeDataSourceFactory;
import com.oneice.tree.utils.GetPropertiesValue;



public class DBUtils {
	private static DataSource pool;
	static{
		try {
			Properties conf = new Properties();
			String url = GetPropertiesValue.getValue("url");
			String name = GetPropertiesValue.getValue("name");
			String password = GetPropertiesValue.getValue("password");
			String driver = GetPropertiesValue.getValue("driver");
			int idlesize = Integer.parseInt(GetPropertiesValue.getValue("idlesize"));
			int maxsize = Integer.parseInt(GetPropertiesValue.getValue("maxsize"));
			int addsize = Integer.parseInt(GetPropertiesValue.getValue("addsize"));

			conf.setProperty("url", url);
			conf.setProperty("name", name);
			conf.setProperty("password", password);
			conf.setProperty("driver", driver);
			conf.setProperty("idlesize", idlesize + "");
			conf.setProperty("maxsize", maxsize + "");
			conf.setProperty("addsize", addsize + "");
			
			pool = new TreeDataSourceFactory().createDataConnectionPool();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//query和update执行的方法体太像了，能提取出来不
	
	/**
	 * 执行更新、删除、插入操作，并返回所影响的列数
	 * @param sql  要执行的sql语句
	 * @param params sql语句中要用的参数
	 * @return
	 * @throws SQLException
	 */
	public synchronized static int update(String sql,Object[] params) throws SQLException{
		
		int result = 0;
		try(
				Connection connection = pool.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)
		)
		{
			if(params != null){
				for(int i = 0; i < params.length;i++){
					ps.setObject(i+1, params[i]);
				}
			}
			
			result = ps.executeUpdate();
		}
		return result;
	}
	/**
	 * 根据传入的参数返回一个bean，使用策略模式不同的生成数据用不同的handler处理
	 * @param sql
	 * @param params
	 * @param clazz
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 */
	public static Object query(String sql,Object[] params,Class clazz,BeanHandler handler) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		try(
			Connection conn = pool.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
		)
		{
			if(params != null)
			{
				for(int i = 0; i < params.length;i++){
					ps.setObject(i+1, params[i]);
				}
			}
			
			try(ResultSet rs = ps.executeQuery()){
				return handler.handerResultSet(clazz, rs);
			}
		}
	}
}


