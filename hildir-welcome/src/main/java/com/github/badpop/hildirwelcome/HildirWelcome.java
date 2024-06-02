package com.github.badpop.hildirwelcome;

import com.github.badpop.hildirwelcome.command.IntroductionCommand;
import com.github.badpop.hildirwelcome.command.PluginReloadCommand;
import com.github.badpop.hildirwelcome.event.PlayerFirstJoinListener;
import io.vavr.collection.List;
import lombok.val;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import static com.github.badpop.hildir.common.colors.HildirColors.LOG_COLOR;
import static com.github.badpop.hildirwelcome.WelcomeBookConstants.*;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;
import static org.bukkit.Bukkit.getPluginManager;

public class HildirWelcome extends JavaPlugin implements Listener {

  private final FileConfiguration mainConfig = getConfig();
  private WelcomeBookManager welcomeBookManager;

  @Override
  public void onEnable() {
    //Config
    getConfig().options().copyDefaults();
    saveDefaultConfig();

    //Build the welcome book and build an instance of the manager
    buildWelcomeBookManager();

    //Register event
    getPluginManager().registerEvents(new PlayerFirstJoinListener(welcomeBookManager), this);

    //Register command
    getCommand("introduction").setExecutor(IntroductionCommand.of(welcomeBookManager));
    getCommand("hwreload").setExecutor(PluginReloadCommand.of(this));

    getComponentLogger().info(Component.text("Successfully initialized hildir-welcome").color(LOG_COLOR));
  }

  private void buildWelcomeBookManager() {
    this.welcomeBookManager = WelcomeBookManager.of(buildWelcomeBook());
  }

  private ItemStack buildWelcomeBook() {
    val book = new ItemStack(Material.WRITTEN_BOOK);
    val meta = (BookMeta) book.getItemMeta();

    meta.setTitle(WELCOME_BOOK_TITLE);
    meta.setAuthor(WELCOME_BOOK_AUTHOR);

    val pages = List.ofAll(getConfig().getStringList("welcome-book.pages"))
      .map(page -> miniMessage().deserialize(page))
      .toJavaArray(Component[]::new);
    meta.addPages(pages);

    val pdc = meta.getPersistentDataContainer();
    pdc.set(WELCOME_BOOK_NAMESPACED_KEY(this), PersistentDataType.STRING, "None");

    book.setItemMeta(meta);
    return book;
  }
}
