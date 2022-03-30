package com.st1.msw;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.ifx.filter.FilterUtils;

public class HttpClientDemo {
	static final Logger logger = LoggerFactory.getLogger(HttpClientDemo.class);

	/**
	 * 送post沒有參數
	 * 
	 * @param url
	 * @return
	 */
	public static String sendPost(String url) {
		HttpClient demo = HttpClientBuilder.create().build();
		if (url == null) {
			logger.info("sendPost url is null!");
			return null;
		}
		url = url.trim();
		logger.info("sendPost url!" + FilterUtils.escape(url));
		HttpPost httpPost;
		HttpResponse response = null;
		try {
			// Get Request Example，取得 google 查詢 httpclient 的結果
			httpPost = new HttpPost(url);
			logger.info("after httpGet!");

			logger.info("before execute!");
			response = demo.execute(httpPost);
			logger.info("after execute!");
		} catch (ClientProtocolException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}

		String responseString = null;
		try {
			logger.info("before EntityUtils!");
			responseString = EntityUtils.toString(response.getEntity());
			logger.info("before getStatusLine!");
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 如果回傳是 200 OK 的話才輸出
				logger.info("200!");
				logger.info(FilterUtils.escape(responseString));

			} else {
				logger.info("no 200!");
				logger.info(FilterUtils.escape(response.getStatusLine().toString()));
			}

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
		return responseString;
	}

	/**
	 * 送post帶參數 Map<String, String> (Object轉換成String)
	 * 
	 * @param url
	 * @param map
	 * @return
	 */
	public static String sendPost(String url, Map<String, String> map) {
		HttpClient demo = HttpClientBuilder.create().build();
		logger.info("sendPost-map url!" + url);
		HttpPost httpPost = new HttpPost(url);
		ArrayList<NameValuePair> postParameters;
		String entryk = null;
		String entryv = "";

		postParameters = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			entryk = entry.getKey();
			entryv = entry.getValue();
			if (entryv == null)
				entryv = "";
			logger.info(FilterUtils.escape("entry.getKey():" + entryk + "," + entryv));

			// if(entryv != null && !entryv.isEmpty()){
			postParameters.add(new BasicNameValuePair(entryk, entryv));
			// }
		}

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
		} catch (UnsupportedEncodingException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
		HttpResponse response = null;
		try {
			logger.info("before execute!");
			logger.info(httpPost.toString());
			logger.info(FilterUtils.escape(httpPost.getParams().toString()));
			response = demo.execute(httpPost);
			logger.info("after execute!");
		} catch (ClientProtocolException e1) {
			StringWriter errors = new StringWriter();
			e1.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} catch (IOException e1) {
			StringWriter errors = new StringWriter();
			e1.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
		String responseString = null;
		try {
			logger.info("before EntityUtils!");
			responseString = EntityUtils.toString(response.getEntity());
			logger.info("after EntityUtils!");
		} catch (ParseException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
		logger.info("before getStatusLine!");
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			// 如果回傳是 200 OK 的話才輸出
			logger.info("200!");
			logger.info(FilterUtils.escape(responseString));
		} else {
			logger.info(FilterUtils.escape(response.getStatusLine().toString()));
		}
		return responseString;
	}

	/**
	 * 送post帶參數 Map<String, String> (Object轉換成String)
	 * 
	 * @param url
	 * @param map
	 * @return
	 */
	public static String sendPostarr(String url, ArrayList<String> arrlist) {
		HttpClient demo = HttpClientBuilder.create().build();
		logger.info("in sendPostarr sendPost-map url!" + url);
		HttpPost httpPost = new HttpPost(url);
		ArrayList<String> postParameters;
		ArrayList<NameValuePair> postParametersmap;
		postParametersmap = new ArrayList<NameValuePair>();

		postParametersmap.add(new BasicNameValuePair("sessionId", arrlist.get(0)));
		postParametersmap.add(new BasicNameValuePair("methodName", arrlist.get(1)));
		postParametersmap.add(new BasicNameValuePair("statusCode", arrlist.get(2)));
		postParametersmap.add(new BasicNameValuePair("p0", arrlist.get(3)));
		postParametersmap.add(new BasicNameValuePair("p1", arrlist.get(4)));
		postParametersmap.add(new BasicNameValuePair("p2", arrlist.get(5)));
		postParametersmap.add(new BasicNameValuePair("p3", arrlist.get(6)));

		postParameters = new ArrayList<String>();
		postParameters = arrlist;

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(postParametersmap));
		} catch (UnsupportedEncodingException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
		HttpResponse response = null;
		try {
			logger.info("before execute!");
			logger.info(FilterUtils.escape(httpPost.toString()));
			logger.info(FilterUtils.escape(httpPost.getParams().toString()));
			response = demo.execute(httpPost);
			logger.info("after execute!");
		} catch (ClientProtocolException e1) {
			StringWriter errors = new StringWriter();
			e1.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} catch (IOException e1) {
			StringWriter errors = new StringWriter();
			e1.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
		String responseString = null;
		try {
			logger.info("before EntityUtils!");
			responseString = EntityUtils.toString(response.getEntity());
			logger.info("after EntityUtils!");
		} catch (ParseException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
		logger.info("before getStatusLine!");
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			// 如果回傳是 200 OK 的話才輸出
			logger.info("200!");
			logger.info(FilterUtils.escape(responseString));
		} else {
			logger.info(FilterUtils.escape(response.getStatusLine().toString()));
		}
		return responseString;
	}
}
