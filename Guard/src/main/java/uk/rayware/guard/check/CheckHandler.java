package uk.rayware.guard.check;

import lombok.experimental.UtilityClass;
import uk.rayware.guard.check.debug.DebugA;
import uk.rayware.guard.check.fly.FlyA;
import uk.rayware.guard.check.packets.PacketsA;
import uk.rayware.guard.check.packets.PacketsB;
import uk.rayware.guard.player.PlayerData;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@UtilityClass
public class CheckHandler {

	public final Class<?>[] CHECKS = new Class[] {
			// Bad Packets
			PacketsA.class,
			PacketsB.class,
			// Debug
			// DebugA.class,
			// Fly
			FlyA.class
	};

	private final List<Constructor<?>> CONSTRUCTORS = new ArrayList<>();

	static {
		for (Class<?> clazz : CHECKS) {
			try {
				CONSTRUCTORS.add(clazz.getConstructor(PlayerData.class));
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
	}

	public void loadChecks(PlayerData data) {
		Set<Check> checks = new HashSet<>();

		for (Constructor<?> constructor : CONSTRUCTORS) {
			try {
				checks.add((Check) constructor.newInstance(data));
			} catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		data.setChecks(checks);
	}

}
