package com.st1.itx.db.service.springjpa.cm;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service
@Repository
public class L9719ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L9719ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		logger.info("l9719.findAll ");
		
		LocalDate inputYearMonth = LocalDate.of(Integer.parseInt(titaVo.getParam("inputYear")) + 1911, Integer.parseInt(titaVo.getParam("inputMonth")), 1);
		LocalDate inputlastYearMonth = inputYearMonth.minusMonths(1);

		String sql = "WITH rawData AS ( ";
		sql += "      SELECT DECODE(NVL(MLB.\"AcctCode\", ' '), '990', '990', 'OTHER') AS \"AcctCode\"";
		sql += "            ,SUM(CASE WHEN DECODE(NVL(MLB.\"AcctCode\", ' '), '990', '990', 'OTHER') = '990' ";
		sql += "                       AND I.\"YearMonth\" = :inputYearMonth ";
		sql += "                 THEN NVL(I.\"AccumDPAmortized\", 0)";
		sql += "                 ELSE 0 END";
		sql += "                )";
		sql += "             -";
		sql += "             SUM(CASE WHEN DECODE(NVL(MLB.\"AcctCode\", ' '), '990', '990', 'OTHER') = '990' ";
		sql += "                       AND I.\"YearMonth\" = :lastYearMonth";
		sql += "                 THEN NVL(I.\"AccumDPAmortized\", 0)";
		sql += "                 ELSE 0 END";
		sql += "                )";
		sql += "             AS \"OvAmt\"";
		sql += "            ,SUM(CASE WHEN DECODE(NVL(MLB.\"AcctCode\", ' '), '990', '990', 'OTHER') = 'OTHER' ";
		sql += "                       AND I.\"YearMonth\" = :inputYearMonth ";
		sql += "                 THEN NVL(I.\"AccumDPAmortized\", 0)";
		sql += "                 ELSE 0 END";
		sql += "                )";
		sql += "             -";
		sql += "             SUM(CASE WHEN DECODE(NVL(MLB.\"AcctCode\", ' '), '990', '990', 'OTHER') = 'OTHER' ";
		sql += "                       AND I.\"YearMonth\" = :lastYearMonth";
		sql += "                 THEN NVL(I.\"AccumDPAmortized\", 0)";
		sql += "                 ELSE 0 END";
		sql += "                )";
		sql += "             AS \"LnAmt\"";
		sql += "      FROM \"Ias39IntMethod\" I";
		sql += "      LEFT JOIN \"MonthlyLoanBal\" MLB ON I.\"YearMonth\" = MLB.\"YearMonth\" ";
		sql += "                                      AND I.\"CustNo\" = MLB.\"CustNo\" ";
		sql += "                                      AND I.\"FacmNo\" = MLB.\"FacmNo\" ";
		sql += "                                      AND I.\"BormNo\" = MLB.\"BormNo\"";
		sql += "      WHERE NVL(I.\"YearMonth\", ' ') IN (:lastYearMonth, :inputYearMonth) ";
		sql += "        AND NVL(MLB.\"CurrencyCode\",' ') = 'TWD'";
		sql += "      GROUP BY DECODE(NVL(MLB.\"AcctCode\", ' '), '990', '990', 'OTHER') ";
		sql += "      ),";
		sql += "      roundData AS (";
		sql += "      SELECT \"AcctCode\"";
		sql += "            ,CASE WHEN \"LnAmt\" < 0";
		sql += "                  THEN CASE WHEN REPLACE(REGEXP_SUBSTR(\"LnAmt\", '\\.\\d'), '.', '') >= 5 THEN TRUNC(\"LnAmt\")+1 ";
		sql += "                            WHEN REPLACE(REGEXP_SUBSTR(\"LnAmt\", '\\.\\d'), '.', '') BETWEEN 0 AND 4 THEN TRUNC(\"LnAmt\")-1";
		sql += "                            ELSE 0 END ";
		sql += "                  WHEN \"LnAmt\" > 0";
		sql += "                  THEN ROUND(\"LnAmt\")";
		sql += "                  ELSE 0 END ";
		sql += "             AS \"LnAmt\"";
		sql += "            ,CASE WHEN \"OvAmt\" < 0";
		sql += "                  THEN CASE WHEN REPLACE(REGEXP_SUBSTR(\"OvAmt\", '\\.\\d'), '.', '') >= 5 THEN TRUNC(\"OvAmt\")+1 ";
		sql += "                            WHEN REPLACE(REGEXP_SUBSTR(\"OvAmt\", '\\.\\d'), '.', '') BETWEEN 0 AND 4 THEN TRUNC(\"OvAmt\")-1";
		sql += "                            ELSE 0 END ";
		sql += "                  WHEN \"OvAmt\" > 0";
		sql += "                  THEN ROUND(\"OvAmt\")";
		sql += "                  ELSE 0 END ";
		sql += "             AS \"OvAmt\"";
		sql += "      FROM rawData";
		sql += "      ),";
		sql += "      totalData AS (";
		sql += "      SELECT CASE WHEN NVL(MLB.\"AcctCode\",' ') = '990' THEN 'OV'";
		sql += "                  ELSE 'LN' END ";
		sql += "             AS \"AcctCode\"";
		sql += "            ,ROUND(SUM(NVL(i.\"AccumDPAmortized\", 0))) AS \"AccumDPAmortized\"";
		sql += "      FROM \"Ias39IntMethod\" i";
		sql += "      LEFT JOIN \"MonthlyLoanBal\" MLB ON I.\"YearMonth\" = MLB.\"YearMonth\" ";
		sql += "                                      AND I.\"CustNo\" = MLB.\"CustNo\" ";
		sql += "                                      AND I.\"FacmNo\" = MLB.\"FacmNo\" ";
		sql += "                                      AND I.\"BormNo\" = MLB.\"BormNo\"";
		sql += "      WHERE i.\"YearMonth\" = :inputYearMonth ";
		sql += "      GROUP BY CASE WHEN NVL(MLB.\"AcctCode\",' ') = '990' ";
		sql += "                    THEN 'OV'";
		sql += "                    ELSE 'LN' END";
		sql += "      )";
		sql += "      SELECT CASE WHEN Loan.\"LnAmt\" >= 0 ";
		sql += "                  THEN Loan.\"LnAmt\" ";
		sql += "                  ELSE 0 END ";
		sql += "             AS \"LnAmt-Bor\"";
		sql += "            ,CASE WHEN Loan.\"LnAmt\" < 0 ";
		sql += "                  THEN ABS(Loan.\"LnAmt\") ";
		sql += "                  ELSE 0 END ";
		sql += "             AS \"LnAmt-Loan\"";
		sql += "      	    ,CodeAIL.\"AcNoCode\" AS \"LnAmt-AcctCode\"";
		sql += "      	    ,CodeAIL.\"AcNoItem\" AS \"LnAmt-AcctItem\"";
		sql += "            ,CASE WHEN Ovdu.\"OvAmt\" >= 0 ";
		sql += "                  THEN Ovdu.\"OvAmt\" ";
		sql += "                  ELSE 0 END ";
		sql += "             AS \"OvAmt-Bor\"";
		sql += "            ,CASE WHEN Ovdu.\"OvAmt\" < 0 ";
		sql += "                  THEN ABS(Ovdu.\"OvAmt\") ";
		sql += "                  ELSE 0 END ";
		sql += "             AS \"OvAmt-Loan\"";
		sql += "      	    ,CodeAIO.\"AcNoCode\" AS \"OvAmt-AcctCode\"";
		sql += "      	    ,CodeAIO.\"AcNoItem\" AS \"OvAmt-AcctItem\"";
		sql += "            ,CASE WHEN Loan.\"LnAmt\" - Ovdu.\"OvAmt\" < 0 ";
		sql += "                  THEN ABS(Loan.\"LnAmt\" + Ovdu.\"OvAmt\") ";
		sql += "                  ELSE 0 END ";
		sql += "             AS \"IntAmt-Bor\"";
		sql += "            ,CASE WHEN Loan.\"LnAmt\" - Ovdu.\"OvAmt\" >= 0 ";
		sql += "                  THEN Loan.\"LnAmt\" + Ovdu.\"OvAmt\" ";
		sql += "                  ELSE 0 END ";
		sql += "             AS \"IntAmt-Loan\"";
		sql += "      	    ,CodeAII.\"AcNoCode\" AS \"IntAmt-AcctCode\"";
		sql += "            ,CodeAII.\"AcNoItem\" AS \"IntAmt-AcctItem\"";
		sql += "            ,TotalA.\"AccumDPAmortized\" AS \"OvduAccum\"";
		sql += "            ,TotalB.\"AccumDPAmortized\" AS \"LoanAccum\"";
		sql += "      	    ,SUBSTR(:inputYearMonth, 1, 4)";
		sql += "      	     || '年'";
		sql += "      	     || SUBSTR(:inputYearMonth, 5, 2)";
		sql += "      	     || '月折溢價攤銷' AS \"Description\"";
		sql += "      FROM roundData Ovdu";
		sql += "      LEFT JOIN roundData Loan   ON Loan.\"AcctCode\"   = 'OTHER'";
		sql += "      LEFT JOIN totalData TotalA ON TotalA.\"AcctCode\" = 'OV'";
		sql += "      LEFT JOIN totalData TotalB ON TotalB.\"AcctCode\" = 'LN'";
		sql += "      LEFT JOIN \"CdAcCode\" CodeAIL ON CodeAIL.\"AcctCode\" = 'AIL'";
		sql += "      LEFT JOIN \"CdAcCode\" CodeAIO ON CodeAIO.\"AcctCode\" = 'AIO'";
		sql += "      LEFT JOIN \"CdAcCode\" CodeAII ON CodeAII.\"AcctCode\" = 'AII'";
		sql += "      WHERE Ovdu.\"AcctCode\" = '990'";

		logger.info("sql=" + sql);
		
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		
		Query query;
		query = em.createNativeQuery(sql);
		
		query.setParameter("inputYearMonth", Integer.toString(inputYearMonth.getYear()) + String.format("%02d",inputYearMonth.getMonthValue()));
		query.setParameter("lastYearMonth", Integer.toString(inputlastYearMonth.getYear()) + String.format("%02d",inputlastYearMonth.getMonthValue()));
		
		return this.convertToMap(query.getResultList());
	}

}