package com.ds.tools.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.ds.tools.util.mycat.DataNode;
import com.ds.tools.util.PropertiesFileUtil;
import com.ds.tools.util.mycat.SchemaLoaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.sql.SQLException;
import java.util.*;

/**
 * 动态数据源（数据源切换）
*/
public class DynamicDataSource extends AbstractRoutingDataSource {

	private final static Logger _log = LoggerFactory.getLogger(DynamicDataSource.class);

	private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();


	public static  String STANDALONE_DB="dndb0";			//默认的公共库
	public static  Set<String> DB_SET=new HashSet<>();		//其他库
	public static  Set<String> LOG1_DB_SET=new HashSet<>();	//log1对应的库
	public static  Set<String> LOG2_DB_SET=new HashSet<>();	//log2对应的库
	private static final String URL_PRE="jdbc:mysql://";
	private static final String URL_SUFF="?useUnicode=true&characterEncoding=utf-8&autoReconnect=true";

	public static final String DB_PRE="dn";
	public static final String LOG1_DB_PRE="log1dn";
	public static final String LOG2_DB_PRE="log2dn";

	@Override
	protected Object determineCurrentLookupKey() {
		String dataSource = getDataSource();
		_log.debug("the current datasource is :{}", dataSource);
		return dataSource;
	}

	/**
	 * 设置数据源
	 * @param dataSource
	 */
	public static void setDataSource(String dataSource) {
		contextHolder.set(dataSource);
	}

	/**
	 * 获取数据源
	 * @return
	 */
	public static String getDataSource() {
		return contextHolder.get();
	}

	/**
	 * 清除数据源
	 */
	public static void clearDataSource() {
		contextHolder.remove();
	}

	public void init(){
		_log.info(">> >> >>初始化所有数据源...");
		List<DataNode> dataNodeList= SchemaLoaderUtil.getDataNodeList();
		String path= DynamicDataSource.class.getClassLoader().getResource("jdbc.properties").getPath();
		PropertiesFileUtil propertiesFileUtil= PropertiesFileUtil.getInstance(path);
		Map<Object,Object> dataSourceMap=new HashMap<>();
		//初始化数据源
		for(DataNode dataNode:dataNodeList){
			DruidDataSource druidDataSource=new DruidDataSource();
			druidDataSource.setDriverClassName(propertiesFileUtil.get("jdbc.driver"));
			druidDataSource.setUrl(URL_PRE+dataNode.getDataHost().getWriteHostUrl()+"/"+dataNode.getDbName()+URL_SUFF);
			druidDataSource.setUsername(dataNode.getDataHost().getUserName());
			druidDataSource.setPassword(dataNode.getDataHost().getPassword());

			druidDataSource.setInitialSize(propertiesFileUtil.getInt("druid.initialSize"));
			druidDataSource.setMinIdle(propertiesFileUtil.getInt("druid.minIdle"));
			druidDataSource.setMaxActive(propertiesFileUtil.getInt("druid.maxActive"));
			druidDataSource.setMaxWait(propertiesFileUtil.getInt("druid.maxWait"));
			druidDataSource.setTimeBetweenEvictionRunsMillis(propertiesFileUtil.getInt("druid.timeBetweenEvictionRunsMillis"));
			druidDataSource.setMinEvictableIdleTimeMillis(propertiesFileUtil.getInt("druid.minEvictableIdleTimeMillis"));
			druidDataSource.setValidationQuery(propertiesFileUtil.get("druid.validationQuery"));
			druidDataSource.setTestWhileIdle(propertiesFileUtil.getBool("druid.testWhileIdle"));
			druidDataSource.setTestOnBorrow(propertiesFileUtil.getBool("druid.testOnBorrow"));
			druidDataSource.setTestOnReturn(propertiesFileUtil.getBool("druid.testOnReturn"));
			try {
				druidDataSource.setFilters(propertiesFileUtil.get("druid.filters"));
				druidDataSource.init();
				dataSourceMap.put(dataNode.getName(),druidDataSource);
				if(dataNode.getName().startsWith(DB_PRE)&&!dataNode.getName().equals(STANDALONE_DB)){
					DB_SET.add(dataNode.getName());
				}else if(dataNode.getName().startsWith(LOG1_DB_PRE)){
					LOG1_DB_SET.add(dataNode.getName());
				}else if(dataNode.getName().startsWith(LOG2_DB_PRE)){
					LOG2_DB_SET.add(dataNode.getName());
				}
				_log.info("数据库：{} 初始化成功！",dataNode.getName());
			} catch (SQLException e) {
				e.printStackTrace();
				_log.error("数据库：{} 初始化失败,程序中断！",dataNode.getName());
				System.exit(1);
			}
		}
		 setTargetDataSources(dataSourceMap);
		_log.info(">> >> >>所有数据源初始结束");
	}

	@Override
	public void afterPropertiesSet() {
		init();
		super.afterPropertiesSet();
	}

	public static void main(String[] args) {
		DynamicDataSource dynamicDataSource = new DynamicDataSource();
		dynamicDataSource.init();
	}


}
