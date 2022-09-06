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
import com.st1.itx.db.domain.JcicZ446;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ446LogService;
import com.st1.itx.db.service.JcicZ446Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8428")
@Scope("prototype")
/**
 * 前置調解單獨全數受清償資料(446)
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8428 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ446Service sJcicZ446Service;
	@Autowired
	public JcicZ446LogService sJcicZ446LogService;
	@Autowired
	public CdCodeService iCdCodeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ446> sJcicZ446 = null;
		sJcicZ446 = sJcicZ446Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ446     = " + sJcicZ446.getSize());
		if (sJcicZ446 != null) {
			for (JcicZ446 xJcicZ446 : sJcicZ446) {
				if ((iSubmitType == 1 && xJcicZ446.getOutJcicTxtDate() == 0) || (iSubmitType == 3 && xJcicZ446.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8326");
					occursListB.putParam("OOHistoryTxCd", "L8056");
					occursListB.putParam("OOCustId", xJcicZ446.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ446.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ446.getSubmitKey(), titaVo));
					occursListB.putParam("OOTranKey", xJcicZ446.getTranKey());
					occursListB.putParam("OOApplyDate", xJcicZ446.getApplyDate());
					occursListB.putParam("OOCourtCode", xJcicZ446.getCourtCode());
					// occursListB.putParam("OOTranCode", xJcicZ446.getTranCode());
					occursListB.putParam("OOTranCode", "446");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ446.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ446.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ446.getOutJcicTxtDate() == iJcicDate && xJcicZ446.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8326");
						occursListB.putParam("OOHistoryTxCd", "L8056d");
						occursListB.putParam("OOCustId", xJcicZ446.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ446.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ446.getSubmitKey(), titaVo));
						occursListB.putParam("OOTranKey", xJcicZ446.getTranKey());
						occursListB.putParam("OOApplyDate", xJcicZ446.getApplyDate());
						occursListB.putParam("OOCourtCode", xJcicZ446.getCourtCode());
//								occursListB.putParam("OOTranCode", xJcicZ446.getTranCode());
						occursListB.putParam("OOTranCode", "446");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ446.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ446.getActualFilingMark());
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