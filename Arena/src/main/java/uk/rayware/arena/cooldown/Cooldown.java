package uk.rayware.arena.cooldown;

import lombok.Getter;

@Getter
public class Cooldown {

	public long start;
	public long duration;

	public Cooldown(long duration) {
		this.start = System.currentTimeMillis();
		this.duration = duration;
	}

	public void reset() {
		start = System.currentTimeMillis();
	}

	public boolean active() {
		return (start + duration) >= System.currentTimeMillis();
	}

	public long remaining() {
		return (start + duration) - System.currentTimeMillis();
	}

	public void end() {
		start = 0;
	}

}
