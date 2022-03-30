package com.st1.ifx.hcomm.sock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HexConvert {
	static final Logger logger = LoggerFactory.getLogger(HexConvert.class);

	public static byte[] convertHexString(byte[] data) {
		// AFTER CP937
		logger.info("before convertHexString:" + HexDump.dumpHexString(data));
		byte[] datachange = new byte[data.length];
		// CCSID 37 has [] at hex BA and BB instead of at hex AD and BD respectively.???
		for (int i = 0; i < data.length; i++) {
			if (data[i] == 0x15) {
				datachange[i] = (byte) 0x25; // 0x0E 會被刪除
			} else {
				datachange[i] = data[i];
			}
		}
		logger.info("after convertHexString:" + HexDump.dumpHexString(datachange));
		return datachange;
	}

}
