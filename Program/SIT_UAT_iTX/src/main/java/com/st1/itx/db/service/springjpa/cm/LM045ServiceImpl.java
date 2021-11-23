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
public class LM045ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;
	
	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		int iENTDY = parse.stringToInteger(titaVo.get("ENTDY")) + 19110000;
		int iYEAR = iENTDY / 10000;
		int iLYEAR = iYEAR - 1;
		int iSYYMM = iLYEAR * 100 + 12;

		int entYearMonth = iENTDY / 100;

		this.info("lM045.findAll SYYMM=" + iSYYMM + ",LYEAR=" + iLYEAR);

		String sql = "SELECT NVL(E.\"Fullname\", d.\"AccCollPsn\") F0 "; // 串不到姓名時顯示員編
		sql += "            ,D.\"YearMonth\" F1 ";
		sql += "            ,NVL(SUM(M.\"BadBal\"), 0) \"BadBal\""; // 超過清償三個月
		sql += "            ,NVL(SUM(M.\"OverBal\"), 0) \"OverBal\""; // 催收款
		sql += "            ,NVL(SUM(M.\"OverBal\" + M.\"BadBal\"), 0) \"TotBal\""; // 催收餘額
		sql += "      FROM (SELECT C.\"CityCode\"";
		sql += "                  ,C.\"AccCollPsn\"";
		sql += "                  ,W.\"YearMonth\"";
		sql += "            FROM (SELECT C.\"CityCode\"";
		sql += "                        ,C.\"AccCollPsn\"";
		sql += "                        ,ROW_NUMBER()";
		sql += "                  OVER (PARTITION BY C.\"AccCollPsn\"";
		sql += "                  ORDER BY C.\"CityCode\") \"SEQ\"";
		sql += "                  FROM \"CdCity\" C) C";
		sql += "            CROSS JOIN (SELECT W.\"Year\" * 100 + W.\"Month\" \"YearMonth\"";
		sql += "                        FROM  \"CdWorkMonth\" W";
		sql += "                        WHERE (W.\"Year\"   = :yyyy";
		sql += "                           AND W.\"Month\" <= 12)";
		sql += "                           OR (W.\"Year\"   = :lyyyy";
		sql += "                           AND W.\"Month\"  = 12)) W";
		sql += "            WHERE C.\"SEQ\" = 1) D";
		sql += "      LEFT JOIN (SELECT F.\"AccCollPsn\"";
		sql += "                       ,M.\"YearMonth\"";
		sql += "                       ,DECODE(M.\"AcctCode\"";
		sql += "                              ,'990'";
		sql += "                              ,M.\"LoanBalance\"";
		sql += "                              ,0) \"OverBal\"";
		sql += "                       ,CASE WHEN F.\"Status\" = '0'";
		sql += "                              AND F.\"OvduTerm\" >= 3";
		sql += "                             THEN M.\"LoanBalance\"";
		sql += "                        ELSE 0 END \"BadBal\"";
		sql += "                 FROM \"MonthlyLoanBal\" M";
		sql += "                 LEFT JOIN \"MonthlyFacBal\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "                                              AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                              AND F.\"YearMonth\" = M.\"YearMonth\"";
		sql += "                 WHERE M.\"YearMonth\" > :syymm";
		sql += "                  AND F.\"Status\" IN (0, 2, 6)) M";
		sql += "      ON M.\"AccCollPsn\" = D.\"AccCollPsn\"";
		sql += "     AND M.\"YearMonth\"  = D.\"YearMonth\"";
		sql += "     LEFT JOIN \"CdEmp\" E ON E.\"EmployeeNo\" = D.\"AccCollPsn\"";
		sql += "     WHERE NVL(d.\"YearMonth\", 999999) <= :entYearMonth ";
		sql += "     GROUP BY D.\"CityCode\"";
		sql += "             ,D.\"AccCollPsn\"";
		sql += "             ,E.\"Fullname\"";
		sql += "             ,D.\"YearMonth\"";
		sql += "     ORDER BY D.\"CityCode\", D.\"YearMonth\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("lyyyy", iLYEAR);
		query.setParameter("yyyy", iYEAR);
		query.setParameter("syymm", iSYYMM);
		query.setParameter("entYearMonth", entYearMonth);
		return this.convertToMap(query);
	}

}