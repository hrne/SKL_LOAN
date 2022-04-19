package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AchDeductMedia;
import com.st1.itx.db.service.AchDeductMediaService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L4R05")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4R05 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L4R05.class);

	@Autowired
	public Parse parse;

	@Autowired
	public AchDeductMediaService achDeductMediaService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R05 ");
		this.totaVo.init(titaVo);

		int iAcDate = parse.stringToInteger(titaVo.getParam("RimAcDate").trim()) + 19110000;
		String iBatchNo = titaVo.getParam("RimBatchNo").trim();
		int iDetailSeq = parse.stringToInteger(titaVo.getParam("RimDetailSeq").trim());

		AchDeductMedia tAchDeductMedia = new AchDeductMedia();

		tAchDeductMedia = achDeductMediaService.detailSeqFirst(iAcDate, iBatchNo, iDetailSeq);

		if (tAchDeductMedia != null) {
			this.totaVo.putParam("L4r05EntryDate", tAchDeductMedia.getEntryDate());
			this.totaVo.putParam("L4r05CustNo", tAchDeductMedia.getCustNo());
			this.totaVo.putParam("L4r05FacmNo", tAchDeductMedia.getFacmNo());
			this.totaVo.putParam("L4r05IntEndDate", tAchDeductMedia.getIntEndDate());
			this.totaVo.putParam("L4r05RepayBank", tAchDeductMedia.getRepayBank());
			this.totaVo.putParam("L4r05RepayAcctNo", tAchDeductMedia.getRepayAcctNo());
			this.totaVo.putParam("L4r05AchRepayCode", tAchDeductMedia.getAchRepayCode());
		} else {
			throw new LogicException(titaVo, "E0001", " 查無資料");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}