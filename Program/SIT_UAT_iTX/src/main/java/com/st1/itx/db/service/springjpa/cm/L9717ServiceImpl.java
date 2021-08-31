package com.st1.itx.db.service.springjpa.cm;

import java.time.LocalDate;
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
public class L9717ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public static enum OutputSortBy {
		Agent, Year, LargeAmt_Customer, LargeAmt_Agent
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo, OutputSortBy kind) throws Exception {
		this.info("l9717.findAll ");

		String sql = "WITH fulldata AS ( SELECT m.\"CustNo\"";
		sql += "                        ,m.\"FacmNo\"";
		sql += "                        ,CM.\"CustName\"";
		sql += "                        ,1 AS \"CNT\"";
		sql += "                        ,CASE";
		sql += "                         WHEN m.\"AcctCode\" = '990' ";
		sql += "                         THEN 990";
		sql += "                         ELSE m.\"OvduTerm\"";
		sql += "                         END AS \"OvduTerm\"";
		sql += "                        ,m.\"PrinBalance\"";
		sql += "                        ,fac.\"BusinessOfficer\"";
		sql += "                        ,emp.\"Fullname\" AS \"BusinessOfficerFullName\"";
		sql += "                        ,trunc(fac.\"FirstDrawdownDate\" / 10000) AS \"DrawdownYear\"";
		sql += "                        ,tot.\"PrinBalance\"   AS \"TotalPrinBalance\"";
		sql += "                        ,TO_CHAR(trunc(fac.\"FirstDrawdownDate\" / 10000))  AS \"Pivot\"";
		sql += "                  FROM \"MonthlyFacBal\" m";
		sql += "                  LEFT JOIN \"FacMain\" fac ON fac.\"CustNo\" = m.\"CustNo\"";
		sql += "                                         AND fac.\"FacmNo\" = m.\"FacmNo\"";
		sql += "                  LEFT JOIN ( SELECT \"CustNo\"";
		sql += "                                    ,SUM(\"PrinBalance\") AS \"PrinBalance\"";
		sql += "                              FROM \"MonthlyFacBal\"";
		sql += "                              WHERE \"YearMonth\" = :inputYearMonth";
		sql += "                              GROUP BY \"CustNo\"";
		sql += "                            ) tot ON tot.\"CustNo\" = m.\"CustNo\"";
		sql += "                  LEFT JOIN \"CdEmp\" emp ON emp.\"EmployeeNo\" = fac.\"BusinessOfficer\"";
		sql += "                  LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = m.\"CustNo\"";
		sql += "                  WHERE m.\"YearMonth\" = :inputYearMonth";
		sql += "                    AND (    :inputBusinessOfficer = '999999'";
		sql += "                          OR nvl(fac.\"BusinessOfficer\", ' ') = :inputBusinessOfficer )";
		sql += "                    AND m.\"PrinBalance\" > 0";
		sql += "                    AND ( nvl(m.\"AcctCode\", ' ') = '990'";
		sql += "                          OR ";
		sql += "                          (     m.\"OvduTerm\" >= :inputOverdueTermMin";
		sql += "                            AND m.\"OvduTerm\" <= :inputOverdueTermMax ) )";
		sql += "                    AND fac.\"FirstDrawdownDate\" >= 19810101";
		sql += "                    AND ( (:largeAmountOnly = 'Y' AND tot.\"PrinBalance\" >= 50000000)";
		sql += "                     OR   (:largeAmountOnly = 'N' AND tot.\"PrinBalance\" <  50000000) )";
		sql += "                )";

		if (kind == OutputSortBy.Year) {
			sql += "      SELECT RES.\"Pivot\" AS \"Pivot\"";
			sql += "            ,RES.\"OvduTerm\" AS \"OvduTerm\"";
			sql += "            ,SUM(RES.\"CNT\") AS \"CNT\"";
			sql += "            ,SUM(RES.\"PrinBalance\") AS \"PrinBalance\"";
			sql += "      FROM (";

			sql += "      SELECT f.\"Pivot\" AS \"Pivot\"";
			sql += "            ,f.\"OvduTerm\" AS \"OvduTerm\"";
			sql += "            ,SUM(f.\"CNT\") AS \"CNT\"";
			sql += "            ,SUM(f.\"PrinBalance\") AS \"PrinBalance\"";
			sql += "      FROM fullData f ";

			for (int i = 1; i <= 7; i++) {

				if (i == 7) {
					i = 990;
				}
				sql += "      UNION ALL";
				sql += "      SELECT DISTINCT f.\"Pivot\" AS \"Pivot\"";
				sql += "            ," + i + " AS \"OvduTerm\"";
				sql += "            ,0";
				sql += "            ,0";
				sql += "      FROM fullData f ";

			}

			sql += "      )RES";
			sql += "      GROUP BY RES.\"Pivot\",RES.\"OvduTerm\"";
			sql += "      ORDER BY RES.\"Pivot\",RES.\"OvduTerm\"";

		}

		if (kind == OutputSortBy.Agent || kind == OutputSortBy.LargeAmt_Agent) {
			sql += "      SELECT RES.\"BusinessOfficer\" AS \"BusinessOfficer\"";
			sql += "            ,RES.\"OvduTerm\" AS \"OvduTerm\"";
			sql += "            ,SUM(RES.\"CNT\") AS \"CNT\"";
			sql += "            ,SUM(RES.\"PrinBalance\") AS \"PrinBalance\"";
			sql += "            ,RES.\"BusinessOfficerFullName\" AS \"BusinessOfficerFullName\"";
			sql += "      FROM (";

			sql += "      SELECT f.\"BusinessOfficer\" AS \"BusinessOfficer\"";
			sql += "            ,f.\"BusinessOfficerFullName\" AS \"BusinessOfficerFullName\"";
			sql += "            ,f.\"OvduTerm\" AS \"OvduTerm\"";
			sql += "            ,SUM(f.\"CNT\") AS \"CNT\"";
			sql += "            ,SUM(f.\"PrinBalance\") AS \"PrinBalance\"";
			sql += "      FROM fullData f ";

			for (int i = 1; i <= 7; i++) {

				if (i == 7) {
					i = 990;
				}
				sql += "      UNION ALL";
				sql += "      SELECT DISTINCT f.\"BusinessOfficer\" AS \"BusinessOfficer\"";
				sql += "            ,f.\"BusinessOfficerFullName\" AS \"BusinessOfficerFullName\"";
				sql += "            ," + i + " AS \"OvduTerm\"";
				sql += "            ,0";
				sql += "            ,0";
				sql += "            ,0";
				sql += "      FROM fullData f ";

			}

			sql += "      )RES";
			sql += "      GROUP BY RES.\"BusinessOfficer\",RES.\"BusinessOfficerFullName\",RES.\"OvduTerm\"";
			sql += "      ORDER BY CASE ";
			sql += "      			 WHEN ASCII(SUBSTR(RES.\"BusinessOfficer\",0,2)) < 65 ";
			sql += "      			 THEN ASCII(SUBSTR(RES.\"BusinessOfficer\",0,2)) + 65";
			sql += "      			 WHEN ASCII(SUBSTR(RES.\"BusinessOfficer\",0,2)) >= 65 ";
			sql += "      			 THEN ASCII(SUBSTR(RES.\"BusinessOfficer\",0,2))";
			sql += "      		     ELSE 1";
			sql += " 			   END ASC";
			sql += " 			  ,SUBSTR(RES.\"BusinessOfficer\",0,2) ASC";
			sql += " 			  ,RES.\"OvduTerm\" ASC";

		}

		if (kind == OutputSortBy.LargeAmt_Customer) {
			sql += "      SELECT f.\"BusinessOfficer\" AS \"empCode\"";
			sql += "            ,f.\"BusinessOfficerFullName\" AS \"empName\"";
			sql += "            ,f.\"CustNo\" AS \"CustNo\"";
			sql += "            ,f.\"CustName\" AS \"CustName\"";
			sql += "            ,f.\"OvduTerm\" AS \"OvduTerm\"";
			sql += "            ,SUM(f.\"PrinBalance\") AS \"TotalBalance\"";
			sql += "      FROM fullData f ";
			sql += "      WHERE f.\"TotalPrinBalance\" >= 50000000 ";
			sql += "      GROUP BY f.\"CustNo\",f.\"CustName\",f.\"BusinessOfficer\",f.\"BusinessOfficerFullName\",f.\"OvduTerm\"";
		}

		this.info("sql=" + sql);

		LocalDate lastYearMonth = LocalDate.of(Integer.parseInt(titaVo.getParam("inputYear")) + 1911,
				Integer.parseInt(titaVo.getParam("inputMonth")), 1).minusMonths(1);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("inputYearMonth",
				Integer.toString(lastYearMonth.getYear()) + String.format("%02d", lastYearMonth.getMonthValue()));
		query.setParameter("inputOverdueTermMin", titaVo.getParam("inputOverdueTermMin"));
		query.setParameter("inputOverdueTermMax", titaVo.getParam("inputOverdueTermMax"));
		query.setParameter("inputBusinessOfficer", titaVo.getParam("inputBusinessOfficer"));
		query.setParameter("largeAmountOnly",
				kind == OutputSortBy.LargeAmt_Agent || kind == OutputSortBy.LargeAmt_Customer ? "Y" : "N");

		return this.convertToMap(query.getResultList());
	}

}