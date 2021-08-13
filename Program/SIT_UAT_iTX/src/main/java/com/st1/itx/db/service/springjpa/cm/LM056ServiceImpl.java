package com.st1.itx.db.service.springjpa.cm;

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
import com.st1.itx.util.date.DateUtil;

@Service
@Repository
/* 逾期放款明細 */
public class LM056ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM056ServiceImpl.class);

	@Autowired
	DateUtil dateUtil;

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		int nENTDY = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;

		String iENTDY = String.valueOf(nENTDY);
		String iYYMM = iENTDY.substring(0, 6);
		dateUtil.init();
		dateUtil.setDate_1(nENTDY);
		dateUtil.setMons(-4);
		int nL4MDY = dateUtil.getCalenderDay();
		String iL4MDY = String.valueOf(nL4MDY);
 
		logger.info("lM056.findAll ENTDY=" + iENTDY + ",L4MDY=" + iL4MDY);

		String sql = "SELECT J.\"SubTranCode\" AS F0";
		sql += "			,J.\"CustNo\" AS F1";
		sql += "			,J.\"FacmNo\" AS F2";
		sql += "			,J.\"BormNo\" AS F3";
		sql += "			,C.\"CustName\" AS F4";
		sql += "			,M.\"LoanBal\" AS F5";
		sql += "			,M.\"MaturityDate\" AS F6";
		sql += "			,CASE";
		sql += "			   WHEN TRUNC(F.\"OvduDate\" / 100) = :yymm THEN 'B3'";
		sql += "			   WHEN M.\"MaturityDate\" < :l4mdy THEN 'B1'";
		sql += "			   WHEN L.\"LegalProg\" IN ('056','058','060') THEN 'C5'";
		sql += "			 ELSE 'B3' END AS F7";
		sql += "			,NVL(L.\"LegalProg\",'#N/A') AS F8";
		sql += "			,'C7' AS F9";
		sql += "			,DECODE(L.\"Amount\",0,'#N/A','無擔保') AS F10";
		sql += "			,DECODE(F.\"RenewCode\",'2','協議',' ') AS F11";
		sql += "	  FROM(SELECT CAST(SUBSTR(J.\"AcctNo\", 1, 7) AS DECIMAL) \"CustNo\"";
		sql += "				 ,CAST(SUBSTR(J.\"AcctNo\",8,3) AS DECIMAL) \"FacmNo\"";
		sql += "				 ,CAST(SUBSTR(J.\"AcctNo\",11, 3) AS DECIMAL) \"BormNo\"";
		sql += "				 ,J.\"SubTranCode\"";
		sql += "		   FROM \"JcicB201\" J";
		sql += "		   WHERE J.\"DataYM\" = :yymm) J";
		sql += "	  LEFT JOIN(SELECT L.\"CustNo\"";
		sql += "					  ,L.\"FacmNo\"";
		sql += "					  ,MAX(L.\"LegalProg\") \"LegalProg\"";
		sql += "					  ,SUM(L.\"Amount\") \"Amount\"";
		sql += "				FROM(SELECT CAST(SUBSTR(J.\"AcctNo\",1,7) AS DECIMAL) \"CustNo\"";
		sql += "					  	   ,CAST(SUBSTR(J.\"AcctNo\",8,3) AS DECIMAL) \"FacmNo\"";
		sql += "						   ,L.\"LegalProg\"";
		sql += "					       ,0 \"Amount\"";
		sql += "						   ,ROW_NUMBER() OVER (PARTITION BY SUBSTR(J.\"AcctNo\",1,10) ORDER BY L.\"RecordDate\" DESC) AS SEQ";
		sql += "					 FROM \"JcicB201\" J";
		sql += "					 LEFT JOIN \"CollLaw\" L ON L.\"CaseCode\" = '1'";
		sql += "											AND L.\"CustNo\" = CAST(SUBSTR(J.\"AcctNo\", 1, 7) AS DECIMAL)";
		sql += "											AND L.\"FacmNo\" = CAST(SUBSTR(J.\"AcctNo\", 8, 3) AS DECIMAL)";
		sql += "											AND L.\"LegalProg\" < 900";
		sql += "					 WHERE J.\"DataYM\" = :yymm";
		sql += "					 UNION ALL";
		sql += "					 SELECT CAST(SUBSTR(J.\"AcctNo\",1,7) AS DECIMAL) \"CustNo\"";
		sql += "						   ,CAST(SUBSTR(J.\"AcctNo\",8,3) AS DECIMAL) \"FacmNo\"";
		sql += "						   ,NULL \"LegalProg\"";
		sql += "						   ,L.\"Amount\"";
		sql += "						   ,ROW_NUMBER() OVER(PARTITION BY SUBSTR(J.\"AcctNo\",1,10) ORDER BY L.\"RecordDate\" DESC) AS SEQ";
		sql += "					 FROM \"JcicB201\" J";
		sql += "					 LEFT JOIN \"CollLaw\" L ON L.\"CaseCode\" ='1'";
		sql += "											AND L.\"CustNo\" = CAST(SUBSTR(J.\"AcctNo\", 1, 7) AS DECIMAL)";
		sql += "											AND L.\"FacmNo\" = CAST(SUBSTR(J.\"AcctNo\", 8, 3) AS DECIMAL)";
		sql += "											AND L.\"LegalProg\" = 901";
		sql += "					 WHERE J.\"DataYM\" = :yymm) L";
		sql += "				 WHERE L.\"SEQ\" = 1";
		sql += "				 GROUP BY L.\"CustNo\", L.\"FacmNo\") L";
		sql += "	  ON L.\"CustNo\" = J.\"CustNo\"";
		sql += "	  AND L.\"FacmNo\" = J.\"FacmNo\"";
		sql += "	  LEFT JOIN \"MonthlyFacBal\" F ON F.\"YearMonth\" = :yymm";
		sql += "								   AND F.\"CustNo\" = J.\"CustNo\"";
		sql += "								   AND F.\"FacmNo\" = J.\"FacmNo\"";
		sql += "	  LEFT JOIN \"LoanBorMain\" M ON M.\"CustNo\" = J.\"CustNo\"";
		sql += "	  							 AND M.\"FacmNo\" = J.\"FacmNo\"";
		sql += "	  							 AND M.\"BormNo\" = J.\"BormNo\"";
		sql += "	  LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = J.\"CustNo\"";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", iYYMM);
		query.setParameter("l4mdy", iL4MDY);

		return this.convertToMap(query.getResultList());
	}

}