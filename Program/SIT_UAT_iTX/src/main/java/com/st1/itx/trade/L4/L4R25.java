package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.InsuOrignal;
import com.st1.itx.db.domain.InsuOrignalId;
import com.st1.itx.db.service.InsuOrignalService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L4R25")
@Scope("prototype")
/**
 * 
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class L4R25 extends TradeBuffer {

	@Autowired
	public InsuOrignalService insuOrignalService;

	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R25 ");
		this.totaVo.init(titaVo);

//		L4611維護、刪除調rim

		int iClCode1 = parse.stringToInteger(titaVo.getParam("RimClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("RimClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("RimClNo"));
		String iPrevInsuNo = titaVo.getParam("RimPrevInsuNo");
		String iRimEndoInsuNo = titaVo.getParam("RimEndoInsuNo");
        
		if("".equals(iRimEndoInsuNo)) {
			iRimEndoInsuNo = " " ;
		}
		InsuOrignal tInsuOrignal = new InsuOrignal();
		InsuOrignalId tInsuOrignalId = new InsuOrignalId();

		tInsuOrignalId.setClCode1(iClCode1);
		tInsuOrignalId.setClCode2(iClCode2);
		tInsuOrignalId.setClNo(iClNo);
		tInsuOrignalId.setEndoInsuNo(iRimEndoInsuNo);
		tInsuOrignalId.setOrigInsuNo(iPrevInsuNo);
		
		
		tInsuOrignal = insuOrignalService.findById(tInsuOrignalId, titaVo);

		if (tInsuOrignal != null) {

			this.totaVo.putParam("L4r25InsuCompany", tInsuOrignal.getInsuCompany());
			this.totaVo.putParam("L4r25InsuTypeCode", tInsuOrignal.getInsuTypeCode());
			this.totaVo.putParam("L4r25FireInsuCovrg", tInsuOrignal.getFireInsuCovrg());
			this.totaVo.putParam("L4r25EthqInsuCovrg", tInsuOrignal.getEthqInsuCovrg());
			this.totaVo.putParam("L4r25FireInsuPrem", tInsuOrignal.getFireInsuPrem());
			this.totaVo.putParam("L4r25EthqInsuPrem", tInsuOrignal.getEthqInsuPrem());
			this.totaVo.putParam("L4r25InsuStartDate", tInsuOrignal.getInsuStartDate());
			this.totaVo.putParam("L4r25InsuEndDate", tInsuOrignal.getInsuEndDate());
		} else {
			throw new LogicException(titaVo, "E0001", " 查無資料");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}