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
import com.st1.itx.db.domain.JcicZ040;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ040LogService;
import com.st1.itx.db.service.JcicZ040Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8403")
@Scope("prototype")
/**
 * 同意報送例外處裡檔案(040)
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8403 extends TradeBuffer {

	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ040Service sJcicZ040Service;
	@Autowired
	public JcicZ040LogService sJcicZ040LogService;
	@Autowired
	public CdCodeService iCdCodeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();
		this.limit = 5000;
		Slice<JcicZ040> sJcicZ040 = null;
		sJcicZ040 = sJcicZ040Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ040     = " + sJcicZ040.getSize());
		this.info("L8403 iSubmitType  = " + iSubmitType);
		if (sJcicZ040 != null) {
			for (JcicZ040 xJcicZ040 : sJcicZ040) {
					if ((iSubmitType == 1 && xJcicZ040.getOutJcicTxtDate() == 0) || 
							(iSubmitType == 3 && xJcicZ040.getActualFilingDate() == 0)) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8301");
						occursListB.putParam("OOHistoryTxCd", "L8031");
						occursListB.putParam("OOCustId", xJcicZ040.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ040.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ040.getSubmitKey(), titaVo));
						occursListB.putParam("OORcDate", xJcicZ040.getRcDate());
						occursListB.putParam("OOTranKey", xJcicZ040.getTranKey());
				//		occursListB.putParam("OOTranCode", xJcicZ040.getTranCode());
						occursListB.putParam("OOTranCode", "040");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ040.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ040.getActualFilingMark());
						this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ040.getOutJcicTxtDate() == iJcicDate && xJcicZ040.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8301");
						occursListB.putParam("OOHistoryTxCd", "L8031");
						occursListB.putParam("OOCustId", xJcicZ040.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ040.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ040.getSubmitKey(), titaVo));
						occursListB.putParam("OORcDate", xJcicZ040.getRcDate());
						occursListB.putParam("OOTranKey", xJcicZ040.getTranKey());
//					occursListB.putParam("OOTranCode", xJcicZ040.getTranCode());
						occursListB.putParam("OOTranCode", "040");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ040.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ040.getActualFilingMark());
						this.info("OOCustId    = " + xJcicZ040.getCustId());
						this.info("OOSubmitKey  = " + xJcicZ040.getSubmitKey());
						this.info("OORcDate   = " + xJcicZ040.getRcDate());
						this.info("OOTranKey  = " + xJcicZ040.getTranKey());
						this.info("OOActualFilingDate  = " + iActualFilingDate);
						this.info("OOActualFilingMark  = " + xJcicZ040.getActualFilingMark());
						this.info("TranCode    = " + "040");
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
