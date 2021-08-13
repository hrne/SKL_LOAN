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
/* 逾期放款明細 */
public class LM040ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM040ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		logger.info("LM040ServiceImpl findAll ");
		int entryMonth = (titaVo.getEntDyI() + 19110000) / 100;
		int inputDate = Integer.parseInt(titaVo.getParam("InputDate")) + 19110000;
		logger.info("LM040ServiceImpl entryMonth = " + entryMonth);
		logger.info("LM040ServiceImpl inputDate =  " + inputDate);

		String sql = " ";
		sql += " SELECT CASE";
		sql += "          WHEN M.\"EntCode\" IN ('0', '2') THEN '0'";
		sql += "        ELSE '1' END            AS F0";
		sql += "       ,CASE";
		sql += "          WHEN NVL(M.\"CityCode\",' ') = ' ' THEN '0'";
		sql += "        ELSE M.\"CityCode\" END AS F1";
		sql += "       ,SUM(M.\"PrinBalance\")  AS F2";
		sql += " FROM \"MonthlyFacBal\" M";
		sql += " LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "                        AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += " WHERE M.\"YearMonth\" = :entryMonth";
		sql += "   AND M.\"Status\" = 0";
		sql += "   AND M.\"PrinBalance\" > 0";
		sql += "   AND F.\"FirstDrawdownDate\" <= :inputDate";
		sql += " GROUP BY CASE";
		sql += "            WHEN M.\"EntCode\" IN ('0', '2') THEN '0'";
		sql += "          ELSE '1' END";
		sql += "         ,CASE";
		sql += "            WHEN NVL(M.\"CityCode\",' ') = ' ' THEN '0'";
		sql += "          ELSE M.\"CityCode\" END";
		sql += " ORDER BY F0";
		sql += "         ,F1";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entryMonth", entryMonth);
		query.setParameter("inputDate", inputDate);

		return this.convertToMap(query.getResultList());
	}

}