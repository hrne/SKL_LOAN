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
public class LM003ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM003ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		logger.info("lM003.findAll ");
		String sql = "";
		sql += " SELECT S1.\"DataYear\" ";
		sql += "       ,S1.\"DataMonth\" ";
		sql += "       ,SUM(S1.\"DrawdownAmt\")       AS \"DrawdownAmt\" ";
		sql += "       ,SUM(S1.\"CloseLoan\")         AS \"CloseLoan\" ";
		sql += "       ,SUM(S1.\"CloseSale\")         AS \"CloseSale\" ";
		sql += "       ,SUM(S1.\"CloseSelfRepay\")    AS \"CloseSelfRepay\" ";
		sql += "       ,SUM(S1.\"ExtraRepay\")        AS \"ExtraRepay\" ";
		sql += "       ,SUM(S1.\"PrincipalAmortize\") AS \"PrincipalAmortize\" ";
		sql += "       ,SUM(S1.\"Collection\")        AS \"Collection\" ";
		sql += "       ,SUM(S1.\"LoanBalance\")       AS \"LoanBalance\" ";
		sql += "       ,NVL(SUM(S2.\"TotalBalance\"),0) AS \"TotalBalance\" ";
		sql += "       ,NVL(SUM(S3.\"TotalBalance\"),0) AS \"EntTotalBalance\" ";
		sql += "       ,NVL(SUM(S4.\"EntNatRepay\"),0) AS \"EntNatRepay\" ";
		sql += " FROM ( SELECT \"DataYear\" ";
		sql += "              ,\"DataMonth\" ";
		sql += "              ,\"DrawdownAmt\" ";
		sql += "              ,\"CloseLoan\" ";
		sql += "              ,\"CloseSale\" ";
		sql += "              ,\"CloseSelfRepay\" ";
		sql += "              ,\"ExtraRepay\" ";
		sql += "              ,\"PrincipalAmortize\" ";
		sql += "              ,\"Collection\" ";
		sql += "              ,\"LoanBalance\" ";
		sql += "              ,\"EntType\" ";
		sql += "        FROM \"MonthlyLM003\" ";
		sql += "      ) S1 ";
		sql += " LEFT JOIN ( SELECT \"YearMonth\" ";
		sql += "                  , SUM(\"LoanBalance\") AS \"TotalBalance\" ";
		sql += "             FROM \"MonthlyLoanBal\" ";
		sql += "             GROUP BY \"YearMonth\" ) S2 ON S2.\"YearMonth\" = S1.\"DataYear\" || LPAD(S1.\"DataMonth\",2,0) ";
		sql += " LEFT JOIN ( SELECT \"YearMonth\" ";
		sql += "                  , \"EntCode\" ";
		sql += "                  , SUM(\"LoanBalance\") AS \"TotalBalance\" ";
		sql += "             FROM \"MonthlyLoanBal\" ";
		sql += "             GROUP BY \"YearMonth\",\"EntCode\" ) S3 ON S3.\"YearMonth\" = S1.\"DataYear\" || LPAD(S1.\"DataMonth\",2,0) ";
		sql += "                                                AND S3.\"EntCode\" = '1' ";
		sql += " LEFT JOIN ( SELECT \"DataYear\" ";
		sql += "                  , \"DataMonth\" ";
		sql += "                  , \"EntType\" ";
		sql += "                  , SUM(\"CloseLoan\") ";
		sql += "                   +SUM(\"CloseSale\") ";
		sql += "                   +SUM(\"CloseSelfRepay\") ";
		sql += "                   +SUM(\"ExtraRepay\") ";
		sql += "                   +SUM(\"PrincipalAmortize\") ";
		sql += "                   +SUM(\"Collection\") AS \"EntNatRepay\" ";
		sql += "             FROM \"MonthlyLM003\" ";
		sql += "             GROUP BY \"DataYear\",\"DataMonth\",\"EntType\" ) S4 ON S4.\"DataYear\" = S1.\"DataYear\" ";
		sql += "                                                           AND S4.\"DataMonth\" = S1.\"DataMonth\" ";
		sql += "                                                           AND S4.\"EntType\" = '2' ";
		sql += "                                                           AND S1.\"EntType\" != '2' ";
		sql += " WHERE S1.\"DataYear\" || LPAD(S1.\"DataMonth\",2,0) BETWEEN :inputDateStart AND :inputDateEnd ";
		sql += "   AND S1.\"DataMonth\" != '0' ";
		sql += " GROUP BY S1.\"DataYear\",S1.\"DataMonth\" ";
		sql += " ORDER BY S1.\"DataYear\",S1.\"DataMonth\" ";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		
		query.setParameter("inputDateStart", Integer.parseInt(titaVo.getParam("inputDateStart")) + 191100);
		query.setParameter("inputDateEnd", Integer.parseInt(titaVo.getParam("inputDateEnd")) + 191100);
		
		return this.convertToMap(query.getResultList());
	}

}