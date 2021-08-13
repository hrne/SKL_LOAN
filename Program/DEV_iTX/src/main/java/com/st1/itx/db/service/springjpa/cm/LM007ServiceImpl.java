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
public class LM007ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM007ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(int iTbsdyf, String acSubBookCode, TitaVo titaVo) throws Exception {
		logger.info("lM007.findAll ");
		logger.info("lM007 findAll acSubBookCode = " + acSubBookCode);
		String entdy = String.valueOf(iTbsdyf / 10000);
		String sql = "SELECT S1.\"AcMonth\"";
		sql += "            ,SUM(CASE WHEN S1.\"AcctCode\" = '310' AND S1.\"EntCode\" = '1' THEN S1.\"TxAmt\" ELSE 0 END)AS \"IC1EntAmt\"";
		sql += "            ,SUM(CASE WHEN S1.\"AcctCode\" = '310' AND S1.\"EntCode\" = '0' THEN S1.\"TxAmt\" ELSE 0 END)AS \"IC1Amt\"";
		sql += "            ,SUM(CASE WHEN S1.\"AcctCode\" = '320' AND S1.\"EntCode\" = '1' THEN S1.\"TxAmt\" ELSE 0 END)AS \"IC2EntAmt\"";
		sql += "            ,SUM(CASE WHEN S1.\"AcctCode\" = '320' AND S1.\"EntCode\" = '0' THEN S1.\"TxAmt\" ELSE 0 END)AS \"IC2Amt\"";
		sql += "            ,SUM(CASE WHEN S1.\"AcctCode\" = '330' AND S1.\"EntCode\" = '1' THEN S1.\"TxAmt\" ELSE 0 END)AS \"IC3EntAmt\"";
		sql += "            ,SUM(CASE WHEN S1.\"AcctCode\" = '330' AND S1.\"EntCode\" = '0' THEN S1.\"TxAmt\" ELSE 0 END)AS \"IC3Amt\"";
		sql += "            ,SUM(CASE WHEN S1.\"AcctCode\" = '340' AND S1.\"EntCode\" = '0' THEN S1.\"TxAmt\" ELSE 0 END)AS \"IC4EntAmt\"";
		sql += "            ,SUM(CASE WHEN S1.\"AcctCode\" = '340' AND S1.\"EntCode\" = '1' THEN S1.\"TxAmt\" ELSE 0 END)AS \"IC4Amt\"";
		sql += "            ,SUM(CASE WHEN S1.\"AcctCode\" = '999' AND S1.\"EntCode\" = '0' THEN S1.\"TxAmt\" ELSE 0 END)AS \"ColAmt\"";
		sql += "            ,SUM(S1.\"TxAmt\") AS \"TotalAmt\"";
		sql += "      FROM(SELECT A.\"AcSubBookCode\" AS F0";
		sql += "                 ,TRUNC(A.\"AcDate\" / 100) AS \"AcMonth\"";
		sql += "                 ,DECODE (L.\"Status\", 5, '999', F.\"AcctCode\") AS \"AcctCode\"";
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
//		sql += "	            LEFT JOIN \"CdCode\"   D ON D.\"DefCode\" = 'acSubBookCode'";
//		sql += "	                                    AND D.\"Code\" = B.\"acSubBookCode\"";
		sql += "           WHERE TRUNC(A.\"AcDate\" / 10000) = :entdy";
		sql += "             AND A.\"AcctCode\" IN ('IC1', 'IC2', 'IC3', 'IC4', 'IOV', 'IOP')";
		sql += "      ) S1";
		sql += "      WHERE S1.\"F0\" LIKE :acSubBookCode ";
		sql += "      GROUP BY S1.\"AcMonth\"";
		sql += "      ORDER BY S1.\"AcMonth\"";
		
		logger.info("sql="+sql);
		
		Query query;
		
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query=em.createNativeQuery(sql);query.setParameter("entdy",entdy);
		query.setParameter("acSubBookCode", acSubBookCode);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll_SubBookCodes(TitaVo titaVo) throws Exception
	{
		logger.info("LM007 findAll_SubBookCodes initiated");
		
		String sql = "";
		sql += " SELECT ";
		sql += " \"Code\" F0, ";
		sql += " \"Item\" F1 ";
		sql += " FROM ";
		sql += " \"CdCode\" WHERE \"DefCode\" = 'AcSubBookCode' ";
		sql += " UNION ALL ";
		sql += " SELECT "; 
		sql += " '%' F0, ";
		sql += " u'全部' F1 ";
		sql += " FROM ";
		sql += " DUAL ";
		
		logger.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query.getResultList());
	}
}