package com.st1.itx.trade.L8;

import java.util.ArrayList;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.JcicZ055;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ055LogService;
import com.st1.itx.db.service.JcicZ055Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8418")
@Scope("prototype")
/**
 * 變更還款方案結案通知資料(055)
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8418 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ055Service sJcicZ055Service;
	@Autowired
	public JcicZ055LogService sJcicZ055LogService;
	@Autowired
	public CdCodeService iCdCodeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ055> sJcicZ055 = null;
		sJcicZ055 = sJcicZ055Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ055     = " + sJcicZ055.getSize());
		if (sJcicZ055 != null) {
			for (JcicZ055 xJcicZ055 : sJcicZ055) {
				if ((iSubmitType == 1 && xJcicZ055.getOutJcicTxtDate() == 0) || (iSubmitType == 3 && xJcicZ055.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8316");
					occursListB.putParam("OOHistoryTxCd", "L8046");
					occursListB.putParam("OOCustId", xJcicZ055.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ055.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ055.getSubmitKey(), titaVo));
					occursListB.putParam("OOCaseStatus", xJcicZ055.getCaseStatus());
					occursListB.putParam("OOClaimDate", xJcicZ055.getClaimDate());
					occursListB.putParam("OOCourtCode", xJcicZ055.getCourtCode());
					occursListB.putParam("OOTranKey", xJcicZ055.getTranKey());
					// occursListB.putParam("OOTranCode", xJcicZ055.getTranCode());
					occursListB.putParam("OOTranCode", "055");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ055.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ055.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ055.getOutJcicTxtDate() == iJcicDate && xJcicZ055.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8316");
						occursListB.putParam("OOHistoryTxCd", "L8046d");
						occursListB.putParam("OOCustId", xJcicZ055.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ055.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ055.getSubmitKey(), titaVo));
						occursListB.putParam("OOCaseStatus", xJcicZ055.getCaseStatus());
						occursListB.putParam("OOClaimDate", xJcicZ055.getClaimDate());
						occursListB.putParam("OOCourtCode", xJcicZ055.getCourtCode());
						occursListB.putParam("OOTranKey", xJcicZ055.getTranKey());
//					occursListB.putParam("OOTranCode", xJcicZ055.getTranCode());
						occursListB.putParam("OOTranCode", "055");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ055.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ055.getActualFilingMark());
						this.totaVo.addOccursList(occursListB);
					}
				}
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	public String dealBankName(String BankId, TitaVo titaVo) throws LogicException {
		CdCode tCdCode = new CdCode();
		tCdCode = iCdCodeService.getItemFirst(8, "JcicBankCode", BankId, titaVo);
		String JcicBankName = "";// 80碼長度
		if (tCdCode != null) {
			JcicBankName = tCdCode.getItem();
		}
		return JcicBankName;
	}
}