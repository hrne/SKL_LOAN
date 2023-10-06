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
public class LM078ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo, int RptMonth) throws Exception {
		this.info("LM078.findAll ");
		this.info("LM078ServiceImpl RptMonth: " + RptMonth);

		String ruleCode = "'B044%'";

		String sql = "	";
		sql += "    WITH \"CFSum\" AS (";
		sql += "        SELECT Cl.\"CustNo\"";
		sql += "             , Cl.\"FacmNo\"";
		sql += "             , Nvl(";
		sql += "            Cm.\"CityCode\", '05'";
		sql += "        ) AS \"CityCode\"";
		sql += "             , SUM(Nvl(";
		sql += "            Cl.\"OriEvaNotWorth\", 0";
		sql += "        )) AS \"EvaNetWorth\"";
		sql += "        FROM \"ClFac\"   Cl";
		sql += "        LEFT JOIN \"ClMain\"  Cm ON Cm.\"ClCode1\" = Cl.\"ClCode1\"";
		sql += "                                 AND";
		sql += "                                 Cm.\"ClCode2\" = Cl.\"ClCode2\"";
		sql += "                                 AND";
		sql += "                                 Cm.\"ClNo\" = Cl.\"ClNo\"";
		sql += "        WHERE Cl.\"MainFlag\" = 'Y'";
		sql += "        GROUP BY Cl.\"CustNo\"";
		sql += "               , Cl.\"FacmNo\"";
		sql += "               , Nvl(";
		sql += "            Cm.\"CityCode\", '05'";
		sql += "        )";
		sql += "    ), \"Tmp\" AS (";
		sql += "        SELECT CASE Nvl(";
		sql += "            Cm.\"CityCode\", '05'";
		sql += "        )";
		sql += "            WHEN '05'  THEN 1";
		sql += "            WHEN '10'  THEN 2";
		sql += "            WHEN '15'  THEN 3";
		sql += "            WHEN '35'  THEN 4";
		sql += "            WHEN '65'  THEN 5";
		sql += "            WHEN '70'  THEN 6";
		sql += "            ELSE 7";
		sql += "        END AS \"CitySeq\"";
		sql += "             , Fac.\"RuleCode\"";
		sql += "             , 1                                                   AS \"Count\"";
		sql += "             , Round(";
		sql += "            Fac.\"ApproveRate\", 2";
		sql += "        )                          AS \"ApproveRate\"";
		sql += "             , Round(";
		sql += "            Lm.\"DrawdownAmt\" / 100000000, 2";
		sql += "        )                 AS \"DrawdownAmt\"";
		sql += "             , Round(";
		sql += "            Fac.\"LineAmt\" / 100000000, 2";
		sql += "        )                    AS \"LineAmt\"";
		sql += "             , Round(";
		sql += "            Cm.\"EvaNetWorth\" / 100000000, 2";
		sql += "        )                 AS \"EvaNetWorth\"";
		sql += "             , Decode ( Nvl(Cm.\"EvaNetWorth\",0),0,0,";		
		sql += "            	 Round(";
		sql += "            		Fac.\"LineAmt\" / Cm.\"EvaNetWorth\", 4";
		sql += "        		 ) * 100)           AS \"LTV\"";
		sql += "             , Fc.\"ApplDate\"";
		sql += "        FROM \"FacMain\"      Fac";
		sql += "        LEFT JOIN \"LoanBorMain\"  Lm ON Lm.\"CustNo\" = Fac.\"CustNo\"";
		sql += "                                      AND";
		sql += "                                      Lm.\"FacmNo\" = Fac.\"FacmNo\"";
//		sql += "                                      AND";
//		sql += "                                      Lm.\"BormNo\" = Fac.\"LastBormNo\"";
		sql += "        LEFT JOIN \"CFSum\"        Cm ON Cm.\"CustNo\" = Fac.\"CustNo\"";
		sql += "                                AND";
		sql += "                                Cm.\"FacmNo\" = Fac.\"FacmNo\"";
		sql += "        LEFT JOIN \"CdCode\"       Cd ON Cd.\"DefCode\" = 'RuleCode'";
		sql += "                                 AND";
		sql += "                                 Cd.\"Code\" = Fac.\"RuleCode\"";
		sql += "        LEFT JOIN \"FacCaseAppl\"  Fc ON Fc.\"ApplNo\" = Fac.\"ApplNo\"";
		sql += "        LEFT JOIN \"CustMain\"      C ON C.\"CustNo\" = Fac.\"CustNo\"";
		sql += "        WHERE Trunc(Lm.\"DrawdownDate\" / 100) = :RptMonth";
		sql += "              AND";
		sql += "              Fac.\"CustTypeCode\" = 'S01'";
		sql += "              AND";
		sql += "              Fac.\"RuleCode\" LIKE " + ruleCode;
		sql += "              AND";
		sql += "			  Fac.\"CompensateFlag\" LIKE ";
		sql += "              		Case When C.\"EntCode\" = 0 Then '%' ";
		sql += "				   		 Else 'N' End ";
		sql += "              AND";
		sql += "			  Lm.\"RenewFlag\" LIKE ";
		sql += "              		Case When C.\"EntCode\" = 0 Then '%' ";
		sql += "				   		 Else '0' End ";
		sql += "    )";
		sql += "    SELECT \"CitySeq\"";
		sql += "         , SUM(\"Count\")                                                     AS \"Count\"";
		sql += "         , SUM(\"LineAmt\")                                                   AS \"LineAmt\"";
		sql += "         , SUM(\"ApproveRate\")                                               AS \"ApproveRate\"";
		sql += "         , SUM(\"LTV\")                                                       AS \"LTV\"";
		sql += "         , SUM(\"LineAmt\" * \"ApproveRate\" / \"LineAmt\") / SUM(\"Count\")        AS \"wAvgRate\"";
		sql += "         , SUM(\"LineAmt\" * \"LTV\" / \"LineAmt\") / SUM(\"Count\")               AS \"wAvgLTV\"";
		sql += "         , MAX(\"ApplDate\")                                                     AS \"ApplDate\"";
		sql += "    FROM \"Tmp\"";
		sql += "    GROUP BY \"CitySeq\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("RptMonth", RptMonth);

		return this.convertToMap(query);
	}

}