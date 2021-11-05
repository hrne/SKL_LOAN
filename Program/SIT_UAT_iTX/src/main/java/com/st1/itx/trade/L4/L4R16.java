package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;

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
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4R16")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4R16 extends TradeBuffer {

	@Autowired
	public Parse parse;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public EmpDeductScheduleService empDeductScheduleService;

	@Autowired
	public CdCodeService cdCodeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R16 ");
		this.totaVo.init(titaVo);

//		L4510進交易顯示流程別1~9對應之媒體日&入帳日
//		媒體日 == 當日者顯示,else隱藏+跳過
//		若要回頭或提前產製，於L4500修改日期

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200;

		int rimToday = parse.stringToInteger(titaVo.getParam("RimToday")) + 19110000;
		int iOpItem = parse.stringToInteger(titaVo.getParam("OpItem")); // 作業項目 1.15日薪 2.非15日薪
//		抓取媒體日為今日者
		List<EmpDeductSchedule> lEmpDeductSchedule = new ArrayList<EmpDeductSchedule>();
		Slice<EmpDeductSchedule> slEmpDeductSchedule = empDeductScheduleService.mediaDateRange(rimToday, rimToday,
				this.index, this.limit, titaVo);
		if (slEmpDeductSchedule != null) {
			for (EmpDeductSchedule tEmpDeductSchedule : slEmpDeductSchedule.getContent()) {
				CdCode tCdCode = cdCodeService.getItemFirst(4, "EmpDeductType", tEmpDeductSchedule.getAgType1(),
						titaVo);
				if (iOpItem == 1 && "1".equals(tCdCode.getItem().substring(0, 1))) {
					lEmpDeductSchedule.add(tEmpDeductSchedule);
				}
				if (iOpItem == 2 && "2".equals(tCdCode.getItem().substring(0, 1))) {
					lEmpDeductSchedule.add(tEmpDeductSchedule);
				}
			}
		}
		if (lEmpDeductSchedule.size() > 0) {
			for (int j = 1; j <= 9; j++) {
				boolean flag = false;
				for (EmpDeductSchedule tEmpDeductSchedule : lEmpDeductSchedule) {
					this.info("tEmpDeductSchedule.getAgType1() : " + tEmpDeductSchedule.getAgType1());
					int i = parse.stringToInteger(tEmpDeductSchedule.getAgType1());

					if (i == j) {
						flag = true;
						this.totaVo.putParam("L4r16WorkMonth" + i, tEmpDeductSchedule.getWorkMonth() - 191100);
						this.totaVo.putParam("L4r16MediaDate" + i, tEmpDeductSchedule.getMediaDate());
						this.totaVo.putParam("L4r16EntryDate" + i, tEmpDeductSchedule.getEntryDate());
						break;
					}
				}
				if (!flag) {
					this.totaVo.putParam("L4r16WorkMonth" + j, 0);
					this.totaVo.putParam("L4r16MediaDate" + j, 0);
					this.totaVo.putParam("L4r16EntryDate" + j, 0);
				}
			}
		} else {
			for (int j = 1; j <= 9; j++) {
				this.totaVo.putParam("L4r16WorkMonth" + j, 0);
				this.totaVo.putParam("L4r16MediaDate" + j, 0);
				this.totaVo.putParam("L4r16EntryDate" + j, 0);
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}