package com.st1.itx.db.service.springjpa.cm;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Service("bS720ServiceImpl")
public class BS720ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	public List<Map<String, String>> FindAll(TitaVo titaVo, int YearMonth, int YearMonthLast) throws Exception {
		String sql = "";
		sql += " SELECT IIM.\"YearMonth\" ";
		sql += "       ,ROUND(SUM(CASE WHEN NVL(MFB.\"AcctCode\",' ') = '990' ";
		sql += "                       THEN IIM.\"AccumDPAmortized\" ";
		sql += "                  ELSE 0 END)) AS \"OvAmortized\" ";
		sql += "       ,ROUND(SUM(CASE WHEN NVL(MFB.\"AcctCode\",' ') <> '990' ";
		sql += "                       THEN IIM.\"AccumDPAmortized\" ";
		sql += "                  ELSE 0 END)) AS \"LnAmortized\" ";
		sql += " FROM \"Ias39IntMethod\" IIM ";
		sql += " LEFT JOIN \"MonthlyFacBal\" MFB ON MFB.\"YearMonth\" = IIM.\"YearMonth\" ";
		sql += "                                AND MFB.\"CustNo\"    = IIM.\"CustNo\" ";
		sql += "                                AND MFB.\"FacmNo\"    = IIM.\"FacmNo\" ";
		sql += " WHERE IIM.\"YearMonth\" IN (:YearMonth, :YearMonthLast) ";
		sql += " GROUP BY IIM.\"YearMonth\" ";
		sql += " ORDER BY IIM.\"YearMonth\" DESC "; // Makes sure YearMonthLast output being the last one

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);

		query.setParameter("YearMonth", YearMonth);
		query.setParameter("YearMonthLast", YearMonthLast);

		return this.convertToMap(query);
	}
}
