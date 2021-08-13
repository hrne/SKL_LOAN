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
public class LP003ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LP003ServiceImpl.class);
 
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> wkSsn(TitaVo titaVo) throws Exception {

		String iENTDY = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000);
		logger.info("LP003ServiceImpl.wkSsn ENTDY=" + iENTDY);

		// 總表 f0:本日之工作年,f1:本日之工作月
//		String sql = "SELECT W.\"Year\" AS F0, W.\"Month\" F1 " + " FROM  \"CdWorkMonth\" W" + " WHERE W.\"StartDate\" <= :iday" + "   AND W.\"EndDate\"   >= :iday";
		String sql = "SELECT W.\"Year\" AS F0";
		sql += "			,W.\"Month\" AS F1";
//		sql += "			,DECODE(W.\"EndDate\", :iday ,W.\"EndDate\",W.\"StartDate\" - 1 )AS F2";
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
	public List<Map<String, String>> findDept(TitaVo titaVo, Map<String, String> wkVo) throws Exception {

//		String iYEAR = wkVo.get("F0");
//		String iYearMonth = wkVo.get("F0") + String.format("%02d", Integer.valueOf(wkVo.get("F1")));


		int iYEAR = Integer.parseInt(wkVo.get("F0"));
		int iMM = Integer.parseInt(wkVo.get("F1"));

		if (Integer.parseInt(wkVo.get("F1")) == 1) {
			iYEAR = Integer.parseInt(wkVo.get("F0")) - 1;
			iMM = 13;
		} else {
			iYEAR = Integer.parseInt(wkVo.get("F0"));
			iMM = Integer.parseInt(wkVo.get("F1")) - 1;
		}
		
		
		logger.info("LP003ServiceImpl.findDept iyear=" + iYEAR+"~imm="+iMM);
//		logger.info("LP003ServiceImpl.findDept iYearMonth=" + iYearMonth);
		String sql = "SELECT S.\"SEQ\" AS F0";
		sql += "			,O.\"DepItem\" AS F1";
		sql += "			,D.\"DepartOfficer\" AS F2";
		sql += "			,O.\"WorkMonth\" AS F3";
		sql += "			,NVL(PO3.\"GoalAmt\", 0) AS F4";
		sql += "			,NVL(O.\"PerfCnt\", 0) AS F5";
		sql += "			,NVL(O.\"PerfAmt\", 0) AS F6";
		sql += "			,O.\"DeptCode\" AS F7";
		sql += "	  FROM(SELECT RS.\"DepItem\"";
		sql += "				 ,RS.\"WorkMonth\"";
		sql += "				 ,RS.\"DeptCode\"";
		sql += "				 ,SUM(RS.\"PerfCnt\") \"PerfCnt\"";
		sql += "				 ,SUM(RS.\"PerfAmt\") \"PerfAmt\"";
		sql += "		   FROM (SELECT O.\"DepItem\"";
		sql += "				 	   ,O.\"WorkMonth\"";
		sql += "				 	   ,O.\"DeptCode\"";
		sql += "				 	   ,NVL(D.\"PerfCnt\", 0) \"PerfCnt\"";
		sql += "				 	   ,NVL(D.\"PerfAmt\", 0) \"PerfAmt\"";
		sql += "		   		 FROM \"PfBsOfficer\" O";
		sql += "		   		 LEFT JOIN \"PfBsDetail\" D ON D.\"WorkMonth\" = O.\"WorkMonth\"";
		sql += "		   							       AND D.\"BsOfficer\" = O.\"EmpNo\"";
		sql += "		  		 WHERE TRUNC(O.\"WorkMonth\" / 100 ) = :iyear";
		sql += "				   AND O.\"DeptCode\" IN ('A0E000','A0F000','A0M000','A0B000')";
		sql += "		   	     UNION ALL";
		sql += "		   	     SELECT O.\"DepItem\"";
		sql += "				 	   ,W.\"Year\" * 100 + W.\"Month\" AS \"WorkMonth\"";
		sql += "				 	   ,O.\"DeptCode\"";
		sql += "				 	   ,0  \"PerfCnt\"";
		sql += "				 	   ,0 \"PerfAmt\"";
		sql += "				 FROM \"PfBsOfficer\" O";
		sql += "		 		 LEFT JOIN \"PfBsDetail\" D ON D.\"WorkMonth\" = O.\"WorkMonth\"";
		sql += "		   			  				       AND D.\"BsOfficer\" = O.\"EmpNo\"";
		sql += "		 		 CROSS JOIN \"CdWorkMonth\" W";
		sql += "		   		 WHERE W.\"Year\" = :iyear";
		sql += "				   AND W.\"Month\" >= 1";
		sql += "				   AND W.\"Month\" <= :imm ";
		sql += "				   AND O.\"DeptCode\" IN ('A0E000','A0F000','A0M000','A0B000')) RS";
		sql += "		   GROUP BY RS.\"DepItem\", RS.\"WorkMonth\" ,RS.\"DeptCode\" ) O";
		sql += "	  LEFT JOIN (SELECT * FROM \"PfDeparment\" WHERE \"DepartOfficer\" IS NOT NULL AND \"DistItem\" IS NULL) D ";
		sql += "	  ON D.\"DeptCode\" = O.\"DeptCode\"";
		sql += "	  LEFT JOIN (SELECT \"WorkMonth\",\"DeptCode\",SUM(\"GoalAmt\") AS \"GoalAmt\" FROM \"PfBsOfficer\" WHERE TRUNC(\"WorkMonth\" / 100) = :iyear GROUP BY \"WorkMonth\",\"DeptCode\") PO3 ";
		sql += "	  ON PO3.\"WorkMonth\" = O.\"WorkMonth\" AND PO3.\"DeptCode\" = O.\"DeptCode\"";
		sql += "	  LEFT JOIN(SELECT O.\"DeptCode\"";
		sql += "					  ,ROW_NUMBER() OVER(ORDER BY SUM(NVL(D.\"PerfAmt\",0)) / SUM(NVL(O.\"GoalAmt\",0)) DESC) AS SEQ";
		sql += "				FROM \"PfBsOfficer\" O";
		sql += "				LEFT JOIN \"PfBsDetail\" D ON D.\"WorkMonth\" = O.\"WorkMonth\"";
		sql += "										  AND D.\"BsOfficer\" = O.\"EmpNo\"";
		sql += "				WHERE TRUNC(O.\"WorkMonth\" / 100) = :iyear";
		sql += "				  AND O.\"DeptCode\" IN ('A0E000','A0F000','A0M000','A0B000')";
		sql += "				GROUP BY O.\"DeptCode\") S";
		sql += "	  ON S.\"DeptCode\" = O.\"DeptCode\"";
		sql += " 	  WHERE O.\"WorkMonth\" <= :iwkm";
		sql += "	  ORDER BY S.\"SEQ\",O.\"DepItem\", O.\"WorkMonth\"";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("iyear", iYEAR);
		query.setParameter("imm", iMM);
		query.setParameter("iwkm", iYEAR * 100 + iMM);
		return this.convertToMap(query.getResultList());
	}


	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findDeptThisWKM(TitaVo titaVo, Map<String, String> wkVo) throws Exception {


		int iYEAR = Integer.parseInt(wkVo.get("F0"));
		int iMM = Integer.parseInt(wkVo.get("F1"));


		
		logger.info("LP003ServiceImpl.findDeptThisWkm iyear=" + iYEAR+"~imm="+iMM);

		String sql = " ";
		sql += "	  SELECT ROW_NUMBER() OVER(ORDER BY  SUM(NVL(O.\"PerfAmt\",0)) / SUM(NVL(PO2.\"GoalAmt\",0)) DESC) AS F0";
		sql += "			,DECODE(O.\"DeptCode\",'A0B000','台北','A0F000','台北','A0M000','高雄','A0E000','台中') AS F1";
		sql += "			,D.\"DepartOfficer\" AS F2";
		sql += "			,PO3.\"EmpNo\" AS F3";
		sql += "			,SUM(NVL(PO2.\"GoalAmt\",0)) AS F4";
		sql += "		    ,SUM(NVL(O.\"PerfAmt\",0)) AS F5";
		sql += "			,SUM(NVL(O.\"PerfCnt\",0)) AS F6";
		sql += "			,ROUND(SUM(NVL(O.\"PerfAmt\",0)) / SUM(NVL(PO2.\"GoalAmt\",0)) , 4) AS F7";
		sql += "			,O.\"DepItem\" AS F8";
		sql += "			,O.\"DeptCode\" AS F9";
		sql += "			,O.\"WorkMonth\" AS F10";
		sql += "	  FROM(SELECT O.\"DepItem\"";
		sql += "				 ,O.\"WorkMonth\"";
		sql += "				 ,O.\"DeptCode\"";
		sql += "				 ,O.\"EmpNo\"";
		sql += "				 ,SUM(D.\"PerfCnt\") \"PerfCnt\"";
		sql += "				 ,SUM(D.\"PerfAmt\") \"PerfAmt\"";
		sql += "		   FROM \"PfBsOfficer\" O";
		sql += "		   LEFT JOIN \"PfBsDetail\" D ON D.\"WorkMonth\" = O.\"WorkMonth\"";
		sql += "		   							 AND D.\"BsOfficer\" = O.\"EmpNo\"";
		sql += "		   WHERE O.\"WorkMonth\" = :iwkm";
		sql += "		     AND O.\"DeptCode\" IN ('A0E000','A0F000','A0M000','A0B000')";
		sql += "		   GROUP BY O.\"DepItem\",O.\"WorkMonth\",O.\"DeptCode\",O.\"EmpNo\") O";
		sql += "	  LEFT JOIN (SELECT * FROM \"PfDeparment\" WHERE \"DepartOfficer\" IS NOT NULL AND \"DistItem\" IS NULL) D ";
		sql += "	  ON D.\"DeptCode\" = O.\"DeptCode\"";
		sql += "	  LEFT JOIN \"PfBsOfficer\"	PO2 ON PO2.\"WorkMonth\" = O.\"WorkMonth\"";
		sql += "	  							   AND PO2.\"EmpNo\" = O.\"EmpNo\"";
		sql += "	  LEFT JOIN (SELECT * FROM \"PfBsOfficer\" WHERE \"DistItem\" ='房貸部專' AND \"WorkMonth\" = :iwkm ) PO3 ";
		sql += "	  ON PO3.\"WorkMonth\" = O.\"WorkMonth\" AND PO3.\"DeptCode\" = O.\"DeptCode\"";
		sql += " 	  WHERE O.\"WorkMonth\" = :iwkm";
		sql += "		AND O.\"DeptCode\" IN ('A0E000','A0F000','A0M000','A0B000')";
		sql += " 	  GROUP BY O.\"DepItem\"";
		sql += "			  ,D.\"DepartOfficer\"";
		sql += "			  ,O.\"WorkMonth\"";
		sql += "			  ,PO3.\"EmpNo\"";
		sql += "			  ,O.\"DeptCode\"";
		sql += "	  ORDER BY \"F0\",\"F9\", \"F4\"";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("iwkm", iYEAR * 100 + iMM);

		return this.convertToMap(query.getResultList());
	}




	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findEmp(TitaVo titaVo, Map<String, String> wkVo,int isBs) throws Exception {

//		String iYEAR = wkVo.get("F0");
//		String iYearMonth = wkVo.get("F0") + String.format("%02d", Integer.valueOf(wkVo.get("F1")));

		int iYEAR = Integer.parseInt(wkVo.get("F0"));
		int iMM = Integer.parseInt(wkVo.get("F1"));

		if (Integer.parseInt(wkVo.get("F1")) == 1) {
			iYEAR = Integer.parseInt(wkVo.get("F0")) - 1;
			iMM = 13;
		} else {
			iYEAR = Integer.parseInt(wkVo.get("F0"));
			iMM = Integer.parseInt(wkVo.get("F1")) - 1;
		}

		logger.info("LP003ServiceImpl.findEmp iyear=" + iYEAR);

		String sql = "SELECT S.\"SEQ\" AS F0";
		sql += "			,PO2.\"DepItem\" AS F1";
		sql += "			,PO2.\"DistItem\" AS F2";
		sql += "			,PO2.\"Fullname\" AS F3";
		sql += "			,O.\"WorkMonth\" AS F4";
		sql += "			,NVL(PO.\"GoalAmt\",0) AS F5";
		sql += "			,NVL(O.\"PerfCnt\",0)AS F6";
		sql += "			,NVL(O.\"PerfAmt\",0)AS F7";
		sql += "			,O.\"EmpNo\" AS F8";
		sql += "	  FROM(SELECT RS.\"EmpNo\"";
		sql += "				 ,RS.\"WorkMonth\"";
		sql += "				 ,SUM(RS.\"PerfCnt\") \"PerfCnt\"";
		sql += "				 ,SUM(RS.\"PerfAmt\") \"PerfAmt\"";
		sql += "		   FROM (SELECT O.\"EmpNo\"";
		sql += "				 	   ,O.\"WorkMonth\"";
		sql += "				 	   ,NVL(D.\"PerfCnt\" ,0)  \"PerfCnt\"";
		sql += "				 	   ,NVL(D.\"PerfAmt\" ,0) \"PerfAmt\"";
		sql += "				 FROM \"PfBsOfficer\" O";
		sql += "		 		 LEFT JOIN \"PfBsDetail\" D ON D.\"WorkMonth\" = O.\"WorkMonth\"";
		sql += "		   			  				       AND D.\"BsOfficer\" = O.\"EmpNo\"";
		sql += "		   		 WHERE TRUNC(O.\"WorkMonth\" / 100) = :iyear";
		sql += "		           AND O.\"DeptCode\" IN ('A0E000','A0F000','A0M000','A0B000')";
		sql += "		   	     UNION ALL";
		sql += "		   	     SELECT O.\"EmpNo\"";
		sql += "				 	   ,W.\"Year\" * 100 + W.\"Month\" AS \"WorkMonth\"";
		sql += "				 	   ,0  \"PerfCnt\"";
		sql += "				 	   ,0 \"PerfAmt\"";
		sql += "				 FROM \"PfBsOfficer\" O";
		sql += "		 		 LEFT JOIN \"PfBsDetail\" D ON D.\"WorkMonth\" = O.\"WorkMonth\"";
		sql += "		   			  				       AND D.\"BsOfficer\" = O.\"EmpNo\"";
		sql += "		 		 CROSS JOIN \"CdWorkMonth\" W";
		sql += "		   		 WHERE W.\"Year\" = :iyear";
		sql += "				   AND W.\"Month\" >= 1";
		sql += "				   AND W.\"Month\" <= :imm ";
		sql += "				   AND O.\"DeptCode\" IN ('A0E000','A0F000','A0M000','A0B000')) RS";
		sql += "		   GROUP BY RS.\"EmpNo\", RS.\"WorkMonth\" ) O";
		sql += "	  LEFT JOIN(SELECT DISTINCT \"EmpNo\"";
		sql += "	  							,\"Fullname\"";
		sql += "	  							,\"DepItem\"";
		sql += "	  							,\"DistItem\"";
		sql += "	  			FROM \"PfBsOfficer\"";
		sql += "	  			WHERE TRUNC(\"WorkMonth\" / 100 ) = :iyear ";
		sql += "		          AND \"DeptCode\" IN ('A0E000','A0F000','A0M000','A0B000')) PO2";
		sql += "	  ON PO2.\"EmpNo\" = O.\"EmpNo\"";		
		sql += "	  LEFT JOIN \"PfBsOfficer\" PO ON PO.\"WorkMonth\" = O.\"WorkMonth\" AND PO.\"EmpNo\" = O.\"EmpNo\"";	

		sql += "	  LEFT JOIN(SELECT O.\"EmpNo\"";
		sql += "                      ,O.\"GoalAmt\"";
		sql += "					  ,ROW_NUMBER() OVER ( ORDER BY SS.\"PerfAmt\" /  DECODE(O.\"GoalAmt\",'0','1',o.\"GoalAmt\") DESC) AS SEQ";
		sql += "				FROM \"PfBsOfficer\" O";
		sql += "				LEFT JOIN(SELECT O.\"EmpNo\"";
		sql += "							    ,SUM(NVL(D.\"PerfAmt\",0)) AS \"PerfAmt\"";
		sql += "						  FROM \"PfBsOfficer\" O ";
		sql += "						  LEFT JOIN \"PfBsDetail\" D ON D.\"WorkMonth\" = O.\"WorkMonth\"";
		sql += "										            AND D.\"BsOfficer\" = O.\"EmpNo\"";
		sql += "						  WHERE TRUNC(O.\"WorkMonth\" / 100) = :iyear";
		sql += "	              			AND O.\"DeptCode\" IN ('A0E000','A0F000','A0M000','A0B000')";
		sql += "						  GROUP BY O.\"EmpNo\") SS ";
		sql += "                          ON SS.\"EmpNo\" = O.\"EmpNo\"";
		sql += "						  WHERE O.\"WorkMonth\"  = :iwkm  ";
		//1=房貸部專
		if(isBs == 1){
			sql += " 	   				AND O.\"DistItem\" = '房貸部專' ";
		}else{
			sql += " 	    			AND O.\"DistItem\" <> '房貸部專' ";
		}
		sql +="	) S ";
		sql += "	  ON S.\"EmpNo\" =  O.\"EmpNo\"";
		sql += " 	  WHERE O.\"WorkMonth\" <= :iwkm ";
		sql += " 	    AND PO2.\"DistItem\" IS NOT NULL";
		//1=房貸部專
		if(isBs == 1){
			sql += " 	    AND PO2.\"DistItem\" = '房貸部專' ";
		}else{
			sql += " 	    AND PO2.\"DistItem\" <> '房貸部專' ";
		}
		
		sql += "	  ORDER BY S.\"SEQ\", O.\"EmpNo\",O.\"WorkMonth\"";

		logger.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iyear", iYEAR);
		query.setParameter("imm", iMM);
		query.setParameter("iwkm", iYEAR * 100 + iMM);

		return this.convertToMap(query.getResultList());
	}




	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findEmpThisWKM(TitaVo titaVo, Map<String, String> wkVo,int isBs) throws Exception {


		int iYEAR = Integer.parseInt(wkVo.get("F0"));
		int iMM = Integer.parseInt(wkVo.get("F1"));

		if (Integer.parseInt(wkVo.get("F1")) == 1) {
			iYEAR = Integer.parseInt(wkVo.get("F0")) - 1;
			iMM = 13;
		} else {
			iYEAR = Integer.parseInt(wkVo.get("F0"));
			iMM = Integer.parseInt(wkVo.get("F1")) - 1;
		}

		logger.info("LP003ServiceImpl.findEmpThisWkm iyear=" + iYEAR);

		String sql = "SELECT S.\"SEQ\" AS F0";
		sql += "			,PO2.\"Fullname\" AS F1";
		sql += "			,PO2.\"EmpNo\" AS F2";
		sql += "			,O.\"GoalAmt\" AS F3";
		sql += "			,O.\"PerfAmt\" AS F4";
		sql += "			,O.\"PerfCnt\" AS F5";
		sql += "			,round(DECODE(O.\"GoalAmt\",'0','0',O.\"PerfAmt\") / DECODE(O.\"GoalAmt\",'0','1',O.\"GoalAmt\"), 5)  AS F6";
//		sql += "			,ROUND(O.\"PerfAmt\" / O.\"GoalAmt\" , 5) AS F6";
		sql += "			,PO2.\"DepItem\" AS F7";
		sql += "			,PO2.\"DistItem\" AS F8";
		sql += "	  FROM(SELECT DISTINCT \"EmpNo\"";
		sql += "	  					  ,\"Fullname\"";
		sql += "	  					  ,\"DepItem\"";
		sql += "	  					  ,\"DistItem\"";
		sql += "	  	   FROM \"PfBsOfficer\"";
		sql += "	  	   WHERE \"WorkMonth\" = :iwkm ) PO2";
		sql += "	  LEFT JOIN(SELECT O.\"EmpNo\"";
		sql += "				 	  ,O.\"WorkMonth\"";
		sql += "				 	  ,O.\"GoalAmt\" \"GoalAmt\"";
		sql += "				 	  ,SUM(NVL(D.\"PerfCnt\" ,0))  \"PerfCnt\"";
		sql += "				 	  ,SUM(NVL(D.\"PerfAmt\" ,0)) \"PerfAmt\"";
		sql += "				 FROM \"PfBsOfficer\" O";
		sql += "		 		 LEFT JOIN \"PfBsDetail\" D ON D.\"WorkMonth\" = O.\"WorkMonth\"";
		sql += "		   			  				       AND D.\"BsOfficer\" = O.\"EmpNo\"";
		sql += "		   		 WHERE O.\"WorkMonth\" = :iwkm ";
		sql += "		           AND O.\"DeptCode\" IN ('A0E000','A0F000','A0M000','A0B000')";
		sql += "		   		 GROUP BY O.\"EmpNo\",O.\"WorkMonth\",O.\"GoalAmt\" ) O";
		sql += "	  ON PO2.\"EmpNo\" = O.\"EmpNo\"";	
		sql += "	  LEFT JOIN(SELECT O.\"EmpNo\"";
		sql += "				 	  ,O.\"WorkMonth\"";
		sql += "					  ,ROW_NUMBER() OVER ( ORDER BY DECODE(o.\"GoalAmt\",'0','0',SUM(nvl(d.\"PerfAmt\", 0))) / DECODE(o.\"GoalAmt\",'0','1',o.\"GoalAmt\") DESC) AS SEQ";
		sql += "				FROM \"PfBsOfficer\" O";
		sql += "				LEFT JOIN \"PfBsDetail\" D ON D.\"WorkMonth\" = O.\"WorkMonth\"";
		sql += "										  AND D.\"BsOfficer\" = O.\"EmpNo\"";
		sql += "				WHERE O.\"WorkMonth\" = :iwkm ";
		//1=房貸部專
		if(isBs == 1){
			sql += " 	    AND O.\"DistItem\" = '房貸部專' ";
		}else{
			sql += " 	    AND O.\"DistItem\" <> '房貸部專' ";
		}
		sql += "				GROUP BY O.\"EmpNo\",O.\"WorkMonth\",O.\"GoalAmt\" ) S";
		sql += "	  ON S.\"EmpNo\" =  O.\"EmpNo\"";
		//1=房貸部專
		if(isBs == 1){
			sql += " 	    WHERE PO2.\"DistItem\" = '房貸部專' ";
		}else{
			sql += " 	    WHERE PO2.\"DistItem\" <> '房貸部專' ";
		}
//		sql += "		     AND O.\"DeptCode\" IN ('A0E000','A0F000','A0M000','A0B000')";
		sql += "	  ORDER BY S.\"SEQ\", PO2.\"DistItem\"";

		logger.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		// query.setParameter("iyear", iYEAR);
		query.setParameter("iwkm", iYEAR * 100 + iMM);

		return this.convertToMap(query.getResultList());
	}
}

