package fitpay.engtest.constants;

public final class AppConstants {
    public static final String USERS_URL = "/users/{0}";
    public static final String DEVICES_URL = USERS_URL + "/devices";
    public static final String CARDS_URL = USERS_URL + "/creditCards";

    private AppConstants() {
        throw new AssertionError();
    }
}
