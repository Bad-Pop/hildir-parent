package com.github.badpop.hildir.common.cooldown;

import com.github.badpop.hildir.common.database.RedisConnectionPool;
import io.vavr.control.Option;
import lombok.NonNull;
import lombok.val;
import redis.clients.jedis.Jedis;

import static io.vavr.API.Try;

/**
 * Class used to manage cooldowns in redis.
 *
 * <p>
 * This class will consume less memory than the {@link InMemoryCooldownManager} class if we store a lot of cooldowns.
 * However, it will generate network costs that we wouldn't have with in-memory cooldowns.
 *
 * <p>
 * This class can also be used to manage multi-command cooldowns.
 */
public abstract non-sealed class RedisCooldownManager implements CooldownManager<String> {

  private final RedisConnectionPool connectionPool;

  public RedisCooldownManager(RedisConnectionPool connectionPool) {
    this.connectionPool = connectionPool;
  }

  private Jedis getClient() {
    return connectionPool.getResource();
  }

  public void putUnderCooldown(@NonNull String target, @NonNull Long duration) {
    try (Jedis client = getClient()) {
      //On stocke l'instant T (dans le futur) où le cooldown s'arrête en ms
      client.setex(target, duration, String.valueOf(computeNextAvailabilityInMs(duration)));
    }
  }

  public Option<Long> get(@NonNull String target) {
    try (Jedis client = getClient()) {
      val value = client.get(target);
      return Try(() -> Long.parseLong(value))
        .toOption()
        .map(nextAvailabilityMs -> nextAvailabilityMs - System.currentTimeMillis())
        .map(this::millisToSeconds);
    }
  }

  public void removeCooldown(@NonNull String target) {
    try (Jedis client = getClient()) {
      client.del(target);
    }
  }
}
