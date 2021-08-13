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
public class LM027ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM027ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		String entdy = String.valueOf((Integer.valueOf(titaVo.getParam("ENTDY").toString()) + 19110000) / 100);

		logger.info("lM027.findAll ");
		String sql = "";
		sql += "	SELECT M.\"CustNo\"";
		sql += "	      ,M.\"FacmNo\"";
		sql += "		  ,M.\"CustName\"";
		sql += "		  ,M.\"Principal\"";
		sql += "		  ,NVL(M.\"TxAmt\",0) AS \"thisBal\"";
		sql += "		  ,NVL(M.\"TxAmt\" + LTT.\"TempAmt\",0) AS \"lastBal\""; 
		sql += "		  ,NVL(-LTT.\"TempAmt\",0) AS \"Amt\"";
		sql += "	FROM(SELECT O.\"CustNo\" AS \"CustNo\"";
		sql += "	      	   ,O.\"FacmNo\" AS \"FacmNo\"";
		sql += "		  	   ,C.\"CustName\" AS \"CustName\"";
		sql += "		  	   ,SUM(LT.	\"Principal\") AS \"Principal\"";
		sql += "		  	   ,SUM(NVL(D.\"TxAmt\",0)) AS \"TxAmt\"";
		sql += "		 FROM \"LoanOverdue\" O";
		sql += "    	 LEFT JOIN \"LoanBorMain\" M ON M.\"CustNo\" = O.\"CustNo\"";
		sql += "    						        AND M.\"FacmNo\" = O.\"FacmNo\"";
		sql += "    						        AND M.\"BormNo\" = O.\"BormNo\"";
		sql += "    	 LEFT JOIN (SELECT \"CustNo\"";
		sql += "    				 	  ,\"FacmNo\"";
		sql += "    				 	  ,DECODE(\"DbCr\",'C',\"TxAmt\",-\"TxAmt\") As \"TxAmt\"";
		sql += "    		   		FROM \"AcDetail\"";
		sql += " 			   	    WHERE TRUNC(\"AcDate\" / 100 ) = :entdy ";
		sql += " 			   		  AND \"AcctCode\" = 'F08' )D";
		sql += "		 ON D.\"CustNo\" = M.\"CustNo\" AND D.\"FacmNo\" = M.\"FacmNo\"";
		sql += "    	 LEFT JOIN (SELECT \"CustNo\"";
		sql += "    				 	  ,\"FacmNo\"";
		sql += "    				 	  ,\"BormNo\"";
		sql += "   					 	  ,\"Principal\"";
		sql += "    		   	    FROM(SELECT ROW_NUMBER () OVER (PARTITION BY \"CustNo\",\"FacmNo\",\"BormNo\" ORDER BY \"BorxNo\" DESC) AS \"Seq\"";
		sql += " 						  	   ,LT.\"CustNo\"";
		sql += " 						  	   ,LT.\"FacmNo\"";
		sql += " 						  	   ,LT.\"BormNo\"";
		sql += " 						  	   ,LT.\"Principal\"";
		sql += " 						 FROM \"LoanBorTx\" LT ) LT";
		sql += " 			   			 WHERE LT.\"Seq\" = 1 ) LT";
		sql += "		  ON LT.\"CustNo\" = M.\"CustNo\"";
		sql += "         AND LT.\"FacmNo\" = M.\"FacmNo\"";
		sql += "         AND LT.\"BormNo\" = M.\"BormNo\"";
		sql += "		 LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\"";
		sql += "		 WHERE M.\"Status\" in ('6','7') ";
		sql += "		   AND O.\"Status\" in ('2','3') ";
		sql += "		 GROUP BY O.\"CustNo\"";
		sql += "				 ,O.\"FacmNo\"";
		sql += "				 ,C.\"CustName\"";
		sql += "		 ORDER BY \"CustNo\"";
		sql += "				 ,\"FacmNo\") M";
		sql += "	LEFT JOIN(SELECT \"CustNo\"";
		sql += "			    	,\"FacmNo\"";
		sql += "				    ,\"LastBadDebt\" AS \"LastBal\"";
		sql += "			  FROM(SELECT \"CustNo\"";
		sql += "			    		 ,\"FacmNo\"";
		sql += "						 ,\"BormNo\"";
		sql += "						 ,NVL(\"BadDebtAmt\",0) + NVL(\"BadDebtBal\",0) AS \"LastBadDebt\"";
		// sql += "						 ,NVL(\"OvduAmt\",0) + NVL(\"OvduBal\",0) AS \"LastOvdu\"";
		sql += "						 ,ROW_NUMBER () OVER (PARTITION BY \"CustNo\",\"FacmNo\" ORDER BY \"BormNo\" DESC) AS \"Seq\"";
		sql += "				   FROM \"LoanOverdue\")";
		sql += "			  WHERE \"Seq\" = 1 ) O2";
		sql += "	ON O2.\"CustNo\" = M.\"CustNo\" AND O2.\"FacmNo\" = M.\"FacmNo\"";
		sql += "	LEFT JOIN (SELECT \"CustNo\"";
		sql += "					 ,\"FacmNo\"";
		sql += "         			 ,\"BormNo\"";
		sql += "					 ,\"TempAmt\"";
		sql += " 		       FROM (SELECT ROW_NUMBER () OVER (PARTITION BY \"CustNo\",\"FacmNo\",\"BormNo\" ORDER BY \"BorxNo\" DESC) AS \"Seq\"";
		sql += " 						   ,LT.* ";
		sql += "  					 FROM \"LoanBorTx\" LT";
		sql += " 			   		 WHERE TRUNC(\"AcDate\" / 100 ) = :entdy ";
		sql += " 				 	   AND \"FacmNo\" = 0 ) LT";
		sql += " 			   WHERE LT.\"Seq\" = 1 ) LTT ";
		sql += " 	ON LTT.\"CustNo\" = M.\"CustNo\"";

		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entdy", entdy);
		return this.convertToMap(query.getResultList());
	}

}

// sql += "	SELECT O.\"CustNo\" AS \"CustNo\"";
// sql += "	      ,O.\"FacmNo\" AS \"FacmNo\"";
// sql += "		  ,C.\"CustName\" AS \"CustName\"";
// sql += "		  ,SUM(\"Principal\") AS \"Principal\"";
// sql += "		  ,SUM(NVL(O.\"BadDebtAmt\",0)) AS \"BadDebtAmt\"";
// sql += "		  ,SUM(O.\"BadDebtAmt\"-O.\"BadDebtBal\") AS \"BadDebtDiff\"";
// sql += "		  ,SUM(NVL(D.\"TxAmt\",0)) AS \"TxAmt\"";
// sql += "	FROM \"LoanOverdue\" O";
// sql += "    LEFT JOIN \"LoanBorMain\" M ON M.\"CustNo\" = O.\"CustNo\"";
// sql += "    						   AND M.\"FacmNo\" = O.\"FacmNo\"";
// sql += "    						   AND M.\"BormNo\" = O.\"BormNo\"";
// sql += "    LEFT JOIN (SELECT \"CustNo\"";
// sql += "    				 ,\"FacmNo\"";
// sql += "    				 ,\"BormNo\"";
// sql += "   					 ,\"Principal\"";
// sql += "    		   FROM(SELECT ROW_NUMBER () OVER (PARTITION BY \"CustNo\",\"FacmNo\",\"BormNo\" ORDER BY \"BorxNo\" DESC) AS \"Seq\"";
// sql += " 						  ,LT.\"CustNo\"";
// sql += " 						  ,LT.\"FacmNo\"";
// sql += " 						  ,LT.\"BormNo\"";
// sql += " 						  ,LT.\"Principal\"";
// sql += " 					FROM \"LoanBorTx\" LT ) LT";
// sql += " 			   		WHERE LT.\"Seq\" = 1 ) LT";
// sql += "	ON LT.\"CustNo\" = M.\"CustNo\" AND LT.\"FacmNo\" = M.\"FacmNo\"";
// sql += "    LEFT JOIN (SELECT \"CustNo\"";
// sql += "    				 ,\"FacmNo\"";
// sql += "    				 ,DECODE(\"DbCr\",'C',\"TxAmt\",-\"TxAmt\") As \"TxAmt\"";
// sql += "    		   FROM \"AcDetail\"";
// sql += " 			   WHERE TRUNC(\"AcDate\" / 100 ) = :entdy ";
// sql += " 			   AND \"AcctCode\" = 'F08' )D";
// sql += "	ON D.\"CustNo\" = M.\"CustNo\" AND D.\"FacmNo\" = M.\"FacmNo\"";
// sql += "	LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\"";
// sql += "	WHERE M.\"Status\" in ('6','7') ";
// sql += "	GROUP BY O.\"CustNo\"";
// sql += "			,O.\"FacmNo\"";
// sql += "			,C.\"CustName\"";
// sql += "	ORDER BY \"CustNo\"";
// sql += "			,\"FacmNo\"";

// String sql = "SELECT D.\"F1\"";
// sql += "             , D.\"F2\"";
// sql += "             , C.\"CustName\"";
// sql += "             , D.\"F3\"";
// sql += "             , D.\"F4\"";
// sql += "             , D.\"F5\"";
// sql += "        FROM ( SELECT \"F1\"";
// sql += "                     , \"F2\"";
// sql += "                     , SUM(F3) F3";
// sql += "                     , SUM(F4) F4";
// sql += "                     , SUM(F5) F5";
// sql += "               FROM (	SELECT O.\"CustNo\" AS F1";
// sql += "                           , O.\"FacmNo\" AS F2";
// sql += "                           , O.\"BadDebtAmt\" AS F3";
// sql += "                           , O.\"BadDebtAmt\" - O.\"BadDebtBal\" AS F4";
// sql += "                           , 0 AS F5";
// sql += "                      FROM \"LoanOverdue\" O";
// sql += "                      LEFT JOIN \"LoanBorMain\" M ON M.\"CustNo\"  =  O.\"CustNo\"";
// sql += "                                                 AND M.\"FacmNo\"  =  O.\"FacmNo\"";
// sql += "                                                 AND M.\"BormNo\"  =  O.\"BormNo\"";
// sql += "                      WHERE M.\"Status\" IN (6, 7)";
// sql += "                      UNION ALL";
// sql += "                      SELECT A.\"CustNo\" AS F1";
// sql += "                           , A.\"FacmNo\" AS F2";
// sql += "                           , 0 AS F3";
// sql += "                           , 0 AS F4";
// sql += "                           , DECODE(A.\"DbCr\", 'C', A.\"TxAmt\", - A.\"TxAmt\") AS F5";
// sql += "                      FROM \"AcDetail\" A";
// sql += "                      WHERE TRUNC(A.\"AcDate\" / 100) = :entdy";
// sql += "                        AND A.\"AcctCode\" = 'F08')";
// sql += "        	   WHERE \"F1\" <> 0 ";
// sql += "               GROUP BY \"F1\", \"F2\"";
// sql += "               ORDER BY \"F1\", \"F2\") D ";
// sql += "        LEFT JOIN \"CustMain\" C ON C.\"CustNo\"  =  D.\"F1\"";