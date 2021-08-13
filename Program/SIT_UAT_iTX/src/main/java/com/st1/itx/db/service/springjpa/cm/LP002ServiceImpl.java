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
public class LP002ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LP002ServiceImpl.class);
 
	@Autowired
	private BaseEntityManager baseEntityManager;
 
	@Override
	public void afterPropertiesSet() throws Exception {

	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> wkSsn(TitaVo titaVo) throws Exception {

		String iENTDY = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000);
		logger.info("lp002.wkSsn ENTDY=" + iENTDY);

//		String sql = "SELECT W.\"Year\" F0, W.\"Month\" F1 " + " FROM  \"CdWorkMonth\" W" + " WHERE W.\"StartDate\" <= :iday" + "   AND W.\"EndDate\"   >= :iday";
		// 總表 f0:本日之工作年,f1:本日之工作月
		String sql = "SELECT W.\"Year\" AS F0";
		sql += "			,W.\"Month\" AS F1";
		sql += "	  FROM \"CdWorkMonth\" W";
		sql += "	  WHERE W.\"StartDate\" <= :iday";
		sql += "	  AND W.\"EndDate\" >= :iday";
		logger.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iday", iENTDY);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings({ "unchecked" })
	//部室
	public List<Map<String, String>> findDept(TitaVo titaVo, Map<String, String> wkVo) throws Exception {

		String iENTDY = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000);
//		String iYEAR = String.valueOf((Integer.valueOf((titaVo.get("ENTDY")) + 19110000) / 10000));
//		String iMM = String.valueOf((Integer.valueOf((titaVo.get("ENTDY").substring(0, 6)) + 191100) % 100));

		int iYEAR = Integer.parseInt(wkVo.get("F0"));
		int iMM = Integer.parseInt(wkVo.get("F1"));

		if (Integer.parseInt(wkVo.get("F1")) == 1) {
			iYEAR = Integer.parseInt(wkVo.get("F0")) - 1;
			iMM = 13;
		} else {
			iYEAR = Integer.parseInt(wkVo.get("F0"));
			iMM = Integer.parseInt(wkVo.get("F1")) - 1;
		}

		logger.info("lp002.findDept ENTDY=" + iENTDY + ",iyear=" + iYEAR + ", imm=" + iYEAR * 100 + iMM);
		String sql = "SELECT B.\"UnitItem\" AS F0";
		sql += "			,E.\"Fullname\" AS F1";
		sql += "			,D.\"Fullname\" AS F2";
		sql += "			,I.\"WorkMonth\" AS F3";
		sql += "			,I.\"PerfCnt\" AS F4";
		sql += "			,I.\"PerfAmt\" AS F5";
		sql += "			,I.\"DeptCode\" AS F6";
		sql += "	  FROM(SELECT I.\"DeptCode\"";
		sql += "				 ,I.\"WorkMonth\"";
		sql += "				 ,SUM(I.\"PerfCnt\") \"PerfCnt\"";
		sql += "				 ,SUM(I.\"PerfAmt\") \"PerfAmt\"";
		sql += "		   FROM(SELECT I.\"DeptCode\"";
		sql += "					  ,I.\"WorkMonth\"";
		sql += "					  ,I.\"PerfCnt\"";
		sql += "					  ,I.\"PerfAmt\"";
		sql += "				FROM \"PfItDetail\" I";
		sql += "				WHERE TRUNC(I.\"WorkMonth\" / 100) = :iyear";
		sql += "				  AND I.\"PerfDate\" <= :iday";
		sql += "				  AND I.\"DeptCode\" IS NOT NULL";
		sql += "				UNION ALL";
		sql += "				SELECT B.\"DeptCode\" \"DeptCode\"";
		sql += "					  ,W.\"Year\" * 100 + W.\"Month\" \"WorkMonth\"";
		sql += "					  ,0 \"PerfCnt\"";
		sql += "					  ,0 \"PerfAmt\"";
		sql += "				FROM \"CdBcm\" B";
		sql += "				CROSS JOIN \"CdWorkMonth\" W";
		sql += "				WHERE B.\"DeptCode\" IN ('A0B000','A0F000','A0E000','A0M000')";
		sql += "				  AND W.\"Year\" = :iyear ) I";
		sql += "		   GROUP BY I.\"DeptCode\", I.\"WorkMonth\") I";
		sql += "	  LEFT JOIN (SELECT \"EmpNo\"";
		sql += "					   ,\"Fullname\"";
		sql += "					   ,\"DeptCode\"";
		sql += "					   ,COUNT(*)";
		sql += "	  			 FROM \"PfBsOfficer\"";
		sql += "	  			 WHERE \"WorkMonth\"  = :wkm ";
		sql += "	  			   AND \"DistCode\" IS NULL";
		sql += "	  			   AND \"DistItem\" LIKE '房貸%'";
		sql += "	  			 GROUP BY \"EmpNo\",\"Fullname\",\"DeptCode\") D";
		sql += "	  			 ON D.\"DeptCode\"=I.\"DeptCode\"";
		sql += "	  LEFT JOIN \"CdBcm\" B ON B.\"UnitCode\" = I.\"DeptCode\"";
		sql += "	  LEFT JOIN \"CdEmp\" E ON E.\"EmployeeNo\" = B.\"UnitManager\"";
		sql += "	  WHERE I.\"DeptCode\" IN ('A0B000','A0F000','A0E000','A0M000')";
		sql += "        AND I.\"WorkMonth\" <= :wkm ";
		sql += "	  ORDER BY I.\"DeptCode\", I.\"WorkMonth\"";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iyear", iYEAR);
		query.setParameter("iday", iENTDY);
		query.setParameter("wkm", iYEAR * 100 + iMM);


		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings({ "unchecked" })
	//區部
	public List<Map<String, String>> findDist(TitaVo titaVo, Map<String, String> wkVo) throws Exception {

		String iENTDY = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000);
		int iYEAR = Integer.parseInt(wkVo.get("F0"));
		int iMM = Integer.parseInt(wkVo.get("F1"));

		if (Integer.parseInt(wkVo.get("F1")) == 1) {
			iYEAR = Integer.parseInt(wkVo.get("F0")) - 1;
			iMM = 13;
		} else {
			iYEAR = Integer.parseInt(wkVo.get("F0"));
			iMM = Integer.parseInt(wkVo.get("F1")) - 1;
		}
		logger.info("lp002.findDist ENTDY=" + iENTDY + ",iyear=" + iYEAR + ", imm=" + iMM);

		String sql = "SELECT B.\"DeptItem\" AS F0";
		sql += "			,B.\"UnitItem\" AS F1";
		sql += "			,E.\"Fullname\" AS F2";
		sql += "			,D.\"DepartOfficer\" AS F3";
		sql += "			,I.\"WorkMonth\" AS F4";
		sql += "			,I.\"PerfCnt\" AS F5";
		sql += "			,I.\"PerfAmt\" AS F6";
		sql += "			,I.\"DistCode\" AS F7";
		sql += "	  FROM(SELECT I.\"DistCode\"";
		sql += "				 ,I.\"WorkMonth\"";
		sql += "				 ,SUM(I.\"PerfCnt\") \"PerfCnt\"";
		sql += "				 ,SUM(I.\"PerfAmt\") \"PerfAmt\"";
		sql += "		   FROM(SELECT I.\"DistCode\"";
		sql += "					  ,I.\"WorkMonth\"";
		sql += "					  ,I.\"PerfCnt\"";
		sql += "					  ,I.\"PerfAmt\"";
		sql += "			    FROM \"PfItDetail\" I";
		sql += "			    WHERE TRUNC(I.\"WorkMonth\" / 100) = :iyear";

		sql += "			    UNION ALL";
		sql += "			    SELECT B.\"UnitCode\" \"DistCode\"";
		sql += "					  ,W.\"Year\" * 100 + W.\"Month\" \"WorkMonth\"";
		sql += "					  ,0 \"PerfCnt\"";
		sql += "					  ,0 \"PerfAmt\"";
		sql += "			    FROM \"CdBcm\" B";
		sql += "			    CROSS JOIN \"CdWorkMonth\" W";
		sql += "			    WHERE B.\"DeptCode\" IN ('A0B000','A0F000','A0E000','A0M000')";
		sql += "				  AND B.\"UnitCode\" = B.\"DistCode\"";
		sql += "				  AND W.\"Year\" = :iyear";
		sql += "				  AND W.\"Month\" >= 1";
		sql += "				  AND W.\"Month\" <= :imm) I";
		sql += "		   GROUP BY I.\"DistCode\",I.\"WorkMonth\") I";
		// sql += "	   LEFT JOIN(SELECT \"DistCode\"";
		// sql += "					   ,ROW_NUMBER() OVER ( ORDER BY SUM(\"PerfAmt\") DESC) AS SEQ";
		// sql += "			     FROM(SELECT I.\"DistCode\"";
		// sql += "							,I.\"PerfAmt\"";
		// sql += "				      FROM \"PfItDetail\" I";
		// sql += "				      WHERE TRUNC(I.\"WorkMonth\" / 100) = :iyear";
		// sql += "						AND I.\"PerfDate\" <= :iday";
		// sql += "					  UNION ALL";
		// sql += "					  SELECT B.\"DistCode\"";
		// sql += "					 	    ,0 \"PerfAmt\"";
		// sql += "					  FROM \"CdBcm\" B";
		// sql += "					  WHERE B.\"DeptCode\" IN('A0B000','A0F000','A0E000','A0M000')";
		// sql += "					    AND B.\"DistCode\" = B.\"UnitCode\")";
		// sql += "				 GROUP BY \"DistCode\") S";
		// sql += "	   ON S.\"DistCode\" = I.\"DistCode\"";
		sql += "	   LEFT JOIN \"CdBcm\" B";
		sql += "	   ON B.\"UnitCode\" = B.\"DistCode\" AND B.\"UnitCode\" = I.\"DistCode\"";
		sql += "	   LEFT JOIN (SELECT * FROM \"PfDeparment\" WHERE \"DepartOfficer\" IS NOT NULL AND \"DistItem\" IS NOT NULL ) D";
		sql += "	   ON D.\"DistCode\" = B.\"DistCode\"";
		sql += "	   LEFT JOIN \"CdEmp\" E";
		sql += "	   ON E.\"EmployeeNo\" = B.\"UnitManager\"";
		sql += "	   WHERE B.\"DeptCode\" IN ('A0B000','A0F000','A0E000','A0M000')";
		sql += "	     AND B.\"DistCode\" = B.\"UnitCode\"";
		sql += "	     AND E.\"Fullname\" IS NOT NULL";
		sql += "	   ORDER BY B.\"DeptItem\",I.\"DistCode\", I.\"WorkMonth\"";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iyear", iYEAR);

		query.setParameter("imm", iMM);

		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings({ "unchecked" })
		// 營管 營推 業推 業開
	public List<Map<String, String>> findUnit(TitaVo titaVo, Map<String, String> wkVo, String unitCode)
			throws Exception {

		String iENTDY = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000);
		int iYEAR = Integer.parseInt(wkVo.get("F0"));
		int iMM = Integer.parseInt(wkVo.get("F1"));

		if (Integer.parseInt(wkVo.get("F1")) == 1) {
			iYEAR = Integer.parseInt(wkVo.get("F0")) - 1;
			iMM = 13;
		} else {
			iYEAR = Integer.parseInt(wkVo.get("F0"));
			iMM = Integer.parseInt(wkVo.get("F1")) - 1;
		}
		logger.info("lP002.findUnit ENTDY=" + iENTDY + ",iyear=" + iYEAR + ", imm=" + iMM);

		String sql = "SELECT B.\"DistItem\" AS F0";
		sql += "			,B.\"UnitItem\" AS F1";
		sql += "			,E.\"Fullname\" AS F2";
		sql += "			,I.\"WorkMonth\" AS F3";
		sql += "			,I.\"PerfCnt\" AS F4";
		sql += "			,I.\"PerfAmt\" AS F5";
		sql += "			,B.\"DeptCode\" AS F6";
		sql += "			,I.\"UnitCode\" AS F7";
		sql += "	  FROM(SELECT I.\"UnitCode\"";
		sql += "				 ,I.\"WorkMonth\"";
		sql += "				 ,SUM(I.\"PerfCnt\") \"PerfCnt\"";
		sql += "				 ,SUM(I.\"PerfAmt\") \"PerfAmt\"";
		sql += "		   FROM(SELECT I.\"UnitCode\"";
		sql += "					  ,I.\"WorkMonth\"";
		sql += "					  ,I.\"PerfCnt\"";
		sql += "					  ,I.\"PerfAmt\"";
		sql += "			    FROM \"PfItDetail\" I";
		sql += "				WHERE TRUNC(I.\"WorkMonth\" / 100) = :iyear";
		sql += "				  AND I.\"PerfDate\" <= :iday";
		sql += "				UNION ALL";
		sql += "				SELECT B.\"UnitCode\" \"DeptCode\"";
		sql += "					  ,W.\"Year\" * 100 + W.\"Month\" \"WorkMonth\"";
		sql += "					  ,0 \"PerfCnt\"";
		sql += "					  ,0 \"PerfAmt\"";
		sql += "				FROM \"CdBcm\" B";
		sql += "				CROSS JOIN \"CdWorkMonth\" W";
		sql += "				WHERE B.\"DeptCode\" IN ('A0B000','A0F000','A0E000','A0M000')";
		sql += "				  AND B.\"DistCode\" <> B.\"UnitCode\"";
		sql += "				  AND W.\"Year\" = :iyear";
		sql += "				  AND W.\"Month\" >= 1";
		sql += "				  AND W.\"Month\" <= :imm) I";
		sql += "		   GROUP BY I.\"UnitCode\", I.\"WorkMonth\") I";
		// sql += "	  LEFT JOIN(SELECT \"DeptCode\"";
		// sql += "					  ,\"UnitCode\"";
		// sql += "					  ,ROW_NUMBER() OVER (PARTITION BY \"DeptCode\" ORDER BY SUM(\"PerfAmt\") DESC)AS SEQ";
		// sql += "			 	FROM(SELECT I.\"DeptCode\"";
		// sql += "						   ,I.\"UnitCode\"";
		// sql += "						   ,I.\"PerfAmt\"";
		// sql += "					 FROM \"PfItDetail\" I";
		// sql += "					 WHERE TRUNC(I.\"WorkMonth\" / 100) = :iyear";
		// sql += "					   AND I.\"PerfDate\" <= :iday";
		// sql += "					 UNION ALL";
		// sql += "				 	 SELECT B.\"DeptCode\"";
		// sql += "						   ,B.\"UnitCode\"";
		// sql += "						   ,0 \"PerfAmt\"";
		// sql += "					 FROM \"CdBcm\" B";
		// sql += "					 WHERE B.\"DeptCode\" IN ('A0B000','A0F000','A0E000','A0M000')";
		// sql += "					   AND B.\"DistCode\" <> B.\"UnitCode\")";
		// sql += "				GROUP BY \"DeptCode\", \"UnitCode\") S";
		//sql += "	  ON S.\"UnitCode\" = I.\"UnitCode\"";
		sql += "	  LEFT JOIN \"CdBcm\" B";
		sql += "	  ON B.\"UnitCode\" = I.\"UnitCode\"";
		sql += "	  LEFT JOIN \"CdEmp\" E";
		sql += "	  ON E.\"EmployeeNo\" = B.\"UnitManager\"";
		sql += "	   WHERE B.\"DeptCode\" IN ('" + unitCode + "')";
		sql += "	     AND B.\"UnitManager\" IS NOT NULL ";
		sql += "	  ORDER BY B.\"DeptCode\", I.\"UnitCode\", I.\"WorkMonth\"";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iyear", iYEAR);
		query.setParameter("iday", iENTDY);
		query.setParameter("imm", iMM);

		return this.convertToMap(query.getResultList());
	}
}