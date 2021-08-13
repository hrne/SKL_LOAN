package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.EmpDeductSchedule;
import com.st1.itx.db.domain.EmpDeductScheduleId;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.EmpDeductScheduleService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4500")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */

public class L4500 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L4500.class);

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public CdCodeService cdCodeService;

	@Autowired
	public EmpDeductScheduleService empDeductScheduleService;

	int empDeductYear = 0;
	String agType1 = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4500 ");
		this.totaVo.init(titaVo);

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		empDeductYear = parse.stringToInteger(titaVo.getParam("EmpDeductYear")) + 1911;
		agType1 = titaVo.getParam("AgType1");

//		刪除
		dele(titaVo);

//		每次直接update所有grid內容
//		1.抓取cdCode，EmpDeductType與ApType1之對應(15/非15)
//		2.根據EmpDeductType決定insert之Grid

//		分兩個grid維護
//		15日薪為13個工作月
//		非15為12個工作月
		CdCode tCdCode = cdCodeService.getItemFirst(4, "EmpDeductType", agType1, titaVo);

//		2.非15日薪A
		if ("2".equals(tCdCode.getItem().substring(0, 1))) {
//			非15日薪
			for (int i = 1; i <= 13; i++) {
				EmpDeductSchedule tEmpDeductSchedule = new EmpDeductSchedule();
				EmpDeductScheduleId tEmpDeductScheduleId = new EmpDeductScheduleId();
				tEmpDeductScheduleId.setWorkMonth(parse.stringToInteger(empDeductYear + parse.IntegerToString(i, 2)));
				tEmpDeductScheduleId.setAgType1(agType1);

				tEmpDeductSchedule.setEntryDate(parse.stringToInteger(titaVo.getParam("EntryDateA" + i)));
				tEmpDeductSchedule.setMediaDate(parse.stringToInteger(titaVo.getParam("MediaDateA" + i)));

				tEmpDeductSchedule.setEmpDeductScheduleId(tEmpDeductScheduleId);

				try {
					empDeductScheduleService.insert(tEmpDeductSchedule, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "EmpDeductSchedule insert error : " + e.getErrorMsg());
				}
			}
		} else {
			for (int i = 1; i <= 12; i++) {
				EmpDeductSchedule tEmpDeductSchedule = new EmpDeductSchedule();
				EmpDeductScheduleId tEmpDeductScheduleId = new EmpDeductScheduleId();
				tEmpDeductScheduleId.setWorkMonth(parse.stringToInteger(empDeductYear + parse.IntegerToString(i, 2)));
				tEmpDeductScheduleId.setAgType1(agType1);

				tEmpDeductSchedule.setEntryDate(parse.stringToInteger(titaVo.getParam("EntryDateB" + i)));
				tEmpDeductSchedule.setMediaDate(parse.stringToInteger(titaVo.getParam("MediaDateB" + i)));

				tEmpDeductSchedule.setEmpDeductScheduleId(tEmpDeductScheduleId);

				try {
					empDeductScheduleService.insert(tEmpDeductSchedule, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "EmpDeductSchedule insert error : " + e.getErrorMsg());
				}
			}

		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void dele(TitaVo titaVo) throws LogicException {
		int empDeductM1 = parse.stringToInteger(empDeductYear + "00");
		int empDeductM2 = parse.stringToInteger(empDeductYear + "99");

		Slice<EmpDeductSchedule> sEmpDeductSchedule = null;
		List<EmpDeductSchedule> lEmpDeductSchedule = new ArrayList<EmpDeductSchedule>();

		sEmpDeductSchedule = empDeductScheduleService.findL4R15B(empDeductM1, empDeductM2, agType1, this.index, this.limit, titaVo);
		lEmpDeductSchedule = sEmpDeductSchedule == null ? null : sEmpDeductSchedule.getContent();

		if (lEmpDeductSchedule != null && lEmpDeductSchedule.size() != 0) {
			for (EmpDeductSchedule tEmpDeductSchedule : lEmpDeductSchedule) {

				EmpDeductSchedule deleEmpDeductSchedule = empDeductScheduleService.holdById(tEmpDeductSchedule.getEmpDeductScheduleId(), titaVo);
				try {
					empDeductScheduleService.delete(deleEmpDeductSchedule, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "EmpDeductSchedule delete error : " + e.getErrorMsg());
				}
			}
		}
	}
}