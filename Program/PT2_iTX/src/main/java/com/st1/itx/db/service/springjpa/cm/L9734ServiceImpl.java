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
public class L9734ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	/**
	 * 執行報表輸出(覆審報表)
	 * 
	 * @param titaVo
	 * @param acDate
	 * @param conditionCode
	 * @return
	 * @throws Exception
	 */

	public List<Map<String, String>> findAll(TitaVo titaVo, int acDate, int conditionCode) throws Exception {
		// 年
		int iYear = acDate / 100;
		// 月
		int iMonth = acDate / 100 % 100;

		String iYearMonth = String.valueOf((iYear * 100) + iMonth);

		int reChkYearMonth = Integer.valueOf(titaVo.getParam("ReChkYearMonth")) ;
		if(reChkYearMonth > 0) {
			reChkYearMonth = reChkYearMonth+ 191100;
		}
		
		String sql = " ";
		this.info("iYeariMonth=" + iYearMonth);
		this.info("conditionCode=" + conditionCode);
		this.info("reChkYearMonth=" + reChkYearMonth);
		String iYearMonthL5 = "";

		// 此表根據樣張明細備註有取五個月前的撥款日
		if (iMonth - 5 < 0) {
			iYearMonthL5 = String.valueOf(((iYear - 1) * 100) + (12 + (iMonth - 5)));
		} else {
			iYearMonthL5 = String.valueOf((iYear * 100) + (iMonth - 5));
		}

		iYearMonth = String.valueOf((iYear * 100) + iMonth);

		switch (conditionCode) {

		case 1:// 覆審案件資料表-個金3000萬以上
		case 2:// 覆審案件資料表-企金3000萬以上
			sql += "SELECT R.\"CustNo\"";
			sql += "			,R.\"FacmNo\"";
			sql += "			,\"Fn_ParseEOL\"(C.\"CustName\",0) AS \"CustName\"";
			sql += "			,R.\"ReChkYearMonth\"";
			sql += "			,R.\"DrawdownDate\"";
			sql += "			,R3.\"PrevPayIntDate\"";
			sql += "			,R.\"LoanBal\"";
			sql += "			,R2.\"tLoanBal\"";
			sql += "			,NVL(RC.\"RenewCode\",' ') AS \"RenewCode\"";
			sql += "			,DECODE(R.\"ReCheckCode\",'2','*',' ') AS \"ReCheckCode\"";
			sql += "			,R.\"Evaluation\"";
			sql += "			,CE2.\"Fullname\" AS \"ReChkName\"";
			sql += "			,CE.\"Fullname\" AS \"TlrName\"";
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
			sql += "				   AND \"ConditionCode\" = :cond ";
			sql += "				   AND \"LoanBal\" > 0 ";
			sql += "				 GROUP BY \"CustNo\") R2";
			sql += "	    ON R2.\"CustNo\" = R.\"CustNo\" ";
			sql += "	  LEFT JOIN (SELECT \"CustNo\"";
			sql += "					   ,\"FacmNo\" ";
			sql += "					   ,MIN(\"PrevPayIntDate\") AS \"PrevPayIntDate\"";
			sql += "				 FROM \"LoanBorMain\"";
			sql += "	  			 WHERE \"LoanBal\" > 0 ";
			sql += "				 GROUP BY \"CustNo\",\"FacmNo\") R3";
			sql += "	    ON R3.\"CustNo\" = R.\"CustNo\" ";
			sql += "	   AND R3.\"FacmNo\" = R.\"FacmNo\" ";
			sql += "	  LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = R.\"CustNo\" ";
			sql += "	   						 AND F.\"FacmNo\" = R.\"FacmNo\" ";
			sql += "	  LEFT JOIN \"CdEmp\" CE ON CE.\"EmployeeNo\" = F.\"BusinessOfficer\" ";
			sql += "	  LEFT JOIN \"CdEmp\" CE2 ON CE2.\"EmployeeNo\" = R.\"ReChkEmpNo\" ";
			sql += "	  WHERE R.\"YearMonth\" = :yyyymm";
			sql += "		AND R.\"ConditionCode\" = :cond ";
			sql += "		AND R.\"LoanBal\" > 0 ";
			if (reChkYearMonth != 0) {
				sql += "		AND R.\"ReChkYearMonth\" = :reChkYearMonth ";
			}
			sql += "	  ORDER BY R.\"CustNo\",R.\"FacmNo\"";
			break;

		case 3:// 覆審案件資料表-個金2000萬以上小於3000萬
			sql += "SELECT R.\"CustNo\"";
			sql += "			,R.\"FacmNo\"";
			sql += "			,\"Fn_ParseEOL\"(C.\"CustName\",0) AS \"CustName\"";
			sql += "			,R.\"ReChkUnit\"";
			sql += "			,E.\"CenterShortName\"";
			sql += "			,R.\"ReChkYearMonth\"";
			sql += "			,R.\"DrawdownDate\"";
			sql += "			,R3.\"PrevPayIntDate\"";
			sql += "			,R.\"LoanBal\"";
			sql += "			,R2.\"tLoanBal\"";
			sql += "			,NVL(RC.\"RenewCode\",' ') AS \"RenewCode\"";
			sql += "			,DECODE(R.\"ReCheckCode\",'2','*',' ') AS \"ReCheckCode\"";
			sql += "			,R.\"Evaluation\"";
			sql += "			,CE2.\"Fullname\" AS \"ReChkName\"";
			sql += "			,CE.\"Fullname\" AS \"TlrName\"";
			sql += "	  FROM \"InnReCheck\" R";
			sql += "	  LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = R.\"CustNo\"";
			sql += "	  LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = R.\"CustNo\"";
			sql += "	  						 AND F.\"FacmNo\" = R.\"FacmNo\"";
			sql += "	  LEFT JOIN \"CdEmp\" E ON E.\"EmployeeNo\" = F.\"Supervisor\"";
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
			sql += "				   AND \"ConditionCode\" = 3";
			sql += "				   AND \"LoanBal\" > 0 ";
			sql += "				 GROUP BY \"CustNo\") R2";
			sql += "	    ON R2.\"CustNo\" = R.\"CustNo\" ";
			sql += "	  LEFT JOIN (SELECT \"CustNo\"";
			sql += "					   ,\"FacmNo\" ";
			sql += "					   ,MIN(\"PrevPayIntDate\") AS \"PrevPayIntDate\"";
			sql += "				 FROM \"LoanBorMain\"";
			sql += "	  			 WHERE \"LoanBal\" > 0 ";
			sql += "				 GROUP BY \"CustNo\",\"FacmNo\") R3";
			sql += "	    ON R3.\"CustNo\" = R.\"CustNo\" ";
			sql += "	   AND R3.\"FacmNo\" = R.\"FacmNo\" ";
			sql += "	  LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = R.\"CustNo\" ";
			sql += "	   						 AND F.\"FacmNo\" = R.\"FacmNo\" ";
			sql += "	  LEFT JOIN \"CdEmp\" CE ON CE.\"EmployeeNo\" = F.\"BusinessOfficer\" ";
			sql += "	  LEFT JOIN \"CdEmp\" CE2 ON CE2.\"EmployeeNo\" = R.\"ReChkEmpNo\" ";
			sql += "	  WHERE R.\"YearMonth\" = :yyyymm";
			sql += "		AND R.\"ConditionCode\" = :cond ";
			sql += "		AND R.\"LoanBal\" > 0 ";
			if (reChkYearMonth != 0) {
				sql += "		AND R.\"ReChkYearMonth\" = :reChkYearMonth ";
			}
			sql += "	  ORDER BY R.\"CustNo\", R.\"FacmNo\"";
			break;

		case 4:// 覆審案件資料表-個金100萬以上小於2000萬

			sql += "SELECT DISTINCT(S1.\"CustNo\") AS \"CustNo\"";
			sql += "			,S1.\"FacmNo\"";
			sql += "			,S1.\"CustName\" ";
			sql += "			,S1.\"ReChkUnit\" ";
			sql += "			,S1.\"CenterShortName\" ";
			sql += "			,S1.\"ReChkYearMonth\" ";
			sql += "			,S1.\"DrawdownDate\" ";
			sql += "			,R3.\"PrevPayIntDate\"";
			sql += "			,S1.\"LoanBal\" ";
			sql += "			,R2.\"tLoanBal\" ";
			sql += "			,NVL(RC.\"RenewCode\",' ') AS \"RenewCode\"";
			sql += "			,DECODE(S1.\"ReCheckCode\",'2','*',' ') AS \"ReCheckCode\"";
			sql += "			,S1.\"Evaluation\" ";
			sql += "			,CE2.\"Fullname\" AS \"ReChkName\"";
			sql += "			,CE.\"Fullname\" AS \"TlrName\"";
			sql += "	  FROM(SELECT DISTINCT(R.\"CustNo\")";
			sql += "	  	   FROM \"InnReCheck\" R";
			sql += "	  	   LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = R.\"CustNo\"";
			sql += "	  	   LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = R.\"CustNo\"";
			sql += "							 	  AND F.\"FacmNo\" = R.\"FacmNo\"";
			sql += "	  	   LEFT JOIN \"CdEmp\" E ON E.\"EmployeeNo\" = F.\"Supervisor\"";
			sql += "	  	   LEFT JOIN \"LoanBorMain\" LBM ON LBM.\"CustNo\" = R.\"CustNo\"";
			sql += "							 	  	    AND LBM.\"FacmNo\" = R.\"FacmNo\"";
			sql += "	  	   WHERE R.\"YearMonth\" = :yyyymm";
			sql += "			 AND R.\"ConditionCode\" = 4";
			sql += "			 AND R.\"LoanBal\" > 0 ";
			sql += "			 AND TRUNC(LBM.\"DrawdownDate\"/100) = :l5yymm ) S0";
			sql += "	  LEFT JOIN(SELECT R.\"CustNo\" \"CustNo\"";
			sql += "					  ,R.\"FacmNo\" \"FacmNo\"";
			sql += "				  	  ,\"Fn_ParseEOL\"(C.\"CustName\",0) \"CustName\"";
			sql += "				  	  ,E.\"CenterShortName\" \"CenterShortName\"";
			sql += "					  ,R.\"ReChkUnit\" \"ReChkUnit\"";
			sql += "					  ,R.\"ReChkYearMonth\" \"ReChkYearMonth\"";
			sql += "					  ,R.\"DrawdownDate\" \"DrawdownDate\"";
			sql += "					  ,R.\"LoanBal\" \"LoanBal\"";
			sql += "					  ,DECODE(R.\"ReCheckCode\",'2','*',' ') \"ReCheckCode\"";
			sql += "					  ,R.\"Evaluation\" \"Evaluation\"";
			sql += "					  ,R.\"ReChkEmpNo\" \"ReChkEmpNo\"";
			sql += "	  		    FROM \"InnReCheck\" R";
			sql += "	  	  	  	LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = R.\"CustNo\"";
			sql += "	  		  	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = R.\"CustNo\"";
			sql += "							 	       AND F.\"FacmNo\" = R.\"FacmNo\"";
			sql += "	  		  	LEFT JOIN \"CdEmp\" E ON E.\"EmployeeNo\" = F.\"Supervisor\"";
			sql += "	  	   		LEFT JOIN \"LoanBorMain\" LBM ON LBM.\"CustNo\" = R.\"CustNo\"";
			sql += "							 	  	    	 AND LBM.\"FacmNo\" = R.\"FacmNo\"";
			sql += "	  		  	WHERE R.\"YearMonth\" = :yyyymm";
			sql += "				  AND R.\"ConditionCode\" = 4";
			sql += "				  AND R.\"LoanBal\" > 0 ) S1";
			sql += "	  ON S0.\"CustNo\"=S1.\"CustNo\"";
			sql += "	  LEFT JOIN (SELECT \"CustNo\"";
			sql += "					   ,\"NewFacmNo\"";
			sql += "					   ,MAX(\"RenewCode\") AS \"RenewCode\"";
			sql += "				 FROM \"AcLoanRenew\"";
			sql += "				 GROUP BY \"CustNo\",\"NewFacmNo\") RC";
			sql += "	    ON RC.\"CustNo\" = S1.\"CustNo\" ";
			sql += "	   AND RC.\"NewFacmNo\" = S1.\"FacmNo\"";
			sql += "	  LEFT JOIN (SELECT \"CustNo\"";
			sql += "					   ,SUM(\"LoanBal\") AS \"tLoanBal\"";
			sql += "				 FROM \"InnReCheck\"";
			sql += "	  			 WHERE \"YearMonth\" = :yyyymm";
			sql += "				   AND \"ConditionCode\" = :cond ";
			sql += "				   AND \"LoanBal\" > 0 ";
			sql += "				 GROUP BY \"CustNo\") R2";
			sql += "	    ON R2.\"CustNo\" = S1.\"CustNo\" ";
			sql += "	  LEFT JOIN (SELECT \"CustNo\"";
			sql += "					   ,\"FacmNo\" ";
			sql += "					   ,MIN(\"PrevPayIntDate\") AS \"PrevPayIntDate\"";
			sql += "				 FROM \"LoanBorMain\"";
			sql += "	  			 WHERE \"LoanBal\" > 0 ";
			sql += "				 GROUP BY \"CustNo\",\"FacmNo\") R3";
			sql += "	    ON R3.\"CustNo\" = S1.\"CustNo\" ";
			sql += "	   AND R3.\"FacmNo\" = S1.\"FacmNo\" ";
			sql += "	  LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = S1.\"CustNo\" ";
			sql += "	   						 AND F.\"FacmNo\" = S1.\"FacmNo\" ";
			sql += "	  LEFT JOIN \"CdEmp\" CE ON CE.\"EmployeeNo\" = F.\"BusinessOfficer\" ";
			sql += "	  LEFT JOIN \"CdEmp\" CE2 ON CE2.\"EmployeeNo\" = S1.\"ReChkEmpNo\" ";
			if (reChkYearMonth != 0) {
				sql += "	WHERE S1.\"ReChkYearMonth\" = :reChkYearMonth ";
			}
			sql += "	  ORDER BY S1.\"CustNo\", S1.\"FacmNo\"";

			break;

		case 5:// 覆審案件資料表-企金未達3000萬
			sql += "	SELECT R.\"CustNo\" ";
			sql += "			,R.\"FacmNo\" ";
			sql += "			,\"Fn_ParseEOL\"(C.\"CustName\",0) AS \"CustName\"";
			sql += "			,R.\"ReChkUnit\" ";
			sql += "			,MOD(R.\"ReChkYearMonth\", 100) AS \"ReChkMonth\"";
			sql += "			,R.\"DrawdownDate\" ";
			sql += "			,R3.\"PrevPayIntDate\"";
			sql += "			,R.\"LoanBal\" ";
			sql += "			,R2.\"tLoanBal\" ";
			sql += "			,NVL(RC.\"RenewCode\",' ') AS \"RenewCode\"";
			sql += "			,DECODE(R.\"ReCheckCode\",'2','*',' ') AS \"ReCheckCode\"";
			sql += "			,DECODE(R.\"ReChkYearMonth\",0,' ','V') AS \"ReChkYearMonthCode\"";
			sql += "			,R.\"ReChkYearMonth\" ";
			sql += "			,R.\"Evaluation\" ";
			sql += "			,CE2.\"Fullname\" AS \"ReChkName\"";
			sql += "			,CE.\"Fullname\" AS \"TlrName\"";
			sql += "	  FROM \"InnReCheck\" R";
			sql += "	  LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = R.\"CustNo\"";
			sql += "	  LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = R.\"CustNo\"";
			sql += "							 AND F.\"FacmNo\" = R.\"FacmNo\"";
			sql += "	  LEFT JOIN \"CdEmp\" E ON E.\"EmployeeNo\" = F.\"Supervisor\"";
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
			sql += "				   AND \"ConditionCode\" = 5";
			sql += "				   AND \"LoanBal\" > 0 ";
			sql += "				 GROUP BY \"CustNo\") R2";
			sql += "	    ON R2.\"CustNo\" = R.\"CustNo\" ";
			sql += "	  LEFT JOIN (SELECT \"CustNo\"";
			sql += "					   ,\"FacmNo\" ";
			sql += "					   ,MIN(\"PrevPayIntDate\") AS \"PrevPayIntDate\"";
			sql += "				 FROM \"LoanBorMain\"";
			sql += "	  			 WHERE \"LoanBal\" > 0 ";
			sql += "				 GROUP BY \"CustNo\",\"FacmNo\") R3";
			sql += "	    ON R3.\"CustNo\" = R.\"CustNo\" ";
			sql += "	   AND R3.\"FacmNo\" = R.\"FacmNo\" ";
			sql += "	  LEFT JOIN \"CdEmp\" CE ON CE.\"EmployeeNo\" = F.\"BusinessOfficer\" ";
			sql += "	  LEFT JOIN \"CdEmp\" CE2 ON CE2.\"EmployeeNo\" = R.\"ReChkEmpNo\" ";
			sql += "	  WHERE R.\"YearMonth\" = :yyyymm";
			sql += "		AND R.\"ConditionCode\" = :cond ";
			sql += "		AND R.\"LoanBal\" > 0 ";
			if (reChkYearMonth != 0) {
				sql += "		AND R.\"ReChkYearMonth\" = :reChkYearMonth ";
			}
			sql += "	  ORDER BY R.\"CustNo\", R.\"FacmNo\"";
			break;

		case 6:// 土地貸款覆審表
			sql += "WITH \"TEMP\" AS (SELECT \"CustNo\"";
			sql += "							   ,\"FacmNo\"";
			sql += "					  	   	   ,MAX(\"DrawdownDate\") AS \"DrawdownDate\"";
			sql += "						FROM \"InnReCheck\"";
			sql += "						WHERE \"YearMonth\" = :yyyymm ";
			sql += "						GROUP BY \"CustNo\"";
			sql += "								,\"FacmNo\")";
			sql += "	 SELECT  R.\"CustNo\" ";
			sql += "			,R.\"FacmNo\" ";
			sql += "			,\"Fn_ParseEOL\"(C.\"CustName\",0) AS \"CustName\"";
			sql += "			,R.\"CustTypeItem\" ";
			sql += "			,R.\"UsageItem\" ";
			sql += "			,CI.\"CityItem\" ";
			sql += "			,MOD(R.\"ReChkYearMonth\",100) AS \"ReChkMonth\"";
			sql += "			,TEMP.\"DrawdownDate\" ";
			sql += "			,R3.\"PrevPayIntDate\"";
			sql += "			,R.\"LoanBal\" ";
			sql += "			,R2.\"tLoanBal\"";
			sql += "			,NVL(RC.\"RenewCode\",' ') AS \"RenewCode\"";
			sql += "			,DECODE(R.\"FollowMark\",'1','免','2','V','3','X',' ') AS \"FollowMark\"";
			sql += "			,R.\"ReChkUnit\" ";
			sql += "			,R.\"Evaluation\" ";
			sql += "			,R.\"Remark\" ";
			sql += "			,CE2.\"Fullname\" AS \"ReChkName\"";
			sql += "			,CE.\"Fullname\" AS \"TlrName\"";
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
			sql += "	  LEFT JOIN (SELECT \"CustNo\"";
			sql += "					   ,\"FacmNo\" ";
			sql += "					   ,MIN(\"PrevPayIntDate\") AS \"PrevPayIntDate\"";
			sql += "				 FROM \"LoanBorMain\"";
			sql += "	  			 WHERE \"LoanBal\" > 0 ";
			sql += "				 GROUP BY \"CustNo\",\"FacmNo\") R3";
			sql += "	    ON R3.\"CustNo\" = R.\"CustNo\" ";
			sql += "	   AND R3.\"FacmNo\" = R.\"FacmNo\" ";
			sql += "	  LEFT JOIN \"TEMP\" TEMP ON TEMP.\"CustNo\" = R.\"CustNo\"";
			sql += "	  						 AND TEMP.\"FacmNo\" = R.\"FacmNo\"";
			sql += "	  LEFT JOIN \"CdEmp\" CE ON CE.\"EmployeeNo\" = FM.\"BusinessOfficer\" ";
			sql += "	  LEFT JOIN \"CdEmp\" CE2 ON CE2.\"EmployeeNo\" = R.\"ReChkEmpNo\" ";
			sql += "	  WHERE R.\"YearMonth\" = :yyyymm";
			sql += "		AND R.\"ConditionCode\" = :cond";
			sql += "		AND R.\"LoanBal\" > 0 ";
			if (reChkYearMonth != 0) {
				sql += "		AND R.\"ReChkYearMonth\" = :reChkYearMonth ";
			}
			sql += "	  ORDER BY R.\"CustNo\", R.\"FacmNo\"";
			break;

		}

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("yyyymm", iYearMonth);
		query.setParameter("cond", conditionCode);
		if (conditionCode == 4) {
			this.info("iYearMonthL5=" + iYearMonthL5);
			query.setParameter("l5yymm", iYearMonthL5);
		}

		if (reChkYearMonth != 0) {
			query.setParameter("reChkYearMonth", reChkYearMonth);
		}
		return this.convertToMap(query);
	}

	/**
	 * 查詢明細(覆審相關報表共用)
	 * 
	 * @param titaVo
	 * @param yearMonth     西元年月
	 * @param conditionCode 條件代碼
	 * @return
	 * @throws Exception
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

		this.info("L9734.findAll iYeariMonth=" + iYearMonth);

		String sql = "	";
		sql += "	SELECT :cond AS F0";
		sql += "		  ,CM.\"BranchNo\" AS F1";
		sql += "		  ,C.\"CustNo\" AS F2";
		sql += "		  ,C.\"FacmNo\" AS F3";
		sql += "		  ,C.\"BormNo\" AS F4";
		sql += "		  ,CM.\"CustName\" AS F5";
		sql += "		  ,L.\"DrawdownDate\" - 19110000 AS F6";
		sql += "		  ,C.\"LoanBalance\" AS F7";
		sql += "		  ,F.\"MaturityDate\" - 19110000 AS F8";
		sql += "		  ,C.\"ClCode1\" AS F9";
		sql += "		  ,C.\"ClCode2\" AS F10";
		sql += "		  ,C.\"ClNo\" AS F11";
		sql += "		  ,NVL(CA.\"CityShort\",' ') AS F12";
		sql += "		  ,NVL(CA.\"AreaShort\",' ') AS F13";
		sql += "		  ,NVL(SUBSTR(CLS.\"IrItem\",1,INSTR(CLS.\"IrItem\",'段',1,1)-1),' ') AS F14";
		sql += "		  ,NVL(SUBSTR(CLS.\"IrItem\",INSTR(CLS.\"IrItem\",'段',1,1)+1,INSTR(CLS.\"IrItem\",'段',1,2)-1),' ') AS F15";
		sql += "		  ,NVL(CLB.\"BdLocation\",' ') AS F16";
		sql += "		  ,CT.\"CityCode\" AS F17";
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
		sql += "	LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" = C.\"CustNo\"";
		sql += "						       AND L.\"FacmNo\" = C.\"FacmNo\"";
		sql += "						       AND L.\"BormNo\" = C.\"BormNo\"";
		sql += "	LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = R.\"CustNo\"";
		sql += "	LEFT JOIN \"ClLand\" CLD ON CLD.\"ClCode1\" = C.\"ClCode1\"";
		sql += "							AND CLD.\"ClCode2\" = C.\"ClCode2\"";
		sql += "							AND CLD.\"ClNo\" = C.\"ClNo\"";
		sql += "							AND CLD.\"LandSeq\" = 1 ";
		sql += "	LEFT JOIN \"CdCity\" CT ON CT.\"CityCode\" = C.\"CityCode\"";
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