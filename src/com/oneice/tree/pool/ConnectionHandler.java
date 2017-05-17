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
//			TreeDataSourcePool.writeLock.lock();
			TreeDataSourcePool.lock.lock();
			logger.info("添加前连接池的连接数量为:"+treeDataSourcePool.getPool().size());
			try{
				//若池的空间和最大空间相等，直接关闭连接
//				if(treeDataSourcePool.getPool().size() == treeDataSourcePool.getMaxsize())
//				{
//					method.invoke(proxy, args);
//					logger.info("连接池中空间已满，将该连接关闭");
//				}
				treeDataSourcePool.getPool().add((Connection) proxy);
				try{
					treeDataSourcePool.condition.signalAll();
				}catch(Exception e){
					logger.info("返回连接到池时，唤醒别的线程失败",e);
				}
				
				treeDataSourcePool.inused--;
				//严重影响性能
				Method setAutoCommit = proxy.getClass().getMethod("setAutoCommit", boolean.class);
				setAutoCommit.invoke(proxy, true);
				logger.info("连接返还到池中，设置自动提交为true");
				logger.info("当前连接池的连接数量为:"+treeDataSourcePool.getPool().size());
			}finally{
//				TreeDataSourcePool.writeLock.unlock();
				TreeDataSourcePool.lock.unlock();
			}
		}else{
			return method.invoke(target, args);
		}
		return null;
	}
	
}
