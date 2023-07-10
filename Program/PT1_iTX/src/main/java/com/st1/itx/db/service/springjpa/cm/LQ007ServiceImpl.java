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
import com.st1.itx.util.parse.Parse;

@Service
@Repository
public class LQ007ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {

	}
	/**
	 * 查當年度
	 * @param titaVo 
	 * @return 
	 * @throws Exception 
	 * */
	public List<Map<String, String>> findThisYear(TitaVo titaVo) throws Exception {
		this.info("lQ007.findThisYear ");

		int entdy = (parse.stringToInteger(titaVo.getParam("ENTDY")) + 19110000);

		int endYM = entdy / 100;
		int startYM = endYM / 100 * 100 + 1;

		this.info("entdy =" + entdy);
		this.info("startYM =" + startYM);
		this.info("endYM =" + endYM);

		String sql = "	";
		// -- 所有專案放款餘額
		sql += "    WITH \"tmpBal\" AS (";
		sql += "        SELECT *";
		sql += "        FROM \"CdComm\"";
		sql += "        WHERE \"CdType\" = '02'";
		sql += "              AND \"CdItem\" = '01'";
		sql += "              AND Trunc(\"EffectDate\" / 100) BETWEEN :Sym AND :Eym";
		sql += "              AND Mod(";
		sql += "            Trunc(\"EffectDate\" / 100), 100";
		sql += "        ) IN ( 3 , 6 , 9 , 12 )";
		sql += "    ), \"tmpMainBal\" AS (";
		sql += "        SELECT \"ProdNo\"";
		sql += "             , \"YearMonth\"";
		sql += "             , SUM(\"LoanBal\") AS \"Bal\"";
		sql += "        FROM (";
		sql += "            SELECT 'AA'                                            AS \"ProdNo\"";
		sql += "                 , JSON_VALUE(\"JsonFields\", '$.\"YearMonth\"')       AS \"YearMonth\"";
		sql += "                 , NVL(JSON_VALUE(\"JsonFields\", '$.\"340LoanBal\"'),0)      AS \"LoanBal\"";
		sql += "            FROM \"tmpBal\"";
		sql += "            UNION";
		sql += "            SELECT 'IA'                                           AS \"ProdNo\"";
		sql += "                 , JSON_VALUE(\"JsonFields\", '$.\"YearMonth\"')      AS \"YearMonth\"";
		sql += "                 ,  NVL(JSON_VALUE(\"JsonFields\", '$.\"IALoanBal\"') ,0)     AS \"LoanBal\"";
		sql += "            FROM \"tmpBal\"";
		sql += "            UNION";
		sql += "            SELECT 'IB'                                           AS \"ProdNo\"";
		sql += "                 , JSON_VALUE(\"JsonFields\", '$.\"YearMonth\"')      AS \"YearMonth\"";
		sql += "                 ,  NVL(JSON_VALUE(\"JsonFields\", '$.\"IBLoanBal\"'),0)      AS \"LoanBal\"";
		sql += "            FROM \"tmpBal\"";
		sql += "            UNION";
		sql += "            SELECT 'IC'                                           AS \"ProdNo\"";
		sql += "                 , JSON_VALUE(\"JsonFields\", '$.\"YearMonth\"')      AS \"YearMonth\"";
		sql += "                 ,  NVL(JSON_VALUE(\"JsonFields\", '$.\"ICLoanBal\"'),0)      AS \"LoanBal\"";
		sql += "            FROM \"tmpBal\"";
		sql += "            UNION";
		sql += "            SELECT 'ID'                                           AS \"ProdNo\"";
		sql += "                 , JSON_VALUE(\"JsonFields\", '$.\"YearMonth\"')      AS \"YearMonth\"";
		sql += "                 ,  NVL(JSON_VALUE(\"JsonFields\", '$.\"IDLoanBal\"'),0)      AS \"LoanBal\"";
		sql += "            FROM \"tmpBal\"";
		sql += "            UNION";
		sql += "            SELECT 'ID'                                           AS \"ProdNo\"";
		sql += "                 , JSON_VALUE(\"JsonFields\", '$.\"YearMonth\"')      AS \"YearMonth\"";
		sql += "                 ,  NVL(JSON_VALUE(\"JsonFields\", '$.\"IELoanBal\"'),0)      AS \"LoanBal\"";
		sql += "            FROM \"tmpBal\"";
		sql += "            UNION";
		sql += "            SELECT 'IF'                                           AS \"ProdNo\"";
		sql += "                 , JSON_VALUE(\"JsonFields\", '$.\"YearMonth\"')      AS \"YearMonth\"";
		sql += "                 ,  NVL(JSON_VALUE(\"JsonFields\", '$.\"IFLoanBal\"'),0)      AS \"LoanBal\"";
		sql += "            FROM \"tmpBal\"";
		sql += "            UNION";
		sql += "            SELECT 'IF'                                           AS \"ProdNo\"";
		sql += "                 , JSON_VALUE(\"JsonFields\", '$.\"YearMonth\"')      AS \"YearMonth\"";
		sql += "                 ,  NVL(JSON_VALUE(\"JsonFields\", '$.\"IGLoanBal\"'),0)      AS \"LoanBal\"";
		sql += "            FROM \"tmpBal\"";
		sql += "            UNION";
		sql += "            SELECT 'IH'                                           AS \"ProdNo\"";
		sql += "                 , JSON_VALUE(\"JsonFields\", '$.\"YearMonth\"')      AS \"YearMonth\"";
		sql += "                 ,  NVL(JSON_VALUE(\"JsonFields\", '$.\"IHLoanBal\"'),0)      AS \"LoanBal\"";
		sql += "            FROM \"tmpBal\"";
		sql += "            UNION";
		sql += "            SELECT 'IH'                                           AS \"ProdNo\"";
		sql += "                 , JSON_VALUE(\"JsonFields\", '$.\"YearMonth\"')      AS \"YearMonth\"";
		sql += "                 ,  NVL(JSON_VALUE(\"JsonFields\", '$.\"IILoanBal\"'),0)      AS \"LoanBal\"";
		sql += "            FROM \"tmpBal\"";
		sql += "            UNION";
		sql += "            SELECT 'ZZ'                                            AS \"ProdNo\"";
		sql += "                 , JSON_VALUE(\"JsonFields\", '$.\"YearMonth\"')       AS \"YearMonth\"";
		sql += "                 ,  NVL(JSON_VALUE(\"JsonFields\", '$.\"921LoanBal\"'),0)      AS \"LoanBal\"";
		sql += "            FROM \"tmpBal\"";
		sql += "        )";
		sql += "        GROUP BY \"ProdNo\"";
		sql += "               , \"YearMonth\"";
		// -- 首購利息收入
		sql += "    ), \"tmp340Int\" AS (";
		sql += "        SELECT 'AA'               AS \"ProdNo\"";
		sql += "             , CASE";
		sql += "                WHEN Mod( R.\"YearMonth\", 100 ) IN ( 1 , 2 , 3 ) ";
		sql += "                THEN Trunc(R.\"YearMonth\" / 100) * 100 + 3";
		sql += "                WHEN Mod( R.\"YearMonth\", 100 ) IN ( 4 , 5 , 6 ) ";
		sql += "                THEN Trunc(R.\"YearMonth\" / 100) * 100 + 6";
		sql += "                WHEN Mod( R.\"YearMonth\", 100 ) IN ( 7 , 8 , 9) ";
		sql += "                THEN Trunc(R.\"YearMonth\" / 100) * 100 + 9";
		sql += "                WHEN Mod( R.\"YearMonth\", 100 ) IN ( 10 , 11 , 12 ) ";
		sql += "                THEN Trunc(R.\"YearMonth\" / 100) * 100 + 12";
		sql += "              END AS \"YearMonth\"";
		sql += "             , SUM(R.\"Int\")       AS \"Int\"";
		sql += "        FROM (";
		sql += "            SELECT Trunc(A.\"AcDate\" / 100) AS \"YearMonth\"";
		sql += "               , Round( SUM(Decode( A.\"DbCr\", 'C', A.\"TxAmt\", - A.\"TxAmt\" ) / 1000)) * 1000 AS \"Int\"";
		sql += "            FROM \"AcDetail\"       A";
		sql += "            LEFT JOIN \"FacMain\"        F ON F.\"CustNo\" = A.\"CustNo\"";
		sql += "                                     AND F.\"FacmNo\" = A.\"FacmNo\"";
		sql += "                                     AND F.\"CustNo\" <> 0";
		sql += "            LEFT JOIN \"MonthlyFacBal\"  L ON L.\"CustNo\" = A.\"CustNo\"";
		sql += "                                           AND L.\"FacmNo\" = A.\"FacmNo\"";
		sql += "                                           AND L.\"YearMonth\" = Trunc(A.\"AcDate\" / 100)";
		sql += "            WHERE Trunc(A.\"AcDate\" / 100) BETWEEN :Sym AND :Eym";
		sql += "                  AND Decode( L.\"Status\", 5 , '990' , F.\"AcctCode\" ) = '340'";
		sql += "                  AND A.\"AcSubBookCode\" = '00A'";
		sql += "                  AND A.\"AcctCode\" IN ( 'IC1' , 'IC2' , 'IC3' , 'IC4' , 'IOV' , 'IOP' )";
		sql += "            GROUP BY Trunc(A.\"AcDate\" / 100)";
		sql += "            UNION";
		sql += "            SELECT Trunc(A.\"AcDate\" / 100) AS \"YearMonth\"";
		sql += "                 , Round( SUM(Decode( A.\"DbCr\", 'C', A.\"TxAmt\", - A.\"TxAmt\" ) / 1000)) * 1000 AS \"Int\"";
		sql += "            FROM \"AcDetail\"       A";
		sql += "            LEFT JOIN \"FacMain\"        F ON F.\"CustNo\" = A.\"CustNo\"";
		sql += "                                     AND F.\"FacmNo\" = A.\"FacmNo\"";
		sql += "                                     AND F.\"CustNo\" <> 0";
		sql += "            LEFT JOIN \"MonthlyFacBal\"  L ON L.\"CustNo\" = A.\"CustNo\"";
		sql += "                                           AND L.\"FacmNo\" = A.\"FacmNo\"";
		sql += "                                           AND L.\"YearMonth\" = Trunc(A.\"AcDate\" / 100)";
		sql += "                  WHERE Trunc(A.\"AcDate\" / 100) BETWEEN :Sym AND :Eym";
		sql += "                  AND Decode( L.\"Status\", 5 , '990' , F.\"AcctCode\" ) = '340'";
		sql += "                  AND A.\"AcSubBookCode\" = '201'";
		sql += "                  AND A.\"AcctCode\" IN ( 'IC1' , 'IC2' , 'IC3' , 'IC4' , 'IOV' , 'IOP' )";
		sql += "            GROUP BY Trunc(A.\"AcDate\" / 100)";
		sql += "        ) R";
		sql += "        GROUP BY 'AA'";
		sql += "               , CASE";
		sql += "                    WHEN Mod( R.\"YearMonth\", 100 ) IN ( 1 , 2 , 3 ) ";
		sql += "                    THEN Trunc(R.\"YearMonth\" / 100) * 100 + 3";
		sql += "                    WHEN Mod( R.\"YearMonth\", 100 ) IN ( 4 , 5 , 6 ) ";
		sql += "                    THEN Trunc(R.\"YearMonth\" / 100) * 100 + 6";
		sql += "                    WHEN Mod( R.\"YearMonth\", 100 ) IN ( 7 , 8 , 9) ";
		sql += "                    THEN Trunc(R.\"YearMonth\" / 100) * 100 + 9";
		sql += "                    WHEN Mod( R.\"YearMonth\", 100 ) IN ( 10 , 11 , 12 ) ";
		sql += "                    THEN Trunc(R.\"YearMonth\" / 100) * 100 + 12";
		sql += "                  END ";
		// -- 政府優惠商品利息收入
		sql += "    ), \"tmpProdInt\" AS (";
		sql += "        SELECT CASE";
		sql += "                WHEN F.\"ProdNo\" IN ( 'IE' ) THEN 'ID'";
		sql += "                WHEN F.\"ProdNo\" IN ( 'IG' ) THEN 'IF'";
		sql += "                WHEN F.\"ProdNo\" IN ( 'II'  ) THEN 'IH'";
		sql += "                ELSE F.\"ProdNo\"";
		sql += "               END AS \"ProdNo\"";
		sql += "             , CASE";
		sql += "                WHEN Mod( Trunc(L.\"AcDate\" / 100), 100 ) IN ( 1 , 2 , 3 ) ";
		sql += "                THEN Trunc(L.\"AcDate\" / 10000) * 100 + 3";
		sql += "                WHEN Mod( Trunc(L.\"AcDate\" / 100), 100 ) IN ( 4 , 5 , 6 )  ";
		sql += "                THEN Trunc(L.\"AcDate\" / 10000) * 100 + 6";
		sql += "                WHEN Mod( Trunc(L.\"AcDate\" / 100), 100 ) IN ( 7 , 8 , 9 ) ";
		sql += "                THEN Trunc(L.\"AcDate\" / 10000) * 100 + 9";
		sql += "                WHEN Mod( Trunc(L.\"AcDate\" / 100), 100 ) IN ( 10 , 11 , 12 ) ";
		sql += "                THEN Trunc(L.\"AcDate\" / 10000) * 100 + 12";
		sql += "              END AS \"YearMonth\"";
		sql += "             , SUM(\"Interest\") AS \"Int\"";
		sql += "        FROM \"LoanBorTx\"  L";
		sql += "        LEFT JOIN \"FacMain\"    F ON F.\"CustNo\" = L.\"CustNo\"";
		sql += "                                 AND F.\"FacmNo\" = L.\"FacmNo\"";
		sql += "        WHERE Nvl( F.\"ProdNo\", ' ' ) BETWEEN 'IA' AND 'II'";
		sql += "              AND Trunc(L.\"AcDate\" / 100) BETWEEN :Sym AND :Eym";
		sql += "              AND L.\"TitaHCode\" = '0'";
		sql += "        GROUP BY CASE";
		sql += "                    WHEN F.\"ProdNo\" IN ( 'IE' ) THEN 'ID'";
		sql += "                    WHEN F.\"ProdNo\" IN ( 'IG' ) THEN 'IF'";
		sql += "                    WHEN F.\"ProdNo\" IN ( 'II'  ) THEN 'IH'";
		sql += "                    ELSE F.\"ProdNo\"";
		sql += "                   END";
		sql += "                 , CASE";
		sql += "                    WHEN Mod( Trunc(L.\"AcDate\" / 100), 100 ) IN ( 1 , 2 , 3 ) ";
		sql += "                    THEN Trunc(L.\"AcDate\" / 10000) * 100 + 3";
		sql += "                    WHEN Mod( Trunc(L.\"AcDate\" / 100), 100 ) IN ( 4 , 5 , 6 )  ";
		sql += "                    THEN Trunc(L.\"AcDate\" / 10000) * 100 + 6";
		sql += "                    WHEN Mod( Trunc(L.\"AcDate\" / 100), 100 ) IN ( 7 , 8 , 9 ) ";
		sql += "                    THEN Trunc(L.\"AcDate\" / 10000) * 100 + 9";
		sql += "                    WHEN Mod( Trunc(L.\"AcDate\" / 100), 100 ) IN ( 10 , 11 , 12 ) ";
		sql += "                    THEN Trunc(L.\"AcDate\" / 10000) * 100 + 12";
		sql += "                  END ";
		// -- F15 F16
		sql += "    ), \"tmpF1516Int\" AS (";
		sql += "        SELECT 'ZZ' AS \"ProdNo\"";
		sql += "              , CASE";
		sql += "                    WHEN Mod( Trunc(\"AcDate\" / 100), 100 ) IN ( 1 , 2 , 3 ) ";
		sql += "                    THEN Trunc(\"AcDate\" / 10000) * 100 + 3";
		sql += "                    WHEN Mod( Trunc(\"AcDate\" / 100), 100 ) IN ( 4 , 5 , 6 )  ";
		sql += "                    THEN Trunc(\"AcDate\" / 10000) * 100 + 6";
		sql += "                    WHEN Mod( Trunc(\"AcDate\" / 100), 100 ) IN ( 7 , 8 , 9 ) ";
		sql += "                    THEN Trunc(\"AcDate\" / 10000) * 100 + 9";
		sql += "                    WHEN Mod( Trunc(\"AcDate\" / 100), 100 ) IN ( 10 , 11 , 12 ) ";
		sql += "                    THEN Trunc(\"AcDate\" / 10000) * 100 + 12";
		sql += "                  END AS \"YearMonth\"";
		sql += "             , SUM(Decode( \"DbCr\", 'C' , \"TxAmt\", - \"TxAmt\" )) AS \"Int\"";
		sql += "        FROM \"AcDetail\"";
		sql += "        WHERE Trunc(\"AcDate\" / 100) BETWEEN :Sym AND :Eym";
		sql += "              AND \"AcctCode\" IN ('F15' , 'F16' )";
		sql += "        GROUP BY 'ZZ'";
		sql += "              , CASE";
		sql += "                    WHEN Mod( Trunc(\"AcDate\" / 100), 100 ) IN ( 1 , 2 , 3 ) ";
		sql += "                    THEN Trunc(\"AcDate\" / 10000) * 100 + 3";
		sql += "                    WHEN Mod( Trunc(\"AcDate\" / 100), 100 ) IN ( 4 , 5 , 6 )  ";
		sql += "                    THEN Trunc(\"AcDate\" / 10000) * 100 + 6";
		sql += "                    WHEN Mod( Trunc(\"AcDate\" / 100), 100 ) IN ( 7 , 8 , 9 ) ";
		sql += "                    THEN Trunc(\"AcDate\" / 10000) * 100 + 9";
		sql += "                    WHEN Mod( Trunc(\"AcDate\" / 100), 100 ) IN ( 10 , 11 , 12 ) ";
		sql += "                    THEN Trunc(\"AcDate\" / 10000) * 100 + 12";
		sql += "                  END";
		// --合併利息收入
		sql += "    ), \"tmpMainInt\" AS (";
		sql += "        SELECT \"ProdNo\"";
		sql += "             , CASE";
		sql += "                WHEN Mod( \"YearMonth\", 100 ) IN ( 1 , 2 , 3 ) ";
		sql += "                THEN Trunc(\"YearMonth\" / 100) * 100 + 3";
		sql += "                WHEN Mod( \"YearMonth\", 100 ) IN ( 4 , 5 , 6 ) ";
		sql += "                THEN Trunc(\"YearMonth\" / 100) * 100 + 6";
		sql += "                WHEN Mod( \"YearMonth\", 100 ) IN ( 7 , 8 , 9) ";
		sql += "                THEN Trunc(\"YearMonth\" / 100) * 100 + 9";
		sql += "                WHEN Mod( \"YearMonth\", 100 ) IN ( 10 , 11 , 12 ) ";
		sql += "                THEN Trunc(\"YearMonth\" / 100) * 100 + 12";
		sql += "              END AS \"YearMonth\"";
		sql += "             , NVL(SUM(\"Int\"),0) AS \"Int\"";
		sql += "        FROM (";
		sql += "            SELECT * FROM \"tmp340Int\"";
		sql += "            UNION";
		sql += "            SELECT * FROM \"tmpProdInt\"";
		sql += "            UNION";
		sql += "            SELECT * FROM \"tmpF1516Int\"";
		sql += "        )";
		sql += "        GROUP BY \"ProdNo\"";
		sql += "                , CASE";
		sql += "                    WHEN Mod( \"YearMonth\", 100 ) IN ( 1 , 2 , 3 ) ";
		sql += "                    THEN Trunc(\"YearMonth\" / 100) * 100 + 3";
		sql += "                    WHEN Mod( \"YearMonth\", 100 ) IN ( 4 , 5 , 6 ) ";
		sql += "                    THEN Trunc(\"YearMonth\" / 100) * 100 + 6";
		sql += "                    WHEN Mod( \"YearMonth\", 100 ) IN ( 7 , 8 , 9) ";
		sql += "                    THEN Trunc(\"YearMonth\" / 100) * 100 + 9";
		sql += "                    WHEN Mod( \"YearMonth\", 100 ) IN ( 10 , 11 , 12 ) ";
		sql += "                    THEN Trunc(\"YearMonth\" / 100) * 100 + 12";
		sql += "                  END ";
		sql += "    )";
		sql += "    SELECT M.\"ProdNo\" AS \"ProdNoShow\"";
		sql += "         , M.\"YearMonth\"    AS \"VisibleMonth\"";
		sql += "         , M.\"Bal\"          AS \"BalSum\"";
		sql += "         , I.\"Int\"          AS \"IntSum\"";
		sql += "    FROM \"tmpMainBal\"  M";
		sql += "    LEFT JOIN \"tmpMainInt\"  I ON I.\"ProdNo\" = M.\"ProdNo\"";
		sql += "                                AND I.\"YearMonth\" = M.\"YearMonth\"";
		sql += "    ORDER BY M.\"YearMonth\" ASC";
		sql += "           , M.\"ProdNo\" ASC";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("Sym", startYM);
		query.setParameter("Eym", endYM);

		return this.convertToMap(query);
	}
	/**
	 * 查非當年度(僅查12月份)
	 * @param titaVo 
	 * @return 
	 * @throws Exception 
	 * */
	public List<Map<String, String>> findNotThisYear(TitaVo titaVo) throws Exception {
		this.info("lQ007.findNotThisYear ");

		int entdy = parse.stringToInteger(titaVo.getParam("ENTDY")) + 19110000;

		int sYear = entdy / 10000 - 3;
		int eYear = sYear + 2;

		int startYM = sYear * 100 + 1;
		int endYM = eYear * 100 + 12;

		this.info("entdy =" + entdy);
		this.info("startYM =" + startYM);
		this.info("endYM =" + endYM);

		String sql = "	";
		// -- 所有專案放款餘額
		sql += "    WITH \"tmpBal\" AS (";
		sql += "        SELECT *";
		sql += "        FROM \"CdComm\"";
		sql += "        WHERE \"CdType\" = '02'";
		sql += "              AND \"CdItem\" = '01'";
		sql += "              AND Trunc(\"EffectDate\" / 100) BETWEEN :Sym AND :Eym";
		sql += "              AND Mod(";
		sql += "            Trunc(\"EffectDate\" / 100), 100";
		sql += "        ) IN ( 12 )";
		sql += "    ), \"tmpMainBal\" AS (";
		sql += "        SELECT \"ProdNo\"";
		sql += "             , \"YearMonth\"";
		sql += "             , SUM(\"LoanBal\") AS \"Bal\"";
		sql += "        FROM (";
		sql += "            SELECT 'AA'                                            AS \"ProdNo\"";
		sql += "                 , JSON_VALUE(\"JsonFields\", '$.\"YearMonth\"')       AS \"YearMonth\"";
		sql += "                 , NVL(JSON_VALUE(\"JsonFields\", '$.\"340LoanBal\"'),0)      AS \"LoanBal\"";
		sql += "            FROM \"tmpBal\"";
		sql += "            UNION";
		sql += "            SELECT 'IA'                                           AS \"ProdNo\"";
		sql += "                 , JSON_VALUE(\"JsonFields\", '$.\"YearMonth\"')      AS \"YearMonth\"";
		sql += "                 ,  NVL(JSON_VALUE(\"JsonFields\", '$.\"IALoanBal\"') ,0)     AS \"LoanBal\"";
		sql += "            FROM \"tmpBal\"";
		sql += "            UNION";
		sql += "            SELECT 'IB'                                           AS \"ProdNo\"";
		sql += "                 , JSON_VALUE(\"JsonFields\", '$.\"YearMonth\"')      AS \"YearMonth\"";
		sql += "                 ,  NVL(JSON_VALUE(\"JsonFields\", '$.\"IBLoanBal\"'),0)      AS \"LoanBal\"";
		sql += "            FROM \"tmpBal\"";
		sql += "            UNION";
		sql += "            SELECT 'IC'                                           AS \"ProdNo\"";
		sql += "                 , JSON_VALUE(\"JsonFields\", '$.\"YearMonth\"')      AS \"YearMonth\"";
		sql += "                 ,  NVL(JSON_VALUE(\"JsonFields\", '$.\"ICLoanBal\"'),0)      AS \"LoanBal\"";
		sql += "            FROM \"tmpBal\"";
		sql += "            UNION";
		sql += "            SELECT 'ID'                                           AS \"ProdNo\"";
		sql += "                 , JSON_VALUE(\"JsonFields\", '$.\"YearMonth\"')      AS \"YearMonth\"";
		sql += "                 ,  NVL(JSON_VALUE(\"JsonFields\", '$.\"IDLoanBal\"'),0)      AS \"LoanBal\"";
		sql += "            FROM \"tmpBal\"";
		sql += "            UNION";
		sql += "            SELECT 'ID'                                           AS \"ProdNo\"";
		sql += "                 , JSON_VALUE(\"JsonFields\", '$.\"YearMonth\"')      AS \"YearMonth\"";
		sql += "                 ,  NVL(JSON_VALUE(\"JsonFields\", '$.\"IELoanBal\"'),0)      AS \"LoanBal\"";
		sql += "            FROM \"tmpBal\"";
		sql += "            UNION";
		sql += "            SELECT 'IF'                                           AS \"ProdNo\"";
		sql += "                 , JSON_VALUE(\"JsonFields\", '$.\"YearMonth\"')      AS \"YearMonth\"";
		sql += "                 ,  NVL(JSON_VALUE(\"JsonFields\", '$.\"IFLoanBal\"'),0)      AS \"LoanBal\"";
		sql += "            FROM \"tmpBal\"";
		sql += "            UNION";
		sql += "            SELECT 'IF'                                           AS \"ProdNo\"";
		sql += "                 , JSON_VALUE(\"JsonFields\", '$.\"YearMonth\"')      AS \"YearMonth\"";
		sql += "                 ,  NVL(JSON_VALUE(\"JsonFields\", '$.\"IGLoanBal\"'),0)      AS \"LoanBal\"";
		sql += "            FROM \"tmpBal\"";
		sql += "            UNION";
		sql += "            SELECT 'IH'                                           AS \"ProdNo\"";
		sql += "                 , JSON_VALUE(\"JsonFields\", '$.\"YearMonth\"')      AS \"YearMonth\"";
		sql += "                 ,  NVL(JSON_VALUE(\"JsonFields\", '$.\"IHLoanBal\"'),0)      AS \"LoanBal\"";
		sql += "            FROM \"tmpBal\"";
		sql += "            UNION";
		sql += "            SELECT 'IH'                                           AS \"ProdNo\"";
		sql += "                 , JSON_VALUE(\"JsonFields\", '$.\"YearMonth\"')      AS \"YearMonth\"";
		sql += "                 ,  NVL(JSON_VALUE(\"JsonFields\", '$.\"IILoanBal\"'),0)      AS \"LoanBal\"";
		sql += "            FROM \"tmpBal\"";
		sql += "            UNION";
		sql += "            SELECT 'ZZ'                                            AS \"ProdNo\"";
		sql += "                 , JSON_VALUE(\"JsonFields\", '$.\"YearMonth\"')       AS \"YearMonth\"";
		sql += "                 ,  NVL(JSON_VALUE(\"JsonFields\", '$.\"921LoanBal\"'),0)      AS \"LoanBal\"";
		sql += "            FROM \"tmpBal\"";
		sql += "        )";
		sql += "        GROUP BY \"ProdNo\"";
		sql += "               , \"YearMonth\"";
		// -- 首購利息收入
		sql += "    ), \"tmp340Int\" AS (";
		sql += "        SELECT 'AA'               AS \"ProdNo\"";
		sql += "             , R.\"YearMonth\" AS \"YearMonth\"";
		sql += "             , SUM(R.\"Int\")       AS \"Int\"";
		sql += "        FROM (";
		sql += "            SELECT Trunc(A.\"AcDate\" / 10000 ) * 100 + 12 AS \"YearMonth\"";
		sql += "               , Round( SUM(Decode( A.\"DbCr\", 'C', A.\"TxAmt\", - A.\"TxAmt\" ) / 1000)) * 1000 AS \"Int\"";
		sql += "            FROM \"AcDetail\"       A";
		sql += "            LEFT JOIN \"FacMain\"        F ON F.\"CustNo\" = A.\"CustNo\"";
		sql += "                                     AND F.\"FacmNo\" = A.\"FacmNo\"";
		sql += "                                     AND F.\"CustNo\" <> 0";
		sql += "            LEFT JOIN \"MonthlyFacBal\"  L ON L.\"CustNo\" = A.\"CustNo\"";
		sql += "                                           AND L.\"FacmNo\" = A.\"FacmNo\"";
		sql += "                                           AND L.\"YearMonth\" = Trunc(A.\"AcDate\" / 100)";
		sql += "            WHERE Trunc(A.\"AcDate\" / 100) BETWEEN :Sym AND :Eym";
		sql += "                  AND Decode( L.\"Status\", 5 , '990' , F.\"AcctCode\" ) = '340'";
		sql += "                  AND A.\"AcSubBookCode\" = '00A'";
		sql += "                  AND A.\"AcctCode\" IN ( 'IC1' , 'IC2' , 'IC3' , 'IC4' , 'IOV' , 'IOP' )";
		sql += "            GROUP BY Trunc(A.\"AcDate\" / 10000) * 100 + 12 ";
		sql += "            UNION";
		sql += "            SELECT Trunc(A.\"AcDate\" / 10000) * 100 + 12 AS \"YearMonth\"";
		sql += "                 , Round( SUM(Decode( A.\"DbCr\", 'C', A.\"TxAmt\", - A.\"TxAmt\" ) / 1000)) * 1000 AS \"Int\"";
		sql += "            FROM \"AcDetail\"       A";
		sql += "            LEFT JOIN \"FacMain\"        F ON F.\"CustNo\" = A.\"CustNo\"";
		sql += "                                     AND F.\"FacmNo\" = A.\"FacmNo\"";
		sql += "                                     AND F.\"CustNo\" <> 0";
		sql += "            LEFT JOIN \"MonthlyFacBal\"  L ON L.\"CustNo\" = A.\"CustNo\"";
		sql += "                                           AND L.\"FacmNo\" = A.\"FacmNo\"";
		sql += "                                           AND L.\"YearMonth\" = Trunc(A.\"AcDate\" / 100)";
		sql += "                  WHERE Trunc(A.\"AcDate\" / 100) BETWEEN :Sym AND :Eym";
		sql += "                  AND Decode( L.\"Status\", 5 , '990' , F.\"AcctCode\" ) = '340'";
		sql += "                  AND A.\"AcSubBookCode\" = '201'";
		sql += "                  AND A.\"AcctCode\" IN ( 'IC1' , 'IC2' , 'IC3' , 'IC4' , 'IOV' , 'IOP' )";
		sql += "            GROUP BY Trunc(A.\"AcDate\" / 10000) * 100 + 12 ";
		sql += "        ) R";
		sql += "        GROUP BY 'AA'";
		sql += "               , R.\"YearMonth\"";
		// -- 政府優惠商品利息收入
		sql += "    ), \"tmpProdInt\" AS (";
		sql += "        SELECT CASE";
		sql += "                WHEN F.\"ProdNo\" IN ( 'IE' ) THEN 'ID'";
		sql += "                WHEN F.\"ProdNo\" IN ( 'IG' ) THEN 'IF'";
		sql += "                WHEN F.\"ProdNo\" IN ( 'II'  ) THEN 'IH'";
		sql += "                ELSE F.\"ProdNo\"";
		sql += "               END AS \"ProdNo\"";
		sql += "             , Trunc(L.\"AcDate\" / 10000) * 100 + 12 AS \"YearMonth\"";
		sql += "             , SUM(\"Interest\") AS \"Int\"";
		sql += "        FROM \"LoanBorTx\"  L";
		sql += "        LEFT JOIN \"FacMain\"    F ON F.\"CustNo\" = L.\"CustNo\"";
		sql += "                                 AND F.\"FacmNo\" = L.\"FacmNo\"";
		sql += "        WHERE Nvl( F.\"ProdNo\", ' ' ) BETWEEN 'IA' AND 'II'";
		sql += "              AND Trunc(L.\"AcDate\" / 100) BETWEEN :Sym AND :Eym";
		sql += "              AND L.\"TitaHCode\" = '0'";
		sql += "        GROUP BY CASE";
		sql += "                    WHEN F.\"ProdNo\" IN ( 'IE' ) THEN 'ID'";
		sql += "                    WHEN F.\"ProdNo\" IN ( 'IG' ) THEN 'IF'";
		sql += "                    WHEN F.\"ProdNo\" IN ( 'II'  ) THEN 'IH'";
		sql += "                    ELSE F.\"ProdNo\"";
		sql += "                   END";
		sql += "             	, Trunc(L.\"AcDate\" / 10000) *100 + 12 ";
		// -- F15 F16
		sql += "    ), \"tmpF1516Int\" AS (";
		sql += "        SELECT 'ZZ' AS \"ProdNo\"";
		sql += "              , Trunc(\"AcDate\" / 10000) * 100 + 12 AS \"YearMonth\"";
		sql += "             , SUM(Decode( \"DbCr\", 'C' , \"TxAmt\", - \"TxAmt\" )) AS \"Int\"";
		sql += "        FROM \"AcDetail\"";
		sql += "        WHERE Trunc(\"AcDate\" / 100) BETWEEN :Sym AND :Eym";
		sql += "              AND \"AcctCode\" IN ('F15' , 'F16' )";
		sql += "        GROUP BY 'ZZ'";
		sql += "               , Trunc(\"AcDate\" / 10000) * 100 + 12 ";
		// --合併利息收入
		sql += "    ), \"tmpMainInt\" AS (";
		sql += "        SELECT \"ProdNo\"";
		sql += "             , \"YearMonth\" AS \"YearMonth\"";
		sql += "             , NVL(SUM(\"Int\"),0)  AS \"Int\"";
		sql += "        FROM (";
		sql += "            SELECT * FROM \"tmp340Int\"";
		sql += "            UNION";
		sql += "            SELECT * FROM \"tmpProdInt\"";
		sql += "            UNION";
		sql += "            SELECT * FROM \"tmpF1516Int\"";
		sql += "        )";
		sql += "        GROUP BY \"ProdNo\"";
		sql += "            	,\"YearMonth\"";
		sql += "    )";
		sql += "    SELECT M.\"ProdNo\"  AS \"ProdNoShow\"";
		sql += "         , M.\"YearMonth\"    AS \"VisibleMonth\"";
		sql += "         , M.\"Bal\"          AS \"BalSum\"";
		sql += "         , I.\"Int\"          AS \"IntSum\"";
		sql += "    FROM \"tmpMainBal\"  M";
		sql += "    LEFT JOIN \"tmpMainInt\"  I ON I.\"ProdNo\" = M.\"ProdNo\"";
		sql += "                                AND I.\"YearMonth\" = M.\"YearMonth\"";
		sql += "    ORDER BY M.\"YearMonth\" ASC";
		sql += "           , M.\"ProdNo\" ASC";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("Sym", startYM);
		query.setParameter("Eym", endYM);

		return this.convertToMap(query);
	}

}