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
import com.st1.itx.db.domain.JcicZ051;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ051LogService;
import com.st1.itx.db.service.JcicZ051Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8414")
@Scope("prototype")
/**
 * 變更還款方案結案通知資料(051)
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8414 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ051Service sJcicZ051Service;
	@Autowired
	public JcicZ051LogService sJcicZ051LogService;
	@Autowired
	public CdCodeService iCdCodeService;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ051> sJcicZ051 = null;
		sJcicZ051 = sJcicZ051Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ051     = " + sJcicZ051.getSize());
		if (sJcicZ051 != null) {
			for (JcicZ051 xJcicZ051 : sJcicZ051) {
					if ((iSubmitType == 1 && xJcicZ051.getOutJcicTxtDate() == 0) || 
							(iSubmitType == 3 && xJcicZ051.getActualFilingDate() == 0)) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8312");
						occursListB.putParam("OOHistoryTxCd", "L8042");
						occursListB.putParam("OOCustId", xJcicZ051.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ051.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ051.getSubmitKey(), titaVo));
						occursListB.putParam("OORcDate", xJcicZ051.getRcDate());
                        occursListB.putParam("OODelayYM", xJcicZ051.getDelayYM());
						occursListB.putParam("OOTranKey", xJcicZ051.getTranKey());
				//		occursListB.putParam("OOTranCode", xJcicZ051.getTranCode());
						occursListB.putParam("OOTranCode", "051");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ051.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ051.getActualFilingMark());
						this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ051.getOutJcicTxtDate() == iJcicDate && xJcicZ051.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8312");
						occursListB.putParam("OOHistoryTxCd", "L8042");
						occursListB.putParam("OOCustId", xJcicZ051.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ051.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ051.getSubmitKey(), titaVo));
						occursListB.putParam("OORcDate", xJcicZ051.getRcDate());
                        occursListB.putParam("OODelayYM", xJcicZ051.getDelayYM());
						occursListB.putParam("OOTranKey", xJcicZ051.getTranKey());
//					occursListB.putParam("OOTranCode", xJcicZ051.getTranCode());
						occursListB.putParam("OOTranCode", "051");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ051.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ051.getActualFilingMark());
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