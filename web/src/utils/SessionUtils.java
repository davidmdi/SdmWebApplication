package utils;


import constants.Constants;
import logic.users.User;
import logic.users.UserManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    public static String getUsername (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.USERNAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static User getUser(HttpServletRequest request, ServletContext servletContext) {
        User user = null;
        UserManager currUserManager = (UserManager) servletContext.getAttribute(Constants.USER_MANAGER_ATTRIBUTE_NAME);
        HttpSession session = request.getSession(false);

        if(session != null){
            String username = (String)session.getAttribute(Constants.USERNAME);
            user = currUserManager.getUserByName(username);
        }

        return user;
    }

    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }
}