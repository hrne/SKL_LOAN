package com.st1.itx.trade.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.st1.itx.buffer.TxBuffer;

@Component
public class ScheduledSample {
	private static final Logger logger = LoggerFactory.getLogger(ScheduledSample.class);

	@Autowired
	public TxBuffer txBuffer;

	/*
	 * 秒（0~59） 分鐘（0~59） 小時（0~23） 天（月）（0~31，但是你需要考慮你月的天數） 月（0~11） 星期（1~7 1=SUN 或
	 * SUN，MON，TUE，WED，THU，FRI，SAT）
	 */
//	@Scheduled(cron = "0/20 * * * * ?")
	public void ex() {
		logger.info("定時啟動 每20秒....");

	}
}
