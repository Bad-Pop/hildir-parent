package com.github.badpop.hildirapi.command;

import com.github.badpop.hildir.common.command.SimpleCommandExecutor;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

@RequiredArgsConstructor
public class HildirApiCommand implements SimpleCommandExecutor, TabCompleter {

  private static final List<String> CMD_FIRST_NODES = List.of("v", "version");

  private final HildirApiVersionSubCommand hildirApiVersionSubCommand;

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (args.length == 0 || (args.length == 1 && (args[0].equals("v") || args[0].equals("version")))) { //route to version sub command
      return hildirApiVersionSubCommand.onCommand(sender, command, label, args);
    }

    sender.sendMessage(text("Commande invalide...").color(RED));
    return false;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (args.length == 0) {
      return CMD_FIRST_NODES;
    } else if (args.length == 1) {
      return StringUtil.copyPartialMatches(args[0], CMD_FIRST_NODES, new ArrayList<>());
    }

    return null;
  }
}
