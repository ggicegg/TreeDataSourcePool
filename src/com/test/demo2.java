package com.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import com.oneice.jdbc.utils.DBUtils;
import com.oneice.jdbc.utils.ListBeanHandler;
import com.oneice.tree.pool.TreeDataSourceFactory;
import com.oneice.tree.utils.GetPropertiesValue;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

import sun.applet.Main;

public class demo2 {
	private static Object WAIT_OBJECT = new Object();
	private static DataSource pool;
	static {
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

	private static int i = 0;
	private static int MAX_THREAD_NUM = 300;
	private static int MAX_CONNECTIONS = 2000;
	private static int threadCount = 0;
	private static CountDownLatch startLatch = new CountDownLatch(1);
	private static CountDownLatch endLatch = new CountDownLatch(MAX_THREAD_NUM);
	public static void main(String[] args) throws SQLException, InstantiationException, IllegalAccessException,
			NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, InterruptedException {

		long start = System.currentTimeMillis();
		demo2 d = new demo2();
		for (i = 0; i < MAX_THREAD_NUM; i++) {
			new Thread(() -> {
				try {
					startLatch.await();
					try {
						int k = MAX_CONNECTIONS;
						while (k-- > 0) {
							Connection connection = pool.getConnection();
							connection.close();
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (SecurityException | IllegalArgumentException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					endLatch.countDown();
				}
				
			}).start();
		}
		
		System.out.println("线程被阻塞");
		startLatch.countDown();
		long s = System.currentTimeMillis();
		System.out.println("线程开始执行");
		endLatch.await();
		long e = System.currentTimeMillis();
		System.out.println("线程结束执行共用时:"+(e-s)+"毫秒");

	}

}
