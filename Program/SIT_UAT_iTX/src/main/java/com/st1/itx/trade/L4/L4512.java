package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.EmpDeductMedia;
import com.st1.itx.db.domain.EmpDeductMediaId;
import com.st1.itx.db.service.EmpDeductMediaService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4512")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4512 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L4512.class);

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public EmpDeductMediaService empDeductMediaService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4512 ");
		this.totaVo.init(titaVo);

//		2.媒體維護
		int iFunctionCode = parse.stringToInteger(titaVo.getParam("FunctionCode"));

		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));

		int iRepayCode = parse.stringToInteger(titaVo.getParam("RepayCode"));

		int iPerfMonth = parse.stringToInteger(titaVo.getParam("PerfMonth")) + 191100;

		int iPerfRepayCode = parse.stringToInteger(titaVo.getParam("PerfRepayCode"));

		BigDecimal iRepayAmt = parse.stringToBigDecimal(titaVo.getParam("RepayAmt"));

		int iMediaDate = parse.stringToInteger(titaVo.getParam("MediaDate")) + 19110000;

		String iMediaKind = titaVo.getParam("MediaKind");

		int iMediaSeq = parse.stringToInteger(titaVo.getParam("MediaSeq"));

		EmpDeductMedia tEmpDeductMedia = new EmpDeductMedia();
		EmpDeductMediaId tEmpDeductMediaId = new EmpDeductMediaId();

		if (iFunctionCode == 2) {
			tEmpDeductMediaId.setMediaDate(iMediaDate);
			tEmpDeductMediaId.setMediaKind(iMediaKind);
			tEmpDeductMediaId.setMediaSeq(iMediaSeq);

			tEmpDeductMedia = empDeductMediaService.holdById(tEmpDeductMediaId);

			tEmpDeductMedia.setCustNo(iCustNo);
			tEmpDeductMedia.setRepayCode(iRepayCode);
			tEmpDeductMedia.setPerfMonth(iPerfMonth);
			tEmpDeductMedia.setPerfRepayCode(iPerfRepayCode);
			tEmpDeductMedia.setRepayAmt(iRepayAmt);

			try {
				empDeductMediaService.update(tEmpDeductMedia);
			} catch (DBException e) {
				throw new LogicException("E0007", "EmpDeductMedia update error : " + e.getErrorMsg());
			}

		} else if (iFunctionCode == 4) {
			tEmpDeductMediaId.setMediaDate(iMediaDate);
			tEmpDeductMediaId.setMediaKind(iMediaKind);
			tEmpDeductMediaId.setMediaSeq(iMediaSeq);

			tEmpDeductMedia = empDeductMediaService.holdById(tEmpDeductMediaId);
			try {
				empDeductMediaService.delete(tEmpDeductMedia);
			} catch (DBException e) {
				throw new LogicException("E0008", "EmpDeductMedia delete error : " + e.getErrorMsg());
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}