package com.st1.itx.trade.L3;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.LoanIntDetail;
import com.st1.itx.db.service.LoanIntDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L3913")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L3913 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L3913.class);

	@Autowired
	public LoanIntDetailService sLoanIntDetailService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3913 ");
		this.totaVo.init(titaVo);

		// 輸入數值TITA
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		int iBormNo = parse.stringToInteger(titaVo.getParam("BormNo"));
		int iAcDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		String iTellerNo = titaVo.getParam("TellerNo");
		String iTxtNo = titaVo.getParam("TxtNo");

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500; // 39 + 98 * 500 = 49039

		Slice<LoanIntDetail> slLoanIntDetail;
		List<LoanIntDetail> lLoanIntDetail;

		slLoanIntDetail = sLoanIntDetailService.fildFacmNoEq(iCustNo, iFacmNo, iBormNo, iAcDate, iTellerNo, iTxtNo, this.index, this.limit, titaVo);
		if (slLoanIntDetail == null) {
			throw new LogicException(titaVo, "E0001", "計息明細檔"); // 查詢資料不存在
		}
		lLoanIntDetail = slLoanIntDetail == null ? null : new ArrayList<LoanIntDetail>(slLoanIntDetail.getContent());

		for (LoanIntDetail tLoanIntDetail : lLoanIntDetail) {

			OccursList occursList = new OccursList();

			occursList.putParam("OOFacmNo", tLoanIntDetail.getFacmNo());
			occursList.putParam("OOBormNo", tLoanIntDetail.getBormNo());
			occursList.putParam("OOIntStartDate", tLoanIntDetail.getIntStartDate());
			occursList.putParam("OOIntEndDate", tLoanIntDetail.getIntEndDate());
			occursList.putParam("OOAmount", tLoanIntDetail.getAmount());
			occursList.putParam("OORate", tLoanIntDetail.getIntRate());
			occursList.putParam("OOPrincipal", tLoanIntDetail.getPrincipal());
			occursList.putParam("OOInterest", tLoanIntDetail.getInterest());
			occursList.putParam("OODelayInt", tLoanIntDetail.getDelayInt());
			occursList.putParam("OOBreachAmt", tLoanIntDetail.getBreachAmt());
			occursList.putParam("OOCloseBreachAmt", tLoanIntDetail.getCloseBreachAmt());
			occursList.putParam("OOLoanBal", tLoanIntDetail.getLoanBal());

			// 將每筆資料放入Tota的OcList
			this.totaVo.addOccursList(occursList);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}