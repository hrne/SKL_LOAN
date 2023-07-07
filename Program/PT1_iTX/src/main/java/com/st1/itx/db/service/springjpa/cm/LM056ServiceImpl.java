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
import com.st1.itx.util.date.DateUtil;

@Service
@Repository
/* 逾期放款明細 */
public class LM056ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	DateUtil dateUtil;

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	/**
	 * 查詢資料
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * @param isAllData 是否為產出明細
	 * @return 
	 * @throws Exception 
	 * 
	 */
	public List<Map<String, String>> findAll(TitaVo titaVo, int yearMonth, String isAllData) throws Exception {

		this.info("lM056.findAll");
		this.info(" yymm=" + yearMonth);

		String sql = " ";		
		sql+=" SELECT Mlb.\"CustNo\"                 AS \"CustNo\"";
		sql+="      , \"Fn_ParseEOL\"( C.\"CustName\", 0 ) AS \"CustName\"";
		sql+="      , C.\"CustId\"                   AS \"CustId\"";
		sql+="      , SUM(Mlb.\"LoanBalance\")       AS \"LoanBalance\"";
		sql+="      , CASE";
		sql+="         WHEN Syno.\"CustNo\" IS NOT NULL THEN '聯貸'";
		sql+="         WHEN S.\"RelName\" IS NOT NULL THEN '*'";
		sql+="         WHEN Mlb2.\"CustNo\" IS NOT NULL THEN '**'";
		sql+="         ELSE ' '";
		sql+="        END AS \"KIND\"";
		sql+="      , Decode( S.\"BusTitle\", NULL , ";
		sql+="             Decode( S.\"RelName\", NULL , ' ' , '為本公司負責人' || '(' || S.\"HeadTitle\" || ";
		sql+="                     Decode( S.\"RelTitle\", '本人' , ' ' , S.\"HeadName\" ) || ')' ||";
		sql+="                     Decode( S.\"RelTitle\", '本人' , ' ' , '之' || S.\"RelTitle\" )";
		sql+="             ), '該公司' || S.\"BusTitle\" || '(' || S.\"RelName\" || ')' || '為本公司' || S.\"HeadTitle\" || '之' || S.\"RelTitle\"";
		sql+="       ) AS \"isRelt\"";
		sql+=" FROM \"MonthlyLoanBal\" Mlb";
		sql+=" LEFT JOIN \"MonthlyFacBal\"  Mfb ON Mfb.\"CustNo\" = Mlb.\"CustNo\"";
		sql+="                                AND Mfb.\"FacmNo\" = Mlb.\"FacmNo\"";
		sql+="                                AND  Mfb.\"YearMonth\" = Mlb.\"YearMonth\"";
		sql+=" LEFT JOIN (";
		sql+="     SELECT * FROM (";
		sql+="         SELECT M.\"CustNo\"";
		sql+="              , SUM(F.\"LineAmt\") AS \"LineAmt\"";
		sql+="         FROM \"MonthlyFacBal\"  M";
		sql+="         LEFT JOIN \"FacMain\"        F ON F.\"CustNo\" = M.\"CustNo\"";
		sql+="                                  AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql+="         WHERE M.\"YearMonth\" = :yymm";
		sql+="           AND M.\"PrinBalance\" > 0";
		sql+="         GROUP BY M.\"CustNo\"";
		sql+="     ) M";
		sql+="     WHERE M.\"LineAmt\" > 100000000";
		sql+=" ) Mlb2 ON Mlb2.\"CustNo\" = Mfb.\"CustNo\"";
		sql+=" LEFT JOIN \"CustMain\"        C ON C.\"CustNo\" = Mlb.\"CustNo\"";
		sql+=" LEFT JOIN \"LoanBorMain\"     L ON L.\"CustNo\" = Mlb.\"CustNo\"";
		sql+="                              AND L.\"FacmNo\" = Mlb.\"FacmNo\"";
		sql+="                              AND L.\"BormNo\" = Mlb.\"BormNo\"";
		sql+=" LEFT JOIN \"FacMain\"        F2 ON F2.\"CustNo\" = Mlb.\"CustNo\"";
		sql+="                              AND F2.\"FacmNo\" = Mlb.\"FacmNo\"";
		sql+="                              AND F2.\"LastBormNo\" = Mlb.\"BormNo\"";
		sql+=" LEFT JOIN \"FacCaseAppl\"     Fca ON Fca.\"ApplNo\" = F2.\"ApplNo\"";
		sql+=" LEFT JOIN (";
		sql+="     SELECT S.*";
		sql+="           , ROW_NUMBER() OVER(PARTITION BY \"RptId\"";
		sql+="                     ORDER BY To_Number(Cc.\"Code\")) AS \"Seq\"";
		sql+="     FROM (";
		sql+="         SELECT Decode(\"BusId\", '-', ";
		sql+="                     Decode( \"RelId\", '-' , \"HeadName\" , \"RelName\"";
		sql+="                     ), \"BusName\" ) AS \"CustName\"";
		sql+="              , To_Char( Decode( \"BusId\", '-' , ";
		sql+="                             Decode( \"RelId\", '-' , \"HeadId\" , \"RelId\" ), \"BusId\" ) ";
		sql+="                         ) AS \"RptId\"";
		sql+="              , \"RelWithCompany\" AS \"Rel\"";
		sql+="              , \"HeadName\"";
		sql+="              , \"HeadTitle\"";
		sql+="              , \"RelName\"";
		sql+="              , \"RelTitle\"";
		sql+="              , \"BusTitle\"";
		sql+="         FROM \"LifeRelHead\"";
		sql+="         WHERE \"RelWithCompany\" = 'A'";
		sql+="               AND Trunc(\"AcDate\" / 100) = :yymm";
		sql+="               AND \"LoanBalance\" > 0";
		sql+="         UNION";
		sql+="         SELECT \"EmpName\"            AS \"CustName\"";
		sql+="              , To_Char(\"EmpId\")     AS \"RptId\"";
		sql+="              , 'N'                  AS \"Rel\"";
		sql+="              , NULL                 AS \"HeadName\"";
		sql+="              , NULL                 AS \"HeadTitle\"";
		sql+="              , NULL                 AS \"RelName\"";
		sql+="              , NULL                 AS \"RelTitle\"";
		sql+="              , NULL                 AS \"BusTitle\"";
		sql+="         FROM \"LifeRelEmp\"";
		sql+="         WHERE Trunc(\"AcDate\" / 100) = :yymm";
		sql+="     ) S";
		sql+="     LEFT JOIN \"CdCode\" Cc ON Cc.\"DefCode\" = 'RelsCode'";
		sql+="                          AND Cc.\"Item\" LIKE S.\"HeadTitle\"";
		sql+=" ) S ON S.\"RptId\" = C.\"CustId\"";
		sql+="        AND S.\"Seq\" = 1";
		sql+=" LEFT JOIN (";
		sql+="     SELECT DISTINCT \"CustNo\"";
		sql+="     FROM \"AcDetail\"";
		sql+="     WHERE \"AcctCode\" IN (";
		sql+="         'F12'";
		sql+="       , 'F27'";
		sql+="     )";
		sql+=" ) Syno ON Syno.\"CustNo\" = Mlb.\"CustNo\"";
		sql+=" WHERE Mlb.\"YearMonth\" = :yymm";
		sql+="       AND Mlb.\"LoanBalance\" > 0";
		if (isAllData == "N") {
			sql+="      AND CASE";
			sql+="             WHEN Syno.\"CustNo\" IS NOT NULL THEN '聯貸'";
			sql+="             WHEN S.\"RelName\" IS NOT NULL THEN '*'";
			sql+="             WHEN Mlb2.\"CustNo\" IS NOT NULL THEN '**'";
			sql+="         ELSE ' ' END <> ' '";
		}
		sql+=" GROUP BY Mlb.\"CustNo\"";
		sql+="        , \"Fn_ParseEOL\"( C.\"CustName\", 0 )";
		sql+="        , C.\"CustId\"";
		sql+="        , CASE";
		sql+="             WHEN Syno.\"CustNo\" IS NOT NULL THEN '聯貸'";
		sql+="             WHEN S.\"RelName\" IS NOT NULL THEN '*'";
		sql+="             WHEN Mlb2.\"CustNo\" IS NOT NULL THEN '**'";
		sql+="         ELSE ' ' END";
		sql+="       , Decode( S.\"BusTitle\", NULL , ";
		sql+="             Decode( S.\"RelName\", NULL , ' ' , '為本公司負責人' || '(' || S.\"HeadTitle\" || ";
		sql+="                     Decode( S.\"RelTitle\", '本人' , ' ' , S.\"HeadName\" ) || ')' ||";
		sql+="                     Decode( S.\"RelTitle\", '本人' , ' ' , '之' || S.\"RelTitle\" )";
		sql+="             ), '該公司' || S.\"BusTitle\" || '(' || S.\"RelName\" || ')' || '為本公司' || S.\"HeadTitle\" || '之' || S.\"RelTitle\"";
		sql+="       ) ";
		sql+=" ORDER BY \"LoanBalance\" DESC ";
		
		
		
		
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", yearMonth);

		return this.convertToMap(query);
	}

	/**
	 * 查詢資料
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * @return 
	 * @throws Exception 
	 * 
	 */
	public List<Map<String, String>> findAll2(TitaVo titaVo, int yearMonth) throws Exception {

		this.info("lM056.findAll2");
		this.info("yymm=" + yearMonth);

		String sql = " ";
		sql += "	WITH \"roundData\" AS (";
		sql += "		SELECT ROUND(SUM(NVL(I.\"AccumDPAmortized\",0)),0) AS \"LnAmt\"";
		sql += "		FROM \"Ias39IntMethod\" I";
		sql += "		LEFT JOIN \"MonthlyLoanBal\" MLB ON MLB.\"YearMonth\" = I.\"YearMonth\"";
		sql += "										AND MLB.\"CustNo\" = I.\"CustNo\"";
		sql += "										AND MLB.\"FacmNo\" = I.\"FacmNo\"";
		sql += "										AND MLB.\"BormNo\" = I.\"BormNo\"";
		sql += "		WHERE I.\"YearMonth\" = :yymm ";
		sql += "		  AND MLB.\"AcctCode\" <> 990 ";
		sql += "	),\"tempTotal\" AS (";
		sql += "		SELECT 'H37' AS \"Column\"";
		sql += "			  ,SUM(M.\"PrinBalance\") AS \"Value\"";
		sql += "		FROM \"MonthlyFacBal\" M";
		sql += "		WHERE M.\"YearMonth\" = :yymm ";
		sql += "		  AND M.\"PrinBalance\" > 0 ";
		sql += "		GROUP BY DECODE(M.\"EntCode\",'1','G6','G7')";
		sql += "		UNION";
		sql += "		SELECT 'H37' AS \"Column\"";
		sql += "			  ,\"LoanBal\" AS \"Value\"";
		sql += "		FROM \"MonthlyLM052AssetClass\"";
		sql += "		WHERE \"YearMonth\" = :yymm ";
		sql += "		  AND \"AssetClassNo\" = 62 ";
		sql += "		UNION";
		sql += "		SELECT 'H37' AS \"Column\"";
		sql += "			  ,NVL(R.\"LnAmt\",0) AS \"Value\"";
		sql += "		FROM \"roundData\" R";
		sql += "		UNION";
		sql += "		SELECT CASE";
		sql += "				 WHEN M.\"AcctCode\" <> '990' AND M.\"OvduTerm\" >=3 ";
		sql += "				 THEN 'D41'";
		sql += "				 WHEN M.\"AcctCode\" = '990' ";
		sql += "				 THEN 'D40'";
		sql += "			   ELSE 'N' END AS \"Column\"";
		sql += "			  ,SUM(M.\"PrinBalance\") AS \"Value\"";
		sql += "		FROM \"MonthlyFacBal\" M";
		sql += "		WHERE M.\"YearMonth\" = :yymm ";
		sql += "		  AND M.\"PrinBalance\" > 0 ";
		sql += "		GROUP BY CASE";
		sql += "				   WHEN M.\"AcctCode\" <> '990' AND M.\"OvduTerm\" >=3 ";
		sql += "				   THEN 'D41'";
		sql += "				   WHEN M.\"AcctCode\" = '990' ";
		sql += "				   THEN 'D40'";
		sql += "			     ELSE 'N' END";
		sql += "	) ";
		sql += "	SELECT \"Column\" AS \"Column\" ";
		sql += "	      ,SUM(\"Value\") AS \"Value\" ";
		sql += "	FROM \"tempTotal\"";
		sql += "	GROUP BY \"Column\" ";
	
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", yearMonth);

		return this.convertToMap(query);
	}
}
