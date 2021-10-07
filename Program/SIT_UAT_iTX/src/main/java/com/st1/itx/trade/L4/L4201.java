package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BatxDetail;
import com.st1.itx.db.domain.BatxDetailId;
import com.st1.itx.db.service.BatxDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * RepayTypeA=9,1<br>
 * VirtualAcctNoA=9,14<br>
 * END=X,1<br>
 */

@Service("L4201")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4201 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public BatxDetailService batxDetailService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4201 ");
		this.totaVo.init(titaVo);

		int iAcDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		String iBatchNo = titaVo.getParam("BatchNo");
		int iDetailSeq = parse.stringToInteger(titaVo.getParam("DetailSeq"));
		int iRepayTypeA = parse.stringToInteger(titaVo.getParam("RepayTypeA"));
		int iCustNoA = parse.stringToInteger(titaVo.getParam("CustNoA"));
		String iProcStsCode = titaVo.getParam("ProcStsCode");

		BatxDetail tBatxDetail = new BatxDetail();
		BatxDetailId tBatxDetailId = new BatxDetailId();

		tBatxDetailId.setAcDate(iAcDate);
		tBatxDetailId.setBatchNo(iBatchNo);
		tBatxDetailId.setDetailSeq(iDetailSeq);

		tBatxDetail = batxDetailService.findById(tBatxDetailId);

		if (tBatxDetail != null) {
			if (tBatxDetail.getRepayType() == iRepayTypeA && tBatxDetail.getCustNo() == iCustNoA
					&& iProcStsCode.equals(tBatxDetail.getProcStsCode())) {
				throw new LogicException(titaVo, "E0012", "修改值與現有資料相同");
			} else {
				tBatxDetail = batxDetailService.holdById(tBatxDetailId);
				tBatxDetail.setRepayType(iRepayTypeA);
				tBatxDetail.setCustNo(iCustNoA);
				tBatxDetail.setProcStsCode(iProcStsCode);
				try {
					batxDetailService.update(tBatxDetail);
				} catch (DBException e) {
					if (e.getErrorId() == 2)
						throw new LogicException(titaVo, "E0007", "update " + e.getErrorMsg());
				}
			}
		} else {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}