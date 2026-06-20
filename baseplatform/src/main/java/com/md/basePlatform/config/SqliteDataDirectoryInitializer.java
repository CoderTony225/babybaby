package com.md.basePlatform.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 确保 SQLite 数据库文件所在目录存在。
 */
@Component
@Profile("sqlite")
public class SqliteDataDirectoryInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(SqliteDataDirectoryInitializer.class);

    private final String sqlitePath;

    public SqliteDataDirectoryInitializer(
            @Value("${app.sqlite.path:${user.dir}/data/uav.db}") String sqlitePath) {
        this.sqlitePath = sqlitePath;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Path path = Paths.get(sqlitePath).toAbsolutePath().normalize();
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
            log.info("SQLite database directory ready: {}", parent);
        }
    }
}
