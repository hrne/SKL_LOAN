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
import com.st1.itx.db.domain.JcicZ061;
import com.st1.itx.db.domain.JcicZ061Log;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ061LogService;
import com.st1.itx.db.service.JcicZ061Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8049")
@Scope("prototype")
public class L8049 extends TradeBuffer  {
		@Autowired
		public CdEmpService iCdEmpService;
		@Autowired
		public JcicZ061Service iJcicZ061Service;
		@Autowired
		public JcicZ061LogService iJcicZ061LogService;
		@Override
		public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
			this.info("active L8049 ");
			this.totaVo.init(titaVo);
			String iUkey = titaVo.getParam("Ukey");
			
			this.index = titaVo.getReturnIndex();
			this.limit = 500;

			Slice<JcicZ061Log> rJcicZ061Log = null;
			rJcicZ061Log = iJcicZ061LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
			JcicZ061 rJcicZ061 = new JcicZ061();
			rJcicZ061 = iJcicZ061Service.ukeyFirst(iUkey, titaVo);
			if (rJcicZ061 == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			if (!rJcicZ061.getTranKey().equals("A")&&rJcicZ061.getOutJcicTxtDate()==0) {
				OccursList occursListA = new OccursList();
				String iLastUpdateEmpNo = rJcicZ061.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursListA.putParam("OOTranKey", rJcicZ061.getTranKey());
				occursListA.putParam("OOExpBalanceAmt", rJcicZ061.getExpBalanceAmt());
				occursListA.putParam("OOCashBalanceAmt", rJcicZ061.getCashBalanceAmt());
				occursListA.putParam("OOCreditBalanceAmt", rJcicZ061.getCreditBalanceAmt());
				occursListA.putParam("OOMaxMainNote", rJcicZ061.getMaxMainNote());
				occursListA.putParam("OOIsGuarantor", rJcicZ061.getIsGuarantor());
				occursListA.putParam("OOIsChangePayment", rJcicZ061.getIsChangePayment());
				iCdEmp = iCdEmpService.findAgentIdFirst(iLastUpdateEmpNo, titaVo);
				if (iLastUpdateEmpNo.equals("")) {
					occursListA.putParam("OOLastUpdateEmpNoName", "");
				}else {
					if (iCdEmp == null) {
						occursListA.putParam("OOLastUpdateEmpNoName", "");
					}else {
						occursListA.putParam("OOLastUpdateEmpNoName", iCdEmp.getFullname());
					}
				}
				String taU = rJcicZ061.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursListA.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursListA.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
				occursListA.putParam("OOOutJcicTxtDate", rJcicZ061.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursListA);
			}
			if (rJcicZ061Log == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			for (JcicZ061Log rrJcicZ061Log:rJcicZ061Log) {
				OccursList occursList = new OccursList();
				String iLastUpdateEmpNo = rrJcicZ061Log.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursList.putParam("OOTranKey", rrJcicZ061Log.getTranKey());
				occursList.putParam("OOExpBalanceAmt", rrJcicZ061Log.getExpBalanceAmt());
				occursList.putParam("OOCashBalanceAmt", rrJcicZ061Log.getCashBalanceAmt());
				occursList.putParam("OOCreditBalanceAmt", rrJcicZ061Log.getCreditBalanceAmt());
				occursList.putParam("OOMaxMainNote", rrJcicZ061Log.getMaxMainNote());
				occursList.putParam("OOIsGuarantor", rrJcicZ061Log.getIsGuarantor());
				occursList.putParam("OOIsChangePayment", rrJcicZ061Log.getIsChangePayment());
				iCdEmp = iCdEmpService.findAgentIdFirst(iLastUpdateEmpNo, titaVo);
				if (iLastUpdateEmpNo.equals("")) {
					occursList.putParam("OOLastUpdateEmpNoName", "");
				}else {
					if (iCdEmp == null) {
						occursList.putParam("OOLastUpdateEmpNoName", "");
					}else {
						occursList.putParam("OOLastUpdateEmpNoName", iCdEmp.getFullname());
					}
				}
				String taU = rrJcicZ061Log.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursList.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursList.putParam("OOLastUpdateEmpNo",iLastUpdateEmpNo);
				occursList.putParam("OOOutJcicTxtDate", rrJcicZ061Log.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursList);
			}		
			this.addList(this.totaVo);
			return this.sendList();
		}
}
	


