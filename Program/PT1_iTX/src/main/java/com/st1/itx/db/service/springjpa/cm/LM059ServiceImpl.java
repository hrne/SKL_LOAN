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
/* 逾期放款明細 */
public class LM059ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}
	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * 
	 */
	public List<Map<String, String>> findAll(TitaVo titaVo, int yearMonth) throws Exception {

		

		String iYearMonth = String.valueOf(yearMonth);

		this.info("lM059.findAll YYMM=" + iYearMonth);

		String sql = "SELECT ROUND(SUM(NVL(D.\"LoanBalance\",0)),0)                  AS F0";
		sql += "            ,SUM(NVL(D.\"TdBal\",0))                                 AS F1";
		sql += "            ,SUM(NVL(D.\"LoanBalance\",0)) - SUM(NVL(D.\"TdBal\",0)) AS F2";
		sql += "      FROM(SELECT 1 AS \"Group\"";
		sql += "                 ,NVL(I.\"BookValue\",M.\"LoanBalance\") AS \"LoanBalance\"";
		sql += "                 ,0 AS \"TdBal\"";
		sql += "           FROM \"MonthlyLoanBal\" M ";
		sql += "           LEFT JOIN \"Ias39IntMethod\" I ON I.\"CustNo\" = M.\"CustNo\"";
		sql += "                                         AND I.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                         AND I.\"BormNo\" = M.\"BormNo\"";
		sql += "                                         AND I.\"YearMonth\" = M.\"YearMonth\"";
		sql += "           WHERE M.\"YearMonth\" = :yymm";
		sql += "           UNION ALL";
		sql += "           SELECT 1 AS \"Group\"";
		sql += "                 ,0 AS \"LoanBalance\"";
		sql += "                 ,\"LegalLoss\" AS \"TdbBal\"";
		sql += "           FROM \"MonthlyLM052Loss\" ";
		sql += "           WHERE \"YearMonth\" = :yymm";
//		sql += "           UNION ALL";
//		sql += "           SELECT 1 AS \"Group\"";
//		sql += "                 ,0 AS \"LoanBalance\"";
//		sql += "                 ,A.\"TdBal\"";
//		sql += "           FROM \"AcMain\" A";
//		sql += "           WHERE A.\"MonthEndYm\" = :yymm";
//		sql += "             AND A.\"AcctCode\" IN ('F18')"; // 備抵呆帳－催收款項－放款部 2021/2/4
		sql += "          ) D";
		sql += "      GROUP BY D.\"Group\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", iYearMonth);

		return this.convertToMap(query);
	}

}