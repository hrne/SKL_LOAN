package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L4931ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * CUST_TYPE=9,1<br>
 * RATECATE=X,2<br>
 * ADJUSTIND=X,1<br>
 * INQCD=9,1<br>
 * RATEIND=9,1<br>
 * YYYMM=9,5<br>
 * END=X,1<br>
 */

@Service("L4931")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4931 extends TradeBuffer {

	@Autowired
	public Parse parse;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public L4931ServiceImpl l4931ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4931 ");
		this.totaVo.init(titaVo);

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100;

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			// *** 折返控制相關 ***
			resultList = l4931ServiceImpl.findAll(this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.error("l4931ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", e.getMessage());
		}

		if (resultList != null && resultList.size() != 0) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			if (resultList.size() == this.limit && hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}

			for (Map<String, String> result : resultList) {
				this.info("result =" + result.toString());
				OccursList occursList = new OccursList();
				int presEffDate = parse.stringToInteger(result.get("PresEffDate"));
				int curtEffDate = parse.stringToInteger(result.get("CurtEffDate"));
				int prevIntDate = parse.stringToInteger(result.get("PrevIntDate"));

				if (presEffDate > 19110000) {
					presEffDate = presEffDate - 19110000;
				}
				if (curtEffDate > 19110000) {
					curtEffDate = curtEffDate - 19110000;
				}
				if (prevIntDate > 19110000) {
					prevIntDate = prevIntDate - 19110000;
				}
				occursList.putParam("OOCityItem", result.get("CityItem"));
				occursList.putParam("OOAreaItem", result.get("AreaItem"));
				occursList.putParam("OOAdjCode", result.get("AdjCode"));
				occursList.putParam("OOCustNo", result.get("CustNo"));
				occursList.putParam("OOFacmNo", result.get("FacmNo"));
				occursList.putParam("OOBormNo", result.get("BormNo"));
				occursList.putParam("OOCustName", result.get("CustName"));
				occursList.putParam("OOTotalLoanBal", result.get("TotBalance"));
				occursList.putParam("OODrawdownAmt", result.get("DrawdownAmt"));
				occursList.putParam("OOLoanBal", result.get("LoanBalance"));
				occursList.putParam("OOPresEffDate", presEffDate);
				occursList.putParam("OOCurtEffDate", curtEffDate);
				occursList.putParam("OOPrevIntDate", prevIntDate);
				occursList.putParam("OOCustCode", result.get("CustCode"));
				occursList.putParam("OOProdNo", result.get("ProdNo"));
				occursList.putParam("OORateIncr", result.get("RateIncr"));
				occursList.putParam("OOContractRate", result.get("ContractRate"));
				occursList.putParam("OOPresentRate", result.get("PresentRate"));
				occursList.putParam("OOProposalRate", result.get("ProposalRate"));
				occursList.putParam("OOAdjustedRate", result.get("AdjustedRate"));
				occursList.putParam("OOContrIndexRate", result.get("ContrBaseRate"));
				occursList.putParam("OOContrRateIncr", result.get("ContrRateIncr"));
				occursList.putParam("OOPropIndexRate", result.get("CurrBaseRate"));
				occursList.putParam("OOIndividualIncr", result.get("IndividualIncr"));
				occursList.putParam("OOUpperLimitRate", result.get("UpperLimitRate"));
				occursList.putParam("OOLowerLimitRate", result.get("LowerLimitRate"));
				occursList.putParam("OOOvduTerm", result.get("OvduTerm"));
				occursList.putParam("OOIncrFlag", result.get("IncrFlag"));

				TempVo tempVo = new TempVo();
				tempVo = tempVo.getVo(result.get("F34"));
				String procNote = "";
				if (tempVo.get("CheckMsg") != null) {
					procNote += tempVo.get("CheckMsg");
				}
				if (tempVo.get("WarnMsg") != null) {
					procNote += tempVo.get("WarnMsg");
				}
				occursList.putParam("OOProcNote", procNote);

				this.totaVo.addOccursList(occursList);
			}
		} else {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private Boolean hasNext() {
		Boolean result = true;

		int times = this.index + 1;
		int cnt = l4931ServiceImpl.getSize();
		int size = times * this.limit;

		this.info("index ..." + this.index);
		this.info("times ..." + times);
		this.info("cnt ..." + cnt);
		this.info("size ..." + size);

		if (size == cnt) {
			result = false;
		}
		this.info("result ..." + result);

		return result;
	}
}