package com.oneice.tree.pool;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.oneice.tree.support.logging.LogFactory;

public class TreeDataSourcePool implements DataSource {
	private final org.apache.log4j.Logger logger = LogFactory.getLog(TreeDataSourcePool.class);

	// 使用相对效率较高的读写锁
	public static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	public static Lock readLock = readWriteLock.readLock();
	public static Condition condition = null;
	public static Lock writeLock = readWriteLock.writeLock();
	public static ReentrantLock lock = new ReentrantLock();
	private ProxyConnectionFactory proxyConnectionFactory;

	// 存放连接的集合
	private final LinkedList<Connection> pool = new LinkedList();

	public LinkedList<Connection> getPool() {
		return pool;
	}

	// 连接池的配置属性
	private static String url;
	private static String name;
	private static String password;
	private static String driver;
	private static int idlesize;
	private static int addsize;
	private static int maxsize;

	// 正在使用中的连接数
	public static int inused = 0;

	public static int getInused() {
		return inused;
	}

	public static String getUrl() {
		return url;
	}

	public static void setUrl(String url) {
		TreeDataSourcePool.url = url;
	}

	public static String getName() {
		return name;
	}

	public static void setName(String name) {
		TreeDataSourcePool.name = name;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		TreeDataSourcePool.password = password;
	}

	public static String getDriver() {
		return driver;
	}

	public static void setDriver(String driver) {
		TreeDataSourcePool.driver = driver;
	}

	public static int getIdlesize() {
		return idlesize;
	}

	public static void setIdlesize(int idlesize) {
		TreeDataSourcePool.idlesize = idlesize;
	}

	public static int getAddsize() {
		return addsize;
	}

	public static void setAddsize(int addsize) {
		TreeDataSourcePool.addsize = addsize;
	}

	public static int getMaxsize() {
		return maxsize;
	}

	public static void setMaxsize(int maxsize) {
		TreeDataSourcePool.maxsize = maxsize;
	}

	public TreeDataSourcePool() {
		condition = lock.newCondition();
	}

	/**
	 * 连接池的关闭
	 * 
	 * @author ice
	 */
	public void close() {
		pool.clear();
		logger.info("关闭连接池，清空所有元素");
	}

	/**
	 * 加载空转的连接 使用protected将其保护起来
	 * 
	 * @param size
	 *            加载的数目
	 * @author ice
	 */
	protected void addConn() {
		pool.clear();
		logger.info("清空所有元素");
		addConn(idlesize);
		logger.info("添加初始的连接");
	}

	/**
	 * 加载指定数目的连接
	 * 
	 * @param size
	 * @author ice
	 */
	private void addConn(int size) {
		// writeLock.lock();
		// lock.lock();
		proxyConnectionFactory = new ProxyConnectionFactory(this);
		for (int i = 0; i < size; i++) {
			Connection connection = proxyConnectionFactory.createProxyConnecion();
			pool.add(connection);
		}
		// writeLock.unlock();
		// lock.unlock();
	}

	@Override
	/**
	 * 从池中获取连接
	 */
	public Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
		// readLock.lock();
		lock.lock();
		Connection connection = null;
		try {
			while (pool.size() == 0) {
				// wait();
				// condition.await();
				if (inused == maxsize) {
					logger.info("获取连接失败，请等待，当前使用的连接以达到最大值");
					// wait();
					condition.await();
				} else {
					// 如果剩余空间不够每次增加连接数，就只将剩下的空位填满
					int trueSize = 0;
					trueSize = (maxsize - pool.size()) > addsize ? addsize : (maxsize - pool.size());
					addConn(trueSize);
					// notifyAll();
					try{
						condition.notifyAll();
					}catch(Exception e){
						
					}
				}
			}
			connection = pool.removeFirst();
			inused++;
			logger.info("当前使用了" + inused + "个连接,连接池的大小为" + pool.size());
			return (Connection) connection;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// readLock.unlock();
			lock.unlock();
		}
		return connection;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}
