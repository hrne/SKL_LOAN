package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.ClOtherRights;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacClose;
import com.st1.itx.db.domain.FacCloseId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClOtherRightsService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2076")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2076 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacCloseService sFacCloseService;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public ClOtherRightsService ClOtherRightsService;
	@Autowired
	public ClFacService clFacService;
	@Autowired
	public LoanBorMainService loanBorMainService;

	@Autowired
	public DateUtil dateUtil;
	@Autowired
	public Parse parse;
	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2076 ");
		this.totaVo.init(titaVo);

		// wk
		int wkCloseNo = 1;
		// new PK
		FacCloseId tFacCloseId = new FacCloseId();
		// new table
		FacClose tFacCloseMaxCloseNo = new FacClose();
		FacClose tFacClose = new FacClose();
		CustMain tCustMain = new CustMain();
		List<ClFac> lClFac = new ArrayList<ClFac>();
		List<ClOtherRights> lClOtherRights = new ArrayList<ClOtherRights>();
//		long pdfSnoF = 0L ;

		// tita
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		String iFunCode = titaVo.getParam("FunCode");
		int iCloseNo = parse.stringToInteger(titaVo.getParam("CloseNo"));
		int wkFacmNoSt = 1;
		int wkFacmNoEd = 999;
		if (iFacmNo > 0) {
			wkFacmNoSt = iFacmNo;
			wkFacmNoEd = iFacmNo;
		}
//		if ("7".equals(titaVo.getParam("FunCd"))) {
//			
//			 pdfSnoF = doRpt(titaVo);
//			this.info("PdfSnoF 清償作業: " + pdfSnoF);
//			
//			//編公文編號
//
//		} else {

//
//			FacClose BeforeFacClose = sFacCloseService.findById(new FacCloseId(iCustNo, iCloseNo), titaVo);
//			// 測試是否存在清償作業檔 如不存在拋錯,如存在取該戶號清償序號最大筆+1
//			tFacCloseMaxCloseNo = sFacCloseService.findMaxCloseNoFirst(iCustNo);
//
//			if (tFacCloseMaxCloseNo != null) {
//				wkCloseNo = tFacCloseMaxCloseNo.getCloseNo() + 1;
//			} else {
//				throw new LogicException(titaVo, "E2003", "查無清償作業檔資料"); // 查無資料
//			}
//
//			tFacCloseId.setCustNo(iCustNo);
//			tFacCloseId.setCloseNo(wkCloseNo);
//
////		tFacClose = sFacCloseService.findById(tFacCloseId, titaVo);
//			this.info("iCustNo   =" + iCustNo);
//			this.info("iFacmNo   =" + iFacmNo);
//			this.info("iCloseNo  =" + iCloseNo);
//			this.info("tFacCloseOld  =" + tFacClose);
//
//			tFacClose.setFacCloseId(tFacCloseId);
//			tFacClose.setCustNo(iCustNo);
//			tFacClose.setCloseNo(wkCloseNo);
//			tFacClose.setFacmNo(iFacmNo);
//			tFacClose.setFunCode(iFunCode);
//			tFacClose.setCarLoan(BeforeFacClose.getCarLoan());
//			tFacClose.setCloseDate(BeforeFacClose.getCloseDate());
//			tFacClose.setCloseReasonCode(titaVo.getParam("CloseReasonCode"));
//			tFacClose.setCloseAmt(BeforeFacClose.getCloseAmt());
//			tFacClose.setCollectFlag(BeforeFacClose.getCollectFlag());
//			tFacClose.setCollectWayCode(titaVo.getParam("CollectWayCode"));
//			tFacClose.setReceiveDate(parse.stringToInteger(titaVo.getParam("ReceiveDate")));
//			tFacClose.setTelNo1(titaVo.getParam("TelNo1"));
//			tFacClose.setTelNo2(titaVo.getParam("TelNo2"));
//			tFacClose.setFaxNum(titaVo.getParam("FaxNum"));
//			tFacClose.setEntryDate(BeforeFacClose.getEntryDate());
//			tFacClose.setAgreeNo(BeforeFacClose.getAgreeNo());
//			tFacClose.setDocNo(BeforeFacClose.getDocNo());
//			tFacClose.setClsNo(BeforeFacClose.getClsNo());
//			tFacClose.setRmk(titaVo.getParam("Rmk"));
//			tFacClose.setClCode1(BeforeFacClose.getClCode1());
//			tFacClose.setClCode2(BeforeFacClose.getClCode2());
//			tFacClose.setClNo(BeforeFacClose.getClNo());
//
//			try {
//				sFacCloseService.insert(tFacClose, titaVo);
//			} catch (DBException e) {
//				throw new LogicException("E0005", "清償作業檔");
//			}

		// 擔保品與額度關聯檔
		Slice<ClFac> slClFac = clFacService.selectForL2017CustNo(iCustNo, wkFacmNoSt, wkFacmNoEd, 0, Integer.MAX_VALUE,
				titaVo);
		lClFac = slClFac == null ? null : slClFac.getContent();
		this.info("slClFac  = " + slClFac);
		if (lClFac != null) {
			int wkClCode1 = 0;
			int wkClCode2 = 0;
			int wkClNo = 0;
			for (ClFac tClFac : slClFac) {
				if (wkClCode1 == tClFac.getClCode1() && wkClCode2 == tClFac.getClCode2()
						&& wkClNo == tClFac.getClNo()) {
					continue;
				}

				wkClCode1 = tClFac.getClCode1();
				wkClCode2 = tClFac.getClCode2();
				wkClNo = tClFac.getClNo();

				this.info("lClOtherRights = " + lClOtherRights);

				OccursList occursList = new OccursList();
				occursList.putParam("OOClCode1", tClFac.getClCode1());
				occursList.putParam("OOClCode2", tClFac.getClCode2());
				occursList.putParam("OOClNo", tClFac.getClNo());
				occursList.putParam("OOSeq", "");
				occursList.putParam("OOCity", "");
				occursList.putParam("OOLandAdm", "");
				occursList.putParam("OORecYear", "");
				occursList.putParam("OORecWord", "");
				occursList.putParam("OORecNumber", "");
				occursList.putParam("OORightsNote", "");
				occursList.putParam("OOSecuredTotal", "0");
				occursList.putParam("OOAllClose", "");

				// 全部結案
				List<ClFac> l2ClFac = new ArrayList<ClFac>(); // 擔保品與額度關聯檔
				l2ClFac = slClFac == null ? null : slClFac.getContent();
				boolean isAllClose = true;
				for (ClFac c : l2ClFac) {
					if ((c.getCustNo() == iCustNo && iFacmNo == 0)
							|| (c.getCustNo() == iCustNo && c.getFacmNo() == iFacmNo)) {

						// 撥款主檔
						Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(c.getCustNo(), c.getFacmNo(),
								c.getFacmNo(), 1, 900, 0, Integer.MAX_VALUE, titaVo);
						if (slLoanBorMain != null) {
							for (LoanBorMain t : slLoanBorMain.getContent()) {
								// 戶況 0: 正常戶1:展期2: 催收戶3: 結案戶4: 逾期戶5: 催收結案戶6: 呆帳戶7: 部分轉呆戶8: 債權轉讓戶9: 呆帳結案戶
								if (t.getStatus() == 0 || t.getStatus() == 2 || t.getStatus() == 4 || t.getStatus() == 6
										|| t.getStatus() == 8) {
									isAllClose = false;
									break;
								}
							}
						}
					}
				}

				Slice<ClOtherRights> slClOtherRights = ClOtherRightsService.findClNo(tClFac.getClCode1(),
						tClFac.getClCode2(), tClFac.getClNo(), 0, Integer.MAX_VALUE, titaVo);
				lClOtherRights = slClOtherRights == null ? null : slClOtherRights.getContent();

				if (lClOtherRights != null) {
					for (ClOtherRights tClOtherRights : lClOtherRights) {

						occursList = new OccursList();

						occursList.putParam("OOClCode1", tClOtherRights.getClCode1());
						occursList.putParam("OOClCode2", tClOtherRights.getClCode2());
						occursList.putParam("OOClNo", tClOtherRights.getClNo());
						occursList.putParam("OOSeq", tClOtherRights.getSeq());
						occursList.putParam("OOCity", tClOtherRights.getCity());
						occursList.putParam("OOLandAdm", tClOtherRights.getLandAdm());
						occursList.putParam("OORecYear", tClOtherRights.getRecYear());
						occursList.putParam("OORecWord", tClOtherRights.getRecWord());
						occursList.putParam("OORecNumber", tClOtherRights.getRecNumber());
						occursList.putParam("OORightsNote", tClOtherRights.getRightsNote());
						occursList.putParam("OOSecuredTotal", tClOtherRights.getSecuredTotal());
						if (isAllClose) {
							occursList.putParam("OOAllClose", "Y");
						} else {
							occursList.putParam("OOAllClose", "N");
						}

						totaVo.addOccursList(occursList);
					}

				} else {
					if (isAllClose) {
						occursList.putParam("OOAllClose", "Y");
					} else {
						occursList.putParam("OOAllClose", "N");
					}

					totaVo.addOccursList(occursList);
				}

			}
		} else {
			throw new LogicException("E2003", "額度與擔保品關聯檔"); // 查無資料
		}
//		}

//		totaVo.put("PdfSnoF", "" + pdfSnoF);
		this.addList(this.totaVo);
		return this.sendList();
	}

//	public long doRpt(TitaVo titaVo) throws LogicException {
//		this.info("L2670 doRpt started.");
//
//		// 撈資料組報表
//		L2076Rpt.exec(titaVo);
//
//		// 寫產檔記錄到TxReport
//		long rptNo = L2076Rpt.close();
//
//		// 產生PDF檔案
//		L2076Rpt.toPdf(rptNo);
//
//		this.info("L2670 doRpt finished.");
//		return rptNo;
//	}
}