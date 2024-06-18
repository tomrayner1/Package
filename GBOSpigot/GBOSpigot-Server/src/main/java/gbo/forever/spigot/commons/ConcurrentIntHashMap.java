package gbo.forever.spigot.commons;

import gbo.forever.spigot.async.AsyncUtil;
import net.minecraft.server.IntHashMap;

import static gbo.forever.spigot.async.AsyncUtil.runSynchronized;

public class ConcurrentIntHashMap<V> extends IntHashMap<V> {
	
	public V get(int var1) {
		return AsyncUtil.runSynchronized(this, () -> super.get(var1));
	}

	public boolean b(int var1) {
		return AsyncUtil.runSynchronized(this, () -> super.b(var1));
	}

	public void a(int var1, V var2) {
		AsyncUtil.runSynchronized(this, () -> super.a(var1, var2));
	}

	public V d(int var1) {
		return AsyncUtil.runSynchronized(this, () -> super.d(var1));
	}

	public void c() {
		AsyncUtil.runSynchronized(this, super::c);
	}
}
