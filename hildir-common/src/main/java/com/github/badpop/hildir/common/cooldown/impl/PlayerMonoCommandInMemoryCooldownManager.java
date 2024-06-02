package com.github.badpop.hildir.common.cooldown.impl;

import com.github.badpop.hildir.common.cooldown.InMemoryCooldownManager;
import com.google.common.cache.Cache;
import lombok.NonNull;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * A in memory cooldown manager for player commands.
 * This class does not manage multi-command.
 * <p>
 * For more information, see {@link InMemoryCooldownManager}.
 */
public class PlayerMonoCommandInMemoryCooldownManager extends InMemoryCooldownManager<UUID> {

  public PlayerMonoCommandInMemoryCooldownManager(@NonNull Cache<UUID, Long> cooldowns) {
    super(cooldowns);
  }

  public PlayerMonoCommandInMemoryCooldownManager(@NonNull Long duration, @NonNull TimeUnit unit) {
    super(duration, unit);
  }

  public PlayerMonoCommandInMemoryCooldownManager(@NonNull Duration duration) {
    super(duration);
  }
}
