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
public class LM018ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("lM018.findAll ");

		String entdy = String.valueOf((Integer.valueOf(titaVo.getParam("ENTDY")) + 19110000) / 100);

		String sql = "";
		sql += " SELECT amt.\"ProdNo\" ";
		sql += "       ,VM.\"ViableMonth\" ";
		sql += "       ,MAX(amt.\"BalSum\") \"BalSum\" ";
		sql += "       ,SUM(int.\"IntSum\") \"IntSum\" ";
		sql += " FROM ( SELECT UNIQUE \"YearMonth\" ";
		sql += "                     ,CASE WHEN :entYear * 100 + CEIL(MOD(\"YearMonth\", 100) / 3) * 3 < TO_NUMBER(:entdy) ";
		sql += "                           THEN :entYear * 100 + CEIL(MOD(\"YearMonth\", 100) / 3) * 3 ";
		sql += "                      ELSE TO_NUMBER(:entdy) END \"ViableMonth\" ";
		sql += "                     ,:entYear * 100 + CEIL(MOD(\"YearMonth\",100) / 3) * 3 \"ActualSeason\" ";
		sql += "        FROM \"MonthlyLoanBal\" ";
		sql += "        WHERE \"YearMonth\" BETWEEN :entYear || '01' AND :entdy ";
		sql += "      ) VM ";
		sql += " LEFT JOIN ( SELECT \"YearMonth\" ";
		sql += "                   ,CASE WHEN \"AcctCode\" = '340' ";
		sql += "                         THEN 'AA' ";
		sql += "                    ELSE DECODE(\"ProdNo\", 'IE', 'ID', 'IG', 'IF', 'II', 'IH', '81', 'ZZ', '82', 'ZZ', '83', 'ZZ', \"ProdNo\") END \"ProdNo\" ";
		sql += "                   ,SUM(\"LoanBalance\") \"BalSum\" ";
		sql += "             FROM \"MonthlyLoanBal\" ";
		sql += "             WHERE ( (     \"ProdNo\"   IN ('IA','IB','IC','ID','IE','ID','IG','IF','II','IH','81','ZZ','82','ZZ','83','ZZ') ";
		sql += "                       AND \"AcctCode\" IN ('310','320','330') ) ";
		sql += "                     OR ";
		sql += "                     ( \"AcctCode\" = '340') ";
		sql += "                   ) ";
		sql += "             GROUP BY \"YearMonth\" ";
		sql += "                     ,CASE WHEN \"AcctCode\" = '340' ";
		sql += "                           THEN 'AA' ";
		sql += "                      ELSE DECODE(\"ProdNo\", 'IE', 'ID', 'IG', 'IF', 'II', 'IH', '81', 'ZZ', '82', 'ZZ', '83', 'ZZ', \"ProdNo\") END ";
		sql += "           ) amt ON amt.\"YearMonth\" = VM.\"YearMonth\" ";
		sql += "                AND amt.\"YearMonth\" = VM.\"ViableMonth\" ";
		sql += " LEFT JOIN ( SELECT \"YearMonth\" ";
		sql += "                   ,CASE WHEN \"AcctCode\" = '340' ";
		sql += "                         THEN 'AA' ";
		sql += "                    ELSE DECODE(\"ProdNo\", 'IE', 'ID', 'IG', 'IF', 'II', 'IH', '81', 'ZZ', '82', 'ZZ', '83', 'ZZ', \"ProdNo\") END \"ProdNo\" ";
		sql += "                   ,SUM(\"IntAmtRcv\") \"IntSum\" ";
		sql += "             FROM \"MonthlyLoanBal\" ";
		sql += "             WHERE \"YearMonth\" LIKE :entYear || '%' ";
		sql += "             GROUP BY \"YearMonth\" ";
		sql += "                     ,CASE WHEN \"AcctCode\" = '340' ";
		sql += "                           THEN 'AA' ";
		sql += "                      ELSE DECODE(\"ProdNo\", 'IE', 'ID', 'IG', 'IF', 'II', 'IH', '81', 'ZZ', '82', 'ZZ', '83', 'ZZ', \"ProdNo\") END ";
		sql += "           ) int ON FLOOR(int.\"YearMonth\" / 100) * 100 + CEIL(MOD(int.\"YearMonth\", 100) / 3) * 3 <= VM.\"ActualSeason\" ";
		sql += "                AND int.\"ProdNo\" = amt.\"ProdNo\" ";
		sql += " WHERE amt.\"ProdNo\" is not null ";
		sql += " GROUP BY vm.\"ViableMonth\" ";
		sql += "         ,amt.\"ProdNo\" ";
		sql += " ORDER BY vm.\"ViableMonth\" ";
		sql += "         ,amt.\"ProdNo\" ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entdy", entdy);
		query.setParameter("entYear", entdy.substring(0,4));
		return this.convertToMap(query.getResultList());
	}

}