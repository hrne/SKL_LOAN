
package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.EmpDeductDtl;
import com.st1.itx.db.domain.EmpDeductSchedule;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.EmpDeductDtlService;
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

	private int iMediaDate = 0;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public EmpDeductScheduleService empDeductScheduleService;

	@Autowired
	public EmpDeductDtlService empDeductDtlService;

	@Autowired
	public CdCodeService cdCodeService;

	Slice<EmpDeductSchedule> slEmpDeductSchedule = null;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4510 ");
		this.totaVo.init(titaVo);
		Boolean fg = false;
//		根據輸入之入帳日查詢BORM(ACDATE)->FACM(REPAYCODE:03)寫入empdtl
		iMediaDate = parse.stringToInteger(titaVo.get("MediaDate")) + 19110000;
		int iOpItem = parse.stringToInteger(titaVo.getParam("OpItem")); // 作業項目 1.15日薪 2.非15日薪
//		抓取媒體日為今日者
		slEmpDeductSchedule = empDeductScheduleService.mediaDateRange(iMediaDate, iMediaDate, this.index, this.limit,
				titaVo);

		if (slEmpDeductSchedule == null) {
			throw new LogicException("E0001", "查無媒體日為今日資料");
		} else {
			for (EmpDeductSchedule tEmpDeductSchedule : slEmpDeductSchedule.getContent()) {
				CdCode tCdCode = cdCodeService.getItemFirst(4, "EmpDeductType", tEmpDeductSchedule.getAgType1(),
						titaVo);
//			  1.15日薪 2.非15日薪
				if (iOpItem == 1 && ("4".equals(tCdCode.getCode().substring(0, 1))
						|| "5".equals(tCdCode.getCode().substring(0, 1)))) {
					fg = true;
				}
				if (iOpItem == 2 && !"4".equals(tCdCode.getCode().substring(0, 1))
						&& !"5".equals(tCdCode.getCode().substring(0, 1))) {
					fg = true;
				}
			}
		}

		if (!fg) {
			throw new LogicException("E0001", "查無媒體日為今日資料");
		}

		Slice<EmpDeductDtl> slEmpDeductDtl = empDeductDtlService.mediaDateRng(iMediaDate, iOpItem == 1 ? "4" : "5", 0,
				Integer.MAX_VALUE, titaVo);
		List<EmpDeductDtl> lEmpDeductDtl = new ArrayList<EmpDeductDtl>();
		lEmpDeductDtl = slEmpDeductDtl == null ? null : slEmpDeductDtl.getContent();

		if (lEmpDeductDtl != null) {
			for (EmpDeductDtl tEmpDeductDtl : lEmpDeductDtl) {
				if (tEmpDeductDtl.getMediaSeq() > 0) {
					throw new LogicException(titaVo, "E0015", "已執行L4511-產生媒體檔，須訂正L4511"); // 檢查錯誤
				}
			}
			try {
				empDeductDtlService.deleteAll(lEmpDeductDtl, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0008", "員工扣薪檔刪除失敗 :" + e.getErrorMsg());
			}
		}
		if (titaVo.isHcodeNormal()) {
			MySpring.newTask("L4510Batch", this.txBuffer, titaVo);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

}