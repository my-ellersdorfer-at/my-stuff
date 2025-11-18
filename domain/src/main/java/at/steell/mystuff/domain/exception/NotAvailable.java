package at.steell.mystuff.domain.exception;

public class NotAvailable extends RuntimeException {
    private final String userName;

    public NotAvailable(final String theUserName) {
        super(String.format("No assets are available for user '%s'.", theUserName));
        userName = theUserName;
    }

    public String getUserName() {
        return userName;
    }
}
