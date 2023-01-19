package com.st1.itx.util.common;

import org.springframework.context.annotation.Scope;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

/**
 * RestCom
 *
 * @author Wei
 * @version 1.0.0
 */
@Service
@Scope("prototype")
public class RestCom extends RestTemplate {

	public RestCom() {
		// 設置超時
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setConnectTimeout(1 * 60 * 1000); // 1分鐘
		requestFactory.setReadTimeout(3 * 60 * 1000); // 3分鐘
		setRequestFactory(requestFactory);

		// 設置傳輸格式為 JSON
		getMessageConverters().add(new MappingJackson2HttpMessageConverter());

		// 設置傳輸編碼為 UTF-8
		getMessageConverters().forEach(converter -> {
			if (converter instanceof StringHttpMessageConverter) {
				((StringHttpMessageConverter) converter).setDefaultCharset(StandardCharsets.UTF_8);
			}
		});
	}
}
