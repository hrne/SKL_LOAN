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
import com.st1.itx.db.domain.JcicZ454;
import com.st1.itx.db.domain.JcicZ454Log;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ454LogService;
import com.st1.itx.db.service.JcicZ454Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8061")
@Scope("prototype")
public class L8061 extends TradeBuffer {
	@Autowired
	public CdEmpService iCdEmpService;
	@Autowired
	public JcicZ454Service iJcicZ454Service;
	@Autowired
	public JcicZ454LogService iJcicZ454LogService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8061 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("Ukey");

		this.index = titaVo.getReturnIndex();
		this.limit = 500;

		Slice<JcicZ454Log> rJcicZ454Log = null;
		rJcicZ454Log = iJcicZ454LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
		JcicZ454 rJcicZ454 = new JcicZ454();
		rJcicZ454 = iJcicZ454Service.ukeyFirst(iUkey, titaVo);
		if (rJcicZ454 == null) {
			throw new LogicException(titaVo, "E0001", "");
		}
		if (!rJcicZ454.getTranKey().equals("A") && rJcicZ454.getOutJcicTxtDate() == 0) {
			OccursList occursListA = new OccursList();
			String iLastUpdateEmpNo = rJcicZ454.getLastUpdateEmpNo();
			CdEmp iCdEmp = new CdEmp();
			occursListA.putParam("OOTranKey", rJcicZ454.getTranKey());
			occursListA.putParam("OOPayOffResult", rJcicZ454.getPayOffResult());
			occursListA.putParam("OOPayOffDate", rJcicZ454.getPayOffDate());
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
			String taU = rJcicZ454.getLastUpdate().toString();
			String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
			String uTime = taU.substring(11, 19);
			occursListA.putParam("OOLastUpdate", uaDate + " " + uTime);
			occursListA.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
			occursListA.putParam("OOOutJcicTxtDate", rJcicZ454.getOutJcicTxtDate());
			JcicZ454Log rrJcicZ454Log = iJcicZ454LogService.ukeyFirst(rJcicZ454.getUkey(), titaVo);
			occursListA.putParam("OOTxSeq", rrJcicZ454Log.getTxSeq());
			occursListA.putParam("OOUkey", rrJcicZ454Log.getUkey());

			this.totaVo.addOccursList(occursListA);
		}
		if (rJcicZ454Log == null) {
			throw new LogicException(titaVo, "E0001", "");
		}
		for (JcicZ454Log rrJcicZ454Log : rJcicZ454Log) {
			OccursList occursList = new OccursList();
			String iLastUpdateEmpNo = rrJcicZ454Log.getLastUpdateEmpNo();
			CdEmp iCdEmp = new CdEmp();
			occursList.putParam("OOTranKey", rrJcicZ454Log.getTranKey());
			occursList.putParam("OOPayOffResult", rrJcicZ454Log.getPayOffResult());
			occursList.putParam("OOPayOffDate", rrJcicZ454Log.getPayOffDate());
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
			String taU = rrJcicZ454Log.getLastUpdate().toString();
			String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
			String uTime = taU.substring(11, 19);
			occursList.putParam("OOLastUpdate", uaDate + " " + uTime);
			occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
			occursList.putParam("OOOutJcicTxtDate", rrJcicZ454Log.getOutJcicTxtDate());
			occursList.putParam("OOTxSeq", rrJcicZ454Log.getTxSeq());
			occursList.putParam("OOUkey", rrJcicZ454Log.getUkey());
			this.totaVo.addOccursList(occursList);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}
