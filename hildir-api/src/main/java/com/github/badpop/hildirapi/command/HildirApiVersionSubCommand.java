package com.github.badpop.hildirapi.command;

import com.github.badpop.hildir.common.command.SimpleCommandExecutor;
import com.github.badpop.hildirapi.HildirApi;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static com.github.badpop.hildir.common.colors.HildirColors.GOLD;
import static net.kyori.adventure.text.Component.text;

@RequiredArgsConstructor
public class HildirApiVersionSubCommand implements SimpleCommandExecutor {

  private final HildirApi api;

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    sender.sendMessage(text("Version actuelle de l'api : : %s".formatted(api.toString())).color(GOLD));
    return true;
  }
}
