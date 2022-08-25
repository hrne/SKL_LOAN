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
import com.st1.itx.db.domain.JcicZ062;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ062LogService;
import com.st1.itx.db.service.JcicZ062Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8422")
@Scope("prototype")
/**
 * 金融機構無擔保債務變更還款條件協議資料(062)
 * 
 * @author Luisito
 * @version 1.0.0
 */
public class L8422 extends TradeBuffer {
	
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ062Service sJcicZ062Service;
	@Autowired
	public JcicZ062LogService sJcicZ062LogService;
	@Autowired
	public CdCodeService iCdCodeService;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);
        this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ062> sJcicZ062 = null;
		sJcicZ062 = sJcicZ062Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ062     = " + sJcicZ062.getSize());
		if (sJcicZ062 != null) {
			for (JcicZ062 xJcicZ062 : sJcicZ062) {
				if ((iSubmitType == 1 && xJcicZ062.getOutJcicTxtDate() == 0)
						|| (iSubmitType == 3 && xJcicZ062.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8320");
					occursListB.putParam("OOHistoryTxCd", "L8050");
					occursListB.putParam("OOCustId", xJcicZ062.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ062.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ062.getSubmitKey(), titaVo));
                    occursListB.putParam("OORcDate", xJcicZ062.getRcDate());
                    occursListB.putParam("OOTranKey", xJcicZ062.getTranKey());
                    occursListB.putParam("OOChangePayDate", xJcicZ062.getChangePayDate());
					// occursListB.putParam("OOTranCode", xJcicZ062.getTranCode());
					occursListB.putParam("OOTranCode", "062");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ062.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ062.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ062.getOutJcicTxtDate() == iJcicDate && xJcicZ062.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8320");
						occursListB.putParam("OOHistoryTxCd", "L8050d");
						occursListB.putParam("OOCustId", xJcicZ062.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ062.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ062.getSubmitKey(), titaVo));
                        occursListB.putParam("OORcDate", xJcicZ062.getRcDate());
                        occursListB.putParam("OOTranKey", xJcicZ062.getTranKey());
                        occursListB.putParam("OOChangePayDate", xJcicZ062.getChangePayDate());
//					occursListB.putParam("OOTranCode", xJcicZ062.getTranCode());
						occursListB.putParam("OOTranCode", "062");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ062.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ062.getActualFilingMark());
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