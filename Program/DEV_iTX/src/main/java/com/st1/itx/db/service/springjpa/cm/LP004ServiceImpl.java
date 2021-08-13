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
public class LP004ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LP004ServiceImpl.class);
 
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> wkSsn(TitaVo titaVo) throws Exception {

		String iENTDY = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000);
		logger.info("lW001.wkSsn ENTDY=" + iENTDY);

		// 總表 f0:本日之工作年,f1:本日之工作月

		String sql = "SELECT W.\"Year\" AS F0";
		sql += "			,W.\"Month\" AS F1";
		sql += "			,W.\"Year\"*100+W.\"Month\" AS F2";
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
	public List<Map<String, String>> findArea(TitaVo titaVo, Map<String, String> wkVo) throws Exception {

		int iYEAR = Integer.valueOf(wkVo.get("F0"));
		int iMM = Integer.valueOf(wkVo.get("F1"));
		int iYYMM = Integer.valueOf(wkVo.get("F2"));
	
		int lastyymm = 0;
		String iWKSSN = "";
		// EX:工作月為202001 上個工作月 201913
//		if (iMM == 1) {
//			lastyymm = (iYEAR - 1) * 100 + 13;
//		} else {
//			// 202012-1=202011
//			lastyymm = iYYMM - 1;
//		}
//		if (iMM < 4) {
//			iWKSSN = iYEAR + "1";
//		} else if (iMM < 7) {
//			iWKSSN = iYEAR + "2";
//		} else if (iMM < 10) {
//			iWKSSN = iYEAR + "3";
//		} else {
//			iWKSSN = iYEAR + "4";
//		}

//		logger.info("LP004.findArea iyymm=" + iYYMM + ",liyymm=" + iWKSSN);

		String sql = "SELECT tab1.\"UnitItem\"";
		sql += "      		,tab1.\"Fullname\"";
		sql += "      		,(CASE WHEN tab2.\"PerfCnt\" >= 0 THEN tab2.\"PerfCnt\" ELSE 0 END) \"lastPerfCnt\"";
		sql += "      		,(CASE WHEN tab2.\"PerfAmt\" >= 0 THEN tab2.\"PerfAmt\" ELSE 0 END) \"lastPerfAmt\"";
		sql += "      		,(CASE WHEN tab3.\"PerfCnt\" >= 0 THEN tab3.\"PerfCnt\" ELSE 0 END) \"thisPerfCnt\"";
		sql += "      		,(CASE WHEN tab3.\"PerfAmt\" >= 0 THEN tab3.\"PerfAmt\" ELSE 0 END) \"thisPerfAmt\"";

		sql += "      FROM(SELECT CB.\"UnitCode\"";
		sql += "      			 ,CB.\"UnitItem\"";
		sql += "      			 ,CB.\"UnitManager\"";
		sql += "      			 ,CE.\"Fullname\" ";
		sql += "      	   FROM \"CdBcm\" CB";
		sql += "      	   LEFT JOIN \"CdEmp\" CE ON CE.\"EmployeeNo\"=CB.\"UnitManager\" ";
		sql += "      	   WHERE \"UnitCode\" IN('10HC00','10HJ00','10HL00'))tab1";
		sql += "      LEFT JOIN(SELECT \"unitArea\"";
		sql += " 					  ,SUM(\"PerfCnt\") \"PerfCnt\"";
		sql += " 					  ,SUM(\"PerfAmt\") \"PerfAmt\"";
		sql += " 				FROM(SELECT \"PerfCnt\"";
		sql += "       				  	   ,\"PerfAmt\"";
		sql += "     				       ,(CASE ";
		sql += "              			       WHEN \"DeptCode\"='A0B000' THEN '10HC00' ";
		sql += "              			       WHEN \"DeptCode\"='A0F000' THEN '10HC00' ";
		sql += "              			       WHEN \"DeptCode\"='A0E000' THEN '10HJ00'";
		sql += "              			       WHEN \"DeptCode\"='A0M000' THEN '10HL00' ";
		sql += "          			         ELSE NULL END)AS \"unitArea\"";
		sql += "    			     FROM \"PfItDetail\" ";
		sql += "    			     WHERE \"WorkMonth\"= :liyymm";
		sql += "      			       AND \"DeptCode\" IN('A0B000','A0F000','A0E000','A0M000'))";
		sql += "    			GROUP BY \"unitArea\")tab2";
		sql += "      ON tab2.\"unitArea\"=tab1.\"UnitCode\"";
		sql += "      LEFT JOIN(SELECT \"unitArea\"";
		sql += " 					  ,SUM(\"PerfCnt\") \"PerfCnt\"";
		sql += " 					  ,SUM(\"PerfAmt\") \"PerfAmt\"";
		sql += " 				FROM(SELECT \"PerfCnt\"";
		sql += "       				  	   ,\"PerfAmt\"";
		sql += "     				       ,(CASE ";
		sql += "              			       WHEN \"DeptCode\"='A0B000' THEN '10HC00' ";
		sql += "              			       WHEN \"DeptCode\"='A0F000' THEN '10HC00' ";
		sql += "              			       WHEN \"DeptCode\"='A0E000' THEN '10HJ00'";
		sql += "              			       WHEN \"DeptCode\"='A0M000' THEN '10HL00' ";
		sql += "          			         ELSE NULL END)AS \"unitArea\"";
		sql += "    			     FROM \"PfItDetail\" ";
		sql += "    			     WHERE \"WorkMonth\"= :iyymm";
		sql += "      			       AND \"DeptCode\" IN('A0B000','A0F000','A0E000','A0M000'))";
		sql += "   		 		GROUP BY \"unitArea\")tab3";
		sql += "      ON tab3.\"unitArea\"=tab1.\"UnitCode\"";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iyymm", iYYMM);
		query.setParameter("liyymm", lastyymm);
		return this.convertToMap(query.getResultList());

	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findBsOfficer(TitaVo titaVo, Map<String, String> wkVo) throws Exception {

		String iYEAR = wkVo.get("F0");
		int iMM = Integer.valueOf(wkVo.get("F1"));
		String iYYMM = wkVo.get("F2");
//		String iWKSSN = "";
//		if (iMM < 4) {
//			iWKSSN = iYEAR + "1";
//		} else if (iMM < 7) {
//			iWKSSN = iYEAR + "2";
//		} else if (iMM < 10) {
//			iWKSSN = iYEAR + "3";
//		} else {
//			iWKSSN = iYEAR + "4";
//		}

//		logger.info("LP004.findBsOfficer iyymm=" + iYYMM + ",iWKSSN=" + iWKSSN);

		String sql = "SELECT PBO.\"Fullname\"";
		sql += "			,PBO.\"EmpNo\"";
		sql += "			,PBO.\"GoalAmt\"";
		sql += "			,NVL(BO.\"PerfAmt\",0) \"PerfAmt\"";
		sql += "			,NVL(BO.\"PerfCnt\",0) \"PerfCnt\"";
		sql += "            ,PBO.\"DepItem\"";
		sql += "			,PBO.\"DistItem\"";
		sql += "	  FROM(SELECT \"Fullname\"";
		sql += "				 ,\"EmpNo\"";
		sql += "				 ,\"GoalAmt\"";
		sql += "            	 ,\"DepItem\"";
		sql += "				 ,\"DistItem\"";
		sql += "				 ,\"WorkMonth\"";
		sql += "	  	   FROM \"PfBsOfficer\"";
		sql += "      	   WHERE \"WorkMonth\"= :iyymm )PBO";
		sql += "      LEFT JOIN(SELECT \"BsOfficer\"";
		sql += "					  ,\"WorkMonth\"";
		sql += "					  ,SUM(\"PerfCnt\") \"PerfCnt\"";
		sql += "					  ,SUM(\"PerfAmt\") \"PerfAmt\"";
		sql += "                FROM \"PfBsDetail\"";
		sql += "      	   		WHERE \"WorkMonth\"= :iyymm ";
		sql += "                GROUP BY \"BsOfficer\"";
		sql += "						,\"WorkMonth\") BO";
		sql += "      ON PBO.\"EmpNo\"=BO.\"BsOfficer\"";
		sql += "      ORDER BY \"PerfAmt\" DESC";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iyymm", iYYMM);

		return this.convertToMap(query.getResultList());

	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAllDept(TitaVo titaVo, Map<String, String> wkVo) throws Exception {

		String iYEAR = wkVo.get("F0");
		int iMM = Integer.valueOf(wkVo.get("F1"));
		String iYYMM = wkVo.get("F2");
//		String iWKSSN = "";
//		if (iMM < 4) {
//			iWKSSN = iYEAR + "1";
//		} else if (iMM < 7) {
//			iWKSSN = iYEAR + "2";
//		} else if (iMM < 10) {
//			iWKSSN = iYEAR + "3";
//		} else {
//			iWKSSN = iYEAR + "4";
//		}

//		logger.info("LP004.findAllDept iyymm=" + iYYMM + ",iWKSSN=" + iWKSSN);

		String sql = "SELECT (CASE";
		sql += "			    WHEN tab1.\"DeptCode\"='A0B000' OR (tab1.\"DepItem\" like '營%' AND tab1.\"DepItem\" like '%管%') THEN '台北' ";
		sql += "                WHEN tab1.\"DeptCode\"='A0F000' OR (tab1.\"DepItem\" like '營%' AND tab1.\"DepItem\" like '%推%') THEN '台北' ";
		sql += "           	    WHEN tab1.\"DeptCode\"='A0E000' OR (tab1.\"DepItem\" like '業%' AND tab1.\"DepItem\" like '%推%') THEN '台中'";
		sql += "                WHEN tab1.\"DeptCode\"='A0M000' OR (tab1.\"DepItem\" like '業%' AND tab1.\"DepItem\" like '%開%') THEN '高雄' ";
		sql += "              ELSE NULL END) AS \"area\"";
		sql += "            ,tab1.\"Fullname\"";
		sql += "            ,tab1.\"EmpNo\"";
		sql += "            ,tab1.\"GoalAmt\"";
		sql += "            ,(CASE WHEN tab2.\"PerfAmt\" >0 THEN tab2.\"PerfAmt\" ELSE 0 END) \"PerfAmt\"";
		sql += "            ,(CASE WHEN tab2.\"PerfCnt\" >0 THEN tab2.\"PerfCnt\" ELSE 0 END) \"PerfCnt\"";
		sql += "            ,(CASE WHEN tab1.\"GoalAmt\" >0 AND tab2.\"PerfAmt\">0 THEN ROUND(tab2.\"PerfAmt\"/tab1.\"GoalAmt\",4) ELSE 0 END) \"rate\"";
		sql += "            ,tab1.\"DepItem\"";
		sql += "	  FROM(SELECT \"DeptCode\"";
		sql += "             	 ,\"DepItem\"";
		sql += "              	 ,\"EmpNo\"";
		sql += "             	 ,\"Fullname\"";
		sql += "              	 ,\"DistItem\"";
		sql += "              	 ,\"AreaCode\" AS \"sDeptCode\"";
		sql += "              	 ,\"GoalAmt\"";
		sql += "           FROM \"PfBsOfficer\"";
		sql += "           WHERE \"WorkMonth\"= :iyymm";
		sql += "             AND \"DistItem\" = '房貸部專') tab1";
		sql += "	  LEFT JOIN(SELECT \"BsOfficer\"";
		sql += "					  ,SUM(\"PerfCnt\") \"PerfCnt\"";
		sql += "					  ,SUM(\"PerfAmt\") \"PerfAmt\"";
		sql += "                FROM \"PfBsDetail\"";
		sql += "                WHERE \"WorkMonth\"= :iyymm";
		sql += "                GROUP BY \"BsOfficer\") tab2";
		sql += "      ON tab1.\"EmpNo\"=tab2.\"BsOfficer\"";
		sql += "	  ORDER BY \"rate\" DESC";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iyymm", iYYMM);
//		query.setParameter("iwkssn", iWKSSN);

		return this.convertToMap(query.getResultList());

	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findDist(TitaVo titaVo, Map<String, String> wkVo) throws Exception {

		
		String iYEAR = wkVo.get("F0");
		int iMM = Integer.valueOf(wkVo.get("F1"));
		String iYYMM = wkVo.get("F2");
		// 季初的工作月
		String iWKs = "";
		// 季末的工作月
		String iWKe = "";

//		if (iMM < 4) {
//			iWKs = iYEAR + "01";
//			iWKe = iYEAR + String.format("%02d", iMM);
//		} else if (iMM < 7) {
//			iWKs = iYEAR + "04";
//			iWKe = iYEAR + String.format("%02d", iMM);
//		} else if (iMM < 10) {
//			iWKs = iYEAR + "07";
//			iWKe = iYEAR + String.format("%02d", iMM);
//		} else {
//			iWKs = iYEAR + "10";
//			iWKe = iYEAR + String.format("%02d", iMM);
//		}

//		logger.info("LP004.findDist iYYMM=" + iYYMM + ",iWKSSN=" + iWKs + "~" + iWKe);

		String sql = "SELECT (CASE";
		sql += "   			   WHEN tab1.\"DeptCode\"='A0B000' THEN '營管部'";
		sql += "      		   WHEN tab1.\"DeptCode\"='A0F000' THEN '營推部'";
		sql += "      		   WHEN tab1.\"DeptCode\"='A0E000' THEN '業推部'";
		sql += "        	   WHEN tab1.\"DeptCode\"='A0M000' THEN '業開部'";
		sql += "          	   WHEN tab1.\"DeptCode\"='A0X000' THEN '專業行銷課'";
		sql += "          	   ELSE null END) AS \"DeptName\"";
		sql += "			,tab1.\"Fullname\"";
		sql += "			,tab4.\"Fullname\" \"sFullname\"";
		sql += "    		,(CASE";
		sql += "    		    WHEN tab2.\"sumPerfCnt\">0 THEN tab2.\"sumPerfCnt\"";
		sql += "    		  ELSE 0 END) AS \"wmCnt\"";
		sql += "        	,(CASE";
		sql += "    		    WHEN tab2.\"sumPerfAmt\">0 THEN tab2.\"sumPerfAmt\"";
		sql += "    		 ELSE 0 END) AS \"wmAmt\"";
		sql += "     		,(CASE";
		sql += "   			    WHEN tab3.\"sumPerfCnt\">0 THEN tab3.\"sumPerfCnt\"";
		sql += "    		  ELSE 0 END) AS \"QCnt\"";
		sql += "        	,(CASE";
		sql += "    		    WHEN tab3.\"sumPerfAmt\">0 THEN tab3.\"sumPerfAmt\"";
		sql += "    		  ELSE 0 END) AS \"QAmt\"";
		sql += "      FROM(SELECT DISTINCT(CB.\"DeptManager\")";
		sql += "				 ,CB.\"DeptCode\"";
		sql += "				 ,CB.\"DeptItem\"";
		sql += "				 ,CE.\"Fullname\"";
		sql += "	 	   FROM \"CdBcm\" CB";
		sql += "      	   LEFT JOIN \"CdEmp\" CE";
		sql += "      	   ON CE.\"EmployeeNo\"=CB.\"DeptManager\"";
		sql += "       	   WHERE CB.\"DeptCode\" IN('A0B000','A0F000','A0E000','A0M000')) tab1";
		sql += "      LEFT JOIN(SELECT \"DeptManager\"";
		sql += "					  ,SUM(\"PerfCnt\") \"sumPerfCnt\"";
		sql += "					  ,SUM(\"PerfAmt\") \"sumPerfAmt\"";
		sql += "			    FROM \"PfItDetail\" ";
		sql += "        	 	WHERE \"WorkMonth\"= :iyymm";
		sql += "        		  AND \"DeptCode\" IN('A0B000','A0F000','A0E000','A0M000')";
		sql += "         		  AND \"DistManager\" is not null";
		sql += "         		GROUP BY \"DeptManager\") tab2";
		sql += "      ON tab1.\"DeptManager\"=tab2.\"DeptManager\" ";
		sql += "      LEFT JOIN(SELECT \"DeptManager\"";
		sql += "					  ,SUM(\"PerfCnt\") \"sumPerfCnt\"";
		sql += "					  ,SUM(\"PerfAmt\") \"sumPerfAmt\"";
		sql += "			    FROM \"PfItDetail\" ";
		sql += "        		WHERE \"WorkMonth\">= :iWKs ";
		sql += "        		  AND \"WorkMonth\"<= :iWKe ";
		sql += "        		  AND \"DeptCode\" IN('A0B000','A0F000','A0E000','A0M000')";
		sql += "         		  AND \"DeptManager\" IS NOT NULL";
		sql += "         		GROUP BY \"DeptManager\") tab3 ";
		sql += "      ON tab1.\"DeptManager\"=tab3.\"DeptManager\"";
		sql += "      LEFT JOIN(SELECT \"Fullname\"";
		sql += "					  ,\"EmpNo\"";
		sql += "					  ,\"DeptCode\"";
		sql += "					  ,\"DepItem\"";
		sql += "					  ,\"DistItem\"";
		sql += "              	 	  ,\"AreaCode\" AS \"sDeptCode\"";
		sql += "					  ,\"GoalAmt\"";
		sql += "    		  	FROM \"PfBsOfficer\"";
		sql += "					 WHERE \"WorkMonth\"= :iyymm ";
		sql += "					   AND \"DistItem\" = '房貸部專') tab4";
		sql += "	  ON tab4.\"DeptCode\"=tab1.\"DeptCode\"";
		sql += "      ORDER BY \"wmAmt\" DESC";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iyymm", iYYMM);
		query.setParameter("iWKs", iWKs);
		query.setParameter("iWKe", iWKe);

		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findMortgageSpecialist(TitaVo titaVo, Map<String, String> wkVo) throws Exception {

		String iYYMM = wkVo.get("F2");

		logger.info("LP004.findAsDept iYYMM=" + iYYMM);

		String sql = "SELECT \"Fullname\"";
		sql += "			,\"DeptCode\"";
		sql += "			,\"DistItem\"";
		sql += "	  FROM \"PfBsOfficer\"";
		sql += "	  WHERE \"WorkMonth\"= :iyymm ";
		sql += "		AND \"DistItem\" not like '%部專'";

		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iyymm", iYYMM);

		return this.convertToMap(query.getResultList());

	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAsDept(TitaVo titaVo, Map<String, String> wkVo) throws Exception {
		
		String iYEAR = wkVo.get("F0");
		int iMM = Integer.valueOf(wkVo.get("F1"));
		String iYYMM = wkVo.get("F2");
		// 季初的工作月
		String iWKs = "";
		// 季末的工作月
		String iWKe = "";
		if (iMM < 4) {
			iWKs = iYEAR + "01";
			iWKe = iYEAR + String.format("%02d", iMM);
		} else if (iMM < 7) {
			iWKs = iYEAR + "04";
			iWKe = iYEAR + String.format("%02d", iMM);
		} else if (iMM < 10) {
			iWKs = iYEAR + "07";
			iWKe = iYEAR + String.format("%02d", iMM);
		} else {
			iWKs = iYEAR + "10";
			iWKe = iYEAR + String.format("%02d", iMM);
		}

		logger.info("LP004.findAsDept iYYMM=" + iYYMM + ",iWKSSN=" + iWKs + "~" + iWKe);

		String sql = "SELECT (CASE";
		sql += "   			   WHEN tab1.\"DeptCode\"='A0B000' THEN '營管部'";
		sql += "      		   WHEN tab1.\"DeptCode\"='A0F000' THEN '營推部'";
		sql += "      		   WHEN tab1.\"DeptCode\"='A0E000' THEN '業推部'";
		sql += "        	   WHEN tab1.\"DeptCode\"='A0M000' THEN '業開部'";
		sql += "          	   WHEN tab1.\"DeptCode\"='A0X000' THEN '專業行銷課'";
		sql += "          	   ELSE null END) AS \"DeptName\"";
		sql += "    		,tab1.\"DistItem\"";
		sql += "			,tab1.\"Fullname\"";
		sql += "    		,(CASE";
		sql += "    		    WHEN tab2.\"sumPerfCnt\">0 THEN tab2.\"sumPerfCnt\"";
		sql += "    		  ELSE 0 END) AS \"wmCnt\"";
		sql += "        	,(CASE";
		sql += "    		    WHEN tab2.\"sumPerfAmt\">0 THEN tab2.\"sumPerfAmt\"";
		sql += "    		 ELSE 0 END) AS \"wmAmt\"";
		sql += "     		,(CASE";
		sql += "   			    WHEN tab3.\"sumPerfCnt\">0 THEN tab3.\"sumPerfCnt\"";
		sql += "    		  ELSE 0 END) AS \"QCnt\"";
		sql += "        	,(CASE";
		sql += "    		    WHEN tab3.\"sumPerfAmt\">0 THEN tab3.\"sumPerfAmt\"";
		sql += "    		  ELSE 0 END) AS \"QAmt\"";
		sql += "      FROM(SELECT DISTINCT(CB.\"DistManager\")";
		sql += "				 ,CB.\"DistCode\"";
		sql += "				 ,CB.\"DistItem\"";
		sql += "				 ,CB.\"DeptCode\"";
		sql += "				 ,CB.\"DeptItem\"";
		sql += "				 ,CE.\"Fullname\"";
		sql += "	 	   FROM \"CdBcm\" CB";
		sql += "      	   LEFT JOIN \"CdEmp\" CE";
		sql += "      	   ON CE.\"EmployeeNo\"=CB.\"DistManager\"";
		sql += "       	   WHERE CB.\"DeptCode\" IN('A0B000','A0F000','A0E000','A0M000')";
		sql += "        	 AND CB.\"DistManager\" IS NOT NULL";
		sql += "         	 AND CB.\"DistCode\" IS NOT NULL) tab1";
		sql += "      LEFT JOIN(SELECT \"DistManager\"";
		sql += "					  ,SUM(\"PerfCnt\") \"sumPerfCnt\"";
		sql += "					  ,SUM(\"PerfAmt\") \"sumPerfAmt\"";
		sql += "			    FROM \"PfItDetail\" ";
		sql += "        	 	WHERE \"WorkMonth\"= :iyymm";
		sql += "        		  AND \"DeptCode\" IN('A0B000','A0F000','A0E000','A0M000')";
		sql += "         		  AND \"DistManager\" is not null";
		sql += "         		GROUP BY \"DistManager\") tab2";
		sql += "      ON tab1.\"DistManager\"=tab2.\"DistManager\" ";
		sql += "      LEFT JOIN(SELECT \"DistManager\"";
		sql += "					  ,SUM(\"PerfCnt\") \"sumPerfCnt\"";
		sql += "					  ,SUM(\"PerfAmt\") \"sumPerfAmt\"";
		sql += "			    FROM \"PfItDetail\" ";
		sql += "        		WHERE \"WorkMonth\">= :iWKs ";
		sql += "        		  AND \"WorkMonth\"<= :iWKe ";
		sql += "        		  AND \"DeptCode\" IN('A0B000','A0F000','A0E000','A0M000')";
		sql += "         		  AND \"DistManager\" IS NOT NULL";
		sql += "         		GROUP BY \"DistManager\") tab3 ";
		sql += "      ON tab1.\"DistManager\"=tab3.\"DistManager\"";
		sql += "      ORDER BY \"wmAmt\" DESC";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iyymm", iYYMM);
		query.setParameter("iWKs", iWKs);
		query.setParameter("iWKe", iWKe);

		return this.convertToMap(query.getResultList());

	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAll(TitaVo titaVo, Map<String, String> wkVo, String deptCode)
			throws Exception {
		String sdeptCode = "";
		String iYEAR = wkVo.get("F0");
		int iMM = Integer.valueOf(wkVo.get("F1"));
		String iYYMM = wkVo.get("F2");
		// 季初的工作月
		String iWKs = "";
		// 季末的工作月
		String iWKe = "";
		if (iMM < 4) {
			iWKs = iYEAR + "01";
			iWKe = iYEAR + String.format("%02d", iMM);
		} else if (iMM < 7) {
			iWKs = iYEAR + "04";
			iWKe = iYEAR + String.format("%02d", iMM);
		} else if (iMM < 10) {
			iWKs = iYEAR + "07";
			iWKe = iYEAR + String.format("%02d", iMM);
		} else {
			iWKs = iYEAR + "10";
			iWKe = iYEAR + String.format("%02d", iMM);
		}

		if (deptCode == "A0B000" || deptCode == "A0F000" || deptCode == "A0E000" || deptCode == "A0M000"
				|| deptCode == "A0X000") {
			sdeptCode = "'" + deptCode + "'";
		} else {
			sdeptCode = "'A0B000','A0F000','A0E000','A0M000'";
		}

		logger.info("LP004.findAll iYYMM=" + iYYMM + ",iWKSSN=" + iWKs + "~" + iWKe);

		String sql = "SELECT(CASE";
		sql += "   			   WHEN tab1.\"DeptCode\"='A0B000' THEN '營管部'";
		sql += "      		   WHEN tab1.\"DeptCode\"='A0F000' THEN '營推部'";
		sql += "      		   WHEN tab1.\"DeptCode\"='A0E000' THEN '業推部'";
		sql += "        	   WHEN tab1.\"DeptCode\"='A0M000' THEN '業開部'";
		sql += "          	   WHEN tab1.\"DeptCode\"='A0X000' THEN '專業行銷課'";
		sql += "          	   ELSE null END) AS \"DeptName\"";
		sql += "    		,tab1.\"DistItem\"";
		sql += "			,tab1.\"UnitItem\"";
		sql += "			,tab1.\"Fullname\"";
		sql += "    		,(CASE";
		sql += "    		    WHEN tab2.\"sumPerfCnt\">0 THEN tab2.\"sumPerfCnt\"";
		sql += "    		  ELSE 0 END) AS \"wmCnt\"";
		sql += "        	,(CASE";
		sql += "    		    WHEN tab2.\"sumPerfAmt\">0 THEN tab2.\"sumPerfAmt\"";
		sql += "    		 ELSE 0 END) AS \"wmAmt\"";
		sql += "     		,(CASE";
		sql += "   			    WHEN tab3.\"sumPerfCnt\">0 THEN tab3.\"sumPerfCnt\"";
		sql += "    		  ELSE 0 END) AS \"QCnt\"";
		sql += "        	,(CASE";
		sql += "    		    WHEN tab3.\"sumPerfAmt\">0 THEN tab3.\"sumPerfAmt\"";
		sql += "    		  ELSE 0 END) AS \"QAmt\"";
		sql += "      FROM(SELECT CB.\"DeptCode\"";
		sql += "				 ,CB.\"DeptItem\"";
		sql += "				 ,CB.\"DistCode\"";
		sql += "				 ,CB.\"DistItem\"";
		sql += "				 ,CB.\"UnitCode\"";
		sql += "				 ,CB.\"UnitItem\"";
		sql += "				 ,CB.\"UnitManager\"";
		sql += "				 ,CE.\"Fullname\" FROM \"CdBcm\" CB";
		sql += "      	   LEFT JOIN \"CdEmp\" CE";
		sql += "      	   ON CE.\"EmployeeNo\"=CB.\"UnitManager\"";
		sql += "       	   WHERE CB.\"DeptCode\" IN(" + sdeptCode + ")";
		sql += "        	 AND CB.\"UnitManager\" is not null";
		sql += "         	 AND CB.\"DistCode\" is not null";
		sql += "         	 AND CB.\"DistCode\" <> CB.\"UnitCode\") tab1";
		sql += "      LEFT JOIN(SELECT \"UnitManager\"";
		sql += "					  ,SUM(\"PerfCnt\") \"sumPerfCnt\"";
		sql += "					  ,SUM(\"PerfAmt\") \"sumPerfAmt\"";
		sql += "			    FROM \"PfItDetail\" ";
		sql += "        	 	WHERE \"WorkMonth\"= :iyymm";
		sql += "        		  AND \"DeptCode\" IN(" + sdeptCode + ")";
		sql += "         		  AND \"UnitManager\" is not null";
		sql += "         		GROUP BY \"UnitManager\") tab2";
		sql += "      ON tab1.\"UnitManager\"=tab2.\"UnitManager\" ";
		sql += "      LEFT JOIN(SELECT \"UnitManager\"";
		sql += "					  ,SUM(\"PerfCnt\") \"sumPerfCnt\"";
		sql += "					  ,SUM(\"PerfAmt\") \"sumPerfAmt\"";
		sql += "			    FROM \"PfItDetail\" ";
		sql += "        		WHERE \"WorkMonth\">= :iWKs ";
		sql += "        		  AND \"WorkMonth\"<= :iWKe ";
		sql += "        		  AND \"DeptCode\" IN(" + sdeptCode + ")";
		sql += "         		  AND \"UnitManager\" is not null";
		sql += "         		GROUP BY \"UnitManager\") tab3 ";
		sql += "      ON tab1.\"UnitManager\"=tab3.\"UnitManager\"";
		sql += "      ORDER BY \"wmAmt\" DESC";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iyymm", iYYMM);
		query.setParameter("iWKs", iWKs);
		query.setParameter("iWKe", iWKe);

		return this.convertToMap(query.getResultList());

	}
}

