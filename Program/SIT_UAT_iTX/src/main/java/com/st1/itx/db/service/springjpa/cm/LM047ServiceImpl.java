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
		String sql = "SELECT R.\"CustNo\"                                                           F0";
		sql += "            ,R.\"OldFacmNo\"                                                        F1";
		sql += "            ,C.\"CustName\"                                                         F2";
		sql += "            ,F.\"LineAmt\"                                                          F3";
		sql += "            ,M.\"Status\"                                                           F4";
		sql += "            ,R.\"OvduPrinAmt\"                                                      F5";
		sql += "            ,R.\"OvduIntAmt\"                                                       F6";
		sql += "            ,R.\"BadDebtAmt\"                                                       F7";
		sql += "            ,L.\"Amount1\"                                                          F8";
		sql += "            ,L.\"Amount2\"                                                          F9";
		sql += "            ,L.\"Amount3\"                                                          F10";
		sql += "            ,NVL(L.\"Memo1\", '')                                                   F11";
		sql += "            ,CASE WHEN NVL(L.\"Amount2\", 0) = 0                                    ";
		sql += "                  THEN '有'                                                         ";
		sql += "             ELSE '無' END                                                          F12";
		sql += "            ,M.\"LoanTermYy\" || ';' || M.\"LoanTermMm\" || ';' || M.\"LoanTermDd\" F13";
		sql += "            ,M.\"DrawdownDate\"                                                     F14";
		sql += "            ,M.\"MaturityDate\"                                                     F15";
		sql += "            ,M.\"TotalPeriod\"                                                      F16";
		sql += "            ,M.\"RepaidPeriod\"                                                     F17";
		sql += "            ,M.\"PaidTerms\"                                                        F18";
		sql += "            ,NVL(L.\"Memo2\", '')                                                   F19";
		sql += "            ,E.\"Fullname\"                                                         F20";
		sql += "      FROM (SELECT  R.\"CustNo\"";
		sql += "                   ,R.\"NewFacmNo\"";
		sql += "                   ,R.\"NewBormNo\"";
		sql += "                   ,R.\"OldFacmNo\"";
		sql += "                   ,R.\"OldBormNo\"";
		sql += "                   ,O.\"OvduPrinAmt\"";
		sql += "                   ,O.\"OvduIntAmt\" + O.\"OvduBreachAmt\" \"OvduIntAmt\"";
		sql += "                   ,O.\"BadDebtAmt\"";
		sql += "            FROM \"AcLoanRenew\" R";
		sql += "            LEFT JOIN \"LoanOverdue\" O ON O.\"CustNo\" = R.\"CustNo\"";
		sql += "                                       AND O.\"FacmNo\" = R.\"NewFacmNo\"";
		sql += "                                       AND O.\"BormNo\" = R.\"NewBormNo\"";
		sql += "                                       AND O.\"Status\" IN (1, 2)";
		sql += "            WHERE R.\"RenewCode\" = '2'";
		sql += "              AND R.\"MainFlag\"  = 'Y') R";
		sql += "      LEFT JOIN (SELECT L.\"CustNo\"";
		sql += "                       ,L.\"FacmNo\"";
		sql += "                       ,SUM(CASE WHEN L.\"LegalProg\" IN ('058', '061') THEN L.\"Amount\"";
		sql += "                                 ELSE 0 END) \"Amount1\"";
		sql += "                       ,SUM(CASE WHEN L.\"LegalProg\" = '901'";
		sql += "                                  AND L.\"SEQ\" = 1";
		sql += "                                 THEN L.\"Amount\"";
		sql += "                            ELSE 0 END) \"Amount2\"";
		sql += "                       ,SUM(CASE WHEN L.\"LegalProg\" = '077'";
		sql += "                                  AND L.\"SEQ\" = 1";
		sql += "                                 THEN L.\"Amount\"";
		sql += "                            ELSE 0 END) \"Amount3\"";
		sql += "                       ,MAX(CASE WHEN L.\"LegalProg\" = '077'";
		sql += "                                  AND L.\"SEQ\" = 1";
		sql += "                                 THEN L.\"Memo\"";
		sql += "                            ELSE NULL END) \"Memo1\"";
		sql += "                       ,MAX(CASE WHEN L.\"LegalProg\" < '900'";
		sql += "                                 THEN TO_CHAR(L.\"RecordDate\") || L.\"Memo\"";
		sql += "                            ELSE NULL END) \"Memo2\"";
		sql += "                 FROM (SELECT R.\"CustNo\"";
		sql += "                             ,R.\"NewFacmNo\" \"FacmNo\"";
		sql += "                             ,L.\"LegalProg\"";
		sql += "                             ,L.\"RecordDate\"";
		sql += "                             ,L.\"Amount\"";
		sql += "                             ,L.\"Memo\"";
		sql += "                             ,ROW_NUMBER() OVER (PARTITION BY L.\"CustNo\"";
		sql += "                                                             ,L.\"FacmNo\"";
		sql += "                                                             ,L.\"LegalProg\"";
		sql += "                                                              ORDER BY L.\"RecordDate\" DESC) AS SEQ";
		sql += "                       FROM \"AcLoanRenew\" R";
		sql += "                       LEFT JOIN \"CollLaw\" L ON L.\"CaseCode\" = '1'";
		sql += "                                              AND L.\"CustNo\"   =  R.\"CustNo\"";
		sql += "                                              AND L.\"FacmNo\"   =  R.\"NewFacmNo\"";
		sql += "                       WHERE R.\"RenewCode\" = '2'";
		sql += "                         AND R.\"MainFlag\"  = 'Y') L";
		sql += "                 GROUP BY L.\"CustNo\", L.\"FacmNo\") L ON L.\"CustNo\" = R.\"CustNo\"";
		sql += "                                                       AND L.\"FacmNo\" = R.\"NewFacmNo\"";
		sql += "      LEFT JOIN \"LoanBorMain\" M ON M.\"CustNo\" = R.\"CustNo\"";
		sql += "                                 AND M.\"FacmNo\" = R.\"NewFacmNo\"";
		sql += "                                 AND M.\"BormNo\" = R.\"NewBormNo\"";
		sql += "      LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = R.\"CustNo\"";
		sql += "                             AND F.\"FacmNo\" = R.\"OldFacmNo\"";
		sql += "      LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = R.\"CustNo\"";
		sql += "      LEFT JOIN \"CollList\" CL ON CL.\"CustNo\" = R.\"CustNo\"";
		sql += "                               AND CL.\"FacmNo\" = R.\"NewFacmNo\"";
		sql += "      LEFT JOIN \"CdEmp\" E ON E.\"EmployeeNo\" = CL.\"LegalPsn\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

}