package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class LM039ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM039ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		String entdy = String.valueOf((Integer.valueOf(titaVo.get("ENTDY").toString()) + 19110000) / 100);
		logger.info("lM039.findAll ");
		String sql = "SELECT CASE WHEN B.\"AcctSource\" = 'A' THEN B.\"AcctSource\"";
		sql += "             ELSE '' END AS C1";
		sql += "            ,O.\"CustNo\"";
		sql += "            ,O.\"FacmNo\"";
		sql += "            ,O.\"BormNo\"";
		sql += "            ,C.\"CustName\"";
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
		sql += "            ,F.\"UtilBal\"";
		sql += "            ,F.\"CreditOfficer\"";
		sql += "            ,F.\"LoanOfficer\"";
		sql += "            ,Li.\"AccCollPsn\"";
		sql += "            ,F.\"UsageCode\"";
		sql += "            ,F.\"ProdNo\"";
		sql += "            ,E.\"Fullname\"";
		sql += "      FROM \"LoanOverdue\" O";
		sql += "      LEFT JOIN \"MonthlyLoanBal\" M ON M.\"YearMonth\" = :entdy";
		sql += "                                    AND M.\"CustNo\"    = O.\"CustNo\"";
		sql += "                                    AND M.\"FacmNo\"    = O.\"FacmNo\"";
		sql += "                                    AND M.\"BormNo\"    = O.\"BormNo\"";
		sql += "      LEFT JOIN \"MonthlyFacBal\" MF ON MF.\"YearMonth\" = :entdy";
		sql += "                                    AND MF.\"CustNo\"    = O.\"CustNo\"";
		sql += "                                    AND MF.\"FacmNo\"    = O.\"FacmNo\"";
		sql += "      LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" = O.\"CustNo\"";
		sql += "                                 AND L.\"FacmNo\" = O.\"FacmNo\"";
		sql += "                                 AND L.\"BormNo\" = O.\"BormNo\"";
		sql += "      LEFT JOIN \"CdAcBook\" B ON B.\"AcBookCode\" = MF.\"AcBookCode\"";
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
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entdy", entdy);
		return this.convertToMap(query.getResultList());
	}

}