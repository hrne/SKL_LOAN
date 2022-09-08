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
import com.st1.itx.db.domain.JcicZ575;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ575LogService;
import com.st1.itx.db.service.JcicZ575Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8439")
@Scope("prototype")
/**
 * 聯徵產品檔案匯出作業(575)
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8439 extends TradeBuffer {
	
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ575Service sJcicZ575Service;
	@Autowired
	public JcicZ575LogService sJcicZ575LogService;
	@Autowired
	public CdCodeService iCdCodeService;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);
        this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ575> sJcicZ575 = null;
		sJcicZ575 = sJcicZ575Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ575     = " + sJcicZ575.getSize());
		if (sJcicZ575 != null) {
			for (JcicZ575 xJcicZ575 : sJcicZ575) {
				if ((iSubmitType == 1 && xJcicZ575.getOutJcicTxtDate() == 0)
						|| (iSubmitType == 3 && xJcicZ575.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8337");
					occursListB.putParam("OOHistoryTxCd", "L8067");
					occursListB.putParam("OOCustId", xJcicZ575.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ575.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ575.getSubmitKey(), titaVo));
                    occursListB.putParam("OOApplyDate", xJcicZ575.getApplyDate());
                    occursListB.putParam("OOTranKey", xJcicZ575.getTranKey());
					occursListB.putParam("OOBankId", xJcicZ575.getBankId());
					// occursListB.putParam("OOTranCode", xJcicZ575.getTranCode());
					occursListB.putParam("OOTranCode", "575");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ575.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ575.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ575.getOutJcicTxtDate() == iJcicDate && xJcicZ575.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8337");
						occursListB.putParam("OOHistoryTxCd", "L8067d");
						occursListB.putParam("OOCustId", xJcicZ575.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ575.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ575.getSubmitKey(), titaVo));
                        occursListB.putParam("OOApplyDate", xJcicZ575.getApplyDate());
                        occursListB.putParam("OOTranKey", xJcicZ575.getTranKey());
    					occursListB.putParam("OOBankId", xJcicZ575.getBankId());
//					occursListB.putParam("OOTranCode", xJcicZ575.getTranCode());
						occursListB.putParam("OOTranCode", "575");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ575.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ575.getActualFilingMark());
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