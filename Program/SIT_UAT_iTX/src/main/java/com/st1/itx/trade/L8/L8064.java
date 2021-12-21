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
import com.st1.itx.db.domain.JcicZ572;
import com.st1.itx.db.domain.JcicZ572Log;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ572LogService;
import com.st1.itx.db.service.JcicZ572Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8064")
@Scope("prototype")
public class L8064 extends TradeBuffer {
	@Autowired
	public CdEmpService iCdEmpService;
	@Autowired
	public JcicZ572Service iJcicZ572Service;
	@Autowired
	public JcicZ572LogService iJcicZ572LogService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8064 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("Ukey");

		this.index = titaVo.getReturnIndex();
		this.limit = 500;

		Slice<JcicZ572Log> rJcicZ572Log = null;
		rJcicZ572Log = iJcicZ572LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
		JcicZ572 rJcicZ572 = new JcicZ572();
		rJcicZ572 = iJcicZ572Service.ukeyFirst(iUkey, titaVo);
		if (rJcicZ572 == null) {
			throw new LogicException(titaVo, "E0001", "");
		}
		if (!rJcicZ572.getTranKey().equals("A") && rJcicZ572.getOutJcicTxtDate() == 0) {
			OccursList occursListA = new OccursList();
			String iLastUpdateEmpNo = rJcicZ572.getLastUpdateEmpNo();
			CdEmp iCdEmp = new CdEmp();
			occursListA.putParam("OOTranKey", rJcicZ572.getTranKey());
			occursListA.putParam("OOStartDate", rJcicZ572.getStartDate());
			occursListA.putParam("OOAllotAmt", rJcicZ572.getAllotAmt());
			occursListA.putParam("OOOwnPercentage", rJcicZ572.getOwnPercentage());
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
			String taU = rJcicZ572.getLastUpdate().toString();
			String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
			String uTime = taU.substring(11, 19);
			occursListA.putParam("OOLastUpdate", uaDate + " " + uTime);
			occursListA.putParam("OOLastUpdateEmpNo", rJcicZ572.getLastUpdateEmpNo());
			occursListA.putParam("OOOutJcicTxtDate", rJcicZ572.getOutJcicTxtDate());
			this.totaVo.addOccursList(occursListA);
		}
		if (rJcicZ572Log == null) {
			throw new LogicException(titaVo, "E0001", "");
		}
		for (JcicZ572Log rrJcicZ572Log : rJcicZ572Log) {
			OccursList occursList = new OccursList();
			String iLastUpdateEmpNo = rrJcicZ572Log.getLastUpdateEmpNo();
			CdEmp iCdEmp = new CdEmp();
			occursList.putParam("OOTranKey", rrJcicZ572Log.getTranKey());
			occursList.putParam("OOStartDate", rrJcicZ572Log.getStartDate());
			occursList.putParam("OOAllotAmt", rrJcicZ572Log.getAllotAmt());
			occursList.putParam("OOOwnPercentage", rrJcicZ572Log.getOwnPercentage());
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
			String taU = rrJcicZ572Log.getLastUpdate().toString();
			String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
			String uTime = taU.substring(11, 19);
			occursList.putParam("OOLastUpdate", uaDate + " " + uTime);
			occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
			occursList.putParam("OOOutJcicTxtDate", rrJcicZ572Log.getOutJcicTxtDate());
			this.totaVo.addOccursList(occursList);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}
