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
import com.st1.itx.db.domain.JcicZ443;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ443LogService;
import com.st1.itx.db.service.JcicZ443Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8426")
@Scope("prototype")
/**
 * 前置調解回報有擔保債權金額資料(443)
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8426 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ443Service sJcicZ443Service;
	@Autowired
	public JcicZ443LogService sJcicZ443LogService;
	@Autowired
	public CdCodeService iCdCodeService;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ443> sJcicZ443 = null;
		sJcicZ443 = sJcicZ443Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ443     = " + sJcicZ443.getSize());
		if (sJcicZ443 != null) {
			for (JcicZ443 xJcicZ443 : sJcicZ443) {
				if ((iSubmitType == 1 && xJcicZ443.getOutJcicTxtDate() == 0)
						|| (iSubmitType == 3 && xJcicZ443.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8324");
					occursListB.putParam("OOHistoryTxCd", "L8054");
					occursListB.putParam("OOCustId", xJcicZ443.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ443.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ443.getSubmitKey(), titaVo));
					occursListB.putParam("OOAccount", xJcicZ443.getAccount());
					occursListB.putParam("OOApplyDate", xJcicZ443.getApplyDate());
                    occursListB.putParam("OOCourtCode", xJcicZ443.getCourtCode());
                    occursListB.putParam("OOTranKey", xJcicZ443.getTranKey());
					occursListB.putParam("OOMaxMainCode", xJcicZ443.getMaxMainCode());
                    occursListB.putParam("OOMaxMainCodeX", dealBankName(xJcicZ443.getMaxMainCode(), titaVo));
					// occursListB.putParam("OOTranCode", xJcicZ443.getTranCode());
					occursListB.putParam("OOTranCode", "443");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ443.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ443.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ443.getOutJcicTxtDate() == iJcicDate && xJcicZ443.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8324");
						occursListB.putParam("OOHistoryTxCd", "L8054d");
						occursListB.putParam("OOCustId", xJcicZ443.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ443.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ443.getSubmitKey(), titaVo));
						occursListB.putParam("OOAccount", xJcicZ443.getAccount());
						occursListB.putParam("OOApplyDate", xJcicZ443.getApplyDate());
                        occursListB.putParam("OOCourtCode", xJcicZ443.getCourtCode());
                        occursListB.putParam("OOTranKey", xJcicZ443.getTranKey());
						occursListB.putParam("OOMaxMainCode", xJcicZ443.getMaxMainCode());
                        occursListB.putParam("OOMaxMainCodeX", dealBankName(xJcicZ443.getMaxMainCode(), titaVo));
//					occursListB.putParam("OOTranCode", xJcicZ443.getTranCode());
						occursListB.putParam("OOTranCode", "443");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ443.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ443.getActualFilingMark());
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