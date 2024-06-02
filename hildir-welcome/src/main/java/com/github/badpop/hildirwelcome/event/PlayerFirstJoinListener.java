package com.github.badpop.hildirwelcome.event;

import com.github.badpop.hildirwelcome.WelcomeBookManager;
import lombok.Value;
import lombok.val;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@Value
public class PlayerFirstJoinListener implements Listener {

  WelcomeBookManager welcomeBookManager;

  @EventHandler
  void onPLayerJoin(PlayerJoinEvent e) {
    val player = e.getPlayer();
    if (!player.hasPlayedBefore()) {
      welcomeBookManager.openBookForPlayer(player);
    }
  }
}
