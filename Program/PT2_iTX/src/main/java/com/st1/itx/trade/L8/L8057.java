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
import com.st1.itx.db.domain.JcicZ447;
import com.st1.itx.db.domain.JcicZ447Log;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ447LogService;
import com.st1.itx.db.service.JcicZ447Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8057")
@Scope("prototype")
public class L8057 extends TradeBuffer {
	@Autowired
	public CdEmpService iCdEmpService;
	@Autowired
	public JcicZ447Service iJcicZ447Service;
	@Autowired
	public JcicZ447LogService iJcicZ447LogService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8057 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("Ukey");

		this.index = titaVo.getReturnIndex();
		this.limit = 500;

		Slice<JcicZ447Log> rJcicZ447Log = null;
		rJcicZ447Log = iJcicZ447LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
		JcicZ447 rJcicZ447 = new JcicZ447();
		rJcicZ447 = iJcicZ447Service.ukeyFirst(iUkey, titaVo);
		if (rJcicZ447 == null) {
			throw new LogicException(titaVo, "E0001", "");
		}
		if (!rJcicZ447.getTranKey().equals("A") && rJcicZ447.getOutJcicTxtDate() == 0) {
			OccursList occursListA = new OccursList();
			String iLastUpdateEmpNo = rJcicZ447.getLastUpdateEmpNo();
			CdEmp iCdEmp = new CdEmp();
			occursListA.putParam("OOTranKey", rJcicZ447.getTranKey());
			occursListA.putParam("OOCivil323Amt", rJcicZ447.getCivil323Amt());
			occursListA.putParam("OOTotalAmt", rJcicZ447.getTotalAmt());
			occursListA.putParam("OOSignDate", rJcicZ447.getSignDate());
			occursListA.putParam("OOFirstPayDate", rJcicZ447.getFirstPayDate());
			occursListA.putParam("OOPeriod", rJcicZ447.getPeriod());
			occursListA.putParam("OORate", rJcicZ447.getRate());
			occursListA.putParam("OOMonthPayAmt", rJcicZ447.getMonthPayAmt());
			occursListA.putParam("OOPayAccount", rJcicZ447.getPayAccount());
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
			String taU = rJcicZ447.getLastUpdate().toString();
			String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
			String uTime = taU.substring(11, 19);
			occursListA.putParam("OOLastUpdate", uaDate + " " + uTime);
			occursListA.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
			occursListA.putParam("OOOutJcicTxtDate", rJcicZ447.getOutJcicTxtDate());
			JcicZ447Log rrJcicZ447Log = iJcicZ447LogService.ukeyFirst(rJcicZ447.getUkey(), titaVo);
			occursListA.putParam("OOTxSeq", rrJcicZ447Log.getTxSeq());
			occursListA.putParam("OOUkey", rrJcicZ447Log.getUkey());
			this.totaVo.addOccursList(occursListA);
		}
		if (rJcicZ447Log == null) {
			throw new LogicException(titaVo, "E0001", "");
		}
		for (JcicZ447Log rrJcicZ447Log : rJcicZ447Log) {
			OccursList occursList = new OccursList();
			String iLastUpdateEmpNo = rrJcicZ447Log.getLastUpdateEmpNo();
			CdEmp iCdEmp = new CdEmp();
			occursList.putParam("OOTranKey", rrJcicZ447Log.getTranKey());
			occursList.putParam("OOCivil323Amt", rrJcicZ447Log.getCivil323Amt());
			occursList.putParam("OOTotalAmt", rrJcicZ447Log.getTotalAmt());
			occursList.putParam("OOSignDate", rrJcicZ447Log.getSignDate());
			occursList.putParam("OOFirstPayDate", rrJcicZ447Log.getFirstPayDate());
			occursList.putParam("OOPeriod", rrJcicZ447Log.getPeriod());//
			occursList.putParam("OORate", rrJcicZ447Log.getRate());//
			occursList.putParam("OOMonthPayAmt", rrJcicZ447Log.getMonthPayAmt());//
			occursList.putParam("OOPayAccount", rrJcicZ447Log.getPayAccount());//
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
			String taU = rrJcicZ447Log.getLastUpdate().toString();
			String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
			String uTime = taU.substring(11, 19);
			occursList.putParam("OOLastUpdate", uaDate + " " + uTime);
			occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
			occursList.putParam("OOOutJcicTxtDate", rrJcicZ447Log.getOutJcicTxtDate());
			occursList.putParam("OOTxSeq", rrJcicZ447Log.getTxSeq());
			occursList.putParam("OOUkey", rrJcicZ447Log.getUkey());
			this.totaVo.addOccursList(occursList);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}
