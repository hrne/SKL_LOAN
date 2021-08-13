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

@Service
@Repository
/* 逾期放款明細 */
public class LM046ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM046ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		String entYear = String.valueOf((Integer.valueOf(titaVo.get("ENTDY").toString()) + 19110000) / 10000);
		
		logger.info("lM046.findAll entYear =" + entYear);
		
		String sql = "";
		sql += " WITH fullQuery AS ( ";
		sql += " SELECT M.\"YearMonth\" \"YearMonth\" ";
		sql += " 	  ,M.\"EntCode\" \"EntCode\" ";
		sql += " 	  ,SUM(M.\"LoanCnt\") \"LoanCnt\" ";
		sql += "       ,SUM(M.\"LoanBal\") \"LoanBal\" ";
		sql += "       ,SUM(M.\"OverCnt\") \"OverCnt\" ";
		sql += "       ,SUM(M.\"OverBal\") \"OverBal\" ";
		sql += "       ,SUM(M.\"BadOverCnt\") \"BadOverCnt\" ";
		sql += "       ,SUM(M.\"BadOverBal\") \"BadOverBal\" ";
		sql += " 	  FROM ";
		sql += " 	  ( SELECT * ";
		sql += "         FROM ( SELECT TRUNC(M.\"YearMonth\" / 100) * 10 + TRUNC((MOD(M.\"YearMonth\", 100) + 2) / 3) \"YearMonth\" ";
		sql += "                      ,DECODE(M.\"EntCode\", '1', '0', '1') \"EntCode\" ";
		sql += "                      ,1 \"LoanCnt\" ";
		sql += "                      ,M.\"PrinBalance\" \"LoanBal\" ";
		sql += "                      ,CASE WHEN M.\"Status\" = 0 ";
		sql += "                             AND M.\"OvduTerm\" >= 3 THEN 1 ";
		sql += "                       ELSE 0 END \"OverCnt\" ";
		sql += "                      ,CASE WHEN M.\"Status\" = 0 ";
		sql += "                             AND M.\"OvduTerm\" >= 3 THEN M.\"PrinBalance\" ";
		sql += "                       ELSE 0 END \"OverBal\" ";
		sql += "                      ,CASE WHEN M.\"Status\" IN ('2', '6') ";
		sql += "                             AND M.\"OvduTerm\" >= 6 THEN 1 ";
		sql += "                       ELSE 0 END \"BadOverCnt\" ";
		sql += "                      ,CASE WHEN M.\"Status\" IN ('2', '6') ";
		sql += "                             AND M.\"OvduTerm\" >= 6 THEN M.\"PrinBalance\" ";
		sql += "                       ELSE 0 END \"BadOverBal\" ";
		sql += "                FROM \"MonthlyFacBal\" M ";
		sql += "                WHERE M.\"Status\" IN ('0', '2', '6') ";
		sql += "              ) M ";
		sql += "         WHERE M.\"YearMonth\" BETWEEN :entYear-1||1 AND :entYear||4 ";
		sql += " 	       OR M.\"YearMonth\" IN (:entYear-2||4,:entYear-3||4,:entYear-4||4) ";
		sql += " 	    UNION ALL ";
		sql += " 	    SELECT TRUNC(:entYear||1),'1',0,0,0,0,0,0 FROM DUAL ";
		sql += " 	    UNION ALL ";
		sql += " 	    SELECT TRUNC(:entYear||2),'1',0,0,0,0,0,0 FROM DUAL ";
		sql += " 	    UNION ";
		sql += " 	    SELECT TRUNC(:entYear||3),'1',0,0,0,0,0,0 FROM DUAL ";
		sql += " 	    UNION ";
		sql += " 	    SELECT TRUNC(:entYear||4),'1',0,0,0,0,0,0 FROM DUAL ";
		sql += " 	    UNION ";
		sql += " 	    SELECT TRUNC(:entYear-1||1),'1',0,0,0,0,0,0 FROM DUAL ";
		sql += " 	    UNION ";
		sql += " 	    SELECT TRUNC(:entYear-1||2),'1',0,0,0,0,0,0 FROM DUAL ";
		sql += " 	    UNION ";
		sql += " 	    SELECT TRUNC(:entYear-1||3),'1',0,0,0,0,0,0 FROM DUAL ";
		sql += " 	    UNION ";
		sql += " 	    SELECT TRUNC(:entYear-1||4),'1',0,0,0,0,0,0 FROM DUAL ";
		sql += " 	    UNION ";
		sql += " 	    SELECT TRUNC(:entYear-2||4),'1',0,0,0,0,0,0 FROM DUAL ";
		sql += " 	    UNION ";
		sql += " 	    SELECT TRUNC(:entYear-3||4),'1',0,0,0,0,0,0 FROM DUAL ";
		sql += " 	    UNION ";
		sql += " 	    SELECT TRUNC(:entYear-4||4),'1',0,0,0,0,0,0 FROM DUAL ";
		sql += "       ) M ";
		sql += " GROUP BY M.\"YearMonth\", M.\"EntCode\" ";
		sql += " ) ";
		sql += " ";
		sql += " SELECT substr(q1.\"YearMonth\",1,4)-1911||'年Q'||substr(q1.\"YearMonth\",5,1) F0 ";
		sql += "       ,q1.\"LoanCnt\" F1 ";
		sql += "       ,q1.\"LoanBal\" F2 ";
		sql += "       ,q1.\"OverCnt\" F3 ";
		sql += "       ,q1.\"OverBal\" F4 ";
		sql += "       ,q1.\"BadOverCnt\" F5 ";
		sql += "       ,q1.\"BadOverBal\" F6 ";
		sql += "       ,NVL(q2.\"LoanCnt\",0) F7 ";
		sql += "       ,NVL(q2.\"LoanBal\",0) F8 ";
		sql += "       ,NVL(q2.\"OverCnt\",0) F9 ";
		sql += "       ,NVL(q2.\"OverBal\",0) F10 ";
		sql += "       ,NVL(q2.\"BadOverCnt\",0) F11 ";
		sql += "       ,NVL(q2.\"BadOverBal\",0) F12 ";
		sql += " FROM fullQuery q1 ";
		sql += " LEFT JOIN fullQuery q2 ON q2.\"YearMonth\" = Q1.\"YearMonth\" ";
		sql += "                       AND q2.\"EntCode\" != 1 ";
		sql += " WHERE q1.\"EntCode\" = 1 ";
		sql += " ORDER BY q1.\"YearMonth\" DESC ";
		
//		String sql = "SELECT SUM(M.\"LoanCnt\") F0";
//		sql += "            ,SUM(M.\"LoanBal\") F1";
//		sql += "            ,SUM(M.\"OverCnt\") F2";
//		sql += "           ,'F3'                F3";
//		sql += "            ,SUM(M.\"OverBal\") F4";
//		sql += "           ,'F5'                F5";
//		sql += "            ,SUM(M.\"BadCnt\")  F6";
//		sql += "           ,'F7'                F7";
//		sql += "            ,SUM(M.\"BadBal\")  F8";
//		sql += "           ,'F9'                F9";
//		sql += "            ,M.\"EntCode\"      F10";
//		sql += "      FROM (SELECT DECODE(M.\"EntCode\"";
//		sql += "                        ,'1'";
//		sql += "                         ,0";
//		sql += "                         ,1) \"EntCode\"";
//		sql += "                  ,1 \"LoanCnt\"";
//		sql += "                  ,M.\"PrinBalance\" \"LoanBal\"";
//		sql += "           ,CASE WHEN M.\"Status\" = 0";
//		sql += "                  AND M.\"OvduTerm\" >= 3 ";
//		sql += "                  THEN 1";
//		sql += "            ELSE 0 END \"OverCnt\"";
//		sql += "           ,CASE WHEN M.\"Status\" = 0";
//		sql += "                  AND M.\"OvduTerm\" >= 3 ";
//		sql += "                 THEN M.\"PrinBalance\"";
//		sql += "            ELSE 0 END \"OverBal\"";
//		sql += "           ,DECODE(M.\"Status\"";
//		sql += "                  ,0";
//		sql += "                  ,0";
//		sql += "                  ,1) \"BadCnt\"";
//		sql += "           ,DECODE(M.\"Status\"";
//		sql += "                  ,0";
//		sql += "                  ,0";
//		sql += "                  ,M.\"PrinBalance\") \"BadBal\"";
//		sql += "            FROM \"MonthlyFacBal\" M";
//		sql += "            WHERE M.\"YearMonth\" = :entdy";
//		sql += "              AND M.\"Status\" IN (0, 2, 6)";
//		sql += "            UNION ALL";
//		sql += "            SELECT 9 \"EntCode\"";
//		sql += "                  ,1 \"LoanCnt\"";
//		sql += "                  ,M.\"PrinBalance\" \"LoanBal\"";
//		sql += "           ,CASE WHEN M.\"Status\" = 0";
//		sql += "                  AND M.\"OvduTerm\" >= 3";
//		sql += "                 THEN 1";
//		sql += "            ELSE 0 END \"OverCnt\"";
//		sql += "           ,CASE WHEN M.\"Status\" = 0";
//		sql += "                  AND M.\"OvduTerm\" >= 3";
//		sql += "                 THEN M.\"PrinBalance\"";
//		sql += "            ELSE 0 END \"OverBal\"";
//		sql += "           ,DECODE(M.\"Status\"";
//		sql += "                  ,0";
//		sql += "                  ,0";
//		sql += "                  ,1) \"BadCnt\"";
//		sql += "           ,DECODE(M.\"Status\"";
//		sql += "                  ,0";
//		sql += "                  ,0";
//		sql += "                  ,M.\"PrinBalance\") \"BadBal\"";
//		sql += "            FROM \"MonthlyFacBal\" M";
//		sql += "            WHERE M.\"YearMonth\" = :entdy";
//		sql += "              AND M.\"Status\" IN (0, 2, 6)) M";
//		sql += "      GROUP BY M.\"EntCode\"";
//		sql += "      ORDER BY M.\"EntCode\"";

		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entYear", entYear);
		
		return this.convertToMap(query.getResultList());
	}

}