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
import com.st1.itx.db.domain.JcicZ448;
import com.st1.itx.db.domain.JcicZ448Log;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ448LogService;
import com.st1.itx.db.service.JcicZ448Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8058")
@Scope("prototype")
public class L8058 extends TradeBuffer  {
		@Autowired
		public CdEmpService iCdEmpService;
		@Autowired
		public JcicZ448Service iJcicZ448Service;
		@Autowired
		public JcicZ448LogService iJcicZ448LogService;
		@Override
		public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
			this.info("active L8058 ");
			this.totaVo.init(titaVo);
			String iUkey = titaVo.getParam("Ukey");
			
			this.index = titaVo.getReturnIndex();
			this.limit = 500;

			Slice<JcicZ448Log> rJcicZ448Log = null;
			rJcicZ448Log = iJcicZ448LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
			JcicZ448 rJcicZ448 = new JcicZ448();
			rJcicZ448 = iJcicZ448Service.ukeyFirst(iUkey, titaVo);
			if (rJcicZ448 == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			if (!rJcicZ448.getTranKey().equals("A")&&rJcicZ448.getOutJcicTxtDate()==0) {
				OccursList occursListA = new OccursList();
				String iLastUpdateEmpNo = rJcicZ448.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursListA.putParam("OOTranKey", rJcicZ448.getTranKey());
				occursListA.putParam("OOSignPrin", rJcicZ448.getSignPrin());
				occursListA.putParam("OOSignOther", rJcicZ448.getSignOther());
				occursListA.putParam("OOOwnPercentage", rJcicZ448.getOwnPercentage());
				occursListA.putParam("OOAcQuitAmt", rJcicZ448.getAcQuitAmt());				
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
				String taU = rJcicZ448.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursListA.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursListA.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
				occursListA.putParam("OOOutJcicTxtDate", rJcicZ448.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursListA);
			}
			if (rJcicZ448Log == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			for (JcicZ448Log rrJcicZ448Log:rJcicZ448Log) {
				OccursList occursList = new OccursList();
				String iLastUpdateEmpNo = rrJcicZ448Log.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursList.putParam("OOTranKey", rrJcicZ448Log.getTranKey());
				occursList.putParam("OOSignPrin", rrJcicZ448Log.getSignPrin());
				occursList.putParam("OOSignOther", rrJcicZ448Log.getSignOther());
				occursList.putParam("OOOwnPercentage", rrJcicZ448Log.getOwnPercentage());
				occursList.putParam("OOAcQuitAmt", rrJcicZ448Log.getAcQuitAmt());
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
 				String taU = rrJcicZ448Log.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursList.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
				occursList.putParam("OOOutJcicTxtDate", rrJcicZ448Log.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursList);
			}		
			this.addList(this.totaVo);
			return this.sendList();
		}
}
	


