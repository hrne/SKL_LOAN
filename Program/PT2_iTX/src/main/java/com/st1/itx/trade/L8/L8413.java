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
import com.st1.itx.db.domain.JcicZ050;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ050LogService;
import com.st1.itx.db.service.JcicZ050Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8413")
@Scope("prototype")
/**
 * 變更還款方案結案通知資料(050)
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8413 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ050Service sJcicZ050Service;
	@Autowired
	public JcicZ050LogService sJcicZ050LogService;
	@Autowired
	public CdCodeService iCdCodeService;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();
		this.limit = Integer.MAX_VALUE;
		Slice<JcicZ050> sJcicZ050 = null;
		sJcicZ050 = sJcicZ050Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ050     = " + sJcicZ050.getSize());
		if (sJcicZ050 != null) {
			for (JcicZ050 xJcicZ050 : sJcicZ050) {
					if ((iSubmitType == 1 && xJcicZ050.getOutJcicTxtDate() == 0) || 
							(iSubmitType == 3 && xJcicZ050.getActualFilingDate() == 0)) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8311");
						occursListB.putParam("OOHistoryTxCd", "L8041");
						occursListB.putParam("OOCustId", xJcicZ050.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ050.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ050.getSubmitKey(), titaVo));
						occursListB.putParam("OORcDate", xJcicZ050.getRcDate());
                        occursListB.putParam("OOPayDate", xJcicZ050.getPayDate());
						occursListB.putParam("OOTranKey", xJcicZ050.getTranKey());
				//		occursListB.putParam("OOTranCode", xJcicZ050.getTranCode());
						occursListB.putParam("OOTranCode", "050");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ050.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ050.getActualFilingMark());
						this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ050.getOutJcicTxtDate() == iJcicDate && xJcicZ050.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8311");
						occursListB.putParam("OOHistoryTxCd", "L8041");
						occursListB.putParam("OOCustId", xJcicZ050.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ050.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ050.getSubmitKey(), titaVo));
						occursListB.putParam("OORcDate", xJcicZ050.getRcDate());
                        occursListB.putParam("OOPayDate", xJcicZ050.getPayDate());
						occursListB.putParam("OOTranKey", xJcicZ050.getTranKey());
//					occursListB.putParam("OOTranCode", xJcicZ050.getTranCode());
						occursListB.putParam("OOTranCode", "050");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ050.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ050.getActualFilingMark());
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