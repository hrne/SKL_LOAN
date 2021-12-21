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
import com.st1.itx.db.domain.JcicZ056;
import com.st1.itx.db.domain.JcicZ056Log;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ056LogService;
import com.st1.itx.db.service.JcicZ056Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8047")
@Scope("prototype")
public class L8047 extends TradeBuffer {
	@Autowired
	public CdEmpService iCdEmpService;
	@Autowired
	public JcicZ056Service iJcicZ056Service;
	@Autowired
	public JcicZ056LogService iJcicZ056LogService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8047 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("Ukey");

		this.index = titaVo.getReturnIndex();
		this.limit = 500;

		Slice<JcicZ056Log> rJcicZ056Log = null;
		rJcicZ056Log = iJcicZ056LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
		JcicZ056 rJcicZ056 = new JcicZ056();
		rJcicZ056 = iJcicZ056Service.ukeyFirst(iUkey, titaVo);
		if (rJcicZ056 == null) {
			throw new LogicException(titaVo, "E0001", "");
		}
		if (!rJcicZ056.getTranKey().equals("A") && rJcicZ056.getOutJcicTxtDate() == 0) {
			OccursList occursListA = new OccursList();
			String iLastUpdateEmpNo = rJcicZ056.getLastUpdateEmpNo();
			CdEmp iCdEmp = new CdEmp();
			occursListA.putParam("OOTranKey", rJcicZ056.getTranKey());
			occursListA.putParam("OOYear", rJcicZ056.getYear() - 1911);
			occursListA.putParam("OOCourtDiv", rJcicZ056.getCourtDiv());
			occursListA.putParam("OOCourtCaseNo", rJcicZ056.getCourtCaseNo());
			occursListA.putParam("OOApprove", rJcicZ056.getApprove());
			occursListA.putParam("OOOutstandAmt", rJcicZ056.getOutstandAmt());
			occursListA.putParam("OOClaimStatus1", rJcicZ056.getClaimStatus1());
			occursListA.putParam("OOSaveDate", rJcicZ056.getSaveDate());
			occursListA.putParam("OOClaimStatus2", rJcicZ056.getClaimStatus2());
			occursListA.putParam("OOSaveEndDate", rJcicZ056.getSaveEndDate());
			occursListA.putParam("OOSubAmt", rJcicZ056.getSubAmt());
			occursListA.putParam("OOAdminName", rJcicZ056.getAdminName());
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
			String taU = rJcicZ056.getLastUpdate().toString();
			String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
			String uTime = taU.substring(11, 19);
			occursListA.putParam("OOLastUpdate", uaDate + " " + uTime);
			occursListA.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
			occursListA.putParam("OOOutJcicTxtDate", rJcicZ056.getOutJcicTxtDate());
			this.totaVo.addOccursList(occursListA);
		}
		if (rJcicZ056Log == null) {
			throw new LogicException(titaVo, "E0001", "");
		}
		for (JcicZ056Log rrJcicZ056Log : rJcicZ056Log) {
			OccursList occursList = new OccursList();
			String iLastUpdateEmpNo = rrJcicZ056Log.getLastUpdateEmpNo();
			CdEmp iCdEmp = new CdEmp();
			occursList.putParam("OOTranKey", rrJcicZ056Log.getTranKey());
			occursList.putParam("OOYear", rrJcicZ056Log.getYear() - 1911);
			occursList.putParam("OOCourtDiv", rrJcicZ056Log.getCourtDiv());
			occursList.putParam("OOCourtCaseNo", rrJcicZ056Log.getCourtCaseNo());
			occursList.putParam("OOApprove", rrJcicZ056Log.getApprove());
			occursList.putParam("OOOutstandAmt", rrJcicZ056Log.getOutstandAmt());
			occursList.putParam("OOClaimStatus1", rrJcicZ056Log.getClaimStatus1());
			occursList.putParam("OOSaveDate", rrJcicZ056Log.getSaveDate());
			occursList.putParam("OOClaimStatus2", rrJcicZ056Log.getClaimStatus2());
			occursList.putParam("OOSaveEndDate", rrJcicZ056Log.getSaveEndDate());
			occursList.putParam("OOSubAmt", rrJcicZ056Log.getSubAmt());
			occursList.putParam("OOAdminName", rrJcicZ056Log.getAdminName());
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
			String taU = rrJcicZ056Log.getLastUpdate().toString();
			String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
			String uTime = taU.substring(11, 19);
			occursList.putParam("OOLastUpdate", uaDate + " " + uTime);
			occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
			occursList.putParam("OOOutJcicTxtDate", rrJcicZ056Log.getOutJcicTxtDate());
			this.totaVo.addOccursList(occursList);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}
