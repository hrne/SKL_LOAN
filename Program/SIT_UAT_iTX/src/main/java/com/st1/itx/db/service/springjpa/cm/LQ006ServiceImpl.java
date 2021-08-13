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
public class LQ006ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LQ006ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		logger.info("lQ006.findAll ");

		String entdy = String.valueOf((Integer.valueOf(titaVo.getParam("ENTDY")) + 19110000));
		
		String sql = "";
		sql += "	SELECT LPAD(TO_CHAR(A.\"CustNo\"),7,'0') || LPAD(TO_CHAR(A.\"FacmNo\"),3,'0') || LPAD(TO_CHAR(A.\"BormNo\"),3,'0') AS \"CustFacmBorm\"";
		sql += "		  ,A.\"LoanBal\" AS \"LoanBal\"";
		sql += "		  ,NVL(B.\"Interest\",0) AS \"Interest\"";

		sql += "	 	  ,ROUND((NVL(FE.\"Fee\",0) + NVL(AR.\"RvBal\",0)) ";
		sql += "			* ROUND(A.\"LoanBal\" / M2.\"TotalBal\" , 8),0)  AS \"Fee\"";

		sql += "		  ,A.\"LoanBal\" + NVL(B.\"Interest\",0)";
		sql += "	 	  + ROUND((NVL(FE.\"Fee\",0) + NVL(AR.\"RvBal\",0)) ";
		sql += "			* ROUND(A.\"LoanBal\" / M2.\"TotalBal\" , 8),0)  AS \"Total\"";

		sql += "		  ,A.\"Days\" AS \"Days\"";
		sql += "	FROM(SELECT M.\"CustNo\" AS \"CustNo\"";
		sql += "			   ,M.\"FacmNo\" AS \"FacmNo\"";
		sql += "			   ,M.\"BormNo\" AS \"BormNo\"";
		sql += "			   ,SUM(M.\"LoanBalance\") AS \"LoanBal\"";
		sql += "			   ,MAX(NVL(C.\"OvduDays\",0)) AS \"Days\"";

		sql += "		 FROM \"MonthlyLoanBal\" M";
		sql += "		 LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\"=M.\"CustNo\"";
		sql += "		 							AND L.\"FacmNo\"=M.\"FacmNo\"";
		sql += "		  							AND L.\"BormNo\"=M.\"BormNo\"";
		sql += "		 LEFT JOIN \"CollList\" C ON C.\"CustNo\"=M.\"CustNo\"";
		sql += "		 						 AND C.\"FacmNo\"=M.\"FacmNo\"";
		sql += "		 WHERE M.\"YearMonth\" = TRUNC( :entdy /100 )";
		sql += "		   AND M.\"LoanBalance\" > 0";
		sql += "		 GROUP BY M.\"CustNo\"";
		sql += "				 ,M.\"FacmNo\"";
		sql += "				 ,M.\"BormNo\") A";
		sql += "	LEFT JOIN(SELECT \"CustNo\"";
		sql += "					,SUM(DECODE(\"FacmNo\",0,NVL(\"RvBal\",0),0)) AS \"RvBalF\"";
		sql += "					,SUM(DECODE(\"FacmNo\",0,0,NVL(\"RvBal\",0))) AS \"RvBal\"";
		sql += "			  FROM \"AcReceivable\"";
		sql += "			  WHERE \"AcctCode\" IN ('F09','F25','TMI')";
		sql += "				AND \"OpenAcDate\" <= :entdy ";
		sql += "				AND \"ClsFlag\" = 0 ";
		sql += "			  GROUP BY \"CustNo\") AR";
		sql += "		 ON AR.\"CustNo\"=A.\"CustNo\"";

		sql += "	LEFT JOIN(SELECT \"CustNo\" AS \"CustNo\"";
		sql += "				    ,\"FacmNo\" AS \"FacmNo\"";
		sql += "				    ,\"BormNo\" AS \"BormNo\"";
		sql += "				    ,SUM(\"Interest\") AS \"Interest\"";
		sql += "			  FROM \"AcLoanInt\"";
		sql += "			  WHERE \"YearMonth\" = TRUNC( :entdy /100 )";
		sql += "		 	    AND \"IntStartDate\" > 0 ";
		
		sql += "		 	  GROUP BY \"CustNo\"";
		sql += "				      ,\"FacmNo\"";
		sql += "				      ,\"BormNo\") B";
		sql += "	ON B.\"CustNo\"=A.\"CustNo\"";
		sql += "	AND B.\"FacmNo\"=A.\"FacmNo\"";
		sql += "	AND B.\"BormNo\"=A.\"BormNo\"";

		sql += "	LEFT JOIN(SELECT \"CustNo\"";
		sql += "				    ,SUM(\"LoanBalance\") AS \"TotalBal\"";
		sql += "			  FROM \"MonthlyLoanBal\"";
		sql += "			  WHERE \"YearMonth\" = TRUNC( :entdy /100 )";
		sql += "		 	  GROUP BY \"CustNo\" ) M2";
		sql += "	ON M2.\"CustNo\"=A.\"CustNo\"";

		sql += "	LEFT JOIN(SELECT \"CustNo\"";
		sql += "				    ,SUM(\"Fee\") AS \"Fee\"";
		sql += "			  FROM \"ForeclosureFee\"";
		sql += "			  WHERE \"CloseDate\" = 0 ";
		sql += "		 	  GROUP BY \"CustNo\" ) FE";
		sql += "	ON FE.\"CustNo\"=A.\"CustNo\"";

		sql += "	ORDER BY A.\"CustNo\"";

		logger.info("sql=" + sql);
		

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entdy", entdy);
		return this.convertToMap(query.getResultList());
	}



	//ver1.0
	// String sql2 = "SELECT \"F1\""
	// sql += "              , \"F2\"";
	// sql += "              , \"F3\"";
	// sql += "              , SUM(F4) AS F4";
	// sql += "              , SUM(F5) AS F5";
	// sql += "              , MAX(F6) AS F6";
	// sql += "              , SUM(F7) AS F7";
	// sql += "              , MAX(F8) AS F8";
	// sql += "        FROM ( SELECT LPAD(TO_CHAR(A.\"CustNo\"), 7, '0') AS F1";
	// sql += "                    , LPAD(TO_CHAR(A.\"FacmNo\"), 3, '0') AS F2";
	// sql += "                    , LPAD(TO_CHAR(A.\"BormNo\"), 3, '0') AS F3";
	// sql += "                    , A.\"Interest\" AS F4";
	// sql += "                    , A.\"LoanBal\" AS F5";
	// sql += "                    , A.\"PayIntDate\" AS F6";
	// sql += "                    , 0 AS F7";
	// sql += "                    , 0 AS F8";
	// sql += "               FROM \"AcLoanInt\" A WHERE A.\"YearMonth\" = :entdy";
	// sql += "               UNION ALL";
	// sql += "               SELECT LPAD(TO_CHAR(A.\"CustNo\"), 7, '0') AS F1";
	// sql += "                    , LPAD(TO_CHAR(A.\"FacmNo\"), 3, '0') AS F2";
	// sql += "                    , LPAD(SUBSTR(A.\"RvNo\",0,3), 3, '0') AS F3";
	// sql += "                    , 0 AS F4";
	// sql += "                    , 0 AS F5";
	// sql += "                    , 0 AS F6";
	// sql += "                    , A.\"RvBal\" AS F7";
	// sql += "                    , A.\"OpenAcDate\" AS F8";
	// sql += "               FROM \"AcReceivable\" A WHERE A.\"AcctCode\" IN ('F07', 'F09')";
	// sql += "                                         AND  A.\"ClsFlag\" = 0)";
	// sql += "        GROUP BY \"F1\", \"F2\", \"F3\"";
	// sql += "        ORDER BY \"F1\", \"F2\", \"F3\"";
}