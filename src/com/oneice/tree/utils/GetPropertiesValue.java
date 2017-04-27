package com.oneice.tree.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Properties;

public class GetPropertiesValue {
	public static final int ROOT_TYPE = 1;
	public static final int CURRENT_TYPE = 2;
	private static int findType=ROOT_TYPE;
	private static Properties properties = null;
	static{
		properties = new Properties();
	}
	/**
	 * 通用从类加载目录加载配置资源文件工具具体实现方法
	 * @return
	 * @param key 要找的关键字
	 * @param type 从当前目录还是根目录寻找
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static String getValueBykey(String key,int type,String filePath) throws FileNotFoundException, IOException,RuntimeException{
		String value = "";
		String path = null;
		switch(type){
		case ROOT_TYPE:
			//从这里拿到的file路径如果包含中文，会被URL编码成%__所以使用URLDecoder解码
			//使用getClassLoader()会从编译根目录下加载。问题：如果这里使用'/',指的是哪里
			path = GetPropertiesValue.class.getClassLoader().getResource
					(filePath).getFile();
			break;
		case CURRENT_TYPE:
			//不使用getClassLoader()会从该类的包目录下加载,也可以再getResource()中使用'/'根目录
			//最合适的应该是再编译根目录下建立Resource文件夹目录
			path = GetPropertiesValue.class.getResource
					(filePath).getFile();
			break;
		default:
				break;
		}
		
		if(path == null)
		{
			throw new RuntimeException("没有找到路径");
		}
//		System.out.println(path);
		//解码后的路径才能被FileInputStream或者FileReader类读入
		path = URLDecoder.decode(path, "UTF-8");
//		System.out.println(path);
		File file = new File(path);
		properties.load(new FileReader(path));
//		System.out.println(file);
		value = properties.getProperty(key);
//		if(value == null){
//			throw new RuntimeException("没有找到这个值");
//		}
		return value;
	}
	
	/**
	 * 从选定目录下默认properties文件中得到数据，默认文件名为mysql.properties,选择根目录还是当前目录开始查找文件
	 * @param key 要得到的数据
	 * @param type 查找的方式，从root目录下找还是当前目录下找
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String getValue(String key,int type) throws FileNotFoundException, IOException{
		String filePath = "mysql.properties";
		String value = getValueBykey(key, type, filePath);
		return value;
	}
	
	/**
	 * 从选定目录下指定properties文件中得到数据，选择根目录还是当前目录查找文件
	 * @param key 要得到的数据
	 * @param type 查找的方式，从root目录下找还是当前目录下找
	 * @param fileName 被查找的文件名
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String getValue(String key,int type,String fileName) throws FileNotFoundException, IOException{
		String filePath = fileName+".properties";
		String value = getValueBykey(key, type, filePath);
		return value;
	}
	
	/**
	 * 从指定目录下的指定properties文件中得到数据，选择根目录还是当前目录开始查找文件
	 * @param key 要得到的数据
	 * @param type 查找的方式，从root目录下找还是当前目录下找
	 * @param fileName 被查找的文件名
	 * @param filePath 被查找的文件路径（相对于根目录或当前目录）
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String getValue(String key,int type,String fileName,String filePath) throws FileNotFoundException, IOException{
		if(filePath == null || filePath.equals("")){
			filePath = fileName+".properties";
		}
		else{
			filePath = filePath+"/"+fileName+".properties";
		}
		String value = getValueBykey(key, type, filePath);
		return value;
	}
	
	/**
	 * 从GetPropertiesValue目录指定properties文件中得到数据，默认文件名是mysql.properties
	 * @param key
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String getValue(String key) throws FileNotFoundException, IOException{
		String filePath = "mysql.properties";
		String value = getValueBykey(key, findType, filePath);
		return value;
	}
	
	/**
	 * 从GetPropertiesValue目录指定properties文件中得到数据
	 * @param key
	 * @param fileName 指定properties文件名
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String getValue(String key,String fileName) throws FileNotFoundException, IOException{
		String filePath = "";
		filePath = fileName+".properties";
		String value = getValueBykey(key, findType, filePath);
		return value;
	}
}
