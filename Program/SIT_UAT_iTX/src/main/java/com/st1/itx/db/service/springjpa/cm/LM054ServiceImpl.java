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
public class LM054ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo, String acctCode) throws Exception {

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
		
		this.info("lM052.findAll nowDate=" + nowDate + ",ymd=" + thisMonthEndDate);
		
		// 這個月底
		calMonthDate.set(iYear, iMonth, 0);
		thisMonthEndDate = Integer.valueOf(dateFormat.format(calMonthDate.getTime()));
		
		// 上個月底
		calMonthDate.set(iYear, iMonth - 1, 0);

		int lastMonthEndDate = Integer.valueOf(dateFormat.format(calMonthDate.getTime()));

		this.info("lM052.findAll yymm=" + (iYear * 100 + iMonth) + ",lyymm=" + lastMonthEndDate / 100 + ",ymd="
				+ thisMonthEndDate + ",ymd=" + lastMonthEndDate);

		// 當年月
		String iYearMonth = iYear * 100 + iMonth + "";

		// 上年月
		String lYearMonth = lastMonthEndDate / 100 + "";

		// 月底
		String eYmd = thisMonthEndDate + "";
		// 月初
		String sYmd = iYear * 100 + iMonth + "01";

		this.info("lM054.findAll iYearMonth=" + iYearMonth + ",iYearMonth=" + lYearMonth + "sYmd=" + sYmd + ",eYmd="
				+ eYmd);

		String sql = "";
		if (acctCode.equals("N")) {
			/*
			 * F0 放款代號(戶號) F1 放款種類 F2 放款對象名稱 (戶名) F3 放款對象關係人代碼 F4 利害關係人代碼 F5
			 * 是否為專案運用公共及社會福利事業投資 F6 是否為聯合貸款 F7 持有資產幣別 F8 放款日期 F9 到期日期 F10 放款年利率 F11 放款餘額
			 * F12 應收利息 F13 擔保品設定順位 F14擔保品估計總值 F15 擔保品核貸金額 F16 轉催收日期 F17 催收狀態 (電催) F18
			 * 催收狀態執行日期 (電催) F19 備抵損失總額 (公式) F20 評估分類 F21 IFRS9評估階段 (公式) F22 備註 (協議戶) F23 備註
			 * (資金專案運用) F24 逾期天數
			 */

			sql += "	SELECT LPAD(MLB.\"CustNo\",7,0) AS F0";
			sql += "		  ,(CASE";
			sql += "			  WHEN MLB.\"ClCode1\" IN (1,2) THEN 'C'";
			sql += "			  WHEN MLB.\"ClCode1\" IN (1,2) THEN 'D'";
			sql += "			  WHEN MLB.\"ClCode1\" IN (1,2) THEN 'A'";
			sql += "			  WHEN MLB.\"ClCode1\" IN (1,2) THEN 'B'";
			sql += "			ELSE 'Z' END ) AS F1";
			sql += "		  ,C.\"CustName\" AS F2";
			sql += "		  ,(CASE";
			sql += "			  WHEN R.\"ReltCode\" IS NULL THEN 'A'";
			sql += "			  WHEN R.\"ReltCode\" ='08' THEN 'C'";
			sql += "			ELSE 'B' END ) AS F3";
			sql += "		  ,(CASE";
			sql += "			  WHEN R.\"ReltCode\" IS NULL AND MLB.\"EntCode\" = 0 THEN 'D'";
			sql += "			  WHEN R.\"ReltCode\" IS NULL AND MLB.\"EntCode\" <> 0 THEN 'C'";
			sql += "			  WHEN R.\"ReltCode\" IS NOT NULL AND MLB.\"EntCode\" = 0 THEN 'B'";
			sql += "			  WHEN R.\"ReltCode\" IS NOT NULL AND MLB.\"EntCode\" <> 0 THEN 'A'";
			sql += "			ELSE ' ' END ) AS F4";
			sql += "		  ,(CASE";
			sql += "			  WHEN MFB.\"ProdNo\" LIKE 'I%' OR MFB.\"FacAcctCode\" = 340 THEN 'Y'";
			sql += "			ELSE 'N' END ) AS F5";
			sql += "		  ,DECODE(L.\"SyndNo\",0,'N','Y') AS F6";
			sql += "		  ,'TWD' AS F7";
			sql += "		  ,L.\"DrawdownDate\" AS F8";
			sql += "		  ,L.\"MaturityDate\" AS F9";
			sql += "		  ,L.\"StoreRate\" / 100 AS F10";
			sql += "		  ,MLB.\"LoanBalance\" AS F11";
			sql += "		  ,MLB.\"IntAmtAcc\" AS F12";
			sql += "		  ,'1' AS F13";
			sql += "		  ,NVL(CM.\"EvaAmt\",0) AS F14";
			sql += "		  ,NVL(F2.\"LineAmt\",0) AS F15";
			sql += "		  ,MFB.\"OvduDate\" AS F16";
			sql += "		  ,CT.\"ResultCode\" AS F17";
			sql += "		  ,CT.\"TelDate\" AS F18";
			sql += "		  ,' ' AS F19";
			sql += "		  ,NVL(SUBSTR(MFB.\"AssetClass\",0,1),1) AS F20";
			sql += "		  ,' ' AS F21";
			sql += "		  ,(CASE";
			sql += "			  WHEN MFB.\"OvduTerm\" >= 1 AND MFB.\"OvduTerm\" <= 5 AND MFB.\"Status\" = 0 AND MFB.\"ProdNo\" IN ('60','61','62') THEN '協議戶'";
			sql += "			  WHEN MFB.\"OvduTerm\" = 0 AND MFB.\"OvduDays\" = 0 AND MFB.\"Status\" = 0 AND MFB.\"ProdNo\" IN ('60','61','62') THEN '協議戶'";
			sql += "			  WHEN MFB.\"OvduDays\" < 30 AND MFB.\"OvduDays\" > 0 AND MFB.\"Status\" = 0 AND MFB.\"ProdNo\" IN ('60','61','62') THEN '協議戶'";
			sql += "			  WHEN MFB.\"Status\" IN (2,6,7) AND MFB.\"ProdNo\" IN ('60','61','62') THEN '協議戶'";
			sql += "			ELSE ' ' END ) AS F22";
			sql += "		  ,(CASE";
			sql += "			  WHEN MFB.\"ProdNo\" LIKE 'I%' OR MFB.\"FacAcctCode\" = 340 THEN '資金專案運用'";
			sql += "			ELSE ' ' END ) AS F23";
			sql += "		  ,MFB.\"OvduDays\" AS F24";
			sql += "	FROM \"MonthlyLoanBal\" MLB";
			sql += "	LEFT JOIN \"MonthlyFacBal\" MFB ON MFB.\"CustNo\" = MLB.\"CustNo\"";
			sql += "								   AND MFB.\"FacmNo\" = MLB.\"FacmNo\"";
			sql += "								   AND MFB.\"YearMonth\" = :yymm";
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
			sql += "	LEFT JOIN \"ClMain\" CM ON CM.\"ClCode1\" = MLB.\"ClCode1\"";
			sql += "						   AND CM.\"ClCode2\" = MLB.\"ClCode2\"";
			sql += "						   AND CM.\"ClNo\" = MLB.\"ClNo\"";
			sql += "	LEFT JOIN \"FacMain\" F2 ON F2.\"CustNo\" = MLB.\"CustNo\"";
			sql += "						    AND F2.\"FacmNo\" = MLB.\"FacmNo\"";
			sql += "						    AND F2.\"LastBormNo\" = MLB.\"BormNo\"";
			sql += "	LEFT JOIN \"ReltMain\" R ON R.\"ReltUKey\" = C.\"CustId\"";
			sql += "	LEFT JOIN ( SELECT \"CustNo\"";
			sql += "					  ,\"FacmNo\"";
			sql += "					  ,\"TelDate\"";
			sql += "					  ,\"ResultCode\"";
			sql += "					  ,ROW_NUMBER () OVER (PARTITION BY \"CustNo\"";
			sql += "					  								   ,\"FacmNo\"";
			sql += "					  					   ORDER BY \"AcDate\" DESC";
			sql += "					  							   ,\"TitaTxtNo\" DESC ) AS \"SEQ\"";
			sql += "				FROM \"CollTel\" ) CT";
			sql += "	ON CT.\"CustNo\" = MFB.\"CustNo\" AND CT.\"FacmNo\" = MFB.\"FacmNo\"";
			sql += "									  AND CT.\"SEQ\" = 1 ";
			sql += "	WHERE MLB.\"YearMonth\" = :yymm";
			sql += "	  AND MLB.\"LoanBalance\" > 0 ";
			sql += "      AND ((MFB.\"OvduTerm\" >= 3 AND MFB.\"OvduTerm\" <= 5 AND MFB.\"Status\" IN (0,2,6,7) AND MFB.\"ProdNo\" IN ('60','61','62')";// --逾期12345
			sql += "	   OR ( MFB.\"OvduTerm\" >= 1 AND MFB.\"OvduTerm\" <= 5 AND MFB.\"Status\" = 0 AND MFB.\"ProdNo\" IN ('60','61','62'))"; // --協12345
			sql += "	   OR ( MFB.\"OvduTerm\" = 0 AND MFB.\"OvduDays\" = 0 AND MFB.\"Status\" = 0 AND MFB.\"ProdNo\" IN ('60','61','62'))"; // --協
			sql += "	   OR ( MFB.\"OvduDays\" < 30 AND MFB.\"OvduDays\" > 0 AND MFB.\"Status\" = 0 AND MFB.\"ProdNo\" IN ('60','61','62'))"; // --協*
			sql += "	   OR ( MFB.\"Status\" IN (2,6,7) AND MFB.\"ProdNo\" IN ('60','61','62'))"; // --催協
			sql += "	   OR ( MFB.\"Status\" IN (2,6,7) AND MFB.\"ProdNo\" NOT IN ('60','61','62'))"; // --催
			sql += "	   OR   MLB2.\"LineAmt\" > 100000000 ))"; // --非以上條件
			sql += "	ORDER BY MFB.\"CustNo\"";
			sql += "			,MFB.\"FacmNo\"";
		} else {

			sql += "	SELECT :eymd  AS F0";
			sql += "		  ,'C' AS F1";
			sql += "		  ,'放款催收費用與哲溢價' AS F2";
			sql += "		  ,'A' AS F3";
			sql += "		  ,'D' AS F4";
			sql += "		  ,'N' AS F5";
			sql += "		  ,'N' AS F6";
			sql += "		  ,'TWD' AS F7";
			sql += "		  ,:symd AS F8";
			sql += "		  ,:eymd AS F9";
			sql += "		  ,0 AS F10"; 
			sql += "		  ,NVL(A.\"Amt\",0) AS F11";
			sql += "		  ,0 AS F12";
			sql += "		  ,1 AS F13";
			sql += "		  ,NVL(A.\"Amt\",0) AS F14";
			sql += "		  ,NVL(A.\"Amt\",0) AS F15";
			sql += "		  ,:eymd AS F16";
			sql += "		  ,'A' AS F17";
			sql += "		  ,:eymd AS F18";
			sql += "		  ,' ' AS F19";
			sql += "		  ,2 AS F20";
			sql += "		  ,2 AS F21";
			sql += "		  ,' ' AS F22";
			sql += "		  ,' ' AS F23";
			sql += "		  ,-1 AS F24";
			sql += "	FROM ( SELECT SUM(DECODE(\"MonthEndYm\",:tyymm,\"TdBal\"))";
			sql += "				  -SUM(DECODE(\"MonthEndYm\",:lyymm,\"TdBal\")) AS \"Amt\"";
			sql += "		   FROM \"AcMain\"";
			sql += "		   WHERE \"MonthEndYm\" IN (:tyymm,:lyymm)";
			sql += "		     AND \"AcNoCode\" IN ('10601301000','10601302000','10601304000')) A";
		}

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		if (acctCode.equals("N")) {

			query.setParameter("yymm", iYearMonth);
		} else {
			query.setParameter("symd", sYmd);
			query.setParameter("eymd", eYmd);
			query.setParameter("lyymm", lYearMonth);
			query.setParameter("tyymm", iYearMonth);
		}
		return this.convertToMap(query);
	}

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

	// @SuppressWarnings({ "unchecked" })
	// public List<Map<String, String>> ias34Ap(TitaVo titaVo) throws Exception {

	// String iENTDY = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) +
	// 19110000);
	// String iYearMonth = iENTDY.substring(0, 6);

	// this.info("lM054.ias34Ap ENTDY=" + iENTDY + ",iYearMonth=" + iYearMonth);

	// String sql = "SELECT I.\"CustNo\" F0";
	// sql += " ,I.\"CustId\" F1";
	// sql += " ,I.\"FacmNo\" F2";
	// sql += " ,I.\"ApplNo\" F3";
	// sql += " ,I.\"BormNo\" F4";
	// sql += " ,I.\"AcCode\" F5";
	// sql += " ,I.\"Status\" F6";
	// sql += " ,I.\"FirstDrawdownDate\" F7";
	// sql += " ,I.\"DrawdownDate\" F8";
	// sql += " ,I.\"FacLineDate\" F9";
	// sql += " ,I.\"MaturityDate\" F10";
	// sql += " ,I.\"LineAmt\" F11";
	// sql += " ,I.\"DrawdownAmt\" F12";
	// sql += " ,I.\"AcctFee\" F13";
	// sql += " ,I.\"LoanBal\" F14";
	// sql += " ,I.\"IntAmt\" F15";
	// sql += " ,I.\"Fee\" F16";
	// sql += " ,I.\"Rate\" F17";
	// sql += " ,I.\"OvduDays\" F18";
	// sql += " ,I.\"OvduDate\" F19";
	// sql += " ,I.\"BadDebtDate\" F20";
	// sql += " ,I.\"BadDebtAmt\" F21";
	// sql += " ,I.\"DerCode\" F22";
	// sql += " ,I.\"GracePeriod\" F23";
	// sql += " ,I.\"ApproveRate\" F24";
	// sql += " ,I.\"AmortizedCode\" F25";
	// sql += " ,I.\"RateCode\" F26";
	// sql += " ,I.\"RepayFreq\" F27";
	// sql += " ,I.\"PayIntFreq\" F28";
	// sql += " ,I.\"IndustryCode\" F29";
	// sql += " ,I.\"ClTypeJCIC\" F30";
	// sql += " ,I.\"Zip3\" F31";
	// sql += " ,I.\"RateCode\" F32";
	// sql += " ,I.\"CustKind\" F33";
	// sql += " ,I.\"AssetKind\" F34";
	// sql += " ,I.\"ProdNo\" F35";
	// sql += " ,I.\"EvaAmt\" F36";
	// sql += " ,I.\"FirstDueDate\" F37";
	// sql += " ,I.\"TotalPeriod\" F38";
	// sql += " ,I.\"AgreeBefFacmNo\" F39";
	// sql += " ,I.\"AgreeBefBormNo\" F40";
	// sql += " ,'F41' F41";// 欄位聯貸項目不知道輸出條件
	// sql += " ,C.\"CustName\" F42";
	// sql += " ,CASE";
	// sql += " WHEN P.\"GovOfferFlag\" = 'Y' THEN '*'";
	// sql += " ELSE ' ' END F43";
	// sql += " FROM \"Ias34Ap\" I";
	// sql += " LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = I.\"CustNo\"";
	// sql += " LEFT JOIN \"FacProd\" P ON P.\"ProdNo\" = I.\"ProdNo\"";
	// sql += " WHERE I.\"DataYM\" = :yearMonth ";
	// sql += " ORDER BY I.\"CustNo\"";
	// sql += " ,I.\"FacmNo\"";
	// sql += " ,I.\"BormNo\"";
	// this.info("sql=" + sql);

	// Query query;
	// EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
	// query = em.createNativeQuery(sql);
	// query.setParameter("yearMonth", iYearMonth);
	// return this.convertToMap(query);
	// }

//	String sql = "SELECT I.\"CustNo\"      F0";
//	sql += "            ,I.\"CustName\"    F1";
//	sql += "            ,I.\"CustId\"      F2";
//	sql += "            ,I.\"LoanBal\"     F3";
//	sql += "            ,CASE";
//	sql += "               WHEN I.\"Type\" = 1 THEN '聯貸'";
//	sql += "               WHEN I.\"Type\" = 2 THEN '**'";
//	sql += "               WHEN I.\"Type\" = 3 THEN '*'";
//	sql += "             ELSE ' ' END      F4";
//	sql += "            ,CASE";
//	sql += "               WHEN I.\"RelsCode\" = '99' THEN N'#N/A'";
//	sql += "             ELSE I.\"Item\" END F5";
//	sql += "      FROM (SELECT I.\"CustNo\"";
//	sql += "                  ,C.\"CustName\"";
//	sql += "                  ,C.\"CustId\"";
//	sql += "                  ,I.\"LoanBal\"";
//	sql += "                  ,CASE WHEN I.\"SyndNo\"  > 0         THEN 1";
//	sql += "                        WHEN I.\"LoanBal\" > 100000000 THEN 2";
//	sql += "                        WHEN I.\"RelsCode\" <> '99'    THEN 3";
//	sql += "                   ELSE 9 END \"Type\", D.\"Item\", I.\"RelsCode\"";
//	sql += "            FROM (SELECT I.\"CustNo\"";
//	sql += "                        ,MAX(NVL(M.\"RelsCode\", '99')) \"RelsCode\"";
//	sql += "                        ,SUM(I.\"LoanBal\") \"LoanBal\"";
//	sql += "                        ,MAX(L.\"SyndNo\") \"SyndNo\"";
//	sql += "                  FROM  \"Ias34Ap\" I";
//	sql += "                  LEFT JOIN \"MonthlyLoanBal\" M ON M.\"YearMonth\" = :yearMonth";
//	sql += "                                                AND M.\"CustNo\"    = I.\"CustNo\"";
//	sql += "                                                AND M.\"FacmNo\"    = I.\"FacmNo\"";
//	sql += "                                                AND M.\"BormNo\"    = I.\"BormNo\"";
//	sql += "                  LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" = I.\"CustNo\"";
//	sql += "                                             AND L.\"FacmNo\" = I.\"FacmNo\"";
//	sql += "                                             AND L.\"BormNo\" = I.\"BormNo\"";
//	sql += "                  WHERE  I.\"DataYM\" = :yearMonth";
//	sql += "                  GROUP BY I.\"CustNo\") I";
//	sql += "            LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = I.\"CustNo\"";
//	sql += "            LEFT JOIN \"CdCode\" D ON D.\"DefCode\" = 'RelsCode'";
//	sql += "                                  AND D.\"DefType\" = '2'";
//	sql += "                                  AND D.\"Code\"    = I.\"RelsCode\") I";
//	sql += "      ORDER BY I.\"Type\", I.\"LoanBal\" DESC";
//	this.info("sql=" + sql);
}