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
public class LM050ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM050ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> fnEquity(TitaVo titaVo) throws Exception {
		String entdy = String.valueOf(Integer.valueOf(titaVo.get("ENTDY").toString()) + 19110000);
		String yy = entdy.substring(0, 4);
		String mm = entdy.substring(4, 6);
		String yyqq = "";
		switch (mm) {
		case "01":
		case "02":
		case "03":
			yyqq = String.valueOf(Integer.valueOf(yy) - 1) + "12";
			break;
		case "04":
		case "05":
		case "06":
			yyqq = yy + "03";
			break;
		case "07":
		case "08":
		case "09":
			yyqq = yy + "06";
			break;
		case "10":
		case "11":
		case "12":
			yyqq = yy + "09";
			break;
		}
		logger.info("lM050.Totalequity yyqq=" + yyqq);
		String sql = "SELECT V.\"Totalequity\"";
		sql += "            ,V.\"YearMonth\"";
		sql += "      FROM \"CdVarValue\" V";
		sql += "      WHERE V.\"YearMonth\" = :lyyqq";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("lyyqq", yyqq);
		return this.convertToMap(query.getResultList());

	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		String yymm = String.valueOf((Integer.valueOf(titaVo.get("ENTDY").toString()) + 19110000) / 100);

		logger.info("lM050.findAll yymm=" + yymm);

		String sql = "SELECT M.\"CustNo\"      F0";
		sql += "            ,C.\"CustName\"    F1";
		sql += "            ,M.\"PrinBalance\" F2";
		sql += "           ,'F3'               F3";
		sql += "            ,C.\"EntCode\"     F4";
		sql += "            ,D.\"Item\"        F5";
		sql += "            ,M.\"Type\"        F6";
		sql += "            ,M.\"RelsCode\"    F7";
		sql += "      FROM (SELECT M.\"Type\"";
		sql += "                  ,DECODE(M.\"Type\"";
		sql += "                         ,2";
		sql += "                         ,M.\"CustNo\"";
		sql += "                         ,NULL) \"CustNo\"";
		sql += "                  ,M.\"RelsCode\"";
		sql += "                  ,SUM(M.\"PrinBalance\") \"PrinBalance\"";
		sql += "            FROM (SELECT CASE WHEN M.\"RelsCode\" IS NOT NULL";
		sql += "                               AND M.\"RelsCode\" NOT IN ('99')";
		sql += "                              THEN 2";
		sql += "                              WHEN C.\"CustTypeCode\" IN ('01') THEN 1";
		sql += "                         ELSE 0 END \"Type\"";
		sql += "                        ,M.\"CustNo\"";
		sql += "                        ,M.\"RelsCode\"";
		sql += "                        ,M.\"PrinBalance\"";
		sql += "                  FROM  \"MonthlyFacBal\" M";
		sql += "                  LEFT  JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\"";
		sql += "                  WHERE M.\"YearMonth\" = :yymm";
		sql += "                    AND M.\"Status\" IN (0, 2, 6)) M";
		sql += "            GROUP BY M.\"Type\"";
		sql += "                    ,DECODE (M.\"Type\"";
		sql += "                            ,2";
		sql += "                            ,M.\"CustNo\"";
		sql += "                            ,NULL)";
		sql += "                    ,M.\"RelsCode\") M";
		sql += "      LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = M.\"CustNo\"";
		sql += "      LEFT JOIN \"CdCode\" D ON D.\"DefCode\" = 'RelsCode'";
		sql += "                            AND D.\"DefType\" =  2";
		sql += "                            AND D.\"Code\"    =  M.\"RelsCode\"";
		sql += "      ORDER BY M.\"Type\", M.\"PrinBalance\" DESC";
		logger.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", yymm);
		return this.convertToMap(query.getResultList());
	}

}