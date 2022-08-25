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
import com.st1.itx.db.domain.JcicZ454;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ454LogService;
import com.st1.itx.db.service.JcicZ454Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8433")
@Scope("prototype")
/**
 * 前置調解單獨全數受清償資料(454)
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8433 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ454Service sJcicZ454Service;
	@Autowired
	public JcicZ454LogService sJcicZ454LogService;
	@Autowired
	public CdCodeService iCdCodeService;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);
        this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ454> sJcicZ454 = null;
		sJcicZ454 = sJcicZ454Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ454     = " + sJcicZ454.getSize());
		if (sJcicZ454 != null) {
			for (JcicZ454 xJcicZ454 : sJcicZ454) {
				if ((iSubmitType == 1 && xJcicZ454.getOutJcicTxtDate() == 0)
						|| (iSubmitType == 3 && xJcicZ454.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8331");
					occursListB.putParam("OOHistoryTxCd", "L8061");
					occursListB.putParam("OOCustId", xJcicZ454.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ454.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ454.getSubmitKey(), titaVo));
                    occursListB.putParam("OOApplyDate", xJcicZ454.getApplyDate());
                    occursListB.putParam("OOTranKey", xJcicZ454.getTranKey());
					occursListB.putParam("OOCourtCode", xJcicZ454.getCourtCode());
                    occursListB.putParam("OOMaxMainCode", xJcicZ454.getMaxMainCode());
                    occursListB.putParam("OOMaxMainCodeX", dealBankName(xJcicZ454.getMaxMainCode(), titaVo));
                    // occursListB.putParam("OOTranCode", xJcicZ454.getTranCode());
					occursListB.putParam("OOTranCode", "454");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ454.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ454.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ454.getOutJcicTxtDate() == iJcicDate && xJcicZ454.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8331");
						occursListB.putParam("OOHistoryTxCd", "L8061d");
						occursListB.putParam("OOCustId", xJcicZ454.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ454.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ454.getSubmitKey(), titaVo));
						occursListB.putParam("OOTranKey", xJcicZ454.getTranKey());
						occursListB.putParam("OOApplyDate", xJcicZ454.getApplyDate());
                        occursListB.putParam("OOCourtCode", xJcicZ454.getCourtCode());
                        occursListB.putParam("OOMaxMainCode", xJcicZ454.getMaxMainCode());
                        occursListB.putParam("OOMaxMainCodeX", dealBankName(xJcicZ454.getMaxMainCode(), titaVo));
//					occursListB.putParam("OOTranCode", xJcicZ454.getTranCode());
						occursListB.putParam("OOTranCode", "454");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ454.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ454.getActualFilingMark());
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