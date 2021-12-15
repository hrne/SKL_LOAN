package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4603")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4603 extends TradeBuffer {
	@Autowired
	public InsuRenewService insuRenewService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public Parse parse;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public FileCom fileCom;

	@Autowired
	public LoanBorMainService loanBorMainService;

	@Autowired
	public FacMainService facMainService;

	@Autowired
	public ClBuildingService clBuildingService;

	@Autowired
	public TxToDoDetailService txToDoDetailService;

	@Autowired
	public TxToDoCom txToDoCom;

	@Autowired
	public AcReceivableCom acReceivableCom;

	@Autowired
	public AcReceivableService acReceivableService;

	@Autowired
	public CustNoticeCom custNoticeCom;
	
	@Autowired
	public MakeFile makeFile;

	private int iInsuEndMonth = 0;
	private int errorCCnt = 0;
	private String checkResultC = "";

	private int noticeFlag = 0;
	

	private List<AcReceivable> lAcReceivable = new ArrayList<AcReceivable>();

	@Value("${iTXOutFolder}")
	private String outFolder = "";

	@Autowired
	public TotaVo totaC;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4603 ");
		this.totaVo.init(titaVo);

		totaC.putParam("MSGID", "L461C");

		txToDoCom.setTxBuffer(this.getTxBuffer());
//		書信
//		String outputLatterFilePath = outFolder + "LNM52P.txt";

		iInsuEndMonth = parse.stringToInteger(titaVo.getParam("InsuEndMonth")) + 191100;

		List<InsuRenew> lInsuRenew = new ArrayList<InsuRenew>();
		
		/*
		 * *** 折返控制相關 *** 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		// *** 折返控制相關 ***
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 129 * 400 = 51600

		
		
//		條件 : 畫面輸入火險年月整月份
		Slice<InsuRenew> slInsuRenew = insuRenewService.selectC(iInsuEndMonth, this.index, this.limit, titaVo);
		
		if (slInsuRenew != null) {
			for (InsuRenew t : slInsuRenew.getContent()) {
//			 續保件
				if (t.getRenewCode() == 2 && t.getStatusCode() == 0) {
					lInsuRenew.add(t);
				}
			}
		}

		if (lInsuRenew.size() == 0) {
			throw new LogicException(titaVo, "E0001", "");// 查無資料

		}
		// 訂正交易
		if (titaVo.isHcodeErase()) {
			deleteTxToDo("TEXT00", titaVo);
			deleteTxToDo("MAIL00", titaVo);
			updateErase(lInsuRenew, titaVo);
		}

		// 正常交易
		if (titaVo.isHcodeNormal()) {
			for (InsuRenew t : slInsuRenew.getContent()) {
				if ("Y".equals(t.getNotiTempFg())) {
					throw new LogicException("E0005", "已入通知，請先訂正此交易。");
				}

//		1.找出客戶通知方式
				CustMain t2CustMain = new CustMain();
				String custName = "";
				t2CustMain = custMainService.custNoFirst(t.getCustNo(), t.getCustNo(), titaVo);
				if (t2CustMain != null) {
					custName = t2CustMain.getCustName();
				}
				checkC(t.getCustNo(), t.getFacmNo(), titaVo);
				updateNormal(t, titaVo);
				if (!"".equals(checkResultC)) {
					// 押品號碼 原保單號碼 戶號 額度 戶名 新保險起日 新保險迄日 火險保額 火線保費 地震險保額 地震險保費 總保費 錯誤說明
					OccursList occursListReport = new OccursList();
					occursListReport.putParam("ReportCClCode1", t.getClCode1());
					occursListReport.putParam("ReportCClCode2", t.getClCode2());
					occursListReport.putParam("ReportCClNo", t.getClNo());
					occursListReport.putParam("ReportCPrevInsuNo", t.getPrevInsuNo());
					occursListReport.putParam("ReportCCustNo", t.getCustNo());
					occursListReport.putParam("ReportCFacmNo", t.getFacmNo());
					occursListReport.putParam("ReportCCustName", custName);
					occursListReport.putParam("ReportCNewInsuStartDate", t.getInsuStartDate());
					occursListReport.putParam("ReportCNewInsuEndDate", t.getInsuEndDate());
					occursListReport.putParam("ReportCFireAmt", t.getFireInsuCovrg());
					occursListReport.putParam("ReportCFireFee", t.getFireInsuPrem());
					occursListReport.putParam("ReportCEthqAmt", t.getEthqInsuCovrg());
					occursListReport.putParam("ReportCEthqFee", t.getEthqInsuPrem());
					occursListReport.putParam("ReportCTotlFee", t.getTotInsuPrem());
					if ("31".equals(checkResultC)) {
						occursListReport.putParam("ReportCErrMsg", "此額度已結案");
					}
					if ("32".equals(checkResultC)) {
						occursListReport.putParam("ReportCErrMsg", "此額度未撥款");
					}
					totaC.addOccursList(occursListReport);
				} else {

					OccursList occursList = new OccursList();
					occursList.putParam("OOCustNo", t.getCustNo());
					occursList.putParam("OOFacmNo", t.getFacmNo());
					occursList.putParam("OOClCode1", t.getClCode1());
					occursList.putParam("OOClCode2", t.getClCode2());
					occursList.putParam("OOClNo", t.getClNo());
					occursList.putParam("OOCustName", custName);
					occursList.putParam("OOInsuNo", t.getPrevInsuNo());
					occursList.putParam("OOLableA", noticeFlag);
					this.totaVo.addOccursList(occursList);
				} // else 
				
				if (lAcReceivable.size() > 0) {
					acReceivableCom.setTxBuffer(this.getTxBuffer());
					acReceivableCom.mnt(0, lAcReceivable, titaVo); // 0-起帳 1-銷帳-刪除
				}
			} // for

			
			if(this.index == 0) {
			  MySpring.newTask("L4603p", this.txBuffer, titaVo); // 寄信 mail  跑批
			  
			}
		}
		
		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slInsuRenew != null && slInsuRenew.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}
		

		this.addList(this.totaVo);

		this.info("errorCCnt  = ");
		totaC.putParam("ErrorCCnt", errorCCnt);
		this.addList(totaC);

		return this.sendList();
	}

	// 刪除處理清單
	private void deleteTxToDo(String itemCode, TitaVo titaVo) throws LogicException {
		List<TxToDoDetail> lTxToDoDetail = new ArrayList<TxToDoDetail>();
		Slice<TxToDoDetail> slTxToDoDetail = txToDoDetailService.detailStatusRange(itemCode, 0, 0, 0, Integer.MAX_VALUE,
				titaVo);
		if (slTxToDoDetail != null) {
			for (TxToDoDetail t : slTxToDoDetail.getContent()) {
				if (t.getDtlValue().length() >= 6 && t.getDtlValue().substring(0, 6).equals("<火險保費>")) {
					lTxToDoDetail.add(t);
				}
			}
			if (lTxToDoDetail.size() > 0) {
				txToDoCom.delByDetailList(lTxToDoDetail, titaVo);
			}
		}
	}

	

//	火險應繳日跟著期款->額度內>0、最小之應繳日
	private void checkC(int custNo, int facmNo, TitaVo titaVo) throws LogicException {
		// 未撥款或已結案
		boolean isClose = true;
		boolean isUnLoan = true;
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(custNo, facmNo, facmNo, 0, 900, this.index,
				this.limit, titaVo);
		if (slLoanBorMain != null) {
			isUnLoan = false;
			for (LoanBorMain tLoanBorMain : slLoanBorMain.getContent()) {
				if (tLoanBorMain.getLoanBal().compareTo(BigDecimal.ZERO) > 0) {
					isClose = false;
				}
			}
		}
		// 已結案
		if (isClose) {
			if ("".equals(checkResultC)) {
				checkResultC += "31";
			} else {
				checkResultC += ",31";
			}
		}
		// 未撥款
		if (isUnLoan) {
			if ("".equals(checkResultC)) {
				checkResultC += "32";
			} else {
				checkResultC += ",32";
			}
		}

		if (!"".equals(checkResultC)) {
			errorCCnt = errorCCnt + 1;
		}
	}

	

	// 入銷帳檔
	private void updateNormal(InsuRenew tInsuRenew, TitaVo titaVo) throws LogicException {
		tInsuRenew = insuRenewService.holdById(tInsuRenew, titaVo);
		if ("".equals(checkResultC)) {
			tInsuRenew.setNotiTempFg("Y");
		} else {
			tInsuRenew.setNotiTempFg("N");
		}
		try {
			insuRenewService.update(tInsuRenew, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0007", "InsuRenew update error");
		}

		AcReceivable acReceivable = new AcReceivable();
		acReceivable.setReceivableFlag(3); // 銷帳科目記號 -> 2-核心出帳 3-未收費用 4-短繳期金 5-另收欠款
		if (tInsuRenew.getStatusCode() == 0) {
			acReceivable.setAcctCode("TMI"); // 業務科目
			acReceivable.setRvAmt(tInsuRenew.getTotInsuPrem()); // 記帳金額
			acReceivable.setCustNo(tInsuRenew.getCustNo());// 戶號+額度
			acReceivable.setFacmNo(tInsuRenew.getFacmNo());
			acReceivable.setRvNo(tInsuRenew.getPrevInsuNo()); // 銷帳編號
			acReceivable.setOpenAcDate(tInsuRenew.getInsuStartDate());
			lAcReceivable.add(acReceivable);
		}
	}

//	將以入通知檔者將其銷帳，並改為未入
	private void updateErase(List<InsuRenew> lInsuRenew, TitaVo titaVo) throws LogicException {
		for (InsuRenew tInsuRenew : lInsuRenew) {
			if ("Y".equals(tInsuRenew.getNotiTempFg())) {
				AcReceivable acReceivable = new AcReceivable();
				acReceivable.setReceivableFlag(3); // 銷帳科目記號 -> 2-核心出帳 3-未收費用 4-短繳期金 5-另收欠款
				acReceivable.setAcctCode("TMI"); // 業務科目
				acReceivable.setRvAmt(tInsuRenew.getTotInsuPrem()); // 記帳金額
				acReceivable.setCustNo(tInsuRenew.getCustNo());// 戶號+額度
				acReceivable.setFacmNo(tInsuRenew.getFacmNo());
				acReceivable.setRvNo(tInsuRenew.getPrevInsuNo()); // 銷帳編號
				acReceivable.setOpenAcDate(tInsuRenew.getInsuStartDate());
				lAcReceivable.add(acReceivable);
			}
			tInsuRenew = insuRenewService.holdById(tInsuRenew, titaVo);
			tInsuRenew.setNotiTempFg(""); // 待通知
			try {
				insuRenewService.update(tInsuRenew, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0007", "InsuRenew update error");
			}
		}
		if (lAcReceivable.size() > 0) {
			acReceivableCom.setTxBuffer(this.getTxBuffer());
			acReceivableCom.mnt(2, lAcReceivable, titaVo); // 0-起帳 1-銷帳2-刪除
		}

	}
}