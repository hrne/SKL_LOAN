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
public class LM039ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * @return 
	 * @throws Exception 
	 * 
	 */
	public List<Map<String, String>> findAll(TitaVo titaVo, int yearMonth) throws Exception {

//		int yearMonth = parse.stringToInteger(titaVo.get("ENTDY")) / 100 + 191100;
		this.info("yearMonth=" + yearMonth);
		this.info("lM039.findAll ");

		String sql = "SELECT CASE WHEN B.\"AcctSource\" = 'A' THEN B.\"AcctSource\"";
		sql += "             ELSE '' END AS C1";
		sql += "            ,O.\"CustNo\"";
		sql += "            ,O.\"FacmNo\"";
		sql += "            ,O.\"BormNo\"";
		sql += "            ,\"Fn_ParseEOL\"(C.\"CustName\",0) AS \"CustName\"";
		sql += "            ,F.\"FirstDrawdownDate\"";
		sql += "            ,L.\"PrevPayIntDate\"";
		sql += "            ,O.\"OvduBal\"";
		sql += "            ,O.\"OvduDate\"";
		sql += "            ,O.\"OvduPrinAmt\"";
		sql += "            ,O.\"OvduIntAmt\"";
		sql += "            ,O.\"OvduAmt\" - O.\"OvduBal\" - O.\"BadDebtAmt\" AS C12";
		sql += "            ,M.\"YearMonth\"";
		sql += "            ,M.\"CityCode\"";
		sql += "            ,M.\"ClCode1\"";
		sql += "            ,M.\"ClCode2\"";
		sql += "            ,CB.\"BdLocation\"";
		sql += "            ,M.\"AcctCode\"";
		sql += "            ,C.\"EntCode\"";
		sql += "            ,C.\"CustTypeCode\"";
		sql += "            ,M.\"FacAcctCode\"";
		sql += "            ,F.\"MaturityDate\"";
		sql += "            ,M.\"StoreRate\"";
		sql += "            ,F.\"UtilAmt\"";
		sql += "            ,F.\"LineAmt\"";
//		sql += "            ,F.\"UtilBal\"";
		sql += "            ,L.\"DrawdownAmt\"";
		sql += "            ,F.\"CreditOfficer\"";
//		sql += "            ,F.\"LoanOfficer\"";
		sql += "            ,F.\"BusinessOfficer\"";
		sql += "            ,Li.\"AccCollPsn\"";
		sql += "            ,F.\"UsageCode\"";
		sql += "            ,F.\"ProdNo\"";
		sql += "            ,E.\"Fullname\"";
		sql += "      FROM \"LoanOverdue\" O";
		sql += "      LEFT JOIN \"MonthlyLoanBal\" M ON M.\"YearMonth\" = :entdy";
		sql += "                                    AND M.\"CustNo\"    = O.\"CustNo\"";
		sql += "                                    AND M.\"FacmNo\"    = O.\"FacmNo\"";
		sql += "                                    AND M.\"BormNo\"    = O.\"BormNo\"";
//		sql += "      LEFT JOIN \"DailyLoanBal\" D   ON D.\"MonthEndYm\" = :entdy";
//		sql += "                                    AND D.\"CustNo\"    = O.\"CustNo\"";
//		sql += "                                    AND D.\"FacmNo\"    = O.\"FacmNo\"";
//		sql += "                                    AND D.\"BormNo\"    = O.\"BormNo\"";
		sql += "      LEFT JOIN \"MonthlyFacBal\" MF ON MF.\"YearMonth\" = :entdy";
		sql += "                                    AND MF.\"CustNo\"    = O.\"CustNo\"";
		sql += "                                    AND MF.\"FacmNo\"    = O.\"FacmNo\"";
		sql += "      LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" = O.\"CustNo\"";
		sql += "                                 AND L.\"FacmNo\" = O.\"FacmNo\"";
		sql += "                                 AND L.\"BormNo\" = O.\"BormNo\"";
		sql += "      LEFT JOIN \"CdAcBook\" B ON B.\"AcSubBookCode\" = MF.\"AcSubBookCode\"";
		sql += "      LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = O.\"CustNo\"";
		sql += "      LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = O.\"CustNo\"";
		sql += "                             AND F.\"FacmNo\" = O.\"FacmNo\"";
		sql += "      LEFT JOIN \"ClBuilding\" CB ON CB.\"ClCode1\" = M.\"ClCode1\"";
		sql += "                                 AND CB.\"ClCode2\" = M.\"ClCode2\"";
		sql += "                                 AND CB.\"ClNo\"    = M.\"ClNo\"";
		sql += "      LEFT JOIN \"CollList\" Li ON Li.\"CustNo\" = O.\"CustNo\"";
		sql += "                               AND Li.\"FacmNo\" = O.\"FacmNo\"";
		sql += "      LEFT JOIN \"CdEmp\" E ON E.\"EmployeeNo\" = Li.\"AccCollPsn\"";
		sql += "      WHERE O.\"Status\" IN (1, 2)";
		sql += "        AND M.\"AcctCode\" = '990'";
		sql += "        AND M.\"LoanBalance\" > 0";
		sql += "      ORDER BY O.\"CustNo\", O.\"FacmNo\", O.\"BormNo\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("entdy", yearMonth);

		return this.convertToMap(query);
	}

}