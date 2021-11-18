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
import com.st1.itx.util.parse.Parse;

@Service
@Repository
public class LM043ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;
	
	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int type, TitaVo titaVo) throws Exception {
		this.info("lM043.findAll ");

		int yearMonth = parse.stringToInteger(titaVo.get("ENTDY")) / 100 + 191100;

		String sql = "SELECT M.\"EntCode\"";
		sql += "            ,M.\"CityCode\"";
		sql += "            ,SUM(M.\"Cnt\") AS \"Cnt\"";
		sql += "            ,SUM(M.\"PrinBalance\") AS \"PrinBalance\"";
		sql += "      FROM (SELECT DECODE(M.\"EntCode\", '1', 1, 0) AS \"EntCode\"";
		sql += "                  ,CASE WHEN M.\"ClCode1\" IN (3,4) ";
		sql += "                        THEN 0 "; // 股票
		sql += "                   ELSE TO_NUMBER(NVL(M.\"CityCode\", 0)) ";
		sql += "                   END AS \"CityCode\"";
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
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		
		query.setParameter("entdy", yearMonth);
		
		return this.convertToMap(query);
	}

	private static final String condition(int type) {
		String sql = "";
		switch (type) {
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
			sql += "";
		}
		return sql;
	}
}