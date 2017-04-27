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
import java.util.concurrent.TimeUnit;

import com.oneice.jdbc.utils.DBUtils;
import com.oneice.jdbc.utils.ListBeanHandler;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

import sun.applet.Main;

public class demo1 {
	private static int i = 0;
	private static int MAX_THREAD_NUM = 10000;
	private static int threadCount = 0;
	public static void main(String[] args) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		
		long start = System.currentTimeMillis();
		demo1 d = new demo1();
		for(i = 0; i < MAX_THREAD_NUM;i++){
			new Thread(()->
			{
//				threadCount++;
				String sql = "select * from user";
				Object[] params = new Object[0];
				ListBeanHandler handler = new ListBeanHandler();
				List<User> users = null;
				try {
					users = d.query(sql, params, handler,i);
				} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException
						| IllegalArgumentException | InvocationTargetException | SQLException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for(User user:users){
					System.out.println(user);
				}
				
				if(i == MAX_THREAD_NUM){
					long end = System.currentTimeMillis();
					System.out.println("用时:"+(end-start)+"ms");
				}
			}).start();
		}
		
		
	}

	private synchronized List<User> query(String sql, Object[] params, ListBeanHandler handler,int i) throws SQLException,
			InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InterruptedException {
		List<User> users;
		if(i < MAX_THREAD_NUM-1)
		{
			System.out.println(new Date().toLocaleString()+":"+i+"个线程被阻塞");
			wait();
		}
		users = (List<User>)DBUtils.query(sql, params, User.class, handler);
//		System.out.println(new Date().toLocaleStr++++++++++++++++++++++++++++++++++++++++++++++++++++++ing()+":"+i+"个线程被唤醒!");
		notifyAll();
		return users;
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
