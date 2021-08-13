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
import com.st1.itx.db.domain.PostDeductMedia;
import com.st1.itx.db.service.PostDeductMediaService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * RimAcDate=9,7<br>
 * RimBatchNo=X,6<br>
 * RimDetailSeq=9,6<br>
 */

@Service("L4R06")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4R06 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L4R06.class);
	@Autowired
	public Parse parse;

	@Autowired
	public PostDeductMediaService postDeductMediaService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R06 ");
		this.totaVo.init(titaVo);

		PostDeductMedia tPostDeductMedia = new PostDeductMedia();
//		PostDeductMediaId tPostDeductMediaId = new PostDeductMediaId();

		int iAcDate = parse.stringToInteger(titaVo.getParam("RimAcDate").trim()) + 19110000;
		String iBatchNo = titaVo.getParam("RimBatchNo").trim();
		int iDetailSeq = parse.stringToInteger(titaVo.getParam("RimDetailSeq").trim());

		tPostDeductMedia = postDeductMediaService.detailSeqFirst(iAcDate, iBatchNo, iDetailSeq);

		if (tPostDeductMedia != null) {
			this.totaVo.putParam("L4r06AcctCode", tPostDeductMedia.getPostDepCode());
			this.totaVo.putParam("L4r06OutsrcCode", tPostDeductMedia.getOutsrcCode());
			this.totaVo.putParam("L4r06DistCode", tPostDeductMedia.getDistCode());
			this.totaVo.putParam("L4r06TransDate", tPostDeductMedia.getTransDate());
			this.totaVo.putParam("L4r06RepayAcctNo", tPostDeductMedia.getRepayAcctNo());
			this.totaVo.putParam("L4r06PostUserNo", tPostDeductMedia.getPostUserNo());
			this.totaVo.putParam("L4r06OutsrcRemark", tPostDeductMedia.getOutsrcRemark());
			this.info("L4r06PostUserNo" + tPostDeductMedia.getPostUserNo());
			this.info("L4r06OutsrcRemark" + tPostDeductMedia.getOutsrcRemark());
		} else {
			throw new LogicException(titaVo, "E0001", " 查無資料");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}