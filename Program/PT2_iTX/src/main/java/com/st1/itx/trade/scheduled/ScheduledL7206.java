package com.st1.itx.trade.scheduled;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.log.SysLogger;

@Component
@Transactional(value = "transactionManager")
public class ScheduledL7206 extends SysLogger {

	@Autowired
	private TxBuffer txBuffer;

	@Autowired
	private DateUtil dateUtil;

	private TitaVo titaVo = new TitaVo();

	/*
	 * 秒（0~59） 分鐘（0~59） 小時（0~23） 天（月）（0~31，但是你需要考慮你月的天數） 月（0~11） 星期（1~7 1=SUN 或
	 * SUN，MON，TUE，WED，THU，FRI，SAT）
	 */
	@Scheduled(cron = "0 10 12 * * ?")
	public void callL7206() {
		this.mustInfo("Active ScheduledL7206 callL7206 Every Day 12:10 ");
		try {
			boolean isHoliDay = init();
			if (!isHoliDay) {
				MySpring.newTask("L7206CustBatch", txBuffer, titaVo);
				MySpring.newTask("L7206ManagerBatch", txBuffer, titaVo);
				MySpring.newTask("L7206EmpBatch", txBuffer, titaVo);
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.mustInfo("ERROR " + errors.toString());
		}
		this.mustInfo("Active ScheduledL7206 callL7206 finished. ");
	}

	public boolean init() throws LogicException {
		titaVo.init();
		titaVo.putParam(ContentName.actfg, "0");
		titaVo.putParam(ContentName.dataBase, ContentName.onLine);
		titaVo.putParam(ContentName.empnot, "999999");
		titaVo.putParam(ContentName.kinbr, "0000");
		titaVo.setDataBaseOnLine();

		this.mustInfo("titaVo.getBrno()=" + titaVo.getBrno());
		
		txBuffer.init(titaVo);

		dateUtil.init();

//		以日曆日抓取假日檔判斷是否假日，非假日才執行
		dateUtil.setDate_1(dateUtil.getNowIntegerForBC());
		dateUtil.setDate_2(dateUtil.getNowIntegerForBC());

		this.mustInfo("dateUtil.getNowIntegerForBC()" + dateUtil.getNowIntegerForBC());
		this.mustInfo("dateUtil.isHoliDay()" + dateUtil.isHoliDay());

		return dateUtil.isHoliDay();
	}
}
