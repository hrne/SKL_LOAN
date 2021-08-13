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
import com.st1.itx.eum.ContentName;

@Service("L4703ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4703ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L4703ServiceImpl.class);

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

		logger.info("L4703.findAll");

		String iFacmNo = titaVo.get("FacmNo");
		String iCustNo = titaVo.get("CustNo");

		String sql = " SELECT * FROM ( " + " SELECT   ' ' F0, Cl.\"CityCode\" F1, Ci.\"CityItem\" F2" + "        , E.\"Fullname\" F3, M.\"CustNo\" F4, M.\"FacmNo\" F5"
				+ "        , C.\"CustName\" F6, F.\"ApplNo\" F7, M.\"GraceDate\" F8" + "        , F.\"LineAmt\" F9, M.\"LoanBal\" F10"
				+ "        , F.\"FirstDrawdownDate\" F11, M.\"PrevPayIntDate\" F12" + "        , M.\"StoreRate\" F13" + "        , NVL(T.\"Mobile\", T.\"TelArea\" || T.\"TelNo\" || T.\"TelExt\") F14"
				+ "        , T.\"LiaisonName\" F15, M.\"NextRepayDate\" F16 " + "        , C.\"CurrZip3\" F17, C.\"CurrZip2\" F18, C2.\"CityItem\" F19"
				+ "        , C3.\"AreaItem\" F20, C.\"CurrRoad\" F21, C.\"CurrSection\" F22" + "        , C.\"CurrAlley\" F23, C.\"CurrLane\" F24, C.\"CurrNum\" F25"
				+ "        , C.\"CurrNumDash\" F26, C.\"CurrFloor\" F27, C.\"CurrFloorDash\" F28" + "        , F.\"RepayCode\" F29, M.\"BormNo\" T1" + "        , ROW_NUMBER() OVER (PARTITION BY "
				+ "              M.\"CustNo\", M.\"FacmNo\", M.\"BormNo\"" + "              ORDER BY T.\"TelTypeCode\") AS SEQ"
				+ " FROM (SELECT  M.\"CustNo\", M.\"FacmNo\", M.\"BormNo\", M.\"GraceDate\"" + "             , M.\"LoanBal\", M.\"PrevPayIntDate\""
				+ "             , M.\"StoreRate\", M.\"NextRepayDate\" " + "        FROM \"LoanBorMain\" M  " + "        WHERE  M.\"CustNo\"     =  : icustno";
		sql = sql + "       ) M" + " LEFT JOIN \"CustMain\" C" + "   ON  C.\"CustNo\"   =  M.\"CustNo\"" + " LEFT JOIN \"FacMain\" F" + "   ON  F.\"CustNo\"   =  M.\"CustNo\""
				+ "   AND F.\"FacmNo\"   =  M.\"FacmNo\"" + " LEFT JOIN \"ClFac\"   CF   " + "    ON CF.\"CustNo\"   =  M.\"CustNo\"" + "   AND CF.\"FacmNo\"   =  M.\"FacmNo\""
				+ "   AND CF.\"MainFlag\" =  'Y'" + " LEFT JOIN \"ClMain\"  Cl" + "   ON  Cl.\"ClCode1\" =  CF.\"ClCode1\"" + "   AND Cl.\"ClCode2\" =  CF.\"ClCode2\""
				+ "   AND Cl.\"ClNo\"    =  CF.\"ClNo\"" + " LEFT JOIN \"CdCity\" Ci" + "   ON Ci.\"CityCode\" =  Cl.\"CityCode\"" + " LEFT JOIN \"CdCity\" C2"
				+ "  ON  C2.\"CityCode\" =  C.\"CurrCityCode\"" + " LEFT JOIN \"CdArea\" C3" + "  ON  C3.\"CityCode\" =  C.\"CurrCityCode\"" + "  AND C3.\"AreaCode\" =  C.\"CurrAreaCode\""
				+ " LEFT JOIN \"CdEmp\" E" + "   ON E.\"EmployeeNo\" =  F.\"BusinessOfficer\"" + " LEFT JOIN \"CustTelNo\" T" + "  ON  T.\"CustUKey\" =  C.\"CustUKey\"" + "  AND T.\"Enable\"   = 'Y'"
				+ " ) D" + " WHERE D.\"SEQ\" = 1" + "   AND F.\"FacmNo\" = :iFacmNo" + " ORDER BY D.\"F0\", D.\"F1\"";

		logger.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		query.setParameter("iFacmNo", iFacmNo);
		query.setParameter("iCustNo", iCustNo);

		return this.convertToMap(query.getResultList());
	}

}