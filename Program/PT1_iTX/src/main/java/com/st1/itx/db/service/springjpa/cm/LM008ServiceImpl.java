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
		sql += " SELECT \"AcctCode\"                     AS \"AcctCode\" ";
		sql += "      , \"Aging\"                        AS \"Aging\" ";
		sql += "      , \"CustNo\"                       AS \"CustNo\" ";
		sql += "      , \"FacmNo\"                       AS \"FacmNo\" ";
		sql += "      , \"Fn_ParseEOL\"(\"CustName\", 0) AS \"CustName\" ";
		sql += "      , SUM(CASE WHEN \"UnpaidInt\" + \"UnexpiredInt\" > 0 "; // 我們需要利息為 0 者來找出其他如迄日、利率...等等欄位的應顯示內容，
		sql += "                 THEN \"LoanBal\" "; // 但放款餘額不可計入此類資料的餘額。
		sql += "            ELSE 0 END)                  AS \"LoanBal\" ";
		sql += "      , MAX(CASE WHEN \"seq\" = 1 "; // 根據 2022 年 3 月的樣張，原報表應該是每戶底下最大的額度、最大的撥款，以及最大的期數
		sql += "                 THEN \"IntRate\" "; // 的那筆利率
		sql += "             ELSE 0 END)                 AS \"IntRate\" ";
		sql += "      , MIN(CASE WHEN \"IntStartDate\" >= 19110101 "; // 找出此月此戶此額度最起先的計息迄日
		sql += "                 THEN \"IntStartDate\" "; // 但因為 AcLoanInt 資料的這個欄位可能是 0，所以透過 MIN 排除掉此欄日期不合法的資料
		sql += "            ELSE 99991231 END)           AS \"IntStartDate\" ";
		sql += "      , SUM(\"UnpaidInt\")               AS \"UnpaidInt\" ";
		sql += "      , SUM(\"UnexpiredInt\")            AS \"UnexpiredInt\" ";
		sql += " FROM (SELECT A.\"AcctCode\" ";
		sql += "            , A.\"Aging\" ";
		sql += "            , A.\"CustNo\" ";
		sql += "            , C.\"CustName\" ";
		sql += "            , A.\"FacmNo\" ";
		sql += "            , ROW_NUMBER() OVER (PARTITION BY A.\"AcctCode\" ";
		sql += "                                             ,A.\"CustNo\" ";
		sql += "                                             ,A.\"FacmNo\" ";
		sql += "                                             ,DECODE(A.\"IntRate\", 0, 0, 1) "; // 有些資料計息利率是 0，透過這裡讓非 0 的利率優先
		sql += "                                 ORDER BY A.\"BormNo\" DESC ";
		sql += "                                         ,A.\"TermNo\" DESC) AS \"seq\" ";
		sql += "            , A.\"LoanBal\" ";
		sql += "            , A.\"IntRate\" ";
		sql += "            , A.\"IntStartDate\" ";
		sql += "            , CASE WHEN TRUNC(A.\"PayIntDate\" / 100)     <= :bcYearMonth ";
		sql += "                    AND TRUNC(A.\"IntStartDate\" / 100)   <= :bcYearMonth ";
		sql += "                    AND TRUNC(A.\"IntEndDate\" / 100)     <= :bcYearMonth ";
		sql += "                    AND TRUNC(LBM.\"MaturityDate\" / 100) >  :bcYearMonth "; // 2022-05-18 應收利息差異會議上SKL珮琪提供:若該戶到期日<=當月月底日,利息放在未到期應收息
		sql += "                   THEN A.\"Interest\" ";
		sql += "                   ELSE 0 ";
		sql += "              END AS \"UnpaidInt\" "; // 已到期
		sql += "            , CASE WHEN TRUNC(A.\"PayIntDate\" / 100)     <= :bcYearMonth ";
		sql += "                    AND TRUNC(A.\"IntStartDate\" / 100)   <= :bcYearMonth ";
		sql += "                    AND TRUNC(A.\"IntEndDate\" / 100)     <= :bcYearMonth ";
		sql += "                    AND TRUNC(LBM.\"MaturityDate\" / 100) >  :bcYearMonth "; // 2022-05-18 應收利息差異會議上SKL珮琪提供:若該戶到期日<=當月月底日,利息放在未到期應收息
		sql += "                   THEN 0 ";
		sql += "                   ELSE A.\"Interest\" ";
		sql += "              END AS \"UnexpiredInt\" "; // 未到期
		sql += "       FROM \"AcLoanInt\" A ";
		sql += "       LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = A.\"CustNo\" ";
		sql += "       LEFT JOIN \"LoanBorMain\" LBM ON LBM.\"CustNo\" = A.\"CustNo\" ";
		sql += "                                    AND LBM.\"FacmNo\" = A.\"FacmNo\" ";
		sql += "                                    AND LBM.\"BormNo\" = A.\"BormNo\" ";
		sql += "       WHERE A.\"YearMonth\" = :bcYearMonth ";
		sql += "         AND A.\"AcctCode\"  = :acctCode ";
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