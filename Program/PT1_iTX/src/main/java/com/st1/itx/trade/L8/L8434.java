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
import com.st1.itx.db.domain.JcicZ570;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ570LogService;
import com.st1.itx.db.service.JcicZ570Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8434")
@Scope("prototype")
/**
 * 聯徵產品檔案匯出作業(570)
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8434 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ570Service sJcicZ570Service;
	@Autowired
	public JcicZ570LogService sJcicZ570LogService;
	@Autowired
	public CdCodeService iCdCodeService;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);

        this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ570> sJcicZ570 = null;
		sJcicZ570 = sJcicZ570Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ570     = " + sJcicZ570.getSize());
		if (sJcicZ570 != null) {
			for (JcicZ570 xJcicZ570 : sJcicZ570) {
				if ((iSubmitType == 1 && xJcicZ570.getOutJcicTxtDate() == 0)
						|| (iSubmitType == 3 && xJcicZ570.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8332");
					occursListB.putParam("OOHistoryTxCd", "L8062");
					occursListB.putParam("OOCustId", xJcicZ570.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ570.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ570.getSubmitKey(), titaVo));
					occursListB.putParam("OOTranKey", xJcicZ570.getTranKey());
					occursListB.putParam("OOApplyDate", xJcicZ570.getApplyDate());
					// occursListB.putParam("OOTranCode", xJcicZ570.getTranCode());
					occursListB.putParam("OOTranCode", "570");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ570.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ570.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ570.getOutJcicTxtDate() == iJcicDate && xJcicZ570.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8332");
						occursListB.putParam("OOHistoryTxCd", "L8062d");
						occursListB.putParam("OOCustId", xJcicZ570.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ570.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ570.getSubmitKey(), titaVo));
						occursListB.putParam("OOTranKey", xJcicZ570.getTranKey());
						occursListB.putParam("OOApplyDate", xJcicZ570.getApplyDate());
//					occursListB.putParam("OOTranCode", xJcicZ570.getTranCode());
						occursListB.putParam("OOTranCode", "570");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ570.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ570.getActualFilingMark());
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