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
import com.st1.itx.db.domain.JcicZ044;
import com.st1.itx.db.domain.JcicZ044Log;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.JcicZ044LogService;
import com.st1.itx.db.service.JcicZ044Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8035")
@Scope("prototype")
public class L8035 extends TradeBuffer {
	@Autowired
	public CdEmpService iCdEmpService;
	@Autowired
	public JcicZ044Service iJcicZ044Service;
	@Autowired
	public JcicZ044LogService iJcicZ044LogService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8035 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("Ukey");
		this.index = titaVo.getReturnIndex();
		this.limit = 500;

		Slice<JcicZ044Log> rJcicZ044Log = null;
		rJcicZ044Log = iJcicZ044LogService.ukeyEq(iUkey, this.index, this.limit, titaVo);
		JcicZ044 rJcicZ044 = new JcicZ044();
		rJcicZ044 = iJcicZ044Service.ukeyFirst(iUkey, titaVo);
		if (rJcicZ044 == null) {
			throw new LogicException(titaVo, "E0001", "");
		}
		if (!rJcicZ044.getTranKey().equals("A") && rJcicZ044.getOutJcicTxtDate() == 0) {
			OccursList occursListA = new OccursList();
			String iLastUpdateEmpNo = rJcicZ044.getLastUpdateEmpNo();
			CdEmp iCdEmp = new CdEmp();
			occursListA.putParam("OOTranKey", rJcicZ044.getTranKey());

			occursListA.putParam("OODebtCode", rJcicZ044.getDebtCode());
			occursListA.putParam("OONonGageAmt", rJcicZ044.getNonGageAmt());
			occursListA.putParam("OOPeriod", rJcicZ044.getPeriod());
			occursListA.putParam("OORate", rJcicZ044.getRate());
			occursListA.putParam("OOMonthPayAmt", rJcicZ044.getMonthPayAmt());
			occursListA.putParam("OOReceYearIncome", rJcicZ044.getReceYearIncome());
			occursListA.putParam("OOReceYear", rJcicZ044.getReceYear());
			occursListA.putParam("OOReceYear2Income", rJcicZ044.getReceYear2Income());
			occursListA.putParam("OOReceYear2", rJcicZ044.getReceYear2());
			occursListA.putParam("OOCurrentMonthIncome", rJcicZ044.getCurrentMonthIncome());
			occursListA.putParam("OOLivingCost", rJcicZ044.getLivingCost());
			occursListA.putParam("OOCompName", rJcicZ044.getCompName());
			occursListA.putParam("OOCompId", rJcicZ044.getCompId());
			occursListA.putParam("OOCarCnt", rJcicZ044.getCarCnt());
			occursListA.putParam("OOHouseCnt", rJcicZ044.getHouseCnt());
			occursListA.putParam("OOLandCnt", rJcicZ044.getLandCnt());
			occursListA.putParam("OOChildCnt", rJcicZ044.getChildCnt());
			occursListA.putParam("OOChildRate", rJcicZ044.getChildRate());
			occursListA.putParam("OOParentCnt", rJcicZ044.getParentCnt());
			occursListA.putParam("OOParentRate", rJcicZ044.getParentRate());
			occursListA.putParam("OOMouthCnt", rJcicZ044.getMouthCnt());
			occursListA.putParam("OOMouthRate", rJcicZ044.getMouthRate());
			occursListA.putParam("OOGradeType", rJcicZ044.getGradeType());
			occursListA.putParam("OOPayLastAmt", rJcicZ044.getPayLastAmt());
			occursListA.putParam("OOPeriod2", rJcicZ044.getPeriod2());
			occursListA.putParam("OORate2", rJcicZ044.getRate2());
			occursListA.putParam("OOMonthPayAmt2", rJcicZ044.getMonthPayAmt2());
			occursListA.putParam("OOPayLastAmt2", rJcicZ044.getPayLastAmt2());
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
			String taU = rJcicZ044.getLastUpdate().toString();
			String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
			String uTime = taU.substring(11, 19);
			occursListA.putParam("OOLastUpdate", uaDate + " " + uTime);
			occursListA.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
			occursListA.putParam("OOOutJcicTxtDate", rJcicZ044.getOutJcicTxtDate());
			this.totaVo.addOccursList(occursListA);
		}
		if (rJcicZ044Log == null) {
			throw new LogicException(titaVo, "E0001", "");
		}
		for (JcicZ044Log rrJcicZ044Log : rJcicZ044Log) {
			OccursList occursList = new OccursList();
			String iLastUpdateEmpNo = rrJcicZ044Log.getLastUpdateEmpNo();
			CdEmp iCdEmp = new CdEmp();
			occursList.putParam("OOTranKey", rrJcicZ044Log.getTranKey());
			occursList.putParam("OODebtCode", rrJcicZ044Log.getDebtCode());
			occursList.putParam("OONonGageAmt", rrJcicZ044Log.getNonGageAmt());
			occursList.putParam("OOPeriod", rrJcicZ044Log.getPeriod());
			occursList.putParam("OORate", rrJcicZ044Log.getRate());
			occursList.putParam("OOMonthPayAmt", rrJcicZ044Log.getMonthPayAmt());
			occursList.putParam("OOReceYearIncome", rrJcicZ044Log.getReceYearIncome());
			occursList.putParam("OOReceYear", rrJcicZ044Log.getReceYear());
			occursList.putParam("OOReceYear2Income", rrJcicZ044Log.getReceYear2Income());
			occursList.putParam("OOReceYear2", rrJcicZ044Log.getReceYear2());
			occursList.putParam("OOCurrentMonthIncome", rrJcicZ044Log.getCurrentMonthIncome());
			occursList.putParam("OOLivingCost", rrJcicZ044Log.getLivingCost());
			occursList.putParam("OOCompName", rrJcicZ044Log.getCompName());
			occursList.putParam("OOCompId", rrJcicZ044Log.getCompId());
			occursList.putParam("OOCarCnt", rrJcicZ044Log.getCarCnt());
			occursList.putParam("OOHouseCnt", rrJcicZ044Log.getHouseCnt());
			occursList.putParam("OOLandCnt", rrJcicZ044Log.getLandCnt());
			occursList.putParam("OOChildCnt", rrJcicZ044Log.getChildCnt());
			occursList.putParam("OOChildRate", rrJcicZ044Log.getChildRate());
			occursList.putParam("OOParentCnt", rrJcicZ044Log.getParentCnt());
			occursList.putParam("OOParentRate", rrJcicZ044Log.getParentRate());
			occursList.putParam("OOMouthCnt", rrJcicZ044Log.getMouthCnt());
			occursList.putParam("OOMouthRate", rrJcicZ044Log.getMouthRate());
			occursList.putParam("OOGradeType", rrJcicZ044Log.getGradeType());
			occursList.putParam("OOPayLastAmt", rrJcicZ044Log.getPayLastAmt());
			occursList.putParam("OOPeriod2", rrJcicZ044Log.getPeriod2());
			occursList.putParam("OORate2", rrJcicZ044Log.getRate2());
			occursList.putParam("OOMonthPayAmt2", rrJcicZ044Log.getMonthPayAmt2());
			occursList.putParam("OOPayLastAmt2", rrJcicZ044Log.getPayLastAmt2());
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
			String taU = rrJcicZ044Log.getLastUpdate().toString();
			String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
			uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
			String uTime = taU.substring(11, 19);
			occursList.putParam("OOLastUpdate", uaDate + " " + uTime);
			occursList.putParam("OOLastUpdateEmpNo", iLastUpdateEmpNo);
			occursList.putParam("OOOutJcicTxtDate", rrJcicZ044Log.getOutJcicTxtDate());
			this.totaVo.addOccursList(occursList);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}
