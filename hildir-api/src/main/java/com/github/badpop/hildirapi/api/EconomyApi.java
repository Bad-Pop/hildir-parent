package com.github.badpop.hildirapi.api;

import io.vavr.control.Option;
import org.bukkit.OfflinePlayer;

/**
 * Api for economy-based actions
 */
public interface EconomyApi {

  /**
   * Gets the player's gold coin balance
   * @param player the to obtain the gold coin balance
   */
  double getPlayerPOBalance(OfflinePlayer player);

  /**
   * Gets the player's stycas balance
   * @param player the to obtain the stycas balance
   */
  Option<Double> getPlayerStycasBalance(OfflinePlayer player);

  /**
   * Check if a player has enough gold pieces
   * @param player the player to check for the amount of gold pieces
   * @param amount the amount the player should have
   * @return true if the player has enough gold pieces, false otherwise.
   */
  boolean hasEnoughPO(OfflinePlayer player, double amount);

  /**
   * Check if a player has enough stycas
   * @param player the player to check for the amount of stycas
   * @param amount the amount the player should have
   * @return true if the player has enough stycas, false otherwise.
   */
  boolean hasEnoughStycas(OfflinePlayer player, double amount);

  /**
   * Add a given amount of gold coins to a player
   * @param player the player to add gold coins
   * @param amount the amount of gold coins to add
   * @return true if operation succeed, false otherwise
   */
  boolean givePOToPlayer(OfflinePlayer player, double amount);

  /**
   * Add a given amount of stycas to a player
   * @param player the player to add stycas
   * @param amount the amount of stycas to add
   * @return true if operation succeed, false otherwise
   */
  boolean giveStycasToPlayer(OfflinePlayer player, double amount);

  /**
   * Withdraw gold coins from a player balance
   * @param player the player to withdraw form his balance
   * @param amount the amount to withdraw
   * @return true if the operation succeed, false otherwise
   */
  boolean withdrawPlayerPO(OfflinePlayer player, double amount);

  /**
   * Withdraw stycas from a player balance
   * @param player the player to withdraw form his balance
   * @param amount the amount to withdraw
   * @return true if the operation succeed, false otherwise
   */
  boolean withdrawPlayerStycas(OfflinePlayer player, double amount);
}
