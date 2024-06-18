package uk.rayware.nitrolib.lunarclient.net.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ModSettings {
	public static final Gson GSON = new GsonBuilder().registerTypeHierarchyAdapter(ModSettings.class, new ModSettingsAdapter()).create();
	
	private final Map<String, ModSetting> modSettings = new HashMap<>();
	
	public ModSettings addModSettings(String modID, ModSetting setting) {
		modSettings.put(modID, setting);
		return this;
	}
	
	public ModSetting getModSetting(String modID) {
		return this.modSettings.get(modID);
	}
	
	public Map<String, ModSetting> getModSettings() {
		return this.modSettings;
	}
	
	public static class ModSetting {
		private boolean enabled;
		private Map<String, Object> properties;
		
		public ModSetting() {}
		
		public ModSetting(boolean enabled, Map<String, Object> properties) {
			this.enabled = enabled;
			this.properties = properties;
		}
		
		public boolean isEnabled() {
			return this.enabled;
		}
		
		public Map<String, Object> getProperties() {
			return this.properties;
		}
		
		@Override
		public String toString() {
			return "ModSetting{enabled=" + this.enabled + ", properties=" + this.properties + '}';
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			ModSetting that = (ModSetting) o;
			return this.enabled == that.enabled && this.properties.equals(that.properties);
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(enabled, properties);
		}
	}
}
