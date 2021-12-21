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
import com.st1.itx.db.domain.JcicZ444;
import com.st1.itx.db.domain.JcicZ444Log;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ444LogService;
import com.st1.itx.db.service.JcicZ444Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8055")
@Scope("prototype")
public class L8055 extends TradeBuffer {
	@Autowired
	public CdEmpService iCdEmpService;
	@Autowired
	public JcicZ444Service iJcicZ444Service;
	@Autowired
	public JcicZ444LogService iJcicZ444LogService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8055 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("Ukey");

		this.index = titaVo.getReturnIndex();
		this.limit = 500;

		Slice<JcicZ444Log> rJcicZ444Log = null;
		rJcicZ444Log = iJcicZ444LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
		JcicZ444 rJcicZ444 = new JcicZ444();
		rJcicZ444 = iJcicZ444Service.ukeyFirst(iUkey, titaVo);
		if (rJcicZ444 == null) {
			throw new LogicException(titaVo, "E0001", "");
		}
		if (!rJcicZ444.getTranKey().equals("A") && rJcicZ444.getOutJcicTxtDate() == 0) {
			OccursList occursListA = new OccursList();
			String iLastUpdateEmpNo = rJcicZ444.getLastUpdateEmpNo();
			CdEmp iCdEmp = new CdEmp();
			occursListA.putParam("OOTranKey", rJcicZ444.getTranKey());
			occursListA.putParam("OOCustRegAddr", rJcicZ444.getCustRegAddr());
			occursListA.putParam("OOCustComAddr", rJcicZ444.getCustComAddr());
			occursListA.putParam("OOCustRegTelNo", rJcicZ444.getCustRegTelNo());
			occursListA.putParam("OOCustComTelNo", rJcicZ444.getCustComTelNo());
			occursListA.putParam("OOCustMobilNo", rJcicZ444.getCustMobilNo());
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
			String taU = rJcicZ444.getLastUpdate().toString();
			String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
			String uTime = taU.substring(11, 19);
			occursListA.putParam("OOLastUpdate", uaDate + " " + uTime);
			occursListA.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
			occursListA.putParam("OOOutJcicTxtDate", rJcicZ444.getOutJcicTxtDate());
			this.totaVo.addOccursList(occursListA);
		}
		if (rJcicZ444Log == null) {
			throw new LogicException(titaVo, "E0001", "");
		}
		for (JcicZ444Log rrJcicZ444Log : rJcicZ444Log) {
			OccursList occursList = new OccursList();
			String iLastUpdateEmpNo = rrJcicZ444Log.getLastUpdateEmpNo();
			CdEmp iCdEmp = new CdEmp();
			occursList.putParam("OOTranKey", rrJcicZ444Log.getTranKey());
			occursList.putParam("OOCustRegAddr", rrJcicZ444Log.getCustRegAddr());
			occursList.putParam("OOCustComAddr", rrJcicZ444Log.getCustComAddr());
			occursList.putParam("OOCustRegTelNo", rrJcicZ444Log.getCustRegTelNo());
			occursList.putParam("OOCustComTelNo", rrJcicZ444Log.getCustComTelNo());
			occursList.putParam("OOCustMobilNo", rrJcicZ444Log.getCustMobilNo());
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
			String taU = rrJcicZ444Log.getLastUpdate().toString();
			String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
			String uTime = taU.substring(11, 19);
			occursList.putParam("OOLastUpdate", uaDate + " " + uTime);
			occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
			occursList.putParam("OOOutJcicTxtDate", rrJcicZ444Log.getOutJcicTxtDate());
			this.totaVo.addOccursList(occursList);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}
