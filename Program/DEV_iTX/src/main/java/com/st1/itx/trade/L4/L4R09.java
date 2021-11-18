package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BatxOthers;
import com.st1.itx.db.domain.BatxOthersId;
import com.st1.itx.db.service.BatxOthersService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L4R09")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4R09 extends TradeBuffer {
	@Autowired
	public Parse parse;

	@Autowired
	public BatxOthersService batxOthersService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R09 ");
		this.totaVo.init(titaVo);

		int iAcDate = parse.stringToInteger(titaVo.getParam("RimAcDate").trim()) + 19110000;
		String iBatchNo = titaVo.getParam("RimBatchNo").trim();
		int iDetailSeq = parse.stringToInteger(titaVo.getParam("RimDetailSeq").trim());

		BatxOthers tBatxOthers = new BatxOthers();
		BatxOthersId tBatxOthersId = new BatxOthersId();

		tBatxOthersId.setAcDate(iAcDate);
		tBatxOthersId.setBatchNo(iBatchNo);
		tBatxOthersId.setDetailSeq(iDetailSeq);

		tBatxOthers = batxOthersService.findById(tBatxOthersId);

		String AcNoCode = "";
		String AcSubCode = "";
		String AcDtlCode = "";

		if (tBatxOthers != null) {

			if (tBatxOthers.getRepayAcCode().length() >= 11)
				AcNoCode = tBatxOthers.getRepayAcCode().substring(0, 11);
			if (tBatxOthers.getRepayAcCode().length() >= 16)
				AcSubCode = tBatxOthers.getRepayAcCode().substring(11, 16);
			if (tBatxOthers.getRepayAcCode().length() >= 18)
				AcDtlCode = tBatxOthers.getRepayAcCode().substring(16);

			this.totaVo.putParam("L4r09RepayType", tBatxOthers.getRepayType());
			this.totaVo.putParam("L4r09RepayCode", tBatxOthers.getRepayCode());
			this.totaVo.putParam("L4r09AcNo", AcNoCode + AcSubCode + AcDtlCode);
			this.totaVo.putParam("L4r09RepayAmt", tBatxOthers.getRepayAmt());
			this.totaVo.putParam("L4r09EntryDate", tBatxOthers.getEntryDate());
			this.totaVo.putParam("L4r09RepayId", tBatxOthers.getRepayId());
			this.totaVo.putParam("L4r09RepayName", tBatxOthers.getRepayName());
			this.totaVo.putParam("L4r09CustNo", tBatxOthers.getCustNo());
			this.totaVo.putParam("L4r09FacmNo", tBatxOthers.getFacmNo());
			this.totaVo.putParam("L4r09CustNm", tBatxOthers.getCustNm());
			this.totaVo.putParam("L4r09RvNo", tBatxOthers.getRvNo());
			this.totaVo.putParam("L4r09Note", tBatxOthers.getNote());
		} else {
			throw new LogicException(titaVo, "E0001", " 查無資料");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}