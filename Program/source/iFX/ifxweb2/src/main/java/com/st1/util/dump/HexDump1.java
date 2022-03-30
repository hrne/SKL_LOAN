package com.st1.util.dump;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.filter.SafeClose;

public class HexDump1 {
	static final Logger logger = LoggerFactory.getLogger(HexDump1.class);

	public static void main(String[] args) throws IOException {
		String inputFileName = args[0];
		int start = Integer.parseInt(args[1]);
		int end = Integer.parseInt(args[2]);
		int width = Integer.parseInt(args[3]);
		byte[] bytes = read(inputFileName, start, end);
		for (int index = 0; index < bytes.length; index += width) {
			printHex(bytes, index, width);
			printAscii(bytes, index, width);
		}
	}

	private static byte[] read(String inputFileName, int start, int end) throws FileNotFoundException, IOException {
		File theFile = new File(FilterUtils.filter(inputFileName));
		FileInputStream input = null;
		try {
			input = new FileInputStream(theFile);
			int skipped = 0;
			while (skipped < start) {
				skipped += input.skip(start - skipped);
			}
			int length = (int) (Math.min(end, theFile.length()) - start);
			byte[] bytes = new byte[length];
			int bytesRead = 0;
			while (bytesRead < bytes.length) {
				bytesRead = input.read(bytes, bytesRead, bytes.length - bytesRead);
				if (bytesRead == -1) {
					break;
				}
			}
			return bytes;
		} finally {
			SafeClose.close(input);
		}
	}

	private static void printHex(byte[] bytes, int offset, int width) {
		for (int index = 0; index < width; index++) {
			if (index + offset < bytes.length) {
				logger.info("%02x ", FilterUtils.escape(bytes[index + offset]));
			} else {
				logger.info("	");
			}
		}
	}

	//
	private static void printAscii(byte[] bytes, int index, int width) throws UnsupportedEncodingException {
		if (index < bytes.length) {
			width = Math.min(width, bytes.length - index);
			logger.info(FilterUtils.escape(":" + new String(bytes, index, width, "UTF-8").replaceAll("\r\n", " ").replaceAll("\n", " ")));
		} else {
			logger.info("");
		}
	}
}
