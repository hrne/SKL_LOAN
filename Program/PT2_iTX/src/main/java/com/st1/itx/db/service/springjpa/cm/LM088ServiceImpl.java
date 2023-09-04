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

@Service("LM088ServiceImpl")
@Repository
public class LM088ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int yearMonth, TitaVo titaVo) throws Exception {
		this.info("LM088ServiceImpl findAll");

		String sql = "";
		sql += "    SELECT";
		sql += "          DECODE(CM.\"EntCode\", 1, 1, 0) AS \"EntCode\"";
		sql += "        , CM.\"CustNo\" AS \"CustNoMain\"";
		sql += "        , CM.\"CustName\"";
		sql += "            || CASE";
		sql += "            WHEN DECODE(CM.\"EntCode\", 1, 1, 0) = 1 THEN";
		sql += "                '及其同一關係企業'";
		sql += "            ELSE";
		sql += "                '及其同一關係人'";
		sql += "            END AS \"GroupName\"";
//		sql += "        , CM.\"CustName\" AS \"CustNameM\"";
		sql += "        , CM2.\"CustName\"";
		sql += "        , CM2.\"CustNo\"";
		sql += "        , CM2.\"CustId\"";
		sql += "        , NVL(M.\"LoanBal\", 0) AS \"LoanBal\"";
		sql += "    FROM";
		sql += "        \"ReltMain\" RM";
		sql += "        LEFT JOIN \"CustMain\" CM";
		sql += "        ON CM.\"CustNo\" = RM.\"CustNo\"";
		sql += "        LEFT JOIN \"CustMain\" CM2";
		sql += "        ON CM2.\"CustUKey\" = RM.\"ReltUKey\"";
		sql += "        LEFT JOIN (";
		sql += "            SELECT";
		sql += "                \"CustNo\",";
		sql += "                SUM(\"PrinBalance\") AS \"LoanBal\"";
		sql += "            FROM";
		sql += "                \"MonthlyFacBal\"";
		sql += "            WHERE";
		sql += "                \"YearMonth\"= :yymm";
		sql += "                AND \"PrinBalance\" > 0";
		sql += "            GROUP BY";
		sql += "                \"CustNo\"";
		sql += "        ) M";
		sql += "        ON M.\"CustNo\" = CM2.\"CustNo\"";
		sql += "    WHERE";
		sql += "        RM.\"CaseNo\" = 0";
		sql += "    ORDER BY";
		sql += "          DECODE(CM.\"EntCode\", 1, 1, 0) DESC";
		sql += "        , CM.\"CustNo\"";
		sql += "        , RM.\"ReltCode\"";
		sql += "        , CM2.\"CustNo\"";
		this.info("sql = " + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		Query query = em.createNativeQuery(sql);
		query.setParameter("yymm", yearMonth);

		return this.convertToMap(query);
	}
}