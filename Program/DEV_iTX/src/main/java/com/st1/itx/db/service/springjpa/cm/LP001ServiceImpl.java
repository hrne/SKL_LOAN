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
public class LP001ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LP001ServiceImpl.class);
 
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> wkSsn(TitaVo titaVo) throws Exception {

		String iENTDY = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000);
		logger.info("lP001.wkSsn ENTDY=" + iENTDY);

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

	public List<Map<String, String>> findAll(TitaVo titaVo, Map<String, String> wkVo) throws Exception {

		String iENTDY = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000);

		int iYEAR = 0;
		int iMM = 0;
		if (Integer.parseInt(wkVo.get("F1")) == 1) {
			iYEAR = Integer.parseInt(wkVo.get("F0")) - 1;
			iMM = 13;
		} else {
			iYEAR = Integer.parseInt(wkVo.get("F0"));
			iMM = Integer.parseInt(wkVo.get("F1"));
		}
		logger.info("lP001.findAll ENTDY=" + iENTDY + ",iyear=" + iYEAR + ", imm=" + iMM);
		String sql = "SELECT CDD.\"Fullname\" AS F0";
		sql += "			,RSS.\"WorkMonth\" AS F1";
		sql += "			,SUM(RSS.\"PerfCnt\") AS F2";
		sql += "			,SUM(RSS.\"PerfAmt\") AS F3";
		sql += "			,RSS.\"AreaCode\" AS F4";
		sql += "	  FROM(SELECT PO.\"AreaCode\"";
		sql += "				 ,PD.\"WorkMonth\"";
		sql += "				 ,SUM(PD.\"PerfCnt\") AS \"PerfCnt\"";
		sql += "				 ,SUM(PD.\"PerfAmt\") AS \"PerfAmt\"";
		sql += "		   FROM \"PfBsDetail\" PD";
		sql += "		   LEFT JOIN \"PfBsOfficer\" PO ON PO.\"EmpNo\"=PD.\"BsOfficer\"";
		sql += "		   							AND PO.\"WorkMonth\"=PD.\"WorkMonth\"";
		sql += "		   WHERE PO.\"AreaCode\" IN('10HC00','10HJ00','10HL00')";
		sql += "			 AND TRUNC(PD.\"WorkMonth\" / 100) = :iyear";
		// sql += "			 AND PD.\"PerfDate\" <= :iday";
		sql += "		   GROUP BY PO.\"AreaCode\",PD.\"WorkMonth\"";
		sql += "		   UNION ALL";
		sql += "		   SELECT B.\"UnitCode\" \"AreaCode\"";
		sql += "				 ,W.\"Year\" * 100 + W.\"Month\" \"WorkMonth\"";
		sql += "				 ,0 \"PerfCnt\"";
		sql += "				 ,0 \"PerfAmt\"";
		sql += "		   FROM \"CdBcm\" B";
		sql += "		   CROSS JOIN \"CdWorkMonth\" W";
		sql += "		   WHERE B.\"UnitCode\" IN('10HC00','10HJ00','10HL00')";
		sql += "			 AND W.\"Year\" = :iyear";
		sql += "			 AND W.\"Month\" >= 1";
		sql += "			 AND W.\"Month\" <= :imm )RSS";
		sql += "	  LEFT JOIN(Select \"Fullname\"";
		sql += "					  ,\"UnitCode\" FROM \"CdBcm\" CBB";
		sql += "				LEFT JOIN \"CdEmp\" CEE ON CEE.\"EmployeeNo\"= CBB.\"UnitManager\"";
		sql += "				WHERE CBB.\"UnitCode\" IN ('10HC00','10HJ00','10HL00')) CDD";
		sql += "	  ON CDD.\"UnitCode\"=RSS.\"AreaCode\"";
		sql += "	  GROUP BY CDD.\"Fullname\",RSS.\"WorkMonth\",RSS.\"AreaCode\"";
		sql += "	  ORDER BY F1,F4";

		logger.info("sql=" + sql);

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		query = em.createNativeQuery(sql);
		query.setParameter("iyear", iYEAR);
		// query.setParameter("iday", iENTDY);
		query.setParameter("imm", iMM);
		return this.convertToMap(query.getResultList());
	}

}



// String sql = "SELECT CDD.\"Fullname\" AS \"Fullname\"";
// sql += "			,RSS.\"AreaCode\" AS \"AreaCode\"";
// sql += "			,SUM(DECODE(SUBSTR(RSS.\"WorkMonth\",5,2),'01',RSS.\"PerfCnt\",0)) AS \"cnt1\"";
// sql += "			,SUM(DECODE(SUBSTR(RSS.\"WorkMonth\",5,2),'01',RSS.\"DrawdownAmt\",0)) AS \"amt1\"";
// sql += "			,SUM(DECODE(SUBSTR(RSS.\"WorkMonth\",5,2),'02',RSS.\"PerfCnt\",0)) AS \"cnt2\"";
// sql += "			,SUM(DECODE(SUBSTR(RSS.\"WorkMonth\",5,2),'02',RSS.\"DrawdownAmt\",0)) AS \"amt2\"";
// sql += "			,SUM(DECODE(SUBSTR(RSS.\"WorkMonth\",5,2),'03',RSS.\"PerfCnt\",0)) AS \"cnt3\"";
// sql += "			,SUM(DECODE(SUBSTR(RSS.\"WorkMonth\",5,2),'03',RSS.\"DrawdownAmt\",0)) AS \"amt3\"";
// sql += "			,SUM(DECODE(SUBSTR(RSS.\"WorkMonth\",5,2),'04',RSS.\"PerfCnt\",0)) AS \"cnt4\"";
// sql += "			,SUM(DECODE(SUBSTR(RSS.\"WorkMonth\",5,2),'04',RSS.\"DrawdownAmt\",0)) AS \"amt4\"";
// sql += "			,SUM(DECODE(SUBSTR(RSS.\"WorkMonth\",5,2),'05',RSS.\"PerfCnt\",0)) AS \"cnt5\"";
// sql += "			,SUM(DECODE(SUBSTR(RSS.\"WorkMonth\",5,2),'05',RSS.\"DrawdownAmt\",0)) AS \"amt5\"";
// sql += "			,SUM(DECODE(SUBSTR(RSS.\"WorkMonth\",5,2),'06',RSS.\"PerfCnt\",0)) AS \"cnt6\"";
// sql += "			,SUM(DECODE(SUBSTR(RSS.\"WorkMonth\",5,2),'06',RSS.\"DrawdownAmt\",0)) AS \"amt6\"";
// sql += "			,SUM(DECODE(SUBSTR(RSS.\"WorkMonth\",5,2),'07',RSS.\"PerfCnt\",0)) AS \"cnt7\"";
// sql += "			,SUM(DECODE(SUBSTR(RSS.\"WorkMonth\",5,2),'07',RSS.\"DrawdownAmt\",0)) AS \"amt7\"";
// sql += "			,SUM(DECODE(SUBSTR(RSS.\"WorkMonth\",5,2),'08',RSS.\"PerfCnt\",0)) AS \"cnt8\"";
// sql += "			,SUM(DECODE(SUBSTR(RSS.\"WorkMonth\",5,2),'08',RSS.\"DrawdownAmt\",0)) AS \"amt8\"";
// sql += "			,SUM(DECODE(SUBSTR(RSS.\"WorkMonth\",5,2),'09',RSS.\"PerfCnt\",0)) AS \"cnt9\"";
// sql += "			,SUM(DECODE(SUBSTR(RSS.\"WorkMonth\",5,2),'09',RSS.\"DrawdownAmt\",0)) AS \"amt9\"";
// sql += "			,SUM(DECODE(SUBSTR(RSS.\"WorkMonth\",5,2),'10',RSS.\"PerfCnt\",0)) AS \"cnt10\"";
// sql += "			,SUM(DECODE(SUBSTR(RSS.\"WorkMonth\",5,2),'10',RSS.\"DrawdownAmt\",0)) AS \"amt10\"";
// sql += "			,SUM(DECODE(SUBSTR(RSS.\"WorkMonth\",5,2),'11',RSS.\"PerfCnt\",0)) AS \"cnt11\"";
// sql += "			,SUM(DECODE(SUBSTR(RSS.\"WorkMonth\",5,2),'11',RSS.\"DrawdownAmt\",0)) AS \"amt11\"";
// sql += "			,SUM(DECODE(SUBSTR(RSS.\"WorkMonth\",5,2),'12',RSS.\"PerfCnt\",0)) AS \"cnt12\"";
// sql += "			,SUM(DECODE(SUBSTR(RSS.\"WorkMonth\",5,2),'12',RSS.\"DrawdownAmt\",0)) AS \"amt12\"";
// sql += "			,SUM(DECODE(SUBSTR(RSS.\"WorkMonth\",5,2),'13',RSS.\"PerfCnt\",0)) AS \"cnt13\"";
// sql += "			,SUM(DECODE(SUBSTR(RSS.\"WorkMonth\",5,2),'13',RSS.\"DrawdownAmt\",0)) AS \"amt13\"";
// sql += "	  FROM(SELECT PO.\"AreaCode\"";
// sql += "				 ,PD.\"WorkMonth\"";
// sql += "				 ,SUM(PD.\"PerfCnt\") AS \"PerfCnt\"";
// sql += "				 ,SUM(PD.\"DrawdownAmt\") AS \"DrawdownAmt\"";
// sql += "		   FROM \"PfBsDetail\" PD";
// sql += "		   LEFT JOIN \"PfBsOfficer\" PO ON PO.\"EmpNo\"=PD.\"BsOfficer\"";
// sql += "		   							AND PO.\"WorkMonth\"=PD.\"WorkMonth\"";
// sql += "		   WHERE PO.\"AreaCode\" IN('10HC00','10HJ00','10HL00')";
// sql += "			 AND TRUNC(PD.\"WorkMonth\" / 100) = :iyear";
// sql += "			 AND PD.\"PerfDate\" <= :iday";
// sql += "		   GROUP BY PO.\"AreaCode\",PD.\"WorkMonth\"";
// sql += "		   UNION ALL";
// sql += "		   SELECT B.\"UnitCode\" \"AreaCode\"";
// sql += "				 ,W.\"Year\" * 100 + W.\"Month\" \"WorkMonth\"";
// sql += "				 ,0 \"PerfCnt\"";
// sql += "				 ,0 \"DrawdownAmt\"";
// sql += "		   FROM \"CdBcm\" B";
// sql += "		   CROSS JOIN \"CdWorkMonth\" W";
// sql += "		   WHERE B.\"UnitCode\" IN('10HC00','10HJ00','10HL00')";
// sql += "			 AND W.\"Year\" = :iyear";
// sql += "			 AND W.\"Month\" >= 1";
// sql += "			 AND W.\"Month\" <= :imm )RSS";
// sql += "	  LEFT JOIN(Select \"Fullname\"";
// sql += "					  ,\"UnitCode\" FROM \"CdBcm\" CBB";
// sql += "				LEFT JOIN \"CdEmp\" CEE ON CEE.\"EmployeeNo\"= CBB.\"UnitManager\"";
// sql += "				WHERE CBB.\"UnitCode\" IN ('10HC00','10HJ00','10HL00')) CDD";
// sql += "	  ON CDD.\"UnitCode\"=RSS.\"AreaCode\"";
// sql += "	  GROUP BY CDD.\"Fullname\",RSS.\"AreaCode\"";
// sql += "	  ORDER BY \"AreaCode\"";