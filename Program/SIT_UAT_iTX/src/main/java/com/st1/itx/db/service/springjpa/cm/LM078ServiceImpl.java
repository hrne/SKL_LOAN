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
public class LM078ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM078ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo, String RptMonth) throws Exception {
		logger.info("LM078.findAll ");
		logger.info("LM078ServiceImpl RptMonth: " + RptMonth);
		
		String sql = "";
		sql += " WITH tmp AS ( SELECT fac.\"RuleCode\" as erule ";
		sql += "                    ,COUNT(*) e0 ";
		sql += "                     ,SUM(fac.\"LineAmt\") e1 ";
		sql += "               FROM \"FacMain\" fac ";
		sql += "               LEFT JOIN ( SELECT cf.\"CustNo\" ";
		sql += "                                 ,cf.\"FacmNo\" ";
		sql += "                                 ,MAX(CASE WHEN CF.\"MainFlag\" = 'Y'  ";
		sql += "                                           THEN cm.\"CityCode\" ";
		sql += "                                           ELSE ' ' END)  ";
		sql += "                                  AS \"CityCode\" ";
		sql += "                                 ,SUM(cm.\"EvaAmt\") AS \"EvaAmt\" ";
		sql += "                           FROM \"ClFac\" cf ";
		sql += "                           LEFT JOIN \"ClMain\" cm ON cm.\"ClCode1\" = cf.\"ClCode1\" ";
		sql += "                                                  AND cm.\"ClCode2\" = cf.\"ClCode2\" ";
		sql += "                                                  AND cm.\"ClNo\" = cf.\"ClNo\" ";
		sql += "                           WHERE cf.\"CustNo\" > 0  ";
		sql += "                             AND cf.\"FacmNo\" > 0 ";
		sql += "                             AND cm.\"ClNo\" > 0 ";
		sql += "                           GROUP BY cf.\"CustNo\" ";
		sql += "                                   ,cf.\"FacmNo\" ";
		sql += "                         ) cm ON cm.\"CustNo\" = fac.\"CustNo\" ";
		sql += "                             AND cm.\"FacmNo\" = fac.\"FacmNo\" ";
		sql += "               LEFT JOIN \"CdCode\" cd ON cd.\"DefCode\" = 'RuleCode' ";
		sql += "                                    AND cd.\"Code\" = fac.\"RuleCode\" ";
		sql += "               LEFT JOIN \"FacCaseAppl\" fca ON fca.\"ApplNo\" = fac.\"ApplNo\" ";
		sql += "               WHERE trunc(fac.\"FirstDrawdownDate\" / 100) = :RptMonth ";
		sql += "               AND fac.\"RuleCode\" IN ('01','02','03','04','06','07','08','09','10') ";
		sql += "               AND NVL(fca.\"ApplDate\",99999999) <= DECODE(fac.\"RuleCode\",'10',20210318,20201207) ";
		sql += "               AND NVL(fca.\"ApproveDate\",0) >= 20201208 ";
		sql += "               GROUP BY fac.\"RuleCode\" ";
		sql += "             ) ";
		sql += " SELECT s.erule f0 ";
		sql += "       ,s.e0 f1 ";
		sql += "       ,s.e1 f2 ";
		sql += " FROM tmp s ";

		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		
		query.setParameter("RptMonth", RptMonth);
		
		return this.convertToMap(query.getResultList());
	}

}