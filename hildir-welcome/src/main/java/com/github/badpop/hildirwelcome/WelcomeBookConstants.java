package com.github.badpop.hildirwelcome;

import org.bukkit.NamespacedKey;

import static net.kyori.adventure.text.format.NamedTextColor.GOLD;

public interface WelcomeBookConstants {

  static NamespacedKey WELCOME_BOOK_NAMESPACED_KEY(HildirWelcome plugin) {
    return new NamespacedKey(plugin, WELCOME_BOOK_PDC_ID);
  }
  String WELCOME_BOOK_PDC_ID = "hildir_welcome_book";

  String WELCOME_BOOK_TITLE = GOLD + "Bienvenue sur Hildir";
  String WELCOME_BOOK_AUTHOR = GOLD + "Hildir";
}
