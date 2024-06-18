package uk.rayware.hub.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import uk.rayware.nitrogen.NitrogenAPI;
import uk.rayware.nitrogen.profile.Profile;
import uk.rayware.nitrolib.NitroLib;
import uk.rayware.nitrolib.menu.Menu;
import uk.rayware.nitrolib.menu.components.ButtonComponent;
import uk.rayware.nitrolib.menu.components.ItemComponent;
import uk.rayware.nitrolib.util.ItemBuilder;

import java.util.Arrays;
import java.util.Date;

public class ProfileMenu extends Menu {

	public ProfileMenu(Player pl) {
		super(ChatColor.GOLD + "" + ChatColor.BOLD + "Your Profile", 6);

		Profile profile = NitrogenAPI.getProfile(pl.getUniqueId());

		if (profile == null) {
			pl.sendMessage(ChatColor.RED + "Please wait while we load your profile.");
			return;
		}

		Date firstSeen = new Date(profile.getFirstSeen());
		Date freeGoldTimer = new Date(profile.getFreeGoldCooldown());

		addMenuComponents(
				new ItemComponent(this, 10, new ItemBuilder(Material.SKULL_ITEM).skull(pl.getName())
						.name(ChatColor.GOLD + "" + ChatColor.BOLD + "Your Profile")
						.lore(Arrays.asList(
								"",
								ChatColor.GOLD + NitroLib.bar + ChatColor.WHITE + " Your rank: " + profile.getRankDisplayName(),
								ChatColor.GOLD + NitroLib.bar + ChatColor.WHITE + " Expires at: " + ChatColor.YELLOW +
										(profile.getRankExpiry(profile.getRank()) == null ? "Never" : NitroLib.longDateTimeFormatter.format(profile.getRankExpiry(profile.getRank()))),
								"",
								ChatColor.GOLD + NitroLib.bar + ChatColor.WHITE + " First joined: " + ChatColor.YELLOW + NitroLib.longDateTimeFormatter.format(firstSeen.toInstant())
						)).build()),

				new ButtonComponent(this, 11, new ItemBuilder(Material.ENDER_CHEST).name(ChatColor.GOLD + "" + ChatColor.BOLD + "Your Cosmetics")
						.lore(Arrays.asList(
								"",
								ChatColor.GOLD + NitroLib.bar + ChatColor.WHITE + " Custom colours",
								ChatColor.GOLD + NitroLib.bar + ChatColor.WHITE + " Tags",
								"",
								ChatColor.YELLOW + "Left click to equip cosmetics."
						)).build(), (player, menu) -> {
					NitroLib.getInstance().getMenuHandler().openMenuForPlayer(player, new CosmeticsMenu());
					return true;
				}),

				new ButtonComponent(this, 12, new ItemBuilder(Material.WATCH).name(ChatColor.GOLD + "" + ChatColor.BOLD + "Open Settings")
						.lore(Arrays.asList(
								ChatColor.GRAY + "Disable annoying features and customise your experience.",
								"",
								ChatColor.YELLOW + "Left click to run /settings."
						)).build(), (player, menu) -> {
					player.closeInventory();
					player.chat("/settings");
					return true;
				}),

				new ButtonComponent(this, 14, new ItemBuilder(Material.DIAMOND).name(ChatColor.GOLD + "" + ChatColor.BOLD + "Your Friends")
						.lore(Arrays.asList(
								"",
								ChatColor.GOLD + NitroLib.bar + ChatColor.WHITE + " Online friends: " + ChatColor.YELLOW + profile.getOnlineFriends().size(),
								ChatColor.GOLD + NitroLib.bar + ChatColor.WHITE + " Total friends: " + ChatColor.YELLOW + profile.getFriends().size(),
								"",
								ChatColor.YELLOW + "Left click to manage your friends list."
						)).build(), (player, menu) -> {
					player.closeInventory();
					player.sendMessage(ChatColor.RED + "Coming soon.");
					return true;
				}),

				new ButtonComponent(this, 15, new ItemBuilder(Material.EMERALD).name(ChatColor.GOLD + "" + ChatColor.BOLD + "Your Group")
						.lore(Arrays.asList(
								"",
								ChatColor.GOLD + NitroLib.bar + ChatColor.WHITE + " Your group: " + ChatColor.RED + "None",
								ChatColor.GOLD + NitroLib.bar + ChatColor.WHITE + " Your rank: " + ChatColor.RED + "None",
								"",
								ChatColor.GOLD + NitroLib.bar + ChatColor.WHITE + " Online members: " + ChatColor.YELLOW + 0,
								ChatColor.GOLD + NitroLib.bar + ChatColor.WHITE + " Total members: " + ChatColor.YELLOW + 0,
								"",
								ChatColor.YELLOW + "Left click to view your group."
						)).build(), (player, menu) -> {
					player.closeInventory();
					player.sendMessage(ChatColor.RED + "Coming soon.");
					return true;
				}),

				new ButtonComponent(this, 16, new ItemBuilder(Material.ITEM_FRAME).name(ChatColor.GOLD + "" + ChatColor.BOLD + "Ignored Players")
						.lore(Arrays.asList(
								"",
								ChatColor.GOLD + NitroLib.bar + ChatColor.WHITE + " Total players ignored: " + ChatColor.GOLD + 0,
								"",
								ChatColor.YELLOW + "Left click manage your ignored players."
						)).build(), (player, menu) -> {
					player.closeInventory();
					player.sendMessage(ChatColor.RED + "Coming soon.");
					return true;
				}),

				new ButtonComponent(this, 31, new ItemBuilder(Material.DOUBLE_PLANT).name(ChatColor.GOLD + "" + ChatColor.BOLD + "Gold Shop")
						.lore(Arrays.asList(
								"",
								ChatColor.GOLD + NitroLib.bar + ChatColor.WHITE + " Your gold: " + ChatColor.YELLOW + profile.getGold() + ChatColor.GOLD + "⛁",
								"",
								ChatColor.YELLOW + "Left click to open the gold shop."
						)).build(), (player, menu) -> {
					player.closeInventory();
					player.chat("/goldshop");
					return true;
				}),

				new ButtonComponent(this, 38, new ItemBuilder(Material.STORAGE_MINECART).name(ChatColor.GOLD + "" + ChatColor.BOLD + "Vote for us")
						.lore(Arrays.asList(
								ChatColor.GRAY + "Earn rewards for voting for our server.",
								"",
								ChatColor.YELLOW + "Left click to run /vote."
						)).build(), (player, menu) -> {
					player.closeInventory();
					player.chat("/vote");
					return true;
				}),

				new ButtonComponent(this, 42, new ItemBuilder(Material.STORAGE_MINECART).name(ChatColor.GOLD + "" + ChatColor.BOLD + "Claim your free rank")
						.lore(Arrays.asList(
								ChatColor.GRAY + "Earn a free rank for following a few steps.",
								"",
								ChatColor.YELLOW + "Left click to run /claimrank."
						)).build(), (player, menu) -> {
					player.closeInventory();
					player.chat("/claimrank");
					return true;
				})
		);

		if (profile.getFreeGoldCooldown() > System.currentTimeMillis()) {
			addMenuComponents(new ButtonComponent(this, 40, new ItemBuilder(Material.REDSTONE_BLOCK).name(ChatColor.RED + "" + ChatColor.BOLD + "You have already claimed free gold today").lore(Arrays.asList(
					"",
					ChatColor.GRAY + "You can next claim free gold at",
					ChatColor.RED + NitroLib.longDateTimeFormatter.format(freeGoldTimer.toInstant())
			)).build(), ((player, menu) -> {
				player.playSound(pl.getLocation(), Sound.NOTE_BASS, 1.0f, 0.0f);

				return true;
			})));
		} else {
			addMenuComponents(new ButtonComponent(this, 40, new ItemBuilder(Material.GOLD_BLOCK).name(ChatColor.GOLD + "" + ChatColor.BOLD + "Claim your free gold")
					.lore(Arrays.asList(
							ChatColor.GRAY + "Earn gold each day for logging in.",
							"",
							ChatColor.YELLOW + "Left click to earn 10 gold."
					)).build(), (player, menu) -> {
				if (profile.getFreeGoldCooldown() > System.currentTimeMillis()) {
					player.sendMessage(ChatColor.RED + "You are on cooldown for this.");
				} else {
					profile.setGold(profile.getGold() + 10);
					profile.setFreeGoldCooldown(System.currentTimeMillis() + (86400 * 1000));
					NitrogenAPI.saveProfile(profile);
					player.playSound(pl.getLocation(), Sound.LEVEL_UP, 1f, 1f);
					player.sendMessage(ChatColor.GREEN + "You now have " + ChatColor.YELLOW + profile.getGold() + ChatColor.GOLD + "⛁" + ChatColor.GREEN + " gold.");
				}

				player.closeInventory();
				NitroLib.getInstance().getMenuHandler().openMenuForPlayer(player, new ProfileMenu(pl));
				return true;
			}));
		}

		fill(null);
	}

}
