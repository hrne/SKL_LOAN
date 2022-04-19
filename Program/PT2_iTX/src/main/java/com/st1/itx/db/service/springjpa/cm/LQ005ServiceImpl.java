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

@Service
@Repository
public class LQ005ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	public List<Map<String, String>> findAll(int inputYearMonthStart, int inputYearMonthEnd, TitaVo titaVo) {
		this.info("lQ005.findAll ");
		this.info("lQ005.inputYearMonthStart = " + inputYearMonthStart);
		this.info("lQ005.inputYearMonthEnd = " + inputYearMonthEnd);

		String sql = "";
		sql += " SELECT CM.\"CustId\" "; // F0 資金或信用收受者-統編
		sql += "      , CM.\"CustName\" "; // F1 資金或信用收受者-姓名
		sql += "      , NVL(S1.\"LoanBal\",0) AS \"HighestLoanBal\" "; // F2 當季最高餘額
		sql += "      , NVL(S2.\"LoanBal\",0) AS \"LoanBal\" "; // F3 季底交易帳列餘額
		sql += " FROM ( SELECT TO_CHAR(\"CusId\") AS \"RptId\" ";
		sql += "        FROM \"RptRelationSelf\" ";
		sql += "        WHERE \"LAW001\" = '1' ";
		sql += "        UNION ";
		sql += "        SELECT TO_CHAR(\"RlbID\") AS \"RptId\" ";
		sql += "        FROM \"RptRelationFamily\" ";
		sql += "        WHERE \"LAW001\" = '1' ";
		sql += "        UNION ";
		sql += "        SELECT TO_CHAR(\"ComNo\") AS \"RptId\" ";
		sql += "        FROM \"RptRelationCompany\" ";
		sql += "        WHERE \"LAW001\" = '1' ";
		sql += "      ) S0 ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustId\" = S0.\"RptId\" ";
		sql += " LEFT JOIN ( SELECT S.\"CustNo\"  ";
		sql += "                  , S.\"LoanBal\" ";
		sql += "             FROM ( SELECT S.\"CustNo\" ";
		sql += "                         , S.\"LoanBal\" ";
		sql += "                         , ROW_NUMBER() OVER (PARTITION BY S.\"CustNo\" ";
		sql += "                                              ORDER BY S.\"LoanBal\" DESC ";
		sql += "                                             ) AS \"Seq\" ";
		sql += "                    FROM ( SELECT \"CustNo\" ";
		sql += "                                , SUM(\"LoanBalance\") AS \"LoanBal\" ";
		sql += "                           FROM \"MonthlyLoanBal\" ";
		sql += "                           Where \"YearMonth\" >= :inputYearMonthStart ";
		sql += "                             AND \"YearMonth\" <= :inputYearMonthEnd ";
		sql += "                           GROUP BY \"CustNo\" ";
		sql += "                                  , \"YearMonth\" ";
		sql += "                         ) S ";
		sql += "                  ) S ";
		sql += "            WHERE S.\"Seq\" = 1 ";
		sql += "              AND S.\"LoanBal\" > 0 ";
		sql += "           ) S1 ON S1.\"CustNo\" = CM.\"CustNo\" ";
		sql += " LEFT JOIN ( SELECT \"CustNo\"  ";
		sql += "                  , SUM(\"LoanBalance\") AS \"LoanBal\" ";
		sql += "             FROM \"MonthlyLoanBal\" ";
		sql += "             WHERE \"YearMonth\" = :inputYearMonthEnd ";
		sql += "             GROUP BY \"CustNo\" ";
		sql += "           ) S2 ON S2.\"CustNo\" = CM.\"CustNo\" ";
		sql += " LEFT JOIN ( SELECT MLB.\"YearMonth\" ";
		sql += "                  , SUM(MLB.\"LoanBalance\") AS \"LoanBalTotal\"";
		sql += "             FROM \"MonthlyLoanBal\" MLB ";
		sql += "             LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = MLB.\"CustNo\" ";
		sql += "             LEFT JOIN ( SELECT TO_CHAR(\"CusId\") AS \"RptId\" ";
		sql += "                         FROM \"RptRelationSelf\" ";
		sql += "                         WHERE \"LAW001\" = '1' ";
		sql += "                         UNION ";
		sql += "                         SELECT TO_CHAR(\"RlbID\") AS \"RptId\" ";
		sql += "                         FROM \"RptRelationFamily\" ";
		sql += "                         WHERE \"LAW001\" = '1' ";
		sql += "                         UNION ";
		sql += "                         SELECT TO_CHAR(\"ComNo\") AS \"RptId\" ";
		sql += "                         FROM \"RptRelationCompany\" ";
		sql += "                         WHERE \"LAW001\" = '1' ";
		sql += "                       ) REL ON REL.\"RptId\" = CM.\"CustId\" ";
		sql += "             WHERE NVL(REL.\"RptId\",' ') != ' ' ";
		sql += "               AND MLB.\"YearMonth\" = :inputYearMonthEnd ";
		sql += "             GROUP BY MLB.\"YearMonth\" ";
		sql += "           ) S4 ON S4.\"YearMonth\" = :inputYearMonthEnd ";
		sql += " WHERE CASE ";
		sql += "         WHEN NVL(S4.\"LoanBalTotal\",0) > 10000000 ";
		sql += "         THEN 1 ";
		sql += "         WHEN NVL(S1.\"LoanBal\",0) > 50000000 ";
		sql += "         THEN 1 ";
		sql += "         WHEN NVL(S2.\"LoanBal\",0) > 10000000 ";
		sql += "         THEN 1 ";
		sql += "       ELSE 0 END > 0 ";
		sql += " ORDER BY CM.\"CustId\" ";
		;
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputYearMonthStart", inputYearMonthStart);
		query.setParameter("inputYearMonthEnd", inputYearMonthEnd);

		return this.convertToMap(query);
	}

}