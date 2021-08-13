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
public class LM002ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM002ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> doQuery(TitaVo titaVo) throws Exception {
		logger.info("lM002.doQuery");

		
		// 資料前兩年一月起至當月
		String startYM = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19090000).substring(0, 4) + "01";
		String endYM = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000).substring(0, 6);
	

		String sql = "";
		sql += "	SELECT S.\"F1\" AS F0";
		sql += "	  	  ,S.\"F2\" AS F1";
		sql += "	  	  ,S.\"F3\" AS F2";
		sql += "	  	  ,SUM(S.\"F4\") AS F3";
		sql += "	FROM( ";
		sql += "	SELECT TRUNC(M.\"YearMonth\" / 100) AS F1 ";
		sql += "	  	  ,1 AS F2 ";
		sql += "	  	  ,MOD(M.\"YearMonth\",100) AS F3 ";
		sql += "	  	  ,M.\"LoanBalance\" AS F4 ";
		sql += "	FROM \"MonthlyLoanBal\" M ";
		sql += "	WHERE M.\"YearMonth\" BETWEEN :startYM AND :endYM ";
		sql += "  	  AND M.\"ProdNo\" IN ('81','82','83') ";
                sql += "          AND M.\"AcctCode\" != '990' ";
		sql += "	UNION ALL ";
		sql += "	SELECT TRUNC(M.\"YearMonth\" / 100) AS F1 ";
		sql += "	  	  ,2 AS F2 ";
		sql += "	  	  ,MOD(M.\"YearMonth\",100) AS F3 ";
		sql += "	  	  ,M.\"LoanBalance\" AS F4 ";
		sql += "	FROM \"MonthlyLoanBal\" M ";
                sql += "        LEFT JOIN \"FacProd\" FP ON FP.\"ProdNo\" = M.\"ProdNo\" ";
                sql += "                                AND NVL(FP.\"GovOfferFlag\", 'N') = 'Y' ";
		sql += "	WHERE M.\"YearMonth\" BETWEEN :startYM AND :endYM ";
		sql += "  	  AND FP.\"ProdNo\" IS NOT NULL";
                sql += "          AND M.\"AcctCode\" != '990' ";
		sql += "	UNION ALL ";
		sql += "	SELECT TRUNC(M.\"YearMonth\" / 100) AS F1 ";
		sql += "	  	  ,3 AS F2 ";
		sql += "	  	  ,MOD(M.\"YearMonth\",100) AS F3 ";
		sql += "	  	  ,M.\"LoanBalance\" AS F4 ";
		sql += "	FROM \"MonthlyLoanBal\" M ";
		sql += "	WHERE M.\"YearMonth\" BETWEEN :startYM AND :endYM ";
		sql += "  	  AND M.\"AcctCode\" = '340' ";
		sql += "	UNION ALL ";
		sql += "	SELECT TRUNC(M.\"YearMonth\" / 100) AS F1 ";
		sql += "	  	  ,4 AS F2 ";
		sql += "	  	  ,MOD(M.\"YearMonth\",100) AS F3 ";
		sql += "	  	  ,M.\"LoanBalance\" AS F4 ";
		sql += "	FROM \"MonthlyLoanBal\" M ";
                sql += "        LEFT JOIN \"FacProd\" FP ON FP.\"ProdNo\" = M.\"ProdNo\" ";
                sql += "                                AND NVL(FP.\"GovOfferFlag\", 'N') = 'Y' ";
		sql += "	WHERE M.\"YearMonth\" BETWEEN :startYM AND :endYM ";
		sql += "  	  AND M.\"AcctCode\" = '990' ";
		sql += "  	  AND (    M.\"FacAcctCode\" = '340' ";
		sql += "            OR FP.\"ProdNo\" IS NOT NULL) ";
		sql += "	  ) S ";
		sql += "	GROUP BY S.\"F1\",S.\"F2\",S.\"F3\" ";

		logger.info("sql=" + sql);

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("startYM", startYM);
		query.setParameter("endYM", endYM);

		return this.convertToMap(query.getResultList());
	}


}