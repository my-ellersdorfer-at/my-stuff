package at.steell.mystuff.application.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.RequestMatcher;

final class PathMatchingRequestMatcher implements RequestMatcher {
    private final String path;

    PathMatchingRequestMatcher(final String matchingPath) {
        path = matchingPath;
    }

    @Override
    public boolean matches(final HttpServletRequest request) {
        return request.getServletPath().contains(path);
    }
}
