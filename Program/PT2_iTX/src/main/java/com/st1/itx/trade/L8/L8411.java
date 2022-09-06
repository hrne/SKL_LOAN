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
import com.st1.itx.db.domain.JcicZ048;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ048LogService;
import com.st1.itx.db.service.JcicZ048Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8411")
@Scope("prototype")
/**
 * 變更還款方案結案通知資料(048)
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8411 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ048Service sJcicZ048Service;
	@Autowired
	public JcicZ048LogService sJcicZ048LogService;
	@Autowired
	public CdCodeService iCdCodeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ048> sJcicZ048 = null;
		sJcicZ048 = sJcicZ048Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ048     = " + sJcicZ048.getSize());
		if (sJcicZ048 != null) {
			for (JcicZ048 xJcicZ048 : sJcicZ048) {
				if ((iSubmitType == 1 && xJcicZ048.getOutJcicTxtDate() == 0) || (iSubmitType == 3 && xJcicZ048.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8309");
					occursListB.putParam("OOHistoryTxCd", "L8039");
					occursListB.putParam("OOCustId", xJcicZ048.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ048.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ048.getSubmitKey(), titaVo));
					occursListB.putParam("OORcDate", xJcicZ048.getRcDate());
					occursListB.putParam("OOTranKey", xJcicZ048.getTranKey());
					// occursListB.putParam("OOTranCode", xJcicZ048.getTranCode());
					occursListB.putParam("OOTranCode", "048");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ048.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ048.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ048.getOutJcicTxtDate() == iJcicDate && xJcicZ048.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8309");
						occursListB.putParam("OOHistoryTxCd", "L8039");
						occursListB.putParam("OOCustId", xJcicZ048.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ048.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ048.getSubmitKey(), titaVo));
						occursListB.putParam("OORcDate", xJcicZ048.getRcDate());
						occursListB.putParam("OOTranKey", xJcicZ048.getTranKey());
//					occursListB.putParam("OOTranCode", xJcicZ048.getTranCode());
						occursListB.putParam("OOTranCode", "048");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ048.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ048.getActualFilingMark());
						this.info("OOCustId    = " + xJcicZ048.getCustId());
						this.info("OOSubmitKey  = " + xJcicZ048.getSubmitKey());
						this.info("OORcDate   = " + xJcicZ048.getRcDate());
						this.info("OOTranKey  = " + xJcicZ048.getTranKey());
						this.info("OOActualFilingDate  = " + iActualFilingDate);
						this.info("OOActualFilingMark  = " + xJcicZ048.getActualFilingMark());
						this.info("TranCode    = " + "048");
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
