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
import com.st1.itx.db.domain.JcicZ042;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ042LogService;
import com.st1.itx.db.service.JcicZ042Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8405")
@Scope("prototype")
/**
 * 同意報送例外處裡檔案(042)
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8405 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ042Service sJcicZ042Service;
	@Autowired
	public JcicZ042LogService sJcicZ042LogService;
	@Autowired
	public CdCodeService iCdCodeService;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8405 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();
		this.limit = 1000;
		Slice<JcicZ042> sJcicZ042 = null;
		sJcicZ042 = sJcicZ042Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ042     = " + sJcicZ042.getSize());
		this.info("L8405 iSubmitType  = " + iSubmitType);
		if (sJcicZ042 != null) {
			for (JcicZ042 xJcicZ042 : sJcicZ042) {
				if ((iSubmitType == 1 && xJcicZ042.getOutJcicTxtDate() == 0)
						|| (iSubmitType == 3 && xJcicZ042.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8303");
					occursListB.putParam("OOHistoryTxCd", "L8033");
					occursListB.putParam("OOCustId", xJcicZ042.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ042.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ042.getSubmitKey(), titaVo));
					occursListB.putParam("OORcDate", xJcicZ042.getRcDate());
					occursListB.putParam("OOMaxMainCode", xJcicZ042.getMaxMainCode());
					occursListB.putParam("OOMaxMainCodeX", dealBankName(xJcicZ042.getMaxMainCode(), titaVo));
					occursListB.putParam("OOTranKey", xJcicZ042.getTranKey());
					// occursListB.putParam("OOTranCode", xJcicZ042.getTranCode());
					occursListB.putParam("OOTranCode", "042");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ042.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ042.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ042.getOutJcicTxtDate() == iJcicDate && xJcicZ042.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8303");
						occursListB.putParam("OOHistoryTxCd", "L8033");
						occursListB.putParam("OOCustId", xJcicZ042.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ042.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ042.getSubmitKey(), titaVo));
						occursListB.putParam("OORcDate", xJcicZ042.getRcDate());
						occursListB.putParam("OOMaxMainCode", xJcicZ042.getMaxMainCode());
						occursListB.putParam("OOMaxMainCodeX", dealBankName(xJcicZ042.getMaxMainCode(), titaVo));
						occursListB.putParam("OOTranKey", xJcicZ042.getTranKey());
//					occursListB.putParam("OOTranCode", xJcicZ042.getTranCode());
						occursListB.putParam("OOTranCode", "042");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ042.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ042.getActualFilingMark());
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