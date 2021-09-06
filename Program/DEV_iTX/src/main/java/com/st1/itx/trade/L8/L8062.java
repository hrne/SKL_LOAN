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
import com.st1.itx.db.domain.JcicZ570;
import com.st1.itx.db.domain.JcicZ570Log;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ570LogService;
import com.st1.itx.db.service.JcicZ570Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8062")
@Scope("prototype")
public class L8062 extends TradeBuffer  {
		@Autowired
		public CdEmpService iCdEmpService;
		@Autowired
		public JcicZ570Service iJcicZ570Service;
		@Autowired
		public JcicZ570LogService iJcicZ570LogService;
		@Autowired
		public CdCodeService iCdCodeService;
		@Override
		public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
			this.info("active L8062 ");
			this.totaVo.init(titaVo);
			String iUkey = titaVo.getParam("Ukey");
			
			this.index = titaVo.getReturnIndex();
			this.limit = 500;

			Slice<JcicZ570Log> rJcicZ570Log = null;
			rJcicZ570Log = iJcicZ570LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
			JcicZ570 rJcicZ570 = new JcicZ570();
			rJcicZ570 = iJcicZ570Service.ukeyFirst(iUkey, titaVo);
			if (rJcicZ570 == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			if (!rJcicZ570.getTranKey().equals("A")&&rJcicZ570.getOutJcicTxtDate()==0) {
				OccursList occursListA = new OccursList();
				String iLastUpdateEmpNo = rJcicZ570.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				 occursListA.putParam("OOTranKey", rJcicZ570.getTranKey());
				    occursListA.putParam("OOAdjudicateDate", rJcicZ570.getAdjudicateDate());
				    occursListA.putParam("OOBankCount", rJcicZ570.getBankCount());
				    occursListA.putParam("OOBank1", rJcicZ570.getBank1());
				    occursListA.putParam("OOBank2", rJcicZ570.getBank2());
				    occursListA.putParam("OOBank3", rJcicZ570.getBank3());
				    occursListA.putParam("OOBank4", rJcicZ570.getBank4());
				    occursListA.putParam("OOBank5", rJcicZ570.getBank5());
				    occursListA.putParam("OOBank6", rJcicZ570.getBank6());
				    occursListA.putParam("OOBank7", rJcicZ570.getBank7());
				    occursListA.putParam("OOBank8", rJcicZ570.getBank8());
				    occursListA.putParam("OOBank9", rJcicZ570.getBank9());
				    occursListA.putParam("OOBank10", rJcicZ570.getBank10());
				    occursListA.putParam("OOBank11", rJcicZ570.getBank11());
				    occursListA.putParam("OOBank12", rJcicZ570.getBank12());
				    occursListA.putParam("OOBank13", rJcicZ570.getBank13());
				    occursListA.putParam("OOBank14", rJcicZ570.getBank14());
				    occursListA.putParam("OOBank15", rJcicZ570.getBank15());
				    occursListA.putParam("OOBank16", rJcicZ570.getBank16());
				    occursListA.putParam("OOBank17", rJcicZ570.getBank17());
				    occursListA.putParam("OOBank18", rJcicZ570.getBank18());
				    occursListA.putParam("OOBank19", rJcicZ570.getBank19());
				    occursListA.putParam("OOBank20", rJcicZ570.getBank20());
				    occursListA.putParam("OOBank21", rJcicZ570.getBank21());
				    occursListA.putParam("OOBank22", rJcicZ570.getBank22());
				    occursListA.putParam("OOBank23", rJcicZ570.getBank23());
				    occursListA.putParam("OOBank24", rJcicZ570.getBank24());
				    occursListA.putParam("OOBank25", rJcicZ570.getBank25());
				    occursListA.putParam("OOBank26", rJcicZ570.getBank26());
				    occursListA.putParam("OOBank27", rJcicZ570.getBank27());
				    occursListA.putParam("OOBank28", rJcicZ570.getBank28());
				    occursListA.putParam("OOBank29", rJcicZ570.getBank29());
				    occursListA.putParam("OOBank30", rJcicZ570.getBank30());
				    
	                occursListA.putParam("OOBank1X", dealBankName(rJcicZ570.getBank1(),titaVo));
					occursListA.putParam("OOBank2X", dealBankName(rJcicZ570.getBank2(),titaVo));
					occursListA.putParam("OOBank3X", dealBankName(rJcicZ570.getBank3(),titaVo));
					occursListA.putParam("OOBank4X", dealBankName(rJcicZ570.getBank4(),titaVo));
					occursListA.putParam("OOBank5X", dealBankName(rJcicZ570.getBank5(),titaVo));
					occursListA.putParam("OOBank6X", dealBankName(rJcicZ570.getBank6(),titaVo));
					occursListA.putParam("OOBank7X", dealBankName(rJcicZ570.getBank7(),titaVo));
					occursListA.putParam("OOBank8X", dealBankName(rJcicZ570.getBank8(),titaVo));
					occursListA.putParam("OOBank9X", dealBankName(rJcicZ570.getBank9(),titaVo));
					occursListA.putParam("OOBank10X", dealBankName(rJcicZ570.getBank10(),titaVo));
					occursListA.putParam("OOBank11X", dealBankName(rJcicZ570.getBank11(),titaVo));
					occursListA.putParam("OOBank12X", dealBankName(rJcicZ570.getBank12(),titaVo));
					occursListA.putParam("OOBank13X", dealBankName(rJcicZ570.getBank13(),titaVo));
					occursListA.putParam("OOBank14X", dealBankName(rJcicZ570.getBank14(),titaVo));
					occursListA.putParam("OOBank15X", dealBankName(rJcicZ570.getBank15(),titaVo));
					occursListA.putParam("OOBank16X", dealBankName(rJcicZ570.getBank16(),titaVo));
					occursListA.putParam("OOBank17X", dealBankName(rJcicZ570.getBank17(),titaVo));
					occursListA.putParam("OOBank18X", dealBankName(rJcicZ570.getBank18(),titaVo));
					occursListA.putParam("OOBank19X", dealBankName(rJcicZ570.getBank19(),titaVo));
					occursListA.putParam("OOBank20X", dealBankName(rJcicZ570.getBank20(),titaVo));
					occursListA.putParam("OOBank21X", dealBankName(rJcicZ570.getBank21(),titaVo));
					occursListA.putParam("OOBank22X", dealBankName(rJcicZ570.getBank22(),titaVo));
					occursListA.putParam("OOBank23X", dealBankName(rJcicZ570.getBank23(),titaVo));
					occursListA.putParam("OOBank24X", dealBankName(rJcicZ570.getBank24(),titaVo));
					occursListA.putParam("OOBank25X", dealBankName(rJcicZ570.getBank25(),titaVo));
					occursListA.putParam("OOBank26X", dealBankName(rJcicZ570.getBank26(),titaVo));
					occursListA.putParam("OOBank27X", dealBankName(rJcicZ570.getBank27(),titaVo));
					occursListA.putParam("OOBank28X", dealBankName(rJcicZ570.getBank28(),titaVo));
					occursListA.putParam("OOBank29X", dealBankName(rJcicZ570.getBank29(),titaVo));
					occursListA.putParam("OOBank30X", dealBankName(rJcicZ570.getBank30(),titaVo));
	                
				    iCdEmp = iCdEmpService.findAgentIdFirst(iLastUpdateEmpNo, titaVo);
				if(iLastUpdateEmpNo.equals("")) {
					occursListA.putParam("OOLastUpdateEmpNoName", "");
				}else {
					if(iCdEmp == null) {
						occursListA.putParam("OOLastUpdateEmpNoName", "");
					}else {
						occursListA.putParam("OOLastUpdateEmpNoName", iCdEmp.getFullname());
					}
				}
                String taU = rJcicZ570.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursListA.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursListA.putParam("OOLastUpdateEmpNo",iLastUpdateEmpNo);
				occursListA.putParam("OOOutJcicTxtDate", rJcicZ570.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursListA);
			}
			if (rJcicZ570Log == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			for (JcicZ570Log rrJcicZ570Log:rJcicZ570Log) {
				OccursList occursList = new OccursList();
				String iLastUpdateEmpNo = rrJcicZ570Log.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
			    occursList.putParam("OOTranKey", rrJcicZ570Log.getTranKey());
			    occursList.putParam("OOAdjudicateDate", rrJcicZ570Log.getAdjudicateDate());
			    occursList.putParam("OOBankCount", rrJcicZ570Log.getBankCount());
			    occursList.putParam("OOBank1", rrJcicZ570Log.getBank1());
			    occursList.putParam("OOBank2", rrJcicZ570Log.getBank2());
			    occursList.putParam("OOBank3", rrJcicZ570Log.getBank3());
			    occursList.putParam("OOBank4", rrJcicZ570Log.getBank4());
			    occursList.putParam("OOBank5", rrJcicZ570Log.getBank5());
			    occursList.putParam("OOBank6", rrJcicZ570Log.getBank6());
			    occursList.putParam("OOBank7", rrJcicZ570Log.getBank7());
			    occursList.putParam("OOBank8", rrJcicZ570Log.getBank8());
			    occursList.putParam("OOBank9", rrJcicZ570Log.getBank9());
			    occursList.putParam("OOBank10", rrJcicZ570Log.getBank10());
			    occursList.putParam("OOBank11", rrJcicZ570Log.getBank11());
			    occursList.putParam("OOBank12", rrJcicZ570Log.getBank12());
			    occursList.putParam("OOBank13", rrJcicZ570Log.getBank13());
			    occursList.putParam("OOBank14", rrJcicZ570Log.getBank14());
			    occursList.putParam("OOBank15", rrJcicZ570Log.getBank15());
			    occursList.putParam("OOBank16", rrJcicZ570Log.getBank16());
			    occursList.putParam("OOBank17", rrJcicZ570Log.getBank17());
			    occursList.putParam("OOBank18", rrJcicZ570Log.getBank18());
			    occursList.putParam("OOBank19", rrJcicZ570Log.getBank19());
			    occursList.putParam("OOBank20", rrJcicZ570Log.getBank20());
			    occursList.putParam("OOBank21", rrJcicZ570Log.getBank21());
			    occursList.putParam("OOBank22", rrJcicZ570Log.getBank22());
			    occursList.putParam("OOBank23", rrJcicZ570Log.getBank23());
			    occursList.putParam("OOBank24", rrJcicZ570Log.getBank24());
			    occursList.putParam("OOBank25", rrJcicZ570Log.getBank25());
			    occursList.putParam("OOBank26", rrJcicZ570Log.getBank26());
			    occursList.putParam("OOBank27", rrJcicZ570Log.getBank27());
			    occursList.putParam("OOBank28", rrJcicZ570Log.getBank28());
			    occursList.putParam("OOBank29", rrJcicZ570Log.getBank29());
			    occursList.putParam("OOBank30", rrJcicZ570Log.getBank30());
				
                occursList.putParam("OOBank1X", dealBankName(rrJcicZ570Log.getBank1(),titaVo));
				occursList.putParam("OOBank2X", dealBankName(rrJcicZ570Log.getBank2(),titaVo));
				occursList.putParam("OOBank3X", dealBankName(rrJcicZ570Log.getBank3(),titaVo));
				occursList.putParam("OOBank4X", dealBankName(rrJcicZ570Log.getBank4(),titaVo));
				occursList.putParam("OOBank5X", dealBankName(rrJcicZ570Log.getBank5(),titaVo));
				occursList.putParam("OOBank6X", dealBankName(rrJcicZ570Log.getBank6(),titaVo));
				occursList.putParam("OOBank7X", dealBankName(rrJcicZ570Log.getBank7(),titaVo));
				occursList.putParam("OOBank8X", dealBankName(rrJcicZ570Log.getBank8(),titaVo));
				occursList.putParam("OOBank9X", dealBankName(rrJcicZ570Log.getBank9(),titaVo));
				occursList.putParam("OOBank10X", dealBankName(rrJcicZ570Log.getBank10(),titaVo));
				occursList.putParam("OOBank11X", dealBankName(rrJcicZ570Log.getBank11(),titaVo));
				occursList.putParam("OOBank12X", dealBankName(rrJcicZ570Log.getBank12(),titaVo));
				occursList.putParam("OOBank13X", dealBankName(rrJcicZ570Log.getBank13(),titaVo));
				occursList.putParam("OOBank14X", dealBankName(rrJcicZ570Log.getBank14(),titaVo));
				occursList.putParam("OOBank15X", dealBankName(rrJcicZ570Log.getBank15(),titaVo));
				occursList.putParam("OOBank16X", dealBankName(rrJcicZ570Log.getBank16(),titaVo));
				occursList.putParam("OOBank17X", dealBankName(rrJcicZ570Log.getBank17(),titaVo));
				occursList.putParam("OOBank18X", dealBankName(rrJcicZ570Log.getBank18(),titaVo));
				occursList.putParam("OOBank19X", dealBankName(rrJcicZ570Log.getBank19(),titaVo));
				occursList.putParam("OOBank20X", dealBankName(rrJcicZ570Log.getBank20(),titaVo));
				occursList.putParam("OOBank21X", dealBankName(rrJcicZ570Log.getBank21(),titaVo));
				occursList.putParam("OOBank22X", dealBankName(rrJcicZ570Log.getBank22(),titaVo));
				occursList.putParam("OOBank23X", dealBankName(rrJcicZ570Log.getBank23(),titaVo));
				occursList.putParam("OOBank24X", dealBankName(rrJcicZ570Log.getBank24(),titaVo));
				occursList.putParam("OOBank25X", dealBankName(rrJcicZ570Log.getBank25(),titaVo));
				occursList.putParam("OOBank26X", dealBankName(rrJcicZ570Log.getBank26(),titaVo));
				occursList.putParam("OOBank27X", dealBankName(rrJcicZ570Log.getBank27(),titaVo));
				occursList.putParam("OOBank28X", dealBankName(rrJcicZ570Log.getBank28(),titaVo));
				occursList.putParam("OOBank29X", dealBankName(rrJcicZ570Log.getBank29(),titaVo));
				occursList.putParam("OOBank30X", dealBankName(rrJcicZ570Log.getBank30(),titaVo));
			    
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
				String taU = rrJcicZ570Log.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursList.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
				occursList.putParam("OOOutJcicTxtDate", rrJcicZ570Log.getOutJcicTxtDate());
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
	


