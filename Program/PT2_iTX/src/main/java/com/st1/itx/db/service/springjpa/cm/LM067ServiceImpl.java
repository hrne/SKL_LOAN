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

public class LM067ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	public List<Map<String, String>> findAll(TitaVo titaVo, int yearMonth) throws Exception {

		// 取得會計日(同頁面上會計日)
		// 年月日
//		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		// 年
		int iYear = yearMonth / 100;
		// 月
		int iMonth = yearMonth % 100;

		String iYearMonth = String.valueOf((iYear * 100) + iMonth);

		this.info("lM067.findAll iYearMonth=" + iYearMonth);

		String sql = "WITH \"TEMP\" AS (SELECT \"CustNo\"";
		sql += "							   ,\"FacmNo\"";
		sql += "					  	   	   ,MAX(\"DrawdownDate\") AS \"DrawdownDate\"";
		sql += "						FROM \"InnReCheck\"";
		sql += "						WHERE \"YearMonth\" = :yyyymm ";
		sql += "						GROUP BY \"CustNo\"";
		sql += "								,\"FacmNo\")";
		sql += "	 SELECT  R.\"CustNo\" AS F0";
		sql += "			,R.\"FacmNo\" AS F1";
		sql += "			,\"Fn_ParseEOL\"(C.\"CustName\",0) AS F2";
		sql += "			,R.\"CustTypeItem\" AS F3";
		sql += "			,R.\"UsageItem\" AS F4";
		sql += "			,CI.\"CityItem\" AS F5";
		sql += "			,MOD(R.\"ReChkYearMonth\",100) AS F6";
		sql += "			,TEMP.\"DrawdownDate\" AS F7";
		sql += "			,R.\"LoanBal\" AS F8";
		sql += "			,R2.\"tLoanBal\" AS F9";
		sql += "			,NVL(RC.\"RenewCode\",' ') AS F10";
		sql += "			,DECODE(R.\"FollowMark\",'1','免','2','V','3','X',' ') AS F11";
		sql += "			,R.\"ReChkUnit\" AS F12";
		sql += "			,R.\"Evaluation\" AS F13";
		sql += "			,R.\"Remark\" AS F14";
		sql += "	  FROM \"InnReCheck\" R";
		sql += "	  LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = R.\"CustNo\"";
		sql += "	  LEFT JOIN \"FacMain\" FM ON FM.\"CustNo\" = R.\"CustNo\"";
		sql += "	  						AND FM.\"FacmNo\" = R.\"FacmNo\"";
		sql += "	  LEFT JOIN \"CdCode\" CC ON CC.\"DefCode\" ='UsageCode'";
		sql += "	  						 AND CC.\"Code\" = CONCAT('0',fm.\"UsageCode\")";
		sql += "	  LEFT JOIN \"ClFac\" CF ON CF.\"CustNo\" = R.\"CustNo\"";
		sql += "	  						AND CF.\"FacmNo\" = R.\"FacmNo\"";
		sql += "	  						AND CF.\"MainFlag\" ='Y'";
		sql += "	  LEFT JOIN \"ClMain\" CM ON CM.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "	  						AND CM.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "	  						AND CM.\"ClNo\" = CF.\"ClNo\"";
		sql += "	  LEFT JOIN \"ClLand\" CL ON CL.\"ClCode1\" = CM.\"ClCode1\"";
		sql += "	  						AND CL.\"ClCode2\" = CM.\"ClCode2\"";
		sql += "	  						AND CL.\"ClNo\" = CM.\"ClNo\"";
		sql += "	  LEFT JOIN \"CdCity\" CI ON CI.\"CityCode\" = CL.\"CityCode\"";
		sql += "	  LEFT JOIN (SELECT \"CustNo\"";
		sql += "					   ,\"NewFacmNo\"";
		sql += "					   ,MAX(\"RenewCode\") AS \"RenewCode\"";
		sql += "				 FROM \"AcLoanRenew\"";
		sql += "				 GROUP BY \"CustNo\",\"NewFacmNo\") RC";
		sql += "	    ON RC.\"CustNo\" = R.\"CustNo\" ";
		sql += "	   AND RC.\"NewFacmNo\" = R.\"FacmNo\"";
		sql += "	  LEFT JOIN (SELECT \"CustNo\"";
		sql += "					   ,SUM(\"LoanBal\") AS \"tLoanBal\"";
		sql += "				 FROM \"InnReCheck\"";
		sql += "	  			 WHERE \"YearMonth\" = :yyyymm";
		sql += "				   AND \"ConditionCode\" = 6";
		sql += "				   AND \"LoanBal\" > 0 ";
		sql += "				 GROUP BY \"CustNo\") R2";
		sql += "	    ON R2.\"CustNo\" = R.\"CustNo\" ";
		sql += "	  LEFT JOIN \"TEMP\" TEMP ON TEMP.\"CustNo\" = R.\"CustNo\"";
		sql += "	  						 AND TEMP.\"FacmNo\" = R.\"FacmNo\"";
		sql += "	  WHERE R.\"YearMonth\" = :yyyymm";
		sql += "		AND R.\"ConditionCode\" = 6";
		sql += "		AND R.\"LoanBal\" > 0 ";
		sql += "	  ORDER BY R.\"CustNo\", R.\"FacmNo\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yyyymm", iYearMonth);
		return this.convertToMap(query);

	}

	/**
	 * 查詢明細(LM062~LM066覆審相關報表共用)
	 * 
	 * @param titaVo
	 * @param yearMonth     西元年月
	 * @param conditionCode 條件代碼
	 * 
	 */
	public List<Map<String, String>> findList(TitaVo titaVo, int yearMonth, int conditionCode) throws Exception {

		// 取得會計日(同頁面上會計日)
		// 年月日
		// int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		// 年
		int iYear = yearMonth / 100;
		// 月
		int iMonth = yearMonth % 100;

		String iYearMonth = String.valueOf((iYear * 100) + iMonth);

		this.info("lM062.findAll iYeariMonth=" + iYearMonth);

		String sql = "	";
		sql += "	SELECT :cond AS F0";
		sql += "		  ,C.\"CustNo\" AS F1";
		sql += "		  ,C.\"FacmNo\" AS F2";
		sql += "		  ,C.\"BormNo\" AS F3";
		sql += "		  ,CM.\"CustName\" AS F4";
		sql += "		  ,R.\"DrawdownDate\" - 19110000 AS F5";
		sql += "		  ,F.\"LineAmt\" AS F6";
		sql += "		  ,F.\"UtilAmt\" AS F7";
		sql += "		  ,' ' AS F8";
		sql += "		  ,CM.\"CustName\" AS F9";
		sql += "		  ,CLD.\"LandSeq\" AS F10";
		sql += "		  ,CLD.\"LandNo1\" AS F11";
		sql += "		  ,CLD.\"LandNo2\" AS F12";
		sql += "		  ,CLD.\"Area\" AS F13";
		sql += "		  ,CM.\"CustTypeCode\" AS F14";
		sql += "		  ,CC1.\"Item\" AS F15";
		sql += "		  ,F.\"UsageCode\" AS F16";
		sql += "		  ,CC2.\"Item\" AS F17";
		sql += "		  ,CT.\"CityCode\" AS F18";
		sql += "		  ,CT.\"CityItem\" AS F19";
		sql += "		  ,MOD(R.\"ReChkYearMonth\",100) AS F20";
		sql += "		  ,R.\"Remark\" AS F21";
		sql += "	FROM (";
		sql += "		SELECT * FROM \"InnReCheck\"";
		sql += "		WHERE \"YearMonth\" = :yyyymm ";
		sql += "		  AND \"ConditionCode\" = :cond ";
		sql += "		  AND \"LoanBal\" > 0 ";
		sql += "	) R";
		sql += "	LEFT JOIN (";
		sql += "		SELECT * FROM \"MonthlyLoanBal\" ";
		sql += "		WHERE \"YearMonth\" = :yyyymm ";
		sql += "		  AND \"LoanBalance\" > 0 ";
		sql += "	) C ON C.\"CustNo\" = R.\"CustNo\" ";
		sql += "	   AND C.\"FacmNo\" = R.\"FacmNo\" ";
		sql += "	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = R.\"CustNo\"";
		sql += "						   AND F.\"FacmNo\" = R.\"FacmNo\"";
		sql += "	LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = R.\"CustNo\"";
		sql += "	LEFT JOIN \"ClLand\" CLD ON CLD.\"ClCode1\" = C.\"ClCode1\"";
		sql += "							AND CLD.\"ClCode2\" = C.\"ClCode2\"";
		sql += "							AND CLD.\"ClNo\" = C.\"ClNo\"";
		sql += "	LEFT JOIN \"CdCity\" CT ON CT.\"CityCode\" = C.\"CityCode\"";
		sql += "	LEFT JOIN \"CdCode\" CC1 ON CC1.\"DefCode\" = 'CustTypeCode'";
		sql += "						   	 AND CC1.\"Code\" = CM.\"CustTypeCode\"";
		sql += "	LEFT JOIN \"CdCode\" CC2 ON CC2.\"DefCode\" = 'UsageCode'";
		sql += "						   	 AND CC2.\"Code\" = F.\"UsageCode\"";
		sql += "	WHERE R.\"CustNo\" IS NOT NULL";
		sql += "	ORDER BY C.\"CustNo\" ASC";
		sql += "		    ,C.\"FacmNo\" ASC";
		sql += "			,C.\"BormNo\" ASC";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yyyymm", iYearMonth);
		query.setParameter("cond", conditionCode);
		return this.convertToMap(query);
	}

}