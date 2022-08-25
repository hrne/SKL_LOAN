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
import com.st1.itx.db.domain.JcicZ053;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ053LogService;
import com.st1.itx.db.service.JcicZ053Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8416")
@Scope("prototype")
/**
 * 變更還款方案結案通知資料(053)
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8416 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ053Service sJcicZ053Service;
	@Autowired
	public JcicZ053LogService sJcicZ053LogService;
	@Autowired
	public CdCodeService iCdCodeService;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ053> sJcicZ053 = null;
		sJcicZ053 = sJcicZ053Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ053     = " + sJcicZ053.getSize());
		if (sJcicZ053 != null) {
			for (JcicZ053 xJcicZ053 : sJcicZ053) {
				if ((iSubmitType == 1 && xJcicZ053.getOutJcicTxtDate() == 0)
						|| (iSubmitType == 3 && xJcicZ053.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8314");
					occursListB.putParam("OOHistoryTxCd", "L8044");
					occursListB.putParam("OOCustId", xJcicZ053.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ053.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ053.getSubmitKey(), titaVo));
					occursListB.putParam("OORcDate", xJcicZ053.getRcDate());
                    occursListB.putParam("OOMaxMainCode",xJcicZ053.getMaxMainCode());
                    occursListB.putParam("OOMaxMainCodeX", dealBankName(xJcicZ053.getMaxMainCode(), titaVo));
                    occursListB.putParam("OOTranKey", xJcicZ053.getTranKey());
					// occursListB.putParam("OOTranCode", xJcicZ053.getTranCode());
					occursListB.putParam("OOTranCode", "053");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ053.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ053.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ053.getOutJcicTxtDate() == iJcicDate && xJcicZ053.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8314");
						occursListB.putParam("OOHistoryTxCd", "L8044d");
						occursListB.putParam("OOCustId", xJcicZ053.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ053.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ053.getSubmitKey(), titaVo));
						occursListB.putParam("OORcDate", xJcicZ053.getRcDate());
                        occursListB.putParam("OOMaxMainCode",xJcicZ053.getMaxMainCode());
                        occursListB.putParam("OOMaxMainCodeX", dealBankName(xJcicZ053.getMaxMainCode(), titaVo));
                        occursListB.putParam("OOTranKey", xJcicZ053.getTranKey());
//					occursListB.putParam("OOTranCode", xJcicZ053.getTranCode());
						occursListB.putParam("OOTranCode", "053");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ053.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ053.getActualFilingMark());
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