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
import com.st1.itx.db.domain.JcicZ050;
import com.st1.itx.db.domain.JcicZ050Log;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ050LogService;
import com.st1.itx.db.service.JcicZ050Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8041")
@Scope("prototype")
public class L8041 extends TradeBuffer  {
		@Autowired
		public CdEmpService iCdEmpService;
		@Autowired
		public JcicZ050Service iJcicZ050Service;
		@Autowired
		public JcicZ050LogService iJcicZ050LogService;
		@Override
		public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
			this.info("active L8041 ");
			this.totaVo.init(titaVo);
			String iUkey = titaVo.getParam("Ukey");
			
			this.index = titaVo.getReturnIndex();
			this.limit = 500;

			Slice<JcicZ050Log> rJcicZ050Log = null;
			rJcicZ050Log = iJcicZ050LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
			JcicZ050 rJcicZ050 = new JcicZ050();
			rJcicZ050 = iJcicZ050Service.ukeyFirst(iUkey, titaVo);
			if (rJcicZ050 == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			if (!rJcicZ050.getTranKey().equals("A")&&rJcicZ050.getOutJcicTxtDate()==0) {
				OccursList occursListA = new OccursList();
				String iLastUpdateEmpNo = rJcicZ050.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursListA.putParam("OOTranKey", rJcicZ050.getTranKey());
				occursListA.putParam("OOPayAmt", rJcicZ050.getPayAmt());
				occursListA.putParam("OOSumRepayActualAmt", rJcicZ050.getSumRepayActualAmt());
				occursListA.putParam("OOSumRepayShouldAmt", rJcicZ050.getSumRepayShouldAmt());
				occursListA.putParam("OOSecondRepayYM", rJcicZ050.getSecondRepayYM());
				occursListA.putParam("OOStatus", rJcicZ050.getStatus());
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
				String taU = rJcicZ050.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursListA.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursListA.putParam("OOLastUpdateEmpNo",iLastUpdateEmpNo);
				occursListA.putParam("OOOutJcicTxtDate", rJcicZ050.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursListA);
			}
			if (rJcicZ050Log == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			for (JcicZ050Log rrJcicZ050Log:rJcicZ050Log) {
				OccursList occursList = new OccursList();
				String iLastUpdateEmpNo = rrJcicZ050Log.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursList.putParam("OOTranKey", rrJcicZ050Log.getTranKey());
				occursList.putParam("OOPayAmt", rrJcicZ050Log.getPayAmt());
				occursList.putParam("OOSumRepayActualAmt", rrJcicZ050Log.getSumRepayActualAmt());
				occursList.putParam("OOSumRepayShouldAmt", rrJcicZ050Log.getSumRepayShouldAmt());
				occursList.putParam("OOSecondRepayYM", rrJcicZ050Log.getSecondRepayYM());
				occursList.putParam("OOStatus", rrJcicZ050Log.getStatus());
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
				String taU = rrJcicZ050Log.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursList.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
				occursList.putParam("OOOutJcicTxtDate", rrJcicZ050Log.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursList);
			}		
			this.addList(this.totaVo);
			return this.sendList();
		}
}
	


