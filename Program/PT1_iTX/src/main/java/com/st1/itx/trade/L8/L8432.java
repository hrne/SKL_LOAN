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
import com.st1.itx.db.domain.JcicZ451;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ451LogService;
import com.st1.itx.db.service.JcicZ451Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8432")
@Scope("prototype")
/**
 * 聯徵產品檔案匯出作業(451)
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8432 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ451Service sJcicZ451Service;
	@Autowired
	public JcicZ451LogService sJcicZ451LogService;
	@Autowired
	public CdCodeService iCdCodeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ451> sJcicZ451 = null;
		sJcicZ451 = sJcicZ451Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ451     = " + sJcicZ451.getSize());
		if (sJcicZ451 != null) {
			for (JcicZ451 xJcicZ451 : sJcicZ451) {
				if ((iSubmitType == 1 && xJcicZ451.getOutJcicTxtDate() == 0) || (iSubmitType == 3 && xJcicZ451.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8330");
					occursListB.putParam("OOHistoryTxCd", "L8060");
					occursListB.putParam("OOCustId", xJcicZ451.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ451.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ451.getSubmitKey(), titaVo));
					occursListB.putParam("OOTranKey", xJcicZ451.getTranKey());
					occursListB.putParam("OOApplyDate", xJcicZ451.getApplyDate());
					occursListB.putParam("OOCourtCode", xJcicZ451.getCourtCode());
					occursListB.putParam("OODelayYM", xJcicZ451.getDelayYM());
					// occursListB.putParam("OOTranCode", xJcicZ451.getTranCode());
					occursListB.putParam("OOTranCode", "451");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ451.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ451.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ451.getOutJcicTxtDate() == iJcicDate && xJcicZ451.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8330");
						occursListB.putParam("OOHistoryTxCd", "L8060d");
						occursListB.putParam("OOCustId", xJcicZ451.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ451.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ451.getSubmitKey(), titaVo));
						occursListB.putParam("OOTranKey", xJcicZ451.getTranKey());
						occursListB.putParam("OOApplyDate", xJcicZ451.getApplyDate());
						occursListB.putParam("OOCourtCode", xJcicZ451.getCourtCode());
						occursListB.putParam("OODelayYM", xJcicZ451.getDelayYM());
//					occursListB.putParam("OOTranCode", xJcicZ451.getTranCode());
						occursListB.putParam("OOTranCode", "451");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ451.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ451.getActualFilingMark());
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