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
import com.st1.itx.db.domain.JcicZ055;
import com.st1.itx.db.domain.JcicZ055Log;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ055LogService;
import com.st1.itx.db.service.JcicZ055Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8046")
@Scope("prototype")
public class L8046 extends TradeBuffer  {
		@Autowired
		public CdEmpService iCdEmpService;
		@Autowired
		public JcicZ055Service iJcicZ055Service;
		@Autowired
		public JcicZ055LogService iJcicZ055LogService;
		@Override
		public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
			this.info("active L8046 ");
			this.totaVo.init(titaVo);
			String iUkey = titaVo.getParam("Ukey");
			
			this.index = titaVo.getReturnIndex();
			this.limit = 500;

			Slice<JcicZ055Log> rJcicZ055Log = null;
			rJcicZ055Log = iJcicZ055LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
			JcicZ055 rJcicZ055 = new JcicZ055();
			rJcicZ055 = iJcicZ055Service.ukeyFirst(iUkey, titaVo);
			if (rJcicZ055 == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			if (!rJcicZ055.getTranKey().equals("A")&&rJcicZ055.getOutJcicTxtDate()==0) {
				OccursList occursListA = new OccursList();
				String iLastUpdateEmpNo = rJcicZ055.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursListA.putParam("OOTranKey", rJcicZ055.getTranKey());
				occursListA.putParam("OOYear", rJcicZ055.getYear());
				occursListA.putParam("OOCourtDiv", rJcicZ055.getCourtDiv());
				occursListA.putParam("OOCourtCaseNo", rJcicZ055.getCourtCaseNo());
				occursListA.putParam("OOPayDate", rJcicZ055.getPayDate());
				occursListA.putParam("OOPayEndDate", rJcicZ055.getPayEndDate());
				occursListA.putParam("OOPeriod", rJcicZ055.getPeriod());
				occursListA.putParam("OORate", rJcicZ055.getRate());
				occursListA.putParam("OOOutstandAmt", rJcicZ055.getOutstandAmt());
				occursListA.putParam("OOSubAmt", rJcicZ055.getSubAmt());
				occursListA.putParam("OOClaimStatus1", rJcicZ055.getClaimStatus1());
				occursListA.putParam("OOSaveDate", rJcicZ055.getSaveDate());
				occursListA.putParam("OOClaimStatus2", rJcicZ055.getClaimStatus2());
				occursListA.putParam("OOSaveEndDate", rJcicZ055.getSaveEndDate());
				occursListA.putParam("OOIsImplement", rJcicZ055.getIsImplement());
				occursListA.putParam("OOInspectName", rJcicZ055.getInspectName());
				
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
				String taU = rJcicZ055.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursListA.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursListA.putParam("OOLastUpdateEmpNo",iLastUpdateEmpNo);
				occursListA.putParam("OOOutJcicTxtDate", rJcicZ055.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursListA);
			}
			if (rJcicZ055Log == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			for (JcicZ055Log rrJcicZ055Log:rJcicZ055Log) {
				OccursList occursList = new OccursList();
				String iLastUpdateEmpNo = rrJcicZ055Log.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursList.putParam("OOTranKey", rrJcicZ055Log.getTranKey());
				occursList.putParam("OOYear", rrJcicZ055Log.getYear());
				occursList.putParam("OOCourtDiv", rrJcicZ055Log.getCourtDiv());
				occursList.putParam("OOCourtCaseNo", rrJcicZ055Log.getCourtCaseNo());
				occursList.putParam("OOPayDate", rrJcicZ055Log.getPayDate());
				occursList.putParam("OOPayEndDate", rrJcicZ055Log.getPayEndDate());
				occursList.putParam("OOPeriod", rrJcicZ055Log.getPeriod());
				occursList.putParam("OORate", rrJcicZ055Log.getRate());
				occursList.putParam("OOOutstandAmt", rrJcicZ055Log.getOutstandAmt());
				occursList.putParam("OOSubAmt", rrJcicZ055Log.getSubAmt());
				occursList.putParam("OOClaimStatus1", rrJcicZ055Log.getClaimStatus1());
				occursList.putParam("OOSaveDate", rrJcicZ055Log.getSaveDate());
				occursList.putParam("OOClaimStatus2", rrJcicZ055Log.getClaimStatus2());
				occursList.putParam("OOSaveEndDate", rrJcicZ055Log.getSaveEndDate());
				occursList.putParam("OOIsImplement", rrJcicZ055Log.getIsImplement());
				occursList.putParam("OOInspectName", rrJcicZ055Log.getInspectName());
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
				String taU = rrJcicZ055Log.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursList.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
				occursList.putParam("OOOutJcicTxtDate", rrJcicZ055Log.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursList);
			}		
			this.addList(this.totaVo);
			return this.sendList();
		}
}
	


