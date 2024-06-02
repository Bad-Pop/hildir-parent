package com.github.badpop.hildirapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.badpop.hildir.common.cooldown.impl.PlayerMultiCommandRedisCooldownManager;
import com.github.badpop.hildir.common.database.HikariConnectionPool;
import com.github.badpop.hildir.common.database.RedisConnectionPool;
import com.github.badpop.hildir.common.dependencyinjection.PluginContextManager;
import com.github.badpop.hildir.common.dependencyinjection.PluginContextManager.PluginContext;
import com.github.badpop.hildirapi.api.EconomyApi;
import com.github.badpop.hildirapi.command.HildirApiCommand;
import com.github.badpop.hildirapi.config.HildirApiConfiguration;
import net.luckperms.api.LuckPerms;
import org.bukkit.plugin.java.JavaPlugin;

import static com.github.badpop.hildir.common.colors.HildirColors.LOG_COLOR;
import static io.vavr.API.Try;
import static io.vavr.API.run;
import static java.util.Collections.emptyList;
import static net.kyori.adventure.text.Component.text;

public final class HildirApi extends JavaPlugin {

  private static PluginContext<HildirApi> context;

  @Override
  public void onEnable() {
    getComponentLogger().info(text("""
            
      ██╗  ██╗██╗██╗     ██████╗ ██╗██████╗      █████╗ ██████╗ ██╗
      ██║  ██║██║██║     ██╔══██╗██║██╔══██╗    ██╔══██╗██╔══██╗██║
      ███████║██║██║     ██║  ██║██║██████╔╝    ███████║██████╔╝██║
      ██╔══██║██║██║     ██║  ██║██║██╔══██╗    ██╔══██║██╔═══╝ ██║
      ██║  ██║██║███████╗██████╔╝██║██║  ██║    ██║  ██║██║     ██║
      ╚═╝  ╚═╝╚═╝╚══════╝╚═════╝ ╚═╝╚═╝  ╚═╝    ╚═╝  ╚═╝╚═╝     ╚═╝ is starting...
      """).color(LOG_COLOR));

    //Config
    getConfig().options().copyDefaults();
    saveDefaultConfig();

    //Api Context building
    context = PluginContextManager.buildPluginContext(this, emptyList(), HildirApiConfiguration.class);

    //Register commands
    getCommand("hildirapi").setExecutor(context.getBean(HildirApiCommand.class));

    getComponentLogger().info(text("HildirApi is started !").color(LOG_COLOR));
  }

  @Override
  public void onDisable() {
    //Close redis connection pool
    Try(() -> context.getBean(RedisConnectionPool.class).getConnectionPool())
      .mapTry(jedisPool -> run(jedisPool::close))
      .onFailure(throwable -> getComponentLogger().warn("Unable to stop redis connection pool...", throwable))
      .onSuccess(unused -> getComponentLogger().info("Successfully stopped redis connection pool"));

    //Close database connection pool
    Try(() -> context.getBean(HikariConnectionPool.class).getDataSource())
      .mapTry(dataSource -> run(dataSource::close))
      .onFailure(throwable -> getComponentLogger().warn("Unable to stop database connection pool...", throwable))
      .onSuccess(unused -> getComponentLogger().info("Successfully stopped database connection pool"));
  }

  public static HildirApi getInstance() {
    return context.getBean(HildirApi.class);
  }

  public ObjectMapper getObjectMapper() {
    return context.getBean(ObjectMapper.class);
  }

  public HikariConnectionPool getHikariConnectionPool() {
    return context.getBean(HikariConnectionPool.class);
  }

  public RedisConnectionPool getRedisConnectionPool() {
    return context.getBean(RedisConnectionPool.class);
  }

  public PlayerMultiCommandRedisCooldownManager getPlayerMultiCommandCooldownManager() {
    return context.getBean(PlayerMultiCommandRedisCooldownManager.class);
  }

  public LuckPerms getLuckPerms() {
    return context.getBean(LuckPerms.class);
  }

  public static EconomyApi getEconomyApi() {
    return context.getBean(EconomyApi.class);
  }

  public static boolean beanExists(Class<?> clazz) {
    return Try(() -> context.getBean(clazz))
      .map(o -> true)
      .getOrElse(false);
  }

  public PluginContext<HildirApi> getContext() {
    return context;
  }
}
