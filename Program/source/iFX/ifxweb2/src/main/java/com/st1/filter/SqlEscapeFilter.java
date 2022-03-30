package com.st1.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.*;
import java.util.*;

/*
 *Servlet Filter implementation class SqlEscapeFilter
 */

public class SqlEscapeFilter implements Filter {
	/**
	 * Default constructor.
	 */
	public SqlEscapeFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		HttpServletRequest hreq = (HttpServletRequest) request;
		Map map = hreq.getParameterMap();
		Iterator itr = map.keySet().iterator();
		while (itr.hasNext()) {
			String key = itr.next().toString();
			String[] values = hreq.getParameterValues(key);
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					values[i] = cleanXSS(values[i]);
				}
			}
			hreq.setAttribute(key, values);
		}
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	private String cleanXSS(String value) {
		value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");

		value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "&#41;");

		value = value.replaceAll("'", "& #39;");

		value = value.replaceAll("eval\\((.*)\\)", "");

		value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");

		value = value.replaceAll("script", "");

		return value;
	}
}
