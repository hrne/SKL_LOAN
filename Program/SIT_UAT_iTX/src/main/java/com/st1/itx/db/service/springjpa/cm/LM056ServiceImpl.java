package com.st1.itx.db.service.springjpa.cm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAll(TitaVo titaVo, String isAllData) throws Exception {

		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		int iYear = (Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 10000;
		int iMonth = ((Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 100) % 100;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		// 當日
		int nowDate = Integer.valueOf(iEntdy);
		Calendar calMonthDate = Calendar.getInstance();
		// 設當年月底日 0是月底
		calMonthDate.set(iYear, iMonth, 0);

		int thisMonthEndDate = Integer.valueOf(dateFormat.format(calMonthDate.getTime()));

		boolean isMonthZero = iMonth - 1 == 0;

		if (nowDate < thisMonthEndDate) {
			iYear = isMonthZero ? (iYear - 1) : iYear;
			iMonth = isMonthZero ? 12 : iMonth - 1;
		}
		
		this.info("lM056.findAll nowDate=" + nowDate + ",yymm=" + ((iYear * 100) +iMonth));
		

		String sql = " ";
		sql += "	SELECT MLB.\"CustNo\"";
		sql += "		  ,C.\"CustName\"";
		sql += "		  ,C.\"CustId\"";
		sql += "		  ,SUM(MLB.\"LoanBalance\") AS \"LoanBalance\"";
		sql += "		  ,(CASE";
		sql += "			  WHEN L.\"SyndNo\" <> 0 THEN '聯貸'";
		sql += "			  WHEN MLB2.\"CustNo\" IS NOT NULL THEN '**'";
		sql += "			  WHEN R.\"ReltCode\" IS NOT NULL THEN '*'";
		sql += "			ELSE ' ' END ) AS \"KIND\"";
		sql += "		  ,'#N/A' AS \"isRelt\"";
		sql += "	FROM \"MonthlyLoanBal\" MLB";
		sql += "	LEFT JOIN \"MonthlyFacBal\" MFB ON MFB.\"CustNo\" = MLB.\"CustNo\"";
		sql += "								   AND MFB.\"FacmNo\" = MLB.\"FacmNo\"";
		sql += "								   AND MFB.\"YearMonth\" = MLB.\"YearMonth\"";
		sql += "	LEFT JOIN ( SELECT * ";
		sql += "				FROM ( SELECT M.\"CustNo\"";
		sql += "					   		 ,SUM(F.\"LineAmt\") AS \"LineAmt\"";
		sql += "					   FROM \"MonthlyFacBal\" M ";
		sql += "					   LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "											  AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "					   WHERE M.\"YearMonth\" = :yymm";
		sql += "					   GROUP BY M.\"CustNo\" ) M ";
		sql += "				WHERE M.\"LineAmt\" > 100000000 ) MLB2";
		sql += "	ON MLB2.\"CustNo\" = MFB.\"CustNo\"";
		sql += "	LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = MLB.\"CustNo\"";
		sql += "	LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" = MLB.\"CustNo\"";
		sql += "							   AND L.\"FacmNo\" = MLB.\"FacmNo\"";
		sql += "							   AND L.\"BormNo\" = MLB.\"BormNo\"";
		sql += "	LEFT JOIN \"ReltMain\" R ON R.\"CustNo\" = C.\"CustNo\"";
		sql += "	WHERE MLB.\"YearMonth\" = :yymm";
		sql += "	  AND MLB.\"LoanBalance\" > 0 ";
		if (isAllData == "N") {
			sql += "	  AND MLB2.\"CustNo\" IS NOT NULL ";
		}
		sql += "	GROUP BY MLB.\"CustNo\"";
		sql += "		    ,C.\"CustName\"";
		sql += "		    ,(CASE";
		sql += "			    WHEN L.\"SyndNo\" <> 0 THEN '聯貸'";
		sql += "			    WHEN MLB2.\"CustNo\" IS NOT NULL THEN '**'";
		sql += "			    WHEN R.\"ReltCode\" IS NOT NULL THEN '*'";
		sql += "			  ELSE ' ' END )";
		sql += "		    ,'#N/A'";
		sql += "	ORDER BY \"LoanBalance\" DESC";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", (iYear * 100) +iMonth);

		return this.convertToMap(query);
	}

	
	
	
	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAll2(TitaVo titaVo) throws Exception {

		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		int iYear = (Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 10000;
		int iMonth = ((Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 100) % 100;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		// 當日
		int nowDate = Integer.valueOf(iEntdy);
		Calendar calMonthDate = Calendar.getInstance();
		// 設當年月底日 0是月底
		calMonthDate.set(iYear, iMonth, 0);

		int thisMonthEndDate = Integer.valueOf(dateFormat.format(calMonthDate.getTime()));

		boolean isMonthZero = iMonth - 1 == 0;

		if (nowDate < thisMonthEndDate) {
			iYear = isMonthZero ? (iYear - 1) : iYear;
			iMonth = isMonthZero ? 12 : iMonth - 1;
		}
		
		this.info("lM056.findAll nowDate=" + nowDate + ",yymm=" + ((iYear * 100) +iMonth));
		

		String sql = " ";
		sql += "SELECT * FROM(";
		sql += "	SELECT ( CASE";
		sql += "       	       WHEN ( M.\"OvduTerm\" > 3 AND M.\"OvduTerm\" <= 6) OR (CL.\"LegalProg\" IN ('056','057','058','060') AND (M.\"OvduTerm\" > 3 OR M.\"PrinBalance\" = 1) AND L.\"Status\" IN (2,6,7)) THEN 'C'";
		sql += "       	     ELSE 'B' END ) AS \"KIND\"";
		sql += "	      ,SUM(M.\"PrinBalance\") AS \"AMT\"";
		sql += "	FROM \"MonthlyLoanBal\" ML";
		sql += "	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = ML.\"CustNo\"";
		sql += "						   AND F.\"FacmNo\" = ML.\"FacmNo\"";
		sql += "	LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = ML.\"CustNo\"";
		sql += "    LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" =  ML.\"CustNo\"";
		sql += "                               AND L.\"FacmNo\" =  ML.\"FacmNo\"";
		sql += "                               AND L.\"BormNo\" =  ML.\"BormNo\"";
		sql += "    LEFT JOIN \"MonthlyFacBal\" M ON M.\"YearMonth\" = ML.\"YearMonth\"";
		sql += "                                 AND M.\"CustNo\" = ML.\"CustNo\"";
		sql += "                                 AND M.\"FacmNo\" = ML.\"FacmNo\"";
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
		sql += "	  AND M.\"PrinBalance\" > 0";
		sql += "	  AND M.\"AssetClass\" IS NOT NULL";
		sql += "	GROUP BY (CASE";
		sql += "       	        WHEN ( M.\"OvduTerm\" > 3 AND M.\"OvduTerm\" <= 6) OR (CL.\"LegalProg\" IN ('056','057','058','060') AND (M.\"OvduTerm\" > 3 OR M.\"PrinBalance\" = 1) AND L.\"Status\" IN (2,6,7)) THEN 'C'";
		sql += "       	      ELSE 'B' END )";
		sql += "	UNION";
		sql += "	SELECT 'TOTAL' AS \"KIND\"";
		sql += "		  ,SUM(\"AMT\") AS \"AMT\"";
		sql += "	FROM ( SELECT SUM(M.\"PrinBalance\") AS \"AMT\"";
		sql += "	       FROM \"MonthlyFacBal\" M";
		sql += "		   WHERE M.\"YearMonth\" = :yymm";
		sql += "	  		 AND M.\"PrinBalance\" > 0";
		sql += "	  		 AND M.\"AssetClass\" IS NULL";
		sql += "	       UNION";
		sql += "	       SELECT SUM(\"DbAmt\" - \"CrAmt\") AS \"AMT\"";
		sql += "	       FROM \"AcMain\"";
		sql += "	       WHERE \"AcNoCode\" IN (10600304000,10601301000,10601302000,10601304000)";
		sql += "	         AND \"MonthEndYm\" = :yymm )";
		sql += "	GROUP BY 'TOTAL'";
		sql += "	UNION";
		sql += "	SELECT 'NTOTAL' AS \"KIND\"";
		sql += "		  ,SUM(\"AMT\") AS \"AMT\"";
		sql += "	FROM ( SELECT SUM(M.\"PrinBalance\") AS \"AMT\"";
		sql += "	       FROM \"MonthlyFacBal\" M";
		sql += "		   WHERE M.\"YearMonth\" = :yymm";
		sql += "	  		 AND M.\"PrinBalance\" > 0";
		sql += "	  		 AND M.\"AssetClass\" IS NULL";
		sql += "	       UNION";
		sql += "	       SELECT SUM(\"DbAmt\" - \"CrAmt\") AS \"AMT\"";
		sql += "	       FROM \"AcMain\"";
		sql += "	       WHERE \"AcNoCode\" IN (10601301000,10601302000,10601304000)";
		sql += "	         AND \"MonthEndYm\" = :yymm )";
		sql += "	GROUP BY 'NTOTAL'";
		sql += ")";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", (iYear * 100) +iMonth);

		return this.convertToMap(query);
	}
}

//String sql = "SELECT J.\"SubTranCode\" AS F0";
//sql += "			,J.\"CustNo\" AS F1";
//sql += "			,J.\"FacmNo\" AS F2";
//sql += "			,J.\"BormNo\" AS F3";
//sql += "			,C.\"CustName\" AS F4";
//sql += "			,M.\"LoanBal\" AS F5";
//sql += "			,M.\"MaturityDate\" AS F6";
//sql += "			,CASE";
//sql += "			   WHEN TRUNC(F.\"OvduDate\" / 100) = :yymm THEN 'B3'";
//sql += "			   WHEN M.\"MaturityDate\" < :l4mdy THEN 'B1'";
//sql += "			   WHEN L.\"LegalProg\" IN ('056','058','060') THEN 'C5'";
//sql += "			 ELSE 'B3' END AS F7";
//sql += "			,NVL(L.\"LegalProg\",'#N/A') AS F8";
//sql += "			,'C7' AS F9";
//sql += "			,DECODE(L.\"Amount\",0,'#N/A','無擔保') AS F10";
//sql += "			,DECODE(F.\"RenewCode\",'2','協議',' ') AS F11";
//sql += "	  FROM(SELECT CAST(SUBSTR(J.\"AcctNo\", 1, 7) AS DECIMAL) \"CustNo\"";
//sql += "				 ,CAST(SUBSTR(J.\"AcctNo\",8,3) AS DECIMAL) \"FacmNo\"";
//sql += "				 ,CAST(SUBSTR(J.\"AcctNo\",11, 3) AS DECIMAL) \"BormNo\"";
//sql += "				 ,J.\"SubTranCode\"";
//sql += "		   FROM \"JcicB201\" J";
//sql += "		   WHERE J.\"DataYM\" = :yymm) J";
//sql += "	  LEFT JOIN(SELECT L.\"CustNo\"";
//sql += "					  ,L.\"FacmNo\"";
//sql += "					  ,MAX(L.\"LegalProg\") \"LegalProg\"";
//sql += "					  ,SUM(L.\"Amount\") \"Amount\"";
//sql += "				FROM(SELECT CAST(SUBSTR(J.\"AcctNo\",1,7) AS DECIMAL) \"CustNo\"";
//sql += "					  	   ,CAST(SUBSTR(J.\"AcctNo\",8,3) AS DECIMAL) \"FacmNo\"";
//sql += "						   ,L.\"LegalProg\"";
//sql += "					       ,0 \"Amount\"";
//sql += "						   ,ROW_NUMBER() OVER (PARTITION BY SUBSTR(J.\"AcctNo\",1,10) ORDER BY L.\"RecordDate\" DESC) AS SEQ";
//sql += "					 FROM \"JcicB201\" J";
//sql += "					 LEFT JOIN \"CollLaw\" L ON L.\"CaseCode\" = '1'";
//sql += "											AND L.\"CustNo\" = CAST(SUBSTR(J.\"AcctNo\", 1, 7) AS DECIMAL)";
//sql += "											AND L.\"FacmNo\" = CAST(SUBSTR(J.\"AcctNo\", 8, 3) AS DECIMAL)";
//sql += "											AND L.\"LegalProg\" < 900";
//sql += "					 WHERE J.\"DataYM\" = :yymm";
//sql += "					 UNION ALL";
//sql += "					 SELECT CAST(SUBSTR(J.\"AcctNo\",1,7) AS DECIMAL) \"CustNo\"";
//sql += "						   ,CAST(SUBSTR(J.\"AcctNo\",8,3) AS DECIMAL) \"FacmNo\"";
//sql += "						   ,NULL \"LegalProg\"";
//sql += "						   ,L.\"Amount\"";
//sql += "						   ,ROW_NUMBER() OVER(PARTITION BY SUBSTR(J.\"AcctNo\",1,10) ORDER BY L.\"RecordDate\" DESC) AS SEQ";
//sql += "					 FROM \"JcicB201\" J";
//sql += "					 LEFT JOIN \"CollLaw\" L ON L.\"CaseCode\" ='1'";
//sql += "											AND L.\"CustNo\" = CAST(SUBSTR(J.\"AcctNo\", 1, 7) AS DECIMAL)";
//sql += "											AND L.\"FacmNo\" = CAST(SUBSTR(J.\"AcctNo\", 8, 3) AS DECIMAL)";
//sql += "											AND L.\"LegalProg\" = 901";
//sql += "					 WHERE J.\"DataYM\" = :yymm) L";
//sql += "				 WHERE L.\"SEQ\" = 1";
//sql += "				 GROUP BY L.\"CustNo\", L.\"FacmNo\") L";
//sql += "	  ON L.\"CustNo\" = J.\"CustNo\"";
//sql += "	  AND L.\"FacmNo\" = J.\"FacmNo\"";
//sql += "	  LEFT JOIN \"MonthlyFacBal\" F ON F.\"YearMonth\" = :yymm";
//sql += "								   AND F.\"CustNo\" = J.\"CustNo\"";
//sql += "								   AND F.\"FacmNo\" = J.\"FacmNo\"";
//sql += "	  LEFT JOIN \"LoanBorMain\" M ON M.\"CustNo\" = J.\"CustNo\"";
//sql += "	  							 AND M.\"FacmNo\" = J.\"FacmNo\"";
//sql += "	  							 AND M.\"BormNo\" = J.\"BormNo\"";
//sql += "	  LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = J.\"CustNo\"";