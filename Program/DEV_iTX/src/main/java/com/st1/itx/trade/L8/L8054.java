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
import com.st1.itx.db.domain.JcicZ443;
import com.st1.itx.db.domain.JcicZ443Log;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ443LogService;
import com.st1.itx.db.service.JcicZ443Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8054")
@Scope("prototype")
public class L8054 extends TradeBuffer {
	@Autowired
	public CdEmpService iCdEmpService;
	@Autowired
	public JcicZ443Service iJcicZ443Service;
	@Autowired
	public JcicZ443LogService iJcicZ443LogService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8054 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("Ukey");

		this.index = titaVo.getReturnIndex();
		this.limit = 500;

		Slice<JcicZ443Log> rJcicZ443Log = null;
		rJcicZ443Log = iJcicZ443LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
		JcicZ443 rJcicZ443 = new JcicZ443();
		rJcicZ443 = iJcicZ443Service.ukeyFirst(iUkey, titaVo);
		if (rJcicZ443 == null) {
			throw new LogicException(titaVo, "E0001", "");
		}
		if (!rJcicZ443.getTranKey().equals("A") && rJcicZ443.getOutJcicTxtDate() == 0) {
			OccursList occursListA = new OccursList();
			String iLastUpdateEmpNo = rJcicZ443.getLastUpdateEmpNo();
			CdEmp iCdEmp = new CdEmp();
			occursListA.putParam("OOTranKey", rJcicZ443.getTranKey());
			occursListA.putParam("OOIsMaxMain", rJcicZ443.getIsMaxMain());
			occursListA.putParam("OOGuarantyType", rJcicZ443.getGuarantyType());
			occursListA.putParam("OOLoanAmt", rJcicZ443.getLoanAmt());
			occursListA.putParam("OOCreditAmt", rJcicZ443.getCreditAmt());
			occursListA.putParam("OOPrincipal", rJcicZ443.getPrincipal());
			occursListA.putParam("OOInterest", rJcicZ443.getInterest());
			occursListA.putParam("OOPenalty", rJcicZ443.getPenalty());
			occursListA.putParam("OOOther", rJcicZ443.getOther());
			occursListA.putParam("OOTerminalPayAmt", rJcicZ443.getTerminalPayAmt());
			occursListA.putParam("OOLatestPayAmt", rJcicZ443.getLatestPayAmt());
			occursListA.putParam("OOFinalPayDay", rJcicZ443.getFinalPayDay());
			occursListA.putParam("OONotyetacQuit", rJcicZ443.getNotyetacQuit());
			occursListA.putParam("OOMothPayDay", rJcicZ443.getMothPayDay());
			occursListA.putParam("OOBeginDate", rJcicZ443.getBeginDate()-191100);
			occursListA.putParam("OOEndDate", rJcicZ443.getEndDate()-191100);
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
			String taU = rJcicZ443.getLastUpdate().toString();
			String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
			String uTime = taU.substring(11, 19);
			occursListA.putParam("OOLastUpdate", uaDate + " " + uTime);
			occursListA.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
			occursListA.putParam("OOOutJcicTxtDate", rJcicZ443.getOutJcicTxtDate());
			this.totaVo.addOccursList(occursListA);
		}
		if (rJcicZ443Log == null) {
			throw new LogicException(titaVo, "E0001", "");
		}
		for (JcicZ443Log rrJcicZ443Log : rJcicZ443Log) {
			OccursList occursList = new OccursList();
			String iLastUpdateEmpNo = rrJcicZ443Log.getLastUpdateEmpNo();
			CdEmp iCdEmp = new CdEmp();
			occursList.putParam("OOTranKey", rrJcicZ443Log.getTranKey());
			occursList.putParam("OOIsMaxMain", rrJcicZ443Log.getIsMaxMain());
			occursList.putParam("OOGuarantyType", rrJcicZ443Log.getGuarantyType());
			occursList.putParam("OOLoanAmt", rrJcicZ443Log.getLoanAmt());
			occursList.putParam("OOCreditAmt", rrJcicZ443Log.getCreditAmt());
			occursList.putParam("OOPrincipal", rrJcicZ443Log.getPrincipal());
			occursList.putParam("OOInterest", rrJcicZ443Log.getInterest());
			occursList.putParam("OOPenalty", rrJcicZ443Log.getPenalty());
			occursList.putParam("OOOther", rrJcicZ443Log.getOther());
			occursList.putParam("OOTerminalPayAmt", rrJcicZ443Log.getTerminalPayAmt());
			occursList.putParam("OOLatestPayAmt", rrJcicZ443Log.getLatestPayAmt());
			occursList.putParam("OOFinalPayDay", rrJcicZ443Log.getFinalPayDay());
			occursList.putParam("OONotyetacQuit", rrJcicZ443Log.getNotyetacQuit());
			occursList.putParam("OOMothPayDay", rrJcicZ443Log.getMothPayDay());
			occursList.putParam("OOBeginDate", rrJcicZ443Log.getBeginDate()-191100);
			occursList.putParam("OOEndDate", rrJcicZ443Log.getEndDate()-191100);
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
			String taU = rrJcicZ443Log.getLastUpdate().toString();
			String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
			String uTime = taU.substring(11, 19);
			occursList.putParam("OOLastUpdate", uaDate + " " + uTime);
			occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
			occursList.putParam("OOOutJcicTxtDate", rrJcicZ443Log.getOutJcicTxtDate());
			this.totaVo.addOccursList(occursList);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}
