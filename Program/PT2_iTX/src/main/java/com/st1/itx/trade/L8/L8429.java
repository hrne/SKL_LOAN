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
import com.st1.itx.db.domain.JcicZ447;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ447LogService;
import com.st1.itx.db.service.JcicZ447Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8429")
@Scope("prototype")
/**
 * 前置調解單獨全數受清償資料(447)
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8429 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ447Service sJcicZ447Service;
	@Autowired
	public JcicZ447LogService sJcicZ447LogService;
	@Autowired
	public CdCodeService iCdCodeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ447> sJcicZ447 = null;
		sJcicZ447 = sJcicZ447Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ447     = " + sJcicZ447.getSize());
		if (sJcicZ447 != null) {
			for (JcicZ447 xJcicZ447 : sJcicZ447) {
				if ((iSubmitType == 1 && xJcicZ447.getOutJcicTxtDate() == 0) || (iSubmitType == 3 && xJcicZ447.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8327");
					occursListB.putParam("OOHistoryTxCd", "L8057");
					occursListB.putParam("OOCustId", xJcicZ447.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ447.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ447.getSubmitKey(), titaVo));
					occursListB.putParam("OOTranKey", xJcicZ447.getTranKey());
					occursListB.putParam("OOApplyDate", xJcicZ447.getApplyDate());
					occursListB.putParam("OOCourtCode", xJcicZ447.getCourtCode());
					// occursListB.putParam("OOTranCode", xJcicZ447.getTranCode());
					occursListB.putParam("OOTranCode", "447");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ447.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ447.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ447.getOutJcicTxtDate() == iJcicDate && xJcicZ447.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8327");
						occursListB.putParam("OOHistoryTxCd", "L8057d");
						occursListB.putParam("OOCustId", xJcicZ447.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ447.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ447.getSubmitKey(), titaVo));
						occursListB.putParam("OOTranKey", xJcicZ447.getTranKey());
						occursListB.putParam("OOApplyDate", xJcicZ447.getApplyDate());
						occursListB.putParam("OOCourtCode", xJcicZ447.getCourtCode());
//								occursListB.putParam("OOTranCode", xJcicZ447.getTranCode());
						occursListB.putParam("OOTranCode", "447");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ447.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ447.getActualFilingMark());
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