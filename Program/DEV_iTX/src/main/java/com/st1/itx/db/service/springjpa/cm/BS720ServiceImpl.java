package com.st1.itx.db.service.springjpa.cm;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Service("bS720ServiceImpl")
public class BS720ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(BS720ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;
	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	public String FindBS720(int YearMonth, int YearMonthLast) throws Exception {
		String sql = 
				"SELECT \r\n" + 
				" T.\"YearMonth\"\r\n" + 
				",SUM(CASE WHEN NVL(TF.\"AcctCode\",' ') = '990' then T.\"AccumDPAmortized\" ELSE 0 END) AS \"OvAmortized\"\r\n" + 
				",SUM(CASE WHEN  NVL(TF.\"AcctCode\",' ') <> '990' then T.\"AccumDPAmortized\" ELSE 0 END) AS \"LnAmortized\"\r\n" + 
				"FROM \"Ias39IntMethod\" T\r\n" + 
				"LEFT JOIN \"MonthlyFacBal\" TF\r\n" + 
				"       ON  TF.\"YearMonth\" = T.\"YearMonth\"\r\n" + 
				"      AND  TF.\"CustNo\" = T.\"CustNo\"\r\n" + 
				"      AND  TF.\"FacmNo\" = T.\"FacmNo\"\r\n" + 
				"WHERE T.\"YearMonth\" = " + YearMonth + " " +
				"GROUP BY T.\"YearMonth\"\r\n" + 
				"UNION ALL\r\n" + 
				"SELECT \r\n" + 
				" L.\"YearMonth\"\r\n" + 
				",SUM(CASE WHEN NVL(LF.\"AcctCode\",' ') = '990' then L.\"AccumDPAmortized\" ELSE 0 END)    AS \"OvAmortizedLast\" \r\n" + 
				",SUM(CASE WHEN  NVL(LF.\"AcctCode\",' ') <> '990' then L.\"AccumDPAmortized\" ELSE 0 END) AS \"LnAmortizedLast\" \r\n" + 
				"FROM \"Ias39IntMethod\" L\r\n" + 
				"LEFT JOIN \"MonthlyFacBal\" LF\r\n" + 
				"       ON  LF.\"YearMonth\" = L.\"YearMonth\"\r\n" + 
				"      AND  LF.\"CustNo\" = L.\"CustNo\"\r\n" + 
				"      AND  LF.\"FacmNo\" = L.\"FacmNo\"\r\n" + 
				"WHERE  L.\"YearMonth\" = " + YearMonthLast + " " +
				"GROUP BY L.\"YearMonth\"\r\n" + 
				"";
		logger.info("sql = " + sql);
		return sql;
	}

	public List<Map<String, String>> FindData(String sql, TitaVo titaVo) throws Exception {
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		logger.info("BS720Service FindData=" + query.toString());
		return this.convertToMap(query.getResultList());
	}
}
