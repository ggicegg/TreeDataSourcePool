package com.oneice.tree.pool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import org.apache.log4j.Logger;

import com.oneice.tree.support.logging.LogFactory;

/**
 * 代理Conneciont对象
 * @author ice
 */
public class ConnectionHandler implements InvocationHandler{
	Logger logger = LogFactory.getLog(ConnectionHandler.class);
	private Object target;
	private TreeDataSourcePool treeDataSourcePool;
	public ConnectionHandler(Object target,TreeDataSourcePool treeDataSourcePool){
		this.target = target;
		this.treeDataSourcePool = treeDataSourcePool;
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		if("close".equals(method.getName())){
			TreeDataSourcePool.writeLock.lock();
			LinkedList list = treeDataSourcePool.getPool();
			try{
				//若池的空间和最大空间相等，直接关闭连接
				if(list.size() == treeDataSourcePool.getMaxsize())
				{
					method.invoke(proxy, args);
					logger.info("连接池中空间已满，将该连接关闭");
				}
				treeDataSourcePool.getPool().add((Connection) proxy);
				treeDataSourcePool.inused--;
				Method setAutoCommit = proxy.getClass().getMethod("setAutoCommit", boolean.class);
				setAutoCommit.invoke(proxy, true);
				logger.info("连接返还到池中，设置自动提交为true");
			}finally{
				TreeDataSourcePool.writeLock.unlock();
			}
		}else{
			return method.invoke(target, args);
		}
		return null;
	}
	
}
