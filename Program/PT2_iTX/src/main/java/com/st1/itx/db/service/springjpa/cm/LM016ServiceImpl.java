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
/* 逾期放款明細 */
public class LM016ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("lM016.findAll ");

		// 戶號區間 Min
		String custNoMin = titaVo.getParam("CustNoMin");

		// 戶號區間 Max
		String custNoMax = titaVo.getParam("CustNoMax");

		String sql = "SELECT GC.\"CustNo\" AS F0";
		sql += "            ,\"Fn_ParseEOL\"(CM.\"CustName\",0) AS F1";
		sql += "            ,GC.\"FacmNo\" AS F2";
		sql += "            ,DECODE(DECODE(LM2.\"LoanBal\",0,0,LM.\"GraceDate\"),0,0,LM.\"PrevPayIntDate\") AS F3";
		sql += "            ,DECODE(LM2.\"LoanBal\",0,0,LM.\"GraceDate\") AS F4";
		sql += "            ,DECODE(LM2.\"LoanBal\",0,0,LM2.\"LoanBal\") AS F5";
		sql += "            ,GC.\"ActUse\" AS F6";
		sql += "      FROM \"GraceCondition\" GC";
		sql += "      LEFT JOIN(SELECT \"CustNo\"";
		sql += "                      ,\"FacmNo\"";
		sql += "                      ,\"BormNo\"";
		sql += "                      ,\"PrevPayIntDate\"";
		sql += "                      ,\"NextPayIntDate\"";
		sql += "                      ,\"GraceDate\"";
		sql += "                      ,ROW_NUMBER () OVER (PARTITION BY \"CustNo\",\"FacmNo\" ORDER BY \"BormNo\" DESC) AS \"SEQ\"";
		sql += "      FROM \"LoanBorMain\"";
		sql += "               ) LM ON LM.\"CustNo\"=GC.\"CustNo\"";
		sql += "                    AND LM.\"FacmNo\"=GC.\"FacmNo\"";
		sql += "                    AND LM.\"SEQ\"=1";
		sql += "      LEFT JOIN(SELECT \"CustNo\"";
		sql += "                      ,\"FacmNo\"";
		sql += "                      ,SUM(\"LoanBal\") \"LoanBal\"";
		sql += "                FROM \"LoanBorMain\"";
		sql += "                GROUP BY \"CustNo\",\"FacmNo\") LM2 ON LM2.\"CustNo\"=GC.\"CustNo\"";
		sql += "                                                    AND LM2.\"FacmNo\"=GC.\"FacmNo\"";
		sql += "      LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\"=GC.\"CustNo\"";
		sql += "      WHERE GC.\"CustNo\" >= :custNoMin";
		sql += "      AND GC.\"CustNo\" <= :custNoMax";
		sql += "      ORDER BY GC.\"CustNo\",GC.\"FacmNo\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("custNoMin", custNoMin);
		query.setParameter("custNoMax", custNoMax);

		return this.convertToMap(query.getResultList());
	}

}