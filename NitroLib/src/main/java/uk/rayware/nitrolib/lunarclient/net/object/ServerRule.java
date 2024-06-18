package uk.rayware.nitrolib.lunarclient.net.object;

import lombok.Getter;

public enum ServerRule {
	MINIMAP_STATUS("minimapStatus", String.class),
	SERVER_HANDLES_WAYPOINTS("serverHandlesWaypoints", Boolean.class),
	COMPETITIVE_GAME("competitiveGame", Boolean.class),
	SHADERS_DISABLED("shadersDisabled", Boolean.class),
	LEGACY_ENCHANTING("legacyEnchanting", Boolean.class),
	VOICE_ENABLED("voiceEnabled", Boolean.class),
	LEGACY_COMBAT("legacyCombat", Boolean.class);
	
	@Getter
	private final String id;
	@Getter
	private final Class type;
	
	ServerRule(String id, Class type) {
		this.id = id;
		this.type = type;
	}
	
	public static ServerRule getRule(String id) {
		for (ServerRule existing : ServerRule.values()) {
			if (existing.id.equals(id)) {
				return existing;
			}
		}
		
		return null;
	}
}
