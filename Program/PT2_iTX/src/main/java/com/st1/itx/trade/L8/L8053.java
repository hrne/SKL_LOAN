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
import com.st1.itx.db.domain.JcicZ442;
import com.st1.itx.db.domain.JcicZ442Log;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ442LogService;
import com.st1.itx.db.service.JcicZ442Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8053")
@Scope("prototype")
public class L8053 extends TradeBuffer {
	@Autowired
	public CdEmpService iCdEmpService;
	@Autowired
	public JcicZ442Service iJcicZ442Service;
	@Autowired
	public JcicZ442LogService iJcicZ442LogService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8053 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("Ukey");

		this.index = titaVo.getReturnIndex();
		this.limit = 500;

		Slice<JcicZ442Log> rJcicZ442Log = null;
		rJcicZ442Log = iJcicZ442LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
		JcicZ442 rJcicZ442 = new JcicZ442();
		rJcicZ442 = iJcicZ442Service.ukeyFirst(iUkey, titaVo);
		if (rJcicZ442 == null) {
			throw new LogicException(titaVo, "E0001", "");
		}
		if (!rJcicZ442.getTranKey().equals("A") && rJcicZ442.getOutJcicTxtDate() == 0) {
			OccursList occursListA = new OccursList();
			String iLastUpdateEmpNo = rJcicZ442.getLastUpdateEmpNo();
			CdEmp iCdEmp = new CdEmp();
			occursListA.putParam("OOTranKey", rJcicZ442.getTranKey());
			occursListA.putParam("OOIsMaxMain", rJcicZ442.getIsMaxMain());
			occursListA.putParam("OOIsClaims", rJcicZ442.getIsClaims());
			occursListA.putParam("OOGuarLoanCnt", rJcicZ442.getGuarLoanCnt());
			occursListA.putParam("OOCivil323ExpAmt", rJcicZ442.getCivil323ExpAmt());
			occursListA.putParam("OOCivil323CashAmt", rJcicZ442.getCivil323CashAmt());
			occursListA.putParam("OOCivil323CreditAmt", rJcicZ442.getCivil323CreditAmt());
			occursListA.putParam("OOCivil323GuarAmt", rJcicZ442.getCivil323GuarAmt());
			occursListA.putParam("OOReceExpPrin", rJcicZ442.getReceExpPrin());
			occursListA.putParam("OOReceExpInte", rJcicZ442.getReceExpInte());
			occursListA.putParam("OOReceExpPena", rJcicZ442.getReceExpPena());
			occursListA.putParam("OOReceExpOther", rJcicZ442.getReceExpOther());
			occursListA.putParam("OOCashCardPrin", rJcicZ442.getCashCardPrin());
			occursListA.putParam("OOCashCardInte", rJcicZ442.getCashCardInte());
			occursListA.putParam("OOCashCardPena", rJcicZ442.getCashCardPena());
			occursListA.putParam("OOCashCardOther", rJcicZ442.getCashCardOther());
			occursListA.putParam("OOCreditCardPrin", rJcicZ442.getCreditCardPrin());
			occursListA.putParam("OOCreditCardInte", rJcicZ442.getCreditCardInte());
			occursListA.putParam("OOCreditCardPena", rJcicZ442.getCreditCardPena());
			occursListA.putParam("OOCreditCardOther", rJcicZ442.getCreditCardOther());
			occursListA.putParam("OOGuarObliPrin", rJcicZ442.getGuarObliPrin());
			occursListA.putParam("OOGuarObliInte", rJcicZ442.getGuarObliInte());
			occursListA.putParam("OOGuarObliPena", rJcicZ442.getGuarObliPena());
			occursListA.putParam("OOGuarObliOther", rJcicZ442.getGuarObliOther());
			iCdEmp = iCdEmpService.findById(iLastUpdateEmpNo, titaVo);
			if (iLastUpdateEmpNo.equals("")) {
				occursListA.putParam("OOLastUpdateEmpNoName", "");
			} else {
				if (iCdEmp == null) {
					occursListA.putParam("OOLastUpdateEmpNoName", "");
				} else {
					occursListA.putParam("OOLastUpdateEmpNoName", iCdEmp.getFullname());
				}
			}
			String taU = rJcicZ442.getLastUpdate().toString();
			String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
			String uTime = taU.substring(11, 19);
			occursListA.putParam("OOLastUpdate", uaDate + " " + uTime);
			occursListA.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
			occursListA.putParam("OOOutJcicTxtDate", rJcicZ442.getOutJcicTxtDate());
			JcicZ442Log rrJcicZ442Log = iJcicZ442LogService.ukeyFirst(rJcicZ442.getUkey(), titaVo);
			occursListA.putParam("OOTxSeq", rrJcicZ442Log.getTxSeq());
			occursListA.putParam("OOUkey", rrJcicZ442Log.getUkey());
			this.totaVo.addOccursList(occursListA);
		}
		if (rJcicZ442Log == null) {
			throw new LogicException(titaVo, "E0001", "");
		}
		for (JcicZ442Log rrJcicZ442Log : rJcicZ442Log) {
			OccursList occursList = new OccursList();
			String iLastUpdateEmpNo = rrJcicZ442Log.getLastUpdateEmpNo();
			CdEmp iCdEmp = new CdEmp();
			occursList.putParam("OOTranKey", rrJcicZ442Log.getTranKey());
			occursList.putParam("OOIsMaxMain", rrJcicZ442Log.getIsMaxMain());
			occursList.putParam("OOIsClaims", rrJcicZ442Log.getIsClaims());
			occursList.putParam("OOGuarLoanCnt", rrJcicZ442Log.getGuarLoanCnt());
			occursList.putParam("OOCivil323ExpAmt", rrJcicZ442Log.getCivil323ExpAmt());
			occursList.putParam("OOCivil323CashAmt", rrJcicZ442Log.getCivil323CashAmt());
			occursList.putParam("OOCivil323CreditAmt", rrJcicZ442Log.getCivil323CreditAmt());
			occursList.putParam("OOCivil323GuarAmt", rrJcicZ442Log.getCivil323GuarAmt());
			occursList.putParam("OOReceExpPrin", rrJcicZ442Log.getReceExpPrin());
			occursList.putParam("OOReceExpInte", rrJcicZ442Log.getReceExpInte());
			occursList.putParam("OOReceExpPena", rrJcicZ442Log.getReceExpPena());
			occursList.putParam("OOReceExpOther", rrJcicZ442Log.getReceExpOther());
			occursList.putParam("OOCashCardPrin", rrJcicZ442Log.getCashCardPrin());
			occursList.putParam("OOCashCardInte", rrJcicZ442Log.getCashCardInte());
			occursList.putParam("OOCashCardPena", rrJcicZ442Log.getCashCardPena());
			occursList.putParam("OOCashCardOther", rrJcicZ442Log.getCashCardOther());
			occursList.putParam("OOCreditCardPrin", rrJcicZ442Log.getCreditCardPrin());
			occursList.putParam("OOCreditCardInte", rrJcicZ442Log.getCreditCardInte());
			occursList.putParam("OOCreditCardPena", rrJcicZ442Log.getCreditCardPena());
			occursList.putParam("OOCreditCardOther", rrJcicZ442Log.getCreditCardOther());
			occursList.putParam("OOGuarObliPrin", rrJcicZ442Log.getGuarObliPrin());
			occursList.putParam("OOGuarObliInte", rrJcicZ442Log.getGuarObliInte());
			occursList.putParam("OOGuarObliPena", rrJcicZ442Log.getGuarObliPena());
			occursList.putParam("OOGuarObliOther", rrJcicZ442Log.getGuarObliOther());
			iCdEmp = iCdEmpService.findById(iLastUpdateEmpNo, titaVo);
			if (iLastUpdateEmpNo.equals("")) {
				occursList.putParam("OOLastUpdateEmpNoName", "");
			} else {
				if (iCdEmp == null) {
					occursList.putParam("OOLastUpdateEmpNoName", "");
				} else {
					occursList.putParam("OOLastUpdateEmpNoName", iCdEmp.getFullname());
				}
			}
			String taU = rrJcicZ442Log.getLastUpdate().toString();
			String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
			String uTime = taU.substring(11, 19);
			occursList.putParam("OOLastUpdate", uaDate + " " + uTime);
			occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
			occursList.putParam("OOOutJcicTxtDate", rrJcicZ442Log.getOutJcicTxtDate());
			occursList.putParam("OOTxSeq", rrJcicZ442Log.getTxSeq());
			occursList.putParam("OOUkey", rrJcicZ442Log.getUkey());
			this.totaVo.addOccursList(occursList);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}
