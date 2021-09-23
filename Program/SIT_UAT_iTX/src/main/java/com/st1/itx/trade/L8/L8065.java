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
import com.st1.itx.db.domain.JcicZ573;
import com.st1.itx.db.domain.JcicZ573Log;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ573LogService;
import com.st1.itx.db.service.JcicZ573Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8065")
@Scope("prototype")
public class L8065 extends TradeBuffer  {
		@Autowired
		public CdEmpService iCdEmpService;
		@Autowired
		public JcicZ573Service iJcicZ573Service;
		@Autowired
		public JcicZ573LogService iJcicZ573LogService;
		@Override
		public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
			this.info("active L8065 ");
			this.totaVo.init(titaVo);
			String iUkey = titaVo.getParam("Ukey");
			
			this.index = titaVo.getReturnIndex();
			this.limit = 500;

			Slice<JcicZ573Log> rJcicZ573Log = null;
			rJcicZ573Log = iJcicZ573LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
			JcicZ573 rJcicZ573 = new JcicZ573();
			rJcicZ573 = iJcicZ573Service.ukeyFirst(iUkey, titaVo);
			if (rJcicZ573 == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			if (!rJcicZ573.getTranKey().equals("A")&&rJcicZ573.getOutJcicTxtDate()==0) {
				OccursList occursListA = new OccursList();
				String iLastUpdateEmpNo = rJcicZ573.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursListA.putParam("OOTranKey", rJcicZ573.getTranKey());
				occursListA.putParam("OOPayAmt", rJcicZ573.getPayAmt());
				occursListA.putParam("OOTotalPayAmt", rJcicZ573.getTotalPayAmt());
				iCdEmp = iCdEmpService.findById(iLastUpdateEmpNo, titaVo);
				if(iLastUpdateEmpNo.equals("")) {
					occursListA.putParam("OOLastUpdateEmpNoName", "");
				}else {
					if(iCdEmp == null) {
						occursListA.putParam("OOLastUpdateEmpNoName", "");
					}else {
						occursListA.putParam("OOLastUpdateEmpNoName",iCdEmp.getFullname());
					}
				}
				String taU = rJcicZ573.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursListA.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursListA.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
				occursListA.putParam("OOOutJcicTxtDate", rJcicZ573.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursListA);
			}
			if (rJcicZ573Log == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			for (JcicZ573Log rrJcicZ573Log:rJcicZ573Log) {
				OccursList occursList = new OccursList();
				String iLastUpdateEmpNo = rrJcicZ573Log.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursList.putParam("OOTranKey", rrJcicZ573Log.getTranKey());			
				occursList.putParam("OOPayAmt", rrJcicZ573Log.getPayAmt());
				occursList.putParam("OOTotalPayAmt", rrJcicZ573Log.getTotalPayAmt());
				iCdEmp = iCdEmpService.findById(iLastUpdateEmpNo, titaVo);
				if(iLastUpdateEmpNo.equals("")) {
					occursList.putParam("OOLastUpdateEmpNoName", "");
				}else {
					if(iCdEmp == null) {
						occursList.putParam("OOLastUpdateEmpNoName", "");
					}else {
						occursList.putParam("OOLastUpdateEmpNoName",iCdEmp.getFullname());
					}
				}
				String taU = rrJcicZ573Log.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursList.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
				occursList.putParam("OOOutJcicTxtDate", rrJcicZ573Log.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursList);
			}		
			this.addList(this.totaVo);
			return this.sendList();
		}
}
	


