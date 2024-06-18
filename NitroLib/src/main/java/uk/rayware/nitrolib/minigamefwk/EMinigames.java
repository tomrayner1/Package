package uk.rayware.nitrolib.minigamefwk;

public class EMinigames {
	enum MinigameStatus {
		LOBBY,
		IN_GAME,
		ENDING
	}
	
	enum PlayerStatus {
		LOBBY,
		PLAYING,
		SPECTATING // or dead
	}
	
	enum World {
		LOBBY,
		MAP
	}
}
