package com.github.badpop.hildirapi.api;

import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import me.TechsCode.UltraEconomy.UltraEconomyAPI;
import me.TechsCode.UltraEconomy.objects.Balance;
import me.TechsCode.UltraEconomy.objects.Currency;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;

import static java.util.function.Function.identity;

public class EconomyManager implements EconomyApi {

  private static final String STYCAS_CURRENCY_NAME = "stycas";
  private final UltraEconomyAPI ueconApi;
  private final Economy vaultEconomy;
  private Currency stycasCurrency;

  public EconomyManager(UltraEconomyAPI ueconApi, Economy vaultEconomy) throws RuntimeException {
    this.ueconApi = ueconApi;
    this.vaultEconomy = vaultEconomy;
    getCurrencies().get(STYCAS_CURRENCY_NAME)
      .peek(currency -> stycasCurrency = currency)
      .getOrElseThrow(() -> new RuntimeException("Unable to to retrieve stycas currency from UltraEconomyApi..."));
  }

  @Override
  public double getPlayerPOBalance(OfflinePlayer player) {
    return vaultEconomy.getBalance(player);
  }

  //TODO REFACTO ? :  WALAH C'EST PAS OPTI LEUR TRUC OU QUOI ?
  @Override
  public Option<Double> getPlayerStycasBalance(OfflinePlayer player) {
    return Option.ofOptional(ueconApi.getAccounts().uuid(player.getUniqueId()))
      .map(account -> account.getBalance(stycasCurrency))
      .map(Balance::getOnHand);
  }

  @Override
  public boolean hasEnoughPO(OfflinePlayer player, double amount) {
    return vaultEconomy.has(player, amount);
  }

  @Override
  public boolean hasEnoughStycas(OfflinePlayer player, double amount) {
    return Option.ofOptional(ueconApi.getAccounts().uuid(player.getUniqueId()))
      .map(account -> account.getBalance(stycasCurrency).getOnHand())
      .map(currentStycas -> currentStycas - amount >= 0.0)
      .getOrElse(false);
  }

  @Override
  public boolean givePOToPlayer(OfflinePlayer player, double amount) {
    return vaultEconomy.depositPlayer(player, amount).transactionSuccess();
  }

  @Override
  public boolean giveStycasToPlayer(OfflinePlayer player, double amount) {
    return Option.ofOptional(ueconApi.getAccounts().uuid(player.getUniqueId()))
      .map(account -> {
        if (player.isOnline()) {
          return account.addBalance(stycasCurrency, amount);
        } else {
          account.addBalanceWhileOffline(stycasCurrency, amount);
          return true;
        }
      }).getOrElse(false);
  }

  @Override
  public boolean withdrawPlayerPO(OfflinePlayer player, double amount) {
    return vaultEconomy.withdrawPlayer(player, amount).transactionSuccess();
  }

  @Override
  public boolean withdrawPlayerStycas(OfflinePlayer player, double amount) {
    return Option.ofOptional(ueconApi.getAccounts().uuid(player.getUniqueId()))
      .map(account -> account.removeBalance(stycasCurrency, amount))
      .getOrElse(false);
  }

  public Map<String, Currency> getCurrencies() {
    return List.ofAll(ueconApi.getCurrencies()).toMap(Currency::getName, identity());
  }
}
