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
import com.st1.itx.db.domain.JcicZ042;
import com.st1.itx.db.domain.JcicZ042Log;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ042LogService;
import com.st1.itx.db.service.JcicZ042Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8033")
@Scope("prototype")
public class L8033 extends TradeBuffer  {
		@Autowired
		public CdEmpService iCdEmpService;
		@Autowired
		public JcicZ042Service iJcicZ042Service;
		@Autowired
		public JcicZ042LogService iJcicZ042LogService;
		@Override
		public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
			this.info("active L8033 ");
			this.totaVo.init(titaVo);
			String iUkey = titaVo.getParam("Ukey");
			this.index = titaVo.getReturnIndex();
			this.limit = 500;

			Slice<JcicZ042Log> rJcicZ042Log = null;
			rJcicZ042Log = iJcicZ042LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
			JcicZ042 rJcicZ042 = new JcicZ042();
			rJcicZ042 = iJcicZ042Service.ukeyFirst(iUkey, titaVo);
			if (rJcicZ042 == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			if (!rJcicZ042.getTranKey().equals("A")&&rJcicZ042.getOutJcicTxtDate()==0) {
				OccursList occursListA = new OccursList();
				String iLastUpdateEmpNo = rJcicZ042.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursListA.putParam("OOTranKey", rJcicZ042.getTranKey());
				occursListA.putParam("OOIsClaims", rJcicZ042.getIsClaims());
				occursListA.putParam("OOGuarLoanCnt", rJcicZ042.getGuarLoanCnt());
				occursListA.putParam("OOExpLoanAmt", rJcicZ042.getExpLoanAmt());
				occursListA.putParam("OOCivil323ExpAmt", rJcicZ042.getCivil323ExpAmt());
				occursListA.putParam("OOReceExpAmt", rJcicZ042.getReceExpAmt());
				occursListA.putParam("OOCashCardAmt", rJcicZ042.getCashCardAmt());
				occursListA.putParam("OOCivil323CashAmt", rJcicZ042.getCivil323CashAmt());
				occursListA.putParam("OOReceCashAmt", rJcicZ042.getReceCashAmt());
				occursListA.putParam("OOCreditCardAmt", rJcicZ042.getCreditCardAmt());
				occursListA.putParam("OOCivil323CreditAmt", rJcicZ042.getCivil323CreditAmt());
				occursListA.putParam("OOReceCreditAmt", rJcicZ042.getReceCreditAmt());
				occursListA.putParam("OOReceExpPrin", rJcicZ042.getReceExpPrin());
				occursListA.putParam("OOReceExpInte", rJcicZ042.getReceExpInte());
				occursListA.putParam("OOReceExpPena", rJcicZ042.getReceExpPena());
				occursListA.putParam("OOReceExpOther", rJcicZ042.getReceExpOther());
				occursListA.putParam("OOCashCardPrin", rJcicZ042.getCashCardPrin());
				occursListA.putParam("OOCashCardInte", rJcicZ042.getCashCardInte());
				occursListA.putParam("OOCashCardPena", rJcicZ042.getCashCardPena());
				occursListA.putParam("OOCashCardOther", rJcicZ042.getCashCardOther());
				occursListA.putParam("OOCreditCardPrin", rJcicZ042.getCreditCardPrin());
				occursListA.putParam("OOCreditCardInte", rJcicZ042.getCreditCardInte());
				occursListA.putParam("OOCreditCardPena", rJcicZ042.getCreditCardPena());
				occursListA.putParam("OOCreditCardOther", rJcicZ042.getCreditCardOther());
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
				String taU = rJcicZ042.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursListA.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursListA.putParam("OOLastUpdateEmpNo",iLastUpdateEmpNo);
				occursListA.putParam("OOOutJcicTxtDate", rJcicZ042.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursListA);
			}
			if (rJcicZ042Log == null) {
				throw new LogicException(titaVo, "E0001", ""); 
			}
			for (JcicZ042Log rrJcicZ042Log:rJcicZ042Log) {
				OccursList occursList = new OccursList();
				String iLastUpdateEmpNo = rrJcicZ042Log.getLastUpdateEmpNo();
				CdEmp iCdEmp = new CdEmp();
				occursList.putParam("OOTranKey", rrJcicZ042Log.getTranKey());
				occursList.putParam("OOIsClaims", rrJcicZ042Log.getIsClaims());
				occursList.putParam("OOGuarLoanCnt", rrJcicZ042Log.getGuarLoanCnt());
				occursList.putParam("OOExpLoanAmt", rrJcicZ042Log.getExpLoanAmt());
				occursList.putParam("OOCivil323ExpAmt", rrJcicZ042Log.getCivil323ExpAmt());
				occursList.putParam("OOReceExpAmt", rrJcicZ042Log.getReceExpAmt());
				occursList.putParam("OOCashCardAmt", rrJcicZ042Log.getCashCardAmt());
				occursList.putParam("OOCivil323CashAmt", rrJcicZ042Log.getCivil323CashAmt());
				occursList.putParam("OOReceCashAmt", rrJcicZ042Log.getReceCashAmt());
				occursList.putParam("OOCreditCardAmt", rrJcicZ042Log.getCreditCardAmt());
				occursList.putParam("OOCivil323CreditAmt", rrJcicZ042Log.getCivil323CreditAmt());
				occursList.putParam("OOReceCreditAmt", rrJcicZ042Log.getReceCreditAmt());
				occursList.putParam("OOReceExpPrin", rrJcicZ042Log.getReceExpPrin());
				occursList.putParam("OOReceExpInte", rrJcicZ042Log.getReceExpInte());
				occursList.putParam("OOReceExpPena", rrJcicZ042Log.getReceExpPena());
				occursList.putParam("OOReceExpOther", rrJcicZ042Log.getReceExpOther());
				occursList.putParam("OOCashCardPrin", rrJcicZ042Log.getCashCardPrin());
				occursList.putParam("OOCashCardInte", rrJcicZ042Log.getCashCardInte());
				occursList.putParam("OOCashCardPena", rrJcicZ042Log.getCashCardPena());
				occursList.putParam("OOCashCardOther", rrJcicZ042Log.getCashCardOther());
				occursList.putParam("OOCreditCardPrin", rrJcicZ042Log.getCreditCardPrin());
				occursList.putParam("OOCreditCardInte", rrJcicZ042Log.getCreditCardInte());
				occursList.putParam("OOCreditCardPena", rrJcicZ042Log.getCreditCardPena());
				occursList.putParam("OOCreditCardOther", rrJcicZ042Log.getCreditCardOther());
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
				String taU = rrJcicZ042Log.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0,10).replace("-", ""))-19110000), 7,'0');
				uaDate = uaDate.substring(0,3)+"/"+uaDate.substring(3, 5)+"/"+uaDate.substring(5);
				String uTime = taU.substring(11,19);
				occursList.putParam("OOLastUpdate",uaDate+" "+uTime);
				occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
				occursList.putParam("OOOutJcicTxtDate", rrJcicZ042Log.getOutJcicTxtDate());
				this.totaVo.addOccursList(occursList);
			}		
			this.addList(this.totaVo);
			return this.sendList();
		}
}
	


