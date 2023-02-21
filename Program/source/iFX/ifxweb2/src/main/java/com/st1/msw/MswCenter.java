package com.st1.msw;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.directwebremoting.Browser;
import org.directwebremoting.Container;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.event.ScriptSessionEvent;
import org.directwebremoting.event.ScriptSessionListener;
import org.directwebremoting.extend.ScriptSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.ifx.domain.UserPubd;
import com.st1.ifx.filter.FilterUtils;
import com.st1.servlet.GlobalValues;
import com.st1.util.Base64;
import com.st1.util.EncrypAES;
import com.st1.msw.OvrData;

public class MswCenter {
	static final Logger logger = LoggerFactory.getLogger(MswCenter.class);
	private final static MswCenter _instance = new MswCenter();
	private UserPub userPub;
	private final static String JS_OvrHandler = "jsOvrHandler";
	private final static int OK = 0;
	private final static int ERROR = -1;

	private MswCenter() {

		logger.info("MswCenter initializing....");
		userPub = UserPub.getInstance();
		initListners();
	}

	private void initListners() {
		Container container = ServerContextFactory.get().getContainer();
		ScriptSessionManager manager = container.getBean(ScriptSessionManager.class);

		ScriptSessionListener listener = new ScriptSessionListener() {

			public void sessionCreated(ScriptSessionEvent ev) {
				String userId = (String) ev.getSession().getAttribute("userId");
				logger.info("ScriptSession userId:" + userId + "," + (String) ev.getSession().getAttribute(GlobalValues.SESSION_UID));
				ev.getSession().setAttribute("userId", userId);
				logger.info("cen-scriptSession created:" + ev.getSession().getId());
				logger.info("\tfor user:" + userId);

			}

			public void sessionDestroyed(ScriptSessionEvent ev) {
				try {
					String sessionId = ev.getSession().getId();
					logger.info("scriptSession destroyed:" + sessionId);
					String userId = (String) ev.getSession().getAttribute("userId");
					logger.info("cen-try to remove " + userId + " scriptSession:" + sessionId);

					if (userId != null) {
						UserInfo userInfo = userPub.getUser(userId);
						if (userInfo != null) {
							// 移除原先加入的這幾行for 重新整理會被剔除的bug
							// 增加移除db的資料? 看起來應該可以踢除掉? start
							// SessionListener.killSession(userInfo.getHttpSessionId());
							// userPub.removeUser(userInfo);
							// end
//							userInfo.removeScriptSessionId(sessionId);
						} else { // 增加了這段有點與TranController重複,但功能不太一樣
							logger.info("MswCenter kill HTTPsessionId:!!");
//							UserPub.invalidateSession(WebContextFactory.get().getSession().getId());
						}
					}
				} catch (IllegalStateException ex) {
					logger.warn("MswCenter sessionDestroyed IllegalStateException!" + ex.getMessage());
				} catch (Exception ex) {
					logger.warn("MswCenter sessionDestroyed !" + ex.getMessage());
				}

			}

		};
		manager.addScriptSessionListener(listener);

	}

	public static MswCenter getInstance() {
		logger.info("MSwCenter.getInstance");
		return _instance;
	}

	private boolean isNullOrEmpty(String s) {
		return s == null || s.length() == 0;
	}

	public void join(String callbackMethod) {
		logger.info("MswCenter.join()");
		if (callbackMethod == null)
			callbackMethod = "__joinDone";
		ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
		HttpSession httpSession = WebContextFactory.get().getSession();
		String userId = (String) httpSession.getAttribute(GlobalValues.SESSION_UID);

		logger.info("join user:" + userId);
		if (!isNullOrEmpty(userId)) {
			scriptSession.setAttribute("userId", userId);
			UserInfo newUser = (UserInfo) httpSession.getAttribute(GlobalValues.SESSION_USER_INFO);
			newUser.addScriptSessionId(scriptSession.getId());
			userPub.addUser(newUser);
			userPub.dump();
			sendTo(scriptSession.getId(), callbackMethod, OK, newUser.toLine());
		} else {
			sendTo(scriptSession.getId(), callbackMethod, ERROR, "something wrong");
		}
	}

	public void talk(String to, String message) {
		logger.info("enter talk, to:" + FilterUtils.escape(to + ", " + message));

		ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
		String talker = (String) scriptSession.getAttribute("userId");
		message = talker + ": " + message;
		logger.info("talker:" + talker);
		boolean talkSuccess = false;
		if (to.equalsIgnoreCase("all")) {
			broadcast("__broadcast", message);
			talkSuccess = true;
		} else {
			UserInfo toUser = userPub.getUser(to);
			if (toUser != null) {
				sendTodd(toUser.getLastSession(), "__talkTo", OK, message);
				talkSuccess = true;
			}

		}
		// sendTo(scriptSession.getId(), callbackMethod, talkSuccess ? OK :
		// ERROR,
		// talkSuccess ? "" : "failed to talk to" + to);
	}

	public void systemTalk(String talker, String to, String message) {
		logger.info(FilterUtils.escape("enter system talk, to:" + to + ", " + message));

		message = talker + ": " + message;
		logger.info("talker:" + FilterUtils.escape(talker));
		boolean talkSuccess = false;
		if (to.equalsIgnoreCase("all")) {
			broadcast("__broadcast", message);
			talkSuccess = true;
		} else {
			UserInfo toUser = userPub.getUser(to);
			logger.info("systemTalk UserInfo toUser:" + FilterUtils.escape(toUser));
			if (toUser != null) {
				sendTodd(toUser.getLastSession(), "__talkTo", OK, message);
				talkSuccess = true;
			}

		}

	}

	public void getUsers(String callbackMethod) {
		logger.info("enter getUsers");
		if (callbackMethod == null)
			callbackMethod = "__getUsersDone";
		ScriptSession scriptSession = WebContextFactory.get().getScriptSession();

		Object[] users = userPub.getAllarray();
		sendTo(scriptSession.getId(), callbackMethod, OK, users);
	}

	public void getSupervisors(String callbackMethod, String txcd, Integer obufgch, int supLevel) {
		logger.info("enter getSupervisors");
		if (callbackMethod == null)
			callbackMethod = "__getSupervisorsDone";
		HttpSession session = WebContextFactory.get().getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute(GlobalValues.SESSION_USER_INFO);
		String userkey = userInfo.brno + userInfo.id;

		Object[] supervisors = userPub.getSupervisor(userInfo.getBrno(), txcd, obufgch, userkey, userInfo.getCldept(), supLevel); // 柯
		// 新增判斷
		logger.info("supervisors:" + supervisors.length);
		sendTo(WebContextFactory.get().getScriptSession().getId(), callbackMethod, OK, supervisors);
	}

	public void sendOvrReq(String supervisorId, final OvrData ovrData) {
		logger.info("enter sendOvrReq(), supervisor:" + FilterUtils.escape(supervisorId));
		logger.info("oldOvr:" + FilterUtils.escape(ovrData.toString()));
		/*
		 * 潘 WebContext wctx = WebContextFactory.get(); logger.info("WebContext wctx:" +
		 * wctx); ScriptSession scriptSession = wctx.getScriptSession();
		 */
		ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
		logger.info("ScriptSession scriptSession:" + scriptSession);
		String userId = (String) scriptSession.getAttribute("userId");
		logger.info("getAttribute userId:" + userId);
		UserInfo supervisor = userPub.getUser(supervisorId);
		UserInfo ovrUser = null;
		if (userId == null) {
			userId = ovrData.userId;// 暫時,null原因尋找中...潘
		}
		ovrUser = userPub.getUser(userId);
		logger.info(FilterUtils.escape("userPub supervisor:" + supervisor + ",ovrUser:" + ovrUser));

		if (supervisor != null) {
			ovrData.userId = userId;
			if (ovrUser != null) {
				ovrData.name = ovrUser.getName();
			} else {
				ovrData.name = "";
			}
			logger.info(FilterUtils.escape("ovrData.userId:" + ovrData.userId + ",ovrData.name:" + ovrData.name));
			logger.info(FilterUtils.escape("Ovr:" + ovrData.toString()));
			logger.info("SUPNM101 : " + supervisor.getName());
			// sendTo(supervisor.getLastSession(), "__appendSupOvrReq", OK,
			// userId, token, txcd, reasons);

			sendTodd(supervisor.getLastSession(), "__appendSupOvrReq", OK, ovrData);
			// Browser.withSession(supervisor.getLastSession(), new Runnable() {
			// @Override
			// public void run() {
			// ScriptSessions
			// .addFunctionCall("__appendSupOvrReq", ovrData);
			// }
			// });
			sendTo(scriptSession.getId(), "__sendOvrReqDone", OK, ovrData.getToken(), supervisorId, supervisor.getName());
		} else {
			sendTo(scriptSession.getId(), "__sendOvrReqDone", ERROR, "supervisor " + supervisorId + " is offline");
		}

	}

	public void cancelWaitOvr(String supervisorId, String token) {
		logger.info(FilterUtils.escape("enter cancelWaitOvr(), supervisor:" + supervisorId));
		ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
		String userId = (String) scriptSession.getAttribute("userId");

		UserInfo supervisor = userPub.getUser(supervisorId);
		if (supervisor != null) {
			sendTodd(supervisor.getLastSession(), "__cancelSupOvrReq", OK, userId, token);
		}
		sendTo(scriptSession.getId(), "__cancelWaitOvr", OK, "cancel sent");
	}

	public void changeOvrStep(String userId, String token, String stepName, String message) {
		logger.info("enter changeOvrStep(), userId:" + FilterUtils.escape(userId));
		UserInfo user = userPub.getUser(userId);
		if (user != null) {
			logger.info("do changeOvrStep!" + FilterUtils.escape(userId) + "," + FilterUtils.escape(user.getLastSession()));
			sendTodd(user.getLastSession(), "__changeOvrStep", OK, token, stepName, message);
		}
	}

	public void ovrNotify(String to, String message) {
		logger.info(FilterUtils.escape("ovrNotify, to:" + to + ", message:" + message));
		ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
		String userId = (String) scriptSession.getAttribute("userId");
		String method = "__ovrProgress";
		UserInfo toUser = userPub.getUser(to);
		if (toUser != null) {
			logger.info(FilterUtils.escape("do ovrNotify!" + userId + "," + toUser.getLastSession()));
			sendTodd(toUser.getLastSession(), method, OK, userId, message);
		}
	}

	public void ovrDone(String action, String userId, String token, String message, String supnm) {
		ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
		String supervisorId = (String) scriptSession.getAttribute("userId");

		logger.info("enter ovrDone, action:" + FilterUtils.escape(action) + ", userId:" + FilterUtils.escape(userId) + ", token:" + FilterUtils.escape(token) + ", message:"
				+ FilterUtils.escape(message) + ",supervisorId:" + FilterUtils.escape(supervisorId));

		UserInfo toUser = userPub.getUser(userId);
		if (toUser != null) {
			logger.info(FilterUtils.escape("do ovrDone!" + userId + "," + toUser.getLastSession()));
			sendTodd(toUser.getLastSession(), "__ovrDone", OK, action.toLowerCase(), token, message, supervisorId, supnm);
		}
	}

	public void beforLogoffUser(UserInfo user) {
		logger.info(FilterUtils.escape("session timeout, log off user, userId:" + user.getBrno() + user.getId()));
		sendTodd(user.getLastSession(), "__beforLogoff", OK, "see you later");
	}

	public void sendTo(String sessionId, final String methodName, final int statusCode, final Object... p) {
		logger.info(FilterUtils.escape("sendTo " + sessionId + " with method:" + methodName + ", statusCode:" + statusCode));
		Browser.withSession(sessionId, new Runnable() {
			@Override
			public void run() {
				ScriptSessions.addFunctionCall(methodName, statusCode, p);
			}
		});
	}

	public void sendTodd(String sessionId, final String methodName, final int statusCode, final Object... p) {
		logger.info(FilterUtils.escape("sendTodd " + sessionId + " with method:" + methodName + ", statusCode:" + statusCode));
		// String sessionIdsrh = sessionId.replaceAll("/","//");
		logger.info("before getdbByscriptSessionId userdb : " + FilterUtils.escape(sessionId));
		UserPubd userdb = userPub.getdbByscriptSessionId(sessionId);
		logger.info("after getdbByscriptSessionId userdb : " + FilterUtils.escape(sessionId));
		logger.info(FilterUtils.escape("userdb getLocate: " + userdb.getLocate()));

		// 通知測試ok
		// String url0 =
		// "http://192.168.10.6:9080/h/mvc/msw/systemTalk/sys/105804/test";

		if (userdb != null && !userdb.getLocate().equals(GlobalValues.getLocalAddr())) {
			logger.info("userdb getLocate: " + userdb.getLocate());
			logger.info(FilterUtils.escape("GlobalValues getLocalAddr: " + GlobalValues.getLocalAddr()));
			logger.warn("sendToOther!!!!");
			String urlother = userdb.getLocate() + "/mvc/msw/sendTo";
			logger.info("sendTodd:" + urlother);
			ArrayList<String> postParameters;
			postParameters = new ArrayList<String>();
			Map<String, String> map = new HashMap<String, String>();
			postParameters.add(sessionId);
			postParameters.add(methodName);
			postParameters.add(Integer.toString(statusCode));
			map.put("sessionId", sessionId);
			map.put("methodName", methodName);
			map.put("statusCode", Integer.toString(statusCode));
			// MswController 固定4個接收
			for (int i = 0; i < 4; i++) {
				if (p.length > i) {
					try {
						postParameters.add(Base64.encodeObject((Serializable) p[i]));
						map.put("p" + i, Base64.encodeObject((Serializable) p[i]));
					} catch (IOException e) {
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						logger.error(errors.toString());
					}
				} else {
					postParameters.add(null);
					map.put("p" + i, null);
				}
			}
			// logger.warn("strp:" + strp);
			// String result2 = HttpClientDemo.sendPostarr(urlother, postParameters);

			String result2 = HttpClientDemo.sendPost(urlother, map);
			logger.info(FilterUtils.escape(result2));

		} else {
			logger.warn("DO sendTo! " + FilterUtils.escape(sessionId) + "," + FilterUtils.escape(methodName));
			Browser.withSession(sessionId, new Runnable() {
				@Override
				public void run() {
					ScriptSessions.addFunctionCall(methodName, statusCode, p);
				}
			});
		}
	}

	private void broadcast(final String methodName, final Object... p) {
		logger.info("broadcast, method:" + FilterUtils.escape(methodName));
		Browser.withCurrentPage(new Runnable() {
			public void run() {
				logger.info("broadcast method:" + methodName);
				ScriptSessions.addFunctionCall(methodName, p);
			}
		});
	}

	/**
	 * 檢查此SESSION密碼是否與參數pswdString相同
	 * 
	 * @param pswdString
	 * @return
	 */
	public String checkSameGuys(String pswdString) {
		ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
		String currid = (String) scriptSession.getAttribute("userId");
		String dePswd = "";
		logger.info("pswdString:" + FilterUtils.escape(pswdString));
		logger.info("currid:" + currid);

		UserInfo toUser = userPub.getUser(currid);
		if (toUser != null) {
			dePswd = EncrypAES.userDecryptor(toUser.pswd);
			logger.info("checkSameGuys pswd :" + FilterUtils.escape(toUser.pswd));
			logger.info("checkSameGuys pswd :" + FilterUtils.escape(dePswd));

			if (dePswd == pswdString || dePswd.equals(pswdString)) {
				logger.info("checkSameGuys is same!");
				return toUser.getName();
			} else {
				logger.info("checkSameGuys not same!");
				return "";
			}
		}
		// 不可能會走這
		return "";
	}

}
