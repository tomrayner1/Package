package uk.rayware.nitroproxy.pyrite;

public class PyriteCredentials {

    private String address, password;
    private final int port;

    public PyriteCredentials(String address, String password, int port) {
        this.address = address;
        this.password = password;
        this.port = port;
    }

    /**
     * Check credential authentication.
     *
     * @return whether current credentials have authentication.
     */
    public boolean isAuth() {
        return this.password != null && !this.password.isEmpty();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

}
