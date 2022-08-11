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
public class LM054ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}
	/**
	 * 查詢資料
	 * 
	 * @param titaVo
	 * @param monthDate 西元年月底日
	 * @param isAcctCode 是否為會計科目項目
	 * @return 
	 * @throws Exception 
	 * 
	 */
	public List<Map<String, String>> findAll(TitaVo titaVo, int monthDate, String isAcctCode) throws Exception {

		// 西元年
		int iYear = monthDate / 10000;
		// 月
		int iMonth = (monthDate / 100) % 100;

		// 當前年月
		String iYearMonth = iYear * 100 + iMonth + "";

		//判斷前一個年月
		iYear = iMonth - 1 == 0 ? (iYear - 1) : iYear;
		iMonth = iMonth - 1 == 0 ? 12 : iMonth - 1;

		// 上個年月
		String lYearMonth = (iYear * 100) + iMonth + "";

		// 月底
		String eYmd = monthDate + "";
		// 月初
		String sYmd = (monthDate / 100) + "01";

		this.info("lM054.findAll iYearMonth=" + iYearMonth + ",iYearMonth=" + lYearMonth + "sYmd=" + sYmd + ",eYmd="
				+ eYmd);

		String sql = "";
		if (isAcctCode.equals("N")) {
			/*
			 * F0 放款代號(戶號) F1 放款種類 F2 放款對象名稱 (戶名) F3 放款對象關係人代碼 F4 利害關係人代碼 F5
			 * 是否為專案運用公共及社會福利事業投資 F6 是否為聯合貸款 F7 持有資產幣別 F8 放款日期 F9 到期日期 F10 放款年利率 F11 放款餘額
			 * F12 應收利息 F13 擔保品設定順位 F14擔保品估計總值 F15 擔保品核貸金額 F16 轉催收日期 F17 催收狀態 (電催) F18
			 * 催收狀態執行日期 (電催) F19 備抵損失總額 (公式) F20 評估分類 F21 IFRS9評估階段 (公式) F22 備註 (協議戶) F23 備註
			 * (資金專案運用) F24 逾期天數
			 */

			sql += "	SELECT LPAD(M.\"CustNo\",7,0) AS F0";
			sql += "		  ,(CASE";
			sql += "			 WHEN M.\"ClCode1\" IN (3) THEN 'D'";
			sql += "			 WHEN M.\"ClCode1\" IN (1,2) ";
			sql += "			  AND (REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]')";
			sql += "			  OR REGEXP_LIKE(M.\"ProdNo\",'8[1-8]')";
			sql += "			  OR M.\"FacAcctCode\" = 340 ) ";
			sql += "			 THEN 'Z' ";
			sql += "			 WHEN M.\"ClCode1\" IN (1,2) THEN 'C'";
			sql += "			ELSE '99' END ) AS \"ClNo\"";
			sql += "		  ,\"Fn_ParseEOL\"(C.\"CustName\",0) AS F2";
			sql += "		  ,(CASE";
			sql += "			  WHEN R.\"ReltCode\" IS NULL THEN 'A'";
			sql += "			  WHEN R.\"ReltCode\" ='08' THEN 'C'";
			sql += "			ELSE 'B' END ) AS F3";
			//EntCode 0=個金,1=企金,2=企金自然人
			sql += "		  ,(CASE";
			sql += "			  WHEN R.\"ReltCode\" IS NULL AND M.\"EntCode\" <> 1 THEN 'D'";
			sql += "			  WHEN R.\"ReltCode\" IS NULL AND M.\"EntCode\" = 1 THEN 'C'";
			sql += "			  WHEN R.\"ReltCode\" IS NOT NULL AND M.\"EntCode\" <> 1 THEN 'B'";
			sql += "			  WHEN R.\"ReltCode\" IS NOT NULL AND M.\"EntCode\" = 1 THEN 'A'";
			sql += "			ELSE ' ' END ) AS F4";
			sql += "		  ,(CASE";
			sql += "			  WHEN REGEXP_LIKE(M2.\"ProdNo\",'I[A-Z]') OR M2.\"FacAcctCode\" = 340 THEN 'Y'";
			sql += "			ELSE 'N' END ) AS F5";
			sql += "		  ,DECODE(FCA.\"SyndNo\",0,'N','Y') AS F6";
			sql += "		  ,'TWD' AS F7";
			sql += "		  ,L.\"DrawdownDate\" AS F8";
			sql += "		  ,L.\"MaturityDate\" AS F9";
			sql += "		  ,L.\"StoreRate\" / 100 AS F10";
			sql += "		  ,M.\"LoanBalance\" AS F11";
			sql += "		  ,M.\"IntAmtAcc\" AS F12";
			sql += "		  ,'1' AS F13";
			sql += "		  ,NVL(CM.\"EvaAmt\",0) AS F14";
			sql += "		  ,NVL(F.\"LineAmt\",0) AS F15";
			sql += "		  ,M2.\"OvduDate\" AS F16";
			sql += "		  ,CT.\"ResultCode\" AS F17";
			sql += "		  ,CT.\"TelDate\" AS F18";
			sql += "		  ,' ' AS F19";
			sql += "		  ,NVL(SUBSTR(M2.\"AssetClass\",0,1),1) AS F20";
			sql += "		  ,' ' AS F21";
			sql += "		  ,(CASE";
			sql += "			  WHEN M2.\"ProdNo\" IN ('60','61','62') THEN '協議戶'";
			sql += "			ELSE ' ' END ) AS F22";
			sql += "		  ,(CASE";
			sql += "			  WHEN  REGEXP_LIKE(M2.\"ProdNo\",'I[A-Z]') OR M2.\"FacAcctCode\" = 340 THEN '資金專案運用'";
			sql += "			ELSE ' ' END ) AS F23";
			sql += "		  ,M2.\"OvduDays\" AS F24";
			sql += "		  ,M.\"CustNo\" || M.\"FacmNo\" || L.\"BormNo\" AS F25";
			sql += "	FROM \"MonthlyLoanBal\" M";
			sql += "	LEFT JOIN ( ";
			sql += "		SELECT DISTINCT \"CustNo\"";
			sql += "			  ,\"FacmNo\"";
			sql += "		FROM ( SELECT \"CustNo\"";
			sql += "					 ,\"FacmNo\"";
			sql += "			   FROM \"MonthlyFacBal\"";
			sql += "			   WHERE \"YearMonth\" = :yymm ";
			sql += "			     AND \"PrinBalance\" > 0 ";
			sql += "			     AND (\"OvduTerm\" >= 3 ";
			sql += "			      OR \"AcctCode\" = 990 ";
			sql += "			      OR \"ProdNo\" IN ('60','61','62'))";
			sql += "			   UNION";
			sql += "			   SELECT R.\"CustNo\"";
			sql += "			   		 ,M2.\"FacmNo\"";
			sql += "			   FROM ( SELECT M.\"CustNo\"";
			sql += "					   		,SUM(F.\"LineAmt\") AS \"LineAmt\"";
			sql += "					  FROM \"MonthlyFacBal\" M ";
			sql += "					  LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
			sql += "					   					     AND F.\"FacmNo\" = M.\"FacmNo\"";
			sql += "					  WHERE M.\"YearMonth\" = :yymm ";
			sql += "			     		AND M.\"PrinBalance\" > 0 ";
			sql += "					  GROUP BY M.\"CustNo\" ) R ";
			sql += "				LEFT JOIN \"MonthlyFacBal\" M2 ON M2.\"CustNo\" = R.\"CustNo\"";
			sql += "											  AND M2.\"YearMonth\" = :yymm ";
			sql += "											  AND M2.\"PrinBalance\" > 0 ";
			sql += "				WHERE R.\"LineAmt\" >= 100000000 ))R";
			sql += "	 ON R.\"CustNo\" = M.\"CustNo\"";
			sql += "	AND R.\"FacmNo\" = M.\"FacmNo\"";
			sql += "	LEFT JOIN \"MonthlyFacBal\" M2 ON M2.\"CustNo\" = M.\"CustNo\"";
			sql += "								   AND M2.\"FacmNo\" = M.\"FacmNo\"";
			sql += "								   AND M2.\"YearMonth\" = :yymm";
			sql += "	LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\"";
			sql += "	LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" = M.\"CustNo\"";
			sql += "							   AND L.\"FacmNo\" = M.\"FacmNo\"";
			sql += "							   AND L.\"BormNo\" = M.\"BormNo\"";
			sql += "	LEFT JOIN \"ClMain\" CM ON CM.\"ClCode1\" = M.\"ClCode1\"";
			sql += "						   AND CM.\"ClCode2\" = M.\"ClCode2\"";
			sql += "						   AND CM.\"ClNo\" = M.\"ClNo\"";
			sql += "	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
			sql += "						    AND F.\"FacmNo\" = M.\"FacmNo\"";
			sql += "	LEFT JOIN \"FacCaseAppl\" FCA ON FCA.\"ApplNo\" = F.\"ApplNo\"";
			sql += "	LEFT JOIN \"ReltMain\" R ON R.\"CustNo\" = C.\"CustNo\"";
			sql += "	LEFT JOIN ( SELECT \"CustNo\"";
			sql += "					  ,\"FacmNo\"";
			sql += "					  ,\"TelDate\"";
			sql += "					  ,\"ResultCode\"";
			sql += "					  ,ROW_NUMBER () OVER (PARTITION BY \"CustNo\"";
			sql += "					  								   ,\"FacmNo\"";
			sql += "					  					   ORDER BY \"AcDate\" DESC";
			sql += "					  							   ,\"TitaTxtNo\" DESC ) AS \"SEQ\"";
			sql += "				FROM \"CollTel\" ) CT";
			sql += "	ON CT.\"CustNo\" = M2.\"CustNo\" AND CT.\"FacmNo\" = M2.\"FacmNo\"";
			sql += "									  AND CT.\"SEQ\" = 1 ";
			sql += "	WHERE M.\"YearMonth\" = :yymm";
			sql += "	  AND M.\"LoanBalance\" > 0 ";
			sql += "	  AND R.\"CustNo\" IS NOT NULL";	
			sql += "	ORDER BY M2.\"CustNo\"";
			sql += "			,M2.\"FacmNo\"";
			sql += "			,L.\"BormNo\"";
		} else {

			sql += "	SELECT :eymd  AS F0";
			sql += "		  ,DECODE(R.\"ClNo\",'A','ZZ','B','ZZ',R.\"ClNo\") AS F1";
			sql += "		  ,CASE";
			sql += "		  	 WHEN R.\"ClNo\" = 'D' THEN '有價證券'";
			sql += "		  	 WHEN R.\"ClNo\" = 'Z' THEN '不動產抵押放款'";
			sql += "		  	 WHEN R.\"ClNo\" = 'C' THEN '不動產擔保放款'";
			sql += "		  	 WHEN R.\"ClNo\" = 'A' THEN '放款折溢價'";
			sql += "		  	 WHEN R.\"ClNo\" = 'B' THEN '放款催收費用與折溢價'";
			sql += "		   ELSE ' ' END AS F2 ";
			sql += "		  ,'A' AS F3";
			sql += "		  ,'D' AS F4";
			sql += "		  ,CASE";
			sql += "		  	 WHEN R.\"ClNo\" = 'Z' THEN 'Y'";
			sql += "		   ELSE 'N' END AS F5 ";
			sql += "		  ,'N' AS F6";
			sql += "		  ,'TWD' AS F7";
			sql += "		  ,:symd AS F8";
			sql += "		  ,:eymd AS F9";
			sql += "		  ,0 AS F10";
			sql += "		  ,NVL(R.\"LoanBalance\",0) AS F11";
			sql += "		  ,NVL(R.\"IntAmtAcc\",0) AS F12";
			sql += "		  ,1 AS F13";
			sql += "		  ,0 AS F14";
			sql += "		  ,NVL(R.\"LineAmt\",0) AS F15";
			sql += "		  ,:eymd AS F16";
			sql += "		  ,'A' AS F17";
			sql += "		  ,:eymd AS F18";
			sql += "		  ,1 AS F19";
			sql += "		  ,1 AS F20";
			sql += "		  ,' ' AS F21";
			sql += "		  ,'1' AS F22";
			sql += "		  ,CASE";
			sql += "		  	 WHEN R.\"ClNo\" = 'Z' THEN '資金專案運用'";
			sql += "		   ELSE ' ' END AS F23 ";
			sql += "		  ,0 AS F24";
			sql += "		  ,' ' AS F25";
			sql += "	FROM ( ";

			sql += "	SELECT (CASE";
			sql += "			  WHEN M.\"ClCode1\" IN (3) THEN 'D'";
			sql += "			  WHEN M.\"ClCode1\" IN (1,2) ";
			sql += "			   AND (REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]')";
			sql += "			   OR REGEXP_LIKE(M.\"ProdNo\",'8[1-8]')";
			sql += "			   OR M.\"FacAcctCode\" = 340 ) ";
			sql += "			  THEN 'Z' ";
			sql += "			  WHEN M.\"ClCode1\" IN (1,2) THEN 'C'";
			sql += "			ELSE '99' END ) AS \"ClNo\"";
			sql += "		  ,SUM(M.\"LoanBalance\") AS \"LoanBalance\"";
			sql += "		  ,SUM(M.\"IntAmtAcc\") AS \"IntAmtAcc\"";
			sql += "		  ,SUM(NVL(F.\"LineAmt\",0)) AS \"LineAmt\"";
			sql += "	FROM \"MonthlyLoanBal\" M";
			sql += "	LEFT JOIN ( ";
			sql += "		SELECT DISTINCT \"CustNo\"";
			sql += "			  ,\"FacmNo\"";
			sql += "		FROM ( SELECT \"CustNo\"";
			sql += "					 ,\"FacmNo\"";
			sql += "			   FROM \"MonthlyFacBal\"";
			sql += "			   WHERE \"YearMonth\" = :yymm ";
			sql += "			     AND \"PrinBalance\" > 0 ";
			sql += "			     AND (\"OvduTerm\" >= 3 ";
			sql += "			      OR \"AcctCode\" = 990 ";
			sql += "			      OR \"ProdNo\" IN ('60','61','62'))";
			sql += "			   UNION";
			sql += "			   SELECT R.\"CustNo\"";
			sql += "			   		 ,M2.\"FacmNo\"";
			sql += "			   FROM ( SELECT M.\"CustNo\"";
			sql += "					   		,SUM(F.\"LineAmt\") AS \"LineAmt\"";
			sql += "					  FROM \"MonthlyFacBal\" M ";
			sql += "					  LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
			sql += "					   					     AND F.\"FacmNo\" = M.\"FacmNo\"";
			sql += "					  WHERE M.\"YearMonth\" = :yymm ";
			sql += "			     		AND M.\"PrinBalance\" > 0 ";
			sql += "					  GROUP BY M.\"CustNo\" ) R ";
			sql += "				LEFT JOIN \"MonthlyFacBal\" M2 ON M2.\"CustNo\" = R.\"CustNo\"";
			sql += "											  AND M2.\"YearMonth\" = :yymm ";
			sql += "											  AND M2.\"PrinBalance\" > 0 ";
			sql += "				WHERE R.\"LineAmt\" >= 100000000 ))R";
			sql += "	 ON R.\"CustNo\" = M.\"CustNo\"";
			sql += "	AND R.\"FacmNo\" = M.\"FacmNo\"";
			sql += "	LEFT JOIN \"MonthlyFacBal\" M2 ON M2.\"CustNo\" = M.\"CustNo\"";
			sql += "								   AND M2.\"FacmNo\" = M.\"FacmNo\"";
			sql += "								   AND M2.\"YearMonth\" = :yymm";
			sql += "	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
			sql += "						    AND F.\"FacmNo\" = M.\"FacmNo\"";
			sql += "	WHERE M.\"YearMonth\" = :yymm";
			sql += "	  AND M.\"LoanBalance\" > 0 ";
			sql += "	  AND R.\"CustNo\" IS NULL";	
			sql += "	GROUP BY CASE";
			sql += "			   WHEN M.\"ClCode1\" IN (3) THEN 'D'";
			sql += "			   WHEN M.\"ClCode1\" IN (1,2) ";
			sql += "			    AND (REGEXP_LIKE(M.\"ProdNo\",'I[A-Z]')";
			sql += "			    OR REGEXP_LIKE(M.\"ProdNo\",'8[1-8]')";
			sql += "			    OR M.\"FacAcctCode\" = 340 ) ";
			sql += "			   THEN 'Z' ";
			sql += "			   WHEN M.\"ClCode1\" IN (1,2) THEN 'C'";
			sql += "			ELSE '99' END ";
			sql += "	UNION";
			sql += "	SELECT 'A' AS \"ClNo\"";
			sql += "          ,ROUND(SUM(NVL(I.\"AccumDPAmortized\",0)),0) AS \"LoanBalance\"";
			sql += "          ,0 AS \"IntAmtAcc\"";
			sql += "          ,ROUND(SUM(NVL(I.\"AccumDPAmortized\",0)),0) AS \"LineAmt\"";
			sql += "	FROM \"Ias39IntMethod\" I";
			sql += "	LEFT JOIN \"MonthlyLoanBal\" MLB ON MLB.\"YearMonth\" = I.\"YearMonth\"";
			sql += "									AND MLB.\"CustNo\" = I.\"CustNo\"";
			sql += "									AND MLB.\"FacmNo\" = I.\"FacmNo\"";
			sql += "									AND MLB.\"BormNo\" = I.\"BormNo\"";
			sql += "	WHERE I.\"YearMonth\" = :yymm ";
			sql += "	  AND MLB.\"AcctCode\" <> 990 ";
			sql += "	UNION";
			sql += "	SELECT 'B' AS \"ClNo\"";
			sql += "		  ,\"LoanBal\" AS \"LoanBalance\"";
			sql += "		  ,0 AS \"IntAmtAcc\"";
			sql += "		  ,\"LoanBal\" AS \"LineAmt\"";
			sql += "	FROM \"MonthlyLM052AssetClass\"";
			sql += "	WHERE \"YearMonth\" = :yymm ";
			sql += "	  AND \"AssetClassNo\" = 62 ";
			sql += " ) R";
			sql += " ORDER BY DECODE(R.\"ClNo\",'C','1','D','2','Z','3','A','4','5') ASC";
		}

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		if (isAcctCode.equals("N")) {

			query.setParameter("yymm", iYearMonth);
		} else {
			query.setParameter("symd", sYmd);// 20211201
			query.setParameter("eymd", eYmd);// 20211231
			query.setParameter("yymm", iYearMonth);
//			query.setParameter("lyymm", lYearMonth);// 202111
//			query.setParameter("tyymm", iYearMonth);// 202112
		}
		return this.convertToMap(query);
	}

	/**
	 * 查詢資料 Ias34Ap
	 * @param titaVo
	 * @return 
	 * @throws Exception 
	 * 
	 */
	public List<Map<String, String>> ias34Ap(TitaVo titaVo) throws Exception {

		String iENTDY = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000);
		String iYearMonth = iENTDY.substring(0, 6);

		this.info("lM054.ias34Ap ENTDY=" + iENTDY + ",iYearMonth=" + iYearMonth);

		String sql = "SELECT M.\"CustNo\"             F0";
		sql += "            ,C.\"CustId\"             F1";
		sql += "            ,M.\"FacmNo\"             F2";
		sql += "            ,F.\"ApplNo\"             F3";
		sql += "            ,M.\"BormNo\"             F4";
		sql += "            ,AR.\"AcNoCode\"            F5";
		sql += "            ,L.\"Status\"             F6";
		sql += "            ,F.\"FirstDrawdownDate\"  F7";
		sql += "            ,L.\"DrawdownDate\"       F8";
		sql += "            ,0 					      F9"; // FacDrawdownDate
		sql += "            ,L.\"MaturityDate\"       F10";
		sql += "            ,F.\"LineAmt\"            F11";
		sql += "            ,L.\"DrawdownAmt\"        F12";
		sql += "            ,F.\"AcctFee\"            F13";
		sql += "            ,M.\"LoanBalance\"        F14";
		sql += "            ,M.\"IntAmtAcc\"          F15";
		sql += "            ,ML.\"FireFee\"";
		sql += "         	+ ML.\"LawFee\"  F16";
		sql += "            ,M.\"StoreRate\" / 100    F17";
		sql += "            ,ML.\"OvduDays\"          F18";
		sql += "            ,ML.\"OvduDate\"          F19";
		sql += "            ,NVL(LO.\"BadDebtDate\",0)F20";
		sql += "            ,NVL(LO.\"BadDebtAmt\",0) F21";
		sql += "            ,' ' 			          F22";// DerCode
		sql += "            ,L.\"GracePeriod\"        F23";
		sql += "            ,F.\"ApproveRate\"        F24";
		sql += "            ,F.\"AmortizedCode\"      F25";
		sql += "            ,F.\"RateCode\"           F26";
		sql += "            ,F.\"RepayFreq\"          F27";
		sql += "            ,F.\"PayIntFreq\"         F28";
		sql += "            ,C.\"IndustryCode\"       F29";
		sql += "            ,CL.\"ClTypeCode\"        F30";
		sql += "            ,C.\"RegZip3\"            F31";
		sql += "            ,M.\"ProdNo\"      		  F32";
		sql += "            ,M.\"EntCode\"            F33";
		sql += "            ,ML.\"AssetClass\"        F34";
		sql += "            ,F.\"BaseRateCode\"       F35";
		sql += "            ,CL.\"EvaAmt\"            F36";
		sql += "            ,L.\"FirstDueDate\"       F37";
		sql += "            ,L.\"TotalPeriod\"        F38";
		sql += "      FROM \"MonthlyLoanBal\" M";
		sql += "      LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\"";

		sql += "      LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "      						 AND F.\"FacmNo\" = M.\"FacmNo\"";

		sql += "      LEFT JOIN \"AcReceivable\" AR ON AR.\"CustNo\" = M.\"CustNo\"";
		sql += "      						 	   AND AR.\"FacmNo\" = M.\"FacmNo\"";
		sql += "      						 	   AND AR.\"RvNo\" = LPAD(M.\"BormNo\",3,0)";
		sql += "      						 	   AND AR.\"AcctCode\" = M.\"AcctCode\"";

		sql += "      LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" = M.\"CustNo\"";
		sql += "      						 	 AND L.\"FacmNo\" = M.\"FacmNo\"";
		sql += "      						 	 AND L.\"BormNo\" = M.\"BormNo\"";

		sql += "      LEFT JOIN \"MonthlyFacBal\" ML ON ML.\"CustNo\" = M.\"CustNo\"";
		sql += "      						 	    AND ML.\"FacmNo\" = M.\"FacmNo\"";
		sql += "      						 	    AND ML.\"YearMonth\" = M.\"YearMonth\"";

		sql += "      LEFT JOIN \"MonthlyFacBal\" ML ON ML.\"CustNo\" = M.\"CustNo\"";
		sql += "      						 	    AND ML.\"FacmNo\" = M.\"FacmNo\"";
		sql += "      						 	    AND ML.\"YearMonth\" = M.\"YearMonth\"";

		sql += "      LEFT JOIN \"LoanOverdue\" LO ON LO.\"CustNo\" = M.\"CustNo\"";
		sql += "      						 	  AND LO.\"FacmNo\" = M.\"FacmNo\"";
		sql += "      						 	  AND LO.\"BormNo\" = M.\"BormNo\"";

		sql += "      LEFT JOIN \"ClFac\" CF ON CF.\"CustNo\" = M.\"CustNo\"";
		sql += "      					    AND CF.\"FacmNo\" = M.\"FacmNo\"";
		sql += "      						AND CF.\"MainFlag\" = 'Y'";

		sql += "      LEFT JOIN \"ClMain\" CL ON CL.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "      					    AND CL.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "      						AND CL.\"ClNo\" =  CF.\"ClNo\"";

		sql += "      WHERE M.\"YearMonth\" = :yearMonth ";
		sql += "		AND M.\"LoanBalance\" > 0 ";
		sql += "      ORDER BY M.\"CustNo\"";
		sql += "              ,M.\"FacmNo\"";
		sql += "              ,M.\"BormNo\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yearMonth", iYearMonth);
		return this.convertToMap(query);
	}
}