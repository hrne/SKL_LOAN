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
import com.st1.itx.db.domain.JcicZ041;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ041LogService;
import com.st1.itx.db.service.JcicZ041Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8404")
@Scope("prototype")
/**
 * 同意報送例外處裡檔案(041)
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8404 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ041Service sJcicZ041Service;
	@Autowired
	public JcicZ041LogService sJcicZ041LogService;
	@Autowired
	public CdCodeService iCdCodeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8404 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ041> sJcicZ041 = null;
		sJcicZ041 = sJcicZ041Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ041     = " + sJcicZ041.getSize());
		if (sJcicZ041 != null) {
			for (JcicZ041 xJcicZ041 : sJcicZ041) {
				if ((iSubmitType == 1 && xJcicZ041.getOutJcicTxtDate() == 0) || (iSubmitType == 3 && xJcicZ041.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8302");
					occursListB.putParam("OOHistoryTxCd", "L8032");
					occursListB.putParam("OOCustId", xJcicZ041.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ041.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ041.getSubmitKey(), titaVo));
					occursListB.putParam("OORcDate", xJcicZ041.getRcDate());
					occursListB.putParam("OOTranKey", xJcicZ041.getTranKey());
					// occursListB.putParam("OOTranCode", xJcicZ041.getTranCode());
					occursListB.putParam("OOTranCode", "041");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ041.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ041.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ041.getOutJcicTxtDate() == iJcicDate && xJcicZ041.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8302");
						occursListB.putParam("OOHistoryTxCd", "L8032");
						occursListB.putParam("OOCustId", xJcicZ041.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ041.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ041.getSubmitKey(), titaVo));
						occursListB.putParam("OORcDate", xJcicZ041.getRcDate());
						occursListB.putParam("OOTranKey", xJcicZ041.getTranKey());
//					occursListB.putParam("OOTranCode", xJcicZ041.getTranCode());
						occursListB.putParam("OOTranCode", "041");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ041.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ041.getActualFilingMark());
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