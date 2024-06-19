package uk.rayware.guard.util;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

@UtilityClass
public class TeleportMessage {

	public void sendTPMessage(Player player, Player target, String message) {
		TextComponent text = new TextComponent(TextComponent.fromLegacyText(message));

		text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(ChatColor.YELLOW
				+ "Click to teleport to " + ChatColor.LIGHT_PURPLE + target.getName() + ChatColor.YELLOW + ".")}));
		text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + target.getName()));

		player.spigot().sendMessage(text);
	}

}