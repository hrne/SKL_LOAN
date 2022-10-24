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
public class LQ003ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	public List<Map<String, String>> findAll(TitaVo titaVo, int inputYearMonth) {
		this.info("lQ003.findAll ");
		this.info("inputYearMonth = " + inputYearMonth);

		String sql = " ";
		sql += " SELECT CC.\"CityCode\" AS F0 ";
		sql += "      , CC.\"CityItem\" AS F1 ";
		sql += "      , ROUND(SUM(M.\"PrinBalance\") / 1000000,8) AS F2 "; // 總額
		sql += "      , ROUND(SUM(CASE ";
		sql += "                    WHEN M.\"AcctCode\" = '990' ";
		sql += "                    THEN M.\"PrinBalance\"";
		sql += "                    WHEN M.\"AcctCode\" != '990' ";
		sql += "                     AND M.\"OvduTerm\" >= 3 ";
		sql += "                    THEN M.\"PrinBalance\" ";
		sql += "                  ELSE 0 END ";
		sql += "                 ) / 1000000,8)  AS F3 ";// 逾放
		sql += "      , ROUND(SUM(CASE ";
		sql += "                    WHEN M.\"AcctCode\" = '990' ";
		sql += "                    THEN M.\"PrinBalance\" ";
		sql += "                  ELSE 0 END ";
		sql += "                 ) / 1000000,8) AS F4 ";// 催收
		sql += "      , SUM(CASE ";
		sql += "              WHEN M.\"AcctCode\" = '990' ";
		sql += "              THEN M.\"Cnt\"";
		sql += "              WHEN M.\"AcctCode\" != '990' ";
		sql += "               AND M.\"OvduTerm\" >= 3 ";
		sql += "              THEN M.\"Cnt\" ";
		sql += "            ELSE 0 END ";
		sql += "           )  AS F5 ";// 逾放件數
		sql += " FROM \"CdCity\" CC";
		sql += " LEFT JOIN (SELECT M.\"AcctCode\" ";
		sql += "                 , M.\"OvduTerm\" ";
		sql += "                 , CASE ";
		sql += "                     WHEN M.\"ClCode1\" IN (3,4) ";
		sql += "                     THEN 0 ";
		sql += "                     ELSE TO_NUMBER(NVL(M.\"CityCode\",0))";
		sql += "                   END AS \"CityCode\" ";
		sql += "                 , 1 AS \"Cnt\" ";
		sql += "                 , M.\"PrinBalance\" ";
		sql += "            FROM \"MonthlyFacBal\" M ";
		sql += "            WHERE M.\"YearMonth\" = :inputYearMonth ";
		sql += "              AND M.\"PrinBalance\" > 0 ";
		sql += "              AND DECODE(M.\"EntCode\",'1',1,0) = 0 ";
		sql += "           ) M ON M.\"CityCode\" = CC.\"CityCode\" ";
		sql += " GROUP BY CC.\"CityItem\" ";
		sql += "        , CC.\"CityCode\" ";
		sql += " ORDER BY CC.\"CityCode\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("inputYearMonth", inputYearMonth);

		return this.convertToMap(query);
	}
}
