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
public class LM047ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("lM047.findAll ");
		String sql = " ";
		sql += " WITH TEMP AS (															";
		sql += "	SELECT l.*															";
		sql += " 	FROM \"FacMain\" f													"; 
		sql += " 	LEFT JOIN \"CollList\" l ON l.\"CustNo\" = f.\"CustNo\"				"; 
		sql += " 							 AND l.\"FacmNo\" =  f.\"FacmNo\"			";
		sql += "    WHERE f.\"ProdNo\"  IN ('60','61','62')		                    	";
		sql += " )																		";
		sql += " ,  OVDU AS (															";
		sql += "      SELECT  		T.\"CustNo\" ";
		sql += "                   ,T.\"FacmNo\" ";
		sql += "                   ,SUM(\"OvduPrinAmt\") 						 AS \"OvduPrinAmt\" ";
		sql += "                   ,SUM(O.\"OvduIntAmt\" + O.\"OvduBreachAmt\")  AS \"OvduIntAmt\" ";
		sql += "                   ,SUM(O.\"BadDebtAmt\") 						 AS \"BadDebtAmt\" ";
		sql += "      FROM TEMP T ";
		sql += "      LEFT JOIN \"LoanOverdue\" O ON O.\"CustNo\" = T.\"CustNo\" ";
		sql += "                                       AND O.\"FacmNo\" = T.\"FacmNo\" ";
		sql += "                                       AND O.\"Status\"  IN (1, 2) 	";
		sql += " 	  GROUP BY 	T.\"CustNo\" , T.\"FacmNo\"								";
		sql += " )																		";
		sql += " , LAW	AS (  ";
		sql += " SELECT L.\"CustNo\" ";
		sql += "        ,L.\"FacmNo\" ";
		sql += "        ,SUM(CASE WHEN L.\"LegalProg\" IN ('058', '061') THEN L.\"Amount\" ";
		sql += "                                 ELSE 0 END) \"Amount1\" ";
		sql += "                       ,SUM(CASE WHEN L.\"LegalProg\" = '901' ";
		sql += "                                  AND L.\"SEQ\" = 1 ";
		sql += "                                 THEN L.\"Amount\" ";
		sql += "                            ELSE 0 END) \"Amount2\" ";
		sql += "                       ,SUM(CASE WHEN L.\"LegalProg\" = '077' ";
		sql += "                                  AND L.\"SEQ\" = 1 ";
		sql += "                                 THEN L.\"Amount\" ";
		sql += "                            ELSE 0 END) \"Amount3\" ";
		sql += "                       ,MAX(CASE WHEN L.\"LegalProg\" = '077' ";
		sql += "                                  AND L.\"SEQ\" = 1 ";
		sql += "                                 THEN L.\"Memo\" ";
		sql += "                            ELSE NULL END) \"Memo1\" ";
		sql += "                       ,MAX(CASE WHEN L.\"LegalProg\" < '900' ";
		sql += "                                 THEN TO_CHAR(L.\"RecordDate\"-19110000) || NVL(L.\"Memo\",L.\"Item\") ";
		sql += "                            ELSE NULL END) \"Memo2\" ";
		sql += "                 FROM (SELECT T.\"CustNo\" ";
		sql += "                             ,T.\"FacmNo\" \"FacmNo\"";
		sql += "                             ,L.\"LegalProg\" ";
		sql += "                             ,L.\"RecordDate\" ";
		sql += "                             ,L.\"Amount\" ";
		sql += "                             ,L.\"Memo\" ";
		sql += "							 ,C.\"Item\" ";
		sql += "                             ,ROW_NUMBER() OVER (PARTITION BY L.\"CustNo\" ";
		sql += "                                                             ,L.\"FacmNo\" ";
		sql += "                                                             ,L.\"LegalProg\" ";
		sql += "                                                              ORDER BY L.\"RecordDate\" DESC) AS SEQ ";
		sql += "                       FROM TEMP T ";
		sql += "                       LEFT JOIN \"CollLaw\" L ON L.\"CaseCode\" = '1' ";
		sql += "                                              AND L.\"CustNo\"   =  T.\"CustNo\" ";
		sql += "                                              AND L.\"FacmNo\"   =  T.\"FacmNo\" ";
		sql += "					   LEFT JOIN \"CdCode\" C ON C.\"DefCode\" = 'LegalProg' ";
		sql += "											 AND C.\"Code\"  = L.\"LegalProg\" ";
		sql += "                      	) L														 ";
		sql += "                 GROUP BY L.\"CustNo\", L.\"FacmNo\" ) 							 ";
		sql += " , DATA AS ( ";
		sql += " 	SELECT DISTINCT T.\"CustNo\"  ";
		sql += " 					,T.\"FacmNo\" ";
		sql += " 					,T.\"Status\" ";
		sql += " 					,T.\"LegalPsn\" ";
		sql += " 					,T.\"PrevIntDate\" ";
		sql += " 	FROM TEMP T ";
		sql += "	WHERE T.\"Status\" IN (0,2,4,7)   ";
		sql += " 	UNION 			";
		sql += " 	SELECT DISTINCT T.\"CustNo\"  ";
		sql += " 					,T.\"FacmNo\" ";
		sql += " 					,T.\"Status\" ";
		sql += " 					,T.\"LegalPsn\" ";
		sql += " 					,T.\"PrevIntDate\" ";
		sql += " 	FROM TEMP T ";
		sql += "    LEFT JOIN \"AcLoanRenew\" N ON N.\"CustNo\" = T.\"CustNo\" ";
		sql += " 								AND N.\"NewFacmNo\"  = T.\"FacmNo\" ";
		sql += " 								AND N.\"NewFacmNo\"  <> N.\"OldFacmNo\" ";
		sql += " 								AND T.\"Status\" NOT IN (0,2,4,7) ";
		sql += "    LEFT JOIN \"TEMP\" O ON O.\"CustNo\" = N.\"CustNo\" ";
		sql += " 						AND O.\"FacmNo\"  = N.\"OldFacmNo\" ";
		sql += "	WHERE T.\"Status\" IN (0,2,4,7)   ";
		sql += " ) ";
		sql += " SELECT 	 D.\"CustNo\"                                                           		F0";
		sql += "            ,D.\"FacmNo\"                                                        F1";
		sql += "            ,\"Fn_ParseEOL\"(C.\"CustName\", 0)                                     F2";
		sql += "            ,F.\"LineAmt\"                                                          F3";
		sql += "            ,M.\"Status\"                                                           F4";
		sql += "            ,O.\"OvduPrinAmt\"                                                      F5";
		sql += "            ,O.\"OvduIntAmt\"                                                       F6";
		sql += "            ,O.\"BadDebtAmt\"                                                       F7";
		sql += "            ,L.\"Amount1\"                                                          F8";
		sql += "            ,L.\"Amount2\"                                                          F9";
		sql += "            ,L.\"Amount3\"                                                          F10";
		sql += "            ,NVL(L.\"Memo1\", '')                                                   F11";
		sql += "            ,CASE WHEN NVL(L.\"Amount2\", 0) = 0                                    ";
		sql += "                  THEN '有'                                                        							 ";
		sql += "             ELSE '無' END                                                          F12";
		sql += "            ,M.\"LoanTermYy\" || ';' || M.\"LoanTermMm\" || ';' || M.\"LoanTermDd\" F13";
		sql += "            ,M.\"DrawdownDate\"                                                     F14";
		sql += "            ,M.\"MaturityDate\"                                                     F15";
		sql += "            ,M.\"TotalPeriod\"                                                      F16";
		sql += "            ,M.\"RepaidPeriod\"                                                     F17";
		sql += "            ,M.\"PaidTerms\"                                                        F18";
		sql += "            ,CASE WHEN NVL(L.\"Memo2\", ' ') = ' ' THEN M.\"CompensateAcct\"			";
		sql += "    			ELSE L.\"Memo2\" || ' ' || M.\"CompensateAcct\"   END 			AS  F19";
		sql += "            ,E.\"Fullname\"                                                         F20";
		sql += "      FROM DATA D ";
		sql += "      LEFT JOIN LAW L ON L.\"CustNo\" = D.\"CustNo\"  ";
		sql += "                     AND L.\"FacmNo\" = D.\"FacmNo\"";
		sql += "      LEFT JOIN OVDU O ON O.\"CustNo\" = D.\"CustNo\"  ";
		sql += "                      AND O.\"FacmNo\" = D.\"FacmNo\"";
		sql += "      LEFT JOIN \"AcLoanRenew\" R ON R.\"CustNo\" = D.\"CustNo\"";
		sql += "                                 AND R.\"NewFacmNo\" = D.\"FacmNo\"";
		sql += "              					 AND R.\"MainFlag\"  = 'Y'  ";
		sql += "      LEFT JOIN \"LoanBorMain\" M ON M.\"CustNo\" = R.\"CustNo\" ";
		sql += "                                 AND M.\"FacmNo\" = R.\"NewFacmNo\" ";
		sql += "                                 AND M.\"BormNo\" = R.\"NewBormNo\" ";
		sql += "      LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = D.\"CustNo\"";
		sql += "                             AND F.\"FacmNo\" = D.\"FacmNo\"";
		sql += "      LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = D.\"CustNo\"";
		sql += "      LEFT JOIN \"CdEmp\" E ON E.\"EmployeeNo\" = D.\"LegalPsn\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

}