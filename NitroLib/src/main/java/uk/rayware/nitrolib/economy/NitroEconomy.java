package uk.rayware.nitrolib.economy;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class NitroEconomy implements Economy
{
	public HashMap<UUID, Double> playerBal = new HashMap<>();

	// internal -
	protected void update(UUID uuid, double bal)
	{
		if (playerBal.containsKey(uuid))
		{
			playerBal.replace(uuid, bal);
		}
		else
		{
			playerBal.put(uuid, bal);
		}

		EconomyHandler.getConfig().set(uuid + ".bal", bal);
	}

	protected double grabValue(UUID uuid)
	{
		if (playerBal.containsKey(uuid))
		{
			return playerBal.get(uuid);
		}
		return EconomyHandler.getConfig().getDouble(uuid + ".bal");
	}
	//

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getName() {
		return "NitroLib";
	}

	@Override
	public boolean hasBankSupport() {
		return false;
	}

	@Override
	public int fractionalDigits() {
		return -1;
	}

	@Override
	public String format(double v) {
		return "$" + v;
	}

	@Override
	public String currencyNamePlural() {
		return "";
	}

	@Override
	public String currencyNameSingular() {
		return "$";
	}

	@Override
	public boolean hasAccount(String s) {
		return true;
	}

	@Override
	public boolean hasAccount(OfflinePlayer offlinePlayer) {
		return true;
	}

	@Override
	public boolean hasAccount(String s, String s1) {
		return true;
	}

	@Override
	public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
		return true;
	}

	@Override
	public double getBalance(String s) {
		return getBalance(Bukkit.getOfflinePlayer(s));
	}

	@Override
	public double getBalance(OfflinePlayer offlinePlayer) {
		return grabValue(offlinePlayer.getUniqueId());
	}

	@Override
	public double getBalance(String s, String s1) {
		return getBalance(Bukkit.getOfflinePlayer(s));
	}

	@Override
	public double getBalance(OfflinePlayer offlinePlayer, String s) {
		return getBalance(offlinePlayer);
	}

	@Override
	public boolean has(String s, double v) {
		return has(Bukkit.getOfflinePlayer(s), v);
	}

	@Override
	public boolean has(OfflinePlayer offlinePlayer, double v) {
		return grabValue(offlinePlayer.getUniqueId()) >= v;
	}

	@Override
	public boolean has(String s, String s1, double v) {
		return has(Bukkit.getOfflinePlayer(s), v);
	}

	@Override
	public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
		return has(offlinePlayer, v);
	}

	@Override
	public EconomyResponse withdrawPlayer(String s, double v) {
		return withdrawPlayer(Bukkit.getOfflinePlayer(s), v);
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
		return null;
	}

	@Override
	public EconomyResponse withdrawPlayer(String s, String s1, double v) {
		return null;
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
		return null;
	}

	@Override
	public EconomyResponse depositPlayer(String s, double v) {
		return null;
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
		return null;
	}

	@Override
	public EconomyResponse depositPlayer(String s, String s1, double v) {
		return null;
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
		return null;
	}

	@Override
	public EconomyResponse createBank(String s, String s1) {
		return null;
	}

	@Override
	public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
		return null;
	}

	@Override
	public EconomyResponse deleteBank(String s) {
		return null;
	}

	@Override
	public EconomyResponse bankBalance(String s) {
		return null;
	}

	@Override
	public EconomyResponse bankHas(String s, double v) {
		return null;
	}

	@Override
	public EconomyResponse bankWithdraw(String s, double v) {
		return null;
	}

	@Override
	public EconomyResponse bankDeposit(String s, double v) {
		return null;
	}

	@Override
	public EconomyResponse isBankOwner(String s, String s1) {
		return null;
	}

	@Override
	public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
		return null;
	}

	@Override
	public EconomyResponse isBankMember(String s, String s1) {
		return null;
	}

	@Override
	public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
		return null;
	}

	@Override
	public List<String> getBanks() {
		return null;
	}

	@Override
	public boolean createPlayerAccount(String s) {
		return false;
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
		return false;
	}

	@Override
	public boolean createPlayerAccount(String s, String s1) {
		return false;
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
		return false;
	}
}
