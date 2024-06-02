package com.github.badpop.hildir.common.command;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public interface SimpleCommandExecutor extends CommandExecutor {

  interface CommandWithRequiredPermission {
    String requiredPermission();
  }

  interface CommandWithPermExecutor extends CommandWithRequiredPermission, SimpleCommandExecutor, TabCompleter {

    @Override
    default boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
      if(sender.hasPermission(requiredPermission()) || sender instanceof ConsoleCommandSender) {
        return onCommand(sender, command, args);
      }
      sender.sendMessage(text("Tu n'as pas la permission pour faire ça...").color(RED));
      return false;
    }

    boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String[] args);
  }

  interface PlayerCommandExecutor extends SimpleCommandExecutor, TabCompleter {
    @Override
    default boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
      if (sender instanceof Player p) {
        return onCommand(p, command, args);
      }

      sender.sendMessage(text("Cette commande doit être utilisée par un joueur.").color(RED));
      return false;
    }

    boolean onCommand(@NotNull Player sender, Command command, String... args);
  }

  interface PlayerWithPermCommandExecutor extends CommandWithRequiredPermission, PlayerCommandExecutor, TabCompleter {

    @Override
    default boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
      if(sender.hasPermission(requiredPermission())) {
        return PlayerCommandExecutor.super.onCommand(sender, command, label, args);
      } else {
        sender.sendMessage(text("Tu n'as pas la permission pour faire ça...").color(RED));
        return false;
      }
    }
  }
}
