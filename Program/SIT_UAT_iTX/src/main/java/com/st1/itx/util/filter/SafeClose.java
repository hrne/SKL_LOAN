package com.st1.itx.util.filter;

import java.io.Closeable;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class SafeClose {
	static final Logger logger = LoggerFactory.getLogger(SafeClose.class);

	/**
	 * 安全關閉IO
	 * 
	 * @param io Closeable
	 */
	public static void close(Closeable io) {
		if (io != null) {
			try {
				io.close();
			} catch (Throwable t) {
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				logger.warn(errors.toString());
			}
		}
	}

	public static void close(Document doc) {
		if (doc != null) {
			try {
				doc.close();
			} catch (Throwable t) {
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				logger.warn(errors.toString());
			}
		}
	}

	public static void close(PdfStamper stamper) {
		if (stamper != null) {
			try {
				stamper.close();
			} catch (Throwable t) {
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				logger.warn(errors.toString());
			}
		}

	}

	public static void close(PdfReader reader) {
		if (reader != null) {
			try {
				reader.close();
			} catch (Throwable t) {
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				logger.warn(errors.toString());
			}
		}

	}

	public static void close(PdfWriter writer) {
		if (writer != null) {
			try {
				writer.close();
			} catch (Throwable t) {
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				logger.warn(errors.toString());
			}
		}
	}
}
