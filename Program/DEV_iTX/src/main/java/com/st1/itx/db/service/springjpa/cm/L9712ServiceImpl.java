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

@Service("l9712ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L9712ServiceImpl extends ASpringJpaParm implements InitializingBean {

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

		this.info("L9712.findAll");

		String iAcDateMin = String.valueOf(Integer.valueOf(titaVo.get("AcDateMin")) + 19110000);
		String iAcDateMax = String.valueOf(Integer.valueOf(titaVo.get("iAcDateMax")) + 19110000);

		String sql = "SELECT T.\"AcDate\" F0";
		sql += "			,T.\"CustNo\" F1";
		sql += "			,T.\"FacmNo\" F2";
		sql += "			,\"Fn_ParseEOL\"(C.\"CustName\",0) F3";
		sql += "			,T.\"Interest\" F4";
		sql += "			,T.\"BreachAmt\" F5";
		sql += "			,NVL(T.\"ReduceAmt\",0) F7";
		sql += "			,NVL(T.\"ReduceBreachAmt\",0) F7";
		sql += "			,T.\"TitaEmpNoS\" F8";
		sql += "	  FROM(SELECT T.\"AcDate\"";
		sql += "				 ,T.\"CustNo\"";
		sql += "				 ,T.\"FacmNo\"";
		sql += "                 ,SUM(T.\"Interest\") \"Interest\"";
		sql += "			     ,SUM(T.\"DelayInt\") + SUM(T.\"BreachAmt\") \"BreachAmt\"";
		sql += "                 ,SUM(JSON_VALUE(T.\"OtherFields\",'$.ReduceAmt')) \"ReduceAmt\"";
		sql += "                 ,SUM(JSON_VALUE(T.\"OtherFields\",'$.ReduceBreachAmt')) \"ReduceBreachAmt\"";
		sql += "                 ,T.\"TitaEmpNoS\"";
		sql += "		   FROM  \"LoanBorTx\" T ";
		sql += "           WHERE T.\"AcDate\" >= :isday ";
		sql += "             AND T.\"AcDate\" <= :ieday ";
		sql += "             AND  T.\"TitaHCode\" = '0' ";
		sql += "	         AND (JSON_VALUE(T.\"OtherFields\",'$.ReduceAmt') > 0 ";
		sql += "	         OR JSON_VALUE(T.\"OtherFields\",'$.ReduceBreachAmt') > 0) ";
		sql += "		   GROUP BY T.\"AcDate\"";
		sql += "				   ,T.\"CustNo\"";
		sql += "			       ,T.\"FacmNo\"";
		sql += "				   ,T.\"TitaEmpNoS\" ) T";
		sql += "	  LEFT JOIN \"CustMain\" C ON  C.\"CustNo\" = T.\"CustNo\"";
		sql += " 	  ORDER  BY  T.\"AcDate\"";
		sql += "				    ,T.\"CustNo\"";
		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("isday", iAcDateMin);
		query.setParameter("ieday", iAcDateMax);
		return this.convertToMap(query.getResultList());
	}

}