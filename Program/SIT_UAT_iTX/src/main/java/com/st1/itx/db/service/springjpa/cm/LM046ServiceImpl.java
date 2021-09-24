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
public class LM046ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		String entYearMonth = String.valueOf((Integer.valueOf(titaVo.get("ENTDY").toString()) + 19110000) / 100);

		this.info("lM046.findAll entYearMonth =" + entYearMonth);

		String sql = "";
		sql += " WITH AllMonthOrSeasons AS ( ";
		// This Season
	   sql += "    SELECT TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(:entYearMonth, 'YYYYMM'), -LEVEL + 1), 'YYYYMM')) \"YearMonth\" ";
	   sql += "      FROM DUAL ";
	   sql += "     WHERE LEVEL <= CASE WHEN SUBSTR(:entYearMonth, 5, 2) IN ('03','06','09','12') ";
	   sql += "                         THEN 1 ";
	   sql += "                         WHEN SUBSTR(:entYearMonth, 5, 2) IN ('02','05','08','11') ";
	   sql += "                         THEN 2 ";
	   sql += "                         WHEN SUBSTR(:entYearMonth, 5, 2) IN ('01','04','07','10') ";
	   sql += "                         THEN 1 ";
	   sql += "                    ELSE 0 END ";
	   sql += "    CONNECT BY LEVEL <= 2 "; // 只有 1Q, 2M, 1M 三種狀況
	   sql += " UNION ALL ";
		// Every Season before this, in this year and last year
	   sql += "    SELECT UNIQUE \"Fn_GetWorkSeason\"(TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(:entYearMonth, 'YYYYMM'), -LEVEL * 3), 'YYYYMM')), 3) \"YearMonth\" ";
	   sql += "    FROM DUAL ";
	   sql += "    WHERE TO_CHAR(ADD_MONTHS(TO_DATE(:entYearMonth, 'YYYYMM'), -LEVEL * 3), 'YYYYMM') >= (SUBSTR(:entYearMonth, 1, 4)-1 || '01') ";
	   sql += "    CONNECT BY LEVEL <= 7 "; // 最多只有七個Q需要出
	   sql += " UNION ALL ";
		// Every 4th Season in the nearest 6 years before last year
	   sql += "    SELECT TO_NUMBER((SUBSTR(:entYearMonth, 1, 4) - 1 - LEVEL) || '12') \"YearMonth\" ";
	   sql += "    FROM DUAL ";
	   sql += "    CONNECT BY LEVEL <= 6 "; // 最多只有六年
	   sql += " ) ";
	   sql += " , TotalData AS ( ";
	   sql += " SELECT CASE WHEN SUBSTR(mons.\"YearMonth\", 5, 2) NOT IN ('03','06','09','12') ";
	   sql += "             THEN SUBSTR(mons.\"YearMonth\", 1, 4) - 1911 || '年' || SUBSTR(mons.\"YearMonth\", 5, 2) ";
	   sql += "        ELSE SUBSTR(mons.\"YearMonth\", 1, 4) - 1911 || '年Q' || SUBSTR(\"Fn_GetWorkSeason\"(mons.\"YearMonth\", 2), 5, 1) END AS \"YearMonthOutput\" ";
	   sql += "       ,COUNT(*) AS \"LoanCnt\" ";
	   sql += "       ,SUM(MFB.\"PrinBalance\") AS \"LoanBal\" ";
	   sql += "       ,COUNT(CASE WHEN MFB.\"Status\" = '0' ";
	   sql += "                    AND MFB.\"OvduTerm\" >= 3 ";
	   sql += "                    AND MFB.\"AcctCode\" != '990' ";
	   sql += "                   THEN 1 END) AS \"OvduCnt\" ";
	   sql += "       ,SUM(CASE WHEN MFB.\"Status\" = '0' ";
	   sql += "                  AND MFB.\"OvduTerm\" >= 3 ";
	   sql += "                  AND MFB.\"AcctCode\" != '990' ";
	   sql += "                 THEN MFB.\"PrinBalance\" ";
	   sql += "            ELSE 0 END) AS \"OvduBal\" ";
	   sql += "       ,COUNT(CASE WHEN MFB.\"Status\" IN ('2','6') ";
	   sql += "                    AND MFB.\"OvduTerm\" >= 6 ";
	   sql += "                   THEN 1 ";
	   sql += "                   WHEN MFB.\"AcctCode\" = '990' ";
	   sql += "                   THEN 1 END) AS \"BadDebtCnt\" ";
	   sql += "       ,SUM(CASE WHEN MFB.\"Status\" IN ('2','6') ";
	   sql += "                  AND MFB.\"OvduTerm\" >= 6 ";
	   sql += "                 THEN MFB.\"PrinBalance\" ";
	   sql += "                 WHEN MFB.\"AcctCode\" = '990' ";
	   sql += "                 THEN MFB.\"PrinBalance\" ";
	   sql += "            ELSE 0 END) AS \"BadDebtBal\" ";
	   sql += "       ,DECODE(CM.\"EntCode\", 1, 1, 0) \"EntType\" ";
	   sql += "       ,mons.\"YearMonth\" \"YearMonth\" ";
	   sql += " FROM ( SELECT AllMonthOrSeasons.* ";
	   sql += "              ,ROW_NUMBER() OVER (ORDER BY \"YearMonth\" DESC) seq ";
	   sql += "        FROM AllMonthOrSeasons ) mons ";
	   sql += " LEFT JOIN \"MonthlyFacBal\" MFB ON MFB.\"YearMonth\" = mons.\"YearMonth\" ";
	   sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = MFB.\"CustNo\" ";
	   sql += " WHERE mons.seq <= 11 ";
	   sql += "   AND (MFB.\"Status\" IN ('0','2','6') ";
	   sql += "        OR ";
	   sql += "        MFB.\"AcctCode\" = '990') ";
	   sql += "   AND MFB.\"PrinBalance\" > 0 ";
	   sql += " GROUP BY mons.\"YearMonth\" ";
	   sql += "         ,DECODE(CM.\"EntCode\", 1, 1, 0) ";
	   sql += " ORDER BY mons.\"YearMonth\" DESC ";
	   sql += " ) ";
	   sql += " SELECT Ent.\"YearMonthOutput\" F0 ";
	   sql += "       ,Ent.\"LoanCnt\" F1 ";
	   sql += "       ,Ent.\"LoanBal\" F2 ";
	   sql += "       ,Ent.\"OvduCnt\" F3 ";
	   sql += "       ,Ent.\"OvduBal\" F4 ";
	   sql += "       ,Ent.\"BadDebtCnt\" F5 ";
	   sql += "       ,Ent.\"BadDebtBal\" F6 ";
	   sql += "       ,NVL(Nat.\"LoanCnt\", 0) F7 ";
	   sql += "       ,NVL(Nat.\"LoanBal\", 0) F8 ";
	   sql += "       ,NVL(Nat.\"OvduCnt\", 0) F9 ";
	   sql += "       ,NVL(Nat.\"OvduBal\", 0) F10 ";
	   sql += "       ,NVL(Nat.\"BadDebtCnt\", 0) F11 ";
	   sql += "       ,NVL(Nat.\"BadDebtBal\", 0) F12 ";
	   sql += " FROM TotalData Ent ";
	   sql += " LEFT JOIN TotalData Nat ON Nat.\"YearMonth\" = Ent.\"YearMonth\" ";
	   sql += "                        AND Nat.\"EntType\" = 0 ";
	   sql += " WHERE Ent.\"EntType\" = 1 ";
	   sql += " ORDER BY Ent.\"YearMonth\" DESC ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("entYearMonth", entYearMonth);

		return this.convertToMap(query);
	}

}