package at.steell.mystuff.acceptance.driver;

public record ServerUrl(int serverPort, String contextPath) {
    public String buildApplicationUrl() {
        return baseUrl() + "/assets/list";
    }

    public String oauthLoginUrl() {
        return baseUrl() + "/oauth2/authorization/my-stuff";
    }

    public String baseUrl() {
        return contextPath != null
            ? String.format("http://localhost:%d%s", serverPort, contextPath)
            : String.format("http://localhost:%d", serverPort);
    }
}
