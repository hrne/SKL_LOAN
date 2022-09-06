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
import com.st1.itx.db.domain.JcicZ444;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ444LogService;
import com.st1.itx.db.service.JcicZ444Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8427")
@Scope("prototype")
/**
 * 前置調解單獨全數受清償資料(444)
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8427 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ444Service sJcicZ444Service;
	@Autowired
	public JcicZ444LogService sJcicZ444LogService;
	@Autowired
	public CdCodeService iCdCodeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ444> sJcicZ444 = null;
		sJcicZ444 = sJcicZ444Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ444     = " + sJcicZ444.getSize());
		if (sJcicZ444 != null) {
			for (JcicZ444 xJcicZ444 : sJcicZ444) {
				if ((iSubmitType == 1 && xJcicZ444.getOutJcicTxtDate() == 0) || (iSubmitType == 3 && xJcicZ444.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8325");
					occursListB.putParam("OOHistoryTxCd", "L8055");
					occursListB.putParam("OOCustId", xJcicZ444.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ444.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ444.getSubmitKey(), titaVo));
					occursListB.putParam("OOApplyDate", xJcicZ444.getApplyDate());
					occursListB.putParam("OOTranKey", xJcicZ444.getTranKey());
					occursListB.putParam("OOCourtCode", xJcicZ444.getCourtCode());
					// occursListB.putParam("OOTranCode", xJcicZ444.getTranCode());
					occursListB.putParam("OOTranCode", "444");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ444.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ444.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ444.getOutJcicTxtDate() == iJcicDate && xJcicZ444.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8325");
						occursListB.putParam("OOHistoryTxCd", "L8055d");
						occursListB.putParam("OOCustId", xJcicZ444.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ444.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ444.getSubmitKey(), titaVo));
						occursListB.putParam("OOTranKey", xJcicZ444.getTranKey());
						occursListB.putParam("OOApplyDate", xJcicZ444.getApplyDate());
						occursListB.putParam("OOCourtCode", xJcicZ444.getCourtCode());
//								occursListB.putParam("OOTranCode", xJcicZ444.getTranCode());
						occursListB.putParam("OOTranCode", "444");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ444.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ444.getActualFilingMark());
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