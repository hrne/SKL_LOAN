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
import com.st1.itx.db.domain.JcicZ045;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ045LogService;
import com.st1.itx.db.service.JcicZ045Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8408")
@Scope("prototype")
/**
 * 變更還款方案結案通知資料(045)
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8408 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ045Service sJcicZ045Service;
	@Autowired
	public JcicZ045LogService sJcicZ045LogService;
	@Autowired
	public CdCodeService iCdCodeService;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8408 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ045> sJcicZ045 = null;
		sJcicZ045 = sJcicZ045Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ045     = " + sJcicZ045.getSize());
		if (sJcicZ045 != null) {
			for (JcicZ045 xJcicZ045 : sJcicZ045) {
				if ((iSubmitType == 1 && xJcicZ045.getOutJcicTxtDate() == 0)
						|| (iSubmitType == 3 && xJcicZ045.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8306");
					occursListB.putParam("OOHistoryTxCd", "L8036");
					occursListB.putParam("OOCustId", xJcicZ045.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ045.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ045.getSubmitKey(), titaVo));
					occursListB.putParam("OORcDate", xJcicZ045.getRcDate());
                    occursListB.putParam("OOMaxMainCode",xJcicZ045.getMaxMainCode());
                    occursListB.putParam("OOMaxMainCodeX", dealBankName(xJcicZ045.getMaxMainCode(), titaVo));
                    occursListB.putParam("OOTranKey", xJcicZ045.getTranKey());
					// occursListB.putParam("OOTranCode", xJcicZ045.getTranCode());
					occursListB.putParam("OOTranCode", "045");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ045.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ045.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ045.getOutJcicTxtDate() == iJcicDate && xJcicZ045.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8306");
						occursListB.putParam("OOHistoryTxCd", "L8036d");
						occursListB.putParam("OOCustId", xJcicZ045.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ045.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ045.getSubmitKey(), titaVo));
						occursListB.putParam("OORcDate", xJcicZ045.getRcDate());
                        occursListB.putParam("OOMaxMainCode",xJcicZ045.getMaxMainCode());
                        occursListB.putParam("OOMaxMainCodeX", dealBankName(xJcicZ045.getMaxMainCode(), titaVo));
                        occursListB.putParam("OOTranKey", xJcicZ045.getTranKey());
//					occursListB.putParam("OOTranCode", xJcicZ045.getTranCode());
						occursListB.putParam("OOTranCode", "045");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ045.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ045.getActualFilingMark());
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