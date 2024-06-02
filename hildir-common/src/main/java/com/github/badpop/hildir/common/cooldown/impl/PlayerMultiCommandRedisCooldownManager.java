package com.github.badpop.hildir.common.cooldown.impl;

import com.github.badpop.hildir.common.cooldown.RedisCooldownManager;
import com.github.badpop.hildir.common.database.RedisConnectionPool;
import io.vavr.control.Option;
import lombok.NonNull;

import java.util.UUID;

/**
 * Cooldown manager for player commands with Redis.
 * This class manages the cooldowns of several commands at once.
 * <p>
 * For more information, see {@link RedisCooldownManager}.
 */
public class PlayerMultiCommandRedisCooldownManager extends RedisCooldownManager {

  public PlayerMultiCommandRedisCooldownManager(@NonNull RedisConnectionPool connectionPool) {
    super(connectionPool);
  }

  public void putUnderCooldown(@NonNull String commandName, @NonNull UUID target, @NonNull Long duration) {
    super.putUnderCooldown(computeRedisKey(commandName, target), duration);
  }

  public Long getOrPutUnderCooldown(@NonNull String commandName, @NonNull UUID target, @NonNull Long duration) {
    return super.getOrPutUnderCooldown(computeRedisKey(commandName, target), duration);
  }

  public Option<Long> get(@NonNull String commandName, @NonNull UUID target) {
    return super.get(computeRedisKey(commandName, target));
  }

  public Long getOrNull(@NonNull String commandName, @NonNull UUID target) {
    return super.getOrNull(computeRedisKey(commandName, target));
  }

  public Long getOrElse(@NonNull String commandName, @NonNull UUID target, @NonNull Long other) {
    return super.getOrElse(computeRedisKey(commandName, target), other);
  }

  boolean isUnderCooldown(@NonNull String commandName, @NonNull UUID target) {
    return super.isUnderCooldown(computeRedisKey(commandName, target));
  }

  public boolean isNotUnderCooldown(@NonNull String commandName, @NonNull UUID target) {
    return !isUnderCooldown(commandName, target);
  }

  public void removeCooldown(@NonNull String commandName, @NonNull UUID target) {
    super.removeCooldown(computeRedisKey(commandName, target));
  }

  private String computeRedisKey(@NonNull String commandName, UUID target) {
    return commandName + "_" + target.toString();
  }
}
