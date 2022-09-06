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
import com.st1.itx.db.domain.JcicZ049;
import com.st1.itx.db.domain.JcicZ049Log;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ049LogService;
import com.st1.itx.db.service.JcicZ049Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8040")
@Scope("prototype")
public class L8040 extends TradeBuffer {
	@Autowired
	public CdEmpService iCdEmpService;
	@Autowired
	public JcicZ049Service iJcicZ049Service;
	@Autowired
	public JcicZ049LogService iJcicZ049LogService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8040 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("Ukey");

		this.index = titaVo.getReturnIndex();
		this.limit = 500;

		Slice<JcicZ049Log> rJcicZ049Log = null;
		rJcicZ049Log = iJcicZ049LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
		JcicZ049 rJcicZ049 = new JcicZ049();
		rJcicZ049 = iJcicZ049Service.ukeyFirst(iUkey, titaVo);
		if (rJcicZ049 == null) {
			throw new LogicException(titaVo, "E0001", "");
		}
		if (!rJcicZ049.getTranKey().equals("A") && rJcicZ049.getOutJcicTxtDate() == 0) {
			OccursList occursListA = new OccursList();
			String iLastUpdateEmpNo = rJcicZ049.getLastUpdateEmpNo();
			CdEmp iCdEmp = new CdEmp();
			occursListA.putParam("OOTranKey", rJcicZ049.getTranKey());
			occursListA.putParam("OOClaimStatus", rJcicZ049.getClaimStatus());
			occursListA.putParam("OOApplyDate", rJcicZ049.getApplyDate());
			occursListA.putParam("OOCourtCode", rJcicZ049.getCourtCode());
			occursListA.putParam("OOYear", rJcicZ049.getYear() - 1911);
			occursListA.putParam("OOCourtDiv", rJcicZ049.getCourtDiv());
			occursListA.putParam("OOCourtCaseNo", rJcicZ049.getCourtCaseNo());
			occursListA.putParam("OOApprove", rJcicZ049.getApprove());
			occursListA.putParam("OOClaimDate", rJcicZ049.getClaimDate());
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
			String taU = rJcicZ049.getLastUpdate().toString();
			String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
			String uTime = taU.substring(11, 19);
			occursListA.putParam("OOLastUpdate", uaDate + " " + uTime);
			occursListA.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
			occursListA.putParam("OOOutJcicTxtDate", rJcicZ049.getOutJcicTxtDate());
			JcicZ049Log rrJcicZ049Log = iJcicZ049LogService.ukeyFirst(rJcicZ049.getUkey(), titaVo);
			occursListA.putParam("OOTxSeq", rrJcicZ049Log.getTxSeq());
			occursListA.putParam("OOUkey", rrJcicZ049Log.getUkey());
			this.totaVo.addOccursList(occursListA);
		}
		if (rJcicZ049Log == null) {
			throw new LogicException(titaVo, "E0001", "");
		}
		for (JcicZ049Log rrJcicZ049Log : rJcicZ049Log) {
			OccursList occursList = new OccursList();
			String iLastUpdateEmpNo = rrJcicZ049Log.getLastUpdateEmpNo();
			CdEmp iCdEmp = new CdEmp();
			occursList.putParam("OOTranKey", rrJcicZ049Log.getTranKey());
			occursList.putParam("OOClaimStatus", rrJcicZ049Log.getClaimStatus());
			occursList.putParam("OOApplyDate", rrJcicZ049Log.getApplyDate());
			occursList.putParam("OOCourtCode", rrJcicZ049Log.getCourtCode());
			occursList.putParam("OOYear", rrJcicZ049Log.getYear() - 1911);
			occursList.putParam("OOCourtDiv", rrJcicZ049Log.getCourtDiv());
			occursList.putParam("OOCourtCaseNo", rrJcicZ049Log.getCourtCaseNo());
			occursList.putParam("OOApprove", rrJcicZ049Log.getApprove());
			occursList.putParam("OOClaimDate", rrJcicZ049Log.getClaimDate());
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
			String taU = rrJcicZ049Log.getLastUpdate().toString();
			String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
			String uTime = taU.substring(11, 19);
			occursList.putParam("OOLastUpdate", uaDate + " " + uTime);
			occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
			occursList.putParam("OOOutJcicTxtDate", rrJcicZ049Log.getOutJcicTxtDate());
			occursList.putParam("OOTxSeq", rrJcicZ049Log.getTxSeq());
			occursList.putParam("OOUkey", rrJcicZ049Log.getUkey());
			this.totaVo.addOccursList(occursList);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}
