package com.github.badpop.hildirapi.config;

import com.github.badpop.hildir.common.cooldown.impl.PlayerMultiCommandRedisCooldownManager;
import com.github.badpop.hildir.common.database.RedisConnectionPool;
import com.github.badpop.hildirapi.HildirApi;
import com.github.badpop.hildirapi.command.HildirApiCommand;
import com.github.badpop.hildirapi.command.HildirApiVersionSubCommand;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandConfiguration {

  /**
   * Main command
   */
  @Bean
  public HildirApiCommand hildirApiCommand(HildirApiVersionSubCommand hildirApiVersionSubCommand) {
    return new HildirApiCommand(hildirApiVersionSubCommand);
  }

  /**
   * Version sub command
   */
  @Bean
  public HildirApiVersionSubCommand hildirApiVersionSubCommand(HildirApi api) {
    return new HildirApiVersionSubCommand(api);
  }

  @Bean
  public PlayerMultiCommandRedisCooldownManager playerMultiCommandRedisCooldownManager(RedisConnectionPool connectionPool) {
    return new PlayerMultiCommandRedisCooldownManager(connectionPool);
  }
}
