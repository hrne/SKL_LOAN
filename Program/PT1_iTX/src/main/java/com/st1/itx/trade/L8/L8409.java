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
import com.st1.itx.db.domain.JcicZ046;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ046LogService;
import com.st1.itx.db.service.JcicZ046Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8409")
@Scope("prototype")
/**
 * 變更還款方案結案通知資料(046)
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8409 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ046Service sJcicZ046Service;
	@Autowired
	public JcicZ046LogService sJcicZ046LogService;
	@Autowired
	public CdCodeService iCdCodeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8409 ");
		this.totaVo.init(titaVo);
		this.index = titaVo.getReturnIndex();
		this.limit = 500;
		Slice<JcicZ046> sJcicZ046 = null;
		sJcicZ046 = sJcicZ046Service.findAll(index, limit, titaVo);
		int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
		int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
		// 取值顯示
		this.info("sJcicZ046     = " + sJcicZ046.getSize());
		if (sJcicZ046 != null) {
			for (JcicZ046 xJcicZ046 : sJcicZ046) {
				if ((iSubmitType == 1 && xJcicZ046.getOutJcicTxtDate() == 0) || (iSubmitType == 3 && xJcicZ046.getActualFilingDate() == 0)) {
					OccursList occursListB = new OccursList();
					occursListB.putParam("OOChainTxCd", "L8307");
					occursListB.putParam("OOHistoryTxCd", "L8037");
					occursListB.putParam("OOCustId", xJcicZ046.getCustId());
					occursListB.putParam("OOSubmitKey", xJcicZ046.getSubmitKey());
					occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ046.getSubmitKey(), titaVo));
					occursListB.putParam("OORcDate", xJcicZ046.getRcDate());
					occursListB.putParam("OOCloseDate", xJcicZ046.getCloseDate());
					occursListB.putParam("OOTranKey", xJcicZ046.getTranKey());
					// occursListB.putParam("OOTranCode", xJcicZ046.getTranCode());
					occursListB.putParam("OOTranCode", "046");
					int iActualFilingDate = 0;
					iActualFilingDate = xJcicZ046.getActualFilingDate();
					if (iActualFilingDate == 0) {
						occursListB.putParam("OOActualFilingDate", "");
					} else {
						occursListB.putParam("OOActualFilingDate", iActualFilingDate);
					}
					occursListB.putParam("OOActualFilingMark", xJcicZ046.getActualFilingMark());
					this.totaVo.addOccursList(occursListB);
				} else if (iSubmitType == 2) {
					if (xJcicZ046.getOutJcicTxtDate() == iJcicDate && xJcicZ046.getActualFilingDate() == 0) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8307");
						occursListB.putParam("OOHistoryTxCd", "L80367");
						occursListB.putParam("OOCustId", xJcicZ046.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ046.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ046.getSubmitKey(), titaVo));
						occursListB.putParam("OORcDate", xJcicZ046.getRcDate());
						occursListB.putParam("OOCloseDate", xJcicZ046.getCloseDate());
						occursListB.putParam("OOTranKey", xJcicZ046.getTranKey());
//					occursListB.putParam("OOTranCode", xJcicZ046.getTranCode());
						occursListB.putParam("OOTranCode", "046");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ046.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ046.getActualFilingMark());
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