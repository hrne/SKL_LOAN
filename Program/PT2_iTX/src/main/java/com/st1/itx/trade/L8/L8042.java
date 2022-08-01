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
import com.st1.itx.db.domain.JcicZ051;
import com.st1.itx.db.domain.JcicZ051Log;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ051LogService;
import com.st1.itx.db.service.JcicZ051Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8042")
@Scope("prototype")
public class L8042 extends TradeBuffer {
	@Autowired
	public CdEmpService iCdEmpService;
	@Autowired
	public JcicZ051Service iJcicZ051Service;
	@Autowired
	public JcicZ051LogService iJcicZ051LogService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8042 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("Ukey");

		this.index = titaVo.getReturnIndex();
		this.limit = 500;

		Slice<JcicZ051Log> rJcicZ051Log = null;
		rJcicZ051Log = iJcicZ051LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
		JcicZ051 rJcicZ051 = new JcicZ051();
		rJcicZ051 = iJcicZ051Service.ukeyFirst(iUkey, titaVo);
		if (rJcicZ051 == null) {
			throw new LogicException(titaVo, "E0001", "");
		}
		if (!rJcicZ051.getTranKey().equals("A") && rJcicZ051.getOutJcicTxtDate() == 0) {
			OccursList occursListA = new OccursList();
			String iLastUpdateEmpNo = rJcicZ051.getLastUpdateEmpNo();
			CdEmp iCdEmp = new CdEmp();
			occursListA.putParam("OOTranKey", rJcicZ051.getTranKey());
			occursListA.putParam("OODelayCode", rJcicZ051.getDelayCode());
			occursListA.putParam("OODelayDesc", rJcicZ051.getDelayDesc());
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
			String taU = rJcicZ051.getLastUpdate().toString();
			String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
			String uTime = taU.substring(11, 19);
			occursListA.putParam("OOLastUpdate", uaDate + " " + uTime);
			occursListA.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
			occursListA.putParam("OOOutJcicTxtDate", rJcicZ051.getOutJcicTxtDate());
			JcicZ051Log rrJcicZ051Log = iJcicZ051LogService.ukeyFirst(rJcicZ051.getUkey(), titaVo);
			occursListA.putParam("OOTxSeq", rrJcicZ051Log.getTxSeq());
			occursListA.putParam("OOUkey", rrJcicZ051Log.getUkey());
			this.totaVo.addOccursList(occursListA);
		}
		if (rJcicZ051Log == null) {
			throw new LogicException(titaVo, "E0001", "");
		}
		for (JcicZ051Log rrJcicZ051Log : rJcicZ051Log) {
			OccursList occursList = new OccursList();
			String iLastUpdateEmpNo = rrJcicZ051Log.getLastUpdateEmpNo();
			CdEmp iCdEmp = new CdEmp();
			occursList.putParam("OOTranKey", rrJcicZ051Log.getTranKey());
			occursList.putParam("OODelayCode", rrJcicZ051Log.getDelayCode());
			occursList.putParam("OODelayDesc", rrJcicZ051Log.getDelayDesc());
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
			String taU = rrJcicZ051Log.getLastUpdate().toString();
			String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
			String uTime = taU.substring(11, 19);
			occursList.putParam("OOLastUpdate", uaDate + " " + uTime);
			occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
			occursList.putParam("OOOutJcicTxtDate", rrJcicZ051Log.getOutJcicTxtDate());
			occursList.putParam("OOTxSeq", rrJcicZ051Log.getTxSeq());
			occursList.putParam("OOUkey", rrJcicZ051Log.getUkey());
			this.totaVo.addOccursList(occursList);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}
