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
public class L9722ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("l9722.findAll ");

		int inputBCYear = Integer.parseInt(titaVo.getParam("InputYear")) + 1911;
		int inputMonth = Integer.parseInt(titaVo.getParam("InputMonth"));

		String yearMonth = Integer.toString(inputBCYear) + Integer.toString(inputMonth);

		this.info("yearMonth = " + yearMonth);

		String sql = "SELECT m.\"CustNo\"               AS F0";
		sql += "            ,m.\"FacmNo\"               AS F1";
		sql += "            ,c.\"EntCode\"              AS F2";
		sql += "            ,m.\"DepartmentCode\"       AS F3";
		sql += "            ,CASE ";
		sql += "               WHEN nvl(cl.\"EvaNetWorth\", 0) = 0";
		sql += "               THEN 0 ";
		sql += "             ELSE trunc(f.\"LineAmt\" / cl.\"EvaNetWorth\" * 100, 2)";
		sql += "             END                        AS F4";
		sql += "            ,m.\"OvduTerm\"             AS F5";
		sql += "            ,m.\"Status\"               AS F6";
		sql += "            ,m.\"ClCode1\"              AS F7";
		sql += "            ,m.\"ClCode2\"              AS F8";
		sql += "            ,m.\"ClNo\"                 AS F9";
		sql += "            ,nvl(cl.\"EvaNetWorth\", 0) AS F10";
		sql += "            ,m.\"PrinBalance\"          AS F11";
		sql += "            ,m.\"BadDebtBal\"           AS F12";
		sql += "            ,f.\"FirstDrawdownDate\"    AS F13";
		sql += "            ,f.\"MaturityDate\"         AS F14";
		sql += "      FROM \"MonthlyFacBal\"  m";
		sql += "      LEFT JOIN \"CustMain\" c ON c.\"CustNo\" = m.\"CustNo\"";
		sql += "      LEFT JOIN \"FacMain\"  f ON f.\"CustNo\" = m.\"CustNo\"";
		sql += "                              AND f.\"FacmNo\" = m.\"FacmNo\"";
		sql += "      LEFT JOIN \"ClImm\"   cl ON cl.\"ClCode1\" = m.\"ClCode1\"";
		sql += "                              AND cl.\"ClCode2\" = m.\"ClCode2\"";
		sql += "                              AND cl.\"ClNo\" = m.\"ClNo\"";
		sql += "      WHERE m.\"Status\" IN (0,2,4,6,7)";
		sql += "        AND m.\"YearMonth\" = :yearMonth";

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("yearMonth", yearMonth);

		return this.convertToMap(query.getResultList());
	}

}