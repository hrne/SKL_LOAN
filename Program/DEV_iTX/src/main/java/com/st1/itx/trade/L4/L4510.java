
package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.EmpDeductSchedule;
import com.st1.itx.db.service.CdCodeService;
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
	
	@Autowired
	public CdCodeService cdCodeService;
	
	Slice<EmpDeductSchedule> slEmpDeductSchedule = null;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4510 ");
		this.totaVo.init(titaVo);
		Boolean fg = false ;
//		根據輸入之入帳日查詢BORM(ACDATE)->FACM(REPAYCODE:03)寫入empdtl
		mediaDate = parse.stringToInteger(titaVo.get("MediaDate")) + 19110000;
		int iOpItem = parse.stringToInteger(titaVo.getParam("OpItem")); // 作業項目 1.15日薪 2.非15日薪
//		抓取媒體日為今日者
		 slEmpDeductSchedule = empDeductScheduleService.mediaDateRange(mediaDate, mediaDate,
				this.index, this.limit, titaVo);
		 
		if (slEmpDeductSchedule == null) {
			throw new LogicException("E0001", "查無資料");
		} else {
			for (EmpDeductSchedule tEmpDeductSchedule : slEmpDeductSchedule.getContent()) {
			CdCode tCdCode = cdCodeService.getItemFirst(4, "EmpDeductType", tEmpDeductSchedule.getAgType1(),titaVo);
//			  1.15日薪 2.非15日薪
			  if (iOpItem == 1 && ("4".equals(tCdCode.getCode().substring(0, 1)) || "5".equals(tCdCode.getCode().substring(0, 1)))) {
				  fg = true;
			  }
			  if (iOpItem == 2 && !"4".equals(tCdCode.getCode().substring(0, 1)) && !"5".equals(tCdCode.getCode().substring(0, 1))) {
				fg = true;
			  }
			}
		}
		
		if(!fg) {
			throw new LogicException("E0001", "查無資料");
		}
		
		MySpring.newTask("L4510Batch", this.txBuffer, titaVo); 
		this.addList(this.totaVo);
		return this.sendList();
	}
}