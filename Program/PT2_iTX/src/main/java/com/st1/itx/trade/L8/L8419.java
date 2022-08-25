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
import com.st1.itx.db.domain.JcicZ056;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ056LogService;
import com.st1.itx.db.service.JcicZ056Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8419")
@Scope("prototype")
/**
 * 變更還款方案結案通知資料(056)
 * 
 * @author Luisito
 * @version 1.0.0
 */
public class L8419 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ056Service sJcicZ056Service;
	@Autowired
	public JcicZ056LogService sJcicZ056LogService;
	@Autowired
	public CdCodeService iCdCodeService;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ056> sJcicZ056 = null;
		sJcicZ056 = sJcicZ056Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ056     = " + sJcicZ056.getSize());
		if (sJcicZ056 != null) {
			for (JcicZ056 xJcicZ056 : sJcicZ056) {
				if ((iSubmitType == 1 && xJcicZ056.getOutJcicTxtDate() == 0)
						|| (iSubmitType == 3 && xJcicZ056.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8317");
					occursListB.putParam("OOHistoryTxCd", "L8047");
					occursListB.putParam("OOCustId", xJcicZ056.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ056.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ056.getSubmitKey(), titaVo));
                    occursListB.putParam("OOCaseStatus", xJcicZ056.getCaseStatus());
                    occursListB.putParam("OOClaimDate", xJcicZ056.getClaimDate());
                    occursListB.putParam("OOCourtCode", xJcicZ056.getCourtCode());
					occursListB.putParam("OOTranKey", xJcicZ056.getTranKey());
					// occursListB.putParam("OOTranCode", xJcicZ056.getTranCode());
					occursListB.putParam("OOTranCode", "056");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ056.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ056.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ056.getOutJcicTxtDate() == iJcicDate && xJcicZ056.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8317");
						occursListB.putParam("OOHistoryTxCd", "L8047d");
						occursListB.putParam("OOCustId", xJcicZ056.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ056.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ056.getSubmitKey(), titaVo));
						occursListB.putParam("OOCaseStatus", xJcicZ056.getCaseStatus());
                        occursListB.putParam("OOClaimDate", xJcicZ056.getClaimDate());
                        occursListB.putParam("OOCourtCode", xJcicZ056.getCourtCode());
						occursListB.putParam("OOTranKey", xJcicZ056.getTranKey());
//					occursListB.putParam("OOTranCode", xJcicZ056.getTranCode());
						occursListB.putParam("OOTranCode", "056");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ056.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ056.getActualFilingMark());
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