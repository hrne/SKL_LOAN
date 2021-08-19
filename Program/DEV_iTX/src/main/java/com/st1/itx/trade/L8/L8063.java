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
import com.st1.itx.db.domain.JcicZ571;
import com.st1.itx.db.domain.JcicZ571Log;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ571LogService;
import com.st1.itx.db.service.JcicZ571Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8063")
@Scope("prototype")
public class L8063 extends TradeBuffer  {
		@Autowired
		public CdEmpService iCdEmpService;
		@Autowired
		public JcicZ571Service iJcicZ571Service;
		@Autowired
		public JcicZ571LogService iJcicZ571LogService;
		@Override
		public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
			this.info("active L8063 ");
			this.totaVo.init(titaVo);
			String iUkey = titaVo.getParam("Ukey");
			
			this.index = titaVo.getReturnIndex();
			this.limit = 500;

			Slice<JcicZ571Log> rJcicZ571Log = null;
			rJcicZ571Log = iJcicZ571LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
			JcicZ571 rJcicZ571 = new JcicZ571();
			rJcicZ571 = iJcicZ571Service.ukeyFirst(iUkey, titaVo);
			if (rJcicZ571 == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			if (!rJcicZ571.getTranKey().equals("A")&&rJcicZ571.getOutJcicTxtDate()==0) {
				OccursList occursListA = new OccursList();
				String iLastUpdateEmpNo = rJcicZ571.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursListA.putParam("OOTranKey", rJcicZ571.getTranKey());
				occursListA.putParam("OOOwnerYn", rJcicZ571.getOwnerYn());
                occursListA.putParam("OOPayYn", rJcicZ571.getPayYn());
                occursListA.putParam("OOOwnerAmt", rJcicZ571.getOwnerAmt());
                occursListA.putParam("OOAllotAmt", rJcicZ571.getAllotAmt());
                occursListA.putParam("OOUnallotAmt", rJcicZ571.getUnallotAmt());
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
				String taU = rJcicZ571.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursListA.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursListA.putParam("OOLastUpdateEmpNo", rJcicZ571.getLastUpdateEmpNo());
				occursListA.putParam("OOOutJcicTxtDate", rJcicZ571.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursListA);
			}
			if (rJcicZ571Log == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			for (JcicZ571Log rrJcicZ571Log:rJcicZ571Log) {
				OccursList occursList = new OccursList();
				String iLastUpdateEmpNo = rrJcicZ571Log.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursList.putParam("OOTranKey", rrJcicZ571Log.getTranKey());
                occursList.putParam("OOOwnerYn", rrJcicZ571Log.getOwnerYn());
                occursList.putParam("OOPayYn", rrJcicZ571Log.getPayYn());
                occursList.putParam("OOOwnerAmt", rrJcicZ571Log.getOwnerAmt());
                occursList.putParam("OOAllotAmt", rrJcicZ571Log.getAllotAmt());
                occursList.putParam("OOUnallotAmt", rrJcicZ571Log.getUnallotAmt());			
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
				String taU = rrJcicZ571Log.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursList.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursList.putParam("OOLastUpdateEmpNo", rrJcicZ571Log.getLastUpdateEmpNo());
				occursList.putParam("OOOutJcicTxtDate", rrJcicZ571Log.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursList);
			}		
			this.addList(this.totaVo);
			return this.sendList();
		}
}
	


