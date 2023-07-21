package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service("LY004ServiceImpl")
@Repository
public class LY004ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int inputYearMonth, TitaVo titaVo) throws Exception {
		this.info("LY004ServiceImpl findAll ");

		// 同年的11月
		int lastInputYearMonth = inputYearMonth - 1;

		String sql = "";
		sql += " WITH \"OvduAmt\" AS (";
		sql += " 	SELECT SUM(\"LoanBal\") AS \"OvduAmt\" ";
		sql += " 	FROM (";
		sql += " 		SELECT SUM(\"PrinBalance\") AS \"LoanBal\"";
		sql += " 		FROM \"MonthlyFacBal\"";
		sql += " 		WHERE \"YearMonth\" = :inputYearMonth ";
		sql += " 		  AND (( \"OvduTerm\" BETWEEN 3 AND 6 ";
		sql += " 			 AND \"AcctCode\" <> 990 ) OR \"AcctCode\" = 990 )";
		sql += " 		UNION";
		sql += " 		SELECT \"LoanBal\" AS \"LoanBal\"";
		sql += " 		FROM \"MonthlyLM052AssetClass\"";
		sql += " 		WHERE \"YearMonth\" = :inputYearMonth ";
		sql += " 		  AND \"AssetClassNo\" IN ('62')";
		sql += " 	)";
		sql += " ) , \"LastOvduAmt\" AS (";
		sql += " 	SELECT SUM(\"LoanBal\") AS \"OvduAmt\" ";
		sql += " 	FROM (";
		sql += " 		SELECT SUM(\"PrinBalance\") AS \"LoanBal\"";
		sql += " 		FROM \"MonthlyFacBal\"";
		sql += " 		WHERE \"YearMonth\" = :lastinputYearMonth ";
		sql += " 		  AND (( \"OvduTerm\" BETWEEN 3 AND 6 ";
		sql += " 			 AND \"AcctCode\" <> 990 ) OR \"AcctCode\" = 990 )";
		sql += " 		UNION";
		sql += " 		SELECT \"LoanBal\" AS \"LoanBal\"";
		sql += " 		FROM \"MonthlyLM052AssetClass\"";
		sql += " 		WHERE \"YearMonth\" = :lastinputYearMonth ";
		sql += " 		  AND \"AssetClassNo\" IN ('62')";
		sql += " 	)";
		sql += " ), \"LoanAmt\" AS (";
		sql += " 	SELECT SUM(\"LoanBal\") AS \"LoanBal\" ";
		sql += " 	FROM (";
		sql += " 		SELECT SUM(\"PrinBalance\") AS \"LoanBal\"";
		sql += " 		FROM \"MonthlyFacBal\"";
		sql += " 		WHERE \"YearMonth\" = :inputYearMonth ";
		sql += " 		UNION";
		sql += " 		SELECT \"LoanBal\" AS \"LoanBal\"";
		sql += " 		FROM \"MonthlyLM052AssetClass\"";
		sql += " 		WHERE \"YearMonth\" = :inputYearMonth ";
		sql += " 		  AND \"AssetClassNo\" IN ('61','62')";
		sql += " 	)";
		sql += " ), \"LastLoanAmt\" AS (";
		sql += " 	SELECT SUM(\"LoanBal\") AS \"LoanBal\" ";
		sql += " 	FROM (";
		sql += " 		SELECT SUM(\"PrinBalance\") AS \"LoanBal\"";
		sql += " 		FROM \"MonthlyFacBal\"";
		sql += " 		WHERE \"YearMonth\" = :lastinputYearMonth ";
		sql += " 		UNION";
		sql += " 		SELECT \"LoanBal\" AS \"LoanBal\"";
		sql += " 		FROM \"MonthlyLM052AssetClass\"";
		sql += " 		WHERE \"YearMonth\" = :lastinputYearMonth ";
		sql += " 		  AND \"AssetClassNo\" IN ('61','62')";
		sql += " 	)";
		sql += " )";
		sql += " SELECT NVL(O.\"OvduAmt\",0)                          AS \"OvduAmt\" "; // 逾放金額：本期末餘額
		sql += "       ,NVL(O.\"OvduAmt\",0) - NVL(LO.\"OvduAmt\",0)  AS \"DiffOvduAmt\" "; // 逾放金額：較上期末增減金額
		sql += "       ,NVL(L.\"LoanBal\",0)                          AS \"LoanBal\" "; // 放款總額：本期末餘額
		sql += "       ,NVL(L.\"LoanBal\",0) - NVL(LL.\"LoanBal\",0)  AS \"DiffLoanBal\" "; // 放款總額：較上期末增減餘額
		sql += "       ,CASE ";
		sql += "          WHEN NVL(L.\"LoanBal\",0) > 0 ";
		sql += "          THEN NVL(O.\"OvduAmt\",0) / L.\"LoanBal\" ";
		sql += "        ELSE 0 END                                AS \"Ratio\" "; // 逾放比率(%)
		sql += " FROM \"OvduAmt\"  O "; // 取本月放款總額
		sql += " 	, \"LastOvduAmt\" LO"; // 取上月放款總額
		sql += " 	, \"LoanAmt\" L"; // 取本月逾放總額
		sql += " 	, \"LastLoanAmt\" LL"; // 取上月逾放總額

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("inputYearMonth", inputYearMonth);
		query.setParameter("lastinputYearMonth", lastInputYearMonth);

		return this.convertToMap(query);
	}

}