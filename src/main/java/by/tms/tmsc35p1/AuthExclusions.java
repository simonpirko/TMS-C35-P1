package by.tms.tmsc35p1;

import java.util.Set;

public class AuthExclusions {
    private static final Set<String> authExclusions = Set.of(
            "/", // home
            "/login", // login
            "/signup" // signup
    );

    public static boolean isAuthExclusion(String path) {
        return authExclusions.contains(path);
    }
}
