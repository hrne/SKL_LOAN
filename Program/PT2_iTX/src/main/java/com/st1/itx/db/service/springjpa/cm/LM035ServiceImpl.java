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
public class LM035ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo, List<Integer> yearMonthList) throws Exception {

		String tmp = "";

		for (int i = 0; i < yearMonthList.size(); i++) {
			this.info(i + " = " + yearMonthList.get(i));
			tmp = tmp + yearMonthList.get(i) + ",";
		}

		tmp = tmp.substring(0, tmp.length() - 1);
		this.info("yearMonthList = " + tmp);

		String sql = "SELECT S1.\"YearMonth\"";
		sql += "			,DECODE(S1.\"CityCode\", '85', '96', S1.\"CityCode\") AS \"CityCode\"";
		sql += "            ,S1.\"CityItem\" AS \"CityItem\"";
		sql += "            ,ROUND((SUM(S1.\"LoanBal\") + SUM(S1.\"ColBal\") + SUM(S1.\"OvduBal\")), 2) AS \"LoanBal\"";
		sql += "            ,ROUND(SUM(S1.\"OvduBal\"), 2) AS \"OvduBal\"";
		sql += "            ,ROUND(SUM(S1.\"ColBal\"), 2) AS \"ColBal\"";
		sql += "            ,CASE WHEN SUM(S1.\"LoanBal\") + SUM(S1.\"ColBal\") > 0";
		sql += "                  THEN ROUND((SUM(S1.\"OvduBal\") + SUM(S1.\"ColBal\")) / (SUM(S1.\"LoanBal\") + SUM(S1.\"ColBal\")), 4)";
		sql += "             ELSE 0 END AS \"Ratio\"";
		sql += "      FROM (SELECT M.\"YearMonth\"";
		sql += "				  ,F.\"CityCode\" AS \"CityCode\"";
		sql += "                  ,C.\"CityItem\" AS \"CityItem\"";
		sql += "                  ,SUM(CASE WHEN F.\"Status\" = '0' AND F.\"OvduTerm\" < 3";
		sql += "                            THEN M.\"LoanBalance\"";
		sql += "                       ELSE 0 END) AS \"LoanBal\"";
		sql += "                  ,SUM(CASE WHEN F.\"Status\" = '0' AND F.\"OvduTerm\" >= 3";
		sql += "                            THEN M.\"LoanBalance\"";
		sql += "                       ELSE 0 END) AS \"OvduBal\"";
		sql += "                  ,SUM(CASE WHEN M.\"AcctCode\" = '990'";
		sql += "                            THEN M.\"LoanBalance\"";
		sql += "                       ELSE 0 END) AS \"ColBal\"";
		sql += "            FROM \"MonthlyLoanBal\" M";
		sql += "            LEFT JOIN \"MonthlyFacBal\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "                                         AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                         AND F.\"YearMonth\" = M.\"YearMonth\"";
		sql += "            LEFT JOIN \"CdCity\" C ON C.\"CityCode\" = F.\"CityCode\"";
		sql += "            WHERE  M.\"YearMonth\" IN (" + tmp + ")";
		sql += "              AND DECODE(M.\"DepartmentCode\", '1', 1, 0) <> 1";
		sql += "              AND M.\"ClCode1\" IN (1, 2)";
		sql += "              AND TO_NUMBER(C.\"CityCode\") < 96";
		sql += "            GROUP BY M.\"YearMonth\", F.\"CityCode\",  C.\"CityItem\"";
		for (int i = 0; i < yearMonthList.size(); i++) {
			sql += "            UNION ALL";
			sql += "            SELECT " + yearMonthList.get(i) + " AS \"YearMonth\"";
			sql += "				  ,C.\"CityCode\" AS \"CityCode\"";
			sql += "                  ,C.\"CityItem\" AS \"CityItem\"";
			sql += "                  ,0 AS \"LoanBal\"";
			sql += "                  ,0 AS \"OvduBal\"";
			sql += "                  ,0 AS \"ColBal\"";
			sql += "            FROM \"CdCity\" C";
			sql += "            WHERE TO_NUMBER(C.\"CityCode\") < 96";

		}
		sql += " 	  	) S1";
		sql += "      GROUP BY S1.\"YearMonth\",S1.\"CityCode\", S1.\"CityItem\"";
		sql += "      ORDER BY S1.\"YearMonth\" ASC, \"CityCode\" ASC";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		return this.convertToMap(query);
	}

}