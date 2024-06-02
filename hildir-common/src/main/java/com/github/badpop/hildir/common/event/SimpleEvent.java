package com.github.badpop.hildir.common.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class that eliminates the need to write boilerplate code to manage handlers for custom events
 */
public abstract class SimpleEvent extends Event {

  private static final HandlerList HANDLERS = new HandlerList();

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLERS;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }
}
