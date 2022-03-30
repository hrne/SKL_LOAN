package com.st1.ifx.file.fdp.item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Scope;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ShareConverter {
	private static final Logger logger = LoggerFactory.getLogger(ShareConverter.class);

	// TODO 未來如有需求,可以依照此設計再增加功能
	/*
	 * 第一個底線之前的全部都是資料夾，看需要幾層都可以 第一個底線到第二個底線之間是檔案完整名稱
	 * 第二個底線之後的是不重複之序號(給平台吃檔inbox使用,無功能) 最後面.share 固定
	 * "資料夾1"-"資料夾2"_檔案完整名稱_不重複序號.share
	 */

	@ServiceActivator
	public void convert(Message msg) throws Throwable {

	}

	private void copyFileToOutputFolder(String filePath) {
	}

	private void moveFileToBackupFolder(String filePath) {
	}
}
