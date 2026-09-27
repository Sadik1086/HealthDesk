package com.healthdesk.service;

import com.healthdesk.dao.UserDAO;
import com.healthdesk.model.User;
import com.healthdesk.util.SessionManager;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();

    public boolean login(String username, String password) {
        if (!userDAO.authenticate(username, password)) return false;
        User user = userDAO.findByUsername(username);
        if (user == null) return false;
        SessionManager.login(user);
        return true;
    }
}
