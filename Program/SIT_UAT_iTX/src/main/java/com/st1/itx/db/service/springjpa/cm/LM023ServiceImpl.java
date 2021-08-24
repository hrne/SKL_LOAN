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
public class LM023ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("lM023.findAll ");
		String entdy = String.valueOf((Integer.valueOf(titaVo.getParam("ENTDY")) + 19110000));
		String acbrno = titaVo.getParam("ACBRNO");
		this.info("acbrno---->" + acbrno);
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
		// sql += " AND M.\"BranchNo\" = :acbrno";
		sql += "          AND M.\"AcNoCode\" LIKE '4%'";
		sql += "        ORDER BY M.\"AcNoCode\", M.\"AcSubBookCode\" DESC";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entdy", entdy);
		query.setParameter("acbrno", acbrno);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll2(TitaVo titaVo) throws Exception {
		this.info("lM023.findAll2 ");
		String thisYear = String.valueOf((Integer.valueOf(titaVo.get("ENTDY").toString()) + 19110000) / 10000);
		String lastYear = String.valueOf(((Integer.valueOf(titaVo.get("ENTDY").toString()) + 19110000) / 10000) - 1);
		// String sql = "SELECT \"F1\"";
		// sql += " ,\"F2\"";
		// sql += " ,SUM(F3) FROM( SELECT 9999 F1";
		// sql += " ,MOD(M.\"MonthEndYm\", 100) F2";
		// sql += " ,M.\"TdBal\" F3";
		// sql += " FROM \"AcMain\" M";
		// sql += " WHERE TRUNC(M.\"MonthEndYm\" / 100) = :entdy";
		// sql += " AND M.\"AcNoCode\" LIKE '4%' ";
		// sql += " AND M.\"AcBookCode\" = '000' ";
		// sql += " UNION ALL";
		// sql += " SELECT B.\"Year\" F1";
		// sql += " ,B.\"Month\" F2";
		// sql += " ,B.\"Budget\" F3";
		// sql += " FROM \"CdBudget\" B";
		// sql += " WHERE B.\"Year\" = :entdy";
		// sql += " UNION ALL";
		// sql += " SELECT B.\"Year\" F1";
		// sql += " ,B.\"Month\" F2";
		// sql += " ,B.\"Budget\" F3";
		// sql += " FROM \"CdBudget\" B";
		// sql += " WHERE B.\"Year\" = :last)";
		// sql += " GROUP BY \"F1\"";
		// sql += " ,\"F2\"";
		// sql += " ORDER BY \"F1\" DESC, \"F2\"";

		String sql = "SELECT \"DataSeq\"";
		sql += "     		,\"Year\"";
		sql += "     		,\"Month\"";
		sql += "     		,SUM(NVL(\"Amt\",0)) AS \"Amt\" ";
		sql += "	  FROM ( SELECT CASE";
		sql += "               		  WHEN TRUNC(M.\"MonthEndYm\" / 100) = :thisYear ";
		sql += "                	  THEN 0";
		sql += "              		ELSE 2 ";
		sql += "              		END AS \"DataSeq\"";
		sql += "            	   ,TRUNC(M.\"MonthEndYm\" / 100) AS \"Year\"";
		sql += "            	   ,MOD(M.\"MonthEndYm\", 100) AS \"Month\"";
		sql += "            	   ,M.\"TdBal\" AS \"Amt\"";
		sql += "      		 FROM \"AcMain\" M";
		sql += "       		 WHERE TRUNC(M.\"MonthEndYm\" / 100) IN (:thisYear,:lastYear)";
		sql += "        	   AND M.\"AcNoCode\" LIKE '4%'";
		sql += "       		 UNION ALL";
		sql += "       		 SELECT 1          AS \"DataSeq\"";
		sql += "            	   ,B.\"Year\"   AS \"Year\"";
		sql += "            	   ,B.\"Month\"  AS \"Month\"";
		sql += "            	   ,B.\"Budget\" AS \"Amt\"";
		sql += "       		 FROM \"CdBudget\" B";
		sql += "       		 WHERE  B.\"Year\" = :thisYear )";
		sql += "	  GROUP BY \"DataSeq\"";
		sql += "       	 	  ,\"Year\"";
		sql += "       		  ,\"Month\"";
		sql += "	  ORDER BY \"DataSeq\"";
		sql += "       		  ,\"Year\"";
		sql += "       		  ,\"Month\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("thisYear", thisYear);
		query.setParameter("lastYear", lastYear);
		return this.convertToMap(query.getResultList());
	}

}