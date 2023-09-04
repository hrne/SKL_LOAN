package com.st1.itx.trade.scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.common.CheckCA;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.log.SysLogger;

@Component
@Transactional(value = "transactionManager")
public class ScheduledCheckCa extends SysLogger {

	@Autowired
	TxBuffer txBuffer;

	/* 日期工具 */
	@Autowired
	private DateUtil dateUtil;

	@Autowired
	private CheckCA checkCA;

	private TitaVo titaVo = new TitaVo();

	/*
	 * 秒（0~59） 分鐘（0~59） 小時（0~23） 天（月）（0~31，但是你需要考慮你月的天數） 月（0~11） 星期（1~7 1=SUN 或
	 * SUN，MON，TUE，WED，THU，FRI，SAT）
	 */
	@Scheduled(cron = "0 30 08 * * ?")
	public void doCheckCa() {
		this.mustInfo("Active ScheduledCheckCa doCheckCa Every Day 08:30 ");
		try {
			boolean isHoliDay = this.init();
			if (!isHoliDay) {
				titaVo.putParam(ContentName.empnot, "999999");
				checkCA.pfxExpiration(titaVo);
			}
		} catch (LogicException e) {
			this.error(e.getMessage());
		}
	}

	public boolean init() throws LogicException {
		this.info("doCheckCa 每日 08:30 啟動...");

		titaVo.init();

		titaVo.putParam(ContentName.actfg, "0");
		titaVo.putParam(ContentName.dataBase, ContentName.onLine);

		txBuffer.init(titaVo);

		dateUtil.init();

//		以日曆日抓取假日檔判斷是否假日，非假日才執行
		dateUtil.setDate_1(dateUtil.getNowIntegerForBC());
		dateUtil.setDate_2(dateUtil.getNowIntegerForBC());

		this.info("dateUtil.getNowIntegerForBC()" + dateUtil.getNowIntegerForBC());
		this.info("dateUtil.isHoliDay()" + dateUtil.isHoliDay());

		return dateUtil.isHoliDay();
	}
}
