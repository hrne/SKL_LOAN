package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BatxDetail;
import com.st1.itx.db.domain.BatxDetailId;
import com.st1.itx.db.domain.BatxHead;
import com.st1.itx.db.domain.BatxHeadId;
import com.st1.itx.db.domain.BatxOthers;
import com.st1.itx.db.domain.BatxOthersId;
import com.st1.itx.db.domain.NegAppr02;
import com.st1.itx.db.domain.NegAppr02Id;
import com.st1.itx.db.service.BatxDetailService;
import com.st1.itx.db.service.BatxHeadService;
import com.st1.itx.db.service.BatxOthersService;
import com.st1.itx.db.service.NegAppr02Service;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcPaymentCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * AcDate=9,7<br>
 * BatchNo=X,6<br>
 * RepayType=9,2<br>
 * RepayCode=9,1<br>
 * AcNo=9,12<br>
 * RepayAmt=9,14.2<br>
 * RepayId=X,10<br>
 * RepayName=X,100<br>
 * CustNo=X,7<br>
 * CustNm=X,100<br>
 * RvNo=X,12<br>
 * Note=X,60<br>
 * END=X,1<br>
 */

@Service("L4210")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4210 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L4210.class);

	/* DB服務注入 */
	@Autowired
	public BatxOthersService batxOthersService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public AcPaymentCom acPaymentCom;

	@Autowired
	public BatxDetailService batxDetailService;

	@Autowired
	public BatxHeadService batxHeadService;

	@Autowired
	public NegAppr02Service negAppr02Service;

	@Autowired
	public SendRsp sendRsp;

	int iFunctionCode = 0;
	int iAcDate = 0;
	int iEntryDate = 0;
	int iBringUpDate = 0;
	String iBatchNo = "";
	int iDetailSeq = 0;
	int iRepayCode = 0;
	int iRepayType = 0;
	int iCustNo = 0;
	int iFacmNo = 0;
	String iAcCode = "";
	String iRepayId = "";
	String iRepayName = "";
	BigDecimal iRepayAmt = BigDecimal.ZERO;
	String iCustNm = "";
	String iRvNo = "";
	String iReconCode = "";
	String iNote = "";
	String wkRemark = "";
	TempVo tempVo = new TempVo();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4210 ");
		this.totaVo.init(titaVo);

		iFunctionCode = parse.stringToInteger(titaVo.getParam("FunctionCode").trim());
		iAcDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		iBatchNo = titaVo.getParam("BatchNo");
		iDetailSeq = parse.stringToInteger(titaVo.getParam("DetailSeq"));
//		iBringUpDate = parse.stringToInteger(titaVo.getParam("BringUpDate")) + 19110000;

		iEntryDate = parse.stringToInteger(titaVo.getParam("EntryDate")) + 19110000;
		iRepayCode = parse.stringToInteger(titaVo.getParam("RepayCode"));
		iRepayType = parse.stringToInteger(titaVo.getParam("RepayType"));
		iAcCode = FormatUtil.padX(titaVo.getParam("AcNoCode"), 8) + FormatUtil.padX(titaVo.getParam("AcSubCode"), 5)
				+ FormatUtil.padX(titaVo.getParam("AcDtlCode"), 2);
		iRepayAmt = parse.stringToBigDecimal(titaVo.getParam("RepayAmt"));
		iRepayId = titaVo.getParam("RepayId");
		iRepayName = titaVo.getParam("RepayName");
		iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		iCustNm = titaVo.getParam("CustNm");
		iRvNo = titaVo.getParam("RvNo");
		iNote = titaVo.getParam("Note");
		if (iRepayCode == 11)
			iReconCode = "P03";
		else if (iRepayCode == 07)
			iReconCode = "T10";
		else if (iRepayCode == 05)
			iReconCode = "OPL";
		else if (iRepayCode == 06)
			iReconCode = "C02";
		else
			iReconCode = "   ";

		// 一般債權撥付資料檔轉入 (新增 & 07 ,代收款-債權協商 & 般債權提兌日 > 0)
		if (iFunctionCode == 1 && parse.stringToInteger(titaVo.getParam("RepayCode")) == 7 && iBringUpDate > 19110000)
			insertFromNegAppr02(titaVo); // NegAppr02 一般債權撥付資料檔
		//
		else {
			switch (iFunctionCode) {
			case 1:
				insertL4210(titaVo);
				break;
			case 2:
				updateL4210(titaVo);
				break;
			case 4:
				deleteL4210(titaVo);
				break;

			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	// 一般債權撥付資料檔轉入
	private void insertFromNegAppr02(TitaVo titaVo) throws LogicException {
		// findAll 整批入帳明細檔
		BigDecimal wkTxAmt = BigDecimal.ZERO;
		BigDecimal iTxAmt = iRepayAmt;

		int cnt = 0;
		NegAppr02 tNegAppr02 = new NegAppr02();
		Slice<NegAppr02> slNegAppr02 = negAppr02Service.bringUpDateEq(iBringUpDate, this.index, Integer.MAX_VALUE);
		List<NegAppr02> lNegAppr02 = slNegAppr02 == null ? null : slNegAppr02.getContent();
		if (lNegAppr02 != null) {
			for (NegAppr02 ng : lNegAppr02) {
				if ("4001".equals(ng.getStatusCode()) && ng.getAcDate() == 0) { // 4001:入/扣帳成功
					iRepayCode = 07; // 07:代收款-債權協商
					iRepayType = 11; // 11:債協匯入款
					iAcCode = "";
					iRepayAmt = ng.getTxAmt();
					iRepayId = ng.getCustId();
					iRepayName = "";
					iCustNo = ng.getCustNo();
					iFacmNo = 0;
					iCustNm = "";
					iRvNo = "";
					iNote = "一般債權撥付轉入";
					// remark =Bank:(5)+債權機構代號(8) + 資料檔交易序號(10) + (1) + 提兌日(7)
					wkRemark = "Bank:" + FormatUtil.padX(ng.getFinCode(), 8) + FormatUtil.padX(ng.getTxSeq(), 10) + " "
							+ titaVo.getParam("BringUpDate");
					wkTxAmt = wkTxAmt.add(ng.getTxAmt());
					cnt++;
					// insert record
					insertL4210(titaVo);
					tNegAppr02 = negAppr02Service.holdById(ng.getNegAppr02Id());
					tNegAppr02.setAcDate(iAcDate);
					try {
						negAppr02Service.update(tNegAppr02);
					} catch (DBException e) {
						throw new LogicException("E0007", "L4210 NegAppr02 update : " + e.getErrorMsg());
					}
				}
			}
		}
		if (wkTxAmt.compareTo(iTxAmt) != 0) {
			throw new LogicException("E0019",
					"一般債權撥付資料筆數=" + lNegAppr02.size() + ",未轉入筆數=" + cnt + ",金額=" + wkTxAmt + ",輸入金額=" + iTxAmt); // E0019
																													// 輸入資料錯誤
		}
	}

	// 一般債權撥付資料檔轉入
	private void deleteFromNegAppr02(TitaVo titaVo) throws LogicException {
		iBringUpDate = parse.stringToInteger(wkRemark.substring(24, 31)) + 19110000;
		NegAppr02 tNegAppr02 = negAppr02Service.holdById(
				new NegAppr02Id(iBringUpDate, wkRemark.substring(5, 13).trim(), wkRemark.substring(13, 23).trim()));
		if (tNegAppr02 != null) {
			tNegAppr02.setAcDate(0);
			try {
				negAppr02Service.update(tNegAppr02);
			} catch (DBException e) {
				throw new LogicException("E0007", "L4210 NegAppr02 update : " + e.getErrorMsg());
			}
		}
	}

	private void insertL4210(TitaVo titaVo) throws LogicException {
		BatxOthers tBatxOthers = new BatxOthers();
		BatxOthersId tBatxOthersId = new BatxOthersId();

		BatxDetail tBatxDetail = new BatxDetail();
		BatxDetailId tBatxDetailId = new BatxDetailId();

		// 第一筆從1開始
		int wkDetailSeq = 1;

		BatxOthers seqBatxOthers = batxOthersService.detSeqFirst(iAcDate);

		// 第二筆後+1續編
		if (seqBatxOthers != null) {
			wkDetailSeq = seqBatxOthers.getDetailSeq() + 1;
			this.info("L4210-82 intDetailSeq : " + wkDetailSeq);
			this.info("L4210-83 seqBatxOthers.getDetailSeq() : " + seqBatxOthers.getDetailSeq());
		}

		tBatxOthersId.setAcDate(iAcDate);
		tBatxOthersId.setBatchNo(iBatchNo);
		tBatxOthersId.setDetailSeq(wkDetailSeq);
		tBatxOthers.setBatxOthersId(tBatxOthersId);

		// 新增
		tBatxOthers.setAcDate(iAcDate);
		tBatxOthers.setEntryDate(iEntryDate);
		tBatxOthers.setBatchNo(iBatchNo);
		tBatxOthers.setDetailSeq(wkDetailSeq);
		tBatxOthers.setRepayType(iRepayType);
		tBatxOthers.setRepayCode(iRepayCode);
		tBatxOthers.setRepayAcCode(iAcCode);
		tBatxOthers.setRepayAmt(iRepayAmt);
		tBatxOthers.setRepayId(iRepayId);
		tBatxOthers.setRepayName(iRepayName);
		tBatxOthers.setCustNo(iCustNo);
		tBatxOthers.setFacmNo(iFacmNo);
		tBatxOthers.setCustNm(iCustNm);
		tBatxOthers.setRvNo(iRvNo);
		tBatxOthers.setNote(iNote);

		try {
			batxOthersService.insert(tBatxOthers, titaVo);
		} catch (DBException e) {
			if (e.getErrorId() == 2)
				throw new LogicException(titaVo, "E0005", "L4210 BatxOthers insert " + e.getErrorMsg());
		}
		// head
		updateBatxHead(iRepayAmt, titaVo);
        // Detail
		tBatxDetailId.setAcDate(iAcDate);
		tBatxDetailId.setBatchNo(iBatchNo);
		tBatxDetailId.setDetailSeq(wkDetailSeq);
		tBatxDetail.setBatxDetailId(tBatxDetailId);
		tBatxDetail.setRepayCode(iRepayCode);
		tBatxDetail.setCustNo(iCustNo);
		tBatxDetail.setFacmNo(iFacmNo);
		tBatxDetail.setRvNo(iRvNo);
		tBatxDetail.setEntryDate(iEntryDate);
		tBatxDetail.setFileName("L4210");
		tBatxDetail.setRepayType(iRepayType);
		tBatxDetail.setReconCode(iReconCode);
		tBatxDetail.setRepayAcCode(iAcCode);
		tBatxDetail.setRepayAmt(iRepayAmt);
		tBatxDetail.setProcStsCode("0");
		tBatxDetail.setProcCode("00000");
		tBatxDetail.setTitaTlrNo(titaVo.getTlrNo());
		tBatxDetail.setTitaTxtNo(titaVo.getTxNo());
		TempVo tempVo = new TempVo();
		tempVo.putParam("Note", iNote);
		tempVo.putParam("Remark", wkRemark);
		tBatxDetail.setProcNote(tempVo.getJsonString());
		try {
			batxDetailService.insert(tBatxDetail, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "L4210 BatxDetail insert " + e.getErrorMsg());
		}
		
//		 大額匯款手工增入入帳
		if (iRepayCode == 11 && titaVo.getEmpNos().trim().isEmpty()) {
			sendRsp.addvReason(this.txBuffer, titaVo, "0502", "");
		}
	}

	private void updateBatxHead(BigDecimal txAmt, TitaVo titaVo) throws LogicException {
//					AcDate	會計日期
//					BatchNo	批號
//					BatxTotAmt	總金額
//					BatxTotCnt	總筆數
//					BatxExeCode	作業狀態
//					BatxStsCode	整批作業狀態
//					TitaTlrNo	經辦
//					TitaTxCd	交易代號
//					A.寫入Head檔			
		BatxHead tBatxHead = new BatxHead();
		BatxHeadId tBatxHeadId = new BatxHeadId();
		tBatxHeadId.setAcDate(iAcDate);
		tBatxHeadId.setBatchNo(iBatchNo);
		Boolean isInsert = false;
		tBatxHead = batxHeadService.holdById(tBatxHeadId);
		if (tBatxHead == null) {
			isInsert = true;
			tBatxHead = new BatxHead();
			tBatxHead.setBatxHeadId(tBatxHeadId);
		}
		tBatxHead.setBatxTotAmt(tBatxHead.getBatxTotAmt().add(txAmt));
		if (txAmt.compareTo(BigDecimal.ZERO) < 0)
			tBatxHead.setBatxTotCnt(tBatxHead.getBatxTotCnt() - 1);
		else
			tBatxHead.setBatxTotCnt(tBatxHead.getBatxTotCnt() + 1);
		tBatxHead.setBatxExeCode("0");
		tBatxHead.setBatxStsCode("0");
		tBatxHead.setTitaTlrNo(titaVo.getTlrNo());
		tBatxHead.setTitaTxCd(titaVo.getTxcd());
		if (isInsert) {
			try {
				this.info("Insert BatxHead !!!");
				batxHeadService.insert(tBatxHead, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "L4210 BatxHead Insert : " + e.getErrorMsg());
			}
		} else {
			try {
				batxHeadService.update(tBatxHead);
			} catch (DBException e) {
				throw new LogicException("E0007", "L4210 BatxHead update : " + e.getErrorMsg());
			}
		}
	}

	private void updateL4210(TitaVo titaVo) throws LogicException {

		BatxDetail tBatxDetail = new BatxDetail();
		BatxDetailId tBatxDetailId = new BatxDetailId();
		tBatxDetailId.setAcDate(iAcDate);
		tBatxDetailId.setBatchNo(iBatchNo);
		tBatxDetailId.setDetailSeq(iDetailSeq);
		tBatxDetail = batxDetailService.holdById(tBatxDetailId);
		if (tBatxDetail == null) {
			throw new LogicException(titaVo, "E0001", "L4210 BatxDetail 無此資料");
		}

		if ("5".equals(tBatxDetail.getProcStsCode()) || "6".equals(tBatxDetail.getProcStsCode())
				|| "7".equals(tBatxDetail.getProcStsCode())) {
			throw new LogicException(titaVo, "E0010", "已入帳，請先訂正該筆資料"); // E0010 功能選擇錯誤
		}

		tempVo = tempVo.getVo(tBatxDetail.getProcNote());
		wkRemark = tempVo.get("Remark");
		this.info("wkRemark=" + wkRemark);
		if (wkRemark.length() >= 23 && "Bank:".equals(wkRemark.substring(0, 5))) {
			throw new LogicException(titaVo, "E0010", "一般債權撥付轉入資料不可修改"); // E0010 功能選擇錯誤
		}

		//
		updateBatxHead(BigDecimal.ZERO.subtract(tBatxDetail.getRepayAmt()), titaVo);
		updateBatxHead(iRepayAmt, titaVo);
		//
		tBatxDetail.setRepayCode(iRepayCode);
		tBatxDetail.setCustNo(iCustNo);
		tBatxDetail.setFacmNo(iFacmNo);
		tBatxDetail.setRvNo(iRvNo);
		tBatxDetail.setEntryDate(iEntryDate);
		tBatxDetail.setFileName("L4210");
		tBatxDetail.setRepayType(iRepayType);
		tBatxDetail.setReconCode(iReconCode);
		tBatxDetail.setRepayAcCode(iAcCode);
		tBatxDetail.setRepayAmt(iRepayAmt);
		tBatxDetail.setProcStsCode("0");
		tBatxDetail.setProcCode("00000");
		tBatxDetail.setTitaTlrNo(titaVo.getTlrNo());
		tBatxDetail.setTitaTxtNo(titaVo.getTxNo());
		tempVo = new TempVo();
		tempVo.putParam("Note", iNote);
		tempVo.putParam("Remark", wkRemark);
		tBatxDetail.setProcNote(tempVo.getJsonString());
		try {
			batxDetailService.update(tBatxDetail);
		} catch (DBException e) {
			if (e.getErrorId() == 2)
				throw new LogicException(titaVo, "E0007", "L4210 BatxDetail update " + e.getErrorMsg());
		}

		BatxOthers tBatxOthers = new BatxOthers();
		BatxOthersId tBatxOthersId = new BatxOthersId();
		tBatxOthersId.setAcDate(iAcDate);
		tBatxOthersId.setBatchNo(iBatchNo);
		tBatxOthersId.setDetailSeq(iDetailSeq);

		tBatxOthers = batxOthersService.holdById(tBatxOthersId);

		if (tBatxOthers == null) {
			throw new LogicException(titaVo, "E0001", "L4210 BatxOthers 無此資料");
		}
		tBatxOthers.setEntryDate(iEntryDate);
		tBatxOthers.setRepayType(iRepayType);
		tBatxOthers.setRepayCode(iRepayCode);
		tBatxOthers.setRepayAcCode(iAcCode);
		tBatxOthers.setRepayAmt(iRepayAmt);
		tBatxOthers.setRepayId(iRepayId);
		tBatxOthers.setRepayName(iRepayName);
		tBatxOthers.setCustNo(iCustNo);
		tBatxOthers.setFacmNo(iFacmNo);
		tBatxOthers.setCustNm(iCustNm);
		tBatxOthers.setRvNo(iRvNo);
		tBatxOthers.setNote(iNote);
		try {
			batxOthersService.update(tBatxOthers);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "L4210 BatxOthers update " + e.getErrorMsg());
		}

//		 大額匯款手工增入入帳
		if (iRepayCode == 11 && titaVo.getEmpNos().trim().isEmpty()) {
			sendRsp.addvReason(this.txBuffer, titaVo, "0502", "");
		}
	}

	private void deleteL4210(TitaVo titaVo) throws LogicException {
		BatxOthers tBatxOthers = new BatxOthers();
		BatxOthersId tBatxOthersId = new BatxOthersId();

		tBatxOthersId.setAcDate(iAcDate);
		tBatxOthersId.setBatchNo(iBatchNo);
		tBatxOthersId.setDetailSeq(iDetailSeq);
		tBatxOthers =  batxOthersService.holdById(tBatxOthersId);
		if (tBatxOthers == null) {
			throw new LogicException(titaVo, "E0001", "L4210 BatxOthers 無此資料");
		}
		
		try {
			batxOthersService.delete(tBatxOthers);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0008", "L4210 BatxOthers delete " + e.getErrorMsg());
		}

		BatxDetail tBatxDetail = new BatxDetail();
		BatxDetailId tBatxDetailId = new BatxDetailId();
		tBatxDetailId.setAcDate(iAcDate);
		tBatxDetailId.setBatchNo(iBatchNo);
		tBatxDetailId.setDetailSeq(iDetailSeq);

		tBatxDetail = batxDetailService.holdById(tBatxDetailId);

		if (tBatxDetail != null) {
			try {
				batxDetailService.delete(tBatxDetail);
			} catch (DBException e) {
				if (e.getErrorId() == 2)
					throw new LogicException(titaVo, "E0008", "L4210 BatxDetail delete " + e.getErrorMsg());
			}
		} else {
			throw new LogicException(titaVo, "E0001", "L4210 BatxDetail 無此資料");
		}

		updateBatxHead(BigDecimal.ZERO.subtract(tBatxDetail.getRepayAmt()), titaVo);

		TempVo tempVo = new TempVo();
		tempVo = tempVo.getVo(tBatxDetail.getProcNote());
		wkRemark = tempVo.get("Remark");
		this.info("wkRemark=" + wkRemark);
		if (wkRemark.length() >= 23 && "Bank:".equals(wkRemark.substring(0, 5))) {
			deleteFromNegAppr02(titaVo);
		}
	}
}