package com.github.badpop.hildirpeople;

import com.github.badpop.hildir.common.dependencyinjection.PluginContextManager;
import com.github.badpop.hildir.common.dependencyinjection.PluginContextManager.PluginContext;
import com.github.badpop.hildirapi.HildirApi;
import com.github.badpop.hildirpeople.config.HildirPeopleConfiguration;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import static com.github.badpop.hildir.common.colors.HildirColors.LOG_COLOR;
import static java.util.Collections.emptyList;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class HildirPeople extends JavaPlugin {

  private static PluginContext<HildirPeople> peopleContext;

  @Override
  public void onEnable() {
    getComponentLogger().info(text("""
            
      ╔─────────────────────────────────────────────────────────────────────────────────────────────────╗
      │                                                                                                 │
      │   ██╗  ██╗██╗██╗     ██████╗ ██╗██████╗     ██████╗ ███████╗ ██████╗ ██████╗ ██╗     ███████╗   │
      │   ██║  ██║██║██║     ██╔══██╗██║██╔══██╗    ██╔══██╗██╔════╝██╔═══██╗██╔══██╗██║     ██╔════╝   │
      │   ███████║██║██║     ██║  ██║██║██████╔╝    ██████╔╝█████╗  ██║   ██║██████╔╝██║     █████╗     │
      │   ██╔══██║██║██║     ██║  ██║██║██╔══██╗    ██╔═══╝ ██╔══╝  ██║   ██║██╔═══╝ ██║     ██╔══╝     │
      │   ██║  ██║██║███████╗██████╔╝██║██║  ██║    ██║     ███████╗╚██████╔╝██║     ███████╗███████╗   │
      │   ╚═╝  ╚═╝╚═╝╚══════╝╚═════╝ ╚═╝╚═╝  ╚═╝    ╚═╝     ╚══════╝ ╚═════╝ ╚═╝     ╚══════╝╚══════╝   │
      │                                                                                                 │
      ╚─────────────────────────────────────────────────────────────────────────────────────────────────╝
      Starting plugin...
      """).color(LOG_COLOR));

    //Config
    getConfig().options().copyDefaults();
    saveDefaultConfig();

    //Context
    buildPluginContext();

    //Commands
    registerCommands();

    //Started
    getComponentLogger().info(text("hildir-people is started !").color(LOG_COLOR));
  }

  private void buildPluginContext() {
    val hildirApi = (HildirApi) Bukkit.getServer().getPluginManager().getPlugin("hildir-api");
    if (hildirApi == null) {
      getComponentLogger().error(text("Unable to enable hildir-people... Is the hildir-api plugin installed ?").color(RED));
      getServer().getPluginManager().disablePlugin(this);
    }

    peopleContext = PluginContextManager.buildPluginContext(this, emptyList(), hildirApi.getContext(), HildirPeopleConfiguration.class);
  }

  private void registerCommands() {
    getCommand("test").setExecutor(peopleContext.getBean(TestCommand.class));
  }
}
