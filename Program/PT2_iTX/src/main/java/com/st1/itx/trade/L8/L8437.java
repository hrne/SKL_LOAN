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
import com.st1.itx.db.domain.JcicZ573;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ573LogService;
import com.st1.itx.db.service.JcicZ573Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8437")
@Scope("prototype")
/**
 * 聯徵產品檔案匯出作業(573)
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8437 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ573Service sJcicZ573Service;
	@Autowired
	public JcicZ573LogService sJcicZ573LogService;
	@Autowired
	public CdCodeService iCdCodeService;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);
        this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ573> sJcicZ573 = null;
		sJcicZ573 = sJcicZ573Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ573     = " + sJcicZ573.getSize());
		if (sJcicZ573 != null) {
			for (JcicZ573 xJcicZ573 : sJcicZ573) {
				if ((iSubmitType == 1 && xJcicZ573.getOutJcicTxtDate() == 0)
						|| (iSubmitType == 3 && xJcicZ573.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8335");
					occursListB.putParam("OOHistoryTxCd", "L8065");
					occursListB.putParam("OOCustId", xJcicZ573.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ573.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ573.getSubmitKey(), titaVo));
                    occursListB.putParam("OOApplyDate", xJcicZ573.getApplyDate());
                    occursListB.putParam("OOTranKey", xJcicZ573.getTranKey());
					occursListB.putParam("OOPayDate", xJcicZ573.getPayDate());
					// occursListB.putParam("OOTranCode", xJcicZ573.getTranCode());
					occursListB.putParam("OOTranCode", "573");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ573.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ573.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ573.getOutJcicTxtDate() == iJcicDate && xJcicZ573.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8335");
						occursListB.putParam("OOHistoryTxCd", "L8065d");
						occursListB.putParam("OOCustId", xJcicZ573.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ573.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ573.getSubmitKey(), titaVo));
                        occursListB.putParam("OOApplyDate", xJcicZ573.getApplyDate());
                        occursListB.putParam("OOTranKey", xJcicZ573.getTranKey());
    					occursListB.putParam("OOPayDate", xJcicZ573.getPayDate());
//					occursListB.putParam("OOTranCode", xJcicZ573.getTranCode());
						occursListB.putParam("OOTranCode", "573");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ573.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ573.getActualFilingMark());
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