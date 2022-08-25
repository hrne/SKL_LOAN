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
import com.st1.itx.db.domain.JcicZ440;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ440LogService;
import com.st1.itx.db.service.JcicZ440Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;

@Service("L8424")
@Scope("prototype")
/**
 * 前置調解無擔保債務分配表資料(440)
 * 
 * @author Luisito
 * @version 1.0.0
 */
public class L8424 extends TradeBuffer {
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public L8403File iL8403File;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicZ440Service sJcicZ440Service;
	@Autowired
	public JcicZ440LogService sJcicZ440LogService;
	@Autowired
	public CdCodeService iCdCodeService;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8403 ");
		this.totaVo.init(titaVo);
		 this.index = titaVo.getReturnIndex();
			this.limit = 500;
			Slice<JcicZ440> sJcicZ440 = null;
			sJcicZ440 = sJcicZ440Service.findAll(index, limit, titaVo);
			int iSubmitType = Integer.valueOf(titaVo.getParam("SubmitType"));
			int iJcicDate = Integer.valueOf(titaVo.getParam("ReportDate"));
			// 取值顯示
			this.info("sJcicZ440     = " + sJcicZ440.getSize());
			if (sJcicZ440 != null) {
				for (JcicZ440 xJcicZ440 : sJcicZ440) {
					if ((iSubmitType == 1 && xJcicZ440.getOutJcicTxtDate() == 0)
							|| (iSubmitType == 3 && xJcicZ440.getActualFilingDate() == 0)) {
						OccursList occursListB = new OccursList();
						occursListB.putParam("OOChainTxCd", "L8322");
						occursListB.putParam("OOHistoryTxCd", "L8052");
						occursListB.putParam("OOCustId", xJcicZ440.getCustId());
						occursListB.putParam("OOSubmitKey", xJcicZ440.getSubmitKey());
						occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ440.getSubmitKey(), titaVo));
						occursListB.putParam("OOTranKey", xJcicZ440.getTranKey());
						occursListB.putParam("OOApplyDate", xJcicZ440.getApplyDate());
	                    occursListB.putParam("OOCourtCode", xJcicZ440.getCourtCode());
						// occursListB.putParam("OOTranCode", xJcicZ440.getTranCode());
						occursListB.putParam("OOTranCode", "440");
						int iActualFilingDate = 0;
						iActualFilingDate = xJcicZ440.getActualFilingDate();
						if (iActualFilingDate == 0) {
							occursListB.putParam("OOActualFilingDate", "");
						} else {
							occursListB.putParam("OOActualFilingDate", iActualFilingDate);
						}
						occursListB.putParam("OOActualFilingMark", xJcicZ440.getActualFilingMark());
						this.totaVo.addOccursList(occursListB);
					} else if (iSubmitType == 2) {
						if (xJcicZ440.getOutJcicTxtDate() == iJcicDate && xJcicZ440.getActualFilingDate() == 0) {
							OccursList occursListB = new OccursList();
							occursListB.putParam("OOChainTxCd", "L8322");
							occursListB.putParam("OOHistoryTxCd", "L8052d");
							occursListB.putParam("OOCustId", xJcicZ440.getCustId());
							occursListB.putParam("OOSubmitKey", xJcicZ440.getSubmitKey());
							occursListB.putParam("OOSubmitKeyX", dealBankName(xJcicZ440.getSubmitKey(), titaVo));
							occursListB.putParam("OOTranKey", xJcicZ440.getTranKey());
							occursListB.putParam("OOApplyDate", xJcicZ440.getApplyDate());
	                        occursListB.putParam("OOCourtCode", xJcicZ440.getCourtCode());
//						occursListB.putParam("OOTranCode", xJcicZ440.getTranCode());
							occursListB.putParam("OOTranCode", "440");
							int iActualFilingDate = 0;
							iActualFilingDate = xJcicZ440.getActualFilingDate();
							if (iActualFilingDate == 0) {
								occursListB.putParam("OOActualFilingDate", "");
							} else {
								occursListB.putParam("OOActualFilingDate", iActualFilingDate);
							}
							occursListB.putParam("OOActualFilingMark", xJcicZ440.getActualFilingMark());
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