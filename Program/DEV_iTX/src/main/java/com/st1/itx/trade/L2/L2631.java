package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacClose;
import com.st1.itx.db.domain.FacCloseId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.CdGseqService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.GSeqCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 */

@Service("L2631")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2631 extends TradeBuffer {

	@Autowired
	public CdGseqService cdGseqService;
	/* DB服務注入 */
	@Autowired
	public FacCloseService sFacCloseService;
	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public ClFacService clFacService;
	@Autowired
	public LoanBorMainService loanBorMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 報表服務注入 */
	@Autowired
	public L2076Report L2076Report;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	GSeqCom gGSeqCom;

	@Autowired
	AcReceivableCom acReceivableCom;

	@Autowired
	BaTxCom baTxCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2631 ");
		this.totaVo.init(titaVo);

		// new PK
		FacCloseId tFacCloseId = new FacCloseId();
		// new table
		FacClose tFacCloseMaxCloseNo = new FacClose();
		FacClose tFacClose = new FacClose();
		CustMain tCustMain = new CustMain();
		List<ClFac> lClFac = new ArrayList<ClFac>();
		FacClose BeforeFacClose = new FacClose();
		// tita
		// 登放記號
		int iActFg = parse.stringToInteger(titaVo.getParam("ACTFG"));
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		// 統編
		String iCustId = titaVo.getParam("CustId");
		long pdfSnoF = 0;

		if (!iCustId.isEmpty()) {
			// 如輸入統編,先取出戶號
			tCustMain = sCustMainService.custIdFirst(iCustId);
			iCustNo = tCustMain.getCustNo();
			if (iCustNo == 0) {
				this.info("該統編在客戶主檔無戶號");
			}
		}
		// 功能
		String iFunCode = titaVo.getParam("FunCode");
		// 入帳日期
		int iTranDate = parse.stringToInteger(titaVo.getParam("TranDate"));
		// 申請日期
		int iApplDate = parse.stringToInteger(titaVo.getParam("ApplDate"));
		// 結案區分
		int iCloseInd = parse.stringToInteger(titaVo.getParam("CloseInd"));
		// 額度編號
		int iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));

		int iCloseNo = parse.stringToInteger(titaVo.getParam("CloseNo"));
		BigDecimal iCloseAmt = parse.stringToBigDecimal(titaVo.getParam("CloseAmt"));

		// 清償序號
		int wkcloseNo = 1;
		// wk
		int wkDocNo = 0;
		int wkCloseDate = 0;
		// 測試是否存在清償作業檔 如不存在清償序號為01,如存在取該戶號額度清償序號最大筆+1
		tFacCloseMaxCloseNo = sFacCloseService.findMaxCloseNoFirst(iCustNo);
		if (tFacCloseMaxCloseNo != null) {
			wkcloseNo = tFacCloseMaxCloseNo.getCloseNo() + 1;
		}
		this.info("清償序號 = " + wkcloseNo);

		tFacCloseId.setCustNo(iCustNo);
		tFacCloseId.setCloseNo(wkcloseNo);

		tFacClose.setFacCloseId(tFacCloseId);
		tFacClose.setCustNo(iCustNo);
		tFacClose.setCloseNo(wkcloseNo);
		tFacClose.setFacmNo(iFacmNo);
		tFacClose.setActFlag(iActFg);
		tFacClose.setFunCode(titaVo.getParam("FunCode"));
		tFacClose.setApplDate(iApplDate);

		// 入帳日期L2632
		tFacClose.setCloseDate(0);
		tFacClose.setCollectFlag(titaVo.getParam("CollectFlag"));
		tFacClose.setCloseAmt(iCloseAmt);
		tFacClose.setTelNo1(titaVo.getParam("TelNo1"));
		tFacClose.setTelNo2(titaVo.getParam("TelNo2"));
		tFacClose.setFaxNum(titaVo.getParam("FaxNum"));
		tFacClose.setCloseReasonCode(titaVo.getParam("CloseReasonCode"));
		tFacClose.setCollectWayCode(titaVo.getParam("CollectWayCode"));
		tFacClose.setReceiveDate(parse.stringToInteger(titaVo.getParam("ReceiveDate")));
		tFacClose.setEntryDate(iTranDate);
		// 塗銷同意書編號L2632
		tFacClose.setAgreeNo("");
		// 公文編號L2632
		tFacClose.setDocNo(wkDocNo);
		// 銷號欄L2632
		tFacClose.setClsNo("");
		tFacClose.setRmk(titaVo.getParam("Rmk1"));
		// **[清償違約金即時收取]部分償還時一律記短收，連同下期期款收回，提前結案時計算清償違約金併入結案金額。
		// **[清償違約金領清償證明收取]<清償請領>結案時計算全部的部分償還及提前結案的清償違約金，併入結案金額。
		// [清償違約金領清償證明收取]<清償補領>計算全部的部分償還及提前結案的清償違約金，L3200收取清償違約金領後，才可列印清償證明。
		if ("2".equals(iFunCode)) {
			if ("".equals(titaVo.getParam("RpFg")) && iCloseAmt.compareTo(BigDecimal.ZERO) > 0) {
				// 短繳清償違約金處理, 新增銷帳檔
				List<AcReceivable> lAcReceivable = new ArrayList<AcReceivable>();
				AcReceivable tAcReceivable = new AcReceivable();
				tAcReceivable.setReceivableFlag(4); // 短繳期金
				tAcReceivable.setAcctCode("YOP");
				tAcReceivable.setCustNo(iCustNo);
				tAcReceivable.setFacmNo(iFacmNo);
				tAcReceivable.setRvNo("000");
				tAcReceivable.setRvAmt(iCloseAmt);
				lAcReceivable.add(tAcReceivable);
				acReceivableCom.setTxBuffer(this.getTxBuffer());
				acReceivableCom.mnt(0, lAcReceivable, titaVo); // 0-起帳
			}
			// 收取清償違約金後 (L3200)，才能列印清償證明
			if ("7".equals(titaVo.getParam("RpFg"))) {
				// 查詢各項費用
				baTxCom.setTxBuffer(this.getTxBuffer());
				baTxCom.settingUnPaid(iTranDate, iCustNo, iFacmNo, 0, 99, BigDecimal.ZERO, titaVo); // 99-費用全部(含未到期)
				if (baTxCom.getShortCloseBreach().compareTo(BigDecimal.ZERO) > 0) {
					throw new LogicException(titaVo, "E3094", "清償違約金 = " + baTxCom.getShortCloseBreach()); // 不可有短繳金額
				}
			}
		}

		if ("7".equals(titaVo.getParam("RpFg"))) {

			int wkFacmNoSt = 1;
			int wkFacmNoEd = 999;
			if (iFacmNo > 0) {
				wkFacmNoSt = iFacmNo;
				wkFacmNoEd = iFacmNo;
			}
			// 擔保品與額度關聯檔
			Slice<ClFac> slClFac = clFacService.selectForL2017CustNo(iCustNo, wkFacmNoSt, wkFacmNoEd, 0,
					Integer.MAX_VALUE, titaVo);
			lClFac = slClFac == null ? null : slClFac.getContent();
			// 全部結案
			boolean isAllClose = true;
			for (ClFac c : lClFac) {
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
			if (!isAllClose) {
				throw new LogicException(titaVo, "E2074", "未全部結案不可列印清償證明 "); // 未全部結案，不可列印
			}

			BeforeFacClose = sFacCloseService.holdById(new FacCloseId(iCustNo, iCloseNo), titaVo);
			BeforeFacClose.setReceiveFg(1);
			// 自動取公文編號
			this.txBuffer.getTxCom();
			wkDocNo = gGSeqCom.getSeqNo(110, 0, "L2", "2631", 9999, titaVo);
			String finalDocNo = StringUtils.leftPad(String.valueOf(wkDocNo), 4, "0");

			this.info("BeforeFacClose = " + BeforeFacClose);
			String docNo = titaVo.getCalDy().substring(0, 3) + finalDocNo;
			tFacClose.setFunCode(titaVo.getParam("FunCode"));
			tFacClose.setActFlag(BeforeFacClose.getActFlag());
			tFacClose.setApplDate(BeforeFacClose.getApplDate());
			tFacClose.setCloseDate(BeforeFacClose.getCloseDate());
			tFacClose.setCloseAmt(BeforeFacClose.getCloseAmt());
			tFacClose.setAgreeNo(BeforeFacClose.getAgreeNo());
			tFacClose.setCollectFlag("Y");
			tFacClose.setEntryDate(BeforeFacClose.getEntryDate());
			tFacClose.setDocNo(parse.stringToInteger(docNo));
			tFacClose.setClsNo(BeforeFacClose.getClsNo());
			tFacClose.setRmk(titaVo.getParam("Rmk1"));
			tFacClose.setClCode1(parse.stringToInteger(titaVo.getParam("OOClCode1")));
			tFacClose.setClCode2(parse.stringToInteger(titaVo.getParam("OOClCode2")));
			tFacClose.setClNo(parse.stringToInteger(titaVo.getParam("OOClNo")));
		}
		this.info("tFacClose  = " + tFacClose);
		try {
			sFacCloseService.insert(tFacClose, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0005", "清償作業檔");
		}

		if ("7".equals(titaVo.getParam("RpFg"))) {

			pdfSnoF = doRpt(titaVo, tFacClose);
			this.info("PdfSnoF 清償作業: " + pdfSnoF);

			try {
				sFacCloseService.update(BeforeFacClose, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "清償作業檔");
			}
		}

		// 更新領取記號

		this.totaVo.putParam("OCloseNo", wkcloseNo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	public long doRpt(TitaVo titaVo, FacClose tFacClose) throws LogicException {
		this.info("L2670 doRpt started.");

		// 撈資料組報表
		L2076Report.exec(titaVo, tFacClose);

		// 寫產檔記錄到TxReport
		long rptNo = L2076Report.close();

		// 產生PDF檔案
		L2076Report.toPdf(rptNo);

		this.info("L2670 doRpt finished.");
		return rptNo;
	}
}