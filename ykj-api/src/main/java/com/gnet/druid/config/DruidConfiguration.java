package com.gnet.druid.config;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.wall.WallConfig;
import com.gnet.druid.multids.DynamicDataSource;
import com.gnet.druid.multids.LmDataSourceProperties;

@Configuration
@EnableConfigurationProperties(LmDataSourceProperties.class)
public class DruidConfiguration {

	@Bean(name = "dataSource1", initMethod = "init", destroyMethod = "close")
	public DataSource dataSource1(DataSourceProperties dataSourceProperties) throws SQLException {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
		dataSource.setUrl(dataSourceProperties.getUrl());
		dataSource.setUsername(dataSourceProperties.getUsername());
		dataSource.setPassword(dataSourceProperties.getPassword());

		// 初始化大小，最小，最大
		dataSource.setInitialSize(5);
		dataSource.setMinIdle(5);
		dataSource.setMaxActive(20);
		// 配置获取连接等待超时的时间
		dataSource.setMaxWait(60000);
		// 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
		dataSource.setTimeBetweenEvictionRunsMillis(60000);
		// 配置一个连接在池中最小生存的时间，单位是毫秒
		dataSource.setMinEvictableIdleTimeMillis(300000);
		dataSource.setValidationQuery("select 1");
		dataSource.setTestWhileIdle(true);
		dataSource.setTestOnBorrow(false);
		dataSource.setTestOnReturn(false);
		// 打开PSCache，并且指定每个连接上PSCache的大小
		dataSource.setPoolPreparedStatements(true);
		dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
		// 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
		dataSource.addFilters("stat,wall,log4j");
		// 通过connectProperties属性来打开mergeSql功能；慢SQL记录
		dataSource.setConnectionProperties("druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000");

		return dataSource;
	}

	@ConditionalOnProperty(name = "spring.datasource2.enable")
	@Bean(name="dataSource2", initMethod = "init", destroyMethod = "close")
	public DataSource dataSourc2(@Autowired LmDataSourceProperties dataSourceProperties) throws SQLException {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
		dataSource.setUrl(dataSourceProperties.getUrl());
		dataSource.setUsername(dataSourceProperties.getUsername());
		dataSource.setPassword(dataSourceProperties.getPassword());

		// 初始化大小，最小，最大
		dataSource.setInitialSize(5);
		dataSource.setMinIdle(5);
		dataSource.setMaxActive(20);
		// 配置获取连接等待超时的时间
		dataSource.setMaxWait(60000);
		// 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
		dataSource.setTimeBetweenEvictionRunsMillis(60000);
		// 配置一个连接在池中最小生存的时间，单位是毫秒
		dataSource.setMinEvictableIdleTimeMillis(300000);
		dataSource.setValidationQuery("select 1");
		dataSource.setTestWhileIdle(true);
		dataSource.setTestOnBorrow(false);
		dataSource.setTestOnReturn(false);
		// 打开PSCache，并且指定每个连接上PSCache的大小
		dataSource.setPoolPreparedStatements(true);
		dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
		// 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
		dataSource.addFilters("stat,wall,log4j");
		// 通过connectProperties属性来打开mergeSql功能；慢SQL记录
		dataSource.setConnectionProperties("druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000");

		return dataSource;
	}

	@Bean
	@Primary
	public DynamicDataSource dataSource(@Autowired DataSource dataSource1, @Autowired DataSource dataSource2) {
		DynamicDataSource resolver = new DynamicDataSource();

		Map<Object, Object> dataSources = new HashMap<>();
		dataSources.put("dataSource1", dataSource1);
		dataSources.put("dataSource2", dataSource2);

		resolver.setTargetDataSources(dataSources);
		resolver.setDefaultTargetDataSource(dataSource1);

		return resolver;
	}

	@Bean
	public ServletRegistrationBean druidServlet() {
		ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");

		Map<String, String> initParameters = new HashMap<>();
		initParameters.put("allow", "127.0.0.1");
		initParameters.put("loginUsername", "dbsysuser");
		initParameters.put("loginPassword", "dbsysuserPwd");
		bean.setInitParameters(initParameters);

		return bean;
	}

}
