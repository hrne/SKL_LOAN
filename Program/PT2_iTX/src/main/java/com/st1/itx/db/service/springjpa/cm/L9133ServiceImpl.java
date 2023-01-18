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
public class L9133ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findDiffDetail(int reportDate, TitaVo titaVo) {
		this.info("L9133ServiceImpl findDiffDetail ");

		this.info("reportDate = " + reportDate);

		String sql = "";
		sql += " WITH \"AcctCodeData\" AS ( ";
		sql += "   SELECT \"AcctCode\" ";
		sql += "        , \"AcctItem\" ";
		sql += "        , \"AcDate\" ";
		sql += "        , \"BranchNo\" ";
		sql += "        , \"CurrencyCode\" ";
		sql += "        , \"AcSubBookCode\" ";
		sql += "   FROM \"AcAcctCheck\" ";
		sql += "   WHERE CASE ";
		sql += "           WHEN \"AcctMasterBal\" != \"ReceivableBal\" ";
		sql += "           THEN 1 ";
		sql += "           WHEN \"AcctMasterBal\" != \"TdBal\" ";
		sql += "           THEN 1 ";
		sql += "         ELSE 0  ";
		sql += "         END = 1 ";
		sql += "     AND \"AcDate\" = :reportDate ";
		sql += "     AND \"AcctCode\" IN ('310','320','330','340','990') ";
		sql += " ) ";
		sql += " ,\"AR\" AS ( ";
		sql += "   SELECT \"AcctCode\" ";
		sql += "        , \"BranchNo\" ";
		sql += "        , \"CurrencyCode\" ";
		sql += "        , \"AcSubBookCode\" ";
		sql += "        , \"CustNo\" ";
		sql += "        , \"FacmNo\" ";
		sql += "        , \"RvNo\" ";
		sql += "        , \"RvBal\" ";
		sql += "   FROM \"AcReceivable\" ";
		sql += "   WHERE \"AcctCode\" IN ('310','320','330','340','990') ";
		sql += " ) ";
		sql += " ,\"Loan\" AS ( ";
		sql += "   SELECT CASE ";
		sql += "            WHEN NVL(L2.\"BormNo\",0) > 0  ";
		sql += "            THEN '990'  ";
		sql += "          ELSE F1.\"AcctCode\"  ";
		sql += "          END              AS \"AcctCode\" ";
		sql += "        , AR.\"AcSubBookCode\" ";
		sql += "        , 'TWD' AS \"CurrencyCode\" ";
		sql += "        , F1.\"BranchNo\" ";
		sql += "        , F1.\"CustNo\" ";
		sql += "        , F1.\"FacmNo\" ";
		sql += "        , L1.\"BormNo\" ";
		sql += "        , CASE  ";
		sql += "            WHEN NVL(L2.\"BormNo\",0) > 0  ";
		sql += "            THEN L2.\"OvduBal\"  ";
		sql += "          ELSE L1.\"LoanBal\"  ";
		sql += "          END             AS \"LoanBal\" ";
		sql += "   FROM \"FacMain\" F1 ";
		sql += "   LEFT JOIN \"LoanBorMain\" L1 ON L1.\"CustNo\" = F1.\"CustNo\" ";
		sql += "                             AND L1.\"FacmNo\" = F1.\"FacmNo\" ";
		sql += "   LEFT JOIN \"LoanOverdue\" L2 ON L2.\"CustNo\" = L1.\"CustNo\" ";
		sql += "                           AND L2.\"FacmNo\" = L1.\"FacmNo\" ";
		sql += "                           AND L2.\"BormNo\" = L1.\"BormNo\" ";
		sql += "                           AND L2.\"OvduNo\" = L1.\"LastOvduNo\" ";
		sql += "                           AND L1.\"Status\" IN (2,6,7) ";
		sql += "   LEFT JOIN ( SELECT \"CustNo\" ";
		sql += "                    , \"FacmNo\" ";
		sql += "                    , MAX(\"AcSubBookCode\") AS \"AcSubBookCode\" ";
		sql += "               FROM \"AcReceivable\" ";
		sql += "               WHERE \"AcctCode\" IN ('310','320','330','340','990') ";
		sql += "               GROUP BY \"CustNo\" ";
		sql += "                      , \"FacmNo\" ";
		sql += "             ) AR ON AR.\"CustNo\" = F1.\"CustNo\" ";
		sql += "                 AND AR.\"FacmNo\" = F1.\"FacmNo\" ";
		sql += " ) ";
		sql += " ,\"Diff\" AS ( ";
		sql += "   SELECT NVL(A.\"AcctCode\",L.\"AcctCode\")           AS \"AcctCode\" ";
		sql += "        , NVL(A.\"BranchNo\",L.\"BranchNo\")           AS \"BranchNo\" ";
		sql += "        , NVL(A.\"CurrencyCode\",L.\"CurrencyCode\")   AS \"CurrencyCode\" ";
		sql += "        , NVL(A.\"AcSubBookCode\",L.\"AcSubBookCode\") AS \"AcSubBookCode\" ";
		sql += "        , NVL(A.\"CustNo\",L.\"CustNo\")               AS \"CustNo\" ";
		sql += "        , NVL(A.\"FacmNo\",L.\"FacmNo\")               AS \"FacmNo\" ";
		sql += "        , NVL(A.\"RvNo\",L.\"BormNo\")                 AS \"BormNo\" ";
		sql += "        , NVL(A.\"RvBal\",0)                           AS \"AcBal\" ";
		sql += "        , NVL(L.\"LoanBal\",0)                         AS \"AcctMasterBal\" ";
		sql += "        , NVL(A.\"RvBal\",0) - NVL(L.\"LoanBal\",0)    AS \"DiffBal\" ";
		sql += "   FROM \"AR\" A ";
		sql += "   FULL OUTER JOIN \"Loan\" L ON ( ";
		sql += "     L.\"AcctCode\" = A.\"AcctCode\" ";
		sql += "     AND L.\"BranchNo\" = A.\"BranchNo\" ";
		sql += "     AND L.\"CurrencyCode\" = A.\"CurrencyCode\" ";
		sql += "     AND L.\"AcSubBookCode\" = A.\"AcSubBookCode\" ";
		sql += "     AND L.\"CustNo\" = A.\"CustNo\" ";
		sql += "     AND L.\"FacmNo\" = A.\"FacmNo\" ";
		sql += "     AND L.\"BormNo\" = A.\"RvNo\" ";
		sql += "   ) ";
		sql += "   WHERE (NVL(A.\"RvBal\",0) - NVL(L.\"LoanBal\",0)) != 0 ";
		sql += " ) ";
		sql += " SELECT S1.\"AcDate\"                      AS \"AcDate\" ";
		sql += "       ,S1.\"BranchNo\"                    AS \"BranchNo\" ";
		sql += "       ,S1.\"CurrencyCode\"                AS \"CurrencyCode\" ";
		sql += "       ,S1.\"AcSubBookCode\"               AS \"AcSubBookCode\" ";
		sql += "       ,S1.\"AcctCode\"                    AS \"AcctCode\" ";
		sql += "       ,S1.\"AcctItem\"                    AS \"AcctItem\" ";
		sql += "       ,NVL(S2.\"CustNo\",0)               AS \"CustNo\" ";
		sql += "       ,NVL(S2.\"FacmNo\",0)               AS \"FacmNo\" ";
		sql += "       ,NVL(S2.\"BormNo\",0)               AS \"BormNo\" ";
		sql += "       ,NVL(S2.\"AcBal\",0)                AS \"AcBal\" ";
		sql += "       ,NVL(S2.\"AcctMasterBal\",0)        AS \"AcctMasterBal\" ";
		sql += "       ,NVL(S2.\"DiffBal\",0)              AS \"DiffBal\" ";
		sql += " FROM \"AcctCodeData\" S1 ";
		sql += " LEFT JOIN \"Diff\" S2 ON S2.\"AcctCode\" = S1.\"AcctCode\" ";
		sql += "                    AND S2.\"BranchNo\" = S1.\"BranchNo\" ";
		sql += "                    AND S2.\"CurrencyCode\" = S1.\"CurrencyCode\" ";
		sql += "                    AND S2.\"AcSubBookCode\" = S1.\"AcSubBookCode\" ";
		sql += " WHERE NVL(S2.\"DiffBal\",0) != 0 ";

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("reportDate", reportDate);

		return this.convertToMap(query);
	}

}