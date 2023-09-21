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
public class LM073ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo, int RptMonth) throws Exception {
		this.info("LM073.findAll ");
		this.info("LM073ServiceImpl RptMonth: " + RptMonth);

		String sql = "	";
		sql += "  WITH \"CFSum\" AS (";
		sql += "      SELECT \"CustNo\"";
		sql += "           , \"FacmNo\"";
		sql += "           , SUM(Nvl(";
		sql += "          \"OriEvaNotWorth\", 0";
		sql += "      )) AS \"EvaNetWorth\"";
		sql += "      FROM \"ClFac\"";
		sql += "      WHERE \"MainFlag\" = 'Y'";
		sql += "      GROUP BY \"CustNo\"";
		sql += "             , \"FacmNo\"";
		sql += "  )";
		sql += "  SELECT";
		sql += "         Fac.\"RuleCode\"";
		sql += "       , Cc.\"RuleCodeItem\"";
		sql += "       , Cl.\"CityCode\"";
		sql += "       , Fac.\"CustNo\"";
		sql += "       , Fac.\"FacmNo\"";
		sql += "       , Fac.\"FirstDrawdownDate\"";
		sql += "       , Fac.\"LineAmt\"";
		sql += "       , Fac.\"LastBormNo\"";
		sql += "       , LM.\"DrawdownDate\"";
		sql += "       , LM.\"DrawdownAmt\"";
		sql += "       , LM.\"StoreRate\"";
		sql += "       , CASE";
		sql += "           WHEN Nvl(Cs.\"EvaNetWorth\", 0 ) = 0 THEN 0";
		sql += "           ELSE Round( Fac.\"LineAmt\" / Cs.\"EvaNetWorth\", 2)";
		sql += "         END AS \"LoanRatio\"";
		sql += "       , Nvl(Cs.\"EvaNetWorth\", 0) AS \"EvaNetWorth\"";
		sql += "       , Fac.\"CompensateFlag\"";
		sql += "       , LM.\"RenewFlag\"";
		sql += "  FROM \"FacMain\"  Fac";
		sql += "  LEFT JOIN \"LoanBorMain\" LM ON LM.\"CustNo\" = Fac.\"CustNo\"";
		sql += "                            AND LM.\"FacmNo\" = Fac.\"FacmNo\"";
		sql += "                            AND LM.\"BormNo\" = Fac.\"LastBormNo\"";
		sql += "  LEFT JOIN \"CFSum\"     Cs ON Cs.\"CustNo\" = Fac.\"CustNo\"";
		sql += "                          AND Cs.\"FacmNo\" = Fac.\"FacmNo\"";
		sql += "  LEFT JOIN \"ClFac\"     Cf ON Cf.\"CustNo\" = Fac.\"CustNo\"";
		sql += "                          AND Cf.\"FacmNo\" = Fac.\"FacmNo\" ";
		sql += "                          AND Cf.\"MainFlag\" = 'Y'";
		sql += "  LEFT JOIN \"ClMain\" Cl ON Cl.\"ClCode1\" = Cf.\"ClCode1\"";
		sql += "                       AND Cl.\"ClCode2\" = Cf.\"ClCode2\"";
		sql += "                       AND Cl.\"ClNo\" = Cf.\"ClNo\"";
		sql += "  LEFT JOIN \"CdRuleCode\" Cc ON Cc.\"RuleCode\" = Fac.\"RuleCode\"";
		sql += "  WHERE Trunc(Fac.\"FirstDrawdownDate\" / 100) = :RptMonth";
		sql += "        AND";
		sql += "        Fac.\"CustTypeCode\" = 'S01'";



		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("RptMonth", RptMonth);

		return this.convertToMap(query);
	}

}