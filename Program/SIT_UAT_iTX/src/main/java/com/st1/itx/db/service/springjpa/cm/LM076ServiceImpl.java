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
public class LM076ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM076ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo, String RptMonth) throws Exception {
		logger.info("LM076.findAll ");
		logger.info("LM076ServiceImpl RptMonth: " + RptMonth);

		String sql = "WITH tmp AS ( SELECT CASE nvl(cm.\"CityCode\", '05')";
        sql += "                           WHEN '05' THEN 1";
        sql += "                           WHEN '10' THEN 2";
        sql += "                           WHEN '15' THEN 3";
        sql += "                           WHEN '35' THEN 4";
        sql += "                           WHEN '65' THEN 5";
        sql += "                           ELSE 7 END ";
        sql += "                           AS ecity";
        sql += "                          ,SUM(fac.\"LineAmt\") e0";
        sql += "                          ,SUM( CASE WHEN NVL(cm.\"EvaAmt\", 0) > 0";
        sql += "                                     THEN fac.\"LineAmt\" / (cm.\"EvaAmt\" * fac.\"LineAmt\")";
        sql += "                                     ELSE 0 END) e1";
        sql += "                          ,SUM(fac.\"ApproveRate\" * fac.\"LineAmt\") e2";
        sql += "                    FROM \"FacMain\" fac";
        sql += "                    LEFT JOIN ( SELECT cf.\"CustNo\"";
        sql += "                                      ,cf.\"FacmNo\"";
        sql += "                                      ,MAX(CASE ";
        sql += "                                           WHEN CF.\"MainFlag\" = 'Y'"; 
        sql += "                                           THEN cm.\"CityCode\"";
        sql += "                                           ELSE ' ' END) ";
        sql += "                                       AS \"CityCode\"";
        sql += "                                      ,SUM(cm.\"EvaAmt\") AS \"EvaAmt\"";
        sql += "                                FROM \"ClFac\" cf";
        sql += "                                LEFT JOIN \"ClMain\" cm ON cm.\"ClCode1\" = cf.\"ClCode1\"";
        sql += "                                                       AND cm.\"ClCode2\" = cf.\"ClCode2\"";
        sql += "                                                       AND cm.\"ClNo\" = cf.\"ClNo\"";
        sql += "                                WHERE cf.\"CustNo\" > 0 ";
        sql += "                                  AND cf.\"FacmNo\" > 0";
        sql += "                                  AND cm.\"ClNo\" > 0";
        sql += "                                GROUP BY cf.\"CustNo\"";
        sql += "                                        ,cf.\"FacmNo\"";
        sql += "                              ) cm ON cm.\"CustNo\" = fac.\"CustNo\"";
        sql += "                                  AND cm.\"FacmNo\" = fac.\"FacmNo\"";
        sql += "                    LEFT JOIN \"CdCode\" cd ON cd.\"DefCode\" = 'RuleCode'";
        sql += "                                           AND cd.\"Code\" = fac.\"RuleCode\"";
        sql += "                    WHERE trunc(fac.\"FirstDrawdownDate\" / 100) = :RptMonth";
        sql += "                      AND fac.\"RuleCode\" = '08'";
        sql += "                    GROUP BY CASE nvl(cm.\"CityCode\", '05')";
        sql += "                             WHEN '05' THEN 1";
        sql += "                             WHEN '10' THEN 2";
        sql += "                             WHEN '15' THEN 3";
        sql += "                             WHEN '35' THEN 4";
        sql += "                             WHEN '65' THEN 5";
        sql += "                             ELSE 7 END";
        sql += "                  )";
        sql += "      SELECT s.ecity f0";
        sql += "            ,s.e0 f1";
        sql += "            ,s.e1 / s.e0 f2";
        sql += "            ,s.e2 / s.e0 f3";
        sql += "      FROM tmp s";

		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		
		query.setParameter("RptMonth", RptMonth);
		
		return this.convertToMap(query.getResultList());
	}

}