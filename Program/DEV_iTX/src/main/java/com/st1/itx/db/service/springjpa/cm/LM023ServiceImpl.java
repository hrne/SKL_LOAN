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
public class LM023ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM023ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		logger.info("lM023.findAll ");
		String entdy = String.valueOf((Integer.valueOf(titaVo.getParam("ENTDY")) + 19110000));
		String acbrno = titaVo.getParam("ACBRNO");
		logger.info("acbrno---->" + acbrno);
		String sql = "SELECT M.\"AcNoCode\"";
		sql += "             , C.\"AcNoItem\"";
		sql += "             , M.\"AcSubBookCode\"";
		sql += "             , D.\"Item\"";
		sql += "             , M.\"TdBal\" ";
		sql += "        FROM \"AcMain\" M";
		sql += "        LEFT JOIN \"CdAcCode\" C ON C.\"AcNoCode\" = M.\"AcNoCode\"";
		sql += "                                AND C.\"AcSubCode\" = M.\"AcSubCode\" ";
		sql += "        LEFT JOIN \"CdCode\" D ON D.\"DefCode\" = 'AcSubBookCode' ";
		sql += "                              AND D.\"Code\" = M.\"AcSubBookCode\" ";
		sql += "        WHERE M.\"AcDate\" = :entdy";
		sql += "          AND M.\"BranchNo\" = :acbrno";
		sql += "          AND M.\"AcNoCode\" LIKE '4%'";
		sql += "        ORDER BY M.\"AcNoCode\", M.\"AcSubBookCode\" DESC";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entdy", entdy);
		query.setParameter("acbrno", acbrno);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> find1(TitaVo titaVo) throws Exception {
		logger.info("lM023.findAll ");
		String entdy = String.valueOf((Integer.valueOf(titaVo.get("ENTDY").toString()) + 19110000) / 10000);
		String last = String.valueOf(((Integer.valueOf(titaVo.get("ENTDY").toString()) + 19110000) / 10000) - 1);
		String sql = "SELECT \"F1\", \"F2\", SUM(F3) FROM(" + "SELECT 9999 F1, MOD(M.\"MonthEndYm\", 100) F2," + "       M.\"TdBal\" F3" + " FROM \"AcMain\" M"
				+ " WHERE TRUNC(M.\"MonthEndYm\" / 100) = :entdy" + "   AND M.\"AcNoCode\" LIKE '4%'" + "   AND M.\"AcBookCode\" = '000'" + "  UNION ALL"
				+ "  SELECT B.\"Year\" F1, B.\"Month\" F2, B.\"Budget\" F3" + "  FROM \"CdBudget\" B" + "  WHERE B.\"Year\" = :entdy" + "  UNION ALL"
				+ "  SELECT B.\"Year\" F1, B.\"Month\" F2, B.\"Budget\" F3" + "  FROM \"CdBudget\" B" + "  WHERE B.\"Year\" = :last)" + "  GROUP BY \"F1\", \"F2\"" + "  ORDER BY \"F1\" DESC, \"F2\"";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entdy", entdy);
		query.setParameter("last", last);
		return this.convertToMap(query.getResultList());
	}

}