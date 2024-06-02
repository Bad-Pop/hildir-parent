package com.github.badpop.hildirapi.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.badpop.hildir.common.database.HikariConnectionPool;
import com.github.badpop.hildir.common.database.RedisConnectionPool;
import com.github.badpop.hildirapi.HildirApi;
import com.github.badpop.hildirapi.api.EconomyApi;
import com.github.badpop.hildirapi.api.EconomyManager;
import com.zaxxer.hikari.HikariConfig;
import io.vavr.jackson.datatype.VavrModule;
import lombok.val;
import me.TechsCode.UltraEconomy.UltraEconomy;
import me.TechsCode.UltraEconomy.UltraEconomyAPI;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import redis.clients.jedis.JedisPoolConfig;

import java.util.function.Supplier;

import static com.github.badpop.hildir.common.colors.HildirColors.LOG_COLOR;
import static io.vavr.API.Option;
import static io.vavr.API.Try;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

@Configuration
@Import({CommandConfiguration.class})
public class HildirApiConfiguration {

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper()
      .registerModule(new VavrModule())
      .registerModule(new JavaTimeModule())
      .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  @Bean
  public ComponentLogger logger(HildirApi hildirApi) {
    return hildirApi.getComponentLogger();
  }

  @Bean
  public FileConfiguration mainConfiguration(HildirApi hildirApi) {
    return hildirApi.getConfig();
  }

  @Bean
  public LuckPerms luckPerms(ComponentLogger logger) throws PluginApiHookException {
    return Option(Bukkit.getServicesManager().getRegistration(LuckPerms.class))
      .map(RegisteredServiceProvider::getProvider)
      .peek(unused -> logger.info(text("Successfully hook with LuckPerms api !").color(LOG_COLOR)))
      .onEmpty(() -> logger.error(text("Unable to hook with LuckPerms plugin... Maybe the plugin isn't installed on the server ?").color(RED)))
      .getOrElseThrow(() -> new PluginApiHookException("LuckPerms"));
  }

  @Bean
  public UltraEconomyAPI ultraEconomyAPI(ComponentLogger logger) throws PluginApiHookException {
    Supplier<UltraEconomyAPI> supplier = () -> {
      val api = UltraEconomy.getAPI();
      if(api != null) {
        logger.info(text("Successfully hook with UltraEconomy api !").color(LOG_COLOR));
        return api;
      } else {
        logger.error(text("Unable to hook with UltraEconomy plugin... Maybe the plugin isn't installed on the server ?").color(RED));
        return null;
      }
    };

    val api = supplier.get();
    if(api == null) {
      throw new PluginApiHookException("UltraEconomy");
    }
    return api;
  }

  @Bean
  public Economy vaultEconomy(ComponentLogger logger) throws PluginApiHookException {
    val isVaultEnabled = Bukkit.getServer().getPluginManager().getPlugin("Vault") != null;

    if(isVaultEnabled) {
      RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);

      if(rsp != null) {
        logger.info(text("Successfully hook with Vault api !").color(LOG_COLOR));
        return rsp.getProvider();
      } else {
        logger.error(text("Unable to hook with Vault plugin... Maybe the plugin isn't installed on the server ?").color(RED));
        throw new PluginApiHookException("Vault");
      }
    } else {
      logger.error(text("Unable to hook with Vault plugin... Maybe the plugin isn't installed on the server ?").color(RED));
      throw new PluginApiHookException("Vault");
    }
  }

  @Bean
  public EconomyApi economyApi(UltraEconomyAPI ultraEconomyAPI, Economy vaultEconomy) {
    return new EconomyManager(ultraEconomyAPI, vaultEconomy);
  }

  @Bean
  public HikariConnectionPool hikariConnectionPool(FileConfiguration mainConfig, ComponentLogger logger) throws Throwable {
    val JDBC_URL_TEMPLATE = "jdbc:mysql://%s:%s/%s?useSSL=%s&serverTimezone=UTC";
    return Try(() -> {
      val dbSection = mainConfig.getConfigurationSection("database");

      val host = dbSection.getString("host");
      val port = dbSection.getInt("port", 3306);
      val dbName = dbSection.getString("database");
      val user = dbSection.getString("user");
      val password = dbSection.getString("password");
      val useSSL = dbSection.getBoolean("use-ssl", false);
      val poolName = dbSection.getString("pool-name", "hildir-pool");
      val connectionTimeout = dbSection.getLong("connection-timeout-in-ms", 3000);
      val idleTimeout = dbSection.getLong("idle-timeout-in-ms", 60000);
      val minIdle = dbSection.getInt("min-idle", 5);
      val maxPoolSize = dbSection.getInt("max-pool-size", 20);
      val maxLifeTime = dbSection.getLong("max-life-time-in-ms", 1800000);
      val autoCommit = dbSection.getBoolean("auto-commit", true);

      val hikariConfig = new HikariConfig();
      hikariConfig.setJdbcUrl(JDBC_URL_TEMPLATE.formatted(host, port, dbName, useSSL));
      hikariConfig.setUsername(user);
      hikariConfig.setPassword(password);
      hikariConfig.setPoolName(poolName);
      hikariConfig.setConnectionTimeout(connectionTimeout);
      hikariConfig.setIdleTimeout(idleTimeout);
      hikariConfig.setMinimumIdle(minIdle);
      hikariConfig.setMaximumPoolSize(maxPoolSize);
      hikariConfig.setMaxLifetime(maxLifeTime);
      hikariConfig.setAutoCommit(autoCommit);

      return new HikariConnectionPool(hikariConfig);
    })
      .onFailure(throwable -> logger.error("Unable to connect to the database...", throwable))
      .onSuccess(bean -> logger.info(text("Successfully created database connection pool.").color(LOG_COLOR)))
      .getOrElseThrow(throwable -> throwable);
  }

  @Bean
  public RedisConnectionPool redisConnectionPool(FileConfiguration mainConfig, ComponentLogger logger) throws Throwable {
    return Try(() -> {
      val jedisSection = mainConfig.getConfigurationSection("redis");

      val poolConfig = new JedisPoolConfig();
      poolConfig.setMaxTotal(jedisSection.getInt("max-total", 20));
      poolConfig.setMaxIdle(jedisSection.getInt("max-idle", 20));
      poolConfig.setMinIdle(jedisSection.getInt("min-idle", 5));

      return new RedisConnectionPool(
        poolConfig,
        jedisSection.getString("host"),
        jedisSection.getInt("port", 6379),
        jedisSection.getString("user"),
        jedisSection.getString("password"));
    })
      .onFailure(throwable -> logger.error("Unable to build redis connection pool...", throwable))
      .onSuccess(bean -> logger.info(text("Successfully created redis connection pool.").color(LOG_COLOR)))
      .getOrElseThrow(throwable -> throwable);
  }
}
