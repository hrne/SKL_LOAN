package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.EmpDeductMedia;
import com.st1.itx.db.service.EmpDeductMediaService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L4R07")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4R07 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L4R07.class);
	@Autowired
	public Parse parse;

	@Autowired
	public EmpDeductMediaService empDeductMediaService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R07 ");
		this.totaVo.init(titaVo);

		int iAcDate = parse.stringToInteger(titaVo.getParam("RimAcDate").trim()) + 19110000;
		String iBatchNo = titaVo.getParam("RimBatchNo").trim();
		int iDetailSeq = parse.stringToInteger(titaVo.getParam("RimDetailSeq").trim());

		EmpDeductMedia tEmpDeductMedia = new EmpDeductMedia();

		tEmpDeductMedia = empDeductMediaService.detailSeqFirst(iAcDate, iBatchNo, iDetailSeq);

		if (tEmpDeductMedia != null) {
			this.totaVo.putParam("L4r07PerfMonth", tEmpDeductMedia.getPerfMonth() - 191100);
			this.totaVo.putParam("L4r07UnitCode", tEmpDeductMedia.getUnitCode());
			this.totaVo.putParam("L4r07CustId", tEmpDeductMedia.getCustId());
			this.totaVo.putParam("L4r07EntryDate", tEmpDeductMedia.getEntryDate());
		} else {
			throw new LogicException(titaVo, "E0001", " 查無資料");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}