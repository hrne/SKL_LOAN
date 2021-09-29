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

@Service
@Repository
/* 逾期放款明細 */
public class LM055ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

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
		
		this.info("lM055.findAll YYMM=" + iYear * 100 + iMonth);

		// 工作表

		
		/*
		 * 還需要補的
		 *  備呆子目 參考LM042 科子目計算表格
		 * 10623 備抵損失-擔保放款+
		 * 10624100 備抵損失-催收款項-放款部+
		 * 10624200 備抵損失-催收款項-營業稅提撥
		 * 或
		 * RBC表的RBC工作表I41欄 來源參考公式
		 *
		 *
		 *溢折價與催收費用
		 *10600304000 擔保放款-溢折價
		 *10601301000 催收款項-法務費用
		 *10601302000 催收款項-火險費用
		 *10601304000 催收款項-溢折價
		 * */
		String sql = " ";
		sql += "SELECT \"COL\"";
		sql += "	  ,\"KIND\"";
		sql += "	  ,\"AMT\"";
		sql += "	  ,ROUND ( NVL (( CASE";
		sql += "	      	   			WHEN \"COL\" = '3' THEN \"AMT\" * 0.005";
		sql += "	       	   			WHEN \"COL\" = '4' THEN \"AMT\" * 0.02";
		sql += "	       	   			WHEN \"COL\" = '5' THEN \"AMT\" * 0.1";
		sql += "	       	   			WHEN \"COL\" = '6' THEN \"AMT\" * 0.5";
		sql += "	       	  			WHEN \"COL\" = '7' THEN \"AMT\" * 1";
		sql += "	       	   			WHEN \"COL\" = '99' THEN \"AMT\" * 0.01";
		sql += "	   				END),0),0) AS \"Allowance\"";
		sql += "FROM(";
		//--逾期放款和未列入逾期應予評估放款
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
		sql += "	  AND M.\"AssetClass\" IS NOT NULL";
		sql += "	GROUP BY ( CASE";
		sql += "       	         WHEN M.\"OvduTerm\" IN (3,4,5,6) OR M.\"Status\" IN (2,6,7) THEN '1'";
		sql += "       	       ELSE '2' END )";
		sql += "	        ,( CASE";
		sql += "       	         WHEN M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') THEN 'Z'";
		sql += "       	       ELSE 'C' END )";
		sql += "	UNION";
		//--正常放款I
		sql += "	SELECT '3' AS \"COL\"";
		sql += "		  ,\"KIND\" AS \"KIND\"";
		sql += "		  ,SUM(\"AMT\") AS \"AMT\"";
		sql += "	FROM ( SELECT ( CASE";
		sql += "					  WHEN \"ClCode1\" IN (1,2) AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]')) THEN 'Z'";
		sql += "					  WHEN \"ClCode1\" IN (1,2) THEN 'C'";
		sql += "					  WHEN \"ClCode1\" IN (1,2) THEN 'D'";
		sql += "					  ELSE 'N'";
		sql += "					END ) AS \"KIND\"";
		sql += "	             ,SUM(M.\"PrinBalance\") AS \"AMT\"";
		sql += "	       FROM \"MonthlyFacBal\" M";
		sql += "		   LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "						   		  AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "		   WHERE M.\"YearMonth\" = :yymm";
		sql += "	  		 AND M.\"PrinBalance\" > 0";
		sql += "	  		 AND M.\"AssetClass\" IS NULL";
		sql += "	       GROUP BY (CASE";
		sql += "					   WHEN \"ClCode1\" IN (1,2) AND (M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]')) THEN 'Z'";
		sql += "					   WHEN \"ClCode1\" IN (1,2) THEN 'C'";
		sql += "					   WHEN \"ClCode1\" IN (1,2) THEN 'D'";
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
		//--應予注意II、可望收回III、收回困難IV、收回無望V
		sql += "	SELECT ( CASE";
		sql += "       	       WHEN M.\"PrinBalance\" = 1 THEN '7'";
		sql += "       	       WHEN M.\"OvduTerm\" >= 12 AND M.\"ProdNo\" IN ('60','61','62') THEN '4'";
		sql += "       	       WHEN M.\"OvduTerm\" >= 12 THEN '5'";
		sql += "       	       WHEN M.\"OvduTerm\" >= 7 THEN '4'";
		sql += "       	       WHEN M.\"OvduTerm\" >= 1 THEN '4'";
		sql += "       	       WHEN M.\"ProdNo\" IN ('60','61','62') THEN '4'";
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
		sql += "	  AND M.\"AssetClass\" IS NOT NULL";
		sql += "	GROUP BY (CASE";
		sql += "       	        WHEN M.\"PrinBalance\" = 1 THEN '7'";
		sql += "       	        WHEN M.\"OvduTerm\" >= 12 AND M.\"ProdNo\" IN ('60','61','62') THEN '4'";
		sql += "       	        WHEN M.\"OvduTerm\" >= 12 THEN '5'";
		sql += "       	        WHEN M.\"OvduTerm\" >= 7 THEN '4'";
		sql += "       	        WHEN M.\"OvduTerm\" >= 1 THEN '4'";
		sql += "       	        WHEN M.\"ProdNo\" IN ('60','61','62') THEN '4'";
		sql += "       	      END )";
		sql += "	        ,( CASE";
		sql += "       	         WHEN M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') THEN 'Z'";
		sql += "       	       ELSE 'C' END )";
		sql += "	UNION";
		//--購置住宅+修繕貸款
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
		sql += "	  AND M.\"AssetClass\" IS NULL";
		sql += "	  AND F.\"UsageCode\" = '02'";
		sql += "	GROUP BY ( CASE";
		sql += "       	         WHEN M.\"FacAcctCode\" = 340 OR REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]') THEN 'Z'";
		sql += "       	       ELSE 'C' END )";
		sql += "	UNION";		
		//--五類資產評估合計 
		sql += "	SELECT 'FIVE' AS \"COL\"";
		sql += "		  ,'FIVE' AS \"KIND\"";
		sql += "		  ,SUM(DECODE(\"COL\",'11',\"AMT\" * 0.005";
		sql += "		  					 ,'12',\"AMT\" * 0.015";
		sql += "		  					 ,'2',\"AMT\" * 0.02";
		sql += "		  					 ,'3',\"AMT\" * 0.1";
		sql += "		  					 ,'4',\"AMT\" * 0.5";
		sql += "		  					 ,'5',\"AMT\" * 1)) AS \"AMT\"";
		sql += "	FROM ( SELECT DECODE(F.\"UsageCode\",'02','12','11') AS \"COL\"";
		sql += "				 ,SUM(M.\"PrinBalance\") AS \"AMT\"";
		sql += "		   FROM \"MonthlyFacBal\" M";
		sql += "		   LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "			  	 			   	  AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "		   WHERE M.\"YearMonth\" = :yymm";
		sql += "	  		 AND M.\"PrinBalance\" > 0";
		sql += "	  	   	 AND M.\"AssetClass\" IS NULL";
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
		sql += "	  	   	 AND M.\"AssetClass\" IS NOT NULL";
		sql += "	       GROUP BY (CASE";
		sql += "       	       		   WHEN M.\"PrinBalance\" = 1 THEN '5'";
		sql += "       	       		   WHEN M.\"OvduTerm\" >= 12 AND M.\"ProdNo\" IN ('60','61','62') THEN '2'";
		sql += "       	       		   WHEN M.\"OvduTerm\" >= 12 THEN '3'";
		sql += "       	       		   WHEN M.\"OvduTerm\" >= 7 THEN '2'";
		sql += "       	       		   WHEN M.\"OvduTerm\" >= 1 THEN '2'";
		sql += "       	       		   WHEN M.\"ProdNo\" IN ('60','61','62') THEN '2'";
		sql += "       	     	     END )";
		sql += "	     )";
		sql += ")";	
		
		
		
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", iYear * 100 + iMonth);
		return this.convertToMap(query);
	}

//	@SuppressWarnings({ "unchecked" })
//	public List<Map<String, String>> findAll_1(TitaVo titaVo) throws Exception {
//
//		String iENTDY = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000);
//		String iYYMM = iENTDY.substring(0, 6);
//
//		this.info("lM055.findAll_1 ENTDY=" + iENTDY + ",YYMM=" + iYYMM);
//
//		// 手搞
//
//		String sql = "SELECT I.\"CustNo\"                             F0";
//		sql += "            ,I.\"CustId\"                             F1";
//		sql += "            ,I.\"FacmNo\"                             F2";
//		sql += "            ,I.\"BormNo\"                             F3";
//		sql += "            ,I.\"LoanBal\"                            F4";
//		sql += "            ,I.\"OvduDays\"                           F5";
//		sql += "            ,I.\"OvduDate\"                           F6";
//		sql += "            ,I.\"ClTypeJCIC\"                         F7";
//		sql += "            ,I.\"RateCode\"                       F8";
//		sql += "            ,CASE WHEN I.\"AssetKind\" = NULL THEN '#N/A'";
//		sql += "                  WHEN I.\"AssetKind\" = 0    THEN '#N/A'";
//		sql += "             ELSE CAST(I.\"AssetKind\" AS CHAR) END   F9";
//		sql += "            ,CASE WHEN I.\"OvduDate\" > 0 THEN '催'";
//		sql += "                  WHEN I.\"OvduDays\" = 0 THEN '#N/A'";
//		sql += "             ELSE CAST(I.\"OvduTerm\" AS CHAR) END    F10";
//		sql += "            ,I.\"AcctCode\"                           F11";
//		sql += "            ,I.\"Project\"                            F12";
//		sql += "            ,DECODE(I.\"Project\"";
//		sql += "                  ,'*'";
//		sql += "                  ,'Z'";
//		sql += "                  ,I.\"LoanType\")                    F13";
//		sql += "            ,I.\"Project\"                            F14";
//		sql += "      FROM (SELECT  I.\"CustNo\"";
//		sql += "                   ,I.\"CustId\"";
//		sql += "                   ,I.\"FacmNo\"";
//		sql += "                   ,I.\"BormNo\"";
//		sql += "                   ,I.\"AcCode\"";
//		sql += "                   ,I.\"RateCode\"";
//		sql += "                   ,I.\"LoanBal\"";
//		sql += "                   ,I.\"OvduDays\"";
//		sql += "                   ,I.\"OvduDate\"";
//		sql += "                   ,I.\"ClTypeJCIC\"";
//		sql += "                   ,I.\"ProdNo\"";
//		sql += "                   ,I.\"AssetKind\"";
//		sql += "                   ,M.\"OvduTerm\"";
//		sql += "                   ,M.\"AcctCode\"";
//		sql += "                   ,CASE WHEN M.\"AcctCode\" = '340' THEN '*'";
//		sql += "                         WHEN I.\"RateCode\" IN ('IA', 'IB', 'IC', 'ID', 'IE', 'IF', 'IG', 'IH') THEN '*'";
//		sql += "                         WHEN I.\"RateCode\" IN ('81', '82', '83', '84', '85', '86', '87', '88') THEN '*'";
//		sql += "                    ELSE '' END \"Project\"";
//		sql += "                   ,CASE WHEN I.\"ClTypeJCIC\" =    '1H' THEN 'E'";
//		sql += "                         WHEN I.\"ClTypeJCIC\" LIKE '1%' THEN 'D'";
//		sql += "                         WHEN I.\"ClTypeJCIC\" LIKE '2%' THEN 'C'";
//		sql += "                         WHEN I.\"ClTypeJCIC\" LIKE '3%' THEN 'B'";
//		sql += "                         WHEN I.\"ClTypeJCIC\" LIKE '4%' THEN 'B'";
//		sql += "                    ELSE 'A' END \"LoanType\"";
//		sql += "            FROM \"Ias34Ap\" I";
//		sql += "            LEFT JOIN \"MonthlyFacBal\" M ON M.\"YearMonth\" = :yymm";
//		sql += "                                         AND M.\"CustNo\"    =  I.\"CustNo\"";
//		sql += "                                         AND M.\"FacmNo\"    =  I.\"FacmNo\"";
//		sql += "                                             WHERE I.\"DataYM\" = :yymm) I";
//		sql += "      ORDER BY I.\"CustNo\"";
//		sql += "              ,I.\"FacmNo\"";
//		sql += "              ,I.\"BormNo\"";
//
//		this.info("sql=" + sql);
//
//		Query query;
//		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
//		query = em.createNativeQuery(sql);
//		query.setParameter("yymm", iYYMM);
//		return this.convertToMap(query);
//	}

}


//String sql = "SELECT I.\"CustNo\"                             F0";
//sql += "            ,I.\"CustId\"                             F1";
//sql += "            ,I.\"FacmNo\"                             F2";
//sql += "            ,I.\"ApplNo\"                             F3";
//sql += "            ,I.\"BormNo\"                             F4";
//sql += "            ,I.\"AcCode\"                             F5";
//sql += "            ,I.\"Status\"                             F6";
//sql += "            ,I.\"FirstDrawdownDate\"                  F7";
//sql += "            ,I.\"DrawdownDate\"                       F8";
//sql += "            ,I.\"FacLineDate\"                        F9";
//sql += "            ,I.\"MaturityDate\"                       F10";
//sql += "            ,I.\"LineAmt\"                            F11";
//sql += "            ,I.\"LoanBal\"                            F12";
//sql += "            ,I.\"IntAmt\"                             F13";
//sql += "            ,I.\"Fee\"                                F14";
//sql += "            ,I.\"Rate\"                               F15";
//sql += "            ,I.\"OvduDays\"                           F16";
//sql += "            ,I.\"OvduDate\"                           F17";
//sql += "            ,I.\"IndustryCode\"                       F18";
//sql += "            ,I.\"ClTypeJCIC\"                         F19";
//sql += "            ,I.\"CityCode\"                           F20";
//sql += "            ,I.\"RateCode\"                       F21";
//sql += "            ,I.\"CustKind\"                           F22";
//sql += "            ,CASE WHEN I.\"AssetKind\" = NULL THEN '#N/A'";
//sql += "                  WHEN I.\"AssetKind\" = 0    THEN '#N/A'";
//sql += "             ELSE CAST(I.\"AssetKind\" AS CHAR) END   F23";
//sql += "            ,I.\"ProdNo\"                             F24";
//sql += "            ,CASE WHEN I.\"AssetKind\" = NULL THEN '#N/A'";
//sql += "                  WHEN I.\"AssetKind\" = 0    THEN '#N/A'";
//sql += "             ELSE CAST(I.\"AssetKind\" AS CHAR) END   F25";
//sql += "            ,CASE WHEN I.\"OvduDate\" > 0 THEN '催'";
//sql += "                  WHEN I.\"OvduDays\" = 0 THEN '#N/A'";
//sql += "             ELSE CAST(I.\"OvduTerm\" AS CHAR) END    F26";
//sql += "            ,I.\"AcctCode\"                           F27";
//sql += "            ,I.\"Project\"                            F28";
//sql += "            ,DECODE(I.\"Project\"";
//sql += "                  ,'*'";
//sql += "                  ,'Z'";
//sql += "                   ,I.\"LoanType\")                   F29";
//sql += "      FROM (SELECT I.\"CustNo\"";
//sql += "                  ,I.\"CustId\"";
//sql += "                  ,I.\"FacmNo\"";
//sql += "                  ,I.\"BormNo\"";
//sql += "                  ,I.\"AcCode\"";
//sql += "                  ,I.\"Status\"";
//sql += "                  ,I.\"FirstDrawdownDate\"";
//sql += "                  ,I.\"DrawdownDate\"";
//sql += "                  ,I.\"FacLineDate\"";
//sql += "                  ,I.\"MaturityDate\"";
//sql += "                  ,I.\"LineAmt\"";
//sql += "                  ,I.\"LoanBal\"";
//sql += "                  ,I.\"IntAmt\"";
//sql += "                  ,I.\"Fee\"";
//sql += "                  ,I.\"Rate\"";
//sql += "                  ,I.\"OvduDays\"";
//sql += "                  ,I.\"OvduDate\"";
//sql += "                  ,I.\"IndustryCode\"";
//sql += "                  ,I.\"ClTypeJCIC\"";
//sql += "                  ,I.\"CityCode\"";
//sql += "                  ,I.\"RateCode\"";
//sql += "                  ,I.\"CustKind\"";
//sql += "                  ,I.\"ProdNo\"";
//sql += "                  ,I.\"AssetKind\"";
//sql += "                  ,M.\"OvduTerm\"";
//sql += "                  ,M.\"AcctCode\"";
//sql += "                  ,I.\"ApplNo\"";
//sql += "                  ,CASE WHEN M.\"AcctCode\" = '340' THEN '*'";
//sql += "                        WHEN I.\"ProdNo\" LIKE 'I%' THEN '*'";
//sql += "                        WHEN I.\"ProdNo\" LIKE '8%' THEN '*'";
//sql += "                   ELSE ' ' END \"Project\"";
//sql += "                  ,CASE WHEN I.\"ClTypeJCIC\" = '1H' THEN 'E'";
//sql += "                        WHEN I.\"ClTypeJCIC\" LIKE '1%' THEN 'D'";
//sql += "                        WHEN I.\"ClTypeJCIC\" LIKE '2%' THEN 'C'";
//sql += "                        WHEN I.\"ClTypeJCIC\" LIKE '3%' THEN 'B'";
//sql += "                        WHEN I.\"ClTypeJCIC\" LIKE '4%' THEN 'B'";
//sql += "                   ELSE 'A' END \"LoanType\"";
//sql += "            FROM \"Ias34Ap\" I";
//sql += "            LEFT JOIN \"MonthlyFacBal\" M ON M.\"YearMonth\" = :yymm";
//sql += "                                         AND M.\"CustNo\"    =  I.\"CustNo\"";
//sql += "                                         AND M.\"FacmNo\"    =  I.\"FacmNo\"";
//sql += "            WHERE I.\"DataYM\" = :yymm) I";
//sql += "      ORDER BY I.\"CustNo\"";
//sql += "              ,I.\"FacmNo\"";
//sql += "              ,I.\"BormNo\"";