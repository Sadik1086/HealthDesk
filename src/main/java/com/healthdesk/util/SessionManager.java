package com.healthdesk.util;

import com.healthdesk.model.User;

public final class SessionManager {
    private static User currentUser;

    private SessionManager() {}

    public static void login(User user)  { currentUser = user; }
    public static void logout()          { currentUser = null; }
    public static User getCurrentUser()  { return currentUser; }
    public static boolean isLoggedIn()   { return currentUser != null; }

    public static boolean hasRole(String... roles) {
        if (currentUser == null) return false;
        for (String r : roles) {
            if (r.equalsIgnoreCase(currentUser.getRole())) return true;
        }
        return false;
    }
}
