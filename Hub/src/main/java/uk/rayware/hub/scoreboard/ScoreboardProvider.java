package uk.rayware.hub.scoreboard;

import org.bukkit.entity.Player;
import uk.rayware.gboqueue.QueuePlugin;
import uk.rayware.gboqueue.player.QueuePlayer;
import uk.rayware.hub.HubPlugin;
import uk.rayware.nitrogen.NitrogenAPI;
import uk.rayware.nitrogen.profile.Profile;
import uk.rayware.nitrolib.NitroLib;
import uk.rayware.nitrolib.board.BoardAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ScoreboardProvider implements BoardAdapter {

	@Override
	public String getTitle(Player player) {
		return "&4&lHUB";
	}

	@Override
	public List<String> getScores(Player player) {
		List<String> scores = new ArrayList<>();

		Profile profile = NitrogenAPI.getProfile(player.getUniqueId());

		scores.add("&7&m--------------------");
		scores.add("&4" + player.getName());

		if (profile == null) {
			return scores;
		}

		try {
			scores.add("&f  Rank: &r" + profile.getRankDisplayName());
		} catch (Exception ignored) {
			scores.add("&f  Rank: &cError");
		}
		//scores.add("&6" + NitroLib.bar + "&f Gold: &e" + profile.getGold() + "&r&6⛁");

		QueuePlayer queuePlayer = QueuePlugin.playerHandler.getQueuePlayers().get(player.getUniqueId());

		if (queuePlayer != null && !queuePlayer.getQueues().isEmpty()) {
			scores.add("");
			scores.add("&4Queue");

			var queues = queuePlayer.getQueues();
			AtomicInteger size = new AtomicInteger();

			queues.forEach((id, pos) -> {
				size.getAndIncrement();

				if (size.get() <= 2) {
					scores.add("&f  " + NitrogenAPI.getStoredServerData(id).get("display") + ":&c #" + ++pos);
				}
			});

			if (size.get() > 2) {
				scores.add("&f  And &c" + (size.get() - 2) + "&f more...");
			}
		}

		scores.add("");
		scores.add("&4Servers");
		scores.add("&f  Global: &c" + HubPlugin.getInstance().getGlobalCount());

		scores.add("");
		scores.add(HubPlugin.getInstance().getBoardDisplay());
		scores.add("&7&m--------------------");

		return scores;
	}
}
