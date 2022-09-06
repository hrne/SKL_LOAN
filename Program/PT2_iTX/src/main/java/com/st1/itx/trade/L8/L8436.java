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
import com.st1.itx.db.domain.JcicZ572;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ572LogService;
import com.st1.itx.db.service.JcicZ572Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8436")
@Scope("prototype")
/**
 * 聯徵產品檔案匯出作業(572)
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8436 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ572Service sJcicZ572Service;
	@Autowired
	public JcicZ572LogService sJcicZ572LogService;
	@Autowired
	public CdCodeService iCdCodeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ572> sJcicZ572 = null;
		sJcicZ572 = sJcicZ572Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ572     = " + sJcicZ572.getSize());
		if (sJcicZ572 != null) {
			for (JcicZ572 xJcicZ572 : sJcicZ572) {
				if ((iSubmitType == 1 && xJcicZ572.getOutJcicTxtDate() == 0) || (iSubmitType == 3 && xJcicZ572.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8334");
					occursListB.putParam("OOHistoryTxCd", "L8064");
					occursListB.putParam("OOCustId", xJcicZ572.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ572.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ572.getSubmitKey(), titaVo));
					occursListB.putParam("OOApplyDate", xJcicZ572.getApplyDate());
					occursListB.putParam("OOPayDate", xJcicZ572.getPayDate());
					occursListB.putParam("OOTranKey", xJcicZ572.getTranKey());
					occursListB.putParam("OOBankId", xJcicZ572.getBankId());
					// occursListB.putParam("OOTranCode", xJcicZ572.getTranCode());
					occursListB.putParam("OOTranCode", "572");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ572.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ572.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ572.getOutJcicTxtDate() == iJcicDate && xJcicZ572.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8334");
						occursListB.putParam("OOHistoryTxCd", "L8064d");
						occursListB.putParam("OOCustId", xJcicZ572.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ572.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ572.getSubmitKey(), titaVo));
						occursListB.putParam("OOApplyDate", xJcicZ572.getApplyDate());
						occursListB.putParam("OOPayDate", xJcicZ572.getPayDate());
						occursListB.putParam("OOTranKey", xJcicZ572.getTranKey());
						occursListB.putParam("OOBankId", xJcicZ572.getBankId());
//					occursListB.putParam("OOTranCode", xJcicZ572.getTranCode());
						occursListB.putParam("OOTranCode", "572");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ572.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ572.getActualFilingMark());
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