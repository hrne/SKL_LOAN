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
public class LW003ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findWorkMonth(TitaVo titaVo, int tbsdy) throws Exception {

		this.info("tbsdy=" + tbsdy);

		String sql = " ";
		sql += "	SELECT \"Year\" AS F0";
		sql += "		  ,\"Month\" AS F1";
		sql += "		  ,\"EndDate\" AS F2";
		sql += "	FROM \"CdWorkMonth\"";
		sql += "	WHERE \"EndDate\" = ";
		sql += "	  ( SELECT MAX(\"EndDate\") ";
		sql += "		FROM \"CdWorkMonth\" ";
		sql += "		WHERE \"EndDate\" <= :iday )";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iday", tbsdy);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll1(TitaVo titaVo, int wkYear, int wkMonth) throws Exception {

		this.info("lW003.findAll1");

		this.info("wkYear=" + wkYear + ",wkMonth=" + wkMonth);

		String sql = " ";
		sql += "	SELECT PD.\"WorkMonth\" AS \"WorkMonth\"";
		sql += "      	  ,ROUND(SUM(PD.\"DrawdownAmt\") / 10000 ) AS \"Performance\" ";
		sql += "	FROM \"PfBsDetail\" PD";
		sql += "    LEFT JOIN \"PfBsOfficer\" PO ON PO.\"EmpNo\" = PD.\"BsOfficer\" ";
		sql += "                                AND PO.\"WorkMonth\" = PD.\"WorkMonth\" ";
		sql += "    WHERE NVL(PO.\"AreaCode\",' ') IN ('10HC00','10HJ00','10HL00') ";
		sql += "      AND NVL(PO.\"DeptCode\",' ') IN ('A0B000','A0E000','A0F000','A0M000') ";
		sql += "      AND TRUNC(PD.\"WorkMonth\" / 100 ) = :iwkyear ";
		sql += "      AND MOD(PD.\"WorkMonth\", 100 ) <= :iwkmonth ";
		sql += "      AND PD.\"PerfAmt\" >= 0 ";
		sql += "    GROUP BY PD.\"WorkMonth\" ";
		sql += "    ORDER BY PD.\"WorkMonth\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iwkyear", wkYear);
		query.setParameter("iwkmonth", wkMonth);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll2(TitaVo titaVo, int wkYear, int wkMonth) throws Exception {

		this.info("lW003.findAll2");

		this.info("wkYear=" + wkYear + ",wkMonth=" + wkMonth);

		String sql = " ";
		sql += "  WITH \"Data1\" AS (";
		sql += "	SELECT B.\"DeptItem\"";
		sql += "      	  ,I.\"WorkMonth\"";
		sql += "      	  ,I.\"PerfCnt\"";
		sql += "          ,I.\"PerfAmt\"";
		sql += "      	  ,B.\"DeptCode\"";
		sql += "	FROM ( SELECT I.\"DistCode\" ";
		sql += "                 ,I.\"WorkMonth\" ";
		sql += "                 ,SUM(I.\"PerfCnt\") AS \"PerfCnt\" ";
		sql += "                 ,SUM(I.\"PerfAmt\") AS \"PerfAmt\" ";
		sql += "           FROM ( SELECT I.\"DistCode\" ";
		sql += "                    	,I.\"WorkMonth\" ";
		sql += "                    	,I.\"PerfCnt\" ";
		sql += "                   		,I.\"PerfAmt\" ";
		sql += "                  FROM \"PfItDetail\" I ";
		sql += "                  WHERE TRUNC(I.\"WorkMonth\" / 100) = :iwkyear ";
		sql += "                  UNION ALL";
		sql += "                  SELECT B.\"UnitCode\"                 AS \"DistCode\" ";
		sql += "                        ,W.\"Year\" * 100 + W.\"Month\" AS \"WorkMonth\" ";
		sql += "                        ,0                              AS \"PerfCnt\" ";
		sql += "                        ,0                              AS \"PerfAmt\" ";
		sql += "                  FROM \"CdBcm\" B ";
		sql += "                  CROSS JOIN \"CdWorkMonth\" W ";
		sql += "                  WHERE B.\"DeptCode\" IN ('A0B000','A0F000','A0E000','A0M000') ";
		sql += "                    AND B.\"DistCode\" = B.\"UnitCode\" ";
		sql += "                    AND W.\"Year\" = :iwkyear ";
		sql += "                    AND W.\"Month\" >= 1";
		sql += "                    AND W.\"Month\" <= :iwkmonth ) I ";
		sql += "           GROUP BY I.\"DistCode\" ";
		sql += "                   ,I.\"WorkMonth\" ) I ";
		sql += "    LEFT JOIN \"CdBcm\" B ON B.\"UnitCode\" = B.\"DistCode\" ";
		sql += "    					 AND B.\"UnitCode\" = I.\"DistCode\" ";
		sql += "    LEFT JOIN ( SELECT * ";
		sql += "                FROM \"PfDeparment\"";
		sql += "                WHERE \"DepartOfficer\" IS NOT NULL ";
		sql += "                  AND \"DistItem\" IS NOT NULL ";
		sql += "              ) D ON D.\"DistCode\" = B.\"DistCode\" ";
		sql += "    LEFT JOIN \"CdEmp\" E ON E.\"EmployeeNo\" = B.\"UnitManager\" ";
		sql += " 	WHERE B.\"DeptCode\" IN ('A0B000','A0F000','A0E000','A0M000') ";
		sql += "      AND E.\"Fullname\" IS NOT NULL)";
		sql += " ";
		sql += "	SELECT R.\"DeptCode\"";
		sql += "          ,CASE ";
		sql += "          	 WHEN MOD(R.\"WorkMonth\",100) <= 3 THEN '1'";
		sql += "          	 WHEN MOD(R.\"WorkMonth\",100) <= 6 THEN '2'";
		sql += "          	 WHEN MOD(R.\"WorkMonth\",100) <= 9 THEN '3'";
		sql += "          	 WHEN MOD(R.\"WorkMonth\",100) <= 12 THEN '4'";
		sql += "          	 ELSE '5'";
		sql += "           END AS \"Quarter\""; // --工作季(13工獨立出來)
		sql += "          ,SUM(NVL(R.\"Bonus\",0)) AS \"Bonus\"";
		sql += "	FROM ( SELECT D.\"WorkMonth\"";
		sql += "				 ,D.\"DeptCode\"";
		sql += "          		 ,CASE ";
		sql += "          	 		WHEN D.\"PerfAmt\" >= 30000000 THEN ROUND(D.\"PerfAmt\" / 3000000) * 500 ";
		sql += "          	 		WHEN D.\"PerfAmt\" >= 20000000 AND D.\"PerfAmt\" < 30000000 THEN 2500";
		sql += "          	 		WHEN D.\"PerfAmt\" >= 10000000 AND D.\"PerfAmt\" < 20000000 THEN 1000";
		sql += "          	 		WHEN D.\"PerfAmt\" < 10000000 THEN 0";
		sql += "           		  END AS \"Bonus\""; // --計算獎金(根據樣張上公式判斷)
		sql += "		   FROM \"Data1\" D ) R ";
		sql += " 	WHERE R.\"DeptCode\" IN ('A0B000','A0F000','A0E000','A0M000') ";
		sql += " 	GROUP BY R.\"DeptCode\"";
		sql += "            ,CASE ";
		sql += "          	   WHEN MOD(R.\"WorkMonth\",100) <= 3 THEN '1'";
		sql += "          	   WHEN MOD(R.\"WorkMonth\",100) <= 6 THEN '2'";
		sql += "          	   WHEN MOD(R.\"WorkMonth\",100) <= 9 THEN '3'";
		sql += "          	   WHEN MOD(R.\"WorkMonth\",100) <= 12 THEN '4'";
		sql += "          	   ELSE '5'";
		sql += "             END";
		sql += " 	ORDER BY \"Quarter\" ASC";
		sql += "		    ,R.\"DeptCode\" ASC";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iwkyear", wkYear);
		query.setParameter("iwkmonth", wkMonth);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll3(TitaVo titaVo, int wkYear, int wkMonth) throws Exception {

		this.info("lW003.findAll3");

		this.info("wkYear=" + wkYear + ",wkMonth=" + wkMonth);

		String sql = " ";
		sql += "  WITH \"Data1\" AS (";
		sql += "	SELECT I.\"WorkMonth\"";
		sql += "      	  ,I.\"PerfCnt\"";
		sql += "          ,I.\"PerfAmt\"";
		sql += "      	  ,B.\"DeptCode\"";
		sql += "          ,I.\"UnitCode\"";
		sql += "	FROM ( SELECT I.\"UnitCode\" ";
		sql += "                 ,I.\"WorkMonth\" ";
		sql += "                 ,SUM(I.\"PerfCnt\") AS \"PerfCnt\" ";
		sql += "                 ,SUM(I.\"PerfAmt\") AS \"PerfAmt\" ";
		sql += "           FROM ( SELECT I.\"UnitCode\" ";
		sql += "                    	,I.\"WorkMonth\" ";
		sql += "                    	,I.\"PerfCnt\" ";
		sql += "                   		,I.\"PerfAmt\" ";
		sql += "                  FROM \"PfItDetail\" I ";
		sql += "                  WHERE TRUNC(I.\"WorkMonth\" / 100) = :iwkyear ";
		sql += "                  UNION ALL";
		sql += "                  SELECT B.\"UnitCode\"                 AS \"DistCode\" ";
		sql += "                        ,W.\"Year\" * 100 + W.\"Month\" AS \"WorkMonth\" ";
		sql += "                        ,0                              AS \"PerfCnt\" ";
		sql += "                        ,0                              AS \"PerfAmt\" ";
		sql += "                  FROM \"CdBcm\" B ";
		sql += "                  CROSS JOIN \"CdWorkMonth\" W ";
		sql += "                  WHERE B.\"DeptCode\" IN ('A0B000','A0F000','A0E000','A0M000') ";
		sql += "                    AND B.\"DistCode\" <> B.\"UnitCode\" ";
		sql += "                    AND W.\"Year\" = :iwkyear ";
		sql += "                    AND W.\"Month\" >= 1";
		sql += "                    AND W.\"Month\" <= :iwkmonth ) I ";
		sql += "           GROUP BY I.\"UnitCode\" ";
		sql += "                   ,I.\"WorkMonth\" ) I ";
		sql += "    LEFT JOIN \"CdBcm\" B ON B.\"UnitCode\" = I.\"UnitCode\" ";
		sql += "    LEFT JOIN \"CdEmp\" E ON E.\"EmployeeNo\" = B.\"UnitManager\" ";
		sql += " 	WHERE B.\"UnitManager\" IS NOT NULL)";
		sql += " ";
		sql += "	SELECT R.\"DeptCode\"";
		sql += "          ,CASE ";
		sql += "          	 WHEN MOD(R.\"WorkMonth\",100) <= 3 THEN '1'";
		sql += "          	 WHEN MOD(R.\"WorkMonth\",100) <= 6 THEN '2'";
		sql += "          	 WHEN MOD(R.\"WorkMonth\",100) <= 9 THEN '3'";
		sql += "          	 WHEN MOD(R.\"WorkMonth\",100) <= 12 THEN '4'";
		sql += "          	 ELSE '5'";
		sql += "           END AS \"Quarter\""; // --工作季(13工獨立出來)
		sql += "          ,SUM(NVL(R.\"cntBonus\",0)) + SUM(NVL(R.\"amtBonus\",0)) AS \"Bonus\"";
		sql += "	FROM ( SELECT D.\"WorkMonth\"";
		sql += "				 ,D.\"DeptCode\"";
		sql += "          		 ,CASE ";
		sql += "          	 		WHEN D.\"PerfCnt\" >= 4 THEN D.\"PerfCnt\" * 500 ";
		sql += "          	 		WHEN D.\"PerfCnt\" >= 3 THEN 1500";
		sql += "          	 		WHEN D.\"PerfCnt\" >= 2 THEN 1000";
		sql += "          	 		WHEN D.\"PerfCnt\" >= 1 THEN 500";
		sql += "           		  END AS \"cntBonus\""; // --計算獎金(根據樣張上公式判斷)
		sql += "          		 ,CASE ";
		sql += "          	 		WHEN D.\"PerfAmt\" >= 9000000 THEN ROUND(D.\"PerfAmt\" / 1000000) * 500 ";
		sql += "          	 		WHEN D.\"PerfAmt\" >= 6000000 AND D.\"PerfAmt\" < 9000000 THEN 1000";
		sql += "          	 		WHEN D.\"PerfAmt\" >= 3000000 AND D.\"PerfAmt\" < 6000000 THEN 500";
		sql += "          	 		WHEN D.\"PerfAmt\" < 3000000 THEN 0";
		sql += "           		  END AS \"amtBonus\""; // --計算獎金(根據樣張上公式判斷)
		sql += "		   FROM \"Data1\" D ) R ";
		sql += " 	WHERE R.\"DeptCode\" IN ('A0B000','A0E000','A0F000','A0M000') ";
		sql += " 	GROUP BY R.\"DeptCode\"";
		sql += "            ,CASE ";
		sql += "          	   WHEN MOD(R.\"WorkMonth\",100) <= 3 THEN '1'";
		sql += "          	   WHEN MOD(R.\"WorkMonth\",100) <= 6 THEN '2'";
		sql += "          	   WHEN MOD(R.\"WorkMonth\",100) <= 9 THEN '3'";
		sql += "          	   WHEN MOD(R.\"WorkMonth\",100) <= 12 THEN '4'";
		sql += "          	   ELSE '5'";
		sql += "             END";
		sql += " 	ORDER BY \"Quarter\" ASC";
		sql += "		    ,R.\"DeptCode\" ASC";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iwkyear", wkYear);
		query.setParameter("iwkmonth", wkMonth);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll4(TitaVo titaVo, int wkYear, int wkMonth) throws Exception {

		this.info("lW003.findAll4");

		this.info("wkYear=" + wkYear + ",wkMonth=" + wkMonth);

		String sql = " ";
		sql += "  WITH \"Data1\" AS (";
		sql += "	SELECT IM.\"WorkMonth\"";
		sql += "	      ,SUM(R.\"IntroducerAddBonus\") AS \"IntroAddBonus\"";
		sql += "	FROM( SELECT I.* ";
		sql += "	            ,ROW_NUMBER() OVER(";
		sql += "		  		 PARTITION BY I.\"CustNo\", I.\"FacmNo\"";
		sql += "	        	     ORDER BY I.\"DrawdownDate\" ASC ) AS \"SEQ\"";
		sql += " 	      FROM \"PfItDetail\" I";
		sql += "		  WHERE TRUNC(I.\"WorkMonth\"/100) = :iwkyear ";
		sql += " 	     	AND MOD(I.\"WorkMonth\",100) <= :iwkmonth ";
		sql += " 	     	AND I.\"PieceCode\" IN ('A','1')";
		sql += " 	     	AND I.\"DistCode\" IS NOT NULL ) IM ";
		sql += "	LEFT JOIN ( SELECT  \"CustNo\"";
		sql += "					  , \"FacmNo\"";
		sql += "					  , \"Introducer\"";
		sql += "	  	 			  , SUM(\"IntroducerAddBonus\") AS \"IntroducerAddBonus\"";
		sql += "	            FROM \"PfReward\"";
		sql += "		  		WHERE TRUNC(\"WorkMonth\"/100) = :iwkyear ";
		sql += " 	     		  AND MOD(\"WorkMonth\",100) <= :iwkmonth ";
		sql += "	        	GROUP BY \"CustNo\"";
		sql += "						,\"FacmNo\"";
		sql += "						,\"Introducer\"";
		sql += "	          ) R ON R.\"CustNo\" = IM.\"CustNo\" AND R.\"FacmNo\" = IM.\"FacmNo\"";
		sql += "				 								  AND R.\"Introducer\" = IM.\"Introducer\"";
		sql += "	WHERE  IM.\"SEQ\" =  1";
		sql += "	GROUP BY IM.\"WorkMonth\")";
		sql += " ";
		sql += "	SELECT \"WorkMonth\"";
		sql += "          ,SUM(NVL(\"Bonus\",0)) AS \"Bonus\"";
		sql += "	FROM ( SELECT D.\"WorkMonth\"";
		sql += "				 ,D.\"IntroAddBonus\" AS \"Bonus\"";
		sql += "		   FROM \"Data1\" D ";
		sql += "		   UNION ";
		sql += "		   SELECT \"Year\" * 100 + \"Month\" AS \"WorkMonth\"";
		sql += "			     , 0 AS \"Bonus\"";
		sql += "		   FROM \"CdWorkMonth\"";
		sql += " 		   WHERE \"Year\" = :iwkyear ";
		sql += " 	         AND \"Month\" <= :iwkmonth )";
		sql += " 	GROUP BY \"WorkMonth\"";
		sql += " 	ORDER BY \"WorkMonth\" ASC";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iwkyear", wkYear);
		query.setParameter("iwkmonth", wkMonth);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll5(TitaVo titaVo, int wkYear, int wkMonth) throws Exception {

		this.info("lW003.findAll5");

		this.info("wkYear=" + wkYear + ",wkMonth=" + wkMonth);

		String sql = " ";
		sql += "	SELECT \"Quarter\" AS \"Quarter\"";
		sql += "      	  ,CASE";
		sql += "      	     WHEN \"Bonus\" > 6000000 THEN (\"Bonus\" / 10000) * 5";
		sql += "      	     WHEN \"Bonus\" > 3000000 THEN (\"Bonus\" / 10000) * 4";
		sql += "      	   END AS \"Bonus\"";
		sql += "    FROM ( SELECT CASE";
		sql += "    				WHEN MOD(P.\"WorkMonth\",100) <=3 THEN '1'";
		sql += "    				WHEN MOD(P.\"WorkMonth\",100) <=6 THEN '2'";
		sql += "    				WHEN MOD(P.\"WorkMonth\",100) <=9 THEN '3'";
		sql += "    				WHEN MOD(P.\"WorkMonth\",100) <=12 THEN '4'";
		sql += "    				ELSE '5'";
		sql += "    			  END AS \"Quarter\"";
		sql += "    			 ,SUM(NVL(P.\"DrawdownAmt\",0)) AS \"Bonus\"";
		sql += "		   FROM \"PfItDetail\" P";
		sql += "		   WHERE TRUNC(\"WorkMonth\" / 100) = :iwkyear";
		sql += "		     AND MOD(\"WorkMonth\" ,100) <= :iwkmonth";
		sql += "		     AND \"DistCode\" IN ('A0X100','A0X200','A0X300','A0X400','A0X500')";
		sql += "		   GROUP BY CASE";
		sql += "    				  WHEN MOD(P.\"WorkMonth\",100) <=3 THEN '1'";
		sql += "    				  WHEN MOD(P.\"WorkMonth\",100) <=6 THEN '2'";
		sql += "    				  WHEN MOD(P.\"WorkMonth\",100) <=9 THEN '3'";
		sql += "    				  WHEN MOD(P.\"WorkMonth\",100) <=12 THEN '4'";
		sql += "    				  ELSE '5'";
		sql += "    			    END )";
		sql += "	ORDER BY \"Quarter\" ASC";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iwkyear", wkYear);
		query.setParameter("iwkmonth", wkMonth);
		return this.convertToMap(query.getResultList());
	}

}