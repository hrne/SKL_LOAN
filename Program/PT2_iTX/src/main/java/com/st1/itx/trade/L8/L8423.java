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
import com.st1.itx.db.domain.JcicZ063;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ063LogService;
import com.st1.itx.db.service.JcicZ063Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8423")
@Scope("prototype")
/**
 * 變更還款方案結案通知資料(063)
 * 
 * @author Luisito
 * @version 1.0.0
 */
public class L8423 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ063Service sJcicZ063Service;
	@Autowired
	public JcicZ063LogService sJcicZ063LogService;
	@Autowired
	public CdCodeService iCdCodeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ063> sJcicZ063 = null;
		sJcicZ063 = sJcicZ063Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ063     = " + sJcicZ063.getSize());
		if (sJcicZ063 != null) {
			for (JcicZ063 xJcicZ063 : sJcicZ063) {
				if ((iSubmitType == 1 && xJcicZ063.getOutJcicTxtDate() == 0) || (iSubmitType == 3 && xJcicZ063.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8321");
					occursListB.putParam("OOHistoryTxCd", "L8051");
					occursListB.putParam("OOCustId", xJcicZ063.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ063.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ063.getSubmitKey(), titaVo));
					occursListB.putParam("OORcDate", xJcicZ063.getRcDate());
					occursListB.putParam("OOTranKey", xJcicZ063.getTranKey());
					occursListB.putParam("OOChangePayDate", xJcicZ063.getChangePayDate());
					// occursListB.putParam("OOTranCode", xJcicZ063.getTranCode());
					occursListB.putParam("OOTranCode", "063");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ063.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ063.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ063.getOutJcicTxtDate() == iJcicDate && xJcicZ063.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8321");
						occursListB.putParam("OOHistoryTxCd", "L8051d");
						occursListB.putParam("OOCustId", xJcicZ063.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ063.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ063.getSubmitKey(), titaVo));
						occursListB.putParam("OORcDate", xJcicZ063.getRcDate());
						occursListB.putParam("OOTranKey", xJcicZ063.getTranKey());
						occursListB.putParam("OOChangePayDate", xJcicZ063.getChangePayDate());
//					occursListB.putParam("OOTranCode", xJcicZ063.getTranCode());
						occursListB.putParam("OOTranCode", "063");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ063.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ063.getActualFilingMark());
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