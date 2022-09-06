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
import com.st1.itx.db.domain.JcicZ047;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ047LogService;
import com.st1.itx.db.service.JcicZ047Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8410")
@Scope("prototype")
/**
 * 變更還款方案結案通知資料(047)
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8410 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ047Service sJcicZ047Service;
	@Autowired
	public JcicZ047LogService sJcicZ047LogService;
	@Autowired
	public CdCodeService iCdCodeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8410 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ047> sJcicZ047 = null;
		sJcicZ047 = sJcicZ047Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ047     = " + sJcicZ047.getSize());
		if (sJcicZ047 != null) {
			for (JcicZ047 xJcicZ047 : sJcicZ047) {
				if ((iSubmitType == 1 && xJcicZ047.getOutJcicTxtDate() == 0) || (iSubmitType == 3 && xJcicZ047.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8308");
					occursListB.putParam("OOHistoryTxCd", "L8038");
					occursListB.putParam("OOCustId", xJcicZ047.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ047.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ047.getSubmitKey(), titaVo));
					occursListB.putParam("OORcDate", xJcicZ047.getRcDate());
					occursListB.putParam("OOTranKey", xJcicZ047.getTranKey());
					// occursListB.putParam("OOTranCode", xJcicZ047.getTranCode());
					occursListB.putParam("OOTranCode", "047");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ047.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ047.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ047.getOutJcicTxtDate() == iJcicDate && xJcicZ047.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8308");
						occursListB.putParam("OOHistoryTxCd", "L8038");
						occursListB.putParam("OOCustId", xJcicZ047.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ047.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ047.getSubmitKey(), titaVo));
						occursListB.putParam("OORcDate", xJcicZ047.getRcDate());
						occursListB.putParam("OOTranKey", xJcicZ047.getTranKey());
//					occursListB.putParam("OOTranCode", xJcicZ047.getTranCode());
						occursListB.putParam("OOTranCode", "047");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ047.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ047.getActualFilingMark());
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
