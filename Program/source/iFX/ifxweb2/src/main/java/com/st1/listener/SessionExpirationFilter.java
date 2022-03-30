package com.st1.listener;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.ifx.filter.FilterUtils;
import com.st1.msw.MswCenter;
import com.st1.msw.UserInfo;
import com.st1.msw.UserPub;

/**
 * Servlet Filter implementation class SessionExpirationFilter
 */
@WebFilter("/*")
public class SessionExpirationFilter implements Filter {
	static final Logger logger = LoggerFactory.getLogger(SessionExpirationFilter.class);

	/**
	 * Default constructor.
	 */
	public SessionExpirationFilter() {
		logger.info("* * * SessionExpirationFilter constructed");
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest hReq = (HttpServletRequest) request;
//		HttpServletResponse hRes = (HttpServletResponse) response;

		String reqPath = hReq.getRequestURI();

		HttpSession session = hReq.getSession(false);

		if (null != session) {

			String sessionId = session.getId();
			UserPub pub = UserPub.getInstance();
			UserInfo user = pub.getBySessionId(sessionId);
			if (user == null) {
				try {
					chain.doFilter(request, response);
				} catch (Exception ex) {
					logger.info("chain.doFilter:" + ex.getMessage());
				}
				return;
			}
			String userId = (user != null) ? user.getFullId() : "*unknown*";

			Date expirationDate = null;

			try {
				expirationDate = (Date) session.getAttribute("expirationDate");
			} catch (Exception ex) {
				logger.info("SessionExpirationFilter killSession getAttribute expirationDate!" + ex.getMessage());
			}
			Date dtNow = new Date();
			long seconds = 0L;
			if (expirationDate != null) {
//				logger.info("doFilter       : [" + reqPath + "]");
//				logger.info("User           : [" + user.getFullId() + "]");
//				logger.info("expirationDate : [" + expirationDate.toString() + "]");
//				logger.info("dtNow          : [" + dtNow.toString() + "]");

				if (expirationDate.before(dtNow)) {
					try {
						session.invalidate();
						logger.info("Session Invalidate!!!!");
					} catch (Exception ex) {
						StringWriter errors = new StringWriter();
						ex.printStackTrace(new PrintWriter(errors));
						logger.error(errors.toString());
					}
					session = null;
					return;
				} else {
					seconds = (expirationDate.getTime() - dtNow.getTime()) / 1000;
					if (seconds <= 60) {
						if (session.getAttribute("beforeTimeout") == null) {
							session.setAttribute("beforeTimeout", "yes");

							MswCenter.getInstance().beforLogoffUser(user);

							// 1.5 more minute
							// 一分鐘等待櫃員選擇是否繼續
							// 若選擇登出 再半分鐘執行登出
							logger.info("session.getMaxInactiveInterval() " + new Date(System.currentTimeMillis() + 90 * 1000));
							logger.info("session.getMaxInactiveInterval() " + session.getMaxInactiveInterval());
							session.setAttribute("expirationDate", new Date(System.currentTimeMillis() + 90 * 1000));
						} else {
							logger.info(FilterUtils.escape(userId) + "** already in before logoff!");
						}
					}
				}
			}
			// add mvc/hnd/tickers
			// 2016/11/01 加入濾掉 XG910匯率的刷新
			if (reqPath == null || !(reqPath.endsWith("ReverseAjax.dwr") || reqPath.contains("hnd/tickers") || reqPath.contains("EXCHRATE/ALL"))) {
				try {
					if (session.getAttribute("beforeTimeout") != null)
						session.removeAttribute("beforeTimeout");
					else
						logger.info(FilterUtils.escape(userId + "** beforeTimeout not null=>" + reqPath));

//					logger.info("session.getMaxInactiveInterval() " + session.getMaxInactiveInterval());
					session.setAttribute("expirationDate", new Date(System.currentTimeMillis() + 1800 * 1000));
				} catch (Exception ex) {
					logger.info("SessionExpirationFilter beforeTimeout!" + ex.getMessage());
				}
			}
		}

		try {
			// pass the request along the filter chain
			chain.doFilter(request, response);
		} catch (Exception ex) {
			logger.warn("chain.doFilter warn!" + ex.getMessage());
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
