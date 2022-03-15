package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BatxDetail;
import com.st1.itx.db.domain.BatxDetailId;
import com.st1.itx.db.service.BatxDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * RimAcDate=9,7<br>
 * RimBatchNo=X,6<br>
 * RimDetailSeq=9,6<br>
 */

@Service("L4R08")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4R08 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L4R08.class);
	@Autowired
	public Parse parse;

	@Autowired
	public BatxDetailService batxDetailService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R08 ");
		this.totaVo.init(titaVo);

		int iAcDate = parse.stringToInteger(titaVo.getParam("RimAcDate").trim()) + 19110000;
		String iBatchNo = titaVo.getParam("RimBatchNo").trim();
		int iDetailSeq = parse.stringToInteger(titaVo.getParam("RimDetailSeq").trim());

		BatxDetail tBatxDetail = new BatxDetail();
		BatxDetailId tBatxDetailId = new BatxDetailId();

		tBatxDetailId.setAcDate(iAcDate);
		tBatxDetailId.setBatchNo(iBatchNo);
		tBatxDetailId.setDetailSeq(iDetailSeq);

		tBatxDetail = batxDetailService.findById(tBatxDetailId);

		if (tBatxDetail != null) {
			TempVo tempVo = new TempVo();
			tempVo = tempVo.getVo(tBatxDetail.getProcNote());

			String chequeAcct = tBatxDetail.getRvNo().substring(0, 9);
			String chequeNo = tBatxDetail.getRvNo().substring(10, 17);
			int chequeDate = 0;
			BigDecimal chequeAmt = BigDecimal.ZERO;

			this.info("chequeAcct : " + chequeAcct);
			this.info("chequeNo : " + chequeNo);

			this.totaVo.putParam("L4r08ChequeAcct", chequeAcct);
			this.totaVo.putParam("L4r08ChequeNo", chequeNo);

			if ("00000".equals(tBatxDetail.getProcCode())) {
				chequeDate = parse.stringToInteger(tempVo.getParam("Remark").substring(0, 7));
				chequeAmt = parse.stringToBigDecimal(tempVo.getParam("Remark").substring(8));
			}
			this.info("chequeDate : " + chequeDate);
			this.info("chequeAmt : " + chequeAmt);

			this.totaVo.putParam("L4r08ChequeDate", chequeDate);
			this.totaVo.putParam("L4r08ChequeAmt", chequeAmt);

		} else {
			throw new LogicException(titaVo, "E0001", " 查無資料");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}