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
import com.st1.itx.db.domain.JcicZ044;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ044LogService;
import com.st1.itx.db.service.JcicZ044Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8407")
@Scope("prototype")
/**
 * 請求同意債務清償方案通知資料(044)
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8407 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ044Service sJcicZ044Service;
	@Autowired
	public JcicZ044LogService sJcicZ044LogService;
	@Autowired
	public CdCodeService iCdCodeService;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8407 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ044> sJcicZ044 = null;
		sJcicZ044 = sJcicZ044Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ044     = " + sJcicZ044.getSize());
		if (sJcicZ044 != null) {
			for (JcicZ044 xJcicZ044 : sJcicZ044) {
				if ((iSubmitType == 1 && xJcicZ044.getOutJcicTxtDate() == 0)
						|| (iSubmitType == 3 && xJcicZ044.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8305");
					occursListB.putParam("OOHistoryTxCd", "L8035");
					occursListB.putParam("OOCustId", xJcicZ044.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ044.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ044.getSubmitKey(), titaVo));
					occursListB.putParam("OORcDate", xJcicZ044.getRcDate());
					occursListB.putParam("OOTranKey", xJcicZ044.getTranKey());
					// occursListB.putParam("OOTranCode", xJcicZ044.getTranCode());
					occursListB.putParam("OOTranCode", "044");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ044.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ044.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ044.getOutJcicTxtDate() == iJcicDate && xJcicZ044.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8305");
						occursListB.putParam("OOHistoryTxCd", "L8035");
						occursListB.putParam("OOCustId", xJcicZ044.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ044.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ044.getSubmitKey(), titaVo));
						occursListB.putParam("OORcDate", xJcicZ044.getRcDate());
						occursListB.putParam("OOTranKey", xJcicZ044.getTranKey());
//					occursListB.putParam("OOTranCode", xJcicZ044.getTranCode());
						occursListB.putParam("OOTranCode", "044");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ044.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ044.getActualFilingMark());
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