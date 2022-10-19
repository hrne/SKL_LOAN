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
import com.st1.itx.db.domain.JcicZ043;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ043LogService;
import com.st1.itx.db.service.JcicZ043Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8406")
@Scope("prototype")
/**
 * 同意報送例外處裡檔案(043)
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8406 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ043Service sJcicZ043Service;
	@Autowired
	public JcicZ043LogService sJcicZ043LogService;
	@Autowired
	public CdCodeService iCdCodeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8406 ");
		this.totaVo.init(titaVo);
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();
		this.limit = 1000;
		Slice<JcicZ043> sJcicZ043 = null;
		sJcicZ043 = sJcicZ043Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ043     = " + sJcicZ043.getSize());
		if (sJcicZ043 != null) {
			for (JcicZ043 xJcicZ043 : sJcicZ043) {
				if ((iSubmitType == 1 && xJcicZ043.getOutJcicTxtDate() == 0)
						|| (iSubmitType == 3 && xJcicZ043.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8304");
					occursListB.putParam("OOHistoryTxCd", "L8034");
					occursListB.putParam("OOCustId", xJcicZ043.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ043.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ043.getSubmitKey(), titaVo));
					occursListB.putParam("OORcDate", xJcicZ043.getRcDate());
					occursListB.putParam("OOMaxMainCode", xJcicZ043.getMaxMainCode());
					occursListB.putParam("OOAccount", xJcicZ043.getAccount());
					occursListB.putParam("OOTranKey", xJcicZ043.getTranKey());
					// occursListB.putParam("OOTranCode", xJcicZ043.getTranCode());
					occursListB.putParam("OOTranCode", "043");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ043.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ043.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ043.getOutJcicTxtDate() == iJcicDate && xJcicZ043.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8304");
						occursListB.putParam("OOHistoryTxCd", "L8034");
						occursListB.putParam("OOCustId", xJcicZ043.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ043.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ043.getSubmitKey(), titaVo));
						occursListB.putParam("OORcDate", xJcicZ043.getRcDate());
						occursListB.putParam("OOMaxMainCode", xJcicZ043.getMaxMainCode());
						occursListB.putParam("OOAccount", xJcicZ043.getAccount());
						occursListB.putParam("OOTranKey", xJcicZ043.getTranKey());
//					occursListB.putParam("OOTranCode", xJcicZ043.getTranCode());
						occursListB.putParam("OOTranCode", "043");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ043.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ043.getActualFilingMark());
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