package com.github.badpop.hildir.common.event;

import lombok.NonNull;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * Utility class that eliminates the need to write boilerplate code to manage handlers for custom cancellable events.
 */
public abstract class SimpleCancellableEvent extends SimpleEvent implements Cancellable {

  private boolean cancelled;

  public SimpleCancellableEvent() {
    this.cancelled = false;
  }

  /**
   * @param cancelled the initial cancellation status of the event
   */
  public SimpleCancellableEvent(boolean cancelled) {
    this.cancelled = cancelled;
  }

  /**
   * Same as {@link Event#callEvent()} but with a better naming.
   */
  public void sendEvent() {
    this.callEvent();
  }

  /**
   * Define if this event is cancelled or not
   * @param cancel true if you wish to cancel this event
   */
  @Override
  public void setCancelled(boolean cancel) {
    this.cancelled = cancel;
  }

  /**
   * Cancel this event.
   */
  public void cancel() {
    this.cancelled = true;
  }

  /**
   * Utility method used to execute an action if the event is not cancelled.
   * @param action the action to execute if this event is not cancelled
   */
  public void doIfNotCancelled(@NotNull @NonNull Runnable action) {
    if (!cancelled) {
      action.run();
    }
  }

  /**
   * Utility method used to execute an action if the event is not cancelled and get the action result. This method will return {@code null} if the event is cancelled.
   * @param action the action to execute if this event is not cancelled
   */
  public <T> T doIfNotCancelled(@NotNull @NonNull Supplier<T> action) {
    if (!cancelled) {
      return action.get();
    }
    return null;
  }

  /**
   * Utility method used to execute an action if the event is cancelled.
   * @param action the action to execute if this event is cancelled
   */
  public void doIfCancelled(@NotNull @NonNull Runnable action) {
    if (cancelled) {
      action.run();
    }
  }

  /**
   * Utility method used to execute an action if the event is cancelled and get the action result. This method will return {@code null} if the event is not cancelled.
   * @param action the action to execute if this event is cancelled
   */
  public <T> T doIfCancelled(@NotNull @NonNull Supplier<T> action) {
    if (cancelled) {
      return action.get();
    }
    return null;
  }

  /**
   * Utility method used to send the event and then execute an action if the event is not cancelled.
   * @param action the action to execute if this event is not cancelled
   */
  public void sendAndDoIfNotCancelled(@NotNull @NonNull Runnable action) {
    this.sendEvent();
    doIfNotCancelled(action);
  }

  /**
   * Utility method used to send the event and then execute an action if the event is not cancelled. This method will return {@code null} if the event is cancelled.
   * @param action the action to execute if this event is not cancelled
   */
  public <T> T sendAndDoIfNotCancelled(@NotNull @NonNull Supplier<T> action) {
    this.sendEvent();
    return doIfNotCancelled(action);
  }

  /**
   * Utility method used to send the event and then execute an action if the event is cancelled.
   * @param action the action to execute if this event is not cancelled
   */
  public void sendAndDoIfCancelled(@NotNull @NonNull Runnable action) {
    this.sendEvent();
    doIfCancelled(action);
  }

  /**
   * Utility method used to send the event and then execute an action if the event is cancelled. This method will return {@code null} if the event is not cancelled.
   * @param action the action to execute if this event is not cancelled
   */
  public <T> T sendAndDoIfCancelled(@NotNull @NonNull Supplier<T> action) {
    this.sendEvent();
    return doIfCancelled(action);
  }
}
