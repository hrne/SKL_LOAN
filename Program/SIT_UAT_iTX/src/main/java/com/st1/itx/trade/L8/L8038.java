package com.st1.itx.trade.L8;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.JcicZ047;
import com.st1.itx.db.domain.JcicZ047Log;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ047LogService;
import com.st1.itx.db.service.JcicZ047Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8038")
@Scope("prototype")
public class L8038 extends TradeBuffer  {
		@Autowired
		public CdEmpService iCdEmpService;
		@Autowired
		public JcicZ047Service iJcicZ047Service;
		@Autowired
		public JcicZ047LogService iJcicZ047LogService;
		@Override
		public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
			this.info("active L8038 ");
			this.totaVo.init(titaVo);
			String iUkey = titaVo.getParam("Ukey");
			
			this.index = titaVo.getReturnIndex();
			this.limit = 500;

			Slice<JcicZ047Log> rJcicZ047Log = null;
			rJcicZ047Log = iJcicZ047LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
			JcicZ047 rJcicZ047 = new JcicZ047();
			rJcicZ047 = iJcicZ047Service.ukeyFirst(iUkey, titaVo);
			if (rJcicZ047 == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			if (!rJcicZ047.getTranKey().equals("A")&&rJcicZ047.getOutJcicTxtDate()==0) {
				OccursList occursListA = new OccursList();
				String iLastUpdateEmpNo = rJcicZ047.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursListA.putParam("OOTranKey", rJcicZ047.getTranKey());
				occursListA.putParam("OOPeriod", rJcicZ047.getPeriod());
				occursListA.putParam("OORate", rJcicZ047.getRate());
				occursListA.putParam("OOCivil323ExpAmt", rJcicZ047.getCivil323ExpAmt());
				occursListA.putParam("OOExpLoanAmt", rJcicZ047.getExpLoanAmt());
				occursListA.putParam("OOCivil323CashAmt", rJcicZ047.getCivil323CashAmt());
				occursListA.putParam("OOCashCardAmt", rJcicZ047.getCashCardAmt());
				occursListA.putParam("OOCivil323CreditAmt", rJcicZ047.getCivil323CreditAmt());
				occursListA.putParam("OOCreditCardAmt", rJcicZ047.getCreditCardAmt());
				occursListA.putParam("OOCivil323Amt", rJcicZ047.getCivil323Amt());
				occursListA.putParam("OOTotalAmt", rJcicZ047.getTotalAmt());
				occursListA.putParam("OOPassDate", rJcicZ047.getPassDate());
				occursListA.putParam("OOInterviewDate", rJcicZ047.getInterviewDate());
				occursListA.putParam("OOSignDate", rJcicZ047.getSignDate());
				occursListA.putParam("OOLimitDate", rJcicZ047.getLimitDate());
				occursListA.putParam("OOFirstPayDate", rJcicZ047.getFirstPayDate());
				occursListA.putParam("OOMonthPayAmt", rJcicZ047.getMonthPayAmt());
				occursListA.putParam("OOPayAccount", rJcicZ047.getPayAccount());
				occursListA.putParam("OOPostAddr", rJcicZ047.getPostAddr());
				occursListA.putParam("OOGradeType", rJcicZ047.getGradeType());
				occursListA.putParam("OOPayLastAmt", rJcicZ047.getPayLastAmt());
				occursListA.putParam("OOPeriod2", rJcicZ047.getPeriod2());
				occursListA.putParam("OORate2", rJcicZ047.getRate2());
				occursListA.putParam("OOMonthPayAmt2", rJcicZ047.getMonthPayAmt2());
				occursListA.putParam("OOPayLastAmt2", rJcicZ047.getPayLastAmt2());
				iCdEmp = iCdEmpService.findById(iLastUpdateEmpNo, titaVo);
				if (iLastUpdateEmpNo.equals("")) {
					occursListA.putParam("OOLastUpdateEmpNoName", "");
				}else {
					if (iCdEmp == null) {
						occursListA.putParam("OOLastUpdateEmpNoName", "");
					}else {
						occursListA.putParam("OOLastUpdateEmpNoName", iCdEmp.getFullname());
					}
				}
				String taU = rJcicZ047.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursListA.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursListA.putParam("OOLastUpdateEmpNo",iLastUpdateEmpNo);
				occursListA.putParam("OOOutJcicTxtDate", rJcicZ047.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursListA);
			}
			if (rJcicZ047Log == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			for (JcicZ047Log rrJcicZ047Log:rJcicZ047Log) {
				OccursList occursList = new OccursList();
				String iLastUpdateEmpNo = rrJcicZ047Log.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursList.putParam("OOTranKey", rrJcicZ047Log.getTranKey());
				occursList.putParam("OORate", rrJcicZ047Log.getRate());
				occursList.putParam("OOCivil323ExpAmt", rrJcicZ047Log.getCivil323ExpAmt());
				occursList.putParam("OOExpLoanAmt", rrJcicZ047Log.getExpLoanAmt());
				occursList.putParam("OOCivil323CashAmt", rrJcicZ047Log.getCivil323CashAmt());
				occursList.putParam("OOCashCardAmt", rrJcicZ047Log.getCashCardAmt());
				occursList.putParam("OOCivil323CreditAmt", rrJcicZ047Log.getCivil323CreditAmt());
				occursList.putParam("OOCreditCardAmt", rrJcicZ047Log.getCreditCardAmt());
				occursList.putParam("OOCivil323Amt", rrJcicZ047Log.getCivil323Amt());
				occursList.putParam("OOTotalAmt", rrJcicZ047Log.getTotalAmt());
				occursList.putParam("OOPassDate", rrJcicZ047Log.getPassDate());
				occursList.putParam("OOInterviewDate", rrJcicZ047Log.getInterviewDate());
				occursList.putParam("OOSignDate", rrJcicZ047Log.getSignDate());
				occursList.putParam("OOLimitDate", rrJcicZ047Log.getLimitDate());
				occursList.putParam("OOFirstPayDate", rrJcicZ047Log.getFirstPayDate());
				occursList.putParam("OOMonthPayAmt", rrJcicZ047Log.getMonthPayAmt());
				occursList.putParam("OOPayAccount", rrJcicZ047Log.getPayAccount());
				occursList.putParam("OOPostAddr", rrJcicZ047Log.getPostAddr());
				occursList.putParam("OOGradeType", rrJcicZ047Log.getGradeType());
				occursList.putParam("OOPayLastAmt", rrJcicZ047Log.getPayLastAmt());
				occursList.putParam("OOPeriod2", rrJcicZ047Log.getPeriod2());
				occursList.putParam("OORate2", rrJcicZ047Log.getRate2());
				occursList.putParam("OOMonthPayAmt2", rrJcicZ047Log.getMonthPayAmt2());
				occursList.putParam("OOPayLastAmt2", rrJcicZ047Log.getPayLastAmt2());
				occursList.putParam("OOPeriod", rrJcicZ047Log.getPeriod());
				
				iCdEmp = iCdEmpService.findById(iLastUpdateEmpNo, titaVo);
				if (iLastUpdateEmpNo.equals("")) {
					occursList.putParam("OOLastUpdateEmpNoName", "");
				}else {
					if (iCdEmp == null) {
						occursList.putParam("OOLastUpdateEmpNoName", "");
					}else {
						occursList.putParam("OOLastUpdateEmpNoName", iCdEmp.getFullname());
					}
				}
				String taU = rrJcicZ047Log.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursList.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
				occursList.putParam("OOOutJcicTxtDate", rrJcicZ047Log.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursList);
			}		
			this.addList(this.totaVo);
			return this.sendList();
		}
}
	


