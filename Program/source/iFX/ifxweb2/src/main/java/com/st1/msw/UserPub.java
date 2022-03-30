package com.st1.msw;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import com.st1.ifx.domain.UserPubd;
import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.service.UserPubService;
import com.st1.listener.SessionListener;
import com.st1.servlet.GlobalValues;
import com.st1.util.Base64;
import com.st1.util.MySpring;

//containsByKey 與 getbykey 是否要合併? 只要讀db1次
//原先只做最小幅度修改(整塊users搬到DB),但如果真的效能不好的話,可能要把讀取全部後慢慢篩選的部分改成開成DB2欄位..
public class UserPub {
	static final Logger logger = LoggerFactory.getLogger(UserPub.class);
	private static final UserPub _instance = new UserPub();

	private ConcurrentHashMap<String, UserInfo> users;
	static UserPubService userPubService = MySpring.getUserPubService();

	private UserPub() {
		users = new ConcurrentHashMap<String, UserInfo>();
	}

	public static UserPub getInstance() {
		return _instance;
	}

	public static String checkUser(String userId) {
		logger.info(FilterUtils.escape("checkUser:" + userId));

		if (_instance.containsByKey(userId)) {
			logger.info("checkUser containsByKey!");
			return _instance.getByKey(userId).getHttpSessionId();
		}
		return null;

		/**
		 * 原本直接return 這行.更改為上方撈db2資料確認是否存在 logger.info("checkUser F:"+
		 * SessionListener.findUser(userId)); return SessionListener.findUser(userId);
		 */
	}

	public static void invalidateSession(String sessionId) {
		logger.info("before invalidateSession :" + FilterUtils.escape(sessionId));
		SessionListener.killSession(sessionId);
		logger.info("after invalidateSession :" + FilterUtils.escape(sessionId));
	}

	public boolean addUser(UserInfo newUser) {
		logger.info("addUser!!!");
		logger.info("in UserPub.addUser");
		String key = newUser.getFullId();
		if (containsByKey(key)) {
			UserInfo u = getByKey(key);
			if (isSameUser(u, newUser)) {
				logger.info("user " + newUser.id + " already exists.delete than insert!");
				// updataByKey(key, newUser);
				userPubService.removeKey("USER", key);
				putByKey(key, newUser);
				logger.info("after updataByKey!!."); // 純粹更新 strUserinfo
				return false;
			} else {
				removeByKey(key);
				logger.info(FilterUtils.escape("kill session, user:" + key));
				invalidateSession(u.httpSessionId); // 殺掉jsp session
				logger.info(FilterUtils.escape("user:" + key + "," + u.httpSessionId + " killed?!"));
			}
		}
		putByKey(key, newUser);

//		logger.info("added new user:" + newUser.toString());
		logger.info("added new user:" + newUser.getFullId());

		return true;
	}

	public UserInfo getUser(String fullId) {
		logger.info("getUser!!!");
		String brno = fullId.substring(0, 4);
		String tlrno = fullId.substring(4);
		logger.info(FilterUtils.escape("getUser, userId:" + fullId + ", brno:" + brno + ", tlrno:" + tlrno));

		if (containsByKey(fullId)) {
			return getByKey(fullId);
		} else
			return null;

	}

	private boolean isSameUser(UserInfo a, UserInfo b) {
		logger.info("isSameUser!!!");
		return (a.id.equals(b.id) && a.httpSessionId.equals(b.httpSessionId));
	}

	public boolean removeUser(UserInfo userToRemoved) {
		logger.info("removeUser!!!");
		if (containsByKey(userToRemoved.getFullId())) {
			logger.info(FilterUtils.escape("in removeUser :" + userToRemoved.getFullId()));
			UserInfo u = getByKey(userToRemoved.getFullId());
			// multithread下, user可能因timeout而再登入 但我們無法確定先後順序(timeout與再登入順序)
			// 所以 removeUser時 必須確認httpSessionId, 否則會刪到重新登入之user
			logger.info(FilterUtils.escape("u  httpSessionId :" + u.httpSessionId));
			logger.info(FilterUtils.escape("Ru httpSessionId :" + userToRemoved.httpSessionId));
			logger.info(FilterUtils.escape("equals1?" + u.httpSessionId.equals(userToRemoved.httpSessionId)));
			logger.info(FilterUtils.escape("equals2?" + u.httpSessionId.toString().equals(userToRemoved.httpSessionId.toString())));
			if (u.httpSessionId.equals(userToRemoved.httpSessionId)) {
				logger.info(FilterUtils.escape("user " + userToRemoved.getId() + "'s session:" + userToRemoved + ",  removeUser."));
				removeByKey(userToRemoved.getFullId());
			}
			logger.info("removeUser true");
			return true;
		}
		logger.info("removeUser false");
		return false;

	}

	// for forceout used!
	public static void removeUserbyKey(String key) {
		userPubService.removeKeyDo("USER", key);
	}

	private static final UserInfo[] EMPTY_USERS = new UserInfo[0];

	public boolean exists(UserInfo thatUser) {
		logger.info("exists!!!");
		return containsByKey(thatUser.getFullId());
	}

	public UserInfo getBySessionId(String sessionId) {

		logger.info("getBySessionId!!!");
		UserPubd rtnUserpub = userPubService.findhttpSessionId("USER", sessionId);
		if (rtnUserpub == null) {
			logger.info("getBySessionId null.");
			return null;
		}
		UserInfo utmp = null;
		try {
			utmp = (UserInfo) Base64.decodeToObject(rtnUserpub.getUserInfo());
		} catch (ClassNotFoundException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}

		return utmp;
	}

	//
	// public UserInfo getBySessionId(String sessionId) {
	// //logger.info("getBySessionId!!!");
	// //logger.info("sessionId:"+sessionId);
	// // 因為getBySessionId() 只有在session expiration時才會被呼叫
	// // 所以可以慢慢地 iterate每一個user,
	// Collection<UserInfo> usersDbtmp = getDbUsers();
	// if (usersDbtmp == null) {
	// return null;
	// }
	// for (UserInfo u : usersDbtmp) {
	// //logger.info("in getBySessionId u.");
	// //logger.info("getBySessionId u:"+u.httpSessionId);
	// if (u.httpSessionId.equals(sessionId)) {
	// logger.info("u.getFullId():"+u.getFullId()+","+sessionId +" is same! time:");
	// return u;
	// }
	// }
	// logger.info("getBySessionId null:" + sessionId );
	// return null;
	// }
	public Object[] getSupervisor(String brn, String txcd, Integer obufgch, String userkey, String cldept) {
		logger.info("getSupervisor!!!");
		List<String> supervisors = new ArrayList<String>();

		logger.info("getBySessionId!!! brn:" + brn);
		List<UserPubd> rtnUserpubs = userPubService.findSupervisor("USER", brn, cldept);
		int number;
		char word;
		int newnumber = 0;
		String updkey = "";
		if (txcd != null) {
			word = txcd.charAt(1); // 取得輸txcd的第二碼
			switch (word) {
			case '0':
				word = 'A';
				break;
			case '1':
				word = 'B';
				break;
			case '2':
				word = 'C';
				break;
			case '3':
				word = 'D';
				break;
			case '4':
				word = 'E';
				break;
			case '5':
				word = 'F';
				break;
			case '6':
				word = 'G';
				break;
			case '7':
				word = 'H';
				break;
			case '8':
				word = 'I';
				break;
			case '9':
				word = 'J';
				break;
			}
			number = word; // 將取得的字元轉換成Unicode碼
			newnumber = (number - 65); // 不+ 1 從0開始
		}

		for (UserPubd upd : rtnUserpubs) {
			if (upd == null) {
				continue;
			}
			updkey = upd.getXey();
			// 相同?
			if (userkey != null && updkey.equals(userkey)) {
				continue;
			}

			if (txcd != null) {
				if (obufgch != null && obufgch == 2 && upd.getOapKnd().charAt(newnumber) == '1') {
					logger.info(FilterUtils.escape("分行" + upd.getBrno() + upd.getName() + "授權有此交易" + txcd + "執行權限  OBUFG:" + obufgch));
					supervisors.add(upd.toRtnstring());
				} else if (obufgch != null && obufgch == 1 && upd.getDapKnd().charAt(newnumber) == '1') {
					logger.info(FilterUtils.escape("分行" + upd.getBrno() + upd.getName() + "授權有此交易" + txcd + "執行權限  OBUFG:" + obufgch));
					supervisors.add(upd.toRtnstring());
				} else if (obufgch != null && obufgch == 0) {
					logger.info(FilterUtils.escape("分行" + upd.getBrno() + upd.getName() + "授權有此交易" + txcd + "執行權限  OBUFG:" + obufgch));
					supervisors.add(upd.toRtnstring());
				} else {
					logger.info(FilterUtils.escape("分行" + upd.getBrno() + upd.getName() + "授權沒有此交易" + txcd + "執行權限"));
				}
			} else {
				logger.info(FilterUtils.escape("分行" + upd.getBrno() + upd.getName() + "忽略交易代號與odBU權限"));
				supervisors.add(upd.toRtnstring());
			}
		}
		return supervisors.toArray();

	}
	//
	// public UserInfo[] getSupervisor(String brn, String txcd, Integer obufgch) {
	// logger.info("getSupervisor!!!");
	// List<UserInfo> supervisors = new ArrayList<UserInfo>();
	// Collection<UserInfo> usersDbtmp = getDbUsers();
	// if (usersDbtmp == null) {
	// return supervisors.toArray(EMPTY_USERS);
	// }
	// int number;
	// char word;
	// int newnumber = 0;
	// if (txcd != null) {
	// word = txcd.charAt(1); // 取得輸txcd的第二碼
	// number = word; // 將取得的字元轉換成Unicode碼
	// newnumber = (number - 65); // 不+ 1 從0開始
	// }
	//
	// for (UserInfo u : usersDbtmp) {
	// if (u.brno.equals(brn) && (u.level.equals("1") || u.level.equals("2"))) {
	// if (txcd != null) {
	// if (obufgch != null && obufgch == 2
	// && u.oapKnd.charAt(newnumber) == '1') {
	// logger.info(FilterUtils.escape("分行" + u.brno + u.name + "授權有此交易" + txcd
	// + "執行權限 OBUFG:" + obufgch));
	// supervisors.add(u);
	// } else if (obufgch != null && obufgch == 1
	// && u.dapKnd.charAt(newnumber) == '1') {
	// logger.info(FilterUtils.escape("分行" + u.brno + u.name + "授權有此交易" + txcd
	// + "執行權限 OBUFG:" + obufgch));
	// supervisors.add(u);
	// } else if (obufgch != null && obufgch == 0) {
	// logger.info(FilterUtils.escape("分行" + u.brno + u.name + "授權有此交易" + txcd
	// + "執行權限 OBUFG:" + obufgch));
	// supervisors.add(u);
	// } else {
	// logger.info(FilterUtils.escape("分行" + u.brno + u.name + "授權沒有此交易" + txcd +
	// "執行權限"));
	// }
	// } else {
	// logger.info(FilterUtils.escape("分行" + u.brno + u.name + "忽略交易代號與odBU權限"));
	// supervisors.add(u);
	// }
	// }
	// }
	// return supervisors.toArray(EMPTY_USERS);
	//
	// }

	public UserInfo[] getBranchUsers(String brn) {
		logger.info("getBranchUsers!!!");
		List<UserPubd> rtnUserpubs = userPubService.findBranchUsers("USER", brn);
		List<UserInfo> brnUsers = new ArrayList<UserInfo>();

		for (UserPubd upd : rtnUserpubs) {
			UserInfo u = null;
			try {
				u = (UserInfo) Base64.decodeToObject(upd.getUserInfo());
			} catch (ClassNotFoundException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
			} catch (IOException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
			}
			if (u == null) {
				continue;
			} else {
				brnUsers.add(u);
			}
		}
		return brnUsers.toArray(EMPTY_USERS);
	}

	// public UserInfo[] getBranchUsers(String brn) {
	// logger.info("getBranchUsers!!!");
	// List<UserInfo> brnUsers = new ArrayList<UserInfo>();
	// Collection<UserInfo> usersDbtmp = getDbUsers();
	// if (usersDbtmp == null) {
	// return brnUsers.toArray(EMPTY_USERS);
	// }
	// for (UserInfo u : usersDbtmp) {
	// if (u.brno.equals(brn)) {
	// brnUsers.add(u);
	// }
	// }
	// return brnUsers.toArray(EMPTY_USERS);
	//
	// }

	public void dump() {
		logger.info("dump!!!");
		List<UserPubd> rtnUserpubs = userPubService.findAllUsers("USER");
		if (rtnUserpubs != null) {
			for (UserPubd u : rtnUserpubs) {
				logger.info(FilterUtils.escape(u.toString()));
			}
		}
	}

	//
	// public void dump() {
	// logger.info("dump!!!");
	// Collection<UserInfo> usersDbtmp = getDbUsers();
	// if (usersDbtmp != null) {
	// for (UserInfo u : usersDbtmp) {
	// logger.info(u.toString());
	// }
	// }
	// }
	public Object[] getAllarray() {
		logger.info("getAllarray!");
		List<UserPubd> rtnUserpubs = userPubService.findAllUsers("USER");
		List<String> lines = new ArrayList<String>();
		if (rtnUserpubs == null) {
			logger.info("getAllarray null.");
			return null;
		}
		for (UserPubd u : rtnUserpubs) {
			lines.add(u.toRtnstring());
		}
		return lines.toArray();
	}

	public UserInfo[] getAll() {
		logger.info("getAll!");
		Collection<UserInfo> usersDbtmp = getDbUsers();
		if (usersDbtmp == null) {
			return null;
		}

		return usersDbtmp.toArray(EMPTY_USERS);
	}

	// 重新抓取db內容
	// users.values()
	public Collection<UserInfo> getDbUsers() {
		// logger.info("getDbUsers!");
		StopWatch watch = new StopWatch();
		watch.start();
		List<UserPubd> usersdb = userPubService.findAll("USER");
		logger.info("\nTotal execution time of userPubService.findAll: " + watch.getTotalTimeMillis());
		// logger.info("usersdb.size:" + usersdb.size());
		ConcurrentHashMap<String, UserInfo> dbusersMap = new ConcurrentHashMap<String, UserInfo>();
		if (usersdb == null || usersdb.isEmpty() || usersdb.size() <= 0) {
			logger.info("usersdb.isEmpty()!!");
			return null;
		}

		for (UserPubd u : usersdb) {
			try {
				// logger.info("u.getKey():" + u.getKey() + ",u.getUserInfo():"
				// + u.getUserInfo());
				UserInfo utmp = (UserInfo) Base64.decodeToObject(u.getUserInfo());
				// logger.info("utmp start!" );
				// logger.info("utmp toString:" + utmp.toString());
				// logger.info("utmp getFullId:" + utmp.getFullId());
				dbusersMap.put(u.getXey(), utmp);
			} catch (IOException e) {
				logger.error("IOException:" + e.getMessage());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				logger.error("ClassNotFoundException:" + e.getMessage());
			}
		}
		// logger.info("userdb size:" + dbusersMap.size());
		watch.stop();
		logger.info("\nTotal execution time to getDbUsers in millis: " + watch.getTotalTimeMillis());
		return dbusersMap.values();
	}

	// users.containsKey
	private boolean containsByKey(String key) {
		return userPubService.containsKey("USER", key);
	}

	// users.get
	private UserInfo getByKey(String key) {
		UserPubd strUserinfo = userPubService.find("USER", key);
		if (strUserinfo == null) {
			return null;
		}
//		logger.info("strUserinfo:" + FilterUtils.escape(strUserinfo.getUserInfo()));
		logger.info("strUserinfo:");
		try {
			// 取得db資料後,轉換字串to物件
			return (UserInfo) Base64.decodeToObject(strUserinfo.getUserInfo());
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} catch (ClassNotFoundException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
		return null;
	}

	public UserPubd getdbByscriptSessionId(String scriptSession) {
		logger.info("getdbByscriptSessionId!!!");
		UserPubd rtnUserpub = userPubService.findscriptSessionId("USER", scriptSession);
		if (rtnUserpub == null) {
			logger.info("getdbByscriptSessionId null.");
			return null;
		}

		return rtnUserpub;

	}

	// public UserPubd getdbByscriptSessionId(String scriptSession) {
	// logger.info("getdbByscriptSessionId!!!");
	//
	// Collection<UserInfo> usersDbtmp = getDbUsers();
	// if (usersDbtmp == null) {
	// return null;
	// }
	// for (UserInfo u : usersDbtmp) {
	// //logger.info("in getUserpubdBySessionId u.");
	// //logger.info("getUserpubdBySessionId u:"+u.httpSessionId);
	// if (u.getLastSession().equals(scriptSession)) {
	// logger.info("u.getFullId():"+u.getFullId()+","+scriptSession +" is same!");
	// return userPubService.find("USER", u.getFullId());
	// }
	// }
	// logger.info("getdbByscriptSessionId null.");
	// return null;
	// }

	// users.remove
	private void removeByKey(String key) {
		userPubService.removeKey("USER", key);
	}

	// users.put
	private void putByKey(String key, UserInfo userInfo) {
		String strUserinfo = "";
		try {
			strUserinfo = Base64.encodeObject(userInfo);
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
		UserPubd b = new UserPubd();
		b.setTableName("USER");
		b.setXey(key);
		b.setBrno(userInfo.getBrno());
		b.setLvel(userInfo.getLevel());
		b.setName(userInfo.getName());
		b.setHttpSessionId(userInfo.getHttpSessionId());
		b.setScriptSessionId(userInfo.getLastSession());
		b.setDapKnd(userInfo.getDapKnd());
		b.setOapKnd(userInfo.getOapKnd());
		b.setCldept(userInfo.getCldept());
		// b.setPswd();
		b.setOvrToken(userInfo.getOvrToken());
		b.setLastJnlSeq(-1);
		b.setLocate(GlobalValues.getLocalAddr());
//		logger.info("getLocalAddr:" + FilterUtils.escape(GlobalValues.getLocalAddr()));
//		logger.info("strUserinfo len:" + FilterUtils.escape(strUserinfo.length() + "," + strUserinfo));
		b.setUserInfo(strUserinfo);
		userPubService.save(b);
	}

	private void updataByKey(String key, UserInfo userInfo) {
		String strUserinfo = "";
		try {
			strUserinfo = Base64.encodeObject(userInfo);
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
		userPubService.updataSavedata("USER", key, userInfo.getBrno(), userInfo.getLevel(), userInfo.getName(), userInfo.getHttpSessionId(), userInfo.getLastSession(), userInfo.getDapKnd(),
				userInfo.getOapKnd(), userInfo.getOvrToken(), GlobalValues.getLocalAddr(), userInfo.getLastJnlSeq(), strUserinfo);
	}
	// private void updataByKey(String key, UserInfo userInfo) {
	// String strUserinfo = "";
	// try {
	// strUserinfo = Base64.encodeObject(userInfo);
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// StringWriter errors = new StringWriter();
	// e.printStackTrace(new PrintWriter(errors));
	// logger.error(errors.toString());
	// }
	// userPubService.updataSave("USER",key,strUserinfo);
	// }

}
