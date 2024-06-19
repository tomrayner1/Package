package uk.rayware.guard.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Ticks {

	public int since(long time) {
		double diff = System.currentTimeMillis() - time;

		return (int) Math.floor(diff / 50.0);
	}

}
