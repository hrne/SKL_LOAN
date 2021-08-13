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
public class LM043ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM043ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(int type, TitaVo titaVo) throws Exception {
		logger.info("lM043.findAll ");

		String entdy = String.valueOf((Integer.valueOf(titaVo.get("ENTDY").toString()) + 19110000) / 100);

		String sql = "SELECT M.\"EntCode\"";
		sql += "            ,M.\"CityCode\"";
		sql += "            ,SUM(M.\"Cnt\") AS \"Cnt\"";
		sql += "            ,SUM(M.\"PrinBalance\") AS \"PrinBalance\"";
		sql += "      FROM (SELECT DECODE(M.\"EntCode\", '1', 1, 0) AS \"EntCode\"";
		sql += "                  ,TO_NUMBER(NVL(M.\"CityCode\", 0)) AS \"CityCode\"";
		sql += "                  ,1 AS \"Cnt\"";
		sql += "                  ,M.\"PrinBalance\"";
		sql += "            FROM \"MonthlyFacBal\" M";
		sql += "            WHERE M.\"YearMonth\" = :entdy";
		sql += condition(type);
		sql += "              AND M.\"PrinBalance\" > 0";
		sql += "      UNION ALL";
		sql += "      SELECT 0 AS \"EntCode\"";
		sql += "            ,TO_NUMBER(C.\"CityCode\") AS \"CityCode\"";
		sql += "            ,0 AS \"Cnt\"";
		sql += "            ,0 AS \"PrinBalance\"";
		sql += "      FROM \"CdCity\" C";
		sql += "      UNION ALL";
		sql += "      SELECT 1 AS \"EntCode\"";
		sql += "            ,TO_NUMBER(C.\"CityCode\") AS \"CityCode\"";
		sql += "            ,0 AS \"Cnt\"";
		sql += "            ,0 AS \"PrinBalance\"";
		sql += "      FROM \"CdCity\" C";
		sql += "      UNION ALL";
		sql += "      SELECT 0 AS \"EntCode\"";
		sql += "            ,0 AS \"CityCode\"";
		sql += "            ,0 AS \"Cnt\"";
		sql += "            ,0 AS \"PrinBalance\"";
		sql += "      FROM DUAL";
		sql += "      UNION ALL";
		sql += "      SELECT 1 AS \"EntCode\"";
		sql += "            ,0 AS \"CityCode\"";
		sql += "            ,0 AS \"Cnt\"";
		sql += "            ,0 AS \"PrinBalance\"";
		sql += "      FROM DUAL) M";
		sql += "      GROUP BY M.\"EntCode\"";
		sql += "              ,M.\"CityCode\"";
		sql += "      ORDER BY M.\"EntCode\"";
		sql += "              ,M.\"CityCode\"";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entdy", entdy);
		return this.convertToMap(query.getResultList());
	}
	
	private String condition(int type) {
		String sql = "";
		switch(type) {
		case 0:
			sql += " AND M.\"AcctCode\" <> '990'";
			break;
		case 1:
			sql += " AND M.\"AcctCode\" = '990'";
			break;
		case 2:
			sql += " AND M.\"AcctCode\" <> '990'";
			sql += " AND M.\"OvduTerm\" >= 3";
			break;
		default:
			sql +="";
		}
		return sql;	
	}
}