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
		sql += "		  ,LO.\"thisAmt\"";
		sql += "	      ,NVL(PDA.\"AdjCntingCode\",I.\"PieceCode\") AS \"PieceCode\"";
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
		sql += "	      ,NVL(PDA.\"AdjPerfEqAmt\",I.\"PerfEqAmt\") AS \"PerfEqAmt\"";
		sql += " 	      ,NVL(PDA.\"AdjPerfReward\",I.\"PerfReward\") AS \"PerfReward\"";
		sql += " 	      ,NVL(E2.\"AgentId\", E5.\"AgentId\") AS \"UnitAgentId\"";
		sql += "	      ,NVL(E3.\"AgentId\", E6.\"AgentId\") AS \"DistAgentId\"";
		sql += "	      ,NVL(PDA.\"AdjPerfAmt\",R.\"IntroducerAddBonus\") AS \"IntroducerAddBonus\"";
		sql += "	FROM(SELECT I.\"CustNo\"";
		sql += " 	           ,I.\"FacmNo\"";
		sql += "			   ,SUM(I.\"DrawdownAmt\") AS \"DrawdownAmt\"";
		sql += "	           ,SUM(I.\"PerfEqAmt\")";
		sql += "	           ,SUM(I.\"PerfReward\")";
		sql += "	    FROM \"PfItDetail\" I";
		sql += "		WHERE I.\"WorkMonth\" = ( :inputYear * 100 + :inputMonth )";
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
		sql += "			  WHERE I.\"WorkMonth\" = ( :inputYear * 100 + :inputMonth )";
		sql += " 	     		 AND I.\"PieceCode\" IN ('A','1')";
		sql += " 	     		 AND I.\"DistCode\" IS NOT NULL ) I ";
		sql += "	 ON I.\"CustNo\" = IM.\"CustNo\"";
		sql += "	AND I.\"FacmNo\" = IM.\"FacmNo\"";
		sql += "	AND I.\"SEQ\" = 1";
		sql += "	LEFT JOIN ( SELECT  L.\"CustNo\"";
		sql += "	  	 			  , SUM(L.\"DrawdownAmt\") AS \"totalDrawdownAmt\"";
		sql += "	            FROM \"LoanBorMain\" L";
		sql += " 	     	    LEFT JOIN \"CdWorkMonth\" M ON M.\"Year\" = :inputYear ";
		sql += " 	                            		   AND M.\"Month\" = :inputMonth ";
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
		sql += "	LEFT JOIN \"CdWorkMonth\" M ON M.\"Year\" <= :inputYear ";
		sql += "	                           AND M.\"Month\" >= :inputMonth ";
		sql += "	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = I.\"CustNo\"";
		sql += "	                       AND F.\"FacmNo\" = I.\"FacmNo\"";
		sql += "	LEFT JOIN \"PfReward\" R ON R.\"CustNo\" = I.\"CustNo\"";
		sql += "	                        AND R.\"FacmNo\" = I.\"FacmNo\"";
		sql += "	                        AND R.\"BormNo\" = I.\"BormNo\"";
		sql += "	                        AND R.\"Introducer\" = I.\"Introducer\"";
		sql += "	                        AND R.\"WorkMonth\" = (M.\"Year\" * 100) + M.\"Month\"";
		sql += "	LEFT JOIN \"PfItDetailAdjust\" PDA ON R.\"CustNo\" = I.\"CustNo\"";
		sql += "	                        	      AND R.\"FacmNo\" = I.\"FacmNo\"";
		sql += "	                                  AND R.\"WorkMonth\" = (M.\"Year\" * 100) + M.\"Month\"";
		sql += "	LEFT JOIN ( SELECT  L.\"CustNo\"";
		sql += "					  , L.\"FacmNo\"";
		sql += "	  	 			  , SUM(L.\"TxAmt\") AS \"thisAmt\"";
		sql += "	            FROM \"LoanBorTx\" L";
		sql += " 	     	    LEFT JOIN \"CdWorkMonth\" M ON M.\"Year\" = :inputYear ";
		sql += " 	                            		   AND M.\"Month\" = :inputMonth ";
		sql += "	        	WHERE L.\"AcDate\" >= M.\"StartDate\"";
		sql += "	              AND L.\"AcDate\" <= M.\"EndDate\"";
		sql += "	        	GROUP BY L.\"CustNo\"";
		sql += "						,L.\"FacmNo\") LO";
		sql += "	 ON LO.\"CustNo\" = I.\"CustNo\" AND LO.\"FacmNo\" = I.\"FacmNo\"";
		sql += "	WHERE  I.\"WorkMonth\" =  :inputYear * 100  + :inputMonth ";
		sql += "	ORDER BY I.\"CustNo\", I.\"FacmNo\"";

		this.info("sql=" + sql);
		this.info("workYear" + (Integer.valueOf(titaVo.getParam("inputYear")) + 1911));
		this.info("workMonth" + Integer.valueOf(titaVo.getParam("inputMonth")));
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

//		query.setParameter("iday", iENTDY);
		
		query.setParameter("inputYear", Integer.valueOf(titaVo.getParam("inputYear")) + 1911);
		query.setParameter("inputMonth", Integer.valueOf(titaVo.getParam("inputMonth")));
//		query.setParameter("inputMonthStart", titaVo.getParam("inputMonthStart"));
//		query.setParameter("inputMonthEnd", titaVo.getParam("inputMonthEnd"));

		return this.convertToMap(query);
	}

}