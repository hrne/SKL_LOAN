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
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4454ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4454ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L4454ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Autowired
	private Parse parse;
	
	@Autowired
	private DateUtil dateUtil;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		logger.info("L4454.findAll");

		int iCustNo = parse.stringToInteger(titaVo.get("CustNo"));
		int iFacmNo = parse.stringToInteger(titaVo.get("FacmNo"));
		int iEntDy = parse.stringToInteger(titaVo.get("AcDate")) + 19110000;

		logger.info("iCustNo = " + iCustNo);
		logger.info("iFacmNo = " + iFacmNo);
		logger.info("iEntDy = " + iEntDy);

		String sql = " SELECT   ' '       AS F0        ";
		sql += "   , M.\"CustNo\"         AS F1        ";
		sql += "   , C.\"CustName\"       AS F2        ";
		sql += "   , C.\"CurrZip3\"       AS F3        ";
		sql += "   , C.\"CurrZip2\"       AS F4        ";
		sql += "   , C2.\"CityItem\"      AS F5        ";
		sql += "   , C3.\"AreaItem\"      AS F6        ";
		sql += "   , C.\"CurrRoad\"       AS F7        ";
		sql += "   , C.\"CurrSection\"    AS F8        ";
		sql += "   , C.\"CurrAlley\"      AS F9        ";
		sql += "   , C.\"CurrLane\"       AS F10       ";
		sql += "   , C.\"CurrNum\"        AS F11       ";
		sql += "   , C.\"CurrNumDash\"    AS F12       ";
		sql += "   , C.\"CurrFloor\"      AS F13       ";
		sql += "   , C.\"CurrFloorDash\"  AS F14       ";
		sql += "    FROM (                             ";
		sql += "   SELECT                              ";
		sql += "     D.\"CustNo\"                      ";
		sql += "   , D.\"FacmNo\"                      ";
		sql += "   FROM \"BatxDetail\" D               ";
		sql += "   LEFT JOIN \"BatxHead\" H  ON   H.\"AcDate\"  =  D.\"AcDate\"       ";
		sql += "                            AND   H.\"BatchNo\" =  D.\"BatchNo\"	 ";
		sql += "   WHERE D.\"CustNo\"     =  " + iCustNo;
		sql += "     AND D.\"FacmNo\"     =  " + iFacmNo;
		sql += "     AND D.\"AcDate\"     =  " + iEntDy;
		sql += "     AND D.\"RepayCode\"     =  2        ";
		sql += "     AND H.\"BatxExeCode\"   <> 8        ";
		sql += "   ) M                                  ";
		sql += "    LEFT JOIN \"CustMain\" C   ON   C.\"CustNo\"   =  M.\"CustNo\"       ";
		sql += "    LEFT JOIN \"CdCity\"   C2  ON  C2.\"CityCode\" =  C.\"CurrCityCode\" ";
		sql += "    LEFT JOIN \"CdArea\"   C3  ON  C3.\"CityCode\" =  C.\"CurrCityCode\" ";
		sql += "                            AND  C3.\"AreaCode\" =  C.\"CurrAreaCode\"	 ";
		sql += "    WHERE M.\"CustNo\" is not null     ";


		logger.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query.getResultList());
	}

}