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
import com.st1.itx.db.domain.JcicZ048;
import com.st1.itx.db.domain.JcicZ048Log;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ048LogService;
import com.st1.itx.db.service.JcicZ048Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8039")
@Scope("prototype")
public class L8039 extends TradeBuffer {
	@Autowired
	public CdEmpService iCdEmpService;
	@Autowired
	public JcicZ048Service iJcicZ048Service;
	@Autowired
	public JcicZ048LogService iJcicZ048LogService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8039 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("Ukey");

		this.index = titaVo.getReturnIndex();
		this.limit = 500;

		Slice<JcicZ048Log> rJcicZ048Log = null;
		rJcicZ048Log = iJcicZ048LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
		JcicZ048 rJcicZ048 = new JcicZ048();
		rJcicZ048 = iJcicZ048Service.ukeyFirst(iUkey, titaVo);
		if (rJcicZ048 == null) {
			throw new LogicException(titaVo, "E0001", "");
		}
		if (!rJcicZ048.getTranKey().equals("A") && rJcicZ048.getOutJcicTxtDate() == 0) {
			OccursList occursListA = new OccursList();
			String iLastUpdateEmpNo = rJcicZ048.getLastUpdateEmpNo();
			CdEmp iCdEmp = new CdEmp();
			occursListA.putParam("OOTranKey", rJcicZ048.getTranKey());
			occursListA.putParam("OOCustId", rJcicZ048.getCustId());
			occursListA.putParam("OORcDate", rJcicZ048.getRcDate());
			occursListA.putParam("OOCustRegAddr", rJcicZ048.getCustRegAddr());
			occursListA.putParam("OOCustComAddr", rJcicZ048.getCustComAddr());
			occursListA.putParam("OOCustRegTelNo", rJcicZ048.getCustRegTelNo());
			occursListA.putParam("OOCustComTelNo", rJcicZ048.getCustComTelNo());
			occursListA.putParam("OOCustMobilNo", rJcicZ048.getCustMobilNo());
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
			String taU = rJcicZ048.getLastUpdate().toString();
			String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
			String uTime = taU.substring(11, 19);
			occursListA.putParam("OOLastUpdate", uaDate + " " + uTime);
			occursListA.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
			occursListA.putParam("OOOutJcicTxtDate", rJcicZ048.getOutJcicTxtDate());
			JcicZ048Log rrJcicZ048Log = iJcicZ048LogService.ukeyFirst(rJcicZ048.getUkey(), titaVo);
			occursListA.putParam("OOTxSeq", rrJcicZ048Log.getTxSeq());
			occursListA.putParam("OOUkey", rrJcicZ048Log.getUkey());
			this.totaVo.addOccursList(occursListA);
		}
		if (rJcicZ048Log == null) {
			throw new LogicException(titaVo, "E0001", "");
		}
		for (JcicZ048Log rrJcicZ048Log : rJcicZ048Log) {
			OccursList occursList = new OccursList();
			String iLastUpdateEmpNo = rrJcicZ048Log.getLastUpdateEmpNo();
			CdEmp iCdEmp = new CdEmp();
			occursList.putParam("OOTranKey", rrJcicZ048Log.getTranKey());
			occursList.putParam("OOCustRegAddr", rrJcicZ048Log.getCustRegAddr());
			occursList.putParam("OOCustComAddr", rrJcicZ048Log.getCustComAddr());
			occursList.putParam("OOCustRegTelNo", rrJcicZ048Log.getCustRegTelNo());
			occursList.putParam("OOCustComTelNo", rrJcicZ048Log.getCustComTelNo());
			occursList.putParam("OOCustMobilNo", rrJcicZ048Log.getCustMobilNo());
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
			String taU = rrJcicZ048Log.getLastUpdate().toString();
			String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
			String uTime = taU.substring(11, 19);
			occursList.putParam("OOLastUpdate", uaDate + " " + uTime);
			occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
			occursList.putParam("OOOutJcicTxtDate", rrJcicZ048Log.getOutJcicTxtDate());
			occursList.putParam("OOTxSeq", rrJcicZ048Log.getTxSeq());
			occursList.putParam("OOUkey", rrJcicZ048Log.getUkey());
			this.totaVo.addOccursList(occursList);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}
