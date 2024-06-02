package com.github.badpop.hildir.common.cooldown.impl;

import com.github.badpop.hildir.common.cooldown.RedisCooldownManager;
import com.github.badpop.hildir.common.database.RedisConnectionPool;
import io.vavr.control.Option;
import lombok.NonNull;

import java.util.UUID;

/**
 * Cooldown manager for player commands with Redis.
 * This class manages cooldowns for a single command at a time.
 * <p>
 * For more information, see {@link RedisCooldownManager}.
 */
public class PlayerMonoCommandRedisCooldownManager extends RedisCooldownManager {

  /**
   * Must be a unique value and not shared between several redis cooldown managers.
   */
  private final String commandName;

  /**
   * @param commandName Must be a unique value and not shared between several redis cooldown managers.
   */
  public PlayerMonoCommandRedisCooldownManager(@NonNull RedisConnectionPool connectionPool, @NonNull String commandName) {
    super(connectionPool);
    this.commandName = commandName;
  }

  public void putUnderCooldown(@NonNull UUID target, @NonNull Long duration) {
    super.putUnderCooldown(computeRedisKey(target), duration);
  }

  public Long getOrPutUnderCooldown(@NonNull UUID target, @NonNull Long duration) {
    return super.getOrPutUnderCooldown(computeRedisKey(target), duration);
  }

  public Option<Long> get(@NonNull UUID target) {
    return super.get(computeRedisKey(target));
  }

  public Long getOrNull(@NonNull UUID target) {
    return super.getOrNull(computeRedisKey(target));
  }

  public Long getOrElse(@NonNull UUID target, @NonNull Long other) {
    return super.getOrElse(computeRedisKey(target), other);
  }

  boolean isUnderCooldown(@NonNull UUID target) {
    return super.isUnderCooldown(computeRedisKey(target));
  }

  public boolean isNotUnderCooldown(@NonNull UUID target) {
    return !isUnderCooldown(target);
  }

  public void removeCooldown(@NonNull UUID target) {
    super.removeCooldown(computeRedisKey(target));
  }

  private String computeRedisKey(UUID target) {
    return commandName + "_" + target.toString();
  }
}
