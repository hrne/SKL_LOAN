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
public class L9723ServiceImpl extends ASpringJpaParm implements InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findDataCount(TitaVo titaVo) throws Exception {
		this.info("L9723FindData");

		int iYearMonth = ((Integer.valueOf(titaVo.getParam("ReportDateY")) + 1911) * 100)
				+ Integer.valueOf(titaVo.getParam("ReportDateM"));

		this.info("iYearMonth     = " + iYearMonth);

		String sql = "select count(*) AS \"count\", ";
		sql += "           \"YearMonth\" as \"YearMonth\" ";
		sql += "            from (SELECT \"CustNo\", ";
		sql += "             \"YearMonth\"  ";
		sql += "              FROM \"MonthlyFacBal\" ";
		sql += "              WHERE \"YearMonth\" = " + iYearMonth + " ";
		sql += "                AND \"PrinBalance\" > 0 ";
		sql += "           GROUP BY \"CustNo\",\"YearMonth\" ";
		sql += "           )";
		sql += "   GROUP BY \"YearMonth\" ";

		this.info("sql1=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		Query query;
		query = em.createNativeQuery(sql);
		return this.convertToMap(query);

	}

	public List<Map<String, String>> findData(TitaVo titaVo) throws Exception {
		this.info("L9723FindData");

		int iYearMonth = ((Integer.valueOf(titaVo.getParam("ReportDateY")) + 1911) * 100)
				+ Integer.valueOf(titaVo.getParam("ReportDateM"));

		this.info("iYearMonth     = " + iYearMonth);

		String sql = "";
		sql += "	SELECT M.\"CustNo\"";
		sql += "          ,M.\"LoanBalance\"";
		sql += "          ,NVL(CM.\"CurrRoad\",CM.\"RegRoad\") AS \"Address\"";
		sql += "          ,CM.\"AMLGroup\"";
		sql += "          ,CM.\"NationalityCode\"";
		sql += "          ,CM.\"IndustryCode\"";
		sql += "          ,CM.\"JobTitle\"";
		sql += "	FROM (	SELECT \"CustNo\"";
		sql += "            	  ,SUM(\"PrinBalance\") AS \"LoanBalance\"";
		sql += "            FROM \"MonthlyFacBal\" ";
		sql += "            WHERE \"YearMonth\" = " + iYearMonth + " ";
		sql += "              AND \"PrinBalance\" > 0 ";
		sql += "            GROUP BY \"CustNo\"";
		sql += "          ) M";
		sql += "    LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = M.\"CustNo\"";
		sql += "	ORDER BY M.\"CustNo\" ";

		this.info("sql2=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		Query query;
		query = em.createNativeQuery(sql);
		return this.convertToMap(query);

	}

}