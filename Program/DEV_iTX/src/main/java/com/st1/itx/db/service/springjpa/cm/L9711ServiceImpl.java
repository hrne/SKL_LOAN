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

@Service("l9711ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L9711ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L9711ServiceImpl.class);

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

		logger.info("L9711.findAll");

		String iSDAY = String.valueOf(Integer.valueOf(titaVo.get("ACCTDATE_ST")) + 19110000);
		String iEDAY = String.valueOf(Integer.valueOf(titaVo.get("ACCTDATE_ED")) + 19110000);

		String iCUSTNO = titaVo.get("CUSTNO");
		logger.info("iCUSTNO="+iCUSTNO+",iSDAY="+iSDAY+",iEDAY="+iEDAY);
		String sql = "";
		sql += "SELECT * FROM(";
		sql += "	SELECT ' ' F0";
		sql += "        ,Cl.\"CityCode\" F1";
		sql += "        ,Ci.\"CityItem\" F2";
		sql += "        ,E.\"Fullname\" F3";
		sql += "        ,M.\"CustNo\" F4";
		sql += "        ,M.\"FacmNo\" F5";
		sql += "		,C.\"CustName\" F6";
		sql += "		,F.\"ApplNo\" F7";
		sql += "        ,M.\"MaturityDate\" F8";
		sql += "        ,F.\"LineAmt\" F9";
		sql += "		,M.\"LoanBal\" F10";
		sql += "        ,F.\"FirstDrawdownDate\" F11";
		sql += "        ,M.\"PrevPayIntDate\" F12";
		sql += "        ,M.\"StoreRate\" F13";
		sql += "        ,T.\"TelArea\" || T.\"TelNo\" || T.\"TelExt\" F14";
		sql += "        ,T.\"LiaisonName\" F15";
		sql += "        ,M.\"NextRepayDate\" F16";
		sql += "        ,C.\"CurrZip3\" F17";
		sql += "        ,C.\"CurrZip2\" F18";
		sql += "        ,C2.\"CityItem\" F19";
		sql += "        ,C3.\"AreaItem\" F20";
		sql += "        ,C.\"CurrRoad\" F21";
		sql += "        ,C.\"CurrSection\" F22";
		sql += "        ,C.\"CurrAlley\" F23";
		sql += "        ,C.\"CurrLane\" F24";
		sql += "        ,C.\"CurrNum\" F25";
		sql += "        ,C.\"CurrNumDash\" F26";
		sql += "        ,C.\"CurrFloor\" F27";
		sql += "        ,C.\"CurrFloorDash\" F28";
		sql += "        ,F.\"RepayCode\" F29";
		sql += "        ,M.\"AmortizedCode\" F30";
		sql += "        ,F.\"AcctCode\" F31";
		// sql += "        ,M.\"BormNo\" T1";
		sql += "        ,ROW_NUMBER() OVER (PARTITION BY M.\"CustNo\", M.\"FacmNo\" ORDER BY T.\"TelTypeCode\") AS SEQ";
		sql += "	FROM(SELECT M.\"CustNo\"";
		sql += "			   ,M.\"FacmNo\"";
		sql += "			   ,M.\"BormNo\"";
		sql += "			   ,M.\"MaturityDate\"";
		sql += "               ,M.\"LoanBal\"";
		sql += "			   ,M.\"PrevPayIntDate\"";
		sql += "               ,M.\"StoreRate\"";
		sql += "			   ,M.\"NextRepayDate\"";
		sql += "			   ,M.\"AmortizedCode\"";
		sql += "	     FROM \"LoanBorMain\" M";
		sql += "       WHERE M.\"MaturityDate\"  >= :isday ";
		sql += "         AND M.\"MaturityDate\"  <= :ieday ";
		if (!iCUSTNO.equals("0000000")) {
			sql += "          AND  M.\"CustNo\" = :icustno ";
		}
		sql += "         AND  M.\"Status\" = 0 ) M";
		sql += "	LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\"";
		sql += "  LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "						   AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "	LEFT JOIN \"ClFac\" CF ON CF.\"CustNo\" = M.\"CustNo\"";
		sql += "						  AND CF.\"FacmNo\" = M.\"FacmNo\"";
		sql += "						  AND CF.\"MainFlag\" = 'Y' ";
		sql += "	LEFT JOIN \"ClMain\" Cl ON Cl.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "						   AND Cl.\"ClCode2\" =  CF.\"ClCode2\"";
		sql += "						   AND Cl.\"ClNo\" = CF.\"ClNo\"";
		sql += "	LEFT JOIN \"CdCity\" Ci ON Ci.\"CityCode\" = Cl.\"CityCode\"";
		sql += "	LEFT JOIN \"CdCity\" C2 ON C2.\"CityCode\" = C.\"CurrCityCode\"";
		sql += "	LEFT JOIN \"CdArea\" C3 ON C3.\"CityCode\" = C.\"CurrCityCode\"";
		sql += "						   AND C3.\"AreaCode\" = C.\"CurrAreaCode\"";
		sql += "	LEFT JOIN \"CdEmp\" E ON E.\"EmployeeNo\" = F.\"BusinessOfficer\"";
		sql += "	LEFT JOIN \"CustTelNo\" T ON  T.\"CustUKey\" = C.\"CustUKey\"";
		sql += "							 AND T.\"Enable\"   = 'Y' ) D";
		sql += "	WHERE D.\"SEQ\" = 1 ";
		sql += "	ORDER BY D.\"F1\"";
		sql += "			,D.\"F3\"";
		sql += "			,D.\"F4\"";
		sql += "			,D.\"F5\"";

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