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
/* 逾期放款明細 */
public class LM052ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}
	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * @param lastYM 上西元年月
	 * @param formNum 表格次序
	 * 
	 */
	
	public List<Map<String, String>> findAll(TitaVo titaVo,int yearMonth,int lastYM, int formNum) throws Exception {
		this.info("lM052.findAll");

		String iYearMonth = String.valueOf(yearMonth);

		this.info("yymm=" + iYearMonth + ",lyymm=" + lastYM);

		String sql = " ";
		
		if (formNum == 1) {

			sql += "WITH rawData AS ( ";
			sql += "      SELECT SUM(CASE WHEN I.\"YearMonth\" = :yymm ";
			sql += "                 THEN NVL(I.\"AccumDPAmortized\", 0)";
			sql += "                 ELSE 0 END";
			sql += "                )";
			sql += "             -";
			sql += "             SUM(CASE WHEN I.\"YearMonth\" = :lyymm";
			sql += "                 THEN NVL(I.\"AccumDPAmortized\", 0)";
			sql += "                 ELSE 0 END";
			sql += "                )";
			sql += "             AS \"LnAmt\"";
			sql += "      FROM \"Ias39IntMethod\" I";
			sql += "      LEFT JOIN \"MonthlyLoanBal\" MLB ON I.\"YearMonth\" = MLB.\"YearMonth\" ";
			sql += "                                      AND I.\"CustNo\" = MLB.\"CustNo\" ";
			sql += "                                      AND I.\"FacmNo\" = MLB.\"FacmNo\" ";
			sql += "                                      AND I.\"BormNo\" = MLB.\"BormNo\"";
			sql += "      WHERE NVL(I.\"YearMonth\", ' ') IN (:lyymm, :yymm) ";
			sql += "        AND NVL(MLB.\"CurrencyCode\",' ') = 'TWD'";
			sql += "        AND MLB.\"AcctCode\" <> 990 ";
			sql += "      GROUP BY DECODE(NVL(MLB.\"AcctCode\", ' '), '990', '990', 'OTHER') ";
			sql += "      ),";
			sql += "      roundData AS (";
			sql += "      SELECT CASE WHEN \"LnAmt\" < 0";
			sql += "                  THEN CASE WHEN REPLACE(REGEXP_SUBSTR(\"LnAmt\", '\\.\\d'), '.', '') >= 5 THEN TRUNC(\"LnAmt\")+1 ";
			sql += "                            WHEN REPLACE(REGEXP_SUBSTR(\"LnAmt\", '\\.\\d'), '.', '') BETWEEN 0 AND 4 THEN TRUNC(\"LnAmt\")-1";
			sql += "                            ELSE 0 END ";
			sql += "                  WHEN \"LnAmt\" > 0";
			sql += "                  THEN ROUND(\"LnAmt\")";
			sql += "                  ELSE 0 END ";
			sql += "             AS \"LnAmt\"";
			sql += "      FROM rawData";
			sql += "      )";
			sql += "	SELECT \"AssetClassNo\"";
			sql += "          ,\"AcSubBookCode\"";
			sql += "          ,\"LoanBal\"";
			sql += "    FROM \"MonthlyLM052AssetClass\"";
			sql += "    WHERE \"YearMonth\" = :yymm ";
			sql += "    UNION ";			
			sql += "    SELECT '61' AS \"AssetClassNo\" ";
			sql += "      	  ,'999' AS \"AcSubBookCode\" ";
			sql += "          ,CASE WHEN R.\"LnAmt\" >= 0 ";
			sql += "                THEN R.\"LnAmt\" ";
			sql += "                ELSE ABS(R.\"LnAmt\") END AS \"LoanBal\"";
			sql += "    FROM roundData R";

			
		} else if (formNum == 2) {

			// 此年月為上個月
			iYearMonth = String.valueOf(lastYM);

			sql += "	SELECT DECODE(\"AssetClassNo\",'11','1','12','1',\"AssetClassNo\") AS \"AssetClassNo\"";
			sql += "          ,SUM(\"LoanBal\") AS \"LoanBal\"";
			sql += "    FROM \"MonthlyLM052AssetClass\"";
			sql += "    WHERE \"YearMonth\" = :yymm ";
			sql += "    GROUP BY DECODE(\"AssetClassNo\",'11','1','12','1',\"AssetClassNo\")";
			sql += "    ORDER BY \"AssetClassNo\"";

		} else if (formNum == 3) {

			sql += "	SELECT \"OvduNo\"";
			sql += "          ,\"AcctCode\"";
			sql += "          ,\"LoanBal\"";
			sql += "    FROM \"MonthlyLM052Ovdu\"";
			sql += "    WHERE \"YearMonth\" = :yymm ";
			sql += "      AND \"OvduNo\" IN ('1','2','3')";
			sql += "    ORDER BY \"OvduNo\"";
			sql += "   			,\"AcctCode\"";

		} else if (formNum == 4) {

			sql += "	SELECT \"LoanAssetCode\"";
			sql += "          ,\"LoanBal\"";
			sql += "    FROM \"MonthlyLM052LoanAsset\"";
			sql += "    WHERE \"YearMonth\" = :yymm ";
			sql += "    ORDER BY \"LoanAssetCode\"";

		} else if (formNum == 5) {
			
			sql += "	SELECT \"YearMonth\" ";
			sql += "          ,\"AssetEvaTotal\" "; //--五類資產評估合計
			sql += "          ,\"LegalLoss\"";		//--法定備抵損失提撥
			sql += "          ,\"ApprovedLoss\"";   //--會計部核定備抵損失
			sql += "    FROM \"MonthlyLM052Loss\"";
			sql += "    WHERE \"YearMonth\" = :yymm ";

		}

		this.info("sql" + formNum + "=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		if (formNum == 1) {
			query.setParameter("lyymm", lastYM);
		}
		query.setParameter("yymm", iYearMonth);
		return this.convertToMap(query);
	}

}