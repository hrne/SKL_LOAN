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
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service("l9710ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L9710ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L9710ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		logger.info("L9710.findAll");

		String iSDAY = String.valueOf(Integer.valueOf(titaVo.get("ACCTDATE_ST")) + 19110000);
		String iEDAY = String.valueOf(Integer.valueOf(titaVo.get("ACCTDATE_ED")) + 19110000);
		String iCUSTNO = titaVo.get("CustNo");
		
		logger.info("iSDAY:"+iSDAY+",iEDAY:"+iEDAY+",iCUSTNO:"+iCUSTNO);
		
		String sql = " SELECT * ";
		sql += "       FROM ( SELECT               ' ' F0";
		sql += "                     , Cl.\"CityCode\" F1";
		sql += "                     , Ci.\"CityItem\" F2";
		sql += "                     , E.\"Fullname\" F3";
		sql += "                     , M.\"CustNo\" F4";
		sql += "                     , M.\"FacmNo\" F5";
		sql += "                     , C.\"CustName\" F6";
		sql += "                     , F.\"ApplNo\" F7";
		sql += "                     , M.\"GraceDate\" F8";
		sql += "                     , F.\"LineAmt\" F9";
		sql += "                     , M.\"LoanBal\" F10";
		sql += "                     , F.\"FirstDrawdownDate\" F11";
		sql += "                     , M.\"PrevPayIntDate\" F12";
		sql += "                     , M.\"StoreRate\" F13";
		sql += "					 ,DECODE(T.\"TelArea\",NULL,T.\"TelNo\",T.\"TelArea\" || '-' || T.\"TelNo\") F14";
		sql += "                     , T.\"LiaisonName\" F15";
		sql += "                     , M.\"NextRepayDate\" F16";
		sql += "                     , C.\"CurrZip3\" F17";
		sql += "                     , C.\"CurrZip2\" F18";
		sql += "                     , C2.\"CityItem\" F19";	//城市名
		sql += "                     , C3.\"AreaItem\" F20";	//市區
		sql += "                     , C.\"CurrRoad\" F21"; 	//路名
		sql += "                     , C.\"CurrSection\" F22"; 	//段
		sql += "                     , C.\"CurrAlley\" F23";	//巷
		sql += "                     , C.\"CurrLane\" F24";		//弄
		sql += "                     , C.\"CurrNum\" F25";		//號
		sql += "                     , C.\"CurrNumDash\" F26";	//號之
		sql += "                     , C.\"CurrFloor\" F27";	//樓
		sql += "                     , C.\"CurrFloorDash\" F28";//樓之
		sql += "                     , F.\"RepayCode\" F29";
//		sql += "                     , M.\"BormNo\" T1";
		sql += "                     , ROW_NUMBER() OVER ( PARTITION BY M.\"CustNo\", M.\"FacmNo\"";
		sql += "                                               ORDER BY T.\"TelTypeCode\") AS SEQ";
		sql += "              FROM ( SELECT  M.\"CustNo\"";
		sql += "                           , M.\"FacmNo\"";
//		sql += "                           , M.\"BormNo\"";
		sql += "                           , M.\"GraceDate\"";
		sql += "                           , M2.\"LoanBal\"";
		sql += "                           , M.\"PrevPayIntDate\"";
		sql += "                           , M.\"StoreRate\"";
		sql += "                           , M.\"NextRepayDate\"";
		sql += "                     FROM \"LoanBorMain\" M ";
		sql += "                     LEFT JOIN (";
		sql += "						  SELECT \"CustNo\"";
		sql += "								,\"FacmNo\"";
		sql += "								,SUM(\"LoanBal\") \"LoanBal\"";
		sql += " 						  FROM \"LoanBorMain\" M";
		sql += "                     	  WHERE \"GraceDate\"  >= :isday";
		sql += "                       		AND  \"GraceDate\" <= :ieday";
		sql += "                       		AND  \"LoanBal\"  > 0";
		if (!iCUSTNO.equals("0000000")) {
		sql += "                       		AND  \"CustNo\"     =  :icustno";
		}
		sql += "             			  GROUP BY \"CustNo\"";
		sql += "             					  ,\"FacmNo\"";
		sql += "                     ) M2 ON M2.\"CustNo\" = M.\"CustNo\"";
		sql += "                     	 AND M2.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                     WHERE M.\"GraceDate\"  >= :isday";
		sql += "                       AND  M.\"GraceDate\"  <= :ieday";
		sql += "                       AND  M.\"LoanBal\"  > 0";
		if (!iCUSTNO.equals("0000000")) {
		sql += "                       AND  M.\"CustNo\"     =  :icustno";
		}
		sql += "                                                           ) M";
		sql += "              LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\"";
		sql += "              LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "                                     AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "              LEFT JOIN \"ClFac\" CF ON CF.\"CustNo\" = M.\"CustNo\"";
		sql += "                                    AND CF.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                    AND CF.\"MainFlag\" = 'Y'";
		sql += "              LEFT JOIN \"ClMain\" Cl ON Cl.\"ClCode1\" =  CF.\"ClCode1\"";
		sql += "                                     AND Cl.\"ClCode2\" =  CF.\"ClCode2\"";
		sql += "                                     AND Cl.\"ClNo\"    =  CF.\"ClNo\"";
		sql += "              LEFT JOIN \"CdCity\" Ci ON Ci.\"CityCode\" =  Cl.\"CityCode\"";
		sql += "              LEFT JOIN \"CdCity\" C2 ON C2.\"CityCode\" =  C.\"CurrCityCode\"";
		sql += "              LEFT JOIN \"CdArea\" C3 ON C3.\"CityCode\" =  C.\"CurrCityCode\"";
		sql += "                                     AND C3.\"AreaCode\" =  C.\"CurrAreaCode\"";
		sql += "              LEFT JOIN \"CdEmp\" E ON E.\"EmployeeNo\" =  F.\"BusinessOfficer\"";
		sql += "              LEFT JOIN \"CustTelNo\" T ON T.\"CustUKey\" =  C.\"CustUKey\"";
		sql += "                                       AND T.\"Enable\"   = 'Y' ) D";
		sql += "       WHERE D.\"SEQ\" = 1 ";
		sql += " 	   ORDER BY D.\"F1\", D.\"F3\", D.\"F4\"";

		logger.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("isday", iSDAY);
		query.setParameter("ieday", iEDAY);
		if (!iCUSTNO.equals("0000000")) {
			query.setParameter("icustno", iCUSTNO);
		}
		return this.convertToMap(query.getResultList());
	}

}