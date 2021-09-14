package com.st1.itx.db.service.springjpa.cm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
		// query.setParameter("acbrno", acbrno);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll2(TitaVo titaVo) throws Exception {
		this.info("lM023.findAll2 ");

		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		int iYear = (Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 10000;
		int iMonth = ((Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 100) % 100;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		// 當日(int)
		int nowDate = Integer.valueOf(iEntdy);
		Calendar calMonthDate = Calendar.getInstance();
		// 設當年月底日
		calMonthDate.set(iYear, iMonth, 0);

		int thisMonthEndDate = Integer.valueOf(dateFormat.format(calMonthDate.getTime()));

		boolean isMonthZero = iMonth - 1 == 0;

		if (nowDate < thisMonthEndDate) {
			iYear = isMonthZero ? (iYear - 1) : iYear;
			iMonth = isMonthZero ? 12 : iMonth - 1;
		}



		String sql = "SELECT CASE";
		sql += "               WHEN T1.\"Year\" = :thisYear ";
		sql += "               THEN 0";
		sql += "             ELSE 2 END AS \"DataSeq\"";
		sql += "     		,T1.\"Year\"";
		sql += "     		,T1.\"Month\"";
		sql += "     		,(T1.\"Amt\" - T2.\"Amt\") / 100000000 AS \"Amt\"";
		sql += "	  FROM ( SELECT RANK () OVER ( ORDER BY \"Year\",\"Month\") AS \"DataSeq\"";
		sql += "            	   ,\"Year\"";
		sql += "            	   ,\"Month\"";
		sql += "     		       ,SUM(NVL(\"Amt\",0)) AS \"Amt\" ";
		sql += "      		 FROM (SELECT TRUNC(M.\"MonthEndYm\" / 100) AS \"Year\"";
		sql += "   						 ,MOD(M.\"MonthEndYm\",100) AS \"Month\"";
		sql += "   						 ,M.\"TdBal\" AS \"Amt\"";
		sql += "				   FROM \"AcMain\" M";
		sql += "       		       WHERE TRUNC(M.\"MonthEndYm\" / 100) IN (:thisYear,:lastYear)";
		sql += "        	         AND M.\"AcNoCode\" LIKE '4%' )";
		sql += " 			 GROUP BY \"Year\"";
		sql += "		 			 ,\"Month\" ) T1 ";
		sql += "			 LEFT JOIN ";
		sql += "		   ( SELECT RANK () OVER ( ORDER BY \"Year\",\"Month\") AS \"DataSeq\"";
		sql += "            	   ,\"Year\"";
		sql += "            	   ,\"Month\"";
		sql += "     		       ,SUM(NVL(\"Amt\",0)) AS \"Amt\" ";
		sql += "      		 FROM (SELECT TRUNC(M.\"MonthEndYm\" / 100) AS \"Year\"";
		sql += "   						 ,MOD(M.\"MonthEndYm\",100) AS \"Month\"";
		sql += "   						 ,M.\"TdBal\" AS \"Amt\"";
		sql += "				   FROM \"AcMain\" M";
		sql += "       		       WHERE M.\"MonthEndYm\" <> :thisYearMon ";
		sql += "       				 AND (TRUNC(M.\"MonthEndYm\" / 100) IN (:thisYear,:lastYear)";
		sql += "       				 	  OR M.\"MonthEndYm\" = :lastYearMon )";
		sql += "        	         AND M.\"AcNoCode\" LIKE '4%' )";
		sql += " 			 GROUP BY \"Year\"";
		sql += "		 			 ,\"Month\" ) T2 ON T2.\"DataSeq\" = T1.\"DataSeq\" ";
		sql += "       		 UNION ALL";
		sql += "       		 SELECT 1          AS \"DataSeq\"";
		sql += "            	   ,B.\"Year\"   AS \"Year\"";
		sql += "            	   ,B.\"Month\"  AS \"Month\"";
		sql += "            	   ,B.\"Budget\" AS \"Amt\"";
		sql += "       		 FROM \"CdBudget\" B";
		sql += "       		 WHERE  B.\"Year\" = :thisYear ";
		sql += "	  ORDER BY \"DataSeq\"";
		sql += "       		  ,\"Year\"";
		sql += "       		  ,\"Month\"";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("thisYear", iYear + "");
		query.setParameter("lastYear", (iYear - 1) + "");
		query.setParameter("thisYearMon", (iYear * 100) + iMonth + "");
		query.setParameter("lastYearMon", (iYear - 2) + "12");
		return this.convertToMap(query.getResultList());
	}

}