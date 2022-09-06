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
import com.st1.itx.db.domain.JcicZ571;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ571LogService;
import com.st1.itx.db.service.JcicZ571Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8435")
@Scope("prototype")
/**
 * 聯徵產品檔案匯出作業(571)
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8435 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ571Service sJcicZ571Service;
	@Autowired
	public JcicZ571LogService sJcicZ571LogService;
	@Autowired
	public CdCodeService iCdCodeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ571> sJcicZ571 = null;
		sJcicZ571 = sJcicZ571Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ571     = " + sJcicZ571.getSize());
		if (sJcicZ571 != null) {
			for (JcicZ571 xJcicZ571 : sJcicZ571) {
				if ((iSubmitType == 1 && xJcicZ571.getOutJcicTxtDate() == 0) || (iSubmitType == 3 && xJcicZ571.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8333");
					occursListB.putParam("OOHistoryTxCd", "L8063");
					occursListB.putParam("OOCustId", xJcicZ571.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ571.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ571.getSubmitKey(), titaVo));
					occursListB.putParam("OOApplyDate", xJcicZ571.getApplyDate());
					occursListB.putParam("OOTranKey", xJcicZ571.getTranKey());
					occursListB.putParam("OOBankId", xJcicZ571.getBankId());
					// occursListB.putParam("OOTranCode", xJcicZ571.getTranCode());
					occursListB.putParam("OOTranCode", "571");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ571.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ571.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ571.getOutJcicTxtDate() == iJcicDate && xJcicZ571.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8333");
						occursListB.putParam("OOHistoryTxCd", "L8063d");
						occursListB.putParam("OOCustId", xJcicZ571.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ571.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ571.getSubmitKey(), titaVo));
						occursListB.putParam("OOApplyDate", xJcicZ571.getApplyDate());
						occursListB.putParam("OOTranKey", xJcicZ571.getTranKey());
						occursListB.putParam("OOBankId", xJcicZ571.getBankId());
//					occursListB.putParam("OOTranCode", xJcicZ571.getTranCode());
						occursListB.putParam("OOTranCode", "571");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ571.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ571.getActualFilingMark());
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