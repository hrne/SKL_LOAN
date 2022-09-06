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
import com.st1.itx.db.domain.JcicZ052;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ052LogService;
import com.st1.itx.db.service.JcicZ052Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8415")
@Scope("prototype")
/**
 * 變更還款方案結案通知資料(052)
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8415 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ052Service sJcicZ052Service;
	@Autowired
	public JcicZ052LogService sJcicZ052LogService;
	@Autowired
	public CdCodeService iCdCodeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ052> sJcicZ052 = null;
		sJcicZ052 = sJcicZ052Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ052     = " + sJcicZ052.getSize());
		if (sJcicZ052 != null) {
			for (JcicZ052 xJcicZ052 : sJcicZ052) {
				if ((iSubmitType == 1 && xJcicZ052.getOutJcicTxtDate() == 0) || (iSubmitType == 3 && xJcicZ052.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8313");
					occursListB.putParam("OOHistoryTxCd", "L8043");
					occursListB.putParam("OOCustId", xJcicZ052.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ052.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ052.getSubmitKey(), titaVo));
					occursListB.putParam("OORcDate", xJcicZ052.getRcDate());
					occursListB.putParam("OOTranKey", xJcicZ052.getTranKey());
					// occursListB.putParam("OOTranCode", xJcicZ052.getTranCode());
					occursListB.putParam("OOTranCode", "052");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ052.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ052.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ052.getOutJcicTxtDate() == iJcicDate && xJcicZ052.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8313");
						occursListB.putParam("OOHistoryTxCd", "L8043");
						occursListB.putParam("OOCustId", xJcicZ052.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ052.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ052.getSubmitKey(), titaVo));
						occursListB.putParam("OORcDate", xJcicZ052.getRcDate());
						occursListB.putParam("OOTranKey", xJcicZ052.getTranKey());
//					occursListB.putParam("OOTranCode", xJcicZ052.getTranCode());
						occursListB.putParam("OOTranCode", "052");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ052.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ052.getActualFilingMark());
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