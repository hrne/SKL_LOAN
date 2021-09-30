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
public class LM084ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("LM084ServiceImpl findAll ");

		/*
		 * AcLoanInt.Aging 0.一個月以下 1.一～三個月 2.三～六個月 3.六個月以上
		 */

		String sql = "SELECT ALI.\"Aging\"                AS F0 "; // 帳齡
 		sql += "           , LPAD(ALI.\"CustNo\",7,'0')   AS F1 "; // 戶號
		sql += "           , SUBSTR(CM.\"CustName\",0,20) AS F2 "; // 戶名
		sql += "           , MIN(ALI.\"IntEndDate\")      AS F3 "; // 繳息迄日
		sql += "           , MIN(ALI.\"PayIntDate\")      AS F4 "; // 應繳息日
		sql += "           , SUM(ALI.\"Interest\")        AS F5 "; // 應收利息
		sql += "      FROM \"AcLoanInt\" ALI ";
		sql += "      LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = ALI.\"CustNo\" ";
		sql += "      WHERE ALI.\"YearMonth\" = :inputYearMonth ";
		sql += "      GROUP BY ALI.\"Aging\" ";
		sql += "             , LPAD(ALI.\"CustNo\",7,'0') ";
		sql += "             , CM.\"CustName\"  ";
		sql += "      ORDER BY ALI.\"Aging\" ";
		sql += "             , LPAD(ALI.\"CustNo\",7,'0') ";
		sql += "             , CM.\"CustName\"  ";
		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		String inputYearMonth = Integer.toString(Integer.parseInt(titaVo.getParam("inputYear")) + 1911)
				+ titaVo.getParam("inputMonth");
		this.info("LM084ServiceImpl input");
		this.info("inputYearMonth: " + inputYearMonth);
		query.setParameter("inputYearMonth", inputYearMonth);

		return this.convertToMap(query);
	}

}