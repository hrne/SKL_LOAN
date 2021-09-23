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
import com.st1.itx.db.domain.JcicZ062;
import com.st1.itx.db.domain.JcicZ062Log;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ062LogService;
import com.st1.itx.db.service.JcicZ062Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8050")
@Scope("prototype")
public class L8050 extends TradeBuffer  {
		@Autowired
		public CdEmpService iCdEmpService;
		@Autowired
		public JcicZ062Service iJcicZ062Service;
		@Autowired
		public JcicZ062LogService iJcicZ062LogService;
		@Override
		public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
			this.info("active L8050 ");
			this.totaVo.init(titaVo);
			String iUkey = titaVo.getParam("Ukey");
			
			this.index = titaVo.getReturnIndex();
			this.limit = 500;

			Slice<JcicZ062Log> rJcicZ062Log = null;
			rJcicZ062Log = iJcicZ062LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
			JcicZ062 rJcicZ062 = new JcicZ062();
			rJcicZ062 = iJcicZ062Service.ukeyFirst(iUkey, titaVo);
			if (rJcicZ062 == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			if (!rJcicZ062.getTranKey().equals("A")&&rJcicZ062.getOutJcicTxtDate()==0) {
				OccursList occursListA = new OccursList();
				String iLastUpdateEmpNo = rJcicZ062.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursListA.putParam("OOTranKey", rJcicZ062.getTranKey());
				occursListA.putParam("OOCompletePeriod", rJcicZ062.getCompletePeriod());
				occursListA.putParam("OOPeriod", rJcicZ062.getPeriod());
				occursListA.putParam("OORate", rJcicZ062.getRate());
				occursListA.putParam("OOExpBalanceAmt", rJcicZ062.getExpBalanceAmt());
				occursListA.putParam("OOCashBalanceAmt", rJcicZ062.getCashBalanceAmt());
				occursListA.putParam("OOCreditBalanceAmt", rJcicZ062.getCreditBalanceAmt());
				occursListA.putParam("OOChaRepayAmt", rJcicZ062.getChaRepayAmt());
				occursListA.putParam("OOChaRepayAgreeDate", rJcicZ062.getChaRepayAgreeDate());
				occursListA.putParam("OOChaRepayViewDate", rJcicZ062.getChaRepayViewDate());
				occursListA.putParam("OOChaRepayEndDate", rJcicZ062.getChaRepayEndDate());
				occursListA.putParam("OOChaRepayFirstDate", rJcicZ062.getChaRepayFirstDate());
				occursListA.putParam("OOPayAccount", rJcicZ062.getPayAccount());
				occursListA.putParam("OOPostAddr", rJcicZ062.getPostAddr());
				occursListA.putParam("OOMonthPayAmt", rJcicZ062.getMonthPayAmt());
				occursListA.putParam("OOGradeType", rJcicZ062.getGradeType());
				occursListA.putParam("OOPeriod2", rJcicZ062.getPeriod2());
				occursListA.putParam("OORate2", rJcicZ062.getRate2());
				occursListA.putParam("OOMonthPayAmt2", rJcicZ062.getMonthPayAmt2());
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
				String taU = rJcicZ062.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursListA.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursListA.putParam("OOLastUpdateEmpNo", rJcicZ062.getLastUpdateEmpNo());
				occursListA.putParam("OOOutJcicTxtDate", rJcicZ062.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursListA);
			}
			if (rJcicZ062Log == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			for (JcicZ062Log rrJcicZ062Log:rJcicZ062Log) {
				OccursList occursList = new OccursList();
				String iLastUpdateEmpNo = rrJcicZ062Log.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursList.putParam("OOTranKey", rrJcicZ062Log.getTranKey());
				occursList.putParam("OOCompletePeriod", rrJcicZ062Log.getCompletePeriod());
				occursList.putParam("OOPeriod", rrJcicZ062Log.getPeriod());
				occursList.putParam("OORate", rrJcicZ062Log.getRate());
				occursList.putParam("OOExpBalanceAmt", rrJcicZ062Log.getExpBalanceAmt());
				occursList.putParam("OOCashBalanceAmt", rrJcicZ062Log.getCashBalanceAmt());
				occursList.putParam("OOCreditBalanceAmt", rrJcicZ062Log.getCreditBalanceAmt());
				occursList.putParam("OOChaRepayAmt", rrJcicZ062Log.getChaRepayAmt());
				occursList.putParam("OOChaRepayAgreeDate", rrJcicZ062Log.getChaRepayAgreeDate());
				occursList.putParam("OOChaRepayViewDate", rrJcicZ062Log.getChaRepayViewDate());
				occursList.putParam("OOChaRepayEndDate", rrJcicZ062Log.getChaRepayEndDate());
				occursList.putParam("OOChaRepayFirstDate", rrJcicZ062Log.getChaRepayFirstDate());
				occursList.putParam("OOPayAccount", rrJcicZ062Log.getPayAccount());
				occursList.putParam("OOPostAddr", rrJcicZ062Log.getPostAddr());
				occursList.putParam("OOMonthPayAmt", rrJcicZ062Log.getMonthPayAmt());
				occursList.putParam("OOGradeType", rrJcicZ062Log.getGradeType());
				occursList.putParam("OOPeriod2", rrJcicZ062Log.getPeriod2());
				occursList.putParam("OORate2", rrJcicZ062Log.getRate2());
				occursList.putParam("OOMonthPayAmt2", rrJcicZ062Log.getMonthPayAmt2());
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
				String taU = rrJcicZ062Log.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursList.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
				occursList.putParam("OOOutJcicTxtDate", rrJcicZ062Log.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursList);
			}		
			this.addList(this.totaVo);
			return this.sendList();
		}
}
	


