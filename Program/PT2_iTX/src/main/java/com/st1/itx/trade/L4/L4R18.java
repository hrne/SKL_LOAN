package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.domain.InsuRenewId;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L4R18")
@Scope("prototype")
/**
 * 
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class L4R18 extends TradeBuffer {

	@Autowired
	public InsuRenewService insuRenewService;

	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R18 ");
		this.totaVo.init(titaVo);

//		L4611維護、刪除調rim

		int iClCode1 = parse.stringToInteger(titaVo.getParam("RimClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("RimClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("RimClNo"));
		String iPrevInsuNo = titaVo.getParam("RimPrevInsuNo");
		String iEndoInsuNo = titaVo.getParam("RimEndoInsuNo");
		int iInsuYearMonth = parse.stringToInteger(titaVo.getParam("RimInsuYearMonth"));
		

		if ("".equals(iEndoInsuNo)) {
			iEndoInsuNo = " ";
		}

		this.info("iClCode1 : " + iClCode1);
		this.info("iClCode2 : " + iClCode2);
		this.info("iClNo : " + iClNo);
		this.info("iPrevInsuNo : " + iPrevInsuNo);
		this.info("iEndoInsuNo : " + iEndoInsuNo);

		InsuRenew tInsuRenew = new InsuRenew();
		InsuRenewId tInsuRenewId = new InsuRenewId();

		tInsuRenewId.setClCode1(iClCode1);
		tInsuRenewId.setClCode2(iClCode2);
		tInsuRenewId.setClNo(iClNo);
		tInsuRenewId.setPrevInsuNo(iPrevInsuNo);
		tInsuRenewId.setEndoInsuNo(iEndoInsuNo);
		tInsuRenewId.setInsuYearMonth(iInsuYearMonth);

		tInsuRenew = insuRenewService.findById(tInsuRenewId, titaVo);

		if (tInsuRenew != null) {
			String renewCodeX = "";
			if (tInsuRenew.getRenewCode() == 1) {
				renewCodeX = "自保";
			} else if (tInsuRenew.getRenewCode() == 2) {
				renewCodeX = "續保";
			}

			this.totaVo.putParam("L4r18CustNo", tInsuRenew.getCustNo());
			this.totaVo.putParam("L4r18FacmNo", tInsuRenew.getFacmNo());
			this.totaVo.putParam("L4r18InsuYearMonth", tInsuRenew.getInsuYearMonth() - 191100);
			this.totaVo.putParam("L4r18NowInsuNo", tInsuRenew.getNowInsuNo());
			this.totaVo.putParam("L4r18EndoInsuNo", tInsuRenew.getEndoInsuNo());
			this.totaVo.putParam("L4r18FireInsuCovrg", tInsuRenew.getFireInsuCovrg());
			this.totaVo.putParam("L4r18FireInsuPrem", tInsuRenew.getFireInsuPrem());
			this.totaVo.putParam("L4r18EthqInsuCovrg", tInsuRenew.getEthqInsuCovrg());
			this.totaVo.putParam("L4r18EthqInsuPrem", tInsuRenew.getEthqInsuPrem());
			this.totaVo.putParam("L4r18InsuStartDate", tInsuRenew.getInsuStartDate());
			this.totaVo.putParam("L4r18InsuEndDate", tInsuRenew.getInsuEndDate());
			this.totaVo.putParam("L4r18AcDate", tInsuRenew.getAcDate());
			this.totaVo.putParam("L4r18NotiTempFg", tInsuRenew.getNotiTempFg());
			this.totaVo.putParam("L4r18TitaTxtNo", tInsuRenew.getTitaTxtNo());
			this.totaVo.putParam("L4r18StatusCode", tInsuRenew.getStatusCode());
			this.totaVo.putParam("L4r18OvduDate", tInsuRenew.getOvduDate());
			this.totaVo.putParam("L4r18OvduNo", tInsuRenew.getOvduNo());
			this.totaVo.putParam("L4r18RenewCode", tInsuRenew.getRenewCode());
			this.totaVo.putParam("L4r18RenewCodeX", renewCodeX);
			this.totaVo.putParam("L4r18InsuCompany", tInsuRenew.getInsuCompany());
			this.totaVo.putParam("L4r18InsuTypeCode", tInsuRenew.getInsuTypeCode());
			this.totaVo.putParam("L4r18CommericalFlag", tInsuRenew.getCommericalFlag());
			this.totaVo.putParam("L4r18Remark", tInsuRenew.getRemark());
			String iInsuReceiptDate = parse.IntegerToString(tInsuRenew.getInsuReceiptDate(),7);
			if(("0000000").equals(iInsuReceiptDate)) {
				iInsuReceiptDate = " ";
			}
			this.totaVo.putParam("L4r18InsuReceiptDate", iInsuReceiptDate);

		} else {
//			傳回前端，由前端判斷
			this.totaVo.putParam("L4r18CustNo", 0);
			this.totaVo.putParam("L4r18FacmNo", 0);
			this.totaVo.putParam("L4r18InsuYearMonth", 0);
			this.totaVo.putParam("L4r18NowInsuNo", "");
			this.totaVo.putParam("L4r18EndoInsuNo", "");
			this.totaVo.putParam("L4r18FireInsuCovrg", 0);
			this.totaVo.putParam("L4r18FireInsuPrem", 0);
			this.totaVo.putParam("L4r18EthqInsuCovrg", 0);
			this.totaVo.putParam("L4r18EthqInsuPrem", 0);
			this.totaVo.putParam("L4r18InsuStartDate", 0);
			this.totaVo.putParam("L4r18InsuEndDate", 0);
			this.totaVo.putParam("L4r18AcDate", 0);
			this.totaVo.putParam("L4r18NotiTempFg", "");
			this.totaVo.putParam("L4r18TitaTxtNo", 0);
			this.totaVo.putParam("L4r18StatusCode", 0);
			this.totaVo.putParam("L4r18OvduDate", 0);
			this.totaVo.putParam("L4r18OvduNo", 0);
			this.totaVo.putParam("L4r18RenewCode", 0);
			this.totaVo.putParam("L4r18RenewCodeX", 0);
			this.totaVo.putParam("L4r18InsuCompany", 0);
			this.totaVo.putParam("L4r18InsuTypeCode", 0);
			this.totaVo.putParam("L4r18CommericalFlag", "");
			this.totaVo.putParam("L4r18Remark", "");
			this.totaVo.putParam("L4r18InsuReceiptDate", "");
			this.totaVo.putParam("L4r18InsuReceiptDateX","");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}