package utils;


import constants.Constants;
import logic.Logic.Engine;
import logic.users.UserManager;

import javax.servlet.ServletContext;

public class ServletUtils {

	//private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
	private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";// need change to my logic

	/*
	Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
	the actual fetch of them is remained un-synchronized for performance POV
	 */
	private static final Object userManagerLock = new Object();
	private static final Object engineLock = new Object();
	private static final Object chatManagerLock = new Object();


	public static UserManager getUserManager(ServletContext servletContext) {

		synchronized (userManagerLock) {
			if (servletContext.getAttribute(Constants.USER_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(Constants.USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
			}
		}
		return (UserManager) servletContext.getAttribute(Constants.USER_MANAGER_ATTRIBUTE_NAME);
	}

	public static Engine getEngine(ServletContext servletContext) {

		synchronized (engineLock) {
			if (servletContext.getAttribute(Constants.ENGINE_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(Constants.ENGINE_ATTRIBUTE_NAME, new Engine());
			}
		}
		return (Engine) servletContext.getAttribute(Constants.ENGINE_ATTRIBUTE_NAME);
	}



}
