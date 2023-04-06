package com.st1.itx.trade.scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.SystemParasService;
import com.st1.itx.db.service.TxFileService;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.FtpClient;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.log.SysLogger;
import com.st1.itx.util.mail.MailService;
import com.st1.itx.util.parse.Parse;

@Component
@Transactional(value = "transactionManager")
public class ScheduledL5500 extends SysLogger {

	@Autowired
	TxBuffer txBuffer;

	/* 轉型共用工具 */
	@Autowired
	Parse parse;

	/* 日期工具 */
	@Autowired
	DateUtil dateUtil;

	@Autowired
	TxToDoDetailService txToDoDetailService;

	@Autowired
	MakeFile makeFile;

	@Autowired
	MakeReport makeReport;

	@Autowired
	TxToDoCom txToDoCom;

	@Autowired
	TxFileService txFileService;

	@Autowired
	SystemParasService systemParasService;

	@Autowired
	FtpClient ftpClient;

	@Value("${iTXOutFolder}")
	private String outFolder = "";

	@Autowired
	MailService mailService;

	private TitaVo titaVo = new TitaVo();

//	ScheduledL5500

	/*
	 * 秒（0~59） 分鐘（0~59） 小時（0~23） 天（月）（0~31，但是你需要考慮你月的天數） 月（0~11） 星期（1~7 1=SUN 或
	 * SUN，MON，TUE，WED，THU，FRI，SAT）
	 */
	@Scheduled(cron = "0 30 15 * * ?")

	public void tt() {
		try {
			boolean isHoliDay = this.init();
			if (!isHoliDay) {
				MySpring.newTask("L5500", this.txBuffer, titaVo);
			}
		} catch (LogicException e) {
			this.error(e.getMessage());
		}
	}

	public boolean init() throws LogicException {
		this.info("每日 15:30 啟動...");
//		this.info("每30 秒啟動...");

//		預設tita Lable欄位為空值 & DB指定為online
		titaVo.init();

//		TxToDoCom need
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
