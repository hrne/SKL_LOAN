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
import com.st1.itx.db.domain.JcicZ061;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ061LogService;
import com.st1.itx.db.service.JcicZ061Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8421")
@Scope("prototype")
/**
 * 聯徵產品檔案匯出作業(061)
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8421 extends TradeBuffer {
	
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ061Service sJcicZ061Service;
	@Autowired
	public JcicZ061LogService sJcicZ061LogService;
	@Autowired
	public CdCodeService iCdCodeService;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);
        this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ061> sJcicZ061 = null;
		sJcicZ061 = sJcicZ061Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ061     = " + sJcicZ061.getSize());
		if (sJcicZ061 != null) {
			for (JcicZ061 xJcicZ061 : sJcicZ061) {
				if ((iSubmitType == 1 && xJcicZ061.getOutJcicTxtDate() == 0)
						|| (iSubmitType == 3 && xJcicZ061.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8319");
					occursListB.putParam("OOHistoryTxCd", "L8049");
					occursListB.putParam("OOCustId", xJcicZ061.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ061.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ061.getSubmitKey(), titaVo));
                    occursListB.putParam("OORcDate", xJcicZ061.getRcDate());
                    occursListB.putParam("OOMaxMainCode",xJcicZ061.getMaxMainCode());
                    occursListB.putParam("OOMaxMainCodeX", dealBankName(xJcicZ061.getMaxMainCode(), titaVo));
                    occursListB.putParam("OOTranKey", xJcicZ061.getTranKey());
                    occursListB.putParam("OOChangePayDate", xJcicZ061.getChangePayDate());
					// occursListB.putParam("OOTranCode", xJcicZ061.getTranCode());
					occursListB.putParam("OOTranCode", "061");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ061.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ061.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ061.getOutJcicTxtDate() == iJcicDate && xJcicZ061.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8319");
						occursListB.putParam("OOHistoryTxCd", "L8049d");
						occursListB.putParam("OOCustId", xJcicZ061.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ061.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ061.getSubmitKey(), titaVo));
                        occursListB.putParam("OORcDate", xJcicZ061.getRcDate());
                        occursListB.putParam("OOMaxMainCode",xJcicZ061.getMaxMainCode());
                        occursListB.putParam("OOMaxMainCodeX", dealBankName(xJcicZ061.getMaxMainCode(), titaVo));
                        occursListB.putParam("OOTranKey", xJcicZ061.getTranKey());
                        occursListB.putParam("OOChangePayDate", xJcicZ061.getChangePayDate());
//					occursListB.putParam("OOTranCode", xJcicZ061.getTranCode());
						occursListB.putParam("OOTranCode", "061");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ061.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ061.getActualFilingMark());
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