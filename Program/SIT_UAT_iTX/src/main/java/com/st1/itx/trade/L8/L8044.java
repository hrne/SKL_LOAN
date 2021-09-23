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
import com.st1.itx.db.domain.JcicZ053;
import com.st1.itx.db.domain.JcicZ053Log;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ053LogService;
import com.st1.itx.db.service.JcicZ053Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8044")
@Scope("prototype")
public class L8044 extends TradeBuffer  {
		@Autowired
		public CdEmpService iCdEmpService;
		@Autowired
		public JcicZ053Service iJcicZ053Service;
		@Autowired
		public JcicZ053LogService iJcicZ053LogService;
		@Override
		public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
			this.info("active L8044 ");
			this.totaVo.init(titaVo);
			String iUkey = titaVo.getParam("Ukey");
			
			this.index = titaVo.getReturnIndex();
			this.limit = 500;

			Slice<JcicZ053Log> rJcicZ053Log = null;
			rJcicZ053Log = iJcicZ053LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
			JcicZ053 rJcicZ053 = new JcicZ053();
			rJcicZ053 = iJcicZ053Service.ukeyFirst(iUkey, titaVo);
			if (rJcicZ053 == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			if (!rJcicZ053.getTranKey().equals("A")&&rJcicZ053.getOutJcicTxtDate()==0) {
				OccursList occursListA = new OccursList();
				String iLastUpdateEmpNo = rJcicZ053.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursListA.putParam("OOTranKey", rJcicZ053.getTranKey());
				occursListA.putParam("OOAgreeSend",rJcicZ053.getAgreeSend());
				occursListA.putParam("OOAgreeSendData1",rJcicZ053.getAgreeSendData1());
				occursListA.putParam("OOAgreeSendData2",rJcicZ053.getAgreeSendData2());
				occursListA.putParam("OOChangePayDate",rJcicZ053.getChangePayDate());
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
				String taU = rJcicZ053.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursListA.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursListA.putParam("OOLastUpdateEmpNo",iLastUpdateEmpNo);
				occursListA.putParam("OOOutJcicTxtDate", rJcicZ053.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursListA);
			}
			if (rJcicZ053Log == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			for (JcicZ053Log rrJcicZ053Log:rJcicZ053Log) {
				OccursList occursList = new OccursList();
				String iLastUpdateEmpNo = rrJcicZ053Log.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursList.putParam("OOTranKey", rrJcicZ053Log.getTranKey());
				occursList.putParam("OOAgreeSend",rrJcicZ053Log.getAgreeSend());
				occursList.putParam("OOAgreeSendData1",rrJcicZ053Log.getAgreeSendData1());
				occursList.putParam("OOAgreeSendData2",rrJcicZ053Log.getAgreeSendData2());
				occursList.putParam("OOChangePayDate",rrJcicZ053Log.getChangePayDate());
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
				String taU = rrJcicZ053Log.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursList.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
				occursList.putParam("OOOutJcicTxtDate", rrJcicZ053Log.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursList);
			}		
			this.addList(this.totaVo);
			return this.sendList();
		}
}
	


