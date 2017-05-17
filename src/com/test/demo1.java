package com.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
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

public class demo1 {
	private static Object WAIT_OBJECT = new Object();
	private static DataSource pool;
//	static{
//		try {
//			Properties conf = new Properties();
//			String url = GetPropertiesValue.getValue("url");
//			String name = GetPropertiesValue.getValue("name");
//			String password = GetPropertiesValue.getValue("password");
//			String driver = GetPropertiesValue.getValue("driver");
//			int idlesize = Integer.parseInt(GetPropertiesValue.getValue("idlesize"));
//			int maxsize = Integer.parseInt(GetPropertiesValue.getValue("maxsize"));
//			int addsize = Integer.parseInt(GetPropertiesValue.getValue("addsize"));
//
//			conf.setProperty("url", url);
//			conf.setProperty("name", name);
//			conf.setProperty("password", password);
//			conf.setProperty("driver", driver);
//			conf.setProperty("idlesize", idlesize + "");
//			conf.setProperty("maxsize", maxsize + "");
//			conf.setProperty("addsize", addsize + "");
//			
//			pool = new TreeDataSourceFactory().createDataConnectionPool();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	private static int i = 0;
	private static int MAX_THREAD_NUM = 30;
	private static int MAX_CONNECTIONS = 1000;
	private static int threadCount = 0;
	private static CountDownLatch startLatch = new CountDownLatch(1);
	private static CountDownLatch endLatch = new CountDownLatch(MAX_THREAD_NUM);
	public static void main(String[] args) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, InterruptedException {
		
		long start = System.currentTimeMillis();
		demo1 d = new demo1();
		for(i = 0; i < MAX_THREAD_NUM;i++){
			new Thread(()->
			{
//				threadCount++;
				try {
					startLatch.await();
					String sql = "select * from user";
					Object[] params = new Object[0];
					ListBeanHandler handler = new ListBeanHandler();
					List<User> users = null;
					try {
//						synchronized(WAIT_OBJECT){
//							if(i < MAX_THREAD_NUM-1)
//							{
//								System.out.println(new Date().toLocaleString()+":"+i+"个线程被阻塞");
//								WAIT_OBJECT.wait();
//							}
//							System.out.println(new Date().toLocaleString()+":"+i+"个线程被唤醒!");
//							WAIT_OBJECT.notifyAll();
//						}
						users = d.query(sql, params, handler,i);
					} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException
							| IllegalArgumentException | InvocationTargetException | SQLException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
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

	private List<User> query(String sql, Object[] params, ListBeanHandler handler,int i) throws SQLException,
			InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InterruptedException {
		List<User> users = null;
//		if(i < MAX_THREAD_NUM-1)
//		{
//			System.out.println(new Date().toLocaleString()+":"+i+"个线程被阻塞");
//			wait();
//		}
		int k = MAX_CONNECTIONS;
		while(k-- > 0){
			users = (List<User>)DBUtils.query(sql, params, User.class, handler);
			for(User user:users){
				System.out.println(user);
			}
//			return users;
		}
//		System.out.println(new Date().toLocaleString()+":"+i+"个线程被唤醒!");
//		notifyAll();
//		WAIT_OBJECT.notifyAll();
//		return users;
		
		return null;
	}
	
	
	
}
class test{
	static{
		System.out.println("我是静态代码块");
	}
	public static void show(){
		System.out.println("我是静态方法show");
	}
}
