package com.st1.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.st1.ifx.hcomm.HostTran;
import com.st1.ifx.hcomm.app.ImsLoginSock;
import com.st1.ifx.hcomm.app.SimpleJournal;
import com.st1.ifx.menu.TranListBuilder;
import com.st1.ifx.service.CodeListService;
import com.st1.ifx.service.HelpListService;
import com.st1.ifx.service.JournalService;
import com.st1.ifx.service.LocalRimService;
import com.st1.ifx.service.MsgService;
import com.st1.ifx.service.SwiftUnsoMsgService;
import com.st1.ifx.service.TickerService;
import com.st1.ifx.service.TranDocService;
import com.st1.ifx.service.TxcdService;
import com.st1.ifx.service.UserPubService;

public class MySpring implements ApplicationContextAware {
	private static Log log = LogFactory.getLog(MySpring.class);
	private static ApplicationContext appCtx;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appCtx = applicationContext;
		log.info("spring context injected");
	}

	public static Object getBean(String beanName) {
		return appCtx.getBean(beanName);
	}

	public static <T> T getBean(String name, Class<T> requiredType) {
		log.info("getBean:" + name);
		return appCtx.getBean(name, requiredType);
	}

	public static JournalService getJournalService() {
		return appCtx.getBean("journalService", JournalService.class);
	}

	public static TranDocService getTranDocService() {
		return appCtx.getBean("tranDocService", TranDocService.class);
	}

	public static HelpListService getHelpListService() {
		return appCtx.getBean("helpListService", HelpListService.class);
	}

	public static TxcdService getTxcdService() {
		return appCtx.getBean("txcdService", TxcdService.class);
	}

	public static CodeListService getCodeListService() {
		return appCtx.getBean("codeListService", CodeListService.class);
	}

	public static LocalRimService getLocalRimService() {
		return appCtx.getBean("localRimService", LocalRimService.class);
	}

	public static MsgService getMsgService() {
		return appCtx.getBean("msgService", MsgService.class);
	}

	public static TickerService getTickerService() {
		return appCtx.getBean("tickerService", TickerService.class);
	}

	public static UserPubService getUserPubService() {
		return appCtx.getBean("userPubService", UserPubService.class);
	}

	public static SwiftUnsoMsgService getSwiftUnsoMsgService() {
		return appCtx.getBean("swiftUnsoMsgService", SwiftUnsoMsgService.class);
	}

	// public static ImsLogin getLoginHostBean() {
	// return appCtx.getBean("imsLogin", ImsLogin.class);
	// }
	public static ImsLoginSock getLoginHostBean() {
		return appCtx.getBean("imsLoginSock", ImsLoginSock.class);
	}

	public static SimpleJournal getSimpleJournal() {
		return appCtx.getBean("simpleJournal", SimpleJournal.class);
	}

	public static HostTran getHostTranBean() {
		return appCtx.getBean("hostTran", HostTran.class);
	}

	public static TranListBuilder getTranListBuilder() {
		return appCtx.getBean("tranListBuilder", TranListBuilder.class);
	}

}
