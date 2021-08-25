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
public class LM070ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		String iENTDY = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000);
		String iYEAR = iENTDY.substring(0, 4);

		this.info("lM070.findAll wkyear=" + iYEAR + ",iday=" + iENTDY);
		
		String sql = " ";
		sql += "	SELECT I.\"CustNo\"";
		sql += "	      ,I.\"FacmNo\"";
		sql += "	      ,F.\"UtilBal\"";
		sql += "	      ,I.\"PieceCode\"";
		sql += "	      ,NVL(I.\"CntingCode\", 'N') AS \"CntingCode\"";
		sql += "	      ,L.\"totalDrawdownAmt\"";
		sql += "	      ,NVL(I.\"Introducer\", C.\"Introducer\") AS \"Introducer\"";
		sql += "	      ,C.\"CustId\"";
		sql += "	      ,NVL(I.\"DeptCode\", E4.\"CenterCode2\") AS \"DeptCode\"";
		sql += "	      ,NVL(I.\"DistCode\", E4.\"CenterCode1\") AS \"DistCode\"";
		sql += "	      ,NVL(I.\"UnitCode\", E4.\"CenterCode\") AS \"UnitCode\"";
		sql += "	      ,NVL(B2.\"UnitItem\",E4.\"CenterCode2Name\") AS \"ItDeptItem\"";
		sql += " 	      ,NVL(B3.\"UnitItem\", E4.\"CenterCode1Name\") AS \"ItDistItem\"";
		sql += "	      ,NVL(B1.\"UnitItem\", E4.\"CenterCodeName\") AS \"ItUnitIem\"";
		sql += "	      ,F.\"FirstDrawdownDate\"";
		sql += "	      ,I.\"ProdCode\"";
		sql += "	      ,I.\"PerfEqAmt\"";
		sql += " 	      ,I.\"PerfReward\"";
		sql += " 	      ,NVL(E2.\"AgentId\", E5.\"AgentId\") AS \"UnitAgentId\"";
		sql += "	      ,NVL(E2.\"AgentId\", E5.\"AgentId\") AS \"DistAgentId\"";
		sql += "	      ,R.\"IntroducerAddBonus\"";
		sql += "	FROM(SELECT I.\"CustNo\"";
		sql += " 	           ,I.\"FacmNo\"";
		sql += "			   ,SUM(I.\"DrawdownAmt\") AS \"DrawdownAmt\"";
		sql += "	           ,SUM(I.\"PerfEqAmt\")";
		sql += "	           ,SUM(I.\"PerfReward\")";
		sql += "	    FROM \"PfItDetail\" I";
		sql += "	    LEFT JOIN \"CdWorkMonth\" M ON M.\"StartDate\" <= :iday ";
		sql += "	                                AND M.\"EndDate\" >= :iday ";
		sql += "		WHERE I.\"WorkMonth\" = ( M.\"Year\" * 100 + M.\"Month\" )";
		sql += "	      AND I.\"PerfDate\" >= M.\"StartDate\"";
		sql += "	  	  AND I.\"PerfDate\" <= M.\"EndDate\"";
		sql += "	      AND I.\"PieceCode\" IN ('A','1')";
		sql += "	      AND I.\"DistCode\" IS NOT NULL";
		sql += "	    GROUP BY I.\"CustNo\"";
		sql += "	            ,I.\"FacmNo\") IM";
		sql += "	LEFT JOIN (SELECT I.* ";
		sql += "	                 ,ROW_NUMBER() OVER(";
		sql += "		  				PARTITION BY I.\"CustNo\", I.\"FacmNo\"";
		sql += "	        	        ORDER BY";
		sql += "	                    I.\"DrawdownDate\" ASC ) AS \"SEQ\"";
		sql += " 	          FROM \"PfItDetail\" I";
		sql += " 	    	   LEFT JOIN \"CdWorkMonth\" M ON M.\"StartDate\" <= :iday ";
		sql += "  	                              		  AND M.\"EndDate\" >= :iday ";
		sql += "			   WHERE I.\"WorkMonth\" = ( M.\"Year\" * 100 + M.\"Month\" )";
		sql += "	      	     AND I.\"PerfDate\" >= M.\"StartDate\"";
		sql += " 	 	  		 AND I.\"PerfDate\" <= M.\"EndDate\"";
		sql += " 	     		 AND I.\"PieceCode\" IN ('A','1')";
		sql += " 	     		 AND I.\"DistCode\" IS NOT NULL ) I ";
		sql += "	 ON I.\"CustNo\" = IM.\"CustNo\"";
		sql += "	AND I.\"FacmNo\" = IM.\"FacmNo\"";
		sql += "	AND I.\"SEQ\" = 1";
		sql += "	LEFT JOIN ( SELECT  L.\"CustNo\"";
		sql += "	  	 			  , SUM(L.\"DrawdownAmt\") AS \"totalDrawdownAmt\"";
		sql += "	            FROM \"LoanBorMain\" L";
		sql += " 	     	    LEFT JOIN \"CdWorkMonth\" M ON M.\"StartDate\" <= :iday ";
		sql += " 	                            		   AND M.\"EndDate\" >= :iday ";
		sql += "	        	WHERE L.\"DrawdownDate\" >= M.\"StartDate\"";
		sql += "	              AND L.\"DrawdownDate\" <= M.\"EndDate\"";
		sql += "	        	GROUP BY L.\"CustNo\" ) L";
		sql += "	 ON L.\"CustNo\" = I.\"CustNo\"";
		sql += "	LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = I.\"CustNo\"";
		sql += "	LEFT JOIN \"CdEmp\" E1 ON E1.\"EmployeeNo\" = I.\"Introducer\""; // 取介紹人姓名
		sql += "	LEFT JOIN \"CdEmp\" E2 ON E2.\"EmployeeNo\" = I.\"UnitManager\""; // 取處經理姓名(介紹人)
		sql += "	LEFT JOIN \"CdEmp\" E3 ON E3.\"EmployeeNo\" = I.\"DistManager\""; // 取區經理姓名(介紹人)
		sql += "	LEFT JOIN \"CdBcm\" B1 ON B1.\"UnitCode\" = I.\"UnitCode\""; // 取單位中文(介紹人)
		sql += "	LEFT JOIN \"CdBcm\" B2 ON B2.\"UnitCode\" = I.\"DeptCode\""; // 取部室中文(介紹人)
		sql += "	LEFT JOIN \"CdBcm\" B3 ON B3.\"UnitCode\" = I.\"DistCode\""; // 取區部中文(介紹人)
		sql += "	LEFT JOIN \"CdEmp\" E4 ON E4.\"EmployeeNo\" = C.\"Introducer\"";
		sql += "	LEFT JOIN \"CdBcm\" B4 ON B4.\"UnitCode\" = E4.\"CenterCode\"";
		sql += "	                      AND B4.\"DistCode\" = E4.\"CenterCode1\"";
		sql += "	                      AND B4.\"DeptCode\" = E4.\"CenterCode2\"";
		sql += "	LEFT JOIN \"CdEmp\" E5 ON E5.\"EmployeeNo\" = B4.\"UnitManager\"";
		sql += "	LEFT JOIN \"CdEmp\" E6 ON E6.\"EmployeeNo\" = B4.\"DistManager\"";
		sql += "	LEFT JOIN \"CdWorkMonth\" M ON M.\"StartDate\" <= :iday ";
		sql += "	                           AND M.\"EndDate\" >= :iday ";
		sql += "	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = I.\"CustNo\"";
		sql += "	                       AND F.\"FacmNo\" = I.\"FacmNo\"";
		sql += "	LEFT JOIN \"PfReward\" R ON R.\"CustNo\" = I.\"CustNo\"";
		sql += "	                        AND R.\"FacmNo\" = I.\"FacmNo\"";
		sql += "	                        AND R.\"BormNo\" = I.\"BormNo\"";
		sql += "	                        AND R.\"Introducer\" = I.\"Introducer\"";
		sql += "	WHERE  I.\"WorkMonth\" = ( M.\"Year\" * 100 + M.\"Month\" )";
		sql += "	  AND I.\"PerfDate\" >= M.\"StartDate\"";
		sql += "	  AND I.\"PerfDate\" <= M.\"EndDate\"";
		sql += "	  AND I.\"PieceCode\" IN ('A', '1')";
		sql += "	ORDER BY I.\"CustNo\", I.\"FacmNo\"";

		this.info("sql=" + sql);

		// String sql2 = "SELECT NVL(E1.\"Fullname\", E4.\"Fullname\") AS \"ItName\""; // 介紹人姓名
		// sql2 += "            ,NVL(I.\"Introducer\", C.\"Introducer\") AS \"Introducer\""; // 員工代號(介紹人)
		// sql2 += "            ,E1.\"AgentId\"";
		// sql2 += "            ,C.\"CustName\""; // 戶名
		// sql2 += "            ,I.\"CustNo\""; // 戶號
		// sql2 += "            ,I.\"FacmNo\""; // 額度號碼
		// sql2 += "            ,F.\"FirstDrawdownDate\""; // 撥款日
		// sql2 += "            ,I.\"DrawdownAmt\""; // 撥款金額
		// sql2 += "            ,F.\"UtilBal\"";
		// sql2 += "            ,I.\"ProdCode\""; // 商品代碼
		// sql2 += "            ,I.\"PieceCode\""; // 計件代碼
		// sql2 += "            ,NVL(I.\"CntingCode\", 'N') AS \"CntingCode\""; // 是否計件
		// sql2 += "            ,NVL(I.\"DeptCode\", E4.\"CenterCode2\") AS \"DeptCode\""; // 部室代號(介紹人)
		// sql2 += "            ,NVL(I.\"DistCode\", E4.\"CenterCode1\") AS \"DistCode\""; // 區部代號(介紹人)
		// sql2 += "            ,NVL(I.\"UnitCode\", E4.\"CenterCode\") AS \"UnitCode\""; // 單位代號(介紹人)
		// sql2 += "            ,NVL(B2.\"UnitItem\", E4.\"CenterCode2Name\") AS \"ItDeptItem\""; // 部室中文(介紹人)
		// sql2 += "            ,NVL(B3.\"UnitItem\", E4.\"CenterCode1Name\") AS \"ItDistItem\""; // 區部中文(介紹人)
		// sql2 += "            ,NVL(B1.\"UnitItem\", E4.\"CenterCodeName\") AS \"ItUnitIem\""; // 單位中文(介紹人)
		// sql2 += "            ,NVL(E2.\"Fullname\", E5.\"Fullname\") AS \"ItUnitManager\""; // 處經理姓名(介紹人)
		// sql2 += "            ,NVL(E3.\"Fullname\", E6.\"Fullname\") AS \"ItDistManager\""; // 區經理姓名(介紹人)
		// sql2 += "            ,I.\"PerfEqAmt\""; // 換算業績
		// sql2 += "            ,I.\"PerfReward\""; // 業務報酬
		// sql2 += "            ,R.\"IntroducerBonus\"";
		// sql2 += "      FROM \"PfItDetail\" I";
		// sql2 += "      LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = I.\"CustNo\"";
		// sql2 += "      LEFT JOIN \"CdEmp\" E1 ON E1.\"EmployeeNo\" = I.\"Introducer\""; // 取介紹人姓名
		// sql2 += "      LEFT JOIN \"CdEmp\" E2 ON E2.\"EmployeeNo\" = I.\"UnitManager\""; // 取處經理姓名(介紹人)
		// sql2 += "      LEFT JOIN \"CdEmp\" E3 ON E3.\"EmployeeNo\" = I.\"DistManager\""; // 取區經理姓名(介紹人)
		// sql2 += "      LEFT JOIN \"CdBcm\" B1 ON B1.\"UnitCode\" = I.\"UnitCode\""; // 取單位中文(介紹人)
		// sql2 += "      LEFT JOIN \"CdBcm\" B2 ON B2.\"UnitCode\" = I.\"DeptCode\""; // 取部室中文(介紹人)
		// sql2 += "      LEFT JOIN \"CdBcm\" B3 ON B3.\"UnitCode\" = I.\"DistCode\""; // 取區部中文(介紹人)
		// sql2 += "      LEFT JOIN \"CdEmp\" E4 ON E4.\"EmployeeNo\" = C.\"Introducer\"";
		// sql2 += "      LEFT JOIN \"CdBcm\" B4 ON B4.\"UnitCode\" = E4.\"CenterCode\"";
		// sql2 += "                            AND B4.\"DistCode\" = E4.\"CenterCode1\"";
		// sql2 += "                            AND B4.\"DeptCode\" = E4.\"CenterCode2\"";
		// sql2 += "      LEFT JOIN \"CdEmp\" E5 ON E5.\"EmployeeNo\" = B4.\"UnitManager\"";
		// sql2 += "      LEFT JOIN \"CdEmp\" E6 ON E6.\"EmployeeNo\" = B4.\"DistManager\"";
		// sql2 += "      LEFT JOIN \"CdWorkMonth\" M on (M.\"Year\" * 100 + M.\"Month\") = I.\"WorkMonth\"";
		// sql2 += "      LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = I.\"CustNo\"";
		// sql2 += "                             AND F.\"FacmNo\" = I.\"FacmNo\"";
		// sql2 += "      LEFT JOIN \"PfReward\" R ON R.\"CustNo\" = I.\"CustNo\"";
		// sql2 += "                              AND R.\"FacmNo\" = I.\"FacmNo\"";
		// sql2 += "                              AND R.\"BormNo\" = I.\"BormNo\"";
		// sql2 += "                              AND R.\"Introducer\" = I.\"Introducer\"";
		// sql2 += "      WHERE M.\"StartDate\" <= :entdy";
		// sql2 += "        AND M.\"EndDate\" >= :entdy";
		// sql2 += "        AND I.\"PieceCode\" IN ('A', '1')";
		// sql2 += "      ORDER BY I.\"CustNo\", I.\"FacmNo\"";
		// this.info("sql2=" + sql2);
		
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
//		query.setParameter("iwkyear", iYEAR);
		query.setParameter("iday", iENTDY);

		return this.convertToMap(query.getResultList());
	}


}