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
import com.st1.itx.db.domain.JcicZ041;
import com.st1.itx.db.domain.JcicZ041Log;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ041LogService;
import com.st1.itx.db.service.JcicZ041Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8032")
@Scope("prototype")
public class L8032 extends TradeBuffer  {
		@Autowired
		public CdEmpService iCdEmpService;
		@Autowired
		public JcicZ041Service iJcicZ041Service;
		@Autowired
		public JcicZ041LogService iJcicZ041LogService;
		@Override
		public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
			this.info("active L8032 ");
			this.totaVo.init(titaVo);
			String iUkey = titaVo.getParam("Ukey");
			this.index = titaVo.getReturnIndex();
			this.limit = 500;

			Slice<JcicZ041Log> rJcicZ041Log = null;
			rJcicZ041Log = iJcicZ041LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
			JcicZ041 rJcicZ041 = new JcicZ041();
			rJcicZ041 = iJcicZ041Service.ukeyFirst(iUkey, titaVo);
			if (rJcicZ041 == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			if (!rJcicZ041.getTranKey().equals("A")&&rJcicZ041.getOutJcicTxtDate()==0) {
				OccursList occursListA = new OccursList();
				String iLastUpdateEmpNo = rJcicZ041.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursListA.putParam("OOTranKey", rJcicZ041.getTranKey());
                occursListA.putParam("OOScDate", rJcicZ041.getScDate());
                occursListA.putParam("OONegoStartDate", rJcicZ041.getNegoStartDate());
                occursListA.putParam("OONonFinClaimAmt", rJcicZ041.getNonFinClaimAmt());
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
				String taU = rJcicZ041.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursListA.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursListA.putParam("OOLastUpdateEmpNo",iLastUpdateEmpNo);
				occursListA.putParam("OOOutJcicTxtDate", rJcicZ041.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursListA);
			}
			if (rJcicZ041Log == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			for (JcicZ041Log rrJcicZ041Log:rJcicZ041Log) {
				OccursList occursList = new OccursList();
				String iLastUpdateEmpNo = rrJcicZ041Log.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursList.putParam("OOTranKey", rrJcicZ041Log.getTranKey());
                occursList.putParam("OOScDate", rrJcicZ041Log.getScDate());
                occursList.putParam("OONegoStartDate", rrJcicZ041Log.getNegoStartDate());
                occursList.putParam("OONonFinClaimAmt", rrJcicZ041Log.getNonFinClaimAmt());
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
				String taU = rrJcicZ041Log.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursList.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
				occursList.putParam("OOOutJcicTxtDate", rrJcicZ041Log.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursList);
			}		
			this.addList(this.totaVo);
			return this.sendList();
		}
}
	


