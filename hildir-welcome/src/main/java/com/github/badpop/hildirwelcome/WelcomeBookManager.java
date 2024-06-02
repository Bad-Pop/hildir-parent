package com.github.badpop.hildirwelcome;

import lombok.Value;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Value(staticConstructor = "of")
public class WelcomeBookManager {

  ItemStack welcomeBook;

  public void openBookForPlayer(Player player) {
    player.openBook(welcomeBook);
  }
}
