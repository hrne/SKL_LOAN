
package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.EmpDeductSchedule;
import com.st1.itx.db.service.EmpDeductScheduleService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.parse.Parse;

@Service("L4510")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4510 extends TradeBuffer {

	private int mediaDate = 0;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	
	@Autowired
	public EmpDeductScheduleService empDeductScheduleService;
	
	Slice<EmpDeductSchedule> slEmpDeductSchedule = null;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4510 ");
		this.totaVo.init(titaVo);

//		根據輸入之入帳日查詢BORM(ACDATE)->FACM(REPAYCODE:03)寫入empdtl
		mediaDate = parse.stringToInteger(titaVo.get("MediaDate")) + 19110000;

//		抓取媒體日為今日者
		 slEmpDeductSchedule = empDeductScheduleService.mediaDateRange(mediaDate, mediaDate,
				this.index, this.limit, titaVo);
		if (slEmpDeductSchedule == null) {
			throw new LogicException("E0001", "查無資料");
		} 
		MySpring.newTask("L4510p", this.txBuffer, titaVo); 
		this.addList(this.totaVo);
		return this.sendList();
	}
}