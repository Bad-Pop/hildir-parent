package com.github.badpop.hildir.common.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.NonNull;

import java.sql.Connection;
import java.sql.SQLException;

public final class HikariConnectionPool {

  private final HikariConfig config;

  @Getter
  private final HikariDataSource dataSource;

  public HikariConnectionPool(@NonNull HikariConfig config) {
    this.config = config;
    this.dataSource = new HikariDataSource(config);
  }

  public HikariConnectionPool(@NonNull String jdbcUrl, @NonNull String username, @NonNull String password) {
    this.config = new HikariConfig();
    config.setJdbcUrl(jdbcUrl);
    config.setUsername(username);
    config.setPassword(password);
    config.setPoolName("main-pool");
    config.setConnectionTimeout(3000);
    config.setIdleTimeout(60000);
    config.setMinimumIdle(3);
    config.setMaximumPoolSize(20);
    config.setMaxLifetime(5000);
    config.setAutoCommit(true);
    this.dataSource = new HikariDataSource(config);
  }

  public Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }
}
