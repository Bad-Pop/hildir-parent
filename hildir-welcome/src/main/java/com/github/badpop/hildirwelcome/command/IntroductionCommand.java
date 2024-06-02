package com.github.badpop.hildirwelcome.command;

import com.github.badpop.hildir.common.command.SimpleCommandExecutor.PlayerCommandExecutor;
import com.github.badpop.hildirwelcome.WelcomeBookManager;
import lombok.Value;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Value(staticConstructor = "of")
public class IntroductionCommand implements PlayerCommandExecutor {

  private static final List<String> firstNode = List.of("introduction, intro");

  WelcomeBookManager welcomeBookManager;

  @Override
  public boolean onCommand(@NotNull Player sender, Command command, String... args) {
    welcomeBookManager.openBookForPlayer(sender);
    return true;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    return firstNode;
  }
}
