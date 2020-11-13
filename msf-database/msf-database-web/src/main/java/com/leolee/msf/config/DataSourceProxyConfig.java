package com.leolee.msf.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceProxyConfig {

    @Bean("originClient")
    @ConfigurationProperties(prefix = "spring.datasource.client")
    public DataSource dataSourceMaster() {
        return new DruidDataSource();
    }

    @Bean("originClient2")
    @ConfigurationProperties(prefix = "spring.datasource.client2")
    public DataSource dataSourceClient2() {
        return new DruidDataSource();
    }

    @Bean("originOrder")
    @ConfigurationProperties(prefix = "spring.datasource.order")
    public DataSource dataSourceOrder() {
        return new DruidDataSource();
    }

    @Bean("originStorage")
    @ConfigurationProperties(prefix = "spring.datasource.storage")
    public DataSource dataSourceStorage() {
        return new DruidDataSource();
    }

    @Bean("originPay")
    @ConfigurationProperties(prefix = "spring.datasource.pay")
    public DataSource dataSourcePay() {
        return new DruidDataSource();
    }

    @Bean(name = "client")
    public DataSourceProxy masterDataSourceProxy(@Qualifier("originClient") DataSource dataSource) {
        return new DataSourceProxy(dataSource);
    }

    @Bean(name = "client2")
    public DataSourceProxy client2DataSourceProxy(@Qualifier("originClient2") DataSource dataSource) {
        return new DataSourceProxy(dataSource);
    }

    @Bean(name = "order")
    public DataSourceProxy orderDataSourceProxy(@Qualifier("originOrder") DataSource dataSource) {
        return new DataSourceProxy(dataSource);
    }

    @Bean(name = "storage")
    public DataSourceProxy storageDataSourceProxy(@Qualifier("originStorage") DataSource dataSource) {
        return new DataSourceProxy(dataSource);
    }

    @Bean(name = "pay")
    public DataSourceProxy payDataSourceProxy(@Qualifier("originPay") DataSource dataSource) {
        return new DataSourceProxy(dataSource);
    }

    @Bean("dynamicDataSource")
    public DataSource dynamicDataSource(@Qualifier("client") DataSource dataSourceClient,
                                        @Qualifier("client2") DataSource dataSourceClient2,
                                        @Qualifier("order") DataSource dataSourceOrder,
                                        @Qualifier("storage") DataSource dataSourceStorage,
                                        @Qualifier("pay") DataSource dataSourcePay) {

        DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();

        Map<Object, Object> dataSourceMap = new HashMap<>(5);
        dataSourceMap.put(DataSourceKey.CLIENT.name(), dataSourceClient);
        dataSourceMap.put(DataSourceKey.CLIENT2.name(), dataSourceClient2);
        dataSourceMap.put(DataSourceKey.ORDER.name(), dataSourceOrder);
        dataSourceMap.put(DataSourceKey.STORAGE.name(), dataSourceStorage);
        dataSourceMap.put(DataSourceKey.PAY.name(), dataSourcePay);

        dynamicRoutingDataSource.setDefaultTargetDataSource(dataSourceOrder);
        dynamicRoutingDataSource.setTargetDataSources(dataSourceMap);

        DynamicDataSourceContextHolder.getDataSourceKeys().addAll(dataSourceMap.keySet());

        return dynamicRoutingDataSource;
    }

    @Bean
    @ConfigurationProperties(prefix = "mybatis")
    public MybatisSqlSessionFactoryBean sqlSessionFactoryBean(@Qualifier("dynamicDataSource") DataSource dataSource) {
        // 这里用 MybatisSqlSessionFactoryBean 代替了 SqlSessionFactoryBean，否则 MyBatisPlus 不会生效
        MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        mybatisSqlSessionFactoryBean.setDataSource(dataSource);
        return mybatisSqlSessionFactoryBean;
    }

}
