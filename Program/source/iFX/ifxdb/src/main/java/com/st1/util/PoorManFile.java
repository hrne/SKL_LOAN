package com.st1.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.filter.SafeClose;

/**
 * Read and write a file using an explicit encoding. Removing the encoding from
 * this code will simply cause the system's default encoding to be used instead.
 */
public final class PoorManFile {
	static final Logger logger = LoggerFactory.getLogger(PoorManFile.class);

	public PoorManFile(String filepath) {
		this.filepath = filepath;
	}

	/** Constructor. */
	PoorManFile(String PoorManFile, String encoding) {
		this.encoding = encoding;
		this.filepath = PoorManFile;
	}

	public static String combine(String... paths) {
		File file = new File(paths[0]);

		for (int i = 1; i < paths.length; i++) {
			file = new File(file, paths[i]);
		}

		return file.getPath();
	}

	public void createParentFolder() {
		File file = new File(filepath);
		File parent = new File(file.getParent());
		if (!parent.exists())
			parent.mkdirs();
	}

	/** Write fixed content to the given file. */
	public void write(String s) throws IOException {
		FileOutputStream fos = null;
		Writer out = null;

		log("Writing to file named " + filepath + ". Encoding: " + encoding);
//		File f = new File(FilterUtils.filter(filepath));
		File f = new File(filepath);
		if (!f.exists())
			f.createNewFile();
		try {
			fos = new FileOutputStream(filepath);
			out = new OutputStreamWriter(fos, encoding);
			out.write(s);
		} finally {
			SafeClose.close(out);
			SafeClose.close(fos);
		}
	}

	public void append(String s) throws IOException {
		FileOutputStream fos = null;
		Writer out = null;

		log("Appending to file named " + filepath + ". Encoding: " + encoding);
		File f = new File(filepath);
		if (!f.exists())
			f.createNewFile();

		try {
			fos = new FileOutputStream(filepath, true);
			out = new OutputStreamWriter(fos, encoding);
			out.write(s);
		} finally {
			SafeClose.close(out);
			SafeClose.close(fos);
		}
	}

	/** Read the contents of the given file. */
	public String read() throws IOException {
		log("Reading from file.");
		FileInputStream fis = null;
		Scanner scanner = null;
		try {
			fis = new FileInputStream(filepath);
			StringBuilder text = new StringBuilder();
			String NL = System.getProperty("line.separator");

			scanner = new Scanner(fis, encoding);
			while (scanner.hasNextLine()) {
				text.append(scanner.nextLine() + NL);
			}
			log("Text read filepath:" + filepath + ",len: " + text.length());
			return text.toString();
		} finally {
			SafeClose.close(scanner);
			SafeClose.close(fis);
		}
	}

	// PRIVATE
	private String filepath;
	private String encoding = "utf-8";

	private void log(String aMessage) {
		logger.info(FilterUtils.escape(aMessage));
	}

	/** Requires two arguments - the file name, and the encoding to use. */
	public static void main(String... aArgs) throws IOException {

		String fileName = "d:/temp/abc.txt";
		String encoding = "utf-8";
		PoorManFile test = new PoorManFile(fileName, encoding);
		test.append("hello\n");
		for (int i = 0; i < 10; i++) {
			test.append("bye " + i + "\n");
		}
		String s = test.read();
		logger.info("S:" + FilterUtils.escape(s));

	}

}
