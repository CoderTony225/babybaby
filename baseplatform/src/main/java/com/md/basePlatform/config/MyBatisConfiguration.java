package com.md.basePlatform.config;

import java.time.LocalDateTime;
import java.util.Properties;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis 扩展配置：按 JDBC 厂商区分 databaseId，便于 SQL 方言分支。
 */
@Configuration
@MapperScan("com.md.basePlatform.repository.mapper")
public class MyBatisConfiguration {

    /**
     * 注册数据库标识解析器，供 Mapper XML 中 {@code _databaseId} 使用。
     *
     * @return DatabaseIdProvider
     */
    @Bean
    public DatabaseIdProvider databaseIdProvider() {
        VendorDatabaseIdProvider provider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();
        properties.setProperty("MySQL", "mysql");
        properties.setProperty("SQLite", "sqlite");
        provider.setProperties(properties);
        return provider;
    }

    /**
     * 注册 LocalDateTime 类型处理器（兼容 SQLite 文本存取）。
     *
     * @return ConfigurationCustomizer
     */
    @Bean
    public ConfigurationCustomizer localDateTimeHandlerCustomizer() {
        return configuration -> {
            TypeHandlerRegistry registry = configuration.getTypeHandlerRegistry();
            registry.register(LocalDateTime.class, FlexibleLocalDateTimeTypeHandler.class);
        };
    }
}
