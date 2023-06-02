package com.st1.itx.util.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.util.filter.FilterUtils;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.log.SysLogger;

/**
 * WebClient
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Component("webClient")
@Scope("singleton")
public class WebClient extends SysLogger {
	/**
	 * 訊息通知
	 * 
	 * @param vDate  Expiry Date
	 * @param vTime  Expiry Time
	 * @param to     TlrNo
	 * @param mode   msg Mode
	 * @param txCode chan Code
	 * @param params ntxbuf
	 * @param msg    Content
	 * @param titaVo TitaVo
	 * @return String
	 */
	public String sendPost(String vDate, String vTime, String to, String mode, String txCode, String params, String msg, TitaVo titaVo) {
		this.info("sendPost...");
		this.info("mode : " + mode + " txCode : " + txCode + " msg : " + msg);

		if (vDate == null || vTime == null || to == null || mode == null || txCode == null || params == null || msg == null) {
			this.info("One Of Params Is Null..");
			return null;
		}

		String msgNo = "";

		String url = FormatUtil.padX(vDate, 8) + FormatUtil.padX(vTime, 4) + FormatUtil.pad9(msgNo, 5) + FormatUtil.padX(msg.replaceAll("\\.", "@"), 200);
		url += FormatUtil.padX(mode, 1);
		url += FormatUtil.padX(txCode, 5);
		url += params;
		if (System.getProperty("ifx_LocalAddr") == null)
			url = "http://192.168.10.8:7003/iFX/mvc/msw/systemTalk/host/" + to + "/" + url.trim();
		else
			url = System.getProperty("ifx_LocalAddr") + "/mvc/msw/systemTalk/host/" + to + "/" + url.trim();
		url = UriUtils.encodePath(url, "UTF-8");
		// url = url.replaceAll(" ", "_");
		HttpClient httpClient = HttpClientBuilder.create().build();
		this.info("sendPost url!" + url);
		HttpPost httpPost;
		HttpResponse response = null;
		try {
			// Get Request Example，取得 google 查詢 httpclient 的結果
			httpPost = new HttpPost(url);
			this.info("after httpGet!");

			this.info("before execute!");
			response = httpClient.execute(httpPost);
			this.info("after execute!");
		} catch (ClientProtocolException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

		String responseString = null;
		try {
			this.info("before EntityUtils!");
			responseString = EntityUtils.toString(response.getEntity());
			this.info("before getStatusLine!");
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 如果回傳是 200 OK 的話才輸出
				this.info("200!");
				this.info(FilterUtils.escape(responseString));

			} else {
				this.info("no 200!");
				this.info(FilterUtils.escape(response.getStatusLine().toString()));
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}
		return responseString;
	}

	/**
	 * @param brNo     Brno
	 * @param tickNo   訊息編號 (編號一致後蓋前) 刪除模式依據此序號刪除
	 * @param stopTime 顯示停止時間 西元 8 + 4 時間
	 * @param msg      訊息內容
	 * @param mode     false : insert and replace mode true : delete mode
	 * @param titaVo   titaVo
	 */
	public void sendTicker(String brNo, String tickNo, String stopTime, String msg, boolean mode, TitaVo titaVo) {
		this.info("sendTicker...");
		this.info("tickNo : [" + tickNo + "] Msg : [" + msg + "]");

		if ((Objects.isNull(msg) || Objects.isNull(tickNo) || Objects.isNull(titaVo)) && !mode) {
			this.info("One Of Params Is Null..");
			return;
		}

		if (Objects.isNull(tickNo) && mode) {
			this.info("tickNo Is Null..");
			return;
		}

		String url;
		if (System.getProperty("ifx_LocalAddr") == null)
			url = "http://192.168.10.8:7003/iFX/mvc/hnd/tickers/add";
		else
			url = System.getProperty("ifx_LocalAddr") + "/mvc/hnd/tickers/add";

//        url = UriUtils.encodePath(url, "UTF-8");

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);// 创建httpPost
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-Type", "application/json");
		String charSet = "UTF-8";

		Map<String, String> m = new HashMap<>();
		m.put("brNo", brNo);
		m.put("tickNo", tickNo);
		m.put("msg", msg);
		m.put("stopTime", stopTime);
		m.put("mode", mode ? "1" : "0");

		try {
			StringEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(m), charSet);
			httpPost.setEntity(entity);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);
			StatusLine status = response.getStatusLine();
			int state = status.getStatusCode();
			if (state == HttpStatus.SC_OK) {
				HttpEntity responseEntity = response.getEntity();
				String jsonString = EntityUtils.toString(responseEntity);
			} else {
				this.error("返回" + state + "(" + url + ")");
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            this.error(errors.toString());
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					StringWriter errors = new StringWriter();
                    e.printStackTrace(new PrintWriter(errors));
                    this.error(errors.toString());
				}
			}
			try {
				httpclient.close();
			} catch (IOException e) {
				StringWriter errors = new StringWriter();
                e.printStackTrace(new PrintWriter(errors));
                this.error(errors.toString());
			}
		}
	}
}
