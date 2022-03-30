package com.st1.dwr;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyBase {
	static final Logger logger = LoggerFactory.getLogger(ProxyBase.class);

	public ProxyBase() {
		super();
	}

	public static Object getInstance(String className) {
		Object instance = null;
		try {
			instance = Class.forName(className).newInstance();
		} catch (InstantiationException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			throw new RuntimeException(e);
		}
		return instance;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object reflectionCall(final Object aninstance, final String classname, final String amethodname, final Class[] parameterTypes, final Object[] parameters) {
		Object res;// = null;
		try {
			Class aclass;// = null;
			if (aninstance == null) {
				aclass = Class.forName(classname);
			} else {
				aclass = aninstance.getClass();
			}
			// Class[] parameterTypes = new Class[]{String[].class};
			final Method amethod = aclass.getDeclaredMethod(amethodname, parameterTypes);
			AccessController.doPrivileged(new PrivilegedAction() {
				public Object run() {

					return null; // nothing to return
				}
			});
			res = amethod.invoke(aninstance, parameters);
		} catch (final ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (final SecurityException e) {
			throw new RuntimeException(e);
		} catch (final NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (final IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (final InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		return res;
	}

}