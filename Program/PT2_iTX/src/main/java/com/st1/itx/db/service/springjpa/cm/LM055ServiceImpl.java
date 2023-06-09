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
public class LM055ServiceImpl extends ASpringJpaParm implements InitializingBean {

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
	 * @return
	 * @throws Exception
	 * 
	 */
	public List<Map<String, String>> findAll(TitaVo titaVo, int yearMonth) throws Exception {

		this.info("lM055.findAll");

		int ilDate = 0;
		int iYear = yearMonth / 100;
		int iMonth = yearMonth % 100;
		if (iMonth == 1) {
			ilDate = (iYear - 1) * 10000 + 1201;
		} else {
			ilDate = (yearMonth - 1) * 100 + 1;
		}

		this.info("yearMonth=" + yearMonth);
		this.info("ilDate=" + ilDate);

		String sql = " ";
		// --取 逾期放款、未列入逾期應予評估放款(KIND=1、2)
		sql += "	WITH \"tempA\" AS (";
		sql += "	SELECT ( CASE";
		sql += "       	       WHEN M.\"ClCode1\" IN (3) THEN 'D'";
		sql += "       	       WHEN M.\"ClCode1\" IN (1,2) ";
		sql += "		 	    AND ( M.\"FacAcctCode\" = 340 ";
		sql += "				 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') ";
		sql += "				 OR REGEXP_LIKE(M.\"ProdNo\",'8[1-8]')) THEN 'Z' ";
		sql += "       	       WHEN M.\"ClCode1\" IN (1,2) THEN 'C' ";
		sql += "       	     ELSE '99' END ) AS \"TYPE\"";
		sql += "	      ,M.\"AssetClass\" AS \"KIND\"";
		sql += "	      ,SUM(M.\"PrinBalance\") AS \"AMT\"";
		sql += "	FROM \"MonthlyFacBal\" M";
		sql += "	WHERE M.\"YearMonth\" = :yymm";
		sql += "	  AND M.\"PrinBalance\" > 0";
		sql += "	GROUP BY(CASE";
		sql += "       	       WHEN M.\"ClCode1\" IN (3) THEN 'D'";
		sql += "       	       WHEN M.\"ClCode1\" IN (1,2) ";
		sql += "		 	    AND ( M.\"FacAcctCode\" = 340 ";
		sql += "				 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') ";
		sql += "				 OR REGEXP_LIKE(M.\"ProdNo\",'8[1-8]')) THEN 'Z' ";
		sql += "       	       WHEN M.\"ClCode1\" IN (1,2) THEN 'C' ";
		sql += "       	     ELSE '99' END )";
		sql += "	      ,M.\"AssetClass\"";
		// --取 放款折溢價及催收費用
		sql += "	),\"tempB\" AS (";
		sql += "		SELECT 'A' AS \"ClNo\"";
		sql += "          	  ,ROUND(SUM(NVL(I.\"AccumDPAmortized\",0)),0) AS \"AMT\"";
		sql += "		FROM \"Ias39IntMethod\" I";
		sql += "		LEFT JOIN \"MonthlyLoanBal\" MLB ON MLB.\"YearMonth\" = I.\"YearMonth\"";
		sql += "										AND MLB.\"CustNo\" = I.\"CustNo\"";
		sql += "										AND MLB.\"FacmNo\" = I.\"FacmNo\"";
		sql += "										AND MLB.\"BormNo\" = I.\"BormNo\"";
		sql += "		WHERE I.\"YearMonth\" = :yymm ";
		sql += "	  	  AND MLB.\"AcctCode\" <> 990 ";
		sql += "	),\"tempC\" AS (";
		// --取 購置住宅+修繕貸款(正常)
		sql += "	SELECT RES.\"TYPE\" AS \"TYPE\" ";
		sql += "		  ,1 AS \"KIND\" ";
		sql += "		  ,SUM(RES.\"AMT\") AS \"AMT\"";
		sql += "	FROM (";
		sql += "	SELECT ( CASE";
		sql += "			   WHEN M.\"ClCode1\" IN (1,2) AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') OR REGEXP_LIKE(M.\"ProdNo\",'8[1-8]')) THEN 'Z'";
		sql += "			   WHEN M.\"ClCode1\" IN (1,2) THEN 'C'";
		sql += "			   WHEN M.\"ClCode1\" IN (3,4) THEN 'D'";
		sql += "			   ELSE '99'";
		sql += "			 END ) AS \"TYPE\"";
		sql += "		  ,( CASE";
		sql += "			   WHEN M.\"ClCode1\" IN (1,2) AND F.\"FirstDrawdownDate\" >= 20100101 AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') OR REGEXP_LIKE(M.\"ProdNo\",'8[1-8]')) THEN '3'";
		sql += "			   WHEN M.\"ClCode1\" IN (1,2) AND CDI.\"IndustryCode\" IS NOT NULL THEN '2'";
		sql += "			   WHEN M.\"ClCode1\" IN (1,2) AND M.\"ProdNo\" NOT IN ('60','61','62') AND F.\"UsageCode\" = '02' AND CDI.\"IndustryCode\" IS NULL THEN '1'";
		sql += "			   WHEN M.\"ClCode1\" IN (3,4) THEN '4'";
		sql += "			   ELSE '99'";
		sql += "			 END ) AS \"KIND\"";
		sql += "	      ,SUM(M.\"PrinBalance\") AS \"AMT\"";
		sql += "	FROM \"MonthlyFacBal\" M";
		sql += "	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "						   AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "	LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = M.\"CustNo\"";
		sql += "	LEFT JOIN \"CdIndustry\" CDI ON CDI.\"IndustryCode\" = CM.\"IndustryCode\"";
		sql += "							    AND (CDI.\"IndustryItem\" LIKE '不動產%' OR CDI.\"IndustryItem\" LIKE '建築%')";
		sql += "	WHERE M.\"YearMonth\" = :yymm";
		sql += "	  AND M.\"PrinBalance\" > 0";
		sql += "	  AND M.\"PrevIntDate\"  >= :lyymmdd";
		sql += "	GROUP BY ( CASE";
		sql += "			     WHEN M.\"ClCode1\" IN (1,2) AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') OR REGEXP_LIKE(M.\"ProdNo\",'8[1-8]')) THEN 'Z'";
		sql += "			     WHEN M.\"ClCode1\" IN (1,2) THEN 'C'";
		sql += "			     WHEN M.\"ClCode1\" IN (3,4) THEN 'D'";
		sql += "			     ELSE '99'";
		sql += "			   END ) ";
		sql += "		    ,( CASE";
		sql += "			     WHEN M.\"ClCode1\" IN (1,2) AND F.\"FirstDrawdownDate\" >= 20100101 AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') OR REGEXP_LIKE(M.\"ProdNo\",'8[1-8]')) THEN '3'";
		sql += "			     WHEN M.\"ClCode1\" IN (1,2) AND CDI.\"IndustryCode\" IS NOT NULL THEN '2'";
		sql += "			     WHEN M.\"ClCode1\" IN (1,2) AND M.\"ProdNo\" NOT IN ('60','61','62') AND F.\"UsageCode\" = '02' AND CDI.\"IndustryCode\" IS NULL THEN '1'";
		sql += "			     WHEN M.\"ClCode1\" IN (3,4) THEN '4'";
		sql += "			     ELSE '99'";
		sql += "			   END ))RES ";
		sql += "	WHERE RES.\"KIND\" IN (1,2)";
		sql += "	GROUP BY RES.\"TYPE\"";
		// --取 應收利息
		sql += "	),\"tempD\" AS (";
		sql += "	SELECT SUM(M.\"IntAmtAcc\") AS \"AMT\" ";
		sql += "	FROM \"MonthlyLoanBal\" M";
		sql += "	WHERE M.\"YearMonth\" = :yymm";
		sql += "	  AND M.\"LoanBalance\" > 0 ";
		// --取 資產五分類(正常、應予注意、可望收回、收回困難、收回無望)
		sql += "	),\"tempE\" AS (";
		sql += "	SELECT ( CASE";
		sql += "			   WHEN M.\"ClCode1\" IN (3) THEN 'D'";
		sql += "			   WHEN M.\"ClCode1\" IN (1,2) AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') OR REGEXP_LIKE(M.\"ProdNo\",'8[1-8]')) THEN 'Z'";
		sql += "			   WHEN M.\"ClCode1\" IN (1,2) THEN 'C'";
		sql += "			   ELSE '99'";
		sql += "			 END ) AS \"TYPE\"";
		sql += "		  ,( CASE";
		sql += "			   WHEN M.\"OvduTerm\" IN (3,4,5) THEN '1' ";
		sql += "			   WHEN M.\"AcctCode\" = 990 THEN '1' ";
		sql += "			   WHEN M.\"ProdNo\" IN ('60','61','62') AND M.\"OvduTerm\" = 0 AND M.\"AcctCode\" <> 990 THEN '1' ";
		sql += "			   WHEN M.\"OvduTerm\" IN (1,2) THEN '2'";
		sql += "			   ELSE '3'";
		sql += "			 END ) AS \"KIND\"";
		sql += "	      ,SUM(M.\"PrinBalance\") AS \"AMT\"";
		sql += "	FROM \"MonthlyFacBal\" M";
		sql += "	WHERE M.\"YearMonth\" = :yymm";
		sql += "	  AND M.\"PrinBalance\" > 0";
		sql += "	GROUP BY(CASE";
		sql += "			   WHEN M.\"ClCode1\" IN (3) THEN 'D'";
		sql += "			   WHEN M.\"ClCode1\" IN (1,2) AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') OR REGEXP_LIKE(M.\"ProdNo\",'8[1-8]')) THEN 'Z'";
		sql += "			   WHEN M.\"ClCode1\" IN (1,2) THEN 'C'";
		sql += "			   ELSE '99'";
		sql += "			 END )";
		sql += "		  ,( CASE";
		sql += "			   WHEN M.\"OvduTerm\" IN (3,4,5) THEN '1' ";
		sql += "			   WHEN M.\"AcctCode\" = 990 THEN '1' ";
		sql += "			   WHEN M.\"ProdNo\" IN ('60','61','62') AND M.\"OvduTerm\" = 0 AND M.\"AcctCode\" <> 990 THEN '1' ";
		sql += "			   WHEN M.\"OvduTerm\" IN (1,2) THEN '2'";
		sql += "			   ELSE '3'";
		sql += "			 END )";
		// --取 折溢價、催收費用
		sql += "	),\"tempF\" AS (";
		sql += "	SELECT 'ZZ' AS \"TYPE\"";
		sql += "		  ,4 AS \"KIND\"";
		sql += "		  ,\"LoanBal\" AS \"AMT\"";
		sql += "	FROM \"MonthlyLM052AssetClass\"";
		sql += "	WHERE \"YearMonth\" = :yymm";
		sql += "	  AND \"AssetClassNo\" = 62 ";
		sql += "	UNION";
		sql += "    SELECT 'ZZ' AS \"TYPE\"";
		sql += "          ,3 AS \"KIND\"";
		sql += "		  ,NVL(R.\"LnAmt\",0) AS \"AMT\"";
		sql += "    FROM (";
		sql += "		SELECT ROUND(SUM(NVL(I.\"AccumDPAmortized\", 0)),0) AS \"LnAmt\"";
		sql += "    	FROM \"Ias39IntMethod\" I";
		sql += "        LEFT JOIN \"MonthlyLoanBal\" MLB ON I.\"YearMonth\" = MLB.\"YearMonth\" ";
		sql += "                                        AND I.\"CustNo\" = MLB.\"CustNo\" ";
		sql += "                                        AND I.\"FacmNo\" = MLB.\"FacmNo\" ";
		sql += "                                        AND I.\"BormNo\" = MLB.\"BormNo\"";
		sql += "        WHERE I.\"YearMonth\" IN (:yymm) ";
		sql += "          AND MLB.\"AcctCode\" <> 990 ) R";
		sql += "	),\"tempG\" AS (";
		// --IFRS 9 預期損失增提金額
		sql += "	SELECT 'C' AS \"TYPE\"";
		sql += "		  ,13 AS \"KIND\"";
		sql += "		  ,SUM(\"AMT\") AS \"AMT\"";
		sql += "	FROM (";
		sql += "		SELECT \"LegalLoss\" AS \"AMT\" ";
		sql += "	    FROM \"MonthlyLM052Loss\"";
		sql += "	    WHERE \"YearMonth\" = :lyymm";
		sql += "		UNION";
		sql += "		SELECT - \"AssetEvaTotal\" AS \"AMT\" ";
		sql += "	    FROM \"MonthlyLM052Loss\"";
		sql += "	    WHERE \"YearMonth\" = :yymm";
		sql += "	)";
		sql += "	SELECT R.\"TYPE\" AS F0";
		sql += "		  ,R.\"KIND\" AS F1";
		sql += "		  ,R.\"AMT\" AS F2";
		sql += "		  ,CASE";
		sql += "			 WHEN R.\"KIND\" = 1 THEN '逾期放款' ";
		sql += "			 WHEN R.\"KIND\" = 2 THEN '未列入逾期應予評估放款' ";
		sql += "			 WHEN R.\"KIND\" = 3 THEN '正常放款I' ";
		sql += "			 WHEN R.\"KIND\" = 4 THEN '應予注意II' ";
		sql += "			 WHEN R.\"KIND\" = 5 THEN '可望收回III'";
		sql += "			 WHEN R.\"KIND\" = 6 THEN '收回困難IV'";
		sql += "			 WHEN R.\"KIND\" = 7 THEN '收回無望V'";
		sql += "			 WHEN R.\"KIND\" = 8 THEN '備抵損失I' ";
		sql += "			 WHEN R.\"KIND\" = 9 THEN '備抵損失II' ";
		sql += "			 WHEN R.\"KIND\" = 10 THEN '備抵損失III' ";
		sql += "			 WHEN R.\"KIND\" = 11 THEN '備抵損失IV' ";
		sql += "			 WHEN R.\"KIND\" = 12 THEN '備抵損失V'";
		sql += "			 WHEN R.\"KIND\" = 13 THEN 'IFRS 9預期損失增提金額'";
		sql += "		   END AS F3";
		sql += "	FROM ( ";
		sql += "		SELECT E.\"TYPE\" AS \"TYPE\"";
		sql += "			  ,TO_NUMBER(E.\"KIND\") AS \"KIND\"";
		sql += "			  ,E.\"AMT\" AS \"AMT\"";
		sql += "		FROM \"tempE\" E";
		sql += "		WHERE E.\"KIND\" IN (1,2)";
		sql += "		UNION";
		sql += "		SELECT A.\"TYPE\" AS \"TYPE\"";
		sql += "			  ,TO_NUMBER(A.\"KIND\") + 2 AS \"KIND\"";
		sql += "			  ,A.\"AMT\" AS \"AMT\"";
		sql += "		FROM \"tempA\" A";
		sql += "		UNION";
		sql += "		SELECT A.\"TYPE\" AS \"TYPE\"";
		sql += "			  ,TO_NUMBER(A.\"KIND\") + 7 AS \"KIND\"";
		sql += "			  ,ROUND(";
		sql += "			   CASE ";
		sql += "			  	 WHEN A.\"KIND\" = 1 AND A.\"TYPE\" = 'D' THEN A.\"AMT\" * 0.005 ";
		sql += "			  	 WHEN A.\"KIND\" = 1 THEN (A.\"AMT\" ";
		sql += "				      + (SELECT NVL(B.\"AMT\",0) FROM \"tempB\" B)) * 0.005 ";
		sql += "					  + C.\"AMT\" * 0.01 ";
		sql += "			  	 WHEN A.\"KIND\" = 2 AND A.\"TYPE\" = 'C' THEN A.\"AMT\" * 0.02 ";
		sql += "					  + (SELECT NVL(D.\"AMT\",0) FROM \"tempD\" D) * 0.02 ";
		sql += "			  	 WHEN A.\"KIND\" = 2 THEN A.\"AMT\" * 0.02 ";
		sql += "			  	 WHEN A.\"KIND\" = 3 THEN A.\"AMT\" * 0.1 ";
		sql += "			  	 WHEN A.\"KIND\" = 4 THEN A.\"AMT\" * 0.5 ";
		sql += "			  	 WHEN A.\"KIND\" = 5 THEN A.\"AMT\" * 1";
		sql += "			   END , 0 ) AS \"AMT\"";
		sql += "		FROM \"tempA\" A";
		sql += "		LEFT JOIN \"tempC\" C ON C.\"TYPE\" = A.\"TYPE\"";
		sql += "							 AND C.\"KIND\" = A.\"KIND\"";
		sql += "		UNION";
		sql += "		SELECT F.\"TYPE\" AS \"TYPE\"";
		sql += "			  ,1 AS \"KIND\"";
		sql += "			  ,NVL(F.\"AMT\",0) AS \"AMT\"";
		sql += "		FROM \"tempF\" F";
		sql += "		WHERE F.\"KIND\" = 4 ";
		sql += "		UNION";
		sql += "		SELECT F.\"TYPE\" AS \"TYPE\"";
		sql += "			  ,TO_NUMBER(F.\"KIND\") AS \"KIND\"";
		sql += "			  ,NVL(F.\"AMT\",0) AS \"AMT\"";
		sql += "		FROM \"tempF\" F";
		sql += "		UNION";
		sql += "		SELECT F.\"TYPE\" AS \"TYPE\"";
		sql += "			  ,TO_NUMBER(F.\"KIND\") + 5 AS \"KIND\"";
		sql += "			  ,ROUND(";
		sql += "			   CASE ";
		sql += "			  	 WHEN F.\"KIND\" = 3 THEN F.\"AMT\" * 0.005 ";
		sql += "			  	 WHEN F.\"KIND\" = 4 THEN F.\"AMT\" * 0.02 ";
		sql += "			   END , 0 ) AS \"AMT\"";
		sql += "		FROM \"tempF\" F";
		sql += "		WHERE F.\"KIND\" IN (3,4)";
		sql += "		UNION";
		sql += "		SELECT G.\"TYPE\" AS \"TYPE\"";
		sql += "			  ,G.\"KIND\" AS \"KIND\"";
		sql += "			  ,NVL(G.\"AMT\",0) AS \"AMT\"";
		sql += "		FROM \"tempG\" G";
		sql += "	)R";
		sql += "	ORDER BY R.\"KIND\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", yearMonth);
		query.setParameter("lyymmdd", ilDate);
		query.setParameter("lyymm", ilDate / 100);
		return this.convertToMap(query);
	}

	public List<Map<String, String>> findAll2(TitaVo titaVo, int yearMonth) throws Exception {

		this.info("lM055.findAll");
		this.info("yearMonth=" + yearMonth);

		// 工作表

		/*
		 * 還需要補的 備呆子目 參考LM042 科子目計算表格 10623 備抵損失-擔保放款+ 10624100 備抵損失-催收款項-放款部+ 10624200
		 * 備抵損失-催收款項-營業稅提撥 或 RBC表的RBC工作表I41欄 來源參考公式
		 *
		 *
		 * 溢折價與催收費用 10600304000 擔保放款-溢折價 10601301000 催收款項-法務費用 10601302000 催收款項-火險費用
		 * 10601304000 催收款項-溢折價
		 */
		String sql = " ";
		sql += "SELECT RES.\"COL\"";
		sql += "	  ,RES.\"KIND\"";
		sql += "	  ,RES.\"AMT\"";
		sql += "	  ,RES.\"Allowance\"";
		sql += "	  +NVL(I.\"INT\",0) AS \"Allowance\"";
		sql += "FROM( ";
		sql += "SELECT \"COL\"";
		sql += "	  ,\"KIND\"";
		sql += "	  ,ROUND(\"AMT\",0) AS \"AMT\"";
		sql += "	  ,ROUND ( NVL (( CASE";
		sql += "	      	   			WHEN \"COL\" = '3' THEN \"AMT\" * 0.005";
		sql += "	       	   			WHEN \"COL\" = '4' THEN \"AMT\" * 0.02";
		sql += "	       	   			WHEN \"COL\" = '5' THEN \"AMT\" * 0.1";
		sql += "	       	   			WHEN \"COL\" = '6' THEN \"AMT\" * 0.5";
		sql += "	       	  			WHEN \"COL\" = '7' THEN \"AMT\" * 1";
		sql += "	       	   			WHEN \"COL\" = '99' THEN \"AMT\" * 0.01";
		sql += "	   				END),0),0) AS \"Allowance\"";
		sql += "FROM(";
		// --逾期放款和未列入逾期應予評估放款
		sql += "	SELECT ( CASE";
		sql += "       	       WHEN M.\"OvduTerm\" IN (3,4,5,6) OR M.\"Status\" IN (2,6,7) THEN '1'";
		sql += "       	     ELSE '2' END ) AS \"COL\"";
		sql += "	      ,( CASE";
		sql += "       	       WHEN M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') THEN 'Z'";
		sql += "       	     ELSE 'C' END ) AS \"KIND\"";
		sql += "	      ,SUM(M.\"PrinBalance\") AS \"AMT\"";
		sql += "	FROM \"MonthlyFacBal\" M";
		sql += "	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "						   AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "	WHERE M.\"YearMonth\" = :yymm";
		sql += "	  AND M.\"PrinBalance\" > 0";
		sql += "	  AND SUBSTR(M.\"AssetClass\",0,1) = '1' ";
		// sql += " AND M.\"AssetClass\" IS NOT NULL";
		sql += "	GROUP BY ( CASE";
		sql += "       	         WHEN M.\"OvduTerm\" IN (3,4,5,6) OR M.\"Status\" IN (2,6,7) THEN '1'";
		sql += "       	       ELSE '2' END )";
		sql += "	        ,( CASE";
		sql += "       	         WHEN M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') THEN 'Z'";
		sql += "       	       ELSE 'C' END )";
		sql += "	UNION";
		// --正常放款I
		sql += "	SELECT '3' AS \"COL\"";
		sql += "		  ,\"KIND\" AS \"KIND\"";
		sql += "		  ,SUM(\"AMT\") AS \"AMT\"";
		sql += "	FROM ( SELECT ( CASE";
		sql += "					  WHEN \"ClCode1\" IN (1,2) AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]')) THEN 'Z'";
		sql += "					  WHEN \"ClCode1\" IN (1,2) THEN 'C'";
		sql += "					  WHEN \"ClCode1\" IN (3,4) THEN 'D'";
		sql += "					  ELSE 'N'";
		sql += "					END ) AS \"KIND\"";
		sql += "	             ,SUM(M.\"PrinBalance\") AS \"AMT\"";
		sql += "	       FROM \"MonthlyFacBal\" M";
		sql += "		   LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "						   		  AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "		   WHERE M.\"YearMonth\" = :yymm";
		sql += "	  		 AND M.\"PrinBalance\" > 0";
		sql += "	 		 AND SUBSTR(M.\"AssetClass\",0,1) <> '1' ";
		// sql += " AND M.\"AssetClass\" IS NULL";
		sql += "	       GROUP BY (CASE";
		sql += "					   WHEN \"ClCode1\" IN (1,2) AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]')) THEN 'Z'";
		sql += "					   WHEN \"ClCode1\" IN (1,2) THEN 'C'";
		sql += "					   WHEN \"ClCode1\" IN (3,4) THEN 'D'";
		sql += "					   ELSE 'N'";
		sql += "					 END )";
		sql += "	       UNION";
		sql += "	       SELECT 'C' AS \"KIND\"";
		sql += "	       	     ,SUM(\"DbAmt\" - \"CrAmt\") AS \"AMT\"";
		sql += "	       FROM \"AcMain\"";
		sql += "	       WHERE \"AcNoCode\" IN (10600304000,10601301000,10601302000,10601304000)";
		sql += "	         AND \"MonthEndYm\" = :yymm ) RES";
		sql += "		 GROUP BY '3'";
		sql += "		 		 ,RES.\"KIND\"";
		sql += "	UNION";
		// --應予注意II、可望收回III、收回困難IV、收回無望V
		sql += "	SELECT ( CASE";
		sql += "       	       WHEN M.\"PrinBalance\" = 1 THEN '7'";
		sql += "       	       WHEN M.\"OvduTerm\" >= 12 AND M.\"ProdNo\" IN ('60','61','62') THEN '4'";
		sql += "       	       WHEN M.\"OvduTerm\" >= 12 THEN '5'";
		sql += "       	       WHEN M.\"OvduTerm\" >= 7 THEN '4'";
		sql += "       	       WHEN M.\"OvduTerm\" >= 1 THEN '4'";
		sql += "       	       WHEN M.\"ProdNo\" IN ('60','61','62') THEN '4'";
		sql += "       	       ELSE '4' ";
		sql += "       	     END ) AS \"COL\"";
		sql += "	      ,( CASE";
		sql += "       	       WHEN M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') THEN 'Z'";
		sql += "       	     ELSE 'C' END ) AS \"KIND\"";
		sql += "	      ,SUM(M.\"PrinBalance\") AS \"AMT\"";
		sql += "	FROM \"MonthlyFacBal\" M";
		sql += "	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "						   AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "	WHERE M.\"YearMonth\" = :yymm";
		sql += "	  AND M.\"PrinBalance\" > 0";
		sql += "	  AND SUBSTR(M.\"AssetClass\",0,1) <> '1' ";
		// sql += " AND M.\"AssetClass\" IS NOT NULL";
		sql += "	GROUP BY (CASE";
		sql += "       	        WHEN M.\"PrinBalance\" = 1 THEN '7'";
		sql += "       	        WHEN M.\"OvduTerm\" >= 12 AND M.\"ProdNo\" IN ('60','61','62') THEN '4'";
		sql += "       	        WHEN M.\"OvduTerm\" >= 12 THEN '5'";
		sql += "       	        WHEN M.\"OvduTerm\" >= 7 THEN '4'";
		sql += "       	        WHEN M.\"OvduTerm\" >= 1 THEN '4'";
		sql += "       	        WHEN M.\"ProdNo\" IN ('60','61','62') THEN '4'";
		sql += "       	       ELSE '4' ";
		sql += "       	      END )";
		sql += "	        ,( CASE";
		sql += "       	         WHEN M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') THEN 'Z'";
		sql += "       	       ELSE 'C' END )";
		sql += "	UNION";
		// --購置住宅+修繕貸款
		sql += "	SELECT '99' AS \"COL\"";
		sql += "	      ,( CASE";
		sql += "       	       WHEN M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') THEN 'Z'";
		sql += "       	     ELSE 'C' END ) AS \"KIND\"";
		sql += "	      ,SUM(M.\"PrinBalance\") AS \"AMT\"";
		sql += "	FROM \"MonthlyFacBal\" M";
		sql += "	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "						   AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "	WHERE M.\"YearMonth\" = :yymm";
		sql += "	  AND M.\"PrinBalance\" > 0";
		sql += "	  AND SUBSTR(M.\"AssetClass\",0,1) <> '1' ";
		// sql += " AND M.\"AssetClass\" IS NULL";
		sql += "	  AND F.\"UsageCode\" = '02'";
		sql += "	GROUP BY ( CASE";
		sql += "       	         WHEN M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') THEN 'Z'";
		sql += "       	       ELSE 'C' END )";
		sql += "	UNION";
		// --五類資產評估合計
		sql += "	SELECT 'FIVE' AS \"COL\"";
		sql += "		  ,'FIVE' AS \"KIND\"";
		sql += "		  ,SUM(DECODE(\"COL\",'11',\"AMT\" * 0.005";
		sql += "		  					 ,'12',\"AMT\" * 0.015";
		sql += "		  					 ,'2',\"AMT\" * 0.02";
		sql += "		  					 ,'3',\"AMT\" * 0.1";
		sql += "		  					 ,'4',\"AMT\" * 0.5";
		sql += "		  					 ,'5',\"AMT\" * 1";
		sql += "		  					 ,'I',\"AMT\" * 0.02)) AS \"AMT\"";
		sql += "	FROM ( SELECT DECODE(F.\"UsageCode\",'02','12','11') AS \"COL\"";
		sql += "				 ,SUM(M.\"PrinBalance\") AS \"AMT\"";
		sql += "		   FROM \"MonthlyFacBal\" M";
		sql += "		   LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "			  	 			   	  AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "		   WHERE M.\"YearMonth\" = :yymm";
		sql += "	  		 AND M.\"PrinBalance\" > 0";
		// sql += " AND M.\"AssetClass\" IS NULL";
		sql += "	  		 AND SUBSTR(M.\"AssetClass\",0,1) <> '1' ";
		sql += "	       GROUP BY DECODE(F.\"UsageCode\",'02','12','11')";
		sql += "	       UNION";
		sql += "		   SELECT ( CASE";
		sql += "       	       		  WHEN M.\"PrinBalance\" = 1 THEN '5'";
		sql += "       	       		  WHEN M.\"OvduTerm\" >= 12 AND M.\"ProdNo\" IN ('60','61','62') THEN '2'";
		sql += "       	       		  WHEN M.\"OvduTerm\" >= 12 THEN '3'";
		sql += "       	       		  WHEN M.\"OvduTerm\" >= 7 THEN '2'";
		sql += "       	       		  WHEN M.\"OvduTerm\" >= 1 THEN '2'";
		sql += "       	       		  WHEN M.\"ProdNo\" IN ('60','61','62') THEN '2'";
		sql += "       	     	    END ) AS \"COL\"";
		sql += "	      		 ,SUM(M.\"PrinBalance\") AS \"AMT\"";
		sql += "	       FROM \"MonthlyFacBal\" M";
		sql += "		   LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "			  	 			   	  AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "		   WHERE M.\"YearMonth\" = :yymm";
		sql += "	  		 AND M.\"PrinBalance\" > 0";
		sql += "	  		 AND SUBSTR(M.\"AssetClass\",0,1) = '1' ";
		// sql += " AND M.\"AssetClass\" IS NOT NULL";
		sql += "	       GROUP BY (CASE";
		sql += "       	       		   WHEN M.\"PrinBalance\" = 1 THEN '5'";
		sql += "       	       		   WHEN M.\"OvduTerm\" >= 12 AND M.\"ProdNo\" IN ('60','61','62') THEN '2'";
		sql += "       	       		   WHEN M.\"OvduTerm\" >= 12 THEN '3'";
		sql += "       	       		   WHEN M.\"OvduTerm\" >= 7 THEN '2'";
		sql += "       	       		   WHEN M.\"OvduTerm\" >= 1 THEN '2'";
		sql += "       	       		   WHEN M.\"ProdNo\" IN ('60','61','62') THEN '2'";
		sql += "       	     	     END )";
		sql += "	       UNION";
		sql += "	       SELECT 'I' AS \"COL\"";
		sql += "	       		 ,SUM(\"IntAmtAcc\") AS \"KIND\"";
		sql += "	       FROM \"MonthlyLoanBal\"";
		sql += "	       WHERE \"LoanBalance\" > 0";
		sql += "	         AND \"YearMonth\" = :yymm ";
		sql += "	     )";
		sql += " )) RES ";
		sql += "LEFT JOIN(";
		sql += "	SELECT '4' AS \"COL\"";
		sql += "		  ,'C' AS \"KIND\"";
		sql += "		  ,SUM(\"IntAmtAcc\") * 0.02 AS \"INT\"";
		sql += "	FROM \"MonthlyLoanBal\"";
		sql += "	WHERE \"LoanBalance\" > 0";
		sql += "	  AND \"YearMonth\" = :yymm ";
		sql += ") I ON I.\"COL\" = RES.\"COL\" AND I.\"KIND\" = RES.\"KIND\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", yearMonth);
		return this.convertToMap(query);
	}

}
