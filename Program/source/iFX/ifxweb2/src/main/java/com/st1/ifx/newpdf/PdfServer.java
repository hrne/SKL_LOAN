package com.st1.ifx.newpdf;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.ifx.filter.SafeClose;
import com.st1.ifx.hcomm.sock.HexDump;

public class PdfServer extends java.lang.Thread {
	static final Logger logger = LoggerFactory.getLogger(PdfServer.class);

	private boolean OutServer = false;
	private ServerSocket server;
	private int ServerPort = 5100;

	public PdfServer() {
		logger.info("PdfServer Port:" + ServerPort);
		try {
			server = new ServerSocket(ServerPort);
		} catch (java.io.IOException e) {
			logger.info("PdfServer error!" + ServerPort);
			logger.info("IOException :" + e.toString());
		}
	}

	public void run() {
		Socket socket = null;
		InputStream in;

		logger.info("PdfServer !");
		while (!OutServer) {
			in = null;
			try {
				synchronized (server) {
					socket = server.accept();
				}
				logger.info("InetAddress = " + socket.getInetAddress());
				socket.setSoTimeout(15000);
				logger.info("pdf ok");
				// java.util.Timer timer = new java.util.Timer();
				// java.util.TimerTask task = new CheckSocketComp(socket);
				// timer.schedule(task, 10000);//�@�����٨S�Ұʧ���,�۰�cutdown

				/*
				 * in = socket.getInputStream(); int leng =
				 * Integer.parseInt(in.toString().substring(0, 4)); byte[] len = new byte[leng];
				 * int readCount =0;
				 */

				in = socket.getInputStream();
				try {
					logger.info("pdf in:" + in);
					PdfLengthFramer framer = new PdfLengthFramer(in);
					byte[] data = framer.nextMsg();
					logger.info("pdf data:" + data);
					String result = new String(data, "cp937");
					logger.info("pdf result:" + result);
					logger.info("CP937 data:" + HexDump.dumpHexString(data));

				} catch (Exception e) {
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					logger.error(errors.toString());
				}

				logger.info("pdf GO-ItxtSample");
				in.close();
				logger.info("pdf GO-ItxtSample1");
				in = null;
				socket.shutdownInput();
				socket.close();
				logger.info("pdf GO-ItxtSample2");

			} catch (java.io.IOException e) {
				logger.info("Socket error!");
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
			} catch (NumberFormatException e1) {
				StringWriter errors = new StringWriter();
				e1.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
			} catch (Exception e1) {
				StringWriter errors = new StringWriter();
				e1.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
			} finally {
				if (socket != null)
					try {
						socket.close();
					} catch (IOException e) {
						;
					}
				SafeClose.close(in);
			}
		}
	}

	public static void main(String args[]) {
		(new PdfServer()).start();
	}

}
