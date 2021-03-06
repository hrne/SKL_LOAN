package com.st1.itx.eum;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 各線程自有變數
 * 
 * @author AdamPan
 *
 */
public class ThreadVariable {
	private static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>();

	public static Object getObject(String id) {
		if (threadLocal.get() == null)
			return null;

		return threadLocal.get().get(id);
	}

	public static void setObject(String id, Object value) {
		if (Objects.isNull(value))
			return;

		if (threadLocal.get() == null) {
			threadLocal.set(new LinkedHashMap<String, Object>());
			threadLocal.get().put(id, value);
		} else
			threadLocal.get().put(id, value);
	}

	public static ThreadLocal<Map<String, Object>> getThreadLocal() {
		return threadLocal;
	}

	public static void setThreadLocal(ThreadLocal<Map<String, Object>> threadLocal) {
		ThreadVariable.threadLocal = threadLocal;
	}

	public static boolean isLogger() {
		if (threadLocal.get() == null || threadLocal.get().get(ContentName.loggerFg) == null)
			return false;
		return (boolean) threadLocal.get().get(ContentName.loggerFg);
	}

	public static void setLoggerOn() {
		setObject(ContentName.loggerFg, true);
	}

	public static void setLoggerOff() {
		setObject(ContentName.loggerFg, false);
	}

	public static String getEmpNot() {
		if (threadLocal.get() == null || threadLocal.get().get(ContentName.empnot) == null)
			return "";
		else
			return (String) threadLocal.get().get(ContentName.empnot);
	}

	public static void clearThreadLocal() {
		if (!Objects.isNull(threadLocal.get()))
			threadLocal.get().clear();
		threadLocal.remove();
	}

}
