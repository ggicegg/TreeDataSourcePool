package com.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.oneice.tree.pool.TreeDataSourceFactory;
import com.oneice.tree.pool.TreeDataSourcePool;
import com.oneice.tree.utils.GetPropertiesValue;

public class Test4Pool {
	private Properties conf = new Properties();
	private static int i = 0;
	private static int MAX_THREAD_NUM = 3000;
	private static int threadCount = 0;
	@Before
	public void init() throws FileNotFoundException, IOException {

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

	}

	@Test
	public void test4CreatePool() {

		TreeDataSourcePool pool = null;
		pool = new TreeDataSourceFactory().createDataConnectionPool();
		try (Connection connection = pool.getConnection();) {
			String sql = "select * from user";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
}
