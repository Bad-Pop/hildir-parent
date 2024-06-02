package com.github.badpop.hildirpeople;

import com.github.badpop.hildir.common.database.HikariConnectionPool;
import lombok.Value;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@Value
public class TestCommand implements CommandExecutor {

  HikariConnectionPool hikariConnectionPool;

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    sender.sendMessage("OK...");
    return false;
  }
}
