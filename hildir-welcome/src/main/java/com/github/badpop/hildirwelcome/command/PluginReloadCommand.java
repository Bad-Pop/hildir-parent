package com.github.badpop.hildirwelcome.command;

import com.github.badpop.hildir.common.command.SimpleCommandExecutor.CommandWithPermExecutor;
import com.github.badpop.hildirwelcome.HildirWelcome;
import lombok.Value;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.github.badpop.hildirwelcome.command.Permissions.RELOAD;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

@Value(staticConstructor = "of")
public class PluginReloadCommand implements CommandWithPermExecutor {

  HildirWelcome plugin;

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String[] args) {
    sender.sendMessage(text("Reload de hildir-welcome en cours...").color(GREEN));
    plugin.reloadConfig();
    plugin.onEnable();
    sender.sendMessage(text("Reload de hildir-welcome terminé !").color(GREEN));
    return true;
  }

  @Override
  public String requiredPermission() {
    return RELOAD.getValue();
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    return null;
  }
}
