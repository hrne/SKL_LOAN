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

/**
 * LM008-應收利息明細表
 * 
 * @author ST1-Wei
 * @version 1.0.0
 */
@Service
@Repository
public class LM008ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	public List<Map<String, String>> findAcctCodeGroup(TitaVo titaVo) throws Exception {
		this.info("lM008.findAcctCodeGroup ");

		String bcYearMonth = String.valueOf((Integer.valueOf(titaVo.getParam("ENTDY")) + 19110000) / 100);

		String sql = "SELECT NVL(\"AcctCode\", '   ') AS \"AcctCode\"";
		sql += "            ,COUNT(*) AS \"Cnts\"";
		sql += "        FROM ( SELECT A.\"AcctCode\"";
		sql += "               FROM \"AcLoanInt\" A";
		sql += "               LEFT JOIN \"CustMain\" C ON  C.\"CustNo\" = A.\"CustNo\"";
		sql += "               WHERE A.\"YearMonth\" = :bcYearMonth )";
		sql += "        GROUP BY \"AcctCode\"";
		sql += "        ORDER BY \"AcctCode\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("bcYearMonth", bcYearMonth);
		return this.convertToMap(query);
	}

	public List<Map<String, String>> findDetail(String acctCode, TitaVo titaVo) throws Exception {
		this.info("lM008.findDetail acctCode = " + acctCode);

		String bcYearMonth = String.valueOf((Integer.valueOf(titaVo.getParam("ENTDY")) + 19110000) / 100);

		String sql = "";
		sql += " SELECT \"AcctCode\"                   AS \"AcctCode\" ";
		sql += "      , \"Aging\"                      AS \"Aging\" ";
		sql += "      , \"CustNo\"                     AS \"CustNo\" ";
		sql += "      , \"FacmNo\"                     AS \"FacmNo\" ";
		sql += "      , \"Fn_ParseEOL\"(\"CustName\", 0) AS \"CustName\" ";
		sql += "      , SUM(\"LoanBal\")               AS \"LoanBal\" ";
		sql += "      , MAX(\"IntRate\")               AS \"IntRate\" ";
		sql += "      , MIN(\"IntStartDate\")          AS \"IntStartDate\" ";
		sql += "      , SUM(\"UnpaidInt\")             AS \"UnpaidInt\" ";
		sql += "      , SUM(\"UnexpiredInt\")          AS \"UnexpiredInt\" ";
		sql += " FROM (SELECT A.\"AcctCode\" ";
		sql += "            , A.\"Aging\" ";
		sql += "            , A.\"CustNo\" ";
		sql += "            , C.\"CustName\" ";
		sql += "            , A.\"FacmNo\" ";
		sql += "            , A.\"LoanBal\" ";
		sql += "            , A.\"IntRate\" ";
		sql += "            , DECODE(A.\"IntStartDate\", 0, 99991231, A.\"IntStartDate\") \"IntStartDate\" ";
		sql += "            , CASE WHEN A.\"PayIntDate\" <= ";
		sql += "                        TO_CHAR(ADD_MONTHS(TO_DATE(:bcYearMonth || '01', 'YYYYMMDD') - 1, 1), 'YYYYMMDD') ";
		sql += "                    AND SUBSTR(A.\"IntStartDate\", 1, 6) != :bcYearMonth ";
		sql += "                       THEN A.\"Interest\" "; // 繳息日小於等於月底日曆日，且繳息起日非當月
		sql += "                       ELSE 0 ";
		sql += "              END                                                     \"UnpaidInt\" "; // 已到期
		sql += "            , CASE WHEN A.\"PayIntDate\" > ";
		sql += "                        TO_CHAR(ADD_MONTHS(TO_DATE(:bcYearMonth || '01', 'YYYYMMDD') - 1, 1), 'YYYYMMDD') ";
		sql += "                       THEN A.\"Interest\" "; // 繳息日大於月底日曆日
		sql += "                   WHEN SUBSTR(A.\"IntStartDate\", 1, 6) = :bcYearMonth ";
		sql += "                       THEN A.\"Interest\" "; // 計息起日為同月時，視為未到期
		sql += "                       ELSE 0 ";
		sql += "              END                                                     \"UnexpiredInt\" "; // 未到期
		sql += "       FROM \"AcLoanInt\" A ";
		sql += "       LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = A.\"CustNo\" ";
		sql += "       WHERE A.\"YearMonth\" = :bcYearMonth ";
		sql += "         AND A.\"AcctCode\" = :acctCode ";
		sql += "      ) S1 ";
		sql += " GROUP BY \"AcctCode\" ";
		sql += "        , \"Aging\" ";
		sql += "        , \"CustNo\" ";
		sql += "        , \"CustName\" ";
		sql += "        , \"FacmNo\" ";
		sql += " ORDER BY \"AcctCode\" ";
		sql += "        , \"Aging\" ";
		sql += "        , \"CustNo\" ";
		sql += "        , \"FacmNo\" ";

		
		
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("acctCode", acctCode);
		query.setParameter("bcYearMonth", bcYearMonth);
		return this.convertToMap(query);
	}

}