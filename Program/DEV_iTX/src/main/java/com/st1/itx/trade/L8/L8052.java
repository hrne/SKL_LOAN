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
import com.st1.itx.db.domain.JcicZ440;
import com.st1.itx.db.domain.JcicZ440Log;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ440LogService;
import com.st1.itx.db.service.JcicZ440Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8052")
@Scope("prototype")
public class L8052 extends TradeBuffer  {
		@Autowired
		public CdEmpService iCdEmpService;
		@Autowired
		public JcicZ440Service iJcicZ440Service;
		@Autowired
		public JcicZ440LogService iJcicZ440LogService;
		@Autowired
		public CdCodeService iCdCodeService;
		@Override
		public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
			this.info("active L8052 ");
			this.totaVo.init(titaVo);
			String iUkey = titaVo.getParam("Ukey");
			
			this.index = titaVo.getReturnIndex();
			this.limit = 500;

			Slice<JcicZ440Log> rJcicZ440Log = null;
			rJcicZ440Log = iJcicZ440LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
			JcicZ440 rJcicZ440 = new JcicZ440();
			rJcicZ440 = iJcicZ440Service.ukeyFirst(iUkey, titaVo);
			if (rJcicZ440 == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			if (!rJcicZ440.getTranKey().equals("A")&&rJcicZ440.getOutJcicTxtDate()==0) {
				OccursList occursListA = new OccursList();
				String iLastUpdateEmpNo = rJcicZ440.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursListA.putParam("OOTranKey", rJcicZ440.getTranKey());
				occursListA.putParam("OOAgreeDate", rJcicZ440.getAgreeDate());
				occursListA.putParam("OOStartDate", rJcicZ440.getStartDate());
				occursListA.putParam("OORemindDate", rJcicZ440.getRemindDate());
				occursListA.putParam("OOApplyType", rJcicZ440.getApplyType());
				occursListA.putParam("OOReportYn", rJcicZ440.getReportYn());
				occursListA.putParam("OONotBankId1", rJcicZ440.getNotBankId1());
				occursListA.putParam("OONotBankId1X", dealBankName(rJcicZ440.getNotBankId1(),titaVo));
				occursListA.putParam("OONotBankId2", rJcicZ440.getNotBankId2());
				occursListA.putParam("OONotBankId2X", dealBankName(rJcicZ440.getNotBankId2(),titaVo));
				occursListA.putParam("OONotBankId3", rJcicZ440.getNotBankId3());
				occursListA.putParam("OONotBankId3X", dealBankName(rJcicZ440.getNotBankId3(),titaVo));
				occursListA.putParam("OONotBankId4", rJcicZ440.getNotBankId4());
				occursListA.putParam("OONotBankId4X", dealBankName(rJcicZ440.getNotBankId4(),titaVo));
				occursListA.putParam("OONotBankId5", rJcicZ440.getNotBankId5());
				occursListA.putParam("OONotBankId5X", dealBankName(rJcicZ440.getNotBankId5(),titaVo));
				occursListA.putParam("OONotBankId6", rJcicZ440.getNotBankId6());			
				occursListA.putParam("OONotBankId6X", dealBankName(rJcicZ440.getNotBankId6(),titaVo));
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
				String taU = rJcicZ440.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursListA.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursListA.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
				occursListA.putParam("OOOutJcicTxtDate", rJcicZ440.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursListA);
			}
			if (rJcicZ440Log == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			for (JcicZ440Log rrJcicZ440Log:rJcicZ440Log) {
				OccursList occursList = new OccursList();
				String iLastUpdateEmpNo = rrJcicZ440Log.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursList.putParam("OOTranKey", rrJcicZ440Log.getTranKey());
				occursList.putParam("OOAgreeDate", rrJcicZ440Log.getAgreeDate());
				occursList.putParam("OOStartDate", rrJcicZ440Log.getStartDate());
				occursList.putParam("OORemindDate", rrJcicZ440Log.getRemindDate());
				occursList.putParam("OOApplyType", rrJcicZ440Log.getApplyType());
				occursList.putParam("OOReportYn", rrJcicZ440Log.getReportYn());
				occursList.putParam("OONotBankId1", rrJcicZ440Log.getNotBankId1());
				occursList.putParam("OONotBankId2", rrJcicZ440Log.getNotBankId2());
				occursList.putParam("OONotBankId3", rrJcicZ440Log.getNotBankId3());
				occursList.putParam("OONotBankId4", rrJcicZ440Log.getNotBankId4());
				occursList.putParam("OONotBankId5", rrJcicZ440Log.getNotBankId5());
				occursList.putParam("OONotBankId6", rrJcicZ440Log.getNotBankId6());
				occursList.putParam("OONotBankId1X", dealBankName(rrJcicZ440Log.getNotBankId1(),titaVo));
				occursList.putParam("OONotBankId2X", dealBankName(rrJcicZ440Log.getNotBankId2(),titaVo));
				occursList.putParam("OONotBankId3X", dealBankName(rrJcicZ440Log.getNotBankId3(),titaVo));
				occursList.putParam("OONotBankId4X", dealBankName(rrJcicZ440Log.getNotBankId4(),titaVo));
				occursList.putParam("OONotBankId5X", dealBankName(rrJcicZ440Log.getNotBankId5(),titaVo));
				occursList.putParam("OONotBankId6X", dealBankName(rrJcicZ440Log.getNotBankId6(),titaVo));
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
				String taU = rrJcicZ440Log.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursList.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
				occursList.putParam("OOOutJcicTxtDate", rrJcicZ440Log.getOutJcicTxtDate());
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
	


