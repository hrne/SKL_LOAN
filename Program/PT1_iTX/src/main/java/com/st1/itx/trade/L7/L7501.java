package com.st1.itx.trade.L7;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L7501ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;

/**
 * 房屋貸款餘額查詢<BR>
 * 供新光人壽官網查詢使用
 *
 * @author Wei
 * @version 1.0.0
 */
@Service("L7501")
@Scope("prototype")
public class L7501 extends TradeBuffer {

	@Autowired
	L7501ServiceImpl l7501ServiceImpl;

	private int occursCounts = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7501");
		this.totaVo.init(titaVo);

		String inputCustId = titaVo.getParam("CustId");

		this.info("L7501 inputCustId = " + inputCustId);

		List<Map<String, String>> loanDataList = l7501ServiceImpl.getLoanData(inputCustId, titaVo);

		if (loanDataList == null || loanDataList.isEmpty()) {
			this.info("L7501 result is empty.");
		} else {
			occursCounts = loanDataList.size();
			this.info("L7501 occursCounts = " + occursCounts);
			totaVo.putParam("CustNo", loanDataList.get(0).get("CustNo"));
			totaVo.putParam("CustName", loanDataList.get(0).get("CustName"));
			for (Map<String, String> loanData : loanDataList) {
				this.info("L7501 loanData = " + loanData.toString());
				moveTota(loanData);
			}
		}
		totaVo.putParam("OccursCounts", occursCounts);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveTota(Map<String, String> data) {
		// new occurs
		OccursList occurslist = new OccursList();
		occurslist.putParam("FacmNo", data.get("FacmNo"));
		occurslist.putParam("DrawdownDate", data.get("DrawdownDate"));
		occurslist.putParam("MaturityDate", data.get("MaturityDate"));
		occurslist.putParam("PrevIntDate", data.get("PrevIntDate"));
		occurslist.putParam("LoanBal", data.get("LoanBal"));
		occurslist.putParam("LbsDy", data.get("LbsDy"));
		this.totaVo.addOccursList(occurslist);		
	}
}