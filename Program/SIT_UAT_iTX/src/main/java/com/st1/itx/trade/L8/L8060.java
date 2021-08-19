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
import com.st1.itx.db.domain.JcicZ451;
import com.st1.itx.db.domain.JcicZ451Log;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ451LogService;
import com.st1.itx.db.service.JcicZ451Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8060")
@Scope("prototype")
public class L8060 extends TradeBuffer  {
		@Autowired
		public CdEmpService iCdEmpService;
		@Autowired
		public JcicZ451Service iJcicZ451Service;
		@Autowired
		public JcicZ451LogService iJcicZ451LogService;
		@Override
		public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
			this.info("active L8060 ");
			this.totaVo.init(titaVo);
			String iUkey = titaVo.getParam("Ukey");
			
			this.index = titaVo.getReturnIndex();
			this.limit = 500;

			Slice<JcicZ451Log> rJcicZ451Log = null;
			rJcicZ451Log = iJcicZ451LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
			JcicZ451 rJcicZ451 = new JcicZ451();
			rJcicZ451 = iJcicZ451Service.ukeyFirst(iUkey, titaVo);
			if (rJcicZ451 == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			if (!rJcicZ451.getTranKey().equals("A")&&rJcicZ451.getOutJcicTxtDate()==0) {
				OccursList occursListA = new OccursList();
				String iLastUpdateEmpNo = rJcicZ451.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursListA.putParam("OOTranKey", rJcicZ451.getTranKey());
				occursListA.putParam("OODelayCode", rJcicZ451.getDelayCode());			
				iCdEmp = iCdEmpService.findAgentIdFirst(iLastUpdateEmpNo, titaVo);
				if(iLastUpdateEmpNo.equals("")) {
					occursListA.putParam("OOLastUpdateEmpNoName", "");
				}else {
					if(iCdEmp == null) {
						occursListA.putParam("OOLastUpdateEmpNoName", "");
					}else {
						occursListA.putParam("OOLastUpdateEmpNoName",iCdEmp.getFullname());
					}
				}
				String taU = rJcicZ451.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursListA.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursListA.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
				occursListA.putParam("OOOutJcicTxtDate", rJcicZ451.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursListA);
			}
			if (rJcicZ451Log == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			for (JcicZ451Log rrJcicZ451Log:rJcicZ451Log) {
				OccursList occursList = new OccursList();
				String iLastUpdateEmpNo = rrJcicZ451Log.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursList.putParam("OOTranKey", rrJcicZ451Log.getTranKey());
				occursList.putParam("OODelayCode", rrJcicZ451Log.getDelayCode());	
				iCdEmp = iCdEmpService.findAgentIdFirst(iLastUpdateEmpNo, titaVo);
				if(iLastUpdateEmpNo.equals("")) {
					occursList.putParam("OOLastUpdateEmpNoName", "");
				}else {
					if(iCdEmp == null) {
						occursList.putParam("OOLastUpdateEmpNoName", "");
					}else {
						occursList.putParam("OOLastUpdateEmpNoName",iCdEmp.getFullname());
					}
				}
				String taU = rrJcicZ451Log.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursList.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
				occursList.putParam("OOOutJcicTxtDate", rrJcicZ451Log.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursList);
			}		
			this.addList(this.totaVo);
			return this.sendList();
		}
}
	


