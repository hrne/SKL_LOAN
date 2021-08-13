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
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service
@Repository
public class LM073ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM073ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo, String RptMonth) throws Exception {
		logger.info("LM073.findAll ");
		logger.info("LM073ServiceImpl RptMonth: " + RptMonth);

		String sql = "SELECT CM.\"CityCode\"";
		sql += "	        ,FAC.\"RuleCode\"";
		sql += "		    ,CD.\"Item\"";
		sql += "	        ,FAC.\"CustNo\"";
		sql += "	        ,FAC.\"FacmNo\"";
		sql += "	        ,FAC.\"LineAmt\"";
		sql += "	        ,FAC.\"ApproveRate\"";
		sql += "	        ,CM.\"EvaAmt\"";
		sql += "	        ,FAC.\"FirstDrawdownDate\"";
		sql += "	  FROM \"FacMain\" FAC";
		sql += "	  LEFT JOIN (SELECT CF.\"CustNo\"";
		sql += "	                   ,CF.\"FacmNo\"";
		sql += "	                   ,MAX(CASE";
		sql += "	                        WHEN CF.\"MainFlag\" = 'Y' THEN CM.\"CityCode\"";
		sql += "	                        ELSE ' ' END) AS \"CityCode\"";
		sql += "	                   ,SUM(CM.\"EvaAmt\") AS \"EvaAmt\"";
		sql += "	             FROM \"ClFac\" CF";
		sql += "	             LEFT JOIN \"ClMain\" CM ON CM.\"ClCode1\" = CF.\"ClCode1\"";
		sql += "	                                     AND CM.\"ClCode2\" = CF.\"ClCode2\"";
		sql += "	                                     AND CM.\"ClNo\"    = CF.\"ClNo\"";
		sql += "	             WHERE CF.\"CustNo\" > 0";
		sql += "	             AND CF.\"FacmNo\" > 0";
		sql += "	             AND CM.\"ClNo\" > 0";
		sql += "	             GROUP BY CF.\"CustNo\"";
		sql += "	                     ,CF.\"FacmNo\"";
		sql += "	             ) CM ON CM.\"CustNo\" = FAC.\"CustNo\"";
		sql += "	                  AND CM.\"FacmNo\" = FAC.\"FacmNo\"";
		sql += "	  LEFT JOIN \"CdCode\" CD ON CD.\"DefCode\" = 'RuleCode'";
		sql += "	                          AND CD.\"Code\" = FAC.\"RuleCode\"";
		sql += "	  WHERE TRUNC(FAC.\"FirstDrawdownDate\" / 100) = :RptMonth";

		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		
		query.setParameter("RptMonth", RptMonth);
		
		return this.convertToMap(query.getResultList());
	}

}