package uk.rayware.nitrolib.backup;

public enum Codes {
	AUTHENTICATED("Authenticated"),
	NOT_AUTHENTICATED("Not Authenticated"),
	NO_CONNECTION("No Connection"),
	ERROR("Error"),
	SUCCESS("Success");
	
	private String code;
	
	Codes(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
}
