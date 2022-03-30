package com.st1.ifx.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NewsTickerTask {
	private static final Logger logger = LoggerFactory.getLogger(NewsTickerTask.class);

	private final static ReentrantReadWriteLock fLock = new ReentrantReadWriteLock();
	private final static Lock fReadLock = fLock.readLock();
	private final static Lock fWriteLock = fLock.writeLock();

	// @Scheduled(initialDelay = 10000, fixedDelay = 300000)
	public void doSomethingWithDelay() {
		fWriteLock.lock();
		try {
			readNews();
			logger.info("current tickers:");
			for (Object o : news) {
				logger.info(o.toString());
			}
		} finally {
			fWriteLock.unlock();
		}
	}

	private void readNews() {
		news = null;
		List<String> list = new ArrayList<String>();
		String fmt = "<a href='%s'  target='_blank' >〈新聞〉%s</a>";
		Document doc;
		try {

			// need http protocol
			doc = Jsoup.connect("http://tw.news.yahoo.com/world/most-popular/").get();

			// get page title
			String title = doc.title();
			System.out.println("title : " + title);

			Element popular = doc.getElementById("MediaStoryList");
			// get all links
			Elements links = popular.select("a[href]");
			for (Element link : links) {
				String t = String.format(fmt, link.attr("abs:href"), link.text());
				list.add(t);
				System.out.println(t);
				// get the value from href attribute
				System.out.println("\nlink : " + link.attr("abs:href"));
				System.out.println("text : " + link.text());
			}
			news = new String[list.size()];
			for (int i = 0; i < news.length; i++)
				news[i] = list.get(i);
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}

	}

	private static String[] news = new String[0];

	public String[] getNews() {
		String[] result = new String[0];
		fReadLock.lock();
		try {
			if (news != null) {
				result = new String[news.length];
				for (int i = 0; i < news.length; i++) {
					result[i] = news[i];
				}
			}
		} finally {
			fReadLock.unlock();
		}
		return result;
	}
}
