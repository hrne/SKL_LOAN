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
import com.st1.itx.db.domain.JcicZ448;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ448LogService;
import com.st1.itx.db.service.JcicZ448Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8430")
@Scope("prototype")
/**
 * 前置調解單獨全數受清償資料(448)
 * 
 * @author Luisito
 * @version 1.0.0
 */
public class L8430 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ448Service sJcicZ448Service;
	@Autowired
	public JcicZ448LogService sJcicZ448LogService;
	@Autowired
	public CdCodeService iCdCodeService;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ448> sJcicZ448 = null;
		sJcicZ448 = sJcicZ448Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ448     = " + sJcicZ448.getSize());
		if (sJcicZ448 != null) {
			for (JcicZ448 xJcicZ448 : sJcicZ448) {
				if ((iSubmitType == 1 && xJcicZ448.getOutJcicTxtDate() == 0)
						|| (iSubmitType == 3 && xJcicZ448.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8328");
					occursListB.putParam("OOHistoryTxCd", "L8058");
					occursListB.putParam("OOCustId", xJcicZ448.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ448.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ448.getSubmitKey(), titaVo));
                    occursListB.putParam("OOApplyDate", xJcicZ448.getApplyDate());
                    occursListB.putParam("OOTranKey", xJcicZ448.getTranKey());
					occursListB.putParam("OOCourtCode", xJcicZ448.getCourtCode());
                    occursListB.putParam("OOMaxMainCode", xJcicZ448.getMaxMainCode());
                    occursListB.putParam("OOMaxMainCodeX", dealBankName(xJcicZ448.getMaxMainCode(), titaVo));
                    // occursListB.putParam("OOTranCode", xJcicZ448.getTranCode());
					occursListB.putParam("OOTranCode", "448");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ448.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ448.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ448.getOutJcicTxtDate() == iJcicDate && xJcicZ448.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8328");
						occursListB.putParam("OOHistoryTxCd", "L8058d");
						occursListB.putParam("OOCustId", xJcicZ448.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ448.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ448.getSubmitKey(), titaVo));
						occursListB.putParam("OOTranKey", xJcicZ448.getTranKey());
						occursListB.putParam("OOApplyDate", xJcicZ448.getApplyDate());
                        occursListB.putParam("OOCourtCode", xJcicZ448.getCourtCode());
                        occursListB.putParam("OOMaxMainCode", xJcicZ448.getMaxMainCode());
                        occursListB.putParam("OOMaxMainCodeX", dealBankName(xJcicZ448.getMaxMainCode(), titaVo));
//					occursListB.putParam("OOTranCode", xJcicZ448.getTranCode());
						occursListB.putParam("OOTranCode", "448");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ448.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ448.getActualFilingMark());
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