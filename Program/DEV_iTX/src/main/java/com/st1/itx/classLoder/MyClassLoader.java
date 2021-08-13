package com.st1.itx.classLoder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

public class MyClassLoader {

	private final static ConcurrentHashMap<String, MyURLClassLoader> LOADER_CACHE = new ConcurrentHashMap<>();

//    private MyURLClassLoader urlClassLoader;

	public void loadJar(String jarName) throws MalformedURLException {
		MyURLClassLoader urlClassLoader = LOADER_CACHE.get(jarName);
		if (urlClassLoader != null) {
			return;
		}
		urlClassLoader = new MyURLClassLoader();
//        String path = systemConfig.getExternalClassPath();
		String path = "C://jcuJar";
		URL jarUrl = new URL("jar:file:/" + path + "/" + jarName + "!/");
		urlClassLoader.addURLFile(jarUrl);
		LOADER_CACHE.put(jarName, urlClassLoader);
	}

	public Class<?> loadClass(String jarName, String name) throws ClassNotFoundException {
		MyURLClassLoader urlClassLoader = LOADER_CACHE.get(jarName);
		if (urlClassLoader == null) {
			return null;
		}
		return urlClassLoader.loadClass(name);
	}

	public void unloadJarFile(String jarName) throws MalformedURLException {
		MyURLClassLoader urlClassLoader = LOADER_CACHE.get(jarName);
		if (urlClassLoader == null) {
			return;
		}
//        String path = systemConfig.getExternalClassPath();
		String path = "C://jcuJar";
		String jarStr = "jar:file:/" + path + "/" + jarName + "!/";
		urlClassLoader.unloadJarFile(jarStr);
		urlClassLoader = null;
		LOADER_CACHE.remove(jarName);
	}
}