package uk.rayware.guard.check;

import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import uk.rayware.guard.Guard;
import uk.rayware.guard.event.CheckFailEvent;
import uk.rayware.guard.player.PlayerData;

@Getter
@RequiredArgsConstructor
public abstract class Check {

	protected final PlayerData data;

	private final String name;

	private final double punishVl;
	public double vl = 0;

	@Setter
	private boolean enabled = true, bans = true, experimental = false, shouldLog = true;

	public void fail() {
		fail(null);
	}
	public void fail(String info) {
		if (data.isBanWaiting()) {
			return;
		}

		if (vl >= punishVl && bans && !experimental) {
			data.setBanWaiting(true);

			/*if (Cobalt.getCobalt().getCobaltConfig().isAnnouncePunishment()) {
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
						Cobalt.getCobalt().getCobaltConfig().getAnnounceMessage().replace("%player%", data.getPlayer().getName())));
			}*/

			Bukkit.getScheduler().runTask(Guard.getGuard(), () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
					"kick " + data.getPlayer().getName() + " Cheating"));
		} else {
			Bukkit.getServer().getPluginManager().callEvent(new CheckFailEvent(this, getData(), info));
		}
	}

	public void handle(PacketPlayReceiveEvent event) { }
	public void handleOutgoing(PacketPlaySendEvent event) { }

}
