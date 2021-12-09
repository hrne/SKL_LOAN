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
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service
@Repository
/* 逾期放款明細 */
public class L9714ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("L9714.findAll");

		String iCUSTNO = titaVo.get("CustNo");
		String iFACMNO = titaVo.get("FacmNo");
		String iUSEFG = titaVo.get("UsageCode");
		String iSDAY = String.valueOf(Integer.valueOf(titaVo.get("ACCTDATE_ST")) + 19110000);
		String iEDAY = String.valueOf(Integer.valueOf(titaVo.get("ACCTDATE_ED")) + 19110000);

		String sql = "SELECT \"Fn_ParseEOL\"(C.\"CustName\",0) F0";
		sql += "            ,C.\"CustId\" F1";
		sql += "            ,F.\"CustNo\" F2";
		sql += "            ,F.\"FacmNo\" F3";
		sql += "            ,F.\"DrawdownAmt\" F4";
		sql += "            ,F.\"FirstDrawdownDate\" F5";
		sql += "            ,F.\"MaturityDate\" F6";
		sql += "            ,F.\"LoanBal\" F7";
		sql += "            ,T.\"UsageCode\" F8";
		sql += "            ,NVL(T.\"Interest\", 0) F9";
		sql += "      FROM(SELECT F.\"CustNo\"";
		sql += "                 ,F.\"FacmNo\"";
		sql += "                 ,F.\"FirstDrawdownDate\"";
		sql += "                 ,F.\"MaturityDate\"";
		sql += "                 ,SUM(M.\"DrawdownAmt\") \"DrawdownAmt\"";
		sql += "                 ,SUM(M.\"LoanBal\") \"LoanBal\"";
		sql += "           FROM \"FacMain\" F";
		sql += "           LEFT JOIN \"LoanBorMain\" M ON M.\"CustNo\" = F.\"CustNo\"";
		sql += "                                      AND M.\"FacmNo\" = F.\"FacmNo\"";
		sql += "           WHERE F.\"CustNo\" = :icustno";
		if (!iFACMNO.equals("000")) {
			sql += " AND F.\"FacmNo\" = :ifacmno";
		}
		sql += "           GROUP BY F.\"CustNo\"";
		sql += "                   ,F.\"FacmNo\"";
		sql += "                   ,F.\"FirstDrawdownDate\"";
		sql += "                   ,F.\"MaturityDate\" ";
		sql += "          ) F";
		sql += "      LEFT JOIN (SELECT T.\"CustNo\"";
		sql += "                       ,T.\"FacmNo\"";
		sql += "                       ,M.\"UsageCode\"";
		sql += "                       ,SUM(T.\"Interest\") \"Interest\"";
		sql += "                 FROM (SELECT T.\"CustNo\"";
		sql += "                             ,T.\"FacmNo\"";
		sql += "                             ,T.\"BormNo\"";
		sql += "                             ,T.\"Interest\" + T.\"DelayInt\" + T.\"BreachAmt\" - T.\"UnpaidInterest\" - T.\"UnpaidCloseBreach\" - NVL(JSON_VALUE(T.\"OtherFields\",'$.ReduceAmt'),0) \"Interest\"";
		sql += "                       FROM \"LoanBorTx\" T";
		sql += "                       WHERE T.\"AcDate\" >= :isday";
		sql += "                         AND T.\"AcDate\" <= :ieday";
		sql += "                         AND T.\"CustNo\" = :icustno";
		if (!iFACMNO.equals("000")) {
			sql += " AND T.\"FacmNo\" = :ifacmno";
		}
		sql += "                         AND T.\"TitaHCode\" = '0'";
		sql += "                      ) T";
		sql += "                 LEFT JOIN \"LoanBorMain\" M ON M.\"CustNo\" = T.\"CustNo\"";
		sql += "                                            AND M.\"FacmNo\" = T.\"FacmNo\"";
		sql += "                                            AND M.\"BormNo\" = T.\"BormNo\"";
		sql += "                 WHERE T.\"Interest\" > 0 ";
		if (!iUSEFG.equals("00")) {
			sql += " AND M.\"UsageCode\" =  :iusefg";
		}
		sql += "                 GROUP BY T.\"CustNo\"";
		sql += "                         ,T.\"FacmNo\"";
		sql += "                         ,M.\"UsageCode\"";
		sql += "                ) T ON T.\"CustNo\" = F.\"CustNo\"";
		sql += "                   AND T.\"FacmNo\" = F.\"FacmNo\"";
		sql += "      LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = F.\"CustNo\"";
		sql += "      WHERE NVL(T.\"Interest\", 0) > 0";
		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		query = em.createNativeQuery(sql);
		query.setParameter("icustno", iCUSTNO);
		query.setParameter("isday", iSDAY);
		query.setParameter("ieday", iEDAY);
		if (!iFACMNO.equals("000")) {
			query.setParameter("ifacmno", iFACMNO);
		}
		if (!iUSEFG.equals("00")) {
			query.setParameter("iusefg", iUSEFG);
		}
		return this.convertToMap(query.getResultList());
	}

}