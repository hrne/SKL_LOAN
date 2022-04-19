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
import com.st1.itx.db.domain.JcicZ575;
import com.st1.itx.db.domain.JcicZ575Log;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ575LogService;
import com.st1.itx.db.service.JcicZ575Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8067")
@Scope("prototype")
public class L8067 extends TradeBuffer {
	@Autowired
	public CdEmpService iCdEmpService;
	@Autowired
	public JcicZ575Service iJcicZ575Service;
	@Autowired
	public JcicZ575LogService iJcicZ575LogService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8067 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("Ukey");

		this.index = titaVo.getReturnIndex();
		this.limit = 500;

		Slice<JcicZ575Log> rJcicZ575Log = null;
		rJcicZ575Log = iJcicZ575LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
		JcicZ575 rJcicZ575 = new JcicZ575();
		rJcicZ575 = iJcicZ575Service.ukeyFirst(iUkey, titaVo);
		if (rJcicZ575 == null) {
			throw new LogicException(titaVo, "E0001", "");
		}
		if (!rJcicZ575.getTranKey().equals("A") && rJcicZ575.getOutJcicTxtDate() == 0) {
			OccursList occursListA = new OccursList();
			String iLastUpdateEmpNo = rJcicZ575.getLastUpdateEmpNo();
			CdEmp iCdEmp = new CdEmp();
			occursListA.putParam("OOTranKey", rJcicZ575.getTranKey());
			occursListA.putParam("OOModifyType", rJcicZ575.getModifyType());
			occursListA.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
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
			String taU = rJcicZ575.getLastUpdate().toString();
			String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
			String uTime = taU.substring(11, 19);
			occursListA.putParam("OOLastUpdate", uaDate + " " + uTime);
			occursListA.putParam("OOOutJcicTxtDate", rJcicZ575.getOutJcicTxtDate());
			this.totaVo.addOccursList(occursListA);
		}
		if (rJcicZ575Log == null) {
			throw new LogicException(titaVo, "E0001", "");
		}
		for (JcicZ575Log rrJcicZ575Log : rJcicZ575Log) {
			OccursList occursList = new OccursList();
			String iLastUpdateEmpNo = rrJcicZ575Log.getLastUpdateEmpNo();
			CdEmp iCdEmp = new CdEmp();
			occursList.putParam("OOTranKey", rrJcicZ575Log.getTranKey());
			occursList.putParam("OOModifyType", rrJcicZ575Log.getModifyType());
			occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
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
			String taU = rrJcicZ575Log.getLastUpdate().toString();
			String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
			String uTime = taU.substring(11, 19);
			occursList.putParam("OOLastUpdate", uaDate + " " + uTime);
			occursList.putParam("OOOutJcicTxtDate", rrJcicZ575Log.getOutJcicTxtDate());
			this.totaVo.addOccursList(occursList);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}
