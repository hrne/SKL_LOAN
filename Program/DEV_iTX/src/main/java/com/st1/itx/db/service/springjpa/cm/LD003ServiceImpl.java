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
/* 逾期放款明細 */
public class LD003ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("LD003.findAll ");

//		目前AcSubBookCode只有分一般帳戶(00A)和利變A(201)?
		String sql = " ";
		sql += " SELECT S.\"ColCount\"";
		sql += "       ,SUM(S.\"Counts\")      AS \"Counts\"";
		sql += "       ,SUM(S.\"LoanBalance\") AS \"Amt\"";
		sql += " FROM (  SELECT CASE";
		sql += "                  WHEN D.\"AcctCode\" = '310' AND A.\"AcSubBookCode\" = '00A' THEN 1";
		sql += "                  WHEN D.\"AcctCode\" = '310' AND A.\"AcSubBookCode\" = '201' THEN 2";
		sql += "                  WHEN D.\"AcctCode\" = '320' AND A.\"AcSubBookCode\" = '00A' THEN 5";
		sql += "                  WHEN D.\"AcctCode\" = '320' AND A.\"AcSubBookCode\" = '201' THEN 6";
		sql += "                  WHEN D.\"AcctCode\" = '330' AND A.\"AcSubBookCode\" = '00A' THEN 9";
		sql += "                  WHEN D.\"AcctCode\" = '330' AND A.\"AcSubBookCode\" = '201' THEN 10";
		sql += "                  WHEN D.\"AcctCode\" = '340' AND A.\"AcSubBookCode\" = '00A' THEN 13";
		sql += "                  WHEN D.\"AcctCode\" = '340' AND A.\"AcSubBookCode\" = '201' THEN 14";
		sql += "                ELSE 99 END AS \"ColCount\"";
		sql += "               ,A.\"Count\" AS \"Counts\"";
		sql += "               ,D.\"AcctCode\"";
		sql += "               ,A.\"AcSubBookCode\"";
		sql += "               ,D.\"LoanBalance\"";
		sql += "         FROM ( SELECT D.\"AcctCode\"";
		sql += "                      ,D.\"FacAcctCode\"";
		sql += "                      ,D.\"CustNo\"";
		sql += "                      ,D.\"FacmNo\"";
		sql += "                      ,SUM(D.\"LoanBalance\") AS \"LoanBalance\"";
		sql += "                FROM \"DailyLoanBal\" D ";
		sql += "                WHERE D.\"LoanBalance\" > 0";
		sql += "                  AND D.\"LatestFlag\" = 1";
		sql += "                GROUP BY D.\"AcctCode\"";
		sql += "                        ,D.\"FacAcctCode\"";
		sql += "                        ,D.\"CustNo\"";
		sql += "                        ,D.\"FacmNo\"";
		sql += "              ) D";
		sql += "         LEFT JOIN ( SELECT A0.\"AcctCode\"";
		sql += "                           ,A0.\"CustNo\"";
		sql += "                           ,A0.\"FacmNo\"";
		sql += "                           ,A0.\"AcSubBookCode\"";
		sql += "                           ,COUNT(*) AS \"Count\"";
		sql += "                     FROM \"DailyLoanBal\" D0";
		sql += "                     LEFT JOIN \"AcReceivable\" A0 ON A0.\"AcctCode\" = D0.\"AcctCode\"";
		sql += "                                                AND A0.\"CustNo\"   = D0.\"CustNo\"";
		sql += "                                                AND A0.\"FacmNo\"   = D0.\"FacmNo\"";
		sql += "                                                AND SUBSTR(A0.\"RvNo\",0,3) = LPAD(D0.\"BormNo\",3,'0')";
		sql += "                     WHERE D0.\"LoanBalance\" > 0";
		sql += "                       AND D0.\"LatestFlag\" = 1";
		sql += "                       AND A0.\"AcctCode\" IN ('310','320','330','340')";
		sql += "                     GROUP BY  A0.\"AcctCode\"";
		sql += "                              ,A0.\"CustNo\"";
		sql += "                              ,A0.\"FacmNo\"";
		sql += "                              ,A0.\"AcSubBookCode\"";
		sql += "                   ) A ON A.\"AcctCode\" = D.\"AcctCode\"";
		sql += "                      AND A.\"CustNo\"   = D.\"CustNo\"";
		sql += "                      AND A.\"FacmNo\"   = D.\"FacmNo\"";
		sql += "         WHERE D.\"LoanBalance\" > 0";
		for (int i = 1; i <= 16; i++) {
			sql += "        UNION ALL";
			sql += "        SELECT " + i + ",0,'0','0',0 FROM DUAL";
		}
		sql += ") S ";
		sql += " WHERE S.\"ColCount\" <> 99";
		sql += " GROUP BY S.\"ColCount\"";
		sql += " ORDER BY S.\"ColCount\"";

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}
}