package com.github.badpop.hildir.common.dependencyinjection;

import lombok.NonNull;
import lombok.val;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static com.github.badpop.hildir.common.colors.HildirColors.LOG_COLOR;
import static net.kyori.adventure.text.Component.text;

/**
 * Utility interface used to build a plugin's context in order to take advantage of dependency injection.
 */
public interface PluginContextManager {

  /**
   * Constructs an instance of a PluginContext which will allow dependencies to be injected.
   *
   * @param plugin           the main class of the plugin (the one that extends JavaPlugin).
   * @param customBeans      an iterable containing all the customs beans to be added to the context
   * @param componentClasses Register one or more component classes to be processed.
   * @param <T>              the main class of the plugin (the one that extends JavaPlugin).
   * @return a new JavaPlugin instance
   */
  static <T extends JavaPlugin> PluginContext<T> buildPluginContext(@NotNull @NonNull T plugin,
                                                                    @Nullable Iterable<CustomBean<?>> customBeans,
                                                                    @NotNull @NonNull Class<?>... componentClasses) {
    return new PluginContext<>(plugin, customBeans, componentClasses);
  }

  /**
   * Constructs an instance of a PluginContext which will allow dependencies to be injected.
   *
   * @param plugin           the main class of the plugin (the one that extends JavaPlugin).
   * @param customBeans      an iterable containing all the customs beans to be added to the context
   * @param componentClasses Register one or more component classes to be processed.
   * @param <T>              the main class of the plugin (the one that extends JavaPlugin).
   * @return a new JavaPlugin instance
   */
  static <T extends JavaPlugin, P extends JavaPlugin, C extends PluginContext<P>> PluginContext<T> buildPluginContext(@NotNull @NonNull T plugin,
                                                                                                                      @Nullable Iterable<CustomBean<?>> customBeans,
                                                                                                                      @NotNull C parentContext,
                                                                                                                      @NotNull @NonNull Class<?>... componentClasses) {
    return new PluginContext<>(plugin, customBeans, parentContext, componentClasses);
  }

  class PluginContext<T extends JavaPlugin> extends AnnotationConfigApplicationContext {

    private PluginContext(T plugin, Iterable<CustomBean<?>> customBeans, Class<?>... componentClasses) {
      super();

      plugin.getComponentLogger().info(text("Building %s context...".formatted(plugin.toString())).color(LOG_COLOR));

      setDisplayName(plugin + " plugin context");

      val beanFactory = getBeanFactory();
      beanFactory.registerSingleton("plugin", plugin);
      beanFactory.registerSingleton("logger", plugin.getComponentLogger());

      if (customBeans != null) {
        customBeans.forEach(customBean -> beanFactory.registerSingleton(customBean.beanName(), customBean.beanInstance()));
      }

      register(componentClasses);
      refresh();

      plugin.getComponentLogger().info(text("%s context successfully built !".formatted(plugin.toString())).color(LOG_COLOR));
    }

    private <P extends JavaPlugin, C extends PluginContext<P>> PluginContext(T plugin, Iterable<CustomBean<?>> customBeans, C parentContext, Class<?>... componentClasses) {
      super();
      plugin.getComponentLogger().info(text("Building %s context...".formatted(plugin.toString())).color(LOG_COLOR));

      setDisplayName(plugin + " plugin context");
      setId(plugin.getName() + "_plugin_context");
      setParent(parentContext);

      val beanFactory = getBeanFactory();
      beanFactory.registerSingleton("plugin", plugin);
      beanFactory.registerSingleton("logger", plugin.getComponentLogger());

      if (customBeans != null) {
        customBeans.forEach(customBean -> beanFactory.registerSingleton(customBean.beanName(), customBean.beanInstance()));
      }

      register(componentClasses);
      refresh();

      plugin.getComponentLogger().info(text("%s context successfully built !".formatted(plugin.toString())).color(LOG_COLOR));
    }
  }

  record CustomBean<T>(String beanName, T beanInstance) {
  }
}
