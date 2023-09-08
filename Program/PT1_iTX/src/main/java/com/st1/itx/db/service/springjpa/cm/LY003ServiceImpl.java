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
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service("LY003ServiceImpl")
@Repository
/* 逾期放款明細 */
public class LY003ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	/**
	 * 查詢資料
	 * 
	 * @param titaVo
	 * @param formNum        表格次序
	 * @param endOfYearMonth 西元年底年月
	 * @return
	 * @throws Exception
	 * 
	 */
	public List<Map<String, String>> findAll(TitaVo titaVo, int formNum, int endOfYearMonth) throws Exception {

		this.info("LY003.findAll " + formNum);

		String sql = " ";
		sql += "	SELECT ";

		if (formNum == 2 || formNum == 3) {
			sql += "		(CASE";
			sql += "    		  WHEN MF2.\"EntCode\" = 1 AND R.\"RptId\" IS NOT NULL THEN 'A'";
			sql += "    		  WHEN MF2.\"EntCode\" <> 1 AND R.\"RptId\" IS NOT NULL THEN 'B'";
			sql += "    		  WHEN MF2.\"EntCode\" = 1 AND R.\"RptId\" IS NULL THEN 'C'";
			sql += "     		  WHEN MF2.\"EntCode\" <> 1 AND R.\"RptId\" IS NULL THEN 'D'";
			sql += "   		    END) AS \"TYPE\"";
			sql += "		  ,SUM(NVL(R.\"LineAmt\",0)) AS \"LineAmt\"";
			sql += "		  ,SUM(NVL(MF2.\"LoanBalance\",0)) AS \"LoanBalance\"";

		}
		if (formNum == 1) {
			sql += "	   (CASE";
			sql += "    		  WHEN MFB.\"ClCode1\" IN (1,2) ";
			sql += "    		   AND (MFB.\"FacAcctCode\" = 340 OR REGEXP_LIKE(MFB.\"ProdNo\",'I[A-Z]')) THEN 'Z'";
			sql += "    		  WHEN MFB.\"ClCode1\" IN (1,2) THEN 'C'";
			sql += "    		  WHEN MFB.\"ClCode1\" IN (3,4) THEN 'D'";
			sql += "    		  WHEN MFB.\"ClCode1\" IN (5) THEN 'A'";
			sql += "    		  WHEN MFB.\"ClCode1\" IN (9) THEN 'B'";
			sql += "   		    END) AS \"TYPE\"";
			sql += "		  ,SUM(NVL(R.\"LineAmt\",0)) AS \"LineAmt\"";
			sql += "		  ,SUM(NVL(MF2.\"LoanBalance\",0)) AS \"LoanBalance\"";
		}
//		if (formNum == 4) {
//			sql += "		SUBSTR(MFB.\"AssetClass\",0,1) AS \"AssetClass\"";
//			sql += "	   ,(CASE";
//			sql += "    		  WHEN MFB.\"ClCode1\" IN (1,2) ";
//			sql += "    		   AND (MFB.\"FacAcctCode\" = 340 OR REGEXP_LIKE(MFB.\"ProdNo\",'I[A-Z]')) THEN 'Z'";
//			sql += "    		  WHEN MFB.\"ClCode1\" IN (1,2) THEN 'C'";
//			sql += "    		  WHEN MFB.\"ClCode1\" IN (3,4) THEN 'D'";
//			sql += "    		  WHEN MFB.\"ClCode1\" IN (5) THEN 'A'";
//			sql += "    		  WHEN MFB.\"ClCode1\" IN (9) THEN 'B'";
//			sql += "   		    END) AS \"TYPE\"";
//			sql += "		  ,SUM(NVL(R.\"LineAmt\",0)) AS \"LineAmt\"";
//			sql += "		  ,SUM(NVL(MF2.\"LoanBalance\",0)) AS \"LoanBalance\"";
//		}
		sql += "	FROM ( SELECT S.\"CustNo\"";
		sql += "				 ,S.\"FacmNo\"";
		sql += "				 ,S.\"LineAmt\"";
		sql += "		   FROM ( SELECT \"CustNo\"";
		sql += "		   				,SUM(\"LineAmt\") AS \"LineAmt\"";
		sql += "		   		  FROM \"FacMain\"";
		sql += "		   		  WHERE \"UtilAmt\" > 0 ";
		sql += "		   		  GROUP BY \"CustNo\" ) T";
		sql += "		   LEFT JOIN( SELECT \"CustNo\"";
		sql += "		   					,\"FacmNo\"";
		sql += "		   					,SUM(\"LineAmt\") AS \"LineAmt\"";
		sql += "		   		 	  FROM \"FacMain\"";
		sql += "		   		  	  WHERE \"UtilAmt\" > 0 ";
		sql += "		   		  	  GROUP BY \"CustNo\"";
		sql += "		   		  	  		  ,\"FacmNo\" ) S";
		sql += "		   ON S.\"CustNo\" =  T.\"CustNo\"";
		if (formNum == 1 || formNum == 2) {
			sql += "		   WHERE T.\"LineAmt\" >= 100000000";
		}
		sql += "		  ) R";
		sql += "	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = R.\"CustNo\"";
		sql += "						   AND F.\"FacmNo\" = R.\"FacmNo\"";
		sql += "	LEFT JOIN ( SELECT DISTINCT \"CustNo\"";
		sql += "					  ,\"FacmNo\"";
		sql += "					  ,\"ClCode1\"";
		sql += "					  ,\"ClCode2\"";
		sql += "					  ,\"ClNo\"";
		sql += "				FROM \"MonthlyLoanBal\"";
		sql += "				WHERE \"YearMonth\" = :yymm";
		sql += "				  AND \"LoanBalance\" > 0 ) MF";
		sql += "	 ON MF.\"CustNo\" = F.\"CustNo\"";
		sql += "	AND MF.\"FacmNo\" = F.\"FacmNo\"";
		sql += "	LEFT JOIN \"MonthlyFacBal\" MFB ON MFB.\"CustNo\" = MF.\"CustNo\"";
		sql += "	 							   AND MFB.\"FacmNo\" = MF.\"FacmNo\"";
		sql += "	 							   AND MFB.\"YearMonth\" = :yymm ";
		sql += "	LEFT JOIN \"ClMain\" CM ON CM.\"ClCode1\" = MF.\"ClCode1\"";
		sql += "							AND CM.\"ClCode2\" = MF.\"ClCode2\"";
		sql += "							AND CM.\"ClNo\" = MF.\"ClNo\"";
		sql += "	LEFT JOIN ( SELECT \"CustNo\"";
		sql += "					  ,\"FacmNo\"";
		sql += "					  ,\"EntCode\"";
		sql += "					  ,SUM(\"LoanBalance\") AS \"LoanBalance\"";
		sql += "				FROM \"MonthlyLoanBal\"";
		sql += "				WHERE \"YearMonth\" = :yymm";
		sql += "				  AND \"LoanBalance\" > 0 ";
		sql += "				GROUP BY \"CustNo\"";
		sql += "						,\"FacmNo\"";
		sql += "						,\"EntCode\" ) MF2";
		sql += "	 ON MF2.\"CustNo\" = F.\"CustNo\"";
		sql += "	AND MF2.\"FacmNo\" = F.\"FacmNo\"";
		sql += "    LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = R.\"CustNo\"";
		sql += "    LEFT JOIN (SELECT TO_CHAR(\"CusId\") AS \"RptId\" ";
		sql += "               FROM \"RptRelationSelf\" ";
		sql += "               WHERE \"LAW005\" = '1' ";
		sql += "               UNION ";
		sql += "               SELECT TO_CHAR(\"RlbID\") AS \"RptId\" ";
		sql += "               FROM \"RptRelationFamily\" ";
		sql += "               WHERE \"LAW005\" = '1' ";
		sql += "               UNION ";
		sql += "               SELECT TO_CHAR(\"ComNo\") AS \"RptId\" ";
		sql += "               FROM \"RptRelationCompany\" ";
		sql += "               WHERE \"LAW005\" = '1' ";
		sql += "             ) R ON R.\"RptId\" = CM.\"CustId\" ";

		if (formNum == 2 || formNum == 3) {
			sql += "	GROUP BY (CASE";
			sql += "    		    WHEN MF2.\"EntCode\" = 1 AND R.\"RptId\" IS NOT NULL THEN 'A'";
			sql += "    		    WHEN MF2.\"EntCode\" <> 1 AND R.\"RptId\" IS NOT NULL THEN 'B'";
			sql += "    		    WHEN MF2.\"EntCode\" = 1 AND R.\"RptId\" IS NULL THEN 'C'";
			sql += "     		    WHEN MF2.\"EntCode\" <> 1 AND R.\"RptId\" IS NULL THEN 'D'";
			sql += "   		      END)";
		}

		if (formNum == 1) {
			sql += "	GROUP BY (CASE";
			sql += "    		    WHEN MFB.\"ClCode1\" IN (1,2) ";
			sql += "    		     AND (MFB.\"FacAcctCode\" = 340 OR REGEXP_LIKE(MFB.\"ProdNo\",'I[A-Z]')) THEN 'Z'";
			sql += "    		    WHEN MFB.\"ClCode1\" IN (1,2) THEN 'C'";
			sql += "    		    WHEN MFB.\"ClCode1\" IN (3,4) THEN 'D'";
			sql += "    		    WHEN MFB.\"ClCode1\" IN (5) THEN 'A'";
			sql += "    		    WHEN MFB.\"ClCode1\" IN (9) THEN 'B'";
			sql += "   		      END)";
		}
//		if (formNum == 4) {
//			sql += "	GROUP BY SUBSTR(MFB.\"AssetClass\",0,1)";
//			sql += "	   		,(CASE";
//			sql += "    		    WHEN MFB.\"ClCode1\" IN (1,2) ";
//			sql += "    		     AND (MFB.\"FacAcctCode\" = 340 OR REGEXP_LIKE(MFB.\"ProdNo\",'I[A-Z]')) THEN 'Z'";
//			sql += "    		    WHEN MFB.\"ClCode1\" IN (1,2) THEN 'C'";
//			sql += "    		    WHEN MFB.\"ClCode1\" IN (3,4) THEN 'D'";
//			sql += "    		    WHEN MFB.\"ClCode1\" IN (5) THEN 'A'";
//			sql += "    		    WHEN MFB.\"ClCode1\" IN (9) THEN 'B'";
//			sql += "   		      END)";
//		}

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", endOfYearMonth);
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

		this.info("lY003.findAll2");
		this.info("yymm=" + yearMonth);

		String sql = " ";
		sql += " SELECT \"KIND\"";
		sql += "       ,SUM(NVL(\"AMT\",0)) AS \"AMT\"";
		sql += " FROM(";
		sql += "	SELECT ( CASE";
		sql += "       	       WHEN M.\"OvduTerm\" > 3 AND M.\"OvduTerm\" <= 6 THEN 'C'";
		sql += "       	       WHEN CL.\"LegalProg\" IN ('056','057','058','060') AND (M.\"OvduTerm\" > 3 OR M.\"PrinBalance\" = 1) AND M.\"Status\" IN (2,6,7) THEN 'C'";
		sql += "       	     ELSE 'B' END ) AS \"KIND\"";
		sql += "	      ,M.\"PrinBalance\" AS \"AMT\"";
		sql += "	FROM \"MonthlyFacBal\" M";
		sql += "	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "						   AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "	LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\"";
		sql += "	LEFT JOIN(SELECT L.\"CustNo\"";
		sql += "				    ,L.\"FacmNo\"";
		sql += "					,L.\"LegalProg\"";
		sql += "					,L.\"Amount\"";
		sql += "					,L.\"Memo\"";
		sql += "					,ROW_NUMBER() OVER (PARTITION BY L.\"CustNo\", L.\"FacmNo\" ORDER BY L.\"TitaTxtNo\" DESC) AS \"SEQ\"";
		sql += "			  FROM \"CollLaw\" L";
		sql += "		      WHERE TRUNC(L.\"AcDate\" / 100) <= :yymm ) CL";
		sql += "	  ON CL.\"CustNo\" = M.\"CustNo\" AND CL.\"FacmNo\" = M.\"FacmNo\" ";
		sql += "	 								  AND CL.\"SEQ\" = 1";
		sql += "	WHERE M.\"YearMonth\" = :yymm";
		sql += "	  AND M.\"PrinBalance\" > 0 )";
		sql += "    GROUP BY \"KIND\"";
		sql += "	UNION";
		sql += "	SELECT 'COLLECTION' AS \"KIND\"";
		sql += "       	  ,SUM(NVL(\"AMT\",0)) AS \"AMT\"";
		sql += "	FROM ( SELECT SUM(\"DbAmt\" - \"CrAmt\") AS \"AMT\"";
		sql += "	       FROM \"AcMain\"";
		sql += "	       WHERE \"AcNoCode\" IN (10600304000,10601301000,10601302000,10601304000)";
		sql += "	         AND \"MonthEndYm\" = :yymm )";
		sql += "	GROUP BY 'COLLECTION'";
		sql += "	UNION";
		sql += "	SELECT 'TOTAL' AS \"KIND\"";
		sql += "       	  ,SUM(NVL(\"AMT\",0)) AS \"AMT\"";
		sql += "	FROM ( SELECT SUM(M.\"PrinBalance\") AS \"AMT\"";
		sql += "	       FROM \"MonthlyFacBal\" M";
		sql += "		   WHERE M.\"YearMonth\" = :yymm";
		sql += "	  		 AND M.\"PrinBalance\" > 0";
		sql += "	       UNION";
		sql += "	       SELECT SUM(\"DbAmt\" - \"CrAmt\") AS \"AMT\"";
		sql += "	       FROM \"AcMain\"";
		sql += "	       WHERE \"AcNoCode\" IN (10600304000,10601301000,10601302000,10601304000)";
		sql += "	         AND \"MonthEndYm\" = :yymm )";
		sql += "	GROUP BY 'TOTAL'";
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
	public List<Map<String, String>> findAll3(TitaVo titaVo, int yearMonth) throws Exception {

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
		sql += "	WITH TEMPDATA AS ( ";
		sql += "	SELECT  ";
		sql += "			\"Kind\"		 ";
		sql += "	      	,SUM(CASE WHEN \"Type\" IN (4,5) THEN \"bal\" ELSE 0 END ) AS \"Amt5\" "; // 逾期放款
		sql += "	      	,SUM(CASE WHEN \"Type\" IN (1,3) THEN \"bal\" ELSE 0 END ) AS \"Amt6\" "; // 未列入逾期應予評估放款
		sql += "	      	,SUM(CASE WHEN \"Type\" IN (2) THEN \"bal\" ELSE 0 END ) AS \"Amt7\" "; // 正常放款
		sql += "	      	,SUM(CASE WHEN \"Type\" IN (8,9) THEN \"bal\" ELSE 0 END ) AS \"AdjAmt\" "; // 88金額+專案放款差額
		sql += "	      	,SUM(CASE WHEN \"AssetClass\" = '2' THEN \"bal\" ELSE 0 END ) AS \"Amt8\" "; // 應予注意
		sql += "	      	,SUM(CASE WHEN \"AssetClass\" = '3' THEN \"bal\" ELSE 0 END ) AS \"Amt9\" "; // 可望收回
		sql += "	      	,SUM(CASE WHEN \"AssetClass\" = '4' THEN \"bal\" ELSE 0 END ) AS \"Amt10\" "; // 收回困難
		sql += "	      	,SUM(CASE WHEN \"AssetClass\" = '5' THEN \"bal\" ELSE 0 END ) AS \"Amt11\" "; // 收回無望
		sql += "	      	,SUM(CASE WHEN \"AssetClass\" = '' THEN \"bal\" ELSE 0 END ) AS \"Amt12\" "; // 備抵損失
		sql += "	FROM (  ";
		sql += "			SELECT ";
		sql += "			CASE WHEN \"FacAcctCode\" IN ('340') 	   THEN  'G'		  "; // 政府專案
		sql += "				 WHEN \"ProdNo\" BETWEEN 'IA' AND 'II' THEN  'G'		  ";
		sql += "				 WHEN \"ProdNo\" BETWEEN '81' AND '83' THEN  'G'		  ";
		sql += "				 WHEN \"ProdNo\" = '88' 			   THEN  'G'			  ";
		sql += "			   	 WHEN \"ClCode1\" IN (3,4) 			   THEN 'D' "; // 有價股票
		sql += "			   	 WHEN \"ClCode1\" IN (1,2)				THEN 'C'  "; // 不動產
		sql += "			   	 WHEN \"ClCode1\" IN (5)				THEN 'A'  "; // 銀行保證
		sql += "			   	 ELSE 'B'"; // 動產
		sql += "			END AS \"Kind\"  "; // 類別
		sql += "			, \"AssetClass\"   	  "; // 5分類
		sql += "			,CASE WHEN \"AcctCode\" = '990' 							THEN 5	 "; // 5.催收
		sql += "			 WHEN \"ProdNo\" IN ('60','61','62') AND \"OvduTerm\" = 0	THEN 1	 "; // 1.協議0期
		sql += "			 WHEN \"OvduTerm\" = 0 										THEN 2	 "; // 2.正常0期
		sql += "			 WHEN \"OvduTerm\" IN (1,2)									THEN 3	 "; // 3.逾1~2期
		sql += "			 ELSE 4	 "; // 4.逾3期
		sql += "			 END								 AS \"Type\"	 ";
		sql += "			,\"PrinBalance\"  					 AS \"bal\" ";
		sql += "			FROM \"MonthlyFacBal\"  ";
		sql += "			WHERE \"YearMonth\" = :yymm ";
		sql += "	  		AND \"PrinBalance\" > 0  ";
		sql += "	  		UNION ALL  ";
		sql += "	  		SELECT   ";
		sql += "	  		'C'	 AS \"Kind\" ";
		sql += "	  		,'1'	 AS \"AssetClass\" ";
		sql += "	  		,8	 AS \"Type\" "; // 88風災金額
		sql += "			,SUM(\"PrinBalance\") 		AS \"bal\" "; // 88金額
		sql += "			FROM \"MonthlyFacBal\"  ";
		sql += "			WHERE \"YearMonth\" = :yymm ";
		sql += "	  		AND \"ProdNo\" = '88'  ";
		sql += "	  		AND \"AcctCode\" IN ('310','320','330')  ";
		sql += "	  		AND \"PrinBalance\" > 0  ";
		sql += "	  		UNION ALL  ";
		sql += "	  		SELECT   ";
		sql += "	  		'C'	 AS \"Kind\" ";
		sql += "	  		,'1'	 AS \"AssetClass\" ";
		sql += "	  		,9	 AS \"Type\" "; // 專案放款差額
		sql += "			,NVL(JSON_VALUE(\"JsonFields\",'$.oLoanBal'),0) - NVL(JSON_VALUE(\"JsonFields\",'$.LoanBal'),0)   		AS \"bal\" "; // 88金額
		sql += "			FROM \"CdComm\"  ";
		sql += "			WHERE \"CdType\" IN ('02')  ";
		sql += "				AND \"CdItem\" IN ('01') ";
		sql += "				AND \"EffectDate\" = :yymm*100+1 ";
		sql += "			)	 ";
		sql += "			group by \"Kind\"	 ";
		sql += "			)	 ";
		sql += "			select * from 	TEMPDATA ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", yearMonth);
		return this.convertToMap(query);
	}
}