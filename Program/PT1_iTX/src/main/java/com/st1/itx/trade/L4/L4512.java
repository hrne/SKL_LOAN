package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.EmpDeductDtl;
import com.st1.itx.db.domain.EmpDeductDtlId;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.EmpDeductDtlService;
import com.st1.itx.db.service.EmpDeductMediaService;
import com.st1.itx.db.service.EmpDeductScheduleService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4512")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4512 extends TradeBuffer {

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public EmpDeductMediaService empDeductMediaService;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public FacMainService facMainService;

	@Autowired
	public CdEmpService cdEmpService;

	@Autowired
	public EmpDeductScheduleService empDeductScheduleService;

	@Autowired
	public EmpDeductDtlService empDeductDtlService;

	@Autowired
	public CdCodeService cdCodeService;

	private int iFunctionCode;
	private int iEntryDate;
	private int iPerfMonth;
	private String iProcCode;
	private String iAcctCode;
	private String iRepayCode;
	private int iAchRepayCode;
	private int iCustNo;
	private int iFacmNo;
	private int iBormNo;

	private String iMediaKind;


	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4512 ");
		this.totaVo.init(titaVo);

		iFunctionCode = parse.stringToInteger(titaVo.getParam("FunctionCode"));
		iEntryDate = parse.stringToInteger(titaVo.getParam("EntryDate"));
		iPerfMonth = parse.stringToInteger(titaVo.getParam("PerfMonth")) + 191100;
		iProcCode = titaVo.getParam("ProcCode");
		iAcctCode = titaVo.getParam("AcctCode");
		iRepayCode = titaVo.getParam("RepayCode");
		iAchRepayCode = parse.stringToInteger(titaVo.getParam("AchRepayCode"));
		iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		iBormNo = parse.stringToInteger(titaVo.getParam("BormNo"));
		
		CustMain tCustMain = custMainService.custNoFirst(iCustNo, iCustNo, titaVo);
		if (tCustMain == null) {
			throw new LogicException("E0001", "CustMain");
		}
		
		// 15日薪放5  非15日薪放1
		String AgType1 = "";
		
		switch(iProcCode) {
		case "4":
		case "5":
			AgType1 = "5";
			break;
		case "1":
		case "2":
		case "3":
		case "6":
		case "7":
		case "8":
		case "9":
			AgType1 = "1";
			break;
		default:
			break;
			
		}
		
		CdCode tCdCode = cdCodeService.getItemFirst(4, "EmpDeductType", AgType1, titaVo);
//			4.15日薪 5.非15日薪 CdCode 4 5 15日薪
		if ("4".equals(tCdCode.getCode().substring(0, 1)) || "5".equals(tCdCode.getCode().substring(0, 1))) {
			iMediaKind = "4";
		} else {
			iMediaKind = "5";
		}
		
		switch (iFunctionCode) {
		case 1:

			// 新增明細檔
			maintainEmpDeductDtl(1, tCustMain,  titaVo);
			break;
		case 2:

			// 修改明細檔
			maintainEmpDeductDtl(2, tCustMain,  titaVo);
			
			break;
		case 4:
			// 刪除明細檔
			maintainEmpDeductDtl(4, tCustMain,  titaVo);
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();

	}

	private void maintainEmpDeductDtl(int FunctionCode, CustMain tCustMain, TitaVo titaVo)
			throws LogicException {
		
		if(FunctionCode == 1) {  // 新增
			
			EmpDeductDtl tEmpDeductDtl = new EmpDeductDtl();
			EmpDeductDtlId tEmpDeductDtlId = new EmpDeductDtlId();
			tEmpDeductDtlId.setEntryDate(iEntryDate);
			tEmpDeductDtlId.setPerfMonth(iPerfMonth);
			tEmpDeductDtlId.setProcCode(iProcCode);
			tEmpDeductDtlId.setAcctCode(iAcctCode);
			tEmpDeductDtlId.setRepayCode(iRepayCode);
			tEmpDeductDtlId.setAchRepayCode(iAchRepayCode);
			tEmpDeductDtlId.setCustNo(iCustNo);
			tEmpDeductDtlId.setFacmNo(iFacmNo);
			tEmpDeductDtlId.setBormNo(iBormNo);
			tEmpDeductDtl.setEmpDeductDtlId(tEmpDeductDtlId);
			
			tEmpDeductDtl.setEmpNo(tCustMain.getEmpNo());
			tEmpDeductDtl.setCustId(tCustMain.getCustId());
			tEmpDeductDtl.setRepayAmt(parse.stringToBigDecimal(titaVo.get("RepayAmt")));
			tEmpDeductDtl.setPrincipal(parse.stringToBigDecimal(titaVo.get("Principal")));
			tEmpDeductDtl.setInterest(parse.stringToBigDecimal(titaVo.get("Interest")));
			tEmpDeductDtl.setSumOvpayAmt(parse.stringToBigDecimal(titaVo.get("SumOvpayAmt")));
			tEmpDeductDtl.setMediaDate(0);
			tEmpDeductDtl.setMediaKind(iMediaKind);
			
			try {
				empDeductDtlService.insert(tEmpDeductDtl, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "員工扣薪明細檔新增失敗 :" + e.getErrorMsg());
			}
		} else if(FunctionCode == 2){
			
			EmpDeductDtl tEmpDeductDtl = new EmpDeductDtl();
			EmpDeductDtlId tEmpDeductDtlId = new EmpDeductDtlId();
			tEmpDeductDtlId.setEntryDate(iEntryDate);
			tEmpDeductDtlId.setPerfMonth(iPerfMonth);
			tEmpDeductDtlId.setProcCode(iProcCode);
			tEmpDeductDtlId.setAcctCode(iAcctCode);
			tEmpDeductDtlId.setRepayCode(iRepayCode);
			tEmpDeductDtlId.setAchRepayCode(iAchRepayCode);
			tEmpDeductDtlId.setCustNo(iCustNo);
			tEmpDeductDtlId.setFacmNo(iFacmNo);
			tEmpDeductDtlId.setBormNo(iBormNo);
			tEmpDeductDtl.setEmpDeductDtlId(tEmpDeductDtlId);
			
			tEmpDeductDtl = empDeductDtlService.holdById(tEmpDeductDtlId, titaVo);
			
			if(tEmpDeductDtl == null ) {
				
			}
			tEmpDeductDtl.setRepayAmt(parse.stringToBigDecimal(titaVo.get("RepayAmt")));
			tEmpDeductDtl.setPrincipal(parse.stringToBigDecimal(titaVo.get("Principal")));
			tEmpDeductDtl.setInterest(parse.stringToBigDecimal(titaVo.get("Interest")));
			tEmpDeductDtl.setSumOvpayAmt(parse.stringToBigDecimal(titaVo.get("SumOvpayAmt")));
			
			try {
				empDeductDtlService.update(tEmpDeductDtl, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0007", "員工扣薪明細檔修改失敗 : " + e.getErrorMsg());
			}
			
		} else if (FunctionCode == 4) {
			
			EmpDeductDtl tEmpDeductDtl = new EmpDeductDtl();
			EmpDeductDtlId tEmpDeductDtlId = new EmpDeductDtlId();
			tEmpDeductDtlId.setEntryDate(iEntryDate);
			tEmpDeductDtlId.setPerfMonth(iPerfMonth);
			tEmpDeductDtlId.setProcCode(iProcCode);
			tEmpDeductDtlId.setAcctCode(iAcctCode);
			tEmpDeductDtlId.setRepayCode(iRepayCode);
			tEmpDeductDtlId.setAchRepayCode(iAchRepayCode);
			tEmpDeductDtlId.setCustNo(iCustNo);
			tEmpDeductDtlId.setFacmNo(iFacmNo);
			tEmpDeductDtlId.setBormNo(iBormNo);
			tEmpDeductDtl.setEmpDeductDtlId(tEmpDeductDtlId);
			
			tEmpDeductDtl = empDeductDtlService.holdById(tEmpDeductDtlId, titaVo);
			
			try {
				empDeductDtlService.delete(tEmpDeductDtl, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0008", "員工扣薪明細檔刪除失敗 : " + e.getErrorMsg());
			}
		}
	}

	
}