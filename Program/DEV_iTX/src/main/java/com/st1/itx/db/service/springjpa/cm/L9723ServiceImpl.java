package com.st1.itx.db.service.springjpa.cm;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.util.parse.Parse;

@Service
@Repository
public class L9723ServiceImpl extends ASpringJpaParm implements InitializingBean {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	private BaseEntityManager baseEntityManager;

	private String result;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public String findAll(TitaVo titaVo) throws Exception {
		this.info("l9723.findAll ");

		String inputYearMonth = Integer.toString(Integer.parseInt(titaVo.getParam("inputYear")) + 1911) + titaVo.getParam("inputMonth");

		this.info("l9723 inputYearMonth " + inputYearMonth);

		String sql = "SELECT COUNT(*) AS \"Count\"";
		sql += "      FROM ( SELECT \"CustNo\"";
		sql += "             FROM \"MonthlyFacBal\"";
		sql += "             WHERE \"YearMonth\" = :inputYearMonth";
		sql += "               AND \"PrinBalance\" > 0";
		sql += "             GROUP BY \"CustNo\"";
		sql += "           )";

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("inputYearMonth", inputYearMonth);

		try {
			result = ((BigDecimal) query.getSingleResult()).toString();
		} catch (Exception e) {
			result = "0";
		}

		this.info("l9723 result:" + result);

		return result;
	}

}