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
import com.st1.itx.db.domain.JcicZ045;
import com.st1.itx.db.domain.JcicZ045Log;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ045LogService;
import com.st1.itx.db.service.JcicZ045Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8036")
@Scope("prototype")
public class L8036 extends TradeBuffer  {
		@Autowired
		public CdEmpService iCdEmpService;
		@Autowired
		public JcicZ045Service iJcicZ045Service;
		@Autowired
		public JcicZ045LogService iJcicZ045LogService;
		@Override
		public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
			this.info("active L8036 ");
			this.totaVo.init(titaVo);
			String iUkey = titaVo.getParam("Ukey");
			
			this.index = titaVo.getReturnIndex();
			this.limit = 500;

			Slice<JcicZ045Log> rJcicZ045Log = null;
			rJcicZ045Log = iJcicZ045LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
			JcicZ045 rJcicZ045 = new JcicZ045();
			rJcicZ045 = iJcicZ045Service.ukeyFirst(iUkey, titaVo);
			if (rJcicZ045 == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			if (!rJcicZ045.getTranKey().equals("A")&&rJcicZ045.getOutJcicTxtDate()==0) {
				OccursList occursListA = new OccursList();
				String iLastUpdateEmpNo = rJcicZ045.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursListA.putParam("OOTranKey", rJcicZ045.getTranKey());
				occursListA.putParam("OOAgreeCode", rJcicZ045.getAgreeCode());
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
				String taU = rJcicZ045.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursListA.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursListA.putParam("OOLastUpdateEmpNo",iLastUpdateEmpNo);
				occursListA.putParam("OOOutJcicTxtDate", rJcicZ045.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursListA);
			}
			if (rJcicZ045Log == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			for (JcicZ045Log rrJcicZ045Log:rJcicZ045Log) {
				OccursList occursList = new OccursList();
				String iLastUpdateEmpNo = rrJcicZ045Log.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursList.putParam("OOTranKey", rrJcicZ045Log.getTranKey());
				occursList.putParam("OOAgreeCode", rrJcicZ045Log.getAgreeCode());
				occursList.putParam("OOOutJcicTxtDate", rrJcicZ045Log.getOutJcicTxtDate());
				
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
				String taU = rrJcicZ045Log.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursList.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
				this.totaVo.addOccursList(occursList);
			}		
			this.addList(this.totaVo);
			return this.sendList();
		}
}
	


