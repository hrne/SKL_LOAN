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
public class LM062ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo, int yearMonth) throws Exception {

		// 取得會計日(同頁面上會計日)
		// 年月日
		// int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		// 年
		int iYear = yearMonth / 100;
		// 月
		int iMonth = yearMonth % 100;

		String iYearMonth = String.valueOf((iYear * 100) + iMonth);

		this.info("lM062.findAll iYeariMonth=" + iYearMonth);

		String sql = "SELECT R.\"CustNo\" AS F0";
		sql += "			,R.\"FacmNo\" AS F1";
		sql += "			,\"Fn_ParseEOL\"(C.\"CustName\",0) AS F2";
		sql += "			,R.\"ReChkYearMonth\" AS F3";
		sql += "			,R.\"DrawdownDate\" AS F4";
		sql += "			,R.\"LoanBal\" AS F5";
		sql += "			,R2.\"tLoanBal\" AS F6";
		sql += "			,NVL(RC.\"RenewCode\",' ') AS F7";
		sql += "			,DECODE(R.\"ReCheckCode\",'2','*',' ') AS F8";
		sql += "			,R.\"Evaluation\" AS F9";
		sql += "	  FROM \"InnReCheck\" R";
		sql += "	  LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = R.\"CustNo\"";
		sql += "	  LEFT JOIN (SELECT \"CustNo\"";
		sql += "					   ,\"NewFacmNo\"";
		sql += "					   ,MAX(\"RenewCode\") AS \"RenewCode\"";
		sql += "				 FROM \"AcLoanRenew\"";
		sql += "				 GROUP BY \"CustNo\",\"NewFacmNo\") RC";
		sql += "	  ON RC.\"CustNo\" = R.\"CustNo\" ";
		sql += "	   AND RC.\"NewFacmNo\" = R.\"FacmNo\"";
		sql += "	  LEFT JOIN (SELECT \"CustNo\"";
		sql += "					   ,SUM(\"LoanBal\") AS \"tLoanBal\"";
		sql += "				 FROM \"InnReCheck\"";
		sql += "	  			 WHERE \"YearMonth\" = :yyyymm";
		sql += "				   AND \"ConditionCode\" = 1";
		sql += "				   AND \"LoanBal\" > 0 ";
		sql += "				 GROUP BY \"CustNo\") R2";
		sql += "	    ON R2.\"CustNo\" = R.\"CustNo\" ";
		sql += "	  WHERE R.\"YearMonth\" = :yyyymm";
		sql += "		AND R.\"ConditionCode\" = 1";
		sql += "		AND R.\"LoanBal\" > 0 ";
		sql += "	  ORDER BY R.\"CustNo\",R.\"FacmNo\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yyyymm", iYearMonth);
		return this.convertToMap(query);
	}

	/**
	 * 查詢明細(LM062~LM066覆審相關報表共用)
	 * @param titaVo
	 * @param yearMonth 西元年月
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
		sql += "		  ,CM.\"BranchNo\" AS F1";
		sql += "		  ,C.\"CustNo\" AS F2";
		sql += "		  ,C.\"FacmNo\" AS F3";
		sql += "		  ,C.\"BormNo\" AS F4";
		sql += "		  ,CM.\"CustName\" AS F5";
		sql += "		  ,R.\"DrawdownDate\" - 19110000 AS F6";
		sql += "		  ,R.\"LoanBal\" AS F7";
		sql += "		  ,F.\"MaturityDate\" - 19110000 AS F8";
		sql += "		  ,C.\"ClCode1\" AS F9";
		sql += "		  ,C.\"ClCode2\" AS F10";
		sql += "		  ,C.\"ClNo\" AS F11";
		sql += "		  ,NVL(CA.\"CityShort\",' ') AS F12";
		sql += "		  ,NVL(CA.\"AreaShort\",' ') AS F13";
		sql += "		  ,NVL(SUBSTR(CLS.\"IrItem\",1,INSTR(CLS.\"IrItem\",'段',1,1)-1),' ') AS F14";
		sql += "		  ,NVL(SUBSTR(CLS.\"IrItem\",INSTR(CLS.\"IrItem\",'段',1,1)+1,INSTR(CLS.\"IrItem\",'段',1,2)-1),' ') AS F15";
		sql += "		  ,NVL(CLB.\"BdLocation\",' ') AS F16";
		sql += "		  ,R.\"CityItem\" AS F17";
		sql += "		  ,R.\"ReChkUnit\" AS F18";
		sql += "		  ,' ' AS F19";
		sql += "		  ,' ' AS F20";
		sql += "		  ,MOD(R.\"ReChkYearMonth\",100) AS F21";
		sql += "		  ,R.\"UsageItem\" AS F22";
		sql += "		  ,R.\"Remark\" AS F23";
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
		sql += "							AND CLD.\"LandSeq\" = 1 ";
		sql += "	LEFT JOIN \"CdArea\" CA ON CA.\"CityCode\" = CLD.\"CityCode\"";
		sql += "						   AND CA.\"AreaCode\" = CLD.\"AreaCode\"";
		sql += "	LEFT JOIN \"CdLandSection\" CLS ON CLS.\"CityCode\" = CLD.\"CityCode\"";
		sql += "						   		   AND CLS.\"AreaCode\" = CLD.\"AreaCode\"";
		sql += "						   		   AND CLS.\"IrCode\" = CLD.\"IrCode\"";
		sql += "	LEFT JOIN \"ClBuilding\" CLB ON CLB.\"ClCode1\" = C.\"ClCode1\"";
		sql += "								AND CLB.\"ClCode2\" = C.\"ClCode2\"";
		sql += "								AND CLB.\"ClNo\" = C.\"ClNo\"";
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