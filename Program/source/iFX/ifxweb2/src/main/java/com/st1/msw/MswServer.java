package com.st1.msw;

import java.util.Date;
import javax.servlet.http.HttpSession;

import org.directwebremoting.Browser;
import org.directwebremoting.Container;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.event.ScriptSessionEvent;
import org.directwebremoting.event.ScriptSessionListener;
import org.directwebremoting.extend.ScriptSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.ifx.filter.FilterUtils;
import com.st1.servlet.GlobalValues;

public class MswServer {
	static final Logger logger = LoggerFactory.getLogger(MswServer.class);
	private final static MswServer _instance = new MswServer();
	private UserPub userPub;
	private final static String JS_OvrHandler = "jsOvrHandler";

	private MswServer() {

		logger.info("MswServer initializing....");
		userPub = UserPub.getInstance();
		initListeners();
	}

	private void initListeners() {
		Container container = ServerContextFactory.get().getContainer();
		ScriptSessionManager manager = container.getBean(ScriptSessionManager.class);
		ScriptSessionListener listener = new ScriptSessionListener() {
			public void sessionCreated(ScriptSessionEvent ev) {
				HttpSession session = WebContextFactory.get().getSession();
				String userId = (String) session.getAttribute(GlobalValues.SESSION_UID);
				logger.info("@#sv$for user:" + userId);
				logger.info("@#sv$for user2:" + (String) ev.getSession().getAttribute("userId"));
				ev.getSession().setAttribute("userId", userId);
				logger.info("sv-scriptSession created:" + ev.getSession().getId());
				logger.info("\tfor user:" + userId);

			}

			public void sessionDestroyed(ScriptSessionEvent ev) {
				logger.info("scriptSession destroyed:" + ev.getSession().getId());
				String userId = (String) ev.getSession().getAttribute("userId");
				logger.info("sv-try to remove " + userId + " scriptSession:" + ev.getSession().getId());
				UserInfo userInfo = userPub.getUser(userId);
				userInfo.removeScriptSessionId(ev.getSession().getId());
			}
		};
		manager.addScriptSessionListener(listener);

	}

	public static MswServer getInstance() {
		logger.info("MswServer.getInstance");
		return _instance;
	}

	public void join(String callbackMethod) {
		logger.info("MswServer.join() callbackMethod");
		ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
		HttpSession httpSession = WebContextFactory.get().getSession();
		String userId = (String) httpSession.getAttribute(GlobalValues.SESSION_UID);
		if (!isNullOrEmpty(userId)) {

			scriptSession.setAttribute("userId", userId);
			UserInfo newUser = (UserInfo) httpSession.getAttribute(GlobalValues.SESSION_USER_INFO);
			newUser.addScriptSessionId(scriptSession.getId());
			userPub.addUser(newUser);
			userPub.dump();
			replyWith(scriptSession.getId(), callbackMethod, newUser.toLine());
		} else {
			replyWith(scriptSession.getId(), callbackMethod, (Object) null);
		}
	}

	public void getUsers(String callbackMethod) {
		ScriptSession scriptSession = WebContextFactory.get().getScriptSession();

		Object[] users = userPub.getAllarray();
		replyWith(scriptSession.getId(), callbackMethod, users);
	}

	private boolean isNullOrEmpty(String s) {
		return s == null || s.length() == 0;
	}

	public void join() {
		logger.info("MswServer.join() ");
		HttpSession session = WebContextFactory.get().getSession();
		String userId = (String) session.getAttribute(GlobalValues.SESSION_UID);

		if (!isNullOrEmpty(userId)) {
			ScriptSession scriptSession = WebContextFactory.get().getScriptSession();

			scriptSession.setAttribute("userId", userId);
			String sessionId = scriptSession.getId();
			logger.info("join new member:" + userId);

			UserInfo newUser = (UserInfo) session.getAttribute(GlobalValues.SESSION_USER_INFO);
			newUser.addScriptSessionId(scriptSession.getId());
			userPub.addUser(newUser);
			userPub.dump();
			replyWith(sessionId, "signon", newUser.toLine());

			// refresh browser user list
			Object[] users = userPub.getAllarray();
			broadcast("members", users);

		}
	}

	public void bye() {
		HttpSession session = WebContextFactory.get().getSession();
		String userId = (String) session.getAttribute(GlobalValues.SESSION_UID);
		if (userId != null) {

		}
	}

	public void send(Passage passage) {
		ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
		HttpSession session = WebContextFactory.get().getSession();
		String userId = (String) session.getAttribute(GlobalValues.SESSION_UID);
		if (userId == null) {
			logger.info("HTTP session is reNew, abort scriptSession");
			scriptSession.invalidate();
			return;
		}
		if (passage.from == null)
			passage.from = userId;

		logger.info("receive passage at " + (new Date()));
		logger.info(passage.toString());

		String action = passage.action;
		if (action.equalsIgnoreCase("TALK")) {
			processTalk(passage);
		} else if (action.equalsIgnoreCase("getSupListReq")) {
			processGetSupList(passage, scriptSession.getId());
		} else if (action.equalsIgnoreCase("ovrReq")) {
			processOvrReq(passage, scriptSession.getId());
		} else if (action.equalsIgnoreCase("ack")) {
			processAck(passage, scriptSession.getId());
		} else {
			logger.info("unknown action");
			replyWith(scriptSession.getId(), "receiveMessages", "Unlnown action:" + passage.action);
		}
	}

	private void processAck(Passage passage, String scriptSessionId) {
		logger.info("enter process ACK, from " + scriptSessionId + " to " + passage.ackSessionId);
		logger.info("content:" + passage.content);
		String[] ss = passage.content.split("\\.");
		String jsMethod = ss[0];
		String jsAction = ss[1];
		passage.action = jsAction;
		replyWith(passage.ackSessionId, jsMethod, passage);
		logger.info("ack sent");
	}

	private void processOvrReq(Passage passage, String scriptSessionId) {

		UserInfo supervisor = userPub.getUser(passage.to);
		if (supervisor != null) {
			String supervisorSessionId = supervisor.getLastSession();
			logger.info(FilterUtils.escape("send to supervisor " + passage.to + " with session:" + supervisorSessionId));
			logger.info(FilterUtils.escape("ack session id:" + scriptSessionId));
			// send to this supervisor
			Passage passageToSupervisor = new Passage(supervisor.brno, passage.from, "ovrReq", "text", passage.from + " request ovr");
			passageToSupervisor.setAckSessionId(scriptSessionId);
			replyWith(supervisorSessionId, JS_OvrHandler, passageToSupervisor);
		}
		// TODO: add error handler
	}

	private void processGetSupList(Passage passage, String scriptSessionId) {
		logger.info("enter processGetSupList");
		String jsMethod = "getSupListResp";
		if (!isNullOrEmpty(passage.callback))
			jsMethod = passage.callback;

		HttpSession session = WebContextFactory.get().getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute(GlobalValues.SESSION_USER_INFO);
		// refresh browser user list
		Object[] users = userPub.getSupervisor(userInfo.getBrno(), null, null, null, null);
		replyWith(scriptSessionId, jsMethod, users);

	}

	private void processTalk(Passage passage) {
		String jsMethod = "receiveMessages";
		logger.info("enter processTalk");
		Passage reply = new Passage(passage.brno, passage.from, passage.action, "text", passage.content);
		if (passage.to.equalsIgnoreCase("aLL")) {
			broadcast(jsMethod, reply);
		} else {
			UserInfo user = userPub.getUser(passage.to);
			if (user != null) {
				replyWith(user.getLastSession(), jsMethod, reply);
			} else {
				System.err.println("can't find user:" + passage.to);
			}
		}
	}

	private void broadcast(final String methodName, final Object... p) {
		logger.info("broadcast");
		Browser.withCurrentPage(new Runnable() {
			public void run() {
				logger.info("broadcast method:" + methodName);
				ScriptSessions.addFunctionCall(methodName, p);
			}
		});
	}

	private void broadcastOthers(String userId, final String methodName, final Object... p) {
		Browser.withCurrentPageFiltered(new Myscriptsessionfilter(userId), new Runnable() {
			public void run() {
				ScriptSessions.addFunctionCall(methodName, p);
			}
		});
	}

	private void replyWith(String sessionId, final String methodName, final Object... p) {
		Browser.withSession(sessionId, new Runnable() {
			@Override
			public void run() {
				ScriptSessions.addFunctionCall(methodName, p);
			}
		});
	}

	private void replyError(String sessionId, final Object... p) {
		replyWith(sessionId, "showError", p);
	}

}

class Myscriptsessionfilter implements ScriptSessionFilter {
	String userId = null;

	public Myscriptsessionfilter(String userId) {
		this.userId = userId;
	}

	@Override
	public boolean match(ScriptSession session) {
		if (userId == null || userId.trim().length() == 0)
			return true;
		String _userId = (String) session.getAttribute("userId");
		if (!userId.equalsIgnoreCase(_userId))
			return false;
		else
			return true;

	}

}
