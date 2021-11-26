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
public class LM007ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int iTbsdyf, String acSubBookCode, TitaVo titaVo) throws Exception {
		this.info("lM007.findAll ");
		this.info("lM007 findAll acSubBookCode = " + acSubBookCode);
		String entdy = String.valueOf(iTbsdyf / 10000);
		String sql = "SELECT S1.\"AcMonth\"";
		sql += "            ,SUM(CASE WHEN S1.\"AcctCode\" = '310' AND S1.\"EntCode\" = '1' THEN S1.\"TxAmt\" ELSE 0 END) AS \"IC1EntAmt\"";
		sql += "            ,SUM(CASE WHEN S1.\"AcctCode\" = '310' AND S1.\"EntCode\" = '0' THEN S1.\"TxAmt\" ELSE 0 END) AS \"IC1Amt\"";
		sql += "            ,SUM(CASE WHEN S1.\"AcctCode\" = '320' AND S1.\"EntCode\" = '1' THEN S1.\"TxAmt\" ELSE 0 END) AS \"IC2EntAmt\"";
		sql += "            ,SUM(CASE WHEN S1.\"AcctCode\" = '320' AND S1.\"EntCode\" = '0' THEN S1.\"TxAmt\" ELSE 0 END) AS \"IC2Amt\"";
		sql += "            ,SUM(CASE WHEN S1.\"AcctCode\" = '330' AND S1.\"EntCode\" = '1' THEN S1.\"TxAmt\" ELSE 0 END) AS \"IC3EntAmt\"";
		sql += "            ,SUM(CASE WHEN S1.\"AcctCode\" = '330' AND S1.\"EntCode\" = '0' THEN S1.\"TxAmt\" ELSE 0 END) AS \"IC3Amt\"";
		sql += "            ,SUM(CASE WHEN S1.\"AcctCode\" = '340' AND S1.\"EntCode\" = '0' THEN S1.\"TxAmt\" ELSE 0 END) AS \"IC4EntAmt\"";
		sql += "            ,SUM(CASE WHEN S1.\"AcctCode\" = '340' AND S1.\"EntCode\" = '1' THEN S1.\"TxAmt\" ELSE 0 END) AS \"IC4Amt\"";
		sql += "            ,SUM(CASE WHEN S1.\"AcctCode\" = '990' AND S1.\"EntCode\" = '0' THEN S1.\"TxAmt\" ELSE 0 END) AS \"ColAmt\"";
		sql += "            ,SUM(CASE WHEN S1.\"AcctCode\" IN ('310','320','330','340','990') THEN (S1.\"TxAmt\") ELSE 0 END) AS \"TotalAmt\"";
		sql += "      FROM(SELECT A.\"AcSubBookCode\" AS F0";
		sql += "                 ,MOD(TRUNC(A.\"AcDate\" / 100), 100) AS \"AcMonth\"";
		sql += "                 ,DECODE (L.\"Status\", 5, '990', F.\"AcctCode\") AS \"AcctCode\"";
		sql += "                 ,DECODE (C.\"EntCode\", '1', 1, 0) AS \"EntCode\"";
		sql += "                 ,DECODE (A.\"DbCr\", 'C', A.\"TxAmt\", -A.\"TxAmt\") AS \"TxAmt\"";
		sql += "           FROM \"AcDetail\" A";
		sql += "           LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = A.\"CustNo\"";
		sql += "                                   AND A.\"CustNo\" NOT IN (0)";
		sql += "           LEFT JOIN \"CdAcBook\" B ON B.\"AcSubBookCode\" = A.\"AcSubBookCode\"";
		sql += "                                   AND B.\"AcSubBookCode\" = A.\"AcSubBookCode\"";
		sql += "           LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = A.\"CustNo\"";
		sql += "                                  AND F.\"FacmNo\" = A.\"FacmNo\"";
		sql += "                                  AND F.\"CustNo\" <> 0";
		sql += "           LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" = A.\"CustNo\"";
		sql += "                                      AND L.\"FacmNo\" = A.\"FacmNo\"";
		sql += "                                      AND L.\"BormNo\" = A.\"BormNo\"";
		;
		sql += "           WHERE TRUNC(A.\"AcDate\" / 10000) = :entdy";
		sql += "             AND A.\"AcctCode\" IN ('IC1', 'IC2', 'IC3', 'IC4', 'IOV', 'IOP')";
		sql += "      ) S1";
		sql += "      WHERE S1.\"F0\" LIKE :acSubBookCode ";
		sql += "      GROUP BY S1.\"AcMonth\"";
		sql += "      ORDER BY S1.\"AcMonth\"";

		this.info("sql=" + sql);

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entdy", entdy);
		query.setParameter("acSubBookCode", acSubBookCode);
		return this.convertToMap(query);
	}

	public List<Map<String, String>> findAll_SubBookCodes(TitaVo titaVo) throws Exception {
		this.info("LM007 findAll_SubBookCodes initiated");

		String sql = "";
		sql += " SELECT \"Code\" F0 ";
		sql += "       ,\"Item\" F1 ";
		sql += " FROM \"CdCode\" ";
		sql += " WHERE \"DefCode\" = 'AcSubBookCode' ";
		sql += " UNION ALL ";
		sql += " SELECT '%' F0 ";
		sql += "       ,u'全部' F1 ";
		sql += " FROM DUAL ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}
}