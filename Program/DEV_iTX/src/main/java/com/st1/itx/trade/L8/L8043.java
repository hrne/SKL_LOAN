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
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.JcicZ052;
import com.st1.itx.db.domain.JcicZ052Log;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ052LogService;
import com.st1.itx.db.service.JcicZ052Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8043")
@Scope("prototype")
public class L8043 extends TradeBuffer  {
		@Autowired
		public CdEmpService iCdEmpService;
		@Autowired
		public JcicZ052Service iJcicZ052Service;
		@Autowired
		public JcicZ052LogService iJcicZ052LogService;
		@Autowired
		public CdCodeService iCdCodeService;
		@Override
		public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
			this.info("active L8043 ");
			this.totaVo.init(titaVo);
			String iUkey = titaVo.getParam("Ukey");
			
			this.index = titaVo.getReturnIndex();
			this.limit = 500;

			Slice<JcicZ052Log> rJcicZ052Log = null;
			rJcicZ052Log = iJcicZ052LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
			JcicZ052 rJcicZ052 = new JcicZ052();
			rJcicZ052 = iJcicZ052Service.ukeyFirst(iUkey, titaVo);
			if (rJcicZ052 == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			if (!rJcicZ052.getTranKey().equals("A")&&rJcicZ052.getOutJcicTxtDate()==0) {
				OccursList occursListA = new OccursList();
				String iLastUpdateEmpNo = rJcicZ052.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursListA.putParam("OOTranKey", rJcicZ052.getTranKey());
				occursListA.putParam("OOBankCode1",rJcicZ052.getBankCode1());
				occursListA.putParam("OODataCode1",rJcicZ052.getDataCode1());
				occursListA.putParam("OOBankCode2",rJcicZ052.getBankCode2());
				occursListA.putParam("OODataCode2",rJcicZ052.getDataCode2());
				occursListA.putParam("OOBankCode3",rJcicZ052.getBankCode3());
				occursListA.putParam("OODataCode3",rJcicZ052.getDataCode3());
				occursListA.putParam("OOBankCode4",rJcicZ052.getBankCode4());
				occursListA.putParam("OODataCode4",rJcicZ052.getDataCode4());
				occursListA.putParam("OOBankCode5",rJcicZ052.getBankCode5());
				occursListA.putParam("OODataCode5",rJcicZ052.getDataCode5());
                occursListA.putParam("OOBankCode1X", dealBankName(rJcicZ052.getBankCode1(),titaVo));
				occursListA.putParam("OOBankCode2X", dealBankName(rJcicZ052.getBankCode2(),titaVo));
				occursListA.putParam("OOBankCode3X", dealBankName(rJcicZ052.getBankCode3(),titaVo));
				occursListA.putParam("OOBankCode4X", dealBankName(rJcicZ052.getBankCode4(),titaVo));
				occursListA.putParam("OOBankCode5X", dealBankName(rJcicZ052.getBankCode5(),titaVo));

				occursListA.putParam("OOChangePayDate",rJcicZ052.getChangePayDate());
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
				String taU = rJcicZ052.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursListA.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursListA.putParam("OOLastUpdateEmpNo",iLastUpdateEmpNo);
				occursListA.putParam("OOOutJcicTxtDate", rJcicZ052.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursListA);
			}
			if (rJcicZ052Log == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			for (JcicZ052Log rrJcicZ052Log:rJcicZ052Log) {
				OccursList occursList = new OccursList();
				String iLastUpdateEmpNo = rrJcicZ052Log.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursList.putParam("OOTranKey", rrJcicZ052Log.getTranKey());
				occursList.putParam("OOBankCode1",rrJcicZ052Log.getBankCode1());
				occursList.putParam("OODataCode1",rrJcicZ052Log.getDataCode1());
				occursList.putParam("OOBankCode2",rrJcicZ052Log.getBankCode2());
				occursList.putParam("OODataCode2",rrJcicZ052Log.getDataCode2());
				occursList.putParam("OOBankCode3",rrJcicZ052Log.getBankCode3());
				occursList.putParam("OODataCode3",rrJcicZ052Log.getDataCode3());
				occursList.putParam("OOBankCode4",rrJcicZ052Log.getBankCode4());
				occursList.putParam("OODataCode4",rrJcicZ052Log.getDataCode4());
				occursList.putParam("OOBankCode5",rrJcicZ052Log.getBankCode5());
				occursList.putParam("OODataCode5",rrJcicZ052Log.getDataCode5());
				
                occursList.putParam("OOBankCode1X", dealBankName(rrJcicZ052Log.getBankCode1(),titaVo));
				occursList.putParam("OOBankCode2X", dealBankName(rrJcicZ052Log.getBankCode2(),titaVo));
				occursList.putParam("OOBankCode3X", dealBankName(rrJcicZ052Log.getBankCode3(),titaVo));
				occursList.putParam("OOBankCode4X", dealBankName(rrJcicZ052Log.getBankCode4(),titaVo));
				occursList.putParam("OOBankCode5X", dealBankName(rrJcicZ052Log.getBankCode5(),titaVo));

				occursList.putParam("OOChangePayDate",rrJcicZ052Log.getChangePayDate());
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
				String taU = rrJcicZ052Log.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursList.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
				occursList.putParam("OOOutJcicTxtDate", rrJcicZ052Log.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursList);
			}		
			this.addList(this.totaVo);
			return this.sendList();
		}
		public String dealBankName(String BankId,TitaVo titaVo) throws LogicException {
			CdCode tCdCode = new CdCode();
			tCdCode=iCdCodeService.getItemFirst(8, "JcicBankCode", BankId,titaVo);
			String JcicBankName="";//80碼長度
			if(tCdCode!=null) {
				JcicBankName=tCdCode.getItem();
			}
			return JcicBankName;
		}
}
	


