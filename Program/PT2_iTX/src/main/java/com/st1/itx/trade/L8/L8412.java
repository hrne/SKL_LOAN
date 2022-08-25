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
import com.st1.itx.db.domain.JcicZ049;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ049LogService;
import com.st1.itx.db.service.JcicZ049Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8412")
@Scope("prototype")
/**
 * 變更還款方案結案通知資料(049)
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8412 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ049Service sJcicZ049Service;
	@Autowired
	public JcicZ049LogService sJcicZ049LogService;
	@Autowired
	public CdCodeService iCdCodeService;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ049> sJcicZ049 = null;
		sJcicZ049 = sJcicZ049Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ049     = " + sJcicZ049.getSize());
		if (sJcicZ049 != null) {
			for (JcicZ049 xJcicZ049 : sJcicZ049) {
					if ((iSubmitType == 1 && xJcicZ049.getOutJcicTxtDate() == 0) || 
							(iSubmitType == 3 && xJcicZ049.getActualFilingDate() == 0)) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8310");
						occursListB.putParam("OOHistoryTxCd", "L8040");
						occursListB.putParam("OOCustId", xJcicZ049.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ049.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ049.getSubmitKey(), titaVo));
						occursListB.putParam("OORcDate", xJcicZ049.getRcDate());
						occursListB.putParam("OOTranKey", xJcicZ049.getTranKey());
				//		occursListB.putParam("OOTranCode", xJcicZ049.getTranCode());
						occursListB.putParam("OOTranCode", "049");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ049.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ049.getActualFilingMark());
						this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ049.getOutJcicTxtDate() == iJcicDate && xJcicZ049.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8310");
						occursListB.putParam("OOHistoryTxCd", "L8040");
						occursListB.putParam("OOCustId", xJcicZ049.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ049.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ049.getSubmitKey(), titaVo));
						occursListB.putParam("OORcDate", xJcicZ049.getRcDate());
						occursListB.putParam("OOTranKey", xJcicZ049.getTranKey());
//					occursListB.putParam("OOTranCode", xJcicZ049.getTranCode());
						occursListB.putParam("OOTranCode", "049");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ049.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ049.getActualFilingMark());
						this.info("OOCustId    = " + xJcicZ049.getCustId());
						this.info("OOSubmitKey  = " + xJcicZ049.getSubmitKey());
						this.info("OORcDate   = " + xJcicZ049.getRcDate());
						this.info("OOTranKey  = " + xJcicZ049.getTranKey());
						this.info("OOActualFilingDate  = " + iActualFilingDate);
						this.info("OOActualFilingMark  = " + xJcicZ049.getActualFilingMark());
						this.info("TranCode    = " + "049");
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
