package com.st1.ifx.web;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.st1.ifx.filter.FilterUtils;
import com.st1.servlet.GlobalValues;

@Service
public class VersionTask {
	static final Logger logger = LoggerFactory.getLogger(VersionTask.class);

	@Scheduled(initialDelay = 90000, fixedDelay = 30000)
	public void doSomethingWithDelay() {
		// logger.info("checking js version, path:"
		// + GlobalValues.jsVersionPath);
		if (timeStamp == 0L) {
			File file = new File(FilterUtils.filter(GlobalValues.jsVersionPath));
			if (file != null) {
				this.timeStamp = file.lastModified();
				// 一開始就讀取版號
				logger.info("readJsVersion!!" + timeStamp);
				GlobalValues.readJsVersion();
			}
		} else {
			if (isUpdated()) {
				logger.info("js version changed");
				GlobalValues.readJsVersion();
			}
		}
		// for Help.js
		if (timeStamp2 == 0L) {
			File file2 = new File(FilterUtils.filter(GlobalValues.helpjsVersionPath));
			if (file2 != null) {
				this.timeStamp2 = file2.lastModified();
				logger.info("readHelpjsVersion!!" + timeStamp2);
				// 一開始就讀取版號
				GlobalValues.readHelpjsVersion(false);
			}
		} else {
			if (ishelpUpdated()) {
				logger.info("Help.js version changed");
				GlobalValues.readHelpjsVersion(false);
			}
		}
	}

	private long timeStamp = 0L;
	private long timeStamp2 = 0L;// for Help.js

	private boolean isUpdated() {
		File file = new File(FilterUtils.filter(GlobalValues.jsVersionPath));
		long timeStamp = file.lastModified();

		if (this.timeStamp != timeStamp) {
			this.timeStamp = timeStamp;
			// Yes, file is updated
			return true;
		}
		// No, file is not updated
		return false;
	}

	// for Help.js
	private boolean ishelpUpdated() {
		File file2 = new File(FilterUtils.filter(GlobalValues.helpjsVersionPath));
		long timeStamp2 = file2.lastModified();

		if (this.timeStamp2 != timeStamp2) {
			this.timeStamp2 = timeStamp2;
			// Yes, file is updated
			return true;
		}
		// No, file is not updated
		return false;
	}

}
