package com.github.badpop.hildir.common.cooldown;

import io.vavr.control.Option;
import lombok.NonNull;

import java.util.concurrent.TimeUnit;

import static java.util.function.Function.identity;

/**
 * A cooldown is an imposed waiting time before a player can perform an action again.
 */
public sealed interface CooldownManager<T> permits InMemoryCooldownManager, RedisCooldownManager {

  /**
   * Puts a target under cooldown
   *
   * @param target   the target to be put under cooldown
   * @param duration cooldown time in seconds
   */
  void putUnderCooldown(@NonNull T target, @NonNull Long duration);

  /**
   * Recovers the remaining duration in seconds of the current cooldown if the target is under cooldown.
   * Otherwise, adds a cooldown to the target of the duration provided in seconds.
   *
   * @param target   the target under cooldown
   * @param duration the duration of the cooldown to be applied in seconds
   * @return the target's current cooldown if it exists, otherwise its new cooldown
   */
  default Long getOrPutUnderCooldown(@NonNull T target, @NonNull Long duration) {
    return get(target).onEmpty(() -> putUnderCooldown(target, duration))
      .fold(() -> duration, identity());
  }

  /**
   * Recovers the remaining duration in seconds of the current cooldown
   *
   * @param target the target under cooldown
   */
  Option<Long> get(@NonNull T target);

  /**
   * Retrieves the remaining duration in seconds of the current cooldown if the target is found, otherwise null.
   *
   * @param target the target under cooldown
   */
  default Long getOrNull(@NonNull T target) {
    return get(target).getOrNull();
  }

  /**
   * Retrieves the remaining duration in seconds of the current cooldown if the target is found, otherwise the value of `other`.
   *
   * @param target the target under cooldown
   */
  default Long getOrElse(@NonNull T target, @NonNull Long other) {
    return get(target).getOrElse(other);
  }

  /**
   * Returns true if the target is under cooldown, otherwise false
   *
   * @param target the target under cooldown
   */
  default boolean isUnderCooldown(@NonNull T target) {
    return get(target).fold(() -> false, found -> true);
  }

  /**
   * Returns true if the target is not under cooldown, otherwise false
   *
   * @param target the target under cooldown
   */
  default boolean isNotUnderCooldown(@NonNull T target) {
    return !isUnderCooldown(target);
  }

  /**
   * Removes the cooldown on the target
   *
   * @param target the target under cooldown
   */
  void removeCooldown(@NonNull T target);

  default Long secondsToMillis(Long seconds) {
    return TimeUnit.SECONDS.toMillis(seconds);
  }

  default Long millisToSeconds(Long millis) {
    return TimeUnit.MILLISECONDS.toSeconds(millis);
  }

  default Long computeNextAvailabilityInMs(Long durationInSeconds) {
    return System.currentTimeMillis() + secondsToMillis(durationInSeconds);
  }
}
