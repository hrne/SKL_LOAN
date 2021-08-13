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
public class L9717ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L9717ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}
	
	public static enum OutputSortBy {
		Agent,
		Year,
		LargeAmt_Customer,
		LargeAmt_Agent
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo, OutputSortBy kind) throws Exception {
		logger.info("l9717.findAll ");
		
		String sql = "WITH fulldata AS ( SELECT m.\"CustNo\"";
		sql += "                        ,m.\"FacmNo\"";
		sql += "                        ,CM.\"CustName\"";
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
		sql += "                        ,CASE :outputSortByColumn WHEN 'Year' ";
		sql += "                                                  THEN TO_CHAR(trunc(fac.\"FirstDrawdownDate\" / 10000)) ";
		sql += "                                                  ELSE fac.\"BusinessOfficer\" ";
		sql += "                         END AS \"Pivot\"";
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
		
		if (kind != OutputSortBy.LargeAmt_Customer )
		{
		sql += "      SELECT f.\"Pivot\" AS \"Pivot\"";
		sql += "            ,f.\"BusinessOfficer\"";
		sql += "            ,f.\"BusinessOfficerFullName\"";
		sql += "            ,f.\"DrawdownYear\"";
		sql += "            ,nvl(termi.cnt, 0) \"TermICnt\"";
		sql += "            ,nvl(termi.amt, 0) \"TermIAmt\"";
		sql += "            ,nvl(termii.cnt, 0) \"TermIICnt\"";
		sql += "            ,nvl(termii.amt, 0) \"TermIIAmt\"";
		sql += "            ,nvl(termiii.cnt, 0) \"TermIIICnt\"";
		sql += "            ,nvl(termiii.amt, 0) \"TermIIIAmt\"";
		sql += "            ,nvl(termiv.cnt, 0) \"TermIVCnt\"";
		sql += "            ,nvl(termiv.amt, 0) \"TermIVAmt\"";
		sql += "            ,nvl(termv.cnt, 0) \"TermVCnt\"";
		sql += "            ,nvl(termv.amt, 0) \"TermVAmt\"";
		sql += "            ,nvl(termvi.cnt, 0) \"TermVICnt\"";
		sql += "            ,nvl(termvi.amt, 0) \"TermVIAmt\"";
		sql += "            ,nvl(termOvdu.cnt, 0) \"TermOvduCnt\"";
		sql += "            ,nvl(termOvdu.amt, 0) \"TermOvduAmt\"";
		sql += "            ,NVL(termi.cnt,0) + NVL(termii.cnt,0) + NVL(termiii.cnt,0) + NVL(termiv.cnt,0) + NVL(termv.cnt,0) + NVL(termvi.cnt,0) + NVL(termovdu.cnt,0) as \"totalCnt\"";
		sql += "            ,NVL(termi.amt,0) + NVL(termii.amt,0) + NVL(termiii.amt,0) + NVL(termiv.amt,0) + NVL(termv.amt,0) + NVL(termvi.amt,0) + NVL(termovdu.amt,0) as \"totalAmt\"";
		sql += "      FROM fullData f ";
		sql += "      LEFT JOIN ( SELECT \"Pivot\"";
		sql += "                        ,COUNT(*) AS cnt";
		sql += "                        ,SUM(\"PrinBalance\") AS amt";
		sql += "                  FROM fullData f";
		sql += "                  WHERE f.\"OvduTerm\" = 1 ";
		sql += "                  GROUP BY \"Pivot\"";
		sql += "                ) TermI ON termi.\"Pivot\" = f.\"Pivot\"";
		sql += "      LEFT JOIN ( SELECT \"Pivot\"";
		sql += "                        ,COUNT(*) AS cnt";
		sql += "                        ,SUM(\"PrinBalance\") AS amt";
		sql += "                  FROM fullData f";
		sql += "                  WHERE f.\"OvduTerm\" = 2 ";
		sql += "                  GROUP BY \"Pivot\"";
		sql += "                ) TermII ON termii.\"Pivot\" = f.\"Pivot\"";
		sql += "      LEFT JOIN ( SELECT \"Pivot\"";
		sql += "                        ,COUNT(*) AS cnt";
		sql += "                        ,SUM(\"PrinBalance\") AS amt";
		sql += "                  FROM fullData f";
		sql += "                  WHERE f.\"OvduTerm\" = 3 ";
		sql += "                  GROUP BY \"Pivot\"";
		sql += "                ) TermIII ON termiii.\"Pivot\" = f.\"Pivot\"";
		sql += "      LEFT JOIN ( SELECT \"Pivot\"";
		sql += "                        ,COUNT(*) AS cnt";
		sql += "                        ,SUM(\"PrinBalance\") AS amt";
		sql += "                  FROM fullData f";
		sql += "                  WHERE f.\"OvduTerm\" = 4  ";
		sql += "                  GROUP BY \"Pivot\"";
		sql += "                ) TermIV ON termiii.\"Pivot\" = f.\"Pivot\"";
		sql += "      LEFT JOIN ( SELECT \"Pivot\"";
		sql += "                        ,COUNT(*) AS cnt";
		sql += "                        ,SUM(\"PrinBalance\") AS amt";
		sql += "                  FROM fullData f";
		sql += "                  WHERE f.\"OvduTerm\" = 5 ";
		sql += "                  GROUP BY \"Pivot\"";
		sql += "                ) TermV ON termiii.\"Pivot\" = f.\"Pivot\"";
		sql += "      LEFT JOIN ( SELECT \"Pivot\"";
		sql += "                        ,COUNT(*) AS cnt";
		sql += "                        ,SUM(\"PrinBalance\") AS amt";
		sql += "                  FROM fullData f";
		sql += "                  WHERE f.\"OvduTerm\" = 6 ";
		sql += "                  GROUP BY \"Pivot\"";
		sql += "                      ) TermVI ON termiii.\"Pivot\" = f.\"Pivot\"";
		sql += "      LEFT JOIN ( SELECT \"Pivot\"";
		sql += "                        ,COUNT(*) AS cnt";
		sql += "                        ,SUM(\"PrinBalance\") AS amt";
		sql += "                  FROM fullData f";
		sql += "                  WHERE f.\"OvduTerm\" = 990 ";
		sql += "                  GROUP BY \"Pivot\"";
		sql += "                ) TermOvdu ON termOvdu.\"Pivot\" = f.\"Pivot\"";
		}
		else
		{
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

		logger.info("sql=" + sql);
		
		LocalDate lastYearMonth = LocalDate.of(  Integer.parseInt(titaVo.getParam("inputYear"))+1911
				                               , Integer.parseInt(titaVo.getParam("inputMonth"))
				                               , 1 ).minusMonths(1);
		
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		
		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("inputYearMonth", Integer.toString(lastYearMonth.getYear()) + String.format("%02d",lastYearMonth.getMonthValue()));
		query.setParameter("inputOverdueTermMin", titaVo.getParam("inputOverdueTermMin"));
		query.setParameter("inputOverdueTermMax", titaVo.getParam("inputOverdueTermMax"));
		query.setParameter("inputBusinessOfficer", titaVo.getParam("inputBusinessOfficer"));
		query.setParameter("largeAmountOnly", kind == OutputSortBy.LargeAmt_Agent || kind == OutputSortBy.LargeAmt_Customer ? "Y" : "N");
		query.setParameter("outputSortByColumn", kind == OutputSortBy.Year ? "Year" : "BusinessOfficer");
		
		return this.convertToMap(query.getResultList());
	}

}