package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.EmpDeductDtl;
import com.st1.itx.db.domain.EmpDeductDtlId;
import com.st1.itx.db.domain.EmpDeductMedia;
import com.st1.itx.db.domain.EmpDeductMediaId;
import com.st1.itx.db.domain.EmpDeductSchedule;
import com.st1.itx.db.domain.EmpDeductScheduleId;
import com.st1.itx.db.domain.FacMain;
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

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4512 ");
		this.totaVo.init(titaVo);

//		2.媒體維護
		int iFunctionCode = parse.stringToInteger(titaVo.getParam("FunctionCode"));

		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));

		int iRepayCode = parse.stringToInteger(titaVo.getParam("RepayCode"));

		int iPerfMonth = parse.stringToInteger(titaVo.getParam("PerfMonth")) + 191100;

		int iPerfRepayCode = parse.stringToInteger(titaVo.getParam("PerfRepayCode"));

		BigDecimal iRepayAmt = parse.stringToBigDecimal(titaVo.getParam("RepayAmt"));

		int iMediaDate = parse.stringToInteger(titaVo.getParam("MediaDate")) ;

		String iMediaKind = titaVo.getParam("MediaKind");

		int iMediaSeq = parse.stringToInteger(titaVo.getParam("MediaSeq"));

		EmpDeductMedia tEmpDeductMedia = new EmpDeductMedia();
		EmpDeductMediaId tEmpDeductMediaId = new EmpDeductMediaId();
		switch (iFunctionCode) {
		case 1:
			CustMain tCustMain = custMainService.custNoFirst(iCustNo, iCustNo, titaVo);
			if (tCustMain == null) {
				throw new LogicException("E0001", "CustMain");
			}
			CdEmp tCdEmp = cdEmpService.findById(tCustMain.getEmpNo(), titaVo);
			if (tCdEmp == null) {
				throw new LogicException("E0001", "員工檔");
			}
			EmpDeductSchedule tEmpDeductSchedule = empDeductScheduleService.findById(new EmpDeductScheduleId(iPerfMonth, tCdEmp.getAgType1()), titaVo);
			if (tEmpDeductSchedule == null) {
				throw new LogicException("E0001", "員工扣薪日程表");
			}
			if (tEmpDeductSchedule.getMediaDate() != iMediaDate) {
				throw new LogicException("E0001", "員工扣薪日程表的媒體日期不符" + tEmpDeductSchedule.getMediaDate());
			}
			CdCode tCdCode = cdCodeService.getItemFirst(4, "EmpDeductType", tEmpDeductSchedule.getAgType1(), titaVo);
//			4.15日薪 5.非15日薪 CdCode 4 5 15日薪
			if ("4".equals(tCdCode.getCode().substring(0, 1)) || "5".equals(tCdCode.getCode().substring(0, 1))) {
				iMediaKind = "4";
			} else {
				iMediaKind = "5";
			}
			
			iMediaDate = iMediaDate + 19110000;
			EmpDeductMedia t1EmpDeductMedia = empDeductMediaService.lastMediaSeqFirst(iMediaDate, iMediaKind, titaVo);
			if (t1EmpDeductMedia == null) {
				iMediaSeq = 1;
			} else {
				iMediaSeq = t1EmpDeductMedia.getMediaSeq() + 1;
			}

			tEmpDeductMediaId.setMediaDate(iMediaDate);
			tEmpDeductMediaId.setMediaKind(iMediaKind);
			tEmpDeductMediaId.setMediaSeq(iMediaSeq);
			tEmpDeductMedia.setMediaDate(iMediaDate);
			tEmpDeductMedia.setMediaKind(iMediaKind);
			tEmpDeductMedia.setMediaSeq(iMediaSeq);
			tEmpDeductMedia.setEmpDeductMediaId(tEmpDeductMediaId);
			tEmpDeductMedia.setCustNo(iCustNo);
			tEmpDeductMedia.setRepayCode(iRepayCode);
			tEmpDeductMedia.setPerfMonth(iPerfMonth);
			tEmpDeductMedia.setPerfRepayCode(iPerfRepayCode);
			tEmpDeductMedia.setRepayAmt(iRepayAmt);
			tEmpDeductMedia.setFlowCode(tCdEmp.getAgType1());
			tEmpDeductMedia.setUnitCode(tCdEmp.getCenterCodeAcc());
			tEmpDeductMedia.setCustId(tCustMain.getCustId());
			tEmpDeductMedia.setEntryDate(tEmpDeductSchedule.getEntryDate());
			tEmpDeductMedia.setTxAmt(BigDecimal.ZERO);
			tEmpDeductMedia.setErrorCode("");
			tEmpDeductMedia.setAcctCode("000");
			if (iRepayCode == 1) {
				Slice<FacMain> slFacMain = facMainService.CustNoAll(iCustNo, this.index, this.limit, titaVo);
				if (slFacMain != null) {
					for (FacMain tFacMain : slFacMain.getContent()) {
						if (tFacMain.getUtilAmt().compareTo(BigDecimal.ZERO) > 0)
							tEmpDeductMedia.setAcctCode(tFacMain.getAcctCode());
					}
				}
			}
			tEmpDeductMedia.setAcDate(0);
			tEmpDeductMedia.setBatchNo("");
			tEmpDeductMedia.setDetailSeq(0);
			try {
				empDeductMediaService.insert(tEmpDeductMedia, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "EmpDeductMedia " + e.getErrorMsg());
			}
			// insert EmpDeduc
			insertEmpDeductDtl(tEmpDeductMedia, tCustMain, tCdEmp, titaVo);
			break;
		case 2:
			
			iMediaDate = iMediaDate + 19110000;
			
			tEmpDeductMediaId.setMediaDate(iMediaDate);
			tEmpDeductMediaId.setMediaKind(iMediaKind);
			tEmpDeductMediaId.setMediaSeq(iMediaSeq);

			tEmpDeductMedia = empDeductMediaService.holdById(tEmpDeductMediaId, titaVo);

			tEmpDeductMedia.setCustNo(iCustNo);
			tEmpDeductMedia.setRepayCode(iRepayCode);
			tEmpDeductMedia.setPerfMonth(iPerfMonth);
			tEmpDeductMedia.setPerfRepayCode(iPerfRepayCode);
			tEmpDeductMedia.setRepayAmt(iRepayAmt);
			try {
				empDeductMediaService.update(tEmpDeductMedia, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0007", "EmpDeductMedia : " + e.getErrorMsg());
			}
			break;
		case 4:
			
			iMediaDate = iMediaDate + 19110000;
			tEmpDeductMediaId.setMediaDate(iMediaDate);
			tEmpDeductMediaId.setMediaKind(iMediaKind);
			tEmpDeductMediaId.setMediaSeq(iMediaSeq);
			tEmpDeductMedia = empDeductMediaService.holdById(tEmpDeductMediaId, titaVo);
			
			if (tEmpDeductMedia == null) {
				throw new LogicException("E0006", "EmpDeductDtl"); // E0006 鎖定資料時，發生錯誤
			}
			try {
				empDeductMediaService.delete(tEmpDeductMedia, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0008", "EmpDeductMedia : " + e.getErrorMsg());
			}
			
			// delete EmpDeductDtl
			Slice<EmpDeductDtl> slEmpDeductDtl = empDeductDtlService.mediaSeqEq(iMediaDate, iMediaKind, iMediaSeq, this.index, Integer.MAX_VALUE, titaVo);
			if (slEmpDeductDtl == null) {
				throw new LogicException("E0006", "EmpDeductDtl"); // E0006 鎖定資料時，發生錯誤
			}
			try {
				empDeductDtlService.deleteAll(slEmpDeductDtl.getContent(), titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", "EmpDeductDtl : " + e.getErrorMsg());
			}

			break;
		}

		this.addList(this.totaVo);
		return this.sendList();

	}

	private void insertEmpDeductDtl(EmpDeductMedia t, CustMain tCustMain, CdEmp tCdEmp, TitaVo titaVo) throws LogicException {
		EmpDeductDtl tEmpDeductDtl = new EmpDeductDtl();
		EmpDeductDtlId tEmpDeductDtlId = new EmpDeductDtlId();
		tEmpDeductDtlId.setEntryDate(t.getEntryDate());
		tEmpDeductDtlId.setCustNo(t.getCustNo());
		tEmpDeductDtlId.setAchRepayCode(t.getRepayCode());
		tEmpDeductDtlId.setPerfMonth(t.getPerfMonth());
		tEmpDeductDtlId.setProcCode(t.getFlowCode());
		tEmpDeductDtlId.setRepayCode(parse.IntegerToString(t.getPerfRepayCode(), 1));
		tEmpDeductDtlId.setAcctCode(t.getAcctCode());
		tEmpDeductDtlId.setFacmNo(0);
		tEmpDeductDtlId.setBormNo(0);
		tEmpDeductDtl.setEmpDeductDtlId(tEmpDeductDtlId);
		tEmpDeductDtl.setEmpNo(tCustMain.getEmpNo());
		tEmpDeductDtl.setCustId(tCustMain.getCustId());
		tEmpDeductDtl.setTxAmt(BigDecimal.ZERO);
		tEmpDeductDtl.setRepayAmt(t.getRepayAmt());
		tEmpDeductDtl.setErrMsg("");
		tEmpDeductDtl.setAcdate(0);
		tEmpDeductDtl.setTitaTxtNo("");
		tEmpDeductDtl.setTitaTlrNo("");
		tEmpDeductDtl.setBatchNo("");
		tEmpDeductDtl.setBatchNo("");
		tEmpDeductDtl.setResignCode(tCdEmp.getAgStatusCode());
		tEmpDeductDtl.setDeptCode(tCdEmp.getCenterCodeAcc2());
		tEmpDeductDtl.setUnitCode(tCdEmp.getCenterCodeAcc());
		tEmpDeductDtl.setIntStartDate(0);
		tEmpDeductDtl.setIntEndDate(0);
		tEmpDeductDtl.setPositCode(tCdEmp.getAgPost());
		tEmpDeductDtl.setPrincipal(BigDecimal.ZERO);
		tEmpDeductDtl.setCurrPrinAmt(BigDecimal.ZERO);
		tEmpDeductDtl.setInterest(BigDecimal.ZERO);
		tEmpDeductDtl.setCurrIntAmt(BigDecimal.ZERO);
		tEmpDeductDtl.setSumOvpayAmt(BigDecimal.ZERO);
		tEmpDeductDtl.setJsonFields("");
		tEmpDeductDtl.setMediaDate(t.getMediaDate());
		tEmpDeductDtl.setMediaKind(t.getMediaKind());
		tEmpDeductDtl.setMediaSeq(t.getMediaSeq());

		try {
			empDeductDtlService.insert(tEmpDeductDtl, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0005", "員工扣薪檔新增失敗 :" + e.getErrorMsg());
		}
	}

}