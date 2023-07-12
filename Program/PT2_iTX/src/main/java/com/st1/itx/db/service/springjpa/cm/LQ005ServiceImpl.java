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
import com.st1.itx.util.parse.Parse;

@Service
@Repository
public class LQ005ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	/**
	 * 查詢
	 * 
	 * @param yearMonth 西元年月
	 * @param titaVo
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> findAll(int yearMonth, TitaVo titaVo) throws Exception {
		this.info("LQ005ServiceImpl.findAll.. ");

		this.info("yearMonth =" + yearMonth);

		int yy = yearMonth / 100;
		int mm = yearMonth % 100;
		int lYearMonth = 0;
		switch (mm) {
		case 1:
		case 2:
		case 3:
			lYearMonth = (yy - 1) * 100 + 12;
			break;
		case 4:
		case 5:
		case 6:
			lYearMonth = yy * 100 + 3;
			break;
		case 7:
		case 8:
		case 9:
			lYearMonth = yy * 100 + 6;
			break;
		case 10:
		case 11:
		case 12:
			lYearMonth = yy * 100 + 9;
			break;
		}
		this.info("lYearMonth =" + lYearMonth);
		String sql = "	";
		sql += "  WITH \"Main\" AS (";
		sql += "      SELECT F.\"Id\"";
		sql += "           , F.\"Name\"";
		sql += "           , F.\"LoanBalance\"";
		sql += "           , F.\"CompanyName\"";
		sql += "           , F.\"AcDate\"";
		sql += "           , CASE";
		sql += "          WHEN \"CompanyName\" LIKE '新%金%'";
		sql += "               AND";
		sql += "               Length(\"CompanyName\") = 4 THEN 1";
		sql += "          WHEN \"CompanyName\" LIKE '新%人%' THEN 2";
		sql += "          WHEN \"CompanyName\" LIKE '新%證%' THEN 3";
		sql += "          WHEN \"CompanyName\" LIKE '新%保%' THEN 4";
		sql += "          WHEN \"CompanyName\" LIKE '新%銀%' THEN 5";
		sql += "          ELSE 9";
		sql += "      END \"Seq\"";
		sql += "      FROM \"FinHoldRel\" F";
		sql += "      WHERE Trunc(F.\"AcDate\" / 100) = :yymm ";
		sql += "  ), \"tmpMain\" AS (";
		sql += "      SELECT \"Id\"";
		sql += "           , MIN(\"Seq\") AS \"Seq\"";
		sql += "      FROM \"Main\"";
		sql += "      GROUP BY \"Id\"";
		sql += "  ), \"lastMain\" AS (";
		sql += "      SELECT Tm.\"Id\"";
		sql += "           , M.\"LoanBalance\"";
		sql += "      FROM \"tmpMain\"   Tm";
		sql += "      LEFT JOIN \"CustMain\"  Cm ON Cm.\"CustId\" = Tm.\"Id\"";
		sql += "      LEFT JOIN (";
		sql += "          SELECT M.\"CustNo\"";
		sql += "               , SUM(M.\"PrinBalance\") AS \"LoanBalance\"";
		sql += "          FROM \"MonthlyFacBal\" M";
		sql += "          WHERE M.\"YearMonth\" = :lyymm ";
		sql += "                AND";
		sql += "                M.\"PrinBalance\" > 0";
		sql += "          GROUP BY M.\"CustNo\"";
		sql += "      ) M ON M.\"CustNo\" = Cm.\"CustNo\"";
		sql += "  )";
		sql += "  SELECT M.\"Id\"";
		sql += "       , M.\"Name\"";
		sql += "       , CASE";
		sql += "     	   WHEN Lm.\"LoanBalance\" >= M.\"LoanBalance\" THEN Lm.\"LoanBalance\"";
		sql += "      	   ELSE M.\"LoanBalance\"";
		sql += "  		 END AS \"MaxLoanBal\"";
		sql += "       , M.\"LoanBalance\" AS \"LoanBal\"";
		sql += "  FROM \"tmpMain\"   Tm";
		sql += "  LEFT JOIN \"Main\" M ON Tm.\"Id\" = M.\"Id\"";
		sql += "                      AND Tm.\"Seq\" = M.\"Seq\"";
		sql += "  LEFT JOIN \"lastMain\"  Lm ON Lm.\"Id\" = Tm.\"Id\"";
		sql += "  WHERE Tm.\"Id\" IS NOT NULL";
		sql += "  ORDER BY M.\"LoanBalance\" DESC";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("yymm", yearMonth);
		query.setParameter("lyymm", lYearMonth);
		return this.convertToMap(query);
	}

}