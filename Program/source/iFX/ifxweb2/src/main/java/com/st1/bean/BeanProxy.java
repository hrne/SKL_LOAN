package com.st1.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.dwr.ProxyBase;
import com.st1.ifx.filter.FilterUtils;

public class BeanProxy extends ProxyBase {
	private static String beanPackage = "com.st1.bean.";
	static final Logger logger = LoggerFactory.getLogger(BeanProxy.class);

	public String dispatch(String bean, String reqJson) {
		String[] ss = bean.split("\\.");
		String clsName = beanPackage + ss[0];
		String method = ss[1];
		logger.info(FilterUtils.escape("invoke " + clsName + ", method:" + method));
		logger.info(FilterUtils.escape("parms:" + reqJson));
		@SuppressWarnings("rawtypes")
		Class[] argTypes = new Class[] { String.class };
		Object instance = getInstance(clsName);

		Object r = reflectionCall(instance, clsName, method, argTypes, new Object[] { reqJson });
		logger.info("dispatch result:\n" + r.toString());
		return r.toString();
	}

}
