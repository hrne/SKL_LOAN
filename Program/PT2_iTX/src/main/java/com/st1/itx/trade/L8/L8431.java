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
import com.st1.itx.db.domain.JcicZ450;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ450LogService;
import com.st1.itx.db.service.JcicZ450Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8431")
@Scope("prototype")
/**
 * 前置調解單獨全數受清償資料(450)
 * 
 * @author Luisito
 * @version 1.0.0
 */
public class L8431 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ450Service sJcicZ450Service;
	@Autowired
	public JcicZ450LogService sJcicZ450LogService;
	@Autowired
	public CdCodeService iCdCodeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ450> sJcicZ450 = null;
		sJcicZ450 = sJcicZ450Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ450     = " + sJcicZ450.getSize());
		if (sJcicZ450 != null) {
			for (JcicZ450 xJcicZ450 : sJcicZ450) {
				if ((iSubmitType == 1 && xJcicZ450.getOutJcicTxtDate() == 0) || (iSubmitType == 3 && xJcicZ450.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8329");
					occursListB.putParam("OOHistoryTxCd", "L8059");
					occursListB.putParam("OOCustId", xJcicZ450.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ450.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ450.getSubmitKey(), titaVo));
					occursListB.putParam("OOTranKey", xJcicZ450.getTranKey());
					occursListB.putParam("OOApplyDate", xJcicZ450.getApplyDate());
					occursListB.putParam("OOCourtCode", xJcicZ450.getCourtCode());
					occursListB.putParam("OOPayDate", xJcicZ450.getPayDate());
					// occursListB.putParam("OOTranCode", xJcicZ450.getTranCode());
					occursListB.putParam("OOTranCode", "450");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ450.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ450.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ450.getOutJcicTxtDate() == iJcicDate && xJcicZ450.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8329");
						occursListB.putParam("OOHistoryTxCd", "L8059d");
						occursListB.putParam("OOCustId", xJcicZ450.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ450.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ450.getSubmitKey(), titaVo));
						occursListB.putParam("OOTranKey", xJcicZ450.getTranKey());
						occursListB.putParam("OOApplyDate", xJcicZ450.getApplyDate());
						occursListB.putParam("OOCourtCode", xJcicZ450.getCourtCode());
						occursListB.putParam("OOPayDate", xJcicZ450.getPayDate());
//					occursListB.putParam("OOTranCode", xJcicZ450.getTranCode());
						occursListB.putParam("OOTranCode", "450");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ450.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ450.getActualFilingMark());
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