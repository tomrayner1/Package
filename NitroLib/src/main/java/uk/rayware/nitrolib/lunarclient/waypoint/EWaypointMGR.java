package uk.rayware.nitrolib.lunarclient.waypoint;

public enum EWaypointMGR {
	AMENDED(2),
	REMOVED(1),
	ADDED(0),
	ALREADY_EXISTS(-1),
	NOT_FOUND(-2);
	
	
	private int code;
	
	EWaypointMGR(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
}
