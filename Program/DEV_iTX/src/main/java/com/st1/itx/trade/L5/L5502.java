package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.PfBsDetail;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.PfBsDetailService;
import com.st1.itx.db.service.PfBsOfficerService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * PerfDate=9,7<br>
 * CustNo=9,7<br>
 * FacmNo=9,3<br>
 * BormNo=9,3<br>
 * AdjPerfCnt=9,2.1<br>
 * AdjPerfAmt=9,14.2<br>
 */

@Service("L5502")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5502 extends TradeBuffer {
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	@Autowired
	public DataLog dataLog;

	@Autowired
	public PfBsDetailService sPfBsDetailService;
	@Autowired
	public PfBsOfficerService sPfBsOfficerService;

	@Autowired
	public CdEmpService sCdEmpService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("Run L5502");
		this.info("active L5502 ");
		this.totaVo.init(titaVo);
		Long iLogNo = Long.valueOf(titaVo.get("LogNo").trim());
		String PerfDate = titaVo.getParam("PerfDate").trim();// 業績日期
		String CustNo = titaVo.getParam("CustNo").trim();// 戶號
		String FacmNo = titaVo.getParam("FacmNo").trim();// 額度編號
		String BormNo = titaVo.getParam("BormNo").trim();// 撥款序號
		titaVo.getParam("BsOfficer").trim();
		String AdjBsOfficer = titaVo.getParam("AdjBsOfficer").trim();// 調整後房貸專員
		String AdjPerfCnt = titaVo.getParam("AdjPerfCnt").trim();// 調整後件數
		String AdjPerfAmt = titaVo.getParam("AdjPerfAmt").trim();// 調整後金額

		int IntPerfDate = 0;
		if (PerfDate != null && PerfDate.length() != 0) {
			IntPerfDate = Integer.parseInt(PerfDate);
			if (IntPerfDate != 0 && String.valueOf(IntPerfDate).length() <= 7) {
				IntPerfDate = IntPerfDate + 19110000;
			}
		} else {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", "[業績日期]未填寫");
		}
		// "CdEmp" E0 ON E0."EmployeeNo" = B."BsOfficer" --AdjIntroducer
		// 查驗房貸專員是否在 員工資料檔案(CdEmp)內 --不確定 額度主黨(FacMain)
		if (AdjBsOfficer != null) {
			CdEmp CdEmpVO = sCdEmpService.findById(AdjBsOfficer, titaVo);
			if (CdEmpVO != null) {

			} else {
				throw new LogicException(titaVo, "E0006", "[房貸專員]查無此員工編號");
			}
		}
		PfBsDetail pfBDsetail = sPfBsDetailService.holdById(iLogNo, titaVo);
		PfBsDetail oPfBsDetail = (PfBsDetail) dataLog.clone(pfBDsetail);// 資料異動前

		if (pfBDsetail != null) {
			// 放行 1:登 2:放
			int ActFg = titaVo.getActFgI();
			if (ActFg == 2) {
			} else if (ActFg == 2) {

				pfBDsetail.setBsOfficer(AdjBsOfficer);

				pfBDsetail.setPerfCnt(new BigDecimal(AdjPerfCnt));

				pfBDsetail.setPerfAmt(new BigDecimal(AdjPerfAmt));

				try {
					pfBDsetail = sPfBsDetailService.update2(pfBDsetail, titaVo);
					dataLog.setEnv(titaVo, oPfBsDetail, pfBDsetail);// 資料異動後-2
					dataLog.exec();// 資料異動後-3
				} catch (DBException e) {
					// E0007 更新資料時，發生錯誤
					throw new LogicException(titaVo, "E0007", "");
				}
			}
		} else {
			// E0006 鎖定資料時，發生錯誤
			throw new LogicException(titaVo, "E0006", "");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}