package com.st1.bean;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.ifx.filter.FilterUtils;
import com.st1.util.PoorManFile;
import com.st1.util.PoorManJson;

@Component("fileReposBean")
@Scope("singleton")
public class FileReposBean {

	@Value("${ifx_fxtxwrite}")
	private String fileRepos;

	private final Logger logger = LoggerFactory.getLogger(FileReposBean.class);

	@PostConstruct
	private void init() {
		this.fileRepos += File.separator + "temp";
		logger.info("fileReposBean init : " + FilterUtils.escape(fileRepos));
		File f = new File(FilterUtils.filter(fileRepos));
		if (!f.exists())
			f.mkdirs();
	}

	private String getFilePath(String filename) {
		return this.fileRepos + File.separator + filename;

	}

	private void createFolder(String filePath) {
		logger.info("createFolder:" + FilterUtils.escape(filePath));
		File file = new File(FilterUtils.filter(filePath));
		File parent = new File(file.getParent());
		logger.info("parent:" + parent.toString());
		if (!parent.exists()) {
			parent.mkdirs();
			logger.info("create:" + parent.toString());
		}
	}

	public String write(String filename, String content) {
		logger.info("file path:" + FilterUtils.escape(filename));
		logger.info("content:" + FilterUtils.escape(content));

		String filePath = getFilePath(filename);
		createFolder(filePath);

		PoorManFile poorFile = new PoorManFile(filePath);
		try {
			poorFile.write(content);
			return makeResponse(true, "write ok");
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			return makeResponse(false, "write error:\n" + e.getMessage() + "\n路徑:" + filePath);
		}
	}

	public String read(String filename, boolean br) {
		// HashMap<String, String> map = PoorManJson.json2Map(req);
		// String filename = map.get("p");
		String filePath = getFilePath(filename);
		PoorManFile poorFile = new PoorManFile(filePath);
		try {
			// return makeResponse(true, poorFile.read());
			if (br)
				return makeResponse(true, MimeUtility.encodeWord(poorFile.read(), "utf-8", "Q")); // chrome
			else
				return makeResponse(true, URLEncoder.encode(poorFile.read(), "utf-8"));
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			return makeResponse(false, "read error:\n" + e.getMessage());
		}
	}

	public String read(String filename) {
		String filePath = getFilePath(filename);
		PoorManFile poorFile = new PoorManFile(filePath);
		try {
			return makeResponse(true, poorFile.read());
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			return makeResponse(false, "read error:\n" + e.getMessage());
		}
	}

	public String delete(String filename) {
		String filePath = getFilePath(filename);
		try {
			File f = new File(filePath);
			if (f.exists()) {
				f.delete();
			}
			return makeResponse(true, "");
		} catch (Exception e) {
			return makeResponse(false, e.getMessage());
		}

	}

	public boolean exists(String filename, String txCode) {
		String filePath = this.getFilePath(filename);
		File f = new File(FilterUtils.filter(filePath));
		File[] files = f.listFiles(this.fileNameFilter(txCode));
		return !Objects.isNull(files) && files.length > 0;
	}

	public List<String> listFile(String filename, String txCode) {
		String filePath = this.getFilePath(filename);
		File f = new File(FilterUtils.filter(filePath));
		List<File> fileLi = Arrays.asList(f.listFiles(this.fileNameFilter(txCode)));
		List<String> fileNameLi = new ArrayList<String>();
		for (File fi : fileLi)
			fileNameLi.add(filename + "/" + fi.getName());
		return fileNameLi;
	}

	private FilenameFilter fileNameFilter(String txCode) {
		return new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.matches("^" + txCode + "_.+");
			}
		};
	}

	private String makeResponse(boolean ok, String msg) {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("status", ok);
		m.put("msg", msg);
		return PoorManJson.toJson(m);
	}

	public String getFileRepos() {
		return fileRepos;
	}

	public void setFileRepos(String fileRepos) {
		this.fileRepos = fileRepos;
	}
}
