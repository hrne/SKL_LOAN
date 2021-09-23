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
import com.st1.itx.db.domain.JcicZ450;
import com.st1.itx.db.domain.JcicZ450Log;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ450LogService;
import com.st1.itx.db.service.JcicZ450Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8059")
@Scope("prototype")
public class L8059 extends TradeBuffer  {
		@Autowired
		public CdEmpService iCdEmpService;
		@Autowired
		public JcicZ450Service iJcicZ450Service;
		@Autowired
		public JcicZ450LogService iJcicZ450LogService;
		@Override
		public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
			this.info("active L8059 ");
			this.totaVo.init(titaVo);
			String iUkey = titaVo.getParam("Ukey");
			
			this.index = titaVo.getReturnIndex();
			this.limit = 500;

			Slice<JcicZ450Log> rJcicZ450Log = null;
			rJcicZ450Log = iJcicZ450LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
			JcicZ450 rJcicZ450 = new JcicZ450();
			rJcicZ450 = iJcicZ450Service.ukeyFirst(iUkey, titaVo);
			if (rJcicZ450 == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			if (!rJcicZ450.getTranKey().equals("A")&&rJcicZ450.getOutJcicTxtDate()==0) {
				OccursList occursListA = new OccursList();
				String iLastUpdateEmpNo = rJcicZ450.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursListA.putParam("OOTranKey", rJcicZ450.getTranKey());
				occursListA.putParam("OOPayAmt",rJcicZ450.getPayAmt());
				occursListA.putParam("OOSumRepayActualAmt",rJcicZ450.getSumRepayActualAmt());
				occursListA.putParam("OOSumRepayShouldAmt",rJcicZ450.getSumRepayShouldAmt());
				occursListA.putParam("OOPayStatus",rJcicZ450.getPayStatus());
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
				String taU = rJcicZ450.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursListA.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursListA.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
				occursListA.putParam("OOOutJcicTxtDate", rJcicZ450.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursListA);
			}
			if (rJcicZ450Log == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			for (JcicZ450Log rrJcicZ450Log:rJcicZ450Log) {
				OccursList occursList = new OccursList();
				String iLastUpdateEmpNo = rrJcicZ450Log.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursList.putParam("OOTranKey", rrJcicZ450Log.getTranKey());
				occursList.putParam("OOPayAmt",rrJcicZ450Log.getPayAmt());
				occursList.putParam("OOSumRepayActualAmt",rrJcicZ450Log.getSumRepayActualAmt());
				occursList.putParam("OOSumRepayShouldAmt",rrJcicZ450Log.getSumRepayShouldAmt());
				occursList.putParam("OOPayStatus",rrJcicZ450Log.getPayStatus());
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
 				String taU = rrJcicZ450Log.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursList.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
				occursList.putParam("OOOutJcicTxtDate", rrJcicZ450Log.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursList);
			}		
			this.addList(this.totaVo);
			return this.sendList();
		}
}
	


