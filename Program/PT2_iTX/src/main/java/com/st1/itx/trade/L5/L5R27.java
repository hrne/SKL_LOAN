package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBcm;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.domain.PfBsDetail;
import com.st1.itx.db.domain.PfItDetail;
import com.st1.itx.db.domain.TxControl;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.PfBsDetailService;
import com.st1.itx.db.service.PfItDetailService;
import com.st1.itx.db.service.TxControlService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;

@Service("L5R27")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5R27 extends TradeBuffer {

	@Autowired
	public PfItDetailService sPfItDetailService;

	@Autowired
	public CustMainService sCustMainService;

	@Autowired
	public PfBsDetailService sPfBsDetailService;

	@Autowired
	public CdEmpService sCdEmpService;

	@Autowired
	public CdBcmService sCdBcmService;

	@Autowired
	public FacMainService sFacMainService;

	@Autowired
	public TxControlService txControlService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public CdWorkMonthService cdWorkMonthService;
	@Autowired
	public DateUtil dateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5R27 ");
		this.totaVo.init(titaVo);
		this.info("L5R27 Run");

		int funCode = Integer.valueOf(titaVo.getParam("FunCode"));
		int iCustNo = Integer.valueOf(titaVo.get("CustNo"));
		int iFacmNo = Integer.valueOf(titaVo.get("FacmNo"));
		int iBormNo = Integer.valueOf(titaVo.get("BormNo"));
		long logNo = Long.valueOf(titaVo.getParam("LogNo"));
		PfItDetail pfItDetail = new PfItDetail();

		int perfDateF = titaVo.getEntDyI() + 19110000; // 業績日期(西元)
		// 工作月(西曆)
		CdWorkMonth tCdWorkMonth = cdWorkMonthService.findDateFirst(perfDateF, perfDateF, titaVo);
		if (tCdWorkMonth == null) {
			throw new LogicException(titaVo, "E0001", "CdWorkMonth 放款業績工作月對照檔，業績日期=" + perfDateF); // 查詢資料不存在
		}
		int startDate = tCdWorkMonth.getStartDate();
		// 設定生效日期為工作月止日+1日
		dateUtil.init();
		dateUtil.setDate_1(startDate);
		dateUtil.setDays(-1);
		perfDateF = dateUtil.getCalenderDay() + 19110000;
		tCdWorkMonth = cdWorkMonthService.findDateFirst(perfDateF, perfDateF, titaVo);
		if (tCdWorkMonth == null) {
			throw new LogicException(titaVo, "E0001", "CdWorkMonth 放款業績工作月對照檔，業績日期=" + perfDateF); // 查詢資料不存在
		}
		int workMonth = (tCdWorkMonth.getYear() * 100 + tCdWorkMonth.getMonth()) - 191100;
		if ("L5505".equals(titaVo.getTxcd())) {
			String controlCode = "L5510." + (workMonth + 191100) + ".1";
			TxControl txControl = txControlService.findById(controlCode, titaVo);
			if (txControl == null) {
				throw new LogicException(titaVo, "E0010", "未執行 L5510 保費檢核");
			}
		}

		if (funCode == 1) {

			LoanBorMain tLoanBorMain = loanBorMainService.findById(new LoanBorMainId(iCustNo, iFacmNo, iBormNo),
					titaVo);
			if (tLoanBorMain == null) {
				throw new LogicException(titaVo, "E0001", "撥款資料");
			}
			FacMain tFacMain = sFacMainService.findById(new FacMainId(iCustNo, iFacmNo), titaVo);
			if (tFacMain == null) {
				throw new LogicException(titaVo, "E0001", "額度資料");
			}
			pfItDetail = sPfItDetailService.findBormNoFirst(iCustNo, iFacmNo, iBormNo, titaVo);

			if (pfItDetail == null || pfItDetail.getRepayType() != 0) {
				pfItDetail = new PfItDetail();
				pfItDetail.setCustNo(iCustNo);
				pfItDetail.setFacmNo(iFacmNo);
				pfItDetail.setBormNo(iBormNo);
				pfItDetail.setPieceCode(tLoanBorMain.getPieceCode());
				pfItDetail.setProdCode(tFacMain.getProdNo());
				pfItDetail.setDrawdownAmt(tLoanBorMain.getDrawdownAmt());
				pfItDetail.setDrawdownDate(tLoanBorMain.getDrawdownDate());
				pfItDetail.setIntroducer(tFacMain.getIntroducer());
			} else {
				pfItDetail.setDrawdownAmt(tLoanBorMain.getDrawdownAmt());
				pfItDetail.setDrawdownDate(tLoanBorMain.getDrawdownDate());
				if (pfItDetail.getIntroducer().isEmpty()) {
					pfItDetail.setIntroducer(tFacMain.getIntroducer());
				}
			}
		} else {
			pfItDetail = sPfItDetailService.findById(logNo, titaVo);
			if (pfItDetail == null) {
				throw new LogicException(titaVo, "E0001", "介紹人業績資料");
			}
			// UtilBal = facMain.getUtilBal();

		}
		totaVo.putParam("L5r27CustNo", pfItDetail.getCustNo());
		totaVo.putParam("L5r27FacmNo", pfItDetail.getFacmNo());
		totaVo.putParam("L5r27BormNo", pfItDetail.getBormNo());
		totaVo.putParam("L5r27WorkMonth", pfItDetail.getWorkMonth() > 0 ? pfItDetail.getWorkMonth() - 191100 : 0);

		String CustNm = "";
		CustMain CustMainVo = sCustMainService.custNoFirst(pfItDetail.getCustNo(), pfItDetail.getCustNo(), titaVo);
		if (CustMainVo == null) {
			throw new LogicException(titaVo, "E0001", "客戶資料");
		}
		CustNm = CustMainVo.getCustName();
		totaVo.putParam("L5r27CustNm", CustNm);

		PfBsDetail pfBsItDetail = sPfBsDetailService.findByTxFirst(pfItDetail.getCustNo(), pfItDetail.getFacmNo(),
				pfItDetail.getBormNo(), pfItDetail.getPerfDate(), pfItDetail.getRepayType(), pfItDetail.getPieceCode(),
				titaVo);

		if (pfBsItDetail == null) {
			totaVo.putParam("L5r27BsOfficer", "");
			totaVo.putParam("L5r27BsOfficerName", "");
		} else {
			totaVo.putParam("L5r27BsOfficer", pfBsItDetail.getBsOfficer());
			String BsOfficerName = FindEmpName(pfBsItDetail.getBsOfficer(), titaVo);
			totaVo.putParam("L5r27BsOfficerName", BsOfficerName);
		}

		totaVo.putParam("L5r27Introducer", pfItDetail.getIntroducer());

		String IntroducerName = FindEmpName(pfItDetail.getIntroducer(), titaVo);

		totaVo.putParam("L5r27IntroducerName", IntroducerName);
		// 處經理
		totaVo.putParam("L5r27UnitManager", pfItDetail.getUnitManager());
		String UnitManagerName = FindEmpName(pfItDetail.getUnitManager(), titaVo);
		totaVo.putParam("L5r27UnitManagerName", UnitManagerName);
		// 區經理
		totaVo.putParam("L5r27DistManager", pfItDetail.getDistManager());
		String DistManagerName = FindEmpName(pfItDetail.getDistManager(), titaVo);
		totaVo.putParam("L5r27DistManagerName", DistManagerName);

		totaVo.putParam("L5r27UtilBal", pfItDetail.getDrawdownAmt());
		// 部室
		totaVo.putParam("L5r27DeptCode", pfItDetail.getDeptCode());
		String deptCodeX = FindCdBcm(pfItDetail.getDeptCode(), titaVo);
		totaVo.putParam("L5r27DeptCodeX", deptCodeX);
		// 區部
		totaVo.putParam("L5r27DistCode", pfItDetail.getDistCode());
		String distCodeX = FindCdBcm(pfItDetail.getDistCode(), titaVo);
		totaVo.putParam("L5r27DistCodeX", distCodeX);
		// 單位
		totaVo.putParam("L5r27UnitCode", pfItDetail.getUnitCode());
		String unitCodeX = FindCdBcm(pfItDetail.getUnitCode(), titaVo);
		totaVo.putParam("L5r27UnitCodeX", unitCodeX);
		totaVo.putParam("L5r27AdjPerfEqAmt", pfItDetail.getPerfEqAmt());
		totaVo.putParam("L5r27AdjPerfReward", pfItDetail.getPerfReward());
		totaVo.putParam("L5r27AdjPerfAmt", pfItDetail.getPerfAmt());
		totaVo.putParam("L5r27AdjCntingCode", pfItDetail.getCntingCode());
		totaVo.putParam("L5r27AdjRange", pfItDetail.getAdjRange());
		totaVo.putParam("L5r27AdjLogNo", 0);
		totaVo.putParam("L5r27LastWorkMonth", workMonth);

		this.addList(this.totaVo);
		return this.sendList();
	}

	public String FindEmpName(String employeeNo, TitaVo titaVo) {
		String EmpName = "";
		if (employeeNo != null && employeeNo.length() != 0) {
			CdEmp CdEmpVo = sCdEmpService.findById(employeeNo, titaVo);
			if (CdEmpVo != null) {
				EmpName = CdEmpVo.getFullname();
			}
		}
		return EmpName;
	}

	public String FindCdBcm(String cdBcmUnitCode, TitaVo titaVo) {
		String codeName = "";
		if (cdBcmUnitCode != null && cdBcmUnitCode.length() != 0) {
			CdBcm tCdBcm = sCdBcmService.findById(cdBcmUnitCode, titaVo);
			if (tCdBcm != null) {
				codeName = tCdBcm.getUnitItem();
			}
		}
		return codeName;
	}

}