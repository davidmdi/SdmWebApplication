package utils;


import constants.Constants;
import logic.Logic.Engine;
import logic.Logic.My_CLASS.MyOrder;
import logic.Logic.My_CLASS.MyStoreSingleOrderItems;
import logic.Logic.My_CLASS.User;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class SessionUtils {

    public static String getUsername (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.USERNAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static User getUser(HttpServletRequest request, ServletContext servletContext) {
        User user = null;
        Engine engine = (Engine) servletContext.getAttribute(Constants.ENGINE_ATTRIBUTE_NAME);
        HttpSession session = request.getSession(false);

        if(session != null){
            String username = (String)session.getAttribute(Constants.USERNAME);
            user = engine.getMyUsers().getUserByName(username);
        }

        return user;
    }

    public static String getAreaName (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.SELECTED_ZONE) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static MyOrder getOrder(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        MyOrder sessionAttribute = session != null ? (MyOrder)session.getAttribute(Constants.CUSTOMER_ORDER) : null;
        return sessionAttribute != null ? sessionAttribute : null;
    }
    public static Map<Integer, MyStoreSingleOrderItems> getStoreSingleOrderItemsMap(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Map<Integer, MyStoreSingleOrderItems> sessionAttribute = session != null ?
            (Map<Integer, MyStoreSingleOrderItems>)session.getAttribute(Constants.CUSTOMER_ORDER) : null;
        return sessionAttribute != null ? sessionAttribute : null;
    }
    /*
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
     */

    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }
}