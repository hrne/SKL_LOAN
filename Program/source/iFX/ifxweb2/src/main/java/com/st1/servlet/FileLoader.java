package com.st1.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.filter.SafeClose;

/**
 * Servlet implementation class FileLoader
 */
@WebServlet("/FileLoader")
public class FileLoader extends HttpServlet {
	static final Logger logger = LoggerFactory.getLogger(FileLoader.class);
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FileLoader() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String filename = System.getProperty("ifx_fxworkfile") + File.separator + "runtime" + File.separator + "props" + File.separator + "help.js";
		ServletContext sc = getServletContext();
		String mimeType = sc.getMimeType(filename);
		FileInputStream in = null;
		OutputStream out = null;
		if (mimeType == null) {
			sc.log("Could not get MIME type of " + "help.js 51");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		logger.info("writing help.js");
		try {
			// Set content size
			File file = new File(FilterUtils.filter(filename));
			response.setContentLength((int) file.length());

			// Open the file and output streams
			in = new FileInputStream(file);
			out = response.getOutputStream();

			// Copy the contents of the file to the output stream
			byte[] buf = new byte[1024];
			int count = 0;
			while ((count = in.read(buf)) >= 0) {
				out.write(buf, 0, count);
			}

			logger.info("write help.js done");
		} finally {
			SafeClose.close(out);
			SafeClose.close(in);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
