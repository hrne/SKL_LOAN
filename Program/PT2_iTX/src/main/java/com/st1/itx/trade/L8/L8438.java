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
import com.st1.itx.db.domain.JcicZ574;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ574LogService;
import com.st1.itx.db.service.JcicZ574Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8438")
@Scope("prototype")
/**
 * 聯徵產品檔案匯出作業(574)
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8438 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ574Service sJcicZ574Service;
	@Autowired
	public JcicZ574LogService sJcicZ574LogService;
	@Autowired
	public CdCodeService iCdCodeService;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);
        this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ574> sJcicZ574 = null;
		sJcicZ574 = sJcicZ574Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ574     = " + sJcicZ574.getSize());
		if (sJcicZ574 != null) {
			for (JcicZ574 xJcicZ574 : sJcicZ574) {
				if ((iSubmitType == 1 && xJcicZ574.getOutJcicTxtDate() == 0)
						|| (iSubmitType == 3 && xJcicZ574.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8336");
					occursListB.putParam("OOHistoryTxCd", "L8066");
					occursListB.putParam("OOCustId", xJcicZ574.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ574.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ574.getSubmitKey(), titaVo));
					occursListB.putParam("OOTranKey", xJcicZ574.getTranKey());
					occursListB.putParam("OOApplyDate", xJcicZ574.getApplyDate());
					// occursListB.putParam("OOTranCode", xJcicZ574.getTranCode());
					occursListB.putParam("OOTranCode", "574");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ574.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ574.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ574.getOutJcicTxtDate() == iJcicDate && xJcicZ574.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8336");
						occursListB.putParam("OOHistoryTxCd", "L8066d");
						occursListB.putParam("OOCustId", xJcicZ574.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ574.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ574.getSubmitKey(), titaVo));
						occursListB.putParam("OOTranKey", xJcicZ574.getTranKey());
						occursListB.putParam("OOApplyDate", xJcicZ574.getApplyDate());
//					occursListB.putParam("OOTranCode", xJcicZ574.getTranCode());
						occursListB.putParam("OOTranCode", "574");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ574.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ574.getActualFilingMark());
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