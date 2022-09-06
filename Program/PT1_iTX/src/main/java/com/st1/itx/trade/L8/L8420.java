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
import com.st1.itx.db.domain.JcicZ060;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ060LogService;
import com.st1.itx.db.service.JcicZ060Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8420")
@Scope("prototype")
/**
 * 聯徵產品檔案匯出作業(060)
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8420 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ060Service sJcicZ060Service;
	@Autowired
	public JcicZ060LogService sJcicZ060LogService;
	@Autowired
	public CdCodeService iCdCodeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ060> sJcicZ060 = null;
		sJcicZ060 = sJcicZ060Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ060     = " + sJcicZ060.getSize());
		if (sJcicZ060 != null) {
			for (JcicZ060 xJcicZ060 : sJcicZ060) {
				if ((iSubmitType == 1 && xJcicZ060.getOutJcicTxtDate() == 0) || (iSubmitType == 3 && xJcicZ060.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8318");
					occursListB.putParam("OOHistoryTxCd", "L8048");
					occursListB.putParam("OOCustId", xJcicZ060.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ060.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ060.getSubmitKey(), titaVo));
					occursListB.putParam("OORcDate", xJcicZ060.getRcDate());
					occursListB.putParam("OOChangePayDate", xJcicZ060.getChangePayDate());
					occursListB.putParam("OOTranKey", xJcicZ060.getTranKey());
					// occursListB.putParam("OOTranCode", xJcicZ060.getTranCode());
					occursListB.putParam("OOTranCode", "060");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ060.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ060.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ060.getOutJcicTxtDate() == iJcicDate && xJcicZ060.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8318");
						occursListB.putParam("OOHistoryTxCd", "L8048d");
						occursListB.putParam("OOCustId", xJcicZ060.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ060.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ060.getSubmitKey(), titaVo));
						occursListB.putParam("OOTranKey", xJcicZ060.getTranKey());
						occursListB.putParam("OORcDate", xJcicZ060.getRcDate());
						occursListB.putParam("OOChangePayDate", xJcicZ060.getChangePayDate());
//					occursListB.putParam("OOTranCode", xJcicZ060.getTranCode());
						occursListB.putParam("OOTranCode", "060");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ060.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ060.getActualFilingMark());
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