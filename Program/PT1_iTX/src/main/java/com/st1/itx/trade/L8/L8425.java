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
import com.st1.itx.db.domain.JcicZ442;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ442LogService;
import com.st1.itx.db.service.JcicZ442Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8425")
@Scope("prototype")
/**
 * 前置調解無擔保債務分配表資料(442)
 * 
 * @author Luisito
 * @version 1.0.0
 */
public class L8425 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ442Service sJcicZ442Service;
	@Autowired
	public JcicZ442LogService sJcicZ442LogService;
	@Autowired
	public CdCodeService iCdCodeService;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ442> sJcicZ442 = null;
		sJcicZ442 = sJcicZ442Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ442     = " + sJcicZ442.getSize());
		if (sJcicZ442 != null) {
			for (JcicZ442 xJcicZ442 : sJcicZ442) {
				if ((iSubmitType == 1 && xJcicZ442.getOutJcicTxtDate() == 0)
						|| (iSubmitType == 3 && xJcicZ442.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8323");
					occursListB.putParam("OOHistoryTxCd", "L8053");
					occursListB.putParam("OOCustId", xJcicZ442.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ442.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ442.getSubmitKey(), titaVo));
                    occursListB.putParam("OOApplyDate", xJcicZ442.getApplyDate());
                    occursListB.putParam("OOTranKey", xJcicZ442.getTranKey());
					occursListB.putParam("OOCourtCode", xJcicZ442.getCourtCode());
                    occursListB.putParam("OOMaxMainCode", xJcicZ442.getMaxMainCode());
                    occursListB.putParam("OOMaxMainCodeX", dealBankName(xJcicZ442.getMaxMainCode(), titaVo));
					// occursListB.putParam("OOTranCode", xJcicZ442.getTranCode());
					occursListB.putParam("OOTranCode", "442");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ442.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ442.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ442.getOutJcicTxtDate() == iJcicDate && xJcicZ442.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8323");
						occursListB.putParam("OOHistoryTxCd", "L8053d");
						occursListB.putParam("OOCustId", xJcicZ442.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ442.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ442.getSubmitKey(), titaVo));
                        occursListB.putParam("OOApplyDate", xJcicZ442.getApplyDate());
                        occursListB.putParam("OOTranKey", xJcicZ442.getTranKey());
						occursListB.putParam("OOCourtCode", xJcicZ442.getCourtCode());
                        occursListB.putParam("OOMaxMainCode", xJcicZ442.getMaxMainCode());
                        occursListB.putParam("OOMaxMainCodeX", dealBankName(xJcicZ442.getMaxMainCode(), titaVo));
    					//occursListB.putParam("OOTranCode", xJcicZ442.getTranCode());
						occursListB.putParam("OOTranCode", "442");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ442.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ442.getActualFilingMark());
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