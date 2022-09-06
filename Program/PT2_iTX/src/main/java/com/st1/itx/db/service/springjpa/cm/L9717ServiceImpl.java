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
public class L9717ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public static enum OutputSortBy {
		Agent, Year, LargeAmt_Customer, LargeAmt_Agent
	}

	public List<Map<String, String>> findAll(TitaVo titaVo, OutputSortBy kind) throws Exception {
		this.info("l9717.findAll ");

		String sql = "";

		switch (kind) {
		case Year:
			sql += " SELECT \"FirstDrawdownYear\"-1911 || ' 年撥款件' \"FirstDrawdownYearShow\" ";
			sql += "       ,SUM(\"1TermCount\") \"1TermCount\" ";
			sql += "       ,SUM(\"1TermAmount\") \"1TermAmount\" ";
			sql += "       ,SUM(\"2TermCount\") \"2TermCount\" ";
			sql += "       ,SUM(\"2TermAmount\") \"2TermAmount\" ";
			sql += "       ,SUM(\"3TermCount\") \"3TermCount\" ";
			sql += "       ,SUM(\"3TermAmount\") \"3TermAmount\" ";
			sql += "       ,SUM(\"4TermCount\") \"4TermCount\" ";
			sql += "       ,SUM(\"4TermAmount\") \"4TermAmount\" ";
			sql += "       ,SUM(\"5TermCount\") \"5TermCount\" ";
			sql += "       ,SUM(\"5TermAmount\") \"5TermAmount\" ";
			sql += "       ,SUM(\"6TermCount\") \"6TermCount\" ";
			sql += "       ,SUM(\"6TermAmount\") \"6TermAmount\" ";
			sql += "       ,SUM(\"TurnOvduCount\") \"TurnOvduCount\" ";
			sql += "       ,SUM(\"TurnOvduAmount\") \"TurnOvduAmount\" ";
			sql += "       ,SUM(\"TotalCount\") \"TotalCount\" ";
			sql += "       ,SUM(\"TotalAmount\") \"TotalAmount\" ";
//			sql += " FROM ( SELECT LEVEL \"FirstDrawdownYear\" ";
//			sql += "              ,0 \"1TermCount\" ";
//			sql += "              ,0 \"1TermAmount\" ";
//			sql += "              ,0 \"2TermCount\" ";
//			sql += "              ,0 \"2TermAmount\" ";
//			sql += "              ,0 \"3TermCount\" ";
//			sql += "              ,0 \"3TermAmount\" ";
//			sql += "              ,0 \"4TermCount\" ";
//			sql += "              ,0 \"4TermAmount\" ";
//			sql += "              ,0 \"5TermCount\" ";
//			sql += "              ,0 \"5TermAmount\" ";
//			sql += "              ,0 \"6TermCount\" ";
//			sql += "              ,0 \"6TermAmount\" ";
//			sql += "              ,0 \"TurnOvduCount\" ";
//			sql += "              ,0 \"TurnOvduAmount\" ";
//			sql += "              ,0 \"TotalCount\" ";
//			sql += "              ,0 \"TotalAmount\" ";
//			sql += "        FROM DUAL ";
//			sql += "        WHERE LEVEL >= 1994 ";
//			sql += "        CONNECT BY LEVEL <= SUBSTR(\"Fn_GetLastMonth\"(:entYear), 1, 4) ";
//			sql += "  ";
//			sql += "        UNION ";
			sql += "  ";
			sql += " FROM ( SELECT TO_NUMBER(SUBSTR(FM.\"FirstDrawdownDate\", 1, 4)) \"FirstDrawdownYear\" ";
			sql += "              ,DECODE(MFB.\"OvduTerm\", 1, 1, 0) \"1TermCount\" ";
			sql += "              ,DECODE(MFB.\"OvduTerm\", 1, MFB.\"PrinBalance\", 0) \"1TermAmount\" ";
			sql += "              ,DECODE(MFB.\"OvduTerm\", 2, 1, 0) \"2TermCount\" ";
			sql += "              ,DECODE(MFB.\"OvduTerm\", 2, MFB.\"PrinBalance\", 0) \"2TermAmount\" ";
			sql += "              ,DECODE(MFB.\"OvduTerm\", 3, 1, 0) \"3TermCount\" ";
			sql += "              ,DECODE(MFB.\"OvduTerm\", 3, MFB.\"PrinBalance\", 0) \"3TermAmount\" ";
			sql += "              ,DECODE(MFB.\"OvduTerm\", 4, 1, 0) \"4TermCount\" ";
			sql += "              ,DECODE(MFB.\"OvduTerm\", 4, MFB.\"PrinBalance\", 0) \"4TermAmount\" ";
			sql += "              ,DECODE(MFB.\"OvduTerm\", 5, 1, 0) \"5TermCount\" ";
			sql += "              ,DECODE(MFB.\"OvduTerm\", 5, MFB.\"PrinBalance\", 0) \"5TermAmount\" ";
			sql += "              ,DECODE(MFB.\"OvduTerm\", 6, 1, 0) \"6TermCount\" ";
			sql += "              ,DECODE(MFB.\"OvduTerm\", 6, MFB.\"PrinBalance\", 0) \"6TermAmount\" ";
			sql += "              ,DECODE(MFB.\"OvduTerm\", 9, 1, 0) \"TurnOvduCount\" ";
			sql += "              ,DECODE(MFB.\"OvduTerm\", 9, MFB.\"PrinBalance\", 0) \"TurnOvduAmount\" ";
			sql += "              ,1 \"TotalCount\" ";
			sql += "              ,MFB.\"PrinBalance\" \"TotalAmount\" ";
			sql += " 		FROM ( ";
			sql += " 			SELECT \"CustNo\" ";
			sql += " 		  		  ,\"FacmNo\" ";
			sql += " 		  		  ,CASE ";
			sql += " 		   	 		 WHEN \"AcctCode\" = '990' THEN 9";
			sql += " 		   	 		 WHEN \"OvduTerm\" IN (1,2,3,4,5) THEN \"OvduTerm\" ";
			sql += " 		   		   ELSE 0 END AS \"OvduTerm\" ";
//			sql += " 		  		  ,\"OvduDays\" ";
			sql += " 		  		  ,\"PrinBalance\" ";
			sql += " 			FROM \"MonthlyFacBal\"";
			sql += " 			WHERE \"YearMonth\" = :entYearMonth ";
			sql += " 	  		  AND \"PrinBalance\" > 0 ";
			sql += " 		) MFB ";
			sql += "        LEFT JOIN \"FacMain\" FM ON FM.\"CustNo\" = MFB.\"CustNo\" ";
			sql += "                              AND FM.\"FacmNo\" = MFB.\"FacmNo\" ";
			sql += "        WHERE  FM.\"FirstDrawdownDate\" >= 19810101 ";
			sql += "          AND (NVL(:businessOfficer, ' ') = '999999' OR :businessOfficer = FM.\"BusinessOfficer\") ";
//			sql += "   		  AND MFB.\"OvduDays\" > 0 ";
//			sql += "   		  AND MFB.\"OvduTerm\" <> 0 ";
			sql += "      ) ";
			sql += " GROUP BY \"FirstDrawdownYear\" ";
			sql += " ORDER BY \"FirstDrawdownYear\" ";

			/*
			 * sql += " WITH \"allData\" AS ( "; sql +=
			 * " SELECT \"FirstDrawdownYear\"-1911 || ' 年撥款件' \"FirstDrawdownYearShow\" ";
			 * sql += "       ,SUM(\"1TermCount\") \"1TermCount\" "; sql +=
			 * "       ,SUM(\"1TermAmount\") \"1TermAmount\" "; sql +=
			 * "       ,SUM(\"2TermCount\") \"2TermCount\" "; sql +=
			 * "       ,SUM(\"2TermAmount\") \"2TermAmount\" "; sql +=
			 * "       ,SUM(\"3TermCount\") \"3TermCount\" "; sql +=
			 * "       ,SUM(\"3TermAmount\") \"3TermAmount\" "; sql +=
			 * "       ,SUM(\"4TermCount\") \"4TermCount\" "; sql +=
			 * "       ,SUM(\"4TermAmount\") \"4TermAmount\" "; sql +=
			 * "       ,SUM(\"5TermCount\") \"5TermCount\" "; sql +=
			 * "       ,SUM(\"5TermAmount\") \"5TermAmount\" "; sql +=
			 * "       ,SUM(\"6TermCount\") \"6TermCount\" "; sql +=
			 * "       ,SUM(\"6TermAmount\") \"6TermAmount\" "; sql +=
			 * "       ,SUM(\"TurnOvduCount\") \"TurnOvduCount\" "; sql +=
			 * "       ,SUM(\"TurnOvduAmount\") \"TurnOvduAmount\" "; sql +=
			 * "       ,SUM(\"TotalCount\") \"TotalCount\" "; sql +=
			 * "       ,SUM(\"TotalAmount\") \"TotalAmount\" "; sql +=
			 * " FROM ( SELECT LEVEL \"FirstDrawdownYear\" "; sql +=
			 * "              ,0 \"1TermCount\" "; sql +=
			 * "              ,0 \"1TermAmount\" "; sql +=
			 * "              ,0 \"2TermCount\" "; sql +=
			 * "              ,0 \"2TermAmount\" "; sql +=
			 * "              ,0 \"3TermCount\" "; sql +=
			 * "              ,0 \"3TermAmount\" "; sql +=
			 * "              ,0 \"4TermCount\" "; sql +=
			 * "              ,0 \"4TermAmount\" "; sql +=
			 * "              ,0 \"5TermCount\" "; sql +=
			 * "              ,0 \"5TermAmount\" "; sql +=
			 * "              ,0 \"6TermCount\" "; sql +=
			 * "              ,0 \"6TermAmount\" "; sql +=
			 * "              ,0 \"TurnOvduCount\" "; sql +=
			 * "              ,0 \"TurnOvduAmount\" "; sql +=
			 * "              ,0 \"TotalCount\" "; sql +=
			 * "              ,0 \"TotalAmount\" "; sql += "        FROM DUAL "; sql +=
			 * "        WHERE LEVEL >= 1994 "; sql +=
			 * "        CONNECT BY LEVEL <= SUBSTR(\"Fn_GetLastMonth\"(:entYear), 1, 4) ";
			 * sql += "  "; sql += "        UNION "; sql += "  "; sql +=
			 * "        SELECT TO_NUMBER(SUBSTR(FM.\"FirstDrawdownDate\", 1, 4)) \"FirstDrawdownYear\" "
			 * ; sql += "              ,DECODE(MFB.\"OvduTerm\", 1, 1, 0) \"1TermCount\" ";
			 * sql +=
			 * "              ,DECODE(MFB.\"OvduTerm\", 1, MFB.\"PrinBalance\", 0) \"1TermAmount\" "
			 * ; sql += "              ,DECODE(MFB.\"OvduTerm\", 2, 1, 0) \"2TermCount\" ";
			 * sql +=
			 * "              ,DECODE(MFB.\"OvduTerm\", 2, MFB.\"PrinBalance\", 0) \"2TermAmount\" "
			 * ; sql += "              ,DECODE(MFB.\"OvduTerm\", 3, 1, 0) \"3TermCount\" ";
			 * sql +=
			 * "              ,DECODE(MFB.\"OvduTerm\", 3, MFB.\"PrinBalance\", 0) \"3TermAmount\" "
			 * ; sql += "              ,DECODE(MFB.\"OvduTerm\", 4, 1, 0) \"4TermCount\" ";
			 * sql +=
			 * "              ,DECODE(MFB.\"OvduTerm\", 4, MFB.\"PrinBalance\", 0) \"4TermAmount\" "
			 * ; sql += "              ,DECODE(MFB.\"OvduTerm\", 5, 1, 0) \"5TermCount\" ";
			 * sql +=
			 * "              ,DECODE(MFB.\"OvduTerm\", 5, MFB.\"PrinBalance\", 0) \"5TermAmount\" "
			 * ; sql += "              ,DECODE(MFB.\"OvduTerm\", 6, 1, 0) \"6TermCount\" ";
			 * sql +=
			 * "              ,DECODE(MFB.\"OvduTerm\", 6, MFB.\"PrinBalance\", 0) \"6TermAmount\" "
			 * ; sql +=
			 * "              ,DECODE(MFB.\"OvduTerm\", 9, 1, 0) \"TurnOvduCount\" "; sql +=
			 * "              ,DECODE(MFB.\"OvduTerm\", 9, MFB.\"PrinBalance\", 0) \"TurnOvduAmount\" "
			 * ; sql += "              ,1 \"TotalCount\" "; sql +=
			 * "              ,MFB.\"PrinBalance\" \"TotalAmount\" "; sql +=
			 * " 		FROM ( "; sql += " 			SELECT \"CustNo\" "; sql +=
			 * " 		  		  ,\"FacmNo\" "; sql += " 		  		  ,CASE "; sql +=
			 * " 		   	 		 WHEN \"AcctCode\" = 990 THEN 9"; sql +=
			 * " 		   	 		 WHEN \"OvduTerm\" IN (1,2,3,4,5) THEN \"OvduTerm\" ";
			 * sql += " 		   		   ELSE 0 END AS \"OvduTerm\" "; sql +=
			 * " 		  		  ,\"PrinBalance\" "; sql +=
			 * " 			FROM \"MonthlyFacBal\""; sql +=
			 * " 			WHERE \"YearMonth\" = :entYearMonth "; sql +=
			 * " 	  		  AND \"PrinBalance\" > 0 "; sql += " 		) MFB "; sql +=
			 * "        LEFT JOIN \"FacMain\" FM ON FM.\"CustNo\" = MFB.\"CustNo\" "; sql +=
			 * "                              AND FM.\"FacmNo\" = MFB.\"FacmNo\" "; sql +=
			 * "        WHERE  FM.\"FirstDrawdownDate\" >= 19810101 "; sql +=
			 * "          AND (NVL(:businessOfficer, ' ') = ' ' OR :businessOfficer = FM.\"BusinessOfficer\") "
			 * ; sql += "   		  AND MFB.\"OvduTerm\" <> 0 "; sql += "      ) "; sql +=
			 * " GROUP BY \"FirstDrawdownYear\" "; sql += " )"; sql += " SELECT * FROM (";
			 * sql += " SELECT * FROM \"allData\""; sql += " UNION"; sql +=
			 * " SELECT '999 各期小計' AS \"FirstDrawdownYearShow\""; sql +=
			 * "       ,SUM(\"1TermCount\") \"1TermCount\" "; sql +=
			 * "       ,SUM(\"1TermAmount\") \"1TermAmount\" "; sql +=
			 * "       ,SUM(\"2TermCount\") \"2TermCount\" "; sql +=
			 * "       ,SUM(\"2TermAmount\") \"2TermAmount\" "; sql +=
			 * "       ,SUM(\"3TermCount\") \"3TermCount\" "; sql +=
			 * "       ,SUM(\"3TermAmount\") \"3TermAmount\" "; sql +=
			 * "       ,SUM(\"4TermCount\") \"4TermCount\" "; sql +=
			 * "       ,SUM(\"4TermAmount\") \"4TermAmount\" "; sql +=
			 * "       ,SUM(\"5TermCount\") \"5TermCount\" "; sql +=
			 * "       ,SUM(\"5TermAmount\") \"5TermAmount\" "; sql +=
			 * "       ,SUM(\"6TermCount\") \"6TermCount\" "; sql +=
			 * "       ,SUM(\"6TermAmount\") \"6TermAmount\" "; sql +=
			 * "       ,SUM(\"TurnOvduCount\") \"TurnOvduCount\" "; sql +=
			 * "       ,SUM(\"TurnOvduAmount\") \"TurnOvduAmount\" "; sql +=
			 * "       ,SUM(\"TotalCount\") \"TotalCount\" "; sql +=
			 * "       ,SUM(\"TotalAmount\") \"TotalAmount\" "; sql += " FROM \"allData\"";
			 * sql += " ) R"; sql +=
			 * " ORDER BY TO_NUMBER(SUBSTE(R.\"FirstDrawdownYearShow\" , 0 , 3 )) ASC";
			 */

			break;

		case Agent:
			sql += " SELECT UPPER( NVL( ";
			sql += "		CASE WHEN FM.\"FirstDrawdownDate\" >= 20050101 ";
			sql += "             THEN FM.\"BusinessOfficer\" ";
			sql += "        ELSE FM.\"CreditOfficer\" END ,' '))\"Officer\" ";
			sql += "	   ,NVL(";
			sql += " 	    CASE WHEN FM.\"FirstDrawdownDate\" >= 20050101 ";
			sql += "             THEN BusinessOfficer.\"Fullname\" ";
			sql += "        ELSE CreditOfficer.\"Fullname\" END,' ') \"EmpName\" ";
			sql += "       ,SUM(DECODE(M.\"OvduTerm\", 1, 1, 0) ) \"1TermCount\" ";
			sql += "       ,SUM(DECODE(M.\"OvduTerm\", 1, M.\"PrinBalance\", 0)) \"1TermAmount\" ";
			sql += "       ,SUM(DECODE(M.\"OvduTerm\", 2, 1, 0)) \"2TermCount\" ";
			sql += "       ,SUM(DECODE(M.\"OvduTerm\", 2, M.\"PrinBalance\", 0)) \"2TermAmount\" ";
			sql += "       ,SUM(DECODE(M.\"OvduTerm\", 3, 1, 0)) \"3TermCount\" ";
			sql += "       ,SUM(DECODE(M.\"OvduTerm\", 3, M.\"PrinBalance\", 0)) \"3TermAmount\" ";
			sql += "       ,SUM(DECODE(M.\"OvduTerm\", 4, 1, 0)) \"4TermCount\" ";
			sql += "       ,SUM(DECODE(M.\"OvduTerm\", 4, M.\"PrinBalance\", 0)) \"4TermAmount\" ";
			sql += "       ,SUM(DECODE(M.\"OvduTerm\", 5, 1, 0)) \"5TermCount\" ";
			sql += "       ,SUM(DECODE(M.\"OvduTerm\", 5, M.\"PrinBalance\", 0)) \"5TermAmount\" ";
			sql += "       ,SUM(DECODE(M.\"OvduTerm\", 6, 1, 0)) \"6TermCount\" ";
			sql += "       ,SUM(DECODE(M.\"OvduTerm\", 6, M.\"PrinBalance\", 0)) \"6TermAmount\" ";
			sql += "       ,SUM(DECODE(M.\"OvduTerm\", '9', 1, 0)) \"TurnOvduCount\" ";
			sql += "       ,SUM(DECODE(M.\"OvduTerm\", '9', M.\"PrinBalance\", 0)) \"TurnOvduAmount\" ";
			sql += "       ,COUNT(*) \"TotalCount\" ";
			sql += "       ,SUM(M.\"PrinBalance\") \"TotalAmount\" ";
			sql += " FROM ( ";
			sql += " 	SELECT \"CustNo\" ";
			sql += " 		  ,\"FacmNo\" ";
			sql += " 		  ,CASE ";
			sql += " 		   	 WHEN \"AcctCode\" = '990' THEN 9";
			sql += " 		   	 WHEN \"OvduTerm\" IN (1,2,3,4,5) THEN \"OvduTerm\" ";
			sql += " 		   ELSE 0 END AS \"OvduTerm\" ";
			sql += " 		  ,\"OvduDays\" ";
			sql += " 		  ,\"PrinBalance\" ";
			sql += " 	FROM \"MonthlyFacBal\"";
			sql += " 	WHERE \"YearMonth\" =  :entYearMonth ";
			sql += " 	  AND \"PrinBalance\" > 0 ";
			sql += " ) M ";
			sql += " LEFT JOIN \"FacMain\" FM ON FM.\"CustNo\" = M.\"CustNo\" ";
			sql += "                       AND FM.\"FacmNo\" = M.\"FacmNo\" ";
			sql += " LEFT JOIN \"CdEmp\" BusinessOfficer ON UPPER(BusinessOfficer.\"EmployeeNo\") = UPPER(FM.\"BusinessOfficer\") ";
			sql += " LEFT JOIN \"CdEmp\" CreditOfficer ON UPPER(CreditOfficer.\"EmployeeNo\") = UPPER(FM.\"CreditOfficer\") ";
			sql += " WHERE ( :businessOfficer = '999999'  ";
			sql += "    OR  NVL (CASE WHEN FM.\"FirstDrawdownDate\" >= 20050101 ";
			sql += "                   THEN BusinessOfficer.\"EmployeeNo\" ";
			sql += "       		  ELSE CreditOfficer.\"EmployeeNo\" END,  ' ') = :businessOfficer )";
//			sql += "   AND M.\"OvduDays\" > 0 ";
//			sql += "   AND M.\"OvduTerm\" <> 0 ";
			sql += " GROUP BY UPPER(NVL( CASE WHEN FM.\"FirstDrawdownDate\" >= 20050101 ";
			sql += "             			  THEN FM.\"BusinessOfficer\" ";
			sql += "        	   			  ELSE FM.\"CreditOfficer\" END ,' '))";
			sql += "		 ,NVL( CASE WHEN FM.\"FirstDrawdownDate\" >= 20050101 ";
			sql += "             		THEN BusinessOfficer.\"Fullname\" ";
			sql += "        	   ELSE CreditOfficer.\"Fullname\" END,' ')";
			sql += " ORDER BY CASE ";
			sql += " 			WHEN ASCII(SUBSTR(\"Officer\",0,1)) IS NULL OR \"Officer\" = ' ' ";
			sql += "            THEN 64 ";
			sql += " 			WHEN ASCII(SUBSTR(\"Officer\",0,1)) >= 65 ";
			sql += "            THEN ASCII(SUBSTR(\"Officer\",0,1)) ";
			sql += "          ELSE ASCII(SUBSTR(\"Officer\",0,1)) + 43 END ASC";
			sql += "         ,\"Officer\" ASC";

			break;

		case LargeAmt_Customer:
			sql += " SELECT FM.\"CreditOfficer\" \"Officer\" ";
			sql += "       ,CE.\"Fullname\" \"EmpName\" ";
			sql += "       ,MFB.\"CustNo\" \"CustNo\" ";
			sql += "       ,SUBSTR(\"Fn_ParseEOL\"(CM.\"CustName\", 0), 1, 5) \"CustName\" ";
			sql += "       ,DECODE(MFB.\"AcctCode\", '990', 990, MFB.\"OvduTerm\") \"Term\" ";
			sql += "       ,SUM(MFB.\"PrinBalance\") \"PrinBal\" ";
			sql += " FROM \"MonthlyFacBal\" MFB ";
			sql += " LEFT JOIN \"FacMain\" FM ON FM.\"CustNo\" = MFB.\"CustNo\" ";
			sql += "                       AND FM.\"FacmNo\" = MFB.\"FacmNo\" ";
			sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = MFB.\"CustNo\" ";
			sql += " LEFT JOIN \"CdEmp\" CE ON CE.\"EmployeeNo\" = FM.\"CreditOfficer\" ";
			sql += " WHERE ((MFB.\"OvduTerm\" BETWEEN :ovduTermMin AND :ovduTermMax AND MFB.\"OvduDays\" > 0) ";
			sql += "        OR MFB.\"AcctCode\" = '990') ";
			sql += "   AND MFB.\"PrinBalance\" > 50000000 ";
			sql += "   AND MFB.\"YearMonth\" = \"Fn_GetLastMonth\"(:entYearMonth) ";
			sql += "   AND NVL(FM.\"CreditOfficer\", ' ') != ' ' ";
			sql += "   AND (:businessOfficer = ' ' OR FM.\"CreditOfficer\" = :businessOfficer) ";
			sql += " GROUP BY FM.\"CreditOfficer\" ";
			sql += "         ,CE.\"Fullname\" ";
			sql += "         ,MFB.\"CustNo\" ";
			sql += "         ,CM.\"CustName\" ";
			sql += "         ,DECODE(MFB.\"AcctCode\", '990', 990, MFB.\"OvduTerm\") ";
			sql += " ORDER BY \"Officer\" ";

			break;

		case LargeAmt_Agent:
			sql += " SELECT FM.\"CreditOfficer\" \"Officer\" ";
			sql += "       ,CE.\"Fullname\" \"EmpName\" ";
			sql += "       ,SUM(DECODE(MFB.\"OvduTerm\", 1, 1, 0)) \"1TermCount\" ";
			sql += "       ,SUM(DECODE(MFB.\"OvduTerm\", 1, MFB.\"PrinBalance\", 0)) \"1TermAmount\" ";
			sql += "       ,SUM(DECODE(MFB.\"OvduTerm\", 2, 1, 0)) \"2TermCount\" ";
			sql += "       ,SUM(DECODE(MFB.\"OvduTerm\", 2, MFB.\"PrinBalance\", 0)) \"2TermAmount\" ";
			sql += "       ,SUM(DECODE(MFB.\"OvduTerm\", 3, 1, 0)) \"3TermCount\" ";
			sql += "       ,SUM(DECODE(MFB.\"OvduTerm\", 3, MFB.\"PrinBalance\", 0)) \"3TermAmount\" ";
			sql += "       ,SUM(DECODE(MFB.\"OvduTerm\", 4, 1, 0)) \"4TermCount\" ";
			sql += "       ,SUM(DECODE(MFB.\"OvduTerm\", 4, MFB.\"PrinBalance\", 0)) \"4TermAmount\" ";
			sql += "       ,SUM(DECODE(MFB.\"OvduTerm\", 5, 1, 0)) \"5TermCount\" ";
			sql += "       ,SUM(DECODE(MFB.\"OvduTerm\", 5, MFB.\"PrinBalance\", 0)) \"5TermAmount\" ";
			sql += "       ,SUM(CASE WHEN MFB.\"OvduTerm\" > 6 AND MFB.\"AcctCode\" != '990' ";
			sql += "                 THEN 1 ";
			sql += "            ELSE 0 END) \"6TermCount\" ";
			sql += "       ,SUM(CASE WHEN MFB.\"OvduTerm\" > 6 AND MFB.\"AcctCode\" != '990' ";
			sql += "                 THEN MFB.\"PrinBalance\" ";
			sql += "            ELSE 0 END) \"6TermAmount\" ";
			sql += "       ,SUM(DECODE(MFB.\"AcctCode\", '990', 1, 0)) \"TurnOvduCount\" ";
			sql += "       ,SUM(DECODE(MFB.\"AcctCode\", '990', MFB.\"PrinBalance\", 0)) \"TurnOvduAmount\" ";
			sql += "       ,COUNT(*) \"TotalCount\" ";
			sql += "       ,SUM(MFB.\"PrinBalance\") \"TotalAmount\" ";
			sql += " FROM \"MonthlyFacBal\" MFB ";
			sql += " LEFT JOIN \"FacMain\" FM ON FM.\"CustNo\" = MFB.\"CustNo\" ";
			sql += "                         AND FM.\"FacmNo\" = MFB.\"FacmNo\" ";
			sql += " LEFT JOIN \"CdEmp\" CE ON CE.\"EmployeeNo\" = FM.\"CreditOfficer\" ";
			sql += " WHERE ((MFB.\"OvduTerm\" BETWEEN :ovduTermMin AND :ovduTermMax AND MFB.\"OvduDays\" > 0) ";
			sql += "        OR MFB.\"AcctCode\" = '990') ";
			sql += "   AND MFB.\"PrinBalance\" > 50000000 ";
			sql += "   AND MFB.\"YearMonth\" = \"Fn_GetLastMonth\"(:entYearMonth) ";
			sql += "   AND NVL(FM.\"CreditOfficer\", ' ') != ' ' ";
			sql += "   AND (:businessOfficer = ' ' OR FM.\"CreditOfficer\" = :businessOfficer) ";
			sql += " GROUP BY FM.\"CreditOfficer\", CE.\"Fullname\" ";
			sql += " ORDER BY \"Officer\" ";

			break;

		default:
			break;
		}

		this.info("sql=" + sql);

		// 表中實際要查lastYearMonth
		// 利用 DB 的 Function 做, 簡短一些
		String entYearMonth = (parse.stringToInteger(titaVo.getParam("inputYear")) + 1911) + titaVo.getParam("inputMonth");

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("entYearMonth", entYearMonth);

		if (kind == OutputSortBy.LargeAmt_Agent || kind == OutputSortBy.LargeAmt_Customer) {
			query.setParameter("ovduTermMin", titaVo.getParam("inputOverdueTermMin"));
			query.setParameter("ovduTermMax", titaVo.getParam("inputOverdueTermMax"));
		}

		// 員編輸入空白代表所有員編
		query.setParameter("businessOfficer", titaVo.getParam("inputBusinessOfficer").trim().isEmpty() ? " " : titaVo.getParam("inputBusinessOfficer"));

//		if (kind == OutputSortBy.Year) {
//			query.setParameter("entYear", entYearMonth);
//		}

		this.info("L9717ServiceImpl inputs:");
		this.info("entYearMonth: " + entYearMonth);
		this.info("ovduTermMin: " + titaVo.getParam("inputOverdueTermMin"));
		this.info("ovduTermMax: " + titaVo.getParam("inputOverdueTermMax"));
		this.info("businessOfficer: '" + (titaVo.getParam("inputBusinessOfficer").trim().isEmpty() ? " " : titaVo.getParam("inputBusinessOfficer")) + "'");
		this.info("entYear: " + entYearMonth);

		return this.convertToMap(query);
	}

}