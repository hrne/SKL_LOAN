package com.st1.itx.trade.LM;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.MonthlyLM042RBC;
import com.st1.itx.db.domain.MonthlyLM042RBCId;
import com.st1.itx.db.service.MonthlyLM042RBCService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 */
@Service("LMR42")
@Scope("prototype")
/**
 *
 *
 * @author Ted
 * @version 1.0.0
 */
public class LMR42 extends TradeBuffer {

	@Autowired
	MonthlyLM042RBCService sMonthlyLM042RBCService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LM042 ");
		this.totaVo.init(titaVo);
		int yearMonth = this.parse.stringToInteger(titaVo.getParam("YearMonth")) + 191100;

		Slice<MonthlyLM042RBC> sMonthlyLM042RBC;
		// 找當月12筆(一整組為12筆)
		sMonthlyLM042RBC = sMonthlyLM042RBCService.findYearMonthAll(yearMonth, 0, 12, titaVo);

		// 判斷有無當月資料
		if (sMonthlyLM042RBC == null) {
			this.info("insert data");
			// 新增當月資料
			insertData(titaVo, yearMonth);

			// 新增後再次搜尋
			sMonthlyLM042RBC = sMonthlyLM042RBCService.findYearMonthAll(yearMonth, 0, 12, titaVo);
		}

		List<MonthlyLM042RBC> lMonthlyLM042RBC = sMonthlyLM042RBC == null ? null : sMonthlyLM042RBC.getContent();

		this.info("lMonthlyLM042RBC=" + lMonthlyLM042RBC.toString());

		// 畫面顯示 風險係數
		for (MonthlyLM042RBC tMonthlyLM042RBC : lMonthlyLM042RBC) {
			if ("1".equals(tMonthlyLM042RBC.getLoanType())) {

				switch (tMonthlyLM042RBC.getLoanItem()) {
				case "A":
					this.totaVo.putParam("LMR42RiskFactor1", tMonthlyLM042RBC.getRiskFactor());
					break;
				case "B":
					this.totaVo.putParam("LMR42RiskFactor2", tMonthlyLM042RBC.getRiskFactor());
					break;
				case "C":
					this.totaVo.putParam("LMR42RiskFactor3", tMonthlyLM042RBC.getRiskFactor());
					break;
				case "D":
					this.totaVo.putParam("LMR42RiskFactor4", tMonthlyLM042RBC.getRiskFactor());
					break;
				case "E":
					this.totaVo.putParam("LMR42RiskFactor5", tMonthlyLM042RBC.getRiskFactor());
					break;
				case "F":
					this.totaVo.putParam("LMR42RiskFactor6", tMonthlyLM042RBC.getRiskFactor());
					break;
				}

			}

		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	/**
	 * 新增資料
	 * 
	 * @param titaVo
	 * @param yearMonth 當月
	 */
	private void insertData(TitaVo titaVo, int yearMonth) throws LogicException {
		List<MonthlyLM042RBC> insMonthlyLM042RBC = new ArrayList<MonthlyLM042RBC>();

		MonthlyLM042RBC mMonthlyLM042RBC = new MonthlyLM042RBC();
		MonthlyLM042RBCId monthlyLM042RBCId = new MonthlyLM042RBCId();

		// 新增兩次:一般放款(1)和專案放款(2)
		for (int i = 1; i <= 2; i++) {
			mMonthlyLM042RBC = new MonthlyLM042RBC();
			monthlyLM042RBCId = new MonthlyLM042RBCId();
			monthlyLM042RBCId.setYearMonth(yearMonth);
			monthlyLM042RBCId.setLoanType(String.valueOf(i));
			monthlyLM042RBCId.setLoanItem("A");
			monthlyLM042RBCId.setRelatedCode("N");
			mMonthlyLM042RBC.setMonthlyLM042RBCId(monthlyLM042RBCId);
			mMonthlyLM042RBC.setRiskFactor(new BigDecimal("0.0000"));
			mMonthlyLM042RBC.setLoanAmount(new BigDecimal("0"));
			insMonthlyLM042RBC.add(mMonthlyLM042RBC);

			mMonthlyLM042RBC = new MonthlyLM042RBC();
			monthlyLM042RBCId = new MonthlyLM042RBCId();
			monthlyLM042RBCId.setYearMonth(yearMonth);
			monthlyLM042RBCId.setLoanType(String.valueOf(i));
			monthlyLM042RBCId.setLoanItem("B");
			monthlyLM042RBCId.setRelatedCode("N");
			mMonthlyLM042RBC.setMonthlyLM042RBCId(monthlyLM042RBCId);
			mMonthlyLM042RBC.setRiskFactor(new BigDecimal("0.0000"));
			mMonthlyLM042RBC.setLoanAmount(new BigDecimal("0"));
			insMonthlyLM042RBC.add(mMonthlyLM042RBC);

			mMonthlyLM042RBC = new MonthlyLM042RBC();
			monthlyLM042RBCId = new MonthlyLM042RBCId();
			monthlyLM042RBCId.setYearMonth(yearMonth);
			monthlyLM042RBCId.setLoanType(String.valueOf(i));
			monthlyLM042RBCId.setLoanItem("C");
			monthlyLM042RBCId.setRelatedCode("N");
			mMonthlyLM042RBC.setMonthlyLM042RBCId(monthlyLM042RBCId);
			mMonthlyLM042RBC.setRiskFactor(new BigDecimal("0.0000"));
			mMonthlyLM042RBC.setLoanAmount(new BigDecimal("0"));
			insMonthlyLM042RBC.add(mMonthlyLM042RBC);

			mMonthlyLM042RBC = new MonthlyLM042RBC();
			monthlyLM042RBCId = new MonthlyLM042RBCId();
			monthlyLM042RBCId.setYearMonth(yearMonth);
			monthlyLM042RBCId.setLoanType(String.valueOf(i));
			monthlyLM042RBCId.setLoanItem("D");
			monthlyLM042RBCId.setRelatedCode("N");
			mMonthlyLM042RBC.setMonthlyLM042RBCId(monthlyLM042RBCId);
			mMonthlyLM042RBC.setRiskFactor(new BigDecimal("0.0000"));
			mMonthlyLM042RBC.setLoanAmount(new BigDecimal("0"));
			insMonthlyLM042RBC.add(mMonthlyLM042RBC);

			mMonthlyLM042RBC = new MonthlyLM042RBC();
			monthlyLM042RBCId = new MonthlyLM042RBCId();
			monthlyLM042RBCId.setYearMonth(yearMonth);
			monthlyLM042RBCId.setLoanType(String.valueOf(i));
			monthlyLM042RBCId.setLoanItem("E");
			monthlyLM042RBCId.setRelatedCode("Y");
			mMonthlyLM042RBC.setMonthlyLM042RBCId(monthlyLM042RBCId);
			mMonthlyLM042RBC.setRiskFactor(new BigDecimal("0.0000"));
			mMonthlyLM042RBC.setLoanAmount(new BigDecimal("0"));
			insMonthlyLM042RBC.add(mMonthlyLM042RBC);

			mMonthlyLM042RBC = new MonthlyLM042RBC();
			monthlyLM042RBCId = new MonthlyLM042RBCId();
			monthlyLM042RBCId.setYearMonth(yearMonth);
			monthlyLM042RBCId.setLoanType(String.valueOf(i));
			monthlyLM042RBCId.setLoanItem("F");
			monthlyLM042RBCId.setRelatedCode("Y");
			mMonthlyLM042RBC.setMonthlyLM042RBCId(monthlyLM042RBCId);
			mMonthlyLM042RBC.setRiskFactor(new BigDecimal("0.0000"));
			mMonthlyLM042RBC.setLoanAmount(new BigDecimal("0"));
			insMonthlyLM042RBC.add(mMonthlyLM042RBC);
		}

		try {

			sMonthlyLM042RBCService.insertAll(insMonthlyLM042RBC, titaVo);
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}