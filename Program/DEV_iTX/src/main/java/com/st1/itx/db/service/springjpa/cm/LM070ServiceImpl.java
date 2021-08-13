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
public class LM070ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM070ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
 
	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {


		String iENTDY = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000);
		String iYEAR = iENTDY.substring(0, 4);

		logger.info("lM070.findAll wkyear=" + iYEAR + ",entdy=" + iENTDY);
 
		String sql = "SELECT D.\"CustNo\" AS F0";				//戶號
		sql += "			,D.\"FacmNo\" AS F1";				//額度編號
		sql += "			,F.\"UtilBal\" AS F2";				//已動用額度餘額
		sql += "			,D.\"PieceCode\" AS F3";			//計件代碼
		sql += "			,D.\"CntingCode\" AS F4";			//是否計件
		sql += "			,F.\"UtilAmt\" AS F5";				//累計撥款金額
		sql += "			,D.\"Introducer\" AS F6";			//介紹人
		sql += "			,E0.\"AgentId\" AS F7";				//介紹人 業務人員身份證字號
		sql += "			,D.\"UnitCode\" AS F8";				//單位代號
		sql += "			,D.\"DistCode\" AS F9";				//區部代號
		sql += "			,D.\"DeptCode\" AS F10";			//部室代號
		sql += "			,B0.\"UnitItem\" AS F11";			//電腦編號 介紹人
		sql += "			,B1.\"UnitItem\" AS F12";			//電腦編號 處經理代號
		sql += "			,B2.\"UnitItem\" AS F13";			//電腦編號 區經理代號
		sql += "			,F.\"FirstDrawdownDate\" AS F14";	//初貸日
		sql += "			,F.\"BaseRateCode\" AS F15";		//指標利率代碼
		sql += "			,D.\"PerfEqAmt\" AS F16";			//業績換算
		sql += "			,D.\"PerfReward\" AS F17";			//業務報酬
		sql += "			,E1.\"AgentId\" AS F18";			//處經理 業務人員身份證字號
		sql += "			,E2.\"AgentId\" AS F19";			//區經理 業務人員身份證字號
		sql += "			,D.\"IntroducerAddBonus\" AS F20";	//介紹人加碼獎勵津貼
		sql += "	  FROM(SELECT D.\"CustNo\"";
		sql += "				 ,D.\"FacmNo\"";
		sql += "				 ,D.\"Introducer\"";
		sql += "				 ,D.\"PieceCode\"";
		sql += "				 ,D.\"CntingCode\"";
		sql += "				 ,D.\"UnitCode\"";
		sql += "				 ,D.\"DistCode\"";
		sql += "				 ,D.\"DeptCode\"";
		sql += "				 ,D.\"UnitManager\"";
		sql += "				 ,D.\"DistManager\"";
		sql += "				 ,SUM(D.\"PerfEqAmt\") \"PerfEqAmt\"";
		sql += "				 ,SUM(D.\"PerfReward\") \"PerfReward\"";
		sql += "				 ,SUM(P.\"IntroducerAddBonus\") \"IntroducerAddBonus\"";
		sql += "		   FROM \"PfItDetail\" D";
		sql += "		   LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = D.\"CustNo\"";
		sql += "		   LEFT JOIN \"PfReward\" P ON P.\"PerfDate\" = D.\"PerfDate\"";
		sql += "								   AND P.\"CustNo\" = D.\"CustNo\"";
		sql += "								   AND P.\"FacmNo\" = D.\"FacmNo\"";
		sql += "								   AND P.\"BormNo\" = D.\"BormNo\"";
		sql += "		   WHERE TRUNC(P.\"WorkMonth\" / 100) = :iwkyear";
		sql += "			 AND P.\"PerfDate\" <= :iday";
		sql += "			 AND C.\"EntCode\" = 0";
		sql += "		   GROUP BY D.\"CustNo\"";
		sql += "				   ,D.\"FacmNo\"";
		sql += "				   ,D.\"Introducer\"";
		sql += "				   ,D.\"PieceCode\"";
		sql += "				   ,D.\"CntingCode\"";
		sql += "				   ,D.\"UnitCode\"";
		sql += "				   ,D.\"DistCode\"";
		sql += "				   ,D.\"DeptCode\"";
		sql += "				   ,D.\"UnitManager\"";
		sql += "				   ,D.\"DistManager\") D";
		sql += "	  LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = D.\"CustNo\"";
		sql += "							 AND F.\"FacmNo\" = D.\"FacmNo\"";
		sql += "	  LEFT JOIN \"CdEmp\" E0 ON E0.\"EmployeeNo\" = D.\"Introducer\"";
		sql += "	  LEFT JOIN \"CdEmp\" E1 ON E1.\"EmployeeNo\" = D.\"UnitManager\"";
		sql += "	  LEFT JOIN \"CdEmp\" E2 ON E2.\"EmployeeNo\" = D.\"DistManager\"";
		sql += "	  LEFT JOIN \"CdBcm\" B0 ON B0.\"UnitCode\" = D.\"UnitCode\"";
		sql += "	  LEFT JOIN \"CdBcm\" B1 ON B1.\"UnitCode\" = D.\"DistCode\"";
		sql += "	  LEFT JOIN \"CdBcm\" B2 ON B2.\"UnitCode\" = D.\"DeptCode\"";
		// sql += "	  WHERE D.\"IntroducerAddBonus\" > 0";

		logger.info("sql=" + sql);

		
		String sql2 = "SELECT NVL(E1.\"Fullname\", E4.\"Fullname\") AS \"ItName\""; // 介紹人姓名
		sql2 += "            ,NVL(I.\"Introducer\", C.\"Introducer\") AS \"Introducer\""; // 員工代號(介紹人)
		sql2 += "            ,E1.\"AgentId\"";
		sql2 += "            ,C.\"CustName\""; // 戶名
		sql2 += "            ,I.\"CustNo\""; // 戶號
		sql2 += "            ,I.\"FacmNo\""; // 額度號碼
		sql2 += "            ,F.\"FirstDrawdownDate\""; // 撥款日
		sql2 += "            ,I.\"DrawdownAmt\""; // 撥款金額
		sql2 += "            ,F.\"UtilBal\"";
		sql2 += "            ,I.\"ProdCode\""; // 商品代碼
		sql2 += "            ,I.\"PieceCode\""; // 計件代碼
		sql2 += "            ,NVL(I.\"CntingCode\", 'N') AS \"CntingCode\""; // 是否計件
		sql2 += "            ,NVL(I.\"DeptCode\", E4.\"CenterCode2\") AS \"DeptCode\""; // 部室代號(介紹人)
		sql2 += "            ,NVL(I.\"DistCode\", E4.\"CenterCode1\") AS \"DistCode\""; // 區部代號(介紹人)
		sql2 += "            ,NVL(I.\"UnitCode\", E4.\"CenterCode\") AS \"UnitCode\""; // 單位代號(介紹人)
		sql2 += "            ,NVL(B2.\"UnitItem\", E4.\"CenterCode2Name\") AS \"ItDeptItem\""; // 部室中文(介紹人)
		sql2 += "            ,NVL(B3.\"UnitItem\", E4.\"CenterCode1Name\") AS \"ItDistItem\""; // 區部中文(介紹人)
		sql2 += "            ,NVL(B1.\"UnitItem\", E4.\"CenterCodeName\") AS \"ItUnitIem\""; // 單位中文(介紹人)
		sql2 += "            ,NVL(E2.\"Fullname\", E5.\"Fullname\") AS \"ItUnitManager\""; // 處經理姓名(介紹人)
		sql2 += "            ,NVL(E3.\"Fullname\", E6.\"Fullname\") AS \"ItDistManager\""; // 區經理姓名(介紹人)
		sql2 += "            ,I.\"PerfEqAmt\""; // 換算業績
		sql2 += "            ,I.\"PerfReward\""; // 業務報酬
		sql2 += "            ,R.\"IntroducerBonus\"";
		sql2 += "      FROM \"PfItDetail\" I";
		sql2 += "      LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = I.\"CustNo\"";
		sql2 += "      LEFT JOIN \"CdEmp\" E1 ON E1.\"EmployeeNo\" = I.\"Introducer\""; // 取介紹人姓名
		sql2 += "      LEFT JOIN \"CdEmp\" E2 ON E2.\"EmployeeNo\" = I.\"UnitManager\""; // 取處經理姓名(介紹人)
		sql2 += "      LEFT JOIN \"CdEmp\" E3 ON E3.\"EmployeeNo\" = I.\"DistManager\""; // 取區經理姓名(介紹人)
		sql2 += "      LEFT JOIN \"CdBcm\" B1 ON B1.\"UnitCode\" = I.\"UnitCode\""; // 取單位中文(介紹人)
		sql2 += "      LEFT JOIN \"CdBcm\" B2 ON B2.\"UnitCode\" = I.\"DeptCode\""; // 取部室中文(介紹人)
		sql2 += "      LEFT JOIN \"CdBcm\" B3 ON B3.\"UnitCode\" = I.\"DistCode\""; // 取區部中文(介紹人)
		sql2 += "      LEFT JOIN \"CdEmp\" E4 ON E4.\"EmployeeNo\" = C.\"Introducer\"";
		sql2 += "      LEFT JOIN \"CdBcm\" B4 ON B4.\"UnitCode\" = E4.\"CenterCode\"";
		sql2 += "                            AND B4.\"DistCode\" = E4.\"CenterCode1\"";
		sql2 += "                            AND B4.\"DeptCode\" = E4.\"CenterCode2\"";
		sql2 += "      LEFT JOIN \"CdEmp\" E5 ON E5.\"EmployeeNo\" = B4.\"UnitManager\"";
		sql2 += "      LEFT JOIN \"CdEmp\" E6 ON E6.\"EmployeeNo\" = B4.\"DistManager\"";
		sql2 += "      LEFT JOIN \"CdWorkMonth\" M on (M.\"Year\" * 100 + M.\"Month\") = I.\"WorkMonth\"";
		sql2 += "      LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = I.\"CustNo\"";
		sql2 += "                             AND F.\"FacmNo\" = I.\"FacmNo\"";
		sql2 += "      LEFT JOIN \"PfReward\" R ON R.\"CustNo\" = I.\"CustNo\"";
		sql2 += "                              AND R.\"FacmNo\" = I.\"FacmNo\"";
		sql2 += "                              AND R.\"BormNo\" = I.\"BormNo\"";
		sql2 += "                              AND R.\"Introducer\" = I.\"Introducer\"";
		sql2 += "      WHERE M.\"StartDate\" <= :entdy";
		sql2 += "        AND M.\"EndDate\" >= :entdy";
		sql2 += "        AND I.\"PieceCode\" IN ('A', '1')";
		sql2 += "      ORDER BY I.\"CustNo\", I.\"FacmNo\"";
		logger.info("sql2=" + sql2);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iwkyear", iYEAR);
		query.setParameter("iday", iENTDY);

		return this.convertToMap(query.getResultList());
	}

//	@SuppressWarnings("unchecked")
//	public List<Map<String, String>> wkSsn(TitaVo titaVo) throws Exception {
//
//		String iENTDY = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000);
//		logger.info("lW001.wkSsn ENTDY=" + iENTDY);
//
//		// 總表 f0:本日之工作年,f1:本日之工作月
//		String sql = "SELECT W.\"Year\" AS F0";
//		sql += "			,W.\"Month\" AS F1";
//		sql += "	  FROM \"CdWorkMonth\" W";
//		sql += "	  WHERE W.\"StartDate\" <= :iday";
//		sql += "	    AND W.\"EndDate\" >= :iday";
//		logger.info("sql=" + sql);
//		Query query;
//		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
//		query = em.createNativeQuery(sql);
//		query.setParameter("iday", iENTDY);
//		return this.convertToMap(query.getResultList());
//	}
	
//	@SuppressWarnings({ "unchecked" })
//	public List<Map<String, String>> findAll(TitaVo titaVo, Map<String, String> wkVo) throws Exception {
//
//		String iYEAR = wkVo.get("F0");
//
//		String iENTDY = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000);
//
//		logger.info("lM070.findAll wkyear=" + iYEAR + ",entdy=" + iENTDY);
//
//		String sql = "SELECT D.\"CustNo\" AS F0";
//		sql += "			,D.\"FacmNo\" AS F1";
//		sql += "			,F.\"UtilBal\" AS F2";
//		sql += "			,D.\"PieceCode\" AS F3";
//		sql += "			,D.\"CntingCode\" AS F4";
//		sql += "			,F.\"UtilAmt\" AS F5";
//		sql += "			,D.\"Introducer\" AS F6";
//		sql += "			,E0.\"AgentId\" AS F7";
//		sql += "			,D.\"UnitCode\" AS F8";
//		sql += "			,D.\"DistCode\" AS F9";
//		sql += "			,D.\"DeptCode\" AS F10";
//		sql += "			,B0.\"UnitItem\" AS F11";
//		sql += "			,B1.\"UnitItem\" AS F12";
//		sql += "			,B2.\"UnitItem\" AS F13";
//		sql += "			,F.\"FirstDrawdownDate\" AS F14";
//		sql += "			,F.\"BaseRateCode\" AS F15";
//		sql += "			,D.\"PerfEqAmt\" AS F16";
//		sql += "			,D.\"PerfReward\" AS F17";
//		sql += "			,E1.\"AgentId\" AS F18";
//		sql += "			,E2.\"AgentId\" AS F19";
//		sql += "			,D.\"IntroducerAddBonus\" AS F20";
//		sql += "	  FROM(SELECT D.\"CustNo\"";
//		sql += "				 ,D.\"FacmNo\"";
//		sql += "				 ,D.\"Introducer\"";
//		sql += "				 ,D.\"PieceCode\"";
//		sql += "				 ,D.\"CntingCode\"";
//		sql += "				 ,D.\"UnitCode\"";
//		sql += "				 ,D.\"DistCode\"";
//		sql += "				 ,D.\"DeptCode\"";
//		sql += "				 ,D.\"UnitManager\"";
//		sql += "				 ,D.\"DistManager\"";
//		sql += "				 ,SUM(D.\"PerfEqAmt\") \"PerfEqAmt\"";
//		sql += "				 ,SUM(D.\"PerfReward\") \"PerfReward\"";
//		sql += "				 ,SUM(P.\"IntroducerAddBonus\") \"IntroducerAddBonus\"";
//		sql += "		   FROM \"PfItDetail\" D";
//		sql += "		   LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = D.\"CustNo\"";
//		sql += "		   LEFT JOIN \"PfReward\" P ON P.\"PerfDate\" = D.\"PerfDate\"";
//		sql += "								   AND P.\"CustNo\" = D.\"CustNo\"";
//		sql += "								   AND P.\"FacmNo\" = D.\"FacmNo\"";
//		sql += "								   AND P.\"BormNo\" = D.\"BormNo\"";
//		sql += "		   WHERE TRUNC(P.\"WorkMonth\" / 100) = :iwkyear";
//		sql += "			 AND P.\"PerfDate\" <= :iday";
//		sql += "			 AND C.\"EntCode\" = 0";
//		sql += "		   GROUP BY D.\"CustNo\"";
//		sql += "				   ,D.\"FacmNo\"";
//		sql += "				   ,D.\"Introducer\"";
//		sql += "				   ,D.\"PieceCode\"";
//		sql += "				   ,D.\"CntingCode\"";
//		sql += "				   ,D.\"UnitCode\"";
//		sql += "				   ,D.\"DistCode\"";
//		sql += "				   ,D.\"DeptCode\"";
//		sql += "				   ,D.\"UnitManager\"";
//		sql += "				   ,D.\"DistManager\") D";
//		sql += "	  LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = D.\"CustNo\"";
//		sql += "							 AND F.\"FacmNo\" = D.\"FacmNo\"";
//		sql += "	  LEFT JOIN \"CdEmp\" E0 ON E0.\"EmployeeNo\" = D.\"Introducer\"";
//		sql += "	  LEFT JOIN \"CdEmp\" E1 ON E1.\"EmployeeNo\" = D.\"UnitManager\"";
//		sql += "	  LEFT JOIN \"CdEmp\" E2 ON E2.\"EmployeeNo\" = D.\"DistManager\"";
//		sql += "	  LEFT JOIN \"CdBcm\" B0 ON B0.\"UnitCode\" = D.\"UnitCode\"";
//		sql += "	  LEFT JOIN \"CdBcm\" B1 ON B1.\"UnitCode\" = D.\"DistCode\"";
//		sql += "	  LEFT JOIN \"CdBcm\" B2 ON B2.\"UnitCode\" = D.\"DeptCode\"";
//		sql += "	  WHERE D.\"IntroducerAddBonus\" > 0";
//
//		logger.info("sql=" + sql);
//
//		Query query;
//		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
//		query = em.createNativeQuery(sql);
//		query.setParameter("iwkyear", iYEAR);
//		query.setParameter("iday", iENTDY);
//
//		return this.convertToMap(query.getResultList());
//	}

}