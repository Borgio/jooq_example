package com.example.config;

import com.example.config.jooq.ExceptionTranslator;
import com.example.config.jooq.SpringTransactionProvider;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.jooq.ExecuteListenerProvider;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Created by cjemison on 1/28/17.
 */
@Configuration
@EnableTransactionManagement
public class WebConfig {
  @Value("${spring.ds_mysql.username}")
  private String user;

  @Value("${spring.ds_mysql.password}")
  private String password;

  @Value("${spring.ds_mysql.url}")
  private String dataSourceUrl;

  @Value("${spring.ds_mysql.driverClassName}")
  private String driverClassName;

  @Bean
  @Primary
  public DataSource dataSource() {
    final HikariConfig config = new HikariConfig();
    config.setDriverClassName(driverClassName);
    config.setJdbcUrl(dataSourceUrl);
    config.setUsername(user);
    config.setPassword(password);
    config.setConnectionTimeout(15000);
    config.setIdleTimeout(150000);
    config.setMaximumPoolSize(25);
    config.setMaxLifetime(900000);
    config.setConnectionTestQuery("select 1 from dual");
    config.setInitializationFailTimeout(0);
    config.addDataSourceProperty("cachePrepStmts", "true");
    config.addDataSourceProperty("prepStmtCacheSize", "250");
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

    final HikariDataSource ds = new HikariDataSource(config);
    ds.setDriverClassName(driverClassName);
    ds.setJdbcUrl(dataSourceUrl);
    ds.setUsername(user);
    ds.setPassword(password);
    return ds;
  }

  @Bean
  @Primary
  @Scope("singleton")
  public DataSourceTransactionManager dataSourceTransactionManager() {
    return new DataSourceTransactionManager(dataSource());
  }

  @Bean
  public TransactionAwareDataSourceProxy transactionAwareDataSource() {
    return new TransactionAwareDataSourceProxy(dataSource());
  }

  @Bean
  public DataSourceConnectionProvider connectionProvider() {
    return new DataSourceConnectionProvider(transactionAwareDataSource());
  }

  @Bean
  public SpringTransactionProvider jooqToSpringExceptionTransformer() {
    return new SpringTransactionProvider(dataSourceTransactionManager());
  }

  @Bean
  public ExceptionTranslator exceptionTranslator() {
    return new ExceptionTranslator();
  }

  @Bean
  public ExecuteListenerProvider executeListenerProvider() {
    return new DefaultExecuteListenerProvider(exceptionTranslator());
  }

  @Bean
  public org.jooq.Configuration jooqConfig() {
    return new DefaultConfiguration()
          .derive(connectionProvider())
          .derive(jooqToSpringExceptionTransformer())
          .derive(executeListenerProvider())
          .derive(SQLDialect.MARIADB);
  }

  @Bean
  public DefaultDSLContext dsl() {
    return new DefaultDSLContext(jooqConfig());
  }
}
