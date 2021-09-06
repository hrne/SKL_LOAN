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
import com.st1.itx.db.domain.JcicZ040;
import com.st1.itx.db.domain.JcicZ040Log;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ040LogService;
import com.st1.itx.db.service.JcicZ040Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8031")
@Scope("prototype")
public class L8031 extends TradeBuffer  {
		@Autowired
		public CdEmpService iCdEmpService;
		@Autowired
		public JcicZ040Service iJcicZ040Service;
		@Autowired
		public JcicZ040LogService iJcicZ040LogService;
		@Autowired
		public CdCodeService iCdCodeService;
		@Override
		public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
			this.info("active L8031 ");
			this.totaVo.init(titaVo);
			String iUkey = titaVo.getParam("Ukey");
			this.index = titaVo.getReturnIndex();
			this.limit = 500;

			Slice<JcicZ040Log> rJcicZ040Log = null;
			rJcicZ040Log = iJcicZ040LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
			JcicZ040 rJcicZ040 = new JcicZ040();
			rJcicZ040 = iJcicZ040Service.ukeyFirst(iUkey, titaVo);
			if (rJcicZ040 == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			if (!rJcicZ040.getTranKey().equals("A")&&rJcicZ040.getOutJcicTxtDate()==0) {
				OccursList occursListA = new OccursList();
				String iLastUpdateEmpNo = rJcicZ040.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursListA.putParam("OOTranKey", rJcicZ040.getTranKey());
				occursListA.putParam("OORbDate", rJcicZ040.getRbDate());
				occursListA.putParam("OOApplyType", rJcicZ040.getApplyType());
				occursListA.putParam("OORefBankId", rJcicZ040.getRefBankId());
				occursListA.putParam("OORefBankIdX", dealBankName(rJcicZ040.getRefBankId(),titaVo));
				occursListA.putParam("OONotBankId1", rJcicZ040.getNotBankId1());
				occursListA.putParam("OONotBankId1X", dealBankName(rJcicZ040.getNotBankId1(),titaVo));
				occursListA.putParam("OONotBankId2", rJcicZ040.getNotBankId2());
				occursListA.putParam("OONotBankId2X", dealBankName(rJcicZ040.getNotBankId2(),titaVo));
				occursListA.putParam("OONotBankId3", rJcicZ040.getNotBankId3());
				occursListA.putParam("OONotBankId3X", dealBankName(rJcicZ040.getNotBankId3(),titaVo));
				occursListA.putParam("OONotBankId4", rJcicZ040.getNotBankId4());
				occursListA.putParam("OONotBankId4X", dealBankName(rJcicZ040.getNotBankId4(),titaVo));
				occursListA.putParam("OONotBankId5", rJcicZ040.getNotBankId5());
				occursListA.putParam("OONotBankId5X", dealBankName(rJcicZ040.getNotBankId5(),titaVo));
				occursListA.putParam("OONotBankId6", rJcicZ040.getNotBankId6());
				occursListA.putParam("OONotBankId6X", dealBankName(rJcicZ040.getNotBankId6(),titaVo));
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
				String taU = rJcicZ040.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursListA.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursListA.putParam("OOLastUpdateEmpNo",iLastUpdateEmpNo);
				occursListA.putParam("OOOutJcicTxtDate", rJcicZ040.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursListA);
			}
			if (rJcicZ040Log == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			for (JcicZ040Log rrJcicZ040Log:rJcicZ040Log) {
				OccursList occursList = new OccursList();
				String iLastUpdateEmpNo = rrJcicZ040Log.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursList.putParam("OOTranKey", rrJcicZ040Log.getTranKey());
				occursList.putParam("OORbDate", rrJcicZ040Log.getRbDate());
				occursList.putParam("OOApplyType", rrJcicZ040Log.getApplyType());
				occursList.putParam("OORefBankId", rrJcicZ040Log.getRefBankId());
				occursList.putParam("OORefBankIdX", dealBankName(rrJcicZ040Log.getRefBankId(),titaVo));
				occursList.putParam("OONotBankId1", rrJcicZ040Log.getNotBankId1());
				occursList.putParam("OONotBankId1X", dealBankName(rrJcicZ040Log.getNotBankId1(),titaVo));
				occursList.putParam("OONotBankId2", rrJcicZ040Log.getNotBankId2());
				occursList.putParam("OONotBankId2X", dealBankName(rrJcicZ040Log.getNotBankId2(),titaVo));
				occursList.putParam("OONotBankId3", rrJcicZ040Log.getNotBankId3());
				occursList.putParam("OONotBankId3X", dealBankName(rrJcicZ040Log.getNotBankId3(),titaVo));
				occursList.putParam("OONotBankId4", rrJcicZ040Log.getNotBankId4());
				occursList.putParam("OONotBankId4X", dealBankName(rrJcicZ040Log.getNotBankId4(),titaVo));
				occursList.putParam("OONotBankId5", rrJcicZ040Log.getNotBankId5());
				occursList.putParam("OONotBankId5X", dealBankName(rrJcicZ040Log.getNotBankId5(),titaVo));
				occursList.putParam("OONotBankId6", rrJcicZ040Log.getNotBankId6());
				occursList.putParam("OONotBankId6X", dealBankName(rrJcicZ040Log.getNotBankId6(),titaVo));
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
				String taU = rrJcicZ040Log.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursList.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
				occursList.putParam("OOOutJcicTxtDate", rrJcicZ040Log.getOutJcicTxtDate());
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
	


