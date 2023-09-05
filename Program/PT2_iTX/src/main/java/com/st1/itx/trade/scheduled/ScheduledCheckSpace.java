package com.st1.itx.trade.scheduled;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.log.SysLogger;

@Component
@Transactional(value = "transactionManager")
public class ScheduledCheckSpace extends SysLogger {

	@Autowired
	TxBuffer txBuffer;

	/* 日期工具 */
	@Autowired
	private DateUtil dateUtil;

	@Autowired
	private WebClient webClient;

	@Autowired
	private FileCom fileCom;

	private TitaVo titaVo = new TitaVo();

	private List<String> dataLineList = new ArrayList<>();

	/*
	 * 秒（0~59） 分鐘（0~59） 小時（0~23） 天（月）（0~31，但是你需要考慮你月的天數） 月（0~11） 星期（1~7 1=SUN 或
	 * SUN，MON，TUE，WED，THU，FRI，SAT）
	 */
	@Scheduled(cron = "0 30 08 * * ?")
	public void checkSpace() {
		this.mustInfo("Active ScheduledCheckSpace checkSpace Every Day 08:30 ");

		init();

		// 清空跑馬燈
		webClient.sendTicker("0000", "CHECK_SPACE", "000000000000", "", true, titaVo);

		// 讀檔
		readCheckSpaceTxt();

		// 取得使用量百分比
		BigDecimal usedSpace = getPercentage();

		// 判斷是否超過80%,若超過,發跑馬燈
		sendTickerIfUsedSpaceOver80(usedSpace);
	}

	private void init() {
		titaVo.init();

		titaVo.putParam(ContentName.actfg, "0");
		titaVo.putParam(ContentName.dataBase, ContentName.onLine);
		titaVo.putParam(ContentName.empnot, "999999");

		try {
			txBuffer.init(titaVo);
		} catch (LogicException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.mustInfo(errors.toString());
		}
	}

	private void readCheckSpaceTxt() {
		dataLineList = new ArrayList<>();
		try {
			dataLineList = fileCom.intputTxt("/home/weblogic/checkSpace.txt", "UTF-8");
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.mustInfo(errors.toString());
			dataLineList = new ArrayList<>();
		}
	}

	private BigDecimal getPercentage() {
		BigDecimal usedSpace = new BigDecimal("81.00");
		if (dataLineList != null && !dataLineList.isEmpty()) {
			String usedSpaceTxt = dataLineList.get(0);
			this.mustInfo("usedSpaceTxt=" + usedSpaceTxt);
			if (usedSpaceTxt.contains(":")) {
				String[] s = usedSpaceTxt.split(":");
				this.mustInfo("s[0]=" + s[0]);
				this.mustInfo("s[1]=" + s[1]);
				String s1 = s[1].trim();
				s1 = s1.substring(0, s1.lastIndexOf("%"));
				try {
					usedSpace = new BigDecimal(s1);
				} catch (Exception e) {
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					this.mustInfo(errors.toString());
					usedSpace = new BigDecimal("81.00");
				}
			}
		}
		return usedSpace;
	}

	private void sendTickerIfUsedSpaceOver80(BigDecimal usedSpace) {
		if (usedSpace.compareTo(new BigDecimal("80.00")) > 0) {
			try {
				webClient.sendTicker("0000", "CHECK_SPACE", "" + dateUtil.getCalenderDay() + "0000", "硬碟已使用容量已超過80%",
						false, titaVo);
			} catch (LogicException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.mustInfo(errors.toString());
			}
		}
	}
}
