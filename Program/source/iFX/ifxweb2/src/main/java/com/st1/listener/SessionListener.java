package com.st1.listener;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.extend.ScriptSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.ifx.filter.FilterUtils;
import com.st1.msw.UserInfo;
import com.st1.msw.UserPub;
import com.st1.servlet.GlobalValues;

/**
 * Application Lifecycle Listener implementation class SessionListener
 * Serializable for 測試看看...
 */

@WebListener
public class SessionListener implements HttpSessionListener, HttpSessionAttributeListener {
	private static final Map<String, HttpSession> sessions = new HashMap<String, HttpSession>();
	static final Logger logger = LoggerFactory.getLogger(SessionListener.class);

	/**
	 * Default constructor.
	 */
	public SessionListener() {

	}

	/**
	 * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
	 */
	public void sessionCreated(HttpSessionEvent se) {
		logger.info("SessionListener sessionCreated!");
		HttpSession session = se.getSession();
		sessions.put(session.getId(), session);

		logger.info("# # # # :" + se.getSession().getId() + " is in");
		ServletContext ctx = se.getSession().getServletContext();
		Integer numSessions = (Integer) ctx.getAttribute("numSessions");
		if (numSessions == null) {
			numSessions = new Integer(1);
		} else {
			int count = numSessions.intValue();
			numSessions = new Integer(count + 1);
		}
		ctx.setAttribute("numSessions", numSessions);
		logger.info("active sessions:" + numSessions);
	}

	/**
	 * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
	 */
	public void sessionDestroyed(HttpSessionEvent se) {
		logger.info("SessionListener sessionDestroyed!");
		try {
			sessions.remove(se.getSession().getId());

			logger.info("# # # # :" + se.getSession().getId() + " is out");
			removeUser(se.getSession());
			logger.info("after sessionDestroyed-removeUser!");
			ServletContext ctx = se.getSession().getServletContext();
			Integer numSessions = (Integer) ctx.getAttribute("numSessions");
			logger.info("sessionDestroyed numSessions:" + numSessions);
			if (numSessions == null) {
				numSessions = new Integer(0);
			} else {
				int count = numSessions.intValue();
				numSessions = new Integer(count - 1);
			}

			ctx.setAttribute("numSessions", numSessions);
			logger.info("active sessions:" + numSessions);
		} catch (IllegalStateException ex) {
			logger.warn("SessionListener sessionDestroyed IllegalStateException!" + ex.getMessage());
		} catch (Exception ex) {
			logger.warn("SessionListener sessionDestroyed !" + ex.getMessage());
		}
	}

	public static String findUser(String userId) {
		logger.info("SessionListener findUser!");
		Iterator it = sessions.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			HttpSession session = (HttpSession) pairs.getValue();
			String oldUser = (String) session.getAttribute(GlobalValues.SESSION_UID);
			if (oldUser != null && oldUser.equals(userId)) {
				return session.getId();
			}
		}
		return null;
	}

	public static void killSession(String sessionId) {
		logger.info("SessionListener killSession!:" + FilterUtils.escape(sessionId));
		try {
			HttpSession session = sessions.get(sessionId);
			if (session != null) {
				// sessions.remove(sessionId);

				logger.info("killSession before invalidate.");
				session.invalidate();
				logger.info("killSession after invalidate.");
			} else {
				logger.info("can't kill session:" + FilterUtils.escape(sessionId));
			}
		} catch (IllegalStateException ex) {
			logger.warn("SessionListener killSession IllegalStateException!" + ex.getMessage());
		} catch (Exception ex) {
			logger.warn("SessionListener killSession !" + ex.getMessage());
		}
	}

	public static HttpSession find(String sessionId) {
		return sessions.get(sessionId);
	}

	public static Map<String, HttpSession> getSessions() {
		return sessions;
	}

	private void removeUser(HttpSession session) {
		logger.info("SessionListener removeUser!");
		String sessionId = session.getId();
		UserPub pub = UserPub.getInstance();
		UserInfo user = pub.getBySessionId(sessionId);
		logger.info("SessionListener removeUser-user!");
		if (user != null) {
			boolean removed = pub.removeUser(user);
			logger.info(FilterUtils.escape("user " + user.getId() + "'s session:" + sessionId + ",  removed:" + removed));
		} else {
			// 有可能MswCenter已經踢了
			logger.info("no user for session:" + sessionId);
		}
		pub.dump();
		// removeDwrScriptSession(session);
	}

	private ScriptSessionManager getScriptManager() {
		return ServerContextFactory.get().getContainer().getBean(ScriptSessionManager.class);

	}

	private void removeDwrScriptSession(HttpSession session) {
		ScriptSessionManager manager = getScriptManager();

		Collection<ScriptSession> col = manager.getScriptSessionsByHttpSessionId(session.getId());
		for (ScriptSession s : col) {
			s.invalidate();
		}
	}

	@Override
	public void attributeAdded(HttpSessionBindingEvent sbe) {
		HttpSession session = sbe.getSession();
		logger.info("# # # id:" + session.getId());
		logger.info("==>attribute added, " + sbe.getName() + "=" + sbe.getValue());

	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent sbe) {

	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent sbe) {

	}
}
