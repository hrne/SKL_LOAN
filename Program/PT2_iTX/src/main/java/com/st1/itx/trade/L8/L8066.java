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
import com.st1.itx.db.domain.JcicZ574;
import com.st1.itx.db.domain.JcicZ574Log;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ574LogService;
import com.st1.itx.db.service.JcicZ574Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8066")
@Scope("prototype")
public class L8066 extends TradeBuffer {
	@Autowired
	public CdEmpService iCdEmpService;
	@Autowired
	public JcicZ574Service iJcicZ574Service;
	@Autowired
	public JcicZ574LogService iJcicZ574LogService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8066 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("Ukey");

		this.index = titaVo.getReturnIndex();
		this.limit = 500;

		Slice<JcicZ574Log> rJcicZ574Log = null;
		rJcicZ574Log = iJcicZ574LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
		JcicZ574 rJcicZ574 = new JcicZ574();
		rJcicZ574 = iJcicZ574Service.ukeyFirst(iUkey, titaVo);
		if (rJcicZ574 == null) {
			throw new LogicException(titaVo, "E0001", "");
		}
		if (!rJcicZ574.getTranKey().equals("A") && rJcicZ574.getOutJcicTxtDate() == 0) {
			OccursList occursListA = new OccursList();
			String iLastUpdateEmpNo = rJcicZ574.getLastUpdateEmpNo();
			CdEmp iCdEmp = new CdEmp();
			occursListA.putParam("OOTranKey", rJcicZ574.getTranKey());
			occursListA.putParam("OOCloseDate", rJcicZ574.getCloseDate());
			occursListA.putParam("OOCloseMark", rJcicZ574.getCloseMark());
			occursListA.putParam("OOPhoneNo", rJcicZ574.getPhoneNo());
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
			String taU = rJcicZ574.getLastUpdate().toString();
			String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
			String uTime = taU.substring(11, 19);
			occursListA.putParam("OOLastUpdate", uaDate + " " + uTime);
			occursListA.putParam("OOLastUpdateEmpNo", rJcicZ574.getLastUpdateEmpNo());
			occursListA.putParam("OOOutJcicTxtDate", rJcicZ574.getOutJcicTxtDate());
			JcicZ574Log rrJcicZ574Log = iJcicZ574LogService.ukeyFirst(rJcicZ574.getUkey(), titaVo);
			occursListA.putParam("OOTxSeq", rrJcicZ574Log.getTxSeq());
			occursListA.putParam("OOUkey", rrJcicZ574Log.getUkey());

			this.totaVo.addOccursList(occursListA);
		}
		if (rJcicZ574Log == null) {
			throw new LogicException(titaVo, "E0001", "");
		}
		for (JcicZ574Log rrJcicZ574Log : rJcicZ574Log) {
			OccursList occursList = new OccursList();
			String iLastUpdateEmpNo = rrJcicZ574Log.getLastUpdateEmpNo();
			CdEmp iCdEmp = new CdEmp();
			occursList.putParam("OOTranKey", rrJcicZ574Log.getTranKey());
			occursList.putParam("OOCloseDate", rrJcicZ574Log.getCloseDate());
			occursList.putParam("OOCloseMark", rrJcicZ574Log.getCloseMark());
			occursList.putParam("OOPhoneNo", rrJcicZ574Log.getPhoneNo());
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
			String taU = rrJcicZ574Log.getLastUpdate().toString();
			String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
			String uTime = taU.substring(11, 19);
			occursList.putParam("OOLastUpdate", uaDate + " " + uTime);
			occursList.putParam("OOLastUpdateEmpNo", rrJcicZ574Log.getLastUpdateEmpNo());
			occursList.putParam("OOOutJcicTxtDate", rrJcicZ574Log.getOutJcicTxtDate());
			occursList.putParam("OOTxSeq", rrJcicZ574Log.getTxSeq());
			occursList.putParam("OOUkey", rrJcicZ574Log.getUkey());
			this.totaVo.addOccursList(occursList);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}
