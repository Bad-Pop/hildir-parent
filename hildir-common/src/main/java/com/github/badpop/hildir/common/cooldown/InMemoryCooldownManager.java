package com.github.badpop.hildir.common.cooldown;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.vavr.control.Option;
import lombok.NonNull;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static io.vavr.API.Option;

/**
 * Class used to manage cooldowns directly in memory.
 * <p>
 * For performance reasons, no existing implementation can handle multi-command cooldowns.
 * However, it can be done with a little code.
 */
public abstract non-sealed class InMemoryCooldownManager<T> implements CooldownManager<T> {

  private final Cache<T, Long> cooldowns;

  /**
   * @param cooldowns the cache to be used in memory
   */
  public InMemoryCooldownManager(@NonNull Cache<T, Long> cooldowns) {
    this.cooldowns = cooldowns;
  }

  /**
   * @param duration the time before a cache entry is removed after being written
   * @param unit     the type of duration
   */
  public InMemoryCooldownManager(@NonNull Long duration, @NonNull TimeUnit unit) {
    this.cooldowns = CacheBuilder.newBuilder()
      .expireAfterWrite(duration, unit)
      .build();
  }

  /**
   * @param duration the time before a cache entry is removed after being written
   */
  public InMemoryCooldownManager(@NonNull Duration duration) {
    this.cooldowns = CacheBuilder.newBuilder()
      .expireAfterWrite(duration)
      .build();
  }

  public void putUnderCooldown(@NonNull T target, @NonNull Long duration) {
    cooldowns.put(target, computeNextAvailabilityInMs(duration));
  }

  public Option<Long> get(@NonNull T target) {
    return Option(cooldowns.getIfPresent(target))
      .map(nextAvailabilityMs -> nextAvailabilityMs - System.currentTimeMillis())
      .map(this::millisToSeconds);
  }

  public void removeCooldown(@NonNull T target) {
    cooldowns.invalidate(target);
  }
}
