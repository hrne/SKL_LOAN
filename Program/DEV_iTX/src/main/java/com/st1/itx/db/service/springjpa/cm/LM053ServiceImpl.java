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
public class LM053ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM053ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;
 
	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		String iENTDY = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000);
		String iYYMM = iENTDY.substring(0, 6);
		logger.info("lM053.findAll YYMM=" + iYYMM);

		String sql = "SELECT L.\"RecordDate058\" F0";
		sql += "            ,Ci.\"CityItem\"     F1";
		sql += "            ,L.\"CustNo\"        F2";
		sql += "            ,L.\"FacmNo\"        F3";
		sql += "            ,C.\"CustName\"      F4";
		sql += "            ,L.\"Amount056\"     F5";
		sql += "            ,L.\"Amount058\"     F6";
		sql += "            ,CASE WHEN M.\"PrinBalance\" < L.\"Amount901\" THEN L.\"Amount901\" - M.\"PrinBalance\"";
		sql += "             ELSE 0 END          F7";
		sql += "            ,L.\"AcDate060\"     F8";
		sql += "           ,'F9'                 F9";
		sql += "            ,E.\"Fullname\"      F10";
		sql += "            ,CASE WHEN L.\"Amount058\" <> L.\"Amount060\" THEN '金額不合'";
		sql += "             ELSE ' ' END        F11";
		sql += "      FROM (SELECT L.\"RecordDate058\"";
		sql += "                  ,L.\"CustNo\"";
		sql += "                  ,L.\"FacmNo\"";
		sql += "                  ,L.\"Amount058\"";
		sql += "                  ,L.\"AcDate\"";
		sql += "                  ,L.\"TitaTlrNo\"";
		sql += "                  ,L.\"TitaTxtNo\"";
		sql += "                  ,SUM(L.\"Amount056\") \"Amount056\"";
		sql += "                  ,SUM(L.\"Amount060\") \"Amount060\"";
		sql += "                  ,SUM(L.\"Amount901\") \"Amount901\"";
		sql += "                  ,MAX(L.\"AcDate060\") \"AcDate060\"";
		sql += "            FROM (SELECT L.\"RecordDate058\"";
		sql += "                        ,L.\"CustNo\"";
		sql += "                        ,L.\"FacmNo\"";
		sql += "                        ,L.\"Amount058\"";
		sql += "                        ,L.\"AcDate\"";
		sql += "                        ,L.\"TitaTlrNo\"";
		sql += "                        ,L.\"TitaTxtNo\"";
		sql += "                        ,DECODE(T.\"LegalProg\"";
		sql += "                              ,'056'";
		sql += "                               ,T.\"Amount\"";
		sql += "                               ,0) \"Amount056\"";
		sql += "                        ,DECODE(T.\"LegalProg\"";
		sql += "                              ,'060'";
		sql += "                               ,T.\"Amount\"";
		sql += "                               ,0) \"Amount060\"";
		sql += "                        ,DECODE(T.\"LegalProg\"";
		sql += "                              ,'901'";
		sql += "                               ,T.\"Amount\"";
		sql += "                               ,0) \"Amount901\"";
		sql += "                        ,DECODE(T.\"LegalProg\"";
		sql += "                              ,'060'";
		sql += "                               ,T.\"AcDate\"";
		sql += "                               ,0) \"AcDate060\"";
		sql += "                        ,ROW_NUMBER() OVER (PARTITION BY L.\"RecordDate058\"";
		sql += "                                                        ,L.\"CustNo\"";
		sql += "                                                        ,L.\"FacmNo\"";
		sql += "                                                         ORDER BY T.\"RecordDate\" DESC) AS SEQ";
		sql += "                  FROM (SELECT L.\"RecordDate\" \"RecordDate058\"";
		sql += "                              ,L.\"CustNo\", L.\"FacmNo\"";
		sql += "                              ,L.\"Amount\" \"Amount058\"";
		sql += "                              ,L.\"AcDate\"";
		sql += "                              ,L.\"TitaTlrNo\"";
		sql += "                              ,L.\"TitaTxtNo\"";
		sql += "                        FROM \"CollLaw\" L";
		sql += "                        WHERE L.\"LegalProg\" = '058') L";
		sql += "                  LEFT JOIN \"CollLaw\" T ON T.\"CustNo\" = L.\"CustNo\"";
		sql += "                                         AND T.\"FacmNo\" = L.\"FacmNo\"";
		sql += "                                         AND T.\"LegalProg\" IN ('056', '060', '901')) L";
		sql += "            WHERE L.\"SEQ\" = 1";
		sql += "            GROUP BY L.\"RecordDate058\"";
		sql += "                    ,L.\"CustNo\"";
		sql += "                    ,L.\"FacmNo\"";
		sql += "                    ,L.\"Amount058\"";
		sql += "                    ,L.\"AcDate\"";
		sql += "                    ,L.\"TitaTlrNo\"";
		sql += "                    ,L.\"TitaTxtNo\") L";
		sql += "      LEFT JOIN \"MonthlyFacBal\" M ON M.\"YearMonth\" = :yymm";
		sql += "                                   AND M.\"CustNo\"    =  L.\"CustNo\"";
		sql += "                                   AND M.\"FacmNo\"    =  L.\"FacmNo\"";
		sql += "      LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = L.\"CustNo\"";
		sql += "      LEFT JOIN \"CdCity\" Ci ON Ci.\"CityCode\" = M.\"CityCode\"";
		sql += "      LEFT JOIN \"CdEmp\"  E ON E.\"EmployeeNo\" = M.\"LegalPsn\"";

		logger.info("sql=" + sql);
 
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", iYYMM);

		return this.convertToMap(query.getResultList());
	}

}