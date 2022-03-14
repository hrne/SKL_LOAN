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

@Service("L4R15")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4R15 extends TradeBuffer {

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
		this.info("active L4R15 ");
		this.totaVo.init(titaVo);

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200;

		int empDeductM1 = parse.stringToInteger((1911 + parse.stringToInteger(titaVo.getParam("RimYear"))) + "00");
		int empDeductM2 = parse.stringToInteger((1911 + parse.stringToInteger(titaVo.getParam("RimYear"))) + "99");
		String agType1 = titaVo.getParam("RimAgType1");

		Slice<EmpDeductSchedule> sEmpDeductSchedule = null;
		List<EmpDeductSchedule> lEmpDeductSchedule = new ArrayList<EmpDeductSchedule>();

		sEmpDeductSchedule = empDeductScheduleService.findL4R15B(empDeductM1, empDeductM2, agType1, this.index, this.limit, titaVo);

		lEmpDeductSchedule = sEmpDeductSchedule == null ? null : sEmpDeductSchedule.getContent();

		CdCode tCdCode = cdCodeService.getItemFirst(4, "EmpDeductType", agType1, titaVo);

		this.totaVo.putParam("L4r15EmpDeductFlag", tCdCode.getCode().substring(0, 1));

		this.info("EmpDeductFlag: " + tCdCode.getItem().substring(0, 1));

		if (lEmpDeductSchedule != null && lEmpDeductSchedule.size() != 0) {
			int cntA = 0;
			int cntB = 0;

//			15日薪補0
			if ("1".equals(tCdCode.getItem().substring(0, 1))) {
				this.totaVo.putParam("L4r15EntryDateA13", 0);
				this.totaVo.putParam("L4r15MediaDateA13", 0);
				this.totaVo.putParam("L4r15RepayEndDateA13", 0);
			}

			for (EmpDeductSchedule tEmpDeductSchedule : lEmpDeductSchedule) {
//				EntryDateA
//				MediaDateA
//				EntryDateB
//				MediaDateB
				cntA = cntA + 1;
				cntB = cntB + 1;

				int i = parse.stringToInteger((tEmpDeductSchedule.getWorkMonth() + "").substring(4, 6));

				this.info("A.i: " + i);

				if (i <= 13) {
					this.totaVo.putParam("L4r15EntryDateA" + i, tEmpDeductSchedule.getEntryDate());
					this.totaVo.putParam("L4r15MediaDateA" + i, tEmpDeductSchedule.getMediaDate());
					this.totaVo.putParam("L4r15RepayEndDateA" + i, tEmpDeductSchedule.getRepayEndDate());
				} else {
					this.info(i + " continue... ");
					continue;
				}

				if (i <= 12) {
					this.totaVo.putParam("L4r15EntryDateB" + i, tEmpDeductSchedule.getEntryDate());
					this.totaVo.putParam("L4r15MediaDateB" + i, tEmpDeductSchedule.getMediaDate());
					this.totaVo.putParam("L4r15RepayEndDateB" + i, tEmpDeductSchedule.getRepayEndDate());
				} else {
					this.info(i + " continue... ");
					continue;
				}
			}
		} else {
			this.info("not found...");

			for (int i = 1; i <= 13; i++) {
				this.totaVo.putParam("L4r15EntryDateA" + i, 0);
				this.totaVo.putParam("L4r15MediaDateA" + i, 0);
				this.totaVo.putParam("L4r15RepayEndDateA" + i, 0);
				if (i <= 12) {
					this.totaVo.putParam("L4r15EntryDateB" + i, 0);
					this.totaVo.putParam("L4r15MediaDateB" + i, 0);
					this.totaVo.putParam("L4r15RepayEndDateB" + i, 0);
				}
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}