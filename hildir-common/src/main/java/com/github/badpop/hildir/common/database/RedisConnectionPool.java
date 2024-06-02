package com.github.badpop.hildir.common.database;

import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

//TODO REFACTOR ? : MANAGER SECURED REDIS CONNECTIONS BECAUSE IT IS NOT DONE HERE. SEE -> https://redis.io/docs/latest/develop/connect/clients/java/jedis/#connect
public final class RedisConnectionPool {

  @Getter
  private final JedisPool connectionPool;

  public RedisConnectionPool(@NonNull JedisPool connectionPool) {
    this.connectionPool = connectionPool;
  }

  public RedisConnectionPool(@NonNull JedisPoolConfig poolConfig, String host, int port, String user, String password) {
    this.connectionPool = new JedisPool(poolConfig, host, port, user, password);
  }

  public RedisConnectionPool(String host, int port, String user, String password) {
    val poolConfig = new JedisPoolConfig();
    poolConfig.setMaxTotal(20);
    poolConfig.setMaxIdle(20);
    poolConfig.setMinIdle(5);
    poolConfig.setTestOnBorrow(true);
    poolConfig.setTestOnReturn(true);
    poolConfig.setTestWhileIdle(true);
    poolConfig.setMinEvictableIdleDuration(Duration.ofSeconds(60));
    poolConfig.setTimeBetweenEvictionRuns(Duration.ofSeconds(30));
    this.connectionPool = new JedisPool(poolConfig, host, port);
  }

  public Jedis getResource() {
    return connectionPool.getResource();
  }
}
