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
import com.st1.itx.db.domain.JcicZ046;
import com.st1.itx.db.domain.JcicZ046Log;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ046LogService;
import com.st1.itx.db.service.JcicZ046Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8037")
@Scope("prototype")
public class L8037 extends TradeBuffer  {
		@Autowired
		public CdEmpService iCdEmpService;
		@Autowired
		public JcicZ046Service iJcicZ046Service;
		@Autowired
		public JcicZ046LogService iJcicZ046LogService;
		@Override
		public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
			this.info("active L8037 ");
			this.totaVo.init(titaVo);
			String iUkey = titaVo.getParam("Ukey");
			
			this.index = titaVo.getReturnIndex();
			this.limit = 500;

			Slice<JcicZ046Log> rJcicZ046Log = null;
			rJcicZ046Log = iJcicZ046LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
			JcicZ046 rJcicZ046 = new JcicZ046();
			rJcicZ046 = iJcicZ046Service.ukeyFirst(iUkey, titaVo);
			if (rJcicZ046 == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			if (!rJcicZ046.getTranKey().equals("A")&&rJcicZ046.getOutJcicTxtDate()==0) {
				OccursList occursListA = new OccursList();
				String iLastUpdateEmpNo = rJcicZ046.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursListA.putParam("OOTranKey", rJcicZ046.getTranKey());
				occursListA.putParam("OOCloseCode", rJcicZ046.getCloseCode());
				occursListA.putParam("OOBreakCode", rJcicZ046.getBreakCode());
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
				String taU = rJcicZ046.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursListA.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursListA.putParam("OOLastUpdateEmpNo",iLastUpdateEmpNo);
				occursListA.putParam("OOOutJcicTxtDate", rJcicZ046.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursListA);
			}
			if (rJcicZ046Log == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			for (JcicZ046Log rrJcicZ046Log:rJcicZ046Log) {
				OccursList occursList = new OccursList();
				String iLastUpdateEmpNo = rrJcicZ046Log.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursList.putParam("OOTranKey", rrJcicZ046Log.getTranKey());
				occursList.putParam("OOCloseCode", rrJcicZ046Log.getCloseCode());
				occursList.putParam("OOBreakCode", rrJcicZ046Log.getBreakCode());
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
				String taU = rrJcicZ046Log.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursList.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
				occursList.putParam("OOOutJcicTxtDate", rrJcicZ046Log.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursList);
			}		
			this.addList(this.totaVo);
			return this.sendList();
		}
}
	


