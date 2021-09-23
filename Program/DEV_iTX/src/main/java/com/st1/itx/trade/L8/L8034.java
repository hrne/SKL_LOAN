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
import com.st1.itx.db.domain.JcicZ043;
import com.st1.itx.db.domain.JcicZ043Log;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ043LogService;
import com.st1.itx.db.service.JcicZ043Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8034")
@Scope("prototype")
public class L8034 extends TradeBuffer  {
		@Autowired
		public CdEmpService iCdEmpService;
		@Autowired
		public JcicZ043Service iJcicZ043Service;
		@Autowired
		public JcicZ043LogService iJcicZ043LogService;
		@Override
		public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
			this.info("active L8034 ");
			this.totaVo.init(titaVo);
			String iUkey = titaVo.getParam("Ukey");
			this.index = titaVo.getReturnIndex();
			this.limit = 500;

			Slice<JcicZ043Log> rJcicZ043Log = null;
			rJcicZ043Log = iJcicZ043LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
			JcicZ043 rJcicZ043 = new JcicZ043();
			rJcicZ043 = iJcicZ043Service.ukeyFirst(iUkey, titaVo);
			if (rJcicZ043 == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			if (!rJcicZ043.getTranKey().equals("A")&&rJcicZ043.getOutJcicTxtDate()==0) {
				OccursList occursListA = new OccursList();
				String iLastUpdateEmpNo = rJcicZ043.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursListA.putParam("OOTranKey", rJcicZ043.getTranKey());
                occursListA.putParam("OOCollateralType", rJcicZ043.getCollateralType());
                occursListA.putParam("OOOriginLoanAmt", rJcicZ043.getOriginLoanAmt());
                occursListA.putParam("OOCreditBalance", rJcicZ043.getCreditBalance());
                occursListA.putParam("OOPerPeriordAmt", rJcicZ043.getPerPeriordAmt());
                occursListA.putParam("OOLastPayAmt", rJcicZ043.getLastPayAmt());
                occursListA.putParam("OOLastPayDate", rJcicZ043.getLastPayDate());
                occursListA.putParam("OOOutstandAmt", rJcicZ043.getOutstandAmt());
                occursListA.putParam("OORepayPerMonDay", rJcicZ043.getRepayPerMonDay());
                occursListA.putParam("OOContractStartYM", rJcicZ043.getContractStartYM());
                occursListA.putParam("OOContractEndYM", rJcicZ043.getContractEndYM());
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
				String taU = rJcicZ043.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursListA.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursListA.putParam("OOLastUpdateEmpNo",iLastUpdateEmpNo);
				occursListA.putParam("OOOutJcicTxtDate", rJcicZ043.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursListA);
			}
			if (rJcicZ043Log == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			for (JcicZ043Log rrJcicZ043Log:rJcicZ043Log) {
				OccursList occursList = new OccursList();
				String iLastUpdateEmpNo = rrJcicZ043Log.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursList.putParam("OOTranKey", rrJcicZ043Log.getTranKey());
                occursList.putParam("OOCollateralType", rrJcicZ043Log.getCollateralType());
                occursList.putParam("OOOriginLoanAmt", rrJcicZ043Log.getOriginLoanAmt());
                occursList.putParam("OOCreditBalance", rrJcicZ043Log.getCreditBalance());
                occursList.putParam("OOPerPeriordAmt", rrJcicZ043Log.getPerPeriordAmt());
                occursList.putParam("OOLastPayAmt", rrJcicZ043Log.getLastPayAmt());
                occursList.putParam("OOLastPayDate", rrJcicZ043Log.getLastPayDate());
                occursList.putParam("OOOutstandAmt", rrJcicZ043Log.getOutstandAmt());
                occursList.putParam("OORepayPerMonDay", rrJcicZ043Log.getRepayPerMonDay());
                occursList.putParam("OOContractStartYM", rrJcicZ043Log.getContractStartYM());
                occursList.putParam("OOContractEndYM", rrJcicZ043Log.getContractEndYM());
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
				String taU = rrJcicZ043Log.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursList.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
				occursList.putParam("OOOutJcicTxtDate", rrJcicZ043Log.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursList);
			}		
			this.addList(this.totaVo);
			return this.sendList();
		}
}
	


