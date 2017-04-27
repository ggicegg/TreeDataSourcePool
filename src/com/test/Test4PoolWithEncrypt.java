package com.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Test;

import com.oneice.tree.pool.TreeDataSourceFactory;
import com.oneice.tree.pool.TreeDataSourcePool;

public class Test4PoolWithEncrypt {

	@Test
	public void test4Encrypt() {
		MyEncryptProperties my = new MyEncryptProperties();

//		TreeDataSourcePool pool = new TreeDataSourceFactory(my).createDataConnectionPool();
		TreeDataSourcePool pool = new TreeDataSourceFactory().createDataConnectionPool();
		
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
