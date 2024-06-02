package com.github.badpop.hildirapi.config;

public class PluginApiHookException extends Exception {

  public PluginApiHookException(String pluginName) {
    super("Unable to hook with %s's plugin api.".formatted(pluginName));
  }
}
